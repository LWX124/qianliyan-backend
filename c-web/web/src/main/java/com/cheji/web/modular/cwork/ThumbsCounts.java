package com.cheji.web.modular.cwork;

//视频点赞总数
public class ThumbsCounts {
    private String videoId;
    private Integer thumbsCounts;

    public ThumbsCounts(String videoId, Integer thumbsCounts) {
        this.videoId = videoId;
        this.thumbsCounts = thumbsCounts;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public Integer getThumbsCounts() {
        return thumbsCounts;
    }

    public void setThumbsCounts(Integer thumbsCounts) {
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
