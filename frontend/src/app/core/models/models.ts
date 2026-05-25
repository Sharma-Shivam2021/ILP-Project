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
  fullName?: string;
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
  rejectionReason?: string;
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
  allowShiftChange?: boolean;
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
  employeeCode?: string;
  contactEmail?: string;
  contactPhone?: string;
  departmentName?: string;
  nurseType?: string;
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

export interface RegisterRequestDto {
  username?: string;
  password?: string;
  fullName?: string;
  email?: string;
  contactPhone?: string;
  role?: Role;
  employeeCode?: string;
  departmentId?: number;
  nurseType?: string;
}

export interface AuditLog {
  id: number;
  action: string;
  performedBy: string;
  targetUser?: string;
  details: string;
  timestamp: string;
}

export interface AdminUserResponseDto {
  userId: number;
  username: string;
  role: string;
  profileId?: number;
  employeeCode?: string;
  fullName?: string;
  email?: string;
  contactPhone?: string;
  departmentName?: string;
  departmentId?: number;
  isActive: boolean;
  nurseType?: string;
}

export interface DepartmentResponse {
  id: number;
  name: string;
  location: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface DailyAssignmentDto {
  date: string;
  dayOfWeek: string;
  shiftName: string;
  shiftTime?: string;
}

export interface NurseWeeklyReportDto {
  nurseId: number;
  fullName: string;
  employeeCode: string;
  contactEmail?: string;
  contactPhone?: string;
  nurseType: string;
  allowShiftChange: boolean;
  totalShifts: number;
  swapsApprovedCount: number;
  swapsPendingCount: number;
  dailyAssignments: DailyAssignmentDto[];
}

export interface WeeklyReportDto {
  departmentName: string;
  startDate: string;
  endDate: string;
  nurses: NurseWeeklyReportDto[];
}



