import { Material } from './material';

export interface DetalleOrden {
  idDetalle: number;
  material: Material;
  cantidad: number;
  precioUnitario: number;
  subtotal: number;
}