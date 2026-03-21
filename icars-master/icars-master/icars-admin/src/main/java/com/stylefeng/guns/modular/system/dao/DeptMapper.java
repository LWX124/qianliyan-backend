package com.stylefeng.guns.modular.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.node.ZTreeNode;
import com.stylefeng.guns.modular.system.model.Dept;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门表 Mapper 接口
 * </p>
 *
 * @author kosans
 * @since 2017-07-11
 */
public interface DeptMapper extends BaseMapper<Dept> {

    /**
     * 获取ztree的节点列表
     */
    List<ZTreeNode> tree();

    /**
     * 获取所有部门列表
     */
    List<Map<String, Object>> list(@Param("condition") String condition);

    /**
     * 获取所有4S门店列表
     */
    List<Map<String, Object>> listFours(@Param("fullName") String fullName, @Param("tips") String tips);
    /**
     * 根据地市查询
     */
    List<Dept> selectPageByCondition(@Param("page") Page<Dept> page, @Param("dict") String dict, @Param("qcpp") String qcpp);

}