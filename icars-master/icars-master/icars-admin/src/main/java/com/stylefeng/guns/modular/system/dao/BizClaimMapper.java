package com.stylefeng.guns.modular.system.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.modular.system.model.BizClaim;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 理赔单 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2018-08-27
 */
public interface BizClaimMapper extends BaseMapper<BizClaim> {
    /**
     * 根据条件查询事故列表 分页
     */
    List<Map<String, Object>> selectListForPage(@Param("page") Page<BizClaim> page, @Param("bizClaim") BizClaim bizClaim, @Param("startTime") String startTime, @Param("endTime") String endTime);

    List<Map<String, Object>> selectListForPageByOpendId(@Param("page") Page<BizClaim> page,  @Param("openId")String openId,@Param("type")Integer type);

    List<Map<String, Object>> selectAllForXcx(@Param("page") Page<BizClaim> page,@Param("openId")  String openId,@Param("type") Integer type);
}
