package com.cheji.web.constant;

public enum BaseErrorCode {

    //系统内部错误
    BASE_SYSTEM_EXCEPTION("系统内部异常,请联系管理员","500");

    //错误信息
    private String message;

    //错误码
    private String code;

    BaseErrorCode(String message , String code){
        this.message = message ;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }
}
