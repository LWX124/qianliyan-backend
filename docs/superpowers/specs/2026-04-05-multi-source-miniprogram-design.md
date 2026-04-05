# 多小程序来源识别系统设计

## 背景

当前后端服务需要同时对接4个微信小程序。这4个小程序功能相同但UI有所区别，各自拥有独立的微信appId和独立的用户体系。后端需要识别请求来自哪个小程序，以便：
- 登录时使用正确的appId调用微信API
- 用户注册时记录来源
- 审核时按来源分配审核人员

## 现有基础

- 小程序端（qianliyan-minipro）已在 `config/index.js` 定义 `source: 'qianliyan'`
- `request.js` 每次请求已携带 `X-Source` header
- 后端目前未读取和使用该 header
- `WxMaConfiguration` 已支持 `setMultiConfigs` 多appId配置，但当前只配了一套

## Source 定义

使用字符串枚举标识各小程序来源：

| Source | 小程序 | appId | 状态 |
|--------|--------|-------|------|
| `SSP` | 千里眼 (qianliyan-minipro) | `wx5d88d6c7c216e1f3` | 已有 |
| `TBD_2` | 小程序2 | 待定 | 预留 |
| `TBD_3` | 小程序3 | 待定 | 预留 |
| `TBD_4` | 小程序4 | 待定 | 预留 |

预留的 source 值后续确定后直接修改枚举即可。

## 架构设计

### 请求链路

```
小程序 → X-Source header → SourceInterceptor → SourceContext(ThreadLocal) → Controller/Service
```

### 1. SourceContext — ThreadLocal 上下文

新增 `SourceContext.java`，使用 ThreadLocal 在请求链路中传递 source：

```java
public class SourceContext {
    private static final ThreadLocal<String> CURRENT_SOURCE = new ThreadLocal<>();

    public static void setSource(String source) { CURRENT_SOURCE.set(source); }
    public static String getSource() { return CURRENT_SOURCE.get(); }
    public static void clear() { CURRENT_SOURCE.remove(); }
}
```

位置：`c-web/web/src/main/java/com/cheji/web/context/SourceContext.java`

### 2. SourceEnum — 来源枚举

新增 `SourceEnum.java`，定义合法的 source 值：

```java
public enum SourceEnum {
    SSP("SSP", "千里眼"),
    TBD_2("TBD_2", "小程序2"),
    TBD_3("TBD_3", "小程序3"),
    TBD_4("TBD_4", "小程序4");

    private final String code;
    private final String name;
    // constructor, getters, static fromCode(String) method
}
```

位置：`c-web/web/src/main/java/com/cheji/web/constant/SourceEnum.java`

### 3. SourceInterceptor — 请求拦截器

新增 Spring `HandlerInterceptor`：

- `preHandle`：从 `request.getHeader("X-Source")` 读取 source
- 校验 source 是否在 `SourceEnum` 中，不合法则返回 400
- 合法则调用 `SourceContext.setSource(source)`
- `afterCompletion`：调用 `SourceContext.clear()` 防止内存泄漏

位置：`c-web/web/src/main/java/com/cheji/web/interceptor/SourceInterceptor.java`

需要在 WebMvcConfigurer 中注册该拦截器，拦截所有路径 `/**`，排除登录接口 `/user/wxMiniLogin`（登录接口从 body 中读取 source 并在 controller 内部处理）。当前项目 controller 没有统一的路径前缀，各 controller 使用独立的 RequestMapping（如 `/user`、`/indent`、`/file` 等）。

### 4. 微信配置多套路由

**WxMaProperties.Config** 增加 `source` 字段：

```java
@Data
public static class Config {
    private String appid;
    private String secret;
    private String token;
    private String aesKey;
    private String msgDataFormat;
    private String source;  // 新增：对应的小程序来源标识
}
```

**application-pro.yml** 配置多套：

```yaml
wx:
  miniapp:
    configs:
      - appid: wx5d88d6c7c216e1f3
        secret: xxx
        source: SSP
      - appid: wxXXXXXXXXXXXXXXXX  # 预留
        secret: xxx
        source: TBD_2
```

**WxMaConfiguration** 改造：
- 保留现有 `setMultiConfigs` 逻辑（按 appId 索引）
- 新增 `Map<String, String> sourceToAppIdMap` Bean，建立 source → appId 映射
- 提供 `getAppIdBySource(String source)` 方法

### 5. 登录流程改造

**WxMiniLoginController.wxMiniLogin** 改造：

1. 从请求 body 中已有的 `source` 字段（第50行已读取但未使用）获取来源
2. 通过 `sourceToAppIdMap` 获取对应的 appId
3. 调用 `wxMaService.switchoverTo(appId)` 切换到对应配置
4. 再调用 `getSessionInfo(code)` 换取 openId
5. 用户注册时将 source 写入 `AppUserEntity.source`
6. 将 source 写入 `TokenPojo.source`，存入 Redis

### 6. Token 增加 source

**TokenPojo** 增加 `source` 字段：

```java
public class TokenPojo {
    private String thirdSessionKey;
    private String unionId;
    private AppUserEntity appUserEntity;
    private String source;  // 新增
    // getters/setters
}
```

登录时写入 source，后续 `BaseController.getCurrentLoginUser` 解析时也能获取到 source。这样即使某些场景 header 丢失，也能从 token 中恢复 source。

### 7. 数据库变更

**app_user 表：**
```sql
ALTER TABLE app_user ADD COLUMN source VARCHAR(20) DEFAULT NULL COMMENT '小程序来源标识';
CREATE INDEX idx_app_user_source ON app_user(source);
```

**app_b_user 表：**
```sql
ALTER TABLE app_b_user ADD COLUMN source VARCHAR(20) DEFAULT NULL COMMENT '小程序来源标识';
CREATE INDEX idx_app_b_user_source ON app_b_user(source);
```

对应实体类 `AppUserEntity` 和 `AppBUser` 增加 `source` 字段及 getter/setter。

存量数据处理：现有用户全部来自千里眼小程序，执行：
```sql
UPDATE app_user SET source = 'SSP' WHERE source IS NULL;
UPDATE app_b_user SET source = 'SSP' WHERE source IS NULL;
```

### 8. 审核系统按来源分配

审核查询接口增加 source 参数过滤：
- 后台管理系统（jeesite_new）的审核列表接口增加 `source` 查询参数
- 审核人员可按 source 筛选待审核数据
- 审核分配规则：后台配置 source → 审核人员 的映射（可在系统配置表中维护，或初期直接在代码中硬编码映射关系）

### 9. 小程序端改动

**qianliyan-minipro/config/index.js：**
```javascript
const config = {
  baseUrl: 'http://114.215.211.119/wx-admin',
  source: 'SSP',  // 从 'qianliyan' 改为 'SSP'
  appId: 'wx5d88d6c7c216e1f3',
  subscribeTemplateId: '8_0YosP9YlA9Y-H8EWBD7hz6NVC81u3eZezVAw_JxYs'
}
```

`request.js` 无需改动，已通过 `config.source` 自动携带。

其他3个小程序复制相同的 request.js 机制，各自配置不同的 source 值。

## 改动文件清单

### 后端新增
| 文件 | 说明 |
|------|------|
| `c-web/web/.../context/SourceContext.java` | ThreadLocal 上下文 |
| `c-web/web/.../constant/SourceEnum.java` | 来源枚举 |
| `c-web/web/.../interceptor/SourceInterceptor.java` | 请求拦截器 |

### 后端修改
| 文件 | 改动 |
|------|------|
| `WxMaProperties.java` | Config 增加 source 字段 |
| `WxMaConfiguration.java` | 构建 source→appId 映射 |
| `WxMiniLoginController.java` | 按 source 选 appId，注册写入 source |
| `TokenPojo.java` | 增加 source 字段 |
| `BaseController.java` | 解析 token 中的 source |
| `AppUserEntity.java` | 增加 source 字段 |
| `AppBUser.java` | 增加 source 字段 |
| `application-pro.yml` | 多套微信 miniapp 配置 |
| `application-test.yml` | 多套微信 miniapp 配置 |
| 审核相关 Controller | 增加 source 过滤参数 |
| WebMvcConfigurer | 注册 SourceInterceptor |

### 小程序修改
| 文件 | 改动 |
|------|------|
| `qianliyan-minipro/config/index.js` | source 改为 `'SSP'` |

### 数据库
| 操作 | 说明 |
|------|------|
| ALTER TABLE app_user | 增加 source 字段 + 索引 |
| ALTER TABLE app_b_user | 增加 source 字段 + 索引 |
| UPDATE 存量数据 | 设为 'SSP' |
