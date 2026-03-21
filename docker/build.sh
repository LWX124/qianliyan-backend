#!/bin/bash

set -e

BASE_DIR="$(cd "$(dirname "$0")/.." && pwd)"
DOCKER_DIR="$BASE_DIR/docker"
JAR_DIR="$DOCKER_DIR/jars"

export JAVA_HOME="/Library/Java/JavaVirtualMachines/temurin-8.jdk/Contents/Home"
export PATH="$JAVA_HOME/bin:/opt/homebrew/bin:$PATH"

RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m'

mkdir -p "$JAR_DIR"

build_project() {
  local name="$1"
  local dir="$2"
  local jar_src="$3"
  local jar_dest="$JAR_DIR/$4"

  echo -e "${GREEN}[构建]${NC} $name ..."
  cd "$dir"
  if mvn clean package -DskipTests -q; then
    cp "$jar_src" "$jar_dest"
    echo -e "${GREEN}[完成]${NC} $name -> jars/$4"
  else
    echo -e "${RED}[失败]${NC} $name"
    return 1
  fi
}

echo "========== 开始构建所有服务 =========="

# 1. xxl-job (多模块，从父 pom 构建)
echo -e "${GREEN}[构建]${NC} xxl-job-master (多模块) ..."
cd "$BASE_DIR/xxl-job-master"
mvn clean package -DskipTests -q
cp "$BASE_DIR/xxl-job-master/xxl-job-admin/target/xxl-job-admin-2.4.1-SNAPSHOT.jar" "$JAR_DIR/xxl-job-admin.jar"
echo -e "${GREEN}[完成]${NC} xxl-job-admin"

# 2. web0508
build_project "web0508" \
  "$BASE_DIR/web0508/web" \
  "$BASE_DIR/web0508/web/target/web-v1.1.2.jar" \
  "web.jar"

# 3. icars-master (多模块，从父 pom 构建)
echo -e "${GREEN}[构建]${NC} icars-master (多模块) ..."
cd "$BASE_DIR/icars-master/icars-master"
mvn clean package -DskipTests -q
cp "$BASE_DIR/icars-master/icars-master/icars-admin/target/icars-admin-1.0.0.jar" "$JAR_DIR/icars-admin.jar"
cp "$BASE_DIR/icars-master/icars-master/icars-rest/target/icars-rest-0.0.1.jar" "$JAR_DIR/icars-rest.jar"
echo -e "${GREEN}[完成]${NC} icars-admin + icars-rest"

# 4. b
build_project "b" \
  "$BASE_DIR/b" \
  "$BASE_DIR/b/target/b-0.0.1-SNAPSHOT.jar" \
  "b.jar"

# 5. jeesite_new
build_project "jeesite_new" \
  "$BASE_DIR/jeesite_new" \
  "$BASE_DIR/jeesite_new/target/cjMange.jar" \
  "jeesite.jar"

echo ""
echo "========== 构建完成 =========="
echo "JAR 文件已输出到: $JAR_DIR/"
ls -lh "$JAR_DIR/"
echo ""
echo "下一步: cd $DOCKER_DIR && docker-compose up -d"
