package com.cheji.web.modular.mapper;

import com.cheji.web.modular.cwork.BUserMessageDto;
import com.cheji.web.modular.domain.AppUserBMessageEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户服务信息表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2020-05-06
 */
public interface AppUserBMessageMapper extends BaseMapper<AppUserBMessageEntity> {

    List<BUserMessageDto> findInWork(@Param("score")Integer score, @Param("pagesize")Integer pagesize);

    AppUserBMessageEntity selectByBIdWorkType(@Param("userBId")Integer userBId, @Param("i")int i);

    AppUserBMessageEntity selectByUserId(Integer userBId);

}
