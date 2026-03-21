package com.cheji.web.exception;


import com.alibaba.fastjson.JSONObject;
import com.cheji.web.constant.BaseErrorCode;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@ControllerAdvice + @ResponseBody
//拥有aop的实现，对controller做增强，可以在controller方法执行前后，做一些增强逻辑
//@RestControllerAdvice
public class GlobalExceptionHandler {

    //异常处理
    @ExceptionHandler(Exception.class)
    public JSONObject exceptionHandler(Exception e){
        JSONObject jsonObject = new JSONObject();
        if(e.getClass().getSimpleName().equals("AccessDeniedException")){
            jsonObject.put("msg","无访问权限");
            return jsonObject;
        }
        e.printStackTrace();
        jsonObject.put(BaseErrorCode.BASE_SYSTEM_EXCEPTION.getCode(),BaseErrorCode.BASE_SYSTEM_EXCEPTION.getMessage());
        return jsonObject;
    }

    //参数异常处理
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public JSONObject methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e){
        JSONObject jsonObject = new JSONObject();
        e.printStackTrace();
        //拼接错误信息
        StringBuilder sb = new StringBuilder();

        e.getBindingResult().getAllErrors().forEach(err->{
            sb.append(err.getDefaultMessage()).append(" ; ");
        });
        jsonObject.put("40002",sb.toString());
        return jsonObject;
    }
    //自定义异常处理
    @ExceptionHandler(GlobalCustomException.class)
    public JSONObject globalCustomExceptionHandler(GlobalCustomException e){
        JSONObject jsonObject = new JSONObject();
        e.printStackTrace();
        jsonObject.put(e.getCode(),e. getMessage());
        return jsonObject;
    }

}
