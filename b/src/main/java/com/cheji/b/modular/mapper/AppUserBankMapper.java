package com.cheji.b.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.b.modular.domain.AppUserBankEntity;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 视频评论表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-08-13
 */
public interface AppUserBankMapper extends BaseMapper<AppUserBankEntity> {


    List<Map> selectListById(Integer id);

    List<AppUserBankEntity> findBankCard(Integer userBId);
}
