# 多源小程序账号隔离 实施计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 不同 source 小程序登录后产生独立的 biz_wx_user 账号，实现用户数据完全隔离。

**Architecture:** 在登录链路中传入 source 参数，后端按 source 路由到对应的微信 appId/secret 换取 openid，biz_wx_user 表增加 source 字段标记来源。不同 appId 的 openid 天然不同，所以只需改登录链路，后续业务代码按 openid 查询自然隔离。

**Tech Stack:** Java/Spring Boot (icars-admin), MyBatis-Plus, Redis, 微信小程序原生框架

---

### Task 1: BizWxUser 模型增加 source 字段

**Files:**
- Modify: `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/model/BizWxUser.java:79` (在 version 字段后添加)

- [ ] **Step 1: 在 BizWxUser.java 的 version 字段后添加 source 字段**

在 `private Integer version;` (第79行) 之后添加：

```java
    /**
     * 小程序来源标识（SSP、TTP 等）
     */
    private String source;
```

- [ ] **Step 2: 添加 getter/setter**

在 `setVersion` 方法之后（约第271行），添加：

```java
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
```

- [ ] **Step 3: 更新 toString 方法**

在 `toString()` 方法中，在 `", version=" + version` 后面添加：

```java
                ", source='" + source + '\'' +
```

- [ ] **Step 4: Commit**

```bash
git add icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/model/BizWxUser.java
git commit -m "feat: BizWxUser 模型增加 source 字段"
```

---

### Task 2: Mapper 层支持 source 查询

**Files:**
- Modify: `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/dao/BizWxUserMapper.java:36`
- Modify: `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/dao/mapping/BizWxUserMapper.xml:27-50`

- [ ] **Step 1: BizWxUserMapper.java — 修改 selectBizWxUser 方法签名**

将第36行：

```java
    BizWxUser selectBizWxUser(@Param("openid") String openid);
```

改为：

```java
    BizWxUser selectBizWxUser(@Param("openid") String openid, @Param("source") String source);
```

- [ ] **Step 2: BizWxUserMapper.xml — Combine_Column_List 加入 source**

将第28行的 `Combine_Column_List` 中，在 `a.version,` 后面加入 `a.source,`：

```xml
    <sql id="Combine_Column_List">
        a.id, a.openid,a.unionid,a.third_openid as thirdOpenid, a.type, a.alipay_account as alipayAccount, a.extensionAccount, a.bindAccount,a.headImg, a.wxname,a.phone, a.createTime, a.version, a.source,b.bankcard,b.bankUserName, b.bankName, b.bankSecondName, b.idcard, (CASE WHEN b.id is null THEN 0 WHEN b.status = 4 THEN 3 WHEN b.status = 5 THEN 2 ELSE 1 END) as userType, b.account
    </sql>
```

- [ ] **Step 3: BizWxUserMapper.xml — selectBizWxUser 加入 source 条件**

将第43-50行改为：

```xml
    <select id="selectBizWxUser" resultType="com.stylefeng.guns.modular.system.model.BizWxUser">
        select
        <include refid="Combine_Column_List" />
        from biz_wx_user a
        left join sys_user b on a.phone = b.phone and a.phone is not null and a.phone != '' and b.status != 3
        where
        a.openid = #{openid}
        <if test="source != null and source != ''">
            AND a.source = #{source}
        </if>
    </select>
```

- [ ] **Step 4: Commit**

```bash
git add icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/dao/BizWxUserMapper.java
git add icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/dao/mapping/BizWxUserMapper.xml
git commit -m "feat: Mapper 层 selectBizWxUser 支持 source 参数"
```

---

### Task 3: Service 层透传 source

**Files:**
- Modify: `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/service/IBizWxUserService.java:25`
- Modify: `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/service/impl/BizWxUserServiceImpl.java:50-53`

- [ ] **Step 1: IBizWxUserService.java — 修改接口方法签名**

将第25行：

```java
    BizWxUser selectBizWxUser(String openid);
```

改为：

```java
    BizWxUser selectBizWxUser(String openid, String source);
```

- [ ] **Step 2: BizWxUserServiceImpl.java — 修改实现**

将第51-52行：

```java
    public BizWxUser selectBizWxUser(String openid) {
        return this.baseMapper.selectBizWxUser(openid);
    }
```

改为：

```java
    public BizWxUser selectBizWxUser(String openid, String source) {
        return this.baseMapper.selectBizWxUser(openid, source);
    }
```

- [ ] **Step 3: Commit**

```bash
git add icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/service/IBizWxUserService.java
git add icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/service/impl/BizWxUserServiceImpl.java
git commit -m "feat: Service 层 selectBizWxUser 透传 source"
```

---

### Task 4: WxSession 增加 source 字段

**Files:**
- Modify: `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/constant/WxSession.java`

- [ ] **Step 1: 添加 source 字段**

在第20行 `private User user;` 之后添加：

```java
    private String source;
```

- [ ] **Step 2: 添加 getter/setter**

在 `setUser` 方法（第66-68行）之后添加：

```java
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
```

- [ ] **Step 3: 修改构造函数，增加 source 参数**

将第70-78行的构造函数：

```java
    public WxSession(String openId, String wxSessionKey, long expires, long createSeconds, BizWxUser bizWxUser ,User user,String unionId) {
        this.openId = openId;
        this.wxSessionKey = wxSessionKey;
        this.expires = expires;
        this.createSeconds = createSeconds;
        this.unionId = unionId;
        this.bizWxUser = bizWxUser;
        this.user = user;
    }
```

改为：

```java
    public WxSession(String openId, String wxSessionKey, long expires, long createSeconds, BizWxUser bizWxUser, User user, String unionId, String source) {
        this.openId = openId;
        this.wxSessionKey = wxSessionKey;
        this.expires = expires;
        this.createSeconds = createSeconds;
        this.unionId = unionId;
        this.bizWxUser = bizWxUser;
        this.user = user;
        this.source = source;
    }
```

- [ ] **Step 4: Commit**

```bash
git add icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/constant/WxSession.java
git commit -m "feat: WxSession 增加 source 字段"
```

---

### Task 5: WxService — 按 source 路由 appId 并透传 source

**Files:**
- Modify: `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/service/impl/WxService.java:79-93,141-158,95-130`

- [ ] **Step 1: createWxSession 方法增加 source 参数，按 source 路由 appId**

将第79-93行：

```java
    public Map<String, Object> createWxSession(String wxCode) {
        StringBuffer sb = new StringBuffer();
        sb.append("appid=").append(wxAuthProperties.getAppId());
        sb.append("&secret=").append(wxAuthProperties.getSecret());
        sb.append("&js_code=").append(wxCode);
        sb.append("&grant_type=").append(wxAuthProperties.getGrantType());
        log.info("HTTP GET:");
        log.info("URL = " + wxAuthProperties.getSessionHost() + ", param : " + sb.toString());
        String res = HttpRequest.sendGet(wxAuthProperties.getSessionHost(), sb.toString());
        log.info("RESPONSE =" + res);
        if (res == null || res.equals("")) {
            return null;
        }
        return JSON.parseObject(res, Map.class);
    }
```

改为：

```java
    public Map<String, Object> createWxSession(String wxCode, String source) {
        StringBuffer sb = new StringBuffer();
        sb.append("appid=").append(wxAuthProperties.getAppIdBySource(source));
        sb.append("&secret=").append(wxAuthProperties.getSecretBySource(source));
        sb.append("&js_code=").append(wxCode);
        sb.append("&grant_type=").append(wxAuthProperties.getGrantType());
        log.info("HTTP GET: source={}", source);
        log.info("URL = " + wxAuthProperties.getSessionHost() + ", param : " + sb.toString());
        String res = HttpRequest.sendGet(wxAuthProperties.getSessionHost(), sb.toString());
        log.info("RESPONSE =" + res);
        if (res == null || res.equals("")) {
            return null;
        }
        return JSON.parseObject(res, Map.class);
    }
```

- [ ] **Step 2: create3rdSession 方法增加 source 参数**

将第141-158行：

```java
    public String create3rdSession(String wxOpenId, String wxSessionKey, Long expires, Long createSeconds, String unionId) {
        String thirdSessionKey = RandomStringUtils.randomAlphanumeric(64);
        BizWxUser bizWxUser = bizWxUserService.selectBizWxUser(wxOpenId);
        if (bizWxUser == null) {
            log.error("创建小程序登录信息 bizWxUser==null wxOpenId={}", wxOpenId);
        }
        User user = null;
        if (bizWxUser != null && StringUtils.isNotEmpty(bizWxUser.getPhone())) {
            List<User> users = userService.getByPhone(bizWxUser.getPhone());
            user = (users == null || users.size() != 1) ? null : users.get(0);
        }
        if (user == null) {
            log.error("缓存微信openId和session_key user为null   wxOpenId={}", wxOpenId);
        }
        String s = JSONObject.toJSONString(new WxSession(wxOpenId, wxSessionKey, expires, createSeconds, bizWxUser, user, unionId));
        jedisUtil.set("XCX_LOGIN_" + thirdSessionKey, s, 60 * 60);
        return thirdSessionKey;
    }
```

改为：

```java
    public String create3rdSession(String wxOpenId, String wxSessionKey, Long expires, Long createSeconds, String unionId, String source) {
        String thirdSessionKey = RandomStringUtils.randomAlphanumeric(64);
        BizWxUser bizWxUser = bizWxUserService.selectBizWxUser(wxOpenId, source);
        if (bizWxUser == null) {
            log.error("创建小程序登录信息 bizWxUser==null wxOpenId={}, source={}", wxOpenId, source);
        }
        User user = null;
        if (bizWxUser != null && StringUtils.isNotEmpty(bizWxUser.getPhone())) {
            List<User> users = userService.getByPhone(bizWxUser.getPhone());
            user = (users == null || users.size() != 1) ? null : users.get(0);
        }
        if (user == null) {
            log.error("缓存微信openId和session_key user为null   wxOpenId={}", wxOpenId);
        }
        String s = JSONObject.toJSONString(new WxSession(wxOpenId, wxSessionKey, expires, createSeconds, bizWxUser, user, unionId, source));
        jedisUtil.set("XCX_LOGIN_" + thirdSessionKey, s, 60 * 60);
        return thirdSessionKey;
    }
```

- [ ] **Step 3: getWxSession 方法解析 source 字段**

在 `getWxSession` 方法中（约第107行 `Long createSeconds = ...` 之后），添加一行读取 source：

```java
        String source = jsonObject.getString("source");
```

然后将第121行的 WxSession 构造：

```java
        WxSession wxSession = new WxSession(openId, wxSessionKey, expires, createSeconds, bizWxUserBean, user, unionId);
```

改为：

```java
        WxSession wxSession = new WxSession(openId, wxSessionKey, expires, createSeconds, bizWxUserBean, user, unionId, source);
```

- [ ] **Step 4: getMyMessage 方法传 null source（向后兼容）**

将第374行：

```java
        BizWxUser bizWxUser = bizWxUserService.selectBizWxUser(openId);
```

改为：

```java
        BizWxUser bizWxUser = bizWxUserService.selectBizWxUser(openId, null);
```

- [ ] **Step 5: Commit**

```bash
git add icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/service/impl/WxService.java
git commit -m "feat: WxService 按 source 路由 appId 并透传 source"
```

---

### Task 6: WxAuthController — 登录接口接收 source

**Files:**
- Modify: `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/controller/WxAuthController.java:78-124`

- [ ] **Step 1: createWxSession 方法增加 source 参数并改造全部登录逻辑**

将第77-124行：

```java
    @RequestMapping(value = "/api/v1/wx/getSession", method = RequestMethod.GET, produces = "application/json")
    public Map<String, Object> createWxSession(@RequestParam(required = true, value = "code") String wxCode) {
        long createSeconds = System.currentTimeMillis() / 1000;
        Map<String, Object> wxSessionMap = wxService.createWxSession(wxCode);

        if (null == wxSessionMap) {
            return rtnParam(50010, null);
        }
        //获取异常
        if (wxSessionMap.containsKey("errcode")) {
            return rtnParam(50020, null);
        }
        String wxOpenId = (String) wxSessionMap.get("openid");
        String wxSessionKey = (String) wxSessionMap.get("session_key");
        String unionId = (String) wxSessionMap.get("unionid");
        Long expires = 60 * 60L;
        if (StringUtils.isEmpty(wxOpenId)) {
            log.error("/api/v1/wx/getSession ### wxOpenId={};wxSessionKey={}", wxOpenId, wxSessionKey);
        }
        String thirdSession = wxService.create3rdSession(wxOpenId, wxSessionKey, expires, createSeconds, unionId);
        BizWxUser var1 = wxService.getWxSession(thirdSession).getBizWxUser();
        if (var1 == null) {
            //缓存内用户信息不在，查找数据库
            BizWxUser bizWxUser1 = bizWxUserService.selectBizWxUser(wxOpenId);
            if (bizWxUser1 == null) {
                BizWxUser bizWxUser = new BizWxUser();
                bizWxUser.setOpenid(wxOpenId);
                bizWxUser.setUnionId(unionId);
                bizWxUser.setType(1);
                bizWxUser.setCreateTime(new Date());
                bizWxUserService.insert(bizWxUser);
                var1 = bizWxUser;
            } else {
                var1 = bizWxUser1;
            }
        }
        // 如果已有用户但 unionId 为空，补写 unionId
        if (var1 != null && StringUtils.isNotEmpty(unionId) && StringUtils.isEmpty(var1.getUnionId())) {
            var1.setUnionId(unionId);
            bizWxUserService.updateById(var1);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("sessionId", thirdSession);
        data.put("userId", var1 != null ? var1.getId() : 0);
        data.put("headImg", var1 != null ? var1.getHeadImg() : "");
        data.put("wxname", var1 != null ? var1.getWxname() : "");
        return rtnParam(0, data);
    }
```

改为：

```java
    @RequestMapping(value = "/api/v1/wx/getSession", method = RequestMethod.GET, produces = "application/json")
    public Map<String, Object> createWxSession(@RequestParam(required = true, value = "code") String wxCode,
                                                @RequestParam(required = false, value = "source") String source) {
        log.info("/api/v1/wx/getSession source={}", source);
        long createSeconds = System.currentTimeMillis() / 1000;
        Map<String, Object> wxSessionMap = wxService.createWxSession(wxCode, source);

        if (null == wxSessionMap) {
            return rtnParam(50010, null);
        }
        //获取异常
        if (wxSessionMap.containsKey("errcode")) {
            return rtnParam(50020, null);
        }
        String wxOpenId = (String) wxSessionMap.get("openid");
        String wxSessionKey = (String) wxSessionMap.get("session_key");
        String unionId = (String) wxSessionMap.get("unionid");
        Long expires = 60 * 60L;
        if (StringUtils.isEmpty(wxOpenId)) {
            log.error("/api/v1/wx/getSession ### wxOpenId={};wxSessionKey={}", wxOpenId, wxSessionKey);
        }
        String thirdSession = wxService.create3rdSession(wxOpenId, wxSessionKey, expires, createSeconds, unionId, source);
        BizWxUser var1 = wxService.getWxSession(thirdSession).getBizWxUser();
        if (var1 == null) {
            //缓存内用户信息不在，查找数据库
            BizWxUser bizWxUser1 = bizWxUserService.selectBizWxUser(wxOpenId, source);
            if (bizWxUser1 == null) {
                BizWxUser bizWxUser = new BizWxUser();
                bizWxUser.setOpenid(wxOpenId);
                bizWxUser.setUnionId(unionId);
                bizWxUser.setType(1);
                bizWxUser.setSource(source);
                bizWxUser.setCreateTime(new Date());
                bizWxUserService.insert(bizWxUser);
                var1 = bizWxUser;
            } else {
                var1 = bizWxUser1;
            }
        }
        // 如果已有用户但 unionId 为空，补写 unionId
        if (var1 != null && StringUtils.isNotEmpty(unionId) && StringUtils.isEmpty(var1.getUnionId())) {
            var1.setUnionId(unionId);
            bizWxUserService.updateById(var1);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("sessionId", thirdSession);
        data.put("userId", var1 != null ? var1.getId() : 0);
        data.put("headImg", var1 != null ? var1.getHeadImg() : "");
        data.put("wxname", var1 != null ? var1.getWxname() : "");
        return rtnParam(0, data);
    }
```

- [ ] **Step 2: Commit**

```bash
git add icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/controller/WxAuthController.java
git commit -m "feat: WxAuthController 登录接口接收 source 参数"
```

---

### Task 7: 修复其他 selectBizWxUser 调用点（编译兼容）

**Files:**
- Modify: `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/controller/WxAuthController.java:141,193`
- Modify: `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/controller/WxRestController.java:182,384,502,602,643,697`
- Modify: `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/controller/AccidentController.java:344`
- Modify: `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/controller/appcontroller/AppRestController.java:2604`
- Modify: `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/controller/BizAlipayBillController.java:113`
- Modify: `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/wxpay/impl/WxPayBizService.java:93`

因为 `selectBizWxUser` 的签名从 `(String openid)` 改为了 `(String openid, String source)`，所有调用点都需要传第二个参数。这些调用点都是通过 openid 查用户的业务逻辑，openid 已经是 source 特定的（不同 appId 返回不同 openid），传 `null` 即可向后兼容。

- [ ] **Step 1: WxAuthController.java — updateProfile 和 uploadAvatar 方法**

第141行（updateProfile 方法内）：

```java
        BizWxUser bizWxUser = bizWxUserService.selectBizWxUser(wxSession.getOpenId());
```

改为：

```java
        BizWxUser bizWxUser = bizWxUserService.selectBizWxUser(wxSession.getOpenId(), null);
```

第193行（uploadAvatar 方法内）同样改法：

```java
            BizWxUser bizWxUser = bizWxUserService.selectBizWxUser(wxSession.getOpenId());
```

改为：

```java
            BizWxUser bizWxUser = bizWxUserService.selectBizWxUser(wxSession.getOpenId(), null);
```

- [ ] **Step 2: WxRestController.java — 6 处调用**

第182、384、502、602、643、697行，全部从：

```java
bizWxUserService.selectBizWxUser(wxSession.getOpenId())
```

改为：

```java
bizWxUserService.selectBizWxUser(wxSession.getOpenId(), null)
```

- [ ] **Step 3: AccidentController.java — 第344行**

```java
        BizWxUser bizWxUser = bizWxUserService.selectBizWxUser(accident.getOpenid());
```

改为：

```java
        BizWxUser bizWxUser = bizWxUserService.selectBizWxUser(accident.getOpenid(), null);
```

- [ ] **Step 4: AppRestController.java — 第2604行**

```java
        BizWxUser bizWxUser = bizWxUserService.selectBizWxUser(accident.getOpenid());
```

改为：

```java
        BizWxUser bizWxUser = bizWxUserService.selectBizWxUser(accident.getOpenid(), null);
```

- [ ] **Step 5: BizAlipayBillController.java — 第113行**

```java
            BizWxUser bizWxUser = bizWxUserService.selectBizWxUser(accident.getOpenid());
```

改为：

```java
            BizWxUser bizWxUser = bizWxUserService.selectBizWxUser(accident.getOpenid(), null);
```

- [ ] **Step 6: WxPayBizService.java — 第93行**

```java
        BizWxUser bizWxUser = bizWxUserService.selectBizWxUser(openid);
```

改为：

```java
        BizWxUser bizWxUser = bizWxUserService.selectBizWxUser(openid, null);
```

- [ ] **Step 7: Commit**

```bash
git add icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/controller/WxAuthController.java
git add icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/controller/WxRestController.java
git add icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/controller/AccidentController.java
git add icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/controller/appcontroller/AppRestController.java
git add icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/controller/BizAlipayBillController.java
git add icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/wxpay/impl/WxPayBizService.java
git commit -m "fix: 修复所有 selectBizWxUser 调用点，传 null source 向后兼容"
```

---

### Task 8: 小程序 app.js — 登录时传 source

**Files:**
- Modify: `/Users/lwx/Workspace/waibao/qianliyan/qianliyan-minipro/app.js:135`

- [ ] **Step 1: 修改登录请求，传入 source 参数**

将第133-135行：

```js
          url: config.baseUrl + '/api/v1/wx/getSession',
          method: 'GET',
          data: { code: res.code },
```

改为：

```js
          url: config.baseUrl + '/api/v1/wx/getSession',
          method: 'GET',
          data: { code: res.code, source: config.source },
```

- [ ] **Step 2: Commit（在 qianliyan-minipro 仓库中）**

```bash
cd /Users/lwx/Workspace/waibao/qianliyan/qianliyan-minipro
git add app.js
git commit -m "feat: 登录时传 source 参数给后端"
```

---

### Task 9: 数据库迁移 — biz_wx_user 表加 source 列

**Files:**
- Create: `docs/sql/2026-05-05-add-source-to-biz-wx-user.sql`

- [ ] **Step 1: 创建 SQL 迁移脚本**

```sql
-- 多源小程序账号隔离：biz_wx_user 表增加 source 字段
-- 存量数据默认标记为 SSP（拍事故小程序）

ALTER TABLE biz_wx_user ADD COLUMN source VARCHAR(20) DEFAULT 'SSP' COMMENT '小程序来源标识（SSP=拍事故, TTP=天天拍）';

UPDATE biz_wx_user SET source = 'SSP' WHERE source IS NULL;
```

- [ ] **Step 2: Commit**

```bash
git add docs/sql/2026-05-05-add-source-to-biz-wx-user.sql
git commit -m "feat: SQL 迁移脚本 - biz_wx_user 增加 source 字段"
```

---

### Task 10: 配置 TTP 源的 appId/secret

**Files:**
- Modify: `icars-master/icars-master/icars-admin/src/main/resources/application.properties` (或对应的 yml 文件)

- [ ] **Step 1: 确认配置文件中 TTP source 的配置**

在配置文件中添加 TTP 的 appId 和 secret（具体值需要用户提供）：

```properties
wx.sources.TTP.appId=TTP的微信小程序APPID
wx.sources.TTP.secret=TTP的微信小程序SECRET
wx.sources.TTP.templateId=TTP的订阅消息模板ID（可选）
```

注意：此步骤需要用户提供 TTP 小程序的实际 appId 和 secret。如果已经配置过，确认配置正确即可。

- [ ] **Step 2: Commit（如果有改动）**

```bash
git add icars-master/icars-master/icars-admin/src/main/resources/
git commit -m "feat: 配置 TTP 源的微信小程序 appId/secret"
```

---

### Task 11: 在远程数据库执行迁移脚本

**前置条件：** 用户确认后才执行（遵守部署铁律）。

- [ ] **Step 1: 在远程服务器上执行 SQL**

```bash
ssh root@114.215.211.119 "docker exec -i amiba-mysql mysql -u root -p'密码' 数据库名 < -" < docs/sql/2026-05-05-add-source-to-biz-wx-user.sql
```

或者手动登录执行：

```bash
ssh root@114.215.211.119
docker exec -it amiba-mysql mysql -u root -p
# 然后执行 SQL 脚本内容
```

- [ ] **Step 2: 验证列已添加**

```bash
ssh root@114.215.211.119 "docker exec -i amiba-mysql mysql -u root -p'密码' -e 'DESCRIBE biz_wx_user source' 数据库名"
```

预期输出包含 `source | varchar(20) | YES | | SSP`
