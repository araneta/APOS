import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { ToastrService } from 'ngx-toastr';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.scss'
})
export class SignupComponent implements OnInit {
  signupForm = new FormGroup({
    username : new FormControl(),
    password : new FormControl()
  })

  constructor(
    public authService: AuthService,
    private toastr: ToastrService,
    private router: Router,
  ){}

  ngOnInit(): void {
    if (this.authService.isAuthenticated()) {
      this.router.navigate(['/dashboard']);
    }
  }

  onSubmit(){
    console.log("submit", this.signupForm.value);
    
    if(this.signupForm.invalid){
      console.log("submit invalid", this.signupForm.value);
      this.toastr.error('Please fill in all required fields', 'Invalid Form');
      return;
    }
    
    // Use non-null assertion (!) or default values to ensure types are `string`.
    const username = this.signupForm.value.username || '';
    const password = this.signupForm.value.password || '';
    
    this.authService.signup(username, password).subscribe({
      next: (response) => {
        if (response.status === 'ok') {
          this.toastr.success('Account created successfully!', 'Success');
        } else {
          this.toastr.error('Failed to create account', 'Error');
        }
      },
      error: (error: any) => {
        this.toastr.error(error.message || 'Failed to create account', 'Error');
      }
    });
  }
}
