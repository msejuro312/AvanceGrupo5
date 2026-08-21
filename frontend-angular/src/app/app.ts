import { Component } from '@angular/core';
import { NavigationEnd, Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

import { SesionService } from './services/sesion.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  esLogin = true;
  nombreUsuario = '';

  constructor(private router: Router, private sesionService: SesionService) {
    this.router.events.subscribe((evento) => {
      if (evento instanceof NavigationEnd) {
        this.esLogin = evento.urlAfterRedirects === '/login';
        const usuario = this.sesionService.obtenerUsuario();
        this.nombreUsuario = usuario ? `${usuario.nombres} ${usuario.apellidos}` : '';
      }
    });
  }

  cerrarMenu() {
    const chk = document.getElementById('btn-menu') as HTMLInputElement | null;
    if (chk) chk.checked = false;
  }

  cerrarSesion() {
    const Swal = (window as any).Swal;
    Swal.fire({
      title: '¿Deseas cerrar sesión?',
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Sí, cerrar sesión',
      cancelButtonText: 'Cancelar'
    }).then((resultado: any) => {
      if (resultado.isConfirmed) {
        this.cerrarMenu();
        this.sesionService.cerrarSesion();
        this.router.navigate(['/login']);
      }
    });
  }
}
