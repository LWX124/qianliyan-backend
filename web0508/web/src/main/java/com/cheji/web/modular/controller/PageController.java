package com.cheji.web.modular.controller;

import com.cheji.web.constant.RedisConstant;
import com.cheji.web.modular.domain.AccidentRecordEntity;
import com.cheji.web.modular.domain.AppUserEntity;
import com.cheji.web.modular.domain.VideoEntity;
import com.cheji.web.modular.mapper.AppUserMapper;
import com.cheji.web.modular.mapper.VideoMapper;
import com.cheji.web.modular.service.AccidentRecordService;
import com.cheji.web.modular.service.VideoCommentsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * 返回页面
 */
@Controller
@RequestMapping("/page")
public class PageController {

    private Logger logger = LoggerFactory.getLogger(PageController.class);

    @Resource
    private VideoMapper videoMapper;

    @Resource
    private AccidentRecordService accidentRecordService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private VideoCommentsService videoCommentsService;

//    @Resource
//    private AppUserMapper appUserMapper;

    @GetMapping("/auctionAgreement")
    public String auctionAgreement() {
        return "auctionAgreement";
    }

    @GetMapping("/agreement")
    public String agreement() {
        return "agreement";
    }

    @GetMapping("/register1")
    public String register(String code, Model model) {
        model.addAttribute("code", code);
        return "register";
    }

    @GetMapping("/privacy")
    public String privacy() {
        return "privacy";
    }

    @GetMapping("/pair")
    public String pair() {
        return "pair";
    }

    @GetMapping("/userAgreement")
    public String userAgreement(){
        return "userAgreement";
    }


    @GetMapping("/rescueAgreement")
    public String rescueAgreement(){
        return "rescueAgreement";
    }

    @GetMapping("/yearCheckAgreement")
    public String yearCheckAgreement(){
        return "yearCheckAgreement";
    }

    @GetMapping("/subDrivingAgreement")
    public String subDrivingAgreement(){
        return "subDrivingAgreement";
    }

    @GetMapping("/upgradeRules")
    public String upgradeRules(){
        return "upgradeRules";
    }



    /**
     * h5分享视频页面
     *
     * @param code    邀请码
     * @param videoId 视频id
     * @return
     */
    @GetMapping("/share")
    public String share(String code, String videoId, Model model) {
        if (StringUtils.isEmpty(videoId)) {
            logger.error("分享视频videoId 为空  code={}", code);
            return "";
        }
        VideoEntity videoEntity = videoMapper.selectById(videoId);
        if (videoEntity == null) {
            logger.error("分享视频videoEntity 为空  videoId={}", videoId);
            return "";
        }

        //获取到点赞数量
        //获取到视频i
        String id = videoEntity.getId();
        Long size = stringRedisTemplate.opsForZSet().size(RedisConstant.SET_VIDEO_THUMBS + id);

        //获取到评论数量
        Long videoCommentCount = videoCommentsService.findVideoCommentCount(Long.valueOf(id),1);

//        String userId = videoEntity.getUserId();
//        AppUserEntity appUserEntity = appUserMapper.selectById(userId);

        model.addAttribute("code", code);
        model.addAttribute("video", videoEntity.getUrl());
//        model.addAttribute("source", videoEntity.getSource() == null || videoEntity.getSource() == 2);
        model.addAttribute("up_num", size);
        model.addAttribute("comment_num", videoCommentCount);
        String address = videoEntity.getAddress();
        if (address.length() > 8) {
            address = address.substring(0, 8)+"...";
        }
        model.addAttribute("address", address);
        model.addAttribute("introduce", videoEntity.getIntroduce());
        return "share";
    }


    @GetMapping("/accWebs")
    public String accWebs(String code, String accid, Model model) {
        if (StringUtils.isEmpty(accid)) {
            logger.error("分享给事故奖励accid 为空  accid={}", accid);
            return "";
        }
        //获取到点赞数量
        //获取到视频id

        //获取到评论数量

//        String userId = videoEntity.getUserId();
//        AppUserEntity appUserEntity = appUserMapper.selectById(userId);
        AccidentRecordEntity accidentRecord = accidentRecordService.selectById(accid);
        if (accidentRecord==null){
            logger.error("accidentRecord 为空  accidentRecord={}", accidentRecord);
            return "";
        }


        model.addAttribute("code", code);
        model.addAttribute("video", accidentRecord.getVideo());
//        model.addAttribute("source", videoEntity.getSource() == null || videoEntity.getSource() == 2);
        model.addAttribute("up_num", "0");
        model.addAttribute("comment_num", "0");
        String address = accidentRecord.getAddress();
        if (address.length() > 8) {
            address = address.substring(0, 8)+"...";
        }
        model.addAttribute("address", address);
        model.addAttribute("introduce", accidentRecord.getIntroduce());
        return "share";
    }
}
