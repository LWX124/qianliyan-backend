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
import com.stylefeng.guns.modular.system.model.BizAccidLevel;
import com.stylefeng.guns.modular.system.service.IBizAccidLevelService;

/**
 * 控制器
 *
 * @author kosan
 * @Date 2018-09-14 19:11:39
 */
@Controller
@RequestMapping("/bizAccidLevel")
public class BizAccidLevelController extends BaseController {

    private String PREFIX = "/system/bizAccidLevel/";

    @Autowired
    private IBizAccidLevelService bizAccidLevelService;

    /**
     * 跳转到首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "bizAccidLevel.html";
    }

    /**
     * 跳转到添加
     */
    @RequestMapping("/bizAccidLevel_add")
    public String bizAccidLevelAdd() {
        return PREFIX + "bizAccidLevel_add.html";
    }

    /**
     * 跳转到修改
     */
    @RequestMapping("/bizAccidLevel_update/{bizAccidLevelId}")
    public String bizAccidLevelUpdate(@PathVariable Integer bizAccidLevelId, Model model) {
        BizAccidLevel bizAccidLevel = bizAccidLevelService.selectById(bizAccidLevelId);
        model.addAttribute("item",bizAccidLevel);
        LogObjectHolder.me().set(bizAccidLevel);
        return PREFIX + "bizAccidLevel_edit.html";
    }

    /**
     * 获取列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return bizAccidLevelService.selectList(null);
    }

    /**
     * 新增
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(BizAccidLevel bizAccidLevel) {
        bizAccidLevelService.insert(bizAccidLevel);
        return SUCCESS_TIP;
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer bizAccidLevelId) {
        bizAccidLevelService.deleteById(bizAccidLevelId);
        return SUCCESS_TIP;
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(BizAccidLevel bizAccidLevel) {
        bizAccidLevelService.updateById(bizAccidLevel);
        return SUCCESS_TIP;
    }

    /**
     * 详情
     */
    @RequestMapping(value = "/detail/{bizAccidLevelId}")
    @ResponseBody
    public Object detail(@PathVariable("bizAccidLevelId") Integer bizAccidLevelId) {
        return bizAccidLevelService.selectById(bizAccidLevelId);
    }
}
