package com.jeesite.modules.util2;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("deprecation")
public class HttpsUtil extends DefaultHttpClient {
    public final static String HTTPGET = "GET";

    public final static String HTTPPUT = "PUT";

    public final static String HTTPPOST = "POST";

    public final static String HTTPDELETE = "DELETE";

    public final static String HTTPACCEPT = "Accept";

    public final static String CONTENT_LENGTH = "Content-Length";

    public final static String CHARSET_UTF8 = "UTF-8";

    private static CloseableHttpClient httpClient = getHttpClient();

    public static CloseableHttpClient getHttpClient() {
        SSLContext sslcontext = null;
        try {
            sslcontext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

                @Override
                public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    return true;
                }
            }).build();
        } catch (KeyManagementException e) {
            return null;
        } catch (NoSuchAlgorithmException e) {
            return null;
        } catch (KeyStoreException e) {
            return null;
        }

        // Allow TLSv1, TLSv1.1, TLSv1.2 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,
                new String[] { "TLSv1", "TLSv1.1", "TLSv1.2" }, null,
                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom().setRedirectsEnabled(false).build())
                .setSSLSocketFactory(sslsf).build();

        return httpclient;
    }

    public HostnameVerifier hv = new HostnameVerifier() {
        public boolean verify(String urlHostName, SSLSession session) {
            return true;
        }
    };

    public void trustAllHttpsCertificates() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[1];
        TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    static class miTM implements TrustManager, X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType)
                throws CertificateException {
            return;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType)
                throws CertificateException {
            return;
        }
    }

    public HttpResponse doPostJson(String url, Map<String, String> headerMap, String content) {
        HttpPost request = new HttpPost(url);
        addRequestHeader(request, headerMap);

        request.setEntity(new StringEntity(content, ContentType.APPLICATION_JSON));

        return executeHttpRequest(request);
    }

    public StreamClosedHttpResponse doPostMultipartFile(String url, Map<String, String> headerMap, File file) {
        HttpPost request = new HttpPost(url);
        addRequestHeader(request, headerMap);

        FileBody fileBody = new FileBody(file);
        // Content-Type:multipart/form-data;
        // boundary=----WebKitFormBoundarypJTQXMOZ3dLEzJ4b
        HttpEntity reqEntity = (HttpEntity) MultipartEntityBuilder.create().addPart("file", fileBody).build();
        request.setEntity(reqEntity);

        return (StreamClosedHttpResponse) executeHttpRequest(request);
    }

    public StreamClosedHttpResponse doPostJsonGetStatusLine(String url, Map<String, String> headerMap, String content) {
        HttpPost request = new HttpPost(url);
        addRequestHeader(request, headerMap);

        request.setEntity(new StringEntity(content, ContentType.APPLICATION_JSON));

        HttpResponse response = executeHttpRequest(request);
        if (null == response) {
            System.out.println("The response body is null.");
        }

        return (StreamClosedHttpResponse) response;
    }

    public StreamClosedHttpResponse doPostJsonGetStatusLine(String url, String content) {
        HttpPost request = new HttpPost(url);

        request.setEntity(new StringEntity(content, ContentType.APPLICATION_JSON));

        HttpResponse response = executeHttpRequest(request);
        if (null == response) {
            System.out.println("The response body is null.");
        }

        return (StreamClosedHttpResponse) response;
    }

    public List<NameValuePair> paramsConverter(Map<String, Object> params) {
        List<NameValuePair> nvps = new LinkedList<NameValuePair>();
        Set<Map.Entry<String, Object>> paramsSet = params.entrySet();
        for (Map.Entry<String, Object> paramEntry : paramsSet) {
            nvps.add(new BasicNameValuePair(paramEntry.getKey(), paramEntry.getValue().toString()));
        }

        return nvps;
    }

    public StreamClosedHttpResponse doPostFormUrlEncodedGetStatusLine(String url) throws Exception {
        HttpPost request = new HttpPost(url);

        HttpResponse response = executeHttpRequest(request);
        if (null == response) {
            System.out.println("The response body is null.");
            throw new Exception();
        }

        return (StreamClosedHttpResponse) response;
    }

    public StreamClosedHttpResponse doPostFormUrlEncodedGetStatusLine(String url, Map<String, String> headerMap)
            throws Exception {
        HttpPost request = new HttpPost(url);
        addRequestHeader(request, headerMap);

        HttpResponse response = executeHttpRequest(request);
        if (null == response) {
            System.out.println("The response body is null.");
            throw new Exception();
        }

        return (StreamClosedHttpResponse) response;
    }

    public StreamClosedHttpResponse doPostFormUrlEncodedGetStatusLine(String url, List<NameValuePair> formParams)
            throws Exception {
        HttpPost request = new HttpPost(url);

        request.setEntity(new UrlEncodedFormEntity(formParams));

        HttpResponse response = executeHttpRequest(request);
        if (null == response) {
            System.out.println("The response body is null.");
            throw new Exception();
        }

        return (StreamClosedHttpResponse) response;
    }

    public StreamClosedHttpResponse doPostFormUrlEncodedGetStatusLine(String url, Map<String, String> headerMap,
            List<NameValuePair> formParams) throws Exception {
        HttpPost request = new HttpPost(url);
        addRequestHeader(request, headerMap);

        request.setEntity(new UrlEncodedFormEntity(formParams));

        HttpResponse response = executeHttpRequest(request);
        if (null == response) {
            System.out.println("The response body is null.");
            throw new Exception();
        }

        return (StreamClosedHttpResponse) response;
    }

    public HttpResponse doPutJson(String url, Map<String, String> headerMap, String content) {
        HttpPut request = new HttpPut(url);
        addRequestHeader(request, headerMap);

        request.setEntity(new StringEntity(content, ContentType.APPLICATION_JSON));

        return executeHttpRequest(request);
    }

    public HttpResponse doPut(String url, Map<String, String> headerMap) {
        HttpPut request = new HttpPut(url);
        addRequestHeader(request, headerMap);

        return executeHttpRequest(request);
    }

    public StreamClosedHttpResponse doPutJsonGetStatusLine(String url, Map<String, String> headerMap, String content) {
        HttpResponse response = doPutJson(url, headerMap, content);
        if (null == response) {
            System.out.println("The response body is null.");
        }

        return (StreamClosedHttpResponse) response;
    }

    public StreamClosedHttpResponse doPutGetStatusLine(String url, Map<String, String> headerMap) {
        HttpResponse response = doPut(url, headerMap);
        if (null == response) {
            System.out.println("The response body is null.");
        }

        return (StreamClosedHttpResponse) response;
    }

    public HttpResponse doGetWithParas(String url, List<NameValuePair> queryParams, Map<String, String> headerMap)
            throws Exception {
        HttpGet request = new HttpGet();
        addRequestHeader(request, headerMap);

        URIBuilder builder;
        try {
            builder = new URIBuilder(url);
        } catch (URISyntaxException e) {
            System.out.printf("URISyntaxException: {}", e);
            throw new Exception(e);
        }

        if (queryParams != null && !queryParams.isEmpty()) {
            builder.setParameters(queryParams);
        }
        request.setURI(builder.build());

        return executeHttpRequest(request);
    }

    public StreamClosedHttpResponse doGetWithParasGetStatusLine(String url, List<NameValuePair> queryParams,
            Map<String, String> headerMap) throws Exception {
        HttpResponse response = doGetWithParas(url, queryParams, headerMap);
        if (null == response) {
            System.out.println("The response body is null.");
        }

        return (StreamClosedHttpResponse) response;
    }

    public HttpResponse doDelete(String url, Map<String, String> headerMap) {
        HttpDelete request = new HttpDelete(url);
        addRequestHeader(request, headerMap);

        return executeHttpRequest(request);
    }

    public StreamClosedHttpResponse doDeleteGetStatusLine(String url, Map<String, String> headerMap) {
        HttpResponse response = doDelete(url, headerMap);
        if (null == response) {
            System.out.println("The response body is null.");
        }

        return (StreamClosedHttpResponse) response;
    }

    private static void addRequestHeader(HttpUriRequest request, Map<String, String> headerMap) {
        if (headerMap == null) {
            return;
        }

        for (String headerName : headerMap.keySet()) {
            if (CONTENT_LENGTH.equalsIgnoreCase(headerName)) {
                continue;
            }

            String headerValue = headerMap.get(headerName);
            request.addHeader(headerName, headerValue);
        }
    }

    private HttpResponse executeHttpRequest(HttpUriRequest request) {
        HttpResponse response = null;

        try {
            response = httpClient.execute(request);
        } catch (Exception e) {
            System.out.println("executeHttpRequest failed.");
            System.out.println(e.getStackTrace());
        } finally {
            try {
                response = new StreamClosedHttpResponse(response);
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            }
        }

        return response;
    }

    public String getHttpResponseBody(HttpResponse response) throws UnsupportedOperationException, IOException {
        if (response == null) {
            return null;
        }

        String body = null;

        if (response instanceof StreamClosedHttpResponse) {
            body = ((StreamClosedHttpResponse) response).getContent();
        } else {
            HttpEntity entity = response.getEntity();
            if (entity != null && entity.isStreaming()) {
                String encoding = entity.getContentEncoding() != null ? entity.getContentEncoding().getValue() : null;
                body = StreamUtil.inputStream2String(entity.getContent(), encoding);
            }
        }

        return body;
    }
}