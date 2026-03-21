package com.stylefeng.guns.modular.system.controller.xcx;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.config.properties.SysProperties;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.core.common.exception.BizExceptionEnum;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.util.JedisUtil;
import com.stylefeng.guns.modular.system.constant.ApiResponseEntity;
import com.stylefeng.guns.modular.system.constant.RedisKey;
import com.stylefeng.guns.modular.system.constant.SysActive;
import com.stylefeng.guns.modular.system.constant.WxSession;
import com.stylefeng.guns.modular.system.model.BizClaim;
import com.stylefeng.guns.modular.system.model.Notice;
import com.stylefeng.guns.modular.system.model.XcxMaintenance;
import com.stylefeng.guns.modular.system.service.IAccdService;
import com.stylefeng.guns.modular.system.service.IBizClaimService;
import com.stylefeng.guns.modular.system.service.IBizWxPayRecordService;
import com.stylefeng.guns.modular.system.service.INoticeService;
import com.stylefeng.guns.modular.system.service.impl.WxService;
import com.stylefeng.guns.modular.system.service.impl.XcxMaintenanceServiceImpl;
import com.stylefeng.guns.netty.service.IWebSocketMessagePushService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class XcxController {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Resource
    private INoticeService iNoticeService;

    @Resource
    private XcxMaintenanceServiceImpl xcxMaintenanceService;

    @Resource
    private IBizWxPayRecordService bizWxPayRecordService;

    @Resource
    private JedisUtil jedisUtil;

    @Resource(name = "styleFengWxService")
    private WxService wxService;

    @Resource
    private IBizClaimService bizClaimService;

    @Resource
    private IWebSocketMessagePushService webSocketMessagePushService;

    @Resource
    private SysProperties sysProperties;

    @ApiOperation(value = "小程序首页新闻列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "newsType", value = "新闻类型 固定传12", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "页码 1=第一页", required = true, dataType = "int", paramType = "query"),
    })
    @RequestMapping(value = "/api/xcx/home/newsList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseEntity<T> newsList(@RequestParam Integer newsType, @RequestParam Integer page) {
        if (newsType == null || page == null) {
            return new ApiResponseEntity(500, "参数不能为null");
        }
        Notice notice = new Notice();
        notice.setNewsType(newsType);
        Page pa = new Page();
        pa.setSize(4);
        pa.setCurrent(page);
        EntityWrapper<Notice> qryWrapper = new EntityWrapper<>();
        qryWrapper.setEntity(notice);

        Page page1 = iNoticeService.selectPage(pa, qryWrapper);
        return new ApiResponseEntity(page1);
    }

    @ApiOperation(value = "获取首页视频播放地址")
    @RequestMapping(value = "/api/xcx/home/video", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseEntity<T> newsVideo() {
        String url = jedisUtil.get(RedisKey.XCX_HOME_VIDEO_URL);
        return new ApiResponseEntity(url);
    }

    @ApiOperation(value = "获取首页视频播放地址")
    @RequestMapping(value = "/api/xcx/home/setVideo", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseEntity<T> setVideo(String title, String url) {
        JSONObject obj = new JSONObject();
        obj.put("title", "成都首例！将爱车借给饮酒的朋友驾驶，车主被追究刑事责任");
        obj.put("url", "https://msmp.scsjb.cn/gjms_pic/20190320/1553085141048.mp4");
        if (StringUtils.isNotEmpty(title)) {
            obj.put("title", title);
        }
        if (StringUtils.isNotEmpty(url)) {
            obj.put("url", url);
        }
        obj.put("maintain_flag", false);
        jedisUtil.set(RedisKey.XCX_HOME_VIDEO_URL, obj.toJSONString());
        return new ApiResponseEntity(200, "ok");
    }

    @RequestMapping(value = "/api/xcx/maintainList", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "小程序维修list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "1.保养  2.油漆 3.钣金", required = true, dataType = "int", paramType = "query"),
    })
    public ApiResponseEntity<T> maintainList(@RequestParam Integer type) {
        if (type == null || type == 0) {
            return new ApiResponseEntity(500, "参数错误");
        }
        EntityWrapper<XcxMaintenance> qryWrapper = new EntityWrapper<>();
        XcxMaintenance xcxMaintenance = new XcxMaintenance();
        xcxMaintenance.setType(String.valueOf(type));
        qryWrapper.setEntity(xcxMaintenance);
        List<XcxMaintenance> xcxMaintenances = xcxMaintenanceService.selectList(qryWrapper);
        return new ApiResponseEntity(xcxMaintenances);
    }

    @RequestMapping(value = "/api/xcx/maintainInfo", method = RequestMethod.GET)
    @ApiOperation(value = "小程序维修详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "16 17 18 19 20 21", required = true, dataType = "int", paramType = "query"),
    })
    @ResponseBody
    public ApiResponseEntity<T> maintainInfo(@RequestParam Integer id) {
        if (id == null || id == 0) {
            return new ApiResponseEntity(500, "参数错误");
        }
        XcxMaintenance xcxMaintenance1 = xcxMaintenanceService.selectById(id);
        return new ApiResponseEntity(xcxMaintenance1);
    }

    @ApiOperation(value = "新增理赔订单", notes = "新增理赔订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "客户姓名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "phone", value = "客户手机", required = true, dataType = "String"),
            @ApiImplicitParam(name = "lng", value = "经度", required = true, dataType = "Bigdecimal"),
            @ApiImplicitParam(name = "lat", value = "纬度", required = true, dataType = "Bigdecimal"),
            @ApiImplicitParam(name = "address", value = "上报地址名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "maintenance_id", value = "list id", required = false, dataType = "int")
    })
    @RequestMapping(value = "/api/xcx/wx/xcx/claim/add", method = RequestMethod.POST, produces = "application/json")
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
            e.printStackTrace();
            apiResponseEntity.setErrorCode(1002);
            apiResponseEntity.setErrorMsg("网络异常");
            return apiResponseEntity;
        }
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("");
        return apiResponseEntity;
    }


    @ApiOperation(value = "查询个人维修订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "订单类型（ 8.保养  9.油漆 10.钣金 11、维修   0表示查全部）", required = true, dataType = "String"),
            @ApiImplicitParam(name = "limit", value = "10", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "offset", value = "0", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/api/xcx/wx/xcx/myClaim", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity myClaim(@RequestParam("thirdSessionKey") String thirdSessionKey, @RequestParam("type") Integer type,
                                     @RequestParam("limit") Integer limit, @RequestParam("offset") Integer offset) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (type == null || limit == null || offset == null || thirdSessionKey == null) {
            return new ApiResponseEntity(405, "参数错误");
        }

        Page<BizClaim> page = new PageFactory<BizClaim>().defaultPage();

        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("微信session无效或为空");
            return apiResponseEntity;
        }
        List<Map<String, Object>> maps = bizClaimService.selectListForPageByOpendId(page, wxSession.getOpenId(), type);

        apiResponseEntity.setData(maps);
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setErrorMsg("");
        return apiResponseEntity;
    }


    @ApiOperation(value = "我的提成")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdSessionKey", value = "token", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/xcx/personal", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResponseEntity addClaim(@RequestParam String thirdSessionKey) {
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
            Map<String, Object> res = new HashMap<>(2);
            BigDecimal myDeduct = bizWxPayRecordService.getRedPackPercentageCounts(wxSession.getOpenId(), null, null);
            res.put("myDeduct", myDeduct);
            apiResponseEntity.setErrorCode(0);
            apiResponseEntity.setData(res);
        }
        return apiResponseEntity;
    }


    @ApiOperation(value = "getSession")
    @RequestMapping(value = "/api/xcx/getSession", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseEntity getKey(String opendId, String wxSessionKey,String unionId) {
        if (sysProperties.getActive().equals(SysActive.PRO)) {
            return null;
        }
        if (StringUtils.isEmpty(opendId)) {
            LOGGER.error("opendId###/api/xcx/getSession ### opendId={};wxSessionKey={}", opendId, wxSessionKey);
            opendId = "om2cE5lzjXOGVCGXpfvsRF_e3NW8";
        }
        if (StringUtils.isEmpty(wxSessionKey)) {
            LOGGER.error("wxSessionKey###/api/xcx/getSession ### opendId={};wxSessionKey={}", opendId, wxSessionKey);
            wxSessionKey = "123456";
        }
        long createSeconds = System.currentTimeMillis() / 1000;

        Long expires = 60 * 60L;
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();

        String rdSession = wxService.create3rdSession(opendId, wxSessionKey, expires, createSeconds, unionId);
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setData(rdSession);
        return apiResponseEntity;

    }

    @Resource
    private IAccdService iAccdService;

    @ApiOperation(value = "分享的时候 获取上传视频缩略图")
    @RequestMapping(value = "/api/xcx/getThumbnail", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "wxSessionKey", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(name = "accId", value = "10000", required = true, dataType = "Integer")
    })
    @ResponseBody
    public ApiResponseEntity getThumbnail(String wxSessionKey, Integer accId) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        if (accId == null) {
            LOGGER.error("小程序 获取上传视频缩略图 accId={};wxSessionKey={}", accId, wxSessionKey);
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setData("accId不能为空");
            return apiResponseEntity;
        }
        if (StringUtils.isEmpty(wxSessionKey)) {
            LOGGER.error("wxSessionKey###/api/xcx/getThumbnail ### accId={};wxSessionKey={}", accId, wxSessionKey);
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setData("wxSessionKey不能为空");
            return apiResponseEntity;
        }
        WxSession wxSession = wxService.getWxSession(wxSessionKey);
        if (wxSession == null) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登陆失效，请退出重新登陆");
            return apiResponseEntity;
        }
        if (StringUtils.isEmpty(wxSession.getOpenId())) {
            apiResponseEntity.setErrorCode(5001);
            apiResponseEntity.setErrorMsg("登录信息openId不能为空！");
            return apiResponseEntity;
        }

        String url = iAccdService.findUrlByAccIdAndOpenId(wxSession.getOpenId(),accId);

        if (StringUtils.isEmpty(url)) {
            apiResponseEntity.setErrorCode(5002);
            apiResponseEntity.setErrorMsg("未找到目标url，请重试！");
            return apiResponseEntity;
        }
        apiResponseEntity.setErrorCode(0);
        apiResponseEntity.setData(url);
        return apiResponseEntity;

    }

}
