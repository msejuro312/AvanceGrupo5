import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { OrdenCompra } from '../models/orden-compra';

export interface OrdenFiltros {
  estado?: string | null;
  idProveedor?: number | null;
  fechaDesde?: string | null;
  fechaHasta?: string | null;
}

@Injectable({ providedIn: 'root' })
export class OrdenService {
  private apiUrl = 'http://localhost:8080/api/ordenes-compra';

  constructor(private http: HttpClient) {}

  historialPaginado(idUsuario: number, page: number, size: number, filtros?: OrdenFiltros): Observable<any> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (filtros) {
      if (filtros.estado) { params = params.set('estado', filtros.estado); }
      if (filtros.idProveedor != null) { params = params.set('idProveedor', filtros.idProveedor); }
      if (filtros.fechaDesde) { params = params.set('fechaDesde', filtros.fechaDesde); }
      if (filtros.fechaHasta) { params = params.set('fechaHasta', filtros.fechaHasta); }
    }
    return this.http.get<any>(`${this.apiUrl}/usuario/${idUsuario}/paginado`, { params });
  }

  detalle(idOrden: number): Observable<OrdenCompra> {
    return this.http.get<OrdenCompra>(`${this.apiUrl}/${idOrden}`);
  }

  crearOrden(idUsuario: number, orden: any): Observable<OrdenCompra> {
    return this.http.post<OrdenCompra>(`${this.apiUrl}/${idUsuario}/crear`, orden);
  }

  actualizarOrden(idOrden: number, orden: any): Observable<OrdenCompra> {
    return this.http.put<OrdenCompra>(`${this.apiUrl}/${idOrden}`, orden);
  }

  eliminarOrden(idOrden: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${idOrden}`);
  }

  agregarDetalle(idOrden: number, detalle: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/${idOrden}/agregar-detalle`, detalle);
  }

  cambiarEstado(idOrden: number, estado: string): Observable<OrdenCompra> {
    return this.http.put<OrdenCompra>(`${this.apiUrl}/${idOrden}/estado?estado=${estado}`, null);
  }

  descargarPdf(idOrden: number): Observable<HttpResponse<Blob>> {
    return this.http.get(`${this.apiUrl}/${idOrden}/pdf`, {
      responseType: 'blob',
      observe: 'response'
    });
  }
}