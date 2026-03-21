package com.stylefeng.guns.modular.system.controller.count;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.modular.system.model.Accident;
import com.stylefeng.guns.modular.system.service.DataCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 小程序新增用户统计
 *
 * @author kosan
 * @Date 2019-03-14 14:57:25
 */
@Controller
@RequestMapping("/xcxUserCount")
public class XcxUserCountController extends BaseController {

    private String PREFIX = "/system/countData/";

    @Autowired
    private DataCountService dataCountService;

    @RequestMapping("")
    public String index() {
        return PREFIX + "xcxUserCountForDay.html";
    }


    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list() {
        Page page = new PageFactory().defaultPage();
        return dataCountService.xcxUserCount(page);
    }

}
