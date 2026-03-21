package com.stylefeng.guns.modular.system.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.modular.system.model.BizClaim;
import com.stylefeng.guns.modular.system.dao.BizClaimMapper;
import com.stylefeng.guns.modular.system.service.IBizClaimService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 理赔单 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-08-27
 */
@Service
public class BizClaimServiceImpl extends ServiceImpl<BizClaimMapper, BizClaim> implements IBizClaimService {

    @Value("${spring.wx.claimImgLocalPath}")
    private String claimImgLocalPath;

    @Value("${spring.wx.netHost}")
    private String netHost;

    @Override
    public List<Map<String, Object>> selectListForPage(Page<BizClaim> page, BizClaim bizClaim, String startTime, String endTime) {
        List<Map<String, Object>> result = this.baseMapper.selectListForPage(page, bizClaim, startTime, endTime);
        return result;
    }

    @Override
    public List<Map<String, Object>> selectListForPageByOpendId(Page<BizClaim> page, String openId, Integer type) {
        if (type == 0) {
            List<Map<String, Object>> result = this.baseMapper.selectAllForXcx(page, openId, type);
            return result;
        }
        List<Map<String, Object>> result = this.baseMapper.selectListForPageByOpendId(page, openId, type);
        return result;
    }

    @Override
    public void addClaimImage(Integer id, String claimImg) throws IOException {
        BizClaim bizClaim = this.baseMapper.selectById(id);
        bizClaim.setClaimImg(claimImg);
        this.baseMapper.updateById(bizClaim);
    }

    @Override
    public List<String> uploadFile(List<MultipartFile> files) throws IOException {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            String random = RandomStringUtils.randomAlphabetic(32);
            String fileName;
            fileName = random.concat(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."), file.getOriginalFilename().length()));
            String imgUrl = null;
            File newfile = new File(claimImgLocalPath, fileName);
            if (!newfile.getParentFile().exists()) {
                newfile.getParentFile().mkdirs();
            }
            FileCopyUtils.copy(file.getBytes(), newfile);
            imgUrl = netHost.concat("claimImg/").concat(fileName);
            urls.add(imgUrl);
        }
        return urls;
    }
}
