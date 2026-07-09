/**
 * Endpoint: GET /api/leyes/{id}/votacion
 * 
 * Response Body:
 * {
 *   "id": string,
 *   "titulo": string,
 *   "votosFavor": number,
 *   "votosContra": number,
 *   "votosAbstencion": number,
 *   "valorPrincipal": number
 * }
 */
export interface ResultadoVotacion {
  id: string;
  titulo: string;
  valorPrincipal: number;
  unitadPrincipal: string;
  votosFavor: number;
  votosContra: number;
  votosAbstencion: number;
  escalaMinima: number;
  escalaMedia: number;
  escalaMaxima: number;
  tieneMenuOpciones: boolean;
}