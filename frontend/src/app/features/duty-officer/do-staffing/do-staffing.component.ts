import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTableModule } from '@angular/material/table';
import { DutyOfficerService } from '../../../core/services/duty-officer.service';
import { ShiftService } from '../../../core/services/shift.service';

import { DepartmentStaffingDto, NurseResponseDto, ShiftResponseDto, AssignShiftDto } from '../../../core/models/models';

@Component({
  selector: 'app-do-staffing',
  standalone: true,
  imports: [
    CommonModule, FormsModule,
    MatCardModule, MatButtonModule, MatIconModule,
    MatInputModule, MatFormFieldModule, MatSelectModule,
    MatDatepickerModule, MatNativeDateModule,
    MatSnackBarModule, MatTableModule
  ],
  templateUrl: './do-staffing.component.html',
  styleUrls: ['./do-staffing.component.css']
})
export class DoStaffingComponent implements OnInit {
  private doService = inject(DutyOfficerService);
  private shiftService = inject(ShiftService);
  private snackBar = inject(MatSnackBar);

  selectedDate: Date = new Date();
  staffingReport: DepartmentStaffingDto | null = null;
  nurses: NurseResponseDto[] = [];
  shifts: ShiftResponseDto[] = [];

  loading = false;
  showAssignForm = false;

  assignDto: AssignShiftDto = { nurseId: 0, shiftId: 0, shiftDate: '' };
  displayedColumns = ['nurse', 'shift', 'time', 'status'];

  ngOnInit() {
    this.loadReport();
    this.loadDropdownData();
  }

  loadReport() {
    this.loading = true;
    this.doService.getStaffingReport(this.formatDate(this.selectedDate)).subscribe({
      next: (r) => { this.staffingReport = r; this.loading = false; },
      error: (err) => {
        console.error(err);
        this.snackBar.open('Failed to load staffing report', 'Dismiss', { duration: 3000 });
        this.loading = false;
      }
    });
  }

  loadDropdownData() {
    this.doService.getDepartmentNurses().subscribe({
      next: (list) => this.nurses = list,
      error: (err) => console.error('Error loading nurses', err)
    });
    this.shiftService.getAllShifts().subscribe({
      next: (list) => this.shifts = list.filter(s => s.active),
      error: (err) => console.error('Error loading shifts', err)
    });
  }

  onDateChange() { this.loadReport(); }

  toggleAssignForm() {
    this.showAssignForm = !this.showAssignForm;
    if (this.showAssignForm) {
      this.assignDto = { nurseId: 0, shiftId: 0, shiftDate: this.formatDate(this.selectedDate) };
    }
  }

  submitAssignment() {
    if (!this.assignDto.nurseId || !this.assignDto.shiftId || !this.assignDto.shiftDate) {
      this.snackBar.open('Please fill all fields', 'Dismiss', { duration: 3000 });
      return;
    }
    const payload = { ...this.assignDto, shiftDate: this.formatDate(this.assignDto.shiftDate) };
    this.loading = true;
    this.shiftService.assignShift(payload).subscribe({
      next: (res) => {
        this.snackBar.open(`Shift assigned to ${res.nurseFullName}!`, 'Close', { duration: 3000 });
        this.showAssignForm = false;
        this.loadReport();
      },
      error: (err) => {
        const msg = err.error?.message || 'Failed to assign shift.';
        this.snackBar.open(msg, 'Close', { duration: 5000 });
        this.loading = false;
      }
    });
  }

  getInitials(name: string): string {
    if (!name) return 'N';
    return name.slice(0, 2).toUpperCase();
  }

  private formatDate(date: any): string {
    if (!date) return '';
    if (typeof date === 'string') return date.includes('T') ? date.split('T')[0] : date;
    const d = new Date(date);
    return [d.getFullYear(), String(d.getMonth() + 1).padStart(2, '0'), String(d.getDate()).padStart(2, '0')].join('-');
  }
}
