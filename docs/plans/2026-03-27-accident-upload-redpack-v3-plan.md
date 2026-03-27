# 事故上传→审核→微信商家转账V3直发红包 实施计划

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** 打通小程序事故上传→icars-admin审核→微信商家转账V3直发红包到用户零钱的完整链路，替换摇摇啦代发方案。

**Architecture:** 小程序上传视频到七牛云后，调用icars-admin的 `/api/v1/wx/accid/newAdd` 创建事故记录；管理员在icars-admin或jeesite_new后台审核；审核通过后调用新的微信商家转账V3 Service直接将红包打到用户微信零钱。jeesite_new保留审核能力，通过HTTP回调 icars-admin 的 `/api/v1/app/checkSuccess` 接口触发V3转账。

**Tech Stack:** 微信商家转账V3 API（SHA256-RSA2048签名，JSON格式），Spring Boot，微信支付Java SDK（wechatpay-java），小程序端JS。

---

## 前置条件（编码前必须准备）

在开始编码前，确保以下材料已就绪：

1. 新微信商户号已注册并完成实名认证
2. 商户平台已开通「商家转账到零钱」产品（产品中心 → 商家转账到零钱 → 开通）
3. 小程序 AppID（wx5d88d6c7c216e1f3）已在新商户号后台完成绑定
4. 已下载商户API证书（得到 `apiclient_key.pem` 和 `apiclient_cert.pem`）
5. 已设置APIv3密钥（32位字符串）
6. 已知商户证书序列号（`openssl x509 -noout -serial -in apiclient_cert.pem` 可查）

---

## Task 1: icars-admin — 新增V3配置属性类

**Files:**
- Create: `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/config/properties/WxPayV3Properties.java`
- Modify: `icars-master/icars-master/icars-admin/src/main/resources/application-local.properties`
- Modify: `icars-master/icars-master/icars-admin/src/main/resources/application-test.properties`
- Modify: `icars-master/icars-master/icars-admin/src/main/resources/application-prod.properties`

**Step 1: 创建V3配置属性类**

```java
// WxPayV3Properties.java
package com.stylefeng.guns.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "wx.pay.v3")
public class WxPayV3Properties {
    /** 小程序AppID */
    private String appId;
    /** 新商户号 */
    private String mchId;
    /** APIv3密钥（32位） */
    private String apiV3Key;
    /** 商户API私钥路径（apiclient_key.pem） */
    private String privateKeyPath;
    /** 商户API证书路径（apiclient_cert.pem） */
    private String certPath;
    /** 商户证书序列号 */
    private String certSerialNo;

    // getters and setters
    public String getAppId() { return appId; }
    public void setAppId(String appId) { this.appId = appId; }
    public String getMchId() { return mchId; }
    public void setMchId(String mchId) { this.mchId = mchId; }
    public String getApiV3Key() { return apiV3Key; }
    public void setApiV3Key(String apiV3Key) { this.apiV3Key = apiV3Key; }
    public String getPrivateKeyPath() { return privateKeyPath; }
    public void setPrivateKeyPath(String privateKeyPath) { this.privateKeyPath = privateKeyPath; }
    public String getCertPath() { return certPath; }
    public void setCertPath(String certPath) { this.certPath = certPath; }
    public String getCertSerialNo() { return certSerialNo; }
    public void setCertSerialNo(String certSerialNo) { this.certSerialNo = certSerialNo; }
}
```

**Step 2: 在三个配置文件中追加V3配置**

在 `application-local.properties`、`application-test.properties`、`application-prod.properties` 末尾分别追加：

```properties
# 微信商家转账V3配置
wx.pay.v3.appId=wx5d88d6c7c216e1f3
wx.pay.v3.mchId=（新商户号，待填）
wx.pay.v3.apiV3Key=（32位APIv3密钥，待填）
wx.pay.v3.privateKeyPath=/opt/deploy/apiclient_key.pem
wx.pay.v3.certPath=/opt/deploy/apiclient_cert.pem
wx.pay.v3.certSerialNo=（证书序列号，待填）
```

**Step 3: 确认Spring Boot能读取配置**

检查 icars-admin 的主启动类（`GunsApplication.java`）是否有 `@EnableConfigurationProperties`，若无则确认 `@SpringBootApplication` 已存在（Spring Boot会自动扫描 `@Component` 注解的配置类）。

---

## Task 2: icars-admin — 添加微信支付V3 SDK依赖

**Files:**
- Modify: `icars-master/icars-master/icars-admin/pom.xml`

**Step 1: 在pom.xml的dependencies中添加微信支付V3官方SDK**

```xml
<!-- 微信支付V3官方SDK -->
<dependency>
    <groupId>com.github.wechatpay-apiv3</groupId>
    <artifactId>wechatpay-java</artifactId>
    <version>0.2.14</version>
</dependency>
```

放置位置：在现有 `<dependencies>` 块内，紧接其他依赖之后。

**Step 2: 验证依赖可以下载**

```bash
cd /Users/weixi1/Documents/PartWorkspace/qianliyan/backend/icars-master/icars-master
mvn dependency:resolve -pl icars-admin | grep wechatpay
```

期望输出包含：`com.github.wechatpay-apiv3:wechatpay-java:jar:0.2.14`

---

## Task 3: icars-admin — 创建V3商家转账Service

**Files:**
- Create: `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/wxpay/WxPayV3TransferService.java`

**Step 1: 创建Service类**

```java
package com.stylefeng.guns.wxpay;

import com.stylefeng.guns.config.properties.WxPayV3Properties;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.transferbatch.TransferBatchService;
import com.wechat.pay.java.service.transferbatch.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.Instant;
import java.util.Collections;

@Service
public class WxPayV3TransferService {

    private static final Logger log = LoggerFactory.getLogger(WxPayV3TransferService.class);

    @Resource
    private WxPayV3Properties v3Properties;

    private TransferBatchService transferBatchService;

    @PostConstruct
    public void init() {
        RSAAutoCertificateConfig config = new RSAAutoCertificateConfig.Builder()
                .merchantId(v3Properties.getMchId())
                .privateKeyFromPath(v3Properties.getPrivateKeyPath())
                .merchantSerialNumber(v3Properties.getCertSerialNo())
                .apiV3Key(v3Properties.getApiV3Key())
                .build();
        transferBatchService = new TransferBatchService.Builder().config(config).build();
    }

    /**
     * 商家转账到用户零钱（V3）
     *
     * @param openid      用户小程序openid
     * @param accid       事故ID（用于生成唯一批次号）
     * @param amountFen   金额，单位：分
     * @return true=成功，false=失败
     */
    public boolean transferToUser(String openid, Integer accid, long amountFen) {
        try {
            String batchNo = "accid_" + accid + "_" + Instant.now().toEpochMilli();
            String detailNo = "detail_" + accid + "_" + Instant.now().toEpochMilli();

            TransferDetailInput detail = new TransferDetailInput();
            detail.setOutDetailNo(detailNo);
            detail.setTransferAmount(amountFen);
            detail.setTransferRemark("事故上报红包奖励");
            detail.setOpenid(openid);

            InitiateBatchTransferRequest request = new InitiateBatchTransferRequest();
            request.setAppid(v3Properties.getAppId());
            request.setOutBatchNo(batchNo);
            request.setBatchName("事故上报奖励");
            request.setBatchRemark("感谢您参加提报事故，为城市做一份贡献");
            request.setTotalAmount(amountFen);
            request.setTotalNum(1);
            request.setTransferDetailList(Collections.singletonList(detail));

            InitiateBatchTransferResponse response = transferBatchService.initiateBatchTransfer(request);
            log.info("V3商家转账成功 accid={} batchId={} batchNo={}",
                    accid, response.getBatchId(), response.getOutBatchNo());
            return true;
        } catch (Exception e) {
            log.error("V3商家转账失败 accid={} openid={} error={}", accid, openid, e.getMessage(), e);
            return false;
        }
    }
}
```

---

## Task 4: icars-admin — 修改AccidentController.checkSuccess，替换摇摇啦逻辑

**Files:**
- Modify: `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/controller/AccidentController.java`

**当前代码位置：** `:290-434`

**Step 1: 在AccidentController中注入WxPayV3TransferService**

在现有 `@Resource` 注入区域（:57-90行附近）新增：

```java
@Resource
private WxPayV3TransferService wxPayV3TransferService;
```

**Step 2: 修改checkSuccess方法（:294-434）**

找到摇摇啦调用代码块（:309-331），将其注释，替换为V3调用：

```java
// ========== 原摇摇啦代发逻辑（已废弃，注释保留）==========
//String uid = "10815568";
//String apikey = "amiba500500";
//long reqtick = Instant.now().getEpochSecond();
//BigDecimal multiply = amount.multiply(new BigDecimal("100"));
//String type = "1";
//Map<String, String> data = new HashMap<String, String>();
//data.put("uid", uid);
//data.put("type", type);
//data.put("orderid", String.valueOf(accdId));
//data.put("money", String.valueOf(multiply));
//data.put("reqtick", String.valueOf(reqtick));
//data.put("openid", bizWxUser.getThirdOpenId());
//data.put("apikey", apikey);
//String sign = md5(uid + type + accdId + multiply + reqtick + bizWxUser.getThirdOpenId() + apikey);
//String url = "https://www.yaoyaola.net/exapi/SendRedPackToOpenid?...";
//String result1 = HttpUtils.sendHttpGet(url);
//JSONObject resultJOSN = JSONObject.parseObject(result1);
//String errcode = resultJOSN.getString("errcode");
// ========== 摇摇啦代发逻辑结束 ==========

// 微信商家转账V3直发
long amountFen = amount.multiply(new BigDecimal("100")).longValue();
boolean v3Result = wxPayV3TransferService.transferToUser(
        bizWxUser.getOpenid(), accdId, amountFen);

NumberFormat nf = NumberFormat.getInstance();
nf.setGroupingUsed(false);
String amountStr = nf.format(amount.multiply(new BigDecimal(100)).doubleValue());
if (v3Result) {
    wxPayBizService.doSuccess(bizWxUser.getOpenid(),