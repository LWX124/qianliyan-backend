package com.stylefeng.guns.modular.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.modular.system.model.BizOpenFeedback;
import com.stylefeng.guns.modular.system.service.IBizOpenFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 开放平台反馈表控制器
 *
 * @author kosan
 * @Date 2019-03-20 22:36:09
 */
@Controller
@RequestMapping("/bizOpenFeedback")
public class BizOpenFeedbackController extends BaseController {

    private String PREFIX = "/system/bizOpenFeedback/";

    @Autowired
    private IBizOpenFeedbackService bizOpenFeedbackService;

    /**
     * 跳转到开放平台反馈表首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "bizOpenFeedback.html";
    }

    /**
     * 跳转到添加开放平台反馈表
     */
    @RequestMapping("/bizOpenFeedback_add")
    public String bizOpenFeedbackAdd() {
        return PREFIX + "bizOpenFeedback_add.html";
    }

    /**
     * 跳转到修改开放平台反馈表
     */
    @RequestMapping("/bizOpenFeedback_update/{bizOpenFeedbackId}")
    public String bizOpenFeedbackUpdate(@PathVariable Integer bizOpenFeedbackId, Model model) {
        BizOpenFeedback bizOpenFeedback = bizOpenFeedbackService.selectById(bizOpenFeedbackId);
        model.addAttribute("item", bizOpenFeedback);
        LogObjectHolder.me().set(bizOpenFeedback);
        return PREFIX + "bizOpenFeedback_edit.html";
    }

    /**
     * 获取开放平台反馈表列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        Wrapper<BizOpenFeedback> wrapper = new EntityWrapper<>();
        wrapper.orderBy("creTime", true);
        BizOpenFeedback bizOpenFeedback = new BizOpenFeedback();
        bizOpenFeedback.setAccount(condition);
        ((EntityWrapper<BizOpenFeedback>) wrapper).setEntity(bizOpenFeedback);
        return bizOpenFeedbackService.selectList(wrapper);
    }

    /**
     * 新增开放平台反馈表
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(BizOpenFeedback bizOpenFeedback) {
        bizOpenFeedbackService.insert(bizOpenFeedback);
        return SUCCESS_TIP;
    }

    /**
     * 删除开放平台反馈表
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer bizOpenFeedbackId) {
        bizOpenFeedbackService.deleteById(bizOpenFeedbackId);
        return SUCCESS_TIP;
    }

    /**
     * 修改开放平台反馈表
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(BizOpenFeedback bizOpenFeedback) {
        bizOpenFeedbackService.updateById(bizOpenFeedback);
        return SUCCESS_TIP;
    }

    /**
     * 开放平台反馈表详情
     */
    @RequestMapping(value = "/detail/{bizOpenFeedbackId}")
    @ResponseBody
    public Object detail(@PathVariable("bizOpenFeedbackId") Integer bizOpenFeedbackId) {
        return bizOpenFeedbackService.selectById(bizOpenFeedbackId);
    }
}
