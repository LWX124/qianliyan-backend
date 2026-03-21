package com.cheji.web.modular.mapper;

import com.cheji.web.modular.domain.AppUserCouponEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户优惠卷表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2020-07-17
 */
public interface AppUserCouponMapper extends BaseMapper<AppUserCouponEntity> {

    List<AppUserCouponEntity> findCoupon(Integer userid);

    Integer findNoUseCoupon(String userId);

    AppUserCouponEntity findCouponByType(@Param("userid")Integer userid,@Param("type") Integer type);
}
