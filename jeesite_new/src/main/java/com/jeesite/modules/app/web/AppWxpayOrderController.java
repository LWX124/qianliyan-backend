/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.web;

import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.app.dao.AppWxpayBackDao;
import com.jeesite.modules.app.dao.AppWxpayOrderDao;
import com.jeesite.modules.app.entity.AppWxpayBack;
import com.jeesite.modules.app.entity.AppWxpayOrder;
import com.jeesite.modules.app.service.AppWxpayOrderService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * app_wxpay_orderController
 *
 * @author dh
 * @version 2019-12-16
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appWxpayOrder")
public class AppWxpayOrderController extends BaseController {

    @Resource
    private AppWxpayOrderService appWxpayOrderService;

    @Resource
    private AppWxpayOrderDao appWxpayOrderDao;

    @Resource
    private AppWxpayBackDao appWxpayBackDao;

    @Resource
    private WxPayService wxPayService;

    /**
     * 获取数据
     */
    @ModelAttribute
    public AppWxpayOrder get(String outTradeNo, boolean isNewRecord) {
        return appWxpayOrderService.get(outTradeNo, isNewRecord);
    }

    /**
     * 查询列表
     */
    @RequiresPermissions("app:appWxpayOrder:view")
    @RequestMapping(value = {"list", ""})
    public String list(AppWxpayOrder appWxpayOrder, Model model) {
        model.addAttribute("appWxpayOrder", appWxpayOrder);
        return "modules/app/appWxpayOrderList";
    }

    /**
     * 查询列表数据
     */
    @RequiresPermissions("app:appWxpayOrder:view")
    @RequestMapping(value = "listData")
    @ResponseBody
    public Page<AppWxpayOrder> listData(AppWxpayOrder appWxpayOrder, HttpServletRequest request, HttpServletResponse response) {
        appWxpayOrder.setPage(new Page<>(request, response));
        Page<AppWxpayOrder> page = appWxpayOrderService.findPage(appWxpayOrder);
        return page;
    }

    /**
     * 查看编辑表单
     */
    @RequiresPermissions("app:appWxpayOrder:view")
    @RequestMapping(value = "form")
    public String form(AppWxpayOrder appWxpayOrder, Model model) {
        model.addAttribute("appWxpayOrder", appWxpayOrder);
        return "modules/app/appWxpayOrderForm";
    }

    /**
     * 退款
     */
    @RequiresPermissions("app:appWxpayOrder:view")
    @RequestMapping(value = "backMoney")
    @ResponseBody
    public String backMoney(AppWxpayOrder appWxpayOrder, Integer type, String businessId) {
        AppWxpayOrder appWxpayOrder1 = appWxpayOrderDao.get(appWxpayOrder);
        if (appWxpayOrder1 == null) {
            return renderResult(Global.TRUE, text("订单号有误！"));
        }
        if (!appWxpayOrder1.getStatus().equals("2")) {
            return renderResult(Global.TRUE, text("当前状态不能退款！"));
        }

        try {
            appWxpayOrderService.doBack(appWxpayOrder1, type, businessId);
        } catch (WxPayException e) {
            e.printStackTrace();
            return renderResult(Global.TRUE, text("退款失败！"));
        }
        return renderResult(Global.TRUE, text("发送请求成功！"));
    }


    /**
     * 保存app_wxpay_order
     */
    @RequiresPermissions("app:appWxpayOrder:edit")
    @PostMapping(value = "save")
    @ResponseBody
    public String save(@Validated AppWxpayOrder appWxpayOrder) {
        appWxpayOrderService.save(appWxpayOrder);
        return renderResult(Global.TRUE, text("保存app_wxpay_order成功！"));
    }

}
