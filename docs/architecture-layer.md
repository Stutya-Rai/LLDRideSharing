Layers of Architecture
We will design the architecture layers of the system in a structured way, ensuring separation of concerns and modularity. The system will be organized into the following layers:

Client/UI → Controller Layer → Service Layer → Domain Layer

CONTROLLERS

1. RideController
- RideStatusResponse getRideStatus(String rideId)
- Ride requestRide(RideRequest request)
- void cancelRide(String rideId, String reason)
- FareEstimateResponse getFareEstimate(FareEstimateRequest request)

2. DriverController
- void acceptRide(String rideId, String driverId)
- void declineRide(String rideId, String driverId)
- void startRide(String rideId, String driverId)
- void completeRide(String rideId, String driverId)
- void updateLocation(String driverId, Location location)
- void goOnline(String driverId)
- void goOffline(String driverId)

3. PaymentController
- void handlePaymentCallback(String transactionId, PaymentStatus status)

SERVICES

1. RideService
- Ride requestRide(RideRequest request)
- RideStatusResponse getRideStatus(String rideId)
- void cancelRide(String rideId, String reason)
- void driverAccept(String rideId, String driverId)
- void driverDecline(String rideId, String driverId)
- void startRide(String rideId, String driverId)
- void completeRide(String rideId, String driverId)

2. MatchingService (Async)
- Optional<Driver> matchDriver(Ride ride)
- void releaseDriver(String driverId)

3. PricingService
- FareEstimateResponse calculateFare(Location pickup, Location dropoff)

4. PaymentService
- String initiatePayment(String rideId, long amount)
- void handlePaymentCallback(String transactionId, PaymentStatus status)

5. LocationService
- void updateDriverLocation(int driverId, Location location)
- Location getDriverLocation(int driverId)
- double calculateDistance(Location loc1, Location loc2)
- long calculateETA(Location from, Location to)

6. DriverService
- void goOnline(int driverId)
- void goOffline(int driverId)
- Driver getById(int driverId)
- boolean isAvailable(int driverId)

7. LockService (Distributed)
- boolean acquire(String key, long timeoutMs)
- void release(String key)

8. NotificationService
- void sendToDriver(int driverId, NotificationMessage message)
- void sendToRider(int riderId, NotificationMessage message)

9. MapService (External Integration)
- DistanceAndDuration getDistanceAndDuration(Location from, Location to)
- String geocode(Location location)
- Location reverseGeocode(double lat, double lon)