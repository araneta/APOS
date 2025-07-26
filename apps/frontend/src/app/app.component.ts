import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from './services/auth.service';
import { LoadingService } from './loading.service';
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, ReactiveFormsModule, CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'my-angular-project';
  isLoading = true;

  constructor(public authService: AuthService, private loadingService: LoadingService) {
    this.loadingService.loading$.subscribe((state) => {
      console.log('Loading state changed:', state);
      this.isLoading = state;
    });
  }

  setLoading(state: boolean) {
    this.isLoading = state;
  }
}
