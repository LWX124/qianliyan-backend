package com.stylefeng.guns.modular.system.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.modular.system.model.BizWxUser;
import com.baomidou.mybatisplus.service.IService;
import com.stylefeng.guns.modular.system.model.BizWxpayBill;
import com.stylefeng.guns.modular.system.vo.AccidentVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 微信用户信息表 服务类
 * </p>
 *
 * @author kosans
 * @since 2018-07-24
 */
public interface IBizWxUserService extends IService<BizWxUser> {
    int setAlipay(String openid,String alipayAccount);
    int setPhone(String openid,String phone);
    List<BizWxUser> selectByOpenid(String openid);

    BizWxUser selectBizWxUser(String openid);

    List<BizWxUser> selectFansPage(@Param("page") Page<BizWxUser> page, @Param("bizWxUser") BizWxUser bizWxUser);

    void saveOpenId(String thirdOpenid, String openid);

    int incrementShareCount(String openid);

    int incrementShareOpenCount(String openid);
}
