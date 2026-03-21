package com.cheji.web.modular.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cheji.web.constant.RedisConstant;
import com.cheji.web.modular.cwork.CleanMerDto;
import com.cheji.web.modular.domain.AppBUserConfigEntity;
import com.cheji.web.modular.domain.AppBeautyPriceDetailEntity;
import com.cheji.web.modular.domain.CleanIndetEntity;
import com.cheji.web.modular.mapper.AppBeautyPriceDetailMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.ws.RespectBinding;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-12-20
 */
@Service
public class AppBeautyPriceDetailService extends ServiceImpl<AppBeautyPriceDetailMapper, AppBeautyPriceDetailEntity> implements IService<AppBeautyPriceDetailEntity> {

    @Resource
    private AppBeautyPriceDetailMapper appBeautyPriceDetailMapper;

    @Resource
    private AppBUserConfigService appBUserConfigService;

    @Resource
    private BUserService bUserService;

    @Resource
    private CleanIndetService cleanIndetService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public List<String> findUserByBeauty(String beautyType, Integer cityCode) {
        return appBeautyPriceDetailMapper.findUserByBeauty(beautyType,cityCode);
    }

    public CleanMerDto findMerchantsById(String userB, String beautyType) {
        //主图，名字，类型，分数，地址，经纬度
        CleanMerDto buser = bUserService.findMerchantsByid(userB);
        if (buser == null){
            return null;
        }
        if (StringUtils.isEmpty(buser.getUrl())){
            return null;
        }
        if (StringUtils.isEmpty(buser.getMerchantsName())){
            return null;
        }
        if (StringUtils.isEmpty(buser.getAddress())){
            return null;
        }
        //查询到营业时间
        EntityWrapper<AppBUserConfigEntity> appBUserConfigWrapper = new EntityWrapper<>();
        appBUserConfigWrapper.eq("user_b_id", userB);
        AppBUserConfigEntity bUserConfigEntity = appBUserConfigService.selectOne(appBUserConfigWrapper);
        String startTime = bUserConfigEntity.getStartTime();
        String endTime = bUserConfigEntity.getEndTime();
//        Integer start = Integer.valueOf(startTime.substring(0, startTime.indexOf(":")));
//        Integer end = Integer.valueOf(endTime.substring(0, endTime.indexOf(":")));
//
//        Calendar calendar = Calendar.getInstance();
//        Integer now = calendar.get(calendar.HOUR_OF_DAY);
//
//        if (start > end) {
//            if (start > now && now > end) {
//                return null;
//            }
//        }
//        if (end > start) {
//            if (start > now && now > end) {
//                return null;
//            }
//        }
        //查询总订单数该商家的
        EntityWrapper<CleanIndetEntity> cleanIndetWrapper = new EntityWrapper<>();
        cleanIndetWrapper.eq("user_b_id", userB)
                .eq("resource", "2");
        List<CleanIndetEntity> cleanIndetEntities = cleanIndetService.selectList(cleanIndetWrapper);
        if (cleanIndetEntities.isEmpty()) {
            buser.setIndentCount(0);
        } else {
            buser.setIndentCount(cleanIndetEntities.size());
        }
        //查询该商品卖总数
        EntityWrapper<CleanIndetEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("user_b_id", userB)
                .eq("clean_type", beautyType)
                .eq("resource", "1");
        List<CleanIndetEntity> cleanIndetEntities1 = cleanIndetService.selectList(entityWrapper);
        if (cleanIndetEntities1.isEmpty()) {
            buser.setSaveAllCount(0);
        } else {
            buser.setSaveAllCount(cleanIndetEntities1.size());
        }
        //最便宜的优惠价格
        List<AppBeautyPriceDetailEntity> beautyPriceDetails = appBeautyPriceDetailMapper.findBeautyDetails(userB,beautyType);
        AppBeautyPriceDetailEntity beautyPriceDetail = beautyPriceDetails.get(0);
        Integer carType = beautyPriceDetail.getCarType();
        buser.setCarType(carType);
        if (carType == 1) {
            buser.setCarName("小型轿车");
        } else if (carType == 2) {
            buser.setCarName("小型越野");
        } else if (carType == 3) {
            buser.setCarName("大型越野");
        } else {
            buser.setCarName("商务车");
        }
        //原价
        buser.setOrginalPrice(beautyPriceDetail.getOriginalPrice());
        //优惠价
        buser.setPreferentialPrice(beautyPriceDetail.getPreferentialPrice());
        return buser;
    }

    public List<AppBeautyPriceDetailEntity> findDetailsByUserId(String userBId) {
        return appBeautyPriceDetailMapper.findDetailsByUserId(userBId);
    }



    public JSONObject findBeautyDetails(String userBId,JSONObject result) {
        double currentTime = new Long(new Date().getTime()).doubleValue();
        Date date = new Date();
        String string = date.toString();
        stringRedisTemplate.opsForZSet().add(RedisConstant.MERCHANTS_VISIT_COUNT + userBId, string, currentTime);
        //根据商户id查询到数据
        //主图，名称，分数，地址，经纬度，电话，环信username
        CleanMerDto beautyDetails = bUserService.findCleanDetails(userBId);
        if (beautyDetails == null) {
            result.put("code", 200);
            result.put("data", null);
            result.put("msg", "没有数据，检查用户id");
            return result;
        }

        EntityWrapper<AppBUserConfigEntity> configEntityEntityWrapper = new EntityWrapper<>();
        configEntityEntityWrapper.eq("user_b_id", beautyDetails.getUserBId());
        AppBUserConfigEntity appBUserConfigEntity = appBUserConfigService.selectOne(configEntityEntityWrapper);
        beautyDetails.setStartTime(appBUserConfigEntity.getStartTime());
        beautyDetails.setEndTime(appBUserConfigEntity.getEndTime());

        EntityWrapper<CleanIndetEntity> indetEntityWrapper = new EntityWrapper<>();
        indetEntityWrapper.eq("user_b_id", userBId)
                .eq("resource", "1");
        List<CleanIndetEntity> cleanIndetEntities = cleanIndetService.selectList(indetEntityWrapper);
        beautyDetails.setIndentCount(cleanIndetEntities.size());
//        EntityWrapper<AppBeautyPriceDetailEntity> detailEntityWrapper = new EntityWrapper<>();
//        detailEntityWrapper.eq("user_b_id",userBId);
//        List<AppBeautyPriceDetailEntity> priceDetailEntityList = appBeautyPriceDetailService.selectList(detailEntityWrapper);
        //到手比例价格
        String s = stringRedisTemplate.opsForValue().get(RedisConstant.CAR_WASH_SERVICE_PLATFORM_RATIO);
        if (StringUtils.isNotEmpty(s)) {
            BigDecimal bigDecimal = new BigDecimal(1);
            BigDecimal subtract = bigDecimal.subtract(new BigDecimal(s));
            beautyDetails.setSettlementRatio(subtract);
        }

        //服务类型，：列表，车型，美容类型，优惠价，原价
        List<AppBeautyPriceDetailEntity> detailEntities = findDetailsByUserId(userBId);
        if (detailEntities.isEmpty()) {
            result.put("code", 200);
            result.put("data", null);
            result.put("msg", "暂无数据");
        } else {
            for (AppBeautyPriceDetailEntity detailEntity : detailEntities) {
                if (detailEntity.getCarType() == 1) {
                    detailEntity.setCarName("小型轿车");
                } else if (detailEntity.getCarType() == 2) {
                    detailEntity.setCarName("小型越野");
                } else if (detailEntity.getCarType() == 3) {
                    detailEntity.setCarName("大型越野");
                } else {
                    detailEntity.setCarName("商务车");
                }
            }
            beautyDetails.setBeautyPriceDetailList(detailEntities);
            beautyDetails.setPrompt("温馨提示：自购买之日起七天内使用");
            result.put("code", 200);
            result.put("data", beautyDetails);
        }
        return result;
    }
}
