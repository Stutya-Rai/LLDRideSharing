package com.stutya.lld.service;

import com.stutya.lld.domain.PaymentStatus;
import com.stutya.lld.domain.PaymentType;
import com.stutya.lld.domain.Ride;
import com.stutya.lld.repository.RideRepository;
import com.stutya.lld.service.notification.NotificationService;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PaymentService {
    private final RideRepository rideRepository;
    private final NotificationService notificationService;
    private final Map<String, String> rideToPaymentId = new ConcurrentHashMap<>();

    public PaymentService(RideRepository rideRepository, NotificationService notificationService) {
        this.rideRepository = rideRepository;
        this.notificationService = notificationService;
    }

    public String initiatePayment(Ride ride) {
        if (ride.getPaymentType() != PaymentType.PRE_PAYMENT) {
            throw new IllegalStateException("Only PRE_PAYMENT rides can initiate payment");
        }
        String transactionId = UUID.randomUUID().toString();
        ride.setPaymentStatus(PaymentStatus.PENDING);
        ride.setPaymentId(transactionId);
        rideRepository.save(ride);
        rideToPaymentId.put(ride.getId(), transactionId);
        // TODO: Integrate with external gateway, returning providerRef to client.
        System.out.println("[Payment] Initiated payment for ride " + ride.getId() + " transaction=" + transactionId);
        return transactionId;
    }

    public Ride handlePaymentCallback(String transactionId, PaymentStatus status) {
        String rideId = findRideId(transactionId);
        rideToPaymentId.remove(rideId);
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new IllegalArgumentException("Ride not found for transaction " + transactionId));
        ride.setPaymentStatus(status);
        rideRepository.save(ride);
        notificationService.send(new com.stutya.lld.domain.NotificationMessage(
                ride.getRiderId(),
                "Payment " + status,
                "Payment status for ride " + ride.getId() + " is " + status
        ));
        return ride;
    }

    private String findRideId(String transactionId) {
        return rideToPaymentId.entrySet().stream()
                .filter(entry -> entry.getValue().equals(transactionId))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown transaction id " + transactionId));
    }

}

