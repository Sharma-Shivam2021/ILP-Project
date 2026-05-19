import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTableModule } from '@angular/material/table';
import { DutyOfficerService } from '../../../core/services/duty-officer.service';
import { NurseResponseDto } from '../../../core/models/models';

@Component({
  selector: 'app-do-nurses',
  standalone: true,
  imports: [
    CommonModule, FormsModule,
    MatCardModule, MatIconModule, MatInputModule,
    MatFormFieldModule, MatSelectModule, MatSnackBarModule, MatTableModule
  ],
  templateUrl: './do-nurses.component.html',
  styleUrls: ['./do-nurses.component.css']
})
export class DoNursesComponent implements OnInit {
  private doService = inject(DutyOfficerService);
  private snackBar = inject(MatSnackBar);

  allNurses: NurseResponseDto[] = [];
  filteredNurses: NurseResponseDto[] = [];
  loading = false;
  searchQuery = '';
  selectedType = 'ALL';
  displayedColumns = ['code', 'name', 'type', 'phone', 'email', 'status'];

  ngOnInit() { this.loadNurses(); }

  loadNurses() {
    this.loading = true;
    this.doService.getDepartmentNurses().subscribe({
      next: (list) => {
        this.allNurses = list;
        this.applyFilters();
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.snackBar.open('Failed to load nurses directory', 'Dismiss', { duration: 3000 });
        this.loading = false;
      }
    });
  }

  applyFilters() {
    const q = this.searchQuery.trim().toLowerCase();
    this.filteredNurses = this.allNurses.filter(n => {
      const matchSearch = !q ||
        n.fullName.toLowerCase().includes(q) ||
        n.employeeCode.toLowerCase().includes(q) ||
        n.contactEmail?.toLowerCase().includes(q);
      const matchType = this.selectedType === 'ALL' || n.nurseType?.toUpperCase() === this.selectedType;
      return matchSearch && matchType;
    });
  }

  onFilterChange() { this.applyFilters(); }

  getInitials(name: string): string {
    if (!name) return 'N';
    return name.slice(0, 2).toUpperCase();
  }
}
