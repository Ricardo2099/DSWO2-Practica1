import { Injectable, inject } from '@angular/core';
import { NotificationService } from '../../../shared/services/notification.service';

@Injectable({ providedIn: 'root' })
export class DepartamentosNotificationService {
  private readonly notification = inject(NotificationService);

  successCreate(): void {
    this.notification.success('Departamento creado correctamente.');
  }

  successUpdate(): void {
    this.notification.success('Departamento actualizado correctamente.');
  }

  successDelete(): void {
    this.notification.success('Departamento eliminado correctamente.');
  }

  error(message: string): void {
    this.notification.error(message);
  }

  infoConflict(message: string): void {
    this.notification.error(message);
  }
}
