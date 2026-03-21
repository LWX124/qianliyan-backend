package com.stylefeng.guns.modular.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.modular.system.constant.MaintainOrderStatus;
import com.stylefeng.guns.modular.system.constant.RepairePackageType;
import com.stylefeng.guns.modular.system.model.BizMaintainOrderPushrecord;
import com.stylefeng.guns.modular.system.service.IBizMaintainOrderPushrecordService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.BizMaintainPackageOrder;
import com.stylefeng.guns.modular.system.service.IBizMaintainPackageOrderService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 控制器
 *
 * @author kosan
 * @Date 2018-11-21 15:22:11
 */
@Controller
@RequestMapping("/bizMaintainPackageOrder")
public class BizMaintainPackageOrderController extends BaseController {

    private String PREFIX = "/system/bizMaintainPackageOrder/";

    @Autowired
    private IBizMaintainPackageOrderService bizMaintainPackageOrderService;

    @Autowired
    private IBizMaintainOrderPushrecordService bizMaintainOrderPushrecordService;

    /**
     * 跳转到首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "bizMaintainPackageOrder.html";
    }

    /**
     * 跳转到推送理赔顾问页面
     */
    @RequestMapping("/order_push")
    public String toOrderPushPage() {
        return PREFIX + "maintain_order_push.html";
    }

    /**
     * 跳转到添加
     */
    @RequestMapping("/bizMaintainPackageOrder_add")
    public String bizMaintainPackageOrderAdd() {
        return PREFIX + "bizMaintainPackageOrder_add.html";
    }

    /**
     * 跳转到修改
     */
    @RequestMapping("/bizMaintainPackageOrder_update/{bizMaintainPackageOrderId}")
    public String bizMaintainPackageOrderUpdate(@PathVariable Integer bizMaintainPackageOrderId, Model model) {
        BizMaintainPackageOrder bizMaintainPackageOrder = bizMaintainPackageOrderService.selectById(bizMaintainPackageOrderId);
        model.addAttribute("item",bizMaintainPackageOrder);
        LogObjectHolder.me().set(bizMaintainPackageOrder);
        return PREFIX + "bizMaintainPackageOrder_edit.html";
    }

    /**
     * 获取列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition, BizMaintainPackageOrder param) {
        Page<BizMaintainPackageOrder> page = new PageFactory<BizMaintainPackageOrder>().defaultPage();
        param.setPackageType(RepairePackageType.INIT.getCode());
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
     * 保养单 确认接车
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
     * 保养单 取消
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
     * 保养单 完成
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
    @RequestMapping(value = "/detail/{bizMaintainPackageOrderId}")
    @ResponseBody
    public Object detail(@PathVariable("bizMaintainPackageOrderId") Integer bizMaintainPackageOrderId) {
        return bizMaintainPackageOrderService.selectById(bizMaintainPackageOrderId);
    }
}
