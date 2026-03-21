/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppPhotoMer;

import java.util.ArrayList;

/**
 * 图片上传商户图片表DAO接口
 * @author zcq
 * @version 2021-03-08
 */
@MyBatisDao
public interface AppPhotoMerDao extends CrudDao<AppPhotoMer> {

    ArrayList<Integer> findAllImgs(String id);

    void insertNew(AppPhotoMer appPhotoMer);

}