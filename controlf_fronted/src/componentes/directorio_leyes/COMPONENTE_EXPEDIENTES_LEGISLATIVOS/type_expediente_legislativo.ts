/**
 * Endpoint:
 * - GET /api/leyes -> Obtiene lista de leyes para el directorio.
 */
export interface ExpedienteLegislativo {
  id: string; // Código de la ley
  tituloLey: string;
  categoria: string;
  estado: 'APROBADA' | 'EN DEBATE' | 'VETADA';
  accionUrl: string; // URL o referencia de la acción "Ver"
}