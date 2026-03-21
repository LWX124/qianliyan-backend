package com.cheji.b.modular.excep;

public class CusException extends Exception {

    private int code;

    public CusException(){

    }
    public CusException(String str){
        //此处传入的是抛出异常后显示的信息提示
        super(str);
    }
    public CusException(int code, String str){
        super(str);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
