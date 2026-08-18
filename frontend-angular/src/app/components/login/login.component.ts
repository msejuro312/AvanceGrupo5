import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { LoginService } from '../../services/login.service';

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
  error = false;

  constructor(private loginService: LoginService, private router: Router) {}

  iniciarSesion() {
    this.loginService.login(this.login, this.clave).subscribe({
      next: () => {
        this.error = false;
        this.router.navigate(['/materiales']);
      },
      error: (err) => {
        console.error('Error al iniciar sesión', err);
        this.error = true;
      }
    });
  }
}