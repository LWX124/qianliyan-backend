package com.stylefeng.guns.modular.system.service;

import com.stylefeng.guns.modular.system.model.BizClaimerShow;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 模范理赔顾问表 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2018-08-28
 */
public interface IBizClaimerShowService extends IService<BizClaimerShow> {

    List<Map<String, Object>> selectClaimerShows(@Param("name") String name, @Param("status") Integer status);
    List<Map<String, Object>> selectClaimerShowsYes();
    List<Map<String, Object>> getThreeModel();




}
