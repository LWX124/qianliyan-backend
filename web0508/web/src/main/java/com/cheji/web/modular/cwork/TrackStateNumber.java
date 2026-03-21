package com.cheji.web.modular.cwork;

public class TrackStateNumber {
    private Integer newTask;            //新任务
    private Integer comeScene;          //到现场
    private Integer noToScene;          //未到现场
    private Integer noCar;              //无车辆
    private Integer trackAgain;         //再次跟踪
    private Integer agree;              //同意到店
    private Integer refuse;             //拒绝到店
    private Integer pushSuccess;        //推修成功
    private Integer isLocation;

    public Integer getComeScene() {
        return comeScene;
    }

    public void setComeScene(Integer comeScene) {
        this.comeScene = comeScene;
    }

    public Integer getNoToScene() {
        return noToScene;
    }

    public void setNoToScene(Integer noToScene) {
        this.noToScene = noToScene;
    }

    public Integer getNoCar() {
        return noCar;
    }

    public void setNoCar(Integer noCar) {
        this.noCar = noCar;
    }

    public Integer getPushSuccess() {
        return pushSuccess;
    }

    public void setPushSuccess(Integer pushSuccess) {
        this.pushSuccess = pushSuccess;
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

    public Integer getTrackAgain() {
        return trackAgain;
    }

    public void setTrackAgain(Integer trackAgain) {
        this.trackAgain = trackAgain;
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
