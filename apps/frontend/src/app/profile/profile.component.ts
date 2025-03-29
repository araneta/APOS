import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { ToastrService } from 'ngx-toastr';

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
    lastName: new FormControl('')
  });

  constructor(
    private authService: AuthService,
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
  }

  onSubmit() {
    if (this.profileForm.valid) {
      // TODO: Implement profile update logic
      console.log('Profile update:', this.profileForm.value);
      this.toastr.success('Profile updated successfully');
    } else {
      this.toastr.error('Please fill in all required fields');
    }
  }
} 