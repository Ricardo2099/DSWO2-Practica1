import { CommonModule } from '@angular/common';
import { Component, EventEmitter, OnInit, Output, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { finalize } from 'rxjs';
import { DepartamentoOption } from '../../../departamentos/models/departamento-option.models';
import { DepartamentosService } from '../../../departamentos/services/departamentos.service';
import { EmpleadoCreatePayload } from '../../models/empleado.models';
import { NotificationService } from '../../../../shared/services/notification.service';

@Component({
  selector: 'app-empleado-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule
  ],
  templateUrl: './empleado-form.component.html',
  styleUrl: './empleado-form.component.scss'
})
export class EmpleadoFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly departamentosService = inject(DepartamentosService);
  private readonly notificationService = inject(NotificationService);

  @Output() submitEmpleado = new EventEmitter<EmpleadoCreatePayload>();

  readonly form = this.fb.nonNullable.group({
    nombre: ['', [Validators.required]],
    direccion: ['', [Validators.required]],
    telefono: ['', [Validators.required]],
    correo: ['', [Validators.required, Validators.email]],
    departamentoClave: ['', [Validators.required]]
  });

  departamentos: DepartamentoOption[] = [];
  isLoadingDepartamentos = false;

  ngOnInit(): void {
    this.isLoadingDepartamentos = true;
    this.departamentosService
      .getDepartamentos()
      .pipe(finalize(() => (this.isLoadingDepartamentos = false)))
      .subscribe({
        next: (departamentos) => {
          this.departamentos = departamentos;
        },
        error: () => {
          this.notificationService.error('No se pudo cargar el catalogo de departamentos.');
        }
      });
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.submitEmpleado.emit(this.form.getRawValue());
  }
}
