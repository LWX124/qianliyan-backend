package com.cheji.web.pojo;

public class ApiResponseEntity<T> {

    public ApiResponseEntity(T data) {
        this.data = data;
        this.code = 0;
        this.errorMsg = "成功";
    }

    public ApiResponseEntity(int code, String errorMsg) {
        this.code = code;
        this.errorMsg = errorMsg;
    }

    /**
     * 错误码 非0即代表错误
     */
    private int code;

    /**
     * 错误提示信息
     */
    private String errorMsg;

    /**
     * 数据
     */
    private Object data;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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

    public ApiResponseEntity(int code, String errorMsg, Object data) {
        this.code = code;
        this.errorMsg = errorMsg;
        this.data = data;
    }

    public ApiResponseEntity() {
    }

    @Override
    public String toString() {
        return "ApiResponseEntity{" +
                "code=" + code +
                ", errorMsg='" + errorMsg + '\'' +
                ", data=" + data +
                '}';
    }
}
