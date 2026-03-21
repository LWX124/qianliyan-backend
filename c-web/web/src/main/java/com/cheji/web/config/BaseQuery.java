package com.cheji.web.config;

public class BaseQuery {

    //query.getPage(),query.getRows()
    private Integer page=1;
    private Integer rows=10;
    private String keyword;


    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * limit start ,rows
     * @return
     */
    public Integer getStart()
    {
        return (page-1)*rows;
    }


    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }
}
