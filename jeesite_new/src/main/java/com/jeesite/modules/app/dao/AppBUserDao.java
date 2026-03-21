/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppBUser;
import com.jeesite.modules.app.entity.AppIndent;
import com.jeesite.modules.app.entity.AppLableDetailsReviewTree;

import java.util.List;

/**
 * 用户信息表DAO接口
 * @author zcq
 * @version 2019-10-16
 */
@MyBatisDao
public interface AppBUserDao extends CrudDao<AppBUser> {

    List<String> findBrand(String id);


    AppBUser selectFotUpdate(Integer userId);

    AppBUser updateBalance(Integer userBId);

    AppBUser findOne(String phone);

    List<AppLableDetailsReviewTree> findByUserId(String id);

    List<Integer> findAllMerchants();

    List<String> findUpMerchants();

    List<AppBUser> findIsCompany();

    AppBUser findBByPhone(String phone);

    Integer selectBUserMessage(Integer id);

    void findJsUpload(String id, String s);

    void insertFileUrl(String id, String s);

    String findNewUrl(String uploadId);

    List<String> findNewUrl1(String uploadId);

    String findRep(String fileId);

    Integer findShelvesCount();

    Integer findOutCount();

    Integer findAllCount();

    List<String> findMerchantsInsert(String id);

    AppIndent findNumberAndMoney(String id);

    String getCity(String id);


    AppBUser finUserByUpId(String id1);

    List<AppBUser> findAllByState();

    List<String> findImgsByAuctionId(Long upId);
}
