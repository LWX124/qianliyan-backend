package com.stylefeng.guns.modular.system.cache;

import com.stylefeng.guns.modular.system.constant.Location;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocationCache {
    private static Map<String, Location> LOCATION_CACHE = new ConcurrentHashMap<String, Location>();

//    public static void put(){
//        LOCATION_CACHE.entrySet().
//    }
}
