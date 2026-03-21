package com.cheji.b.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.b.modular.domain.AppWxpayOrderEntity;
import com.cheji.b.modular.dto.ChangeDetailsDto;

public interface AppWxpayOrderMapper extends BaseMapper<AppWxpayOrderEntity> {
    void updateStatus(AppWxpayOrderEntity appWxpayOrderEntity);

}
