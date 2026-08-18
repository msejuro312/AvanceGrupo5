export interface Tipo {
  idTipo: number;
  descripcion: string;
}

export interface Usuario {
  idUsuario: number;
  nombres: string;
  apellidos: string;
  login: string;
  clave: string;
  email: string;
  activo: boolean;
  tipo: Tipo;
}