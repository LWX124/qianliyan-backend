package com.jeesite.modules.app.entity;

public class Track {
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Track{" +
                "location='" + location + '\'' +
                '}';
    }
}
