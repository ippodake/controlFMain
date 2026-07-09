package com.controlf.db.schema;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "politicos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Politico {

    public Politico(Integer id, String nombreCompleto, String partidoPolitico, String cargoActual, String region,
                    String comision, Boolean estaActivo, BigDecimal patrimonioDeclarado, String antecedentes,
                    String fotoUrl, List<Promesa> promesas, List<Voto> votos, List<Comentario> comentarios,
                    List<Calificacion> calificaciones) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.partidoPolitico = partidoPolitico;
        this.cargoActual = cargoActual;
        this.region = region;
        this.comision = comision;
        this.estaActivo = estaActivo;
        this.patrimonioDeclarado = patrimonioDeclarado;
        this.antecedentes = antecedentes;
        this.fotoUrl = fotoUrl;
        this.promesas = promesas;
        this.votos = votos;
        this.comentarios = comentarios;
        this.calificaciones = calificaciones;
        this.historialActualizaciones = null;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombreCompleto;

    private String partidoPolitico;
    private String cargoActual;
    private String region;
    private String comision;
    private Boolean estaActivo;
    private BigDecimal patrimonioDeclarado;

    @Column(columnDefinition = "TEXT")
    private String antecedentes;

    @Column(columnDefinition = "TEXT")
    private String historialActualizaciones;

    private String fotoUrl;

    @OneToMany(mappedBy = "politico", cascade = CascadeType.ALL)
    private List<Promesa> promesas;

    @OneToMany(mappedBy = "politico", cascade = CascadeType.ALL)
    private List<Voto> votos;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "politico_comentarios",
        joinColumns = @JoinColumn(name = "politico_id"),
        inverseJoinColumns = @JoinColumn(name = "comentario_id")
    )
    private List<Comentario> comentarios;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "politico_calificaciones",
        joinColumns = @JoinColumn(name = "politico_id"),
        inverseJoinColumns = @JoinColumn(name = "calificacion_id")
    )
    private List<Calificacion> calificaciones;
}
