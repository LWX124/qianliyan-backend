package com.stylefeng.guns.core.util;

import com.stylefeng.guns.modular.system.constant.Location;

import java.util.ArrayList;
import java.util.List;

public class JdkUtil {
    public static List<Location> cloneLocations(List<Location> o) throws CloneNotSupportedException {
        List<Location> v = new ArrayList<Location>();
        for(Location var : o){
            v.add(var.clone());
        }
        return v;
    }
}
