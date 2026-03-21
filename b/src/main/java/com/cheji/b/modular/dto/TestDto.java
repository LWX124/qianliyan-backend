package com.cheji.b.modular.dto;

import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TestDto {

    @Data
    public static
    class SignIn{
        private Date signInDat;

        public SignIn(){

        }

        public SignIn (String day)throws ParseException{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            signInDat = sdf.parse(day);
        }
    }

    public static int getinds(List<SignIn> signInList){
        return 1;
    }


}
