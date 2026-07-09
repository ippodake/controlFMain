/**
 * Endpoint: GET /api/admin/panel
 * 
 * Response Body:
 * {
 *   "tituloSeccion": string,
 *   "opciones": [
 *     {
 *       "nombreOpcion": string,
 *       "icono": string,
 *       "notificacionBadge": number
 *     }
 *   ]
 * }
 */
export interface PanelControl {
  tituloSeccion: string;
  opciones: {
    nombreOpcion: string;
    icono: string;
    notificacionBadge?: number;
  }[];
}