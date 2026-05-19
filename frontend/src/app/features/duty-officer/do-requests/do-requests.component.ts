import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTableModule } from '@angular/material/table';
import { MatChipsModule } from '@angular/material/chips';
import { DutyOfficerService } from '../../../core/services/duty-officer.service';
import { ShiftRequestResponseDto } from '../../../core/models/models';

@Component({
  selector: 'app-do-requests',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatIconModule, MatSnackBarModule, MatTableModule, MatChipsModule],
  templateUrl: './do-requests.component.html',
  styleUrls: ['./do-requests.component.css']
})
export class DoRequestsComponent implements OnInit {
  private doService = inject(DutyOfficerService);
  private snackBar = inject(MatSnackBar);

  requests: ShiftRequestResponseDto[] = [];
  loading = false;
  displayedColumns = ['requester', 'peer', 'shift', 'date', 'status'];

  ngOnInit() { this.loadRequests(); }

  loadRequests() {
    this.loading = true;
    this.doService.getDepartmentShiftRequests().subscribe({
      next: (list) => {
        this.requests = list.sort((a, b) =>
          new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
        );
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.snackBar.open('Failed to load shift requests', 'Dismiss', { duration: 3000 });
        this.loading = false;
      }
    });
  }

  getInitials(name: string): string {
    if (!name) return '?';
    return name.slice(0, 2).toUpperCase();
  }

  getStatusLabel(status: string): string {
    return status.replace(/_/g, ' ');
  }
}
