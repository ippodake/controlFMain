export interface ItemNavegacion {
  id: string;           // Identificador único del item
  label: string;        // Texto del item de navegación
  icono: string;        // Nombre o referencia del ícono
  estaActivo: boolean;  // Si está seleccionado/activo actualmente
  ruta: string;         // Ruta de navegación
}