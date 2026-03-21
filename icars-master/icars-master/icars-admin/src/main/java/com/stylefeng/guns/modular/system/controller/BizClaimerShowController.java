package com.stylefeng.guns.modular.system.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.BizClaimerShow;
import com.stylefeng.guns.modular.system.service.IBizClaimerShowService;

import java.util.Date;

/**
 * 控制器
 *
 * @author kosan
 * @Date 2018-08-28 16:14:10
 */
@Controller
@RequestMapping("/bizClaimerShow")
public class BizClaimerShowController extends BaseController {

    private String PREFIX = "/system/bizClaimerShow/";

    @Autowired
    private IBizClaimerShowService bizClaimerShowService;

    /**
     * 跳转到首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "bizClaimerShow.html";
    }

    /**
     * 跳转到添加
     */
    @RequestMapping("/bizClaimerShow_add")
    public String bizClaimerShowAdd() {
        return PREFIX + "bizClaimerShow_add.html";
    }

    /**
     * 跳转到修改
     */
    @RequestMapping("/bizClaimerShow_update/{bizClaimerShowId}")
    public String bizClaimerShowUpdate(@PathVariable Integer bizClaimerShowId, Model model) {
        BizClaimerShow bizClaimerShow = bizClaimerShowService.selectById(bizClaimerShowId);
        model.addAttribute("item",bizClaimerShow);
        LogObjectHolder.me().set(bizClaimerShow);
        return PREFIX + "bizClaimerShow_edit.html";
    }

    /**
     * 获取列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String name, @RequestParam(required = false) Integer status) {
        return bizClaimerShowService.selectClaimerShows(name, status);
    }

    /**
     * 新增
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(BizClaimerShow bizClaimerShow) {
        bizClaimerShow.setCreatetime(new Date());
        bizClaimerShowService.insert(bizClaimerShow);
        return SUCCESS_TIP;
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer bizClaimerShowId) {
        bizClaimerShowService.deleteById(bizClaimerShowId);
        return SUCCESS_TIP;
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(BizClaimerShow bizClaimerShow) {
        if(StringUtils.isEmpty(bizClaimerShow.getImgUrl())){
            bizClaimerShow.setImgUrl(null);
        }
        bizClaimerShowService.updateById(bizClaimerShow);
        return SUCCESS_TIP;
    }

    /**
     * 详情
     */
    @RequestMapping(value = "/detail/{bizClaimerShowId}")
    @ResponseBody
    public Object detail(@PathVariable("bizClaimerShowId") Integer bizClaimerShowId) {
        return bizClaimerShowService.selectById(bizClaimerShowId);
    }



}
