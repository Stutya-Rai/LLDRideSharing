package com.stutya.lld.domain;

public class Ride {
    private String id;
    private String riderId;
    private String driverId;
    private Location pickupLocation;
    private Location dropoffLocation;
    private RideStatus status;
    private PaymentType paymentType;
    private PaymentStatus paymentStatus;
    private String paymentId;
    private long estimatedFare;
    private double estimatedDistanceKm;
    private double actualDistanceKm;
    private long estimatedDurationSec;
    private long actualDurationSec;
    private long requestedAt;
    private long assignedAt;
    private long acceptedAt;
    private long startedAt;
    private long completedAt;
    private long cancelledAt;
    private String cancellationReason;

    public Ride() {
    }

    public Ride(String id, String riderId, Location pickupLocation, Location dropoffLocation, PaymentType paymentType, long requestedAt) {
        this.id = id;
        this.riderId = riderId;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.paymentType = paymentType;
        this.requestedAt = requestedAt;
        this.status = RideStatus.REQUESTED;
        this.paymentStatus = PaymentStatus.NONE;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRiderId() {
        return riderId;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public Location getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(Location pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public Location getDropoffLocation() {
        return dropoffLocation;
    }

    public void setDropoffLocation(Location dropoffLocation) {
        this.dropoffLocation = dropoffLocation;
    }

    public RideStatus getStatus() {
        return status;
    }

    public void setStatus(RideStatus status) {
        this.status = status;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public long getEstimatedFare() {
        return estimatedFare;
    }

    public void setEstimatedFare(long estimatedFare) {
        this.estimatedFare = estimatedFare;
    }

    public double getEstimatedDistanceKm() {
        return estimatedDistanceKm;
    }

    public void setEstimatedDistanceKm(double estimatedDistanceKm) {
        this.estimatedDistanceKm = estimatedDistanceKm;
    }

    public double getActualDistanceKm() {
        return actualDistanceKm;
    }

    public void setActualDistanceKm(double actualDistanceKm) {
        this.actualDistanceKm = actualDistanceKm;
    }

    public long getEstimatedDurationSec() {
        return estimatedDurationSec;
    }

    public void setEstimatedDurationSec(long estimatedDurationSec) {
        this.estimatedDurationSec = estimatedDurationSec;
    }

    public long getActualDurationSec() {
        return actualDurationSec;
    }

    public void setActualDurationSec(long actualDurationSec) {
        this.actualDurationSec = actualDurationSec;
    }

    public long getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(long requestedAt) {
        this.requestedAt = requestedAt;
    }

    public long getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(long assignedAt) {
        this.assignedAt = assignedAt;
    }

    public long getAcceptedAt() {
        return acceptedAt;
    }

    public void setAcceptedAt(long acceptedAt) {
        this.acceptedAt = acceptedAt;
    }

    public long getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(long startedAt) {
        this.startedAt = startedAt;
    }

    public long getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(long completedAt) {
        this.completedAt = completedAt;
    }

    public long getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(long cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }
}
