package com.stylefeng.guns.modular.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.modular.system.constant.MaintainOrderStatus;
import com.stylefeng.guns.modular.system.constant.RepairePackageType;
import com.stylefeng.guns.modular.system.model.BizMaintainOrderPushrecord;
import com.stylefeng.guns.modular.system.model.BizMaintainPackageOrder;
import com.stylefeng.guns.modular.system.service.IBizMaintainOrderPushrecordService;
import com.stylefeng.guns.modular.system.service.IBizMaintainPackageOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * 控制器
 *
 * @author kosan
 * @Date 2018-11-21 15:22:11
 */
@Controller
@RequestMapping("/bizRepairePackageOrder")
public class BizRepairePackageOrderController extends BaseController {

    private String PREFIX = "/system/bizRepairePackageOrder/";

    @Autowired
    private IBizMaintainPackageOrderService bizMaintainPackageOrderService;

    @Autowired
    private IBizMaintainOrderPushrecordService bizMaintainOrderPushrecordService;

    /**
     * 跳转到首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "bizRepairePackageOrder.html";
    }

    /**
     * 跳转到推送理赔顾问页面
     */
    @RequestMapping("/order_push")
    public String toOrderPushPage() {
        return PREFIX + "repaire_order_push.html";
    }

    /**
     * 跳转到添加
     */
    @RequestMapping("/bizRepairePackageOrder_add")
    public String bizRepairePackageOrderAdd() {
        return PREFIX + "bizRepairePackageOrder_add.html";
    }

    /**
     * 跳转到修改
     */
    @RequestMapping("/bizRepairePackageOrder_update/{bizRepairePackageOrderId}")
    public String bizRepairePackageOrderUpdate(@PathVariable Integer bizRepairePackageOrderId, Model model) {
        BizMaintainPackageOrder bizMaintainPackageOrder = bizMaintainPackageOrderService.selectById(bizRepairePackageOrderId);
        model.addAttribute("item",bizMaintainPackageOrder);
        LogObjectHolder.me().set(bizMaintainPackageOrder);
        return PREFIX + "bizRepairePackageOrder_edit.html";
    }

    /**
     * 获取列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition, BizMaintainPackageOrder param) {
        Page<BizMaintainPackageOrder> page = new PageFactory<BizMaintainPackageOrder>().defaultPage();
        param.setPackageType(RepairePackageType.REPAIRE_PACKAGE.getCode());
        List<BizMaintainPackageOrder> list = bizMaintainPackageOrderService.selectPage(page, param, condition);
        for(BizMaintainPackageOrder order : list){
            order.setOrderStatus(MaintainOrderStatus.valueOf(order.getOrderStatus()).getMessage());
            order.setMapUrl("https://apis.map.qq.com/uri/v1/geocoder?coord="+order.getLat()+","+order.getLng()+"&coord_type=1&refere=myapp&coord_type=1");
        }
        page.setRecords(list);
        return super.packForBT(page);
    }

    /**
     * 新增
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(BizMaintainPackageOrder bizMaintainPackageOrder) {
        bizMaintainPackageOrderService.insert(bizMaintainPackageOrder);
        return SUCCESS_TIP;
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer bizMaintainPackageOrderId) {
        bizMaintainPackageOrderService.deleteById(bizMaintainPackageOrderId);
        return SUCCESS_TIP;
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(BizMaintainPackageOrder bizMaintainPackageOrder) {
        bizMaintainPackageOrderService.updateById(bizMaintainPackageOrder);
        return SUCCESS_TIP;
    }

    /**
     * 维修单 确认接车
     */
    @RequestMapping(value = "/order/accept")
    @ResponseBody
    public Object accept(@RequestParam(required = true) Integer orderid) {
        BizMaintainPackageOrder order = new BizMaintainPackageOrder();
        order.setId(orderid);
        order.setOrderStatus(MaintainOrderStatus.RECEPT.toString());
        order.setModifyTime(new Date());
        bizMaintainPackageOrderService.updateById(order);
        return SUCCESS_TIP;
    }

    /**
     * 维修单 取消
     */
    @RequestMapping(value = "/order/cancel")
    @ResponseBody
    public Object cancel(@RequestParam(required = true) Integer orderid) {
        BizMaintainPackageOrder order = new BizMaintainPackageOrder();
        order.setId(orderid);
        order.setOrderStatus(MaintainOrderStatus.CANCELED.toString());
        order.setModifyTime(new Date());
        bizMaintainPackageOrderService.updateById(order);
        return SUCCESS_TIP;
    }

    /**
     * 维修单 完成
     */
    @RequestMapping(value = "/order/finish")
    @ResponseBody
    public Object finish(@RequestParam(required = true) Integer orderid) {
        BizMaintainPackageOrder order = new BizMaintainPackageOrder();
        order.setId(orderid);
        order.setOrderStatus(MaintainOrderStatus.FINISH.toString());
        order.setModifyTime(new Date());
        bizMaintainPackageOrderService.updateById(order);
        return SUCCESS_TIP;
    }

    /**
     * 推送修理工
     */
    @RequestMapping(value = "/pushRepaireman")
    @ResponseBody
    public Object pushRepaireman(@RequestParam(required = true)Integer orderid, @RequestParam(required = true)String account) {
        BizMaintainOrderPushrecord var1 = bizMaintainOrderPushrecordService.selectOne(new EntityWrapper<BizMaintainOrderPushrecord>().eq("maintain_order_id", orderid));
        BizMaintainOrderPushrecord pushrecord = new BizMaintainOrderPushrecord();
        if(var1 == null){
            pushrecord.setAccount(account);
            pushrecord.setMaintainOrderId(orderid);
            pushrecord.setCreateTime(new Date());
            bizMaintainOrderPushrecordService.insert(pushrecord);
        } else {
            pushrecord.setId(var1.getId());
            pushrecord.setCreateTime(new Date());
            pushrecord.setAccount(account);
            bizMaintainOrderPushrecordService.updateById(pushrecord);
        }
        return SUCCESS_TIP;
    }

    /**
     * 详情
     */
    @RequestMapping(value = "/detail/{bizRepairePackageOrderId}")
    @ResponseBody
    public Object detail(@PathVariable("bizRepairePackageOrderId") Integer bizRepairePackageOrderId) {
        return bizMaintainPackageOrderService.selectById(bizRepairePackageOrderId);
    }
}
