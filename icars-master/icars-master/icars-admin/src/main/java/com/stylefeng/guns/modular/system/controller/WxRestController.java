package com.stylefeng.guns.modular.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.alipay.IAlipayService;
import com.stylefeng.guns.config.properties.WxPayProperties;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.core.common.constant.state.ManagerStatus;
import com.stylefeng.guns.core.common.exception.BizExceptionEnum;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.support.BeanKit;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.core.util.JedisUtil;
import com.stylefeng.guns.core.util.ToolUtil;
import com.stylefeng.guns.huawei.demo.CallNotifyMain;
import com.stylefeng.guns.modular.system.constant.*;
import com.stylefeng.guns.modular.system.model.*;
import com.stylefeng.guns.modular.system.service.*;
import com.stylefeng.guns.modular.system.service.impl.WxService;
import com.stylefeng.guns.modular.system.service.impl.appServiceImpl.AppService;
import com.stylefeng.guns.modular.system.utils.UrlUtil;
import com.stylefeng.guns.modular.system.vo.AccidentListVo;
import com.stylefeng.guns.modular.system.vo.AccidentVo;
import com.stylefeng.guns.modular.system.vo.WxMyMessageVo;
import com.stylefeng.guns.modular.system.warpper.*;
import com.stylefeng.guns.netty.service.IWebSocketMessagePushService;
import com.stylefeng.guns.wxpay.IWxPayBizService;
import com.stylefeng.guns.wxpay.WXPayConstants;
import com.stylefeng.guns.wxpay.WXPayUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

/**
 * 微信用户认证相关
 *
 * @author kosans
 */
@RestController
public class WxRestController extends BaseController {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IAccdService accdService;

    @Resource(name = "styleFengWxService")
    private WxService wxService;

    @Autowired
    private IWxService iWxService;

    @Autowired
    private IBizWxUserService bizWxUserService;

    @Autowired
    private IBizAlipayBillService bizAlipayBillService;

    @Autowired
    private IBizWxpayBillService bizWxpayBillService;

    @Autowired
    private IPushRecordService pushRecordService;

    @Autowired
    IWebSocketMessagePushService webSocketMessagePushService;

    @Autowired
    IAlipayService alipayService;

    @Autowired
    IWxPayBizService wxPayBizService;

    @Autowired
    IBizClaimService bizClaimService;

    @Autowired
    IBizClaimerShowService bizClaimerShowService;

    @Autowired
    private INoticeService noticeService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IOpenClaimService openClaimService;

    @Autowired
    private IDeptService deptService;

    @Autowired
    IDictService iDictService;

    @Autowired
    IBizInsuranceCompanyService bizInsuranceCompanyService;

    @Autowired
    IBizWxpayOrderService bizWxpayOrderService;

    @Autowired
    private WxPayProperties wxPayProperties;

    @Autowired
    private IBizYckBalanceService bizYckBalanceService;

    @Autowired
    private IBizYckCzmxService bizYckCzmxService;

    @Autowired
    private IBizWxPayRecordService bizWxPayRecordService;

    @Autowired
    private IBizMaintainPackageService bizMaintainPackageService;

    @Autowired
    private IBizMaintainPackageOrderService bizMaintainPackageOrderService;


    @Autowired
    private AppService appService;


    @Autowired
    private JedisUtil jedisUtil;


    @Autowired
    CallNotifyMain callNotifyMain;

    /**
     * 事故上报
     *
     * @param
     * @return
     */
    @ApiOperation(value = "新增事故信息", notes = "上传事故接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "视频", required = true, dataType = "MultipartFile"),
            @ApiImplicitParam(name = "lng", value = "经度", required = true, dataType = "Bigdecimal"),
            @ApiImplicitParam(name = "lat", value = "纬度", required = true, dataType = "Bigdecimal"),
            @ApiImplicitParam(name = "address", value = "上报地址名称", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/accid/add", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity add(@RequestParam(required = true, value = "file") MultipartFile file, @Valid AccidentVo accidentVo, @RequestParam String thirdSessionKey, @RequestParam Integer isImage, @RequestParam(required = false) String source, BindingResult result) {
        Map<String, Object> resultMap = new HashMap<>();
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (null == file) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("上报异常");
        }
        if (result.hasErrors()) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("微信session无效或为空");
            return apiResponseEntity;
        }
        // 检查用户是否已授权手机号
        BizWxUser wxUser = bizWxUserService.selectBizWxUser(wxSession.getOpenId());
        if (wxUser == null || StringUtils.isEmpty(wxUser.getPhone())) {
            apiResponseEntity.setErrorCode(5003);
            apiResponseEntity.setErrorMsg("请先授权手机号后再上报");
            return apiResponseEntity;
        }
        // 检查用户是否在黑名单中
        if (wxUser.getBlackList() != null && wxUser.getBlackList() == 1) {
            apiResponseEntity.setErrorCode(5004);
            apiResponseEntity.setErrorMsg("您的账号已被限制上报");
            return apiResponseEntity;
        }
        // 设置来源标识
        if (StringUtils.isNotEmpty(source)) {
            accidentVo.setSource(source);
        }
        try {
            Integer accid = accdService.add(file, accidentVo, thirdSessionKey, isImage);
            accidentVo.setId(accid.longValue());
            Accident accident = accdService.selectById(accid);
            resultMap.put("video", accident.getVideo());
            resultMap.put("accId", accid);
            apiResponseEntity.setData(resultMap);
            AccidentVo var1 = new AccidentVo();
            BeanKit.copyProperties(accident, var1);
            webSocketMessagePushService.pushInner(var1);
            if (BussinessSwitch.autoPushSwitch) {
                this.accdService.autoPush(var1);
            }
        } catch (IOException e) {
            e.printStackTrace();
            apiResponseEntity.setErrorCode(1002);
            apiResponseEntity.setErrorMsg("网络异常");
            return apiResponseEntity;
        }

        List<PushRecord> records = new ArrayList<>();
        List<String> accounts = new ArrayList<>();
        User closestUser = appService.getClosestUser(accidentVo.getLat().doubleValue(), accidentVo.getLng().doubleValue());
        if (closestUser == null || closestUser.getDeptid() == null || closestUser.getDeptid() == 0) {
            apiResponseEntity.setErrorCode(1004);
            apiResponseEntity.setErrorMsg("未找到合适的理赔员");

//
//            PushRecord pushRecord = new PushRecord();
//            pushRecord.setAccid(accidentVo.getId());
//            pushRecord.setAddress(accidentVo.getAddress());
//            pushRecord.setVideo(accidentVo.getVideo());
//            pushRecord.setCreateTime(new Date());
//            pushRecord.setStatus(0);
//            pushRecord.setAccount("1");
//            pushRecord.setDeptid(30);
//            records.add(pushRecord);
//            accounts.add(closestUser.getAccount());
            return apiResponseEntity;
        }
        //获取到最近的理赔员
        String sfkf = closestUser.getSfkf();
        //
        BigDecimal accLevelValue = BigDecimal.ZERO;
        if (sfkf == null || !"N".equals(sfkf)) {
            //扣费用户
            String koufei = "20";
            String koufei2 = jedisUtil.get("KOUFEI");
            if (koufei2 == null || koufei2.equals("") || koufei2.equals("null")) {

            } else {
                koufei = koufei2;
            }

            BigDecimal newbig = new BigDecimal(koufei);
            accLevelValue = newbig;
        }
        PushRecord pushRecord = new PushRecord();
        pushRecord.setAccid(accidentVo.getId());
        pushRecord.setAddress(accidentVo.getAddress());
        pushRecord.setVideo(accidentVo.getVideo());
        pushRecord.setCreateTime(new Date());
        pushRecord.setStatus(0);
        pushRecord.setAccount(closestUser.getAccount());
        Integer deptid = closestUser.getDeptid();
        if (deptid == null || deptid == 0) {
            closestUser.setDeptid(30);
        }
        pushRecord.setDeptid(closestUser.getDeptid());
        records.add(pushRecord);
        accounts.add(closestUser.getAccount());
        //扣理赔顾问滴钱
//        pushRecordService.insertOrUpdateBatch(records);
        pushRecordService.pushClaims(records, closestUser.getAccount(), accLevelValue);

        try {
            appService.sendSms("3", closestUser.getPhone(), closestUser.getName());
            callNotifyMain.sendVoice(closestUser.getName(), closestUser.getPhone());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //推送websocket通知 理赔顾问
        AccidentVo var2 = new AccidentVo();
        BeanKit.copyProperties(accidentVo, var2);
        accdService.pushClaims(var2, accounts);
        accdService.addRedis();
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("成功");
        return apiResponseEntity;
    }

    @ApiOperation(value = "绑定新的openId", notes = "绑定新的openid")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid", value = "openid", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/api/v1/wx/accid/saveOpenId", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity saveOpenId(@Valid @RequestBody AccidentVo accidentVo, @RequestParam String thirdSessionKey, BindingResult result) {
        Map<String, Object> resultMap = new HashMap<>();
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (result.hasErrors()) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("微信session无效或为空");
            return apiResponseEntity;
        }
        List<BizWxUser> bizWxUsers = bizWxUserService.selectByOpenid(wxSession.getOpenId());
        if (!CollectionUtils.isEmpty(bizWxUsers)) {
            BizWxUser bizWxUser = bizWxUsers.get(0);
            if (bizWxUser.getThirdOpenId() != null) {
                apiResponseEntity.setErrorCode(0);
                apiResponseEntity.setErrorMsg("成功");
                return apiResponseEntity;
            }
        }
        bizWxUserService.saveOpenId(accidentVo.getOpenid(), wxSession.getOpenId());
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("成功");
        return apiResponseEntity;
    }


    @ApiOperation(value = "查询有无绑定openId", notes = "查询有无绑定openId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid", value = "openid", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/api/v1/wx/accid/selectOpenId", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity selectOpenId(@Valid @RequestBody AccidentVo accidentVo, @RequestParam String thirdSessionKey, BindingResult result) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("微信session无效或为空");
            return apiResponseEntity;
        }
        List<BizWxUser> bizWxUsers = bizWxUserService.selectByOpenid(wxSession.getOpenId());
        if (!CollectionUtils.isEmpty(bizWxUsers)) {
            BizWxUser bizWxUser = bizWxUsers.get(0);
            if (bizWxUser.getThirdOpenId() != null) {
                apiResponseEntity.setErrorCode(0);
                apiResponseEntity.setErrorMsg("成功");
                apiResponseEntity.setData(1);
            } else {
                apiResponseEntity.setData(0);
            }
        } else {
            apiResponseEntity.setData(0);
        }
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("成功");
        return apiResponseEntity;
    }


    /**
     * 事故上报
     *
     * @param
     * @return
     */
    @ApiOperation(value = "新的新增事故信息", notes = "新的上传事故接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "url", value = "视频", required = true, dataType = "String"),
            @ApiImplicitParam(name = "lng", value = "经度", required = true, dataType = "Bigdecimal"),
            @ApiImplicitParam(name = "lat", value = "纬度", required = true, dataType = "Bigdecimal"),
            @ApiImplicitParam(name = "openid", value = "openid", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/api/v1/wx/accid/newAdd", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity newAdd(@Valid @RequestBody AccidentVo accidentVo, @RequestParam String thirdSessionKey, @RequestParam(required = false) String source, BindingResult result) {
        Map<String, Object> resultMap = new HashMap<>();
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (result.hasErrors()) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("微信session无效或为空");
            return apiResponseEntity;
        }
        // 检查用户是否已授权手机号
        BizWxUser wxUserCheck = bizWxUserService.selectBizWxUser(wxSession.getOpenId());
        if (wxUserCheck == null || StringUtils.isEmpty(wxUserCheck.getPhone())) {
            apiResponseEntity.setErrorCode(5003);
            apiResponseEntity.setErrorMsg("请先授权手机号后再上报");
            return apiResponseEntity;
        }
        // 检查用户是否在黑名单中
        if (wxUserCheck.getBlackList() != null && wxUserCheck.getBlackList() == 1) {
            apiResponseEntity.setErrorCode(5004);
            apiResponseEntity.setErrorMsg("您的账号已被限制上报");
            return apiResponseEntity;
        }
        // 设置来源标识（优先使用参数传入的source，否则用body中的）
        if (StringUtils.isNotEmpty(source)) {
            accidentVo.setSource(source);
        }
        try {
            Integer accid = accdService.newAdd(accidentVo, thirdSessionKey);
            accidentVo.setId(accid.longValue());
            Accident accident = accdService.selectById(accid);
            resultMap.put("video", accident.getVideo());
            resultMap.put("accId", accid);
            apiResponseEntity.setData(resultMap);
            AccidentVo var1 = new AccidentVo();
            BeanKit.copyProperties(accident, var1);
            webSocketMessagePushService.pushInner(var1);
            if (BussinessSwitch.autoPushSwitch) {
                this.accdService.autoPush(var1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            apiResponseEntity.setErrorCode(1002);
            apiResponseEntity.setErrorMsg("网络异常");
            return apiResponseEntity;
        }

        accdService.addRedis();
        List<PushRecord> records = new ArrayList<>();
        List<String> accounts = new ArrayList<>();
        User closestUser = appService.getClosestUser(accidentVo.getLat().doubleValue(), accidentVo.getLng().doubleValue());
        if (closestUser == null || closestUser.getDeptid() == null || closestUser.getDeptid() == 0) {
            apiResponseEntity.setErrorCode(0);
            apiResponseEntity.setErrorMsg("成功");
            return apiResponseEntity;
        }
//        //获取到最近的理赔员
//        String sfkf = closestUser.getSfkf();
//        //
//        BigDecimal accLevelValue = BigDecimal.ZERO;
//        if (sfkf == null || !"N".equals(sfkf)) {
//            //扣费用户
//            String koufei = "20";
//            String koufei2 = jedisUtil.get("KOUFEI");
//            if (koufei2 == null || koufei2.equals("") || koufei2.equals("null")) {
//
//            } else {
//                koufei = koufei2;
//            }
//
//            BigDecimal newbig = new BigDecimal(koufei);
//            accLevelValue = newbig;
//        }
//        PushRecord pushRecord = new PushRecord();
//        pushRecord.setAccid(accidentVo.getId());
//        pushRecord.setAddress(accidentVo.getAddress());
//        pushRecord.setVideo(accidentVo.getVideo());
//        pushRecord.setCreateTime(new Date());
//        pushRecord.setStatus(0);
//        pushRecord.setAccount(closestUser.getAccount());
//        Integer deptid = closestUser.getDeptid();
//        if (deptid == null || deptid == 0) {
//            closestUser.setDeptid(30);
//        }
//        pushRecord.setDeptid(closestUser.getDeptid());
//        records.add(pushRecord);
//        accounts.add(closestUser.getAccount());
//        //扣理赔顾问滴钱
////        pushRecordService.insertOrUpdateBatch(records);
//        pushRecordService.pushClaims(records, closestUser.getAccount(), accLevelValue);

        try {
            appService.sendSms("3", closestUser.getPhone(), closestUser.getName());
            callNotifyMain.sendVoice(closestUser.getName(), closestUser.getPhone());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //推送websocket通知 理赔顾问
        AccidentVo var2 = new AccidentVo();
        BeanKit.copyProperties(accidentVo, var2);
        accdService.pushClaims(var2, accounts);

        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("成功");
        return apiResponseEntity;
    }

    /**
     * 更新事故审核状态
     */
    @ApiOperation(value = "更新事故审核状态", notes = "更新事故审核状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars 登录sessionkey", required = true, dataType = "String"),
            @ApiImplicitParam(name = "accid", value = "事故id", required = true, dataType = "int"),
            @ApiImplicitParam(name = "checked", value = "审核状态，true:审核通过  false:审核不通过", required = true, dataType = "boolean")
    })
    @RequestMapping(value = "/api/v1/wx/accid/check", method = RequestMethod.PUT, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity checkAccident(@RequestParam String thirdSessionKey, @RequestParam Integer accid, @RequestParam boolean checked) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1000);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession != null) {
            BizWxUser bizWxUser = bizWxUserService.selectBizWxUser(wxSession.getOpenId());
            if (bizWxUser == null) {
                apiResponseEntity.setErrorCode(1002);
                apiResponseEntity.setErrorMsg("无用户信息");
                return apiResponseEntity;
            }
            if (checked) {
                //更新事故状态
                int effectRows = this.accdService.setStatus(accid, AccdStatus.CHECK_SUCCESS.getCode());
                Accident accident = accdService.selectById(accid);
                if (effectRows == 1) {
                    if (bizWxUser != null && StringUtils.isNotEmpty(bizWxUser.getAlipayAccount()) && accident != null) ;
                    //支付红包
//					boolean triggerResult = alipayService.autoTrigger(bizWxUser.getAlipayAccount(), accident.getId().intValue());
                    NumberFormat nf = NumberFormat.getInstance();
//					boolean triggerResult = wxPayBizService.autoTrigger(accident.getOpenid(), accident.getId().intValue(), "120");
//					if(triggerResult){
//						apiResponseEntity.setErrorCode(0);
//						apiResponseEntity.setErrorMsg("操作成功");
//					}else{
//						apiResponseEntity.setErrorCode(4002);
//						apiResponseEntity.setErrorMsg("操作失败");
//					}
                }
            } else {
                int effectRows = this.accdService.setStatus(accid, AccdStatus.CHECK_FAIL.getCode());
                if (effectRows == 1) {
                    apiResponseEntity.setErrorCode(0);
                    apiResponseEntity.setErrorMsg("操作成功");
                }
            }
        } else {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("无效用户");
        }
        return apiResponseEntity;
    }

    /**
     * 微信用户在小程序内绑定支付宝账户
     *
     * @param
     * @return
     */
    @ApiOperation(value = "微信用户绑定支付宝", notes = "绑定用户openid与支付宝账户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars 登录sessionkey", required = true, dataType = "String"),
            @ApiImplicitParam(name = "alipayAccount", value = "支付宝账户", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/alipay/add", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity addAlipay(@RequestParam(name = "thirdSessionKey", required = true) String thirdSessionKey, @RequestParam(name = "alipayAccount", required = true) String alipayAccount) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (null == thirdSessionKey || alipayAccount == null) {
            apiResponseEntity.setErrorCode(1003);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("微信session无效或为空");
            return apiResponseEntity;
        }
        this.bizWxUserService.setAlipay(wxSession.getOpenId(), alipayAccount);
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("");
        return apiResponseEntity;
    }

    /**
     * 微信用户绑定推广员工账户
     *
     * @param
     * @return
     */
    @ApiOperation(value = "微信用户绑定推广员工账户", notes = "微信用户绑定推广员工账户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars 登录sessionkey", required = true, dataType = "String"),
            @ApiImplicitParam(name = "extensionAccount", value = "推广员工账户", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/extension/add", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity addExtensionAccount(@RequestParam(name = "thirdSessionKey") String thirdSessionKey, @RequestParam(name = "extensionAccount") String extensionAccount) {
        log.error("###微信用户绑定推广员工账户###  thirdSessionKey={};extensionAccount={}", thirdSessionKey, extensionAccount);
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey) || StringUtils.isEmpty(extensionAccount)) {
            apiResponseEntity.setErrorCode(1003);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("微信session无效或为空");
            return apiResponseEntity;
        }
        BizWxUser bizWxUser = new BizWxUser();
        log.error("###微信用户绑定推广员工账户###  wxSession={};extensionAccount={}", wxSession, extensionAccount);

        if (wxSession.getBizWxUser() == null) {
            log.error("如果从缓存里拿到的wxUser为null 从数据库获取 wxSession={}", wxSession);
            BizWxUser bizWxUser1 = this.bizWxUserService.selectBizWxUser(wxSession.getOpenId());
            wxSession.setBizWxUser(bizWxUser1);
        }
        bizWxUser.setId(wxSession.getBizWxUser().getId());
        bizWxUser.setExtensionAccount(extensionAccount);
        this.bizWxUserService.updateById(bizWxUser);
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("");
        return apiResponseEntity;
    }

    /**
     * 存储微信用户头像和昵称
     *
     * @param
     * @return
     */
    @ApiOperation(value = "存储微信用户头像和昵称", notes = "存储微信用户头像和昵称")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars 登录sessionkey", required = true, dataType = "String"),
            @ApiImplicitParam(name = "headImg", value = "微信用户头像链接", required = true, dataType = "String"),
            @ApiImplicitParam(name = "wxname", value = "微信用户昵称", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/userinfo/bind", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity bindWxUserInfo(@RequestParam(name = "thirdSessionKey", required = true) String thirdSessionKey, @RequestParam(name = "headImg", required = true) String headImg, @RequestParam(name = "wxname", required = true) String wxname) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (null == thirdSessionKey || headImg == null || wxname == null) {
            apiResponseEntity.setErrorCode(1003);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("微信session无效或为空");
            return apiResponseEntity;
        }
        BizWxUser bizWxUser = new BizWxUser();

        if (wxSession.getBizWxUser() == null) {
            log.error("如果从缓存里拿到的wxUser为null 从数据库获取 wxSession={}", wxSession);
            BizWxUser bizWxUser1 = this.bizWxUserService.selectBizWxUser(wxSession.getOpenId());
            wxSession.setBizWxUser(bizWxUser1);
        }
        bizWxUser.setId(wxSession.getBizWxUser().getId());
        bizWxUser.setHeadImg(headImg);
        bizWxUser.setWxname(wxname);
        this.bizWxUserService.updateById(bizWxUser);
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("");
        return apiResponseEntity;
    }

    /**
     * 获取推广二维码图片流
     */
    @RequestMapping("/api/v1/wx/user/user_qrCode/{thirdSessionKey}")
    public void getQrCodeImg(@PathVariable String thirdSessionKey, HttpServletResponse response) {
        if (ToolUtil.isEmpty(thirdSessionKey)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null || StringUtils.isEmpty(wxSession.getOpenId())) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        byte[] data = iWxService.getMiniqrQr(wxSession.getOpenId());
        response.setContentType("image/png");
        try {
            OutputStream stream = response.getOutputStream();
            stream.write(data);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询微信用户信息
     */
    @ApiOperation(value = "查询微信用户信息", notes = "查询微信用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars 登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/user/get", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getWxUser(@RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);

        if (wxSession != null) {
            BizWxUser bizWxUser = bizWxUserService.selectBizWxUser(wxSession.getOpenId());
            if (bizWxUser == null) {
                apiResponseEntity.setErrorCode(1002);
                apiResponseEntity.setErrorMsg("无用户信息");
                return apiResponseEntity;
            }
            if (bizWxUser.getWxname() != null) {
                String urlDecoderString = UrlUtil.getURLDecoderString(bizWxUser.getWxname());
                bizWxUser.setWxname(urlDecoderString);
            }

            bizWxUser.setCreateTime(null);
            bizWxUser.setType(null);
            bizWxUser.setOpenid(null);
            bizWxUser.setId(null);
            bizWxUser.setAlipayAccount(null);
            apiResponseEntity.setErrorCode(0);
            apiResponseEntity.setData(bizWxUser);
        } else {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
        }
        return apiResponseEntity;
    }

    /**
     * 微信用户查询事故提交记录
     */
    @ApiOperation(value = "微信用户查询事故提交记录", notes = "微信用户查询事故提交记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/accids/get", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getWxAccids(@RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        if (StringUtils.isNotEmpty(wxSession.getOpenId())) {
            Page<Accident> page = new PageFactory<Accident>().defaultPage();
            List<Map<String, Object>> accds = accdService.selectAccidentForApi(page, null, wxSession.getOpenId(), null, null, null, null, page.getOrderByField(), page.isAsc());
            page.setRecords((List<Accident>) new AccdWarpper(accds).warp());
            apiResponseEntity.setErrorCode(0);
            apiResponseEntity.setData(page);
        }
        return apiResponseEntity;
    }

    /**
     * 微信用户查询红包发放记录
     */
    @ApiOperation(value = "微信用户查询红包发放记录", notes = "微信用户查询红包发放记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/redPay/get", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getRedPayRecord(@RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        if (StringUtils.isNotEmpty(wxSession.getOpenId())) {
//			Page<BizAlipayBill> page = new PageFactory<BizAlipayBill>().defaultPage();
//			List<Map<String, Object>> result = bizAlipayBillService.selectListForPage(page, wxSession.getOpenId());
//			page.setRecords((List<BizAlipayBill>) new BizAlipayBillWarpper(result).warp());

            Page<BizWxpayBill> page = new PageFactory<BizWxpayBill>().defaultPage();
            List<Map<String, Object>> result = bizWxpayBillService.selectListForPage(page, wxSession.getOpenId());
            page.setRecords((List<BizWxpayBill>) new WxpayBillWarpper(result).warp());
            apiResponseEntity.setErrorCode(0);
            apiResponseEntity.setData(page);
        }
        return apiResponseEntity;
    }

    /**
     * 事故推送记录状态修改
     */
    @ApiOperation(value = "事故推送记录状态修改", notes = "事故推送记录状态修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String"),
            @ApiImplicitParam(name = "id", value = "推送记录ID", required = true, dataType = "int")
    })
    @RequestMapping(value = "/api/v1/wx/accids/pushRecord/updateStatus", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity updateWxAccidsPushRecordStatus(@RequestParam String thirdSessionKey, @RequestParam Integer id) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null || wxSession.getUser() == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        if (StringUtils.isNotEmpty(wxSession.getUser().getAccount())) {
            int effect = pushRecordService.setStatus(id, 1, new Date());
            apiResponseEntity.setErrorCode(0);
        }
        return apiResponseEntity;
    }

    /**
     * 业务员查询事故推送记录
     */
    @ApiOperation(value = "业务员查询事故推送记录", notes = "业务员查询事故推送记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/accids/pushRecord/get", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getWxAccidsPushRecord(@RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null || wxSession.getUser() == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        if (StringUtils.isNotEmpty(wxSession.getUser().getAccount())) {
            Page<PushRecord> page = new PageFactory<PushRecord>().defaultPage();
            List<Map<String, Object>> result = pushRecordService.getPushRecordsByAccount(page, wxSession.getUser().getAccount(), page.getOrderByField(), page.isAsc());
            page.setRecords((List<PushRecord>) new PushRecordWarpper(result).warp());
            apiResponseEntity.setErrorCode(0);
            apiResponseEntity.setData(page);
        }
        return apiResponseEntity;
    }

    /**
     * 业务员查询事故推送已读未读记录情况
     */
    @ApiOperation(value = "业务员查询事故推送已读未读记录情况", notes = "业务员查询事故推送已读未读记录情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/accids/pushRecord/count/get", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getWxAccidsPushRecordCount(@RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null || wxSession.getUser() == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        if (StringUtils.isNotEmpty(wxSession.getUser().getAccount())) {

            int unRead = pushRecordService.selectCount(new EntityWrapper<PushRecord>().eq("account", wxSession.getUser().getAccount()).eq("status", 0));
            apiResponseEntity.setErrorCode(0);
            apiResponseEntity.setData(unRead);
        }
        return apiResponseEntity;
    }

    /**
     * 理赔下单
     *
     * @param
     * @return
     */
    @ApiOperation(value = "新增理赔订单", notes = "新增理赔订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "客户姓名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "phone", value = "客户手机", required = true, dataType = "String"),
            @ApiImplicitParam(name = "lng", value = "经度", required = true, dataType = "Bigdecimal"),
            @ApiImplicitParam(name = "lat", value = "纬度", required = true, dataType = "Bigdecimal"),
            @ApiImplicitParam(name = "address", value = "上报地址名称", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/claim/add", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity addClaim(BizClaim bizClaim, @RequestParam String thirdSessionKey, BindingResult result) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (result.hasErrors()) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("微信session无效或为空");
            return apiResponseEntity;
        }
        bizClaim.setCreatetime(new Date());
        bizClaim.setStatus(0);
        bizClaim.setOpenid(wxSession.getOpenId());
        try {
            bizClaimService.insert(bizClaim);
            JSONObject obj = new JSONObject();
            obj.put("type", "claim_order_notice");
            webSocketMessagePushService.pushInnerForClaim(obj);
        } catch (Exception e) {
            log.error("理赔下单异常", e);
            apiResponseEntity.setErrorCode(1002);
            apiResponseEntity.setErrorMsg("网络异常");
            return apiResponseEntity;
        }
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("");
        return apiResponseEntity;
    }

    /**
     * 更新理赔单状态
     *
     * @param
     * @return
     */
    @ApiOperation(value = "更新理赔单状态", notes = "更新理赔单状态")
    @RequestMapping(value = "/api/v1/wx/claim/changeStatus", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity changeStatus(@RequestParam Integer id, @RequestParam Integer status, @RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("微信session无效或为空");
            return apiResponseEntity;
        }
        if (id == null || status == null) {
            apiResponseEntity.setErrorCode(4001);
            apiResponseEntity.setErrorMsg("参数错误");
            return apiResponseEntity;
        }
        BizClaim bizClaim = new BizClaim();
        bizClaim.setId(id);
        bizClaim.setStatus(status);
        try {
            bizClaimService.updateById(bizClaim);
        } catch (Exception e) {
            log.error("理赔单状态变更异常", e);
            apiResponseEntity.setErrorCode(1002);
            apiResponseEntity.setErrorMsg("网络异常");
            return apiResponseEntity;
        }
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("");
        return apiResponseEntity;
    }

    /**
     * 理赔单新增图片信息
     *
     * @param
     * @return
     */
    @ApiOperation(value = "理赔单新增图片信息", notes = "理赔单新增图片信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "理赔单ID", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/api/v1/wx/claim/add_img", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity addClaimImage(@RequestParam Integer id, @RequestParam String thirdSessionKey, @RequestParam String claimImg) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("微信session无效或为空");
            return apiResponseEntity;
        }
        try {
            bizClaimService.addClaimImage(id, claimImg);
        } catch (IOException e) {
            log.error("理赔添加图片异常", e);
            apiResponseEntity.setErrorCode(1002);
            apiResponseEntity.setErrorMsg("网络异常");
            return apiResponseEntity;
        }
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("");
        return apiResponseEntity;
    }

    /**
     * 上传文件
     *
     * @param
     * @return
     */
    @ApiOperation(value = "上传文件", notes = "上传文件")
    @RequestMapping(value = "/api/v1/wx/claim/uploadfile", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity uploadFile(@RequestParam String thirdSessionKey, HttpServletRequest request) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("微信session无效或为空");
            return apiResponseEntity;
        }
        List<MultipartFile> files = ((MultipartHttpServletRequest) request)
                .getFiles("file");
        try {
            List<String> urls = bizClaimService.uploadFile(files);
            apiResponseEntity.setData(urls);
        } catch (IOException e) {
            log.error("上传文件异常", e);
            apiResponseEntity.setErrorCode(1002);
            apiResponseEntity.setErrorMsg("上传文件异常");
            return apiResponseEntity;
        }
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("");
        return apiResponseEntity;
    }

    /**
     * 微信用户查询理赔单下单记录
     */
    @ApiOperation(value = "微信用户查询理赔单下单记录", notes = "微信用户查询理赔单下单记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/claim/user/get", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getClaimSubmitRecord(@RequestParam String thirdSessionKey, @RequestParam(required = false) Integer id) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        if (StringUtils.isNotEmpty(wxSession.getOpenId())) {
            BizClaim bizClaim = new BizClaim();
            bizClaim.setOpenid(wxSession.getOpenId());
            bizClaim.setId(id);
            Page<BizClaim> page = new PageFactory<BizClaim>().defaultPage();
            List<Map<String, Object>> result = bizClaimService.selectListForPage(page, bizClaim, null, null);
//            page.setRecords((List<BizClaim>) new BizClaimWarpper(result).warp());
            page.setRecords((List<BizClaim>) ((Object) result));
            apiResponseEntity.setErrorCode(0);
            apiResponseEntity.setData("");
        }
        return apiResponseEntity;
    }


    /**
     * 理赔顾问查询理赔单派单记录
     */
    @ApiOperation(value = "理赔顾问查询理赔单派单记录", notes = "理赔顾问查询理赔单派单记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/claim/claimer/get", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getClaimPushRecord(@RequestParam String thirdSessionKey, @RequestParam(required = false) Integer id) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        if (StringUtils.isNotEmpty(wxSession.getOpenId())) {
            BizClaim bizClaim = new BizClaim();
//            bizClaim.setClaimer(wxSession.getUser().getAccount());
            bizClaim.setId(id);
            Page<BizClaim> page = new PageFactory<BizClaim>().defaultPage();
            List<Map<String, Object>> result = bizClaimService.selectListForPage(page, bizClaim, null, null);
            page.setRecords((List<BizClaim>) ((Object) result));
            apiResponseEntity.setErrorCode(0);
            apiResponseEntity.setData("");
        }
        return apiResponseEntity;
    }

    /**
     * 模范理赔顾问列表
     */
    @ApiOperation(value = "模范理赔顾问列表", notes = "模范理赔顾问列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/claim/claimerShow/get", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getClaimShowList(@RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        if (StringUtils.isNotEmpty(wxSession.getOpenId())) {
            List<BizClaimerShow> result = bizClaimerShowService.selectList(new EntityWrapper<BizClaimerShow>().eq("status", 0));
            apiResponseEntity.setErrorCode(0);
            apiResponseEntity.setData(result);
        }
        return apiResponseEntity;
    }

    /**
     * 更新事故真实状态
     *
     * @param
     * @return
     */
    @ApiOperation(value = "更新事故真实状态", notes = "更新事故真实状态")
    @RequestMapping(value = "/api/v1/wx/accid/changeRealness", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity changeRealness(@RequestParam Integer id, @RequestParam Integer status, @RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("微信session无效或为空");
            return apiResponseEntity;
        }
        if (id == null || status == null) {
            apiResponseEntity.setErrorCode(4001);
            apiResponseEntity.setErrorMsg("参数错误");
            return apiResponseEntity;
        }
        Accident accident = new Accident();
        accident.setId(id.longValue());
        accident.setRealness(status);
        try {
            accdService.updateById(accident);
        } catch (Exception e) {
            log.error("更新事故真实状态", e);
            apiResponseEntity.setErrorCode(1002);
            apiResponseEntity.setErrorMsg("网络异常");
            return apiResponseEntity;
        }
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("");
        return apiResponseEntity;
    }

    /**
     * 获取新闻列表
     *
     * @param
     * @return
     */
    @ApiOperation(value = "获取新闻列表", notes = "获取新闻列表")
    @RequestMapping(value = "/api/v1/wx/notice/list", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object list(@RequestBody Notice notice) {
        List<Map<String, Object>> list = this.noticeService.list(notice);
        return list;
    }

    /**
     * 更新新闻浏览量
     *
     * @param
     * @return
     */
    @ApiOperation(value = "更新新闻浏览量", notes = "更新新闻浏览量")
    @RequestMapping(value = "/api/v1/wx/notice/updatepv", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity updatePv(@RequestParam Integer id, @RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("微信session无效或为空");
            return apiResponseEntity;
        }
        if (id == null) {
            apiResponseEntity.setErrorCode(4001);
            apiResponseEntity.setErrorMsg("参数错误");
            return apiResponseEntity;
        }
        try {
            int effectiveRows = noticeService.updateNewsPv(id);
        } catch (Exception e) {
            log.error("更新事故真实状态", e);
            apiResponseEntity.setErrorCode(1002);
            apiResponseEntity.setErrorMsg("网络异常");
            return apiResponseEntity;
        }
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("");
        return apiResponseEntity;
    }

/**
 * ============================================================
 * 	开放平台理赔业务接口
 * ============================================================
 */

    /**
     * 开放平台理赔用户注册
     *
     * @param
     * @return
     */
    @ApiOperation(value = "开放平台理赔用户注册", notes = "开放平台理赔用户注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "客户姓名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "phone", value = "客户手机", required = true, dataType = "String"),
            @ApiImplicitParam(name = "province", value = "省", required = true, dataType = "String"),
            @ApiImplicitParam(name = "city", value = "市", required = true, dataType = "String"),
            @ApiImplicitParam(name = "area", value = "区", required = true, dataType = "String"),
            @ApiImplicitParam(name = "thirdSessionKey", value = "token", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/open/claimer/register", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity claimerRegister(User user, @RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(user.getPhone()) || StringUtils.isEmpty(user.getName()) || StringUtils.isEmpty(user.getProvince()) || StringUtils.isEmpty(user.getCity()) || StringUtils.isEmpty(user.getArea())) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("微信session无效或为空");
            return apiResponseEntity;
        }
        User existUser = userService.selectOne(new EntityWrapper<User>().eq("phone", user.getPhone()).ne("status", 3));
        if (existUser != null) {
            apiResponseEntity.setErrorCode(1003);
            apiResponseEntity.setErrorMsg("该手机号已注册");
            return apiResponseEntity;
        }
        user.setAccount(user.getPhone());
        user.setBirthday(new Date());
        user.setCreatetime(new Date());
        user.setSalt(ShiroKit.getRandomSalt(5));
        user.setPassword(ShiroKit.md5(UUID.randomUUID().toString(), user.getSalt()));
        user.setStatus(ManagerStatus.REGISTERING.getCode());
        user.setSex(1);
        try {
            userService.insert(user);
        } catch (Exception e) {
            log.error("开放平台理赔用户注册", e);
            apiResponseEntity.setErrorCode(1002);
            apiResponseEntity.setErrorMsg("网络异常");
            return apiResponseEntity;
        }
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("");
        return apiResponseEntity;
    }

    /**
     * 开放平台理赔用户绑定银行卡
     *
     * @param
     * @return
     */
    @ApiOperation(value = "开放平台理赔用户绑定银行卡", notes = "开放平台理赔用户绑定银行卡")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bankcard", value = "银行卡号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "bankUserName", value = "银行卡户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "bankName", value = "开户银行", required = true, dataType = "String"),
            @ApiImplicitParam(name = "bankSecondName", value = "开户支行名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "idcard", value = "身份证", required = true, dataType = "String"),
            @ApiImplicitParam(name = "thirdSessionKey", value = "token", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/open/claimer/bindCard", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity claimerBindCard(User user, @RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(user.getBankcard())) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("微信session无效或为空");
            return apiResponseEntity;
        }
        try {
            user.setId(wxSession.getUser().getId());
            userService.updateById(user);
        } catch (Exception e) {
            log.error("开放平台理赔用户绑定银行卡", e);
            apiResponseEntity.setErrorCode(1002);
            apiResponseEntity.setErrorMsg("网络异常");
            return apiResponseEntity;
        }
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("");
        return apiResponseEntity;
    }

    /**
     * 开放平台理赔下单
     *
     * @param
     * @return
     */
    @ApiOperation(value = "开放平台理赔下单", notes = "开放平台理赔下单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "claimImg", value = "图片链接", required = true, dataType = "String"),
            @ApiImplicitParam(name = "lat", value = "纬度", required = true, dataType = "String"),
            @ApiImplicitParam(name = "lng", value = "经度", required = true, dataType = "String"),
            @ApiImplicitParam(name = "address", value = "地址名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "deptids", value = "推送目标门店id集合", required = true, dataType = "String"),
            @ApiImplicitParam(name = "thirdSessionKey", value = "token", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/open/claimer/order/add", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity claimerOrderAdd(OpenClaim openClaim, @RequestParam String deptids, @RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (openClaim.getLng() == null || openClaim.getLat() == null || StringUtils.isEmpty(openClaim.getClaimImg()) || StringUtils.isEmpty(openClaim.getAddress())) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("微信session无效或为空");
            return apiResponseEntity;
        }
        List<OpenClaim> lists = new ArrayList<>();
        String[] deptidStr = deptids.split(",");

        for (String deptid : deptidStr) {

            OpenClaim var1 = new OpenClaim();
            BeanUtils.copyProperties(openClaim, var1);
            var1.setOrderno(openClaimService.getOpenClaimOrderNo());
            var1.setclaimer(wxSession.getUser().getAccount());
            var1.setOpenid(wxSession.getOpenId());
            var1.setDeptid(Integer.parseInt(deptid));
            var1.setStatus(OpenClaimOrderStatus.INIT.getCode());
            var1.setCreatetime(new Date());
            lists.add(var1);
        }

        try {
            openClaimService.insertBatch(lists);
            List<User> users = userService.selectList(new EntityWrapper<User>().in("deptid", deptids));
            List<String> accounts = new ArrayList<>();
            for (User user : users) {
                accounts.add(user.getAccount());
            }
            JSONObject obj = new JSONObject();
            obj.put("type", "open_claim_order_notice");
            accdService.pushOpenClaimToFS(obj, accounts);
            obj.put("type", "open_claim_order_home_notice");
            webSocketMessagePushService.pushInnerForClaim(obj);
        } catch (Exception e) {
            log.error("开放平台理赔下单", e);
            apiResponseEntity.setErrorCode(1002);
            apiResponseEntity.setErrorMsg("网络异常");
            return apiResponseEntity;
        }
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("");
        return apiResponseEntity;
    }

    /**
     * 开放平台理赔单作废
     *
     * @param
     * @return
     */
    @ApiOperation(value = "开放平台理赔单作废", notes = "开放平台理赔单作废")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/open/claimer/order/cancel", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity claimerOrderCancel(Integer id, @RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (id == null) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("微信session无效或为空");
            return apiResponseEntity;
        }
        OpenClaim openClaim = new OpenClaim();
        openClaim.setStatus(OpenClaimOrderStatus.CANCEL.getCode());
        openClaimService.update(openClaim, new EntityWrapper<OpenClaim>().eq("id", id).eq("openid", wxSession.getOpenId()));
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("");
        return apiResponseEntity;
    }


    /**
     * 开放平台理赔下单补全订单
     *
     * @param
     * @return
     */
    @ApiOperation(value = "开放平台理赔下单补全订单", notes = "开放平台理赔下单补全订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "claimImg", value = "图片链接", required = true, dataType = "String"),
            @ApiImplicitParam(name = "id", value = "开放平台理赔单id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "thirdSessionKey", value = "token", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/open/claimer/order/supply", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity claimerOrderSupply(OpenClaim openClaim, @RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (openClaim.getId() == null) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("微信session无效或为空");
            return apiResponseEntity;
        }
        OpenClaim var1 = openClaimService.selectById(openClaim.getId());
        var1.setClaimImg(openClaim.getClaimImg());
        var1.setCjh(openClaim.getCjh());
        var1.setCph(openClaim.getCph());
        var1.setDesc(openClaim.getDesc());
        var1.setPhone(openClaim.getPhone());
        var1.setName(openClaim.getName());
        var1.setAccType(openClaim.getAccType());
        var1.setInsurer(openClaim.getInsurer());
        var1.setInsurAccess(openClaim.getInsurAccess());
        var1.setPromise(openClaim.getPromise());
        var1.setQcpp(openClaim.getQcpp());
        var1.setRescue(openClaim.getRescue());
        var1.setRescueFee(openClaim.getRescueFee());
        var1.setModifytime(new Date());
        var1.setPreFee(openClaim.getPreFee());
        var1.setAddress(openClaim.getAddress());
        var1.setDetailImg(openClaim.getDetailImg());
        var1.setGoodsHurts(openClaim.getGoodsHurts());
        var1.setPersonHurts(openClaim.getPersonHurts());
        var1.setSettleType(openClaim.getSettleType());
        var1.setRescueFeeSettleType(openClaim.getRescueFeeSettleType());
        try {
            openClaimService.updateById(var1);
        } catch (Exception e) {
            log.error("开放平台理赔下单补全订单异常", e);
            apiResponseEntity.setErrorCode(1002);
            apiResponseEntity.setErrorMsg("网络异常");
            return apiResponseEntity;
        }
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("");
        return apiResponseEntity;
    }

    /**
     * 开放平台理赔用户查询理赔单提交记录
     */
    @ApiOperation(value = "开放平台理赔用户查询理赔单提交记录", notes = "开放平台理赔用户查询理赔单提交记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/open/claimer/order/get", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getOpenClaimOrders(@RequestParam String thirdSessionKey, OpenClaim openClaim, @RequestParam(required = false) Integer isComplete) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        if (StringUtils.isNotEmpty(wxSession.getOpenId())) {
            Page<OpenClaim> page = new PageFactory<OpenClaim>().defaultPage();
            openClaim.setOpenid(wxSession.getOpenId());
            List<Map<String, Object>> openClaimOrders = openClaimService.selectByCondition(page, null, openClaim, null, null, null, isComplete, page.getOrderByField(), page.isAsc());
            page.setRecords((List<OpenClaim>) new OpenClaimWarpper(openClaimOrders).warp());
            apiResponseEntity.setErrorCode(0);
            apiResponseEntity.setData(page);
        }
        return apiResponseEntity;
    }

    /**
     * 开放平台理赔用户查询充值记录
     */
    @ApiOperation(value = "开放平台理赔用户查询充值记录", notes = "开放平台理赔用户查询充值记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/open/claimer/wxpayRecord/get", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getOpenClaimPayDetail(@RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        if (StringUtils.isNotEmpty(wxSession.getOpenId())) {
            Page<BizWxpayOrder> page = new PageFactory<BizWxpayOrder>().defaultPage();
            List<Map<String, Object>> BizWxpayOrders = bizWxpayOrderService.selectMaps(new EntityWrapper<BizWxpayOrder>().eq("openid", wxSession.getOpenId()).orderBy("create_time", false));
            page.setRecords((List<BizWxpayOrder>) new BizWxPayOrderWarpper(BizWxpayOrders).warp());
            apiResponseEntity.setErrorCode(0);
            apiResponseEntity.setData(page);
        }
        return apiResponseEntity;
    }

    /**
     * 开放平台理赔用户预存款账单
     */
    @ApiOperation(value = "开放平台理赔用户预存款账单", notes = "开放平台理赔用户预存款账单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/open/claimer/yckmx/get", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getOpenClaimYckDetail(@RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        if (StringUtils.isNotEmpty(wxSession.getOpenId())) {
            Page<BizYckCzmx> page = new PageFactory<BizYckCzmx>().defaultPage();
            List<Map<String, Object>> res = bizYckCzmxService.selectMaps(new EntityWrapper<BizYckCzmx>().eq("openid", wxSession.getOpenId()).orderBy("create_time", false));
            page.setRecords((List<BizYckCzmx>) new BizYckCzmxWarpper(res).warp());
            apiResponseEntity.setErrorCode(0);
            apiResponseEntity.setData(page);
        }
        return apiResponseEntity;
    }

    /**
     * 开放平台理赔用户查询理赔单提交记录统计数
     */
    @ApiOperation(value = "开放平台理赔用户查询理赔单提交记录统计数", notes = "开放平台理赔用户查询理赔单提交记录统计数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/open/claimer/order/count/get", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getOpenClaimOrdersCount(@RequestParam String thirdSessionKey, OpenClaim openClaim, @RequestParam(required = false) Integer isComplete) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        if (StringUtils.isNotEmpty(wxSession.getOpenId())) {
            openClaim.setOpenid(wxSession.getOpenId());
            long counts = openClaimService.countByCondition(openClaim, null, null, null, isComplete);
            apiResponseEntity.setErrorCode(0);
            apiResponseEntity.setData(counts);
        }
        return apiResponseEntity;
    }

    /**
     * 开放平台理赔用户查询理赔单提交记录统计数  首页
     */
    @ApiOperation(value = "开放平台理赔用户查询理赔单提交记录统计数  首页", notes = "开放平台理赔用户查询理赔单提交记录统计数  首页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/open/claimer/order/count/all/get", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getOpenClaimOrdersAllCount(@RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        if (StringUtils.isNotEmpty(wxSession.getOpenId())) {
            Map<String, Long> res = new HashMap<>();
            OpenClaim openClaim = new OpenClaim();
            openClaim.setOpenid(wxSession.getOpenId());
            for (OpenClaimOrderStatus en : OpenClaimOrderStatus.values()) {
                Integer status = en.getCode();
                openClaim.setStatus(status);
                Integer isComplete = (status == -1 ? 1 : null);
                long counts = openClaimService.countByCondition(openClaim, null, null, null, isComplete);
                res.put(en.name(), counts);
            }
            openClaim.setStatus(null);
            long counts = openClaimService.countByCondition(openClaim, null, null, null, 0);
            res.put("UNCOMPLETE", counts);
            apiResponseEntity.setErrorCode(0);
            apiResponseEntity.setData(res);
        }
        return apiResponseEntity;
    }

    /**
     * 4S用户查询理赔单提交记录统计数  首页
     */
    @ApiOperation(value = "4S用户查询理赔单提交记录统计数  首页", notes = "4S用户查询理赔单提交记录统计数  首页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/open/claimer/order/count/FS/get", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getOpenClaimOrdersAllFSCount(@RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        if (StringUtils.isNotEmpty(wxSession.getBizWxUser().getBindAccount())) {
            Map<String, BigDecimal> res = new HashMap<>();
            OpenClaim openClaim = new OpenClaim();

            User user = userService.getByAccount(wxSession.getBizWxUser().getBindAccount());
            openClaim.setDeptid(user.getDeptid());
            for (OpenClaimOrderStatus en : OpenClaimOrderStatus.values()) {
                if (en.getCode() == OpenClaimOrderStatus.INIT.getCode()) {
                    continue;
                }
                Integer status = en.getCode();
                openClaim.setStatus(status);
//				Integer isComplete = (status == -1 ? 1 : null);
                Long counts = openClaimService.countByCondition(openClaim, null, null, null, null);
                res.put(en.name(), BigDecimal.valueOf(counts));
            }
            openClaim.setStatus(OpenClaimOrderStatus.INIT.getCode());
            Long counts1 = openClaimService.countByCondition(openClaim, null, null, null, 0);
            res.put("TXDD", BigDecimal.valueOf(counts1));
            Long counts2 = openClaimService.countByCondition(openClaim, null, null, null, 1);
            res.put("UNACCEPT", BigDecimal.valueOf(counts2));
            openClaim.setStatus(null);
            BigDecimal fixlossCount = openClaimService.queryFixLossSumByCondition(openClaim, null, null, null, null);
            res.put("fixlossCount", fixlossCount);
            apiResponseEntity.setErrorCode(0);
            apiResponseEntity.setData(res);
        }
        return apiResponseEntity;
    }

    /**
     * //     * 开放平台理赔用户查询理赔单累计预计收益,预存款余额
     * //
     */
    @ApiOperation(value = "开放平台理赔用户查询理赔单累计预计收益,预存款余额", notes = "开放平台理赔用户查询理赔单累计预计收益,预存款余额")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/open/claimer/order/income/get", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getOpenClaimOrdersIncome(@RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        if (StringUtils.isNotEmpty(wxSession.getOpenId())) {
            Map<String, String> res = new HashMap<>(2);
            BigDecimal sumIncome = openClaimService.getOpenClaimOrdersIncome(wxSession.getOpenId());
            sumIncome = sumIncome == null ? BigDecimal.ZERO : sumIncome;
            NumberFormat nf = NumberFormat.getInstance();
            nf.setGroupingUsed(false);
            BizYckBalance bizYckBalance = bizYckBalanceService.selectOne(new EntityWrapper<BizYckBalance>().eq("openid", wxSession.getOpenId()));
            res.put("income", nf.format(sumIncome.setScale(0, BigDecimal.ROUND_DOWN)));
            res.put("balance", (bizYckBalance == null || bizYckBalance.getBalance() == null) ? "0" : bizYckBalance.getBalance().stripTrailingZeros().toPlainString());
            apiResponseEntity.setErrorCode(0);
            apiResponseEntity.setData(res);
        }
        return apiResponseEntity;
    }

    /**
     * 微信用户查询修理厂或4S店信息
     */
    @ApiOperation(value = "微信用户查询修理厂或4S店信息", notes = "微信用户查询修理厂或4S店信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String"),
            @ApiImplicitParam(name = "dict", value = "门店所在区域", required = false, dataType = "String"),
            @ApiImplicitParam(name = "qcpp", value = "门店服务汽车品牌", required = false, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/store/get", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getStoreInfo(@RequestParam String thirdSessionKey, @RequestParam(required = false) String dict, @RequestParam(required = false) String qcpp) {
        if (StringUtils.isNotEmpty(qcpp)) {
            qcpp = qcpp.trim();
        }
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        Page<Dept> page = new PageFactory<Dept>().defaultPage();
        List<Dept> resultPage = deptService.selectPageByCondition(page, dict, qcpp);
        page.setRecords(resultPage);
        for (Dept var1 : resultPage) {
            //分离门店首页图片和明细图片
            if (StringUtils.isNotEmpty(var1.getImgUrls())) {
                String[] imgs = var1.getImgUrls().split("\\|");
                var1.setIndexImg(imgs[0]);
                if (imgs.length > 1) {
                    var1.setDetailImg(Arrays.copyOfRange(imgs, 1, imgs.length));
                }
            }
            //根据门店保存的合作保险公司id，适配出保险公司链接
            if (StringUtils.isNotEmpty(var1.getCompanyids())) {
                StringBuilder companyImgs = new StringBuilder();
                List<Integer> ids = new ArrayList<>();
                for (String var2 : var1.getCompanyids().split(",")) {
                    ids.add(Integer.parseInt(var2));
                }
                List<BizInsuranceCompany> resList = bizInsuranceCompanyService.selectBatchIds(ids);
                for (BizInsuranceCompany var3 : resList) {
                    if (var1.getCompanyUrls() == null) {
                        var1.setCompanyUrls(new ArrayList<>());
                    }
                    var1.getCompanyUrls().add(var3.getUrl());
                }
            }
        }
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setData(page);
        return apiResponseEntity;
    }

    /**
     * 微信用户查询修理厂或4S店信息
     */
    @ApiOperation(value = "微信用户查询修理厂或4S店信息", notes = "微信用户查询修理厂或4S店信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String"),
            @ApiImplicitParam(name = "deptid", value = "部门id", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/api/v1/wx/store/getOne", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getStoreInfoById(@RequestParam String thirdSessionKey, @RequestParam(required = true) Integer deptid) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        Dept dept = deptService.selectById(deptid);
        //分离门店首页图片和明细图片
        if (StringUtils.isNotEmpty(dept.getImgUrls())) {
            String[] imgs = dept.getImgUrls().split("\\|");
            dept.setIndexImg(imgs[0]);
            if (imgs.length > 1) {
                dept.setDetailImg(Arrays.copyOfRange(imgs, 1, imgs.length));
            }
        }
        //根据门店保存的合作保险公司id，适配出保险公司链接
        if (StringUtils.isNotEmpty(dept.getCompanyids())) {
            StringBuilder companyImgs = new StringBuilder();
            List<Integer> ids = new ArrayList<>();
            for (String var2 : dept.getCompanyids().split(",")) {
                ids.add(Integer.parseInt(var2));
            }
            List<BizInsuranceCompany> resList = bizInsuranceCompanyService.selectBatchIds(ids);
            for (BizInsuranceCompany var3 : resList) {
                if (dept.getCompanyUrls() == null) {
                    dept.setCompanyUrls(new ArrayList<>());
                }
                dept.getCompanyUrls().add(var3.getUrl());
            }
        }
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setData(dept);
        return apiResponseEntity;
    }

    /**
     * 获取汽车品牌信息
     */
    @ApiOperation(value = "获取汽车品牌信息", notes = "获取汽车品牌信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String"),
            @ApiImplicitParam(name = "dict", value = "数据字典PARENT CODE", required = false, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/carType/get", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getCarType(@RequestParam String thirdSessionKey, @RequestParam(required = true) String dict) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        List<Dict> list = iDictService.selectByParentCode(dict);
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setData(list);
        return apiResponseEntity;
    }


    /**
     * 开放平台充值统一下单
     *
     * @param
     * @return
     */
    @ApiOperation(value = "开放平台充值统一下单", notes = "开放平台充值统一下单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "amount", value = "充值金额", required = true, dataType = "BigDecimal"),
            @ApiImplicitParam(name = "thirdSessionKey", value = "token", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/open/wxpay/unifiedOrder", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity unifiedOrder(@RequestParam BigDecimal amount, @RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) != 1) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("微信session无效或为空");
            return apiResponseEntity;
        }

        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        Map<String, String> reqData = new HashMap<>();
        String outTradeNo = bizWxpayBillService.getWxUnifiedOrderNo();
        reqData.put("out_trade_no", outTradeNo);
        reqData.put("total_fee", nf.format(amount.multiply(new BigDecimal(100)).doubleValue()));
        reqData.put("openid", wxSession.getOpenId());
        try {
            Map<String, String> res = bizWxpayBillService.unifiedOrder(reqData);
            if (WXPayConstants.SUCCESS.equals(res.get("return_code"))) {
                if (WXPayConstants.SUCCESS.equals(res.get("result_code"))) {
                    BizWxpayOrder bizWxpayOrder = new BizWxpayOrder();
                    bizWxpayOrder.setStatus(WxPayOrderStatus.INIT.getCode());
                    bizWxpayOrder.setAmount(amount);
                    bizWxpayOrder.setCreateTime(new Date());
                    bizWxpayOrder.setOpenid(wxSession.getOpenId());
                    bizWxpayOrder.setAccount(wxSession.getUser().getAccount());
                    bizWxpayOrder.setOutTradeNo(outTradeNo);
                    bizWxpayOrder.setPrepayId(res.get("prepay_id"));
                    boolean flag = bizWxpayOrderService.insert(bizWxpayOrder);
                    if (flag) {
                        Map<String, String> responseMap = new HashMap<>();

                        responseMap.put("appId", wxPayProperties.getAppid());
//						responseMap.put("prepay_id",res.get("prepay_id"));
                        responseMap.put("signType", WXPayConstants.SignType.MD5.toString());
                        responseMap.put("nonceStr", WXPayUtil.generateNonceStr());
                        responseMap.put("package", "prepay_id=" + res.get("prepay_id"));
                        responseMap.put("timeStamp", System.currentTimeMillis() / 1000 + "");
                        String sign = WXPayUtil.generateSignature(responseMap, wxPayProperties.getKey(), WXPayConstants.SignType.MD5);
                        responseMap.put("sign", sign);
                        responseMap.put("prepay_id", res.get("prepay_id"));
                        apiResponseEntity.setData(responseMap);
                    }
                }
            }
        } catch (Exception e) {
            log.error("开放平台充值统一下单", e);
            apiResponseEntity.setErrorCode(1002);
            apiResponseEntity.setErrorMsg("网络异常");
            return apiResponseEntity;
        }
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("");
        return apiResponseEntity;
    }

    /**
     * 获取粉丝数统计
     */
    @ApiOperation(value = "获取粉丝数统计", notes = "获取粉丝数统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/fans/counts/get", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getFansCounts(@RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        if (StringUtils.isNotEmpty(wxSession.getOpenId())) {
            Map<String, Integer> res = new HashMap<>(2);
            int count = bizWxUserService.selectCount(new EntityWrapper<BizWxUser>().eq("extensionAccount", wxSession.getOpenId()));
            res.put("fansCount", count);
            apiResponseEntity.setErrorCode(0);
            apiResponseEntity.setData(res);
        }
        return apiResponseEntity;
    }

    /**
     * 获取粉丝数事故红包总数
     */
    @ApiOperation(value = "获取粉丝数事故红包总数", notes = "获取粉丝数事故红包总数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/fans/redpack/counts/get", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getFansRedPackCounts(@RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        if (StringUtils.isNotEmpty(wxSession.getOpenId())) {
            Map<String, BigDecimal> res = new HashMap<>(1);
            BigDecimal redpackSum = bizWxPayRecordService.selectSumRedPack(wxSession.getOpenId(), null, null);
            res.put("redpackSum", redpackSum);
            apiResponseEntity.setErrorCode(0);
            apiResponseEntity.setData(res);
        }
        return apiResponseEntity;
    }

    /**
     * 获取事故红包提成总数
     */
    @ApiOperation(value = "获取事故红包提成总数", notes = "获取事故红包提成总数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/fans/redpack/percentage/count", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getRedPackPercentageCounts(@RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        if (StringUtils.isNotEmpty(wxSession.getOpenId())) {
            Map<String, BigDecimal> res = new HashMap<>(1);
            BigDecimal redPackPercentage = bizWxPayRecordService.getRedPackPercentageCounts(wxSession.getOpenId(), null, null);
            res.put("redPackPercentage", redPackPercentage);
            apiResponseEntity.setErrorCode(0);
            apiResponseEntity.setData(res);
        }
        return apiResponseEntity;
    }

    /**
     * 获取事故红包当日提成总数
     */
    @ApiOperation(value = "获取事故红包当日提成总数", notes = "获取事故红包当日提成总数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/fans/today/percentage/count", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getTodayPercentageCounts(@RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        if (StringUtils.isNotEmpty(wxSession.getOpenId())) {
            Map<String, BigDecimal> res = new HashMap<>(1);
            BigDecimal redPackPercentage = bizWxPayRecordService.getRedPackPercentageCounts(wxSession.getOpenId(), DateUtil.getDay(), DateUtil.getDay());
            res.put("redPackPercentage", redPackPercentage);
            apiResponseEntity.setErrorCode(0);
            apiResponseEntity.setData(res);
        }
        return apiResponseEntity;
    }

    /**
     * 微信用户查询粉丝事故红包记录
     */
    @ApiOperation(value = "微信用户查询粉丝事故红包记录", notes = "微信用户查询粉丝事故红包记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/fans/redpack/list", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getWxFansRedPackList(@RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        if (StringUtils.isNotEmpty(wxSession.getOpenId())) {
            Page<BizWxpayBill> page = new PageFactory<BizWxpayBill>().defaultPage();
            List<Map<String, Object>> result = bizWxpayBillService.selectFansListForPage(page, wxSession.getOpenId(), null, null);
            page.setRecords((List<BizWxpayBill>) new WxpayBillWarpper(result).warp());
            apiResponseEntity.setErrorCode(0);
            apiResponseEntity.setData(page);
        }
        return apiResponseEntity;
    }

    /**
     * 微信用户查询事故提成记录
     */
    @ApiOperation(value = "微信用户查询事故提成记录", notes = "微信用户查询事故提成记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/fans/redpack/percentage/list", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getWxFansRedPackPercentageList(@RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        if (StringUtils.isNotEmpty(wxSession.getOpenId())) {
            Page<BizWxpayBill> page = new PageFactory<BizWxpayBill>().defaultPage();
            List<Map<String, Object>> result = bizWxPayRecordService.getRedPackPercentage(page, wxSession.getOpenId());
            page.setRecords((List<BizWxpayBill>) new WxpayBillWarpper(result).warp());
            apiResponseEntity.setErrorCode(0);
            apiResponseEntity.setData(page);
        }
        return apiResponseEntity;
    }

    /**
     * 微信用户查询粉丝事故当日红包总数
     */
    @ApiOperation(value = "微信用户查询粉丝事故当日红包总数", notes = "微信用户查询粉丝事故当日红包总数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/fans/redpack/today/count", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getWxFansTodayRedPackCount(@RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        if (StringUtils.isNotEmpty(wxSession.getOpenId())) {
            Map<String, BigDecimal> res = new HashMap<>(1);
            BigDecimal redPack = bizWxPayRecordService.selectSumRedPack(wxSession.getOpenId(), DateUtil.getDay(), DateUtil.getDay());
            res.put("redPackToday", redPack);
            apiResponseEntity.setErrorCode(0);
            apiResponseEntity.setData(res);
        }
        return apiResponseEntity;
    }


    /**
     * 微信用户查询粉丝列表
     */
    @ApiOperation(value = "微信用户查询粉丝列表", notes = "微信用户查询粉丝列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/fans/list", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getWxFansList(@RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        if (StringUtils.isNotEmpty(wxSession.getOpenId())) {
            Page<BizWxUser> page = new PageFactory<BizWxUser>().defaultPage();
            BizWxUser bizWxUser = new BizWxUser();
            bizWxUser.setExtensionAccount(wxSession.getOpenId());
            List<BizWxUser> result = bizWxUserService.selectFansPage(page, bizWxUser);
            for (BizWxUser bizWxUser1 : result) {
                if (bizWxUser1 != null && StringUtils.isNotEmpty(bizWxUser1.getWxname())) {
                    String urlDecoderString = UrlUtil.getURLDecoderString(bizWxUser1.getWxname());
                    bizWxUser1.setWxname(urlDecoderString);
                }
            }
            page.setRecords(result);
            apiResponseEntity.setErrorCode(0);
            apiResponseEntity.setData(page);
        }
        return apiResponseEntity;
    }

    /**
     * 4S用户获取开放平台理赔单
     */
    @ApiOperation(value = "4S用户获取开放平台理赔单", notes = "4S用户获取开放平台理赔单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/openClaim/list", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getOpenClaimList(OpenClaim openClaim, String thirdSessionKey, Integer isComplete) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        if (StringUtils.isEmpty(wxSession.getBizWxUser().getBindAccount())) {
            apiResponseEntity.setErrorCode(5031);
            apiResponseEntity.setErrorMsg("用户未登陆");
            return apiResponseEntity;
        }
        User pcUser = userService.getByAccount(wxSession.getBizWxUser().getBindAccount());
        if (pcUser == null) {
            apiResponseEntity.setErrorCode(5031);
            apiResponseEntity.setErrorMsg("用户未登陆");
            return apiResponseEntity;
        }
        Page<OpenClaim> page = new PageFactory<OpenClaim>().defaultPage();
        openClaim.setDeptid(pcUser.getDeptid());
        List<Map<String, Object>> claims = openClaimService.selectByConditionForRest(page, null, openClaim, null, null, null, isComplete, page.getOrderByField(), page.isAsc());
        for (Map var1 : claims) {
            var1.remove("claimerPhone");
            var1.remove("rebateForCompany");
            var1.remove("rebateForEmp");
        }
        page.setRecords((List<OpenClaim>) new OpenClaimWarpper(claims).warp());
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setData(page);
        return apiResponseEntity;
    }

    /**
     * 4S用户移动端登陆
     */
    @ApiOperation(value = "4S用户移动端登陆", notes = "4S用户移动端登陆")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String"),
            @ApiImplicitParam(name = "account", value = "登陆账号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "登陆密码", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/openClaim/login", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity wxOpenClaimLogin(String thirdSessionKey, String account, String password) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey) || StringUtils.isEmpty(account) || StringUtils.isEmpty(password)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        User user = userService.getByAccount(account);
        if (user == null || !ShiroKit.md5(password, user.getSalt()).equals(user.getPassword())) {
            apiResponseEntity.setErrorCode(5030);
            apiResponseEntity.setErrorMsg("登陆失败，账号或密码错误");
            return apiResponseEntity;
        }
        BizWxUser newBizWxUser = new BizWxUser();
        newBizWxUser.setId(wxSession.getBizWxUser().getId());
        newBizWxUser.setBindAccount(account);
        bizWxUserService.updateById(newBizWxUser);
        wxSession.getBizWxUser().setBindAccount(account);
        apiResponseEntity.setErrorCode(0);
        return apiResponseEntity;
    }

    /**
     * 4S用户移动端退出登陆
     */
    @ApiOperation(value = "4S用户移动端退出登陆", notes = "4S用户移动端退出登陆")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/openClaim/logout", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity wxOpenClaimLogout(String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        if (StringUtils.isNotEmpty(wxSession.getBizWxUser().getBindAccount())) {
            boolean flag = bizWxUserService.updateForSet("bindAccount = null", new EntityWrapper<BizWxUser>().eq("openid", wxSession.getOpenId()));
        }
        apiResponseEntity.setErrorCode(0);
        return apiResponseEntity;
    }

    /**
     * 修改开放平台理赔订单  确认接车
     */
    @ApiOperation(value = "修改开放平台理赔订单状态 确认接车", notes = "修改开放平台理赔订单状态 确认接车")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String"),
            @ApiImplicitParam(name = "id", value = "理赔订单id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/openClaim/partner/accept", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity wxOpenClaimCarAccept(String thirdSessionKey, Integer id) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        OpenClaim openClaim = new OpenClaim();
        openClaim.setId(id);
        openClaim.setStatus(OpenClaimOrderStatus.SERVICING.getCode());
        openClaimService.updateById(openClaim);
        apiResponseEntity.setErrorCode(0);
        return apiResponseEntity;
    }

    /**
     * 修改开放平台理赔订单  未到店
     */
    @ApiOperation(value = "修改开放平台理赔订单状态 未到店", notes = "修改开放平台理赔订单状态 未到店")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String"),
            @ApiImplicitParam(name = "id", value = "理赔订单id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/openClaim/partner/unreach", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity wxOpenClaimCarUnreach(String thirdSessionKey, Integer id) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        OpenClaim openClaim = new OpenClaim();
        openClaim.setId(id);
        openClaim.setStatus(OpenClaimOrderStatus.UNREACH.getCode());
        openClaimService.updateById(openClaim);
        apiResponseEntity.setErrorCode(0);
        return apiResponseEntity;
    }

    /**
     * 修改开放平台理赔订单  未接车
     */
    @ApiOperation(value = "修改开放平台理赔订单状态 未接车", notes = "修改开放平台理赔订单状态 未接车")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String"),
            @ApiImplicitParam(name = "id", value = "理赔订单id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/openClaim/partner/unaccept", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity wxOpenClaimCarUnAccept(String thirdSessionKey, Integer id) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        OpenClaim openClaim = new OpenClaim();
        openClaim.setId(id);
        openClaim.setStatus(OpenClaimOrderStatus.INIT.getCode());
        openClaimService.updateById(openClaim);
        apiResponseEntity.setErrorCode(0);
        return apiResponseEntity;
    }

    /**
     * 修改开放平台理赔订单  确认交车
     */
    @ApiOperation(value = "修改开放平台理赔订单状态 确认交车", notes = "修改开放平台理赔订单状态 确认交车")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String"),
            @ApiImplicitParam(name = "id", value = "理赔订单id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/openClaim/partner/placed", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity wxOpenClaimCarPlaced(String thirdSessionKey, Integer id) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        OpenClaim openClaim = new OpenClaim();
        openClaim.setId(id);
        openClaim.setStatus(OpenClaimOrderStatus.PLACED.getCode());
        openClaimService.updateById(openClaim);
        apiResponseEntity.setErrorCode(0);
        return apiResponseEntity;
    }

    /**
     * 修改开放平台理赔订单  确认结算
     */
    @ApiOperation(value = "修改开放平台理赔订单状态 确认结算", notes = "修改开放平台理赔订单状态 确认结算")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String"),
            @ApiImplicitParam(name = "id", value = "理赔订单id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/openClaim/partner/settle", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity wxOpenClaimCarSettled(String thirdSessionKey, Integer id) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey) || id == null) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        OpenClaim openClaim = new OpenClaim();
        openClaim.setId(id);
        openClaim.setStatus(OpenClaimOrderStatus.SETTLED.getCode());
        openClaimService.updateById(openClaim);
        apiResponseEntity.setErrorCode(0);
        return apiResponseEntity;
    }

    /**
     * 开放平台理赔订单  录入定损金额
     */
    @ApiOperation(value = "开放平台理赔订单  录入定损金额", notes = "开放平台理赔订单  录入定损金额")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String"),
            @ApiImplicitParam(name = "id", value = "理赔订单id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "fixloss", value = "定损金额", required = true, dataType = "BigDecimal")
    })
    @RequestMapping(value = "/api/v1/wx/openClaim/partner/addFixloss", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity wxOpenClaimAddFixloss(String thirdSessionKey, Integer id, BigDecimal fixloss) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey) || id == null || fixloss == null) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        User user = userService.getByAccount(wxSession.getBizWxUser().getBindAccount());
        Dept dept = deptService.selectById(user.getDeptid());
        if (dept == null) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        OpenClaim openClaim = new OpenClaim();
        openClaim.setFixloss(fixloss);
        openClaim.setId(id);
        Double scaleForEmp = dept.getScaleForEmp();
        Double scaleForCompany = dept.getScaleForCompany();
        openClaim.setRebateForCompany(scaleForCompany == null ? BigDecimal.ZERO : fixloss.multiply(new BigDecimal(scaleForCompany)).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
        if (openClaim.getRebateForCompany() != null && openClaim.getRebateForCompany().compareTo(BigDecimal.ZERO) == 1) {
            openClaim.setRebateForEmp(scaleForEmp == null ? BigDecimal.ZERO : openClaim.getRebateForCompany().multiply(new BigDecimal(scaleForEmp)).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
        }
        openClaim.setStatus(OpenClaimOrderStatus.PLACED.getCode());
        openClaimService.updateById(openClaim);
        apiResponseEntity.setErrorCode(0);
        return apiResponseEntity;
    }

    /**
     * ============================ 维修保养 ====================================
     */

    /**
     * 查询汽车保养套餐
     */
    @ApiOperation(value = "查询汽车保养套餐", notes = "查询汽车保养套餐")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/bizMaintainPackage/list", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getBizMaintainPackageList(String thirdSessionKey, BizMaintainPackage bizMaintainPackage) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        Page<BizMaintainPackage> page = new PageFactory<BizMaintainPackage>().defaultPage();
        bizMaintainPackage.setPstatus(0);
        bizMaintainPackage.setPackageType(RepairePackageType.INIT.getCode());
        List<BizMaintainPackage> list = bizMaintainPackageService.selectPage(page, bizMaintainPackage, null);
        for (BizMaintainPackage item : list) {
            if (StringUtils.isNotEmpty(item.getDetail())) {
                item.setDetailList(Arrays.asList(item.getDetail().split("#")));
            }
            item.setPstatusName(item.getPstatus() == 0 ? "销售中" : "已下架");
        }
        page.setRecords(list);
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setData(page);
        return apiResponseEntity;
    }

    /**
     * 查询汽车维修套餐
     */
    @ApiOperation(value = "查询汽车维修套餐", notes = "查询汽车维修套餐")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/bizRepairePackage/list", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getBizRepairePackageList(String thirdSessionKey, BizMaintainPackage bizMaintainPackage) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        Page<BizMaintainPackage> page = new PageFactory<BizMaintainPackage>().defaultPage();
        bizMaintainPackage.setPstatus(0);
        bizMaintainPackage.setPackageType(RepairePackageType.REPAIRE_PACKAGE.getCode());
        List<BizMaintainPackage> list = bizMaintainPackageService.selectPage(page, bizMaintainPackage, null);
        for (BizMaintainPackage item : list) {
            if (StringUtils.isNotEmpty(item.getDetail())) {
                item.setDetailList(Arrays.asList(item.getDetail().split("#")));
            }
            item.setPstatusName(item.getPstatus() == 0 ? "销售中" : "已下架");
        }
        page.setRecords(list);
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setData(page);
        return apiResponseEntity;
    }

    /**
     * 客户查询汽车保养订单
     */
    @ApiOperation(value = "客户查询汽车保养订单", notes = "客户查询汽车保养订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/bizMaintainPackageOrder/list", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getBizMaintainPackageOrderList(String thirdSessionKey, BizMaintainPackageOrder order) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        Page<BizMaintainPackageOrder> page = new PageFactory<BizMaintainPackageOrder>().defaultPage();
        order.setOpenid(wxSession.getOpenId());
        order.setPackageType(RepairePackageType.INIT.getCode());
        List<BizMaintainPackageOrder> list = bizMaintainPackageOrderService.selectPage(page, order, null);
        for (BizMaintainPackageOrder item : list) {
            if (StringUtils.isNotEmpty(item.getDetail())) {
                item.setDetailList(Arrays.asList(item.getDetail().split("#")));
                item.setPreImgsList(StringUtils.isEmpty(item.getPreImgs()) ? null : Arrays.asList(item.getPreImgs().split("#")));
                item.setAftImgsList(StringUtils.isEmpty(item.getAftImgs()) ? null : Arrays.asList(item.getAftImgs().split("#")));
                item.setOrderStatus(MaintainOrderStatus.valueOf(item.getOrderStatus()).getMessage());
            }
        }
        page.setRecords(list);
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setData(page);
        return apiResponseEntity;
    }


    /**
     * 修理工查询汽车保养订单
     */
    @ApiOperation(value = "修理工查询汽车保养订单", notes = "修理工查询汽车保养订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/bizMaintainPackageOrder/push/list", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getBizMaintainPackageOrderPushList(String thirdSessionKey, BizMaintainPackageOrder order) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        if (wxSession.getUser() == null || StringUtils.isEmpty(wxSession.getUser().getAccount())) {
            apiResponseEntity.setErrorCode(5002);
            apiResponseEntity.setErrorMsg("无操作权限，请在系统中维护用户信息");
            return apiResponseEntity;
        }
        Page<BizMaintainPackageOrder> page = new PageFactory<BizMaintainPackageOrder>().defaultPage();
        order.setAccount(wxSession.getUser().getAccount());
        order.setPackageType(RepairePackageType.INIT.getCode());
        List<BizMaintainPackageOrder> list = bizMaintainPackageOrderService.selectPageForInner(page, order, null);
        for (BizMaintainPackageOrder item : list) {
            if (StringUtils.isNotEmpty(item.getDetail())) {
                item.setDetailList(Arrays.asList(item.getDetail().split("#")));
                item.setPreImgsList(StringUtils.isEmpty(item.getPreImgs()) ? null : Arrays.asList(item.getPreImgs().split("#")));
                item.setAftImgsList(StringUtils.isEmpty(item.getAftImgs()) ? null : Arrays.asList(item.getAftImgs().split("#")));
                item.setOrderStatus(MaintainOrderStatus.valueOf(item.getOrderStatus()).getMessage());
            }
        }
        page.setRecords(list);
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setData(page);
        return apiResponseEntity;
    }

    /**
     * 汽车保养维修订单下单
     */
    @ApiOperation(value = "汽车保养订单下单", notes = "汽车保养订单下单")
    @RequestMapping(value = "/api/v1/wx/bizMaintainPackageOrder/add", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity addMaintainPackageOrder(String thirdSessionKey, BizMaintainPackageOrder bizMaintainPackageOrder) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        bizMaintainPackageOrder.setOrderStatus(MaintainOrderStatus.CREATED.getCode());
        bizMaintainPackageOrder.setCreateTime(new Date());
        bizMaintainPackageOrder.setOpenid(wxSession.getOpenId());
        bizMaintainPackageOrder.setOrderno(bizMaintainPackageOrderService.getMaintainOrderNo());
        bizMaintainPackageOrderService.insert(bizMaintainPackageOrder);
        JSONObject obj = new JSONObject();
        if (bizMaintainPackageOrder.getPackageType() != null && bizMaintainPackageOrder.getPackageType() == RepairePackageType.REPAIRE_PACKAGE.getCode()) {
            obj.put("type", "repaire_order_home_notice");
        } else {
            obj.put("type", "maintain_order_home_notice");
        }
        webSocketMessagePushService.pushInnerForClaim(obj);
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("操作成功");
        return apiResponseEntity;
    }

    /**
     * 汽车保养维修订单修改
     */
    @ApiOperation(value = "汽车保养维修订单修改", notes = "汽车保养维修订单修改")
    @RequestMapping(value = "/api/v1/wx/bizMaintainPackageOrder/update", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity updateMaintainPackageOrder(@RequestParam String thirdSessionKey, BizMaintainPackageOrder bizMaintainPackageOrder) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        bizMaintainPackageOrderService.updateById(bizMaintainPackageOrder);
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("操作成功");
        return apiResponseEntity;
    }

    /**
     * 客户查保养单提交记录统计数  首页
     */
    @ApiOperation(value = "客户查保养单提交记录统计数  首页", notes = "客户查保养单提交记录统计数  首页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/maintain/user/order/count/get", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getUserMaintainOrdersAllCount(@RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        Map<String, Long> res = new HashMap<>();
        BizMaintainPackageOrder order = new BizMaintainPackageOrder();
        order.setOpenid(wxSession.getOpenId());
        long totalCount = 0;
        for (MaintainOrderStatus en : MaintainOrderStatus.values()) {
            String status = en.getCode();
            order.setOrderStatus(status);
            order.setPackageType(RepairePackageType.INIT.getCode());
            Long counts = bizMaintainPackageOrderService.countByCondition(order);
            res.put(en.name(), counts);
            totalCount = totalCount + counts;
        }
        res.put("totalCount", totalCount);
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setData(res);
        return apiResponseEntity;
    }

    /**
     * 修理工查保养单提交记录统计数  首页
     */
    @ApiOperation(value = "修理工查保养单提交记录统计数  首页", notes = "修理工查保养单提交记录统计数  首页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/maintain/repaireman/order/count/get", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getRepairemanMaintainOrdersAllCount(@RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        if (wxSession.getUser() == null || StringUtils.isEmpty(wxSession.getUser().getAccount())) {
            apiResponseEntity.setErrorCode(5002);
            apiResponseEntity.setErrorMsg("无操作权限，请在系统中维护用户信息");
            return apiResponseEntity;
        }
        Map<String, Integer> res = new HashMap<>();
        BizMaintainPackageOrder order = new BizMaintainPackageOrder();
        order.setAccount(wxSession.getUser().getAccount());
        int totalCount = 0;
        for (MaintainOrderStatus en : MaintainOrderStatus.values()) {
            String status = en.getCode();
            order.setOrderStatus(status);
            order.setPackageType(RepairePackageType.INIT.getCode());
            Long counts = bizMaintainPackageOrderService.countByConditionForInner(order);
            res.put(en.name(), counts.intValue());
            totalCount = totalCount + counts.intValue();
        }
        res.put("totalCount", totalCount);
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setData(res);
        return apiResponseEntity;
    }

    /**
     * 客户查询汽车维修订单
     */
    @ApiOperation(value = "客户查询汽车维修订单", notes = "客户查询汽车维修订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/bizRepairePackageOrder/list", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getBizRepairePackageOrderList(String thirdSessionKey, BizMaintainPackageOrder order) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        Page<BizMaintainPackageOrder> page = new PageFactory<BizMaintainPackageOrder>().defaultPage();
        order.setOpenid(wxSession.getOpenId());
        order.setPackageType(RepairePackageType.REPAIRE_PACKAGE.getCode());
        List<BizMaintainPackageOrder> list = bizMaintainPackageOrderService.selectPage(page, order, null);
        for (BizMaintainPackageOrder item : list) {
            if (StringUtils.isNotEmpty(item.getDetail())) {
                item.setDetailList(Arrays.asList(item.getDetail().split("#")));
                item.setPreImgsList(StringUtils.isEmpty(item.getPreImgs()) ? null : Arrays.asList(item.getPreImgs().split("#")));
                item.setAftImgsList(StringUtils.isEmpty(item.getAftImgs()) ? null : Arrays.asList(item.getAftImgs().split("#")));
                item.setOrderStatus(MaintainOrderStatus.valueOf(item.getOrderStatus()).getMessage());
            }
        }
        page.setRecords(list);
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setData(page);
        return apiResponseEntity;
    }

    /**
     * 修理工查询汽车维修订单
     */
    @ApiOperation(value = "修理工查询汽车维修订单", notes = "修理工查询汽车维修订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/bizRepairePackageOrder/push/list", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getBizRepairePackageOrderPushList(String thirdSessionKey, BizMaintainPackageOrder order) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        if (wxSession.getUser() == null || StringUtils.isEmpty(wxSession.getUser().getAccount())) {
            apiResponseEntity.setErrorCode(5002);
            apiResponseEntity.setErrorMsg("无操作权限，请在系统中维护用户信息");
            return apiResponseEntity;
        }
        Page<BizMaintainPackageOrder> page = null;
        try {
            page = new PageFactory<BizMaintainPackageOrder>().defaultPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        order.setAccount(wxSession.getUser().getAccount());
        order.setPackageType(RepairePackageType.REPAIRE_PACKAGE.getCode());
        List<BizMaintainPackageOrder> list = bizMaintainPackageOrderService.selectPageForInner(page, order, null);
        for (BizMaintainPackageOrder item : list) {
            if (StringUtils.isNotEmpty(item.getDetail())) {
                item.setDetailList(Arrays.asList(item.getDetail().split("#")));
                item.setPreImgsList(StringUtils.isEmpty(item.getPreImgs()) ? null : Arrays.asList(item.getPreImgs().split("#")));
                item.setAftImgsList(StringUtils.isEmpty(item.getAftImgs()) ? null : Arrays.asList(item.getAftImgs().split("#")));
                item.setOrderStatus(MaintainOrderStatus.valueOf(item.getOrderStatus()).getMessage());
            }
        }
        page.setRecords(list);
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setData(page);
        return apiResponseEntity;
    }


    /**
     * 客户查维修单提交记录统计数  首页
     */
    @ApiOperation(value = "客户查维修单提交记录统计数  首页", notes = "客户查维修单提交记录统计数  首页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/repaire/user/order/count/get", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getUserRepaireOrdersAllCount(@RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        Map<String, Long> res = new HashMap<>();
        BizMaintainPackageOrder order = new BizMaintainPackageOrder();
        order.setOpenid(wxSession.getOpenId());
        order.setPackageType(RepairePackageType.REPAIRE_PACKAGE.getCode());
        long totalCount = 0;
        for (MaintainOrderStatus en : MaintainOrderStatus.values()) {
            String status = en.getCode();
            order.setOrderStatus(status);
            long counts = bizMaintainPackageOrderService.countByCondition(order);
            res.put(en.name(), counts);
            totalCount = totalCount + counts;
        }
        res.put("totalCount", totalCount);
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setData(res);
        return apiResponseEntity;
    }

    /**
     * 修理工查维修单提交记录统计数  首页
     */
    @ApiOperation(value = "修理工查维修单提交记录统计数  首页", notes = "修理工查维修单提交记录统计数  首页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/repaire/repaireman/order/count/get", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getRepairemanRepaireOrdersAllCount(@RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        if (wxSession.getUser() == null || StringUtils.isEmpty(wxSession.getUser().getAccount())) {
            apiResponseEntity.setErrorCode(5002);
            apiResponseEntity.setErrorMsg("无操作权限，请在系统中维护用户信息");
            return apiResponseEntity;
        }
        Map<String, Integer> res = new HashMap<>();
        BizMaintainPackageOrder order = new BizMaintainPackageOrder();
        order.setAccount(wxSession.getUser().getAccount());
        int totalCount = 0;
        for (MaintainOrderStatus en : MaintainOrderStatus.values()) {
            String status = en.getCode();
            order.setOrderStatus(status);
            order.setPackageType(RepairePackageType.REPAIRE_PACKAGE.getCode());
            Long counts = bizMaintainPackageOrderService.countByConditionForInner(order);
            res.put(en.name(), counts.intValue());
            totalCount = totalCount + counts.intValue();
        }
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setData(res);
        return apiResponseEntity;
    }


    @ApiOperation(value = "微信用户我的页面信息", notes = "微信用户我的页面信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/myMessage", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity myMessage(@RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        if (StringUtils.isNotEmpty(wxSession.getOpenId())) {
            WxMyMessageVo messageVo = wxService.getMyMessage(wxSession.getOpenId());
            apiResponseEntity = new ApiResponseEntity(messageVo);
        }
        return apiResponseEntity;
    }


    @ApiOperation(value = "微信用户我的页面信息", notes = "微信用户我的页面信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/mySubmitList", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity mySubmitList(@RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        if (StringUtils.isNotEmpty(wxSession.getOpenId())) {
            List<AccidentListVo> list = wxService.mySubmitList(wxSession.getOpenId());
            apiResponseEntity.setErrorCode(0);
            apiResponseEntity.setData(list);
        } else {
            apiResponseEntity.setErrorCode(50003);
            apiResponseEntity.setErrorMsg("没有查询到信息");
        }
        return apiResponseEntity;
    }
}