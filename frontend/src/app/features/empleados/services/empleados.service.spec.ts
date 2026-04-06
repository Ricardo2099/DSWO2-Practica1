import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { EmpleadosService } from './empleados.service';

describe('EmpleadosService', () => {
  let service: EmpleadosService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting(), EmpleadosService]
    });

    service = TestBed.inject(EmpleadosService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('debe listar empleados', () => {
    service.getEmpleados().subscribe((response) => {
      expect(response.length).toBe(1);
      expect(response[0].clave).toBe('EMP-1');
    });

    const req = httpMock.expectOne((r) => r.url === '/api/v1/empleados');
    expect(req.request.method).toBe('GET');

    req.flush([
      {
        clave: 'EMP-1',
        nombre: 'Ana',
        direccion: 'Calle 1',
        telefono: '111',
        correo: 'ana@x.com',
        departamentoClave: 'IT'
      }
    ]);
  });

  it('debe crear empleado', () => {
    const payload = {
      nombre: 'Ana',
      direccion: 'Calle 1',
      telefono: '111',
      correo: 'ana@x.com',
      departamentoClave: 'IT'
    };

    service.createEmpleado(payload).subscribe((response) => {
      expect(response.clave).toBe('EMP-1');
    });

    const req = httpMock.expectOne('/api/v1/empleados');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(payload);
    req.flush({ clave: 'EMP-1', ...payload });
  });

  it('debe eliminar empleado', () => {
    service.deleteEmpleado('EMP-1').subscribe((response) => {
      expect(response).toBeNull();
    });

    const req = httpMock.expectOne('/api/v1/empleados/EMP-1');
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });

  it('debe propagar errores de API', () => {
    service.getEmpleados().subscribe({
      next: () => {
        throw new Error('No debe completar con exito');
      },
      error: (error) => {
        expect(error.status).toBe(500);
      }
    });

    const req = httpMock.expectOne((r) => r.url === '/api/v1/empleados');
    req.flush({ message: 'error' }, { status: 500, statusText: 'Server Error' });
  });
});
