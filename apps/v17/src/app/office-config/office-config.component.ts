import { Component, OnInit } from '@angular/core';
import { OfficeConfigService, OfficeConfig } from '../services/office-config.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../services/auth.service';

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
    startMonth: 1,
    startYear: new Date().getFullYear(),
    endMonth: 12
  };

  constructor(
    private authService: AuthService,
    private service: OfficeConfigService,
    private toastr: ToastrService
  ) {}

  ngOnInit() {
    const token = this.authService.getToken();
    if (token) {
      try {
        const tokenData = JSON.parse(atob(token.split('.')[1]));
        const username = tokenData.username || '';
        console.log('username',username);
        this.service.get().subscribe((data) => {
          if(data==null){
            this.config = {
              headOfficeCode: '',
              officeName: '',
              timezone: 'Asia/Jakarta',
              startMonth: 1,
              startYear: new Date().getFullYear(),
              endMonth: 12
            };          
          } else {
            this.config = data;
          }
        });
      } catch (error) {
        console.error('Error parsing token:', error);
        this.toastr.error('Error loading profile data');
      }
    }

  }

  save(): void {
    console.log('config',this.config);
    this.service.save(this.config).subscribe({
      next: res => {
        alert('Config saved!, Logging out...');
        this.authService.logout();
      },
      error: err => console.error(err)
    });
  }
}
