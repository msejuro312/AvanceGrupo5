import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { LoginService } from '../../services/login.service';
import { SesionService } from '../../services/sesion.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  login = '';
  clave = '';
  cargando = false;
  mensajeError = '';

  constructor(
    private loginService: LoginService,
    private sesionService: SesionService,
    private router: Router
  ) {}

  iniciarSesion() {
    if (this.cargando) return;

    if (!this.login.trim() || !this.clave.trim()) {
      this.mensajeError = 'Ingresa tu login y tu clave.';
      return;
    }

    this.cargando = true;
    this.mensajeError = '';

    this.loginService.login(this.login, this.clave).subscribe({
      next: (usuario) => {
        this.sesionService.guardarUsuario(usuario);
        this.sesionService.guardarCredenciales(this.login, this.clave);
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        console.error('Error al iniciar sesión', err);
        this.cargando = false;
        this.mensajeError = err.status === 401
          ? 'Credenciales inválidas. Verifica tu login y clave.'
          : 'No se pudo conectar con el servidor. Verifica que el backend esté iniciado.';
      }
    });
  }
}
