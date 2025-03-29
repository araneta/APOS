import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Router } from '@angular/router';
import { tap } from 'rxjs/operators';
import { JwtHelperService } from '@auth0/angular-jwt';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthService {
    private tokenKey = 'access_token';
    private apiUrl = environment.apiUrl + '/auth';

    private token: string = "";
    private jwtHelper = new JwtHelperService();

    constructor(private http: HttpClient, private router: Router) {

    }

    login(username: string, password: string): Observable<any> {
        return this.http.post<{ token: string }>(`${this.apiUrl}/login`, { username, password }).pipe(
            tap(response => {
                localStorage.setItem(this.tokenKey, response.token);
            })
        );
    }

    logout() {
        localStorage.removeItem(this.tokenKey);
        this.router.navigate(['/login']);
    }

    getToken(): string | null {
        return localStorage.getItem(this.tokenKey);
    }

    isAuthenticated(): boolean {
        const token = this.getToken();
        return token ? !this.jwtHelper.isTokenExpired(token) : false;
    }
    signup(email: string, password: string): Observable<any> {
        console.log('auth signup');
        const authData = { username: email, password: password };
        return this.http.post<{ token: string }>(`${this.apiUrl}/auth/signup`, authData);
    }
}
