package com.cheji.web.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UseDateUtils {

    public static String backStringDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
}
