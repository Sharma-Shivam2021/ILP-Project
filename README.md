# Software Requirements Specification (SRS)
## Hospital Workforce Shift Coordination System (HWSCS)

---

## 1. Introduction

### 1.1 Purpose
The purpose of this document is to provide a detailed overview of the Hospital Workforce Shift Coordination System (HWSCS). It outlines the architectural design, functional capabilities, and technical constraints of the system. This system acts as an end-to-end medical workforce management solution designed for deployment in hospital environments.

### 1.2 Document Conventions
This document follows standard IEEE formatting for software specifications. The term "System" refers to HWSCS. The term "User" refers to any authorized personnel interacting with the application.

### 1.3 Intended Audience
This document is intended for:
- TCS ILP Evaluators and Reviewers.
- Software Developers and System Architects.
- Hospital Administrators evaluating workforce management solutions.

### 1.4 Project Scope
The HWSCS application automates and secures the process of shift scheduling and roster management for nursing staff. It replaces manual, error-prone paper/Excel schedules with a centralized web portal. It enforces hospital hierarchy rules for shift swapping, generates live staffing analytics, and exports audit-ready PDF reports.

---

## 2. Overall Description

### 2.1 Product Perspective
HWSCS is an independent, web-based application utilizing a multi-tier architecture. It comprises a client-side Single Page Application (SPA) built with Angular and a server-side REST API built with Java Spring Boot, communicating with a MySQL relational database.

### 2.2 User Classes and Characteristics
The system strictly defines three user roles:
1. **Nurse (End-User):** Requires basic technical proficiency. Primary needs are viewing personal schedules, initiating shift swaps with peers, and managing their profile.
2. **Nursing In-Charge (Mid-Level Management):** Responsible for a specific department. Primary needs are daily roster creation, resolving staff conflicts, and approving/rejecting escalated shift swaps.
3. **Duty Officer (Executive Administrator):** Hospital-wide overseer. Primary needs are read-only analytical reporting, broad compliance monitoring, and access to the complete nurse directory.

### 2.3 Operating Environment
- **Client/Frontend:** Any modern web browser (Chrome, Firefox, Edge, Safari).
- **Server/Backend:** Java Runtime Environment (JRE) 17+.
- **Database Server:** MySQL 8.0+.
- **Network:** Requires intranet or internet connectivity between the client and server.

### 2.4 Design and Implementation Constraints
- The frontend must be developed exclusively using Angular 17 Standalone Components.
- The Node.js environment must be strictly constrained to `v18.13.0`.
- The system must use Stateless JSON Web Tokens (JWT) for authentication rather than session cookies to ensure scalability.

---

## 3. System Features & Functional Requirements

### 3.1 Authentication & Security Module
- **FR1.1 (Login):** The system shall authenticate users against the database using hashed passwords.
- **FR1.2 (First-Time Login):** The system shall force users to change their default password upon their initial login before granting access to the main application.
- **FR1.3 (RBAC):** The system shall dynamically generate navigational menus and restrict REST API access based on the logged-in user's role.

### 3.2 Nurse Workflow Module
- **FR2.1 (Schedule Viewing):** Nurses shall be able to view their historically completed, current, and upcoming assigned shifts.
- **FR2.2 (Peer Swap Initiation):** A Nurse shall be able to propose a shift swap to another nurse within the same department.
- **FR2.3 (Peer Swap Resolution):** A Nurse receiving a swap request shall be able to Accept or Reject it. Acceptance forwards the request to the In-Charge.

### 3.3 Nursing In-Charge Management Module
- **FR3.1 (Shift Assignment):** The In-Charge shall be able to assign available nurses to specific shifts on specific dates.
- **FR3.2 (Schedule View):** The In-Charge shall view a unified daily roster for their department.
- **FR3.3 (Managerial Approval):** The In-Charge holds final authority to Approve or Reject a peer-accepted swap request. Approved requests automatically update the database assignments.

### 3.4 Duty Officer Executive Module
- **FR4.1 (Live Analytics):** The system shall calculate and display real-time staffing coverage percentages for the current day.
- **FR4.2 (Audit Reporting):** The system shall aggregate data to generate a multi-page PDF report containing current staffing, pending/resolved swap requests, and the nurse directory.

---

## 4. Nonfunctional Requirements

### 4.1 Performance Requirements
- **Response Time:** API responses (excluding complex PDF generation) should resolve within 500ms under normal load.
- **Client-Side Rendering:** The PDF generation utilizes `jsPDF` locally on the client's browser to offload server memory pressure.

### 4.2 Security Requirements
- Passwords must be hashed using BCrypt.
- All protected API endpoints must require a valid Bearer JWT in the Authorization header.
- Cross-Origin Resource Sharing (CORS) must be configured to only allow requests from trusted frontend domains.

### 4.3 Software Quality Attributes
- **Usability:** The UI utilizes the Angular Material design system, enhanced with a "Medical SaaS" aesthetic (high contrast, readable typography, subtle state elevations) for maximum legibility in high-stress hospital environments.
- **Maintainability:** The backend adheres to SOLID principles and clean MVC architecture. The frontend uses highly decoupled Standalone Components.

---

## 5. API Documentation

All protected endpoints require the client to supply a valid JSON Web Token (JWT) in the `Authorization` header formatted as:
`Authorization: Bearer <JWT_TOKEN>`

### 5.1 Authentication (`/api/auth`)
* **POST `/api/auth/login`**
  * **Payload:** `{ username, password }`
  * **Response:** `{ token, userId, username, role, firstLogin }`
  * **Description:** Authenticates user credentials and returns JWT bearer token along with profile metadata.

### 5.2 Dashboards (`/api/dashboard`)
* **GET `/api/dashboard/nurse`**
  * **Response:** Nurse dashboard statistics and upcoming shifts.
* **GET `/api/dashboard/incharge`**
  * **Response:** In-charge metrics for department coverage, staffing shortages, and pending approvals.
* **GET `/api/dashboard/duty-officer`**
  * **Response:** System-wide staffing metrics, total active requests, and overall hospital-wide coverage percentage.

### 5.3 Duty Officer Operations (`/api/duty-officer`)
* **GET `/api/duty-officer/staffing-report`**
  * **Parameters:** `date` (YYYY-MM-DD), `departmentId` (optional)
  * **Response:** Real-time staffing list and counts (assigned, unassigned, total).
* **GET `/api/duty-officer/nurses`**
  * **Response:** Full directory of active nurses across all departments.
* **GET `/api/duty-officer/shift-requests`**
  * **Response:** List of all historical and current shift swap requests for compliance monitoring.

### 5.4 Nurses (`/api/nurses`)
* **GET `/api/nurses/me`**
  * **Response:** Profile details of the currently authenticated nurse.
* **GET `/api/nurses/me/shifts`**
  * **Response:** Schedule of shifts assigned to the current nurse.
* **GET `/api/nurses/department/{departmentId}`**
  * **Response:** Directory of nurses assigned to a specific department.
* **GET `/api/nurses/{nurseId}`**
  * **Response:** Details of a specific nurse.

### 5.5 Profile (`/api/profile`)
* **GET `/api/profile/me`**
  * **Response:** Current user profile data.
* **PUT `/api/profile/update`**
  * **Payload:** Profile details (contact info, email).
  * **Response:** Updated profile object.
* **PUT `/api/profile/change-password`**
  * **Payload:** `{ oldPassword, newPassword }`
  * **Response:** Success confirmation status.

### 5.6 Shifts (`/api/shifts`)
* **POST `/api/shifts/assign`**
  * **Payload:** `{ nurseId, shiftId, date }`
  * **Response:** Created ShiftAssignment entity.
  * **Description:** Assigns a nurse to a shift. (Restricted to INCHARGE/DUTY_OFFICER).
* **GET `/api/shifts/department/{departmentId}/schedule`**
  * **Parameters:** `date` (YYYY-MM-DD)
  * **Response:** Roster schedule of all assignments in the department for the specified date.

### 5.7 Shift Swap Requests (`/api/shift-requests`)
* **POST `/api/shift-requests`**
  * **Payload:** `{ requesterAssignmentId, peerAssignmentId }`
  * **Response:** Created ShiftRequest details.
  * **Description:** Initiates a new shift swap proposal from the requesting nurse to a peer.
* **PUT `/api/shift-requests/peer-response`**
  * **Payload:** `{ requestId, accept (boolean) }`
  * **Response:** Updated ShiftRequest status (moves status to `PENDING_INCHARGE` if accepted, or `REJECTED_BY_PEER` if declined).
* **PUT `/api/shift-requests/incharge-review`**
  * **Payload:** `{ requestId, approve (boolean), comments }`
  * **Response:** Updated ShiftRequest status. If approved, the system updates the underlying ShiftAssignments automatically.
* **GET `/api/shift-requests/my-requests`**
  * **Response:** Returns outgoing and incoming swap requests for the current nurse.
* **GET `/api/shift-requests/pending-review`**
  * **Response:** List of requests awaiting In-charge review for the department.
* **GET `/api/shift-requests/eligible-peers`**
  * **Parameters:** `shiftAssignmentId`
  * **Response:** List of other nurses' shift assignments that are valid candidates for swapping.
* **PUT `/api/shift-requests/{id}/cancel`**
  * **Response:** Success status.
  * **Description:** Cancels an outgoing pending swap request initiated by the current nurse.

---

## 6. Setup & Installation Guide

### Prerequisites
1. **Java Development Kit (JDK):** Version 17 or higher.
2. **Node.js:** Version 18.13.0 exactly.
3. **Angular CLI:** Version 17 (`npm install -g @angular/cli@17`).
4. **MySQL Server:** Installed and running on `localhost:3306`.

### 6.1 Database Initialization
Create the target schema in your MySQL client:
```sql
CREATE DATABASE hwscs_db;
```
*Note: Hibernate `ddl-auto` is configured to automatically generate all relational tables upon application startup.*

### 6.2 Backend Deployment
1. Navigate to the project root directory.
2. Configure database credentials in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/hwscs_db
   spring.datasource.username=root
   spring.datasource.password=your_password
   ```
3. Boot the Spring application:
   ```bash
   ./mvnw spring-boot:run
   ```
   *The Tomcat server will bind to `http://localhost:8080`.*

### 6.3 Frontend Deployment
1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the Angular development server:
   ```bash
   ng serve
   ```
   *The application will compile and be accessible at `http://localhost:4200`.*
