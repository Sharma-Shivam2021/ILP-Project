import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatTabsModule } from '@angular/material/tabs';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { NurseService } from '../../../core/services/nurse.service';
import { ShiftRequestService } from '../../../core/services/shift-request.service';
import { NurseShiftResponseDto, EligiblePeerDto, ShiftRequestResponseDto } from '../../../core/models/models';

@Component({
  selector: 'app-shift-requests',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatTabsModule,
    MatSelectModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSnackBarModule
  ],
  templateUrl: './shift-requests.component.html',
  styleUrls: ['./shift-requests.component.css']
})
export class ShiftRequestsComponent implements OnInit {
  private fb = inject(FormBuilder);
  private nurseService = inject(NurseService);
  private shiftRequestService = inject(ShiftRequestService);
  private snackBar = inject(MatSnackBar);

  // New Request Tab
  swapForm: FormGroup;
  myUpcomingShifts: NurseShiftResponseDto[] = [];
  eligiblePeers: EligiblePeerDto[] = [];
  filteredPeers: EligiblePeerDto[] = [];

  // Tracking & Action Tabs
  myNurseId: number | null = null;
  sentRequests: ShiftRequestResponseDto[] = [];
  receivedRequests: ShiftRequestResponseDto[] = [];

  constructor() {
    this.swapForm = this.fb.group({
      selectedShiftId: [null, Validators.required],
      searchQuery: [''],
      remarks: ['']
    });

    // When a shift is selected, load eligible peers
    this.swapForm.get('selectedShiftId')?.valueChanges.subscribe(shiftId => {
      if (shiftId) {
        this.loadEligiblePeers(shiftId);
      } else {
        this.eligiblePeers = [];
        this.filteredPeers = [];
      }
    });

    // Client-side search filtering
    this.swapForm.get('searchQuery')?.valueChanges.subscribe(query => {
      this.filterPeers(query);
    });
  }

  ngOnInit() {
    this.loadMyShifts();

    // Fetch profile first to get our nurse ID
    this.nurseService.getMyProfile().subscribe({
      next: (profile) => {
        this.myNurseId = profile.id;
        this.loadMyRequests();
      },
      error: (err) => {
        console.error('Failed to load profile for requests', err);
        // Fallback to load requests anyway, though filtering might be affected
        this.loadMyRequests();
      }
    });
  }

  private loadMyShifts() {
    this.nurseService.getMyShifts().subscribe({
      next: (shifts) => {
        const today = new Date().toISOString().split('T')[0];
        // Only allow swapping future/today shifts that are not already swapped
        this.myUpcomingShifts = shifts.filter(s => s.shiftDate >= today && !s.isSwapped);
      },
      error: (err) => console.error('Failed to load shifts', err)
    });
  }

  private loadEligiblePeers(shiftId: number) {
    this.shiftRequestService.getEligiblePeers(shiftId).subscribe({
      next: (peers) => {
        this.eligiblePeers = peers;
        this.filterPeers(this.swapForm.get('searchQuery')?.value || '');
      },
      error: (err) => console.error('Failed to load eligible peers', err)
    });
  }

  private filterPeers(query: string) {
    if (!query) {
      this.filteredPeers = [...this.eligiblePeers];
      return;
    }
    const lowerQuery = query.toLowerCase();
    this.filteredPeers = this.eligiblePeers.filter(p =>
      (p.nurseName && p.nurseName.toLowerCase().includes(lowerQuery)) ||
      (p.shiftName && p.shiftName.toLowerCase().includes(lowerQuery)) ||
      (p.employeeCode && p.employeeCode.toLowerCase().includes(lowerQuery)) ||
      (p.contactEmail && p.contactEmail.toLowerCase().includes(lowerQuery)) ||
      (p.contactPhone && p.contactPhone.toLowerCase().includes(lowerQuery)) ||
      (p.departmentName && p.departmentName.toLowerCase().includes(lowerQuery)) ||
      (p.nurseType && p.nurseType.toLowerCase().includes(lowerQuery))
    );
  }

  sendRequest(peer: EligiblePeerDto) {
    const shiftId = this.swapForm.get('selectedShiftId')?.value;
    if (!shiftId) return;

    const remarks = this.swapForm.get('remarks')?.value || '';

    this.shiftRequestService.createRequest({
      requesterShiftId: shiftId,
      peerShiftId: peer.nurseShiftId,
      remarks: remarks || 'Shift swap request from nurse dashboard'
    }).subscribe({
      next: () => {
        this.snackBar.open('Swap request sent successfully!', 'Close', { duration: 3000 });
        this.swapForm.reset();
        this.loadMyRequests(); // Refresh lists
      },
      error: (err) => {
        console.error('Failed to send request', err);
        this.snackBar.open(err.error?.message || 'Failed to send request', 'Close', { duration: 3000 });
      }
    });
  }

  private loadMyRequests() {
    this.shiftRequestService.getMyRequests().subscribe({
      next: (requests) => {
        // Sort by newest first
        const sorted = requests.sort((a, b) =>
          new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
        );

        if (this.myNurseId) {
          // I initiated the request
          this.sentRequests = sorted.filter(r => r.requesterNurseId === this.myNurseId);
          // I am the requested peer
          this.receivedRequests = sorted.filter(r => r.peerNurseId === this.myNurseId);
        } else {
          this.sentRequests = sorted;
          this.receivedRequests = [];
        }
      },
      error: (err) => console.error('Failed to load requests', err)
    });
  }

  respondToRequest(requestId: number, accept: boolean) {
    this.shiftRequestService.peerResponse({
      shiftRequestId: requestId,
      accepted: accept,
      remarks: accept ? 'Accepted by peer' : 'Rejected by peer'
    }).subscribe({
      next: () => {
        const msg = accept ? 'Request accepted!' : 'Request rejected.';
        this.snackBar.open(msg, 'Close', { duration: 3000 });
        this.loadMyRequests(); // Refresh tracking and action tabs
      },
      error: (err) => {
        console.error('Failed to respond to request', err);
        this.snackBar.open(err.error?.message || 'Failed to submit response.', 'Close', { duration: 3000 });
      }
    });
  }

  cancelSwapRequest(requestId: number) {
    this.shiftRequestService.cancelRequest(requestId).subscribe({
      next: () => {
        this.snackBar.open('Swap request cancelled successfully.', 'Close', { duration: 3000 });
        this.loadMyRequests(); // Refresh tracking list
      },
      error: (err) => {
        console.error('Failed to cancel request', err);
        this.snackBar.open(err.error?.message || 'Failed to cancel request.', 'Close', { duration: 3000 });
      }
    });
  }
}
