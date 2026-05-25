import { inject } from '@angular/core';
import { CanActivateFn, Router, Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login/login.component';
import { ChangePasswordComponent } from './features/auth/change-password/change-password.component';
import { LayoutComponent } from './layout/layout.component';
import { authGuard } from './core/guards/auth.guard';
import { LandingComponent } from './features/landing/landing.component';
import { roleGuard } from './core/guards/role.guard';
import { NurseDashboardComponent } from './features/dashboard/nurse-dashboard/nurse-dashboard.component';
import { InchargeDashboardComponent } from './features/dashboard/incharge-dashboard/incharge-dashboard.component';
import { DutyOfficerDashboardComponent } from './features/dashboard/duty-officer-dashboard/duty-officer-dashboard.component';
import { AuthService } from './core/services/auth.service';
import { Role } from './core/models/models';

// Guard that redirects to the correct dashboard based on role, or to /login if not authenticated
const roleRedirectGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const user = authService.currentUserValue;

  if (!user) {
    router.navigate(['/login']);
    return false;
  }

  switch (user.role) {
    case Role.NURSE: router.navigate(['/nurse']); break;
    case Role.NURSING_INCHARGE: router.navigate(['/incharge']); break;
    case Role.DUTY_OFFICER: router.navigate(['/duty-officer']); break;
    case Role.ADMIN: router.navigate(['/admin']); break;
    default: router.navigate(['/login']);
  }
  return false; // always redirect, never render the empty route itself
};

export const routes: Routes = [
  { path: '', component: LandingComponent, pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'change-password', component: ChangePasswordComponent, canActivate: [authGuard] },
  {
    path: '',
    component: LayoutComponent,
    canActivate: [authGuard],
    children: [
      {
        path: 'nurse',
        component: NurseDashboardComponent,
        canActivate: [roleGuard],
        data: { roles: [Role.NURSE] }
      },
      {
        path: 'nurse/shifts',
        loadComponent: () => import('./features/nurse/assigned-shifts/assigned-shifts.component').then(c => c.AssignedShiftsComponent),
        canActivate: [roleGuard],
        data: { roles: [Role.NURSE] }
      },
      {
        path: 'nurse/requests',
        loadComponent: () => import('./features/nurse/shift-requests/shift-requests.component').then(c => c.ShiftRequestsComponent),
        canActivate: [roleGuard],
        data: { roles: [Role.NURSE] }
      },
      {
        path: 'nurse/history',
        loadComponent: () => import('./features/nurse/shift-history/shift-history.component').then(c => c.ShiftHistoryComponent),
        canActivate: [roleGuard],
        data: { roles: [Role.NURSE] }
      },
      {
        path: 'nurse/profile',
        loadComponent: () => import('./features/nurse/nurse-profile/nurse-profile.component').then(c => c.NurseProfileComponent),
        canActivate: [roleGuard],
        data: { roles: [Role.NURSE] }
      },
      {
        path: 'incharge',
        component: InchargeDashboardComponent,
        canActivate: [roleGuard],
        data: { roles: [Role.NURSING_INCHARGE] }
      },
      {
        path: 'incharge/approvals',
        loadComponent: () => import('./features/incharge/swap-approvals/swap-approvals.component').then(c => c.SwapApprovalsComponent),
        canActivate: [roleGuard],
        data: { roles: [Role.NURSING_INCHARGE] }
      },
      {
        path: 'incharge/schedule',
        loadComponent: () => import('./features/incharge/dept-schedule/dept-schedule.component').then(c => c.DeptScheduleComponent),
        canActivate: [roleGuard],
        data: { roles: [Role.NURSING_INCHARGE] }
      },
      {
        path: 'incharge/nurses',
        loadComponent: () => import('./features/incharge/nurse-directory/nurse-directory.component').then(c => c.NurseDirectoryComponent),
        canActivate: [roleGuard],
        data: { roles: [Role.NURSING_INCHARGE] }
      },
      {
        path: 'incharge/create-shift',
        loadComponent: () => import('./features/incharge/create-shift/create-shift.component').then(c => c.CreateShiftComponent),
        canActivate: [roleGuard],
        data: { roles: [Role.NURSING_INCHARGE] }
      },
      {
        path: 'duty-officer',
        component: DutyOfficerDashboardComponent,
        canActivate: [roleGuard],
        data: { roles: [Role.DUTY_OFFICER] }
      },
      {
        path: 'duty-officer/staffing',
        loadComponent: () => import('./features/duty-officer/do-staffing/do-staffing.component').then(c => c.DoStaffingComponent),
        canActivate: [roleGuard],
        data: { roles: [Role.DUTY_OFFICER] }
      },
      {
        path: 'duty-officer/requests',
        loadComponent: () => import('./features/duty-officer/do-requests/do-requests.component').then(c => c.DoRequestsComponent),
        canActivate: [roleGuard],
        data: { roles: [Role.DUTY_OFFICER] }
      },
      {
        path: 'duty-officer/nurses',
        loadComponent: () => import('./features/duty-officer/do-nurses/do-nurses.component').then(c => c.DoNursesComponent),
        canActivate: [roleGuard],
        data: { roles: [Role.DUTY_OFFICER] }
      },
      {
        path: 'duty-officer/weekly-report',
        loadComponent: () => import('./features/duty-officer/weekly-report/weekly-report.component').then(c => c.WeeklyReportComponent),
        canActivate: [roleGuard],
        data: { roles: [Role.DUTY_OFFICER] }
      },
      {
        path: 'admin',
        loadComponent: () => import('./features/admin/admin-dashboard/admin-dashboard.component').then(c => c.AdminDashboardComponent),
        canActivate: [roleGuard],
        data: { roles: [Role.ADMIN] }
      },
      {
        path: 'admin/users',
        loadComponent: () => import('./features/admin/user-management/user-management.component').then(c => c.UserManagementComponent),
        canActivate: [roleGuard],
        data: { roles: [Role.ADMIN] }
      },
      {
        path: 'admin/departments',
        loadComponent: () => import('./features/admin/dept-management/dept-management.component').then(c => c.DeptManagementComponent),
        canActivate: [roleGuard],
        data: { roles: [Role.ADMIN] }
      },
      {
        path: 'admin/audit-logs',
        loadComponent: () => import('./features/admin/audit-logs/audit-logs.component').then(c => c.AuditLogsComponent),
        canActivate: [roleGuard],
        data: { roles: [Role.ADMIN] }
      },
      // Root path inside layout: redirect to role-specific dashboard or /login
      { path: '', canActivate: [roleRedirectGuard], children: [] }
    ]
  },
  { path: 'register', loadComponent: () => import('./features/auth/register/register.component').then(c => c.RegisterComponent) },
  { path: '**', redirectTo: '' }
];
