export interface EmpleadoListItem {
  clave: string;
  nombre: string;
  direccion: string;
  telefono: string;
  correo: string;
  departamentoClave: string;
}

export interface EmpleadoCreatePayload {
  nombre: string;
  direccion: string;
  telefono: string;
  correo: string;
  departamentoClave: string;
}
