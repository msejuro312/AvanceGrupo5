import { Injectable } from '@angular/core';

import { Usuario } from '../models/usuario';

@Injectable({ providedIn: 'root' })
export class SesionService {
  private readonly CLAVE_SESION = 'usuarioSerfagab';
  private readonly CLAVE_CREDENCIALES = 'credencialesSerfagab';

  guardarUsuario(usuario: Usuario): void {
    sessionStorage.setItem(this.CLAVE_SESION, JSON.stringify(usuario));
  }

  obtenerUsuario(): Usuario | null {
    const json = sessionStorage.getItem(this.CLAVE_SESION);
    return json ? (JSON.parse(json) as Usuario) : null;
  }

  guardarCredenciales(login: string, clave: string): void {
    const credenciales = { login, clave };
    sessionStorage.setItem(this.CLAVE_CREDENCIALES, JSON.stringify(credenciales));
  }

  obtenerCredenciales(): { login: string; clave: string } | null {
    const json = sessionStorage.getItem(this.CLAVE_CREDENCIALES);
    return json ? (JSON.parse(json) as { login: string; clave: string }) : null;
  }

  esAdministrador(): boolean {
    const usuario = this.obtenerUsuario();
    return !!usuario && usuario.tipo?.descripcion?.toLowerCase() === 'administrador';
  }

  cerrarSesion(): void {
    sessionStorage.removeItem(this.CLAVE_SESION);
    sessionStorage.removeItem(this.CLAVE_CREDENCIALES);
  }
}
