export interface Paginacion {
  id: string;            // Identificador único del componente
  paginaActual: number;  // Página actual
  totalPaginas: number;  // Total de páginas
  hayAnterior: boolean;  // Si el botón "Anterior" está habilitado
  haySiguiente: boolean; // Si el botón "Siguiente" está habilitado
}