package com.stylefeng.guns.modular.system.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.annotion.BussinessLog;
import com.stylefeng.guns.core.common.constant.dictmap.BizMaintainPackageDict;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.modular.system.constant.RepairePackageType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.BizMaintainPackage;
import com.stylefeng.guns.modular.system.service.IBizMaintainPackageService;

import java.util.*;

/**
 * 控制器
 *
 * @author kosan
 * @Date 2018-11-21 09:23:27
 */
@Controller
@RequestMapping("/bizMaintainPackage")
public class BizMaintainPackageController extends BaseController {

    private String PREFIX = "/system/bizMaintainPackage/";

    @Autowired
    private IBizMaintainPackageService bizMaintainPackageService;

    /**
     * 跳转到首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "bizMaintainPackage.html";
    }

    /**
     * 跳转到添加
     */
    @RequestMapping("/bizMaintainPackage_add")
    public String bizMaintainPackageAdd() {
        return PREFIX + "bizMaintainPackage_add.html";
    }

    /**
     * 跳转到修改
     */
    @RequestMapping("/bizMaintainPackage_update/{bizMaintainPackageId}")
    public String bizMaintainPackageUpdate(@PathVariable Integer bizMaintainPackageId, Model model) {
        BizMaintainPackage bizMaintainPackage = bizMaintainPackageService.selectById(bizMaintainPackageId);
        if(StringUtils.isNotEmpty(bizMaintainPackage.getDetail())){
            bizMaintainPackage.setDetailList(Arrays.asList(bizMaintainPackage.getDetail().split("#")));
        }
        model.addAttribute("item",bizMaintainPackage);
        LogObjectHolder.me().set(bizMaintainPackage);
        return PREFIX + "bizMaintainPackage_edit.html";
    }

    /**
     * 获取列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition, BizMaintainPackage param) {
        Page<BizMaintainPackage> page = new PageFactory<BizMaintainPackage>().defaultPage();
        param.setPackageType(RepairePackageType.INIT.getCode());
        List<BizMaintainPackage> list = bizMaintainPackageService.selectPage(page, param, condition);
        for(BizMaintainPackage bizMaintainPackage : list){
            if(StringUtils.isNotEmpty(bizMaintainPackage.getDetail())){
                bizMaintainPackage.setDetailList(Arrays.asList(bizMaintainPackage.getDetail().split("#")));
            }
            bizMaintainPackage.setPstatusName(bizMaintainPackage.getPstatus() == 0 ? "销售中" : "已下架");
        }
        page.setRecords(list);
        return super.packForBT(page);
    }

    /**
     * 新增
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(BizMaintainPackage bizMaintainPackage) {
        bizMaintainPackage.setCreateTime(new Date());
        bizMaintainPackage.setPstatus(0);
        bizMaintainPackage.setPackageType(RepairePackageType.INIT.getCode());
        bizMaintainPackageService.insert(bizMaintainPackage);
        return SUCCESS_TIP;
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer bizMaintainPackageId) {
        bizMaintainPackageService.deleteById(bizMaintainPackageId);
        return SUCCESS_TIP;
    }

    /**
     * 上架/下架
     */
    @RequestMapping(value = "/onShowOrOffShow")
    @ResponseBody
    public Object onShowOrOffShow(@RequestParam Integer bizMaintainPackageId) {
        BizMaintainPackage bizMaintainPackage = bizMaintainPackageService.selectById(bizMaintainPackageId);
        bizMaintainPackage.setPstatus(bizMaintainPackage.getPstatus() == 0 ? 1 : 0);
        bizMaintainPackageService.updateById(bizMaintainPackage);
        return SUCCESS_TIP;
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update")
    @BussinessLog(value = "修改保养套餐", key = "packageName", dict = BizMaintainPackageDict.class)
    @ResponseBody
    public Object update(BizMaintainPackage bizMaintainPackage) {
        bizMaintainPackageService.updateById(bizMaintainPackage);
        return SUCCESS_TIP;
    }

    /**
     * 详情
     */
    @RequestMapping(value = "/detail/{bizMaintainPackageId}")
    @ResponseBody
    public Object detail(@PathVariable("bizMaintainPackageId") Integer bizMaintainPackageId) {
        return bizMaintainPackageService.selectById(bizMaintainPackageId);
    }
}
