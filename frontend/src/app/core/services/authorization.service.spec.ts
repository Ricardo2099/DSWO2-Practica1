import { TestBed } from '@angular/core/testing';
import { AuthorizationService } from './authorization.service';

describe('AuthorizationService', () => {
  let service: AuthorizationService;

  beforeEach(() => {
    localStorage.clear();
    TestBed.configureTestingModule({ providers: [AuthorizationService] });
    service = TestBed.inject(AuthorizationService);
  });

  afterEach(() => {
    localStorage.clear();
  });

  it('debe reconocer rol ADMIN', () => {
    localStorage.setItem('role', 'ADMIN');
    expect(service.isAdmin()).toBe(true);
    expect(service.canCreateDelete()).toBe(true);
    expect(service.isReadOnly()).toBe(false);
  });

  it('debe reconocer rol USER de solo lectura', () => {
    localStorage.setItem('role', 'USER');
    expect(service.isAdmin()).toBe(false);
    expect(service.canCreateDelete()).toBe(false);
    expect(service.isReadOnly()).toBe(true);
  });

  it('debe devolver null con rol invalido', () => {
    localStorage.setItem('role', 'OTHER');
    expect(service.getRole()).toBeNull();
  });
});
