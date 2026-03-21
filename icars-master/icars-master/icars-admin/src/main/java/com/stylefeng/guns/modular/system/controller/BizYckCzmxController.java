package com.stylefeng.guns.modular.system.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.modular.system.model.Accident;
import com.stylefeng.guns.modular.system.warpper.BizYckCzmxWarpper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.BizYckCzmx;
import com.stylefeng.guns.modular.system.service.IBizYckCzmxService;

import java.util.List;
import java.util.Map;

/**
 * 控制器
 *
 * @author kosan
 * @Date 2018-09-15 10:36:32
 */
@Controller
@RequestMapping("/bizYckCzmx")
public class BizYckCzmxController extends BaseController {

    private String PREFIX = "/system/bizYckCzmx/";

    @Autowired
    private IBizYckCzmxService bizYckCzmxService;

    /**
     * 跳转到首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "bizYckCzmx.html";
    }

    /**
     * 跳转到添加
     */
    @RequestMapping("/bizYckCzmx_add")
    public String bizYckCzmxAdd() {
        return PREFIX + "bizYckCzmx_add.html";
    }

    /**
     * 跳转到修改
     */
    @RequestMapping("/bizYckCzmx_update/{bizYckCzmxId}")
    public String bizYckCzmxUpdate(@PathVariable Integer bizYckCzmxId, Model model) {
        BizYckCzmx bizYckCzmx = bizYckCzmxService.selectById(bizYckCzmxId);
        model.addAttribute("item",bizYckCzmx);
        LogObjectHolder.me().set(bizYckCzmx);
        return PREFIX + "bizYckCzmx_edit.html";
    }

    /**
     * 获取列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition, @RequestParam(required = false) String createStartTime, @RequestParam(required = false) String createEndTime, @RequestParam(required = false) Integer detailType) {
        Page<BizYckCzmx> page = new PageFactory<BizYckCzmx>().defaultPage();
        List<Map<String, Object>> res = bizYckCzmxService.selectList(page, condition, createStartTime, createEndTime, detailType, page.getOrderByField(), page.isAsc());
        page.setRecords((List<BizYckCzmx>) new BizYckCzmxWarpper(res).warp());
        return super.packForBT(page);
    }

    /**
     * 新增
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(BizYckCzmx bizYckCzmx) {
        bizYckCzmxService.insert(bizYckCzmx);
        return SUCCESS_TIP;
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer bizYckCzmxId) {
        bizYckCzmxService.deleteById(bizYckCzmxId);
        return SUCCESS_TIP;
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(BizYckCzmx bizYckCzmx) {
        bizYckCzmxService.updateById(bizYckCzmx);
        return SUCCESS_TIP;
    }

    /**
     * 详情
     */
    @RequestMapping(value = "/detail/{bizYckCzmxId}")
    @ResponseBody
    public Object detail(@PathVariable("bizYckCzmxId") Integer bizYckCzmxId) {
        return bizYckCzmxService.selectById(bizYckCzmxId);
    }
}
