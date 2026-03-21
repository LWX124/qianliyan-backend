/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppUpMerchants;
import com.jeesite.modules.app.dao.AppUpMerchantsDao;

import javax.annotation.Resource;

/**
 * 通过图片上架的4s店表Service
 *
 * @author zcq
 * @version 2021-03-08
 */
@Service
@Transactional(readOnly = true)
public class AppUpMerchantsService extends CrudService<AppUpMerchantsDao, AppUpMerchants> {

	@Resource
	private HuanXinService huanXinService;

    @Resource
    private AppUpMerchantsDao appUpMerchantsDao;

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
     * @param appUpMerchants      查询条件
     * @param appUpMerchants.page 分页对象
     * @return
     */
    @Override
    public Page<AppUpMerchants> findPage(AppUpMerchants appUpMerchants) {
        return super.findPage(appUpMerchants);
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

    public List<String> findBrands(ArrayList<Integer> ids) {
        return appUpMerchantsDao.findBrands(ids);
    }

    public List<AppUpMerchants> findUpMerchantsByBrands(List<String> upMerchants) {
        return appUpMerchantsDao.findUpMerchantsByBrands(upMerchants);
    }
}