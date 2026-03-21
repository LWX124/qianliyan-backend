/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.dao.AppUpMerchantsDao;
import com.jeesite.modules.app.entity.AppBUser;
import com.jeesite.modules.app.entity.AppCarBrand;
import com.jeesite.modules.app.entity.AppIndent;
import com.jeesite.modules.app.entity.AppUpMerchants;
import com.jeesite.modules.util2.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;

/**
 * 通过图片上架的4s店表Service
 *
 * @author zcq
 * @version 2021-03-08
 */
@Service
@Transactional(readOnly = true)
public class AppAllMerchantsService extends CrudService<AppUpMerchantsDao, AppUpMerchants> {

    @Resource
    private HuanXinService huanXinService;

    @Resource
    private AppUpMerchantsDao appUpMerchantsDao;

    @Resource
    private AppBUserService appBUserService;

    @Resource
    private AppCarBrandService appCarBrandService;

    /**
     * 获取单条数据
     *
     * @param appUpMerchants
     * @return
     */
    @Override
    public AppUpMerchants get(AppUpMerchants appUpMerchants) {
        return super.get(appUpMerchants);
    }

    /**
     * 查询分页数据
     *
     * @param appUpMerchants 查询条件
     * @return
     */
    @Override
    public Page<AppUpMerchants> findPage(AppUpMerchants appUpMerchants) {
        Page a = appUpMerchants.getPage();
        int pageNo = a.getPageNo();
        int pageSize = a.getPageSize();
        int pageoffset = (pageNo - 1) * pageSize;
        // 查询参数
        String address = appUpMerchants.getAddress();
        String selectName = appUpMerchants.getName();
        String selectBrand = appUpMerchants.getBrand();
        String cityname = appUpMerchants.getCityname();
        Integer selectAdcode = null;
        if (StringUtils.isNotEmpty(selectBrand)) {
            //先用汉字查询到数字
            selectBrand = appUpMerchantsDao.findCarCode(selectBrand);
        }
        if (StringUtils.isNotEmpty(cityname)) {
            //用汉字查询到数字再进去查询
            selectAdcode = Integer.valueOf(appUpMerchantsDao.findCityCode(cityname));
        }
        List<AppUpMerchants> allList = appUpMerchantsDao.findAllMerchants(pageoffset, address, selectName, selectBrand, selectAdcode);
        Long count = appUpMerchantsDao.findCount(address, selectName, selectBrand, selectAdcode);
        for (AppUpMerchants upMerchants : allList) {
            String type = upMerchants.getType();
            if (type.equals("1")) {
                //up
                //查询数据  查询名下订单数据
                String id = upMerchants.getId();
                AppIndent json = appUpMerchantsDao.findNumberAndMoney(id);
                upMerchants.setIndentNumber(json.getBrandId());
                upMerchants.setMoney(json.getBalance());
                Integer adcode = upMerchants.getAdcode();
                String cityName = appUpMerchantsDao.findCity(adcode.toString());
                upMerchants.setCityname(cityName);
            } else {
                //user_b_id
                AppIndent json = appBUserService.findNumberAndMoney(upMerchants.getId());
                upMerchants.setIndentNumber(json.getBrandId());
                upMerchants.setMoney(json.getBalance());
                Integer adcode = upMerchants.getAdcode();
                if (adcode == null || adcode == 0) {
                    upMerchants.setCityname("暂无");
                } else {
                    String name = appBUserService.getCity(upMerchants.getAdcode().toString());
                    upMerchants.setCityname(name);
                }
            }
            String brand = upMerchants.getBrand();
            if (StringUtils.isEmpty(brand)) {
                upMerchants.setBrand("修理厂");
            } else {
                String name = appCarBrandService.get(brand).getName();
                upMerchants.setBrand(name);
            }
        }
        a.setList(allList);
        a.setCount(count);
        return a;
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param appUpMerchants
     */
    @Override
    @Transactional(readOnly = false)
    public void save(AppUpMerchants appUpMerchants) {
        super.save(appUpMerchants);
    }

    /**
     * 更新状态
     *
     * @param appUpMerchants
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(AppUpMerchants appUpMerchants) {
        super.updateStatus(appUpMerchants);
    }

    /**
     * 删除数据
     *
     * @param appUpMerchants
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(AppUpMerchants appUpMerchants) {
        super.delete(appUpMerchants);
    }


    @Transactional(readOnly = false)
    public void insertNew(AppUpMerchants appUpMerchants) {
        appUpMerchantsDao.insertNew(appUpMerchants);
    }


    public List<String> findAllPhone() {
        return appUpMerchantsDao.findAllPhone();
    }

    @Transactional
    public void registerHuanxin(AppUpMerchants appUpMerchants, String newStr) {
        Random random = new Random();
        String userName = appUpMerchants.getId() + newStr + ((int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000) + "";
        String passWord = ((int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000) + "";
        String nikeName = "默认用户" + ((int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000) + "";
        huanXinService.singleRegister(userName, passWord, nikeName, false);

        appUpMerchants.setHuanxinUsername(userName);
        appUpMerchants.setHuanxinPassword(passWord);
        this.update(appUpMerchants);
    }

    public List<AppUpMerchants> findNoHuanxin() {
        return appUpMerchantsDao.findNoHuanxin();
    }
}