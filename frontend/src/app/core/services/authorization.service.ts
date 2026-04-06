import { Injectable } from '@angular/core';

export type AppRole = 'ADMIN' | 'USER';

@Injectable({ providedIn: 'root' })
export class AuthorizationService {
  getRole(): AppRole | null {
    const role = localStorage.getItem('role');
    return role === 'ADMIN' || role === 'USER' ? role : null;
  }

  isAdmin(): boolean {
    return this.getRole() === 'ADMIN';
  }

  canCreateDelete(): boolean {
    return this.isAdmin();
  }

  isReadOnly(): boolean {
    return this.getRole() === 'USER';
  }
}
