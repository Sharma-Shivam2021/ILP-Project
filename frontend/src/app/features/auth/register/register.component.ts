import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { AuthService } from '../../../core/services/auth.service';
import { Role, NurseType, DepartmentResponse } from '../../../core/models/models';

const PASSWORD_PATTERN = '^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,}$';
const INDIAN_PHONE_PATTERN = '^[6-9]\\d{9}$';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    MatCardModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSelectModule,
    MatSnackBarModule
  ],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;
  loading = false;
  departments: DepartmentResponse[] = [];
  roles = [
    { value: Role.NURSE, label: 'Nurse' },
    { value: Role.NURSING_INCHARGE, label: 'Nursing In-charge' },
    { value: Role.DUTY_OFFICER, label: 'Duty Officer' }
  ];
  nurseTypes = [
    { value: 'PERMANENT', label: 'Permanent' },
    { value: 'CONTRACTUAL', label: 'Contractual' }
  ];

  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);

  ngOnInit() {
    this.initForm();
    this.loadDepartments();
  }

  initForm() {
    this.registerForm = this.fb.group({
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

    // Handle conditional validators when role changes
    this.registerForm.get('role')?.valueChanges.subscribe(role => {
      const deptControl = this.registerForm.get('departmentId');
      const typeControl = this.registerForm.get('nurseType');

      if (role === Role.ADMIN) {
        deptControl?.clearValidators();
        typeControl?.clearValidators();
      } else if (role === Role.NURSE) {
        deptControl?.setValidators([Validators.required]);
        typeControl?.setValidators([Validators.required]);
      } else {
        deptControl?.setValidators([Validators.required]);
        typeControl?.clearValidators();
      }

      deptControl?.updateValueAndValidity();
      typeControl?.updateValueAndValidity();
    });

    // Set initial validators for NURSE role
    this.registerForm.get('departmentId')?.setValidators([Validators.required]);
    this.registerForm.get('nurseType')?.setValidators([Validators.required]);
    this.registerForm.get('departmentId')?.updateValueAndValidity();
    this.registerForm.get('nurseType')?.updateValueAndValidity();
  }

  loadDepartments() {
    this.authService.getDepartments().subscribe({
      next: (data) => this.departments = data,
      error: (err) => {
        console.error('Failed to load departments', err);
        this.snackBar.open('Failed to load departments list. Please refresh.', 'Close', { duration: 4000 });
      }
    });
  }

  onSubmit() {
    if (this.registerForm.invalid) {
      return;
    }

    this.loading = true;
    const formValue = { ...this.registerForm.value };

    // Clean optional fields based on role
    if (formValue.role === Role.ADMIN) {
      delete formValue.departmentId;
      delete formValue.nurseType;
    } else if (formValue.role !== Role.NURSE) {
      delete formValue.nurseType;
    }

    this.authService.register(formValue).subscribe({
      next: (res) => {
        this.snackBar.open(res.message || 'Registration successful! Redirecting to login...', 'OK', { duration: 4000 });
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error('Registration error', err);
        const errMsg = err?.error?.message || err?.error || 'Registration failed. Please check inputs.';
        this.snackBar.open(errMsg, 'Close', { duration: 5000 });
        this.loading = false;
      }
    });
  }

  onPhoneInput(event: any) {
    const input = event.target as HTMLInputElement;
    const value = input.value.replace(/[^0-9]/g, '');
    this.registerForm.get('contactPhone')?.setValue(value, { emitEvent: false });
    input.value = value;
  }
}
