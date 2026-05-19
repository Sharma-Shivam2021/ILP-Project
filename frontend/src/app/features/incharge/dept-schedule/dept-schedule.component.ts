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
import { ProfileService } from '../../../core/services/profile.service';
import { ShiftService } from '../../../core/services/shift.service';
import { NurseService } from '../../../core/services/nurse.service';
import { AssignShiftDto, NurseResponseDto, NurseShiftResponseDto, ShiftResponseDto } from '../../../core/models/models';

@Component({
  selector: 'app-dept-schedule',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatFormFieldModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatSnackBarModule,
    MatTableModule
  ],
  templateUrl: './dept-schedule.component.html',
  styleUrls: ['./dept-schedule.component.css']
})
export class DeptScheduleComponent implements OnInit {
  profileService = inject(ProfileService);
  shiftService = inject(ShiftService);
  nurseService = inject(NurseService);
  snackBar = inject(MatSnackBar);

  departmentId!: number;
  departmentName = '';
  selectedDate: string = new Date().toISOString().split('T')[0];
  
  assignments: NurseShiftResponseDto[] = [];
  nurses: NurseResponseDto[] = [];
  shifts: ShiftResponseDto[] = [];
  
  loading = false;
  showAssignForm = false;
  
  assignDto: AssignShiftDto = {
    nurseId: 0,
    shiftId: 0,
    shiftDate: ''
  };

  displayedColumns: string[] = ['nurse', 'shift', 'time', 'status'];

  ngOnInit() {
    this.resolveProfileAndLoad();
  }

  resolveProfileAndLoad() {
    this.loading = true;
    this.profileService.getMyProfile().subscribe({
      next: (profile) => {
        if (profile && profile.department) {
          this.departmentId = profile.department.id;
          this.departmentName = profile.department.name;
          this.loadData();
        } else {
          this.snackBar.open('Unable to resolve department profile.', 'Dismiss', { duration: 4000 });
          this.loading = false;
        }
      },
      error: (err) => {
        console.error('Error fetching incharge profile', err);
        this.snackBar.open('Failed to resolve profile', 'Dismiss', { duration: 4000 });
        this.loading = false;
      }
    });
  }

  loadData() {
    this.loading = true;
    // Get schedule
    this.shiftService.getDepartmentSchedule(this.departmentId, this.selectedDate).subscribe({
      next: (schedule) => {
        this.assignments = schedule;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error fetching schedule', err);
        this.snackBar.open('Failed to load roster schedule', 'Dismiss', { duration: 3000 });
        this.loading = false;
      }
    });

    // Get nurses for assignment dropdown
    this.nurseService.getNursesByDepartment(this.departmentId).subscribe({
      next: (nursesList) => {
        this.nurses = nursesList;
      },
      error: (err) => {
        console.error('Error fetching nurses list', err);
      }
    });

    // Get shifts for assignment dropdown
    this.shiftService.getAllShifts().subscribe({
      next: (shiftsList) => {
        this.shifts = shiftsList.filter(s => s.active);
      },
      error: (err) => {
        console.error('Error fetching shifts list', err);
      }
    });
  }

  onDateChange() {
    if (this.departmentId) {
      this.loadData();
    }
  }

  toggleAssignForm() {
    this.showAssignForm = !this.showAssignForm;
    if (this.showAssignForm) {
      this.assignDto = {
        nurseId: 0,
        shiftId: 0,
        shiftDate: this.selectedDate
      };
    }
  }

  submitAssignment() {
    if (!this.assignDto.nurseId || !this.assignDto.shiftId || !this.assignDto.shiftDate) {
      this.snackBar.open('Please fill out all assignment fields', 'Dismiss', { duration: 3000 });
      return;
    }

    this.loading = true;
    this.shiftService.assignShift(this.assignDto).subscribe({
      next: (res) => {
        this.snackBar.open(`Shift successfully assigned to ${res.nurseFullName}!`, 'Close', { duration: 3000 });
        this.showAssignForm = false;
        this.loadData();
      },
      error: (err) => {
        console.error('Error assigning shift', err);
        const errMsg = err.error?.message || 'Failed to assign shift. The nurse might already be assigned to a shift on this day.';
        this.snackBar.open(errMsg, 'Close', { duration: 5000 });
        this.loading = false;
      }
    });
  }

  getInitials(name: string): string {
    if (!name) return 'N';
    return name.slice(0, 2).toUpperCase();
  }
}
