package com.stylefeng.guns.modular.system.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.node.ZTreeNode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.BizInsuranceCompany;
import com.stylefeng.guns.modular.system.service.IBizInsuranceCompanyService;

import java.util.List;

/**
 * 控制器
 *
 * @author kosan
 * @Date 2018-09-08 14:54:40
 */
@Controller
@RequestMapping("/bizInsuranceCompany")
public class BizInsuranceCompanyController extends BaseController {

    private String PREFIX = "/system/bizInsuranceCompany/";

    @Autowired
    private IBizInsuranceCompanyService bizInsuranceCompanyService;

    /**
     * 跳转到首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "bizInsuranceCompany.html";
    }

    /**
     * 跳转到添加
     */
    @RequestMapping("/bizInsuranceCompany_add")
    public String bizInsuranceCompanyAdd() {
        return PREFIX + "bizInsuranceCompany_add.html";
    }

    /**
     * 跳转到修改
     */
    @RequestMapping("/bizInsuranceCompany_update/{bizInsuranceCompanyId}")
    public String bizInsuranceCompanyUpdate(@PathVariable Integer bizInsuranceCompanyId, Model model) {
        BizInsuranceCompany bizInsuranceCompany = bizInsuranceCompanyService.selectById(bizInsuranceCompanyId);
        model.addAttribute("item",bizInsuranceCompany);
        LogObjectHolder.me().set(bizInsuranceCompany);
        return PREFIX + "bizInsuranceCompany_edit.html";
    }

    /**
     * 获取列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return bizInsuranceCompanyService.selectList(null);
    }

    /**
     * 新增
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(BizInsuranceCompany bizInsuranceCompany) {
        bizInsuranceCompanyService.insert(bizInsuranceCompany);
        return SUCCESS_TIP;
    }

    /**
     * 获取部门的tree列表
     */
    @RequestMapping(value = "/listForSelection")
    @ResponseBody
    public List<BizInsuranceCompany> list() {
        List<BizInsuranceCompany> list = this.bizInsuranceCompanyService.list();
        return list;
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer bizInsuranceCompanyId) {
        bizInsuranceCompanyService.deleteById(bizInsuranceCompanyId);
        return SUCCESS_TIP;
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(BizInsuranceCompany bizInsuranceCompany) {
        bizInsuranceCompanyService.updateById(bizInsuranceCompany);
        return SUCCESS_TIP;
    }

    /**
     * 详情
     */
    @RequestMapping(value = "/detail/{bizInsuranceCompanyId}")
    @ResponseBody
    public Object detail(@PathVariable("bizInsuranceCompanyId") Integer bizInsuranceCompanyId) {
        return bizInsuranceCompanyService.selectById(bizInsuranceCompanyId);
    }
}
