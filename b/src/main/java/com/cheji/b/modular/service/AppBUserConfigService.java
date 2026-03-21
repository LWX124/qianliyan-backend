package com.cheji.b.modular.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.domain.AppBUserConfigEntity;
import com.cheji.b.modular.domain.AppBeautyPriceDetailEntity;
import com.cheji.b.modular.domain.CleanPriceDetailEntity;
import com.cheji.b.modular.mapper.AppBUserConfigMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.swing.text.html.parser.Entity;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 商户关联配置表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-12-23
 */
@Service
public class AppBUserConfigService extends ServiceImpl<AppBUserConfigMapper, AppBUserConfigEntity> implements IService<AppBUserConfigEntity> {
    @Resource
    private CleanPriceDetailService cleanPriceDetailService;

    public void isadd(Integer userBId, CleanPriceDetailEntity priceDetailEntity, AppBUserConfigEntity appBUserConfigEntity) {
        if (appBUserConfigEntity == null) {
            AppBUserConfigEntity configEntity = new AppBUserConfigEntity();
            //查询结果为空，就新增数据
            //营业状态
            if (StringUtils.isNotEmpty(priceDetailEntity.getBusinessType())) {
                configEntity.setBusinessType(priceDetailEntity.getBusinessType());
            }
            //是否开通免费洗车
            if (StringUtils.isNotEmpty(priceDetailEntity.getPassFreeCarwash())) {
                configEntity.setPassFreeCarwash(priceDetailEntity.getPassFreeCarwash());
                //开通了免费洗车
                if (priceDetailEntity.getPassFreeCarwash().equals("1")){
                    CleanPriceDetailEntity cleanPriceDetailEntity = new CleanPriceDetailEntity();
                    cleanPriceDetailEntity.setUserBId(userBId);
                    cleanPriceDetailEntity.setCarType(0);
                    cleanPriceDetailEntity.setCleanType(5);
                    cleanPriceDetailEntity.setOriginalPrice(new BigDecimal(0));
                    cleanPriceDetailEntity.setPreferentialPrice(new BigDecimal(0));
                    cleanPriceDetailEntity.setThriePrice(new BigDecimal(0));
                    cleanPriceDetailEntity.setState(1);
                    cleanPriceDetailEntity.setCreateTime(new Date());
                    cleanPriceDetailEntity.setUpdateTime(new Date());
                    cleanPriceDetailService.insert(cleanPriceDetailEntity);
                }else {
                    EntityWrapper<CleanPriceDetailEntity> wrapper = new EntityWrapper<>();
                    wrapper.eq("user_b_id",userBId)
                            .eq("clean_type",5)
                            .eq("car_type",0);
                    CleanPriceDetailEntity cleanPriceDetailEntity = cleanPriceDetailService.selectOne(wrapper);
                    cleanPriceDetailService.deleteById(cleanPriceDetailEntity);
                }
            }
            //人工/自动
            if (StringUtils.isNotEmpty(priceDetailEntity.getManualAntomatic())) {
                configEntity.setManualAntomatic(priceDetailEntity.getManualAntomatic());
            }
            //开始时间
            if (StringUtils.isNotEmpty(priceDetailEntity.getStartTime())) {
                configEntity.setStartTime(priceDetailEntity.getStartTime());
            }
            //结束时间
            if (StringUtils.isNotEmpty(priceDetailEntity.getEndTime())) {
                configEntity.setEndTime(priceDetailEntity.getEndTime());
                String entTi = priceDetailEntity.getEndTime().substring(0, priceDetailEntity.getEndTime().indexOf(":"));
                Integer endTime = Integer.valueOf(entTi);

                String startTi = priceDetailEntity.getStartTime().substring(0, priceDetailEntity.getStartTime().indexOf(":"));
                Integer startTime = Integer.valueOf(startTi);
                if (startTime>endTime){
                    configEntity.setNightWash("1");
                }else {
                    if (endTime > 20) {
                        configEntity.setNightWash("1");
                    }else {
                        configEntity.setNightWash("0");
                    }
                }
            }
            //是否开通过营业时间
            if (StringUtils.isNotEmpty(priceDetailEntity.getOpenWashTime())) {
                configEntity.setOpenWashTime(priceDetailEntity.getOpenWashTime());
            }
            configEntity.setCreateTime(new Date());
            configEntity.setUpdateTime(new Date());
            configEntity.setUserBId(String.valueOf(userBId));
            insert(configEntity);
        } else {
            if (StringUtils.isNotEmpty(priceDetailEntity.getBusinessType())) {
                appBUserConfigEntity.setBusinessType(priceDetailEntity.getBusinessType());
            }
            //是否开通免费洗车
            if (StringUtils.isNotEmpty(priceDetailEntity.getPassFreeCarwash())) {
                appBUserConfigEntity.setPassFreeCarwash(priceDetailEntity.getPassFreeCarwash());
                //开通了免费洗车
                if (priceDetailEntity.getPassFreeCarwash().equals("1")){
                    CleanPriceDetailEntity cleanPriceDetailEntity = new CleanPriceDetailEntity();
                    cleanPriceDetailEntity.setUserBId(userBId);
                    cleanPriceDetailEntity.setCarType(0);
                    cleanPriceDetailEntity.setCleanType(5);
                    cleanPriceDetailEntity.setOriginalPrice(new BigDecimal(0));
                    cleanPriceDetailEntity.setPreferentialPrice(new BigDecimal(0));
                    cleanPriceDetailEntity.setThriePrice(new BigDecimal(0));
                    cleanPriceDetailEntity.setState(1);
                    cleanPriceDetailEntity.setCreateTime(new Date());
                    cleanPriceDetailEntity.setUpdateTime(new Date());
                    cleanPriceDetailService.insert(cleanPriceDetailEntity);
                }else {
                    EntityWrapper<CleanPriceDetailEntity> wrapper = new EntityWrapper<>();
                    wrapper.eq("user_b_id",userBId)
                            .eq("clean_type",5)
                            .eq("car_type",0);
                    CleanPriceDetailEntity cleanPriceDetailEntity = cleanPriceDetailService.selectOne(wrapper);
                    cleanPriceDetailService.deleteById(cleanPriceDetailEntity);
                }
            }
            //人工/自动
            if (StringUtils.isNotEmpty(priceDetailEntity.getManualAntomatic())) {
                appBUserConfigEntity.setManualAntomatic(priceDetailEntity.getManualAntomatic());
            }
            //开始时间
            if (StringUtils.isNotEmpty(priceDetailEntity.getStartTime())) {
                appBUserConfigEntity.setStartTime(priceDetailEntity.getStartTime());
            }
            //结束时间
            if (StringUtils.isNotEmpty(priceDetailEntity.getEndTime())) {
                appBUserConfigEntity.setEndTime(priceDetailEntity.getEndTime());
                String str = priceDetailEntity.getEndTime().substring(0, priceDetailEntity.getEndTime().indexOf(":"));
                Integer integer = Integer.valueOf(str);

                String startTi = priceDetailEntity.getStartTime().substring(0, priceDetailEntity.getStartTime().indexOf(":"));
                Integer startTime = Integer.valueOf(startTi);
                if (startTime>integer){
                    appBUserConfigEntity.setNightWash("1");
                }else {
                    if (integer > 20) {
                        appBUserConfigEntity.setNightWash("1");
                    }else {
                        appBUserConfigEntity.setNightWash("0");
                    }
                }

            }
            //是否开通过营业时间
            if (StringUtils.isNotEmpty(priceDetailEntity.getOpenWashTime())) {
                appBUserConfigEntity.setOpenWashTime(priceDetailEntity.getOpenWashTime());
            }
            appBUserConfigEntity.setUserBId(String.valueOf(userBId));
            appBUserConfigEntity.setUpdateTime(new Date());
            updateById(appBUserConfigEntity);
        }
    }

    public void isadd(Integer userBId, AppBeautyPriceDetailEntity priceDetailEntity, AppBUserConfigEntity appBUserConfigEntity) {
        if (appBUserConfigEntity == null) {
            AppBUserConfigEntity configEntity = new AppBUserConfigEntity();
            //查询结果为空，就新增数据
            //营业状态
            if (StringUtils.isNotEmpty(priceDetailEntity.getBusinessType())) {
                configEntity.setBusinessType(priceDetailEntity.getBusinessType());
            }
            //开始时间
            if (StringUtils.isNotEmpty(priceDetailEntity.getStartTime())) {
                configEntity.setStartTime(priceDetailEntity.getStartTime());
            }
            //结束时间
            if (StringUtils.isNotEmpty(priceDetailEntity.getEndTime())) {
                configEntity.setEndTime(priceDetailEntity.getEndTime());
                String entTi = priceDetailEntity.getEndTime().substring(0, priceDetailEntity.getEndTime().indexOf(":"));
                Integer endTime = Integer.valueOf(entTi);

                String startTi = priceDetailEntity.getStartTime().substring(0, priceDetailEntity.getStartTime().indexOf(":"));
                Integer startTime = Integer.valueOf(startTi);
                if (startTime>endTime){
                    configEntity.setNightWash("1");
                }else {
                    if (endTime > 20) {
                        configEntity.setNightWash("1");
                    }else {
                        configEntity.setNightWash("0");
                    }
                }
            }
            //是否开通过营业时间
            if (StringUtils.isNotEmpty(priceDetailEntity.getOpenWashTime())) {
                configEntity.setOpenWashTime(priceDetailEntity.getOpenWashTime());
            }
            configEntity.setCreateTime(new Date());
            configEntity.setUpdateTime(new Date());
            configEntity.setUserBId(String.valueOf(userBId));
            insert(configEntity);
        } else {
            if (StringUtils.isNotEmpty(priceDetailEntity.getBusinessType())) {
                appBUserConfigEntity.setBusinessType(priceDetailEntity.getBusinessType());
            }
            //开始时间
            if (StringUtils.isNotEmpty(priceDetailEntity.getStartTime())) {
                appBUserConfigEntity.setStartTime(priceDetailEntity.getStartTime());
            }
            //结束时间
            if (StringUtils.isNotEmpty(priceDetailEntity.getEndTime())) {
                appBUserConfigEntity.setEndTime(priceDetailEntity.getEndTime());
                String str = priceDetailEntity.getEndTime().substring(0, priceDetailEntity.getEndTime().indexOf(":"));
                Integer integer = Integer.valueOf(str);

                String startTi = priceDetailEntity.getStartTime().substring(0, priceDetailEntity.getStartTime().indexOf(":"));
                Integer startTime = Integer.valueOf(startTi);
                if (startTime>integer){
                    appBUserConfigEntity.setNightWash("1");
                }else {
                    if (integer > 20) {
                        appBUserConfigEntity.setNightWash("1");
                    }else {
                        appBUserConfigEntity.setNightWash("0");
                    }
                }

            }
            //是否开通过营业时间
            if (StringUtils.isNotEmpty(priceDetailEntity.getOpenWashTime())) {
                appBUserConfigEntity.setOpenWashTime(priceDetailEntity.getOpenWashTime());
            }
            appBUserConfigEntity.setUserBId(String.valueOf(userBId));
            appBUserConfigEntity.setUpdateTime(new Date());
            updateById(appBUserConfigEntity);
        }
    }
}
