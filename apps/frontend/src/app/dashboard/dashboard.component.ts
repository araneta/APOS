import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  username: string = '';

  constructor(public authService: AuthService) {}

  ngOnInit() {
    // Get username from JWT token if available
    const token = this.authService.getToken();
    if (token) {
      try {
        const tokenData = JSON.parse(atob(token.split('.')[1]));
        this.username = tokenData.username || 'User';
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