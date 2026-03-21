package com.stylefeng.guns.core.util;

import com.stylefeng.guns.modular.system.constant.Location;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class GisUtil {
    private static final double EARTH_RADIUS = 6378.137;//地球半径

    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }
    //根据两个点的左边测算出距离
    public static double getDistance(Location location1, Location location2)
    {
        double radLat1 = rad(location1.getLat().doubleValue());
        double radLat2 = rad(location2.getLat().doubleValue());
        double a = radLat1 - radLat2;
        double b = rad(location1.getLng().doubleValue()) - rad(location2.getLng().doubleValue());
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }
    //找出基本点和数组中最近的一个点

    /**
     * 根据经纬度
     * @param location1
     * @param locations
     * @return
     */
    public static List<Location> getSortList(Location location1, List<Location> locations)
    {
        Location lo = new Location();
        Iterator<Location> it = locations.iterator();
        while (it.hasNext()){
            Location item = it.next();
            lo.lat = item.lat;
            lo.lng = item.lng;
            item.distance = getDistance(location1, lo);
            if(item.distance > 20){
                it.remove();
            }
        }
        locations.sort(new Comparator<Location>() {
            @Override
            public int compare(Location o1, Location o2) {
                if(o1.getDistance() >= o2.getDistance()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        return locations;
    }
//    public static void main(String[] args) {
//        System.out.println(getDistance(new Location(null,null,new BigDecimal(30.590098),new BigDecimal(104.128941)),new Location(null,null,new BigDecimal(30.596425),new BigDecimal(104.143009))));
//    }
}
