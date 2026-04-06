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
    request = request.clone({
      url: `${environment.apiBaseUrl}${req.url}`
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
