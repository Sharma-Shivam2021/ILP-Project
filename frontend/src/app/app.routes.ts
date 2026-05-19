import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login/login.component';
import { ChangePasswordComponent } from './features/auth/change-password/change-password.component';
import { LayoutComponent } from './layout/layout.component';
import { authGuard } from './core/guards/auth.guard';
import { roleGuard } from './core/guards/role.guard';
import { NurseDashboardComponent } from './features/dashboard/nurse-dashboard/nurse-dashboard.component';
import { InchargeDashboardComponent } from './features/dashboard/incharge-dashboard/incharge-dashboard.component';
import { DutyOfficerDashboardComponent } from './features/dashboard/duty-officer-dashboard/duty-officer-dashboard.component';
import { Role } from './core/models/models';

export const routes: Routes = [
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
      { path: '', redirectTo: 'login', pathMatch: 'full' }
    ]
  },
  { path: '**', redirectTo: 'login' }
];
