import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { MaterialesComponent } from './components/materiales/materiales.component';
import { ProveedoresComponent } from './components/proveedores/proveedores.component';
import { OrdenesComponent } from './components/ordenes/ordenes.component';
import { TiposMaterialComponent } from './components/tipos-material/tipos-material.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'materiales', component: MaterialesComponent },
  { path: 'proveedores', component: ProveedoresComponent },
  { path: 'ordenes', component: OrdenesComponent },
  { path: 'tipos-material', component: TiposMaterialComponent },
  { path: '**', redirectTo: 'login' }
];
