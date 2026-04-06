export interface DepartamentoListItem {
  clave: string;
  nombre: string;
}

export interface DepartamentoCreatePayload {
  clave: string;
  nombre: string;
}

export interface DepartamentoUpdatePayload {
  nombre: string;
}
