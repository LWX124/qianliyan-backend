package com.stylefeng.guns.modular.system.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.annotion.Permission;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.BizWxSalary;
import com.stylefeng.guns.modular.system.service.IBizWxSalaryService;

/**
 * 控制器
 *
 * @author kosan
 * @Date 2018-08-23 09:57:10
 */
@Controller
@RequestMapping("/bizWxSalary")
public class BizWxSalaryController extends BaseController {

    private String PREFIX = "/system/bizWxSalary/";

    @Autowired
    private IBizWxSalaryService bizWxSalaryService;

    /**
     * 跳转到首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "bizWxSalary.html";
    }

    /**
     * 跳转到添加
     */
    @RequestMapping("/bizWxSalary_add")
    public String bizWxSalaryAdd() {
        return PREFIX + "bizWxSalary_add.html";
    }

    /**
     * 跳转到修改
     */
    @RequestMapping("/bizWxSalary_update/{bizWxSalaryId}")
    public String bizWxSalaryUpdate(@PathVariable Integer bizWxSalaryId, Model model) {
        BizWxSalary bizWxSalary = bizWxSalaryService.selectById(bizWxSalaryId);
        model.addAttribute("item",bizWxSalary);
        LogObjectHolder.me().set(bizWxSalary);
        return PREFIX + "bizWxSalary_edit.html";
    }

    /**
     * 获取列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return bizWxSalaryService.selectList(null);
    }

    /**
     * 新增
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(BizWxSalary bizWxSalary) {
        bizWxSalaryService.insert(bizWxSalary);
        return SUCCESS_TIP;
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer bizWxSalaryId) {
        bizWxSalaryService.deleteById(bizWxSalaryId);
        return SUCCESS_TIP;
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(BizWxSalary bizWxSalary) {
        bizWxSalaryService.updateById(bizWxSalary);
        return SUCCESS_TIP;
    }

    /**
     * 详情
     */
    @RequestMapping(value = "/detail/{bizWxSalaryId}")
    @ResponseBody
    public Object detail(@PathVariable("bizWxSalaryId") Integer bizWxSalaryId) {
        return bizWxSalaryService.selectById(bizWxSalaryId);
    }
}
