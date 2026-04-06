import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { vi } from 'vitest';
import { EmpleadoFormComponent } from './empleado-form.component';
import { DepartamentosService } from '../../../departamentos/services/departamentos.service';
import { NotificationService } from '../../../../shared/services/notification.service';

describe('EmpleadoFormComponent', () => {
  const departamentosServiceMock = {
    getDepartamentos: vi.fn().mockReturnValue(
      of([{ clave: 'IT', nombre: 'Tecnologia' }])
    )
  };

  const notificationServiceMock = {
    success: vi.fn(),
    error: vi.fn()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmpleadoFormComponent],
      providers: [
        { provide: DepartamentosService, useValue: departamentosServiceMock },
        { provide: NotificationService, useValue: notificationServiceMock }
      ]
    }).compileComponents();
  });

  afterEach(() => {
    departamentosServiceMock.getDepartamentos.mockClear();
    notificationServiceMock.success.mockClear();
    notificationServiceMock.error.mockClear();
  });

  it('debe cargar departamentos en init', () => {
    const fixture = TestBed.createComponent(EmpleadoFormComponent);
    fixture.detectChanges();

    expect(departamentosServiceMock.getDepartamentos).toHaveBeenCalled();
    expect(fixture.componentInstance.departamentos.length).toBe(1);
  });

  it('debe validar campos obligatorios y correo', () => {
    const fixture = TestBed.createComponent(EmpleadoFormComponent);
    fixture.detectChanges();

    const form = fixture.componentInstance.form;
    form.patchValue({
      nombre: '',
      direccion: '',
      telefono: '',
      correo: 'correo-invalido',
      departamentoClave: ''
    });

    expect(form.valid).toBe(false);
  });

  it('debe emitir payload valido al enviar', () => {
    const fixture = TestBed.createComponent(EmpleadoFormComponent);
    fixture.detectChanges();

    const component = fixture.componentInstance;
    const spy = vi.fn();
    component.submitEmpleado.subscribe(spy);

    component.form.patchValue({
      nombre: 'Ana',
      direccion: 'Calle 1',
      telefono: '111',
      correo: 'ana@x.com',
      departamentoClave: 'IT'
    });

    component.onSubmit();

    expect(spy).toHaveBeenCalledWith({
      nombre: 'Ana',
      direccion: 'Calle 1',
      telefono: '111',
      correo: 'ana@x.com',
      departamentoClave: 'IT'
    });
  });
});
