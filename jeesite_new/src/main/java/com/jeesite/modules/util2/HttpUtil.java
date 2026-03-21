package com.jeesite.modules.util2;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class HttpUtil {


    private final static Logger log = LoggerFactory.getLogger(HttpUtil.class);


    private static RequestConfig config;
    private static HttpUtil httpUtil;

    public static HttpUtil createHttpClientUtil(int connTimeout, int reqTimeout) {
        config = RequestConfig.custom().setConnectTimeout(connTimeout).setSocketTimeout(reqTimeout)
                .build();
        httpUtil = new HttpUtil();
        return httpUtil;
    }

    private HttpUtil() {
    }

    public static String requestWithHttpsWithP12(String url, String reqXml, String mchId, String cerPath) {
        try {
            ContentType contentType = ContentType.create("application/xml", "UTF-8");
            HttpUtil httpClient = createHttpClientUtil(8000, 8000);
            return httpClient.invokeRequestWithP12(url, reqXml, true, contentType, mchId, cerPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private KeyStore keyStore = null;

    /**
     * 执行后台Http请求
     *
     * @param reqUrl      请求URl
     * @param params      请求参数
     * @param contentType contentType
     * @param sslFlg      是否需要ssl加密
     * @return
     */
    public String invokeRequestWithP12(String reqUrl, String params, boolean sslFlg,
                                       ContentType contentType, String mchId, String cerPath) {
        CloseableHttpClient httpClient = new DefaultHttpClient();

        try {
            if (keyStore == null) {
                keyStore = initCert(mchId, cerPath);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (sslFlg) {
            try {
                registerSSlp12(httpClient, keyStore, mchId);
            } catch (NoSuchAlgorithmException e) {
                log.error("注册SSL失败", e);
            } catch (KeyManagementException e) {
                log.error("注册SSL失败", e);
            }
        }
        HttpPost httpPost = new HttpPost(reqUrl);
        CloseableHttpResponse response = null;
        try {
            httpPost.setConfig(config);
            httpPost.setEntity(new StringEntity(params, contentType));
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String value = EntityUtils.toString(entity, contentType.getCharset());
            EntityUtils.consume(entity);
            return value;
        } catch (Exception e) {
            log.error("http请求异常", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    log.error("response IOException", e);
                }
            }
            httpPost.releaseConnection();
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error("httpClient IOException", e);
            }
        }

        return "";
    }


    private static void registerSSlp12(HttpClient httpclient, KeyStore keyStore, String mch) throws NoSuchAlgorithmException,
            KeyManagementException {
        SSLContext sslcontext = SSLContext.getInstance("SSL");
        X509TrustManager tm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] arg0,
                                           String arg1) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0,
                                           String arg1) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        String alg = KeyManagerFactory.getDefaultAlgorithm();
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(alg);
        try {
            keyManagerFactory.init(keyStore, mch.toCharArray());
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        KeyManager[] kms = keyManagerFactory.getKeyManagers();

        sslcontext.init(kms, new TrustManager[]{tm}, null);
        SSLSocketFactory sf = new SSLSocketFactory(sslcontext,
                SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        Scheme https = new Scheme("https", 443, sf);
        httpclient.getConnectionManager().getSchemeRegistry().register(https);
    }

    /**
     * 加载证书
     *
     * @param mchId    商户ID
     * @param certPath 证书位置
     * @throws Exception
     */
    private static KeyStore initCert(String mchId, String certPath) throws Exception {

        // 证书密码，默认为商户ID
        String key = mchId;
        // 证书的路径
//        String path = certPath;
        // 指定读取证书格式为PKCS12
        KeyStore keyStore = KeyStore.getInstance("PKCS12");

//        FileInputStream instream = new FileInputStream(new File("/opt/deploy/hb/manage/apiclient_cert.p12"));
        FileInputStream instream = new FileInputStream(new File(certPath));

        try {
            // 指定PKCS12的密码(商户ID)
            keyStore.load(instream, key.toCharArray());

        } catch (Exception e) {
            log.error("keyStoreException", e);
        } finally {
            instream.close();
        }
        return keyStore;
    }
}
