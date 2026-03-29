# 小程序上传记录与奖励功能 Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 补全审核转账后账单写入、新增上传记录/奖励明细/奖励汇总接口、改造小程序上传记录和我的页面、新建奖励记录页面。

**Architecture:** 后端在现有 `AccidentController.checkSuccess` 中补写 `biz_wxpay_bill` 记录，在 `XcxController` 追加三个小程序查询接口，通过 `biz_accident.openid` 关联账单；前端改造 upload-history 接入新接口，新建 reward-records 页面，mine 页接入奖励汇总接口。

**Tech Stack:** Java 8 / Spring Boot / MyBatis-Plus / 微信小程序原生框架

---

## 文件索引

### 后端（Java）

| 操作 | 文件 |
|------|------|
| 修改 | `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/model/BizWxpayBill.java` |
| 修改 | `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/dao/BizWxpayBillMapper.java` |
| 修改 | `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/dao/mapping/BizWxpayBillMapper.xml` |
| 修改 | `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/dao/AccdMapper.java` |
| 修改 | `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/dao/mapping/AccdMapper.xml` |
| 修改 | `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/controller/AccidentController.java` |
| 修改 | `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/controller/xcx/XcxController.java` |

### 前端（小程序）路径相对 `/Users/lwx/Workspace/waibao/qianliyan/qianliyan-minipro/`

| 操作 | 文件 |
|------|------|
| 修改 | `pages/upload-history/upload-history.js` |
| 修改 | `pages/upload-history/upload-history.wxml` |
| 新建 | `pages/reward-records/reward-records.js` |
| 新建 | `pages/reward-records/reward-records.wxml` |
| 新建 | `pages/reward-records/reward-records.wxss` |
| 新建 | `pages/reward-records/reward-records.json` |
| 修改 | `pages/mine/mine.js` |
| 修改 | `pages/mine/mine.wxml` |
| 修改 | `app.json` |

---

### Task 1: DDL — 给 biz_wxpay_bill 加 amount 列

**Files:**
- Modify: `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/model/BizWxpayBill.java:50-54`

- [ ] **Step 1: 执行 DDL**

在数据库执行（可通过 MySQL 客户端或命令行）：
```sql
ALTER TABLE biz_wxpay_bill ADD COLUMN amount DECIMAL(10,2) COMMENT '奖励金额（元）' AFTER status;
```

- [ ] **Step 2: 去掉 BizWxpayBill.amount 的 @TableField(exist = false)**

将 `BizWxpayBill.java` 第 53-54 行：
```java
@TableField(exist = false)
private BigDecimal amount;
```
改为：
```java
private BigDecimal amount;
```

- [ ] **Step 3: 确认编译通过**

```bash
cd /Users/lwx/Workspace/waibao/qianliyan/qianliyan-backend/icars-master/icars-master
mvn compile -pl icars-admin -am -q
```
Expected: BUILD SUCCESS

- [ ] **Step 4: Commit**

```bash
git add icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/model/BizWxpayBill.java
git commit -m "feat: biz_wxpay_bill.amount 字段映射到数据库列"
```

---

### Task 2: 更新 BizWxpayBillMapper.xml add() 写入 amount

**Files:**
- Modify: `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/dao/mapping/BizWxpayBillMapper.xml:89-93`

现有 `add` insert 语句不包含 amount 列，需补充：

- [ ] **Step 1: 修改 XML insert**

将：
```xml
<insert id="add" useGeneratedKeys="true" keyProperty="bizWxpayBill.id" keyColumn="id"  parameterType="com.stylefeng.guns.modular.system.model.BizWxpayBill" >
	insert into biz_wxpay_bill(accid, status, pay_time, create_time)
	values
	(#{bizWxpayBill.accid},#{bizWxpayBill.status},#{bizWxpayBill.payTime},#{bizWxpayBill.createTime})
</insert>
```
改为：
```xml
<insert id="add" useGeneratedKeys="true" keyProperty="bizWxpayBill.id" keyColumn="id"  parameterType="com.stylefeng.guns.modular.system.model.BizWxpayBill" >
	insert into biz_wxpay_bill(accid, status, amount, pay_time, create_time)
	values
	(#{bizWxpayBill.accid},#{bizWxpayBill.status},#{bizWxpayBill.amount},#{bizWxpayBill.payTime},#{bizWxpayBill.createTime})
</insert>
```

- [ ] **Step 2: Commit**

```bash
git add icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/dao/mapping/BizWxpayBillMapper.xml
git commit -m "feat: BizWxpayBill add() 写入 amount 字段"
```

---

### Task 3: AccidentController.checkSuccess 补写账单记录

**Files:**
- Modify: `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/controller/AccidentController.java`

目标：V3 转账调用后，无论成功失败都写入 `biz_wxpay_bill` 记录。

- [ ] **Step 1: 阅读现有 checkSuccess 转账代码**

读取文件第 330-380 行，定位 `wxPayV3TransferService.transferToUser(...)` 调用及其返回值处理。

- [ ] **Step 2: 在 transferToUser 调用之后添加账单写入**

找到 `v3Result` 赋值后（或 transferToUser 返回后），添加：
```java
// 写入账单记录（无论转账成功失败）
BizWxpayBill bill = new BizWxpayBill();
bill.setAccid(accdId);
bill.setAmount(amount);  // 单位：元
bill.setPayTime(new Date());
bill.setCreateTime(new Date());
bill.setStatus(v3Result ? 0 : 1);  // 0=成功 1=失败
bizWxpayBillService.add(bill);
```

注意：`amount` 参数已是元（BigDecimal），直接存入；`BizWxpayBill` 类已在文件 import 区，`bizWxpayBillService` 已注入。

- [ ] **Step 3: 确认编译通过**

```bash
cd /Users/lwx/Workspace/waibao/qianliyan/qianliyan-backend/icars-master/icars-master
mvn compile -pl icars-admin -am -q
```
Expected: BUILD SUCCESS

- [ ] **Step 4: Commit**

```bash
git add icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/controller/AccidentController.java
git commit -m "fix: 审核通过转账后写入 biz_wxpay_bill 账单记录"
```

---

### Task 4: 新增 AccdMapper 上传记录查询方法

**Files:**
- Modify: `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/dao/AccdMapper.java`
- Modify: `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/dao/mapping/AccdMapper.xml`

- [ ] **Step 1: AccdMapper.java 追加方法声明**

在接口末尾（最后一个方法后、`}` 前）添加：
```java
/**
 * 查询指定 openid 的上传记录（含奖励金额），分页
 */
List<Map<String, Object>> selectAccidentListByOpenid(
    @Param("openid") String openid,
    @Param("offset") int offset,
    @Param("pageSize") int pageSize
);
```

- [ ] **Step 2: AccdMapper.xml 追加 select 语句**

在 `AccdMapper.xml` 的 `</mapper>` 前添加：
```xml
<select id="selectAccidentListByOpenid" resultType="map">
    SELECT a.id, a.video, a.acc_img AS accImg, a.status, a.create_time AS createTime,
           a.reason, b.amount AS rewardAmount
    FROM biz_accident a
    LEFT JOIN biz_wxpay_bill b ON b.accid = a.id AND b.status = 0
    WHERE a.openid = #{openid}
    ORDER BY a.create_time DESC
    LIMIT #{pageSize} OFFSET #{offset}
</select>
```

- [ ] **Step 3: 编译确认**

```bash
cd /Users/lwx/Workspace/waibao/qianliyan/qianliyan-backend/icars-master/icars-master
mvn compile -pl icars-admin -am -q
```
Expected: BUILD SUCCESS

- [ ] **Step 4: Commit**

```bash
git add icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/dao/AccdMapper.java \
  icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/dao/mapping/AccdMapper.xml
git commit -m "feat: AccdMapper 新增按 openid 查询上传记录分页方法"
```

---

### Task 5: 新增 BizWxpayBillMapper 奖励查询方法

**Files:**
- Modify: `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/dao/BizWxpayBillMapper.java`
- Modify: `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/dao/mapping/BizWxpayBillMapper.xml`

- [ ] **Step 1: BizWxpayBillMapper.java 追加两个方法声明**

在接口末尾（`getWxUnifiedOrderNo` 方法后、`}` 前）添加：
```java
/**
 * 查询指定 openid 的奖励明细（成功记录），分页
 */
List<Map<String, Object>> selectRewardListByOpenid(
    @Param("openid") String openid,
    @Param("offset") int offset,
    @Param("pageSize") int pageSize
);

/**
 * 统计指定 openid 的累计奖励金额
 */
BigDecimal sumRewardByOpenid(@Param("openid") String openid);
```

- [ ] **Step 2: BizWxpayBillMapper.xml 追加两个 select 语句**

在 `</mapper>` 前添加：
```xml
<select id="selectRewardListByOpenid" resultType="map">
    SELECT b.id, b.amount, b.pay_time AS payTime, a.address AS accidentAddress
    FROM biz_wxpay_bill b
    JOIN biz_accident a ON a.id = b.accid
    WHERE a.openid = #{openid} AND b.status = 0
    ORDER BY b.pay_time DESC
    LIMIT #{pageSize} OFFSET #{offset}
</select>

<select id="sumRewardByOpenid" resultType="java.math.BigDecimal">
    SELECT COALESCE(SUM(b.amount), 0)
    FROM biz_wxpay_bill b
    JOIN biz_accident a ON a.id = b.accid
    WHERE a.openid = #{openid} AND b.status = 0
</select>
```

- [ ] **Step 3: 编译确认**

```bash
cd /Users/lwx/Workspace/waibao/qianliyan/qianliyan-backend/icars-master/icars-master
mvn compile -pl icars-admin -am -q
```
Expected: BUILD SUCCESS

- [ ] **Step 4: Commit**

```bash
git add icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/dao/BizWxpayBillMapper.java \
  icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/dao/mapping/BizWxpayBillMapper.xml
git commit -m "feat: BizWxpayBillMapper 新增奖励明细查询和汇总方法"
```

---

### Task 6: XcxController 新增三个小程序接口

**Files:**
- Modify: `icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/controller/xcx/XcxController.java`

**前置**：XcxController 已注入 `WxService wxService` 和 `IAccdService` 的引用；需额外注入 `BizWxpayBillMapper`（或通过 service）。查看文件确认已有 `@Resource private IAccdService accdService`，若无则添加。

- [ ] **Step 1: 在 XcxController 顶部添加缺失的 import 和注入**

检查 `XcxController.java` 是否已有 `IAccdService`、`BizWxpayBillMapper` 的注入。若无，在现有 `@Resource` 注入块中追加：
```java
@Resource
private IAccdService accdService;

@Resource
private BizWxpayBillMapper bizWxpayBillMapper;
```

同时在 import 区添加（如缺少）：
```java
import com.stylefeng.guns.modular.system.dao.BizWxpayBillMapper;
import com.stylefeng.guns.modular.system.service.IAccdService;
```

- [ ] **Step 2: 追加 /api/v1/wx/accid/list 接口**

在 XcxController 类末尾 `}` 前添加：
```java
/**
 * 小程序 - 本人上传记录列表
 */
@RequestMapping(value = "/api/v1/wx/accid/list", method = RequestMethod.GET)
@ResponseBody
public Object getAccidList(@RequestParam String thirdSessionKey,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "10") int pageSize) {
    WxSession wxSession = wxService.getWxSession(thirdSessionKey);
    if (wxSession == null || wxSession.getOpenId() == null || wxSession.getOpenId().isEmpty()) {
        return new ApiResponseEntity(530, "未登录", null);
    }
    String openid = wxSession.getOpenId();
    int offset = (page - 1) * pageSize;
    List<Map<String, Object>> list = accdService.selectAccidentListByOpenid(openid, offset, pageSize);
    Map<String, Object> data = new HashMap<>();
    data.put("list", list);
    data.put("total", list.size());
    return new ApiResponseEntity(0, "success", data);
}
```

- [ ] **Step 3: 追加 /api/v1/wx/reward/list 接口**

继续在 `}` 前添加：
```java
/**
 * 小程序 - 奖励明细列表
 */
@RequestMapping(value = "/api/v1/wx/reward/list", method = RequestMethod.GET)
@ResponseBody
public Object getRewardList(@RequestParam String thirdSessionKey,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "10") int pageSize) {
    WxSession wxSession = wxService.getWxSession(thirdSessionKey);
    if (wxSession == null || wxSession.getOpenId() == null || wxSession.getOpenId().isEmpty()) {
        return new ApiResponseEntity(530, "未登录", null);
    }
    String openid = wxSession.getOpenId();
    int offset = (page - 1) * pageSize;
    List<Map<String, Object>> list = bizWxpayBillMapper.selectRewardListByOpenid(openid, offset, pageSize);
    Map<String, Object> data = new HashMap<>();
    data.put("list", list);
    return new ApiResponseEntity(0, "success", data);
}
```

- [ ] **Step 4: 追加 /api/v1/wx/user/stats 接口**

继续在 `}` 前添加：
```java
/**
 * 小程序 - 奖励汇总
 */
@RequestMapping(value = "/api/v1/wx/user/stats", method = RequestMethod.GET)
@ResponseBody
public Object getUserStats(@RequestParam String thirdSessionKey) {
    WxSession wxSession = wxService.getWxSession(thirdSessionKey);
    if (wxSession == null || wxSession.getOpenId() == null || wxSession.getOpenId().isEmpty()) {
        return new ApiResponseEntity(530, "未登录", null);
    }
    String openid = wxSession.getOpenId();
    java.math.BigDecimal totalReward = bizWxpayBillMapper.sumRewardByOpenid(openid);
    Map<String, Object> data = new HashMap<>();
    data.put("totalReward", totalReward);
    return new ApiResponseEntity(0, "success", data);
}
```

- [ ] **Step 5: 编译确认**

```bash
cd /Users/lwx/Workspace/waibao/qianliyan/qianliyan-backend/icars-master/icars-master
mvn compile -pl icars-admin -am -q
```
Expected: BUILD SUCCESS

- [ ] **Step 6: Commit**

```bash
git add icars-master/icars-master/icars-admin/src/main/java/com/stylefeng/guns/modular/system/controller/xcx/XcxController.java
git commit -m "feat: XcxController 新增上传记录/奖励明细/奖励汇总三个小程序接口"
```

---

### Task 7: 改造小程序 upload-history 页面

**Files:**
- Modify: `/Users/lwx/Workspace/waibao/qianliyan/qianliyan-minipro/pages/upload-history/upload-history.js`
- Modify: `/Users/lwx/Workspace/waibao/qianliyan/qianliyan-minipro/pages/upload-history/upload-history.wxml`

- [ ] **Step 1: 修改 upload-history.js**

将 `loadRecords` 方法中的接口地址和字段映射更新：
```javascript
loadRecords() {
  this.setData({ loading: true })
  const thirdSessionKey = wx.getStorageSync('thirdSessionKey') || ''
  return request({
    url: '/api/v1/wx/accid/list',
    method: 'GET',
    data: { thirdSessionKey, page: this.data.page, pageSize: 20 }
  }).then(res => {
    const list = (res.data && res.data.list) || []
    const hasMore = list.length >= 20
    if (this.data.page === 1) {
      this.setData({ records: list, loading: false, hasMore })
    } else {
      this.setData({ records: this.data.records.concat(list), loading: false, hasMore })
    }
  }).catch(() => {
    this.setData({ loading: false })
  })
},
```

同时将 `viewDetail` 中的 `item.imgUrl` 改为 `item.accImg`：
```javascript
viewDetail(e) {
  const item = e.currentTarget.dataset.item
  if (item.video) {
    wx.previewMedia({
      sources: [{ url: item.video, type: 'video' }]
    })
  } else if (item.accImg) {
    const urls = item.accImg.split(',')
    wx.previewImage({ urls })
  }
},
```

- [ ] **Step 2: 修改 upload-history.wxml 增加奖励展示**

在 `<view class="record-status">` 块（现有状态标签）之后，`</view class="record-info">` 关闭前，添加奖励行：
```xml
<view class="reward-info">
  <text wx:if="{{item.rewardAmount != null}}" class="reward-amount">奖励：¥{{item.rewardAmount}}</text>
  <text wx:elif="{{item.status === 2}}" class="reward-pending">奖励处理中</text>
</view>
```

同时将 WXML 中 `item.imgUrl` 替换为 `item.accImg`（缩略图显示部分）：
```xml
<view wx:elif="{{item.accImg}}" class="thumb-photo">
  <image src="{{item.accImg.split(',')[0]}}" mode="aspectFill" class="thumb-img"></image>
</view>
```

注意：现有 WXML 的 status 判断用的是 `item.statuse`（有拼写错误），后端返回字段为 `status`，一并修正为 `item.status`。

- [ ] **Step 3: Commit**

```bash
cd /Users/lwx/Workspace/waibao/qianliyan/qianliyan-minipro
git add pages/upload-history/upload-history.js pages/upload-history/upload-history.wxml
git commit -m "feat: upload-history 接入新接口，展示奖励金额"
```

---

### Task 8: 新建小程序 reward-records 页面

**Files:**
- Create: `/Users/lwx/Workspace/waibao/qianliyan/qianliyan-minipro/pages/reward-records/reward-records.json`
- Create: `/Users/lwx/Workspace/waibao/qianliyan/qianliyan-minipro/pages/reward-records/reward-records.js`
- Create: `/Users/lwx/Workspace/waibao/qianliyan/qianliyan-minipro/pages/reward-records/reward-records.wxml`
- Create: `/Users/lwx/Workspace/waibao/qianliyan/qianliyan-minipro/pages/reward-records/reward-records.wxss`

- [ ] **Step 1: 创建 reward-records.json**

```json
{
  \"navigationBarTitleText\": \"奖励记录\",
  \"enablePullDownRefresh\": true
}
```

- [ ] **Step 2: 创建 reward-records.js**

```javascript
const { request } = require('../../utils/request')

Page({
  data: {
    list: [],
    page: 1,
    hasMore: true,
    loading: false
  },

  onLoad() {
    this.loadMore()
  },

  onReachBottom() {
    if (this.data.hasMore && !this.data.loading) {
      this.loadMore()
    }
  },

  onPullDownRefresh() {
    this.setData({ list: [], page: 1, hasMore: true })
    this.loadMore().finally(() => wx.stopPullDownRefresh())
  },

  loadMore() {
    if (this.data.loading || !this.data.hasMore) return Promise.resolve()
    this.setData({ loading: true })
    const thirdSessionKey = wx.getStorageSync('thirdSessionKey') || ''
    return request({
      url: '/api/v1/wx/reward/list',
      method: 'GET',
      data: { thirdSessionKey, page: this.data.page, pageSize: 10 }
    }).then(res => {
      const items = (res.data && res.data.list) || []
      this.setData({
        list: this.data.list.concat(items),
        page: this.data.page + 1,
        hasMore: items.length === 10,
        loading: false
      })
    }).catch(() => {
      this.setData({ loading: false })
    })
  }
})
```

- [ ] **Step 3: 创建 reward-records.wxml**

```xml
<view class=\"page\">
  <view class=\"container\">
    <view class=\"reward-list\" wx:if=\"{{list.length > 0}}\">
      <view class=\"reward-item card\" wx:for=\"{{list}}\" wx:key=\"id\">
        <view class=\"reward-amount\">¥{{item.amount}}</view>
        <view class=\"reward-info\">
          <text class=\"reward-time\">{{item.payTime}}</text>
          <text class=\"reward-address\">{{item.accidentAddress || '未知地点'}}</text>
        </view>
      </view>
    </view>

    <view class=\"empty-state\" wx:if=\"{{!loading && list.length === 0}}\">
      <text class=\"empty-text\">暂无奖励记录</text>
    </view>

    <view class=\"load-more\" wx:if=\"{{loading}}\">
      <text>加载中...</text>
    </view>

    <view class=\"no-more\" wx:if=\"{{!hasMore && list.length > 0}}\">
      <text>已加载全部</text>
    </view>
  </view>
</view>
```

- [ ] **Step 4: 创建 reward-records.wxss**

```css
.page { background: #f5f5f5; min-height: 100vh; }
.container { padding: 16rpx; }
.card { background: #fff; border-radius: 12rpx; margin-bottom: 16rpx; padding: 24rpx; }
.reward-item { display: flex; align-items: center; justify-content: space-between; }
.reward-amount { font-size: 40rpx; font-weight: bold; color: #ff4d4f; }
.reward-info { display: flex; flex-direction: column; align-items: flex-end; }
.reward-time { font-size: 24rpx; color: #999; }
.reward-address { font-size: 24rpx; color: #666; margin-top: 8rpx; max-width: 400rpx; text-align: right; }
.empty-state { text-align: center; padding: 120rpx 0; }
.empty-text { font-size: 28rpx; color: #999; }
.load-more, .no-more { text-align: center; padding: 24rpx; color: #999; font-size: 24rpx; }
```

- [ ] **Step 5: Commit**

```bash
cd /Users/lwx/Workspace/waibao/qianliyan/qianliyan-minipro
git add pages/reward-records/
git commit -m \"feat: 新建奖励记录页面\"
```

---

### Task 9: 改造 mine 页面，接入奖励汇总接口和入口

**Files:**
- Modify: `/Users/lwx/Workspace/waibao/qianliyan/qianliyan-minipro/pages/mine/mine.js`
- Modify: `/Users/lwx/Workspace/waibao/qianliyan/qianliyan-minipro/pages/mine/mine.wxml`

- [ ] **Step 1: 修改 mine.js — loadPersonalData 改调 user/stats**

将 `loadPersonalData` 方法的请求改为调用新接口：
```javascript
loadPersonalData() {
  const { request } = require('../../utils/request')
  const thirdSessionKey = wx.getStorageSync('thirdSessionKey') || ''
  request({
    url: '/api/v1/wx/user/stats',
    method: 'GET',
    data: { thirdSessionKey }
  }).then(res => {
    if (res.errorCode === 0) {
      this.setData({
        'statistics.totalReward': res.data.totalReward || 0
      })
    }
  }).catch(err => {
    console.error('获取奖励汇总失败:', err)
  })
},
```

- [ ] **Step 2: 在 mine.js 中新增 goRewardRecords 方法**

在 `goUploadHistory` 方法之后添加：
```javascript
goRewardRecords() {
  if (!app.globalData.isLogin) {
    app.login({
      success: () => {
        wx.navigateTo({ url: '/pages/reward-records/reward-records' })
      },
      fail: (msg) => {
        wx.showToast({ title: msg || '登录失败', icon: 'none' })
      }
    })
  } else {
    wx.navigateTo({ url: '/pages/reward-records/reward-records' })
  }
},
```

- [ ] **Step 3: 修改 mine.wxml — func-grid 中新增奖励记录入口**

在现有 `func-grid` 中，`goUploadHistory` 对应的 `func-item` 之后添加：
```xml
<view class=\"func-item\" bindtap=\"goRewardRecords\">
  <text class=\"func-icon\">🏆</text>
  <text class=\"func-name\">奖励记录</text>
</view>
```

- [ ] **Step 4: Commit**

```bash
cd /Users/lwx/Workspace/waibao/qianliyan/qianliyan-minipro
git add pages/mine/mine.js pages/mine/mine.wxml
git commit -m \"feat: mine 页面接入奖励汇总接口，新增奖励记录入口\"
```

---

### Task 10: app.json 注册 reward-records 页面

**Files:**
- Modify: `/Users/lwx/Workspace/waibao/qianliyan/qianliyan-minipro/app.json`

- [ ] **Step 1: 在 pages 数组中追加新页面**

将：
```json
\"pages/upload-history/upload-history\"
```
改为：
```json
\"pages/upload-history/upload-history\",
\"pages/reward-records/reward-records\"
```

- [ ] **Step 2: Commit**

```bash
cd /Users/lwx/Workspace/waibao/qianliyan/qianliyan-minipro
git add app.json
git commit -m \"feat: app.json 注册 reward-records 页面\"
```

---

## 手工验证步骤（无自动化测试）

1. **账单写入**：管理端对一条已通过的新事故执行 checkSuccess → 查询 `SELECT * FROM biz_wxpay_bill WHERE accid=?`，应有一条含 amount 的记录。
2. **上传记录接口**：用 Postman/curl 调 `GET /api/v1/wx/accid/list?thirdSessionKey=<valid>&page=1&pageSize=10`，应返回列表（含 rewardAmount 字段）。
3. **奖励明细接口**：调 `/api/v1/wx/reward/list`，已发奖励的用户应看到记录。
4. **奖励汇总接口**：调 `/api/v1/wx/user/stats`，返回 `totalReward`。
5. **小程序 upload-history**：进入上传记录页，审核通过且已转账的记录应显示奖励金额；审核通过但未转账显示"奖励处理中"；待审核不显示奖励。
6. **小程序 reward-records**：我的页面点击"奖励记录"进入新页，已发奖励列表正常展示；无记录时显示空状态。
7. **小程序 mine**：个人中心"合计奖励"数值来自新接口，数字与 DB 汇总一致。
