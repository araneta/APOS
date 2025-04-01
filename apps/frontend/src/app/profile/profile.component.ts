import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { ToastrService } from 'ngx-toastr';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent implements OnInit {
  profileForm = new FormGroup({
    username: new FormControl(''),
    firstName: new FormControl(''),
    lastName: new FormControl(''),
    password: new FormControl('')
  });

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private toastr: ToastrService
  ) {}

  ngOnInit() {
    // Get user data from token
    const token = this.authService.getToken();
    if (token) {
      try {
        const tokenData = JSON.parse(atob(token.split('.')[1]));
        this.profileForm.patchValue({
          username: tokenData.username || ''
        });
      } catch (error) {
        console.error('Error parsing token:', error);
        this.toastr.error('Error loading profile data');
      }
    }

    this.userService.getProfile().subscribe((data) => {
      this.profileForm.patchValue(data);
    });
  }

  onSubmit() {
    if (this.profileForm.valid) {
      const formValue = this.profileForm.value;
      const profileData = {
        username: formValue.username || '',
        firstName: formValue.firstName || '',
        lastName: formValue.lastName || '',
        password: formValue.password || '',
      };
      
      this.userService.updateProfile(profileData).subscribe({
        next: (response) => {
          if (response.status === 'ok') {
            this.toastr.success('Account updated successfully!', 'Success');
          } else {
            this.toastr.error('Failed to update account', 'Error');
          }
        },
        error: (error: any) => {
          this.toastr.error(error.message || 'Failed to update account', 'Error');
        }
      });
    }
  }
} 