package com.cheji.web.exception;

import lombok.Data;

//自定义异常
@Data
public class GlobalCustomException extends  RuntimeException{

    //错误码
    private String code;

    public GlobalCustomException(String message,String code){
        super(message);
        this.code = code;
    }

    public GlobalCustomException(String message){
        super(message);
    }
}

//throw new GlobalCustomException("用户名不能为空");
//e.getMessage -> 用户名不能为空