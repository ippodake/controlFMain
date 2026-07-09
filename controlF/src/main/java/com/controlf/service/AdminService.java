package com.controlf.service;

import com.controlf.db.repository.*;
import com.controlf.db.schema.*;
import com.controlf.db.schema.enums.ImpactoEsperado;
import com.controlf.db.schema.enums.NivelCoherencia;
import com.controlf.dto.PanelControlDTO;
import com.controlf.dto.PanelMantenimientoDTO;
import com.controlf.dto.ReporteHistoricoDTO;
import com.controlf.dto.VinculoRequestDTO;
import com.controlf.dto.MotorCoherenciaDataDTO;
import com.controlf.dto.SimpleItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ReporteRepository reporteRepository;
    private final LogSistemaRepository logRepository;
    private final DataSource dataSource;
    private final PromesaRepository promesaRepository;
    private final LeyRepository leyRepository;
    private final VinculoPromesaLeyRepository vinculoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PoliticoRepository politicoRepository;
    private final VotoRepository votoRepository;

    public MotorCoherenciaDataDTO getMotorData() {
        List<SimpleItemDTO> politicos = politicoRepository.findAll().stream()
                .map(p -> new SimpleItemDTO(p.getId().toString(), p.getNombreCompleto()))
                .collect(Collectors.toList());

        List<SimpleItemDTO> leyes = leyRepository.findAll().stream()
                .map(l -> new SimpleItemDTO(l.getId().toString(), l.getTitulo()))
                .collect(Collectors.toList());

        return MotorCoherenciaDataDTO.builder()
                .politicos(politicos)
                .leyes(leyes)
                .build();
    }

    public List<SimpleItemDTO> getPromesasByPolitico(Integer politicoId) {
        return promesaRepository.findByPoliticoId(politicoId).stream()
                .map(p -> new SimpleItemDTO(p.getId().toString(), p.getDescripcion()))
                .collect(Collectors.toList());
    }

    public void crearVinculoCoherencia(VinculoRequestDTO request) {
        Promesa p = promesaRepository.findById(request.getPromesaId()).orElseThrow();
        Ley l = leyRepository.findById(request.getLeyId()).orElseThrow();

        VinculoPromesaLey v = new VinculoPromesaLey();
        v.setPromesa(p);
        v.setLey(l);
        v.setImpactoEsperado(ImpactoEsperado.valueOf(request.getImpactoEsperado()));
        v.setNivelCoherencia(NivelCoherencia.valueOf(request.getNivelCoherencia()));
        v.setAnalisisCoherencia(request.getAnalisis());

        vinculoRepository.save(v);
        registrarLog("CREAR_VINCULO", "Vínculo creado entre promesa " + p.getId() + " y ley " + l.getId());
    }

    public void ejecutarRespaldo(Integer adminId) {
        registrarLog("BACKUP", "Respaldo manual ejecutado");
    }

    public void limpiarCache() {
        registrarLog("CACHE_CLEAR", "Caché del sistema limpiada");
    }

    public void importarLeyes() {
        registrarLog("IMPORT_LEYES", "Sincronización con API externa iniciada");
    }

    public ReporteHistoricoDTO getHistoricoResumen() {
        long totalLeyes = leyRepository.count();
        long totalVotos = votoRepository.count();
        long votosFavor = votoRepository.countByTipoVoto(com.controlf.db.schema.enums.TipoVoto.FAVOR);
        long votosContra = votoRepository.countByTipoVoto(com.controlf.db.schema.enums.TipoVoto.CONTRA);
        long votosAbstencion = votoRepository.countByTipoVoto(com.controlf.db.schema.enums.TipoVoto.ABSTENCION);
        long leyesAprobadas = leyRepository.countByEstado(com.controlf.db.schema.enums.EstadoLey.APROBADA);
        long leyesEnDebate = leyRepository.countByEstado(com.controlf.db.schema.enums.EstadoLey.DEBATE);

        return ReporteHistoricoDTO.builder()
                .totalLeyes(totalLeyes)
                .totalVotos(totalVotos)
                .votosFavor(votosFavor)
                .votosContra(votosContra)
                .votosAbstencion(votosAbstencion)
                .leyesAprobadas(leyesAprobadas)
                .leyesEnDebate(leyesEnDebate)
                .build();
    }

    private void registrarLog(String accion, String detalles) {
        LogSistema log = new LogSistema();
        log.setAccion(accion);
        log.setDetalles(detalles);
        log.setFecha(LocalDateTime.now());
        logRepository.save(log);
    }

    public PanelControlDTO getSecurityPanel() {
        int reportadosCount = (int) reporteRepository.countByEstado(Reporte.EstadoReporte.PENDIENTE);
        int logsCount = (int) logRepository.count();

        return PanelControlDTO.builder()
                .tituloSeccion("SEGURIDAD Y USUARIOS")
                .opciones(Arrays.asList(
                        new PanelControlDTO.OpcionPanelDTO("Gestionar Roles de Staff", "user-cog", 0),
                        new PanelControlDTO.OpcionPanelDTO("Usuarios Reportados", "flag", reportadosCount),
                        new PanelControlDTO.OpcionPanelDTO("Auditoría de Logs del Sistema", "terminal", logsCount)
                ))
                .build();
    }

    public PanelMantenimientoDTO getMantenimientoInfo() {
        boolean dbStatus = isDatabaseUp();
        int cpuLoad = getSystemCpuLoad();
        
        String ultimaFecha = logRepository.findAll().stream()
                .filter(l -> l.getAccion().contains("BACKUP"))
                .map(l -> l.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                .findFirst()
                .orElse("SIN REGISTRO");

        return PanelMantenimientoDTO.builder()
                .id("srv-01")
                .titulo("ESTADO DEL SERVIDOR")
                .codigoReferencia("RNF-02/05")
                .estadoBaseDeDatos(dbStatus)
                .estadoEtiqueta(dbStatus ? "ONLINE" : "OFFLINE")
                .fechaUltimoRespaldo(ultimaFecha)
                .cargaServidorPorcentaje(cpuLoad)
                .accionesDisponibles(Arrays.asList("BACKUP", "CACHE_CLEAR", "IMPORT_LEYES"))
                .build();
    }

    private boolean isDatabaseUp() {
        try (Connection conn = dataSource.getConnection()) {
            return conn.isValid(1);
        } catch (Exception e) {
            return false;
        }
    }

    private int getSystemCpuLoad() {
        try {
            OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
            double load = osBean.getSystemCpuLoad();
            return (load < 0) ? 0 : (int) (load * 100);
        } catch (Exception e) {
            return 0;
        }
    }
}
