import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { OrdenCompra } from '../models/orden-compra';

@Injectable({ providedIn: 'root' })
export class OrdenService {
  private apiUrl = 'http://localhost:8080/api/ordenes-compra';

  constructor(private http: HttpClient) {}

  historialPaginado(idUsuario: number, page: number, size: number): Observable<any> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<any>(`${this.apiUrl}/usuario/${idUsuario}/paginado`, { params });
  }

  detalle(idOrden: number): Observable<OrdenCompra> {
    return this.http.get<OrdenCompra>(`${this.apiUrl}/${idOrden}`);
  }

  crearOrden(idUsuario: number, orden: any): Observable<OrdenCompra> {
    return this.http.post<OrdenCompra>(`${this.apiUrl}/${idUsuario}/crear`, orden);
  }

  agregarDetalle(idOrden: number, detalle: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/${idOrden}/agregar-detalle`, detalle);
  }

  cambiarEstado(idOrden: number, estado: string): Observable<OrdenCompra> {
    return this.http.put<OrdenCompra>(`${this.apiUrl}/${idOrden}/estado?estado=${estado}`, null);
  }
}