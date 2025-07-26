import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class LoadingService {
  private loadingSubject = new BehaviorSubject<boolean>(false);
  loading$ = this.loadingSubject.asObservable();

  show(): void {
    this.loadingSubject.next(true);
  }

  hide(): void {
    this.loadingSubject.next(false);
  }

  /**
   * Automatically hide after a delay (defaults to 2000ms)
   */
  hideAfter(delay: number = 2000): void {
    setTimeout(() => this.hide(), delay);
  }

  /**
   * Show and then auto-hide after a delay
   */
  showFor(delay: number = 2000): void {
    this.show();
    this.hideAfter(delay);
  }
}
