# 多源小程序账号隔离设计

## 问题

不同 source（如 SSP、TTP）的小程序登录后共享同一个 `biz_wx_user` 账号，应为独立账号。

### 根因

1. 小程序 `app.js` 登录时不传 source，后端无法区分来源
2. 后端 `WxService.createWxSession()` 写死默认 appId/secret，不按 source 路由
3. `biz_wx_user` 表无 source 字段，`selectBizWxUser(openid)` 仅按 openid 查找
4. WxSession 缓存（Redis）不含 source 信息

### 目标

同一微信用户从不同 source 小程序登录后，得到不同的 `biz_wx_user` 记录（独立 userId、独立余额、独立数据）。

## 改动清单

### 1. 数据库：biz_wx_user 表加 source 列

```sql
ALTER TABLE biz_wx_user ADD COLUMN source VARCHAR(20) DEFAULT 'SSP' COMMENT '小程序来源标识';
-- 存量数据默认 SSP
UPDATE biz_wx_user SET source = 'SSP' WHERE source IS NULL;
```

不加 openid+source 唯一索引（不同小程序 appId 的 openid 本身就不同，加了反而可能影响存量数据）。

### 2. 小程序 app.js：登录时传 source

文件：`qianliyan-minipro/app.js` 第 133-135 行

```js
// 改前
data: { code: res.code }
// 改后
data: { code: res.code, source: config.source }
```

### 3. 后端 WxAuthController：接收 source 参数

文件：`WxAuthController.java` 的 `createWxSession` 方法

- 接收 `source` 参数（`@RequestParam`）
- 透传给 `WxService.createWxSession(wxCode, source)`
- 新建用户时 `bizWxUser.setSource(source)`
- 查找用户时改用 `selectBizWxUser(openid, source)`

### 4. 后端 WxService：按 source 路由 appId

文件：`WxService.java`

**createWxSession(wxCode, source)**：
- 用 `wxAuthProperties.getAppIdBySource(source)` 替代 `wxAuthProperties.getAppId()`
- 用 `wxAuthProperties.getSecretBySource(source)` 替代 `wxAuthProperties.getSecret()`

**create3rdSession(openId, sessionKey, expires, createSeconds, unionId, source)**：
- 查用户改为 `selectBizWxUser(openId, source)`
- WxSession 中增加 source 字段
- Redis key 加入 source 维度（可选，目前 key 是随机字符串已经唯一）

### 5. WxSession：增加 source 字段

文件：`WxSession.java`

- 增加 `private String source` 字段及 getter/setter
- 构造函数增加 source 参数
- 这样后续从 session 中可以获取 source，传给需要的方法

### 6. BizWxUser 模型：增加 source 字段

文件：`BizWxUser.java`

```java
private String source;
// getter/setter
```

### 7. Mapper 层：查询加入 source 条件

文件：`BizWxUserMapper.java`

```java
BizWxUser selectBizWxUser(@Param("openid") String openid, @Param("source") String source);
```

文件：`BizWxUserMapper.xml`

```xml
<select id="selectBizWxUser" resultType="...">
    select <include refid="Combine_Column_List" />
    from biz_wx_user a
    left join sys_user b on a.phone = b.phone and a.phone is not null and a.phone != '' and b.status != 3
    where a.openid = #{openid}
    <if test="source != null and source != ''">
        AND a.source = #{source}
    </if>
</select>
```

用 `<if>` 兼容：传了 source 就按 openid+source 查，不传则退回原逻辑。

### 8. Service 层：透传 source

文件：`IBizWxUserService.java` / `BizWxUserServiceImpl.java`

- `selectBizWxUser(String openid)` 保留（向后兼容）
- 新增 `selectBizWxUser(String openid, String source)`

同样，`setPhone`/`setAlipay` 等 update 操作通过 openid 更新，因为不同 source 的 openid 本身不同（不同 appId），所以 update 语句不需要加 source 条件。

### 9. 调用点改造

**需要改的（登录链路，涉及用户创建和 session）**：
- `WxAuthController.createWxSession` — 传 source
- `WxService.createWxSession` — 按 source 路由
- `WxService.create3rdSession` — 按 source 查用户

**不需要改的（通过 session 中已有 openid 查询）**：
- `WxAuthController.updateProfile` — 从 session 拿 openid 查用户，openid 已经是 source 特定的
- `WxAuthController.uploadAvatar` — 同上
- `WxAuthController.bindPhone` — 同上
- `WxRestController` 中各方法 — 同上
- `AccidentController`/`AppRestController` — 从 accident.getOpenid() 查，openid 已绑定 source

**关键洞察**：不同 appId 的小程序，微信返回的 openid 本身就不同。所以只要登录时正确按 source 路由 appId，后续按 openid 查询的代码自然隔离。source 字段主要用于：
1. 登录时正确路由到对应 appId/secret
2. 新建用户时标记来源
3. 后台管理查看用户来源

## 不改的部分

- c-web 的 `WxMiniLoginController` — 已经有 source 隔离，不动
- `biz_wx_user_gzh`（公众号用户表）— 不涉及
- `setPhone`/`setAlipay`/`incrementShareCount` 等 update SQL — openid 已经是 source 特定的，不需要加 source 条件

## 存量数据处理

- 现有 `biz_wx_user` 数据全部标记为 `source='SSP'`
- TTP 用户登录时会自动创建新记录（`source='TTP'`）
