package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.AppUpMerchantsEntity;
import com.cheji.web.modular.mapper.AppUpMerchantsMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;

/**
 * <p>
 * 通过图片上架的4s店表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2021-03-09
 */
@Service
public class AppUpMerchantsService extends ServiceImpl<AppUpMerchantsMapper, AppUpMerchantsEntity> implements IService<AppUpMerchantsEntity> {

    @Resource
    private AppUpMerchantsMapper appUpMerchantsMapper;

    @Resource
    private HuanXinService huanXinService;

    public List<AppUpMerchantsEntity> findNoHuanxin() {
        return appUpMerchantsMapper.findNoHuanxin();
    }

    @Transactional
    public void registerHuanxin(AppUpMerchantsEntity appUpMerchantsEntity, String newStr) {
        Random random = new Random();
        String userName = appUpMerchantsEntity.getId() + newStr + ((int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000) + "";
        String passWord = ((int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000) + "";
        String nikeName = "默认用户" + ((int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000) + "";
        huanXinService.singleRegister(userName, passWord, nikeName, false);

        appUpMerchantsEntity.setHuanxinUsername(userName);
        appUpMerchantsEntity.setHuanxinPassword(passWord);
        this.updateById(appUpMerchantsEntity);
    }
}
