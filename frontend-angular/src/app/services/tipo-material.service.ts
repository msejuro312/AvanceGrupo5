import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { TipoMaterial } from '../models/tipo-material';

export interface TipoMaterialFiltros {
  criterio?: string;
  texto?: string;
}

@Injectable({ providedIn: 'root' })
export class TipoMaterialService {
  private apiUrl = 'http://localhost:8080/api/tipos-material';

  constructor(private http: HttpClient) {}

  listar(): Observable<TipoMaterial[]> {
    return this.http.get<TipoMaterial[]>(this.apiUrl);
  }

  listarPaginado(page: number, size: number, filtros?: TipoMaterialFiltros): Observable<any> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (filtros) {
      if (filtros.criterio) { params = params.set('criterio', filtros.criterio); }
      if (filtros.texto) { params = params.set('texto', filtros.texto); }
    }
    return this.http.get<any>(`${this.apiUrl}/paginado`, { params });
  }

  obtener(id: number): Observable<TipoMaterial> {
    return this.http.get<TipoMaterial>(`${this.apiUrl}/${id}`);
  }

  buscarPorNombre(texto: string): Observable<TipoMaterial[]> {
    return this.http.get<TipoMaterial[]>(`${this.apiUrl}/buscarPorNombre?texto=${texto}`);
  }

  buscarPorDescripcion(texto: string): Observable<TipoMaterial[]> {
    return this.http.get<TipoMaterial[]>(`${this.apiUrl}/buscarPorDescripcion?texto=${texto}`);
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