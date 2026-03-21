package com.stylefeng.guns.modular.system.controller.count;

import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.modular.system.service.DataCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 理赔员 业务统计
 *
 * @author kosan
 * @Date 2019-03-14 14:57:25
 */
@Controller
@RequestMapping("/claimUser")
public class ClaimUserController extends BaseController {

    private String PREFIX = "/system/countData/";

    @Autowired
    private DataCountService dataCountService;


    @RequestMapping("")
    public String index() {
        return PREFIX + "adjustersCount.html";
    }


    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String startTime, String endTime, String name, String companyName) {
        return dataCountService.userCount(startTime, endTime, name, companyName);
    }


}
