/**
 * Endpoints:
 * - GET  /api/leyes/{id}/debate
 * 
 * - POST /api/leyes/{id}/comentarios
 *   Body: { "texto": string, "usuarioId": number }
 * 
 * - POST /api/leyes/{id}/calificaciones
 *   Body: { "puntaje": number, "usuarioId": number }
 */
export interface DebateCiudadano {
  id: string;
  titulo: string;
  puntuacionPromedio: number;
  puntuacionMaxima: number;
  comentarios: import('./Comentario Debate/type_comentario_debate').ComentarioDebate[];
  placeholderComentario: string;
  tieneBotonEnviar: boolean;
}