import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { AuthService } from '../../../core/services/auth.service';

const PASSWORD_PATTERN = '^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,}$';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm: FormGroup;
  loading = false;
  error = '';
  
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  constructor() {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', [
        Validators.required, 
        Validators.pattern(PASSWORD_PATTERN)
      ]]
    });
    
    // Redirect if already logged in
    const user = this.authService.currentUserValue;
    if (user) {
      this.redirectBasedOnRole(user.role);
    }
  }

  onSubmit() {
    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;
    this.error = '';

    this.authService.login(this.loginForm.value).subscribe({
      next: (res) => {
        console.log('Login success, response:', res);
        this.redirectBasedOnRole(res.role);
      },
      error: (err) => {
        console.error('Login error:', err);
        this.error = err?.error?.message || 'Invalid credentials or server error';
        this.loading = false;
      }
    });
  }

  private redirectBasedOnRole(role: string) {
    switch (role) {
      case 'NURSE':
        this.router.navigate(['/nurse']);
        break;
      case 'NURSING_INCHARGE':
        this.router.navigate(['/incharge']);
        break;
      case 'DUTY_OFFICER':
        this.router.navigate(['/duty-officer']);
        break;
      case 'ADMIN':
        this.router.navigate(['/admin']);
        break;
      default:
        this.router.navigate(['/']);
    }
  }
}
