## gstack

Use the `/browse` skill from gstack for all web browsing. Never use `mcp__claude-in-chrome__*` tools.

Available gstack skills: `/office-hours`, `/plan-ceo-review`, `/plan-eng-review`, `/plan-design-review`, `/design-consultation`, `/review`, `/ship`, `/land-and-deploy`, `/canary`, `/benchmark`, `/browse`, `/qa`, `/qa-only`, `/design-review`, `/setup-browser-cookies`, `/setup-deploy`, `/retro`, `/investigate`, `/document-release`, `/codex`, `/cso`, `/autoplan`, `/careful`, `/freeze`, `/guard`, `/unfreeze`, `/gstack-upgrade`.

## 服务器部署信息

### 服务器
- **地址**: `114.215.211.119`（阿里云）
- **登录**: `ssh root@114.215.211.119`
- **部署目录**: `/opt/amiba/`
- **Compose 文件**: `/opt/amiba/docker-compose.server.yml`
- **jar 目录**: `/opt/amiba/jars/`

### 容器列表

| 容器名 | 端口 | jar 文件 | 本地项目 |
|--------|------|----------|----------|
| `amiba-icars-admin` | 8078 | `jars/icars-admin.jar` | `icars-master/icars-master/icars-admin` |
| `amiba-jeesite` | 9002 | `jars/jeesite.jar` | `jeesite_new` |
| `amiba-web` | 8081→8083 | `jars/web.jar` | `c-web` / `web0508` |
| `amiba-b` | 8091 | `jars/b.jar` | `b` |
| `amiba-icars-rest` | 8443 | `jars/icars-rest.jar` | `icars-master/icars-master/icars-rest` |
| `amiba-xxl-job-admin` | 8080 | `jars/xxl-job-admin.jar` | `xxl-job-master` |

### 访问地址
- **wx-admin 后台**: `http://114.215.211.119:8078/wx-admin/`（amiba-icars-admin）
- **jeesite 后台**: `http://114.215.211.119:9002/`

### 部署方式

**方式一：只更新 HTML/模板文件（热更新，无需重新打包，推荐）**
```bash
# 在 backend 目录下执行，指定相对 webapp 的路径
./deploy-icars-admin.sh WEB-INF/view/biz/accid/accid.html
# 多文件同时更新
./deploy-icars-admin.sh WEB-INF/view/biz/accid/accid.html WEB-INF/view/biz/accid/accid_push.html
```

**方式二：上传完整 jar（需先在 IDEA 打包）**
```bash
# 先在 IDEA: Build → Build Artifacts → icars-admin:jar
./deploy-icars-admin.sh
```

**手动操作（紧急情况）**
```bash
# 1. 上传 jar
scp target/icars-admin-1.0.0.jar root@114.215.211.119:/opt/amiba/jars/icars-admin.jar

# 2. 重建镜像并重启容器（必须 rmi + build，否则旧镜像中的 jar 不会更新）
ssh root@114.215.211.119 "docker stop amiba-icars-admin && docker rm amiba-icars-admin && docker rmi amiba-icars-admin 2>/dev/null; cd /opt/amiba && docker-compose -f docker-compose.server.yml build --no-cache icars-admin && docker-compose -f docker-compose.server.yml up -d --no-deps icars-admin"

# 3. 查看启动日志
ssh root@114.215.211.119 "docker logs -f amiba-icars-admin"
```

### 注意事项
- 容器启动较慢（约 2-3 分钟），耐心等待
- 每次部署前会自动备份旧 jar（文件名加时间戳）
- jeesite 的 parent pom（`jeesite4-src`）本地不存在，无法用 mvn 打包，需用 IDEA 打包
- icars-admin 的 HTML 模板路径在 jar 内：`BOOT-INF/classes/WEB-INF/view/`
- jeesite 的 HTML 模板路径：`BOOT-INF/classes/views/modules/`
