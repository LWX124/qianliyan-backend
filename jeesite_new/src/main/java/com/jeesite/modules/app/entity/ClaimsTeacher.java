package com.jeesite.modules.app.entity;


public class ClaimsTeacher{
    private String name;
    private Integer value;
    private Integer indentId;

    public Integer getIndentId() {
        return indentId;
    }

    public void setIndentId(Integer indentId) {
        this.indentId = indentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ClaimsTeacher{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", indentId=" + indentId +
                '}';
    }
}
