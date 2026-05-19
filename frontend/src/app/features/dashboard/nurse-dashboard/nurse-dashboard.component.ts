import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { DashboardService } from '../../../core/services/dashboard.service';
import { NurseDashboardDto } from '../../../core/models/models';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-nurse-dashboard',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatTableModule, MatButtonModule, MatIconModule],
  templateUrl: './nurse-dashboard.component.html',
  styleUrls: ['./nurse-dashboard.component.css']
})
export class NurseDashboardComponent implements OnInit {
  private dashboardService = inject(DashboardService);
  dashboardData$!: Observable<NurseDashboardDto>;

  shiftColumns: string[] = ['date', 'shift', 'time', 'swapped'];
  requestColumns: string[] = ['requester', 'shift', 'status', 'createdAt'];

  ngOnInit() {
    this.dashboardData$ = this.dashboardService.getNurseDashboard();
  }
}
