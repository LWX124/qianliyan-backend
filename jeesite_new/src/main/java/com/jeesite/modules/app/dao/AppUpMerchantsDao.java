/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppIndent;
import com.jeesite.modules.app.entity.AppUpMerchants;

import java.util.ArrayList;
import java.util.List;

/**
 * 通过图片上架的4s店表DAO接口
 * @author zcq
 * @version 2021-03-08
 */
@MyBatisDao
public interface AppUpMerchantsDao extends CrudDao<AppUpMerchants> {

    void insertNew(AppUpMerchants appUpMerchants);

    List<String> findAllPhone();

    List<AppUpMerchants> findNoHuanxin();

    List<AppUpMerchants> findAllMerchants(int pageoffset, String address, String selectName, String selectBrand, Integer selectAdcode);

    Long findCount(String address, String selectName, String selectBrand, Integer selectAdcode);

    AppIndent findNumberAndMoney(String id);

    String findCity(String adcode);

    String findCityCode(String selectAdcode);

    String findCarCode(String selectBrand);

    List<String> findBrands(ArrayList<Integer> ids);

    List<AppUpMerchants> findUpMerchantsByBrands(List<String> upMerchants);

    void insertUpMer(AppUpMerchants appUpMerchants);
}