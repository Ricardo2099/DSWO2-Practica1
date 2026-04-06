import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { DepartamentosService } from './departamentos.service';

describe('DepartamentosService', () => {
  let service: DepartamentosService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting(), DepartamentosService]
    });

    service = TestBed.inject(DepartamentosService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('debe listar departamentos', () => {
    service.getDepartamentos().subscribe((response) => {
      expect(response.length).toBe(1);
      expect(response[0].clave).toBe('IT');
    });

    const req = httpMock.expectOne('/api/v1/departamentos');
    expect(req.request.method).toBe('GET');
    req.flush([{ clave: 'IT', nombre: 'Tecnologia' }]);
  });

  it('debe obtener detalle de departamento por clave', () => {
    service.getDepartamento('IT').subscribe((response) => {
      expect(response.nombre).toBe('Tecnologia');
    });

    const req = httpMock.expectOne('/api/v1/departamentos/IT');
    expect(req.request.method).toBe('GET');
    req.flush({ clave: 'IT', nombre: 'Tecnologia' });
  });

  it('debe crear departamento', () => {
    const payload = { clave: 'RH', nombre: 'Recursos Humanos' };

    service.createDepartamento(payload).subscribe((response) => {
      expect(response.clave).toBe('RH');
    });

    const req = httpMock.expectOne('/api/v1/departamentos');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(payload);
    req.flush(payload);
  });

  it('debe actualizar departamento', () => {
    const payload = { nombre: 'Recursos Humanos y Cultura' };

    service.updateDepartamento('RH', payload).subscribe((response) => {
      expect(response.nombre).toBe(payload.nombre);
    });

    const req = httpMock.expectOne('/api/v1/departamentos/RH');
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(payload);
    req.flush({ clave: 'RH', nombre: payload.nombre });
  });

  it('debe eliminar departamento', () => {
    service.deleteDepartamento('RH').subscribe((response) => {
      expect(response).toBeNull();
    });

    const req = httpMock.expectOne('/api/v1/departamentos/RH');
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });

  it('debe propagar errores de API', () => {
    service.getDepartamentos().subscribe({
      next: () => {
        throw new Error('No debe completar con exito');
      },
      error: (error) => {
        expect(error.status).toBe(500);
      }
    });

    const req = httpMock.expectOne('/api/v1/departamentos');
    req.flush({ message: 'error' }, { status: 500, statusText: 'Server Error' });
  });
});
