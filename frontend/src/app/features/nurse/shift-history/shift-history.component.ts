import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { NurseService } from '../../../core/services/nurse.service';
import { NurseShiftResponseDto } from '../../../core/models/models';

@Component({
  selector: 'app-shift-history',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatTableModule, MatIconModule],
  templateUrl: './shift-history.component.html',
  styleUrls: ['./shift-history.component.css']
})
export class ShiftHistoryComponent implements OnInit {
  private nurseService = inject(NurseService);
  
  pastShifts: NurseShiftResponseDto[] = [];
  displayedColumns: string[] = ['date', 'shift', 'time', 'status'];

  ngOnInit() {
    this.nurseService.getMyShifts().subscribe({
      next: (shifts) => {
        const today = new Date().toISOString().split('T')[0];
        // Filter for past shifts and sort by date descending (newest past shifts first)
        this.pastShifts = shifts
          .filter(s => s.shiftDate < today)
          .sort((a, b) => b.shiftDate.localeCompare(a.shiftDate));
      },
      error: (err) => console.error('Failed to load shifts', err)
    });
  }
}
