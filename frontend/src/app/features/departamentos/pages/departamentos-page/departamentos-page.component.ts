import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTableModule } from '@angular/material/table';
import { finalize } from 'rxjs';
import { AuthorizationService } from '../../../../core/services/authorization.service';
import {
  DepartamentoCreatePayload,
  DepartamentoListItem,
  DepartamentoUpdatePayload
} from '../../models/departamento.models';
import { DepartamentoFormMode } from '../../models/departamento-state.models';
import { DepartamentosErrorMapper } from '../../services/departamentos-error.mapper';
import { DepartamentosNotificationService } from '../../services/departamentos-notification.service';
import { DepartamentosService } from '../../services/departamentos.service';
import { DepartamentoFormComponent, DepartamentoFormValue } from '../../components/departamento-form/departamento-form.component';

@Component({
  selector: 'app-departamentos-page',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatPaginatorModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatCardModule,
    DepartamentoFormComponent
  ],
  templateUrl: './departamentos-page.component.html',
  styleUrl: './departamentos-page.component.scss'
})
export class DepartamentosPageComponent implements OnInit {
  private readonly departamentosService = inject(DepartamentosService);
  private readonly authz = inject(AuthorizationService);
  private readonly notifications = inject(DepartamentosNotificationService);
  private readonly errors = inject(DepartamentosErrorMapper);

  departamentos: DepartamentoListItem[] = [];
  visibleDepartamentos: DepartamentoListItem[] = [];
  selectedDepartamento: DepartamentoListItem | null = null;
  displayedColumns: string[] = ['clave', 'nombre', 'detalle'];

  formMode: DepartamentoFormMode = 'create';
  formValue: DepartamentoFormValue | null = { clave: '', nombre: '' };
  pendingRetryName: string | null = null;

  pageIndex = 0;
  pageSize = 10;
  totalElements = 0;

  isLoading = false;
  isSubmitting = false;

  ngOnInit(): void {
    if (this.canManage) {
      this.displayedColumns = [...this.displayedColumns, 'acciones'];
    }

    this.loadDepartamentos();
  }

  get canManage(): boolean {
    return this.authz.canCreateDelete();
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.updateVisibleDepartamentos();
  }

  onViewDetail(clave: string): void {
    this.departamentosService.getDepartamento(clave).subscribe({
      next: (departamento) => {
        this.selectedDepartamento = departamento;
      },
      error: (error) => {
        this.notifications.error(this.errors.map(error, 'load-detail'));
      }
    });
  }

  onEdit(row: DepartamentoListItem): void {
    if (!this.canManage) {
      return;
    }

    this.departamentosService.getDepartamento(row.clave).subscribe({
      next: (departamento) => {
        this.formMode = 'edit';
        this.formValue = {
          clave: departamento.clave,
          nombre: departamento.nombre
        };
      },
      error: (error) => {
        this.notifications.error(this.errors.map(error, 'load-detail'));
      }
    });
  }

  onCancelEdit(): void {
    this.formMode = 'create';
    this.formValue = { clave: '', nombre: '' };
    this.pendingRetryName = null;
  }

  onSubmitDepartamento(value: DepartamentoFormValue): void {
    if (this.formMode === 'create') {
      const payload: DepartamentoCreatePayload = {
        clave: value.clave,
        nombre: value.nombre
      };
      this.createDepartamento(payload);
      return;
    }

    const clave = this.formValue?.clave ?? value.clave;
    this.updateDepartamento(clave, { nombre: value.nombre });
  }

  onDelete(row: DepartamentoListItem): void {
    if (!this.canManage) {
      return;
    }

    const confirmed = window.confirm(`Eliminar departamento ${row.clave}?`);
    if (!confirmed) {
      return;
    }

    this.isSubmitting = true;
    this.departamentosService
      .deleteDepartamento(row.clave)
      .pipe(finalize(() => (this.isSubmitting = false)))
      .subscribe({
        next: () => {
          this.notifications.successDelete();
          this.loadDepartamentos();
        },
        error: (error) => {
          this.notifications.error(this.errors.map(error, 'delete'));
        }
      });
  }

  private loadDepartamentos(): void {
    this.isLoading = true;
    this.departamentosService
      .getDepartamentos()
      .pipe(finalize(() => (this.isLoading = false)))
      .subscribe({
        next: (departamentos) => {
          this.departamentos = departamentos;
          this.totalElements = departamentos.length;

          const maxPage = Math.max(Math.ceil(this.totalElements / this.pageSize) - 1, 0);
          if (this.pageIndex > maxPage) {
            this.pageIndex = maxPage;
          }

          this.updateVisibleDepartamentos();
        },
        error: (error) => {
          this.notifications.error(this.errors.map(error, 'load-list'));
          this.departamentos = [];
          this.visibleDepartamentos = [];
          this.totalElements = 0;
        }
      });
  }

  private updateVisibleDepartamentos(): void {
    const start = this.pageIndex * this.pageSize;
    const end = start + this.pageSize;
    this.visibleDepartamentos = this.departamentos.slice(start, end);
  }

  private createDepartamento(payload: DepartamentoCreatePayload): void {
    this.isSubmitting = true;
    this.departamentosService
      .createDepartamento(payload)
      .pipe(finalize(() => (this.isSubmitting = false)))
      .subscribe({
        next: () => {
          this.notifications.successCreate();
          this.onCancelEdit();
          this.pageIndex = 0;
          this.loadDepartamentos();
        },
        error: (error) => {
          this.notifications.error(this.errors.map(error, 'create'));
        }
      });
  }

  private updateDepartamento(clave: string, payload: DepartamentoUpdatePayload): void {
    this.isSubmitting = true;
    this.departamentosService
      .updateDepartamento(clave, payload)
      .pipe(finalize(() => (this.isSubmitting = false)))
      .subscribe({
        next: (departamento) => {
          this.notifications.successUpdate();
          this.selectedDepartamento = departamento;
          this.onCancelEdit();
          this.loadDepartamentos();
        },
        error: (error) => {
          if (this.errors.isConflict(error)) {
            this.resolveConflict(clave, payload.nombre);
            return;
          }

          this.notifications.error(this.errors.map(error, 'update'));
        }
      });
  }

  private resolveConflict(clave: string, attemptedNombre: string): void {
    this.departamentosService.getDepartamento(clave).subscribe({
      next: (departamento) => {
        this.formMode = 'edit';
        this.formValue = { clave: departamento.clave, nombre: departamento.nombre };
        this.pendingRetryName = attemptedNombre;
        this.notifications.infoConflict(this.errors.map({ status: 409 }, 'conflict'));

        const retry = window.confirm(
          'Se recargaron los datos vigentes por conflicto de concurrencia. Deseas reintentar con tu cambio de nombre?'
        );

        if (retry && this.pendingRetryName) {
          const retryName = this.pendingRetryName;
          this.pendingRetryName = null;
          this.updateDepartamento(clave, { nombre: retryName });
        }
      },
      error: (loadError) => {
        this.notifications.error(this.errors.map(loadError, 'load-detail'));
      }
    });
  }
}
