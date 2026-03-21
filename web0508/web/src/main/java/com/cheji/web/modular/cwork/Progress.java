package com.cheji.web.modular.cwork;

public class Progress {
    private String workName;
    private String time;

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Progress{" +
                "workName='" + workName + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
