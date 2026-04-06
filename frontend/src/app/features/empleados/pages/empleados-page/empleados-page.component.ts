import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTableModule } from '@angular/material/table';
import { finalize } from 'rxjs';
import { AuthorizationService } from '../../../../core/services/authorization.service';
import { NotificationService } from '../../../../shared/services/notification.service';
import { EmpleadoCreatePayload, EmpleadoListItem } from '../../models/empleado.models';
import { EmpleadosService } from '../../services/empleados.service';
import { EmpleadoFormComponent } from '../../components/empleado-form/empleado-form.component';

@Component({
  selector: 'app-empleados-page',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    EmpleadoFormComponent
  ],
  templateUrl: './empleados-page.component.html',
  styleUrl: './empleados-page.component.scss'
})
export class EmpleadosPageComponent implements OnInit {
  private readonly empleadosService = inject(EmpleadosService);
  private readonly authz = inject(AuthorizationService);
  private readonly notificationService = inject(NotificationService);

  empleados: EmpleadoListItem[] = [];
  displayedColumns: string[] = ['clave', 'nombre', 'direccion', 'telefono', 'correo', 'departamentoClave'];
  isLoading = false;

  ngOnInit(): void {
    if (this.canManage) {
      this.displayedColumns = [...this.displayedColumns, 'acciones'];
    }
    this.loadEmpleados();
  }

  get canManage(): boolean {
    return this.authz.canCreateDelete();
  }

  onCreateEmpleado(payload: EmpleadoCreatePayload): void {
    this.empleadosService.createEmpleado(payload).subscribe({
      next: () => {
        this.notificationService.success('Empleado creado correctamente.');
        this.loadEmpleados();
      },
      error: () => {
        this.notificationService.error('No se pudo crear el empleado.');
      }
    });
  }

  deleteEmpleado(clave: string): void {
    this.empleadosService.deleteEmpleado(clave).subscribe({
      next: () => {
        this.notificationService.success('Empleado eliminado correctamente.');
        this.loadEmpleados();
      },
      error: () => {
        this.notificationService.error('No se pudo eliminar el empleado.');
      }
    });
  }

  private loadEmpleados(): void {
    this.isLoading = true;
    this.empleadosService
      .getEmpleados()
      .pipe(finalize(() => (this.isLoading = false)))
      .subscribe({
        next: (response) => {
          this.empleados = response;
        },
        error: () => {
          this.notificationService.error('No se pudo cargar el listado de empleados.');
          this.empleados = [];
        }
      });
  }
}
