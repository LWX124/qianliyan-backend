package com.cheji.web.modular.mapper;

import com.cheji.web.modular.domain.AppUpMerchantsEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 通过图片上架的4s店表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2021-03-09
 */
public interface AppUpMerchantsMapper extends BaseMapper<AppUpMerchantsEntity> {

    List<AppUpMerchantsEntity> findNoHuanxin();

}
