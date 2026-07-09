/**
 * Endpoints:
 * - GET  /api/admin/mantenimiento
 * 
 * - POST /api/admin/vinculos
 *   Body: { 
 *     "promesaId": number, 
 *     "leyId": number, 
 *     "impactoEsperado": "POSITIVO" | "NEGATIVO", 
 *     "nivelCoherencia": "CUMPLE" | "INCUMPLE" | "AMBIGUO",
 *     "analisis": string 
 *   }
 */
export interface PanelMantenimientoServidor {
  id: string;
  titulo: string;
  codigoReferencia: string;
  estadoBaseDeDatos: boolean;
  estadoEtiqueta: string;
  fechaUltimoRespaldo: string;
  cargaServidorPorcentaje: number;
  accionesDisponibles: string[];
}