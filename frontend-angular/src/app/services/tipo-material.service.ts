import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { TipoMaterial } from '../models/tipo-material';

@Injectable({ providedIn: 'root' })
export class TipoMaterialService {
  private apiUrl = 'http://localhost:8080/api/tipos-material';

  constructor(private http: HttpClient) {}

  listar(): Observable<TipoMaterial[]> {
    return this.http.get<TipoMaterial[]>(this.apiUrl);
  }

  crear(tipo: TipoMaterial): Observable<TipoMaterial> {
    return this.http.post<TipoMaterial>(this.apiUrl, tipo);
  }

  actualizar(id: number, tipo: TipoMaterial): Observable<TipoMaterial> {
    return this.http.put<TipoMaterial>(`${this.apiUrl}/${id}`, tipo);
  }

  eliminar(id: number): Observable<TipoMaterial> {
    return this.http.delete<TipoMaterial>(`${this.apiUrl}/${id}`);
  }
}