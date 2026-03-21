/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.web;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.app.entity.*;
import com.jeesite.modules.app.service.*;
import com.jeesite.modules.constant2.AppConstants;
import com.jeesite.modules.constant2.JgTokenEnum;
import com.jeesite.modules.util2.HuaweiSmsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 红包金额记录表Controller
 *
 * @author zcq
 * @version 2019-08-30
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appPayAmountRecord")
public class AppPayAmountRecordController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(AppPayAmountRecordController.class);

    @Autowired
    private AppPayAmountRecordService appPayAmountRecordService;

    @Autowired
    private AppAccidentRecordService appAccidentRecordService;

    @Autowired
    private AppUserService appUserService;

    @Resource
    private PayWxAmountService payWxAmountService;

    @Resource
    private BizAccidentService bizAccidentService;

    @Resource
    private AppVideoService appVideoService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private AppJgPushService appJgPushService;

    @Resource
    private AppUserBehaviorService appUserBehaviorService;

    @Resource
    private HuaweiSmsService huaweiSmsService;

    @Resource
    private AppPushBillService appPushBillService;

    /**
     * 获取数据
     */
    @ModelAttribute
    public AppPayAmountRecord get(Long id, boolean isNewRecord) {
        return appPayAmountRecordService.get(String.valueOf(id), isNewRecord);
    }

    /**
     * 查询列表
     */
    @RequiresPermissions("app:appPayAmountRecord:view")
    @RequestMapping(value = {"list", ""})
    public String list(AppPayAmountRecord appPayAmountRecord, Model model) {
        model.addAttribute("appPayAmountRecord", appPayAmountRecord);
        return "modules/app/appPayAmountRecordList";
    }

    /**
     * 查询列表数据
     */
    @RequiresPermissions("app:appPayAmountRecord:view")
    @RequestMapping(value = "listData")
    @ResponseBody
    public Page<AppPayAmountRecord> listData(AppPayAmountRecord appPayAmountRecord, HttpServletRequest request, HttpServletResponse response) {
        appPayAmountRecord.setPage(new Page<>(request, response));
        Page<AppPayAmountRecord> page = appPayAmountRecordService.findPage(appPayAmountRecord);
        return page;
    }

    /**
     * 查看编辑表单
     */
    @RequiresPermissions("app:appPayAmountRecord:view")
    @RequestMapping(value = "form")
    public String form(AppPayAmountRecord appPayAmountRecord, Model model, Integer accid, Integer userId) {
        model.addAttribute("appPayAmountRecord", appPayAmountRecord);

        return "modules/app/appPayAmountRecordForm";
    }

    /**
     * 保存红包金额记录表
     */
    @RequiresPermissions("app:appPayAmountRecord:edit")
    @PostMapping(value = "save")
    @ResponseBody
    public String save(@Validated AppPayAmountRecord appPayAmountRecord) {
        appPayAmountRecordService.save(appPayAmountRecord);
        return renderResult(Global.TRUE, text("保存红包金额记录表成功！"));
    }


    @RequestMapping("payReward")
    @ResponseBody
    public Object payReward(String id, BigDecimal payAmount, String type) {
        JSONObject result = new JSONObject();

        //查询到对应pushbill
        int i = Integer.parseInt(type);
        List<AppPushBill> bySourceAccid = appPushBillService.findBySourceAccid(id, i);
        for (AppPushBill appPushBill : bySourceAccid) {
            Long userId = appPushBill.getUserId();
            //
            appPushBill.setReward(payAmount);
            appPushBillService.update(appPushBill);
            //加钱
            AppPayAmountRecord appPayAmountRecord = new AppPayAmountRecord();
            appPayAmountRecord.setAccid(Long.valueOf(id));
            appPayAmountRecord.setUserId(userId);
            appPayAmountRecord.setPayAmount(payAmount.doubleValue());
            appPayAmountRecord.setCreateTime(new Date());
            appPayAmountRecord.setType("1");
            appPayAmountRecordService.addPayAmount(appPayAmountRecord, id,21);
        }
        result.put("stat", "success");
        result.put("mes", "成功！");
        return result;
    }

    //发送红包
    @RequestMapping("addPayAount")
    @ResponseBody
    public Object addPayAount(String id, BigDecimal payAmount, String type) {
        JSONObject result = new JSONObject();

        if (type.equals("3")) {
            try {
                BizAccident bizAccident = bizAccidentService.get(id);
                String openid = bizAccident.getOpenid();
                AppUserBehavior appUserBehavior = appUserBehaviorService.selectByUserId(openid);

                if (appUserBehavior == null) {
                    AppUserBehavior addUserBehavior = new AppUserBehavior();
                    addUserBehavior.setUserId(openid);
                    addUserBehavior.setUserSource("2");
                    addUserBehavior.setCreateTime(new Date());
                    addUserBehavior.setUpdateTime(new Date());
                    appUserBehaviorService.insert(addUserBehavior);
                    appUserBehaviorService.addScount(addUserBehavior, "5");
                } else {
                    //存在就在对应数据上加一
                    appUserBehaviorService.addScount(appUserBehavior, "5");
                }
                //给小程序用户发微信红包
				/*accdId  事故id
				amount  金额
				reason  固定‘审核通过’*/
                String s = payWxAmountService.passSta(bizAccident.getId(), payAmount);
                //查询是否有推广用户，有就新增一条事故数据 然后以20%的比例发钱
            //    String faOpenId = bizAccidentService.findIsFaId(openid);
//                if (StringUtils.isNotEmpty(faOpenId)){
//                    // 有就新增一条事故数据
//                    BizAccident newBizacc = new BizAccident();
//                    newBizacc.setOpenid(faOpenId);
//                    newBizacc.setVideo(bizAccident.getVideo());
//                    newBizacc.setLng(bizAccident.getLng());
//                    newBizacc.setLat(bizAccident.getLat());
//                    newBizacc.setCheckid(bizAccident.getCheckid());
//                    newBizacc.setStatus("1");
//                    newBizacc.setAddress(bizAccident.getAddress());
//                    newBizacc.setDelgeo("1");
//                    newBizacc.setCreatetime(new Date());
//                    newBizacc.setChecktime(new Date());
//                    newBizacc.setPayRed(1);
//                    bizAccidentService.insertNewBiz(newBizacc);
//
//                    //然后以20%的比例发钱
//                    //查询到这条数据
//                    BizAccident accident = bizAccidentService.findOnePay(faOpenId,bizAccident.getVideo());
//                    if (accident != null){
//                        BigDecimal multiply = payAmount.multiply(new BigDecimal("0.2"));
//                        String s2 = payWxAmountService.passSta(accident.getId(),multiply);
//                        logger.error("给小程序上级发钱成功  multiply={},accid={}",multiply,accident.getId());
//                        if (!s2.equals("200")) {
//                            logger.error("给小程序上级发钱失败 原因 s2={},accid={}",s2,accident.getId());
//                        }
//                    }
 //               }

                //根据事故id和source找到对应appvideoid
                String source = "2";
                AppVideo appVideo = appVideoService.findVideoByAccidAndType(id, source);
                if (appVideo != null) {
                    String userId = appVideo.getUserId();
                    AppUser appUser = appUserService.get(userId);
                    String name = appUser.getName();
                    Integer isPay = appVideo.getIsPay();
                    if (isPay == 0) {
                        JSONObject in = new JSONObject();
                        in.put("videoId", appVideo.getId());
                        in.put("url", appVideo.getUrl());
                        in.put("userId", appVideo.getUserId());
                        in.put("count", appVideo.getCount());
                        in.put("share", appVideo.getShare());
                        in.put("appViewCounts", appVideo.getAppViewCounts());
                        in.put("accidentId", appVideo.getAccidentId());
                        in.put("creatTime", appVideo.getCreatTime());
                        in.put("name", name);
                        in.put("introduce", appVideo.getIntroduce());
                        in.put("avatar", appVideo.getAvatar());
                        in.put("address", appVideo.getAddress());
                        in.put("isPay", appVideo.getIsPay());
                        redisTemplate.opsForZSet().remove(AppConstants.APP_VIDEO_LIST, in.toJSONString());

                        appVideo.setIsPay(1);
                        Date creatTime = appVideo.getCreatTime();
                        long time = creatTime.getTime();
                        in.put("videoId", appVideo.getId());
                        in.put("url", appVideo.getUrl());
                        in.put("userId", appVideo.getUserId());
                        in.put("count", appVideo.getCount());
                        in.put("share", appVideo.getShare());
                        in.put("appViewCounts", appVideo.getAppViewCounts());
                        in.put("accidentId", appVideo.getAccidentId());
                        in.put("creatTime", appVideo.getCreatTime());
                        in.put("name", name);
                        in.put("introduce", appVideo.getIntroduce());
                        in.put("avatar", appVideo.getAvatar());
                        in.put("address", appVideo.getAddress());
                        in.put("isPay", appVideo.getIsPay());
                        redisTemplate.opsForZSet().add(AppConstants.APP_VIDEO_LIST, in.toJSONString(), time);
                        appVideoService.update(appVideo);
                    }
                }
                if (s.equals("200")) {
                    result.put("stat", "success");
                    result.put("mes", "成功！");
                    return result;
                } else {
                    result.put("stat", "false");
                    result.put("mes", "推送失败！");
                    return result;
                }
            } catch (Exception e) {
                e.printStackTrace();
                result.put("stat", "false");
                result.put("mes", "推送失败！");
                return result;
            }
        }

        //拿到事故信息
        AppAccidentRecord appAccidentRecord = appAccidentRecordService.get(id);

        //根据事故id和用户id查询是否有红包记录
        //有记录就返回
        List<AppPayAmountRecord> appPayAmount = appPayAmountRecordService.findRecord(id, appAccidentRecord.getUserId());
        if (!appPayAmount.isEmpty()) {
            result.put("stat", "false");
            result.put("mes", "失败！");
            return result;
        }
        try {
            AppPayAmountRecord appPayAmountRecord = new AppPayAmountRecord();
            //判断金额是否为空，为空就是不发
            if (null == payAmount) {
                //为空就是修改
                //修改事故状态为成功
                appAccidentRecord.setStatuse(2);
                appAccidentRecord.setCheckTime(new Date());
                //状态(1：未审核  2：审核通过  3：审核失败）
                //保存状态
                appAccidentRecordService.update(appAccidentRecord);
                return renderResult(Global.TRUE, text("审核成功！"));
            }
            //拿到事故id和用户id
            String accid = appAccidentRecord.getId();
            //查询到用户是否有parentid
            String userId = appAccidentRecord.getUserId();
            //有parentId就给parentid发钱
            AppUser appUser = appUserService.get(userId);
            Integer parentId = appUser.getParentId();


//            if (0 != parentId) {
//                //有父id就给父id发
//                AppPayAmountRecord parentPay = new AppPayAmountRecord();
//                parentPay.setUserId(Long.valueOf(parentId));
//                parentPay.setAccid(Long.valueOf(accid));
//                BigDecimal a = payAmount.multiply(new BigDecimal("0.5"));
//                Double b = a.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//                parentPay.setPayAmount(b);
//                parentPay.setCreateTime(new Date());
//                parentPay.setType("2");
//                appPayAmountRecordService.addPayAmount(parentPay, id,3);
//                //发短信
//                AppUser user = appUserService.findUserById(Long.valueOf(parentId));
//            //    huaweiSmsService.sendSmsByTemplate("9", user.getUsername(), String.valueOf(b));
//
//            }
            appPayAmountRecord.setAccid(Long.valueOf(accid));
            appPayAmountRecord.setUserId(Long.valueOf(userId));
            appPayAmountRecord.setPayAmount(payAmount.doubleValue());
            appPayAmountRecord.setCreateTime(new Date());
            appPayAmountRecord.setType("1");
            //发钱
            appPayAmountRecordService.addPayAmount(appPayAmountRecord, id,3);
            //发短信
            //  发短信
          //  huaweiSmsService.sendSmsByTemplate("8", appUser.getUsername(), String.valueOf(payAmount));

            //修改事故状态为成功
            appAccidentRecord.setStatuse(2);
            appAccidentRecord.setCheckTime(new Date());
            //状态(1：未审核  2：审核通过  3：审核失败）
            //保存状态
            appAccidentRecordService.update(appAccidentRecord);

            //判断是否已经发送并且通过了两个数据，有两个数据就给父id新增10块，发送通知
            Integer recordNumber = appAccidentRecordService.findTowRecord(appUser.getId());
            if (recordNumber == null) {
                recordNumber = 0;
            }
            if (2 == recordNumber) {
                //给父id增加10块
                if (0 !=parentId){
                    AppPayAmountRecord parentPay = new AppPayAmountRecord();
                    parentPay.setUserId(Long.valueOf(parentId));
                    parentPay.setAccid(Long.valueOf(accid));
                    parentPay.setPayAmount(new BigDecimal("10").doubleValue());
                    appPayAmountRecordService.addPayAmount(parentPay, id,19);
                    //发送通知
            //        huaweiSmsService.sendSmsByTemplate("10", appUser.getUsername(), String.valueOf(payAmount));
                }
            }


            //修改视频数据
            String ste = "1";
            AppVideo videoByAccidAndType = appVideoService.findVideoByAccidAndType(id, ste);
            if (videoByAccidAndType != null) {
                Integer isPay = videoByAccidAndType.getIsPay();
                String user = videoByAccidAndType.getUserId();
                AppUser appUser1 = appUserService.get(user);
                String name = appUser1.getName();
                if (isPay == 0) {
                    //先删
                    JSONObject in = new JSONObject();
                    in.put("videoId", videoByAccidAndType.getId());
                    in.put("url", videoByAccidAndType.getUrl());
                    in.put("userId", videoByAccidAndType.getUserId());
                    in.put("count", videoByAccidAndType.getCount());
                    in.put("share", videoByAccidAndType.getShare());
                    in.put("appViewCounts", videoByAccidAndType.getAppViewCounts());
                    in.put("accidentId", videoByAccidAndType.getAccidentId());
                    in.put("creatTime", videoByAccidAndType.getCreatTime());
                    in.put("name", name);
                    in.put("introduce", videoByAccidAndType.getIntroduce());
                    in.put("avatar", videoByAccidAndType.getAvatar());
                    in.put("address", videoByAccidAndType.getAddress());
                    in.put("isPay", videoByAccidAndType.getIsPay());
                    redisTemplate.opsForZSet().remove(AppConstants.APP_VIDEO_LIST, in.toJSONString());

                    //改
                    videoByAccidAndType.setIsPay(1);
                    Date creatTime = videoByAccidAndType.getCreatTime();
                    long time = creatTime.getTime();
                    in.put("videoId", videoByAccidAndType.getId());
                    in.put("url", videoByAccidAndType.getUrl());
                    in.put("userId", videoByAccidAndType.getUserId());
                    in.put("count", videoByAccidAndType.getCount());
                    in.put("share", videoByAccidAndType.getShare());
                    in.put("appViewCounts", videoByAccidAndType.getAppViewCounts());
                    in.put("accidentId", videoByAccidAndType.getAccidentId());
                    in.put("creatTime", videoByAccidAndType.getCreatTime());
                    in.put("name", name);
                    in.put("introduce", videoByAccidAndType.getIntroduce());
                    in.put("avatar", videoByAccidAndType.getAvatar());
                    in.put("address", videoByAccidAndType.getAddress());
                    in.put("isPay", videoByAccidAndType.getIsPay());
                    redisTemplate.opsForZSet().add(AppConstants.APP_VIDEO_LIST, in.toJSONString(), time);
                    appVideoService.update(videoByAccidAndType);
                }
            }
            //添加到极光推送
            //推送到C端用户，发红包
			/*	private String source;		// 来源（C端，B端）
	private String ispush;		// 是否推送（0，没有，1，有）
	private String type;		// 操作类型()
	private String userId;		// C端用户id
	private String userBId;		// B端用户id
	private Date createTime;		// 创建时间
	private Date updateTime;		// 修改时间*/
//            AppJgPush appJgPush = new AppJgPush();
//            appJgPush.setSource("C");
//            appJgPush.setIspush("0");
//            appJgPush.setType("3");
//            appJgPush.setUserId(appAccidentRecord.getUserId());
//            appJgPush.setCreateTime(new Date());
//            appJgPush.setUpdateTime(new Date());
//            appJgPushService.insertJpush(appJgPush);
//			String userId1 = appJgPush.getUserId();
//			String userBId = appJgPush.getUserBId();
//			if (StringUtils.isEmpty(userId1)){
//				userId1 = "0";
//			}
//			if (StringUtils.isEmpty(userBId)){
//				userBId = "0";
//			}
//			Producer.productSend(appJgPush.getSource(),appJgPush.getType(),userId1,userBId);
            //审核通过数据添加1
            //app上传
            //找到表里面是否有用户存在
            AppUserBehavior appUserBehavior = appUserBehaviorService.selectByUserId(userId);

            if (appUserBehavior == null) {
                AppUserBehavior addUserBehavior = new AppUserBehavior();
                addUserBehavior.setUserId(userId);
                addUserBehavior.setUserSource(type);
                addUserBehavior.setCreateTime(new Date());
                addUserBehavior.setUpdateTime(new Date());
                appUserBehaviorService.insert(addUserBehavior);
                appUserBehaviorService.addScount(addUserBehavior, "5");
            } else {
                //存在就在对应数据上加一
                appUserBehaviorService.addScount(appUserBehavior, "5");
            }

            //推送红包声音
            String str = "您有新的车己红包到账，请注意查收";
            JPushService.jiguangPush(appUser.getUsername(), str, JgTokenEnum.C, "8");

            result.put("stat", "success");
            result.put("mes", "成功！");
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            result.put("stat", "false");
            result.put("mes", "失败！");
            return result;
        }
    }

}