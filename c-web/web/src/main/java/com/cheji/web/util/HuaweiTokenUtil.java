package com.cheji.web.util;

import com.cheji.web.constant.HuaweiConstant;
import com.huaweicloud.sdk.core.auth.GlobalCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.core.exception.ConnectionException;
import com.huaweicloud.sdk.core.exception.RequestTimeoutException;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.huaweicloud.sdk.iam.v3.IamClient;
import com.huaweicloud.sdk.iam.v3.model.*;
import com.huaweicloud.sdk.iam.v3.region.IamRegion;

import java.util.ArrayList;
import java.util.List;

public class HuaweiTokenUtil {
    private static final String ak = HuaweiConstant.HUAWEI_CONSTANT_AK;
    private static final String sk = HuaweiConstant.HUAWEI_CONSTANT_SK;
    public static String getToken(){
        ICredential auth = new GlobalCredentials()
                .withAk(ak)
                .withSk(sk);

        IamClient client = IamClient.newBuilder()
                .withCredential(auth)
                .withRegion(IamRegion.valueOf("cn-north-4"))
                .build();
        KeystoneCreateUserTokenByPasswordRequest request = new KeystoneCreateUserTokenByPasswordRequest();
        KeystoneCreateUserTokenByPasswordRequestBody body = new KeystoneCreateUserTokenByPasswordRequestBody();
        AuthScopeDomain domainScope = new AuthScopeDomain();
        domainScope.withId("b0657d93cde246d99eada576e3d3ec72");
        AuthScope scopeAuth = new AuthScope();
        scopeAuth.withDomain(domainScope);
        PwdPasswordUserDomain domainUser = new PwdPasswordUserDomain();
        domainUser.withName("chejiqiche");
        PwdPasswordUser userPassword = new PwdPasswordUser();
        userPassword.withDomain(domainUser)
                .withName("cheji")
                .withPassword("Chejiqiche500500");
        PwdPassword passwordIdentity = new PwdPassword();
        passwordIdentity.withUser(userPassword);
        List<PwdIdentity.MethodsEnum> listIdentityMethods = new ArrayList<>();
        listIdentityMethods.add(PwdIdentity.MethodsEnum.fromValue("password"));
        PwdIdentity identityAuth = new PwdIdentity();
        identityAuth.withMethods(listIdentityMethods)
                .withPassword(passwordIdentity);
        PwdAuth authbody = new PwdAuth();
        authbody.withIdentity(identityAuth)
                .withScope(scopeAuth);
        body.withAuth(authbody);
        request.withBody(body);
        try {
            KeystoneCreateUserTokenByPasswordResponse response = client.keystoneCreateUserTokenByPassword(request);
            return response.getXSubjectToken();
        } catch (ConnectionException e) {
            e.printStackTrace();
        } catch (RequestTimeoutException e) {
            e.printStackTrace();
        } catch (ServiceResponseException e) {
            e.printStackTrace();
            System.out.println(e.getHttpStatusCode());
            System.out.println(e.getErrorCode());
            System.out.println(e.getErrorMsg());
        }
        return null;
    }
}
