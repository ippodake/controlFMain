/**
 * Endpoint: GET /api/leyes/{id}/auditoria
 * 
 * Response Body:
 * {
 *   "id": string,
 *   "titulo": string,
 *   "subtitulo": string,
 *   "filas": [
 *     {
 *       "id": string,
 *       "nombre": string,
 *       "fotoUrl": string,
 *       "bloque": string,
 *       "voto": string,
 *       "analisisCoherencia": string,
 *       "nivelCoherencia": "cumple" | "incumple" | "ambiguo"
 *     }
 *   ]
 * }
 */
export interface AuditoriaCoherencia {
  id: string;                  // Identificador único de la auditoría
  titulo: string;              // Título del componente
  subtitulo: string;           // Descripción debajo del título
  filas: import('./fila auditoria/type_fila_auditoria').FilaAuditoria[];      // Lista de filas (subcomponente)
  textoVerMas: string;         // Texto del enlace inferior
}