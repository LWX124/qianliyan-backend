/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.TreeDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppLableDetailsReviewTree;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 标签和明细表DAO接口
 * @author zcq
 * @version 2019-09-28
 */
@MyBatisDao
public interface AppLableDetailsReviewTreeDao extends TreeDao<AppLableDetailsReviewTree> {

    Double findMaxRebates(String lableCode);

    List<AppLableDetailsReviewTree> findLable(String id);

    List<AppLableDetailsReviewTree> finduserBId(Integer user_b_id);

    Integer getLastLable();

    List<AppLableDetailsReviewTree> findIdTreeLeaf(String lableCode);

    List<AppLableDetailsReviewTree> finduserandParent(@Param("userBId")Integer userBId,@Param("i") int i);

    AppLableDetailsReviewTree selectLable(@Param("userBId")Integer userBId, @Param("lableId")int lableId, @Param("show")int show);

    void add(AppLableDetailsReviewTree lableDetailsReview);

    List<Integer> selectAllAccident();

    List<BigDecimal> selectAllList(Integer userBId);
}