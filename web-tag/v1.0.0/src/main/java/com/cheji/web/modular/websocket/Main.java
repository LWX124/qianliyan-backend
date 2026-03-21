package com.cheji.web.modular.websocket;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.factory.Factory.Payment;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.kernel.util.ResponseChecker;
import com.alipay.easysdk.payment.facetoface.models.AlipayTradePrecreateResponse;
public class Main {

    public static void main(String[] args) throws Exception {
        // 1. 设置参数（全局只需设置一次）
        Factory.setOptions(getOptions());
        try {
            // 2. 发起API调用（以创建当面付收款二维码为例）
            AlipayTradePrecreateResponse response = Payment.FaceToFace()
                    .preCreate("Apple iPhone11 128G", "2234567890", "5799.00");
            // 3. 处理响应或异常
            if (ResponseChecker.success(response)) {
                System.out.println("调用成功");
            } else {
                System.err.println("调用失败，原因：" + response.msg + "，" + response.subMsg);
            }
        } catch (Exception e) {
            System.err.println("调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static Config getOptions() {
        Config config = new Config();
        config.protocol = "https";
        config.gatewayHost = "openapi.alipay.com";
        config.signType = "RSA2";
        config.appId = "2016092300580661";
        // 为避免私钥随源码泄露，推荐从文件中读取私钥字符串而不是写入源码中
        config.merchantPrivateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDhcTW7XPu2q42RQXopBCfKaT5CJufdxA2D3p7pcMygn/n/0xYi/6V/0t5QIL1HVSDeng+o8KwPuKVtZo5yb9VGgw3VoITWNSg62Y9iP72H2KhFqkDTDetCfMPsl4fj90hfZw0/+fQnd+Yq4enodFH47cjEXPHakheliNs9F8e+4awSPWytAThXS2HevUrU5/yKqybIONeCpoyWzi64BJ2st4norAR+mJJI7g/EnSWY+tXUXpslvq7Jvonq80DFio76aDEWiLNT1IEUWWFT8jgSgr3T02gK8L4BMlrLCFXCRUu+HRZOIaArh9D/ZuInR4n4nnAfR4fG+coQzg0EewmHAgMBAAECggEAbx3LrBPChWC9IDEzsu+3ZQ7GfviKJEYzmyGpFcphMXJoWG2EZa+3tO8MYFYitPwCfXdOpKgrRnxuHSQFZeY1KtIBiZ/amXIN6CO/hl+AXWquVdf6p4wevuCb+QsfEX+ajltOro/WhXWVXuGG1uouVRFueUqOcnq7aP2xgcUs0KmhvjnZOtjGpUi8iuo5XXW4ZtI5I7NKL0mG4vrhS1AkfHhQ0JnlfcSbvipfBEfi9ELfnQjOu70mGKqYnLa+Vxx7uCQtQKqU264bJ1fk7pr4yxddTKLj3Lo2MZ+BFaxy3O6gLNd+hCGsZJ9G2WvQlBgQB40RKdW/IKuGrZqWFc4wAQKBgQDzHQlX+uTsmHtzje3N1F8LZwBeuu4lCd+NEiuYrdDfQwJJNiO9ZrajEe1GP5rDjD1IUBdeEKJJ/GaiRucEh2IOUcNG9jRLqD+rmbxTAzn2KUyuk1j+9izaV/wtuSA7UEC5pSKrWa6C3vnuHe1+EdB3HLXuJtT2qXx17KBy7CRcAQKBgQDtZGGns+rcysCi+z5wXy9bRPbAtrqyc4yKWQxenAO3+Ql0rfIoYLryQIMt3QT9EMys0m7uDEh9rEIRliLgkK9NwhW+Gl8LWvlyGertQ2PsFJu8o7dR1OMvwpcsyokz39VkHqe/RCTfZp1sgR1ApJ732lyvgGe3sRlpsF1w4YKFhwKBgBvNm07zg5DHU38AoUyj2AbOzc0HpcqYmXdht5ircDLIUA+dYHL9ty9pEnCxP3rTpAKMHlAEqPq7IeW3JB5xeTZYBXQtHgf1AusWFuKLgOp1CK8CbPAjqTeDTnTQMi52XCCfERr1k66l34Fq7Mk5MlUc9HV6/WoEMhaubB8YgIQBAoGANqcVlHZna82B7APLrDM9w9VsuZFYCh1wDanSnzVr5b7/3kztoz4hzAPuL3Di5SAo/uCziTpqtgQiccoO3YRx14UHXGzV1Nds9kfhPh51srRL5nIKklxgsW4rP7ShQcAWcKZeGDqoAoatV32iLm+36XfsPrthJXkwUJS57nL8iaECgYBTgpuJlexlHzIkGQSt37TS0dpDruYeL9FO46WHWPU4y3wd+tVGrCNJ9N5PWSnfnmlcx8/93s0cKY54Q2RFb3hmHh/X4IdImMPF+FO6c/PqR8mzHbITs/AKz7+A3AjpDbg9yEWkBvYi72GGuit/HVAqCU369sdvNJJsKkVxjIfZ0Q==";
        //注：证书文件路径支持设置为文件系统中的路径或CLASS_PATH中的路径，优先从文件系统中加载，加载失败后会继续尝试从CLASS_PATH中加载
        config.merchantCertPath = "<-- 请填写您的应用公钥证书文件路径，例如：/foo/appCertPublicKey_2019051064521003.crt -->";
        config.alipayCertPath = "<-- 请填写您的支付宝公钥证书文件路径，例如：/foo/alipayCertPublicKey_RSA2.crt -->";
        config.alipayRootCertPath = "<-- 请填写您的支付宝根证书文件路径，例如：/foo/alipayRootCert.crt -->";
        //注：如果采用非证书模式，则无需赋值上面的三个证书路径，改为赋值如下的支付宝公钥字符串即可
        // config.alipayPublicKey = "<-- 请填写您的支付宝公钥，例如：MIIBIjANBg... -->";
        //可设置异步通知接收服务地址（可选）
        config.notifyUrl = "<-- 请填写您的支付类接口异步通知接收服务地址，例如：https://www.test.com/callback -->";
        //可设置AES密钥，调用AES加解密相关接口时需要（可选）
        config.encryptKey = "<-- 请填写您的AES密钥，例如：aa4BtZ4tspm2wnXLb1ThQA== -->";
        return config;
    }
}