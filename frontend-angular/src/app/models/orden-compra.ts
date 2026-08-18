import { Proveedor } from './proveedor';
import { Usuario } from './usuario';
import { DetalleOrden } from './detalle-orden';

export interface OrdenCompra {
  idOrdenCompra: number;
  proveedor: Proveedor;
  usuario: Usuario;
  fecha: string;
  estado: string;
  total: number;
  observaciones: string;
  detalles: DetalleOrden[];
}