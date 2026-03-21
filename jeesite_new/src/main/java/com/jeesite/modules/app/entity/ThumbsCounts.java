package com.jeesite.modules.app.entity;

//视频点赞总数
public class ThumbsCounts {
    private String videoId;
    private String  thumbsCounts;

    public ThumbsCounts(String videoId, String thumbsCounts) {
        this.videoId = videoId;
        this.thumbsCounts = thumbsCounts;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getThumbsCounts() {
        return thumbsCounts;
    }

    public void setThumbsCounts(String thumbsCounts) {
        this.thumbsCounts = thumbsCounts;
    }

    @Override
    public String toString() {
        return "ThumbsCounts{" +
                "videoId='" + videoId + '\'' +
                ", thumbsCounts='" + thumbsCounts + '\'' +
                '}';
    }
}
