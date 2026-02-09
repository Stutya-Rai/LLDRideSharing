package com.stutya.lld.domain;

public class Location {
    private double latitude;
    private double longitude;
    private String address;
    private long timestamp;

    public Location() {
    }

    public Location(double latitude, double longitude, String address, long timestamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.timestamp = timestamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}