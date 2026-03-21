/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.app.entity.AppAccidentRecord;
import com.jeesite.modules.constant2.RedisKeyUtils;
import com.jeesite.modules.pojo2.CommonConfigPojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 基础设置
 *
 * @author dh
 */
@Controller
@RequestMapping(value = "${adminPath}/app/commons")
public class CommonsConfigController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(CommonsConfigController.class);

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 查询列表
     */
    @RequestMapping(value = {"configView", ""})
    public String list(Model model) {
        String s = stringRedisTemplate.opsForValue().get(RedisKeyUtils.APP_HOME_TEXT);
        String s1 = stringRedisTemplate.opsForValue().get(RedisKeyUtils.APP_OIL_SHARE_TEXT);
        CommonConfigPojo commonConfigPojo = new CommonConfigPojo();
        commonConfigPojo.setAppHomeText(s);
        commonConfigPojo.setOilShareText(s1);

        commonConfigPojo.setSceneTitle1(stringRedisTemplate.opsForValue().get(RedisKeyUtils.SHARE_TITLE + 1));
        commonConfigPojo.setSceneContent1(stringRedisTemplate.opsForValue().get(RedisKeyUtils.SHARE_CONTENT + 1));

        commonConfigPojo.setSceneTitle2(stringRedisTemplate.opsForValue().get(RedisKeyUtils.SHARE_TITLE + 2));
        commonConfigPojo.setSceneContent2(stringRedisTemplate.opsForValue().get(RedisKeyUtils.SHARE_CONTENT + 2));

        commonConfigPojo.setSceneTitle3(stringRedisTemplate.opsForValue().get(RedisKeyUtils.SHARE_TITLE + 3));
        commonConfigPojo.setSceneContent3(stringRedisTemplate.opsForValue().get(RedisKeyUtils.SHARE_CONTENT + 3));

        commonConfigPojo.setSceneTitle4(stringRedisTemplate.opsForValue().get(RedisKeyUtils.SHARE_TITLE + 4));
        commonConfigPojo.setSceneContent4(stringRedisTemplate.opsForValue().get(RedisKeyUtils.SHARE_CONTENT + 4));

        commonConfigPojo.setSceneTitle5(stringRedisTemplate.opsForValue().get(RedisKeyUtils.SHARE_TITLE + 5));
        commonConfigPojo.setSceneContent5(stringRedisTemplate.opsForValue().get(RedisKeyUtils.SHARE_CONTENT + 5));

        commonConfigPojo.setSceneTitle6(stringRedisTemplate.opsForValue().get(RedisKeyUtils.SHARE_TITLE + 6));
        commonConfigPojo.setSceneContent6(stringRedisTemplate.opsForValue().get(RedisKeyUtils.SHARE_CONTENT + 6));

        commonConfigPojo.setSceneTitle7(stringRedisTemplate.opsForValue().get(RedisKeyUtils.SHARE_TITLE + 7));
        commonConfigPojo.setSceneContent7(stringRedisTemplate.opsForValue().get(RedisKeyUtils.SHARE_CONTENT + 7));


        commonConfigPojo.setSceneTitle8(stringRedisTemplate.opsForValue().get(RedisKeyUtils.SHARE_TITLE + 8));
        commonConfigPojo.setSceneContent8(stringRedisTemplate.opsForValue().get(RedisKeyUtils.SHARE_CONTENT + 8));


        commonConfigPojo.setSceneTitle9(stringRedisTemplate.opsForValue().get(RedisKeyUtils.SHARE_TITLE + 9));
        commonConfigPojo.setSceneContent9(stringRedisTemplate.opsForValue().get(RedisKeyUtils.SHARE_CONTENT + 9));


        commonConfigPojo.setSceneTitle10(stringRedisTemplate.opsForValue().get(RedisKeyUtils.SHARE_TITLE + 10));
        commonConfigPojo.setSceneContent10(stringRedisTemplate.opsForValue().get(RedisKeyUtils.SHARE_CONTENT + 10));

        commonConfigPojo.setSceneContent11(stringRedisTemplate.opsForValue().get(RedisKeyUtils.SHARE_CONTENT + 11));

        commonConfigPojo.setSceneContent12(stringRedisTemplate.opsForValue().get(RedisKeyUtils.SHARE_CONTENT + 12));

        commonConfigPojo.setSceneTitle13(stringRedisTemplate.opsForValue().get(RedisKeyUtils.SHARE_TITLE + 13));
        commonConfigPojo.setSceneContent13(stringRedisTemplate.opsForValue().get(RedisKeyUtils.SHARE_CONTENT + 13));

        commonConfigPojo.setSceneContent17(stringRedisTemplate.opsForValue().get(RedisKeyUtils.SHARE_TITLE ));
        commonConfigPojo.setSceneContent17(stringRedisTemplate.opsForValue().get(RedisKeyUtils.SHARE_CONTENT));

        commonConfigPojo.setSceneContent18(stringRedisTemplate.opsForValue().get(RedisKeyUtils.SHARE_TITLE + 18));
        commonConfigPojo.setSceneContent18(stringRedisTemplate.opsForValue().get(RedisKeyUtils.SHARE_CONTENT + 18));

        model.addAttribute("commonConfigPojo", commonConfigPojo);

        return "modules/app/commonsConfig";
    }

    /**
     * 保存
     */
    @PostMapping(value = "save")
    @ResponseBody
    public String save(CommonConfigPojo commonConfigPojo) {
        String appHomeText = commonConfigPojo.getAppHomeText();
        String oilShareText = commonConfigPojo.getOilShareText();
        stringRedisTemplate.opsForValue().set(RedisKeyUtils.APP_HOME_TEXT, appHomeText);
//        stringRedisTemplate.opsForValue().set(RedisKeyUtils.APP_OIL_SHARE_TEXT, oilShareText);
        //1：洗车  2美容  3保养  4 维修  5 喷漆   6电瓶  7加油  8 救援 9 违章 10 年检
        stringRedisTemplate.opsForValue().set(RedisKeyUtils.SHARE_TITLE + 1, commonConfigPojo.getSceneTitle1());
        stringRedisTemplate.opsForValue().set(RedisKeyUtils.SHARE_CONTENT + 1, commonConfigPojo.getSceneContent1());

        stringRedisTemplate.opsForValue().set(RedisKeyUtils.SHARE_TITLE + 2, commonConfigPojo.getSceneTitle2());
        stringRedisTemplate.opsForValue().set(RedisKeyUtils.SHARE_CONTENT + 2, commonConfigPojo.getSceneContent2());

        stringRedisTemplate.opsForValue().set(RedisKeyUtils.SHARE_TITLE + 3, commonConfigPojo.getSceneTitle3());
        stringRedisTemplate.opsForValue().set(RedisKeyUtils.SHARE_CONTENT + 3, commonConfigPojo.getSceneContent3());

        stringRedisTemplate.opsForValue().set(RedisKeyUtils.SHARE_TITLE + 4, commonConfigPojo.getSceneTitle4());
        stringRedisTemplate.opsForValue().set(RedisKeyUtils.SHARE_CONTENT + 4, commonConfigPojo.getSceneContent4());

        stringRedisTemplate.opsForValue().set(RedisKeyUtils.SHARE_TITLE + 5, commonConfigPojo.getSceneTitle5());
        stringRedisTemplate.opsForValue().set(RedisKeyUtils.SHARE_CONTENT + 5, commonConfigPojo.getSceneContent5());

        stringRedisTemplate.opsForValue().set(RedisKeyUtils.SHARE_TITLE + 6, commonConfigPojo.getSceneTitle6());
        stringRedisTemplate.opsForValue().set(RedisKeyUtils.SHARE_CONTENT + 6, commonConfigPojo.getSceneContent6());

        stringRedisTemplate.opsForValue().set(RedisKeyUtils.SHARE_TITLE + 7, commonConfigPojo.getSceneTitle7());
        stringRedisTemplate.opsForValue().set(RedisKeyUtils.SHARE_CONTENT + 7, commonConfigPojo.getSceneContent7());

        stringRedisTemplate.opsForValue().set(RedisKeyUtils.SHARE_TITLE + 8, commonConfigPojo.getSceneTitle8());
        stringRedisTemplate.opsForValue().set(RedisKeyUtils.SHARE_CONTENT + 8, commonConfigPojo.getSceneContent8());

        stringRedisTemplate.opsForValue().set(RedisKeyUtils.SHARE_TITLE + 9, commonConfigPojo.getSceneTitle9());
        stringRedisTemplate.opsForValue().set(RedisKeyUtils.SHARE_CONTENT + 9, commonConfigPojo.getSceneContent9());

        stringRedisTemplate.opsForValue().set(RedisKeyUtils.SHARE_TITLE + 10, commonConfigPojo.getSceneTitle10());
        stringRedisTemplate.opsForValue().set(RedisKeyUtils.SHARE_CONTENT + 10, commonConfigPojo.getSceneContent10());

        stringRedisTemplate.opsForValue().set(RedisKeyUtils.SHARE_CONTENT + 11, commonConfigPojo.getSceneContent11());

        stringRedisTemplate.opsForValue().set(RedisKeyUtils.SHARE_CONTENT + 12, commonConfigPojo.getSceneContent12());

        stringRedisTemplate.opsForValue().set(RedisKeyUtils.SHARE_TITLE + 13, commonConfigPojo.getSceneTitle13());
        stringRedisTemplate.opsForValue().set(RedisKeyUtils.SHARE_CONTENT + 13, commonConfigPojo.getSceneContent13());

        //17是默认话术
        stringRedisTemplate.opsForValue().set(RedisKeyUtils.SHARE_TITLE, commonConfigPojo.getSceneTitle17());
        stringRedisTemplate.opsForValue().set(RedisKeyUtils.SHARE_CONTENT, commonConfigPojo.getSceneContent17());

        //18是首页视频分享
        stringRedisTemplate.opsForValue().set(RedisKeyUtils.SHARE_TITLE + 18, commonConfigPojo.getSceneTitle18());
        stringRedisTemplate.opsForValue().set(RedisKeyUtils.SHARE_CONTENT + 18, commonConfigPojo.getSceneContent18());

        return renderResult(Global.TRUE, text("保存成功！"));
    }


}
