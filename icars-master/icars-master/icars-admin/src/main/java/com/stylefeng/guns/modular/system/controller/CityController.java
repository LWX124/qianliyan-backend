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
import com.stylefeng.guns.modular.system.model.City;
import com.stylefeng.guns.modular.system.service.ICityService;

/**
 * 地市表控制器
 *
 * @author kosan
 * @Date 2019-03-20 23:42:46
 */
@Controller
@RequestMapping("/city")
public class CityController extends BaseController {

    private String PREFIX = "/system/city/";

    @Autowired
    private ICityService cityService;

    /**
     * 跳转到地市表首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "city.html";
    }

    /**
     * 跳转到添加地市表
     */
    @RequestMapping("/city_add")
    public String cityAdd() {
        return PREFIX + "city_add.html";
    }

    /**
     * 跳转到修改地市表
     */
    @RequestMapping("/city_update/{cityId}")
    public String cityUpdate(@PathVariable Integer cityId, Model model) {
        City city = cityService.selectById(cityId);
        model.addAttribute("item",city);
        LogObjectHolder.me().set(city);
        return PREFIX + "city_edit.html";
    }

    /**
     * 获取地市表列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return cityService.selectList(null);
    }

    /**
     * 新增地市表
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(City city) {
        cityService.insert(city);
        return SUCCESS_TIP;
    }

    /**
     * 删除地市表
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer cityId) {
        cityService.deleteById(cityId);
        return SUCCESS_TIP;
    }

    /**
     * 修改地市表
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(City city) {
        cityService.updateById(city);
        return SUCCESS_TIP;
    }

    /**
     * 地市表详情
     */
    @RequestMapping(value = "/detail/{cityId}")
    @ResponseBody
    public Object detail(@PathVariable("cityId") Integer cityId) {
        return cityService.selectById(cityId);
    }
}
