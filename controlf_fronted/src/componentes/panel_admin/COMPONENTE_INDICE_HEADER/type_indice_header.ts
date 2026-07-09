export interface SidebarNavigation {
  id: string;                  // Identificador único de la sesión del menú
  nombreApp: string;           // Nombre o título del panel principal (e.g., "PAC ADMIN")
  logoUrl: string;             // URL o referencia del icono principal del escudo
  seccionActiva: string;       // El nombre de la sección actualmente seleccionada ("Admin")
  itemsMenu: NavigationItem[]; // Lista de opciones disponibles en el menú de navegación
  botonCerrarSesion: string;   // Texto del botón de salida ("Cerrar Sesión")
}

interface NavigationItem {
  id: string;                  // Identificador único del item de navegación
  etiqueta: string;            // Texto visible de la opción (e.g., "Dashboard", "Políticos")
  iconoUrl: string;            // Referencia o nombre del icono asociado
  estaSeleccionado: boolean;   // Determina si el elemento está resaltado visualmente
}