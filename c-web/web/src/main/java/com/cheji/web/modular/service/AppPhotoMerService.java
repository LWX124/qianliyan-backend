package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.AppPhotoMerEntity;
import com.cheji.web.modular.domain.MerchantsInfoBannerEntity;
import com.cheji.web.modular.mapper.AppPhotoMerMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 图片上传商户图片表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2021-03-09
 */
@Service
public class AppPhotoMerService extends ServiceImpl<AppPhotoMerMapper, AppPhotoMerEntity> implements IService<AppPhotoMerEntity> {

    @Resource
    private AppPhotoMerMapper appPhotoMerMapper;

    public List<MerchantsInfoBannerEntity> selectBrannerList(String merchantsCode) {
        return appPhotoMerMapper.selectBrannerList(merchantsCode);
    }
}
