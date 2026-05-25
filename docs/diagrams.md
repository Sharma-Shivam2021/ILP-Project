# Hospital Workforce Shift Coordination System (HWSCS) - Diagrams

This document contains a comprehensive collection of architecture and design diagrams for the **Hospital Workforce Shift Coordination System (HWSCS)**. The diagrams are represented using **Mermaid.js** syntax, which renders natively in Markdown viewers.

---

## 1. System Architecture (C4 Container Diagram)

The system is built on a modern multi-tier architecture, featuring an **Angular SPA frontend**, a **Spring Boot REST API backend**, and a **MySQL Database**. PDF report generation is offloaded to the client using `jsPDF` to optimize server resource usage.

```mermaid
flowchart TB
    %% Nodes
    subgraph Client [Client Tier - Web Browser]
        direction TB
        Users("👥 User Roles<br/>(Nurse, In-Charge, Duty Officer)")
        AngularSPA["🅰️ Angular 17 SPA<br/>(Standalone Components,<br/>Angular Material)"]
        JSPDF["📄 jsPDF Generator<br/>(Client-Side Reports)"]
    end

    subgraph Server [Application Tier - Spring Boot Backend]
        direction TB
        SecurityFilter["🔒 Spring Security Filter<br/>(Stateless JWT Authentication)"]
        Controllers["🎮 REST Controllers<br/>(Auth, Shift, Request, etc.)"]
        Services["⚙️ Business Services<br/>(ShiftRequestService, etc.)"]
        JPA["🗃️ Spring Data JPA<br/>(Hibernate ORM)"]
    end

    subgraph Data [Data Tier - Relational Storage]
        Database[("💾 MySQL Database<br/>(hwscs_db)")]
    end

    %% Connections
    Users <-->|"Interacts (HTTPS)"| AngularSPA
    AngularSPA -->|"Generates reports locally"| JSPDF
    AngularSPA <-->|"REST Calls<br/>(Bearer Token)"| SecurityFilter
    SecurityFilter <-->|Validate & Pass| Controllers
    Controllers <--> Services
    Services <--> JPA
    JPA <-->|"SQL Queries"| Database

    %% Styles
    classDef client fill:#f0f9ff,stroke:#0284c7,stroke-width:2px,color:#0f172a;
    classDef server fill:#fdf4ff,stroke:#c084fc,stroke-width:2px,color:#0f172a;
    classDef data fill:#f0fdf4,stroke:#22c55e,stroke-width:2px,color:#0f172a;
    
    class Users,AngularSPA,JSPDF client;
    class SecurityFilter,Controllers,Services,JPA server;
    class Database data;
```

---

## 2. Entity-Relationship (ER) Diagram

The HWSCS relational database model tracks users, their roles, hospital department structures, schedules, and the full audit trail of shift swaps.

```mermaid
erDiagram
    DEPARTMENTS {
        int id PK
        string name "unique, NOT NULL"
        string location "NOT NULL"
        datetime createdAt
        datetime updatedAt
    }

    USERS {
        bigint id PK
        string username "unique, NOT NULL"
        string password "NOT NULL"
        string role "ADMIN | NURSE | NURSING_INCHARGE | DUTY_OFFICER"
        int department_id FK "nullable"
        boolean isActive
        boolean firstLogin
        int failedAttempts
        boolean accountLocked
        datetime lockTime
        datetime createdAt
        datetime updatedAt
    }

    NURSES {
        int id PK
        int user_id FK "unique, NOT NULL"
        int department_id FK "NOT NULL"
        string employeeCode "unique, NOT NULL"
        string fullName "NOT NULL"
        string nurseType "PERMANENT | CONTRACTUAL"
        string contactPhone
        string contactEmail
        datetime createdAt
        datetime updatedAt
    }

    NURSING_INCHARGE {
        int id PK
        int user_id FK "unique, NOT NULL"
        int department_id FK "NOT NULL"
        string employeeCode "unique, NOT NULL"
        string fullName "NOT NULL"
        string contactPhone
        string contactEmail
        datetime createdAt
        datetime updatedAt
    }

    DUTY_OFFICER {
        int id PK
        int user_id FK "unique, NOT NULL"
        int department_id FK "NOT NULL"
        string employeeCode "unique, NOT NULL"
        string fullName "NOT NULL"
        string contactPhone
        string contactEmail
        datetime createdAt
        datetime updatedAt
    }

    SHIFTS {
        int id PK
        string shiftName "unique, NOT NULL"
        time startTime "NOT NULL"
        time endTime "NOT NULL"
        boolean active
        datetime createdAt
        datetime updatedAt
    }

    NURSE_SHIFTS {
        int id PK
        int nurse_id FK "NOT NULL"
        int shift_id FK "NOT NULL"
        date shiftDate "NOT NULL"
        boolean isSwapped
        datetime createdAt
        datetime updatedAt
    }

    SHIFT_REQUESTS {
        int id PK
        int requester_nurse_id FK "NOT NULL"
        int peer_nurse_id FK "NOT NULL"
        int requester_nurse_shift_id FK "NOT NULL"
        int peer_nurse_shift_id FK "NOT NULL"
        string status "PENDING_PEER | PEER_ACCEPTED | etc."
        string remarks "text"
        datetime createdAt
        datetime updatedAt
    }

    REQUEST_HISTORY {
        int id PK
        int shift_request_id FK "NOT NULL"
        int actor_user_id FK "NOT NULL"
        string action "CREATED | APPROVED | etc."
        string remarks "text"
        datetime actedAt
    }

    %% Cardinalities
    USERS }o--o| DEPARTMENTS : "belongs to"
    NURSES ||--|| USERS : "links profile"
    NURSES }|--|| DEPARTMENTS : "belongs to"
    NURSING_INCHARGE ||--|| USERS : "links profile"
    NURSING_INCHARGE }|--|| DEPARTMENTS : "manages"
    DUTY_OFFICER ||--|| USERS : "links profile"
    DUTY_OFFICER }|--|| DEPARTMENTS : "oversees"
    NURSE_SHIFTS }|--|| NURSES : "assigned to"
    NURSE_SHIFTS }|--|| SHIFTS : "uses shift type"
    SHIFT_REQUESTS }|--|| NURSES : "requester"
    SHIFT_REQUESTS }|--|| NURSES : "peer recipient"
    SHIFT_REQUESTS }|--|| NURSE_SHIFTS : "swapping requester shift"
    SHIFT_REQUESTS }|--|| NURSE_SHIFTS : "swapping peer shift"
    REQUEST_HISTORY }|--|| SHIFT_REQUESTS : "tracks status changes"
    REQUEST_HISTORY }|--|| USERS : "performed by user"
```

---

## 3. UML Class Diagram (Core Domain & Services)

Below is the Class Diagram representing the relationships, attributes, and key operations of the domain entities, service layer implementations, and status enums.

```mermaid
classDiagram
    %% Core Entities
    class User {
        +Long id
        +String username
        +String password
        +Role role
        +Department department
        +Boolean isActive
        +Boolean firstLogin
        +Integer failedAttempts
        +Boolean accountLocked
        +LocalDateTime lockTime
        +prePersist() void
        +preUpdate() void
    }

    class Department {
        +Integer id
        +String name
        +String location
    }

    class Nurse {
        +Integer id
        +User user
        +Department department
        +String employeeCode
        +String fullName
        +NurseType nurseType
        +String contactPhone
        +String contactEmail
    }

    class Shift {
        +Integer id
        +String shiftName
        +LocalTime startTime
        +LocalTime endTime
        +Boolean active
    }

    class NurseShift {
        +Integer id
        +Nurse nurse
        +Shift shift
        +LocalDate shiftDate
        +Boolean isSwapped
    }

    class ShiftRequest {
        +Integer id
        +Nurse requesterNurse
        +Nurse peerNurse
        +NurseShift requesterNurseShift
        +NurseShift peerNurseShift
        +RequestStatus status
        +String remarks
    }

    class RequestHistory {
        +Integer id
        +ShiftRequest shiftRequest
        +User actorUser
        +RequestAction action
        +String remarks
        +LocalDateTime actedAt
    }

    %% Service Layer
    class ShiftRequestService {
        <<interface>>
        +createRequest(CreateShiftRequestDto, String) ShiftRequestResponseDto
        +cancelRequest(Integer, String) ShiftRequestResponseDto
        +peerResponse(PeerResponseDto, String) ShiftRequestResponseDto
        +inchargeReview(InchargeApprovalDto, String) ShiftRequestResponseDto
        +getMyRequests(String) List~ShiftRequestResponseDto~
        +getPendingInchargeReview(String) List~ShiftRequestResponseDto~
        +getEligiblePeers(Integer, String) List~EligiblePeerDto~
        +getRequestHistory(Integer, String) List~RequestHistoryResponseDto~
    }

    class ShiftRequestServiceImpl {
        -ShiftRequestRepository shiftRequestRepository
        -NurseRepository nurseRepository
        -NurseShiftRepository nurseShiftRepository
        -RequestHistoryRepository requestHistoryRepository
        -NursingInchargeRepository nursingInchargeRepository
        +executeShiftSwap(ShiftRequest) void
        -saveHistory(ShiftRequest, User, RequestAction, String) void
    }

    %% Enums
    class Role {
        <<enumeration>>
        ADMIN
        NURSE
        NURSING_INCHARGE
        DUTY_OFFICER
    }

    class NurseType {
        <<enumeration>>
        PERMANENT
        CONTRACTUAL
    }

    class RequestStatus {
        <<enumeration>>
        PENDING_PEER
        PEER_ACCEPTED
        PEER_REJECTED
        APPROVED
        REJECTED
        CANCELLED
    }

    class RequestAction {
        <<enumeration>>
        CREATED
        PEER_ACCEPTED
        PEER_REJECTED
        APPROVED
        REJECTED
        CANCELLED
    }

    %% Associations
    User --> Role
    User --> Department
    Nurse --> User
    Nurse --> Department
    Nurse --> NurseType
    NurseShift --> Nurse
    NurseShift --> Shift
    ShiftRequest --> Nurse : "requester & peer"
    ShiftRequest --> NurseShift : "requester & peer shifts"
    ShiftRequest --> RequestStatus
    RequestHistory --> ShiftRequest
    RequestHistory --> User
    RequestHistory --> RequestAction
    
    ShiftRequestService <|.. ShiftRequestServiceImpl
    ShiftRequestServiceImpl --> User : Uses
    ShiftRequestServiceImpl --> ShiftRequest : Manages
```

---

## 4. Sequence Diagram: Shift Swap Request Lifecycle

This sequence diagram details the full, transactional, multi-step workflow involved in proposing and finalizing a shift swap between two nurses within a department.

```mermaid
sequenceDiagram
    autonumber
    actor NurseA as Nurse A (Requester)
    actor NurseB as Nurse B (Peer)
    actor InCharge as Nursing In-Charge
    participant FE as Angular Frontend
    participant BE as Spring Boot Backend
    participant DB as MySQL Database

    %% --- Creation ---
    Note over NurseA, FE: 1. Request Swap Initiation
    NurseA->>FE: Select peer shift and click Swap
    FE->>BE: POST /api/shift-requests (requesterShiftId, peerShiftId, remarks)
    Note over BE: Validaing rules: same department, same date,<br/>future/current shifts, no duplicate requests
    BE->>DB: Save ShiftRequest (status = PENDING_PEER)
    DB-->>BE: Saved entity details
    BE->>DB: Save RequestHistory (action = CREATED)
    DB-->>BE: Saved history details
    BE-->>FE: Return ShiftRequestResponseDto
    FE-->>NurseA: UI shows request pending peer response

    %% --- Peer Response ---
    Note over NurseB, FE: 2. Peer Review and Acceptance
    NurseB->>FE: Open Incoming Swap Requests
    FE->>BE: GET /api/shift-requests/my-requests
    BE->>DB: Query requests where peer = Nurse B
    DB-->>BE: List of requests
    BE-->>FE: Return requests list
    NurseB->>FE: Accept swap request
    FE->>BE: PUT /api/shift-requests/peer-response (requestId, accept = true)
    BE->>DB: Update ShiftRequest status to PEER_ACCEPTED
    BE->>DB: Save RequestHistory (action = PEER_ACCEPTED)
    BE-->>FE: Return updated ShiftRequestResponseDto
    FE-->>NurseB: UI updates status (Pending Incharge approval)

    %% --- Incharge Review ---
    Note over InCharge, FE: 3. Managerial Approval
    InCharge->>FE: Go to "Swap Approvals" page
    FE->>BE: GET /api/shift-requests/pending-review
    BE->>DB: Query requests (status = PEER_ACCEPTED, department = incharge's)
    DB-->>BE: List of pending approvals
    BE-->>FE: Return list
    InCharge->>FE: Click Approve Swap
    FE->>BE: PUT /api/shift-requests/incharge-review (requestId, approve = true)
    Note over BE: Validate: Incharge manages requester's department
    
    %% Database Transaction execution
    rect rgb(240, 253, 244)
        Note over BE, DB: DB Transaction: Execute Shift Swap
        BE->>DB: Swap Shift ID records between NurseShift A & NurseShift B
        BE->>DB: Set both NurseShifts isSwapped = true
        BE->>DB: Update ShiftRequest status to APPROVED
        BE->>DB: Save RequestHistory (action = APPROVED)
    end
    
    BE-->>FE: Return success approved response
    FE-->>InCharge: UI confirms approval and updates roster
```

---

## 5. State Machine Diagram: ShiftRequest Status Lifecycle

The state machine diagram defines all valid state transitions for a shift swap request, starting from creation to ultimate completion or rejection.

```mermaid
stateDiagram-v2
    [*] --> PENDING_PEER : Initiator creates request (CREATED)
    
    state peer_check <<choice>>
    PENDING_PEER --> peer_check : Peer Responds / Action Taken

    peer_check --> CANCELLED : Requester cancels request (CANCELLED)
    peer_check --> PEER_REJECTED : Peer declines request (PEER_REJECTED)
    peer_check --> PEER_ACCEPTED : Peer accepts request (PEER_ACCEPTED)
    
    state incharge_check <<choice>>
    PEER_ACCEPTED --> incharge_check : In-Charge Reviews

    incharge_check --> REJECTED : In-Charge declines (REJECTED)
    incharge_check --> APPROVED : In-Charge approves (APPROVED)

    %% Final states
    CANCELLED --> [*]
    PEER_REJECTED --> [*]
    REJECTED --> [*]
    
    state APPROVED {
        [*] --> UpdateNurseShifts : Swap Shift records in DB
        UpdateNurseShifts --> SetIsSwappedTrue : Mark assignments as swapped
        SetIsSwappedTrue --> [*]
    }
    APPROVED --> [*]
```

---

## 6. Activity Diagram: User Login & Account Lockout Flow

This flowchart illustrates the authentication process, password validation, enforcing first-time password resets, and account lockout handling (lockout triggers after 4 failed attempts; locks for 30 seconds).

```mermaid
flowchart TD
    Start([User opens App & enters login details]) --> RequestAuth[POST /api/auth/login]
    RequestAuth --> FindUser{Does Username exist?}
    
    %% User checks
    FindUser -- No --> ReturnError[Throw Invalid Username or Password]
    FindUser -- Yes --> IsLocked{Is User Account Locked?}

    %% Lockout Handling
    IsLocked -- Yes --> CheckLockTime{Has 30 seconds expired?}
    CheckLockTime -- Yes --> Unlock[Unlock Account, reset attempts to 0] --> CheckPassword
    CheckLockTime -- No --> ThrowLocked[Throw Account temporarily locked. Try again later] --> EndLock([Login Blocked])

    %% Password Validation
    IsLocked -- No --> CheckPassword{Is Password Correct?}
    CheckPassword -- Yes --> ResetAttempts[Reset failedAttempts to 0]
    ResetAttempts --> IsFirstLogin{Is it First Login?}
    
    %% First Login redirect
    IsFirstLogin -- Yes --> ForceReset[Redirect to Force Password Change]
    IsFirstLogin -- No --> GenerateJWT[Generate JWT token & return user details] --> AccessGranted([Access Granted])

    %% Incorrect password path
    CheckPassword -- No --> IncAttempts[Increment failedAttempts by 1]
    IncAttempts --> ExceededMax{Are failedAttempts >= 4?}
    ExceededMax -- Yes --> LockAccount[Set accountLocked = true, lockTime = now] --> ReturnError
    ExceededMax -- No --> SaveAttempts[Save updated attempts] --> ReturnError
    
    ReturnError --> EndError([Login Failed])
```

---

## 7. Use Case Diagram

The Use Case Diagram displays system capabilities partitioned by user role boundaries, including Nurses, Nursing In-Charge, and Duty Officers.

```mermaid
graph LR
    %% Actors
    subgraph Actors [Actors]
        Nurse("🧑‍⚕️ Nurse")
        InCharge("👩‍💼 Nursing In-Charge")
        DutyOfficer("👨‍💼 Duty Officer")
    end

    %% System Boundary
    subgraph System [System Boundary: HWSCS]
        direction TB
        %% Common Use Cases
        UC1((Login & Session Management))
        UC2((First-Time Password Change))
        UC3((Manage User Profile))

        %% Nurse Use Cases
        UC4((View Personal Shifts Roster))
        UC5((Propose Peer Shift Swap))
        UC6((Respond to Swap Requests))
        UC7((Cancel Outgoing Swap Request))

        %% In-Charge Use Cases
        UC8((Assign Shift to Nurse))
        UC9((View Department Schedule))
        UC10((Review & Approve Shift Swaps))
        UC11((View Department Nurses))

        %% Duty Officer Use Cases
        UC12((View Coverage Analytics))
        UC13((Generate PDF Audit Reports))
        UC14((View Hospital Directory))
        UC15((Monitor Shift Swaps Audit))
    end

    %% Actor to Use Case Connections
    Nurse --> UC1
    Nurse --> UC2
    Nurse --> UC3
    Nurse --> UC4
    Nurse --> UC5
    Nurse --> UC6
    Nurse --> UC7

    InCharge --> UC1
    InCharge --> UC3
    InCharge --> UC8
    InCharge --> UC9
    InCharge --> UC10
    InCharge --> UC11

    DutyOfficer --> UC1
    DutyOfficer --> UC3
    DutyOfficer --> UC12
    DutyOfficer --> UC13
    DutyOfficer --> UC14
    DutyOfficer --> UC15

    %% Include relationships
    UC1 -.->|include| UC2

    %% Styles
    classDef actorStyle fill:#f8fafc,stroke:#475569,stroke-width:2px,color:#0f172a;
    classDef ucStyle fill:#f0fdf4,stroke:#16a34a,stroke-width:1.5px,color:#0f172a;
    
    class Nurse,InCharge,DutyOfficer actorStyle;
    class UC1,UC2,UC3,UC4,UC5,UC6,UC7,UC8,UC9,UC10,UC11,UC12,UC13,UC14,UC15 ucStyle;
```

