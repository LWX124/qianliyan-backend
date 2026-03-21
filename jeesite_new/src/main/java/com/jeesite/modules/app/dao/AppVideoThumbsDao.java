/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppVideoThumbs;
import org.apache.ibatis.annotations.Param;

/**
 * 视频点赞表DAO接口
 * @author zcq
 * @version 2019-09-17
 */
@MyBatisDao
public interface AppVideoThumbsDao extends CrudDao<AppVideoThumbs> {

    AppVideoThumbs findByUserIdAndVideoId(String videoId,String userId);
}