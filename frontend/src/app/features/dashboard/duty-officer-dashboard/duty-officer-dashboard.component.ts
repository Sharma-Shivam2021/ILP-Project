import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { DashboardService } from '../../../core/services/dashboard.service';
import { DutyOfficerDashboardDto } from '../../../core/models/models';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-duty-officer-dashboard',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatIconModule],
  templateUrl: './duty-officer-dashboard.component.html',
  styleUrls: ['./duty-officer-dashboard.component.css']
})
export class DutyOfficerDashboardComponent implements OnInit {
  private dashboardService = inject(DashboardService);
  dashboardData$!: Observable<DutyOfficerDashboardDto>;

  ngOnInit() {
    this.dashboardData$ = this.dashboardService.getDutyOfficerDashboard();
  }
}
