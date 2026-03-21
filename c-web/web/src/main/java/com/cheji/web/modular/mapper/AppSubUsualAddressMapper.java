package com.cheji.web.modular.mapper;

import com.cheji.web.modular.domain.AppSubUsualAddressEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 常用地址 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2020-06-10
 */
public interface AppSubUsualAddressMapper extends BaseMapper<AppSubUsualAddressEntity> {

    List<AppSubUsualAddressEntity> selectHistoryAddress(Integer userid);
}
