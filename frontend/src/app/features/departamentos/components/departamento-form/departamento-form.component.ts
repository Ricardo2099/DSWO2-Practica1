import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { DepartamentoFormMode } from '../../models/departamento-state.models';

export interface DepartamentoFormValue {
  clave: string;
  nombre: string;
}

@Component({
  selector: 'app-departamento-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  templateUrl: './departamento-form.component.html',
  styleUrl: './departamento-form.component.scss'
})
export class DepartamentoFormComponent implements OnChanges {
  private readonly fb = inject(FormBuilder);

  @Input() mode: DepartamentoFormMode = 'create';
  @Input() value: DepartamentoFormValue | null = null;
  @Input() isSubmitting = false;

  @Output() submitDepartamento = new EventEmitter<DepartamentoFormValue>();
  @Output() cancelEdit = new EventEmitter<void>();

  readonly form = this.fb.nonNullable.group({
    clave: ['', [Validators.required, Validators.maxLength(50)]],
    nombre: ['', [Validators.required, Validators.maxLength(50)]]
  });

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['value']) {
      const payload = this.value ?? { clave: '', nombre: '' };
      this.form.patchValue(payload, { emitEvent: false });
    }

    if (changes['mode']) {
      if (this.mode === 'edit') {
        this.form.controls.clave.disable({ emitEvent: false });
      } else {
        this.form.controls.clave.enable({ emitEvent: false });
      }
    }
  }

  get submitLabel(): string {
    return this.mode === 'edit' ? 'Guardar cambios' : 'Crear departamento';
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.submitDepartamento.emit(this.form.getRawValue());
  }

  onCancel(): void {
    this.cancelEdit.emit();
  }
}
