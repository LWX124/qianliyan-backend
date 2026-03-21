/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.web;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.app.dao.AppUserDao;
import com.jeesite.modules.app.entity.AppUser;
import com.jeesite.modules.app.entity.AppUserBehavior;
import com.jeesite.modules.app.entity.BizWxUser;
import com.jeesite.modules.app.service.AppUserBehaviorService;
import com.jeesite.modules.app.service.AppUserService;
import com.jeesite.modules.app.service.BizWxUserService;
import com.jeesite.modules.util2.PasswordUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户信息表Controller
 *
 * @author zcq
 * @version 2019-08-08
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appUser")
public class AppUserController extends BaseController {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AppUserDao appUserDao;

    @Resource
    private BizWxUserService bizWxUserService;

    @Resource
    private AppUserBehaviorService appUserBehaviorService;

    /**
     * 获取数据
     */
    @ModelAttribute
    public AppUser get(Long id, boolean isNewRecord) {
        return appUserService.get(String.valueOf(id), isNewRecord);
    }

    /**
     * 查询列表
     */
    @RequiresPermissions("app:appUser:view")
    @RequestMapping(value = {"list", ""})
    public String list(AppUser appUser, Model model) {
        model.addAttribute("appUser", appUser);
        return "modules/app/appUserList";
    }

    /**
     * 查询列表数据
     */
    @RequiresPermissions("app:appUser:view")
    @RequestMapping(value = "listData")
    @ResponseBody
    public Page<AppUser> listData(AppUser appUser, HttpServletRequest request, HttpServletResponse response) {
        appUser.setPage(new Page<>(request, response));
        Page<AppUser> page = appUserService.findPage(appUser);
        return page;
    }

    /**
     * 查看编辑表单
     */
    @RequiresPermissions("app:appUser:view")
    @RequestMapping(value = "form")
    public String form(AppUser appUser, Model model) {
        model.addAttribute("appUser", appUser);
        return "modules/app/appUserForm";
    }

    /**
     * 保存用户信息表
     */
    @RequiresPermissions("app:appUser:edit")
    @PostMapping(value = "save")
    @ResponseBody
    public String save(@Validated AppUser appUser) {
        appUserService.save(appUser);
        return renderResult(Global.TRUE, text("保存用户信息表成功！"));
    }

    /**
     * 删除用户信息表
     */
    @RequiresPermissions("app:appUser:edit")
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(AppUser appUser) {
        appUserService.delete(appUser);
        return renderResult(Global.TRUE, text("删除用户信息表成功！"));
    }

    /**
     * 重置用户密码
     */
    @RequiresPermissions("app:appUser:edit")
    @PostMapping(value = "updatePass")
    @ResponseBody
    public String updatePass(Integer id) {
        AppUser appUser1 = appUserDao.get(new AppUser(id + ""));
        if (appUser1 == null) {
            return renderResult(Global.FALSE, text("没有找到该用户！"));
        }

        //生成8位随机数
        int passWord = (int) ((Math.random() * 9 + 1) * 10000000);

        String salt = PasswordUtil.generateSalt();
        String encodePassword = PasswordUtil.encode(String.valueOf(passWord), salt);//密码加密
        appUser1.setPassword(encodePassword);
        appUser1.setSalt(salt);
        appUserDao.update(appUser1);
        JSONObject reuslt = new JSONObject();
        reuslt.put("pass", String.valueOf(passWord));
        return reuslt.toJSONString();
    }

    @RequiresPermissions("app:appUser:edit")
    @PostMapping(value = "addInner")
    @ResponseBody
    public String addInner(Integer id) {
        AppUser appUser1 = appUserDao.get(new AppUser(id + ""));
        if (appUser1 == null) {
            return renderResult(Global.FALSE, text("没有找到该用户！"));
        }
        Integer isInner = appUser1.getIsInner();
        if (isInner==1){
            appUser1.setIsInner(2);
        }else {
            appUser1.setIsInner(1);
        }
        appUserDao.update(appUser1);
        JSONObject reuslt = new JSONObject();
        reuslt.put("innerState", appUser1.getIsInner());
        return reuslt.toJSONString();
    }

    @RequestMapping("black")
    @ResponseBody
    public String black(String id) {
        //修改他黑名单
        AppUserBehavior appUserBehavior = appUserBehaviorService.get(id);
        String userSource = appUserBehavior.getUserSource();
        String userId = appUserBehavior.getUserId();
        try {
            if (userSource.equals("1")) {
                //c端用户
                AppUser appUser = appUserService.get(userId);
                Integer balck = appUser.getBalck();
                if (balck == 1) {
                    appUser.setBalck(0);
                } else {
                    appUser.setBalck(1);
                }
                appUserService.update(appUser);
            } else {
                //小程序
                BizWxUser byOpenid = bizWxUserService.findByOpenid(userId);
                Integer blacklist = byOpenid.getBlacklist();
                if (blacklist == 1) {
                    byOpenid.setBlacklist(0);
                } else {
                    byOpenid.setBlacklist(1);
                }
                bizWxUserService.update(byOpenid);
            }
            return renderResult(Global.TRUE, text("操作成功！"));
        } catch (Exception e) {
            e.printStackTrace();
            return renderResult(Global.FALSE, text("操作失败,请联系管理员！"));
        }
    }


}