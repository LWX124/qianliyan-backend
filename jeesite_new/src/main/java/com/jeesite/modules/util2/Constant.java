/*
 * File Name: com.huawei.utils.Constant.java
 *
 * Copyright Notice:
 *      Copyright  1998-2008, Huawei Technologies Co., Ltd.  ALL Rights Reserved.
 *
 *      Warning: This computer software sourcecode is protected by copyright law
 *      and international treaties. Unauthorized reproduction or distribution
 *      of this sourcecode, or any portion of it, may result in severe civil and
 *      criminal penalties, and will be prosecuted to the maximum extent
 *      possible under the law.
 */
package com.jeesite.modules.util2;

public class Constant {

    //please replace the IP and Port, when you use the demo.
    public static final String BASE_URL = "https://117.78.29.66:10443";

    //please replace the appId and secret, when you use the demo.
    public static final String CLICK2CALL_APPID = "3Tg5rRR0zTo0LrX7i94UB865Vdtq";
    public static final String CLICK2CALL_SECRET = "QFJvEEvoEoJ59B2jm841nfLdjmF7";
    public static final String CALLNOTIFYVERIFY_APPID = "clEbbLcreTLJit2SOu0Z1R86sMSN";
    public static final String CALLNOTIFYVERIFY_SECRET = "d6ZMQ1Ka99bcsC6pOC79iWT1oNvA";

    /*
     *IP and port of callback url.
     *please replace the IP and Port of your Application deployment environment address, when you use the demo.
     */
    public static final String CALLBACK_BASE_URL = "http://192.88.88.88:9999";

    /*
     * complete callback url
     * please replace uri, when you use the demo.
     */
    public static final String STATUS_CALLBACK_URL = CALLBACK_BASE_URL + "/status";
    public static final String FEE_CALLBACK_URL = CALLBACK_BASE_URL + "/fee";

    //Paths of certificates.
    public static String KEY_PATH = "/ssl/key.pem";
    public static String CERT_PATH = "/ssl/key-cert.pem";

    //*************************** The following constants do not need to be modified *********************************//

    /*
     * request header
     * 1. HEADER_APP_AUTH
     */
    public static final String HEADER_APP_AUTH = "Authorization";
    
    /*
     * Application Access Security:
     * 1. APP_AUTH
     * 2. REFRESH_TOKEN
     */
    public static final String APP_AUTH = BASE_URL + "/rest/fastlogin/v1.0";
    public static final String REFRESH_TOKEN = BASE_URL + "/omp/oauth/refresh";
    public static final String DELETE_AUTH = BASE_URL + "/rest/logout/v1.0";
    public static final String REFRESH_OCEANSTOR = BASE_URL + "/rest/refreshkey/v2.0";

    /*
     * Voice Call:
     * 1. VOICE_CALL_TEST
     * 2. VOICE_CALL_COMERCIAL
     * 3. VOICE_VERIFICATION_TEST
     * 4. VOICE_VERIFICATION_COMERCIAL
     * 5. CALL_NOTIFY_TEST
     * 6. CALL_NOTIFY_COMERCIAL
     * 7. VOICE_FILE_DOWNLOAD
     */
    public static final String VOICE_CALL_TEST = BASE_URL + "/sandbox/rest/httpsessions/click2Call/v2.0";
    public static final String VOICE_CALL_COMERCIAL = BASE_URL + "/rest/httpsessions/click2Call/v2.0";
    public static final String VOICE_VERIFICATION_TEST = BASE_URL + "/sandbox/rest/httpsessions/callVerify/v1.0";
    public static final String VOICE_VERIFICATION_COMERCIAL = BASE_URL + "/rest/httpsessions/callVerify/v1.0";    
    public static final String CALL_NOTIFY_TEST = BASE_URL + "/sandbox/rest/httpsessions/callnotify/";
    public static final String CALL_NOTIFY_COMERCIAL = BASE_URL + "/rest/httpsessions/callnotify/";
    public static final String VOICE_FILE_DOWNLOAD = BASE_URL + "/rest/provision/voice/record/v1.0";
}