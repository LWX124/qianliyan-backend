package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.domain.LableDetailsReviewTreeEntity;
import com.cheji.web.modular.domain.LableEntity;
import com.cheji.web.modular.domain.MerchantsLableEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 标签和明细表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-09-28
 */
public interface LableDetailsReviewTreeMapper extends BaseMapper<LableDetailsReviewTreeEntity> {

    List<MerchantsLableEntity> findSerAndRebById(String merchantsCode);

    BigDecimal findrebates(Long id);

    List<MerchantsLableEntity> findallRebates(Long id);

    List<LableEntity> findLableName(String merchantsCode);

    BigDecimal findHigeRebates(Long id);
}
