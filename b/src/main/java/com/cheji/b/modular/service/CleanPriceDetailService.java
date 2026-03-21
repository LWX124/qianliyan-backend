package com.cheji.b.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.b.modular.domain.CleanPriceDetailEntity;
import com.cheji.b.modular.dto.PriceDetailsDto;
import com.cheji.b.modular.mapper.CleanPriceDetailMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-12-11
 */
@Service
public class CleanPriceDetailService extends ServiceImpl<CleanPriceDetailMapper, CleanPriceDetailEntity> implements IService<CleanPriceDetailEntity> {

    @Resource
    private CleanPriceDetailMapper cleanPriceDetailMapper;

    public List<PriceDetailsDto> selectMesg(Integer userBId) {
        return cleanPriceDetailMapper.selectMesg(userBId);
    }
}
