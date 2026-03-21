package com.cheji.b.modular.mapper;

import com.cheji.b.modular.domain.AccidentRecord;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.b.modular.dto.ListDetailsDto;

/**
 * <p>
 * app上报事故信息表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-09-26
 */
public interface AccidentRecordMapper extends BaseMapper<AccidentRecord> {

    ListDetailsDto findAccidentDetails(String accid);

    ListDetailsDto findAccidentDetail(Integer accid);
}
