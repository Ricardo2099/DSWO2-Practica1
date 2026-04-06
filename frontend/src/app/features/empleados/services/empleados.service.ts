import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EmpleadoCreatePayload, EmpleadoListItem } from '../models/empleado.models';

@Injectable({ providedIn: 'root' })
export class EmpleadosService {
  private readonly http = inject(HttpClient);

  getEmpleados(): Observable<EmpleadoListItem[]> {
    return this.http.get<EmpleadoListItem[]>('/api/v1/empleados');
  }

  createEmpleado(payload: EmpleadoCreatePayload): Observable<EmpleadoListItem> {
    return this.http.post<EmpleadoListItem>('/api/v1/empleados', payload);
  }

  deleteEmpleado(clave: string): Observable<void> {
    return this.http.delete<void>(`/api/v1/empleados/${encodeURIComponent(clave)}`);
  }
}
