package com.stylefeng.guns.modular.system.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.modular.system.model.BizWxUser;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.stylefeng.guns.modular.system.model.BizWxpayBill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 微信用户信息表 Mapper 接口
 * </p>
 *
 * @author kosans
 * @since 2018-07-24
 */
public interface BizWxUserMapper extends BaseMapper<BizWxUser> {
    /**
     * 绑定支付宝账户
     */
    int setAlipay(@Param("alipayAccount") String alipayAccount, @Param("openid") String openid);

    /**
     * 绑定支付宝账户
     */
    int setPhone(@Param("phone") String phone, @Param("openid") String openid);

    int setThirdOpenid(@Param("thirdOpenId") String thirdOpenId, @Param("openid") String openid);

    /**
     * 查询用户信息
     */
    BizWxUser selectBizWxUser(@Param("openid") String openid, @Param("source") String source);

    List<BizWxUser> selectFansPage(@Param("page") Page<BizWxUser> page, @Param("bizWxUser") BizWxUser bizWxUser);

    int incrementShareCount(@Param("openid") String openid);

    int incrementShareOpenCount(@Param("openid") String openid);
}
