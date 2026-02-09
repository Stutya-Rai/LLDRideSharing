package com.stutya.lld.controller;

import com.stutya.lld.domain.PaymentStatus;
import com.stutya.lld.service.RideService;

public class PaymentController {
    private final RideService rideService;

    public PaymentController(RideService rideService) {
        this.rideService = rideService;
    }

    public void handleCallback(String transactionId, PaymentStatus status) {
        rideService.handlePaymentCallback(transactionId, status);
    }
}