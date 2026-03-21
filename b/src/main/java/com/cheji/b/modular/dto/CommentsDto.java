package com.cheji.b.modular.dto;

import com.cheji.b.modular.domain.ImgEntity;
import com.cheji.b.modular.domain.MerchantsCommentsTree;

import java.math.BigDecimal;
import java.util.List;

//评论列表
public class CommentsDto {
    private String commentsCode;    //评论id
    private Integer userId;         //用户id
    private String avatar;          //头像
    private String name;            //名字
    private String time;            //时间
    private BigDecimal socre;       //打分
    private String content;         //内容
    private Integer traffic;        //访问量
    private List<ImgEntity> commentsList;   //评论图片
    private MerchantsCommentsTree commentsTree;//回复数据


    public MerchantsCommentsTree getCommentsTree() {
        return commentsTree;
    }

    public void setCommentsTree(MerchantsCommentsTree commentsTree) {
        this.commentsTree = commentsTree;
    }

    public String getCommentsCode() {
        return commentsCode;
    }

    public void setCommentsCode(String commentsCode) {
        this.commentsCode = commentsCode;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public BigDecimal getSocre() {
        return socre;
    }

    public void setSocre(BigDecimal socre) {
        this.socre = socre;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getTraffic() {
        return traffic;
    }

    public void setTraffic(Integer traffic) {
        this.traffic = traffic;
    }

    public List<ImgEntity> getCommentsList() {
        return commentsList;
    }

    public void setCommentsList(List<ImgEntity> commentsList) {
        this.commentsList = commentsList;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "CommentsDto{" +
                "commentsCode='" + commentsCode + '\'' +
                ", userId=" + userId +
                ", avatar='" + avatar + '\'' +
                ", name='" + name + '\'' +
                ", time='" + time + '\'' +
                ", socre=" + socre +
                ", content='" + content + '\'' +
                ", traffic=" + traffic +
                ", commentsList=" + commentsList +
                ", commentsTree=" + commentsTree +
                '}';
    }
}
