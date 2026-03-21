package com.cheji.web.util;

import java.io.Serializable;
import java.util.List;

public class Page implements Serializable {
    private static final long serialVersionUID = -4076251710795431210L;
    private Integer showCount = 10;
    private int totalPage;
    private int totalResult;
    private int currentPage = 1;
    private int currentResult;
    private List list;
    private String listJson;
    private String sortField;
    private String order;

    public Page() {
    }

    public List getList() {
        return this.list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public int getShowCount() {
        return this.showCount;
    }

    public void setShowCount(Integer showCount) {
        if (showCount != null) {
            this.showCount = showCount;
        }

    }

    public int getTotalPage() {
        this.totalPage = (this.totalResult + this.showCount - 1) / this.showCount;
        return this.totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalResult() {
        return this.totalResult;
    }

    public void setTotalResult(Integer totalResult) {
        this.totalResult = totalResult;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        if (currentPage != null) {
            if (currentPage <= 0) {
                this.currentPage = 1;
            } else {
                this.currentPage = currentPage;
            }
        }

    }

    public int getCurrentResult() {
        return (this.currentPage - 1) * this.showCount;
    }

    public void setCurrentResult(Integer currentResult) {
        this.currentResult = currentResult;
    }

    public String getSortField() {
        return this.sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getOrder() {
        return this.order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Page [currentPage=").append(this.currentPage).append(", showCount=").append(this.showCount).append(",currentResult=").append(this.currentResult).append(", totalPage=").append(this.totalPage).append(", totalResult=").append(this.totalResult).append("]");
        return builder.toString();
    }

    public String getListJson() {
        return this.listJson;
    }

    public void setListJson(String listJson) {
        this.listJson = listJson;
    }
}