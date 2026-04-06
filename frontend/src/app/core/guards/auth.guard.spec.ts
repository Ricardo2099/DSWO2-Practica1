import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { vi } from 'vitest';
import { authGuard } from './auth.guard';

describe('authGuard', () => {
  const parseUrlMock = vi.fn().mockReturnValue('redirect:/login');

  beforeEach(() => {
    localStorage.clear();
    parseUrlMock.mockClear();

    TestBed.configureTestingModule({
      providers: [{ provide: Router, useValue: { parseUrl: parseUrlMock } }]
    });
  });

  afterEach(() => {
    localStorage.clear();
  });

  it('debe permitir acceso cuando existe token', () => {
    localStorage.setItem('token', 'abc123');

    const result = TestBed.runInInjectionContext(() => authGuard({} as never, {} as never));

    expect(result).toBe(true);
  });

  it('debe redirigir a /login cuando no existe token', () => {
    const result = TestBed.runInInjectionContext(() => authGuard({} as never, {} as never));

    expect(parseUrlMock).toHaveBeenCalledWith('/login');
    expect(result).toBe('redirect:/login');
  });
});
