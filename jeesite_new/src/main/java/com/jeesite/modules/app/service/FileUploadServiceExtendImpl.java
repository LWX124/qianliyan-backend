package com.jeesite.modules.app.service;

import com.jeesite.modules.app.dao.AppBUserDao;
import com.jeesite.modules.file.entity.FileEntity;
import com.jeesite.modules.file.entity.FileUpload;
import com.jeesite.modules.file.service.support.FileUploadServiceExtendSupport;
import com.obs.services.ObsClient;
import com.obs.services.model.ObjectMetadata;
import org.apache.http.entity.ContentType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FileUploadServiceExtendImpl extends FileUploadServiceExtendSupport {

    @Resource
    private AppBUserDao appBUserDao;

    //OBS图片访问域名
    public static String endPoint = "obs.cn-southwest-2.myhuaweicloud.com";
    public static String accessKeyId = "IRRNEYTCG1WNG35ST0FP";
    public static String accessKeySecret = "BMIU9nhzo9TSFu5SL09utdGP1xeXsWEFXGFoqa46";
    public static String bucketName = "watermark-a33d";
    public static String oss_domain = "https://watermark-a33d.obs.cn-southwest-2.myhuaweicloud.com/";

    // 创建ObsClient实例
    public static ObsClient obsClient = new ObsClient(accessKeyId, accessKeySecret, endPoint);

    public static String obsUpload(MultipartFile file, String id) throws IOException {
        //CommonsMultipartFile file = (CommonsMultipartFile)multipartFile;
        String fileName = id;
        if(file!=null && !"".equals(file.getOriginalFilename()) && file.getOriginalFilename()!=null){
            InputStream content = file.getInputStream();//获得指定文件的输入流
            ObjectMetadata meta = new ObjectMetadata();// 创建上传Object的Metadata
            meta.setContentLength(file.getSize());  // 必须设置ContentLength
            String originalFilename = file.getOriginalFilename();
          //  fileName =  UUID.randomUUID().toString().replaceAll("-","") + originalFilename.subSequence(originalFilename.lastIndexOf("."), originalFilename.length());
            obsClient.putObject(bucketName,"img/"+fileName,content,meta);// 上传Object.
            if(fileName != null && !"".equals(fileName)){
//                System.out.println(fileName);
                fileName = oss_domain+"img/"+fileName;
            }
        }
        return fileName;
    }

    public String uploadWebimg(MultipartFile file,String id) throws IOException {
        return obsUpload(file, id);
    }

    /**
     * 验证文件是否真实的存在
     *
     * @param fileEntity 文件实体信息
     * @return 文件存在true，不存在false
     */
    public boolean fileExists(FileEntity fileEntity) {
        String path = fileEntity.getFileRealPath();
        File localFile = new File(path);
        return localFile.exists();
    }

    /**
     * 上传文件，首次上传文件都调用（保存到文件实体表之前调用）
     *
     * @param fileEntity                   文件实体信息
     * @param fileEntity.getFileRealPath() 文件实际磁盘路径
     */
    public void uploadFile(FileEntity fileEntity) {

        String path = fileEntity.getFileRealPath();
        String fileId = fileEntity.getFileId();
        String fileExtension = fileEntity.getFileExtension();

        System.out.println(fileId);
        //通过fileiD查询到biztype

        File file = new File(path);
        FileInputStream fileInputStream = null;
        MultipartFile multipartFile = null;
        try {
            fileInputStream = new FileInputStream(file);
            multipartFile = new MockMultipartFile(file.getName(),file.getName(),
                    ContentType.APPLICATION_OCTET_STREAM.toString(),fileInputStream);
            String id = fileEntity.getId();
            String s = obsUpload(multipartFile,id);
            appBUserDao.insertFileUrl(id,s);
//            appBUserDao.findJsUpload(id,s);
            logger.error("### 华为云返回地址 ### s ={}", s);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("### 上传华为云失败 ### e={}", e);
        }

        putObjectToBucket(path, fileId + "." + fileExtension);


    }

    /**
     * 文件上传至本地
     */
    public void putObjectToBucket(String path, String fileName) {
        String prePath = "/home/ftpuser/www/sys/";
        try {
            File file = new File(path); //源文件
            if (file.renameTo(new File(prePath + file.getName()))) {//源文件移动至目标文件目录
                logger.info("### 移动文件成功###");
            } else {
                logger.error("### 移动文件失败### path={};### prePath={};### file.getName()={}", path, prePath, file.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文件下载的URL地址
     *
     * @param fileUpload 文件上传的信息，包括文件实体
     * @return 无文件下载地址，则返回null，方便后续处理
     */
    public String getFileUrl(FileUpload fileUpload) {
        FileEntity fileEntity = fileUpload.getFileEntity();
        String id = fileEntity.getFileId();
        String fileExtension = fileEntity.getFileExtension();
//        String str = "http://chejiqiche.com/sys/" + id + "." + fileExtension;
        String str = oss_domain+"img/" + fileEntity.getId() ;

        str = str.replace("/home/ftpuser/www", "");
        return str;
    }

}
