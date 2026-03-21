package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.cheji.web.modular.domain.AppCouponTypeEntity;
import com.cheji.web.modular.domain.AppUserCouponEntity;
import com.cheji.web.modular.mapper.AppUserCouponMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户优惠卷表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2020-07-17
 */
@Service
public class AppUserCouponService extends ServiceImpl<AppUserCouponMapper, AppUserCouponEntity> implements IService<AppUserCouponEntity> {

    @Resource
    private AppUserCouponMapper appUserCouponMapper;

    @Resource
    private AppCouponTypeService appCouponTypeService;


    //新增优惠卷
    public void addCoupon(Integer userId){
        //三张喷漆100卷
        //新增
        //三次
        for (int i = 0; i < 3; i++) {
            AppUserCouponEntity couponEntity = new AppUserCouponEntity();
            couponEntity.setCouponId(1);
            couponEntity.setUserId(userId);
            Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);//设置起时间
            cal.add(Calendar.YEAR, 1);//增加一年
            couponEntity.setValidityTime(cal.getTime());
            couponEntity.setIsUse(2);
            couponEntity.setCreateTime(new Date());
            couponEntity.setUpdateTime(new Date());
            insert(couponEntity);
        }


        for (int i = 0; i < 10; i++) {
            // 十张10元代驾卷
            AppUserCouponEntity couponEntity = new AppUserCouponEntity();
            couponEntity.setCouponId(2);
            couponEntity.setUserId(userId);
            Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);//设置起时间
            cal.add(Calendar.YEAR, 1);//增加一年
            couponEntity.setValidityTime(cal.getTime());
            couponEntity.setIsUse(2);
            couponEntity.setCreateTime(new Date());
            couponEntity.setUpdateTime(new Date());
            insert(couponEntity);

            //十张 10元卷年检，
            AppUserCouponEntity couponEntity2 = new AppUserCouponEntity();
            couponEntity2.setCouponId(3);
            couponEntity2.setUserId(userId);
            Date date2 = new Date();
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);//设置起时间
            cal2.add(Calendar.YEAR, 1);//增加一年
            couponEntity2.setValidityTime(cal.getTime());
            couponEntity2.setIsUse(2);
            couponEntity2.setCreateTime(new Date());
            couponEntity2.setUpdateTime(new Date());
            insert(couponEntity2);
        }
    }

    public List<AppUserCouponEntity> findCoupon(Integer userid) {
        return appUserCouponMapper.findCoupon(userid);
    }

    public Integer findNoUseCoupon(String userId) {
        return appUserCouponMapper.findNoUseCoupon(userId);
    }

    public AppUserCouponEntity findCouponByType(Integer userid,Integer type) {
        AppUserCouponEntity couponByType = appUserCouponMapper.findCouponByType(userid, type);
        if (couponByType!=null){
            Integer couponId = couponByType.getCouponId();
            AppCouponTypeEntity appCouponTypeEntity = appCouponTypeService.selectById(couponId);
            couponByType.setMoney(appCouponTypeEntity.getMoney());
            return couponByType;
        }else {
            return null;
        }
    }
}
