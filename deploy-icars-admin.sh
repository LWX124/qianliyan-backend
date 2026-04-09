#!/bin/bash
# ============================================================
# icars-admin 一键部署脚本
# 用法：./deploy-icars-admin.sh [html文件相对路径...]
#
# 示例：
#   ./deploy-icars-admin.sh                          # 更新整个 jar（需先打包）
#   ./deploy-icars-admin.sh WEB-INF/view/biz/accid/accid.html  # 只更新单个 html
# ============================================================

set -e

SERVER="root@114.215.211.119"
REMOTE_JAR="/opt/amiba/jars/icars-admin.jar"
CONTAINER="amiba-icars-admin"
SERVICE="icars-admin"           # docker-compose 中的服务名
COMPOSE_FILE="/opt/amiba/docker-compose.server.yml"
LOCAL_PROJECT="$(cd "$(dirname "$0")/icars-master/icars-master/icars-admin" && pwd)"
WEBAPP_DIR="$LOCAL_PROJECT/src/main/webapp"
LOCAL_JAR="$LOCAL_PROJECT/target/icars-admin-1.0.0.jar"

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
NC='\033[0m'

echo "============================================="
echo "  icars-admin 部署工具"
echo "  服务器: $SERVER"
echo "  容器:   $CONTAINER"
echo "============================================="

# ---- 模式一：只更新指定 HTML 文件（热更新，不重新打包）----
if [ "$#" -gt 0 ]; then
    echo -e "${YELLOW}[模式] 热更新 HTML 文件（直接修改 jar，不重新打包）${NC}"

    # 收集要更新的文件
    FILES_JSON="["
    FIRST=1
    for rel_path in "$@"; do
        local_file="$WEBAPP_DIR/$rel_path"
        entry_name="BOOT-INF/classes/$rel_path"

        if [ ! -f "$local_file" ]; then
            echo -e "${RED}[错误] 文件不存在: $local_file${NC}"
            exit 1
        fi

        # 上传到服务器临时目录
        tmp_remote="/tmp/deploy_$(echo "$rel_path" | tr '/' '_')"
        echo "  上传: $rel_path"
        scp "$local_file" "$SERVER:$tmp_remote"

        if [ "$FIRST" -eq 1 ]; then
            FILES_JSON="${FILES_JSON}{\"local\":\"${tmp_remote}\",\"entry\":\"${entry_name}\"}"
            FIRST=0
        else
            FILES_JSON="${FILES_JSON},{\"local\":\"${tmp_remote}\",\"entry\":\"${entry_name}\"}"
        fi
    done
    FILES_JSON="${FILES_JSON}]"

    # 在服务器上用 python3 更新 jar 内容
    echo -e "${YELLOW}[服务器] 更新 jar 内文件...${NC}"
    ssh "$SERVER" "python3 << 'PYEOF'
import zipfile, shutil, os, json, time

jar_path = '${REMOTE_JAR}'
files = json.loads('${FILES_JSON}')

# 备份
backup = jar_path + '.bak.' + time.strftime('%Y%m%d%H%M%S')
shutil.copy2(jar_path, backup)
print('备份:', backup)

# 更新
tmp_jar = jar_path + '.tmp'
with zipfile.ZipFile(jar_path, 'r') as zin:
    with zipfile.ZipFile(tmp_jar, 'w', zipfile.ZIP_DEFLATED) as zout:
        for item in zin.infolist():
            updated = False
            for f in files:
                if item.filename == f['entry']:
                    with open(f['local'], 'rb') as fh:
                        zout.writestr(item, fh.read())
                    print('已更新:', item.filename)
                    updated = True
                    break
            if not updated:
                zout.writestr(item, zin.read(item.filename))

os.replace(tmp_jar, jar_path)
print('jar 更新完成')
PYEOF"

# ---- 模式二：上传完整 jar（需先本地打包）----
else
    echo -e "${YELLOW}[模式] 上传完整 jar 包${NC}"

    if [ ! -f "$LOCAL_JAR" ]; then
        echo -e "${RED}[错误] 本地 jar 不存在: $LOCAL_JAR${NC}"
        echo -e "${YELLOW}请先在 IDEA 中 Build → Build Artifacts → icars-admin:jar${NC}"
        exit 1
    fi

    echo "  上传 jar: $(du -sh "$LOCAL_JAR" | cut -f1)"

    # 服务器备份旧 jar
    ssh "$SERVER" "cp ${REMOTE_JAR} ${REMOTE_JAR}.bak.\$(date +%Y%m%d%H%M%S)"

    # 上传新 jar
    scp "$LOCAL_JAR" "$SERVER:$REMOTE_JAR"
    echo -e "${GREEN}  jar 上传完成${NC}"
fi

# ---- 重建镜像并重启容器（必须 rmi + build，否则旧镜像中的 jar 不会更新）----
echo -e "${YELLOW}[服务器] 重建镜像并重启容器 $CONTAINER ...${NC}"
ssh "$SERVER" "
    docker stop $CONTAINER 2>/dev/null || true
    docker rm $CONTAINER 2>/dev/null || true
    docker rmi $CONTAINER 2>/dev/null || true
    cd /opt/amiba && docker-compose -f $COMPOSE_FILE build --no-cache $SERVICE && docker-compose -f $COMPOSE_FILE up -d --no-deps $SERVICE
"

# ---- 等待启动 ----
echo -e "${YELLOW}[服务器] 等待服务启动（最长3分钟）...${NC}"
for i in $(seq 1 36); do
    sleep 5
    STATUS=$(ssh "$SERVER" "curl -s -o /dev/null -w '%{http_code}' http://localhost:8078/wx-admin/ 2>/dev/null || echo '000'")
    if [ "$STATUS" = "302" ] || [ "$STATUS" = "200" ]; then
        echo -e "${GREEN}[成功] 服务已启动！HTTP $STATUS${NC}"
        echo -e "${GREEN}访问地址: http://114.215.211.119:8078/wx-admin/${NC}"
        exit 0
    fi
    echo "  等待中... ($((i*5))s) HTTP=$STATUS"
done

echo -e "${RED}[超时] 服务启动超时，请检查日志：${NC}"
echo "  ssh $SERVER 'docker logs $CONTAINER --tail 30'"
exit 1
