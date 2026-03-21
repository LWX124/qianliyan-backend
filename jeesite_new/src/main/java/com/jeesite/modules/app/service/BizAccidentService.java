/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.dao.BizAccidentDao;
import com.jeesite.modules.app.entity.AppVideo;
import com.jeesite.modules.app.entity.BizAccident;
import com.jeesite.modules.app.entity.BizWxUser;
import com.jeesite.modules.util2.HuaweiUploadUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * 事故上报信息表Service
 *
 * @author zcq
 * @version 2019-09-24
 */
@Service
@Transactional(readOnly = true)
public class BizAccidentService extends CrudService<BizAccidentDao, BizAccident> {

    private final static Logger logger = LoggerFactory.getLogger(BizAccidentService.class);

    @Resource
    private BizWxUserService bizWxUserService;

    @Resource
    private AppVideoService appVideoService;

    @Resource
    private BizAccidentDao bizAccidentDao;

    /**
     * 获取单条数据
     *
     * @param bizAccident
     * @return
     */
    @Override
    public BizAccident get(BizAccident bizAccident) {
        return super.get(bizAccident);
    }

    /**
     * 查询分页数据
     *
     * @param bizAccident      查询条件
     * @param bizAccident.page 分页对象
     * @return
     */
    @Override
    public Page<BizAccident> findPage(BizAccident bizAccident) {
        return super.findPage(bizAccident);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param bizAccident
     */
    @Override
    @Transactional(readOnly = false)
    public void save(BizAccident bizAccident) {
        super.save(bizAccident);
    }

    /**
     * 更新状态
     *
     * @param bizAccident
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(BizAccident bizAccident) {
        super.updateStatus(bizAccident);
    }

    /**
     * 删除数据
     *
     * @param bizAccident
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(BizAccident bizAccident) {
        super.delete(bizAccident);
    }

    @Transactional(readOnly = false)
    public AppVideo saveApp(String id) {
        //通过事故id查询到具体信息
        BizAccident bizAccident = get(id);
        String video = bizAccident.getVideo();
        String openid = bizAccident.getOpenid();
        String address = bizAccident.getAddress();
        String thumbnailUrl = bizAccident.getThumbnailUrl();

        //根据userid查询到名字
        BizWxUser bizWxUser = bizWxUserService.findByOpenid(openid);
        String headimg = bizWxUser.getHeadimg();
        if (headimg == null) {
            headimg = null;
        }
        String wxname = bizWxUser.getWxname();
        if (wxname == null) {
            wxname = null;
        }
        bizAccident.setIsaddvideo(1);
        AppVideo appVideo = new AppVideo();
        if (bizAccident.getStatus().equals("2")) {
            appVideo.setIsPay(1);
        }
        if (StringUtils.isNotEmpty(video)) {
            if (video.contains("obs")) {
                //app上传
                String replace = video.replace("https://obs-4f4a.obs.cn-southwest-2.myhuaweicloud.com/video/", "http://www.hw.chejiqiche.com/video/");
                appVideo.setUrl(replace);
            } else {
                //小程序上传
				File tmp = new File(video.replace("https://chejiqiche.com/video/", "/home/ftpuser/www/video/"));
//                File tmp = new File(video.replace("https://chejiqiche.com/video/", "d:\\"));

                if (tmp == null) {
                    logger.error("### 把事故视频展示到app表中 ###小视频 读取文件失败### video={}", video);
                    return null;
                }
                try {
                    String fileName = video.replace("https://chejiqiche.com/video/", "");
                    InputStream inputStream = new FileInputStream(tmp);
                    HuaweiUploadUtils.uploadToHuawei(fileName, inputStream);
                } catch (FileNotFoundException e) {
                    logger.error("### 上传华为云obs失败 ####", e.getStackTrace());
                }
                String cdnVideoName = video.replace("https://chejiqiche.com/video/", "http://www.hw.chejiqiche.com/video/");
                logger.info("### 小程序上传 cdn地址 # cdnVideoName={}", cdnVideoName);
                appVideo.setUrl(cdnVideoName);
            }

        }
        //对应的信息放到app表中
        // appVideo.setUrl(video);
        appVideo.setUserId(openid);
        appVideo.setSource(2);
        appVideo.setCount(0);
        appVideo.setShare(0L);
        appVideo.setAppViewCounts(0L);
        appVideo.setAppShowFalg(1);
        appVideo.setAccidentId(Long.valueOf(id));
        appVideo.setThumbnailUrl(thumbnailUrl);
        Date date = new Date();
        appVideo.setCreatTime(date);
        appVideo.setUpdateTime(date);
        appVideo.setName(wxname);
        appVideo.setAvatar(headimg);
        appVideo.setAddress(address);
        appVideoService.insert(appVideo);

        String id1 = appVideo.getId();
        bizAccident.setVideoId(Long.valueOf(id1));
        update(bizAccident);
        return appVideo;
    }

    public List<BizAccident> selectNoDel() {
        return bizAccidentDao.selectNoDel();
    }

    public List<BizAccident> selectRepart(String accId, String type) {
        return bizAccidentDao.selectRepart(accId,type);
    }

    public String findIsFaId(String openid) {
        return bizAccidentDao.findIsFaId(openid);
    }

    @Transactional(readOnly = false)
    public void insertNewBiz(BizAccident newBizacc) {
        bizAccidentDao.insertNewBiz(newBizacc);
    }

    public BizAccident findOnePay(String faOpenId, String video) {
        return bizAccidentDao.findOnePay(faOpenId,video);
    }
}