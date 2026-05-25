import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { NurseService } from '../../../core/services/nurse.service';
import { ProfileService } from '../../../core/services/profile.service';
import { NurseResponseDto } from '../../../core/models/models';

@Component({
  selector: 'app-nurse-profile',
  standalone: true,
  imports: [
    CommonModule, 
    ReactiveFormsModule, 
    MatCardModule, 
    MatInputModule, 
    MatButtonModule, 
    MatIconModule,
    MatSnackBarModule
  ],
  templateUrl: './nurse-profile.component.html',
  styleUrls: ['./nurse-profile.component.css']
})
export class NurseProfileComponent implements OnInit {
  private fb = inject(FormBuilder);
  private nurseService = inject(NurseService);
  private profileService = inject(ProfileService);
  private snackBar = inject(MatSnackBar);

  profileData: NurseResponseDto | null = null;
  profileForm: FormGroup;
  loading = false;

  constructor() {
    const INDIAN_PHONE_PATTERN = '^[6-9]\\d{9}$';
    this.profileForm = this.fb.group({
      fullName: ['', Validators.required],
      contactPhone: ['', [Validators.required, Validators.pattern(INDIAN_PHONE_PATTERN)]],
      contactEmail: ['', [Validators.required, Validators.email]]
    });
  }

  ngOnInit() {
    this.loadProfile();
  }

  loadProfile() {
    this.nurseService.getMyProfile().subscribe({
      next: (profile) => {
        this.profileData = profile;
        this.profileForm.patchValue({
          fullName: profile.fullName,
          contactPhone: profile.contactPhone,
          contactEmail: profile.contactEmail
        });
      },
      error: (err) => {
        console.error('Failed to load profile', err);
        this.snackBar.open('Failed to load profile data.', 'Close', { duration: 3000 });
      }
    });
  }

  onSubmit() {
    if (this.profileForm.invalid) return;

    this.loading = true;
    this.profileService.updateProfile(this.profileForm.value).subscribe({
      next: () => {
        this.snackBar.open('Profile updated successfully!', 'Close', { duration: 3000 });
        this.loading = false;
        // Optionally update the local profileData object here
        if (this.profileData) {
          this.profileData.fullName = this.profileForm.value.fullName;
          this.profileData.contactPhone = this.profileForm.value.contactPhone;
          this.profileData.contactEmail = this.profileForm.value.contactEmail;
        }
      },
      error: (err) => {
        console.error('Failed to update profile', err);
        this.snackBar.open(err.error?.message || 'Failed to update profile.', 'Close', { duration: 3000 });
        this.loading = false;
      }
    });
  }

  onPhoneInput(event: any) {
    const input = event.target as HTMLInputElement;
    const value = input.value.replace(/[^0-9]/g, '');
    this.profileForm.get('contactPhone')?.setValue(value, { emitEvent: false });
    input.value = value;
  }
}
