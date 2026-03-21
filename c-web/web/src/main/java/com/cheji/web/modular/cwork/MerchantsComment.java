package com.cheji.web.modular.cwork;


import com.cheji.web.modular.domain.ImgEntity;
import com.cheji.web.modular.domain.MerchantsCommentsTreeEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

//商户评论打工类
public class MerchantsComment {
    private Long id;
    private String commentCode;     //评论code
    private String userName;        //评论名字
    private String url; //头像地址
    private List<ImgEntity> merchantsCommont;  //商户评论图片地址
    private String counts;     //评论内容
    private String  countTime;     //评论时间
    private MerchantsCommentsTreeEntity commentsTree;  //商户回复数据
    private BigDecimal score;


    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public MerchantsCommentsTreeEntity getCommentsTree() {
        return commentsTree;
    }

    public void setCommentsTree(MerchantsCommentsTreeEntity commentsTree) {
        this.commentsTree = commentsTree;
    }

    public String getCommentCode() {
        return commentCode;
    }

    public void setCommentCode(String commentCode) {
        this.commentCode = commentCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ImgEntity> getMerchantsCommont() {
        return merchantsCommont;
    }

    public void setMerchantsCommont(List<ImgEntity> merchantsCommont) {
        this.merchantsCommont = merchantsCommont;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getCounts() {
        return counts;
    }

    public void setCounts(String counts) {
        this.counts = counts;
    }

    public String getCountTime() {
        return countTime;
    }

    public void setCountTime(String countTime) {
        this.countTime = countTime;
    }

    @Override
    public String toString() {
        return "MerchantsComment{" +
                "id=" + id +
                ", commentCode='" + commentCode + '\'' +
                ", userName='" + userName + '\'' +
                ", url='" + url + '\'' +
                ", merchantsCommont=" + merchantsCommont +
                ", counts='" + counts + '\'' +
                ", countTime='" + countTime + '\'' +
                ", commentsTree=" + commentsTree +
                ", score=" + score +
                '}';
    }
}
