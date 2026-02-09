Step 1: Requirements

Functional Requirements:
As a rider, I can register and log in.
As a driver, I can register/onboard and go online or offline.
As a rider, I can set pickup and destination using map or search.
As a rider, I can view an upfront fare estimate along with ETA before requesting a ride.
As a rider, I can request a ride based on the shown fare estimate.
As a rider, I can cancel a ride before pickup, subject to cancellation policy.
The system asynchronously matches a rider to the nearest available driver from all online drivers (top N by distance).
The system ensures a driver is not assigned to multiple active rides simultaneously.
As a driver, I can accept or decline a ride request within a timeout window.
As a driver, I can navigate to the pickup location and then to the drop-off location.
As a driver, I can start and complete a trip.
As a rider, I can track the driver’s real-time location and trip progress using GPS.
The driver app sends location updates to the system every N seconds.
Payment is processed upon trip completion using supported methods (card/wallet/cash).
As a rider, I receive a payment receipt after trip completion.
Cancellation fees are applied based on defined policy rules.