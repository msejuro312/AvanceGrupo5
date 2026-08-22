import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Proveedor } from '../models/proveedor';

export interface ProveedorFiltros {
  criterio?: string;
  texto?: string;
}

@Injectable({ providedIn: 'root' })
export class ProveedorService {
  private apiUrl = 'http://localhost:8080/api/proveedores';

  constructor(private http: HttpClient) {}

  listar(): Observable<Proveedor[]> {
    return this.http.get<Proveedor[]>(this.apiUrl);
  }

  listarPaginado(page: number, size: number, filtros?: ProveedorFiltros): Observable<any> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (filtros) {
      if (filtros.criterio) { params = params.set('criterio', filtros.criterio); }
      if (filtros.texto) { params = params.set('texto', filtros.texto); }
    }
    return this.http.get<any>(`${this.apiUrl}/paginado`, { params });
  }

  obtener(id: number): Observable<Proveedor> {
    return this.http.get<Proveedor>(`${this.apiUrl}/${id}`);
  }

  buscarPorRazonSocial(texto: string): Observable<Proveedor[]> {
    return this.http.get<Proveedor[]>(`${this.apiUrl}/buscarPorRazonSocial?texto=${texto}`);
  }

  buscarPorRuc(texto: string): Observable<Proveedor[]> {
    return this.http.get<Proveedor[]>(`${this.apiUrl}/buscarPorRuc?texto=${texto}`);
  }

  buscarPorEmail(texto: string): Observable<Proveedor[]> {
    return this.http.get<Proveedor[]>(`${this.apiUrl}/buscarPorEmail?texto=${texto}`);
  }

  crear(proveedor: Proveedor): Observable<Proveedor> {
    return this.http.post<Proveedor>(this.apiUrl, proveedor);
  }

  actualizar(id: number, proveedor: Proveedor): Observable<Proveedor> {
    return this.http.put<Proveedor>(`${this.apiUrl}/${id}`, proveedor);
  }

  eliminar(id: number): Observable<Proveedor> {
    return this.http.delete<Proveedor>(`${this.apiUrl}/${id}`);
  }
}