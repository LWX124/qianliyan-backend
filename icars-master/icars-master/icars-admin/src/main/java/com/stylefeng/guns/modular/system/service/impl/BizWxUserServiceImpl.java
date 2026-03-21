package com.stylefeng.guns.modular.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.modular.system.model.BizWxUser;
import com.stylefeng.guns.modular.system.dao.BizWxUserMapper;
import com.stylefeng.guns.modular.system.model.BizWxpayBill;
import com.stylefeng.guns.modular.system.service.IBizWxUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 微信用户信息表 服务实现类
 * </p>
 *
 * @author kosans
 * @since 2018-07-24
 */
@Service
@Transactional
public class BizWxUserServiceImpl extends ServiceImpl<BizWxUserMapper, BizWxUser> implements IBizWxUserService {

    @Transactional
    @Override
    public int setAlipay(String openid, String alipayAccount) {

        return this.baseMapper.setAlipay(alipayAccount,openid);
    }

    @Transactional
    @Override
    public int setPhone(String openid, String phone) {

        return this.baseMapper.setPhone(phone,openid);
    }

    @Override
    public List<BizWxUser> selectByOpenid(String openid) {
        List<BizWxUser> userList = this.baseMapper.selectList(
                new EntityWrapper<BizWxUser>().eq("openid", openid)
        );
        return userList;
    }

    @Override
    public BizWxUser selectBizWxUser(String openid) {
        return this.baseMapper.selectBizWxUser(openid);
    }

    @Override
    public List<BizWxUser> selectFansPage(Page<BizWxUser> page, BizWxUser bizWxUser) {
        return this.baseMapper.selectFansPage(page, bizWxUser);
    }

    @Override
    public void saveOpenId(String thirdOpenid, String openid) {
        baseMapper.setThirdOpenid(thirdOpenid,openid);
    }
}
