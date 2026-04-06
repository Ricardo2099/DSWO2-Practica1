export interface LoginRequest {
  correo: string;
  contrasena: string;
}

export interface LoginResponse {
  token: string;
  expiraEn: string;
}

export type UiRole = 'ADMIN' | 'USER';
