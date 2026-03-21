package com.cheji.web.modular.cwork;

public class AllCommentDto {
    private Integer allCount;           //全部评价
    private Integer highPraiseCount;    //好评
    private Integer midBabCount;        //中评
    private Integer badReviewCount;     //差评
    private Integer haveImgCount;       //有图

    public Integer getAllCount() {
        return allCount;
    }

    public void setAllCount(Integer allCount) {
        this.allCount = allCount;
    }

    public Integer getHighPraiseCount() {
        return highPraiseCount;
    }

    public void setHighPraiseCount(Integer highPraiseCount) {
        this.highPraiseCount = highPraiseCount;
    }

    public Integer getMidBabCount() {
        return midBabCount;
    }

    public void setMidBabCount(Integer midBabCount) {
        this.midBabCount = midBabCount;
    }

    public Integer getBadReviewCount() {
        return badReviewCount;
    }

    public void setBadReviewCount(Integer badReviewCount) {
        this.badReviewCount = badReviewCount;
    }

    public Integer getHaveImgCount() {
        return haveImgCount;
    }

    public void setHaveImgCount(Integer haveImgCount) {
        this.haveImgCount = haveImgCount;
    }

    @Override
    public String toString() {
        return "AllCommentDto{" +
                "allCount=" + allCount +
                ", highPraiseCount=" + highPraiseCount +
                ", midBabCount=" + midBabCount +
                ", badReviewCount=" + badReviewCount +
                ", haveImgCount=" + haveImgCount +
                '}';
    }
}
