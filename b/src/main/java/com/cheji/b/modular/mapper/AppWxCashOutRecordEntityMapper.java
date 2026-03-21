package com.cheji.b.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.b.modular.domain.AppWxCashOutRecordEntity;
import com.cheji.b.modular.dto.ChangeDetailsDto;

public interface AppWxCashOutRecordEntityMapper extends BaseMapper<AppWxCashOutRecordEntity> {

    ChangeDetailsDto findChangeDetails(String changeId);
}
