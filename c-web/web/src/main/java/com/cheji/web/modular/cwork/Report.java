package com.cheji.web.modular.cwork;

public class Report {
    private String reportId;        //举报数据id
    private String count;           //内容
    private String videoId;         //视频id
    private String userId;          //用户id
    private String firstImg;
    private String secondImg;
    private String thirdImg;
    private String fourthImg;

    public String getFourthImg() {
        return fourthImg;
    }

    public void setFourthImg(String fourthImg) {
        this.fourthImg = fourthImg;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstImg() {
        return firstImg;
    }

    public void setFirstImg(String firstImg) {
        this.firstImg = firstImg;
    }

    public String getSecondImg() {
        return secondImg;
    }

    public void setSecondImg(String secondImg) {
        this.secondImg = secondImg;
    }

    public String getThirdImg() {
        return thirdImg;
    }

    public void setThirdImg(String thirdImg) {
        this.thirdImg = thirdImg;
    }


    @Override
    public String toString() {
        return "Report{" +
                "reportId='" + reportId + '\'' +
                ", count='" + count + '\'' +
                ", videoId='" + videoId + '\'' +
                ", userId='" + userId + '\'' +
                ", firstImg='" + firstImg + '\'' +
                ", secondImg='" + secondImg + '\'' +
                ", thirdImg='" + thirdImg + '\'' +
                '}';
    }
}
