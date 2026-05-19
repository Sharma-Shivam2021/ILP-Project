import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { DashboardService } from '../../../core/services/dashboard.service';
import { InchargeDashboardDto } from '../../../core/models/models';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-incharge-dashboard',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatTableModule, MatButtonModule, MatIconModule],
  templateUrl: './incharge-dashboard.component.html',
  styleUrls: ['./incharge-dashboard.component.css']
})
export class InchargeDashboardComponent implements OnInit {
  private dashboardService = inject(DashboardService);
  dashboardData$!: Observable<InchargeDashboardDto>;

  assignmentColumns: string[] = ['nurse', 'date', 'shift', 'time', 'swapped'];

  ngOnInit() {
    this.dashboardData$ = this.dashboardService.getInchargeDashboard();
  }
}
