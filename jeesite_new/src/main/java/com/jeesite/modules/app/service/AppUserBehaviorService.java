/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import com.jeesite.modules.app.entity.AppUser;
import com.jeesite.modules.app.entity.BizWxUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppUserBehavior;
import com.jeesite.modules.app.dao.AppUserBehaviorDao;

import javax.annotation.Resource;

/**
 * 用户视频统计个数Service
 *
 * @author zcq
 * @version 2019-12-04
 */
@Service
@Transactional(readOnly = true)
public class AppUserBehaviorService extends CrudService<AppUserBehaviorDao, AppUserBehavior> {

    @Resource
    private AppUserBehaviorDao appUserBehaviorDao;

    @Resource
    private AppUserService appUserService;

    @Resource
	private BizWxUserService bizWxUserService;

    /**
     * 获取单条数据
     *
     * @param appUserBehavior
     * @return
     */
    @Override
    public AppUserBehavior get(AppUserBehavior appUserBehavior) {
        return super.get(appUserBehavior);
    }

    /**
     * 查询分页数据
     *
     * @param appUserBehavior      查询条件
     * @param appUserBehavior.page 分页对象
     * @return
     */
    @Override
    public Page<AppUserBehavior> findPage(AppUserBehavior appUserBehavior) {
        Page a = appUserBehavior.getPage();
        Integer pageNo = a.getPageNo();
        Integer pageSize = a.getPageSize();
        Integer pageoffset = (pageNo - 1) * pageSize;
        List<AppUserBehavior> behaviorList = appUserBehaviorDao.allselect(pageoffset);
        int size = behaviorList.size();
        for (AppUserBehavior userBehavior : behaviorList) {
            if (userBehavior.getUserSource().equals("1")) {
                //c端用户
                AppUser appUser = appUserService.get(userBehavior.getUserId());
                userBehavior.setUserName(appUser.getName());
                userBehavior.setUserCome("C端");
                userBehavior.setBalck(appUser.getBalck());
            } else {
				//小程序
				BizWxUser byOpenid = bizWxUserService.findByOpenid(userBehavior.getUserId());
				userBehavior.setUserName(byOpenid.getWxname());
				userBehavior.setUserCome("小程序");
				userBehavior.setBalck(byOpenid.getBlacklist());
			}

        }
        a.setList(behaviorList);
        a.setCount(size);
        return a;
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param appUserBehavior
     */
    @Override
    @Transactional(readOnly = false)
    public void save(AppUserBehavior appUserBehavior) {
        super.save(appUserBehavior);
    }

    /**
     * 更新状态
     *
     * @param appUserBehavior
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(AppUserBehavior appUserBehavior) {
        super.updateStatus(appUserBehavior);
    }

    /**
     * 删除数据
     *
     * @param appUserBehavior
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(AppUserBehavior appUserBehavior) {
        super.delete(appUserBehavior);
    }

    public AppUserBehavior selectByUserId(String userId) {
        return appUserBehaviorDao.selectByUserId(userId);
    }

    @Transactional(readOnly = false)
    public void addScount(AppUserBehavior appUserBehavior, String genre) {
        //1.轻微   2.地上拍   3.车边拍   4.重复  5..通过个数
        if (genre.equals("1")) {
            if (appUserBehavior.getSlight() == null) {
                appUserBehavior.setSlight(0);
            }
            appUserBehavior.setSlight(appUserBehavior.getSlight() + 1);
        } else if (genre.equals("2")) {
            if (appUserBehavior.getOnGround() == null) {
                appUserBehavior.setOnGround(0);
            }
            appUserBehavior.setOnGround(appUserBehavior.getOnGround() + 1);
        } else if (genre.equals("3")) {
            if (appUserBehavior.getNearCar() == null) {
                appUserBehavior.setNearCar(0);
            }
            appUserBehavior.setNearCar(appUserBehavior.getNearCar() + 1);
        } else if (genre.equals("4")) {
            if (appUserBehavior.getRepeat() == null) {
                appUserBehavior.setRepeat(0);
            }
            appUserBehavior.setRepeat(appUserBehavior.getRepeat() + 1);
        } else if (genre.equals("5")) {
            if (appUserBehavior.getPassNumber() == null) {
                appUserBehavior.setPassNumber(0);
            }
            appUserBehavior.setPassNumber(appUserBehavior.getPassNumber() + 1);
        }
        update(appUserBehavior);
    }
}