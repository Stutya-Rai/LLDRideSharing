Step 2: Core Entities

1. Ride (Core Entity)
   id: int [PK]
   rideId: String [UNIQUE]
   riderId: int [FK to Rider]
   driverId: int [FK to Driver, NULLABLE] (NULL until assigned)
   pickupLocation: Location (latitude, longitude, address)
   drop-offLocation: Location (latitude, longitude, address)
   status: RideStatus (Enum: REQUESTED, ASSIGNED, ACCEPTED, IN_PROGRESS, COMPLETED, CANCELLED)
   estimatedFare: long (Stored as integer: actual amount * 100)
   estimatedDistance: double (in km)
   actualDistance: double [NULLABLE] (used for analytics/ETA tuning)
   estimatedDuration: long (in seconds)
   actualDuration: long [NULLABLE] (used for analytics/ETA tuning)
   requestedAt: LocalDateTime
   assignedAt: LocalDateTime [NULLABLE]
   acceptedAt: LocalDateTime [NULLABLE]
   startedAt: LocalDateTime [NULLABLE]
   completedAt: LocalDateTime [NULLABLE]
   cancelledAt: LocalDateTime [NULLABLE]
   cancellationReason: String [NULLABLE]
   paymentType: PaymentType (Enum: PRE_PAYMENT, POST_PAYMENT)
   paymentId: String [NULLABLE] (Payment gateway transaction ID)
   paymentStatus: PaymentStatus (Enum: PENDING, COMPLETED, FAILED, REFUNDED)

2. Rider
   id: int [PK]
   username: String [UNIQUE]
   email: String [UNIQUE]
   phoneNumber: String [UNIQUE]
   name: String
   createdAt: LocalDateTime

3. Driver
   id: int [PK]
   username: String [UNIQUE]
   email: String [UNIQUE]
   phoneNumber: String [UNIQUE]
   name: String
   licenseNumber: String [UNIQUE]
   vehicleNumber: String [UNIQUE]
   vehicleType: String (Sedan, SUV, etc.)
   isOnline: boolean (true when driver is available)
   currentLocation: Location [NULLABLE] (updated via GPS)
   lastLocationUpdate: LocalDateTime [NULLABLE]
   createdAt: LocalDateTime

4. Location
   latitude: double
   longitude: double
   address: String [NULLABLE]
   timestamp: LocalDateTime

5. RideStatus (Enum)
   REQUESTED
   ASSIGNED
   ACCEPTED
   IN_PROGRESS
   COMPLETED
   CANCELLED

6. PaymentStatus (Enum)
   PENDING
   COMPLETED
   FAILED
   REFUNDED

7. PaymentType (Enum)
   PRE_PAYMENT
   POST_PAYMENT

8. DriverStatus (Enum)
   ONLINE
   OFFLINE
   ON_RIDE

9. RideStatusResponse (Response DTO - Not Stored)
   rideId: String
   status: RideStatus
   driver: DriverInfo [NULLABLE] (id, name, phoneNumber, vehicleNumber, currentLocation, eta)
   currentLocation: Location [NULLABLE]
   estimatedFare: long [NULLABLE]
   pickupLocation: Location
   drop-offLocation: Location
   requestedAt: LocalDateTime
   Note: Built dynamically for ride-status polling.

10. FareEstimateResponse (Response DTO)
    estimatedFare: long (actual amount * 100)
    estimatedDistance: double (in km)
    estimatedDuration: long (in seconds)
    currency: String (e.g., "USD")

11. RideRequest (Request DTO)
    riderId: int
    pickupLocation: Location
    drop-offLocation: Location
    paymentType: PaymentType
    Note: POST_PAYMENT is cash-only; PRE_PAYMENT uses payment gateway.

12. FareEstimateRequest (Request DTO)
    pickupLocation: Location
    drop-offLocation: Location

13. LocationUpdateRequest (Request DTO)
    driverId: int
    location: Location
    timestamp: LocalDateTime