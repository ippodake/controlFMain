/**
 * Endpoint: GET /api/politicos
 * Query Params: ?pagina={number}&size={number}
 * 
 * Response Body:
 * {
 *   "id": string,
 *   "cartas": CartaPolitico[],
 *   "paginaActual": number,
 *   "totalPaginas": number
 * }
 */
export interface GrillaPoliticos {
  id: string;                  // Identificador de la grilla
  cartas: import('../Componente carta politico/type_carta_politico').CartaPolitico[];     // Lista de tarjetas (ver type_carta_politico.ts)
  paginaActual: number;        // Página actual
  totalPaginas: number;        // Total de páginas
}