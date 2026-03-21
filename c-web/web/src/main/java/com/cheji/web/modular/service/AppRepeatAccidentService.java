package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.AppRepeatAccidentEntity;
import com.cheji.web.modular.mapper.AppRepeatAccidentMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-11-27
 */
@Service
public class AppRepeatAccidentService extends ServiceImpl<AppRepeatAccidentMapper, AppRepeatAccidentEntity> {

    @Resource
    private AppRepeatAccidentMapper appRepeatAccidentMapper;

    public List<AppRepeatAccidentEntity> selectByAccid(Integer id) {
        return appRepeatAccidentMapper.selectByAccid(id);
    }

    public String selectWxVideo(Integer accidentSource) {
        return appRepeatAccidentMapper.selectWxVideo(accidentSource);
    }
}
