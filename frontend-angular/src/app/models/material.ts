import { TipoMaterial } from './tipo-material';

export interface Material {
  idMaterial: number;
  tipoMaterial: TipoMaterial | null;
  nombre: string;
  unidadMedida: string;
  stockActual: number;
  precioReferencial: number;
  descripcion: string;
  activo: boolean;
  version: number;
}