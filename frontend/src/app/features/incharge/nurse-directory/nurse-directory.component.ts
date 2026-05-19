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
import { ProfileService } from '../../../core/services/profile.service';
import { NurseService } from '../../../core/services/nurse.service';
import { NurseResponseDto } from '../../../core/models/models';

@Component({
  selector: 'app-nurse-directory',
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
  templateUrl: './nurse-directory.component.html',
  styleUrls: ['./nurse-directory.component.css']
})
export class NurseDirectoryComponent implements OnInit {
  profileService = inject(ProfileService);
  nurseService = inject(NurseService);
  snackBar = inject(MatSnackBar);

  departmentId!: number;
  departmentName = '';
  
  allNurses: NurseResponseDto[] = [];
  filteredNurses: NurseResponseDto[] = [];
  
  loading = false;
  searchQuery = '';
  selectedType = 'ALL'; // 'ALL', 'PERMANENT', 'CONTRACTUAL'

  displayedColumns: string[] = ['code', 'name', 'type', 'phone', 'email', 'status'];

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
          this.loadNurses();
        } else {
          this.snackBar.open('Unable to resolve department profile.', 'Dismiss', { duration: 4000 });
          this.loading = false;
        }
      },
      error: (err) => {
        console.error('Error fetching profile', err);
        this.snackBar.open('Failed to resolve profile', 'Dismiss', { duration: 4000 });
        this.loading = false;
      }
    });
  }

  loadNurses() {
    this.loading = true;
    this.nurseService.getNursesByDepartment(this.departmentId).subscribe({
      next: (nurses) => {
        this.allNurses = nurses;
        this.applyFilters();
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading nurses', err);
        this.snackBar.open('Failed to load nurses directory', 'Dismiss', { duration: 3000 });
        this.loading = false;
      }
    });
  }

  applyFilters() {
    this.filteredNurses = this.allNurses.filter(nurse => {
      // 1. Filter by Search Query
      const query = this.searchQuery.trim().toLowerCase();
      const matchesSearch = !query || 
        nurse.fullName.toLowerCase().includes(query) ||
        nurse.employeeCode.toLowerCase().includes(query) ||
        nurse.contactEmail.toLowerCase().includes(query);

      // 2. Filter by Nurse Type
      const matchesType = this.selectedType === 'ALL' || 
        nurse.nurseType?.toUpperCase() === this.selectedType;

      return matchesSearch && matchesType;
    });
  }

  onFilterChange() {
    this.applyFilters();
  }

  getInitials(name: string): string {
    if (!name) return 'N';
    return name.slice(0, 2).toUpperCase();
  }
}
