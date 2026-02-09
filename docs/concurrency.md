Multiple concurrent requests trying to assign the same driver to different rides can cause double-assignment. Here are 3 generic approaches to handle this:

Approach 1: Database Transaction with Row-level Locking

Description:
Use database transaction with high isolation level (SERIALIZABLE or REPEATABLE_READ)
Acquire exclusive locks on driver and ride rows using SELECT FOR UPDATE
Locks are held until transaction commits or rolls back
Example Flow:
BEGIN TRANSACTION
SELECT * FROM driver WHERE id = 1 AND isOnline = true FOR UPDATE
SELECT * FROM ride WHERE id = 5 AND status = 'REQUESTED' FOR UPDATE
Validate: driver.isOnline == true and driver has no active ride
UPDATE ride SET driverId = 1, status = 'ASSIGNED' WHERE id = 5
COMMIT TRANSACTION
Benefits:
Database ensures atomicity (all or nothing)
Prevents concurrent modifications
Works with any SQL database
Language-independent
Limitations:
Locks held for entire transaction duration
Deadlocks possible if locks are acquired in different order
Reduced throughput under high concurrency


Approach 2: Optimistic Locking with Version Field

Description:
Add version or timestamp field to Driver and Ride entities
Read entities with their current version
Verify version has not changed before update
If version changed, retry or reject operation
Example Flow:
READ driver (id=1, isOnline=true, version=5)
READ ride (id=5, status=REQUESTED, version=3)
Validate: driver.isOnline == true
UPDATE ride SET driverId = 1, status = 'ASSIGNED', version = version + 1 WHERE id = 5 AND version = 3
If update affects 0 rows → Version conflict → Retry or fail
If update affects 1 row → Success
Benefits:
No long-held locks
Scales well for high-read systems
Database and language agnostic
Retry-based conflict resolution
Limitations:
Requires retry logic
May fail multiple times under contention
More complex error handling


Approach 3: Distributed Locking (Selected Approach)

Description:
Use distributed lock manager (Redis, etc.)
Acquire lock on driver ID before assignment
Release lock after assignment completes
Lock expiration prevents deadlocks
Example Flow:
ACQUIRE LOCK("driver_lock_1", timeout=200ms)
If lock acquired → Proceed
If lock exists → Skip and try next driver
If timeout expires → Continue matching
READ driver status (isOnline, no active ride)
Validate: driver is available
UPDATE ride to assign driver
RELEASE LOCK("driver_lock_1")
Benefits:
Works across multiple application servers
Prevents double-assignment at application level
Low lock duration → high throughput
Deadlock-safe via lock expiration
Well-suited for real-time matching systems
Limitations:
Requires external infrastructure (Redis)
Network latency for lock operations
Must handle lock expiration carefully


Selected Approach: Distributed Locking (Approach 3)
The system uses distributed locking to prevent driver double-assignment:

Scalability: Works across multiple application servers
Flexibility: Database and language agnostic
Reliability: Lock expiration avoids deadlocks
Performance: Non-blocking locks enable fast matching
Interview-friendly: Demonstrates real-world distributed system design

Implementation Details:
Lock Service: Redis or similar distributed cache
Driver Lock Key: "driver_lock_{driverId}"
Ride Lock Key: "ride_lock_{rideId}"
Lock Timeout: 200ms (driver), 500ms (ride)
Lock Acquisition: Non-blocking; skip on timeout
Lock Ordering: Acquire locks in sorted order (driver → ride)
Lock Release: Always release in finally block