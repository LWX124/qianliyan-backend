package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheji.web.modular.domain.AppUploadRecordEntity;
import com.cheji.web.modular.service.AppUploadRecordService;
import com.cheji.web.pojo.TokenPojo;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 文件上传控制器（小程序端使用）
 */
@Api("文件上传")
@RestController
@RequestMapping("/file")
public class FileController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(FileController.class);

    @Value("${qiniu.access-key}")
    private String accessKey;

    @Value("${qiniu.secret-key}")
    private String secretKey;

    @Value("${qiniu.bucket}")
    private String bucket;

    @Value("${qiniu.domain}")
    private String domain;

    @Resource
    private AppUploadRecordService appUploadRecordService;

    // 私有空间下载链接有效期（秒）：1小时
    private static final long DOWNLOAD_EXPIRE_SECONDS = 3600;

    // 最大文件大小：视频 200MB，图片 10MB
    private static final long MAX_VIDEO_SIZE = 200 * 1024 * 1024L;
    private static final long MAX_IMAGE_SIZE = 10 * 1024 * 1024L;
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
            ".jpg", ".jpeg", ".png", ".gif", ".mp4", ".mov", ".avi"
    );

    @ApiOperation("上传文件（视频/图片）")
    @PostMapping("/upload")
    public JSONObject upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        JSONObject result = new JSONObject();

        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

        if (file == null || file.isEmpty()) {
            result.put("code", 401);
            result.put("msg", "文件不能为空");
            return result;
        }

        // 校验文件扩展名
        String originalFilename = file.getOriginalFilename();
        String ext = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            ext = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        }
        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            result.put("code", 401);
            result.put("msg", "不支持的文件类型，仅支持图片和视频");
            return result;
        }

        // 校验文件大小
        boolean isVideo = ext.equals(".mp4") || ext.equals(".mov") || ext.equals(".avi");
        long maxSize = isVideo ? MAX_VIDEO_SIZE : MAX_IMAGE_SIZE;
        if (file.getSize() > maxSize) {
            result.put("code", 401);
            result.put("msg", isVideo ? "视频文件不能超过200MB" : "图片文件不能超过10MB");
            return result;
        }

        try (InputStream inputStream = file.getInputStream()) {
            String fileName = "img/" + UUID.randomUUID().toString().replaceAll("-", "") + ext;

            // 七牛云上传
            Configuration cfg = new Configuration(Region.autoRegion());
            UploadManager uploadManager = new UploadManager(cfg);
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            uploadManager.put(inputStream, fileName, upToken, null, null);

            String url = domain + fileName;

            // 生成缩略图URL：视频取第1秒帧，图片缩放
            String thumbnailUrl;
            if (isVideo) {
                thumbnailUrl = url + "?vframe/jpg/offset/1/w/480/h/360";
            } else {
                thumbnailUrl = url + "?imageView2/1/w/480/h/360";
            }

            // 保存上传记录
            AppUploadRecordEntity record = new AppUploadRecordEntity();
            record.setUserId(currentLoginUser.getAppUserEntity().getId());
            record.setFileUrl(url);
            record.setFileName(originalFilename);
            record.setFileType(isVideo ? "video" : "image");
            record.setFileSize(file.getSize());
            record.setThumbnailUrl(thumbnailUrl);
            record.setCreateTime(new Date());
            appUploadRecordService.insert(record);

            // 返回签名后的私有下载链接
            String signedUrl = auth.privateDownloadUrl(url, DOWNLOAD_EXPIRE_SECONDS);
            String signedThumbnailUrl = auth.privateDownloadUrl(thumbnailUrl, DOWNLOAD_EXPIRE_SECONDS);

            JSONObject data = new JSONObject();
            data.put("id", record.getId());
            data.put("url", signedUrl);
            data.put("thumbnailUrl", signedThumbnailUrl);
            data.put("fileName", originalFilename);
            data.put("fileType", isVideo ? "video" : "image");
            data.put("fileSize", file.getSize());
            data.put("createTime", record.getCreateTime());

            result.put("code", 200);
            result.put("msg", "上传成功");
            result.put("data", data);
            return result;

        } catch (Exception e) {
            logger.error("文件上传失败", e);
            result.put("code", 500);
            result.put("msg", "上传失败：" + e.getMessage());
            return result;
        }
    }

    @ApiOperation("查询上传记录列表")
    @GetMapping("/list")
    public JSONObject list(@RequestParam(required = false, defaultValue = "1") Integer page,
                           @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                           HttpServletRequest request) {
        JSONObject result = new JSONObject();

        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

        Integer userId = currentLoginUser.getAppUserEntity().getId();
        List<AppUploadRecordEntity> records = appUploadRecordService.listByUserId(userId, page, pageSize);
        int total = appUploadRecordService.countByUserId(userId);

        // 对私有空间的URL进行签名
        Auth auth = Auth.create(accessKey, secretKey);
        for (AppUploadRecordEntity record : records) {
            if (record.getFileUrl() != null) {
                record.setFileUrl(auth.privateDownloadUrl(record.getFileUrl(), DOWNLOAD_EXPIRE_SECONDS));
            }
            if (record.getThumbnailUrl() != null) {
                record.setThumbnailUrl(auth.privateDownloadUrl(record.getThumbnailUrl(), DOWNLOAD_EXPIRE_SECONDS));
            }
        }

        JSONObject data = new JSONObject();
        data.put("list", records);
        data.put("total", total);
        data.put("page", page);
        data.put("pageSize", pageSize);

        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", data);
        return result;
    }

    @ApiOperation("删除上传记录")
    @PostMapping("/delete")
    public JSONObject delete(@RequestParam Long id, HttpServletRequest request) {
        JSONObject result = new JSONObject();

        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

        AppUploadRecordEntity record = appUploadRecordService.selectById(id);
        if (record == null || !record.getUserId().equals(currentLoginUser.getAppUserEntity().getId())) {
            result.put("code", 401);
            result.put("msg", "记录不存在");
            return result;
        }

        appUploadRecordService.deleteById(id);
        result.put("code", 200);
        result.put("msg", "删除成功");
        return result;
    }
}
