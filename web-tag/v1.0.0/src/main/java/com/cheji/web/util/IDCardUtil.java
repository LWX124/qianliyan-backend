package com.cheji.web.util;

import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.ocr.v1.OcrClient;
import com.huaweicloud.sdk.ocr.v1.model.IdCardRequestBody;
import com.huaweicloud.sdk.ocr.v1.model.IdCardResult;
import com.huaweicloud.sdk.ocr.v1.model.RecognizeIdCardRequest;
import com.huaweicloud.sdk.ocr.v1.model.RecognizeIdCardResponse;
import com.huaweicloud.sdk.ocr.v1.region.OcrRegion;


public class IDCardUtil {
    private static final String ak = "RFFC4N4Z0VKXDUJYMQRR";
    private static final String sk = "kVPfaESAohuzIsSlMrtOyIw5guZrfjG0DWSWOd0C";

    public static IdCardResult checkIDCardByUrl(String url) {
        ICredential auth = new BasicCredentials()
                .withAk(ak)
                .withSk(sk);

        OcrClient client = OcrClient.newBuilder()
                .withCredential(auth)
                .withRegion(OcrRegion.valueOf("cn-north-4"))
                .build();
        RecognizeIdCardRequest request = new RecognizeIdCardRequest();
        IdCardRequestBody body = new IdCardRequestBody();
        body.withUrl(url);
        request.withBody(body);
        RecognizeIdCardResponse response = client.recognizeIdCard(request);
        IdCardResult result = response.getResult();
        return result;
    }

    public static IdCardResult checkIDCardBy64(String base) {
        ICredential auth = new BasicCredentials()
                .withAk(ak)
                .withSk(sk);

        OcrClient client = OcrClient.newBuilder()
                .withCredential(auth)
                .withRegion(OcrRegion.valueOf("cn-north-4"))
                .build();
        RecognizeIdCardRequest request = new RecognizeIdCardRequest();
        IdCardRequestBody body = new IdCardRequestBody();
        body.setImage(base);
        request.withBody(body);
        RecognizeIdCardResponse response = client.recognizeIdCard(request);
        IdCardResult result = response.getResult();
        return result;
    }

}
