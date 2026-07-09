export interface FiltroBusqueda {
  id: string;                       // Identificador único del filtro
  label: string;                    // Texto del dropdown
  valorSeleccionado: string | null; // Valor actualmente seleccionado
}