# 事故上报页面微信支付余额显示与审核校验设计

## 需求背景

在 `/wx-admin` 的【系统管理】→【事故上报】页面，需要实现两个功能：

1. 在页面上显示微信支付账户余额，方便管理员实时了解账户状态
2. 在审核通过时校验余额是否充足，如果余额不足则阻止审核并提示管理员充值

当前问题：审核通过时会向用户发放红包奖励，但如果账户余额不足且未及时充值，会导致发放失败。

## 设计目标

- 在事故上报列表页面实时显示微信支付余额
- 审核通过前校验余额是否充足（余额 ≥ 红包金额 × 2）
- 余额不足时阻止审核操作，并明确提示当前余额和所需金额
- 使用微信支付 V3 API 确保余额数据准确

## 技术方案

### 方案选择

采用**方案 A：调用微信支付 V3 余额查询 API**

**理由：**
- 数据准确实时，直接从微信获取商户账户余额
- 项目已集成微信支付 V3 SDK（转账功能），集成成本低
- 查询频率不高（页面加载 + 审核操作），不会造成性能问题
- 避免本地估算带来的数据不准确问题

**余额充足判断标准：**
- 余额 ≥ 当前红包金额 × 2（安全缓冲）
- 例如：要发放 5 元红包，余额至少需要 10 元

## 架构设计

### 后端架构

#### 服务层增强

**WxPayV3TransferService.java**
- 新增方法：`queryMerchantBalance()` - 查询商户账户余额
- 返回值：余额金额（单位：分），查询失败返回 -1
- API 端点：`GET /v3/merchant/fund/balance/BASIC`
- 使用现有的 `HttpClient` 实例和配置

#### Controller 层改动

**AccidentController.java**

1. 新增接口：`GET /accid/balance`
   - 功能：查询并返回微信支付余额
   - 返回格式：`{balance: 123.45, balanceStr: "123.45 元"}`
   - 错误处理：查询失败返回 `ErrorTip(500, "余额查询失败")`

2. 增强方法：`checkSuccess(Integer accdId, BigDecimal amount, String reason)`
   - 在原有逻辑前增加余额校验
   - 校验逻辑：
     - 调用 `queryMerchantBalance()` 获取余额
     - 计算所需金额：`requiredAmount = amount × 2`
     - 比较：`balance >= requiredAmount`
   - 余额不足时返回：`ErrorTip(500, "微信支付余额不足！当前余额：XX 元，需要：YY 元，请及时充值")`
   - 余额充足时继续原有审核流程

### 前端架构

#### 页面显示

**accid.html**
- 在"红包合计"下方新增"微信支付余额"显示区域
- 样式与"红包合计"保持一致
- 余额金额使用蓝色（`#1890ff`）显示，便于区分

#### JS 逻辑

**accid.js**

1. 新增函数：`MgrAccd.queryBalance()`
   - 调用后端 `/accid/balance` 接口
   - 成功：更新页面显示余额
   - 失败：显示"查询失败"

2. 调用时机：
   - 页面初始化时（`$(function() {...})` 中）
   - 搜索/刷新操作后（`MgrAccd.search()` 中）

3. 审核流程：
   - 用户在 `redpay_choose.html` 选择红包金额并提交
   - 后端 `checkSuccess()` 自动校验余额
   - 余额不足时，后端返回错误，前端显示错误提示

## 数据流设计

### 余额查询流程

```
前端页面加载
    ↓
调用 GET /accid/balance
    ↓
后端调用微信 V3 API
    ↓
GET /v3/merchant/fund/balance/BASIC
    ↓
解析 available_amount 字段
    ↓
转换为元（保留2位小数）
    ↓
返回 {balance: 123.45, balanceStr: "123.45 元"}
    ↓
前端显示在页面上
```

### 审核校验流程

```
管理员选择红包金额（如 5.00 元）
    ↓
点击"确认审核"
    ↓
前端提交 POST /accid/checkSuccess?accdId=xxx&amount=5.00
    ↓
后端查询微信支付余额
    ↓
校验：余额 >= amount × 2 (即 >= 10.00 元)
    ↓
    ├─ 余额不足
    │   ↓
    │   返回 ErrorTip(500, "微信支付余额不足！当前余额：8.50 元，需要：10.00 元，请及时充值")
    │   ↓
    │   前端显示错误提示
    │
    └─ 余额充足
        ↓
        继续原有审核流程
        ↓
        修改事故状态为"审核通过"
        ↓
        发放红包（V2 红包或 V3 转账）
        ↓
        发送审核通知
        ↓
        返回 SUCCESS_TIP
```

### 错误处理

| 错误场景 | 处理方式 |
|---------|---------|
| 微信 API 调用失败 | 返回 `ErrorTip(500, "余额查询失败，无法完成审核，请稍后重试")` |
| 网络超时 | 设置 10 秒超时，超时后提示"查询超时" |
| 余额不足 | 明确提示当前余额和所需金额，阻止审核操作 |
| HttpClient 未初始化 | 返回 -1，提示"余额查询失败" |

## 实现细节

### 后端实现

#### 1. WxPayV3TransferService.java 新增方法

```java
/**
 * 查询商户账户余额
 * @return 余额（单位：分），查询失败返回 -1
 */
public long queryMerchantBalance() {
    if (httpClient == null) {
        log.warn("V3商家转账未初始化，无法查询余额");
        return -1;
    }
    try {
        String url = "https://api.mch.weixin.qq.com/v3/merchant/fund/balance/BASIC";

        HttpHeaders headers = new HttpHeaders();
        headers.addHeader("Accept", "application/json");

        HttpRequest request = new HttpRequest.Builder()
            .httpMethod(HttpMethod.GET)
            .url(url)
            .headers(headers)
            .build();

        log.info("查询商户余额请求");
        HttpResponse<JsonResponseBody> response = httpClient.execute(request, JsonResponseBody.class);

        String bodyStr = "";
        com.wechat.pay.java.core.http.ResponseBody rawBody = response.getBody();
        if (rawBody instanceof JsonResponseBody) {
            String raw = ((JsonResponseBody) rawBody).getBody();
            if (raw != null && !raw.isEmpty()) {
                bodyStr = raw;
            }
        }
        log.info("查询商户余额响应 body={}", bodyStr);

        // 解析 available_amount 字段
        if (bodyStr.contains("\"available_amount\"")) {
            int idx = bodyStr.indexOf("\"available_amount\"");
            int colon = bodyStr.indexOf(":", idx);
            int comma = bodyStr.indexOf(",", colon);
            if (comma == -1) comma = bodyStr.indexOf("}", colon);
            String amountStr = bodyStr.substring(colon + 1, comma).trim();
            long balance = Long.parseLong(amountStr);
            log.info("查询商户余额成功 balance={} 分", balance);
            return balance;
        }

        log.error("查询商户余额响应缺少 available_amount 字段 body={}", bodyStr);
        return -1;
    } catch (Exception e) {
        log.error("查询商户余额失败 error={}", e.getMessage(), e);
        return -1;
    }
}
```

#### 2. AccidentController.java 新增接口

在 `AccidentController` 类中新增方法：

```java
/**
 * 查询微信支付余额
 */
@RequestMapping("/balance")
@Permission
@ResponseBody
public Object queryBalance() {
    long balanceFen = wxPayV3TransferService.queryMerchantBalance();
    if (balanceFen < 0) {
        return new ErrorTip(500, "余额查询失败");
    }
    BigDecimal balanceYuan = new BigDecimal(balanceFen)
        .divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
    Map<String, Object> result = new HashMap<>();
    result.put("balance", balanceYuan);
    result.put("balanceStr", balanceYuan.toPlainString() + " 元");
    return result;
}
```

#### 3. AccidentController.checkSuccess() 方法增强

在 `checkSuccess()` 方法开头（第 305 行后）增加余额校验：

```java
public Tip checkSuccess(@RequestParam Integer accdId, @RequestParam BigDecimal amount,
                        @RequestParam(required = false) String reason) throws Exception {
    if (ToolUtil.isEmpty(accdId) || amount == null) {
        throw new GunsException(BizExceptionEnum.REQUEST_NULL);
    }

    // ========== 新增：校验余额是否充足 ==========
    long balanceFen = wxPayV3TransferService.queryMerchantBalance();
    if (balanceFen < 0) {
        return new ErrorTip(500, "余额查询失败，无法完成审核，请稍后重试");
    }
    BigDecimal balanceYuan = new BigDecimal(balanceFen)
        .divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
    BigDecimal requiredAmount = amount.multiply(new BigDecimal("2"));
    if (balanceYuan.compareTo(requiredAmount) < 0) {
        return new ErrorTip(500, "微信支付余额不足！当前余额：" + balanceYuan.toPlainString() +
            " 元，需要：" + requiredAmount.toPlainString() + " 元，请及时充值");
    }
    // ========== 余额校验结束 ==========

    // 先检查是否已发过红包（防止重复操作）
    BizWxpayBill existBill = bizWxpayBillService.selectOneByAccid(accdId);
    // ... 后续原有逻辑保持不变
}
```

### 前端实现

#### 1. accid.html 增加余额显示

在第 60 行（红包合计区域）后增加：

```html
<div class="col-sm-12">
    <div class="row">
        <div class="form-group">
            <label class="col-sm-2 control-label" style="height: 34px;line-height: 34px;">微信支付余额</label>
            <div class="col-sm-10">
                <input class="form-control" id="balanceDiv" name="balanceDiv" type="text" readonly="readonly"
                    style="margin-left: -213px;border: none;text-align: left;background-color: rgb(255, 255, 255);color: #1890ff;"
                    value="查询中...">
            </div>
        </div>
    </div>
</div>
```

#### 2. accid.js 增加余额查询函数

在第 367 行（`MgrAccd.queryRedPackSum` 函数后）增加：

```javascript
/**
 * 查询微信支付余额
 */
MgrAccd.queryBalance = function () {
    var ajax = new $ax(Feng.ctxPath + "/accid/balance", function (data) {
        if (data.balanceStr) {
            $('#balanceDiv').val(data.balanceStr);
        } else {
            $('#balanceDiv').val("查询失败");
        }
    }, function (data) {
        $('#balanceDiv').val("查询失败");
    });
    ajax.start();
}
```

#### 3. 页面加载时查询余额

在第 384 行（`MgrAccd.table = table.init();` 后）增加：

```javascript
MgrAccd.table = table.init();
MgrAccd.queryRedPackSum(MgrAccd.formParams());
MgrAccd.queryBalance(); // 新增：页面加载时查询余额
```

#### 4. 搜索时刷新余额

在第 263 行（`MgrAccd.table.refresh({query: queryData});` 后）增加：

```javascript
MgrAccd.table.refresh({query: queryData});
MgrAccd.queryRedPackSum(queryData);
MgrAccd.queryBalance(); // 新增：搜索时刷新余额
```

## 文件修改清单

### 后端文件

1. `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/wxpay/WxPayV3TransferService.java`
   - 新增方法：`queryMerchantBalance()`

2. `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/controller/AccidentController.java`
   - 新增接口：`queryBalance()`
   - 增强方法：`checkSuccess()` - 增加余额校验逻辑

### 前端文件

3. `icars-master/icars-master/icars-admin/src/main/webapp/WEB-INF/view/biz/accid/accid.html`
   - 新增余额显示区域

4. `icars-master/icars-master/icars-admin/src/main/webapp/static/modular/biz/accd/accid.js`
   - 新增函数：`MgrAccd.queryBalance()`
   - 页面加载时调用余额查询
   - 搜索时刷新余额

## 测试要点

### 功能测试

1. **余额显示测试**
   - 打开事故上报页面，验证余额是否正确显示
   - 执行搜索操作，验证余额是否刷新
   - 刷新页面，验证余额是否重新加载

2. **余额充足场景**
   - 确保账户余额充足（≥ 红包金额 × 2）
   - 选择一条未审核事故，点击"审核通过"
   - 选择红包金额（如 5 元）
   - 点击"确认审核"
   - 验证审核成功，红包发放成功

3. **余额不足场景**
   - 模拟账户余额不足（< 红包金额 × 2）
   - 选择一条未审核事故，点击"审核通过"
   - 选择红包金额
   - 点击"确认审核"
   - 验证提示"微信支付余额不足！当前余额：XX 元，需要：YY 元，请及时充值"
   - 验证事故状态未改变（仍为"未审核"）

4. **边界测试**
   - 余额刚好等于红包金额 × 2，验证能否通过
   - 余额比红包金额 × 2 少 0.01 元，验证是否阻止

### 异常测试

1. **API 调用失败**
   - 模拟微信 API 不可用
   - 验证页面显示"查询失败"
   - 验证审核时提示"余额查询失败，无法完成审核，请稍后重试"

2. **网络超时**
   - 模拟网络延迟
   - 验证超时后的错误提示

3. **配置错误**
   - 模拟 `HttpClient` 未初始化
   - 验证返回 -1，提示"余额查询失败"

## 部署说明

### 部署方式

使用项目提供的部署脚本：

**方式一：只更新 HTML/JS 文件（热更新，推荐）**
```bash
cd /Users/lwx/Workspace/waibao/qianliyan/qianliyan-backend
./deploy-icars-admin.sh WEB-INF/view/biz/accid/accid.html
./deploy-icars-admin.sh static/modular/biz/accd/accid.js
```

**方式二：上传完整 jar（需先在 IDEA 打包）**
```bash
# 1. 在 IDEA: Build → Build Artifacts → icars-admin:jar
# 2. 执行部署脚本
./deploy-icars-admin.sh
```

### 部署后验证

1. 访问 `http://114.215.211.119:8078/wx-admin/`
2. 登录后进入【系统管理】→【事故上报】
3. 验证页面显示微信支付余额
4. 选择一条未审核事故进行审核测试

## 安全考虑

1. **权限控制**
   - 余额查询接口使用 `@Permission` 注解，只有登录用户可访问
   - 审核操作已有权限控制（`Const.ADMIN_NAME, Const.SUB_ADMIN_NAME`）

2. **数据敏感性**
   - 余额信息仅在后台管理系统显示，不对外暴露
   - API 调用使用微信官方 SDK，自动处理签名和加密

3. **防重复操作**
   - 原有逻辑已检查是否重复发放红包（`existBill != null`）
   - 余额校验在状态修改前执行，确保原子性

## 后续优化建议

1. **缓存优化**
   - 考虑增加余额缓存（如 Redis），减少 API 调用频率
   - 缓存时间建议 1-5 分钟，审核时强制刷新

2. **余额预警**
   - 当余额低于阈值（如 100 元）时，自动发送预警通知
   - 可通过企业微信、邮件等方式通知管理员

3. **余额变动记录**
   - 记录每次余额查询结果，便于追踪余额变化
   - 可用于对账和异常排查

4. **批量审核优化**
   - 如果需要批量审核功能，在批量操作前统一校验余额
   - 计算总金额 × 2，一次性校验是否充足
