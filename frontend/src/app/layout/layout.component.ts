import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { AuthService } from '../core/services/auth.service';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatSidenavModule,
    MatToolbarModule,
    MatListModule,
    MatIconModule,
    MatButtonModule,
    MatMenuModule
  ],
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.css']
})
export class LayoutComponent {
  authService = inject(AuthService);
  router = inject(Router);

  get user() {
    return this.authService.currentUserValue;
  }

  logout() {
    this.authService.logout();
  }

  getDashboardLink(): string {
    if (!this.user) return '/';
    switch (this.user.role) {
      case 'NURSE': return '/nurse';
      case 'NURSING_INCHARGE': return '/incharge';
      case 'DUTY_OFFICER': return '/duty-officer';
      default: return '/';
    }
  }
}
