package com.stylefeng.guns.modular.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * <p>
 * 数据统计 Mapper 接口
 * </p>
 *
 * @author duanhong
 * @since 2019-03-14
 */
public interface DataCountMapper extends BaseMapper {
    List<Map<String, Object>> countCoupon(Map in);

    List<Map> countUser(@Param("startTime")String startTime,@Param("endTime") String endTime,@Param("name") String name,@Param("department") String department);

    List<Map> xcxUserCount(int offset, int limit);
}