package com.cheji.web.modular.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.constant.RedisConstant;
import com.cheji.web.modular.cwork.CleanMerDto;
import com.cheji.web.modular.domain.AppBUserConfigEntity;
import com.cheji.web.modular.domain.CleanIndetEntity;
import com.cheji.web.modular.domain.CleanPriceDetailEntity;
import com.cheji.web.modular.excep.CusException;
import com.cheji.web.modular.mapper.CleanPriceDetailMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 商户清洗价格明细表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-12-12
 */
@Service
public class CleanPriceDetailService extends ServiceImpl<CleanPriceDetailMapper, CleanPriceDetailEntity> implements IService<CleanPriceDetailEntity> {

    @Resource
    private CleanPriceDetailMapper cleanPriceDetailMapper;

    @Resource
    private CleanIndetService cleanIndetService;

    @Resource
    private BUserService bUserService;

    @Resource
    private AppBUserConfigService appBUserConfigService;


    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public List<String> findUsersByCleanType(String cleanType, Integer cityCode) {
        if (cleanType.equals("1")) {
            //查询普洗
            return cleanPriceDetailMapper.findUsersByCleanType1(cleanType, cityCode);
        } else if (cleanType.equals("2")) {
            //精洗
            return cleanPriceDetailMapper.findUsersByCleanType1(cleanType, cityCode);
        } else if (cleanType.equals("3")) {
            //自动
            return cleanPriceDetailMapper.findUsersByCleanType3(cleanType, cityCode);
        } else if (cleanType.equals("4")) {
            //夜间洗车
            return cleanPriceDetailMapper.findUsersByCleanType4(cleanType, cityCode);
        } else if (cleanType.equals("5")) {
            //免费
            return cleanPriceDetailMapper.findUserByCleanType5(cleanType, cityCode);
        }
        return cleanPriceDetailMapper.findUsersByCleanType1(cleanType, cityCode);
    }

    public CleanMerDto findMerchantsByid(String userid, String cleanType) {
        //通过商户id查询到每个商户得信息
        //商户主图，名字，类型，分数，地址，经纬度，
        CleanMerDto bUserEntity = bUserService.findMerchantsByid(userid);
        if (bUserEntity == null) {
            return null;
        }
        if (StringUtils.isEmpty(bUserEntity.getUrl())) {
            return null;
        }
        if (StringUtils.isEmpty(bUserEntity.getMerchantsName())) {
            return null;
        }
        if (StringUtils.isEmpty(bUserEntity.getAddress())) {
            return null;
        }
        //查询到营业时间
        EntityWrapper<AppBUserConfigEntity> appBUserConfigWrapper = new EntityWrapper<>();
        appBUserConfigWrapper.eq("user_b_id", userid);
        AppBUserConfigEntity bUserConfigEntity = appBUserConfigService.selectOne(appBUserConfigWrapper);
        String startTime = bUserConfigEntity.getStartTime();
        String endTime = bUserConfigEntity.getEndTime();

        //截取：之前字符串
        String str1=startTime.substring(0, startTime.indexOf(":"));
        //截取：之后字符串
        String str2=startTime.substring(str1.length()+1, startTime.length());

        if (str1.equals("00")&&str2.equals("00")){
            bUserEntity.setStartTime(null);
        }else {
            bUserEntity.setStartTime(startTime);
        }

        String str3=endTime.substring(0, endTime.indexOf(":"));
        //截取：之后字符串
        String str4=endTime.substring(str3.length()+1, endTime.length());

        if (str3.equals("00")&&str4.equals("00")){
            bUserEntity.setEndTime(null);
        }else {
            bUserEntity.setEndTime(endTime);
        }

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
        cleanIndetWrapper.eq("user_b_id", userid)
                .eq("resource", "1");
        List<CleanIndetEntity> cleanIndetEntities = cleanIndetService.selectList(cleanIndetWrapper);
        if (cleanIndetEntities.isEmpty()) {
            bUserEntity.setIndentCount(0);
        } else {
            bUserEntity.setIndentCount(cleanIndetEntities.size());
        }

        //查询该商品卖总数
        EntityWrapper<CleanIndetEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("user_b_id", userid)
                .eq("clean_type", cleanType)
                .eq("resource", "1");
        List<CleanIndetEntity> cleanIndetEntities1 = cleanIndetService.selectList(entityWrapper);
        if (cleanIndetEntities1.isEmpty()) {
            bUserEntity.setSaveAllCount(0);
        } else {
            bUserEntity.setSaveAllCount(cleanIndetEntities1.size());
        }
        //查询到优惠之后最便宜得数据
        List<CleanPriceDetailEntity> priceDetails = cleanPriceDetailMapper.findCleanDetails(userid, cleanType);
        CleanPriceDetailEntity priceDetail = priceDetails.get(0);

        Integer carType = priceDetail.getCarType();
        bUserEntity.setCarType(carType);
        if (carType == 1) {
            bUserEntity.setCarName("小型轿车");
        } else if (carType == 2) {
            bUserEntity.setCarName("小型越野");
        } else if (carType == 3) {
            bUserEntity.setCarName("大型越野");
        } else {
            bUserEntity.setCarName("商务车");
        }
        //原价
        bUserEntity.setOrginalPrice(priceDetail.getOriginalPrice());
        //优惠价
        bUserEntity.setPreferentialPrice(priceDetail.getPreferentialPrice());
        return bUserEntity;
    }

    public List<CleanPriceDetailEntity> findDetailsByUserid(String userBId, String cleanType) {
        return cleanPriceDetailMapper.findDetailsByUserid(userBId, cleanType);
    }

    public CleanPriceDetailEntity selectPriceDetailsForUpdate(String userBId, String carType, String indentCleanType) {
        return cleanPriceDetailMapper.selectPriceDetailsForUpdate(userBId, carType, indentCleanType);
    }

    public JSONObject findCleanDetilas(String userBId, JSONObject result, String cleanType) {
        double currentTime = new Long(new Date().getTime()).doubleValue();
        Date date = new Date();
        String string = date.toString();
        stringRedisTemplate.opsForZSet().add(RedisConstant.MERCHANTS_VISIT_COUNT + userBId, string, currentTime);
        //根据用户id查询到数据
        //主图，名称，分数，地址，经纬度，电话，环信username
        CleanMerDto cleanMerDto = bUserService.findCleanDetails(userBId);
        if (cleanMerDto == null) {
            result.put("code", 200);
            result.put("data", null);
            result.put("msg", "没有数据，检查商户id");
            return result;
        }
        EntityWrapper<CleanIndetEntity> indetEntityWrapper = new EntityWrapper<>();
        indetEntityWrapper.eq("user_b_id", userBId)
                .eq("resource", "1");
        List<CleanIndetEntity> cleanIndetEntities = cleanIndetService.selectList(indetEntityWrapper);
        cleanMerDto.setIndentCount(cleanIndetEntities.size());
        //到手价格比例
        String s = stringRedisTemplate.opsForValue().get(RedisConstant.CAR_WASH_SERVICE_PLATFORM_RATIO);
        if (StringUtils.isEmpty(s)) {
            BigDecimal bigDecimal = new BigDecimal(1);
            BigDecimal subtract = bigDecimal.subtract(new BigDecimal(s));
            cleanMerDto.setSettlementRatio(subtract);
        }

        //是否有免费洗车
        EntityWrapper<AppBUserConfigEntity> configEntityEntityWrapper = new EntityWrapper<>();
        configEntityEntityWrapper.eq("user_b_id", userBId);
        AppBUserConfigEntity appBUserConfigEntity = appBUserConfigService.selectOne(configEntityEntityWrapper);
        cleanMerDto.setStartTime(appBUserConfigEntity.getStartTime());
        cleanMerDto.setEndTime(appBUserConfigEntity.getEndTime());
        //开通了免费洗车
        if (appBUserConfigEntity.getPassFreeCarwash().equals("1")) {
            EntityWrapper<CleanPriceDetailEntity> priceDetailWrapper = new EntityWrapper<>();
            priceDetailWrapper.eq("user_b_id", userBId)
                    .eq("clean_type", 5)
                    .eq("car_type", 0);
            CleanPriceDetailEntity cleanPriceDetailEntity = selectOne(priceDetailWrapper);
            cleanPriceDetailEntity.setCarName("不限");
            cleanPriceDetailEntity.setCleanName("免费");
            cleanPriceDetailEntity.setNote("外观，仪表，座椅，脚垫清洗，内饰吸尘");
            ArrayList<CleanPriceDetailEntity> cleanPriceDetailEntities = new ArrayList<>();
            cleanPriceDetailEntities.add(cleanPriceDetailEntity);
            cleanMerDto.setPriceDetailEntityList(cleanPriceDetailEntities);
            cleanMerDto.setPrompt("温馨提示：自购买之日起七天内使用");
            result.put("code", 200);
            result.put("data", cleanMerDto);
        } else {
            //服务类型  列表：车型，普洗，优惠价，原价，
            if (cleanType == null) {
                cleanType = null;
            } else if (cleanType.equals("4") || cleanType.equals("3")) {
                cleanType = null;
            }
            List<CleanPriceDetailEntity> priceDetailEntityList = findDetailsByUserid(userBId, cleanType);
            if (priceDetailEntityList.isEmpty()) {
                result.put("code", 200);
                result.put("data", null);
                result.put("msg", "暂无数据");
            } else {
                for (CleanPriceDetailEntity detailEntity : priceDetailEntityList) {
                    if (detailEntity.getCarType() == 1) {
                        detailEntity.setCarName("小型轿车");
                    } else if (detailEntity.getCarType() == 2) {
                        detailEntity.setCarName("小型越野");
                    } else if (detailEntity.getCarType() == 3) {
                        detailEntity.setCarName("大型越野");
                    } else {
                        detailEntity.setCarName("商务车");
                    }
                    Integer entityCleanType = detailEntity.getCleanType();
                    if (entityCleanType == 1) {
                        detailEntity.setCleanName("普洗");
                        detailEntity.setNote("外观，仪表，座椅，脚垫清洗，内饰吸尘");
                    } else {
                        detailEntity.setCleanName("精洗");
                        detailEntity.setNote("轮胎打蜡，边缝清洗，发动机舱清洗，车内精致清洗");
                    }
                }
                cleanMerDto.setPriceDetailEntityList(priceDetailEntityList);
                cleanMerDto.setPrompt("温馨提示：自购买之日起七天内使用");
                result.put("code", 200);
                result.put("data", cleanMerDto);
            }
        }
        return result;
    }
}
