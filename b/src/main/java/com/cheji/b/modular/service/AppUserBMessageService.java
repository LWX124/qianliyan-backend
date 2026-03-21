package com.cheji.b.modular.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.constant.RedisConstant;
import com.cheji.b.modular.domain.*;
import com.cheji.b.modular.dto.CenterDetailsDto;
import com.cheji.b.modular.mapper.AppUserBMessageMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.jni.User;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 用户服务信息表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2020-04-29
 */
@Service
public class AppUserBMessageService extends ServiceImpl<AppUserBMessageMapper, AppUserBMessageEntity> implements IService<AppUserBMessageEntity> {

    @Resource
    private UserService bUserService;

    @Resource
    private IndentService indentService;

    @Resource
    private LableDetailsReviewTreeService lableDetailsReviewTreeService;

    @Resource
    private AppCleanIndetService appCleanIndetService;

    @Resource
    private AppUserBMessageService appUserBMessageService;

    @Resource
    private AppSprayPaintIndentService appSprayPaintIndentService;

    @Resource
    private AppRescueIndentService appRescueIndentService;

    @Resource
    private AppYearCheckIndentService appYearCheckIndentService;

    @Resource
    private MerchantsInfoBannerService merchantsInfoBannerService;

    @Resource
    private AppBUserConfigService appBUserConfigService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserService userService;


    public JSONObject selectDetils(Integer userBId, Integer type) {
        //1:推修，2：洗美，3：喷漆，4：救援，5：年检
        JSONObject jsonObject = new JSONObject();
        CenterDetailsDto centerDtaisl = new CenterDetailsDto();
        AppUserEntity appUserEntity = bUserService.selectById(userBId);
        EntityWrapper<LableDetailsReviewTreeEntity> lableDetailsWrapper = new EntityWrapper<>();
        lableDetailsWrapper.eq("user_b_id", userBId)
                .eq("state", 1)
                .eq("`show`", 1);

        EntityWrapper<MerchantsInfoBannerEntity> bannerWrapper = new EntityWrapper<>();
        bannerWrapper.eq("user_b_id", userBId)
                .eq("`index`", 1);

        MerchantsInfoBannerEntity merchantsInfoBanner = merchantsInfoBannerService.selectOne(bannerWrapper);

        //店铺名称，
        //今日订单，总收益，总单数，
        //新订单，服务中，待付款，待评价，已取消
        //查询营业状态
        if (type == 1) {
            //查询是否有服务
            List<LableDetailsReviewTreeEntity> lableDetailsReviewTreeEntities = lableDetailsReviewTreeService.selectList(lableDetailsWrapper);
            if (lableDetailsReviewTreeEntities.isEmpty()) {
                jsonObject.put("code", 408);
                jsonObject.put("msg", "暂未开通服务");
                return jsonObject;
            }
            centerDtaisl = indentService.selectIndentCenterMes(userBId);
            jsonObject.put("name", appUserEntity.getMerchantsName());
            jsonObject.put("headImg", merchantsInfoBanner.getUrl());
            //推修的营业状态
            jsonObject.put("state", 1);
        } else if (type == 2) {
            //洗美
            lableDetailsWrapper.eq("lable_id", 4);
            LableDetailsReviewTreeEntity lableDetailsReview = lableDetailsReviewTreeService.selectOne(lableDetailsWrapper);

            EntityWrapper<LableDetailsReviewTreeEntity> lableDetailsWrapper2 = new EntityWrapper<>();
            lableDetailsWrapper2.eq("user_b_id", userBId)
                    .eq("state", 1)
                    .eq("`show`", 1)
                    .eq("lable_id", 18);
            LableDetailsReviewTreeEntity lableDetailsReview2 = lableDetailsReviewTreeService.selectOne(lableDetailsWrapper2);

            if (lableDetailsReview == null && lableDetailsReview2 == null) {
                jsonObject.put("code", 408);
                jsonObject.put("msg", "暂未开通洗车或者美容服务");
                return jsonObject;
            }

            jsonObject.put("name", appUserEntity.getMerchantsName());
            jsonObject.put("headImg", merchantsInfoBanner.getUrl());
            centerDtaisl = appCleanIndetService.selectCleanCenterMes(userBId);
            //查询b端洗车营业状态
            EntityWrapper<AppBUserConfigEntity> appBUserConfigEntityEntityWrapper = new EntityWrapper<>();
            appBUserConfigEntityEntityWrapper.eq("user_b_id", userBId);
            AppBUserConfigEntity appBUserConfigEntity = appBUserConfigService.selectOne(appBUserConfigEntityEntityWrapper);
            jsonObject.put("state", appBUserConfigEntity.getBusinessType());
        } else if (type == 3) {
            lableDetailsWrapper.eq("lable_id", 12);
            LableDetailsReviewTreeEntity lableDetailsReviewTreeEntity = lableDetailsReviewTreeService.selectOne(lableDetailsWrapper);
            if (lableDetailsReviewTreeEntity == null) {
                jsonObject.put("code", 408);
                jsonObject.put("msg", "暂未开通喷漆服务");
                return jsonObject;
            }
            //查询喷漆技师
            EntityWrapper<AppUserBMessageEntity> bMessageWrapper = new EntityWrapper<>();
            bMessageWrapper.eq("user_b_id", userBId)
                    .eq("wrok_type", 3);
            //查询技师
            AppUserBMessageEntity appUserBMessage = appUserBMessageService.selectOne(bMessageWrapper);
            if (appUserBMessage == null) {
                jsonObject.put("code", 408);
                jsonObject.put("msg", "未查询到喷漆技师");
                return jsonObject;
            }
            jsonObject.put("name", appUserBMessage.getName());
            jsonObject.put("headImg", appUserBMessage.getHeadImg());
            //查询订单列表
            centerDtaisl = appSprayPaintIndentService.selectSprayIndentCenterMes(appUserBMessage.getId());
            jsonObject.put("state", appUserBMessage.getBusinessType());

        } else if (type == 4) {
            // 4：救援，
            lableDetailsWrapper.eq("lable_id", 13);
            LableDetailsReviewTreeEntity lableDetailsReviewTreeEntity = lableDetailsReviewTreeService.selectOne(lableDetailsWrapper);
            if (lableDetailsReviewTreeEntity == null) {
                jsonObject.put("code", 408);
                jsonObject.put("msg", "暂未开通救援服务");
                return jsonObject;
            }
            //查询救援技师
            EntityWrapper<AppUserBMessageEntity> bMessageWrapper = new EntityWrapper<>();
            bMessageWrapper.eq("user_b_id", userBId)
                    .eq("wrok_type", 1);
            //查询技师
            AppUserBMessageEntity appUserBMessage = appUserBMessageService.selectOne(bMessageWrapper);
            if (appUserBMessage == null) {
                jsonObject.put("code", 408);
                jsonObject.put("msg", "未查询到救援技师");
                return jsonObject;
            }
            jsonObject.put("name", appUserBMessage.getName());
            jsonObject.put("headImg", appUserBMessage.getHeadImg());
            jsonObject.put("state", appUserBMessage.getBusinessType());
            centerDtaisl = appRescueIndentService.selectRescueIndentCenterMes(appUserBMessage.getUserBId());

        } else if (type == 5) {
            //   5：年检
            lableDetailsWrapper.eq("lable_id", 15);
            LableDetailsReviewTreeEntity lableDetailsReviewTreeEntity = lableDetailsReviewTreeService.selectOne(lableDetailsWrapper);
            if (lableDetailsReviewTreeEntity == null) {
                jsonObject.put("code", 408);
                jsonObject.put("msg", "暂未开通年检服务");
                return jsonObject;
            }
            //查询年检
            EntityWrapper<AppUserBMessageEntity> bMessageWrapper = new EntityWrapper<>();
            bMessageWrapper.eq("user_b_id", userBId)
                    .eq("wrok_type", 2);
            AppUserBMessageEntity appUserBMessage = appUserBMessageService.selectOne(bMessageWrapper);
            if (appUserBMessage == null) {
                jsonObject.put("code", 408);
                jsonObject.put("msg", "未查询到年检技师");
                return jsonObject;
            }
            jsonObject.put("name", appUserBMessage.getName());
            jsonObject.put("headImg", appUserBMessage.getHeadImg());
            jsonObject.put("state", appUserBMessage.getBusinessType());
            centerDtaisl = appYearCheckIndentService.selectYearIndentCenterMes(appUserBMessage.getUserBId());

        }

        jsonObject.put("todayIndent", centerDtaisl.getTodayIndent());
        BigDecimal allEarnings = centerDtaisl.getAllEarnings();
        BigDecimal multiply = allEarnings.multiply(new BigDecimal("0.8"));
        jsonObject.put("allEarnings", multiply.setScale(2, BigDecimal.ROUND_HALF_UP));
        jsonObject.put("allIndentCount", centerDtaisl.getAllIndentCount());
        jsonObject.put("newIntentCount", centerDtaisl.getNewIntentCount());
        jsonObject.put("inServiceCount", centerDtaisl.getInServiceCount());
        jsonObject.put("obligationCount", centerDtaisl.getObligationCount());   //待付款
        jsonObject.put("toEvaluateCount", centerDtaisl.getToEvaluateCount());   //待评价
        jsonObject.put("cancelledCount", centerDtaisl.getCancelledCount());
        return jsonObject;
    }

    public JSONObject updatamodifyBusssiness(Integer userBId, Integer type) {
        JSONObject object = new JSONObject();
        //  1:推修，2：洗美，3：喷漆，4：救援，5：年检
        AppUserEntity appUserEntity = bUserService.selectById(userBId);
        if (type == 1) {
            object.put("code", 408);
            object.put("msg", "推修营业状态不能修改");
            return object;
        } else if (type == 2) {
            //修改洗美状态
            EntityWrapper<AppBUserConfigEntity> configWrapper = new EntityWrapper<>();
            configWrapper.eq("user_b_id", userBId);
            AppBUserConfigEntity userConfigEntity = appBUserConfigService.selectOne(configWrapper);
            String businessType = userConfigEntity.getBusinessType();
            if (StringUtils.isEmpty(businessType)) {
                userConfigEntity.setBusinessType("1");
            } else {
                if (businessType.equals("1")) {
                    userConfigEntity.setBusinessType("2");
                } else {
                    userConfigEntity.setBusinessType("1");
                }
            }
            appBUserConfigService.updateById(userConfigEntity);
        } else if (type == 3) {
            //修改喷漆状态
            //查询喷漆技师
            EntityWrapper<AppUserBMessageEntity> bMessageWrapper = new EntityWrapper<>();
            bMessageWrapper.eq("user_b_id", userBId)
                    .eq("wrok_type", 3);
            //查询技师
            AppUserBMessageEntity appUserBMessage = appUserBMessageService.selectOne(bMessageWrapper);
            if (appUserBMessage == null) {
                object.put("code", 408);
                object.put("msg", "未查询到喷漆技师");
                return object;
            }
            Integer businessType = appUserBMessage.getBusinessType();
            if (businessType != null) {
                if (businessType == 1) {
                    appUserBMessage.setBusinessType(2);
                } else {
                    appUserBMessage.setBusinessType(1);
                }
            } else {
                appUserBMessage.setBusinessType(1);
            }
            appUserBMessageService.updateById(appUserBMessage);
        } else if (type == 4) {
            EntityWrapper<AppUserBMessageEntity> bMessageWrapper = new EntityWrapper<>();
            bMessageWrapper.eq("user_b_id", userBId)
                    .eq("wrok_type", 1);
            //查询技师
            AppUserBMessageEntity appUserBMessage = appUserBMessageService.selectOne(bMessageWrapper);
            if (appUserBMessage == null) {
                object.put("code", 408);
                object.put("msg", "未查询到救援技师");
                return object;
            }
            Integer businessType = appUserBMessage.getBusinessType();
            if (businessType != null) {
                if (businessType == 1) {
                    appUserBMessage.setBusinessType(2);
                    stringRedisTemplate.opsForGeo().remove(RedisConstant.MERCHANTS_RESCUE_GEO, appUserEntity.getRescueRedis());
                } else {
                    appUserBMessage.setBusinessType(1);
                    addRescueIndentGeo(appUserEntity,appUserBMessage);
                }
            } else {
                appUserBMessage.setBusinessType(1);
                addRescueIndentGeo(appUserEntity,appUserBMessage);
            }
            appUserBMessageService.updateById(appUserBMessage);
        } else if (type == 5) {
            //5：年检
            EntityWrapper<AppUserBMessageEntity> bMessageWrapper = new EntityWrapper<>();
            bMessageWrapper.eq("user_b_id", userBId)
                    .eq("wrok_type", 2);
            AppUserBMessageEntity appUserBMessage = appUserBMessageService.selectOne(bMessageWrapper);
            if (appUserBMessage == null) {
                object.put("code", 408);
                object.put("msg", "未查询到年检技师");
                return object;
            }
            Integer businessType = appUserBMessage.getBusinessType();
            if (businessType != null) {
                if (businessType == 1) {
                    appUserBMessage.setBusinessType(2);
                    //修改为未营业
                    stringRedisTemplate.opsForGeo().remove(RedisConstant.MERCHANTS_YEAR_CHECK_GEO, appUserEntity.getYearcheckRedis());
                } else {
                    appUserBMessage.setBusinessType(1);
                    //修改为营业
                    addYearCheckGeo(appUserEntity,appUserBMessage);
                }
            } else {
                appUserBMessage.setBusinessType(1);
                addYearCheckGeo(appUserEntity,appUserBMessage);
            }
            appUserBMessageService.updateById(appUserBMessage);

        }
        object.put("code", 200);
        return object;
    }


    private void addYearCheckGeo(AppUserEntity appUserEntity, AppUserBMessageEntity appUserBMessage){
        JSONObject js = new JSONObject();
        js.put("id", appUserEntity.getId());
        js.put("merchantsName", appUserEntity.getMerchantsName());
        js.put("lat", appUserBMessage.getLat());
        js.put("lng", appUserBMessage.getLng());
        js.put("address", appUserBMessage.getWorkPlace());
        //修改georedis数据
        stringRedisTemplate.opsForGeo().add(RedisConstant.MERCHANTS_YEAR_CHECK_GEO, new Point(appUserBMessage.getLng().doubleValue(), appUserBMessage.getLat().doubleValue()), js.toJSONString());
        appUserEntity.setYearcheckRedis(js.toJSONString());
        userService.updateById(appUserEntity);
    }

    private void addRescueIndentGeo(AppUserEntity appUserEntity, AppUserBMessageEntity appUserBMessage){
        JSONObject js = new JSONObject();
        js.put("id", appUserEntity.getId());
        js.put("merchantsName", appUserEntity.getMerchantsName());
        js.put("lat", appUserBMessage.getLat());
        js.put("lng", appUserBMessage.getLng());
        js.put("address", appUserBMessage.getWorkPlace());
        //修改georedis数据
        stringRedisTemplate.opsForGeo().add(RedisConstant.MERCHANTS_RESCUE_GEO, new Point(appUserBMessage.getLng().doubleValue(), appUserBMessage.getLat().doubleValue()), js.toJSONString());
        appUserEntity.setRescueRedis(js.toJSONString());
        userService.updateById(appUserEntity);
    }

}
