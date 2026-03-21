package com.cheji.web.modular.mapper;

import com.cheji.web.modular.domain.AppPhotoMerEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.domain.MerchantsInfoBannerEntity;

import java.util.List;

/**
 * <p>
 * 图片上传商户图片表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2021-03-09
 */
public interface AppPhotoMerMapper extends BaseMapper<AppPhotoMerEntity> {

    List<MerchantsInfoBannerEntity> selectBrannerList(String merchantsCode);

}
