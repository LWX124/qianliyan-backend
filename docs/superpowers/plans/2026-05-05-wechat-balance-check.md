# 微信支付余额显示与审核校验实施计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 在事故上报页面显示微信支付余额，并在审核通过前校验余额是否充足（余额 ≥ 红包金额 × 2），余额不足时阻止审核并提示管理员。

**Architecture:** 后端通过微信支付 V3 API 查询商户账户余额，提供余额查询接口和审核前余额校验。前端在页面加载和搜索时显示余额，审核时由后端自动校验并返回错误提示。

**Tech Stack:** Java (Spring Boot), 微信支付 V3 SDK, JavaScript (jQuery), Bootstrap Table

---

## 文件结构

### 后端文件
- **Modify:** `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/wxpay/WxPayV3TransferService.java`
  - 新增 `queryMerchantBalance()` 方法查询商户余额

- **Modify:** `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/controller/AccidentController.java`
  - 新增 `queryBalance()` 接口返回余额
  - 增强 `checkSuccess()` 方法增加余额校验

### 前端文件
- **Modify:** `icars-master/icars-master/icars-admin/src/main/webapp/WEB-INF/view/biz/accid/accid.html`
  - 新增余额显示区域

- **Modify:** `icars-master/icars-master/icars-admin/src/main/webapp/static/modular/biz/accd/accid.js`
  - 新增 `MgrAccd.queryBalance()` 函数
  - 页面加载和搜索时调用余额查询

---

## Task 1: 实现微信支付余额查询服务

**Files:**
- Modify: `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/wxpay/WxPayV3TransferService.java:147`

- [ ] **Step 1: 在 WxPayV3TransferService 类末尾添加余额查询方法**

在第 147 行（类的最后一个方法后）添加：

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

- [ ] **Step 2: 验证代码编译通过**

在 IDEA 中检查是否有编译错误，确保：
- 所有导入的类都存在
- 方法签名正确
- 日志语句格式正确

Expected: 无编译错误

- [ ] **Step 3: 提交余额查询服务实现**

```bash
git add icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/wxpay/WxPayV3TransferService.java
git commit -m "feat: 新增微信支付商户余额查询方法"
```

---

## Task 2: 实现余额查询 Controller 接口

**Files:**
- Modify: `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/controller/AccidentController.java:399`

- [ ] **Step 1: 在 AccidentController 类末尾添加余额查询接口**

在第 399 行（`md5()` 方法后）添加：

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

- [ ] **Step 2: 验证代码编译通过**

在 IDEA 中检查是否有编译错误，确保：
- `wxPayV3TransferService` 已注入（第 80 行）
- `ErrorTip`、`BigDecimal`、`Map`、`HashMap` 已导入
- `@RequestMapping`、`@Permission`、`@ResponseBody` 注解正确

Expected: 无编译错误

- [ ] **Step 3: 提交余额查询接口**

```bash
git add icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/controller/AccidentController.java
git commit -m "feat: 新增余额查询接口 GET /accid/balance"
```

---

## Task 3: 增强审核方法增加余额校验

**Files:**
- Modify: `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/controller/AccidentController.java:305-308`

- [ ] **Step 1: 在 checkSuccess 方法开头增加余额校验逻辑**

在第 305 行 `public Tip checkSuccess(...)` 方法的参数校验后（第 308 行后）插入：

```java
// ========== 校验余额是否充足 ==========
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
```

修改后的方法开头应该是：

```java
public Tip checkSuccess(@RequestParam Integer accdId, @RequestParam BigDecimal amount,
                        @RequestParam(required = false) String reason) throws Exception {
    if (ToolUtil.isEmpty(accdId) || amount == null) {
        throw new GunsException(BizExceptionEnum.REQUEST_NULL);
    }

    // ========== 校验余额是否充足 ==========
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

- [ ] **Step 2: 验证代码编译通过**

在 IDEA 中检查是否有编译错误，确保：
- 余额校验逻辑插入位置正确
- 所有变量类型匹配
- 原有逻辑未被破坏

Expected: 无编译错误

- [ ] **Step 3: 提交审核方法增强**

```bash
git add icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/controller/AccidentController.java
git commit -m "feat: 审核通过前增加余额校验（余额需≥红包金额×2）"
```

---

## Task 4: 前端页面增加余额显示区域

**Files:**
- Modify: `icars-master/icars-master/icars-admin/src/main/webapp/WEB-INF/view/biz/accid/accid.html:60`

- [ ] **Step 1: 在红包合计下方增加余额显示区域**

在第 60 行（红包合计 `</div>` 后）插入：

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

修改后的结构应该是：

```html
<div class="col-sm-12">
    <div class="row">
        <div class="form-group">
            <label class="col-sm-2 control-label" style="height: 34px;line-height: 34px;">红包合计</label>
            <div class="col-sm-10">
                <input class="form-control" id="redpackSumDiv" name="redpackSumDiv" type="text" readonly="readonly" style="margin-left: -213px;border: none;text-align: left;background-color: rgb(255, 255, 255);color: red;" value="">
            </div>
        </div>
    </div>
</div>
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
<div class="hidden-xs" id="mgrAccdTableToolbar" role="group">
```

- [ ] **Step 2: 验证 HTML 语法正确**

检查：
- 标签闭合正确
- 样式属性格式正确
- ID 唯一（`balanceDiv`）

Expected: HTML 格式正确

- [ ] **Step 3: 提交前端页面修改**

```bash
git add icars-master/icars-master/icars-admin/src/main/webapp/WEB-INF/view/biz/accid/accid.html
git commit -m "feat: 事故上报页面增加微信支付余额显示区域"
```

---

## Task 5: 前端 JS 增加余额查询函数

**Files:**
- Modify: `icars-master/icars-master/icars-admin/src/main/webapp/static/modular/biz/accd/accid.js:367`

- [ ] **Step 1: 在 queryRedPackSum 函数后增加余额查询函数**

在第 367 行（`MgrAccd.queryRedPackSum` 函数的 `}` 后）插入：

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

- [ ] **Step 2: 验证 JS 语法正确**

检查：
- 函数定义格式正确
- 回调函数逻辑正确
- jQuery 选择器正确（`#balanceDiv`）

Expected: JS 语法正确

- [ ] **Step 3: 提交余额查询函数**

```bash
git add icars-master/icars-master/icars-admin/src/main/webapp/static/modular/biz/accd/accid.js
git commit -m "feat: 新增余额查询函数 MgrAccd.queryBalance()"
```

---

## Task 6: 页面加载时调用余额查询

**Files:**
- Modify: `icars-master/icars-master/icars-admin/src/main/webapp/static/modular/biz/accd/accid.js:384`

- [ ] **Step 1: 在页面初始化代码中增加余额查询调用**

在第 384 行（`MgrAccd.queryRedPackSum(MgrAccd.formParams());` 后）插入：

```javascript
MgrAccd.queryBalance(); // 页面加载时查询余额
```

修改后的代码应该是：

```javascript
MgrAccd.table = table.init();
MgrAccd.queryRedPackSum(MgrAccd.formParams());
MgrAccd.queryBalance(); // 页面加载时查询余额

// ===== 新记录自动刷新 & 高亮 =====
```

- [ ] **Step 2: 验证调用位置正确**

检查：
- 调用在 `table.init()` 之后
- 调用在页面初始化函数 `$(function() {...})` 内部

Expected: 调用位置正确

- [ ] **Step 3: 提交页面加载时余额查询**

```bash
git add icars-master/icars-master/icars-admin/src/main/webapp/static/modular/biz/accd/accid.js
git commit -m "feat: 页面加载时自动查询微信支付余额"
```

---

## Task 7: 搜索时刷新余额

**Files:**
- Modify: `icars-master/icars-master/icars-admin/src/main/webapp/static/modular/biz/accd/accid.js:265`

- [ ] **Step 1: 在搜索函数中增加余额查询调用**

在第 265 行（`MgrAccd.queryRedPackSum(queryData);` 后）插入：

```javascript
MgrAccd.queryBalance(); // 搜索时刷新余额
```

修改后的 `MgrAccd.search` 函数末尾应该是：

```javascript
MgrAccd.search = function () {
    var queryData = {};

    queryData['openid'] = $("#openid").val();
    queryData['name'] = $("#name").val();
    var createTimeRange = $("#createStartTime").val();
    if(createTimeRange){
        queryData['createStartTime'] = createTimeRange.split('~')[0];
        queryData['createEndTime'] = createTimeRange.split('~')[1];
        if((new Date(queryData['createEndTime']) - new Date(queryData['createStartTime']))/1000/60/60/24 > 7){
            Feng.error("创建时间查询范围不能大于7天!");
            return ;
        }
    }else {
        queryData['createStartTime'] = null;
        queryData['createEndTime'] = null;
    }
    var checkTimeRange = $("#checkStartTime").val();
    if(checkTimeRange){
        queryData['checkStartTime'] = checkTimeRange.split('~')[0];
        queryData['checkEndTime'] = checkTimeRange.split('~')[1];
    }else {
        queryData['checkStartTime'] = null;
        queryData['checkEndTime'] = null;
    }

    queryData['checkStatus'] = $("#checkStatus").val();
    queryData['pushStatus'] = $("#pushStatus").val();
    MgrAccd.table.refresh({query: queryData});

    MgrAccd.queryRedPackSum(queryData);
    MgrAccd.queryBalance(); // 搜索时刷新余额
}
```

- [ ] **Step 2: 验证调用位置正确**

检查：
- 调用在 `MgrAccd.queryRedPackSum(queryData);` 之后
- 调用在 `MgrAccd.search` 函数内部

Expected: 调用位置正确

- [ ] **Step 3: 提交搜索时余额刷新**

```bash
git add icars-master/icars-master/icars-admin/src/main/webapp/static/modular/biz/accd/accid.js
git commit -m "feat: 搜索时自动刷新微信支付余额"
```

---

## Task 8: 本地验证与测试

**Files:**
- Test: 本地启动应用并测试功能

- [ ] **Step 1: 在 IDEA 中打包项目**

在 IDEA 中：
1. 打开 Maven 面板
2. 找到 `icars-admin` 模块
3. 执行 `clean` 然后 `package`

Expected: 打包成功，生成 `target/icars-admin-1.0.0.jar`

- [ ] **Step 2: 本地启动应用（可选）**

如果有本地测试环境：

```bash
cd icars-master/icars-master/icars-admin
java -jar target/icars-admin-1.0.0.jar --spring.profiles.active=local
```

Expected: 应用启动成功

- [ ] **Step 3: 验证编译无错误**

检查 IDEA 控制台输出：
- 无编译错误
- 无明显的运行时错误

Expected: 编译和启动无错误

- [ ] **Step 4: 提交验证记录**

```bash
git add -A
git commit -m "chore: 本地验证编译通过" --allow-empty
```

---

## Task 9: 更新 HTML 版本号强制刷新缓存

**Files:**
- Modify: `icars-master/icars-master/icars-admin/src/main/webapp/WEB-INF/view/biz/accid/accid.html:108`

- [ ] **Step 1: 更新 JS 文件引用的版本号**

将第 108 行的版本号从 `v=20260424` 更新为当前日期：

```html
<script src="${ctxPath}/static/modular/biz/accd/accid.js?v=20260505"></script>
```

- [ ] **Step 2: 验证版本号格式正确**

检查：
- 版本号格式为 `YYYYMMDD`
- 使用当前日期（2026-05-05 → 20260505）

Expected: 版本号格式正确

- [ ] **Step 3: 提交版本号更新**

```bash
git add icars-master/icars-master/icars-admin/src/main/webapp/WEB-INF/view/biz/accid/accid.html
git commit -m "chore: 更新 JS 版本号强制刷新浏览器缓存"
```

---

## Task 10: 最终代码审查与提交

**Files:**
- Review: 所有修改的文件

- [ ] **Step 1: 审查所有修改**

运行 git diff 查看所有改动：

```bash
git diff HEAD~9 HEAD
```

检查：
- 所有代码符合项目规范
- 没有遗漏的文件
- 没有调试代码或注释
- 提交信息清晰

Expected: 所有改动符合预期

- [ ] **Step 2: 查看提交历史**

```bash
git log --oneline -10
```

Expected: 看到 9 个功能提交，提交信息清晰

- [ ] **Step 3: 创建功能完成标记提交**

```bash
git commit --allow-empty -m "feat: 完成微信支付余额显示与审核校验功能

- 新增微信支付 V3 余额查询 API 集成
- 事故上报页面显示实时余额
- 审核通过前校验余额充足性（余额≥红包金额×2）
- 余额不足时阻止审核并提示管理员

相关文件：
- WxPayV3TransferService.java: 新增 queryMerchantBalance()
- AccidentController.java: 新增 /balance 接口，增强 checkSuccess()
- accid.html: 新增余额显示区域
- accid.js: 新增 queryBalance() 函数及调用"
```

---

## 部署说明

完成所有任务后，使用以下命令部署到服务器：

### 方式一：热更新 HTML/JS（推荐，快速）

```bash
cd /Users/lwx/Workspace/waibao/qianliyan/qianliyan-backend
./deploy-icars-admin.sh WEB-INF/view/biz/accid/accid.html
./deploy-icars-admin.sh static/modular/biz/accd/accid.js
```

### 方式二：完整 jar 部署（需要重启）

```bash
# 1. 在 IDEA: Build → Build Artifacts → icars-admin:jar
# 2. 执行部署脚本
cd /Users/lwx/Workspace/waibao/qianliyan/qianliyan-backend
./deploy-icars-admin.sh
```

### 部署后验证

1. 访问 `http://114.215.211.119:8078/wx-admin/`
2. 登录后进入【系统管理】→【事故上报】
3. 验证页面显示"微信支付余额"
4. 验证余额数值正确显示
5. 选择一条未审核事故，点击"审核通过"
6. 选择红包金额，点击"确认审核"
7. 如果余额充足，验证审核成功
8. 如果余额不足，验证提示"微信支付余额不足！当前余额：XX 元，需要：YY 元，请及时充值"

---

## 测试场景

### 场景 1：余额充足
- 前置条件：微信支付账户余额 ≥ 红包金额 × 2
- 操作：审核通过并发放 5 元红包
- 预期：审核成功，红包发放成功

### 场景 2：余额不足
- 前置条件：微信支付账户余额 < 红包金额 × 2
- 操作：审核通过并发放 5 元红包
- 预期：提示"微信支付余额不足！当前余额：8.00 元，需要：10.00 元，请及时充值"，审核失败

### 场景 3：余额查询失败
- 前置条件：微信 API 不可用或网络异常
- 操作：打开事故上报页面
- 预期：余额显示"查询失败"

### 场景 4：审核时余额查询失败
- 前置条件：微信 API 不可用或网络异常
- 操作：审核通过
- 预期：提示"余额查询失败，无法完成审核，请稍后重试"

---

## 回滚方案

如果部署后发现问题，可以快速回滚：

```bash
# 回滚到上一个版本
ssh root@114.215.211.119 "cd /opt/amiba && ls -lt jars/icars-admin.jar.* | head -1"
# 找到备份文件，例如 icars-admin.jar.20260505_103045
ssh root@114.215.211.119 "cd /opt/amiba/jars && cp icars-admin.jar.20260505_103045 icars-admin.jar"
# 重启容器
ssh root@114.215.211.119 "cd /opt/amiba && docker-compose -f docker-compose.server.yml restart icars-admin"
```
