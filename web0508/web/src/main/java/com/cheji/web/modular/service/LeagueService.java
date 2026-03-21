package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.LeagueEntity;
import com.cheji.web.modular.mapper.LeagueMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 加盟信息表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-08-21
 */
@Service
public class LeagueService extends ServiceImpl<LeagueMapper, LeagueEntity> implements IService<LeagueEntity> {

    //保存方法
    public void save(LeagueEntity leagueEntity) {
        leagueEntity.setCreatTime(new Date());
        leagueEntity.setUpdateTime(new Date());
        insert(leagueEntity);
    }
}
