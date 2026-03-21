package com.stylefeng.guns.modular.system.dao;

import com.stylefeng.guns.modular.system.model.BizClaimerShow;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 模范理赔顾问表 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2018-08-28
 */
public interface BizClaimerShowMapper extends BaseMapper<BizClaimerShow> {
    List<Map<String, Object>> selectClaimerShows(@Param("name") String name, @Param("status")Integer status);
    List<Map<String, Object>> selectClaimerShowsYes();
    List<Map<String, Object>> getThreeModel();

}
