package com.stutya.lld.domain;

public class RideRequest {
    private String riderId;
    private Location pickupLocation;
    private Location dropoffLocation;
    private PaymentType paymentType;

    public RideRequest(String riderId, Location pickupLocation, Location dropoffLocation, PaymentType paymentType) {
        this.riderId = riderId;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.paymentType = paymentType;
    }

    public String getRiderId() {
        return riderId;
    }

    public Location getPickupLocation() {
        return pickupLocation;
    }

    public Location getDropoffLocation() {
        return dropoffLocation;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }
}
