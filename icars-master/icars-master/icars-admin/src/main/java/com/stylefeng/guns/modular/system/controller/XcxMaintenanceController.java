package com.stylefeng.guns.modular.system.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.XcxMaintenance;
import com.stylefeng.guns.modular.system.service.IXcxMaintenanceService;
import org.springframework.web.util.HtmlUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 小程序维修表控制器
 *
 * @author kosan
 * @Date 2019-03-31 13:31:01
 */
@Controller
@RequestMapping("/xcxMaintenance")
public class XcxMaintenanceController extends BaseController {

    private String PREFIX = "/system/xcxMaintenance/";

    @Autowired
    private IXcxMaintenanceService xcxMaintenanceService;

    /**
     * 跳转到小程序维修表首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "xcxMaintenance.html";
    }

    /**
     * 跳转到添加小程序维修表
     */
    @RequestMapping("/xcxMaintenance_add")
    public String xcxMaintenanceAdd() {
        return PREFIX + "xcxMaintenance_add.html";
    }

    /**
     * 跳转到修改小程序维修表
     */
    @RequestMapping("/xcxMaintenance_update/{xcxMaintenanceId}")
    public String xcxMaintenanceUpdate(@PathVariable Integer xcxMaintenanceId, Model model) {
        XcxMaintenance xcxMaintenance = xcxMaintenanceService.selectById(xcxMaintenanceId);
        model.addAttribute("item", xcxMaintenance);
        LogObjectHolder.me().set(xcxMaintenance);
        return PREFIX + "xcxMaintenance_edit.html";
    }

    /**
     * 获取小程序维修表列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return xcxMaintenanceService.selectList(null);
    }

    /**
     * 新增小程序维修表
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(XcxMaintenance xcxMaintenance) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        xcxMaintenance.setCreateTime(LocalDateTime.now().format(formatter));
        xcxMaintenance.setUpdateTime(LocalDateTime.now().format(formatter));
        xcxMaintenanceService.insert(xcxMaintenance);
        return SUCCESS_TIP;
    }

    /**
     * 删除小程序维修表
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer xcxMaintenanceId) {
        xcxMaintenanceService.deleteById(xcxMaintenanceId);
        return SUCCESS_TIP;
    }

    /**
     * 修改小程序维修表
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(XcxMaintenance xcxMaintenance) {
        xcxMaintenanceService.updateById(xcxMaintenance);
        return SUCCESS_TIP;
    }

    /**
     * 小程序维修表详情
     */
    @RequestMapping(value = "/detail/{xcxMaintenanceId}")
    @ResponseBody
    public Object detail(@PathVariable("xcxMaintenanceId") Integer xcxMaintenanceId) {
        return xcxMaintenanceService.selectById(xcxMaintenanceId);
    }
}
