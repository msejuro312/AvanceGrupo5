import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Material } from '../models/material';

@Injectable({ providedIn: 'root' })
export class MaterialService {
  private apiUrl = 'http://localhost:8080/api/materiales';

  constructor(private http: HttpClient) {}

  listar(): Observable<Material[]> {
    return this.http.get<Material[]>(this.apiUrl);
  }

  buscar(texto: string): Observable<Material[]> {
    return this.http.get<Material[]>(`${this.apiUrl}/buscarPorNombre?texto=${texto}`);
  }

  crear(material: Material): Observable<Material> {
    return this.http.post<Material>(this.apiUrl, material);
  }

  actualizar(id: number, material: Material): Observable<Material> {
    return this.http.put<Material>(`${this.apiUrl}/${id}`, material);
  }

  eliminar(id: number): Observable<Material> {
    return this.http.delete<Material>(`${this.apiUrl}/${id}`);
  }
}