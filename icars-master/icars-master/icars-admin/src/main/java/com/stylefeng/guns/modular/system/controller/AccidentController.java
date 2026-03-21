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
import com.stylefeng.guns.modular.system.utils.HttpUtils;
import com.stylefeng.guns.modular.system.vo.AccidentVo;
import com.stylefeng.guns.modular.system.warpper.AccdWarpper;
import com.stylefeng.guns.wxpay.IWxPayBizService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
        //修改事故状态
        this.accdService.setStatus(accdId, AccdStatus.CHECK_SUCCESS.getCode(), reason);
        Accident accident = accdService.selectById(accdId);
        BizWxUser bizWxUser = bizWxUserService.selectBizWxUser(accident.getOpenid());
        BizWxpayBill bizWxpayBill = bizWxpayBillService.selectOneByAccid(accdId);
        if (bizWxpayBill != null) {
            return new ErrorTip(500, "操作失败，该事故已发送过红包，请勿重复操作！");
        }

        if (bizWxUser != null && accident != null) {

            String uid = "10815568";
            String apikey = "amiba500500";
            long reqtick = Instant.now().getEpochSecond();
            BigDecimal multiply = amount.multiply(new BigDecimal("100"));
            String type = "1";
            //获取当前时间戳
            Map<String, String> data = new HashMap<String, String>();
            data.put("uid", uid);
            data.put("type", type);
            data.put("orderid", String.valueOf(accdId));
            data.put("money", String.valueOf(multiply));
            data.put("reqtick", String.valueOf(reqtick));
            data.put("openid", bizWxUser.getThirdOpenId());
            data.put("apikey", apikey);
            String sign = md5(uid + type + accdId + multiply + reqtick + bizWxUser.getThirdOpenId() + apikey);
            //拼接地址
            String url = "https://www.yaoyaola.net/exapi/SendRedPackToOpenid?" + "uid=" + uid + "&type=" + type + "&orderid=" + accdId + "&money=" + multiply + "&reqtick=" + reqtick + "&openid=" + bizWxUser.getThirdOpenId() + "&sign=" + sign + "&title=感谢参与提报事故" + "&sendname=阿米巴拍事故";
            System.out.println(url);
            String result1 = HttpUtils.sendHttpGet(url);
            JSONObject resultJOSN = JSONObject.parseObject(result1);
            System.out.println("摇摇啦：");
            System.out.println(resultJOSN);
            String errcode = resultJOSN.getString("errcode");
            //自动派单逻辑已经扣了推送员费用
            //扣除推送人员预存款信息费
//                List<PushRecord> var1 = pushRecordService.selectList(new EntityWrapper<PushRecord>().eq("accid", accident.getId()));
//                if (var1 != null && var1.size() > 0) {
//                    for (PushRecord pr : var1) {
//                        User user = userService.getByAccount(pr.getAccount());
//                        if ("N".equals(user.getSfkf())) {
//                            continue;
//                        }
//                        BizWxUser bizWxUser2 = bizWxUserService.selectOne(new EntityWrapper<BizWxUser>().eq("phone", user.getPhone()));
//                        BizYckBalance bizYckBalance = null;
//                        if (bizWxUser2 == null) {
//                            bizYckBalance = bizYckBalanceService.selectOne(new EntityWrapper<BizYckBalance>().eq("account", user.getAccount()));
//                        } else {
//                            bizYckBalance = bizYckBalanceService.selectOne(new EntityWrapper<BizYckBalance>().eq("openid", bizWxUser2.getOpenid()));
//                        }
//                        if (bizYckBalance == null) {
//                            BizYckBalance addVo = new BizYckBalance();
//                            if (bizWxUser2 == null) {
//                                addVo.setOpenid(user.getAccount());
//                            } else {
//                                addVo.setOpenid(bizWxUser2.getOpenid());
//                            }
//                            addVo.setAccount(user.getAccount());
//                            addVo.setBalance(BigDecimal.ZERO);
//                            addVo.setCreateTime(new Date());
//                            addVo.setModifyTime(new Date());
//                            //之前没有预存款账户记录。新增账户记录
//                            bizYckBalanceService.insert(addVo);
            // 如果要启动这段代码 需要考虑bizWxUser2为null的情况
//                            bizYckBalance = bizYckBalanceService.selectOne(new EntityWrapper<BizYckBalance>().eq("openid", bizWxUser2.getOpenid()));
//                            if (bizYckBalance == null) {
//                                bizYckBalance = bizYckBalanceService.selectOne(new EntityWrapper<BizYckBalance>().eq("account", user.getAccount()));
//                            }
//                        }
//                        //倍数
//                        double multiple = 2;
//                        Dict dict = dictService.selectOne(new EntityWrapper<Dict>().eq("name", "信息费倍数"));
//                        try {
//                            multiple = Double.parseDouble(dict.getCode());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        //推送给开放平台理赔顾问或修理厂 收取的信息费
//                        BigDecimal infoBackCount = amount.multiply(new BigDecimal(multiple));
//                        //扣除金额大于0
//                        if (infoBackCount.compareTo(BigDecimal.ZERO) == 1) {
//                            //余额足够扣除
//                            if (bizYckBalance != null) {
            // 如果要启动这段代码 需要考虑bizWxUser2为null的情况
//                                boolean flag = bizYckBalanceService.reduceBalance(infoBackCount, user.getAccount(), new Date());
//                                boolean flag = bizYckBalanceService.reduceBalance(infoBackCount, bizWxUser2.getOpenid(), new Date());
//                                if (flag) {
//                                    if (infoBackCount.compareTo(BigDecimal.ZERO) == 1) {
//                                        // 记录扣款明细
//                                        BizYckCzmx bizYckCzmx = new BizYckCzmx();
//                                        bizYckCzmx.setAmount(infoBackCount.negate());
//                                        bizYckCzmx.setDetailType(BizYckCzmxStatus.EXPEND.getCode());
            // 如果要启动这段代码 需要考虑bizWxUser2为null的情况
//                                        bizYckCzmx.setOpenid(bizWxUser2.getOpenid());
//                                        bizYckCzmx.setOperator(ShiroKit.getUser().getAccount());
//                                        bizYckCzmx.setCreateTime(new Date());
//                                        bizYckCzmx.setAccid(accdId);
//                                        bizYckCzmxService.insert(bizYckCzmx);
//                                    }
//                                }
//                            } else {
//                                throw new GunsException(BizExceptionEnum.ACCID_BALANCE_REDUCE_LIMIT);
//                            }
//                        }
//                    }
//                }

            //为信息上报人发放红包奖励
//            NumberFormat nf = NumberFormat.getInstance();
//            nf.setGroupingUsed(false);
////                boolean triggerResult = alipayService.autoTrigger(bizWxUser.getAlipayAccount(), accident.getId().intValue());
//            //发送事故红包奖励
//            boolean triggerResult = wxPayBizService.autoTrigger(accident.getOpenid(), accident.getId().intValue(), nf.format(amount.multiply(new BigDecimal(100)).doubleValue()));
//            //为推广父用户发放提成红包
//            if (StringUtils.isNotEmpty(bizWxUser.getExtensionAccount())) {
//                BizWxUser bizWxUser2 = bizWxUserService.selectBizWxUser(bizWxUser.getExtensionAccount());
//                if (bizWxUser2 != null) {
//                    double percent = 0.2;
//                    Dict dict = dictService.selectOne(new EntityWrapper<Dict>().eq("name", "红包提成比例"));
//                    try {
//                        percent = Double.parseDouble(dict.getCode());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    double percentageAmount = amount.multiply(new BigDecimal(100)).multiply(new BigDecimal(percent)).doubleValue();
//                    percentageAmount = percentageAmount >= 100 ? percentageAmount : 100;
//                    wxPayBizService.autoTrigger(bizWxUser2.getOpenid(), accident.getId().intValue() * -1, nf.format(percentageAmount));
//                }
//            }
            if (errcode.equals("0")) {
                return SUCCESS_TIP;
            } else {
                return new ErrorTip(4001, "审核通过，支付失败");
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
}
