import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTableModule } from '@angular/material/table';
import { AdminService } from '../../../core/services/admin.service';
import { DepartmentResponse, AdminUserResponseDto, Role } from '../../../core/models/models';

const PASSWORD_PATTERN = '^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,}$';
const INDIAN_PHONE_PATTERN = '^[6-9]\\d{9}$';

@Component({
  selector: 'app-user-management',
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
    MatSelectModule,
    MatSnackBarModule,
    MatTableModule
  ],
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.css']
})
export class UserManagementComponent implements OnInit {
  adminService = inject(AdminService);
  snackBar = inject(MatSnackBar);
  fb = inject(FormBuilder);

  users: AdminUserResponseDto[] = [];
  filteredUsers: AdminUserResponseDto[] = [];
  departments: DepartmentResponse[] = [];

  loading = false;
  searchQuery = '';
  selectedRoleFilter = 'ALL';

  displayedColumns: string[] = ['username', 'fullName', 'role', 'code', 'department', 'phone', 'email', 'actions'];

  // Modal / Form toggle states
  showCreateForm = false;
  showEditForm = false;
  showResetForm = false;

  userForm!: FormGroup;
  resetForm!: FormGroup;
  editingUserId: number | null = null;
  resettingUserId: number | null = null;
  resettingUsername = '';

  roles = [
    { value: Role.NURSE, label: 'Nurse' },
    { value: Role.NURSING_INCHARGE, label: 'Nursing In-charge' },
    { value: Role.DUTY_OFFICER, label: 'Duty Officer' },
    { value: Role.ADMIN, label: 'Admin' }
  ];

  nurseTypes = [
    { value: 'PERMANENT', label: 'Permanent' },
    { value: 'CONTRACTUAL', label: 'Contractual' }
  ];

  ngOnInit() {
    this.loadUsers();
    this.loadDepartments();
    this.initForms();
  }

  initForms() {
    this.userForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.pattern(PASSWORD_PATTERN)]],
      fullName: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      contactPhone: ['', [Validators.required, Validators.pattern(INDIAN_PHONE_PATTERN)]],
      role: [Role.NURSE, Validators.required],
      employeeCode: ['', [Validators.required, Validators.minLength(3)]],
      departmentId: [null],
      nurseType: ['PERMANENT']
    });

    this.resetForm = this.fb.group({
      password: ['', [Validators.required, Validators.pattern(PASSWORD_PATTERN)]]
    });

    // Reactive role selection checks
    this.userForm.get('role')?.valueChanges.subscribe(role => {
      const deptCtrl = this.userForm.get('departmentId');
      const typeCtrl = this.userForm.get('nurseType');

      if (role === Role.ADMIN) {
        deptCtrl?.clearValidators();
        typeCtrl?.clearValidators();
      } else if (role === Role.NURSE) {
        deptCtrl?.setValidators([Validators.required]);
        typeCtrl?.setValidators([Validators.required]);
      } else {
        deptCtrl?.setValidators([Validators.required]);
        typeCtrl?.clearValidators();
      }

      deptCtrl?.updateValueAndValidity();
      typeCtrl?.updateValueAndValidity();
    });
  }

  loadUsers() {
    this.loading = true;
    this.adminService.getAllUsers().subscribe({
      next: (data) => {
        this.users = data;
        this.applyFilters();
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.snackBar.open('Failed to load users list.', 'Close', { duration: 3000 });
        this.loading = false;
      }
    });
  }

  loadDepartments() {
    this.adminService.getDepartments().subscribe({
      next: (data) => this.departments = data,
      error: (err) => console.error(err)
    });
  }

  applyFilters() {
    const q = this.searchQuery.trim().toLowerCase();
    this.filteredUsers = this.users.filter(u => {
      const matchRole = this.selectedRoleFilter === 'ALL' || u.role === this.selectedRoleFilter;
      
      const matchSearch = !q ||
        (u.username && u.username.toLowerCase().includes(q)) ||
        (u.fullName && u.fullName.toLowerCase().includes(q)) ||
        (u.employeeCode && u.employeeCode.toLowerCase().includes(q)) ||
        (u.email && u.email.toLowerCase().includes(q)) ||
        (u.contactPhone && u.contactPhone.toLowerCase().includes(q)) ||
        (u.departmentName && u.departmentName.toLowerCase().includes(q)) ||
        (u.role && u.role.toLowerCase().includes(q)) ||
        (u.nurseType && u.nurseType.toLowerCase().includes(q));

      return matchRole && matchSearch;
    });
  }

  onFilterChange() {
    this.applyFilters();
  }

  openCreateForm() {
    this.showCreateForm = true;
    this.showEditForm = false;
    this.showResetForm = false;
    this.editingUserId = null;
    this.userForm.reset({ role: Role.NURSE, nurseType: 'PERMANENT' });
    this.userForm.get('password')?.setValidators([Validators.required, Validators.pattern(PASSWORD_PATTERN)]);
    this.userForm.get('password')?.updateValueAndValidity();
  }

  openEditForm(user: AdminUserResponseDto) {
    this.showCreateForm = false;
    this.showEditForm = true;
    this.showResetForm = false;
    this.editingUserId = user.userId;

    this.userForm.reset({
      username: user.username,
      password: '', // optional on edit
      fullName: user.fullName || '',
      email: user.email || '',
      contactPhone: user.contactPhone || '',
      role: user.role,
      employeeCode: user.employeeCode || '',
      departmentId: user.departmentId || null,
      nurseType: user.nurseType || 'PERMANENT'
    });

    // Password is not required on edit
    this.userForm.get('password')?.clearValidators();
    this.userForm.get('password')?.setValidators([Validators.pattern(PASSWORD_PATTERN)]);
    this.userForm.get('password')?.updateValueAndValidity();
  }

  openResetForm(user: AdminUserResponseDto) {
    this.showCreateForm = false;
    this.showEditForm = false;
    this.showResetForm = true;
    this.resettingUserId = user.userId;
    this.resettingUsername = user.username;
    this.resetForm.reset();
  }

  closeForms() {
    this.showCreateForm = false;
    this.showEditForm = false;
    this.showResetForm = false;
    this.editingUserId = null;
    this.resettingUserId = null;
  }

  onSubmitUser() {
    if (this.userForm.invalid) return;

    this.loading = true;
    const formValue = { ...this.userForm.value };
    if (formValue.role === Role.ADMIN) {
      delete formValue.departmentId;
      delete formValue.nurseType;
    } else if (formValue.role !== Role.NURSE) {
      delete formValue.nurseType;
    }

    if (this.showCreateForm) {
      this.adminService.createUser(formValue).subscribe({
        next: (res) => {
          this.snackBar.open(res.message || 'User created successfully!', 'OK', { duration: 3000 });
          this.closeForms();
          this.loadUsers();
        },
        error: (err) => {
          console.error(err);
          this.snackBar.open(err.error?.message || 'Failed to create user', 'Close', { duration: 4000 });
          this.loading = false;
        }
      });
    } else if (this.showEditForm && this.editingUserId) {
      this.adminService.updateUser(this.editingUserId, formValue).subscribe({
        next: (res) => {
          this.snackBar.open(res.message || 'User updated successfully!', 'OK', { duration: 3000 });
          this.closeForms();
          this.loadUsers();
        },
        error: (err) => {
          console.error(err);
          this.snackBar.open(err.error?.message || 'Failed to update user', 'Close', { duration: 4000 });
          this.loading = false;
        }
      });
    }
  }

  onSubmitReset() {
    if (this.resetForm.invalid || !this.resettingUserId) return;

    this.loading = true;
    const { password } = this.resetForm.value;

    this.adminService.resetPassword(this.resettingUserId, password).subscribe({
      next: (res) => {
        this.snackBar.open(res.message || 'Password reset successful!', 'OK', { duration: 3000 });
        this.closeForms();
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.snackBar.open(err.error?.message || 'Failed to reset password', 'Close', { duration: 4000 });
        this.loading = false;
      }
    });
  }

  deleteUser(user: AdminUserResponseDto) {
    if (!confirm(`Are you sure you want to permanently delete user ${user.username}?`)) {
      return;
    }

    this.loading = true;
    this.adminService.deleteUser(user.userId).subscribe({
      next: (res) => {
        this.snackBar.open(res.message || 'User deleted successfully', 'OK', { duration: 3000 });
        this.loadUsers();
      },
      error: (err) => {
        console.error(err);
        this.snackBar.open(err.error?.message || 'Failed to delete user', 'Close', { duration: 4000 });
        this.loading = false;
      }
    });
  }

  onPhoneInput(event: any) {
    const input = event.target as HTMLInputElement;
    const value = input.value.replace(/[^0-9]/g, '');
    this.userForm.get('contactPhone')?.setValue(value, { emitEvent: false });
    input.value = value;
  }
}
