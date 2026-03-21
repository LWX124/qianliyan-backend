package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.domain.AppUserAccountRecordEntity;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 用户 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-08-13
 */
public interface AppUserAccountRecordMapper extends BaseMapper<AppUserAccountRecordEntity> {

    BigDecimal findAllPushReward(Integer id);

    List<String> findAccidMessBill(@Param("id")Integer id,@Param("pagesize")Integer pagesize);
}
