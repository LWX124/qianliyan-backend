#!/bin/bash

export JAVA_HOME="/Library/Java/JavaVirtualMachines/temurin-8.jdk/Contents/Home"
export PATH="$JAVA_HOME/bin:/opt/homebrew/bin:$PATH"

BASE_DIR="$(cd "$(dirname "$0")" && pwd)"
LOG_DIR="$BASE_DIR/logs"
mkdir -p "$LOG_DIR"

# 服务列表: 名称|路径|端口
SERVICES=(
  "xxl-job-admin|$BASE_DIR/xxl-job-master/xxl-job-admin|8080"
  "web0508|$BASE_DIR/web0508/web|8081"
  "icars-admin|$BASE_DIR/icars-master/icars-master/icars-admin|8082"
  "c-web|$BASE_DIR/c-web/web|8083"
  "web-tag|$BASE_DIR/web-tag/v1.0.0|8084"
  "xxl-job-executor|$BASE_DIR/xxl-job-master/xxl-job-executor-samples/xxl-job-executor-sample-springboot|8085"
  "b|$BASE_DIR/b|8091"
  "icars-rest|$BASE_DIR/icars-master/icars-master/icars-rest|8443"
  "jeesite_new|$BASE_DIR/jeesite_new|9002"
)

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

check_port() {
  lsof -i :"$1" -sTCP:LISTEN >/dev/null 2>&1
}

check_deps() {
  echo "========== 检查依赖服务 =========="

  # MySQL
  if mysql -u root -e "SELECT 1" >/dev/null 2>&1; then
    echo -e "  MySQL:    ${GREEN}运行中${NC}"
  else
    echo -e "  MySQL:    ${RED}未启动${NC} — 尝试启动..."
    brew services start mysql 2>/dev/null || mysql.server start 2>/dev/null
    sleep 3
    if mysql -u root -e "SELECT 1" >/dev/null 2>&1; then
      echo -e "  MySQL:    ${GREEN}已启动${NC}"
    else
      echo -e "  MySQL:    ${RED}启动失败，请手动检查${NC}"
    fi
  fi

  # Redis
  if redis-cli ping >/dev/null 2>&1; then
    echo -e "  Redis:    ${GREEN}运行中${NC}"
  else
    echo -e "  Redis:    ${RED}未启动${NC} — 尝试启动..."
    brew services start redis 2>/dev/null || redis-server --daemonize yes 2>/dev/null
    sleep 2
    if redis-cli ping >/dev/null 2>&1; then
      echo -e "  Redis:    ${GREEN}已启动${NC}"
    else
      echo -e "  Redis:    ${RED}启动失败，请手动检查${NC}"
    fi
  fi

  echo ""
}

start_service() {
  local name="$1"
  local path="$2"
  local port="$3"
  local log_file="$LOG_DIR/${name}.log"

  if check_port "$port"; then
    echo -e "  ${YELLOW}[跳过]${NC} $name — 端口 $port 已被占用"
    return
  fi

  if [ ! -d "$path" ]; then
    echo -e "  ${RED}[错误]${NC} $name — 目录不存在: $path"
    return
  fi

  echo -e "  ${GREEN}[启动]${NC} $name (端口 $port) — 日志: logs/${name}.log"
  cd "$path" && nohup mvn spring-boot:run > "$log_file" 2>&1 &
  echo $! > "$LOG_DIR/${name}.pid"
}

stop_all() {
  echo "========== 停止所有服务 =========="
  for entry in "${SERVICES[@]}"; do
    IFS='|' read -r name path port <<< "$entry"
    local pid_file="$LOG_DIR/${name}.pid"
    if [ -f "$pid_file" ]; then
      local pid=$(cat "$pid_file")
      if kill -0 "$pid" 2>/dev/null; then
        echo -e "  ${YELLOW}[停止]${NC} $name (PID: $pid)"
        kill "$pid" 2>/dev/null
        # 同时杀掉 Java 子进程
        pkill -P "$pid" 2>/dev/null
      fi
      rm -f "$pid_file"
    fi
  done
  echo "全部已停止"
}

status_all() {
  echo "========== 服务状态 =========="
  printf "  %-20s %-8s %-10s\n" "服务" "端口" "状态"
  printf "  %-20s %-8s %-10s\n" "----" "----" "----"
  for entry in "${SERVICES[@]}"; do
    IFS='|' read -r name path port <<< "$entry"
    if check_port "$port"; then
      printf "  %-20s %-8s ${GREEN}%-10s${NC}\n" "$name" "$port" "运行中"
    else
      printf "  %-20s %-8s ${RED}%-10s${NC}\n" "$name" "$port" "未运行"
    fi
  done
}

case "${1:-start}" in
  start)
    check_deps
    echo "========== 启动所有服务 =========="
    for entry in "${SERVICES[@]}"; do
      IFS='|' read -r name path port <<< "$entry"
      start_service "$name" "$path" "$port"
    done
    echo ""
    echo "所有服务已在后台启动，等待几秒后可用 '$0 status' 查看状态"
    echo "日志目录: $LOG_DIR/"
    ;;
  stop)
    stop_all
    ;;
  restart)
    stop_all
    sleep 3
    check_deps
    echo "========== 启动所有服务 =========="
    for entry in "${SERVICES[@]}"; do
      IFS='|' read -r name path port <<< "$entry"
      start_service "$name" "$path" "$port"
    done
    ;;
  status)
    status_all
    ;;
  logs)
    if [ -n "$2" ]; then
      tail -f "$LOG_DIR/$2.log"
    else
      echo "用法: $0 logs <服务名>"
      echo "可选: xxl-job-admin web0508 icars-admin c-web web-tag xxl-job-executor b icars-rest jeesite_new"
    fi
    ;;
  *)
    echo "用法: $0 {start|stop|restart|status|logs <服务名>}"
    ;;
esac
