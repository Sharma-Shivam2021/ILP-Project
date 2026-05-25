import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTableModule } from '@angular/material/table';
import { AdminService } from '../../../core/services/admin.service';
import { AuditLog } from '../../../core/models/models';

@Component({
  selector: 'app-audit-logs',
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
    MatSnackBarModule,
    MatTableModule
  ],
  templateUrl: './audit-logs.component.html',
  styleUrls: ['./audit-logs.component.css']
})
export class AuditLogsComponent implements OnInit {
  adminService = inject(AdminService);
  snackBar = inject(MatSnackBar);

  logs: AuditLog[] = [];
  filteredLogs: AuditLog[] = [];
  loading = false;
  searchQuery = '';
  selectedActionFilter = 'ALL';

  displayedColumns = ['timestamp', 'action', 'actor', 'target', 'details'];
  actionTypes: string[] = [];

  ngOnInit() {
    this.loadLogs();
  }

  loadLogs() {
    this.loading = true;
    this.adminService.getAuditLogs().subscribe({
      next: (data) => {
        this.logs = data;
        this.extractActionTypes();
        this.applyFilter();
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.snackBar.open('Failed to load audit logs list', 'Dismiss', { duration: 3000 });
        this.loading = false;
      }
    });
  }

  extractActionTypes() {
    const types = this.logs.map(l => l.action);
    this.actionTypes = Array.from(new Set(types)).sort();
  }

  applyFilter() {
    const q = this.searchQuery.trim().toLowerCase();
    this.filteredLogs = this.logs.filter(l => {
      const matchAction = this.selectedActionFilter === 'ALL' || l.action === this.selectedActionFilter;
      
      const matchSearch = !q ||
        (l.performedBy && l.performedBy.toLowerCase().includes(q)) ||
        (l.targetUser && l.targetUser.toLowerCase().includes(q)) ||
        (l.details && l.details.toLowerCase().includes(q));

      return matchAction && matchSearch;
    });
  }

  onFilterChange() {
    this.applyFilter();
  }

  formatAction(action: string): string {
    if (!action) return '';
    return action.replace(/_/g, ' ');
  }
}
