# 小程序上传记录与奖励功能设计文档

**日期**: 2026-03-27
**状态**: 已确认

## 背景

当前实现完成了：
- 小程序上传视频/图片 → `/api/v1/wx/accid/newAdd` → 创建 status=1（未审核）事故记录
- 管理端审核通过 → `AccidentController.checkSuccess` → 微信商家转账V3直发奖励

**缺失功能**：
1. `biz_wxpay_bill` 未写入：支付后无账单记录，防重复支付检查失效
2. 小程序上传记录接口不存在（upload-history 调用 `/file/list` 但无后端实现）
3. 上传记录列表无奖励金额展示
4. 无奖励记录页面
5. 个人中心无奖励汇总及明细入口

## 目标

1. 小程序上传后记录未审核状态
2. 管理员审核通过后转账并记录账单
3. 用户查看上传记录（含审核状态、奖励金额）
4. 用户查看奖励明细记录
5. 个人中心显示奖励汇总

## 现有表结构说明（无需新建表）

### `biz_accident`
已有字段：`id`、`openid`（上报人微信openid，newAdd 时由 wxSession.getOpenId() 写入）、`video`、`acc_img`（图片URL逗号分隔）、`status`（1:未审核 2:通过 3:失败）、`reason`、`address`、`create_time`

> `biz_accident.openid` 字段已存在，无需新增列。

### `biz_wxpay_bill`
已有字段：`id`、`accid`（关联 biz_accident.id）、`status`（0:成功 1:失败）、`pay_time`、`create_time`

**需要 DDL**：`amount` 字段当前为 `@TableField(exist = false)`，需执行：
```sql
ALTER TABLE biz_wxpay_bill ADD COLUMN amount DECIMAL(10,2) COMMENT '奖励金额（元）' AFTER status;
```
执行后去掉模型中 `amount` 字段的 `@TableField(exist = false)` 注解。

### openid 关联路径（无需在 bill 表加 openid 列）
```sql
biz_wxpay_bill.accid = biz_accident.id AND biz_accident.openid = #{openid}
```

### thirdSessionKey → openid 解析
调用 `WxService.getWxSession(thirdSessionKey)`：
- 返回 null 或 openId 为空 → 返回 errorCode=530，前端跳转登录
- 有效 → 取 `wxSession.getOpenId()` 作为查询条件

## 后端设计

### 1. `AccidentController.checkSuccess` 补充账单记录

**防重复支付**：现有 `selectOneByAccid(accdId) != null` 检查覆盖所有 status，同一事故只会尝试一次转账。

**金额单位**：`checkSuccess` 的 `amount` 参数单位为元（BigDecimal）；V3 调用时乘100转分；`biz_wxpay_bill.amount` 存元，前端直接展示。

V3 转账后写入账单（无论成功失败）：
```java
BizWxpayBill bill = new BizWxpayBill();
bill.setAccid(accdId);
bill.setAmount(amount);  // 元
bill.setPayTime(new Date());
bill.setCreateTime(new Date());
bill.setStatus(v3Result ? 0 : 1);  // 0=成功 1=失败
bizWxpayBillService.add(bill);  // 使用自定义 add() 方法，非 MyBatis-Plus 原生 insert()
// 根据 v3Result 返回 SUCCESS_TIP 或 ErrorTip(4001)
```

### 2. 新增 XcxController 接口（在现有 `XcxController.java` 类中追加，无需新建类；无 @Permission，thirdSessionKey 鉴权）

> **openid 一致性说明**：`biz_accident.openid` 在 `newAdd` 时通过 `wxSession.getOpenId()` 写入；`checkSuccess` 通过 `bizWxUser.getOpenid()` 取 openid 进行转账，而 `bizWxUser` 是通过 `accident.getOpenid()` 关联查出的。两者来源相同，均为微信 openid，可安全用于 JOIN 查询。

#### `GET /api/v1/wx/accid/list` — 本人上传记录
- 入参：`thirdSessionKey`（必填）、`page`（默认1）、`pageSize`（默认10）
- SQL：`SELECT a.id, a.video, a.acc_img, a.status, a.create_time, a.reason, b.amount AS reward_amount FROM biz_accident a LEFT JOIN biz_wxpay_bill b ON b.accid=a.id AND b.status=0 WHERE a.openid=#{openid} ORDER BY a.create_time DESC`
- 返回：`id`、`video`、`accImg`、`status`（1/2/3）、`createTime`、`reason`、`rewardAmount`（null=未发放，单位元）
- 错误：530=未登录；空列表返回 `{errorCode:0,data:{list:[],total:0}}`

#### `GET /api/v1/wx/reward/list` — 奖励明细
- 入参：`thirdSessionKey`（必填）、`page`（默认1）、`pageSize`（默认10）
- SQL：`SELECT b.id, b.amount, b.pay_time, a.address AS accident_address FROM biz_wxpay_bill b JOIN biz_accident a ON a.id=b.accid WHERE a.openid=#{openid} AND b.status=0 ORDER BY b.pay_time DESC`
- 返回：`id`、`amount`（元）、`payTime`、`accidentAddress`（可能为null）

#### `GET /api/v1/wx/user/stats` — 奖励汇总
- 入参：`thirdSessionKey`（必填）
- SQL：`SELECT COALESCE(SUM(b.amount),0) FROM biz_wxpay_bill b JOIN biz_accident a ON a.id=b.accid WHERE a.openid=#{openid} AND b.status=0`
- 返回：`totalReward`（元，无记录时为0）

### 3. Mapper/Service 方法签名

每个 Mapper 方法需在 Java 接口中声明，并在对应的 `*Mapper.xml` 中添加 `<select>` 块实现。

`AccdMapper.xml` 新增两个查询（分页版 + 汇总版）：

```java
// AccdMapper.java
List<Map<String, Object>> selectAccidentByOpenid(
    @Param(\"openid\") String openid,
    @Param(\"page\") int page,
    @Param(\"pageSize\") int pageSize
);

// BizWxpayBillMapper.java
List<Map<String, Object>> selectRewardListByOpenid(
    @Param(\"openid\") String openid,
    @Param(\"page\") int page,
    @Param(\"pageSize\") int pageSize
);

BigDecimal sumRewardByOpenid(@Param(\"openid\") String openid);
```

分页 SQL 统一使用 `LIMIT #{pageSize} OFFSET #{offset}`，offset = (page-1)*pageSize。

## 前端设计

### 1. `upload-history` 页面改造

**JS 改动**（`pages/upload-history/upload-history.js`）：
- 将接口从 `/file/list` 改为 `GET /api/v1/wx/accid/list?thirdSessionKey=&page=&pageSize=10`
- 返回字段映射：`accImg`（替代原 `imgUrl`）、`rewardAmount`（新增，null 表示未发放）
- 增加分页状态：`page`、`hasMore`；`onReachBottom` 触发加载更多；`onPullDownRefresh` 重置并重新加载
- errorCode=530 时调用登录页跳转逻辑（与 request.js 已有处理一致）

**WXML 改动**（`pages/upload-history/upload-history.wxml`）：
- 在每条记录的审核状态标签下方新增奖励金额行：
  ```xml
  <view wx:if=\"{{item.rewardAmount != null}}\" class=\"reward-amount\">
    奖励：¥{{item.rewardAmount}}
  </view>
  <view wx:else class=\"reward-pending\">奖励待发放</view>
  ```
- `viewDetail` 中图片字段改用 `item.accImg`（逗号分割后 split 展示）

### 2. 新增 `reward-records` 页面

新建文件：`pages/reward-records/reward-records.js/.wxml/.wxss/.json`

**JS 核心逻辑**：
```javascript
Page({
  data: { list: [], page: 1, hasMore: true, loading: false },
  onLoad() { this.loadMore() },
  onReachBottom() { if (this.data.hasMore) this.loadMore() },
  onPullDownRefresh() {
    this.setData({ list: [], page: 1, hasMore: true })
    this.loadMore().then(() => wx.stopPullDownRefresh())
  },
  async loadMore() {
    if (this.data.loading || !this.data.hasMore) return
    this.setData({ loading: true })
    const thirdSessionKey = wx.getStorageSync('thirdSessionKey') || ''
    const res = await request({ url: '/api/v1/wx/reward/list',
      data: { thirdSessionKey, page: this.data.page, pageSize: 10 } })
    const items = res.data.list || []
    this.setData({
      list: this.data.list.concat(items),
      page: this.data.page + 1,
      hasMore: items.length === 10,
      loading: false
    })
  }
})
```

**WXML 结构**：每条记录显示`奖励金额`、`发放时间`、`事故地点`（可能为空则显示"未知地点"）。列表为空时展示空状态提示。

### 3. `mine` 页面改造

**JS 改动**（`pages/mine/mine.js`）：
- `onLoad`/`onShow` 中调用 `/api/v1/wx/user/stats` 替代原 `/user/personalCenter`（或额外调用）
- 将返回的 `totalReward` 写入 `data.totalReward`
- 新增方法 `goRewardRecords()` → `wx.navigateTo({ url: '/pages/reward-records/reward-records' })`

**WXML 改动**（`pages/mine/mine.wxml`）：
- 在个人中心卡片中新增：
  ```xml
  <view class="stat-item">
    <text class="stat-label">累计奖励</text>
    <text class="stat-value">¥{{totalReward}}</text>
  </view>
  <view class="entry-item" bindtap="goRewardRecords">
    <text>奖励记录</text>
    <text class="arrow">›</text>
  </view>
  ```

### 4. `app.json` 注册新页面

在 `pages` 数组中新增：
```json
"pages/reward-records/reward-records"
```

## 实现顺序

1. 执行 DDL：`ALTER TABLE biz_wxpay_bill ADD COLUMN amount ...`
2. 去掉 `BizWxpayBill.amount` 的 `@TableField(exist = false)`
3. 在 `AccidentController.checkSuccess` V3转账后插入 `biz_wxpay_bill` 记录
4. 新增 Mapper XML 查询方法（accid/list、reward/list、user/stats）
5. 在 `XcxController` 新增三个接口
6. 改造小程序 `upload-history` 页面（接口+字段+奖励展示）
7. 新建小程序 `reward-records` 页面
8. 改造小程序 `mine` 页面（stats接口+奖励汇总+入口按钮）
9. `app.json` 注册 `reward-records` 页面
