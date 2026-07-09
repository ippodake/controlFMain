import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import InfoBasica from './InfoBasica';
import MetricaCoherencia from './MetricaCoherencia';
import HistorialCoherencia from './HistorialCoherencia';
import ParticipacionCiudadana from './ParticipacionCiudadana';
import { type PerfilPolitico } from './type_perfil_politico';

const PerfilPoliticoPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [perfil, setPerfil] = useState<PerfilPolitico | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [campoEdicion, setCampoEdicion] = useState<'patrimonio' | 'antecedentes'>('patrimonio');
  const [valorEdicion, setValorEdicion] = useState('');
  const [isSaving, setIsSaving] = useState(false);
  const [mensajeEdicion, setMensajeEdicion] = useState<string | null>(null);
  const [historialCambios, setHistorialCambios] = useState<Array<{ campo: string; valor: string; fecha: string }>>([]);

  const fetchPerfil = async () => {
    setIsLoading(true);
    try {
      const response = await fetch(`/api/politicos/${id}`);
      if (!response.ok) throw new Error("Perfil no encontrado");
      const data = await response.json();
      setPerfil(data);
    } catch (error) {
      console.error("Error al cargar el perfil:", error);
      navigate('/'); // Volver al directorio si hay error
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchPerfil();
  }, [id]);

  const handleAddComentario = async (texto: string, puntaje: number) => {
    try {
      await fetch(`/api/politicos/${id}/comentarios`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ texto, usuarioId: 1 })
      });

      await fetch(`/api/politicos/${id}/calificaciones`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ puntaje, usuarioId: 1 })
      });

      fetchPerfil();
    } catch (error) {
      console.error("Error al publicar comentario:", error);
    }
  };

  const handleEditarPolitico = async (event: React.FormEvent) => {
    event.preventDefault();
    if (!id || !valorEdicion.trim()) return;

    setIsSaving(true);
    setMensajeEdicion(null);

    try {
      const response = await fetch(`/api/politicos/${id}`, {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ campo: campoEdicion, valor: valorEdicion.trim() })
      });

      if (!response.ok) throw new Error('No se pudo actualizar el campo');

      const fecha = new Date().toLocaleString('es-EC', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      });

      setHistorialCambios((prev) => [{ campo: campoEdicion, valor: valorEdicion.trim(), fecha }, ...prev]);
      setValorEdicion('');
      setMensajeEdicion(`Cambio guardado en ${campoEdicion === 'patrimonio' ? 'patrimonio' : 'antecedentes'}.`);
      await fetchPerfil();
    } catch (error) {
      console.error('Error al actualizar político:', error);
      setMensajeEdicion('No se pudo guardar el cambio.');
    } finally {
      setIsSaving(false);
    }
  };

  if (isLoading) {
    return (
      <div className="max-w-5xl mx-auto animate-pulse">
        <div className="bg-white h-64 rounded-xl mb-8 border border-slate-200"></div>
        <div className="bg-white h-48 rounded-xl mb-8 border border-slate-200"></div>
        <div className="bg-white h-96 rounded-xl border border-slate-200"></div>
      </div>
    );
  }

  if (!perfil) return null;

  return (
    <div className="max-w-5xl mx-auto">
      <button 
        onClick={() => navigate('/')}
        className="flex items-center gap-2 text-slate-500 hover:text-primary-navy font-bold text-sm mb-6 transition-colors group"
      >
        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round" className="group-hover:-translate-x-1 transition-transform"><line x1="19" y1="12" x2="5" y2="12"/><polyline points="12 19 5 12 12 5"/></svg>
        Volver al Directorio
      </button>

      <InfoBasica 
        nombre={perfil.nombre}
        organizacion={perfil.organizacion}
        cargo={perfil.cargo}
        patrimonio={perfil.patrimonio}
        fotoUrl={perfil.fotoUrl}
        estaActivo={perfil.estaActivo}
        antecedentes={perfil.antecedentes}
      />

      <div className="bg-white rounded-xl shadow-sm border border-slate-200 p-6 mb-8">
        <div className="flex items-center justify-between mb-4">
          <div>
            <h3 className="text-lg font-black text-primary-navy">Actualizar patrimonio o antecedentes</h3>
            <p className="text-sm text-slate-500">Edición mínima conectada al endpoint real del político.</p>
          </div>
        </div>

        <form onSubmit={handleEditarPolitico} className="space-y-4">
          <div className="grid gap-4 md:grid-cols-[180px_1fr_auto]">
            <select
              value={campoEdicion}
              onChange={(event) => setCampoEdicion(event.target.value as 'patrimonio' | 'antecedentes')}
              className="rounded-xl border border-slate-200 bg-white px-4 py-3 text-sm text-slate-700 focus:border-accent-blue focus:outline-none"
            >
              <option value="patrimonio">Patrimonio</option>
              <option value="antecedentes">Antecedentes</option>
            </select>

            <input
              type="text"
              value={valorEdicion}
              onChange={(event) => setValorEdicion(event.target.value)}
              placeholder={campoEdicion === 'patrimonio' ? 'Ej. 1500000' : 'Nuevo texto de antecedentes'}
              className="rounded-xl border border-slate-200 bg-white px-4 py-3 text-sm text-slate-700 focus:border-accent-blue focus:outline-none"
            />

            <button
              type="submit"
              disabled={isSaving}
              className="rounded-xl bg-primary-navy px-4 py-3 text-sm font-bold text-white hover:bg-slate-800 disabled:opacity-60"
            >
              {isSaving ? 'Guardando...' : 'Guardar'}
            </button>
          </div>

          {mensajeEdicion && (
            <p className="text-sm text-success-green">{mensajeEdicion}</p>
          )}
        </form>

        {historialCambios.length > 0 && (
          <div className="mt-6 rounded-xl border border-slate-100 bg-slate-50 p-4">
            <h4 className="text-sm font-bold uppercase tracking-wide text-slate-500 mb-3">Historial reciente</h4>
            <ul className="space-y-2 text-sm text-slate-600">
              {historialCambios.map((item, index) => (
                <li key={`${item.campo}-${index}`} className="flex flex-wrap items-center gap-2">
                  <span className="font-semibold text-primary-navy">{item.campo}</span>
                  <span>→</span>
                  <span>{item.valor}</span>
                  <span className="text-slate-400">({item.fecha})</span>
                </li>
              ))}
            </ul>
          </div>
        )}
      </div>

      <MetricaCoherencia 
        porcentaje={perfil.porcentajeCoherencia}
        estadoEtiqueta={perfil.estadoEtiqueta}
      />

      <HistorialCoherencia 
        historial={perfil.historial}
      />

      <ParticipacionCiudadana 
        comentarios={perfil.comentarios}
        onAddComentario={handleAddComentario}
      />
    </div>
  );
};

export default PerfilPoliticoPage;
