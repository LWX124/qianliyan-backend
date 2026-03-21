package com.stylefeng.guns.modular.system.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.annotion.Permission;
import com.stylefeng.guns.core.common.constant.Const;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.modular.system.model.Accident;
import com.stylefeng.guns.modular.system.warpper.AccdWarpper;
import com.stylefeng.guns.modular.system.warpper.AlipayActivityWarpper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.AlipayActivity;
import com.stylefeng.guns.modular.system.service.IAlipayActivityService;

import java.util.List;
import java.util.Map;

/**
 * 支付宝营销红包活动控制器
 *
 * @author kosan
 * @Date 2018-07-31 14:36:16
 */
@Controller
@RequestMapping("/alipayActivity")
public class AlipayActivityController extends BaseController {

    private String PREFIX = "/system/alipayActivity/";

    @Autowired
    private IAlipayActivityService alipayActivityService;

    /**
     * 跳转到支付宝营销红包活动首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "alipayActivity.html";
    }

    /**
     * 跳转到添加支付宝营销红包活动
     */
    @RequestMapping("/alipayActivity_add")
    public String alipayActivityAdd() {
        return PREFIX + "alipayActivity_add.html";
    }

    /**
     * 跳转到修改支付宝营销红包活动
     */
    @RequestMapping("/alipayActivity_update/{alipayActivityId}")
    public String alipayActivityUpdate(@PathVariable Integer alipayActivityId, Model model) {
        AlipayActivity alipayActivity = alipayActivityService.selectById(alipayActivityId);
        model.addAttribute("item",alipayActivity);
        LogObjectHolder.me().set(alipayActivity);
        return PREFIX + "alipayActivity_edit.html";
    }

    /**
     * 获取支付宝营销红包活动列表
     */
    @RequestMapping(value = "/list")
    @Permission({Const.ADMIN_NAME, Const.SUB_ADMIN_NAME})
    @ResponseBody
    public Object list(@RequestParam(required = true) String campStatus, @RequestParam(required = true) String startTime, @RequestParam(required = true) String endTime) {
        Page<AlipayActivity> page = new PageFactory<AlipayActivity>().defaultPage();
        List<Map<String, Object>> result = alipayActivityService.selectListForPage(page, startTime, endTime, page.getOrderByField(), page.isAsc());
//        List<Map<String, Object>> result = alipayActivityService.selectListForPageFromApi(page.getLimit(), page.getOffset()/page.getLimit(), campStatus);
        page.setRecords((List<AlipayActivity>) new AlipayActivityWarpper(result).warp());
        return super.packForBT(page);
    }

    /**
     * 新增支付宝营销红包活动
     */
    @RequestMapping(value = "/add")
    @Permission({Const.ADMIN_NAME, Const.SUB_ADMIN_NAME})
    @ResponseBody
    public Object add(AlipayActivity alipayActivity) {
        alipayActivityService.add(alipayActivity);
        return SUCCESS_TIP;
    }

    /**
     * 删除支付宝营销红包活动
     */
    @RequestMapping(value = "/delete")
    @Permission({Const.ADMIN_NAME, Const.SUB_ADMIN_NAME})
    @ResponseBody
    public Object delete(@RequestParam Integer alipayActivityId) {
        alipayActivityService.deleteById(alipayActivityId);
        return SUCCESS_TIP;
    }

    /**
     * 修改支付宝营销红包活动
     */
    @RequestMapping(value = "/update")
    @Permission({Const.ADMIN_NAME, Const.SUB_ADMIN_NAME})
    @ResponseBody
    public Object update(AlipayActivity alipayActivity) {
        alipayActivityService.updateById(alipayActivity);
        return SUCCESS_TIP;
    }
}
