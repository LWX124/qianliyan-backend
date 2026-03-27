# 事故上传 → 审核 → 微信商家转账 V3 直发红包 设计方案

## 背景

当前小程序上传事故视频到七牛云后，调用 c-web 的 `/AccidentRecord/addVideo` 接口存储记录，但该流程与 icars-admin 的审核→红包发放链路没有关联。审核通过后的红包发放走的是第三方摇摇啦平台（yaoyaola.net）代发，需要改为微信商家转账 V3 接口直发到用户零钱。

## 决策记录

- 主链路服务：**icars-admin**（小程序直接对接 icars-admin）
- 红包发放方式：**微信商家转账 V3**（`/v3/transfer/batches`）
- 商户号：**新注册商户号**
- jeesite_new 审核：**保留**，通过 HTTP 回调 icars-admin 触发红包
- 废弃代码处理：**注释保留**，不直接删除

## 整体架构

```
小程序 (qianliyan-minipro)
  │  1. 视频上传七牛云拿到URL
  │  2. 调用 icars-admin 创建事故记录
  │     POST /api/v1/wx/accid/newAdd
  ▼
icars-admin (主服务)
  │  → 写入 biz_accident 表 (status=1 未审核)
  │  → WebSocket 推送管理后台
  │  → 自动派单理赔顾问
  │
  ├── icars-admin 后台审核 ──────────────┐
  │   POST /accid/checkSuccess            │
  │                                       ▼
  ├── jeesite_new 后台审核 ──────→ HTTP 回调 icars-admin
  │   PayWxAmountService.passSta()        │
  │   → POST /api/v1/app/checkSuccess     │
  │                                       ▼
  │                              微信商家转账 V3
  │                    POST /v3/transfer/batches
  │                              → 用户微信零钱
  │                              → 记录 biz_wxpay_bill
  └───────────────────────────────────────┘
```

## 变更点清单

### 1. 小程序端（qianliyan-minipro）

| 文件 | 变更内容 |
|------|----------|
| `config/index.js` | `baseUrl` 改为指向 icars-admin 服务地址 |
| `pages/record-video/record-video.js:62` | URL 从 `/AccidentRecord/addVideo` 改为 `/api/v1/wx/accid/newAdd`，参数适配 icars-admin 格式（video, lng, lat, thirdSessionKey） |
| `pages/take-photo/take-photo.js:75` | 同上，图片上传也适配 icars-admin 接口 |
| `utils/request.js:9` | header `THIRDSESSIONKEY` 保持，确保 icars-admin 的 session 体系对接 |

### 2. icars-admin 后端

#### 新增文件

| 文件 | 说明 |
|------|------|
| `WxPayV3Properties.java` | V3 配置属性类（mchId, appId, apiV3Key, 证书路径, 证书序列号） |
| `WxPayV3Configuration.java` | Spring 配置类，加载 V3 证书和 HttpClient |
| `WxPayV3TransferService.java` | 微信商家转账 V3 服务类，调用 `/v3/transfer/batches` |

#### 修改文件

| 文件 | 变更内容 |
|------|----------|
| `AccidentController.java:290-434` | `checkSuccess()` 方法：注释摇摇啦逻辑（:309-331），改为调用 V3 商家转账 |
| `AppRestController.java:2595-2719` | `/api/v1/app/checkSuccess`：注释摇摇啦逻辑，改为调用 V3 商家转账（供 jeesite_new 回调） |
| `WxPayBizService.java:166` | `autoTrigger()` 方法内部改为调用 V3 转账（供 `WxpayBillController.rePay` 重发时使用） |
| `application-prod.properties` | 新增 V3 配置项 |
| `application-test.properties` | 新增 V3 配置项 |
| `application-local.properties` | 新增 V3 配置项 |

#### 部署新增

| 文件 | 说明 |
|------|------|
| `apiclient_key.pem` | 商户API私钥，部署到 `/opt/deploy/` |
| `apiclient_cert.pem` | 商户API证书，部署到 `/opt/deploy/` |

### 3. jeesite_new — 零代码改动

| 文件 | 说明 |
|------|------|
| `PayWxAmountService.java:22` | URL 保持调用 icars-admin 的 `/api/v1/app/checkSuccess`，无需修改。icars-admin 端会将该接口内的逻辑改为 V3 转账 |

## 微信商家转账 V3 配置

### 需要准备的密钥

| 配置项 | 说明 | 获取方式 |
|--------|------|----------|
| `mchId` | 新商户号 | 微信商户平台 → 账户中心 → 商户信息 |
| `appId` | 小程序 AppID (wx5d88d6c7c216e1f3) | 需在商户平台绑定此小程序 AppID |
| `apiV3Key` | APIv3 密钥（32位字符串） | 商户平台 → 账户中心 → API安全 → 设置APIv3密钥 |
| `apiclient_key.pem` | 商户API私钥 | 商户平台 → 账户中心 → API安全 → 申请API证书下载 |
| `apiclient_cert.pem` | 商户API证书 | 同上 |
| `certSerialNo` | 商户证书序列号 | 证书申请后显示，或 `openssl x509 -noout -serial -in apiclient_cert.pem` |

### 商户平台需开通

- 产品中心 → 商家转账到零钱 → 开通

### 配置文件结构

```properties
# application-prod.properties 新增
wx.pay.v3.appId=wx5d88d6c7c216e1f3
wx.pay.v3.mchId=（新商户号）
wx.pay.v3.apiV3Key=（32位密钥）
wx.pay.v3.privateKeyPath=/opt/deploy/apiclient_key.pem
wx.pay.v3.certPath=/opt/deploy/apiclient_cert.pem
wx.pay.v3.certSerialNo=（证书序列号）
```

## V3 商家转账接口要点

### 请求

```
POST https://api.mch.weixin.qq.com/v3/transfer/batches

请求体 (JSON):
{
  "appid": "wx5d88d6c7c216e1f3",
  "out_batch_no": "accid_{事故ID}_{时间戳}",
  "batch_name": "事故上报奖励",
  "batch_remark": "感谢您参加提报事故",
  "total_amount": 120,
  "total_num": 1,
  "transfer_detail_list": [{
    "out_detail_no": "detail_{事故ID}_{时间戳}",
    "transfer_amount": 120,
    "transfer_remark": "事故上报红包奖励",
    "openid": "用户的小程序openid"
  }]
}
```

### 签名方式

- SHA256-RSA2048（用 apiclient_key.pem 私钥签名）
- 数据格式为 JSON（不再是 XML）

### 关键优势

V3 商家转账可以直接使用**小程序 openid**，不再需要通过 unionid 映射公众号 openid。现有代码中 `WxPayBizService.wxPayRedBag()` 的公众号 openid 查询逻辑（:89-92）不再需要。

## 注释保留（不删除）的代码

| 位置 | 内容 |
|------|------|
| `AccidentController.java:309-331` | 摇摇啦平台代发红包逻辑 |
| `AppRestController.java:2709-2719` | 摇摇啦平台代发红包逻辑 |
| `WxPayBizService.wxPayRedBag():87-115` | 旧版微信红包 API 调用 |
| `WxPayBizService.mmpayMktTransfers():58-73` | 旧版企业付款到零钱 API |
| `WxPayBizService.wxPayRedBag():89-92` | 公众号 openid 映射查询 |

## 涉及的数据库表

| 表名 | 库 | 说明 |
|------|-----|------|
| `biz_accident` | icars-admin | 事故主表，status: 1=未审核, 2=审核通过, 3=审核不通过 |
| `biz_wxpay_bill` | icars-admin | 红包支付主表，status: 0=成功, 1=失败 |
| `biz_wx_pay_record` | icars-admin | 红包支付明细表 |
| `biz_wx_user` | icars-admin | 微信用户表（openid, unionId） |
| `app_accident_record` | jeesite_new | C端事故记录表（jeesite_new 审核用） |

## 不变的部分

- 七牛云文件上传逻辑（小程序端 `uploadFile` 方法）
- icars-admin 的 `/api/v1/wx/accid/newAdd` 接口逻辑
- jeesite_new 的 `PayWxAmountService` 回调地址和调用方式
- 审核状态枚举 `AccdStatus`（1=未审核, 2=通过, 3=不通过）
- `biz_wxpay_bill` / `biz_wx_pay_record` 表结构（记录方式不变）
- WebSocket 推送/自动派单逻辑
