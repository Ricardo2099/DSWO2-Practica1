import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { environment } from '../../../environments/environment';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const token = localStorage.getItem('token');
  const hasAbsoluteUrl = /^https?:\/\//i.test(req.url);

  let request = req;

  if (!hasAbsoluteUrl) {
    const baseUrl = environment.apiBaseUrl?.trim() ?? '';
    const normalizedBaseUrl = baseUrl.endsWith('/') ? baseUrl.slice(0, -1) : baseUrl;
    const normalizedPath = req.url.startsWith('/') ? req.url : `/${req.url}`;

    request = request.clone({
      url: `${normalizedBaseUrl}${normalizedPath}`
    });
  }

  if (token) {
    request = request.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  return next(request).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        localStorage.removeItem('token');
        localStorage.removeItem('role');
        void router.navigate(['/login']);
      }
      return throwError(() => error);
    })
  );
};
