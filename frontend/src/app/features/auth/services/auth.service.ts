import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginRequest, LoginResponse, UiRole } from '../models/auth.models';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);

  login(payload: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>('/auth/login', payload);
  }

  logout(): Observable<void> {
    return this.http.post<void>('/auth/logout', {});
  }

  storeSession(token: string, role: UiRole, expiraEn: string): void {
    localStorage.setItem('token', token);
    localStorage.setItem('role', role);
    localStorage.setItem('sessionExpiresAt', expiraEn);
  }

  clearSession(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('sessionExpiresAt');
  }

  hasToken(): boolean {
    return Boolean(localStorage.getItem('token'));
  }
}
