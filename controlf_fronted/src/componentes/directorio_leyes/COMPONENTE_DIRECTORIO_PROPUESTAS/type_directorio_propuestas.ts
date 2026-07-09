/**
 * Endpoint: GET /api/leyes
 * Query Params: ?termino={string}&categoria={string}&estado={string}&anio={number}
 * 
 * Response: ExpedienteLegislativoDTO[]
 */
export interface DirectorioPropuestas {
  id: string;
  terminoBusqueda: string;
  categoriaId: string | null;
  estadoId: string | null;
  anioLegislativo: number | null;
}