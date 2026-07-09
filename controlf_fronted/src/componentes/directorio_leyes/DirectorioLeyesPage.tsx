import React, { useState, useEffect } from 'react';
import BarraBusquedaLeyes from './BarraBusquedaLeyes';
import ListaLeyes from './ListaLeyes';
import Paginacion from '../directorio_politicos/Componente pie pagina/Paginacion';

const DirectorioLeyesPage: React.FC = () => {
  const [leyes, setLeyes] = useState([]);
  const [paginaActual, setPaginaActual] = useState(1);
  const [totalPaginas, setTotalPaginas] = useState(1);
  const [isLoading, setIsLoading] = useState(true);
  const [termino, setTermino] = useState('');
  const [filtros, setFiltros] = useState<Array<{ id: string; label: string; valorSeleccionado: string | null; opciones: string[] }>>([
    { id: 'categoria', label: 'Categoría', valorSeleccionado: null, opciones: [] },
    { id: 'estado', label: 'Estado', valorSeleccionado: null, opciones: [] },
  ]);

  const fetchFiltros = async () => {
    try {
      const response = await fetch('/api/leyes/filtros');
      const data = await response.json();
      setFiltros(prev => prev.map(f => {
        if (f.id === 'categoria') return { ...f, opciones: data.categorias };
        if (f.id === 'estado') return { ...f, opciones: data.estados };
        return f;
      }));
    } catch (error) {
      console.error("Error al cargar filtros de leyes:", error);
    }
  };

  const fetchLeyes = async (page: number) => {
    setIsLoading(true);
    try {
      const params = new URLSearchParams({
        pagina: page.toString(),
        size: '10',
        termino: termino,
        categoria: filtros.find(f => f.id === 'categoria')?.valorSeleccionado || '',
        estado: filtros.find(f => f.id === 'estado')?.valorSeleccionado || ''
      });

      const response = await fetch(`/api/leyes?${params.toString()}`);
      const data = await response.json();
      
      setLeyes(data.leyes);
      setPaginaActual(data.paginaActual);
      setTotalPaginas(data.totalPaginas);
    } catch (error) {
      console.error("Error al cargar leyes:", error);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchFiltros();
  }, []);

  useEffect(() => {
    fetchLeyes(paginaActual);
  }, [paginaActual]);

  const handleSearchSubmit = () => {
    setPaginaActual(1);
    fetchLeyes(1);
  };

  return (
    <div className="max-w-6xl mx-auto">
      <BarraBusquedaLeyes
        termino={termino}
        filtros={filtros}
        onSearchChange={setTermino}
        onFilterChange={(id, val) => setFiltros(prev => prev.map(f => f.id === id ? { ...f, valorSeleccionado: val } : f))}
        onSearchSubmit={handleSearchSubmit}
      />

      <ListaLeyes
        leyes={leyes}
        isLoading={isLoading}
      />

      <Paginacion
        paginaActual={paginaActual}
        totalPaginas={totalPaginas}
        onPageChange={setPaginaActual}
      />
    </div>
  );
};

export default DirectorioLeyesPage;
