/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppMerchants;
import com.jeesite.modules.app.entity.AppMerchantsInfoBanner;

import java.util.List;

/**
 * 商户详情banner图DAO接口
 * @author zcq
 * @version 2019-07-30
 */
@MyBatisDao
public interface AppMerchantsInfoBannerDao extends CrudDao<AppMerchantsInfoBanner> {

    List<AppMerchantsInfoBanner> findImgByMerchantsId(String id);

    AppMerchantsInfoBanner findIndex1Banner(String id);

    List<AppMerchantsInfoBanner> findAllList(String id);

    void insertNew(AppMerchantsInfoBanner infoBanner);

}