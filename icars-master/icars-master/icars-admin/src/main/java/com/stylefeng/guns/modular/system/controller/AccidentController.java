package com.stylefeng.guns.modular.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.base.tips.ErrorTip;
import com.stylefeng.guns.core.base.tips.Tip;
import com.stylefeng.guns.core.common.annotion.BussinessLog;
import com.stylefeng.guns.core.common.annotion.Permission;
import com.stylefeng.guns.core.common.constant.Const;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.core.common.exception.BizExceptionEnum;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.support.BeanKit;
import com.stylefeng.guns.core.util.ToolUtil;
import com.stylefeng.guns.modular.system.constant.AccdStatus;
import com.stylefeng.guns.modular.system.constant.BizWxUserBlackListStatus;
import com.stylefeng.guns.modular.system.constant.dictmap.AccdDict;
import com.stylefeng.guns.modular.system.model.*;
import com.stylefeng.guns.modular.system.service.*;
import com.stylefeng.guns.modular.system.service.impl.WxSubscribeMessageService;
import com.stylefeng.guns.modular.system.utils.HttpUtils;
import com.stylefeng.guns.modular.system.vo.AccidentVo;
import com.stylefeng.guns.modular.system.warpper.AccdWarpper;
import com.stylefeng.guns.wxpay.IWxPayBizService;
import com.stylefeng.guns.wxpay.TransferResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.time.Instant;
import java.util.*;

/**
 * 车辆事故上报控制器
 *
 * @author kosan
 * @Date 2017年2月17日20:27:22
 */
@Controller
@RequestMapping("/accid")
public class AccidentController extends BaseController {

    private String PREFIX = "/biz/accid/";

    @Resource
    private IAccdService accdService;

    @Resource
    private IBizWxUserService bizWxUserService;

    @Resource
    private IDeptService deptService;

    @Resource
    private IUserService userService;

    @Resource
    private IPushRecordService pushRecordService;

    @Resource
    private IWxPayBizService wxPayBizService;

    @Resource
    private IDictService dictService;

    @Resource
    private IBizWxpayBillService bizWxpayBillService;

    @Resource
    private com.stylefeng.guns.wxpay.WxPayV3TransferService wxPayV3TransferService;

    @Resource
    private IBizWxUserGzhService bizWxUserGzhService;

    @Resource
    private WxSubscribeMessageService wxSubscribeMessageService;

    @Value("wx.videoLocalPath")
    private static String videoLocalPath;
    @Value("wx.videoHost")
    private static String videoHost;

    /**
     * 跳转到事故上报管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "accid.html";
    }

    /**
     * 跳转到推送理赔顾问页面
     */
    @RequestMapping("/accid_push_claims")
    public String addPushClaimsView() {
        return PREFIX + "accid_push_claims.html";
    }

    /**
     * 跳转到选择审核原因页面
     */
    @RequestMapping("/accid_check_reason")
    public String addCheckReasonView() {
        return PREFIX + "accid_check_reason.html";
    }

    /**
     * 跳转到推送理赔顾问页面
     */
    @RequestMapping("/redpay_choose")
    public String addCheckSuccessMoneyView() {
        return PREFIX + "redpay_choose.html";
    }

    /**
     * 跳转到推送4S页面
     */
    @RequestMapping("/accid_push")
    public String addView() {
        return PREFIX + "accid_push.html";
    }


    /**
     * 查询事故上报列表
     */
    @RequestMapping("/list")
    @Permission
    @ResponseBody
    public Object list(@RequestParam(required = false) String openid, @RequestParam(required = false) String createStartTime, @RequestParam(required = false) String createEndTime, @RequestParam(required = false) String checkStartTime,
                       @RequestParam(required = false) String checkEndTime, @RequestParam(required = false) Integer checkStatus, @RequestParam(required = false) Integer pushStatus, @RequestParam(required = false) String name) {
        Page<Accident> page = new PageFactory<Accident>().defaultPage();
        List<Map<String, Object>> accds = accdService.selectAccident(page, null, openid, createStartTime, createEndTime, checkStartTime, checkEndTime, checkStatus, pushStatus, page.getOrderByField(), page.isAsc(), name);
        page.setRecords((List<Accident>) new AccdWarpper(accds).warp());
        return super.packForBT(page);
    }

    /**
     * 统计事故红包总金额
     */
    @RequestMapping("/redpack/sum")
    @ResponseBody
    public Object redpackSum(@RequestParam(required = false) String openid, @RequestParam(required = false) String createStartTime, @RequestParam(required = false) String createEndTime,
                             @RequestParam(required = false) String checkStartTime, @RequestParam(required = false) String checkEndTime, @RequestParam(required = false) Integer checkStatus,
                             @RequestParam(required = false) Integer pushStatus, @RequestParam(required = false) String name) {
        BigDecimal redpack = accdService.countAccidentRedPack(openid, createStartTime, createEndTime, checkStartTime, checkEndTime, checkStatus, pushStatus, name);
        List<Map<String, Object>> countByGroups = accdService.countAccidentRedPackByGroup(openid, createStartTime, createEndTime, checkStartTime, checkEndTime, checkStatus, pushStatus, name);
        StringBuilder sb = new StringBuilder();
        sb.append(redpack == null ? 0 : redpack).append("元，事故红包明细: ");
        for (Map<String, Object> map : countByGroups) {
            Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> obj = it.next();
                if (obj.getKey() != null) {
                    if ("amount".equals(obj.getKey())) {
                        sb.append(obj.getValue()).append("元（");
                    } else if ("counts".equals(obj.getKey())) {
                        sb.append(obj.getValue()).append("个） ");
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * 查询事故推送4s门店列表
     */
    @RequestMapping("/pushFsList")
    @Permission
    @ResponseBody
    public Object pushFsList(@RequestParam(required = false) String name, @RequestParam(required = true) Integer accid, @RequestParam(required = false) String carType) {
        List<Map<String, Object>> depts = deptService.listFours(name, carType);
        Accident accident = accdService.selectById(accid);
        depts = accdService.sortByDistanceAsc(accident, depts);
        return depts;
    }

    /**
     * 查询事故推送目标理赔顾问列表
     */
    @RequestMapping("/pushClaimsList")
    @Permission({Const.ADMIN_NAME, Const.SUB_ADMIN_NAME})
    @ResponseBody
    public Object pushClaimsList(User user) {
        List<Map<String, Object>> users = userService.selectUsersForPush(user);
        return users;
    }

    /**
     * 查询订单推送目标员工列表
     */
    @RequestMapping("/pushMaintainList")
    @Permission({Const.ADMIN_NAME, Const.SUB_ADMIN_NAME})
    @ResponseBody
    public Object pushCMaintainList(User user) {
        List<Map<String, Object>> users = userService.selectUsersForMaintainPush(user);
        return users;
    }

    /**
     * 事故推送4S店
     */
    @RequestMapping("/pushFours")
    @BussinessLog(value = "事故推送4S店", key = "accdId", dict = AccdDict.class)
    @Permission({Const.ADMIN_NAME, Const.SUB_ADMIN_NAME})
    @ResponseBody
    public Tip pushFours(@RequestParam Integer accdId, @RequestParam Integer deptid) {
        if (ToolUtil.isEmpty(accdId) || ToolUtil.isEmpty(deptid)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        Accident accident = accdService.selectById(accdId);
        List<User> users = userService.selectList(new EntityWrapper<User>().eq("deptid", deptid));
        List<PushRecord> records = new ArrayList<>();
        List<String> accounts = new ArrayList<>();
        for (User user : users) {
            PushRecord var1 = pushRecordService.selectOne(new EntityWrapper<PushRecord>().eq("accid", accident.getId()).eq("account", user.getAccount()));
            if (var1 != null) {
                var1.setStatus(0);
                var1.setModifyTime(new Date());
                records.add(var1);
            } else {
                PushRecord pushRecord = new PushRecord();
                pushRecord.setAccid(accident.getId());
                pushRecord.setAddress(accident.getAddress());
                pushRecord.setVideo(accident.getVideo());
                pushRecord.setCreateTime(new Date());
                pushRecord.setStatus(0);
                pushRecord.setAccount(user.getAccount());
                pushRecord.setDeptid(user.getDeptid());
                records.add(pushRecord);
            }
            accounts.add(user.getAccount());
        }
        //批量添加目标4S店业务员事故推送记录
        pushRecordService.insertOrUpdateBatch(records);
        //推送websocket通知 4S店
        AccidentVo var1 = new AccidentVo();
        BeanKit.copyProperties(accident, var1);
        accdService.pushFs(var1, accounts);
        return SUCCESS_TIP;
    }

    /**
     * 事故推送理赔顾问
     */
    @RequestMapping("/pushClaims")
    @Permission({Const.ADMIN_NAME, Const.SUB_ADMIN_NAME})
    @ResponseBody
    public Tip pushClaims(@RequestParam Integer accdId, @RequestParam Integer deptid, @RequestParam String account, @RequestParam(required = false) BigDecimal accLevelValue) {
        if (ToolUtil.isEmpty(accdId) || ToolUtil.isEmpty(deptid) || ToolUtil.isEmpty(account)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        Accident accident = accdService.selectById(accdId);
        List<PushRecord> records = new ArrayList<>();
        List<String> accounts = new ArrayList<>();
        PushRecord var1 = pushRecordService.selectOne(new EntityWrapper<PushRecord>().eq("accid", accident.getId()).eq("deptid", deptid));
        if (var1 != null) {
            if (var1.getAccount().equals(account)) {
                accLevelValue = BigDecimal.ZERO;
            }
            var1.setStatus(0);
            var1.setAccount(account);
            var1.setModifyTime(new Date());
            records.add(var1);
        } else {
            PushRecord pushRecord = new PushRecord();
            pushRecord.setAccid(accident.getId());
            pushRecord.setAddress(accident.getAddress());
            pushRecord.setVideo(accident.getVideo());
            pushRecord.setCreateTime(new Date());
            pushRecord.setStatus(0);
            pushRecord.setAccount(account);
            pushRecord.setDeptid(deptid);
            records.add(pushRecord);
        }
        accounts.add(account);
        //添加或更新目标理赔顾问事故推送记录
//        pushRecordService.insertOrUpdateBatch(records);
        pushRecordService.pushClaims(records, account, accLevelValue);
        //推送websocket通知 理赔顾问
        AccidentVo var2 = new AccidentVo();
        BeanKit.copyProperties(accident, var2);
        accdService.pushClaims(var2, accounts);
        return SUCCESS_TIP;
    }

    /**
     * 审核通过事故信息
     */
    @RequestMapping("/checkSuccess")
    @BussinessLog(value = "审核事故通过", key = "accdId", dict = AccdDict.class)
    @Permission({Const.ADMIN_NAME, Const.SUB_ADMIN_NAME})
    @ResponseBody
    public Tip checkSuccess(@RequestParam Integer accdId, @RequestParam BigDecimal amount, @RequestParam(required = false) String reason) throws Exception {
        if (ToolUtil.isEmpty(accdId) || amount == null) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }

        // ========== 校验余额是否充足 ==========
        // 要求余额 >= 红包金额 × 2，提供安全缓冲以应对：
        // 1. 并发审核导致的余额快速消耗
        // 2. 微信支付可能的手续费或其他扣款
        // 3. 避免余额刚好够用时因微小差异导致支付失败
        long balanceFen = wxPayV3TransferService.queryMerchantBalance();
        if (balanceFen < 0) {
            System.err.println("余额查询失败，accdId=" + accdId + "，阻止审核操作");
            return new ErrorTip(500, "余额查询失败，无法完成审核，请稍后重试");
        }
        BigDecimal balanceYuan = new BigDecimal(balanceFen)
            .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal requiredAmount = amount.multiply(new BigDecimal("2"));
        if (balanceYuan.compareTo(requiredAmount) < 0) {
            System.err.println("余额不足，accdId=" + accdId + "，当前余额=" + balanceYuan + "元，需要=" + requiredAmount + "元");
            return new ErrorTip(500, "微信支付余额不足！当前余额：" + balanceYuan.toPlainString() +
                " 元，需要：" + requiredAmount.toPlainString() + " 元，请及时充值");
        }
        System.out.println("余额校验通过，accdId=" + accdId + "，当前余额=" + balanceYuan + "元，红包金额=" + amount + "元");
        // ========== 余额校验结束 ==========

        // 先检查是否已发过红包（防止重复操作）
        BizWxpayBill existBill = bizWxpayBillService.selectOneByAccid(accdId);
        if (existBill != null) {
            return new ErrorTip(500, "操作失败，该事故已发送过红包，请勿重复操作！");
        }
        //修改事故状态，必须检查返回值
        int effectRows = this.accdService.setStatus(accdId, AccdStatus.CHECK_SUCCESS.getCode(), reason);
        if (effectRows == 0) {
            return new ErrorTip(500, "操作失败，只允许审核未审核状态的事故！");
        }
        Accident accident = accdService.selectById(accdId);
        BizWxUser bizWxUser = bizWxUserService.selectBizWxUser(accident.getOpenid(), null);

        if (bizWxUser != null && accident != null) {
            // 获取事故来源标识，用于多源通知
            String source = accident.getSource();

            // ========== 支付前再次校验余额（防止竞态条件） ==========
            long balanceFenBeforePay = wxPayV3TransferService.queryMerchantBalance();
            if (balanceFenBeforePay < 0) {
                // 余额查询失败，但状态已改，记录日志并继续尝试支付
                System.err.println("支付前余额查询失败，accdId=" + accdId + "，继续尝试支付");
            } else {
                BigDecimal balanceYuanBeforePay = new BigDecimal(balanceFenBeforePay)
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
                if (balanceYuanBeforePay.compareTo(amount) < 0) {
                    // 余额不足，状态已改但无法支付，返回特殊错误码
                    return new ErrorTip(4002, "审核通过，但余额不足无法支付！当前余额：" +
                        balanceYuanBeforePay.toPlainString() + " 元，需要：" + amount.toPlainString() + " 元");
                }
            }
            // ========== 余额二次校验结束 ==========

            // ========== 优先尝试V2公众号现金红包 ==========
            boolean redPackAttempted = false;
            boolean redPackResult = false;
            if (StringUtils.isNotEmpty(bizWxUser.getUnionId())) {
                BizWxUserGzh wxUserGzh = bizWxUserGzhService.selectOne(
                        new EntityWrapper<BizWxUserGzh>().eq("unionid", bizWxUser.getUnionId()));
                if (wxUserGzh != null && StringUtils.isNotEmpty(wxUserGzh.getOpenid())) {
                    NumberFormat nf = NumberFormat.getInstance();
                    nf.setGroupingUsed(false);
                    String amountFenStr = nf.format(amount.multiply(new BigDecimal("100")).longValue());
                    redPackAttempted = true;
                    redPackResult = wxPayBizService.autoTrigger(
                            accident.getOpenid(), accident.getId().intValue(), amountFenStr);
                    if (redPackResult) {
                        wxSubscribeMessageService.sendApprovalNotice(accident.getOpenid(), amount, bizWxUser.getWxname(), source);
                        return SUCCESS_TIP;
                    }
                    // 红包失败，autoTrigger 内部已写入失败 bill，不再重复写入
                }
            }

            // ========== Fallback: 微信商家转账V3直发 ==========
            if (!redPackAttempted || !redPackResult) {
                long amountFen = amount.multiply(new BigDecimal("100")).longValue();
                TransferResult v3Result = wxPayV3TransferService.transferToUser(
                        bizWxUser.getOpenid(), accdId, amountFen);

                // V3转账需要自行写入账单记录（红包路径由autoTrigger内部写入）
                if (!redPackAttempted) {
                    BizWxpayBill bill = new BizWxpayBill();
                    bill.setAccid(accdId);
                    bill.setAmount(amount);
                    bill.setPayTime(new Date());
                    bill.setCreateTime(new Date());
                    if (v3Result.isSuccess()) {
                        bill.setStatus(2); // 待用户确认收款
                        bill.setPackageInfo(v3Result.getPackageInfo());
                        bill.setOutBillNo(v3Result.getOutBillNo());
                    } else {
                        bill.setStatus(1); // 转账失败
                    }
                    bizWxpayBillService.add(bill);
                }

                if (v3Result.isSuccess()) {
                    wxSubscribeMessageService.sendApprovalNotice(bizWxUser.getOpenid(), amount, bizWxUser.getWxname(), source);
                    return SUCCESS_TIP;
                } else {
                    return new ErrorTip(4001, "审核通过，支付失败");
                }
            }
        }
        return new ErrorTip(4001, "审核通过，支付失败");
    }

    public static String md5(String input) throws Exception {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 查询微信支付余额
     */
    @RequestMapping(value = "/balance", method = RequestMethod.GET)
    @Permission
    @ResponseBody
    public Object queryBalance() {
        long balanceFen = wxPayV3TransferService.queryMerchantBalance();
        if (balanceFen < 0) {
            return new ErrorTip(500, "余额查询失败");
        }
        BigDecimal balanceYuan = new BigDecimal(balanceFen)
            .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        Map<String, Object> result = new HashMap<>();
        result.put("balance", balanceYuan);
        result.put("balanceStr", balanceYuan.toPlainString() + " 元");
        return result;
    }

    /**
     * 推送事故信息
     */
    @RequestMapping("/push")
    @BussinessLog(value = "推送事故信息", key = "accdId", dict = AccdDict.class)
    @Permission({Const.ADMIN_NAME, Const.SUB_ADMIN_NAME})
    @ResponseBody
    public Tip push(@RequestParam Integer accdId, @RequestParam List<String> thirdSessionKeys) {
        if (ToolUtil.isEmpty(accdId) || thirdSessionKeys == null || thirdSessionKeys.size() <= 0) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        Accident accident = accdService.selectById(accdId);
        AccidentVo accidentVo = new AccidentVo();
        BeanKit.copyProperties(accident, accidentVo);
        this.accdService.push(accidentVo, thirdSessionKeys);
        return SUCCESS_TIP;
    }

    /**
     * 审核失败事故信息
     */
    @RequestMapping("/checkFail")
    @BussinessLog(value = "审核事故不通过", key = "accdId", dict = AccdDict.class)
    @Permission({Const.ADMIN_NAME, Const.SUB_ADMIN_NAME})
    @ResponseBody
    public Tip checkFail(@RequestParam Integer accdId, @RequestParam String reason) {
        if (ToolUtil.isEmpty(accdId)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        int effectRows = this.accdService.setStatus(accdId, AccdStatus.CHECK_FAIL.getCode(), reason);
        if (effectRows != 0) {
            return SUCCESS_TIP;
        }
        return new ErrorTip(500, "操作失败，只允许审核未审核状态事故");
    }

    /**
     * 改变用户黑名单状态
     */
    @RequestMapping("/addBlackList")
    @BussinessLog(value = "改变用户黑名单状态", key = "openid", dict = AccdDict.class)
    @Permission({Const.ADMIN_NAME, Const.SUB_ADMIN_NAME})
    @ResponseBody
    public Tip addBlackList(@RequestParam String openid, @RequestParam Integer blackList) {
        if (ToolUtil.isEmpty(openid) || blackList == null) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        BizWxUser bizWxUser = new BizWxUser();
        if (blackList == BizWxUserBlackListStatus.IS_BLACKLIST.getCode()) {
            bizWxUser.setBlackList(BizWxUserBlackListStatus.IS_BLACKLIST.getCode());
        } else if (blackList == BizWxUserBlackListStatus.INIT.getCode()) {
            bizWxUser.setBlackList(BizWxUserBlackListStatus.INIT.getCode());
        } else {
            return new ErrorTip(500, "非法操作，非法状态");
        }
        boolean flag = this.bizWxUserService.update(bizWxUser, new EntityWrapper<BizWxUser>().eq("openid", openid));
        if (flag) {
            return SUCCESS_TIP;
        }
        return new ErrorTip(500, "操作失败，未找到该用户");
    }

    /**
     * 视频/图片代理接口 - 解决CDN域名SSL证书问题导致浏览器无法直接加载
     */
    @RequestMapping("/media/proxy")
    public void mediaProxy(@RequestParam String url, javax.servlet.http.HttpServletResponse response) {
        if (StringUtils.isEmpty(url) || !url.startsWith("https://cdn.meisaizhixing.cn/")) {
            response.setStatus(403);
            return;
        }
        try {
            // 创建信任所有证书的 SSL 上下文（仅用于本连接，不影响全局）
            javax.net.ssl.SSLContext sslContext = javax.net.ssl.SSLContext.getInstance("TLS");
            sslContext.init(null, new javax.net.ssl.TrustManager[]{
                new javax.net.ssl.X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() { return new java.security.cert.X509Certificate[0]; }
                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
                }
            }, new java.security.SecureRandom());

            java.net.URL mediaUrl = new java.net.URL(url);
            javax.net.ssl.HttpsURLConnection conn = (javax.net.ssl.HttpsURLConnection) mediaUrl.openConnection();
            conn.setSSLSocketFactory(sslContext.getSocketFactory());
            conn.setHostnameVerifier((hostname, session) -> true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setRequestMethod("GET");

            int status = conn.getResponseCode();
            if (status != 200) {
                response.setStatus(status);
                return;
            }

            String contentType = conn.getContentType();
            if (contentType != null) {
                response.setContentType(contentType);
            }
            long contentLength = conn.getContentLengthLong();
            if (contentLength > 0) {
                response.setContentLengthLong(contentLength);
            }

            // 流式转发
            try (java.io.InputStream in = conn.getInputStream();
                 java.io.OutputStream out = response.getOutputStream()) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                out.flush();
            }
        } catch (Exception e) {
            response.setStatus(500);
        }
    }
}
