import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Material } from '../models/material';

export interface MaterialFiltros {
  criterio?: string;
  texto?: string;
  idTipo?: number | null;
}

@Injectable({ providedIn: 'root' })
export class MaterialService {
  private apiUrl = 'http://localhost:8080/api/materiales';

  constructor(private http: HttpClient) {}

  listar(): Observable<Material[]> {
    return this.http.get<Material[]>(this.apiUrl);
  }

  listarPaginado(page: number, size: number, filtros?: MaterialFiltros): Observable<any> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (filtros) {
      if (filtros.criterio) { params = params.set('criterio', filtros.criterio); }
      if (filtros.texto) { params = params.set('texto', filtros.texto); }
      if (filtros.idTipo != null) { params = params.set('idTipo', filtros.idTipo); }
    }
    return this.http.get<any>(`${this.apiUrl}/paginado`, { params });
  }

  obtener(id: number): Observable<Material> {
    return this.http.get<Material>(`${this.apiUrl}/${id}`);
  }

  buscar(texto: string): Observable<Material[]> {
    return this.http.get<Material[]>(`${this.apiUrl}/buscarPorNombre?texto=${texto}`);
  }

  buscarPorTipo(idTipo: number): Observable<Material[]> {
    return this.http.get<Material[]>(`${this.apiUrl}/buscarPorTipo?idTipo=${idTipo}`);
  }

  buscarPorDescripcion(texto: string): Observable<Material[]> {
    return this.http.get<Material[]>(`${this.apiUrl}/buscarPorDescripcion?texto=${texto}`);
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