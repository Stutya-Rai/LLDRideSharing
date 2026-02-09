package com.stutya.lld.domain;

public class Driver {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String vehicleNumber;
    private String vehicleType;
    private DriverStatus status;
    private Location currentLocation;
    private long lastLocationUpdate;

    public Driver() {
    }

    public Driver(String id, String name, String email, String phone, String vehicleNumber, String vehicleType, DriverStatus status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public DriverStatus getStatus() {
        return status;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public long getLastLocationUpdate() {
        return lastLocationUpdate;
    }

    public void setLastLocationUpdate(long lastLocationUpdate) {
        this.lastLocationUpdate = lastLocationUpdate;
    }
}
