/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppAuctionImg;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

/**
 * 拍卖车详情图片DAO接口
 * @author y
 * @version 2022-10-10
 */
@MyBatisDao
public interface AppAuctionImgDao extends CrudDao<AppAuctionImg> {

    ArrayList<AppAuctionImg> findAllImgs(String carId);

    void updateImgOne(@Param("imgSrc") String imgSrc, @Param("imgId")String imgId);
}