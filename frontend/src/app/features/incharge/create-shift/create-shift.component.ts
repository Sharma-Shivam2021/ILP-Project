import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatTableModule } from '@angular/material/table';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { ShiftService } from '../../../core/services/shift.service';
import { ShiftResponseDto } from '../../../core/models/models';

@Component({
  selector: 'app-create-shift',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatFormFieldModule,
    MatTableModule,
    MatSnackBarModule
  ],
  templateUrl: './create-shift.component.html',
  styleUrls: ['./create-shift.component.css']
})
export class CreateShiftComponent implements OnInit {
  shiftService = inject(ShiftService);
  snackBar = inject(MatSnackBar);

  shiftsList: ShiftResponseDto[] = [];
  loading = false;

  shiftName = '';
  startTime = ''; // Format "HH:mm" from input type="time"
  endTime = '';   // Format "HH:mm" from input type="time"

  displayedColumns: string[] = ['name', 'start', 'end', 'status'];

  ngOnInit() {
    this.loadShifts();
  }

  loadShifts() {
    this.loading = true;
    this.shiftService.getAllShifts().subscribe({
      next: (shifts) => {
        this.shiftsList = shifts;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error fetching shifts', err);
        this.snackBar.open('Failed to load existing shifts', 'Dismiss', { duration: 3000 });
        this.loading = false;
      }
    });
  }

  submitCreateShift() {
    if (!this.shiftName.trim() || !this.startTime || !this.endTime) {
      this.snackBar.open('Please fill out all shift details', 'Dismiss', { duration: 3000 });
      return;
    }

    // Append seconds to HH:mm format for Spring Boot LocalTime
    let formattedStart = this.startTime;
    if (formattedStart.length === 5) {
      formattedStart += ':00';
    }

    let formattedEnd = this.endTime;
    if (formattedEnd.length === 5) {
      formattedEnd += ':00';
    }

    const dto = {
      shiftName: this.shiftName.trim(),
      startTime: formattedStart,
      endTime: formattedEnd
    };

    this.loading = true;
    this.shiftService.createShift(dto).subscribe({
      next: (res) => {
        this.snackBar.open(`Shift "${res.shiftName}" successfully created!`, 'Close', { duration: 3000 });
        // Reset form
        this.shiftName = '';
        this.startTime = '';
        this.endTime = '';
        // Reload shifts list
        this.loadShifts();
      },
      error: (err) => {
        console.error('Error creating shift', err);
        const errMsg = err.error?.message || 'Failed to create new shift.';
        this.snackBar.open(errMsg, 'Close', { duration: 5000 });
        this.loading = false;
      }
    });
  }
}
