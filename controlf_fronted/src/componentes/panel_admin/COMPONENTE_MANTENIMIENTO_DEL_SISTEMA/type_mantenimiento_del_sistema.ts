/**
 * Endpoints:
 * - GET  /api/admin/mantenimiento
 * 
 * - POST /api/admin/mantenimiento/respaldo -> (Sin Body)
 * - POST /api/admin/mantenimiento/limpiar-cache -> (Sin Body)
 * - POST /api/admin/importar-leyes -> (Sin Body)
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