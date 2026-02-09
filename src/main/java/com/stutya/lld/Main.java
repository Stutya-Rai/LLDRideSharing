package com.stutya.lld;

import com.stutya.lld.controller.DriverController;
import com.stutya.lld.controller.PaymentController;
import com.stutya.lld.controller.RideController;
import com.stutya.lld.domain.Driver;
import com.stutya.lld.domain.DriverStatus;
import com.stutya.lld.domain.Location;
import com.stutya.lld.domain.PaymentStatus;
import com.stutya.lld.domain.PaymentType;
import com.stutya.lld.domain.Ride;
import com.stutya.lld.domain.RideRequest;
import com.stutya.lld.domain.Rider;
import com.stutya.lld.domain.RideStatusResponse;
import com.stutya.lld.domain.strategy.BasePricingStrategy;
import com.stutya.lld.domain.strategy.NearestDriverStrategy;
import com.stutya.lld.repository.DriverRepository;
import com.stutya.lld.repository.RideRepository;
import com.stutya.lld.repository.RiderRepository;
import com.stutya.lld.repository.LocationRepository;
import com.stutya.lld.repository.impl.InMemoryDriverRepository;
import com.stutya.lld.repository.impl.InMemoryRideRepository;
import com.stutya.lld.repository.impl.InMemoryRiderRepository;
import com.stutya.lld.repository.impl.InMemoryLocationRepository;
import com.stutya.lld.service.DriverService;
import com.stutya.lld.service.LockService;
import com.stutya.lld.service.LocationService;
import com.stutya.lld.service.MatchingService;
import com.stutya.lld.service.PaymentService;
import com.stutya.lld.service.PricingService;
import com.stutya.lld.service.RideService;
import com.stutya.lld.service.notification.NotificationService;

public class Main {
    public static void main(String[] args) {

        RideRepository rideRepository = new InMemoryRideRepository();
        RiderRepository riderRepository = new InMemoryRiderRepository();
        DriverRepository driverRepository = new InMemoryDriverRepository();
        LocationRepository locationRepository = new InMemoryLocationRepository();

        Rider rider = new Rider("rider-1", "Alice Rider", "alice@example.com", "111-222-3333", System.currentTimeMillis());
        riderRepository.save(rider);

        Driver driver = new Driver("driver-1", "Bob Driver", "bob@example.com", "444-555-6666", "KA01AB1234", "Sedan", DriverStatus.ONLINE);
        driver.setCurrentLocation(new Location(37.7749, -122.4194, "Market St", System.currentTimeMillis()));
        driverRepository.save(driver);

        NotificationService notificationService = new NotificationService();
        PricingService pricingService = new PricingService(new BasePricingStrategy());
        LocationService locationService = new LocationService(locationRepository);
        LockService lockService = new LockService();
        MatchingService matchingService = new MatchingService(driverRepository, rideRepository, new NearestDriverStrategy(), lockService, notificationService);
        PaymentService paymentService = new PaymentService(rideRepository, notificationService);
        RideService rideService = new RideService(rideRepository, riderRepository, driverRepository, matchingService, pricingService, paymentService, notificationService, locationService, lockService);
        DriverService driverService = new DriverService(driverRepository);

        RideController rideController = new RideController(rideService);
        DriverController driverController = new DriverController(driverService, rideService);
        PaymentController paymentController = new PaymentController(rideService);

        Location pickup = new Location(37.7749, -122.4194, "Market St", System.currentTimeMillis());
        Location drop = new Location(37.7840, -122.4090, "Mission St", System.currentTimeMillis());
        RideRequest prePaymentRequest = new RideRequest(rider.getId(), pickup, drop, PaymentType.PRE_PAYMENT);

        System.out.println("--- Requesting ride with PRE_PAYMENT ---");
        Ride ride = rideController.requestRide(prePaymentRequest);
        System.out.println("Ride created with id " + ride.getId() + " paymentId=" + ride.getPaymentId());

        paymentController.handleCallback(ride.getPaymentId(), PaymentStatus.COMPLETED);
        driverController.acceptRide(ride.getId(), driver.getId());
        driverController.startRide(ride.getId(), driver.getId());
        driverController.completeRide(ride.getId(), driver.getId());
        RideStatusResponse completedStatus = rideController.getRideStatus(ride.getId());
        System.out.println("Ride status: " + completedStatus.getStatus() + " fare=" + completedStatus.getEstimatedFare());

        System.out.println("--- Requesting ride with POST_PAYMENT (cash) ---");
        RideRequest postPaymentRequest = new RideRequest(rider.getId(), pickup, drop, PaymentType.POST_PAYMENT);
        Ride cashRide = rideController.requestRide(postPaymentRequest);
        driverController.acceptRide(cashRide.getId(), driver.getId());
        driverController.startRide(cashRide.getId(), driver.getId());
        driverController.completeRide(cashRide.getId(), driver.getId());
        RideStatusResponse cashStatus = rideController.getRideStatus(cashRide.getId());
        System.out.println("Cash ride status: " + cashStatus.getStatus() + " fare=" + cashStatus.getEstimatedFare());
    }
}

