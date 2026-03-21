package com.cheji.web.util;

import com.cheji.web.constant.HuaweiConstant;
import com.cheji.web.modular.dto.AppDrivingCardDTO;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.huaweicloud.sdk.ocr.v1.OcrClient;
import com.huaweicloud.sdk.ocr.v1.model.*;
import com.huaweicloud.sdk.ocr.v1.region.OcrRegion;
import org.springframework.beans.BeanUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class OCRUtil {
    private static final String ak = HuaweiConstant.HUAWEI_CONSTANT_AK;
    private static final String sk = HuaweiConstant.HUAWEI_CONSTANT_SK;

    public static AppDrivingCardDTO checkDrivingByUrl(String url) throws ServiceResponseException, ParseException {
        ICredential auth = new BasicCredentials()
                .withAk(ak)
                .withSk(sk);

        OcrClient client = OcrClient.newBuilder()
                .withCredential(auth)
                .withRegion(OcrRegion.valueOf("cn-north-4"))
                .build();
        RecognizeVehicleLicenseRequest request = new RecognizeVehicleLicenseRequest();
        VehicleLicenseRequestBody body = new VehicleLicenseRequestBody();
        body.withUrl(url);
        request.withBody(body);
        RecognizeVehicleLicenseResponse response = client.recognizeVehicleLicense(request);
        VehicleLicenseResult result = response.getResult();
        AppDrivingCardDTO appDrivingCardEntity = new AppDrivingCardDTO();
        BeanUtils.copyProperties(result,appDrivingCardEntity);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if(result.getRegisterDate() != null){
            String registerDate = result.getRegisterDate();
            Date date = simpleDateFormat.parse(registerDate);
            appDrivingCardEntity.setRegisterDate(date);
        }
        if(result.getIssueDate() != null){
            String issueDate = result.getIssueDate();
            Date issue = simpleDateFormat.parse(issueDate);
            appDrivingCardEntity.setIssueDate(issue);
        }
        return appDrivingCardEntity;
    }

    public static BusinessLicenseResult checkCharter(String url) throws ServiceResponseException {
        ICredential auth = new BasicCredentials()
                .withAk(ak)
                .withSk(sk);

        OcrClient client = OcrClient.newBuilder()
                .withCredential(auth)
                .withRegion(OcrRegion.valueOf("cn-north-4"))
                .build();
        RecognizeBusinessLicenseRequest request = new RecognizeBusinessLicenseRequest();
        BusinessLicenseRequestBody body = new BusinessLicenseRequestBody();
        body.withUrl(url);
        request.withBody(body);
        RecognizeBusinessLicenseResponse response = client.recognizeBusinessLicense(request);
        BusinessLicenseResult result = response.getResult();
        return result;
    }

}