/**
 * Endpoint:
 * - GET /api/leyes/{id} -> Obtiene resumen ejecutivo e impacto social.
 */
export interface ContenidoLey {
  id: string;
  titulo: string;
  resumenEjecutivo: string;
  impactoSocial: string;
}