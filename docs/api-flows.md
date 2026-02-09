
1. Fare Estimate Flow
   GET /api/rides/fare-estimate → RideController.getFareEstimate(request)
   → PricingService.calculateFare(pickup, drop-off)
   → MapService.getDistanceAndDuration(pickup, drop-off)
   → PricingStrategy.calculateFare(distance, duration)
   → Return FareEstimateResponse

2. Ride Request Flow
   2.1 Request Ride (Initial)
   POST /api/rides/request → RideController.requestRide(request)
   → RideService.requestRide(request)
   → Validate rider exists
   → Calculate estimated fare
   → Create Ride(status=REQUESTED, paymentType, estimatedFare)
   2.2 PRE_PAYMENT Ride Flow
   → PaymentService.initiatePayment(rideId, estimatedFare)
   → PaymentGatewayRouter.selectProvider(...) → provider.initiatePayment(...)
   → Update Ride(paymentStatus=PENDING, paymentId)
   → Do NOT start driver matching yet
   → Return {rideId, status: REQUESTED, paymentStatus: PENDING}
   2.3 POST_PAYMENT Ride Flow (Cash)
   → Validate cash-only payment
   → Start Async MatchingService.matchDriver(ride)
   → Return {rideId, status: REQUESTED}
   2.4 Payment Callback (PRE_PAYMENT Only)
   POST /api/payments/callback → PaymentController.handlePaymentCallback(transactionId, status)
   → Verify callback authenticity
   → Fetch ride by paymentId
   → On SUCCESS: Update Ride(paymentStatus=COMPLETED) → Start matching → Notify rider
   → On FAILURE: Update Ride(status=CANCELLED, paymentStatus=FAILED) → Notify rider

3. Async Driver Matching Flow
   MatchingService.matchDriver(ride)
   → Ensure ride status is REQUESTED
   → Fetch available drivers (DriverStatus.ONLINE)
   → Apply MatchingStrategy (nearest/top-N drivers)
   → For each driver candidate:
   Acquire distributed lock on driver
   Validate driver still ONLINE
   Push ride notification to driver
   Wait for response (poll ride status, timeout 30s)
   If ACCEPTED → stop matching
   If timeout / decline → try next driver
   Release driver lock
   → If no driver accepts → return empty

4. Driver Accept / Decline Flow
   4.1 Driver Accept Ride
   POST /api/rides/{rideId}/accept → RideController.acceptRide
   → Acquire ride-level lock
   → If status REQUESTED: assign driver (status=ASSIGNED)
   → Set driver status ON_RIDE
   → Update Ride(status=ACCEPTED)
   → Notify rider
   4.2 Driver Decline Ride
   POST /api/rides/{rideId}/decline → RideController.declineRide
   → Acquire ride-level lock
   → If REQUESTED: ignore (no assignment yet)
   → If ASSIGNED: release driver → reset ride → re-trigger matching

5. Rider Polling for Ride Status
   GET /api/rides/{rideId}/status → RideController.getRideStatus
   → RideService.getRideStatus
   → Build RideStatusResponse
   → Rider polls every 2–3 seconds

6. Driver Location Updates (GPS)
   POST /api/drivers/{driverId}/location → DriverController.updateLocation
   → Update Driver.currentLocation & lastLocationUpdate
   → If active ride exists: push location update to rider

7. Start Trip
   POST /api/rides/{rideId}/start → RideController.startRide
   → Validate ride is ACCEPTED
   → Update Ride(status=IN_PROGRESS, startedAt)
   → Notify rider & enable real-time tracking

8. Complete Trip
   POST /api/rides/{rideId}/complete → RideController.completeRide
   → Validate ride is IN_PROGRESS
   → Capture actual distance & duration (telemetry)
   → Update Ride(status=COMPLETED)
   → Handle payment completion (cash or prepaid)
   → Notify rider & release driver

9. Cancel Ride
   POST /api/rides/{rideId}/cancel → RideController.cancelRide
   → Validate cancellation policy
   → Update Ride(status=CANCELLED, reason)
   → Release driver if assigned
   → Apply cancellation fee if applicable
   → Notify rider and driver

10. Driver Online / Offline
    POST /api/drivers/{driverId}/online → DriverService.goOnline
    → Set DriverStatus.ONLINE & register for notifications
    POST /api/drivers/{driverId}/offline → DriverService.goOffline
    → Set DriverStatus.OFFLINE & unregister notifications

