import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTableModule } from '@angular/material/table';
import { AdminService } from '../../../core/services/admin.service';
import { DepartmentResponse } from '../../../core/models/models';

@Component({
  selector: 'app-dept-management',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatFormFieldModule,
    MatSnackBarModule,
    MatTableModule
  ],
  templateUrl: './dept-management.component.html',
  styleUrls: ['./dept-management.component.css']
})
export class DeptManagementComponent implements OnInit {
  adminService = inject(AdminService);
  snackBar = inject(MatSnackBar);
  fb = inject(FormBuilder);

  departments: DepartmentResponse[] = [];
  filteredDepartments: DepartmentResponse[] = [];
  loading = false;
  searchQuery = '';
  displayedColumns = ['name', 'location', 'actions'];

  showForm = false;
  deptForm!: FormGroup;
  editingDeptId: number | null = null;

  ngOnInit() {
    this.loadDepartments();
    this.initForm();
  }

  initForm() {
    this.deptForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2)]],
      location: ['', [Validators.required, Validators.minLength(2)]]
    });
  }

  loadDepartments() {
    this.loading = true;
    this.adminService.getDepartments().subscribe({
      next: (data) => {
        this.departments = data;
        this.applyFilter();
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.snackBar.open('Failed to load departments list', 'Close', { duration: 3000 });
        this.loading = false;
      }
    });
  }

  applyFilter() {
    const q = this.searchQuery.trim().toLowerCase();
    this.filteredDepartments = this.departments.filter(d => 
      !q || d.name.toLowerCase().includes(q) || d.location.toLowerCase().includes(q)
    );
  }

  openCreateForm() {
    this.showForm = true;
    this.editingDeptId = null;
    this.deptForm.reset();
  }

  openEditForm(dept: DepartmentResponse) {
    this.showForm = true;
    this.editingDeptId = dept.id;
    this.deptForm.reset({
      name: dept.name,
      location: dept.location
    });
  }

  closeForm() {
    this.showForm = false;
    this.editingDeptId = null;
  }

  onSubmit() {
    if (this.deptForm.invalid) return;

    this.loading = true;
    const formValue = this.deptForm.value;

    if (this.editingDeptId === null) {
      // Create
      this.adminService.createDepartment(formValue).subscribe({
        next: () => {
          this.snackBar.open('Department created successfully!', 'OK', { duration: 3000 });
          this.closeForm();
          this.loadDepartments();
        },
        error: (err) => {
          console.error(err);
          this.snackBar.open(err.error?.message || 'Failed to create department', 'Close', { duration: 4000 });
          this.loading = false;
        }
      });
    } else {
      // Edit
      this.adminService.updateDepartment(this.editingDeptId, formValue).subscribe({
        next: () => {
          this.snackBar.open('Department updated successfully!', 'OK', { duration: 3000 });
          this.closeForm();
          this.loadDepartments();
        },
        error: (err) => {
          console.error(err);
          this.snackBar.open(err.error?.message || 'Failed to update department', 'Close', { duration: 4000 });
          this.loading = false;
        }
      });
    }
  }

  deleteDept(dept: DepartmentResponse) {
    if (!confirm(`Are you sure you want to delete department ${dept.name}?`)) {
      return;
    }

    this.loading = true;
    this.adminService.deleteDepartment(dept.id).subscribe({
      next: () => {
        this.snackBar.open('Department deleted successfully', 'OK', { duration: 3000 });
        this.loadDepartments();
      },
      error: (err) => {
        console.error(err);
        this.snackBar.open(err.error?.message || 'Failed to delete department. Verify no staff are assigned.', 'Close', { duration: 4000 });
        this.loading = false;
      }
    });
  }
}
