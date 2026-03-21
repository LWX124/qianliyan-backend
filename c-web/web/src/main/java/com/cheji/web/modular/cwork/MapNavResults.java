package com.cheji.web.modular.cwork;

public class MapNavResults {

    private String distance;//行驶距离
    private String duration;//行驶时间（单位：秒）
    private String tolls;//道路收费（单位：元）
    public String getDistance() {
        return distance;
    }
    public void setDistance(String distance) {
        this.distance = distance;
    }
    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }
    public String getTolls() {
        return tolls;
    }
    public void setTolls(String tolls) {
        this.tolls = tolls;
    }
    @Override
    public String toString() {
        return "MapNavResults [distance=" + distance + ", duration=" + duration
                + ", tolls=" + tolls + "]";
    }

}
