import React from 'react';

interface ContenidoLeyProps {
  id: string;
  titulo: string;
  resumenEjecutivo: string;
  impactoSocial: string;
}

const ContenidoLey: React.FC<ContenidoLeyProps> = ({ resumenEjecutivo, impactoSocial }) => {
  return (
    <div className="bg-white rounded-xl shadow-sm border border-slate-200 overflow-hidden mb-8">
      <div className="px-8 py-6 border-b border-slate-100 bg-slate-50/50">
        <h4 className="text-sm font-bold text-primary-navy uppercase tracking-wide flex items-center gap-2">
          <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"/><path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"/></svg>
          Contenido de la Ley (RF-02)
        </h4>
      </div>
      
      <div className="p-8 space-y-8">
        <div>
          <h5 className="text-xs font-black text-slate-400 uppercase tracking-widest mb-3">Resumen Ejecutivo</h5>
          <p className="text-slate-600 leading-relaxed text-sm bg-slate-50 p-4 rounded-lg border border-slate-100 italic">
            "{resumenEjecutivo}"
          </p>
        </div>

        <div>
          <h5 className="text-xs font-black text-slate-400 uppercase tracking-widest mb-3">Impacto Social</h5>
          <p className="text-slate-600 leading-relaxed text-sm">
            {impactoSocial}
          </p>
        </div>
      </div>
    </div>
  );
};

export default ContenidoLey;
