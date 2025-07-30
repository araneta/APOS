import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError, map } from 'rxjs';
import { environment } from '../../environments/environment';
import { ToastrService } from 'ngx-toastr';
import { tap } from 'rxjs/operators';
import { ApiResponse } from './api-response.model';

export interface UserProfile {
  username: string;
  firstName: string;
  lastName: string;
  password?: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = '/users';

  constructor(
    private http: HttpClient,
    private toastr: ToastrService
  ) {}

  private handleError(error: any) {
    if (error.status === 0) {
      this.toastr.error('Server is offline or unreachable. Please try again later.', 'Connection Error');
    } else if (error.status === 401) {
      this.toastr.error('Your session has expired. Please login again.', 'Session Expired');
    } else if (error.status === 403) {
      this.toastr.error('You do not have permission to perform this action.', 'Access Denied');
    } else {
      this.toastr.error(error.message || 'An error occurred while processing your request.', 'Error');
    }
    return throwError(() => error);
  }

  updateProfile(profile: UserProfile): Observable<ApiResponse<UserProfile>> {
    return this.http.put<ApiResponse<UserProfile>>(`${this.apiUrl}/profile`, profile).pipe(
      tap((response) => {
        if (response.status === 'ok') {
          //this.toastr.success(response.message, 'Success');
        }
      }),
      catchError(this.handleError.bind(this))
    );
  }

  getProfile(): Observable<UserProfile> {
    return this.http.get<{ status: string; message: string; data: UserProfile }>(`${this.apiUrl}/profile`).pipe(
      tap(response => {
        console.log('response ',response);
        if (response.status === 'ok') {
          this.toastr.success(response.message, 'Success');
        }
      }),
      map(response => response.data),
      catchError(this.handleError.bind(this))
    );
  }
} 