package com.cheji.web.modular.cwork;

import java.util.List;

//我的团队打工类
public class MyTeam {
    private String id;      //用户id
    private String avatar;  //头像
    private String name;    //名称
    private String label;   //标签
    private Integer teamCount;   //团队人数
    private List<MyTeam> myTeams;   //团队列表

    public Integer getTeamCount() {
        return teamCount;
    }

    public void setTeamCount(Integer teamCount) {
        this.teamCount = teamCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<MyTeam> getMyTeams() {
        return myTeams;
    }

    public void setMyTeams(List<MyTeam> myTeams) {
        this.myTeams = myTeams;
    }

    @Override
    public String toString() {
        return "MyTeam{" +
                "id='" + id + '\'' +
                ", avatar='" + avatar + '\'' +
                ", name='" + name + '\'' +
                ", label='" + label + '\'' +
                ", teamCount='" + teamCount + '\'' +
                ", myTeams=" + myTeams +
                '}';
    }
}
