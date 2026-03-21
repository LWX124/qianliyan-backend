package com.stylefeng.guns.modular.system.constant.poimaps;

import org.apache.commons.collections.map.LinkedMap;

import java.util.Map;

public class POIExportTitleMaps {
    public static Map<String, String> OPEN_CLAIM_EXPORT = new LinkedMap();
    public static Map<String, String> OPEN_CLAIM_PARTNER_EXPORT = new LinkedMap();
    static {
        initOpenClaimExportMap();
        initOpenClaimPartnerExportMap();
    }

    public static void initOpenClaimExportMap(){
        OPEN_CLAIM_EXPORT.put("orderno","订单号");
        OPEN_CLAIM_EXPORT.put("cph","车牌号");
        OPEN_CLAIM_EXPORT.put("fullname","推送4S门店");
        OPEN_CLAIM_EXPORT.put("fixloss","定损金额");

        OPEN_CLAIM_EXPORT.put("bankcard","银行卡号");
        OPEN_CLAIM_EXPORT.put("bankUserName","开户名");
        OPEN_CLAIM_EXPORT.put("bankName","开户银行");
        OPEN_CLAIM_EXPORT.put("bankSecondName","开户银行支行");
        OPEN_CLAIM_EXPORT.put("idcard","理赔顾问身份证");

        OPEN_CLAIM_EXPORT.put("rebateForCompany","公司提成金额");
        OPEN_CLAIM_EXPORT.put("rebateForEmp","业务员提成金额");
        OPEN_CLAIM_EXPORT.put("phone","客户手机号");
        OPEN_CLAIM_EXPORT.put("name","客户姓名");
        OPEN_CLAIM_EXPORT.put("statusName","订单状态");
        OPEN_CLAIM_EXPORT.put("claimerPhone","上报人手机号");
        OPEN_CLAIM_EXPORT.put("payBillNoForClaim","内部付款凭证号");
        OPEN_CLAIM_EXPORT.put("payBillNo","付款凭证号");
        OPEN_CLAIM_EXPORT.put("qcpp","汽车品牌");
        OPEN_CLAIM_EXPORT.put("cjh","车架号");
        OPEN_CLAIM_EXPORT.put("address","事故地址");
        OPEN_CLAIM_EXPORT.put("createtime","开单时间");
    }

    public static void initOpenClaimPartnerExportMap(){
        OPEN_CLAIM_PARTNER_EXPORT.put("orderno","订单号");
        OPEN_CLAIM_PARTNER_EXPORT.put("cph","车牌号");
        OPEN_CLAIM_PARTNER_EXPORT.put("fullname","推送4S门店");
        OPEN_CLAIM_PARTNER_EXPORT.put("fixloss","定损金额");
        OPEN_CLAIM_PARTNER_EXPORT.put("phone","客户手机号");
        OPEN_CLAIM_PARTNER_EXPORT.put("name","客户姓名");
        OPEN_CLAIM_PARTNER_EXPORT.put("statusName","订单状态");
        OPEN_CLAIM_PARTNER_EXPORT.put("qcpp","汽车品牌");
        OPEN_CLAIM_PARTNER_EXPORT.put("cjh","车架号");
        OPEN_CLAIM_PARTNER_EXPORT.put("address","事故地址");
        OPEN_CLAIM_PARTNER_EXPORT.put("createtime","开单时间");
    }
}
