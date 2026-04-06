import { TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { vi } from 'vitest';
import { EmpleadosPageComponent } from './empleados-page.component';
import { EmpleadosService } from '../../services/empleados.service';
import { AuthorizationService } from '../../../../core/services/authorization.service';
import { NotificationService } from '../../../../shared/services/notification.service';
import { DepartamentosService } from '../../../departamentos/services/departamentos.service';

describe('EmpleadosPageComponent', () => {
  const empleadosServiceMock = {
    getEmpleados: vi.fn().mockReturnValue(
      of([
        {
          clave: 'EMP-1',
          nombre: 'Ana',
          direccion: 'Calle 1',
          telefono: '111',
          correo: 'ana@x.com',
          departamentoClave: 'IT'
        }
      ])
    ),
    createEmpleado: vi.fn().mockReturnValue(
      of({
        clave: 'EMP-2',
        nombre: 'Luis',
        direccion: 'Calle 2',
        telefono: '222',
        correo: 'luis@x.com',
        departamentoClave: 'HR'
      })
    ),
    deleteEmpleado: vi.fn().mockReturnValue(of(undefined))
  };

  const authorizationServiceMock = {
    canCreateDelete: vi.fn().mockReturnValue(true)
  };

  const notificationServiceMock = {
    success: vi.fn(),
    error: vi.fn()
  };

  const departamentosServiceMock = {
    getDepartamentos: vi.fn().mockReturnValue(of([]))
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmpleadosPageComponent],
      providers: [
        { provide: EmpleadosService, useValue: empleadosServiceMock },
        { provide: AuthorizationService, useValue: authorizationServiceMock },
        { provide: NotificationService, useValue: notificationServiceMock },
        { provide: DepartamentosService, useValue: departamentosServiceMock }
      ]
    }).compileComponents();
  });

  afterEach(() => {
    empleadosServiceMock.getEmpleados.mockClear();
    empleadosServiceMock.createEmpleado.mockClear();
    empleadosServiceMock.deleteEmpleado.mockClear();
    authorizationServiceMock.canCreateDelete.mockClear();
    notificationServiceMock.success.mockClear();
    notificationServiceMock.error.mockClear();
    departamentosServiceMock.getDepartamentos.mockClear();
  });

  it('debe mostrar columna de acciones para ADMIN', () => {
    authorizationServiceMock.canCreateDelete.mockReturnValue(true);
    const fixture = TestBed.createComponent(EmpleadosPageComponent);
    fixture.detectChanges();

    expect(fixture.componentInstance.displayedColumns).toContain('acciones');
  });

  it('no debe mostrar columna de acciones para USER', () => {
    authorizationServiceMock.canCreateDelete.mockReturnValue(false);
    const fixture = TestBed.createComponent(EmpleadosPageComponent);
    fixture.detectChanges();

    expect(fixture.componentInstance.displayedColumns).not.toContain('acciones');
  });

  it('debe notificar error cuando listado retorna 403', () => {
    empleadosServiceMock.getEmpleados.mockReturnValue(
      throwError(() => ({ status: 403 }))
    );

    const fixture = TestBed.createComponent(EmpleadosPageComponent);
    fixture.detectChanges();

    expect(notificationServiceMock.error).toHaveBeenCalled();
  });
});
