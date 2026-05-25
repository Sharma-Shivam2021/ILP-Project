# Walkthrough - Generated Codebase Diagrams

We have generated 7 comprehensive architecture and design diagrams for the **Hospital Workforce Shift Coordination System (HWSCS)**. They have been saved to a new documentation file: [diagrams.md](file:///C:/Users/91708/Desktop/ILP-Project/docs/diagrams.md).

## What Was Created

We created the following diagrams using text-based **Mermaid.js** syntax:

### 1. System Architecture (C4 Container Diagram)
- **What it shows:** Interaction between the User Roles, Angular 17 SPA, local client-side `jsPDF` reporting tool, Spring Boot REST API (secured by stateless JWT filters), and the MySQL database.

### 2. Entity-Relationship (ER) Diagram
- **What it shows:** Database tables (`users`, `departments`, `nurses`, `nursing_incharge`, `duty_officer`, `shifts`, `nurse_shifts`, `shift_requests`, and `request_history`) with detailed fields, datatypes, PKs, FKs, and exact relationship cardinalities matching the Hibernate entities.

### 3. UML Class Diagram
- **What it shows:** Key backend domain classes, enums, repository interfaces, controllers, and service layers (such as `ShiftRequestService` and its implementation `ShiftRequestServiceImpl`) with attributes and operations.

### 4. Sequence Diagram: Shift Swap Request Lifecycle
- **What it shows:** The full transactional, multi-step process of a shift swap request: from Nurse A initiating the swap, to Nurse B accepting/rejecting the request, to the Nursing In-Charge's review, final approval, and the subsequent DB transaction swapping the assignments.

### 5. State Machine Diagram: ShiftRequest Status Lifecycle
- **What it shows:** The lifecycle of a `ShiftRequest` transition through status states like `PENDING_PEER`, `PEER_ACCEPTED`, `PEER_REJECTED`, `APPROVED`, `REJECTED`, and `CANCELLED`.

### 6. Activity Diagram: User Login & Account Lockout Flow
- **What it shows:** The security authentication logic including password checks, first-time login redirection, failed attempts incrementing, account lockout triggers, and token generation.

### 7. Use Case Diagram
- **What it shows:** System functional capabilities partitioned across user roles (Nurse, Nursing In-Charge, and Duty Officer) inside the HWSCS boundary.

---

## Verification & Integrity Check
- Checked syntax of all Mermaid diagram configurations to ensure compatibility with standard Markdown viewers.
- Double-checked details of database fields and classes against the backend JPA entities and authentication controllers to ensure 100% accuracy.
