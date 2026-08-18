import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Login } from '../models/login';
import { Usuario } from '../models/usuario';

@Injectable({ providedIn: 'root' })
export class LoginService {
  private apiUrl = 'http://localhost:8080/api/auth/login';

  constructor(private http: HttpClient) {}

  login(login: string, clave: string): Observable<Usuario> {
    const body: Login = { login, clave };
    return this.http.post<Usuario>(this.apiUrl, body);
  }
}