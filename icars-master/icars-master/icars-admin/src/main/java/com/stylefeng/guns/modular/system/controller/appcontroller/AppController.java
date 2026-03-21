package com.stylefeng.guns.modular.system.controller.appcontroller;

import com.stylefeng.guns.modular.system.model.Notice;
import com.stylefeng.guns.modular.system.service.INoticeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class AppController {

    @Autowired
    private INoticeService noticeService;

    /**
     * app新闻页
     *
     * @param
     * @return
     */
    @ApiOperation(value = "app新闻页")
    @RequestMapping(value = "/api/v1/app/goAppNews", method = RequestMethod.GET, produces = "application/json")
    public String goAppNews(Integer id, Model model) {
        if (id == null || id < 1) {
            return "/appNews.html";
        }
        Notice notice = new Notice();
        notice.setId(id);
        List<Map<String, Object>> list = this.noticeService.list(notice);
        model.addAttribute("content", list.get(0));
        return "/appNews.html";
    }

    /**
     * app首页
     *
     * @param
     * @return
     */
    @ApiOperation(value = "app首页")
    @RequestMapping(value = "/api/v1/app/goAppHome", method = RequestMethod.GET, produces = "application/json")
    public String goAppHome(String thirdSessionKey, Model model) {
        model.addAttribute("thirdSessionKey", thirdSessionKey);
        return "/appHome.html";
    }

    /**
     * app新闻页
     *
     * @param
     * @return
     */
    @ApiOperation(value = "app新闻页")
    @RequestMapping(value = "/api/v1/app/privacy", method = RequestMethod.GET, produces = "application/json")
    public String privacy() {
        return "/privacy.html";
    }
}
