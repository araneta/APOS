import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, catchError, throwError } from "rxjs";
import { Router } from '@angular/router';
import { tap } from 'rxjs/operators';
import { JwtHelperService } from '@auth0/angular-jwt';
import { environment } from '../../environments/environment';
import { ToastrService } from 'ngx-toastr';

interface SignUpResponse {
    status: string;
    message: string;
    data: {
        id: number;
        username: string;
        password: string;
        role: string;
        firstName: string | null;
        lastName: string | null;
    };
}

interface SignupRequest {
    username: string;
    password: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
    private tokenKey = 'access_token';
    //private apiUrl = environment.apiUrl + '/auth';
    private apiUrl =  '/auth';

    private token: string = "";
    private jwtHelper = new JwtHelperService();

    constructor(
        private http: HttpClient, 
        private router: Router,
        private toastr: ToastrService
    ) {}

    private handleError(error: any) {
        if (error.status === 0) {
            this.toastr.error('Server is offline or unreachable. Please try again later.', 'Connection Error');
        } else if (error.status === 401) {
            this.toastr.error('Invalid credentials', 'Authentication Error');
        } else {
            this.toastr.error(error.message || 'An error occurred', 'Error');
        }
        return throwError(() => error);
    }

    login(username: string, password: string): Observable<any> {
        return this.http.post<{ token: string }>(`${this.apiUrl}/login`, { username, password }).pipe(
            tap(response => {
                localStorage.setItem(this.tokenKey, response.token);
                this.toastr.success('Login successful!', 'Success');
            }),
            catchError(this.handleError.bind(this))
        );
    }

    logout() {
        localStorage.removeItem(this.tokenKey);
        this.router.navigate(['/login']);
        this.toastr.info('You have been logged out', 'Logout');
    }

    getToken(): string | null {
        return localStorage.getItem(this.tokenKey);
    }

    isAuthenticated(): boolean {
        const token = this.getToken();
        return token ? !this.jwtHelper.isTokenExpired(token) : false;
    }

    signup(email: string, password: string): Observable<SignUpResponse> {
        const authData: SignupRequest = { username: email, password: password };
        return this.http.post<SignUpResponse>(`${this.apiUrl}/signup`, authData).pipe(
            tap(response => {
                if (response.status === 'ok') {
                    this.toastr.success(response.message, 'Success');
                } else {
                    this.toastr.error('Failed to create account', 'Error');
                }
            }),
            catchError(this.handleError.bind(this))
        );
    }
}
