export interface IndiceHeader {
  id: string;
  secciones: {
    nombre: string;
    icono: string;
    estaActivo: boolean;
  }[];
}