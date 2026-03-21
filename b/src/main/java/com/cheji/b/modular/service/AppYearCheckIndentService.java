package com.cheji.b.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.domain.AppCleanIndetEntity;
import com.cheji.b.modular.domain.AppYearCheckIndentEntity;
import com.cheji.b.modular.dto.CenterDetailsDto;
import com.cheji.b.modular.mapper.AppSprayPaintIndentMapper;
import com.cheji.b.modular.mapper.AppYearCheckIndentMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * <p>
 * 年检订单表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2020-04-02
 */
@Service
public class AppYearCheckIndentService extends ServiceImpl<AppYearCheckIndentMapper, AppYearCheckIndentEntity> implements IService<AppYearCheckIndentEntity> {

    @Resource
    private AppYearCheckIndentMapper appYearCheckIndentMapper;

    @Resource
    private AppSprayPaintIndentMapper appSprayPaintIndentMapper;

    public CenterDetailsDto selectYearIndentCenterMes(Integer userBId) {
        return appYearCheckIndentMapper.selectYearIndentCenterMes(userBId);
    }

    public List<AppCleanIndetEntity> newYearCheckIndentList(Integer userBId, Integer type, Integer pagesize) {
        pagesize = (pagesize-1)*20;
        List<AppCleanIndetEntity> yearCheckIndentList = appYearCheckIndentMapper.newYearCheckIndentList(userBId, type, pagesize);
        for (AppCleanIndetEntity appCleanIndetEntity : yearCheckIndentList) {

            Integer integer = appSprayPaintIndentMapper.selectByCoupon(appCleanIndetEntity.getCleanIndentNumber());
            if (integer==null){
                appCleanIndetEntity.setAmount(appCleanIndetEntity.getAmount().multiply(new BigDecimal("0.8")).setScale(2,BigDecimal.ROUND_HALF_UP));
            }

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(appCleanIndetEntity.getCreateTime());
            appCleanIndetEntity.setTime(dateString);
        }
        return yearCheckIndentList;
    }
}
