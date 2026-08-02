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
| `amiba-web` | 8081→8083 | `jars/web.jar` | `c-web` / `web0508` |
| `amiba-b` | 8091 | `jars/b.jar` | `b` |
| `amiba-icars-rest` | 8443 | `jars/icars-rest.jar` | `icars-master/icars-master/icars-rest` |
| `amiba-xxl-job-admin` | 8080 | `jars/xxl-job-admin.jar` | `xxl-job-master` |

> **已移除：`amiba-jeesite`（2026-08-02）**
> `jars/jeesite.jar` 内实际是 guns 应用（`com.stylefeng.guns.GunsApplication`），不是 jeesite；
> jar 内硬编码 `spring.profiles.active=local` 指向 `127.0.0.1`，在容器网络中永远连不上数据库。
> 该容器自 2026-04-05 创建起从未成功启动过一次（`jeesite` 库 31 张表全部 0 行、从未写入），
> 却因 `restart: always` 持续崩溃重启累计 16.8 万次，耗尽宿主机 netfilter/BPF 资源，
> 导致 7/28–8/2 整机 TCP 网络栈瘫痪。已从 compose 移除；`jeesite` 数据库与 jar 文件保留。
> 若日后需要真正的 jeesite，须先解决 parent pom（`jeesite4-src`）缺失问题重新打包。

### 访问地址
- **wx-admin 后台**: `http://114.215.211.119:8078/wx-admin/`（amiba-icars-admin）

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

### ⛔ 部署铁律（最高优先级）
- **严禁修改代码后直接推送远端部署！**必须等待用户明确确认后才能执行任何部署操作（包括 scp、deploy 脚本、docker 重启等）
- 修改代码 → 展示改动 → **等用户说"部署"/"deploy"** → 才执行部署
- 违反此规则 = 严重事故

### 编译打包（icars-admin）

**必须使用 Java 8 编译**，本机默认 Maven 使用 Java 25（Homebrew openjdk），会导致 `Invalid CEN header` 错误：
```bash
# 正确方式：指定 JAVA_HOME 为 Java 8
JAVA_HOME=/Library/Java/JavaVirtualMachines/temurin-8.jdk/Contents/Home mvn package -pl icars-admin -am -DskipTests -q

# 错误现象：mvn -version 显示 Java 25，编译报错
# error reading aspectjweaver-1.8.13.jar; Invalid CEN header (invalid zip64 extra data field size)
# 这不是 jar 损坏，是 Java 25 的 zip 库对旧格式 jar 校验更严格
```

**静态资源缓存**：修改 JS/CSS 后需更新 HTML 中的版本号参数（如 `?v=20260516`），否则浏览器会使用缓存的旧文件。

**热更新 JS 文件**：JS 文件也可以用热更新模式部署（和 HTML 一样），路径相对 webapp 目录：
```bash
./deploy-icars-admin.sh "WEB-INF/view/biz/accid/accid.html" "static/modular/biz/accd/accid.js"
```

### 注意事项
- 容器启动较慢（约 2-3 分钟），耐心等待
- 每次部署前会自动备份旧 jar（文件名加时间戳）
- icars-admin 的 HTML 模板路径在 jar 内：`BOOT-INF/classes/WEB-INF/view/`
- **部署时不要带 `amiba-jeesite`**（已于 2026-08-02 移除，原因见上方容器列表下的说明）。
  用 `docker-compose -f docker-compose.server.yml up -d` 时会自动跳过，无需额外操作
- 容器 `restart` 策略：崩溃即无限重启会耗尽宿主机 netfilter/BPF 资源拖垮整机
  （2026-07-28 事故根因）。新增服务一律用 `on-failure:5`，不要用 `always`
