import { Component, OnInit } from '@angular/core';
import { OfficeConfigService, OfficeConfig } from '../services/office-config.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-office-config',
  templateUrl: './office-config.component.html',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule]
})
export class OfficeConfigComponent implements OnInit {
  config: OfficeConfig = {
    headOfficeCode: '',
    officeName: '',
    timezone: 'Asia/Jakarta',
    startMonth: 'April',
    startYear: new Date().getFullYear(),
    endMonth: 'Desember'
  };

  constructor(private service: OfficeConfigService) {}

  ngOnInit() {
    // Get user data from token
    const token = this.service.get();
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

  save(): void {
    this.service.create(this.config).subscribe({
      next: res => alert('Config saved!'),
      error: err => console.error(err)
    });
  }
}
