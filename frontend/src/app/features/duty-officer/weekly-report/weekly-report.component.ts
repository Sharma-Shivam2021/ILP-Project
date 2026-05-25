import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatTableModule } from '@angular/material/table';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { DutyOfficerService } from '../../../core/services/duty-officer.service';
import { WeeklyReportDto, NurseWeeklyReportDto } from '../../../core/models/models';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';

@Component({
  selector: 'app-weekly-report',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatIconModule,
    MatButtonModule,
    MatInputModule,
    MatFormFieldModule,
    MatSelectModule,
    MatTableModule,
    MatProgressBarModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './weekly-report.component.html',
  styleUrls: ['./weekly-report.component.css']
})
export class WeeklyReportComponent implements OnInit {
  private doService = inject(DutyOfficerService);
  private snackBar = inject(MatSnackBar);

  reportData: WeeklyReportDto | null = null;
  filteredNurses: NurseWeeklyReportDto[] = [];
  loading = true;
  downloading = false;
  searchQuery = '';
  selectedTypeFilter = 'ALL';
  currentStartDate = ''; // YYYY-MM-DD
  
  displayedColumns = [
    'nurseInfo',
    'mon',
    'tue',
    'wed',
    'thu',
    'fri',
    'sat',
    'sun',
    'totalShifts',
    'swaps'
  ];

  ngOnInit() {
    this.setToCurrentWeekMonday();
    this.loadWeeklyReport();
  }

  setToCurrentWeekMonday() {
    const today = new Date();
    const day = today.getDay();
    const diff = today.getDate() - day + (day === 0 ? -6 : 1); // Adjust when Sunday to Monday
    const monday = new Date(today.setDate(diff));
    this.currentStartDate = monday.toISOString().split('T')[0];
  }

  loadWeeklyReport() {
    this.loading = true;
    this.doService.getWeeklyReport(this.currentStartDate).subscribe({
      next: (data) => {
        this.reportData = data;
        this.applyFilters();
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading weekly report', err);
        this.snackBar.open('Failed to load weekly report.', 'Close', { duration: 3000 });
        this.loading = false;
      }
    });
  }

  applyFilters() {
    if (!this.reportData) return;

    const query = this.searchQuery.toLowerCase().trim();
    this.filteredNurses = this.reportData.nurses.filter(nurse => {
      // 1. Search Query filter (matches Name, Code, Email, Phone)
      const matchesSearch = !query || 
        nurse.fullName.toLowerCase().includes(query) ||
        nurse.employeeCode.toLowerCase().includes(query) ||
        (nurse.contactEmail && nurse.contactEmail.toLowerCase().includes(query)) ||
        (nurse.contactPhone && nurse.contactPhone.toLowerCase().includes(query));

      // 2. Nurse Type filter
      const matchesType = this.selectedTypeFilter === 'ALL' ||
        nurse.nurseType === this.selectedTypeFilter;

      return matchesSearch && matchesType;
    });
  }

  onFilterChange() {
    this.applyFilters();
  }

  previousWeek() {
    const d = new Date(this.currentStartDate);
    d.setDate(d.getDate() - 7);
    this.currentStartDate = d.toISOString().split('T')[0];
    this.loadWeeklyReport();
  }

  nextWeek() {
    const d = new Date(this.currentStartDate);
    d.setDate(d.getDate() + 7);
    this.currentStartDate = d.toISOString().split('T')[0];
    this.loadWeeklyReport();
  }

  goToCurrentWeek() {
    this.setToCurrentWeekMonday();
    this.loadWeeklyReport();
  }

  getWeekDisplayRange(): string {
    if (!this.reportData) return '';
    const start = new Date(this.reportData.startDate).toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' });
    const end = new Date(this.reportData.endDate).toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' });
    return `${start} — ${end}`;
  }

  get totalNursesCount(): number {
    return this.reportData ? this.reportData.nurses.length : 0;
  }

  get totalScheduledShifts(): number {
    if (!this.reportData) return 0;
    return this.reportData.nurses.reduce((acc, nurse) => acc + nurse.totalShifts, 0);
  }

  get totalApprovedSwaps(): number {
    if (!this.reportData) return 0;
    return this.reportData.nurses.reduce((acc, nurse) => acc + nurse.swapsApprovedCount, 0);
  }

  get totalPendingSwaps(): number {
    if (!this.reportData) return 0;
    return this.reportData.nurses.reduce((acc, nurse) => acc + nurse.swapsPendingCount, 0);
  }

  // Get letter representation of shift name
  getShiftLetter(shiftName: string): string {
    if (!shiftName || shiftName === 'OFF') return 'OFF';
    return shiftName.charAt(0).toUpperCase();
  }

  exportPDF() {
    if (!this.reportData) return;
    this.downloading = true;

    try {
      const doc = new jsPDF('l', 'mm', 'a4'); // landscape A4
      const deptName = this.reportData.departmentName;
      const weekRange = this.getWeekDisplayRange();

      // Header
      doc.setFontSize(20);
      doc.text(`Weekly Nurse Shift Report: ${deptName}`, 14, 18);
      doc.setFontSize(10);
      doc.setTextColor(100);
      doc.text(`Report Period: ${weekRange} | Generated: ${new Date().toLocaleString()}`, 14, 24);

      // Stat Highlights
      doc.setFontSize(11);
      doc.setTextColor(50);
      doc.text(`Total Nurses: ${this.totalNursesCount}  |  Scheduled Shifts: ${this.totalScheduledShifts}  |  Approved Swaps: ${this.totalApprovedSwaps}  |  Pending Swaps: ${this.totalPendingSwaps}`, 14, 32);

      // Prep Table Headers & Rows
      const headers = ['Nurse (Code)', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun', 'Total', 'Swaps (Appr/Pend)'];
      const body = this.filteredNurses.map(nurse => {
        const row = [
          `${nurse.fullName} (${nurse.employeeCode})`
        ];

        // 7 days
        for (let i = 0; i < 7; i++) {
          const assignment = nurse.dailyAssignments[i];
          row.push(assignment ? assignment.shiftName : 'OFF');
        }

        row.push(nurse.totalShifts.toString());
        row.push(`${nurse.swapsApprovedCount} / ${nurse.swapsPendingCount}`);
        return row;
      });

      autoTable(doc, {
        startY: 37,
        head: [headers],
        body: body,
        theme: 'grid',
        headStyles: { fillColor: [103, 58, 183], textColor: [255, 255, 255] }, // Purple matching Duty Officer role
        styles: { fontSize: 9, cellPadding: 3 },
        columnStyles: {
          0: { cellWidth: 50, fontStyle: 'bold' } // Nurse info wider
        }
      });

      doc.save(`Weekly_Report_${deptName.replace(/\s+/g, '_')}_${this.currentStartDate}.pdf`);
      this.snackBar.open('PDF Report exported successfully!', 'Close', { duration: 3000 });
    } catch (error) {
      console.error('PDF generation error', error);
      this.snackBar.open('Error generating PDF report.', 'Close', { duration: 3000 });
    } finally {
      this.downloading = false;
    }
  }
}
