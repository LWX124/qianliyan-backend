package com.cheji.b.modular.dto;

import java.math.BigDecimal;

//评价上半截
public class EvaluationDto {
    private String merchantsName;   //商户名字
    private BigDecimal score;        //评分
    private BigDecimal evaluationCount; //评价数量
    private BigDecimal highPraise;   //好评
    private BigDecimal badReview;   //差评
    private BigDecimal evaComment;  //评价回复率
    private BigDecimal midBabComment;   //中差评回复率
    private Integer highPraiseCount;                 //好评数量
    private Integer badReviewCount;                  //差评数量
    private Integer noResponse;                 //未回复
    private Integer allComments;               //全部
    private Integer goodTcchnology;             //技术好
    private Integer fastSpeed;                  //速度快
    private Integer repeatCustomers;            //回头客
    private Integer serviceEnthusiasm;          //服务热情

    public String getMerchantsName() {
        return merchantsName;
    }

    public void setMerchantsName(String merchantsName) {
        this.merchantsName = merchantsName;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public BigDecimal getEvaluationCount() {
        return evaluationCount;
    }

    public void setEvaluationCount(BigDecimal evaluationCount) {
        this.evaluationCount = evaluationCount;
    }

    public BigDecimal getHighPraise() {
        return highPraise;
    }

    public void setHighPraise(BigDecimal highPraise) {
        this.highPraise = highPraise;
    }

    public BigDecimal getBadReview() {
        return badReview;
    }

    public void setBadReview(BigDecimal badReview) {
        this.badReview = badReview;
    }

    public BigDecimal getEvaComment() {
        return evaComment;
    }

    public void setEvaComment(BigDecimal evaComment) {
        this.evaComment = evaComment;
    }

    public BigDecimal getMidBabComment() {
        return midBabComment;
    }

    public void setMidBabComment(BigDecimal midBabComment) {
        this.midBabComment = midBabComment;
    }

    public Integer getHighPraiseCount() {
        return highPraiseCount;
    }

    public void setHighPraiseCount(Integer highPraiseCount) {
        this.highPraiseCount = highPraiseCount;
    }

    public Integer getBadReviewCount() {
        return badReviewCount;
    }

    public void setBadReviewCount(Integer badReviewCount) {
        this.badReviewCount = badReviewCount;
    }

    public Integer getNoResponse() {
        return noResponse;
    }

    public void setNoResponse(Integer noResponse) {
        this.noResponse = noResponse;
    }

    public Integer getAllComments() {
        return allComments;
    }

    public void setAllComments(Integer allComments) {
        this.allComments = allComments;
    }

    public Integer getGoodTcchnology() {
        return goodTcchnology;
    }

    public void setGoodTcchnology(Integer goodTcchnology) {
        this.goodTcchnology = goodTcchnology;
    }

    public Integer getFastSpeed() {
        return fastSpeed;
    }

    public void setFastSpeed(Integer fastSpeed) {
        this.fastSpeed = fastSpeed;
    }

    public Integer getRepeatCustomers() {
        return repeatCustomers;
    }

    public void setRepeatCustomers(Integer repeatCustomers) {
        this.repeatCustomers = repeatCustomers;
    }

    public Integer getServiceEnthusiasm() {
        return serviceEnthusiasm;
    }

    public void setServiceEnthusiasm(Integer serviceEnthusiasm) {
        this.serviceEnthusiasm = serviceEnthusiasm;
    }

    @Override
    public String toString() {
        return "EvaluationDto{" +
                "merchantsName='" + merchantsName + '\'' +
                ", score=" + score +
                ", evaluationCount=" + evaluationCount +
                ", highPraise=" + highPraise +
                ", badReview=" + badReview +
                ", evaComment=" + evaComment +
                ", midBabComment=" + midBabComment +
                ", highPraiseCount=" + highPraiseCount +
                ", badReviewCount=" + badReviewCount +
                ", noResponse=" + noResponse +
                ", allComments=" + allComments +
                ", goodTcchnology=" + goodTcchnology +
                ", fastSpeed=" + fastSpeed +
                ", repeatCustomers=" + repeatCustomers +
                ", serviceEnthusiasm=" + serviceEnthusiasm +
                '}';
    }
}
