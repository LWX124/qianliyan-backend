package com.stylefeng.guns.modular.system.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.modular.system.model.Accident;
import com.stylefeng.guns.modular.system.warpper.AccdWarpper;
import com.stylefeng.guns.modular.system.warpper.BizWxPayOrderWarpper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.BizWxpayOrder;
import com.stylefeng.guns.modular.system.service.IBizWxpayOrderService;

import java.util.List;
import java.util.Map;

/**
 * 控制器
 *
 * @author kosan
 * @Date 2018-09-10 14:41:03
 */
@Controller
@RequestMapping("/bizWxpayOrder")
public class BizWxpayOrderController extends BaseController {

    private String PREFIX = "/system/bizWxpayOrder/";

    @Autowired
    private IBizWxpayOrderService bizWxpayOrderService;

    /**
     * 跳转到首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "bizWxpayOrder.html";
    }

    /**
     * 跳转到添加
     */
    @RequestMapping("/bizWxpayOrder_add")
    public String bizWxpayOrderAdd() {
        return PREFIX + "bizWxpayOrder_add.html";
    }

    /**
     * 跳转到修改
     */
    @RequestMapping("/bizWxpayOrder_update/{bizWxpayOrderId}")
    public String bizWxpayOrderUpdate(@PathVariable Integer bizWxpayOrderId, Model model) {
        BizWxpayOrder bizWxpayOrder = bizWxpayOrderService.selectById(bizWxpayOrderId);
        model.addAttribute("item",bizWxpayOrder);
        LogObjectHolder.me().set(bizWxpayOrder);
        return PREFIX + "bizWxpayOrder_edit.html";
    }

    /**
     * 获取列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam("condition") String condition, @RequestParam("outTradeNo") String outTradeNo, @RequestParam("status") Integer status, @RequestParam(required = false) String createStartTime, @RequestParam(required = false) String createEndTime) {
        Page<BizWxpayOrder> page = new PageFactory<BizWxpayOrder>().defaultPage();
        List<Map<String, Object>> result = bizWxpayOrderService.selectList(page, null, condition, outTradeNo, createStartTime, createEndTime, status, page.getOrderByField(), page.isAsc());
        page.setRecords((List<BizWxpayOrder>) new BizWxPayOrderWarpper(result).warp());
        return super.packForBT(page);
    }

    /**
     * 新增
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(BizWxpayOrder bizWxpayOrder) {
        bizWxpayOrderService.insert(bizWxpayOrder);
        return SUCCESS_TIP;
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer bizWxpayOrderId) {
        bizWxpayOrderService.deleteById(bizWxpayOrderId);
        return SUCCESS_TIP;
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(BizWxpayOrder bizWxpayOrder) {
        bizWxpayOrderService.updateById(bizWxpayOrder);
        return SUCCESS_TIP;
    }

    /**
     * 详情
     */
    @RequestMapping(value = "/detail/{bizWxpayOrderId}")
    @ResponseBody
    public Object detail(@PathVariable("bizWxpayOrderId") Integer bizWxpayOrderId) {
        return bizWxpayOrderService.selectById(bizWxpayOrderId);
    }
}
