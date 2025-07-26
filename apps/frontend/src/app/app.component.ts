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
  progress = 0;
  private progressInterval: any;

  constructor(public authService: AuthService, private loadingService: LoadingService) {
    this.loadingService.loading$.subscribe((loading) => {
      this.isLoading = loading;

      if (loading) {
        this.startProgress();
      } else {
        this.completeProgress();
      }
    });
  }

  setLoading(state: boolean) {
    this.isLoading = state;
  }
  startProgress(): void {
    this.progress = 0;

    this.progressInterval = setInterval(() => {
      if (this.progress < 95) {
        this.progress += 5;
      }
    }, 100);
  }

  completeProgress(): void {
    clearInterval(this.progressInterval);

    const completeInterval = setInterval(() => {
      if (this.progress < 100) {
        this.progress += 5;
      } else {
        clearInterval(completeInterval);
        // Hide bar after short delay
        setTimeout(() => {
          this.progress = 0;
        }, 300);
      }
    }, 30);
  }

}
