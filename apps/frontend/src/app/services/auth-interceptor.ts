import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from './auth.service';
import { environment } from '../../environments/environment';

export const AuthInterceptorService: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();
  const apiUrl = environment.apiUrl;

  // Only prepend apiUrl if the request URL is not absolute
  const url = req.url.startsWith('http') ? req.url : `${apiUrl}${req.url}`;

  // Automatically attach API URL and JWT token
  const modifiedReq = req.clone({
    url,
    setHeaders: token ? { Authorization: `Bearer ${token}` } : {},
  });

  return next(modifiedReq);
};
