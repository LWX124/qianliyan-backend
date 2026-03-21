/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.modules.app.entity.*;
import com.jeesite.modules.app.service.AppLableDetailsReviewTreeService;
import lombok.SneakyThrows;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.app.service.AppBusinessConfirmService;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * 商户业务确认表Controller
 *
 * @author zcq
 * @version 2020-07-10
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appBusinessConfirm")
public class AppBusinessConfirmController extends BaseController {

    @Autowired
    private AppBusinessConfirmService appBusinessConfirmService;

    @Resource
    private AppLableDetailsReviewTreeService appLableDetailsReviewTreeService;


    /**
     * 获取数据
     */
    @ModelAttribute
    public AppBusinessConfirm get(Long id, boolean isNewRecord) {
        return appBusinessConfirmService.get(String.valueOf(id), isNewRecord);
    }

    /**
     * 查询列表
     */
    @RequiresPermissions("app:appBusinessConfirm:view")
    @RequestMapping(value = {"list", ""})
    public String list(AppBusinessConfirm appBusinessConfirm, Model model) {
        model.addAttribute("appBusinessConfirm", appBusinessConfirm);
        return "modules/app/appBusinessConfirmList";
    }

    /**
     * 查询列表数据
     */
    @RequiresPermissions("app:appBusinessConfirm:view")
    @RequestMapping(value = "listData")
    @ResponseBody
    public Page<AppBusinessConfirm> listData(AppBusinessConfirm appBusinessConfirm, HttpServletRequest request, HttpServletResponse response) {
        appBusinessConfirm.setPage(new Page<>(request, response));
        Page<AppBusinessConfirm> page = appBusinessConfirmService.findPage(appBusinessConfirm);
        return page;
    }

    /**
     * 查看编辑表单
     */
    @RequiresPermissions("app:appBusinessConfirm:view")
    @RequestMapping(value = "form")
    public String form(AppBusinessConfirm appBusinessConfirm, Model model) {
        model.addAttribute("appBusinessConfirm", appBusinessConfirm);
        return "modules/app/appBusinessConfirmForm";
    }

    /**
     * 保存商户业务确认表
     */
    @RequiresPermissions("app:appBusinessConfirm:edit")
    @PostMapping(value = "save")
    @ResponseBody
    public String save(@Validated AppBusinessConfirm appBusinessConfirm) {
        appBusinessConfirmService.save(appBusinessConfirm);
        return renderResult(Global.TRUE, text("保存商户业务确认表成功！"));
    }

    /**
     * 删除商户业务确认表
     */
    @RequiresPermissions("app:appBusinessConfirm:edit")
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(AppBusinessConfirm appBusinessConfirm) {
        appBusinessConfirmService.delete(appBusinessConfirm);
        return renderResult(Global.TRUE, text("删除商户业务确认表成功！"));
    }

    @RequestMapping("/addBusinessList")
    public ModelAndView addBusineeList(String userBId) {

        //查询到对应比例，只要比例
        //传一个对象
        ModelAndView mv = new ModelAndView("modules/app/addBusinessList");
        List<AppBusinessConfirm> list = appBusinessConfirmService.selectByUserBId(userBId);
        if (!list.isEmpty()) {
            for (AppBusinessConfirm appBusinessConfirm : list) {
                String accidentReponsibility = appBusinessConfirm.getAccidentReponsibility();
                if (accidentReponsibility.equals("全责")) {
                    mv.addObject("firstCon", appBusinessConfirm.getCustomersHave());
                    mv.addObject("secondCon", appBusinessConfirm.getNotUnitedCustomer());
                    mv.addObject("thirdCon", appBusinessConfirm.getCharterShop());
                } else if (accidentReponsibility.equals("三责")) {
                    mv.addObject("fourthCon", appBusinessConfirm.getCustomersHave());
                    mv.addObject("fifthCon", appBusinessConfirm.getNotUnitedCustomer());
                    mv.addObject("sixthCon", appBusinessConfirm.getCharterShop());
                } else {
                    mv.addObject("seventhCon", appBusinessConfirm.getCustomersHave());
                    mv.addObject("eighthCon", appBusinessConfirm.getNotUnitedCustomer());
                    mv.addObject("ninthCon", appBusinessConfirm.getCharterShop());
                }
            }
            mv.addObject("ashes", 1);
            mv.addObject("userBId", userBId);
        } else {

            mv.addObject("firstCon", 0);
            mv.addObject("secondCon", 0);
            mv.addObject("thirdCon", 0);

            mv.addObject("fourthCon", 0);
            mv.addObject("fifthCon", 0);
            mv.addObject("sixthCon", 0);

            mv.addObject("seventhCon", 0);
            mv.addObject("eighthCon", 0);
            mv.addObject("ninthCon", 0);

            mv.addObject("userBId", userBId);
            mv.addObject("ashes", 2);
        }
        return mv;
    }

    @RequestMapping("/replaceImg")
    public ModelAndView replaceImg(String imgSrc,String carId) {
        ModelAndView mv = new ModelAndView("modules/app/replaceImgList");
        mv.addObject("imgSrc",imgSrc);
        mv.addObject("carId",carId);
        return mv;
    }

    @RequestMapping("/addAddDayList")
    public ModelAndView addAddDayList(String upId) {
        ModelAndView mv = new ModelAndView("modules/app/addAddDayList");
        mv.addObject("upId",upId);
        return mv;
    }


    @RequestMapping(value = "/saveBusinessCon", method = RequestMethod.POST)
    public JSONObject saveBusinessCon(@RequestBody BusincessConfiList field) {
        JSONObject result = new JSONObject();
        //遍历添加数据到新表种
        //新增项目
        Integer userBId = field.getUserBId();
        AppLableDetailsReviewTree appLableDetailsReviewTree = appLableDetailsReviewTreeService.selectLable(field.getUserBId(), 9, 1);
        if (appLableDetailsReviewTree == null) {
            appLableDetailsReviewTreeService.addFirst(field.getUserBId());
            //判断是否添加过服务项目
            appLableDetailsReviewTreeService.addRescueSecond(field.getUserBId(), 9, null);
        } else {
            appLableDetailsReviewTree.setState(String.valueOf(1));
            appLableDetailsReviewTreeService.update(appLableDetailsReviewTree);
        }
        //修改另一张表的数据
        List<AppBusinessConfirm> list = appBusinessConfirmService.selectByUserBId(userBId.toString());
        if (list.isEmpty()) {
            //新增三条数据
            AppBusinessConfirm appBusinessConfirm1 = new AppBusinessConfirm();
            appBusinessConfirm1.setBusinessConfirm("店内保险");
            appBusinessConfirm1.setAccidentReponsibility("全责");
            appBusinessConfirm1.setCustomersHave(field.getTitle());
            appBusinessConfirm1.setNotUnitedCustomer(field.getTitle2());
            appBusinessConfirm1.setCharterShop(field.getTitle3());
            appBusinessConfirm1.setUserBId(userBId);
            appBusinessConfirm1.setCreateTime(new Date());
            appBusinessConfirm1.setUpdateTime(new Date());
            appBusinessConfirmService.insertCus(appBusinessConfirm1);

            AppBusinessConfirm appBusinessConfirm2 = new AppBusinessConfirm();
            appBusinessConfirm2.setBusinessConfirm("店内保险");
            appBusinessConfirm2.setAccidentReponsibility("三责");
            appBusinessConfirm2.setCustomersHave(field.getTitle4());
            appBusinessConfirm2.setNotUnitedCustomer(field.getTitle5());
            appBusinessConfirm2.setCharterShop(field.getTitle6());
            appBusinessConfirm2.setUserBId(userBId);
            appBusinessConfirm2.setCreateTime(new Date());
            appBusinessConfirm2.setUpdateTime(new Date());
            appBusinessConfirmService.insertCus(appBusinessConfirm2);

            AppBusinessConfirm appBusinessConfirm3 = new AppBusinessConfirm();
            appBusinessConfirm3.setBusinessConfirm("店外保险");
            appBusinessConfirm3.setAccidentReponsibility("包含");
            appBusinessConfirm3.setCustomersHave(field.getTitle7());
            appBusinessConfirm3.setNotUnitedCustomer(field.getTitle8());
            appBusinessConfirm3.setCharterShop(field.getTitle9());
            appBusinessConfirm3.setUserBId(userBId);
            appBusinessConfirm3.setCreateTime(new Date());
            appBusinessConfirm3.setUpdateTime(new Date());
            appBusinessConfirmService.insertCus(appBusinessConfirm3);

        } else {
            for (AppBusinessConfirm appBusinessConfirm : list) {
                String accidentReponsibility = appBusinessConfirm.getAccidentReponsibility();
                if (accidentReponsibility.equals("全责")) {
                    appBusinessConfirm.setCustomersHave(field.getTitle());
                    appBusinessConfirm.setNotUnitedCustomer(field.getTitle2());
                    appBusinessConfirm.setCharterShop(field.getTitle3());
                } else if (accidentReponsibility.equals("三责")) {
                    appBusinessConfirm.setCustomersHave(field.getTitle4());
                    appBusinessConfirm.setNotUnitedCustomer(field.getTitle5());
                    appBusinessConfirm.setCharterShop(field.getTitle6());
                } else {
                    appBusinessConfirm.setCustomersHave(field.getTitle7());
                    appBusinessConfirm.setNotUnitedCustomer(field.getTitle8());
                    appBusinessConfirm.setCharterShop(field.getTitle9());
                }
                appBusinessConfirmService.update(appBusinessConfirm);
            }
        }
        return result;
    }
}