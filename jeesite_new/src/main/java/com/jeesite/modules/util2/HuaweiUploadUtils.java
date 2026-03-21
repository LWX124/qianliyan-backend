package com.jeesite.modules.util2;

import com.jeesite.modules.app.service.BizAccidentService;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;


public class HuaweiUploadUtils {

    private final static Logger logger = LoggerFactory.getLogger(HuaweiUploadUtils.class);

    public static void uploadToHuawei(String fileName,InputStream inputStream){
        String accessKey = "IRRNEYTCG1WNG35ST0FP"; //取值为获取的AK
        String securityKey = "BMIU9nhzo9TSFu5SL09utdGP1xeXsWEFXGFoqa46";  //取值为获取的SK

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        String requesttime = DateUtils.formateDate(System.currentTimeMillis());
        HttpPut httpPut = new HttpPut("https://obs-4f4a.obs.cn-southwest-2.myhuaweicloud.com/video/" + fileName);

        /** 根据请求计算签名 **/
        String contentMD5 = "";
        String contentType = "text/plain";
        String canonicalizedHeaders = "";
        String canonicalizedResource = "/obs-4f4a/video/" + fileName;
        // Content-MD5 、Content-Type 没有直接换行， data格式为RFC 1123，和请求中的时间一致
        String canonicalString = "PUT" + "\n" + contentMD5 + "\n" + contentType + "\n" + requesttime + "\n" + canonicalizedHeaders + canonicalizedResource;
        System.out.println("StringToSign:[" + canonicalString + "]");
        String signature = null;

        try {
            signature = Signature.signWithHmacSha1(securityKey, canonicalString);
            // 上传的文件目录
            InputStreamEntity entity = new InputStreamEntity(inputStream);
            httpPut.setEntity(entity);

            httpPut.addHeader("Date", requesttime);

            // 增加签名头域 Authorization: OBS AccessKeyID:signature
            httpPut.addHeader("Authorization", "OBS " + accessKey + ":" + signature);
            httpPut.addHeader("Content-Type", "text/plain");
            httpResponse = httpClient.execute(httpPut);

            // 打印发送请求信息和收到的响应消息
            logger.info("华为云 obs ：Request Message:", httpPut.getRequestLine());
            for (Header header : httpPut.getAllHeaders()) {
                System.out.println(header.getName() + ":" + header.getValue());
            }
            logger.info("华为云 obs ：Response Message:", httpResponse.getStatusLine());
            for (Header header : httpResponse.getAllHeaders()) {
                System.out.println(header.getName() + ":" + header.getValue());
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpResponse.getEntity().getContent()));

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();

            // print result
            System.out.println(response.toString());


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
