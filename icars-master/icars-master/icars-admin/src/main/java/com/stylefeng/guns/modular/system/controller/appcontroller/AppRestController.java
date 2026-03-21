package com.stylefeng.guns.modular.system.controller.appcontroller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.google.common.collect.ImmutableMap;
import com.stylefeng.guns.core.base.tips.ErrorTip;
import com.stylefeng.guns.core.base.tips.Tip;
import com.stylefeng.guns.core.common.annotion.BussinessLog;
import com.stylefeng.guns.core.common.annotion.Permission;
import com.stylefeng.guns.core.common.constant.Const;
import com.stylefeng.guns.core.common.constant.factory.ConstantFactory;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.core.common.constant.state.ManagerStatus;
import com.stylefeng.guns.core.common.exception.BizExceptionEnum;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.support.HttpKit;
import com.stylefeng.guns.core.util.JedisUtil;
import com.stylefeng.guns.core.util.ToolUtil;
import com.stylefeng.guns.modular.system.constant.*;
import com.stylefeng.guns.modular.system.constant.dictmap.AccdDict;
import com.stylefeng.guns.modular.system.controller.BaseController;
import com.stylefeng.guns.modular.system.model.*;
import com.stylefeng.guns.modular.system.service.*;
import com.stylefeng.guns.modular.system.service.impl.appServiceImpl.AppService;
import com.stylefeng.guns.modular.system.service.impl.appServiceImpl.AppWxPayImpl;
import com.stylefeng.guns.modular.system.utils.HttpUtils;
import com.stylefeng.guns.modular.system.utils.NumberUtil;
import com.stylefeng.guns.modular.system.warpper.BizYckCzmxWarpper;
import com.stylefeng.guns.modular.system.warpper.OpenClaimWarpper;
import com.stylefeng.guns.modular.system.warpper.PushRecordWarpper;
import com.stylefeng.guns.netty.service.IWebSocketMessagePushService;
import com.stylefeng.guns.wxpay.WXPayConstants;
import com.stylefeng.guns.wxpay.WXPayUtil;
import com.stylefeng.guns.wxpay.impl.WxPayBizService;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Instant;
import java.util.*;

/**
 * APP用户认证相关
 *
 * @author liaoxin
 */
@RestController
public class AppRestController extends BaseController {
    private Logger log = LoggerFactory.getLogger(this.getClass());


    @Resource
    private IBizYckBalanceService bizYckBalanceService;

    @Resource
    private IBizOpenNotifyService bizOpenNotifyService;

    @Resource
    private ICityService cityService;


    @Resource
    private IBizOpenFeedbackService bizOpenFeedbackService;


    @Resource
    private IBizYckCzmxService bizYckCzmxService;

    @Resource
    private AppService appService;

    @Resource
    private IAccdService accdService;

    @Resource
    private IUserService iUserService;

    @Resource
    private IBizWxpayOrderService iBizWxpayOrderService;

    private final String SUCCESS = "SUCCESS";

    @Resource
    private IWebSocketMessagePushService webSocketMessagePushService;

    @Resource
    private IBizClaimService bizClaimService;

    @Resource
    private IBizClaimerShowService bizClaimerShowService;

    @Resource
    private INoticeService noticeService;

    @Resource
    private IUserService userService;

    @Resource
    private IOpenClaimService openClaimService;

    @Resource
    private IDeptService deptService;

    @Resource
    private IDictService iDictService;


    @Resource
    private IBizWxpayBillService bizWxpayBillService;

    @Resource
    private JedisUtil jedisUtil;

    @Resource
    private IBizInsuranceCompanyService bizInsuranceCompanyService;

    @Resource
    private IBizWxpayOrderService bizWxpayOrderService;


    @Resource
    private IPushRecordService pushRecordService;

    @Resource
    private IBizWxUserService bizWxUserService;

    @Resource
    private WxPayBizService wxPayBizService;



    /**
     * 查询事故清单列表
     *
     * @return
     */
    @ApiOperation(value = "查询事故信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "session标示token ", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "realness", value = "待处理-1、真实0、不真实1、已撤离2、非事故3", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页多少条数据", required = true, dataType = "String"),
            @ApiImplicitParam(name = "offset", value = "页数-1", required = true, dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "数据存在data中", response = ApiResponseEntity.class),
            @ApiResponse(code = 1001, message = "登录失效", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/api/v1/app/getAccList", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity getAccList(@RequestParam String thirdSessionKey, @RequestParam int realness) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {
            log.error("登录key" + thirdSessionKey + "登录失效");
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录失效,请诱导重新登录");
            return apiResponseEntity;
        }
        Page<PushRecord> page = new PageFactory<PushRecord>().defaultPage();
        List<Map<String, Object>> pushRecords = pushRecordService.getPushRecords2(page, realness, null, null, appSession.getUser().getAccount(), null, true);

        page.setRecords((List<PushRecord>) new PushRecordWarpper(pushRecords).warp());
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("成功");
        apiResponseEntity.setData(page);
        return apiResponseEntity;
    }


    /**
     * 查询事故详情
     *
     * @return
     */
    @ApiOperation(value = "查询事故详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "session标示token ", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "accid", value = "事故id ", required = true, dataType = "String", paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "数据存在data中", response = ApiResponseEntity.class),
            @ApiResponse(code = 1001, message = "登录失效", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/api/v1/app/getAccListDetail", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity getAccListDetail(@RequestParam String thirdSessionKey, @RequestParam int accid) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {
            log.error("登录key" + thirdSessionKey + "登录失效");
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录失效,请诱导重新登录");
            return apiResponseEntity;
        }
        if (accid == 0) {
            apiResponseEntity.setErrorCode(1002);
            apiResponseEntity.setErrorMsg("参数错误");
            return apiResponseEntity;
        }
        List<Map<String, Object>> pushRecords = pushRecordService.getPushRecords3(accid, null, null, appSession.getUser().getAccount(), null, true);
        for (Map<String, Object> map : pushRecords) {
            Object video = map.get("video");
            if (video == null) {
                continue;
            }
            String content = String.valueOf(video);
            if (content.endsWith(".mp4")) {
                map.put("videoFlag", true);
            } else {
                map.put("videoFlag", false);
            }
        }
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("成功");
        apiResponseEntity.setData(pushRecords);
        return apiResponseEntity;
    }


    /**
     * 理赔员更改事故状态，并录入真实的现场信息
     *
     * @return
     */
    @ApiOperation(value = "理赔员更改事故状态，并录入真实的理赔信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "session标示token ", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "id", value = "事故id ", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "realness", value = "真实性 ", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "lng", value = "真实经度", dataType = "Bigdecimal", paramType = "query"),
            @ApiImplicitParam(name = "lat", value = "真实纬度", dataType = "Bigdecimal", paramType = "query"),
            @ApiImplicitParam(name = "realImg", value = "真实照片", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "accImg", value = "事故照片 ", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "topFlag", value = "是否置顶,置顶请传1 ", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "realArrTime", value = "实际到达时间 ", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "realaddress", value = "真实地址 ", dataType = "String", paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "数据存在data中", response = ApiResponseEntity.class),
            @ApiResponse(code = 1001, message = "登录失效", response = ApiResponseEntity.class),
            @ApiResponse(code = 1002, message = "网络异常", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/api/v1/app/updateAccStatus", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity updateAccStatus(@RequestParam String thirdSessionKey, Accident accident) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {
            log.error("登录key" + thirdSessionKey + "登录失效");
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录失效,请诱导重新登录");
            return apiResponseEntity;
        }

        try {
            accdService.updateById(accident);
        } catch (Exception e) {
            log.error("更新事故真实状态", e);
            apiResponseEntity.setErrorCode(1002);
            apiResponseEntity.setErrorMsg("网络异常");
            return apiResponseEntity;
        }
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("成功");
        return apiResponseEntity;
    }

    /**
     * 理赔员下理赔订单
     *
     * @param
     * @return
     */
    @ApiOperation(value = "理赔员下理赔订单", notes = "理赔员下理赔订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "token", required = true, dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "", response = ApiResponseEntity.class),
            @ApiResponse(code = 1001, message = "登录失效", response = ApiResponseEntity.class),
            @ApiResponse(code = 1002, message = "网络异常", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/api/v1/app/claimer/orderadd", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity claimerOrderAdd(OpenClaim openClaim, @RequestParam String deptids, @RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录超时，请重新登录");
            return apiResponseEntity;
        }
        List<OpenClaim> lists = new ArrayList<>();
        String[] deptidStr = deptids.split(",");

        for (String deptid : deptidStr) {
            OpenClaim var1 = new OpenClaim();
            BeanUtils.copyProperties(openClaim, var1);
            var1.setOrderno(openClaimService.getOpenClaimOrderNo());
            var1.setOpenid("");
            var1.setDeptid(Integer.parseInt(deptid));
            var1.setStatus(OpenClaimOrderStatus.INIT.getCode());
            var1.setCreatetime(new Date());
            var1.setclaimer(appSession.getUser().getAccount());
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
        apiResponseEntity.setErrorMsg("成功");
        return apiResponseEntity;
    }


    /**
     * 开放平台理赔员对理赔单信息进行补充
     *
     * @param
     * @return
     */
    @ApiOperation(value = "开放平台理赔员对理赔单信息进行补充", notes = "开放平台理赔员对理赔单信息进行补充")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "开放平台理赔单id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "thirdSessionKey", value = "token", required = true, dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "", response = ApiResponseEntity.class),
            @ApiResponse(code = 1001, message = "登录失效", response = ApiResponseEntity.class),
            @ApiResponse(code = 1002, message = "网络异常", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/api/v1/app/claimer/order/supply", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity claimerOrderSupply(OpenClaim openClaim, @RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (openClaim.getId() == null) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录超时，请重新登录");
            return apiResponseEntity;
        }

        try {
            openClaimService.updateById(openClaim);
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

    //个人中心内容

    /**
     * 忘记密码
     *
     * @param
     * @return
     */
    @ApiOperation(value = " 忘记密码", notes = " 忘记密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "smsCode", value = "短信验证码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "phone", value = "电话号码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "newPassword", value = "新密码", required = true, dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = ApiResponseEntity.class),
            @ApiResponse(code = 1021, message = "短信验证错误", response = ApiResponseEntity.class),
    })
    @RequestMapping(value = "/api/v1/app/forgetPassword", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity forgetPassword(@RequestParam String phone, @RequestParam String newPassword, @RequestParam String smsCode) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (NumberUtil.validNumber(phone)) {
            if (phone.length() != 11) {
                apiResponseEntity.setErrorCode(1021);
                apiResponseEntity.setErrorMsg("入参错误");
                return apiResponseEntity;
            }
            //用户输入的是电话号码
        } else {
            //用户输入是账号
            User byAccount = iUserService.getByAccount(phone);
            if (byAccount == null) {
                apiResponseEntity.setErrorCode(1021);
                apiResponseEntity.setErrorMsg("没有的账号");
                return apiResponseEntity;
            }
            phone = byAccount.getPhone();
        }
        Boolean smsReturn = appService.checkSms(phone, smsCode);
        if (!smsReturn) {
            log.error("开放平台理赔用户注册失败，短信验证失败 phone={};newPassword={};smsCode={};", phone, newPassword, smsCode);
            apiResponseEntity.setErrorCode(1021);
            apiResponseEntity.setErrorMsg("短信验证失败");
            return apiResponseEntity;
        }
        User byAccount = userService.getByAccount(phone);
        //通过验证证明旧密码正确，更改新密码
        User newUser = new User();
        newUser.setSalt(ShiroKit.getRandomSalt(5));
        newUser.setPassword(ShiroKit.md5(newPassword, newUser.getSalt()));
        newUser.setId(byAccount.getId());
        userService.updateById(newUser);
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("成功,请重新登录");
        return apiResponseEntity;
    }


    /**
     * 更改密码
     *
     * @param
     * @return
     */
    @ApiOperation(value = " 更改密码", notes = " 更改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "登录token", required = true, dataType = "String"),
            @ApiImplicitParam(name = "oldPassword", value = "旧密码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "newPassword", value = "新密码", required = true, dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = ApiResponseEntity.class),
            @ApiResponse(code = 1001, message = "登录失效", response = ApiResponseEntity.class),
            @ApiResponse(code = 1002, message = "旧密码不正正确", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/api/v1/app/changePassword", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity changePassword(@RequestParam String oldPassword, @RequestParam String thirdSessionKey, @RequestParam String newPassword) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {
            log.error("登录key" + thirdSessionKey + "登录失效");
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录失效,请诱导重新登录");
            return apiResponseEntity;
        }
        User user = appSession.getUser();
        User byAccount = userService.selectById(user.getId());

        if (!ShiroKit.md5(oldPassword, byAccount.getSalt()).equals(byAccount.getPassword())) {
            apiResponseEntity.setErrorCode(1002);
            apiResponseEntity.setErrorMsg("旧密码不正确");
            return apiResponseEntity;
        }
        //通过验证证明旧密码正确，更改新密码
        User newUser = new User();
        newUser.setSalt(ShiroKit.getRandomSalt(5));
        newUser.setPassword(ShiroKit.md5(newPassword, newUser.getSalt()));
        newUser.setId(byAccount.getId());
        userService.updateById(newUser);
        //移除当前登录信息
        appService.remove(thirdSessionKey);
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("成功,请重新登录");
        return apiResponseEntity;
    }


    //修改个人资料

    /**
     * 修改个人资料
     */
    @ApiOperation(value = "修改个人资料", notes = "修改个人资料")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String"),
            @ApiImplicitParam(name = "headImg", value = "头像", dataType = "String"),
            @ApiImplicitParam(name = "firstImg", value = "模范头像", dataType = "String"),
            @ApiImplicitParam(name = "newPhone", value = "新电话号码", dataType = "String"),
            @ApiImplicitParam(name = "lifeMotto", value = "人生格言", dataType = "String"),
            @ApiImplicitParam(name = "serviceIdea", value = "服务理念", dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "修改成功", response = ApiResponseEntity.class),
            @ApiResponse(code = 1001, message = "登录信息为空", response = ApiResponseEntity.class),
            @ApiResponse(code = 1003, message = "修改失败", response = ApiResponseEntity.class)

    })
    @RequestMapping(value = "/api/v1/app/updateUserDetail", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity updateUserDetail(@RequestParam String thirdSessionKey, String headImg, String firstImg, String lifeMotto, String newPhone, String serviceIdea) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录失效,请诱导重新登录");
            return apiResponseEntity;
        }
        List<BizClaimerShow> getBizClaimerByuserId = bizClaimerShowService.selectList(new EntityWrapper<BizClaimerShow>().eq("userId", appSession.getUser().getId()));
//              List<BizClaimerShow> getBizClaimerByuserId = bizClaimerShowService.selectList(new EntityWrapper<BizClaimerShow>().eq("userId", 44));

        BizClaimerShow bizc = new BizClaimerShow();
        bizc.setMotto(lifeMotto);
        bizc.setStory(serviceIdea);
        bizc.setUserId(appSession.getUser().getId());
        bizc.setName(appSession.getUser().getName());
        bizc.setStatus(0);
        bizc.setImgUrl(firstImg);
        if (getBizClaimerByuserId == null || getBizClaimerByuserId.size() == 0) {
            //还没有设置过签名
            bizClaimerShowService.insert(bizc);
        } else {
            bizClaimerShowService.update(bizc, new EntityWrapper<BizClaimerShow>().eq("userId", appSession.getUser().getId()));
//            bizClaimerShowService.update(bizc,new EntityWrapper<BizClaimerShow>().eq("userId",44));
        }
        List<User> byPhone = userService.getByPhone(newPhone);
        if (byPhone != null && byPhone.size() > 0) {
            apiResponseEntity.setErrorCode(1003);
            apiResponseEntity.setErrorMsg("修改失败，新号码已经注册");
            return apiResponseEntity;
        }
        User newUser = new User();
        System.out.println(headImg);
        System.out.println(newPhone);
        newUser.setId(appSession.getUser().getId());
        boolean flag = false;
        if (headImg != null && !"".equals(headImg) && !"null".equals(newPhone)) {
            newUser.setHeadImg(headImg);
            flag = true;
        }
        if (newPhone != null && !"".equals(newPhone) && !"null".equals(newPhone)) {
            newUser.setPhone(newPhone);
            flag = true;
        }


//        newUser.setId(44);
        if (flag) {
            userService.updateById(newUser);
        }

        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("修改成功");
        return apiResponseEntity;
    }

    /**
     * 查询个人资料
     */
    @ApiOperation(value = "查询个人资料", notes = "查询个人资料")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "修改成功", response = ApiResponseEntity.class),
            @ApiResponse(code = 1001, message = "登录信息为空", response = ApiResponseEntity.class),
    })
    @RequestMapping(value = "/api/v1/app/getUserDetail", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getUserDetail(@RequestParam String thirdSessionKey, String headImg, String firstImg, String lifeMotto, String newPhone, String serviceIdea) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录失效,请诱导重新登录");
            return apiResponseEntity;
        }
        List<BizClaimerShow> getBizClaimerByuserId = bizClaimerShowService.selectList(new EntityWrapper<BizClaimerShow>().eq("userId", appSession.getUser().getId()));
        Map map = new HashMap();
        if (getBizClaimerByuserId != null && getBizClaimerByuserId.size() > 0) {
            map.put("firstImg", getBizClaimerByuserId.get(0).getImgUrl());
            map.put("lifeMotto", getBizClaimerByuserId.get(0).getMotto());
            map.put("serviceIdea", getBizClaimerByuserId.get(0).getStory());
        }
        User user = userService.selectById(appSession.getUser().getId());
        map.put("headImg", user.getHeadImg());
        map.put("newPhone", user.getPhone());

        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("查询成功");
        apiResponseEntity.setData(map);
        return apiResponseEntity;
    }

    @ApiOperation(value = "环信聊天查询理赔员资料", notes = "环信聊天查询理赔员资料")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = ApiResponseEntity.class),
            @ApiResponse(code = 1001, message = "登录信息为空", response = ApiResponseEntity.class),
    })
    @RequestMapping(value = "/api/v1/app/getClaimUserDetail", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getClaimUserDetail(@RequestParam(name = "thirdSessionKey") String thirdSessionKey, @RequestParam(name = "account") String account) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey) || StringUtils.isEmpty(account)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录失效,请诱导重新登录");
            return apiResponseEntity;
        }
        User user = userService.getByAccount(account);
        if (user == null) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        Map result = new HashMap();
        result.put("headImg", user.getHeadImg());
        result.put("newPhone", user.getPhone());

        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("查询成功");
        apiResponseEntity.setData(result);
        return apiResponseEntity;
    }


    //首页数据

    /**
     * 模范理赔顾问列表
     */
    @ApiOperation(value = "模范理赔顾问列表", notes = "模范理赔顾问列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = ApiResponseEntity.class),
            @ApiResponse(code = 1001, message = "登录失效", response = ApiResponseEntity.class),
    })
    @RequestMapping(value = "/api/v1/app/claimerShowget", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity claimerShowget(@RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录失效,请诱导重新登录");
            return apiResponseEntity;
        }
        List<Map<String, Object>> list = bizClaimerShowService.selectClaimerShowsYes();
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("成功");
        apiResponseEntity.setData(list);
        return apiResponseEntity;
    }

    /**
     * 获取新闻列表
     *
     * @param
     * @return
     */
    @ApiOperation(value = "获取新闻列表或者新闻详情", notes = "获取新闻列表或者新闻详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "新闻详情的id,如果传入，则查询详情信息，如果不传则查所有新闻列表", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String", paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = ApiResponseEntity.class),
            @ApiResponse(code = 1001, message = "登录失效", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/api/v1/app/noticelist", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity list(@RequestParam String thirdSessionKey, Integer id) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {
            log.error("登录key" + thirdSessionKey + "登录失效");
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录失效,请诱导重新登录");
            return apiResponseEntity;
        }
        Notice notice = new Notice();
        if (id != null && id != 0) {
            notice.setId(id);
        }
        notice.setNewsType(11);
        List<Map<String, Object>> list = this.noticeService.list(notice);
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setData(list);
        apiResponseEntity.setErrorMsg("成功");
        return apiResponseEntity;
    }

    /**
     * 首页顶获取前三名理赔顾问
     */
    @ApiOperation(value = "首页顶部获取3个理赔佣金模范", notes = "获取3个理赔佣金模范")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String", paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = ApiResponseEntity.class),
            @ApiResponse(code = 1001, message = "登录失效", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/api/v1/app/getThreeModel", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getThreeModel(@RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录失效,请诱导重新登录");
            return apiResponseEntity;
        }
        List<Map<String, Object>> threeModel = bizClaimerShowService.getThreeModel();
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setData(threeModel);
        return apiResponseEntity;
    }


    //登录方法

    /**
     * APP端理赔用户/4S店员登录
     *
     * @return
     */
    @ApiOperation(value = "APP端理赔用户/4S店员登录", notes = "APP端理赔用户/4S店员登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "客户账号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = ApiResponseEntity.class),
            @ApiResponse(code = 1002, message = "该用户尚在审核中", response = ApiResponseEntity.class),
            @ApiResponse(code = 1003, message = "不存在该用户", response = ApiResponseEntity.class),
            @ApiResponse(code = 1004, message = "密码不正确", response = ApiResponseEntity.class)

    })
    @RequestMapping(value = "/api/v1/app/login", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity appUserLogin(User user) {
        long createSeconds = System.currentTimeMillis() / 1000;
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        //根据用户账号查询号码
        User byAccount = userService.getByAccount(user.getAccount());

        if (byAccount == null) {
            apiResponseEntity.setErrorCode(1003);
            apiResponseEntity.setErrorMsg("不存在该用户");
            return apiResponseEntity;
        }

        if (byAccount.getStatus() != 5 && byAccount.getStatus() != 1) {
            apiResponseEntity.setErrorCode(1002);
            apiResponseEntity.setErrorMsg("该用户尚在审核当中");
            return apiResponseEntity;
        }
        if (byAccount.getRoleid() == null || byAccount.getRoleid().equals("")) {
            apiResponseEntity.setErrorCode(1002);
            apiResponseEntity.setErrorMsg("该用户尚在审核当中");
            return apiResponseEntity;
        }

        if (!ShiroKit.md5(user.getPassword(), byAccount.getSalt()).equals(byAccount.getPassword())) {
            apiResponseEntity.setErrorCode(1004);
            apiResponseEntity.setErrorMsg("密码不正确");
            return apiResponseEntity;
        }
        //密码校验通过，创建登录key回写前端
        Long expires = 30 * 24 * 60 * 60L;
        Integer deptid = null;
        try {
            deptid = byAccount.getDeptid();
            if (deptid == null) {
                deptid = 0;
            }
        } catch (Exception e) {
            deptid = 0;
        }


        String appSession = appService.createAppSession(byAccount, expires, createSeconds);
        apiResponseEntity.setData(ImmutableMap.of("deptid", deptid, "sessionId", appSession, "roleId", byAccount.getRoleid(), "account", byAccount.getAccount(), "name", byAccount.getName()));
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("登录成功");
        return apiResponseEntity;
    }

    //注册方法

    /**
     * APP端理赔用户/4S店员注册
     *
     * @param
     * @return
     */
    @ApiOperation(value = "APP端理赔用户/4S店员注册", notes = "APP端理赔用户/4S店员注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "客户姓名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "客户手机", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "deptid", value = "部门id，可不传", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "smsCode", value = "短信code", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "roleid", value = "角色,理赔员传入7,4S店员传入6", required = true, dataType = "String", paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = ApiResponseEntity.class),
            @ApiResponse(code = 1012, message = "短信验证失败", response = ApiResponseEntity.class),
            @ApiResponse(code = 1003, message = "该用户尚在审核中", response = ApiResponseEntity.class),
            @ApiResponse(code = 1002, message = "网络异常", response = ApiResponseEntity.class),
            @ApiResponse(code = 1004, message = "该用户已注册", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/api/v1/app/register", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity appClaimerRegister(User user, @RequestParam(value = "smsCode") String smsCode) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(user.getPhone()) || StringUtils.isEmpty(user.getName())) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        //进行短信验证
        Boolean smsReturn = appService.checkSms(user.getPhone(), smsCode);
        if (!smsReturn) {
            log.error("开放平台理赔用户注册失败，短信验证失败 user={};smsCode={}", user, smsCode);
            apiResponseEntity.setErrorCode(1012);
            apiResponseEntity.setErrorMsg("短信验证失败");
            return apiResponseEntity;
        }
        //查询客户信息，是否已经注册或者是否是在注册当中，注册过，返回已注册，注册中，返回审核中
        List<User> byPhone = userService.getByPhone(user.getPhone());

        if (byPhone != null && byPhone.size() != 0) {
            User byAccount = byPhone.get(0);
            Integer status = byAccount.getStatus();
            if (status == ManagerStatus.REGISTERING.getCode()) {
                log.error("开放平台理赔用户注册失败，该用户尚在审核中");
                apiResponseEntity.setErrorCode(1003);
                apiResponseEntity.setErrorMsg("该用户尚在审核中");
                return apiResponseEntity;
            }
            log.error("开放平台理赔用户注册失败，该用户已注册");
            apiResponseEntity.setErrorCode(1004);
            apiResponseEntity.setErrorMsg("该用户已注册");
            return apiResponseEntity;
        }

        //未注册，则进行注册
        user.setAccount(user.getPhone());
        user.setBirthday(new Date());
        user.setCreatetime(new Date());
        user.setSalt(ShiroKit.getRandomSalt(5));
        user.setPassword(ShiroKit.md5(user.getPassword(), user.getSalt()));
        user.setStatus(ManagerStatus.REGISTERING.getCode());
        user.setSex(1);
        //根据是否存在depId,该角色是属于4S店员还是理赔员


        try {
            userService.insert(user);
        } catch (Exception e) {
            log.error("开放平台理赔用户注册", e);
            apiResponseEntity.setErrorCode(1002);
            apiResponseEntity.setErrorMsg("网络异常");
            return apiResponseEntity;
        }
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("成功，请静待审核");
        return apiResponseEntity;
    }

    /**
     * APP端理赔用户/4S店员注册号码提示
     *
     * @return
     */
    @ApiOperation(value = "APP端理赔用户/4S店员注册号码提示", notes = "APP端理赔用户/4S店员注册号码提示")
    @ApiImplicitParam(name = "phone", value = "电话号码 ", required = true, dataType = "String", paramType = "query")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = ApiResponseEntity.class),
            @ApiResponse(code = 1002, message = "校验号码失败", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/api/v1/app/checkPhone", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity checkPhone(@RequestParam(required = true, value = "phone") String phone) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        //根据用户账号查询号码
        List<User> byPhone = userService.getByPhone(phone);
        if (byPhone != null && byPhone.size() != 0) {
            apiResponseEntity.setErrorCode(1002);
            apiResponseEntity.setErrorMsg("校验号码失败，已存在用户");
            return apiResponseEntity;
        }
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("校验成功");
        return apiResponseEntity;
    }

    /**
     * 短信发送接口
     *
     * @return
     */
    @ApiOperation(value = "短信发送接口", notes = "短信发送接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "电话", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "flag", value = "发送参数,注册短信，请填1，忘记密码请填2", required = true, dataType = "String", paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/api/v1/app/sendSms", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity sendSms(@RequestParam(required = true, value = "phone") String phone, @RequestParam(required = true, value = "flag") String flag) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        //判断用户输入的是电话号码还是账号  比如：gaoxiaoming
        if (NumberUtil.validNumber(phone)) {
            //用户输入电话号码
            if (phone.length() != 11) {
                apiResponseEntity.setErrorCode(1001);
                apiResponseEntity.setErrorMsg("入参错误");
                return apiResponseEntity;
            }
            JSONObject o = appService.sendSms(flag, phone, "");
            apiResponseEntity.setErrorCode(Integer.parseInt(o.get("retCode").toString()));
            apiResponseEntity.setErrorMsg((String) o.get("retMsg"));
        } else {
            //用户输入账号
            User byAccount = iUserService.getByAccount(phone);
            if (byAccount == null) {
                apiResponseEntity.setErrorCode(1001);
                apiResponseEntity.setErrorMsg("入参错误");
            } else {
                JSONObject o = appService.sendSms(flag, byAccount.getPhone(), "");
                apiResponseEntity.setErrorCode(Integer.parseInt(o.get("retCode").toString()));
                apiResponseEntity.setErrorMsg((String) o.get("retMsg"));
            }
        }
        return apiResponseEntity;
    }

    /**
     * 查询保险公司列表
     *
     * @return
     */
    @ApiOperation(value = "查询保险公司列表", notes = "查询保险公司列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "登录token", required = true, dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = ApiResponseEntity.class),
            @ApiResponse(code = 1001, message = "登录失效", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/api/v1/app/getInsuranceCompany", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity getInsuranceCompany(@RequestParam(required = true, value = "thirdSessionKey") String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {
            log.error("登录key" + thirdSessionKey + "登录失效");
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录失效,请诱导重新登录");
            return apiResponseEntity;
        }
        List<BizInsuranceCompany> list = bizInsuranceCompanyService.list();
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("成功");
        apiResponseEntity.setData(list);
        return apiResponseEntity;

    }


    /**
     * 查询4S店列表
     */
    @ApiOperation(value = "查询4S店列表", notes = "查询4S店列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dict", value = "门店所在区域", required = false, dataType = "String"),
            @ApiImplicitParam(name = "qcpp", value = "门店服务汽车品牌", required = false, dataType = "String"),
            @ApiImplicitParam(name = "limit", value = "每页多少条数据", required = true, dataType = "String"),
            @ApiImplicitParam(name = "offset", value = "页数-1", required = true, dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 1005, message = "参数错误", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/api/v1/app/storeget", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity getStoreInfo(@RequestParam(required = false) String dict, @RequestParam(required = false) String qcpp) {
        if (StringUtils.isNotEmpty(qcpp)) {
            qcpp = qcpp.trim();
        }
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        HttpServletRequest request = HttpKit.getRequest();
        String limit = request.getParameter("limit");//每页多少条数据
        String offset = request.getParameter("offset");
        if (limit == null || limit.equals("") || offset.equals("") || offset == null) {
            apiResponseEntity.setErrorCode(1005);
            apiResponseEntity.setErrorMsg("参数错误");
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
                    if (var1.getCompanyUrls2() == null) {
                        List<Map> list = new ArrayList(0);
                        var1.setCompanyUrls2(list);
                    }
                    Map map = new HashMap();
                    map.put("name", var3.getName());
                    map.put("url", var3.getUrl());
                    var1.getCompanyUrls2().add(map);
                }
            }
        }
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setData(page);
        return apiResponseEntity;
    }


    /**
     * 查询4S店详情信息
     */
    @ApiOperation(value = "查询4S店详情信息", notes = "查询4S店详情信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String"),
            @ApiImplicitParam(name = "deptid", value = "部门id,传入可查专门的4S店信息，如果不传，则根据登录信息所归属的4S店查询", dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 1001, message = "登录失效", response = ApiResponseEntity.class),
            @ApiResponse(code = 1002, message = "没有查找到对应4S店", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/api/v1/app/storegetOne", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    ApiResponseEntity storegetOne(@RequestParam String thirdSessionKey, Integer deptid) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {
            log.error("登录key" + thirdSessionKey + "登录失效");
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录失效,请诱导重新登录");
            return apiResponseEntity;
        }
        if (deptid == null || deptid == 0) {
            deptid = appSession.getUser().getDeptid();
        }
        Dept dept = deptService.selectById(deptid);

        if (dept == null) {
            apiResponseEntity.setErrorCode(1002);
            apiResponseEntity.setErrorMsg("没有查找到对应4S店");
            return apiResponseEntity;
        }
        //分离门店首页图片和明细图片
        if (StringUtils.isNotEmpty(dept.getImgUrls())) {
            String[] imgArray = dept.getImgUrls().split("\\|");
            dept.setIndexImg(imgArray[0]);
            if (imgArray.length > 1) {
                dept.setDetailImg(Arrays.copyOfRange(imgArray, 1, imgArray.length));
            }
        } else {
            String[] list = {};
            dept.setDetailImg(list);
        }


        //根据门店保存的合作保险公司id，适配出保险公司链接
        if (StringUtils.isNotEmpty(dept.getCompanyids())) {
            List<Integer> ids = new ArrayList<>();
            for (String var2 : dept.getCompanyids().split(",")) {
                ids.add(Integer.parseInt(var2));
            }
            List<BizInsuranceCompany> resList = bizInsuranceCompanyService.selectBatchIds(ids);
            for (BizInsuranceCompany var3 : resList) {
                if (dept.getCompanyUrls2() == null) {
                    dept.setCompanyUrls2(new ArrayList<>());
                }
                Map map = new HashMap();
                map.put("name", var3.getName());
                map.put("url", var3.getUrl());
                map.put("id", var3.getId());
                dept.getCompanyUrls2().add(map);
            }
        } else {
            List<Map> list = new ArrayList(0);
            dept.setCompanyUrls2(list);
        }
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setData(dept);
        return apiResponseEntity;
    }


    /**
     * 录入4s店信息
     */
    @ApiOperation(value = "录入4s店信息", notes = "录入4s店信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String"),
    })
    @ApiResponses({
            @ApiResponse(code = 1001, message = "登录失效", response = ApiResponseEntity.class),
            @ApiResponse(code = 1003, message = "该用户已经新增过厅店了", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/api/v1/app/inserDept", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity insertDept(@RequestParam String thirdSessionKey, Dept dept) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {
            log.error("登录key" + thirdSessionKey + "登录失效");
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录失效,请诱导重新登录");
            return apiResponseEntity;
        }

//        dept.setSynopsis("18108108527");
//        String synopsis = dept.getSynopsis();
//        List<Dept> synopsis1 = deptService.selectList(new EntityWrapper<Dept>().eq("synopsis", synopsis));
//        if(synopsis1!=null&&synopsis1.size()>0){
//            apiResponseEntity.setErrorCode(1003);
//            apiResponseEntity.setErrorMsg("该号码已经注册厅店");
//            return apiResponseEntity;
//        }
        List<Dept> accounts = deptService.selectList(new EntityWrapper<Dept>().eq("account", appSession.getUser().getAccount()));
        if (accounts != null && accounts.size() > 0) {
            Dept dept1 = accounts.get(0);
            User user = new User();
            user.setDeptid(dept1.getId());
            userService.update(user, new EntityWrapper<User>().eq("account", appSession.getUser().getAccount()));

            //该用户已经新增过4S店了
            apiResponseEntity.setErrorCode(1003);
            apiResponseEntity.setErrorMsg("该用户已经新增过厅店了");
            return apiResponseEntity;
        }
        deptService.insert(dept);
        List<Dept> accounts2 = deptService.selectList(new EntityWrapper<Dept>().eq("account", appSession.getUser().getAccount()));
        if (accounts2 != null && accounts2.size() > 0) {
            Dept dept1 = accounts2.get(0);
            User user = new User();
            user.setDeptid(dept1.getId());
            userService.update(user, new EntityWrapper<User>().eq("account", appSession.getUser().getAccount()));
            User user1 = appSession.getUser();
            user1.setDeptid(dept1.getId());
            appSession.setUser(user1);
        }
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("成功");
        return apiResponseEntity;
    }


    /**
     * 修改4s店信息
     */
    @ApiOperation(value = "修改4s店信息", notes = "修改4s店信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String"),
            @ApiImplicitParam(name = "id", value = "4S店ID", required = true, dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 1001, message = "登录失效", response = ApiResponseEntity.class),
            @ApiResponse(code = 1003, message = "该用户不属于该4S店", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/api/v1/app/updateDept", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity updateDept(@RequestParam String thirdSessionKey, Dept dept) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {
            log.error("登录key" + thirdSessionKey + "登录失效");
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录失效,请诱导重新登录");
            return apiResponseEntity;
        }
        if (appSession.getUser().getDeptid() != dept.getId()) {
            apiResponseEntity.setErrorCode(1003);
            apiResponseEntity.setErrorMsg("该用户不属于该4S店");
            return apiResponseEntity;
        }
        deptService.updateById(dept);
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("成功");
        return apiResponseEntity;
    }


    /**
     * 获取汽车品牌信息
     */
//    @ApiOperation(value = "获取汽车品牌信息", notes = "获取汽车品牌信息")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String"),
//            @ApiImplicitParam(name = "dict", value = "数据字典PARENT，请传CAR_TYPE", required = true, dataType = "String")
//    })
//    @RequestMapping(value = "/api/v1/app/carType/get", method = RequestMethod.POST, produces = "application/json")
//    @ResponseBody
    public ApiResponseEntity getCarType(@RequestParam String thirdSessionKey, @RequestParam(required = true) String dict) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1002);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {
            log.error("登录key" + thirdSessionKey + "登录失效");
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录失效,请诱导重新登录");
            return apiResponseEntity;
        }
        List<Dict> list = iDictService.selectByParentCode(dict);
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setData(list);
        return apiResponseEntity;
    }

    @ApiOperation(value = "开放平台理赔用户绑定银行卡", notes = "开放平台理赔用户绑定银行卡")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bankcard", value = "银行卡号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "bankUserName", value = "银行卡户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "bankName", value = "开户银行", required = true, dataType = "String"),
            @ApiImplicitParam(name = "bankUserName", value = "用户户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "bankSecondName", value = "开户支行名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "idcard", value = "身份证", required = true, dataType = "String"),
            @ApiImplicitParam(name = "thirdSessionKey", value = "token", required = true, dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 1001, message = "登录失效", response = ApiResponseEntity.class),
            @ApiResponse(code = 1002, message = "银行卡位数少了", response = ApiResponseEntity.class),
            @ApiResponse(code = 1003, message = "网络异常", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/api/v1/app/claimerBindCard", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity claimerBindCard(User user, @RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(user.getBankcard())) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {

            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录失效,请诱导重新登录");
            return apiResponseEntity;
        }
        if (user.getBankcard().length() < 16) {
            apiResponseEntity.setErrorCode(1002);
            apiResponseEntity.setErrorMsg("银行卡位数少了");
            return apiResponseEntity;
        }
        try {
            user.setId(appSession.getUser().getId());
            userService.updateById(user);
        } catch (Exception e) {
            log.error("开放平台理赔用户绑定银行卡", e);
            apiResponseEntity.setErrorCode(1003);
            apiResponseEntity.setErrorMsg("网络异常");
            return apiResponseEntity;
        }
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("成功");
        return apiResponseEntity;
    }

    @Resource
    private AppWxPayImpl appWxPay;

    @Value("${wx.pay.mchKey}")
    private String mchKey;

    /**
     * APP充值统一下单?signType值要不要问题，parterId的问题,明天要最新的技术文档
     *
     * @param
     * @return
     */
    @ApiOperation(value = "APP充值统一下单-待联调", notes = "APP充值统一下单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "amount", value = "充值金额", required = true, dataType = "BigDecimal"),
            @ApiImplicitParam(name = "thirdSessionKey", value = "token", required = true, dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 1001, message = "登录失效", response = ApiResponseEntity.class),
            @ApiResponse(code = 1002, message = "网络异常", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/api/v1/app/wxpay/unifiedOrder", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity unifiedOrder(@RequestParam BigDecimal amount, @RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) != 1) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录失效，请诱导重新登录");
            return apiResponseEntity;
        }
        String outTradeNo = bizWxpayBillService.getWxUnifiedOrderNo();
        HttpServletRequest request = HttpKit.getRequest();
        String ipAddress = getIPAddress(request);
        try {
            WxPayAppOrderResult wxPayAppOrderResult = appWxPay.unifiedOrder(amount, outTradeNo, ipAddress);

            BizWxpayOrder bizWxpayOrder = new BizWxpayOrder();
            bizWxpayOrder.setStatus(WxPayOrderStatus.INIT.getCode());
            bizWxpayOrder.setAmount(amount);
            bizWxpayOrder.setCreateTime(new Date());
            bizWxpayOrder.setOpenid("1");
            bizWxpayOrder.setOutTradeNo(outTradeNo);
            bizWxpayOrder.setPrepayId(wxPayAppOrderResult.getPrepayId());
            bizWxpayOrder.setAccount(appSession.getUser().getAccount());
            boolean flag = bizWxpayOrderService.insert(bizWxpayOrder);
            if (flag) {
                Map<String, String> responseMap = new HashMap<>();
                responseMap.put("appid", wxPayAppOrderResult.getAppId());
                responseMap.put("partnerid", wxPayAppOrderResult.getPartnerId());
                responseMap.put("prepayid", wxPayAppOrderResult.getPrepayId());
                responseMap.put("noncestr", WXPayUtil.generateNonceStr());
                responseMap.put("package", "Sign=WXPay");
                responseMap.put("timestamp", System.currentTimeMillis() / 1000 + "");
                String sign = WXPayUtil.generateSignature(responseMap, mchKey, WXPayConstants.SignType.MD5);
                responseMap.put("sign", sign);
                apiResponseEntity.setData(responseMap);
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
     * @param
     * @return
     */
    @ApiOperation(value = "app充值-查询充值是否成功", notes = "查询充值是否成功")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "prepayId", value = "prepayId", required = true, dataType = "String"),
            @ApiImplicitParam(name = "thirdSessionKey", value = "token", required = true, dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 1001, message = "登录失效", response = ApiResponseEntity.class),
            @ApiResponse(code = 1002, message = "网络异常", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/api/v1/app/wxpay/findOrderByPrepayId", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity findOrderByPrepayId(@RequestParam String prepayId, @RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(prepayId)) {
            throw new GunsException(BizExceptionEnum.PARAM_ERR);
        }
        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录失效，请诱导重新登录");
            return apiResponseEntity;
        }
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("account", appSession.getUser().getAccount());
        columnMap.put("prepay_id", prepayId);
        List<BizWxpayOrder> list = iBizWxpayOrderService.selectByMap(columnMap);
        if (list.size() < 1) {
            log.error("###当前微信账号下没有待支付订单，查询失败。### prepayid={};###account={}###", prepayId, appSession.getUser().getAccount());
            throw new GunsException(BizExceptionEnum.WX_ORDER_NULL);
        }

        BizWxpayOrder bizWxpayOrder = list.get(0);
        if (bizWxpayOrder.getStatus() == WxPayOrderStatus.PAY_SUCCESS.getCode()) {
            apiResponseEntity.setErrorCode(0);
            apiResponseEntity.setErrorMsg("支付成功");
            return apiResponseEntity;
        }
        //主动查询支付状态
        try {
            WxPayOrderQueryResult wxPayOrderQueryResult = appWxPay.queryOrder(list.get(0).getOutTradeNo());
            if (wxPayOrderQueryResult.getTradeState().equals(SUCCESS)) {
                //更新用户信息
                iBizWxpayOrderService.updateUserBalance(bizWxpayOrder, new BigDecimal(wxPayOrderQueryResult.getTotalFee()),
                        wxPayOrderQueryResult.getOpenid(), wxPayOrderQueryResult.getOutTradeNo());
                apiResponseEntity.setErrorCode(0);
                apiResponseEntity.setErrorMsg("支付成功");
                return apiResponseEntity;
            } else {
                apiResponseEntity.setErrorCode(0);
                apiResponseEntity.setErrorMsg(covertPayStatus(wxPayOrderQueryResult.getTradeState()));
            }
        } catch (WxPayException e) {
            log.error("###主动查询支付状态 ### prepayid={};###account={}###", prepayId, appSession.getUser().getAccount());
            throw new GunsException(BizExceptionEnum.WX_ORDER_ERROR);
        }
        apiResponseEntity.setErrorCode(500);
        apiResponseEntity.setErrorMsg("未查询到支付结果信息！");
        return apiResponseEntity;
    }

    private String covertPayStatus(String str) {
        switch (str) {
            case SUCCESS:
                return "支付成功";
            case "REFUND":
                return "转入退款";
            case "NOTPAY":
                return "未支付";
            case "CLOSED":
                return "已关闭";
            case "REVOKED":
                return "已撤销（刷卡支付）";
            case "USERPAYING":
                return "USERPAYING";
            case "PAYERROR":
                return "支付失败(其他原因，如银行返回失败)";
        }
        return "";
    }

    private String getIPAddress(HttpServletRequest request) {
        String ip = null;

        //X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }

        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }

        //还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    //4S店员逻辑

    /**
     * 理赔列表查询
     *
     * @return
     */
    @ApiOperation(value = "理赔列表查询")
    @ApiImplicitParam(name = "thirdSessionKey", value = "session标示token ", required = true, dataType = "String")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "flag", value = "理赔员查询本人订单传1，4s店员查询本店订单传2", required = true, dataType = "BigDecimal"),
            @ApiImplicitParam(name = "thirdSessionKey", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(name = "limit", value = "每页多少条数据", required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态，INIT(-1, \"未接车\"), RECEPTED(0, \"已接车\"), SERVICING(1, \"服务中\"), PLACED(2, \"已交车\"), SETTLED(3, \"已结算\"), TRANSED(4, \"打款完成\"), CANCEL(5, \"作废\"), UNREACH(6, \"未到店\"),CERTRECEIVE(7,\"确认接车\"),CERTMONEY(8,\"已定损\");", dataType = "String"),
            @ApiImplicitParam(name = "offset", value = "页数-1", required = true, dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 1001, message = "登录失效", response = ApiResponseEntity.class),
            @ApiResponse(code = 1005, message = "参数错误", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/api/v1/app/queryClaimList", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity queryClaimList(@RequestParam String thirdSessionKey, @RequestParam String flag, String status) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录失效,请诱导重新登录");
            return apiResponseEntity;
        }
        User user = appSession.getUser();

        User nowUser = userService.getByAccount(user.getAccount());
        HttpServletRequest request = HttpKit.getRequest();
        String limit = request.getParameter("limit");//每页多少条数据
        String offset = request.getParameter("offset");
        if (limit == null || limit.equals("") || offset.equals("") || offset == null) {
            apiResponseEntity.setErrorCode(1005);
            apiResponseEntity.setErrorMsg("参数错误");
            return apiResponseEntity;
        }
        Page<OpenClaim> page = new PageFactory<OpenClaim>().defaultPage();
        OpenClaim claim = new OpenClaim();
        if (status != null && !"".equals(status)) {
            claim.setStatus(Integer.parseInt(status));
        }
        if (flag.equals("1")) {
            claim.setclaimer(nowUser.getAccount());
        } else {
            claim.setDeptid(nowUser.getDeptid());
//            claim.setDeptid(41);
        }
        List<Map<String, Object>> claims = openClaimService.selectByCondition(page, null, claim, null, null, null, null, "", true);
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("成功");
        page.setRecords((List<OpenClaim>) new OpenClaimWarpper(claims).warp());
        apiResponseEntity.setData(page);
        return apiResponseEntity;
    }


    /**
     * 理赔列表查询,0和1状态合并
     *
     * @return
     */
    @ApiOperation(value = "理赔列表查询")
    @ApiImplicitParam(name = "thirdSessionKey", value = "session标示token ", required = true, dataType = "String")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "flag", value = "理赔员查询本人订单传1，4s店员查询本店订单传2", required = true, dataType = "BigDecimal"),
            @ApiImplicitParam(name = "thirdSessionKey", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(name = "limit", value = "每页多少条数据", required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态，INIT(-1, \"未接车\"), RECEPTED(0, \"已接车\"), SERVICING(1, \"服务中\"), PLACED(2, \"已交车\"), SETTLED(3, \"已结算\"), TRANSED(4, \"打款完成\"), CANCEL(5, \"作废\"), UNREACH(6, \"未到店\"),CERTRECEIVE(7,\"确认接车\"),CERTMONEY(8,\"已定损\");", dataType = "String"),
            @ApiImplicitParam(name = "offset", value = "页数-1", required = true, dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 1001, message = "登录失效", response = ApiResponseEntity.class),
            @ApiResponse(code = 1005, message = "参数错误", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/api/v1/app/queryClaimListForExtra", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity queryClaimListForExtra(@RequestParam String thirdSessionKey, @RequestParam String flag, String status) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录失效,请诱导重新登录");
            return apiResponseEntity;
        }
        User user = appSession.getUser();

        User nowUser = userService.getByAccount(user.getAccount());
        HttpServletRequest request = HttpKit.getRequest();
        String limit = request.getParameter("limit");//每页多少条数据
        String offset = request.getParameter("offset");
        if (limit == null || limit.equals("") || offset.equals("") || offset == null) {
            apiResponseEntity.setErrorCode(1005);
            apiResponseEntity.setErrorMsg("参数错误");
            return apiResponseEntity;
        }
        Page<OpenClaim> page = new PageFactory<OpenClaim>().defaultPage();
        OpenClaim claim = new OpenClaim();
        if (status != null && !"".equals(status)) {
            claim.setStatus(Integer.parseInt(status));
        }
        if (flag.equals("1")) {
            claim.setclaimer(nowUser.getAccount());
        } else {
            claim.setDeptid(nowUser.getDeptid());
//            claim.setDeptid(41);
        }
        List<Map<String, Object>> claims = openClaimService.selectByCondition2(page, null, claim, null, null, null, null, "", true);
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("成功");
        page.setRecords((List<OpenClaim>) new OpenClaimWarpper(claims).warp());
        apiResponseEntity.setData(page);
        return apiResponseEntity;
    }


    /**
     * 理赔单详情查询
     *
     * @return
     */
    @ApiOperation(value = "4S店员理赔单详情查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "session标示token ", required = true, dataType = "String"),
            @ApiImplicitParam(name = "orderNo", value = "理赔单单号", required = true, dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 1001, message = "登录失效", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/api/v1/app/queryClaimOrder", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity queryClaimOrder(@RequestParam String thirdSessionKey, @RequestParam String orderNo) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {
            log.error("登录key" + thirdSessionKey + "登录失效");
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录失效,请诱导重新登录");
            return apiResponseEntity;
        }

        OpenClaim openClaim = new OpenClaim();
        openClaim.setOrderno(orderNo);
        Map<String, Object> res = openClaimService.selectMap(new EntityWrapper<OpenClaim>().eq("orderno", orderNo));
        Dept dept = deptService.selectById((Serializable) res.get("deptid"));
        res.put("deptName", dept.getFullname());
        res.put("statusName", ConstantFactory.me().getOpenClaimOrderStatusName((Integer) res.get("status")));
        res.put("claimImgList", res.get("claimImg") == null ? null : res.get("claimImg").toString().split("\\|"));
        res.put("detailImgList", res.get("detailImg") == null ? null : res.get("detailImg").toString().split("\\|"));
        String[] b = new String[0];
        if (res.get("claimImgList") == null || "".equals(res.get("claimImgList")) || "null".equals(res.get("claimImgList"))) {
            res.put("claimImgList", b);
        }
        if (res.get("detailImgList") == null || "".equals(res.get("detailImgList")) || "null".equals(res.get("claimImgList"))) {
            res.put("detailImgList", b);
        }
        LogObjectHolder.me().set(openClaim);
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("获取成功");
        apiResponseEntity.setData(res);

        return apiResponseEntity;
    }


    /**
     * 4s店员更改理赔单状态
     *
     * @return
     */
    @ApiOperation(value = "4s店员更改理赔单状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "session标示token ", required = true, dataType = "String"),
            @ApiImplicitParam(name = "claimId", value = "理赔单id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "    INIT(-1, \"未接车\"), RECEPTED(0, \"已接车\"), SERVICING(1, \"服务中\"), PLACED(2, \"已交车\"), SETTLED(3, \"已结算\"), TRANSED(4, \"打款完成\"), CANCEL(5, \"作废\"), UNREACH(6, \"未到店\"),CERTRECEIVE(7,\"确认接车\"),CERTMONEY(8,\"已定损\");\n", required = true, dataType = "String"),
            @ApiImplicitParam(name = "defineMoney", value = "定损金额", dataType = "BigDecimal"),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = ApiResponseEntity.class),
            @ApiResponse(code = 1002, message = "更改状态错误", response = ApiResponseEntity.class),
            @ApiResponse(code = 1003, message = "请定损", response = ApiResponseEntity.class),
            @ApiResponse(code = 1001, message = "登录失效", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/api/v1/app/updateClaimOrder", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity updateClaimOrder(@RequestParam String thirdSessionKey, @RequestParam Integer claimId, @RequestParam String status, BigDecimal defineMoney) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {
            log.error("登录key" + thirdSessionKey + "登录失效");
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录失效,请诱导重新登录");
            return apiResponseEntity;
        }

        OpenClaim openClaim = new OpenClaim();
        openClaim.setId(claimId);

        OpenClaim openClaimEnty = openClaimService.selectOne(new EntityWrapper<OpenClaim>().eq("id", claimId));
        Integer status2 = openClaimEnty.getStatus();
        //查询到的本身订单值
        String status1 = status2.toString();
        int statusold = Integer.parseInt(status1);
        //查询到的修改值
        int statusnew = Integer.parseInt(status);

        if (statusnew < statusold) {
            apiResponseEntity.setErrorCode(1002);
            apiResponseEntity.setErrorMsg("更改状态错误");
            return apiResponseEntity;
        }

//        if (status.equals("-1") && !"0".equals(status1)) {
//            apiResponseEntity.setErrorCode(1002);
//            apiResponseEntity.setErrorMsg("更改状态错误");
//            return apiResponseEntity;
//        }
//        if (status.equals("7") && !"8".equals(status1)) {
//            apiResponseEntity.setErrorCode(1003);
//            apiResponseEntity.setErrorMsg("请定损");
//            return apiResponseEntity;
//        }
//        if (status.equals("8") && !"2".equals(status1)) {
//            apiResponseEntity.setErrorCode(1002);
//            apiResponseEntity.setErrorMsg("更改状态错误");
//            return apiResponseEntity;
//        }
//        if (status1.equals("8")) {
//            if (defineMoney == null || defineMoney.equals("")) {
//                apiResponseEntity.setErrorCode(1002);
//                apiResponseEntity.setErrorMsg("请定损");
//                return apiResponseEntity;
//            }
//            BigDecimal amt = new BigDecimal(String.valueOf(defineMoney));
//            int i = amt.compareTo(BigDecimal.ZERO);
//            if (i == 1) {
//            } else {
//                apiResponseEntity.setErrorCode(1002);
//                apiResponseEntity.setErrorMsg("请定损");
//                return apiResponseEntity;
//            }
//        }
//        if (status.equals("2") && !"3".equals(status1)) {
//            apiResponseEntity.setErrorCode(1002);
//            apiResponseEntity.setErrorMsg("更改状态错误");
//            return apiResponseEntity;
//        }


//
        Dept dept = deptService.selectById(appSession.getUser().getDeptid());
//        Dept dept = deptService.selectById(41);
        if (dept == null) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        openClaim.setStatus(Integer.parseInt(status));
        if (defineMoney != null) {
            BigDecimal newBigDecimal = new BigDecimal(0);
            int i = defineMoney.compareTo(newBigDecimal);
            if (defineMoney != null && i > 0) {
                openClaim.setFixloss(defineMoney);
                Double scaleForEmp = dept.getScaleForEmp();
                Double scaleForCompany = dept.getScaleForCompany();
                openClaim.setRebateForCompany(scaleForCompany == null ? BigDecimal.ZERO : defineMoney.multiply(new BigDecimal(scaleForCompany)).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                if (openClaim.getRebateForCompany() != null && openClaim.getRebateForCompany().compareTo(BigDecimal.ZERO) == 1) {
                    openClaim.setRebateForEmp(scaleForEmp == null ? BigDecimal.ZERO : openClaim.getRebateForCompany().multiply(new BigDecimal(scaleForEmp)).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                }
            }
        }

        openClaimService.updateById(openClaim);
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("成功");
        return apiResponseEntity;
    }


    /**
     * 理赔员定位坐标上传接口
     *
     * @return
     */
    @ApiOperation(value = "理赔员定位坐标上传接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "session标示token ", required = true, dataType = "String"),
            @ApiImplicitParam(name = "lng", value = "经度", required = true, dataType = "Double"),
            @ApiImplicitParam(name = "lat", value = "纬度", required = true, dataType = "Double")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = ApiResponseEntity.class),
            @ApiResponse(code = 1001, message = "登录失效", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/api/v1/app/upDestination", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity upDestination(@RequestParam String thirdSessionKey, @RequestParam Double lng, @RequestParam Double lat) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {
            log.error("登录key" + thirdSessionKey + "登录失效");
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录失效,请诱导重新登录");
            return apiResponseEntity;
        }
        User user = appSession.getUser();
        appService.addUserDes(user, lat, lng);
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("成功");
        return apiResponseEntity;
    }


    /**
     * 理赔员停止收集坐标
     *
     * @return
     */
    @ApiOperation(value = "理赔员停止收集坐标")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "session标示token ", required = true, dataType = "String")

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = ApiResponseEntity.class),
            @ApiResponse(code = 1001, message = "登录失效", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/api/v1/app/reMoveDestination", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity reMoveDestination(@RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {
            log.error("登录key" + thirdSessionKey + "登录失效");
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录失效,请诱导重新登录");
            return apiResponseEntity;
        }
        User user = appSession.getUser();
        appService.removeUserDes(user);
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("成功");
        return apiResponseEntity;
    }


    /**
     * 上传文件，要注意区分上传图片id
     *
     * @param
     * @return
     */
    @ApiOperation(value = "上传文件", notes = "上传文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "session标示token ", required = true, dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "上传地址会返回在data中", response = ApiResponseEntity.class),
            @ApiResponse(code = 1001, message = "登录失效", response = ApiResponseEntity.class),
            @ApiResponse(code = 1002, message = "上传失败", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/api/v1/app/uploadFileForApp", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity uploadFileForApp(@RequestParam String thirdSessionKey, HttpServletRequest request) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {
            log.error("登录key" + thirdSessionKey + "登录失效");
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录失效,请诱导重新登录");
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
        apiResponseEntity.setErrorMsg("成功");
        return apiResponseEntity;
    }


    /**
     * 上传头像图片
     *
     * @param
     * @return
     */
    @ApiOperation(value = "上传头像图片", notes = "上传头像图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "session标示token ", required = true, dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "上传地址会返回在data中", response = ApiResponseEntity.class),
            @ApiResponse(code = 1001, message = "登录失效", response = ApiResponseEntity.class),
            @ApiResponse(code = 1002, message = "上传失败", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/api/v1/app/uploadFileForHeadImg", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity uploadFileForHeadImg(@RequestParam String thirdSessionKey, HttpServletRequest request) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {
            log.error("登录key" + thirdSessionKey + "登录失效");
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录失效,请诱导重新登录");
            return apiResponseEntity;
        }
        List<MultipartFile> files = ((MultipartHttpServletRequest) request)
                .getFiles("file");
        try {
            List<String> urls = userService.uploadFileForUser(files);
            apiResponseEntity.setData(urls);
        } catch (IOException e) {
            log.error("上传文件异常", e);
            apiResponseEntity.setErrorCode(1002);
            apiResponseEntity.setErrorMsg("上传文件异常");
            return apiResponseEntity;
        }
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("成功");
        return apiResponseEntity;
    }


    /**
     * 获取省市区级列表接口
     */
    @ApiOperation(value = "获取省市区级列表接口")

    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = ApiResponseEntity.class),
    })
    @RequestMapping(value = "/api/v1/app/getCity", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity getCity() {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        JSONObject res = new JSONObject();
        List<City> provienceType = cityService.selectList(new EntityWrapper<City>().eq("type", "1"));
        List<City> cityType = cityService.selectList(new EntityWrapper<City>().eq("type", "2"));
        List<City> areaType = cityService.selectList(new EntityWrapper<City>().eq("type", "3"));
        res.put("provienceType", provienceType);
        res.put("cityType", cityType);
        res.put("areaType", areaType);
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("成功");
        apiResponseEntity.setData(res);
        return apiResponseEntity;


    }


    /**
     * 理赔员4s店员获取理赔订单计数
     */
    @ApiOperation(value = "理赔员4s店员获取理赔订单计数，")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "session标示token ", required = true, dataType = "String"),
            @ApiImplicitParam(name = "INIT(-1, \"未接车\"), RECEPTED(0, \"已接车\"), SERVICING(1, \"服务中\"), PLACED(2, \"已交车\"), SETTLED(3, \"已结算\"), TRANSED(4, \"打款完成\"), CANCEL(5, \"作废\"), UNREACH(6, \"未到店\"),CERTRECEIVE(7,\"确认接车\"),CERTMONEY(8,\"已定损\");\n", value = "不用传作为返回值注释 ", dataType = "String"),
            @ApiImplicitParam(name = "flag", value = "标示，如果为理赔员传1，4s店传2 ", required = true, dataType = "String")

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = ApiResponseEntity.class),
            @ApiResponse(code = 1001, message = "登录失效", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/api/v1/app/countOrderNum", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity countOrderNum(@RequestParam String thirdSessionKey, @RequestParam String flag) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {
            log.error("登录key" + thirdSessionKey + "登录失效");
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录失效,请诱导重新登录");
            return apiResponseEntity;
        }


        if (StringUtils.isNotEmpty(appSession.getUser().getAccount())) {
            Map res = new HashMap<>();
            OpenClaim openClaim = new OpenClaim();

            User user = appSession.getUser();
            if (flag.equals("1")) {
                openClaim.setclaimer(user.getAccount());
//           openClaim.setclaimer("13558753335");
            } else {
                openClaim.setDeptid(user.getDeptid());
            }


            for (OpenClaimOrderStatus en : OpenClaimOrderStatus.values()) {
//                if (en.getCode() == OpenClaimOrderStatus.INIT.getCode()) {
//                    continue;
//                }
                Integer status = en.getCode();
                openClaim.setStatus(status);
//				Integer isComplete = (status == -1 ? 1 : null);
                Long counts = openClaimService.countByCondition(openClaim, null, null, null, null);
                if (counts == null) {
                    counts = 0l;
                }
                res.put(en.name(), counts.intValue());
            }

            int RECEPTED = (int) res.get("RECEPTED");
            int SERVICING = (int) res.get("SERVICING");

            res.put("RECEPTED2", RECEPTED + SERVICING);
            res.put("SERVICING2", RECEPTED + SERVICING);
//            openClaim.setStatus(OpenClaimOrderStatus.INIT.getCode());
//            Long counts1 = openClaimService.countByCondition(openClaim, null, null, null, 0);
//            res.put("TXDD", BigDecimal.valueOf(counts1));
//            Long counts2 = openClaimService.countByCondition(openClaim, null, null, null, 1);
//            res.put("UNACCEPT", BigDecimal.valueOf(counts2));
//            openClaim.setStatus(null);
//            BigDecimal fixlossCount = openClaimService.queryFixLossSumByCondition(openClaim, null, null, null, null);
//            res.put("fixlossCount", fixlossCount);
            apiResponseEntity.setErrorCode(0);
            apiResponseEntity.setData(res);
            apiResponseEntity.setErrorMsg("成功");
            return apiResponseEntity;
        } else {
            log.error("登录key" + thirdSessionKey + "登录失效");
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录失效,请诱导重新登录");
            return apiResponseEntity;
        }
    }

    /**
     * 获取理赔佣金总额及当前信息费
     */
    @ApiOperation(value = "获取理赔佣金总额及当前信息费")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "session标示token ", required = true, dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = ApiResponseEntity.class),
            @ApiResponse(code = 1001, message = "登录失效", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/api/v1/app/getInfoMoney", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity getInfoMoney(@RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {
            log.error("登录key" + thirdSessionKey + "登录失效");
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录失效,请诱导重新登录");
            return apiResponseEntity;
        }
        User user = appSession.getUser();
        //获取佣金总额
        JSONObject res = new JSONObject();
        BigDecimal openClaimOrdersIncomeByAccount = openClaimService.getOpenClaimOrdersIncomeByAccount(user.getAccount());
        openClaimOrdersIncomeByAccount = openClaimOrdersIncomeByAccount == null ? BigDecimal.ZERO : openClaimOrdersIncomeByAccount;
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        BizYckBalance bizYckBalance = bizYckBalanceService.selectOne(new EntityWrapper<BizYckBalance>().eq("account", appSession.getUser().getAccount()));
        res.put("income", nf.format(openClaimOrdersIncomeByAccount.setScale(0, BigDecimal.ROUND_DOWN)));
        res.put("balance", (bizYckBalance == null || bizYckBalance.getBalance() == null) ? "0" : bizYckBalance.getBalance().stripTrailingZeros().toPlainString());
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setData(res);
        apiResponseEntity.setErrorMsg("成功");
        return apiResponseEntity;
    }

    /**
     * 开放平台理赔用户预存款账单
     */
    @ApiOperation(value = "开放平台理赔用户预存款账单", notes = "开放平台理赔用户预存款账单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String"),
            @ApiImplicitParam(name = "limit", value = "每页多少条数据", required = true, dataType = "String"),
            @ApiImplicitParam(name = "offset", value = "页数-1", required = true, dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = ApiResponseEntity.class),
            @ApiResponse(code = 1005, message = "参数错误", response = ApiResponseEntity.class),
            @ApiResponse(code = 1001, message = "登录失效", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/api/v1/app/openclaimer/yckmx/get", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity getOpenClaimYckDetailForApp(@RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {
            log.error("登录key" + thirdSessionKey + "登录失效");
            apiResponseEntity.setErrorCode(1002);
            apiResponseEntity.setErrorMsg("登录失效,请诱导重新登录");
            return apiResponseEntity;
        }
        if (StringUtils.isNotEmpty(appSession.getUser().getAccount())) {
            String account = appSession.getUser().getAccount();
            HttpServletRequest request = HttpKit.getRequest();
            String limit = request.getParameter("limit");//每页多少条数据
            String offset = request.getParameter("offset");
            if (limit == null || limit.equals("") || offset.equals("") || offset == null) {
                apiResponseEntity.setErrorCode(1005);
                apiResponseEntity.setErrorMsg("参数错误");
                return apiResponseEntity;
            }

            Page<BizYckCzmx> page = new PageFactory<BizYckCzmx>().defaultPage();
            List<Map<String, Object>> res = bizYckCzmxService.getOrderWaste(page, account);
            for (int i = 0; i < res.size(); i++) {
                Map<String, Object> map = res.get(i);
                if (map.get("address") == null) {
                    map.put("address", "时间：" + map.get("create_time"));
                } else {
                    map.put("address", map.get("address") + " " + map.get("create_time"));
                }
            }
            log.error("res={}", res);
            page.setRecords((List<BizYckCzmx>) new BizYckCzmxWarpper(res).warp());
            apiResponseEntity.setErrorCode(0);
            apiResponseEntity.setData(page);
        }
        return apiResponseEntity;
    }

    /**
     * 获取个人中心数量统计
     */
    @ApiOperation(value = "获取个人中心数量统计", notes = "获取个人中心数量统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = ApiResponseEntity.class),
            @ApiResponse(code = 1001, message = "登录失效", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/api/v1/app/myCount/get", method = RequestMethod.POST, produces = "application/json")

    public ApiResponseEntity getmyCount(@RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {

            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录失效,请诱导重新登录");
            return apiResponseEntity;
        }
        JSONObject res = new JSONObject();
        if (StringUtils.isNotEmpty(appSession.getUser().getAccount())) {
            //查询所有推送记录
            int pusCount = pushRecordService.selectCount(new EntityWrapper<PushRecord>().eq("account", appSession.getUser().getAccount()));
            //查询所有理赔单记录
            int claCount = openClaimService.selectCount(new EntityWrapper<OpenClaim>().eq("claimer", appSession.getUser().getAccount()));
            //查询有效理赔记录
            int claValidCount = openClaimService.selectCount(new EntityWrapper<OpenClaim>().eq("claimer", appSession.getUser().getAccount()).eq("status", "4"));
            //车总数carCount
            int selectsum = 0;
            try {
                selectsum = accdService.selectsum(appSession.getUser().getAccount());
            } catch (Exception e) {

            }
            res.put("pusCount", pusCount);
            res.put("claCount", claCount);
            if (claCount == 0) {
                claCount = 1;
            }
            double claValidCountDouble = claValidCount;
            double claCountDouble = claCount;
            DecimalFormat df = new DecimalFormat("0.0%");
            double i = claValidCountDouble / claCountDouble;
            String format = df.format(i);
            res.put("validPercent", format);
            res.put("selectsum", selectsum);
            String account = appSession.getUser().getAccount();
            User user = userService.getByAccount(account);
            res.put("stars", user.getStars());
            res.put("headImg", user.getHeadImg());
            res.put("workStatus", appService.getStatus(account));
            res.put("name", user.getName());
            res.put("phone", user.getPhone());
            String role = "";
            String roleid = user.getRoleid();
            if ("6".equals(roleid)) {
                role = "4S店员";
            } else if ("7".equals(roleid) || "8".equals(roleid)) {

                role = "理赔顾问";
            }


            res.put("role", role);
            apiResponseEntity.setErrorCode(0);
            apiResponseEntity.setErrorMsg("成功");
            apiResponseEntity.setData(res);
        }
        return apiResponseEntity;
    }


    /**
     * 意见反馈
     */
    @ApiOperation(value = "新增意见反馈", notes = "新增意见反馈")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String"),
            @ApiImplicitParam(name = "content", value = "反馈内容", required = true, dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = ApiResponseEntity.class),
            @ApiResponse(code = 1001, message = "登录失效", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/api/v1/app/addFeedBack", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity addFeedBack(@RequestParam String thirdSessionKey, @RequestParam String content) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {
            log.error("登录key" + thirdSessionKey + "登录失效");
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录失效,请诱导重新登录");
            return apiResponseEntity;
        }
        BizOpenFeedback bizOpenFeedback = new BizOpenFeedback();
        bizOpenFeedback.setAccount(appSession.getUser().getAccount());
        bizOpenFeedback.setContent(content);
        bizOpenFeedback.setCreTime(new Date());
        bizOpenFeedbackService.insert(bizOpenFeedback);
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("成功");
        return apiResponseEntity;
    }

    /**
     * 查询通知列表
     */
    @ApiOperation(value = "查询通知列表", notes = "查询通知列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = ApiResponseEntity.class),
            @ApiResponse(code = 1001, message = "登录失效", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/api/v1/app/getNotify", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity getNotify(@RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (StringUtils.isEmpty(thirdSessionKey)) {
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("请求参数错误");
            return apiResponseEntity;
        }
        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {
            log.error("登录key" + thirdSessionKey + "登录失效");
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录失效,请诱导重新登录");
            return apiResponseEntity;
        }

        List<BizOpenNotify> bizOpenNotifies = bizOpenNotifyService.selectList(new EntityWrapper<BizOpenNotify>().eq("type", "0").or().eq("type", appSession.getUser().getAccount()).orderBy("cre_time", false));

        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("成功");
        apiResponseEntity.setData(bizOpenNotifies);
        return apiResponseEntity;
    }

    /**
     * 文字识别获取ak凭证
     *
     * @param
     * @return
     */
    @ApiOperation(value = "文字识别获取ak凭证")
    @RequestMapping(value = "/api/v1/app/getAk", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity getAk() {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        JSONObject ak = appService.getAk();
        if (ak != null) {
            apiResponseEntity.setErrorCode(0);
            apiResponseEntity.setErrorMsg("成功");
            apiResponseEntity.setData(ak);
            return apiResponseEntity;
        }
        apiResponseEntity.setErrorCode(1002);
        apiResponseEntity.setErrorMsg("失败");
        return apiResponseEntity;
    }

    /**
     * 退出登录
     */
    @ApiOperation(value = "退出登录", notes = "退出登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = ApiResponseEntity.class),
            @ApiResponse(code = 1001, message = "登录失效", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/api/v1/app/logOff", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity logOff(@RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();

        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {
            log.error("登录key" + thirdSessionKey + "登录失效");
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录失效,请诱导重新登录");
            return apiResponseEntity;
        }

        appService.remove(thirdSessionKey);
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("成功");
        return apiResponseEntity;
    }


    /**
     * 查询银行卡信息
     */
    @ApiOperation(value = "查询银行卡信息", notes = "查询银行卡信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "icars登录sessionkey", required = true, dataType = "String")

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = ApiResponseEntity.class),
            @ApiResponse(code = 1001, message = "登录失效", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/api/v1/app/getBankCard", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity getBankCard(@RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();

        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {
            log.error("登录key" + thirdSessionKey + "登录失效");
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录失效,请诱导重新登录");
            return apiResponseEntity;
        }

        User byAccount = userService.getByAccount(appSession.getUser().getAccount());
        String bankcard = byAccount.getBankcard();
        String bankName = byAccount.getBankName();
        String bankUserName = byAccount.getBankUserName();
        if (bankcard != null && !"".equals(bankcard) && !"null".equals(bankcard) && bankcard.length() > 15) {
            bankcard = bankcard.substring(0, bankcard.length() - 11) + "*******" + bankcard.substring(bankcard.length() - 2);

        }
        if (bankUserName != null && !"".equals(bankUserName) && !"null".equals(bankUserName) && bankUserName.length() > 2) {
            bankUserName = bankUserName.substring(0) + "*" + bankUserName.substring(2);
        }

        JSONObject outJson = new JSONObject();
        outJson.put("bankName", bankName);
        outJson.put("bankcard", bankcard);
        outJson.put("bankUserName", bankUserName);
        apiResponseEntity.setData(outJson);
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("成功");
        return apiResponseEntity;
    }


    /**
     * 查询版本号
     */
    @ApiOperation(value = "查询版本号", notes = "查询版本号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "versionCode", value = "versionCode", required = true, dataType = "String")

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = ApiResponseEntity.class)

    })
    @RequestMapping(value = "/api/v1/app/getVersion", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity getVersion(@RequestParam String versionCode) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        JSONObject version = appService.getVersion(versionCode);
        apiResponseEntity.setData(version);
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("成功");
        return apiResponseEntity;
    }


    @ApiOperation(value = "设置版本号", notes = "设置版本号")
    @RequestMapping(value = "/api/v1/app/setVersionInfo", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity setVersionInfo(String par) {
        String str = "{\n\t\"FORCE\": true,\n\t\"VERSION\": \"1.0.5\",\n\t\"DESC\": \"本次更新修复部分bug和ui优化\",\n\t\"URL\": \"https://chejiqiche.com//api/v1/app/getAndroidPackage\",\n\t  \"VERSIONCODE\":\"1\"\n}";

        if (StringUtils.isNotEmpty(par)) {
            str = par;
        }

        jedisUtil.set(RedisKey.CJ_LI_APP, JSONObject.parseObject(str).toJSONString());

        return null;
    }


    @ApiOperation(value = "安卓更新包下载", notes = "安卓更新包下载")
    @RequestMapping(value = "/api/v1/app/getAndroidPackage", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<InputStreamResource> getAndroidPackage(HttpServletResponse response) throws IOException {
        String filePath = "/opt/deploy/app-release.apk";
        FileSystemResource file = new FileSystemResource(filePath);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getFilename()));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        InputStream inputStream = file.getInputStream();
        headers.add("Content-Length", String.valueOf(inputStream.available()));
        return ((ResponseEntity.BodyBuilder) ResponseEntity.ok().headers(headers)).contentLength(file.contentLength()).contentType(MediaType.parseMediaType("application/octet-stream")).body(new InputStreamResource(inputStream));
    }

    /**
     * 获取环信好友列表
     *
     * @param
     * @return
     */
    @ApiOperation(value = "获取环信好友列表")
    @RequestMapping(value = "/api/v1/app/getHxFriendList", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity getHxFriendList(String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        log.error("#####start###########{}", thirdSessionKey);
        if (StringUtils.isNotEmpty(thirdSessionKey)) {
            AppSession appSession = appService.getAppSession(thirdSessionKey);
            if (appSession == null) {
                log.error("登录key" + thirdSessionKey + "登录失效");
                apiResponseEntity.setErrorCode(1001);
                apiResponseEntity.setErrorMsg("登录失效,请诱导重新登录");
                return apiResponseEntity;
            }
            User user = appSession.getUser();
                //理赔员
            String hxFriendList = appService.getHxFriendList(user.getRoleid());
            return new ApiResponseEntity(hxFriendList);
        }else{
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录失效,请重新登录");
            return apiResponseEntity;
        }
    }


    /**
     * 根据账户获取部门
     *
     * @param
     * @return
     */
    @ApiOperation(value = "根据账户获取部门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "account", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/app/getDeptByAccount", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity getDeptByAccount(String account) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        User byAccount = userService.getByAccount(account);
        JSONObject jons = new JSONObject();
        jons.put("deptid", byAccount.getDeptid());
        jons.put("roleid", byAccount.getRoleid());
        apiResponseEntity.setData(jons);
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("成功");
        return apiResponseEntity;
    }

//    @Resource
//    private CallNotifyMain callNotifyMain;
//
//    /**
//     * 测试华为云电话
//     *
//     * @param name
//     * @param phone
//     * @return
//     */
//    @RequestMapping(value = "/api/v1/app/testVoice", method = RequestMethod.POST, produces = "application/json")
//    public ApiResponseEntity testVoice(String name, String phone) {
//        try {
//            callNotifyMain.sendVoice(name, phone);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return new ApiResponseEntity();
//    }


    /**
     * 查询事故清单列表
     *
     * @return
     */
    @ApiOperation(value = "app首页热力图数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "session标示token ", required = true, dataType = "String", paramType = "query"),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = ApiResponseEntity.class),
            @ApiResponse(code = 1001, message = "登录失效", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/api/v1/app/homeAccident", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity homeAccident(@RequestParam String thirdSessionKey) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        AppSession appSession = appService.getAppSession(thirdSessionKey);
        if (appSession == null) {
            log.error("登录key" + thirdSessionKey + "登录失效");
            apiResponseEntity.setErrorCode(1001);
            apiResponseEntity.setErrorMsg("登录失效,请诱导重新登录");
            return apiResponseEntity;
        }
        Map<String, Object> pushRecords = pushRecordService.getAppHomeAccident(appSession.getUser().getAccount());

        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("成功");
        apiResponseEntity.setData(pushRecords);
        return apiResponseEntity;
    }



    @RequestMapping(value = "/api/v1/app/checkFail", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity checkFail(@RequestParam Integer accdId, @RequestParam String reason) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (ToolUtil.isEmpty(accdId)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        int effectRows = this.accdService.setStatus(accdId, AccdStatus.CHECK_FAIL.getCode(), reason);
        if (effectRows != 0) {
            apiResponseEntity.setErrorCode(0);
            apiResponseEntity.setErrorMsg("成功");
            return apiResponseEntity;
        }
        apiResponseEntity.setErrorCode(500);
        apiResponseEntity.setErrorMsg("失败");
        return apiResponseEntity;
    }

    @RequestMapping(value = "/api/v1/app/checkSuccess", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity checkSuccess(@RequestParam Integer accdId, @RequestParam BigDecimal amount, @RequestParam(required = false) String reason) throws Exception {
        if (ToolUtil.isEmpty(accdId) || amount == null) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        //修改事故状态
        this.accdService.setStatus(accdId, AccdStatus.CHECK_SUCCESS.getCode(), reason);
        Accident accident = accdService.selectById(accdId);
        BizWxUser bizWxUser = bizWxUserService.selectBizWxUser(accident.getOpenid());
        BizWxpayBill bizWxpayBill = bizWxpayBillService.selectOneByAccid(accdId);
        if (bizWxpayBill != null) {
            apiResponseEntity.setErrorCode(500);
            apiResponseEntity.setErrorMsg("操作失败，该事故已发送过红包，请勿重复操作");
            return apiResponseEntity;
        }

        if (bizWxUser != null && accident != null) {
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
            //   如果要启动这段代码 需要考虑bizWxUser2为null的情况
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
            //为推广父用户发放提成红包
//            if (StringUtils.isNotEmpty(bizWxUser.getExtensionAccount())) {
//                BizWxUser bizWxUser2 = bizWxUserService.selectBizWxUser(bizWxUser.getExtensionAccount());
//                if (bizWxUser2 != null) {
//                    double percent = 0.2;
//                    Dict dict = iDictService.selectOne(new EntityWrapper<Dict>().eq("name", "红包提成比例"));
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
            String sign = md5(uid + type + accdId + multiply.intValue() + reqtick + bizWxUser.getThirdOpenId() + apikey);
            //拼接地址
            String url = "https://www.yaoyaola.net/exapi/SendRedPackToOpenid?" + "uid=" + uid + "&type=" + type + "&orderid=" + accdId + "&money=" + multiply.intValue() + "&reqtick=" + reqtick + "&openid=" + bizWxUser.getThirdOpenId() + "&sign=" + sign + "&title=感谢参与提报事故" + "&sendname=阿米巴拍事故&wishing=阿米巴拍事故";
            System.out.println(url);
            String result1 = HttpUtils.sendHttpGet(url);
            JSONObject resultJOSN = JSONObject.parseObject(result1);
            System.out.println("摇摇啦：");
            System.out.println(resultJOSN);
            String errcode = resultJOSN.getString("errcode");
            if (errcode.equals("0")) {
                apiResponseEntity.setErrorCode(0);
                apiResponseEntity.setErrorMsg("成功");
                return apiResponseEntity;
            }else {
                apiResponseEntity.setErrorCode(500);
                apiResponseEntity.setErrorMsg("有误");
                return apiResponseEntity;
            }
        }

        apiResponseEntity.setErrorMsg("审核通过，支付失败");
        return apiResponseEntity;
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
}