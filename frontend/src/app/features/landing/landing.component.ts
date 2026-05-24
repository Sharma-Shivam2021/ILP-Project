import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatTabsModule } from '@angular/material/tabs';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatTabsModule
  ],
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.css']
})
export class LandingComponent {
  authService = inject(AuthService);
  router = inject(Router);

  get user() {
    return this.authService.currentUserValue;
  }

  getDashboardLink(): string {
    if (!this.user) return '/login';
    switch (this.user.role) {
      case 'NURSE': return '/nurse';
      case 'NURSING_INCHARGE': return '/incharge';
      case 'DUTY_OFFICER': return '/duty-officer';
      default: return '/login';
    }
  }

  enterPortal() {
    const link = this.getDashboardLink();
    this.router.navigate([link]);
  }

  logout() {
    this.authService.logout();
  }
}
