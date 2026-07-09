import type { ItemSidebarColapsado } from './item sidebar/type_item_sidebar_colapsado';

export interface SidebarColapsado {
  id: string;                        // Identificador del sidebar
  items: ItemSidebarColapsado[];     // Items de navegación (ver type_item_sidebar_colapsado.ts)
  fotoUrlUsuario: string;            // Avatar del usuario en la parte inferior
  textoAdmin: string;                // Etiqueta inferior "ADMIN"
}