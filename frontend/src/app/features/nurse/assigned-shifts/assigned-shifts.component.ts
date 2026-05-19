import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { NurseService } from '../../../core/services/nurse.service';
import { NurseShiftResponseDto } from '../../../core/models/models';

@Component({
  selector: 'app-assigned-shifts',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatTableModule, MatButtonModule, MatIconModule],
  templateUrl: './assigned-shifts.component.html',
  styleUrls: ['./assigned-shifts.component.css']
})
export class AssignedShiftsComponent implements OnInit {
  private nurseService = inject(NurseService);
  
  upcomingShifts: NurseShiftResponseDto[] = [];
  displayedColumns: string[] = ['date', 'shift', 'department', 'swapAvailable'];

  ngOnInit() {
    this.nurseService.getMyShifts().subscribe({
      next: (shifts) => {
        const todayDate = new Date();
        const todayStr = todayDate.toISOString().split('T')[0];
        
        const nextWeekDate = new Date();
        nextWeekDate.setDate(todayDate.getDate() + 7);
        const nextWeekStr = nextWeekDate.toISOString().split('T')[0];

        // Filter for upcoming/today shifts and sort by date ascending (limited to 7 days)
        this.upcomingShifts = shifts
          .filter(s => s.shiftDate >= todayStr && s.shiftDate <= nextWeekStr)
          .sort((a, b) => a.shiftDate.localeCompare(b.shiftDate));
      },
      error: (err) => console.error('Failed to load shifts', err)
    });
  }
}
