/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppVideo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 视频信息表DAO接口
 *
 * @author zcq
 * @version 2019-08-01
 */
@MyBatisDao
public interface AppVideoDao extends CrudDao<AppVideo> {

    //查询7天内的视频总数
    String findAllCount();

    //查询所有数据，用来放到redis里面
    List<AppVideo> findReids();

    //找到三十天内分数最小的视频
    AppVideo select30DayMinScore();

    //根据事故id来查询到对应的video数据
    AppVideo findVideoAccid(String accid);

    AppVideo findVideoByAccidAndType(@Param("id") String id, @Param("source")String source);

    List<AppVideo> findListDot(@Param("pageOffset")int pageOffset, @Param("state")Integer state, @Param("url")String url, @Param("format")String format);

    long findcount();

}