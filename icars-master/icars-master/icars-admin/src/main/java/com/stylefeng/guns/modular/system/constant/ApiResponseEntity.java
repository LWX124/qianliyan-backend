package com.stylefeng.guns.modular.system.constant;

public class ApiResponseEntity<T> {

    public ApiResponseEntity(T data) {
        this.data = data;
        this.errorCode = 0;
        this.errorMsg = "成功";
    }

    public ApiResponseEntity(int errorCode,String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    /**
     * 错误码 非0即代表错误
     */
    private int errorCode;

    /**
     * 错误提示信息
     */
    private String errorMsg;

    /**
     * 数据
     */
    private Object data;


    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ApiResponseEntity(int errorCode, String errorMsg, Object data) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.data = data;
    }

    public ApiResponseEntity() {
    }

    @Override
    public String toString() {
        return "ApiResponseEntity{" +
                "errorCode=" + errorCode +
                ", errorMsg='" + errorMsg + '\'' +
                ", data=" + data +
                '}';
    }
}
