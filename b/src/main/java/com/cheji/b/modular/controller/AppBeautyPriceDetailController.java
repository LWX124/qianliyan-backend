package com.cheji.b.modular.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cheji.b.constant.RedisConstant;
import com.cheji.b.modular.domain.AppBUserConfigEntity;
import com.cheji.b.modular.domain.AppBeautyPriceDetailEntity;
import com.cheji.b.modular.domain.LableDetailsReviewTreeEntity;
import com.cheji.b.modular.dto.PriceDetailsDto;
import com.cheji.b.modular.excep.CusException;
import com.cheji.b.modular.service.AppBUserConfigService;
import com.cheji.b.modular.service.AppBeautyPriceDetailService;
import com.cheji.b.modular.service.LableDetailsReviewTreeService;
import com.cheji.b.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Ashes
 * @since 2019-12-20
 */
@RestController
@RequestMapping("beautyPriceDetail")
public class AppBeautyPriceDetailController extends BaseController {


    @Resource
    private LableDetailsReviewTreeService lableDetailsReviewTreeService;

    @Resource
    private AppBUserConfigService appBUserConfigService;

    @Resource
    private AppBeautyPriceDetailService appBeautyPriceDetailService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @ApiOperation(value = "保存美容价格明细")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "businessType", value = "营业状态", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "startTime", value = "开始时间", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "endTime", value = "结束时间", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "openWashTime", value = "是否开通营业时间", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "data", value = "美容类型，车型，原价，优惠价，到手价,备注", required = true, dataType = "List<PriceDetailsDto>")
    })
    @RequestMapping(value = "/saveBeautyDeatils", method = RequestMethod.POST)
    public JSONObject saveBeautyDeatils(@RequestBody AppBeautyPriceDetailEntity appBeautyPriceDetailEntity, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        //判断登陆
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        if (appBeautyPriceDetailEntity == null) {
            result.put("code", 520);
            result.put("msg", "参数无数据");
            return result;
        }
        Integer userBId = currentLoginUser.getAppUserEntity().getId();

        appBeautyPriceDetailEntity.setUserBId(userBId);

        //修改营业数据状态
        EntityWrapper<AppBUserConfigEntity> appBUserConfigWrapper = new EntityWrapper<>();
        appBUserConfigWrapper.eq("user_b_id", userBId);
        AppBUserConfigEntity appBUserConfigEntity = appBUserConfigService.selectOne(appBUserConfigWrapper);
        appBUserConfigService.isadd(userBId, appBeautyPriceDetailEntity, appBUserConfigEntity);

        //拿到平台抽成数据
        String s = stringRedisTemplate.opsForValue().get(RedisConstant.CAR_WASH_SERVICE_PLATFORM_RATIO);
        BigDecimal bigDecimal = new BigDecimal(1);
        //0.9
        BigDecimal subtract = bigDecimal.subtract(new BigDecimal(s));

        if (appBeautyPriceDetailEntity.getData() != null) {
            for (PriceDetailsDto datum : appBeautyPriceDetailEntity.getData()) {
                if (datum.getOriginalPrice() == null) {
                    result.put("code", 402);
                    result.put("msg", "原价不能为空");
                    return result;
                } else if (datum.getCarType() == null) {
                    result.put("code", 402);
                    result.put("msg", "车型不能为空");
                    return result;
                } else if (datum.getBeautyType() == null) {
                    result.put("code", 402);
                    result.put("msg", "美容类型不能为空");
                    return result;
                } else if (datum.getPreferentialPrice() == null) {
                    result.put("code", 402);
                    result.put("msg", "现价不能为空");
                    return result;
                }
            }

            //判断是否添加第一次级别
            try {
                lableDetailsReviewTreeService.addFirst(userBId);
            } catch (CusException e) {
                result.put("code", e.getCode());
                result.put("msg", e.getMessage());
                return result;
            }

            EntityWrapper<LableDetailsReviewTreeEntity> lableWarpper = new EntityWrapper<>();
            lableWarpper.eq("user_b_id", userBId)
                    .eq("lable_id", 18)
                    .eq("`show`", 1);
            LableDetailsReviewTreeEntity addSecond = lableDetailsReviewTreeService.selectOne(lableWarpper);
            if (addSecond == null) {
                addSecond = lableDetailsReviewTreeService.addBeautySecond(userBId, 18, null);
            }

            EntityWrapper<AppBeautyPriceDetailEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("user_b_id", appBeautyPriceDetailEntity.getUserBId());
            appBeautyPriceDetailService.delete(wrapper);
            AppBeautyPriceDetailEntity appBeautyPriceDetailEntity1 = new AppBeautyPriceDetailEntity();
            for (PriceDetailsDto datum : appBeautyPriceDetailEntity.getData()) {
                if (appBeautyPriceDetailEntity.getBusinessType() == null) {
                    result.put("code", 402);
                    result.put("msg", "BusinessType不能为空");
                    return result;
                }
                appBeautyPriceDetailEntity1.setUserBId(appBeautyPriceDetailEntity.getUserBId());
                appBeautyPriceDetailEntity1.setBeautyType(Integer.valueOf(datum.getBeautyType()));
                appBeautyPriceDetailEntity1.setCarType(Integer.valueOf(datum.getCarType()));
                appBeautyPriceDetailEntity1.setOriginalPrice(datum.getOriginalPrice());
                appBeautyPriceDetailEntity1.setPreferentialPrice(datum.getPreferentialPrice());
                appBeautyPriceDetailEntity1.setThriePrice(datum.getPreferentialPrice().multiply(subtract));
                appBeautyPriceDetailEntity1.setNote(datum.getNote());
                appBeautyPriceDetailEntity1.setState(1);
                appBeautyPriceDetailEntity1.setCreateTime(new Date());
                appBeautyPriceDetailEntity1.setUpdateTime(new Date());
                appBeautyPriceDetailService.insert(appBeautyPriceDetailEntity1);
                // lableDetailsReviewTreeService.addThirdBeauty(appBeautyPriceDetailEntity1, addSecond);
            }
        }
        result.put("code", 200);
        result.put("mesg", "成功");
        return result;
    }



    @ApiOperation(value = "美容价格参数")
    @RequestMapping(value = "/beautyParameter", method = RequestMethod.GET)
    public JSONObject cleanParameter(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        AppBeautyPriceDetailEntity beautyPriceDetailEntity = new AppBeautyPriceDetailEntity();
        ArrayList<PriceDetailsDto> priceDetailsDtos = new ArrayList<>();
        Integer userBId = currentLoginUser.getAppUserEntity().getId();
        //根据userbid查询到config
        EntityWrapper<AppBUserConfigEntity> appBUserConfigWrapper = new EntityWrapper<>();
        appBUserConfigWrapper.eq("user_b_id",userBId);
        AppBUserConfigEntity appBUserConfigEntity = appBUserConfigService.selectOne(appBUserConfigWrapper);
        if (appBUserConfigEntity!=null){
            beautyPriceDetailEntity.setBusinessType(appBUserConfigEntity.getBusinessType());
            beautyPriceDetailEntity.setStartTime(appBUserConfigEntity.getStartTime());
            beautyPriceDetailEntity.setEndTime(appBUserConfigEntity.getEndTime());
        }
        for (int i = 1; i < 11; i++){   //打蜡，抛光，内堂，机舱，装甲，封釉，镀晶，贴膜，车衣，改装
            //查询名称
            String beautyName = appBeautyPriceDetailService.findbeautyType(i);
            for (int j = 1; j < 5; j++) {
                PriceDetailsDto priceDetailsDto = new PriceDetailsDto();
                priceDetailsDto.setBeautyType(String.valueOf(i));
                priceDetailsDto.setBeautyName(beautyName);
                if (j == 1) {
                    priceDetailsDto.setCarName("小型轿车");
                } else if (j == 2) {
                    priceDetailsDto.setCarName("小型越野");
                } else if (j == 3) {
                    priceDetailsDto.setCarName("大型越野");
                } else {
                    priceDetailsDto.setCarName("商务车");
                }
                priceDetailsDto.setCarType(String.valueOf(j));
                //查询对应数据
                EntityWrapper<AppBeautyPriceDetailEntity> priceDetailWrapper = new EntityWrapper<>();
                priceDetailWrapper.eq("beauty_type",i)
                        .eq("car_type",j)
                        .eq("user_b_id",userBId);
                AppBeautyPriceDetailEntity priceDetailEntity = appBeautyPriceDetailService.selectOne(priceDetailWrapper);
                if (priceDetailEntity!=null){
                    priceDetailsDto.setOriginalPrice(priceDetailEntity.getOriginalPrice());
                    priceDetailsDto.setPreferentialPrice(priceDetailEntity.getPreferentialPrice());
                    priceDetailsDto.setThriePrice(priceDetailEntity.getThriePrice());
                }
                priceDetailsDtos.add(priceDetailsDto);
            }
        }
        beautyPriceDetailEntity.setData(priceDetailsDtos);

        String s = stringRedisTemplate.opsForValue().get(RedisConstant.CAR_WASH_SERVICE_PLATFORM_RATIO);
        BigDecimal bigDecimal = new BigDecimal(1);
        BigDecimal subtract = bigDecimal.subtract(new BigDecimal(s));

        beautyPriceDetailEntity.setRabates(subtract);

        result.put("code",200);
        result.put("data",beautyPriceDetailEntity);
        return result;

    }

}
