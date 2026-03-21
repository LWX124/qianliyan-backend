package com.cheji.b.modular.dto;

public class TrackStateNumber {
    private Integer newTask;            //新任务
    private Integer comeScene;          //到现场
    private Integer agree;              //同意到店
    private Integer refuse;             //拒绝到店
    private Integer isLocation;

    public Integer getComeScene() {
        return comeScene;
    }

    public void setComeScene(Integer comeScene) {
        this.comeScene = comeScene;
    }

    public Integer getIsLocation() {
        return isLocation;
    }

    public void setIsLocation(Integer isLocation) {
        this.isLocation = isLocation;
    }

    public Integer getNewTask() {
        return newTask;
    }

    public void setNewTask(Integer newTask) {
        this.newTask = newTask;
    }

    public Integer getAgree() {
        return agree;
    }

    public void setAgree(Integer agree) {
        this.agree = agree;
    }

    public Integer getRefuse() {
        return refuse;
    }

    public void setRefuse(Integer refuse) {
        this.refuse = refuse;
    }
}
