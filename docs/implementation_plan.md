# Implementation Plan - Generate Codebase Diagrams

This plan outlines the creation of comprehensive software design diagrams for the **Hospital Workforce Shift Coordination System (HWSCS)** codebase. The diagrams will be formatted using Mermaid markdown syntax in a new documentation file [diagrams.md](file:///C:/Users/91708/Desktop/ILP-Project/docs/diagrams.md).

## Proposed Diagrams

We will create the following diagrams:

1. **System Architecture Diagram (C4 Container Diagram)**
   - Visualizes the SPA client (Angular 17), REST API server (Spring Boot), database (MySQL), security component (Stateless JWT), and local client-side PDF generator (jsPDF).
2. **Entity-Relationship (ER) Diagram**
   - Details the MySQL database schema, detailing primary/foreign keys, fields, and constraints for tables like `users`, `departments`, `nurses`, `nursing_incharge`, `duty_officer`, `shifts`, `nurse_shifts`, `shift_requests`, and `request_history`.
3. **UML Class Diagram**
   - Illustrates the backend entity relations, core controllers (`AuthController`, `ShiftRequestController`, etc.), service interfaces/implementations (`ShiftRequestServiceImpl`), repositories, and enums.
4. **Sequence Diagram: Shift Swap Request Lifecycle**
   - Traces the process of proposing, peer-responding, and in-charge reviewing shift swap requests, including database updates (e.g., swapping the shifts) and logging history.
5. **State Transition Diagram: ShiftRequest Status**
   - Details how `RequestStatus` transitions between `PENDING_PEER`, `PEER_ACCEPTED`, `PEER_REJECTED`, `APPROVED`, `REJECTED`, and `CANCELLED`.
6. **Activity Diagram: Authentication & Lockout Flow**
   - Represents the first-time password reset process, login attempt failure tracking, account locking, and JWT issuance.

## Proposed Changes

### Documentation

#### [NEW] [diagrams.md](file:///C:/Users/91708/Desktop/ILP-Project/docs/diagrams.md)
- Create a comprehensive documentation file hosting the system architecture, ER, UML class, sequence, state, and activity diagrams in Mermaid markdown format.

## Verification Plan

### Manual Verification
- Review the Mermaid code blocks in the generated file to ensure syntax is valid and they render correctly.
- Cross-reference diagram structures with actual codebase entities (Java classes, enums, controllers) and Angular routing configurations for 100% accuracy.
