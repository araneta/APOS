import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { OfficeConfig, OfficeConfigService } from '../services/office-config.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  username: string = '';
  private company: OfficeConfig | null = null;

  constructor(public authService: AuthService, private service: OfficeConfigService,private router: Router,private toastr: ToastrService) {}

  ngOnInit() {
    // Get username from JWT token if available
    const token = this.authService.getToken();
    if (token) {
      try {
        const tokenData = JSON.parse(atob(token.split('.')[1]));
        this.username = tokenData.username || 'User';
        this.service.get().subscribe((company) => { 
           if(company == null) {
            this.toastr.error('Company profile is empty. Please fill company data', 'Error');
            this.router.navigate(['/office-config']);
          }else{
            this.company = company;
            //this.toastr.success('Welcome to the dashboard, ' + this.username, 'Success');
          }
        });
        
       
      } catch (error) {
        console.error('Error parsing token:', error);
        this.username = 'User';
      }
    }
  }

  onLogout() {
    this.authService.logout();
  }
} 