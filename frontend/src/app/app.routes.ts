import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { LoginPageComponent } from './features/auth/pages/login-page/login-page.component';
import { EmpleadosPageComponent } from './features/empleados/pages/empleados-page/empleados-page.component';

export const routes: Routes = [
	{ path: 'login', component: LoginPageComponent },
	{ path: 'empleados', canActivate: [authGuard], component: EmpleadosPageComponent },
	{
		path: 'departamentos',
		canActivate: [authGuard],
		loadChildren: () =>
			import('./features/departamentos/departamentos.routes').then((m) => m.DEPARTAMENTOS_ROUTES)
	},
	{ path: '', pathMatch: 'full', redirectTo: 'login' },
	{ path: '**', redirectTo: 'login' }
];
