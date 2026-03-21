/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.web;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.app.dao.AppAuctionBidLogDao;
import com.jeesite.modules.app.dao.AppUserDao;
import com.jeesite.modules.app.entity.*;
import com.jeesite.modules.app.service.AppAuctionImgService;
import com.jeesite.modules.app.service.AppAuctionService;
import com.jeesite.modules.app.service.AppAuctionUpService;
import com.jeesite.modules.app.service.FileUploadServiceExtendImpl;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 拍卖车上架信息表Controller
 *
 * @author y
 * @version 2022-10-27
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appAuctionUp")
public class AppAuctionUpController extends BaseController {

    @Autowired
    private AppAuctionUpService appAuctionUpService;

    @Autowired
    private AppAuctionService appAuctionService;

    @Autowired
    private AppAuctionBidLogDao appAuctionBidLogDao;

    @Autowired
    private AppUserDao appUserDao;

    @Resource
    private FileUploadServiceExtendImpl fileUploadServiceExtend;

    @Resource
    private AppAuctionImgService appAuctionImgService;


    /**
     * 获取数据
     */
    @ModelAttribute
    public AppAuctionUp get(Long id, boolean isNewRecord) {
        return appAuctionUpService.get(String.valueOf(id), isNewRecord);
    }

    /**
     * 查询列表
     */
    @RequiresPermissions("app:appAuctionUp:view")
    @RequestMapping(value = {"list", ""})
    public String list(AppAuctionUp appAuctionUp, Model model) {
        model.addAttribute("appAuctionUp", appAuctionUp);
        return "modules/app/appAuctionUpList";
    }

    @RequestMapping("/checkAppAuctionState")
    public ModelAndView checkAppAuctionState(String carId) {
        ModelAndView mv = new ModelAndView("modules/app/checkAppAuctionState");
        mv.addObject("carId", carId);
        mv.addObject("carState", 7);
        return mv;
    }


    /**
     * 查询列表数据
     */
    @RequiresPermissions("app:appAuctionUp:view")
    @RequestMapping(value = "listData")
    @ResponseBody
    public Page<AppAuctionUp> listData(AppAuctionUp appAuctionUp, HttpServletRequest request, HttpServletResponse response) {
        appAuctionUp.setPage(new Page<>(request, response));

        if (appAuctionUp.getTimeFlag() != null && appAuctionUp.getTimeFlag() == 1) {
            appAuctionUp.getSqlMap().getWhere().and("begin_time", QueryType.LTE, new Date()).and("end_time", QueryType.GTE, new Date());
        }

        Page<AppAuctionUp> page = appAuctionUpService.findPage(appAuctionUp);

        page.getList().stream().forEach(x -> {
            AppAuctionBidLog appAuctionBidLog = appAuctionBidLogDao.selectMustOneByCarId(x.getCarId());
            if (appAuctionBidLog != null) {
                AppUser appUser = appUserDao.findUserById(appAuctionBidLog.getUserId());
                x.setBidInfo("最高出价：" + appUser.getName() + "," + appUser.getUsername() + " 出价:" + appAuctionBidLog.getBid());
            } else {
                x.setBidInfo("暂无人出价");
            }
        });

        return page;
    }

    /**
     * 查看编辑表单
     */
    @RequiresPermissions("app:appAuctionUp:view")
    @RequestMapping(value = "form")
    public String form(AppAuctionUp appAuctionUp, Model model) {
        model.addAttribute("appAuctionUp", appAuctionUp);
        JSONObject objs = appAuctionUpService.showImgs(appAuctionUp.getCarId());
        if (Objects.nonNull(objs)) {
            String carImg = objs.getString("carImg");
            if (StringUtils.isNotBlank(carImg)) {
                model.addAttribute("carImg", carImg);
            } else {
                model.addAttribute("carImg", null);
            }

            String newDriveImg = objs.getString("newDriveImg");
            if (StringUtils.isNotBlank(newDriveImg)) {
                model.addAttribute("newDriveImg", newDriveImg);
            } else {
                model.addAttribute("newDriveImg", null);
            }

            String carInvoiceImg = objs.getString("carInvoiceImg");
            if (StringUtils.isNotBlank(carInvoiceImg)) {
                model.addAttribute("carInvoiceImg", carInvoiceImg);
            } else {
                model.addAttribute("carInvoiceImg", null);
            }

            String registrationImg = objs.getString("registrationImg");
            if (StringUtils.isNotBlank(registrationImg)) {
                model.addAttribute("registrationImg", registrationImg);
            } else {
                model.addAttribute("registrationImg", null);
            }

            String insImg = objs.getString("insImg");
            if (StringUtils.isNotBlank(insImg)) {
                model.addAttribute("insImg", insImg);
            } else {
                model.addAttribute("insImg", null);
            }

            String driveImg = objs.getString("driveImg");
            if (StringUtils.isNotBlank(driveImg)) {
                model.addAttribute("driveImg", driveImg);
            } else {
                model.addAttribute("driveImg", null);
            }

            String regImg = objs.getString("regImg");
            if (StringUtils.isNotBlank(regImg)) {
                model.addAttribute("regImg", regImg);
            } else {
                model.addAttribute("regImg", null);
            }

            String beforeRepair = objs.getString("beforeRepair");
            if (StringUtils.isNotBlank(beforeRepair)) {
                model.addAttribute("beforeRepair", beforeRepair);
            } else {
                model.addAttribute("beforeRepair", null);
            }

            String afterRepair = objs.getString("afterRepair");
            if (StringUtils.isNotBlank(afterRepair)) {
                model.addAttribute("afterRepair", afterRepair);
            } else {
                model.addAttribute("afterRepair", null);
            }
        } else {
            model.addAttribute("carImg", null);
            model.addAttribute("insImg", null);
            model.addAttribute("driveImg", null);
            model.addAttribute("regImg", null);
            model.addAttribute("newDriveImg", null);
            model.addAttribute("registrationImg", null);
            model.addAttribute("carInvoiceImg", null);
            model.addAttribute("beforeRepair", null);
            model.addAttribute("afterRepair", null);
        }

        AppAuction appAuction = appAuctionService.get(appAuctionUp.getCarId().toString());

        AppAuctionBidLog appAuctionBidLog = appAuctionBidLogDao.selectMustOneByCarId(appAuctionUp.getCarId());
        if (appAuctionBidLog != null) {
            AppUser appUser = appUserDao.findUserById(appAuctionBidLog.getUserId());
            model.addAttribute("bidLog", "最高出价：" + appUser.getName() + "," + appUser.getUsername() + " 出价:" + appAuctionBidLog.getBid());
        } else {
            model.addAttribute("bidLog", "暂无人出价");
        }

        model.addAttribute("appAuction", appAuction);
        return "modules/app/appAuctionUpForm";
    }

    /**
     * 保存拍卖车上架信息表
     */
    @RequiresPermissions("app:appAuctionUp:edit")
    @PostMapping(value = "save")
    @ResponseBody
    public String save(@Validated AppAuctionUp appAuctionUp) {
        try {
            appAuctionUpService.save(appAuctionUp);
            return renderResult(Global.TRUE, text("保存拍卖车上架信息表成功！"));
        } catch (Exception e) {
            e.printStackTrace();
            return renderResult(Global.FALSE, text(e.getMessage()));
        }
    }


    @RequestMapping(value = "/saveEndTime", method = RequestMethod.GET)
    public JSONObject saveEndTime(Integer title, String upId) {
        JSONObject result = new JSONObject();
        AppAuctionUp appAuctionUp = appAuctionUpService.get(upId);
        if (appAuctionUp == null || appAuctionUp.getEndTime() == null) {
            result.put("code", 401);
            result.put("message", "无法修改！");
            logger.info("###  saveEndTime  无法修改 upId={}", upId);
            return result;
        }
        //判断结束时间大于十分钟
        Date now = new Date();
        long diff = appAuctionUp.getEndTime().getTime() - now.getTime();
        if (diff <= 1000 * 60) {
            result.put("code", 401);
            result.put("message", "已结束或者即将结束无法增加时间！");
            return result;
        }

        //增加天数
        Calendar ca = Calendar.getInstance();
        ca.setTime(appAuctionUp.getEndTime());
        ca.add(Calendar.DATE, title);// num为增加的天数
        Date newEndTime = ca.getTime();
        appAuctionUp.setEndTime(newEndTime);
        appAuctionUp.setUpDateNum(appAuctionUp.getUpDateNum() + title);
        appAuctionUpService.update(appAuctionUp);
        result.put("code", 200);
        result.put("message", "成功！");
        return result;

    }

    //上传图片
    @ResponseBody
    @RequestMapping("uploadReplace")
    public JSONObject uploadReplace(MultipartFile file, String imgSrc, String carId, HttpServletRequest request) throws IOException {
        String fileName = UUID.randomUUID().toString();
        String s1 = fileName.replaceAll("-", "");
        String newSrc = fileUploadServiceExtend.uploadWebimg(file, s1 + ".jpg");
        newSrc = newSrc + "?x-image-process=style/style-c2b2";
        //查询图片
        ArrayList<AppAuctionImg> allImgs = appAuctionImgService.findAllImgs(carId);
        String finalNewSrc = newSrc;
        allImgs.forEach(x -> {
            String url = x.getUrl();
            String replace = url.replace("%2F", "/");
            if (replace.contains("?x-image")) {
                String[] split = replace.split("\\?x-image");
                replace = split[0];
            }
            if (imgSrc.equals(replace)) {
                appAuctionUpService.updateImgOne(finalNewSrc, x.getId());
            }
        });


        JSONObject result = new JSONObject();
        result.put("code", 0);
        result.put("message", "成功！");
        return result;
    }


}