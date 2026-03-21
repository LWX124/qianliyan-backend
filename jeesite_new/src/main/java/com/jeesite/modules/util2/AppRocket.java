package com.jeesite.modules.util2;

public class AppRocket {
    private String source;
    private String type;
    private String userId;
    private String userBId;

    public AppRocket(String source, String type, String userId, String userBId) {
        this.source = source;
        this.type = type;
        this.userId = userId;
        this.userBId = userBId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserBId() {
        return userBId;
    }

    public void setUserBId(String userBId) {
        this.userBId = userBId;
    }

    @Override
    public String toString() {
        return "AppRocket{" +
                "source='" + source + '\'' +
                ", type='" + type + '\'' +
                ", userId='" + userId + '\'' +
                ", userBId='" + userBId + '\'' +
                '}';
    }
}
