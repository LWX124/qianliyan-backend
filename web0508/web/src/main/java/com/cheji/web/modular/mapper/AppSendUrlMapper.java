package com.cheji.web.modular.mapper;

import com.cheji.web.modular.domain.AppSendUrlEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 * 派单记录图片表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2021-01-13
 */
public interface AppSendUrlMapper extends BaseMapper<AppSendUrlEntity> {

    String findMapImg(Integer id);

    String[] findAccidentImg(Integer id);

    String findCheckImg(Long id);

}
