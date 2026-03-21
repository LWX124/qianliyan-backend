package com.jeesite.modules.util2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.TreeMap;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;


/**
 * http请求
 * @author 小郑
 * @date 2018年01月22日
 * */
public class HttpClientCustomSSL {
	/**
	 * httpClient 请求获取公钥
	 * @param parms
	 * @return  
	 * @throws Exception
	 */
	public static String httpClientResultGetPublicKey(String xml) throws Exception{
		StringBuffer reultBuffer = new StringBuffer();
		/*
			注意这里的readCustomerSSL是另一个方法，在下面贴出来\
			读取证书的类
		*/
		SSLConnectionSocketFactory sslsf = ReadSSl.getInstance().readCustomSSL();

		HttpPost httpPost = new HttpPost("https://fraud.mch.weixin.qq.com/risk/getpublickey");
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        StringEntity myEntity = new org.apache.http.entity.StringEntity(xml,"UTF-8");
        myEntity.setContentType("text/xml;charset=UTF-8");
        myEntity.setContentEncoding("UTF-8");
        httpPost.setHeader("Content-Type", "text/xml; charset=UTF-8");
        httpPost.setEntity(myEntity);
        
        CloseableHttpResponse response      = null;
        InputStream inputStream		        = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader       = null;
        try {
        	response = httpclient.execute(httpPost);
        	HttpEntity entity = response.getEntity();
			if (entity!=null){
				inputStream = entity.getContent();
				inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
				bufferedReader = new BufferedReader(inputStreamReader);
				String str = null;
				while ((str = bufferedReader.readLine()) != null) {
					reultBuffer.append(str);
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{

			httpclient.close();
			response.close();
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
		}
     
        return reultBuffer.toString();
	}
}