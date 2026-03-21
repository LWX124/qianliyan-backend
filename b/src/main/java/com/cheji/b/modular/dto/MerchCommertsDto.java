package com.cheji.b.modular.dto;

import java.util.List;

//评价下半截
public class MerchCommertsDto {
    /*private Integer highPraise;                 //好评
    private Integer badReview;                  //差评
    private Integer noResponse;                 //未回复
    private Integer allComments;               //全部
    private Integer goodTcchnology;             //技术好
    private Integer fastSpeed;                  //速度快
    private Integer repeatCustomers;            //回头客
    private Integer serviceEnthusiasm;          //服务热情*/
    private List<CommentsDto> commentsDtoList;

    public List<CommentsDto> getCommentsDtoList() {
        return commentsDtoList;
    }

    public void setCommentsDtoList(List<CommentsDto> commentsDtoList) {
        this.commentsDtoList = commentsDtoList;
    }

    @Override
    public String toString() {
        return "MerchCommertsDto{" +
                ", commentsDtoList=" + commentsDtoList +
                '}';
    }
}
