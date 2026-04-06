import { HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';

export type DepartamentoErrorContext =
  | 'load-list'
  | 'load-detail'
  | 'create'
  | 'update'
  | 'delete'
  | 'conflict';

@Injectable({ providedIn: 'root' })
export class DepartamentosErrorMapper {
  map(error: unknown, context: DepartamentoErrorContext): string {
    const status = this.getStatus(error);

    if (status === 401) {
      return 'Tu sesion no es valida. Inicia sesion nuevamente.';
    }

    if (status === 403) {
      return 'No tienes permisos para realizar esta accion.';
    }

    if (context === 'delete' && status === 409) {
      return 'No se puede eliminar el departamento porque tiene empleados asociados.';
    }

    if (context === 'conflict' && status === 409) {
      return 'El departamento fue actualizado por otro usuario. Se recargaron los datos vigentes.';
    }

    switch (context) {
      case 'load-list':
        return 'No se pudo cargar el listado de departamentos.';
      case 'load-detail':
        return 'No se pudo cargar el detalle del departamento.';
      case 'create':
        return 'No se pudo crear el departamento.';
      case 'update':
        return 'No se pudo actualizar el departamento.';
      case 'delete':
        return 'No se pudo eliminar el departamento.';
      case 'conflict':
        return 'Se detecto un conflicto al actualizar el departamento.';
      default:
        return 'Ocurrio un error inesperado.';
    }
  }

  isConflict(error: unknown): boolean {
    return this.getStatus(error) === 409;
  }

  private getStatus(error: unknown): number | null {
    if (error instanceof HttpErrorResponse) {
      return error.status;
    }

    if (typeof error === 'object' && error !== null && 'status' in error) {
      const maybeStatus = (error as { status?: unknown }).status;
      return typeof maybeStatus === 'number' ? maybeStatus : null;
    }

    return null;
  }
}
