package com.example.semesterproject2;

public class CoordinatesModel {

    int id;
    String userId;
    float lon;
    float lat;
    String timestamp;

    public CoordinatesModel(int id, String userId, float lon, float lat, String timestamp) {
        this.id = id;
        this.userId = userId;
        this.lon = lon;
        this.lat = lat;
        this.timestamp = timestamp;
    }

    public CoordinatesModel() {
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", lon=" + lon +
                ", lat=" + lat +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

