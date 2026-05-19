import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { ShiftRequestService } from '../../../core/services/shift-request.service';
import { ShiftRequestResponseDto } from '../../../core/models/models';

@Component({
  selector: 'app-swap-approvals',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatFormFieldModule,
    MatSnackBarModule
  ],
  templateUrl: './swap-approvals.component.html',
  styleUrls: ['./swap-approvals.component.css']
})
export class SwapApprovalsComponent implements OnInit {
  shiftRequestService = inject(ShiftRequestService);
  snackBar = inject(MatSnackBar);

  pendingRequests: ShiftRequestResponseDto[] = [];
  loading = false;
  
  // Track which request is currently being reviewed (active card id)
  activeRequestId: number | null = null;
  remarks = '';

  ngOnInit() {
    this.loadPendingRequests();
  }

  loadPendingRequests() {
    this.loading = true;
    this.shiftRequestService.getPendingReview().subscribe({
      next: (requests) => {
        this.pendingRequests = requests;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error fetching pending approvals', err);
        this.snackBar.open('Failed to load pending swap requests', 'Dismiss', { duration: 4000 });
        this.loading = false;
      }
    });
  }

  startReview(requestId: number) {
    this.activeRequestId = requestId;
    this.remarks = '';
  }

  cancelReview() {
    this.activeRequestId = null;
    this.remarks = '';
  }

  submitDecision(approved: boolean) {
    if (this.activeRequestId === null) return;

    const dto = {
      shiftRequestId: this.activeRequestId,
      approved: approved,
      remarks: this.remarks.trim() || undefined
    };

    this.shiftRequestService.inchargeReview(dto).subscribe({
      next: (res) => {
        const action = approved ? 'approved' : 'rejected';
        this.snackBar.open(`Shift swap request has been successfully ${action}!`, 'Close', { duration: 3000 });
        this.activeRequestId = null;
        this.remarks = '';
        this.loadPendingRequests();
      },
      error: (err) => {
        console.error('Error deciding swap request', err);
        const errMsg = err.error?.message || 'Failed to submit approval review.';
        this.snackBar.open(errMsg, 'Close', { duration: 5000 });
      }
    });
  }
}
