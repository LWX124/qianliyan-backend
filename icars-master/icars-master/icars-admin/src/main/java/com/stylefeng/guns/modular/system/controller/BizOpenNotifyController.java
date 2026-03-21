package com.stylefeng.guns.modular.system.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.BizOpenNotify;
import com.stylefeng.guns.modular.system.service.IBizOpenNotifyService;

/**
 * 通知消息表控制器
 *
 * @author kosan
 * @Date 2019-03-22 09:15:19
 */
@Controller
@RequestMapping("/bizOpenNotify")
public class BizOpenNotifyController extends BaseController {

    private String PREFIX = "/system/bizOpenNotify/";

    @Autowired
    private IBizOpenNotifyService bizOpenNotifyService;

    /**
     * 跳转到通知消息表首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "bizOpenNotify.html";
    }

    /**
     * 跳转到添加通知消息表
     */
    @RequestMapping("/bizOpenNotify_add")
    public String bizOpenNotifyAdd() {
        return PREFIX + "bizOpenNotify_add.html";
    }

    /**
     * 跳转到修改通知消息表
     */
    @RequestMapping("/bizOpenNotify_update/{bizOpenNotifyId}")
    public String bizOpenNotifyUpdate(@PathVariable Integer bizOpenNotifyId, Model model) {
        BizOpenNotify bizOpenNotify = bizOpenNotifyService.selectById(bizOpenNotifyId);
        model.addAttribute("item",bizOpenNotify);
        LogObjectHolder.me().set(bizOpenNotify);
        return PREFIX + "bizOpenNotify_edit.html";
    }

    /**
     * 获取通知消息表列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return bizOpenNotifyService.selectList(null);
    }

    /**
     * 新增通知消息表
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(BizOpenNotify bizOpenNotify) {
        bizOpenNotifyService.insert(bizOpenNotify);
        return SUCCESS_TIP;
    }

    /**
     * 删除通知消息表
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer bizOpenNotifyId) {
        bizOpenNotifyService.deleteById(bizOpenNotifyId);
        return SUCCESS_TIP;
    }

    /**
     * 修改通知消息表
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(BizOpenNotify bizOpenNotify) {
        bizOpenNotifyService.updateById(bizOpenNotify);
        return SUCCESS_TIP;
    }

    /**
     * 通知消息表详情
     */
    @RequestMapping(value = "/detail/{bizOpenNotifyId}")
    @ResponseBody
    public Object detail(@PathVariable("bizOpenNotifyId") Integer bizOpenNotifyId) {
        return bizOpenNotifyService.selectById(bizOpenNotifyId);
    }
}
