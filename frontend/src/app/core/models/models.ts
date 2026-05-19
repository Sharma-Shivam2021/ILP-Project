export enum Role {
  ADMIN = 'ADMIN',
  NURSE = 'NURSE',
  NURSING_INCHARGE = 'NURSING_INCHARGE',
  DUTY_OFFICER = 'DUTY_OFFICER'
}

export enum NurseType {
  FULL_TIME = 'FULL_TIME',
  PART_TIME = 'PART_TIME',
  CONTRACT = 'CONTRACT'
}

export enum RequestStatus {
  PENDING_PEER = 'PENDING_PEER',
  PENDING_INCHARGE = 'PENDING_INCHARGE',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED',
  CANCELLED = 'CANCELLED'
}

export interface User {
  id: number;
  email: string;
  name: string;
  role: Role;
}

export interface LoginResponseDto {
  token: string;
  username: string;
  role: Role;
  userId: number;
  firstLogin: boolean;
}

export interface LoginRequestDto {
  username?: string;
  password?: string;
}

// ---- Shift DTOs (matches backend exactly) ----

export interface ShiftResponseDto {
  id: number;
  shiftName: string;
  startTime: string;  // LocalTime as "HH:mm:ss"
  endTime: string;
  active: boolean;
}

export interface NurseShiftResponseDto {
  id: number;
  nurseId: number;
  nurseFullName: string;
  shiftDate: string;   // LocalDate as "YYYY-MM-DD"
  shiftName: string;
  startTime: string;
  endTime: string;
  isSwapped: boolean;
}

export interface ShiftRequestResponseDto {
  id: number;
  status: string;
  remarks: string;
  createdAt: string;  // LocalDateTime

  // Requester info
  requesterNurseId: number;
  requesterNurseName: string;
  requesterShiftDate: string;
  requesterShiftName: string;
  requesterShiftStart: string;
  requesterShiftEnd: string;

  // Peer info
  peerNurseId?: number;
  peerNurseName?: string;
  peerShiftDate?: string;
  peerShiftName?: string;
  peerShiftStart?: string;
  peerShiftEnd?: string;
}

// ---- Dashboard DTOs (matches backend exactly) ----

export interface NurseDashboardDto {
  upcomingShiftCount: number;
  pendingRequestCount: number;
  approvedSwapCount: number;
  upcomingShifts: NurseShiftResponseDto[];
  recentRequests: ShiftRequestResponseDto[];
}

export interface InchargeDashboardDto {
  departmentName: string;
  totalNurses: number;
  assignedToday: number;
  unassignedToday: number;
  pendingApprovals: number;
  activeSwapRequests: number;
  todayAssignments: NurseShiftResponseDto[];
}

export interface DutyOfficerDashboardDto {
  departmentName: string;
  totalNurses: number;
  totalAssignedToday: number;
  totalUnassignedToday: number;
  totalPendingPeerRequests: number;
  totalPendingInchargeRequests: number;
  totalApprovedRequests: number;
}

export interface DepartmentStaffingDto {
  departmentName: string;
  date: string;
  totalNurse: number;
  assignedNurses: number;
  unassignedNurses: number;
  assignments: NurseShiftResponseDto[];
}

// ---- New Profile & Shift Request DTOs ----

export interface NurseResponseDto {
  id: number;
  employeeCode: string;
  fullName: string;
  nurseType: string;
  contactPhone: string;
  contactEmail: string;
  departmentName: string;
  username: string;
}

export interface UpdateProfileDto {
  fullName: string;
  contactPhone: string;
  contactEmail: string;
}

export interface EligiblePeerDto {
  nurseId: number;
  nurseName: string;
  nurseShiftId: number;
  shiftName: string;
  shiftStart: string;
  shiftEnd: string;
  shiftDate: string;
}

export interface CreateShiftRequestDto {
  requesterShiftId: number;
  peerShiftId: number;
  remarks?: string;
}

export interface PeerResponseDto {
  shiftRequestId: number;
  accepted: boolean;
  remarks?: string;
}

export interface RequestHistoryResponseDto {
  id: number;
  actorUsername: string;
  actorRole: string;
  action: string;
  remarks: string;
  actedAt: string; // LocalDateTime
}

export interface InchargeApprovalDto {
  shiftRequestId: number;
  approved: boolean;
  remarks?: string;
}

export interface AssignShiftDto {
  nurseId: number;
  shiftId: number;
  shiftDate: string; // "YYYY-MM-DD"
}

export interface CreateShiftDto {
  shiftName: string;
  startTime: string; // "HH:mm:ss"
  endTime: string; // "HH:mm:ss"
}


