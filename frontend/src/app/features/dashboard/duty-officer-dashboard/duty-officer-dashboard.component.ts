import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { DashboardService } from '../../../core/services/dashboard.service';
import { DutyOfficerService } from '../../../core/services/duty-officer.service';
import { DutyOfficerDashboardDto } from '../../../core/models/models';
import { forkJoin } from 'rxjs';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';

@Component({
  selector: 'app-duty-officer-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, MatCardModule, MatIconModule, MatButtonModule, MatProgressBarModule, MatSnackBarModule],
  templateUrl: './duty-officer-dashboard.component.html',
  styleUrls: ['./duty-officer-dashboard.component.css']
})
export class DutyOfficerDashboardComponent implements OnInit {
  private dashboardService = inject(DashboardService);
  private doService = inject(DutyOfficerService);
  private snackBar = inject(MatSnackBar);

  data: DutyOfficerDashboardDto | null = null;
  loading = true;
  downloading = false;
  error = '';

  ngOnInit() {
    this.dashboardService.getDutyOfficerDashboard().subscribe({
      next: (d) => {
        this.data = d;
        this.loading = false;
      },
      error: (err) => {
        console.error('Dashboard error', err);
        this.error = 'Failed to load dashboard data.';
        this.loading = false;
      }
    });
  }

  get assignedPct(): number {
    if (!this.data || !this.data.totalNurses) return 0;
    return Math.round((this.data.totalAssignedToday / this.data.totalNurses) * 100);
  }

  downloadFullReport() {
    this.downloading = true;
    const today = new Date().toISOString().split('T')[0];

    forkJoin({
      staffing: this.doService.getStaffingReport(today),
      requests: this.doService.getDepartmentShiftRequests(),
      nurses: this.doService.getDepartmentNurses()
    }).subscribe({
      next: (results) => {
        try {
          const doc = new jsPDF();
          const deptName = this.data?.departmentName || 'Department';
          let yPos = 20;

          // Header
          doc.setFontSize(22);
          doc.text(`Daily Report: ${deptName}`, 14, yPos);
          doc.setFontSize(11);
          doc.setTextColor(100);
          yPos += 8;
          doc.text(`Generated on: ${new Date().toLocaleString()}`, 14, yPos);
          yPos += 15;

          // 1. Staffing Report
          doc.setFontSize(16);
          doc.setTextColor(0);
          doc.text('Today\'s Staffing Roster', 14, yPos);
          yPos += 5;

          const staffingBody = results.staffing.assignments.map(a => [
            a.nurseFullName,
            a.shiftName,
            `${a.startTime.substring(0,5)} - ${a.endTime.substring(0,5)}`,
            a.isSwapped ? 'Swapped' : 'Regular'
          ]);

          autoTable(doc, {
            startY: yPos,
            head: [['Nurse', 'Shift', 'Time', 'Status']],
            body: staffingBody,
            theme: 'striped',
            headStyles: { fillColor: [103, 58, 183] } // Duty Officer Purple
          });

          yPos = (doc as any).lastAutoTable.finalY + 20;
          if (yPos > 250) { doc.addPage(); yPos = 20; }

          // 2. Swap Requests
          doc.setFontSize(16);
          doc.setTextColor(0);
          doc.text('Recent Swap Requests', 14, yPos);
          yPos += 5;

          const requestsBody = results.requests.map(r => [
            r.requesterNurseName,
            r.peerNurseName || 'N/A',
            `${r.requesterShiftName} -> ${r.peerShiftName || '?'}`,
            r.status.replace(/_/g, ' '),
            new Date(r.createdAt).toLocaleDateString()
          ]);

          autoTable(doc, {
            startY: yPos,
            head: [['Requester', 'Peer', 'Swap', 'Status', 'Date']],
            body: requestsBody,
            theme: 'striped',
            headStyles: { fillColor: [103, 58, 183] }
          });

          yPos = (doc as any).lastAutoTable.finalY + 20;
          if (yPos > 250) { doc.addPage(); yPos = 20; }

          // 3. Nurse Directory
          doc.setFontSize(16);
          doc.setTextColor(0);
          doc.text('Nurse Directory', 14, yPos);
          yPos += 5;

          const nursesBody = results.nurses.map(n => [
            n.employeeCode,
            n.fullName,
            n.nurseType === 'CONTRACTUAL' ? 'Contractual' : 'Permanent',
            n.contactPhone || 'N/A'
          ]);

          autoTable(doc, {
            startY: yPos,
            head: [['Employee ID', 'Name', 'Type', 'Phone']],
            body: nursesBody,
            theme: 'striped',
            headStyles: { fillColor: [103, 58, 183] }
          });

          // Save PDF
          doc.save(`Duty_Officer_Report_${today}.pdf`);
          this.snackBar.open('Report downloaded successfully!', 'Close', { duration: 3000 });
        } catch (e) {
          console.error('PDF Generation Error', e);
          this.snackBar.open('Error generating PDF report.', 'Close', { duration: 3000 });
        } finally {
          this.downloading = false;
        }
      },
      error: (err) => {
        console.error('Report fetch error', err);
        this.snackBar.open('Failed to fetch data for report.', 'Close', { duration: 3000 });
        this.downloading = false;
      }
    });
  }
}
