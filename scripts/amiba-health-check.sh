#!/bin/bash
# 容器健康与宿主机资源检测
#
# 背景：2026-07-28 amiba-b 因 WxPayConfiguration NPE 陷入崩溃重启循环，
# 每 7 分钟重启一次，累计 16.8 万次，耗尽宿主机 netfilter/BPF 资源，
# 导致整机 TCP 网络栈瘫痪（ICMP 可通但所有端口无响应），持续 5 天无人发现。
#
# 本脚本把云监控采集不到的指标写入 syslog，便于在阿里云云监控控制台
# 基于日志关键字配置告警规则。
#
# 告警关键字（控制台配规则时使用）：
#   AMIBA_ALERT_CONTAINER_RESTART  容器重启次数超阈值
#   AMIBA_ALERT_CONTAINER_DOWN     容器非运行状态
#   AMIBA_ALERT_DISK               磁盘使用率超阈值
#   AMIBA_ALERT_NFT_BLOAT          netfilter 规则数异常膨胀
#
# 用法：crontab 每 5 分钟执行一次

set -uo pipefail

TAG="amiba-health"
COMPOSE_FILE="/opt/amiba/docker-compose.server.yml"
STATE_DIR="/var/lib/amiba-health"
mkdir -p "$STATE_DIR"

# 阈值
RESTART_THRESHOLD=5      # 单个容器 5 分钟内新增重启次数
DISK_THRESHOLD=85        # 磁盘使用率百分比
NFT_THRESHOLD=500        # iptables 规则总数

alert() {
    # 同时写 syslog（供云监控日志告警）和 stderr
    logger -t "$TAG" -p daemon.err "$1"
    echo "[ALERT] $1" >&2
}

info() {
    logger -t "$TAG" -p daemon.info "$1"
}

# --- 1. 容器重启次数与运行状态 ---
check_containers() {
    local names
    names=$(docker ps -a --format '{{.Names}}' 2>/dev/null | grep '^amiba-' || true)
    [ -z "$names" ] && { alert "AMIBA_ALERT_CONTAINER_DOWN 未发现任何 amiba-* 容器，Docker 可能异常"; return; }

    while read -r name; do
        [ -z "$name" ] && continue
        local count status prev delta
        count=$(docker inspect -f '{{.RestartCount}}' "$name" 2>/dev/null || echo 0)
        status=$(docker inspect -f '{{.State.Status}}' "$name" 2>/dev/null || echo unknown)

        # 与上次采样比较，检测"正在持续重启"
        local f="$STATE_DIR/${name}.restart"
        prev=$(cat "$f" 2>/dev/null || echo "$count")
        echo "$count" > "$f"
        delta=$(( count - prev ))

        if [ "$delta" -ge "$RESTART_THRESHOLD" ]; then
            alert "AMIBA_ALERT_CONTAINER_RESTART $name 在本采样周期内重启 $delta 次（累计 $count），疑似崩溃重启循环"
        fi

        if [ "$status" != "running" ]; then
            alert "AMIBA_ALERT_CONTAINER_DOWN $name 状态为 $status（累计重启 $count）"
        fi
    done <<< "$names"
}

# --- 2. 磁盘使用率 ---
check_disk() {
    local use
    use=$(df --output=pcent / 2>/dev/null | tail -1 | tr -dc '0-9')
    [ -z "$use" ] && return
    if [ "$use" -ge "$DISK_THRESHOLD" ]; then
        alert "AMIBA_ALERT_DISK 根分区使用率 ${use}%，超过阈值 ${DISK_THRESHOLD}%"
    fi
}

# --- 3. netfilter 规则膨胀（本次故障的直接死因）---
check_nft() {
    local n
    n=$(( $(iptables -S 2>/dev/null | wc -l) + $(iptables -t nat -S 2>/dev/null | wc -l) ))
    if [ "$n" -ge "$NFT_THRESHOLD" ]; then
        alert "AMIBA_ALERT_NFT_BLOAT iptables 规则数 $n，超过阈值 $NFT_THRESHOLD，可能有容器在反复重建网络"
    fi
}

check_containers
check_disk
check_nft
info "健康检查完成"
