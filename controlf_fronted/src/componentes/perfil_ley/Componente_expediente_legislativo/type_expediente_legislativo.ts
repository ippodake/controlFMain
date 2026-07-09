/**
 * Endpoint:
 * - GET /api/leyes -> Obtiene listado general de leyes.
 */
export interface ExpedienteLegislativo {
  id: string;
  tipoExpediente: string;
  codigoExpediente: string;
  titulo: string;
  estaAprobado: boolean;
  categoria: string;
  proponente: string;
  tieneMenuOpciones: boolean;
}