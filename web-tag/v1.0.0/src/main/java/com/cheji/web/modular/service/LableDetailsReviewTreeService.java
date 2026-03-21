package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.LableDetailsReviewTreeEntity;
import com.cheji.web.modular.domain.LableEntity;
import com.cheji.web.modular.domain.MerchantsLableEntity;
import com.cheji.web.modular.mapper.LableDetailsReviewTreeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 标签和明细表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-09-28
 */
@Service
public class LableDetailsReviewTreeService extends ServiceImpl<LableDetailsReviewTreeMapper, LableDetailsReviewTreeEntity> implements IService<LableDetailsReviewTreeEntity> {
    @Resource
    private LableDetailsReviewTreeMapper lableDetailsReviewTreeMapper;


    public List<MerchantsLableEntity> findSerAndRebById(String merchantsCode) {
        return lableDetailsReviewTreeMapper.findSerAndRebById(merchantsCode);
    }

    public BigDecimal findrebates(Long id) {
        return lableDetailsReviewTreeMapper.findrebates(id);
    }

    public List<MerchantsLableEntity> findallRebates(Long id) {
        return lableDetailsReviewTreeMapper.findallRebates(id);
    }

    public List<LableEntity> findLableName(String merchantsCode) {
        return lableDetailsReviewTreeMapper.findLableName(merchantsCode);
    }

    public BigDecimal findHigeRebates(Long id) {
        return lableDetailsReviewTreeMapper.findHigeRebates(id);
    }
}
