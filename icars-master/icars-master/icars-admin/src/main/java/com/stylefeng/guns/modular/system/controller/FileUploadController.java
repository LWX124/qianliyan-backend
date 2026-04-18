package com.stylefeng.guns.modular.system.controller;

import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.stylefeng.guns.modular.system.constant.ApiResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
public class FileUploadController extends BaseController {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${qiniu.access-key}")
    private String accessKey;

    @Value("${qiniu.secret-key}")
    private String secretKey;

    @Value("${qiniu.bucket}")
    private String bucket;

    @Value("${qiniu.domain}")
    private String domain;

    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(
            Arrays.asList(".mp4", ".mov", ".avi", ".jpg", ".jpeg", ".png", ".gif", ".webp", ".heic")
    );

    @RequestMapping(value = "/file/uptoken", method = RequestMethod.GET, produces = "application/json")
    public ApiResponseEntity getUploadToken(HttpServletRequest request) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        try {
            String ext = request.getParameter("ext");
            if (ext == null || ext.isEmpty()) {
                ext = ".mp4";
            }
            if (!ext.startsWith(".")) {
                ext = "." + ext;
            }
            ext = ext.toLowerCase();

            if (!ALLOWED_EXTENSIONS.contains(ext)) {
                apiResponseEntity.setErrorCode(1004);
                apiResponseEntity.setErrorMsg("不支持的文件类型: " + ext);
                return apiResponseEntity;
            }

            String key = "img/" + UUID.randomUUID().toString().replaceAll("-", "") + ext;

            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket, key, 3600, null);

            String publicUrl = domain + key;
            String signedUrl = auth.privateDownloadUrl(publicUrl, 3600L * 24 * 365 * 10);

            Map<String, String> result = new HashMap<>();
            result.put("token", upToken);
            result.put("key", key);
            result.put("url", signedUrl);

            apiResponseEntity.setData(result);
            apiResponseEntity.setErrorCode(0);
            apiResponseEntity.setErrorMsg("");
        } catch (Exception e) {
            log.error("获取七牛上传凭证异常", e);
            apiResponseEntity.setErrorCode(1003);
            apiResponseEntity.setErrorMsg("获取上传凭证失败");
        }
        return apiResponseEntity;
    }

    @RequestMapping(value = "/file/uploadfile", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseEntity uploadFile(HttpServletRequest request) {
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        try {
            List<String> urls = new ArrayList<>();
            Configuration cfg = new Configuration(Region.autoRegion());
            UploadManager uploadManager = new UploadManager(cfg);
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            for (MultipartFile file : files) {
                String originalFilename = file.getOriginalFilename();
                String ext = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    ext = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
                }
                String fileName = "img/" + UUID.randomUUID().toString().replaceAll("-", "") + ext;

                try (InputStream inputStream = file.getInputStream()) {
                    uploadManager.put(inputStream, fileName, upToken, null, null);
                }
                String publicUrl = domain + fileName;
                String privateUrl = auth.privateDownloadUrl(publicUrl, 3600 * 24 * 365 * 10);
                urls.add(privateUrl);
            }

            apiResponseEntity.setData(urls);
            apiResponseEntity.setErrorCode(0);
            apiResponseEntity.setErrorMsg("");
        } catch (Exception e) {
            log.error("上传文件到七牛云异常", e);
            apiResponseEntity.setErrorCode(1002);
            apiResponseEntity.setErrorMsg("上传文件异常");
        }
        return apiResponseEntity;
    }
}
