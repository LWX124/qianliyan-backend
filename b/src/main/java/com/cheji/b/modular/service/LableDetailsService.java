package com.cheji.b.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.domain.LableDetailsEntity;
import com.cheji.b.modular.mapper.LableDetailsMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-09-29
 */
@Service
public class LableDetailsService extends ServiceImpl<LableDetailsMapper, LableDetailsEntity> implements IService<LableDetailsEntity> {

}
