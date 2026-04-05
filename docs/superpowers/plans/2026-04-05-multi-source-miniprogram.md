# 多小程序来源识别系统 Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 让后端能识别请求来自哪个小程序，按 source 路由微信配置、记录用户来源、支持审核按来源筛选和分配。

**Architecture:** 小程序通过 `X-Source` header 传递来源标识，后端拦截器解析后存入 ThreadLocal，登录时按 source 选择对应的微信 appId 配置，用户和商家表持久化 source 字段。

**Tech Stack:** Spring Boot (JeeSite 4.1), MyBatis/MyBatis-Plus, weixin-java-miniapp 4.0.0, Redis, MySQL

---

## File Structure

### New Files
| File | Responsibility |
|------|---------------|
| `c-web/web/src/main/java/com/cheji/web/constant/SourceEnum.java` | Source 枚举定义 |
| `c-web/web/src/main/java/com/cheji/web/context/SourceContext.java` | ThreadLocal 上下文 |
| `c-web/web/src/main/java/com/cheji/web/interceptor/SourceInterceptor.java` | 解析 X-Source header |
| `c-web/web/src/main/java/com/cheji/web/config/WebMvcConfig.java` | 注册拦截器 |
| `sql/add_source_field.sql` | 数据库迁移脚本 |

### Modified Files
| File | Change |
|------|--------|
| `c-web/web/.../config/WxMaProperties.java` | Config 增加 source 字段 |
| `c-web/web/.../config/WxMaConfiguration.java` | 增加 sourceToAppIdMap Bean |
| `c-web/web/.../pojo/TokenPojo.java` | 增加 source 字段 |
| `c-web/web/.../domain/AppUserEntity.java` | 增加 source 字段 |
| `c-web/web/.../controller/BaseController.java` | 解析 token 中的 source |
| `c-web/web/.../controller/WxMiniLoginController.java` | 按 source 路由 appId，注册写入 source |
| `c-web/web/src/main/resources/application-pro.yml` | 多套 miniapp 配置加 source |
| `c-web/web/src/main/resources/application-test.yml` | 多套 miniapp 配置加 source |
| `jeesite_new/.../entity/AppBUser.java` | @Table 增加 source 列，增加字段 |
| `jeesite_new/.../entity/AppUser.java` | @Table 增加 source 列，增加字段 |
| `jeesite_new/.../web/AppBUserController.java` | 列表查询支持 source 筛选 |
| `/Users/lwx/Workspace/waibao/qianliyan/qianliyan-minipro/config/index.js` | source 改为 'SSP' |

---

### Task 1: 创建 SourceEnum 和 SourceContext 基础类

**Files:**
- Create: `c-web/web/src/main/java/com/cheji/web/constant/SourceEnum.java`
- Create: `c-web/web/src/main/java/com/cheji/web/context/SourceContext.java`

- [ ] **Step 1: 创建 SourceEnum**

```java
package com.cheji.web.constant;

public enum SourceEnum {
    SSP("SSP", "千里眼"),
    TBD_2("TBD_2", "小程序2"),
    TBD_3("TBD_3", "小程序3"),
    TBD_4("TBD_4", "小程序4");

    private final String code;
    private final String name;

    SourceEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static SourceEnum fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (SourceEnum source : values()) {
            if (source.code.equals(code)) {
                return source;
            }
        }
        return null;
    }

    public static boolean isValid(String code) {
        return fromCode(code) != null;
    }
}
```

- [ ] **Step 2: 创建 SourceContext**

```java
package com.cheji.web.context;

public class SourceContext {

    private static final ThreadLocal<String> CURRENT_SOURCE = new ThreadLocal<>();

    public static void setSource(String source) {
        CURRENT_SOURCE.set(source);
    }

    public static String getSource() {
        return CURRENT_SOURCE.get();
    }

    public static void clear() {
        CURRENT_SOURCE.remove();
    }
}
```

- [ ] **Step 3: Commit**

```bash
git add c-web/web/src/main/java/com/cheji/web/constant/SourceEnum.java c-web/web/src/main/java/com/cheji/web/context/SourceContext.java
git commit -m "feat: 新增 SourceEnum 和 SourceContext 基础类"
```

---

### Task 2: 创建 SourceInterceptor 和 WebMvcConfig

**Files:**
- Create: `c-web/web/src/main/java/com/cheji/web/interceptor/SourceInterceptor.java`
- Create: `c-web/web/src/main/java/com/cheji/web/config/WebMvcConfig.java`

- [ ] **Step 1: 创建 SourceInterceptor**

```java
package com.cheji.web.interceptor;

import com.cheji.web.constant.SourceEnum;
import com.cheji.web.context.SourceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SourceInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(SourceInterceptor.class);
    private static final String SOURCE_HEADER = "X-Source";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String source = request.getHeader(SOURCE_HEADER);
        if (source == null || !SourceEnum.isValid(source)) {
            logger.warn("请求缺少有效的 X-Source header, source={}, uri={}", source, request.getRequestURI());
            response.setStatus(400);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":400,\"msg\":\"缺少有效的来源标识\"}");
            return false;
        }
        SourceContext.setSource(source);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        SourceContext.clear();
    }
}
```

- [ ] **Step 2: 创建 WebMvcConfig 注册拦截器**

```java
package com.cheji.web.config;

import com.cheji.web.interceptor.SourceInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SourceInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/user/wxMiniLogin");
    }
}
```

- [ ] **Step 3: Commit**

```bash
git add c-web/web/src/main/java/com/cheji/web/interceptor/SourceInterceptor.java c-web/web/src/main/java/com/cheji/web/config/WebMvcConfig.java
git commit -m "feat: 新增 SourceInterceptor 拦截器和 WebMvcConfig 注册"
```

---

### Task 3: TokenPojo 增加 source 字段

**Files:**
- Modify: `c-web/web/src/main/java/com/cheji/web/pojo/TokenPojo.java`

- [ ] **Step 1: 在 TokenPojo 中增加 source 字段**

在 `appUserEntity` 字段后（第11行之后）增加：

```java
    private String source;
```

在 `setAppUserEntity` 方法后（第44行之后）增加 getter/setter：

```java
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
```

更新 `toString()` 方法，在 `appUserEntity` 后追加 `source`：

```java
    @Override
    public String toString() {
        return "TokenPojo{" +
                "thirdSessionKey='" + thirdSessionKey + '\'' +
                ", unionId='" + unionId + '\'' +
                ", appUserEntity=" + appUserEntity +
                ", source='" + source + '\'' +
                '}';
    }
```

- [ ] **Step 2: Commit**

```bash
git add c-web/web/src/main/java/com/cheji/web/pojo/TokenPojo.java
git commit -m "feat: TokenPojo 增加 source 字段"
```

---

### Task 4: AppUserEntity (c-web) 增加 source 字段

**Files:**
- Modify: `c-web/web/src/main/java/com/cheji/web/modular/domain/AppUserEntity.java`

- [ ] **Step 1: 增加 source 字段**

在 `isInner` 字段后（第37行之后）增加：

```java
    private String source;
```

在 `setIsInner` 方法后（第69行之后）增加 getter/setter：

```java
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
```

更新 `toString()` 方法，在 `vipLv` 后追加 `, source='" + source + '\''`。

- [ ] **Step 2: Commit**

```bash
git add c-web/web/src/main/java/com/cheji/web/modular/domain/AppUserEntity.java
git commit -m "feat: AppUserEntity 增加 source 字段"
```

---

### Task 5: WxMaProperties 增加 source 字段 + WxMaConfiguration 增加 sourceToAppIdMap

**Files:**
- Modify: `c-web/web/src/main/java/com/cheji/web/config/WxMaProperties.java:18-43`
- Modify: `c-web/web/src/main/java/com/cheji/web/config/WxMaConfiguration.java:51-82`

- [ ] **Step 1: WxMaProperties.Config 增加 source 字段**

在 `WxMaProperties.java` 的 `Config` 内部类中，`msgDataFormat` 字段后（第42行之后）增加：

```java
        /**
         * 对应的小程序来源标识
         */
        private String source;
```

Lombok `@Data` 会自动生成 getter/setter，无需手动添加。

- [ ] **Step 2: WxMaConfiguration 增加 sourceToAppIdMap Bean**

在 `WxMaConfiguration.java` 中，`wxMaService()` 方法后（第82行之后）增加新的 Bean：

```java
    @Bean
    public java.util.Map<String, String> sourceToAppIdMap() {
        List<WxMaProperties.Config> configs = this.properties.getConfigs();
        if (configs == null) {
            return java.util.Collections.emptyMap();
        }
        java.util.Map<String, String> map = new java.util.HashMap<>();
        for (WxMaProperties.Config config : configs) {
            if (config.getSource() != null) {
                map.put(config.getSource(), config.getAppid());
            }
        }
        log.info("sourceToAppIdMap 初始化完成: {}", map);
        return map;
    }
```

在文件顶部 import 区域增加（如果尚未存在）：

```java
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
```

- [ ] **Step 3: Commit**

```bash
git add c-web/web/src/main/java/com/cheji/web/config/WxMaProperties.java c-web/web/src/main/java/com/cheji/web/config/WxMaConfiguration.java
git commit -m "feat: WxMaProperties 增加 source 字段，WxMaConfiguration 增加 sourceToAppIdMap"
```

---

### Task 6: 改造 WxMiniLoginController 按 source 路由

**Files:**
- Modify: `c-web/web/src/main/java/com/cheji/web/modular/controller/WxMiniLoginController.java`

- [ ] **Step 1: 注入 sourceToAppIdMap**

在 `appUserMapper` 注入后（第43行之后）增加：

```java
    @Resource
    private Map<String, String> sourceToAppIdMap;
```

在文件顶部增加 import：

```java
import java.util.Map;
import com.cheji.web.constant.SourceEnum;
```

- [ ] **Step 2: 改造 wxMiniLogin 方法**

将整个 `wxMiniLogin` 方法替换为以下实现。关键改动：
1. 校验 source 合法性
2. 根据 source 获取 appId 并 switchoverTo
3. 注册时写入 source
4. token 中写入 source

```java
    @ApiOperation("微信小程序登录")
    @PostMapping("/wxMiniLogin")
    public JSONObject wxMiniLogin(@RequestBody JSONObject in) {
        JSONObject result = new JSONObject();
        String code = in.getString("code");
        String source = in.getString("source");

        if (StringUtils.isEmpty(code)) {
            result.put("code", 401);
            result.put("msg", "参数错误");
            return result;
        }

        // 校验 source
        if (StringUtils.isEmpty(source) || !SourceEnum.isValid(source)) {
            result.put("code", 401);
            result.put("msg", "无效的来源标识");
            return result;
        }

        // 根据 source 获取对应的 appId
        String appId = sourceToAppIdMap.get(source);
        if (StringUtils.isEmpty(appId)) {
            result.put("code", 401);
            result.put("msg", "未配置该来源的微信应用");
            return result;
        }

        try {
            // 切换到对应小程序的配置
            wxMaService.switchoverTo(appId);
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);
            String openid = session.getOpenid();

            if (StringUtils.isEmpty(openid)) {
                result.put("code", 401);
                result.put("msg", "获取openid失败");
                return result;
            }

            // 根据 openid 查找用户
            AppUserEntity param = new AppUserEntity();
            param.setWxOpenId(openid);
            AppUserEntity appUserEntity = appUserMapper.selectOne(param);

            if (appUserEntity == null) {
                // 自动注册新用户
                appUserEntity = new AppUserEntity();
                appUserEntity.setWxOpenId(openid);
                appUserEntity.setCreatTime(new Date());
                appUserEntity.setSource(source);
                Random random = new Random();
                appUserEntity.setName("微信用户" + ((int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000));
                appUserMapper.insert(appUserEntity);
            }

            // 生成 token
            String thirdSessionKey = RandomStringUtils.randomAlphanumeric(64);
            TokenPojo tokenPojo = new TokenPojo();
            tokenPojo.setUnionId(null);
            tokenPojo.setAppUserEntity(appUserEntity);
            tokenPojo.setSource(source);

            // 踢掉旧登录
            String oldToken = stringRedisTemplate.opsForValue().get(RedisConstant.USER_ID_TOKEN + appUserEntity.getId());
            if (StringUtils.isNotEmpty(oldToken)) {
                stringRedisTemplate.delete(RedisConstant.USER_TOKEN + oldToken);
            }

            // 存储 token，30天有效
            stringRedisTemplate.opsForValue().set(
                    RedisConstant.USER_TOKEN + thirdSessionKey,
                    JSON.toJSONString(tokenPojo),
                    60 * 60 * 24 * 30, TimeUnit.SECONDS
            );
            stringRedisTemplate.opsForValue().set(
                    RedisConstant.USER_ID_TOKEN + appUserEntity.getId(),
                    thirdSessionKey
            );

            JSONObject data = new JSONObject();
            data.put("thirdSessionKey", thirdSessionKey);
            data.put("userId", appUserEntity.getId());
            data.put("name", appUserEntity.getName());

            result.put("code", 200);
            result.put("msg", "登录成功");
            result.put("data", data);
            return result;

        } catch (WxErrorException e) {
            logger.error("小程序登录失败, source={}", source, e);
            result.put("code", 500);
            result.put("msg", "登录失败：" + e.getMessage());
            return result;
        } catch (Exception e) {
            logger.error("小程序登录异常, source={}", source, e);
            result.put("code", 500);
            result.put("msg", "登录异常：" + e.getMessage());
            return result;
        }
    }
```

- [ ] **Step 3: Commit**

```bash
git add c-web/web/src/main/java/com/cheji/web/modular/controller/WxMiniLoginController.java
git commit -m "feat: WxMiniLoginController 按 source 路由微信 appId"
```

---

### Task 7: BaseController 解析 token 中的 source

**Files:**
- Modify: `c-web/web/src/main/java/com/cheji/web/modular/controller/BaseController.java`

- [ ] **Step 1: 修改 getCurrentLoginUser 方法**

在 `BaseController.java` 中，`getCurrentLoginUser` 方法内，`tokenPojo.setAppUserEntity(appUser)` 之后（第38行之后）增加从 JSON 中解析 source：

```java
        String source = result.getString("source");
        tokenPojo.setSource(source);
```

在文件顶部增加 import（如果尚未存在）：

```java
import com.cheji.web.context.SourceContext;
```

同时在设置 source 后，将 source 也写入 SourceContext（作为 header 的备份来源）：

```java
        if (source != null) {
            SourceContext.setSource(source);
        }
```

完整的 `getCurrentLoginUser` 方法应为：

```java
    protected TokenPojo getCurrentLoginUser(HttpServletRequest request) {
        String thirdsessionkey = request.getHeader(UserConstant.USER_LOGIN_THIRDSESSIONKEY);
        logger.info("###getCurrentLoginUser### thirdsessionkey={}", thirdsessionkey);
        Object o = stringRedisTemplate.opsForValue().get(RedisConstant.USER_TOKEN + thirdsessionkey);
        if (o == null) {
            return null;
        }

        JSONObject result = JSONObject.parseObject(String.valueOf(o));

        AppUserEntity appUser = JSONObject.parseObject(result.getString("appUserEntity"), AppUserEntity.class);

        TokenPojo tokenPojo = new TokenPojo();
        tokenPojo.setThirdSessionKey(thirdsessionkey);
        tokenPojo.setUnionId(null);
        tokenPojo.setAppUserEntity(appUser);

        String source = result.getString("source");
        tokenPojo.setSource(source);
        if (source != null) {
            SourceContext.setSource(source);
        }

        //刷新用户token有效期
        stringRedisTemplate.expire(RedisConstant.USER_TOKEN + thirdsessionkey, 60 * 60 * 24 * 30, TimeUnit.SECONDS);
        return tokenPojo;
    }
```

- [ ] **Step 2: Commit**

```bash
git add c-web/web/src/main/java/com/cheji/web/modular/controller/BaseController.java
git commit -m "feat: BaseController 解析 token 中的 source 并写入 SourceContext"
```

---

### Task 8: YAML 配置增加 source 字段

**Files:**
- Modify: `c-web/web/src/main/resources/application-pro.yml:79-85`
- Modify: `c-web/web/src/main/resources/application-test.yml:75-81`

- [ ] **Step 1: 修改 application-pro.yml**

将 miniapp configs 部分（第79-85行）改为：

```yaml
  miniapp:
    configs:
      - appid: wx5d88d6c7c216e1f3
        secret: 28bc585bd3118857554e0126e20e1abd
        token: #微信小程序消息服务器配置的token
        aesKey: #微信小程序消息服务器配置的EncodingAESKey
        msgDataFormat: JSON
        source: SSP
```

即在现有配置最后一行 `msgDataFormat: JSON` 之后增加 `source: SSP`。

- [ ] **Step 2: 修改 application-test.yml**

同样将 miniapp configs 部分（第75-81行）改为：

```yaml
  miniapp:
    configs:
      - appid: wx5d88d6c7c216e1f3
        secret: 28bc585bd3118857554e0126e20e1abd
        token: #微信小程序消息服务器配置的token
        aesKey: #微信小程序消息服务器配置的EncodingAESKey
        msgDataFormat: JSON
        source: SSP
```

- [ ] **Step 3: Commit**

```bash
git add c-web/web/src/main/resources/application-pro.yml c-web/web/src/main/resources/application-test.yml
git commit -m "feat: YAML 配置 miniapp configs 增加 source 字段"
```

---

### Task 9: jeesite_new 实体类增加 source 字段

**Files:**
- Modify: `jeesite_new/src/main/java/com/jeesite/modules/app/entity/AppBUser.java`
- Modify: `jeesite_new/src/main/java/com/jeesite/modules/app/entity/AppUser.java`

- [ ] **Step 1: AppBUser 增加 source 列注解和字段**

在 `AppBUser.java` 的 `@Table` 注解 columns 数组中，`up_id` 列之后（第74行之后，`},` 之前）增加：

```java
		@Column(name="source", attrName="source", label="小程序来源标识"),
```

在类的字段声明区域，`upId` 字段后（第151行之后）增加：

```java
	private String source;		// 小程序来源标识
```

在 `setUpId` 方法后（第159行之后）增加 getter/setter：

```java
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
```

- [ ] **Step 2: AppUser (jeesite_new) 增加 source 列注解和字段**

在 `AppUser.java` 的 `@Table` 注解 columns 数组中，`is_inner` 列之后（第39行之后，`}, orderBy` 之前）增加：

```java
		@Column(name="source", attrName="source", label="小程序来源标识"),
```

在类的字段声明区域，`isInner` 字段后（第59行之后）增加：

```java
	private String source;		// 小程序来源标识
```

增加 getter/setter（在类的方法区域适当位置）：

```java
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
```

- [ ] **Step 3: Commit**

```bash
git add jeesite_new/src/main/java/com/jeesite/modules/app/entity/AppBUser.java jeesite_new/src/main/java/com/jeesite/modules/app/entity/AppUser.java
git commit -m "feat: jeesite_new AppBUser 和 AppUser 实体增加 source 字段"
```

---

### Task 10: 数据库迁移脚本

**Files:**
- Create: `sql/add_source_field.sql`

- [ ] **Step 1: 创建 SQL 迁移脚本**

```sql
-- 多小程序来源识别：增加 source 字段
-- 执行时间：2026-04-05

-- app_user 表增加 source 字段
ALTER TABLE app_user ADD COLUMN source VARCHAR(20) DEFAULT NULL COMMENT '小程序来源标识';
CREATE INDEX idx_app_user_source ON app_user(source);

-- app_b_user 表增加 source 字段
ALTER TABLE app_b_user ADD COLUMN source VARCHAR(20) DEFAULT NULL COMMENT '小程序来源标识';
CREATE INDEX idx_app_b_user_source ON app_b_user(source);

-- 存量数据迁移：现有用户全部来自千里眼小程序
UPDATE app_user SET source = 'SSP' WHERE source IS NULL;
UPDATE app_b_user SET source = 'SSP' WHERE source IS NULL;
```

- [ ] **Step 2: Commit**

```bash
git add sql/add_source_field.sql
git commit -m "feat: 数据库迁移脚本 - app_user 和 app_b_user 增加 source 字段"
```

---

### Task 11: 后台审核列表支持 source 筛选

**Files:**
- Modify: `jeesite_new/src/main/java/com/jeesite/modules/app/web/AppBUserController.java`

- [ ] **Step 1: 确认 AppBUser 实体已支持 source 查询**

AppBUser 的 `@Table` 注解中 source 列默认 `isQuery=true`（JeeSite 默认值），所以 `AppBUserService.findPage(appBUser)` 在传入带 source 值的 AppBUser 时会自动加上 WHERE 条件。

后台页面的 `listData` 方法已经接收 `AppBUser` 作为查询参数，前端表单传入 `source` 参数即可自动过滤。无需修改 Controller Java 代码。

如果需要在后台列表页面增加 source 下拉筛选框，需要修改对应的 JSP/HTML 模板文件。这属于前端后台管理界面的改动，可以后续根据需要添加。

此步骤确认后端查询已自动支持 source 过滤，无需额外代码改动。

- [ ] **Step 2: Commit（如有改动）**

如果此 Task 无代码改动，跳过 commit。

---

### Task 12: 小程序端 source 改为 SSP

**Files:**
- Modify: `/Users/lwx/Workspace/waibao/qianliyan/qianliyan-minipro/config/index.js`

- [ ] **Step 1: 修改 config/index.js**

将第6行的 `source: 'qianliyan'` 改为 `source: 'SSP'`：

```javascript
const config = {
  // 后端 API 基础地址（icars-admin）
  baseUrl: 'http://114.215.211.119/wx-admin',
  // 小程序来源标识，用于多小程序共用同一后端时区分
  source: 'SSP',
  // 微信小程序 AppID
  appId: 'wx5d88d6c7c216e1f3',
  // 订阅消息模板ID（审核结果通知，从微信后台获取后填入）
  subscribeTemplateId: '8_0YosP9YlA9Y-H8EWBD7hz6NVC81u3eZezVAw_JxYs'
}

module.exports = config
```

`request.js` 无需改动，已通过 `config.source` 自动携带 `X-Source` header。

- [ ] **Step 2: Commit（在小程序项目中）**

```bash
cd /Users/lwx/Workspace/waibao/qianliyan/qianliyan-minipro
git add config/index.js
git commit -m "feat: source 标识从 qianliyan 改为 SSP"
```

---

### Task 13: 验证编译

**Files:** 无新文件

- [ ] **Step 1: 编译后端项目确认无语法错误**

在后端项目根目录执行 Maven 编译（跳过测试）：

```bash
cd /Users/lwx/Workspace/waibao/qianliyan/qianliyan-backend
mvn compile -pl c-web/web -am -q
```

Expected: BUILD SUCCESS，无编译错误。

如果有编译错误，根据错误信息修复后重新编译。

- [ ] **Step 2: 确认所有改动已提交**

```bash
git status
```

Expected: working tree clean
