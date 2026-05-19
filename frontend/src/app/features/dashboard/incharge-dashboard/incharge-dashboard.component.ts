import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { DashboardService } from '../../../core/services/dashboard.service';
import { Observable } from 'rxjs';
import { InchargeDashboardDto } from '../../../core/models/models';

@Component({
  selector: 'app-incharge-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatProgressBarModule
  ],
  templateUrl: './incharge-dashboard.component.html',
  styleUrls: ['./incharge-dashboard.component.css']
})
export class InchargeDashboardComponent implements OnInit {
  dashboardService = inject(DashboardService);
  dashboardData$!: Observable<InchargeDashboardDto>;
  assignmentColumns: string[] = ['nurse', 'date', 'shift', 'time', 'swapped'];
  readonly targetCapacity = 14;

  ngOnInit() {
    this.dashboardData$ = this.dashboardService.getInchargeDashboard();
  }

  getShiftCount(assignments: any[], shiftName: string): number {
    if (!assignments) return 0;
    return assignments.filter(a => a.shiftName?.toLowerCase() === shiftName.toLowerCase()).length;
  }

  getShiftPercentage(assignments: any[], shiftName: string): number {
    const count = this.getShiftCount(assignments, shiftName);
    return Math.min(Math.round((count / this.targetCapacity) * 100), 100);
  }

  getProgressBarColor(assignments: any[], shiftName: string): string {
    const percentage = this.getShiftPercentage(assignments, shiftName);
    if (percentage >= 80) return 'accent'; // Emerald green theme (in Material, accent is yellow/amber, primary is green. Let's color with custom CSS class actually for maximum visual excellence!)
    if (percentage >= 50) return 'primary';
    return 'warn';
  }

  getInitials(name: string): string {
    if (!name) return 'N';
    return name.slice(0, 2).toUpperCase();
  }
}
