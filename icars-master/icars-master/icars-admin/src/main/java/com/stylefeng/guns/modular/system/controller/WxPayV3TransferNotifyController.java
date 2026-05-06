package com.stylefeng.guns.modular.system.controller;

import com.stylefeng.guns.modular.system.constant.AccdStatus;
import com.stylefeng.guns.modular.system.model.BizWxpayBill;
import com.stylefeng.guns.modular.system.service.IAccdService;
import com.stylefeng.guns.modular.system.service.IBizWxpayBillService;
import com.stylefeng.guns.wxpay.WxPayV3TransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class WxPayV3TransferNotifyController {

    private static final Logger log = LoggerFactory.getLogger(WxPayV3TransferNotifyController.class);

    @Resource
    private WxPayV3TransferService wxPayV3TransferService;

    @Resource
    private IBizWxpayBillService bizWxpayBillService;

    @Resource
    private IAccdService accdService;

    @RequestMapping(value = "/api/v1/wxpay/transferNotify", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> transferNotify(HttpServletRequest request) {
        Map<String, String> response = new HashMap<>();
        try {
            // 1. 读取请求体
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            String body = sb.toString();
            log.info("V3转账回调通知收到 body={}", body);

            // 2. 解析外层 JSON 获取加密数据
            String ciphertext = extractJsonString(body, "ciphertext");
            String nonce = extractJsonString(body, "nonce");
            String associatedData = extractJsonString(body, "associated_data");

            if (ciphertext == null || nonce == null) {
                log.error("V3转账回调通知缺少加密字段");
                response.put("code", "FAIL");
                response.put("message", "缺少加密字段");
                return response;
            }

            // 3. AES-256-GCM 解密
            String apiV3Key = wxPayV3TransferService.getApiV3Key();
            String decrypted = decryptAesGcm(ciphertext, nonce, associatedData, apiV3Key);
            log.info("V3转账回调通知解密 decrypted={}", decrypted);

            // 4. 解析明文 JSON
            String outBillNo = extractJsonString(decrypted, "out_bill_no");
            String state = extractJsonString(decrypted, "state");

            if (outBillNo == null || state == null) {
                log.error("V3转账回调通知解密后缺少关键字段 outBillNo={} state={}", outBillNo, state);
                response.put("code", "FAIL");
                response.put("message", "缺少关键字段");
                return response;
            }

            // 5. 查找账单记录
            BizWxpayBill bill = bizWxpayBillService.selectByOutBillNo(outBillNo);
            if (bill == null) {
                log.warn("V3转账回调通知找不到账单 outBillNo={}", outBillNo);
                response.put("code", "SUCCESS");
                response.put("message", "成功");
                return response;
            }

            // 6. 幂等检查
            if (bill.getTransferStatus() != null && bill.getTransferStatus() == 2) {
                log.info("V3转账回调通知跳过已完成账单 outBillNo={}", outBillNo);
                response.put("code", "SUCCESS");
                response.put("message", "成功");
                return response;
            }

            // 7. 根据 state 更新
            if ("SUCCESS".equals(state)) {
                bill.setTransferStatus(2);
                bill.setStatus(0);
                bill.setPayTime(new Date());
                bill.updateById();
                accdService.updateStatus(bill.getAccid(), AccdStatus.REWARD_CLAIMED.getCode());
                log.info("V3转账回调通知: 用户已领取 outBillNo={} accid={}", outBillNo, bill.getAccid());
            } else if ("FAIL".equals(state) || "CANCELLED".equals(state)) {
                String failReason = "CANCELLED".equals(state) ? "用户超时未领取" : "转账失败";
                bill.setTransferStatus(3);
                bill.setStatus(1);
                bill.setFailReason(failReason);
                bill.updateById();
                accdService.updateStatus(bill.getAccid(), AccdStatus.TRANSFER_FAILED.getCode());
                log.info("V3转账回调通知: {} outBillNo={} accid={}", failReason, outBillNo, bill.getAccid());
            } else {
                log.warn("V3转账回调通知: 未知状态 state={} outBillNo={}", state, outBillNo);
            }

            response.put("code", "SUCCESS");
            response.put("message", "成功");
        } catch (Exception e) {
            log.error("V3转账回调通知处理失败", e);
            response.put("code", "FAIL");
            response.put("message", "处理失败");
        }
        return response;
    }

    private String decryptAesGcm(String ciphertext, String nonce, String associatedData, String apiV3Key) throws Exception {
        byte[] key = apiV3Key.getBytes(StandardCharsets.UTF_8);
        byte[] iv = nonce.getBytes(StandardCharsets.UTF_8);
        byte[] ciphertextBytes = Base64.getDecoder().decode(ciphertext);
        byte[] aad = associatedData != null ? associatedData.getBytes(StandardCharsets.UTF_8) : new byte[0];

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
        cipher.updateAAD(aad);
        byte[] decrypted = cipher.doFinal(ciphertextBytes);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    private String extractJsonString(String json, String key) {
        if (json == null) return null;
        String searchKey = "\"" + key + "\"";
        int idx = json.indexOf(searchKey);
        if (idx < 0) return null;
        int colon = json.indexOf(":", idx);
        if (colon < 0) return null;
        // skip whitespace
        int start = colon + 1;
        while (start < json.length() && json.charAt(start) == ' ') start++;
        if (start >= json.length()) return null;
        if (json.charAt(start) == '"') {
            int end = json.indexOf("\"", start + 1);
            if (end < 0) return null;
            return json.substring(start + 1, end);
        }
        return null;
    }
}
