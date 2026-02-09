RIDE STATE PATTERN (State Machine)

RideState (Interface)
+ void accept(Ride ride, int driverId)
+ void cancel(Ride ride, int userId, String reason)
+ void start(Ride ride, int driverId)
+ void complete(Ride ride, int driverId)
  RequestedState
  AssignedState
  AcceptedState
  InProgressState
  CompletedState
  CancelledState
  Note: State objects are created dynamically from RideStatus enum; only enum is persisted.

DRIVER MATCHING STRATEGY
DriverMatchingStrategy (Interface)
+ List<Driver> findMatchingDrivers(Location pickup, List<Driver> candidates, int maxResults)
  NearestDriverStrategy
  FastestEtaStrategy (Future)
  MatchingService acts as strategy context

PRICING STRATEGY
PricingStrategy (Interface)
+ long calculateFare(double distance, long duration, PricingContext context)
  BasePricingStrategy
  SurgePricingStrategy
  PricingService acts as strategy context

PAYMENT GATEWAY STRATEGY
PaymentGatewayProvider (Interface)
+ String getName()
+ String initiatePayment(String rideId, long amount, Map<String, String> paymentDetails)
+ boolean verifyCallback(String transactionId, PaymentStatus status)
  StripePaymentGatewayProvider
  RazorpayPaymentGatewayProvider
  PayPalPaymentGatewayProvider
  MockPaymentGatewayProvider
  PaymentGatewayRouter selects provider dynamically

REPOSITORIES
1. RideRepository
- Optional<Ride> findByRideId(String rideId)
- Ride save(Ride ride)
- List<Ride> findByRiderId(int riderId)
- List<Ride> findByDriverId(int driverId)
- List<Ride> findByStatus(RideStatus status)
2. RiderRepository
- Optional<Rider> findById(int id)
- Optional<Rider> findByEmail(String email)
- Rider save(Rider rider)
3. DriverRepository
- Optional<Driver> findById(String id)
- List<Driver> findByStatus(DriverStatus status)
- Driver save(Driver driver)
- void updateLocation(String driverId, Location location)
4. LocationRepository
- void saveLocation(int driverId, Location location)
- Location getLatestLocation(int driverId)

KEY RELATIONSHIPS & PATTERNS
Association – Ride references Rider and Driver
Aggregation – Ride contains pickup and dropoff Location
Dependency – Services depend on repositories and external services
State Pattern – Ride lifecycle handling
Strategy Pattern – Pricing, Matching, Payment Gateway
Repository Pattern – Data persistence abstraction
Step 5: Core Use Cases & Methods

1. Request Ride Use Case:
   requestRide(request) →
   Validate: rider exists, pickup ≠ dropoff, valid locations
   Calculate estimated fare
   Create Ride(status=REQUESTED, requestedAt=now, paymentType, estimatedFare)
   Lock estimatedFare as the final agreed price (no adjustments after trip)
   If paymentType is PRE_PAYMENT:
   Initiate payment via PaymentService.initiatePayment(rideId, estimatedFare)
   PaymentGatewayRouter.selectProvider(...) → provider.initiatePayment(...)
   Update Ride(paymentStatus=PENDING, paymentId=transactionId)
   Do NOT start matching yet (wait for payment callback)
   Return {rideId, status: REQUESTED, paymentStatus: PENDING} immediately
   If paymentType is POST_PAYMENT:
   Validate: only cash payment allowed
   Trigger async MatchingService.matchDriver(ride) immediately
   Return {rideId, status: REQUESTED} immediately (non-blocking)

2. Match Driver Use Case (Async):
   matchDriver(ride) →
   Fetch ride; ensure status is REQUESTED
   Find available drivers: DriverRepository.findByStatus(ONLINE)
   Apply MatchingStrategy.findMatchingDrivers(pickup, drivers, maxResults=3)
   For each driver candidate (in order):
   Acquire distributed lock on driver (timeout=200ms)
   Re-fetch driver and validate still ONLINE
   Keep driver ONLINE (not assigned yet)
   Push ride notification to driver
   Wait for response (poll every 500ms, timeout 30s):
   If ACCEPTED by same driver → return driver
   If CANCELLED → return empty
   If another driver assigned → break
   If timeout → continue to next driver
   Release driver lock
   If no drivers accepted → return empty Optional

3. Accept Ride Use Case:
   driverAccept(rideId, driverId) →
   Acquire distributed lock on ride
   Fetch ride
   If status is REQUESTED: assign driver (status=ASSIGNED, assignedAt=now)
   Validate driverId matches assigned driver
   Update driver status to ON_RIDE
   Update Ride(status=ACCEPTED, acceptedAt=now)
   Transition ride state via State Pattern
   Push notification to rider
   Release lock

4. Decline Ride Use Case:
   driverDecline(rideId, driverId) →
   Acquire distributed lock on ride
   Fetch ride
   If status is REQUESTED: return (matching continues)
   If status is ASSIGNED:
   Validate driverId
   Release driver (set ONLINE)
   Update Ride(status=REQUESTED, driverId=null)
   Trigger re-matching
   Release lock

5. Start Trip Use Case:
   startRide(rideId, driverId) →
   Acquire distributed lock on ride
   Validate status is ACCEPTED and driver matches
   Update Ride(status=IN_PROGRESS, startedAt=now)
   Transition ride state via State Pattern
   Start real-time location tracking
   Push notification to rider
   Release lock

6. Complete Trip Use Case:
   completeRide(rideId, driverId) →
   Acquire distributed lock on ride
   Validate status is IN_PROGRESS and driver matches
   Capture actual distance & duration (analytics only)
   Update Ride(status=COMPLETED, completedAt=now)
   Transition ride state via State Pattern
   If POST_PAYMENT:
   Driver collects locked estimatedFare (cash)
   Update Ride(paymentStatus=COMPLETED)
   If PRE_PAYMENT:
   Fare already paid via gateway
   Mark driver as available
   Push receipt notification
   Release lock

7. Payment Callback Use Case (PRE_PAYMENT):
   handlePaymentCallback(transactionId, status) →
   Verify callback authenticity
   Find ride by paymentId
   If SUCCESS:
   Update Ride(paymentStatus=COMPLETED)
   Trigger async MatchingService.matchDriver(ride)
   Notify rider
   If FAILURE:
   Update Ride(status=CANCELLED, paymentStatus=FAILED)
   Notify rider

8. Cancel Ride Use Case:
   cancelRide(rideId, userId, reason) →
   Acquire distributed lock on ride
   Validate user is rider or driver
   Validate cancellation policy
   Update Ride(status=CANCELLED, cancelledAt=now)
   Release driver if assigned
   Apply cancellation fee if needed
   Push notifications
   Release lock

9. Update Driver Location Use Case:
   updateDriverLocation(driverId, location) →
   Update driver current location
   If ride IN_PROGRESS:
   Calculate ETA
   Push live location to rider
   Persist location history

10. Get Ride Status Use Case (Polling):
    getRideStatus(rideId) →
    Fetch ride
    Fetch driver & location if assigned
    Calculate ETA if IN_PROGRESS
    Build RideStatusResponse DTO
    Return response (polled every 2–3 seconds)
    Step 6: Apply OOP Principles & Design Patterns