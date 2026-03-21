# 千里眼后端服务 — 部署与接口文档

## 1. 部署架构

### 服务器信息

| 项目 | 值 |
|------|-----|
| 服务器地址 | 114.215.211.119 |
| 操作系统 | Linux x86_64 (Alibaba Cloud) |
| 内存 | 7.3 GB |
| Docker | 24.0.9 |
| docker-compose | v2.24.0 |
| 部署目录 | `/opt/amiba/` |
| SSH 连接 | `ssh -i ~/.ssh/id_ed25519 root@114.215.211.119` |

### 容器清单

系统共 10 个 Docker 容器，分为基础设施层（4 个）和应用服务层（6 个）。

#### 基础设施

| 容器名 | 镜像 | 端口 | 说明 |
|--------|------|------|------|
| amiba-mysql | mysql:5.7 | 3306 | 主数据库，含 5 个 schema（xxl_job、cheji、icars_test、icars_rest、jeesite） |
| amiba-redis | redis:6-alpine | 6379 | 缓存，无密码 |
| amiba-rocketmq-namesrv | apache/rocketmq:4.9.4 | 9876 | 消息队列 NameServer |
| amiba-rocketmq-broker | apache/rocketmq:4.9.4 | 10911 | 消息队列 Broker |

#### 应用服务

| 容器名 | 外部端口 | 内部端口 | Context Path | 协议 | 数据库 | 说明 |
|--------|---------|---------|-------------|------|--------|------|
| amiba-xxl-job-admin | 8080 | 8080 | `/xxl-job-admin` | HTTP | xxl_job | 分布式任务调度管理后台 |
| amiba-web | 8081 | 8081 | `/cServer` | HTTP | cheji | C 端用户 App 后端 |
| amiba-icars-admin | 8082 | 8078 | `/wx-admin` | HTTP | icars_test | 管理后台 + 微信小程序接口 |
| amiba-icars-rest | 8443 | 8443 | `/` | HTTPS | icars_rest | JWT REST API 服务 |
| amiba-b | 8091 | 8091 | `/bServer` | HTTP | cheji | B 端商户 App 后端 |
| amiba-jeesite | 9002 | 9002 | `/jeesite` | HTTP | jeesite | JeeSite 管理后台 |

### 服务依赖关系

```
MySQL ──┬── xxl-job-admin
        ├── web ──── Redis
        ├── icars-admin
        ├── icars-rest
        ├── b ──── Redis, RocketMQ
        └── jeesite ──── Redis, RocketMQ

RocketMQ NameSrv ──── RocketMQ Broker
```

### 部署配置文件

| 文件 | 路径（服务器） | 说明 |
|------|--------------|------|
| docker-compose.server.yml | `/opt/amiba/` | 编排配置 |
| Dockerfile.server | `/opt/amiba/` | alpine 版 JRE（除 jeesite 外的所有服务） |
| Dockerfile.jeesite | `/opt/amiba/` | ubuntu 版 JRE（jeesite 专用，alpine musl 不兼容） |
| jeesite-docker.yml | `/opt/amiba/` | jeesite JDBC/Redis 覆盖配置 |
| keystore.p12 | `/opt/amiba/` | icars-rest SSL 证书 |
| init.sql | `/opt/amiba/mysql/` | 数据库初始化（建库 + xxl-job 建表 + jeesite core 建表） |

---

## 2. 服务访问地址

| 服务 | 访问地址 | 期望响应 |
|------|----------|---------|
| xxl-job-admin | http://114.215.211.119:8080/xxl-job-admin/ | 302 → 登录页 |
| web (C端) | http://114.215.211.119:8081/cServer/ | 404（API 服务，无首页） |
| icars-admin | http://114.215.211.119:8082/wx-admin/ | 302 → 登录页 |
| icars-rest | https://114.215.211.119:8443/ | 200 |
| b (B端) | http://114.215.211.119:8091/bServer/ | 404（API 服务，无首页） |
| jeesite | http://114.215.211.119:9002/jeesite/ | 302 → 登录页 |

---

## 3. API 接口清单

共计约 **1223** 个 REST 接口，分布在 5 个应用服务中。

### 3.1 web — C 端用户 App 后端（276 个接口）

**Base URL:** `http://114.215.211.119:8081/cServer`

| 模块 | 路径前缀 | 说明 | 接口数 |
|------|---------|------|--------|
| AccidentRecordController | `/AccidentRecord` | 事故记录、视频、红包 | 11 |
| AndroidDownController | `/android` | Android APK 版本检查 | 1 |
| AppAuctionAddressController | `/address` | 收货地址管理 | 4 |
| AppAuctionAliPayController | `/auction/alipay` | 支付宝 VIP 订单 | 1 |
| AppAuctionBidController | `/auctionBid` | 竞拍出价 | 3 |
| AppAuctionBrandSubController | `/brandSub` | 品牌订阅 | 3 |
| AppAuctionCashOutController | `/auctionCashOut` | 提现申请 | 1 |
| AppAuctionContrlloer | `/appAuction` | 拍卖车辆核心接口（列表、详情、搜索、收藏） | 17 |
| AppAuctionCounselorController | `/counselor` | 顾问管理 | 2 |
| AppAuctionFeedBackController | `/feedback` | 用户反馈 | 1 |
| AppAuctionFindCarController | `/findcar` | 找车服务 | — |
| AppAuctionIdentifyController | `/identify` | 实名认证 | — |
| AppAuctionMessageIdentifyController | `/authentication` | 短信验证 | — |
| AppAuctionMyMoneyController | `/mymoney` | 我的钱包 | — |
| AppAuctionOrderController | `/auctionOrder` | 拍卖订单 | — |
| AppAuctionVipLvController | `/auctionVip` | VIP 会员等级 | — |
| AppAuctionWarnCarController | `/warnCar` | 关注车辆提醒 | — |
| AppAuctionWithdrawCashController | `/auctionWithdrawCash` | 提现管理 | — |
| AppAuctionWxPayController | `/auction/wxPay` | 微信支付 | — |
| AppBeautyPriceDetailController | `/beautyPriceDetail` | 美容价格明细 | — |
| AppBUserConfigController | `/buserConfig` | B 端用户配置 | — |
| AppClaimTeacherController | `/appClaimTeacher` | 理赔师傅 | — |
| AppRescueIndentController | `/appRescueIndent` | 救援订单 | — |
| AppSendOutSheetController | `/appSendOutSheet` | 派单管理 | — |
| AppSendSheetController | `/appSendSheet` | 派单 | — |
| AppSprayPaintIndentController | `/appSprayPaintIndent` | 喷漆订单 | — |
| AppSubstituteDrivingIndentController | `/appSubstituteDrivingIndent` | 代驾订单 | — |
| AppSubUsualAddressController | `/appSubUsualAddress` | 常用地址 | — |
| AppUserBMessageController | `/appUserBMessage` | B 端消息 | — |
| AppUserCouponController | `/appUserCoupon` | 优惠券 | — |
| AppYearCheckIndentController | `/appYearCheckIndent` | 年检订单 | — |
| BizAccidentController | `/bizAccident` | 事故业务 | — |
| CityController | `/city` | 城市列表 | — |
| CleanIndetController | `/cleanIndet` | 洗车订单 | — |
| CleanPriceDetailController | `/cleanPriceDetail` | 洗车价格明细 | — |
| IndentController | `/indent` | 通用订单 | — |
| IndexController | `/index` | 首页数据 | — |
| LeagueController | `/league` | 联盟/合作 | — |
| LoginController | `/user` | 用户登录注册 | — |
| MerchantsCommentsTreeController | `/merchantsCommentsTree` | 商户评价 | — |
| MerchantsTreeController | `/merchantsTree` | 商户树形列表 | — |
| PushBillController | `/pushBill` | 推送账单 | — |
| VideoCommentsController | `/videoComments` | 视频评论 | — |
| VideoController | `/video` | 视频内容 | — |
| VideoThumbsController | `/videoThumbs` | 视频点赞 | — |
| ViolationController | `/violation` | 违章查询 | — |
| WebSocketController | `/api/socket` | WebSocket 推送 | — |
| WxPayController | `/wx/pay` | 微信支付回调 | — |

### 3.2 b — B 端商户 App 后端（172 个接口）

**Base URL:** `http://114.215.211.119:8091/bServer`

| 模块 | 路径前缀 | 说明 |
|------|---------|------|
| AccidentRecordController | `/accidentRecord` | 事故记录 |
| AndroidDownController | `/android` | Android 版本 |
| AppBeautyPriceDetailController | `/beautyPriceDetail` | 美容价格 |
| AppBUserConfigController | `/buserConfig` | 用户配置 |
| AppCleanIndetController | `/appleanIndet` | 洗车订单 |
| AppRescueIndentController | `/appRescueIndent` | 救援订单 |
| AppSendOutSheetController | `/appSendOutSheet` | 派单 |
| AppSprayPaintIndentController | `/appSprayPaintIndent` | 喷漆订单 |
| AppSubstituteDrivingIndentController | `/appSubstituteDrivingIndent` | 代驾订单 |
| AppUserBMessageController | `/appUserBMessage` | 消息 |
| AppYearCheckIndentController | `/appYearCheckIndent` | 年检订单 |
| CdImgController | `/cdImg` | 车辆图片 |
| CdIndentController | `/cdIndent` | 车辆订单 |
| CdOrderController | `/cdOrder` | 车辆工单 |
| CdPartsDetailsController | `/cdPartsDetails` | 配件明细 |
| CdUserController | `/cdUser` | 车主用户 |
| CleanPriceDetailController | `/cleanPriceDetail` | 洗车价格 |
| FeedbackController | `/feedback` | 反馈 |
| IndentController | `/indent` | 通用订单 |
| LableDetailsReviewTreeController | `/lableDetailsReviewTree` | 标签审核 |
| LoginController | `/user` | 登录 |
| MerchantsCommentsTreeController | `/merchantsCommentsTree` | 商户评价 |
| MerchantsInfoBannerController | `/merchantsInfoBanner` | 商户 Banner |
| MerchantsTreeController | `/merchantsTree` | 商户列表 |
| PersonalController | `/personal` | 个人中心 |
| PushBillController | `/pushBill` | 推送账单 |
| UserController | `/user` | 用户管理 |
| WxPayController | `/wx/pay` | 微信支付 |

### 3.3 icars-admin — 管理后台 + 微信小程序接口（365 个接口）

**Base URL:** `http://114.215.211.119:8082/wx-admin`

混合模式服务：传统 MVC 管理后台（返回 HTML 视图）+ REST API（服务微信小程序和理赔 App）。

| 模块 | 路径前缀 | 说明 |
|------|---------|------|
| AccidentController | `/accid` | 事故管理 |
| AlipayActivityController | `/alipayActivity` | 支付宝活动 |
| BizAccidLevelController | `/bizAccidLevel` | 事故等级 |
| BizAlipayBillController | `/bizAlipayBill` | 支付宝账单 |
| BizClaimController | `/bizClaim` | 理赔管理 |
| BizClaimerShowController | `/bizClaimerShow` | 理赔员展示 |
| BizInsuranceCompanyController | `/bizInsuranceCompany` | 保险公司 |
| BizMaintainPackageController | `/bizMaintainPackage` | 保养套餐 |
| BizMaintainPackageOrderController | `/bizMaintainPackageOrder` | 保养套餐订单 |
| BizOpenFeedbackController | `/bizOpenFeedback` | 开放反馈 |
| BizOpenNotifyController | `/bizOpenNotify` | 开放通知 |
| BizRepairePackageController | `/bizRepairePackage` | 维修套餐 |
| BizRepairePackageOrderController | `/bizRepairePackageOrder` | 维修套餐订单 |
| BizWxSalaryController | `/bizWxSalary` | 微信工资 |
| BizWxpayOrderController | `/bizWxpayOrder` | 微信支付订单 |
| BizYckCzmxController | `/bizYckCzmx` | 养车卡充值明细 |
| BlackboardController | `/blackboard` | 公告板 |
| CityController | `/city` | 城市管理 |
| DeptController | `/dept` | 部门管理 |
| DictController | `/dict` | 字典管理 |
| LogController | `/log` | 日志 |
| MenuController | `/menu` | 菜单管理 |
| NoticeController | `/notice` | 通知 |
| OpenClaimController | `/openClaim` | 开放理赔 |
| PushRecordController | `/pushRecord` | 推送记录 |
| RoleController | `/role` | 角色管理 |
| UserMgrController | `/mgr` | 用户管理 |
| WxpayBillController | `/wxpayBill` | 微信支付账单 |
| XcxMaintenanceController | `/xcxMaintenance` | 小程序保养 |
| **REST API 接口** | | |
| WxAuthController | — | 微信授权 |
| WxPayNotifyController | — | 微信支付回调 |
| WxRestController | — | 微信 REST 接口 |
| AppRestController | — | App REST 接口（`/api/v1/app/*`） |
| XcxController | — | 小程序接口（`/api/v1/wx/*`） |
| ClaimUserController | `/claimUser` | 理赔用户 |
| WxCouponCountController | `/couponCount` | 优惠券统计 |
| XcxUserCountController | `/xcxUserCount` | 小程序用户统计 |
| TestController | `/api/test` | 测试接口 |

### 3.4 icars-rest — JWT REST API 服务（4 个接口）

**Base URL:** `https://114.215.211.119:8443`

| 模块 | 路径前缀 | 说明 |
|------|---------|------|
| AuthController | `/auth` | JWT 认证（获取 Token） |
| ExampleController | `/hello` | 示例接口 |

### 3.5 jeesite — JeeSite 管理后台（406 个接口）

**Base URL:** `http://114.215.211.119:9002/jeesite`

JeeSite 4.x 框架管理后台，管理路径前缀为 `/a`，完整路径格式：`/jeesite/a/app/{模块}/{操作}`。

| 模块 | 路径前缀 | 说明 |
|------|---------|------|
| AppAccidentRecordController | `/a/app/appAccidentRecord` | 事故记录管理 |
| AppAllMerchantsController | `/a/app/appAllMerchants` | 全部商户 |
| AppAuctionBailLogController | `/a/app/appAuctionBailLog` | 拍卖保证金日志 |
| AppAuctionBidLogController | `/a/app/appAuctionBidLog` | 竞拍出价日志 |
| AppAuctionController | `/a/app/appAuction` | 拍卖管理 |
| AppAuctionMessageIdentifyController | `/a/app/appAuctionMessageIdentify` | 短信认证 |
| AppAuctionOnePriceCarLogController | `/a/app/appAuctionOnePriceCarLog` | 一口价记录 |
| AppAuctionOrderController | `/a/app/appAuctionOrder` | 拍卖订单 |
| AppAuctionPayLogController | `/a/app/appAuctionPayLog` | 支付日志 |
| AppAuctionUpController | `/a/app/appAuctionUp` | 上架管理 |
| AppBUserController | `/a/app/appBUser` | B 端用户管理 |
| AppBusinessConfirmController | `/a/app/appBusinessConfirm` | 业务确认 |
| AppCleanIndetController | `/a/app/appCleanIndet` | 洗车订单 |
| AppCleanPriceDetailController | `/a/app/appCleanPriceDetail` | 洗车价格 |
| AppEveryMesgController | `/a/app/appEveryMesg` | 消息管理 |
| AppFeedbackController | `/a/app/appFeedback` | 反馈管理 |
| AppIndentController | `/a/app/appIndent` | 订单管理 |
| AppLableController | `/a/app/appLable` | 标签管理 |
| AppLableDetailsReviewTreeController | `/a/app/appLableDetailsReviewTree` | 标签审核 |
| AppLeagueController | `/a/app/appLeague` | 联盟管理 |
| AppMerchantsCommentsController | `/a/app/appMerchantsComments` | 商户评价 |
| AppMerchantsCommentsTreeController | `/a/app/appMerchantsCommentsTree` | 评价树 |
| AppMerchantsController | `/a/app/appMerchants` | 商户管理 |
| AppMerchantsInfoBannerController | `/a/app/appMerchantsInfoBanner` | 商户 Banner |
| AppMerchantsLableController | `/a/app/appMerchantsLable` | 商户标签 |
| AppOrderRollBackController | `/a/app/appOrderRollBack` | 订单回退 |
| AppOurIndentAmountController | `/a/app/appOurIndentAmount` | 订单金额 |
| AppPayAmountRecordController | `/a/app/appPayAmountRecord` | 支付记录 |
| AppPushBillController | `/a/app/appPushBill` | 推送账单 |
| AppRescueIndentController | `/a/app/appRescueIndent` | 救援订单 |
| AppSendOutSheetController | `/a/app/appSendOutSheet` | 派单管理 |
| AppSendRepairController | `/a/app/appSendRepair` | 送修管理 |
| AppSprayPaintIndentController | `/a/app/appSprayPaintIndent` | 喷漆订单 |
| AppSubstituteDrivingIndentController | `/a/app/appSubstituteDrivingIndent` | 代驾订单 |
| AppUpMerchantsController | `/a/app/appUpMerchants` | 商户上架 |
| AppUserAccountRecordController | `/a/app/appUserAccountRecord` | 用户账户记录 |
| AppUserBehaviorController | `/a/app/appUserBehavior` | 用户行为 |
| AppUserBMessageController | `/a/app/appUserBMessage` | B 端消息 |
| AppUserController | `/a/app/appUser` | 用户管理 |
| AppUserPlusController | `/a/app/appUserPlus` | 用户增值 |
| AppVersionController | `/a/app/appVersion` | 版本管理 |
| AppVideoCommentsController | `/a/app/appVideoComments` | 视频评论 |
| AppVideoController | `/a/app/appVideo` | 视频管理 |
| AppWxBankController | `/a/app/appWxBank` | 微信银行卡 |
| AppWxCashOutRecordController | `/a/app/appWxCashOutRecord` | 提现记录 |
| AppWxpayOrderController | `/a/app/appWxpayOrder` | 微信支付订单 |
| AppYearCheckIndentController | `/a/app/appYearCheckIndent` | 年检订单 |
| BizAccidentController | `/a/app/bizAccident` | 事故管理 |
| CarInforController | `/a/app/carInfor` | 车辆信息 |
| CommonsConfigController | `/a/app/commons` | 通用配置 |
| FowardContoller | `/a/app/forward` | 转发控制 |
| PrivateController | `/web` | 私有接口 |
| WebSocketController | `/api/socket` | WebSocket 推送 |

---

## 4. 运维指南

### 查看所有容器状态

```bash
ssh -i ~/.ssh/id_ed25519 root@114.215.211.119 \
  "cd /opt/amiba && docker-compose -f docker-compose.server.yml ps"
```

### 查看指定服务日志

```bash
ssh -i ~/.ssh/id_ed25519 root@114.215.211.119 \
  "docker logs -f --tail 100 amiba-web"
```

### 重启单个服务

```bash
ssh -i ~/.ssh/id_ed25519 root@114.215.211.119 \
  "cd /opt/amiba && docker-compose -f docker-compose.server.yml restart web"
```

### 重新部署（使用 /deploy 命令）

在本工程目录下执行 `/deploy` 即可自动完成构建、上传、重建容器、验证的完整部署流程。

### 服务启动耗时参考

| 服务 | 启动时间（约） |
|------|--------------|
| xxl-job-admin | 5-6 分钟 |
| web | 10-12 分钟 |
| b | 10-11 分钟 |
| icars-admin | 12-13 分钟 |
| icars-rest | 3-4 分钟 |
| jeesite | 14-15 分钟 |

> 注：服务器内存 7.3G，6 个 Java 服务同时启动时资源竞争较大，启动时间较长。

### 已知限制

- **jeesite 业务表缺失**：`app_auction_up`、`app_wx_cash_out_record` 等业务表的 SQL 不在代码仓库中，需从生产环境导入。不影响 jeesite 服务启动，仅影响部分业务功能。
- **icars-rest SSL 证书**：使用自签名证书 `keystore.p12`，浏览器访问时会提示不安全。
- **MySQL 首次初始化**：`init.sql` 仅在 `mysql_data` volume 不存在时执行。如需重新初始化，需先删除 volume：`docker volume rm amiba_mysql_data`。
