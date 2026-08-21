import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { MaterialesComponent } from './components/materiales/materiales.component';
import { ProveedoresComponent } from './components/proveedores/proveedores.component';
import { OrdenesComponent } from './components/ordenes/ordenes.component';
import { TiposMaterialComponent } from './components/tipos-material/tipos-material.component';
import { sesionGuard } from './services/sesion.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [sesionGuard] },
  { path: 'materiales', component: MaterialesComponent, canActivate: [sesionGuard] },
  { path: 'proveedores', component: ProveedoresComponent, canActivate: [sesionGuard] },
  { path: 'ordenes', component: OrdenesComponent, canActivate: [sesionGuard] },
  { path: 'tipos-material', component: TiposMaterialComponent, canActivate: [sesionGuard] },
  { path: '**', redirectTo: 'dashboard' }
];
