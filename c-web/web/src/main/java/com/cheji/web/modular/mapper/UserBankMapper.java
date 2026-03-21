package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.cwork.BankCard;
import com.cheji.web.modular.domain.UserBankEntity;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-09-02
 */
public interface UserBankMapper extends BaseMapper<UserBankEntity> {

    List<BankCard> findBankAndTail(String id);
}
