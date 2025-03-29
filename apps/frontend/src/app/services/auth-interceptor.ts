import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler } from '@angular/common/http';
import { AuthService } from './auth.service';
import { environment } from '../../environments/environment';

@Injectable()
export class AuthInterceptorService implements HttpInterceptor {
  private apiUrl = environment.apiUrl;

  constructor(private authService: AuthService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    const token = this.authService.getToken();

    // Automatically attach API URL and JWT token
    const modifiedReq = req.clone({
      url: `${this.apiUrl}${req.url}`,
      setHeaders: token ? { Authorization: `Bearer ${token}` } : {},
    });

    return next.handle(modifiedReq);
  }
}
