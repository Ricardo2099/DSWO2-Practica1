import { TestBed } from '@angular/core/testing';
import { HttpClient, provideHttpClient, withInterceptors } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { Router } from '@angular/router';
import { vi } from 'vitest';
import { authInterceptor } from './auth.interceptor';

describe('authInterceptor', () => {
  let http: HttpClient;
  let httpMock: HttpTestingController;
  const routerMock = { navigate: vi.fn().mockResolvedValue(true) };

  beforeEach(() => {
    localStorage.clear();

    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(withInterceptors([authInterceptor])),
        provideHttpClientTesting(),
        { provide: Router, useValue: routerMock }
      ]
    });

    http = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
    routerMock.navigate.mockClear();
    localStorage.clear();
  });

  it('debe adjuntar Authorization Bearer cuando hay token', () => {
    localStorage.setItem('token', 'abc123');

    http.get('/api/v1/empleados').subscribe();

    const req = httpMock.expectOne('/api/v1/empleados');
    expect(req.request.headers.get('Authorization')).toBe('Bearer abc123');
    req.flush({ content: [], totalElements: 0, totalPages: 0, number: 0, size: 10 });
  });

  it('no debe adjuntar Authorization cuando no hay token', () => {
    http.get('/api/v1/departamentos').subscribe();

    const req = httpMock.expectOne('/api/v1/departamentos');
    expect(req.request.headers.has('Authorization')).toBe(false);
    req.flush([]);
  });

  it('debe limpiar sesion y redirigir en 401 (token invalido)', () => {
    localStorage.setItem('token', 'abc123');
    localStorage.setItem('role', 'ADMIN');

    http.get('/api/v1/empleados').subscribe({
      next: () => {
        throw new Error('No debe completar con exito');
      },
      error: (error) => {
        expect(error.status).toBe(401);
      }
    });

    const req = httpMock.expectOne('/api/v1/empleados');
    req.flush({}, { status: 401, statusText: 'Unauthorized' });

    expect(localStorage.getItem('token')).toBeNull();
    expect(localStorage.getItem('role')).toBeNull();
    expect(routerMock.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('debe propagar 403 sin limpiar sesion', () => {
    localStorage.setItem('token', 'abc123');

    http.delete('/api/v1/empleados/EMP-1').subscribe({
      next: () => {
        throw new Error('No debe completar con exito');
      },
      error: (error) => {
        expect(error.status).toBe(403);
      }
    });

    const req = httpMock.expectOne('/api/v1/empleados/EMP-1');
    req.flush({}, { status: 403, statusText: 'Forbidden' });

    expect(localStorage.getItem('token')).toBe('abc123');
    expect(routerMock.navigate).not.toHaveBeenCalled();
  });
});
