import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  DepartamentoCreatePayload,
  DepartamentoListItem,
  DepartamentoUpdatePayload
} from '../models/departamento.models';

@Injectable({ providedIn: 'root' })
export class DepartamentosService {
  private readonly http = inject(HttpClient);

  getDepartamentos(): Observable<DepartamentoListItem[]> {
    return this.http.get<DepartamentoListItem[]>('/api/v1/departamentos');
  }

  getDepartamento(clave: string): Observable<DepartamentoListItem> {
    return this.http.get<DepartamentoListItem>(`/api/v1/departamentos/${encodeURIComponent(clave)}`);
  }

  createDepartamento(payload: DepartamentoCreatePayload): Observable<DepartamentoListItem> {
    return this.http.post<DepartamentoListItem>('/api/v1/departamentos', payload);
  }

  updateDepartamento(clave: string, payload: DepartamentoUpdatePayload): Observable<DepartamentoListItem> {
    return this.http.put<DepartamentoListItem>(`/api/v1/departamentos/${encodeURIComponent(clave)}`, payload);
  }

  deleteDepartamento(clave: string): Observable<void> {
    return this.http.delete<void>(`/api/v1/departamentos/${encodeURIComponent(clave)}`);
  }
}
