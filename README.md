# 🚕 Ride Sharing System – Low Level Design (Java)

A production-grade **Low Level Design (LLD)** of a Ride Sharing system (Uber/Ola-like),
focusing on **scalability, concurrency, and clean architecture**.

This project is intentionally designed for **backend & system design interviews**.

---

## 🎯 Goals of This Design
- Model real-world ride sharing workflows
- Demonstrate strong OOP & SOLID principles
- Handle concurrency & async workflows

---

## 🛠 Tech Stack
- **Language:** Java
- **Build Tool:** Maven
- **Architecture:** Layered (Controller → Service → Domain → Repository)
- **Design Focus:** LLD + Distributed Systems

---

## 🧱 High-Level Architecture

Client
↓
Controller Layer
↓
Service Layer
↓
Domain Layer
↓
Repository Layer


---

## 📁 Project Structure

src/main/java/com/stutya/lld
├── controller
├── service
├── repository
├── domain
└── Main.java


---

## 🚦 Core Functional Flows
- Fare estimation with distance & ETA
- Ride request (async, non-blocking)
- Driver matching (top-N nearest drivers)
- Ride lifecycle (REQUESTED → COMPLETED)
- Real-time driver tracking
- Payment handling (Prepaid / Cash)

---

## 🧠 Key Design Highlights (Interview Focus)

### ✅ State Pattern
Used to manage the **ride lifecycle**:
- REQUESTED
- ASSIGNED
- ACCEPTED
- IN_PROGRESS
- COMPLETED
- CANCELLED

Ensures **valid transitions only**.

---

### ✅ Strategy Pattern
Used in **three critical areas**:
- Driver Matching (Nearest / Fastest ETA)
- Pricing (Base / Surge)
- Payment Gateways (Stripe, Razorpay, Mock)

---

### ✅ Concurrency Handling
- Distributed locking (Redis-style)
- Prevents **driver double-assignment**
- Non-blocking, scalable approach

---

### ✅ Async Driver Matching
- Matching runs asynchronously
- Drivers notified sequentially
- Timeout-based accept/decline logic

---

### ✅ Payment Safety
- Upfront fare is **locked**
- Prepaid rides start matching only after payment success
- Idempotent payment callbacks

---

## 🧪 Edge Cases Covered
- No drivers available
- Driver timeout / decline
- Duplicate ride requests
- Payment failures & refunds
- Network failures during trip
- GPS staleness handling

---

## 📄 Detailed Design Documentation
Full design details are documented here:
/docs
├── requirements.md
├── entities.md
├── api-flows.md
├── concurrency.md
├── state-machine.md
└── future-improvements.md


---

## 🚀 Why This Project?
This project demonstrates:
- Real-world system thinking
- Clean LLD architecture
- Scalability & concurrency awareness

## 👨‍💻 Author
**Stutya Rai**  
B.Tech CSIT | Backend & System Design 
Skills: Java, DSA, LLD, Distributed Systems