/**
 * Endpoints:
 * - GET  /api/politicos
 * 
 * - POST /api/politicos/{id}/comentarios
 *   Body: { "texto": string, "usuarioId": number }
 * 
 * - POST /api/politicos/{id}/calificaciones
 *   Body: { "puntaje": number, "usuarioId": number }
 */
export interface CartaPolitico {
  id: string;                    // Identificador único del político
  nombre: string;                // Nombre completo
  organizacion: string;          // Movimiento o partido
  fotoUrl: string;               // URL del avatar
  estaActivo: boolean;           // Punto de color bajo el avatar
  estadoEtiqueta: string;        // Badge: COHERENTE | INCOHERENTE | ABSTENCIÓN ALTA
  porcentajeCoherencia: number;  // Valor numérico de coherencia (sin %)
  cantidadProyectos: number;     // Número de proyectos
}