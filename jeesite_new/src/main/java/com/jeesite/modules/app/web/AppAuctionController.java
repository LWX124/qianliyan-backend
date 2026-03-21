package com.jeesite.modules.app.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.app.entity.AppAuction;
import com.jeesite.modules.app.service.AppAuctionService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拍卖业务表Controller
 *
 * @author y
 * @version 2022-10-14
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appAuction")
public class AppAuctionController extends BaseController {

    @Autowired
    private AppAuctionService appAuctionService;


    /**
     * 获取数据
     */
    @ModelAttribute
    public AppAuction get(String id, boolean isNewRecord) {
        return appAuctionService.get(id, isNewRecord);
    }

    /**
     * 查询列表
     */
    @RequiresPermissions("app:appAuction:view")
    @RequestMapping(value = {"list", ""})
    public String list(AppAuction appAuction, Model model) {
        model.addAttribute("appAuction", appAuction);
        return "modules/app/appAuctionList";
    }

    /**
     * 查询列表数据
     */
    @RequiresPermissions("app:appAuction:view")
    @RequestMapping(value = "listData")
    @ResponseBody
    public Page<AppAuction> listData(AppAuction appAuction, HttpServletRequest request, HttpServletResponse response) {
        appAuction.setPage(new Page<>(request, response));
        Page<AppAuction> page = appAuctionService.findPage(appAuction);
        return page;
    }

    /**
     * 查看编辑表单
     */
//	@RequiresPermissions("app:appAuction:view")
    @RequestMapping(value = "form")
    public String form(AppAuction appAuction, Model model) {
        model.addAttribute("appAuction", appAuction);
        return "modules/app/appAuctionForm";
    }


    /**
     * 保存拍卖业务表
     */
    @RequiresPermissions("app:appAuction:edit")
    @PostMapping(value = "save")
    @ResponseBody
    public String save(@Validated AppAuction appAuction) {
        try {
            AppAuction oldAppAuction = appAuctionService.get(appAuction.getId());
            if (oldAppAuction.getCarState() == 7) {
                if (appAuction.getPrice() != oldAppAuction.getPrice()) {
                    return renderResult(Global.FALSE, text("拍卖中的订单不能修改起拍价！"));
                }
            }
            appAuctionService.save(appAuction);
        } catch (Exception e) {
            return renderResult(Global.FALSE, text("保存拍卖业务表失败！"));
        }
        return renderResult(Global.TRUE, text("保存拍卖业务表成功！"));
    }

    /**
     * 删除拍卖业务表
     */
    @RequiresPermissions("app:appAuction:edit")
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(AppAuction appAuction) {
        appAuctionService.delete(appAuction);
        return renderResult(Global.TRUE, text("删除拍卖业务表成功！"));
    }

}