import { DepartamentoListItem } from './departamento.models';

export type DepartamentoFormMode = 'create' | 'edit';

export interface DepartamentoFormState {
  mode: DepartamentoFormMode;
  currentClave: string | null;
  isSubmitting: boolean;
  hasConflict: boolean;
  pendingRetry: boolean;
}

export interface DepartamentoListState {
  items: DepartamentoListItem[];
  pageIndex: number;
  pageSize: number;
  total: number;
  isLoading: boolean;
  errorMessage: string | null;
}
