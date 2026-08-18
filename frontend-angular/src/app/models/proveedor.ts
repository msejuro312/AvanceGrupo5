export interface Proveedor {
  idProveedor: number | null;
  razonSocial: string;
  ruc: string;
  celular: string;
  email: string;
  descripcion: string;
  activo: boolean;
}