package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheji.web.pojo.TokenPojo;
import com.obs.services.ObsClient;
import com.obs.services.model.ObjectMetadata;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
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

    private static final String END_POINT = "obs.cn-southwest-2.myhuaweicloud.com";
    private static final String AK = "IRRNEYTCG1WNG35ST0FP";
    private static final String SK = "BMIU9nhzo9TSFu5SL09utdGP1xeXsWEFXGFoqa46";
    private static final String BUCKET = "watermark-a33d";
    private static final String DOMAIN = "https://watermark-a33d.obs.cn-southwest-2.myhuaweicloud.com/";

    private static final ObsClient obsClient = new ObsClient(AK, SK, END_POINT);

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

        try (InputStream content = file.getInputStream()) {
            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + ext;
            String objectKey = "img/" + fileName;

            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(file.getSize());
            obsClient.putObject(BUCKET, objectKey, content, meta);

            String url = DOMAIN + objectKey;

            JSONObject data = new JSONObject();
            data.put("url", url);
            data.put("fileName", fileName);

            result.put("code", 200);
            result.put("msg", "上传成功");
            result.put("data", data);
            return result;

        } catch (IOException e) {
            logger.error("文件上传失败", e);
            result.put("code", 500);
            result.put("msg", "上传失败：" + e.getMessage());
            return result;
        }
    }
}
