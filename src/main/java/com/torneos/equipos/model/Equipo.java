package com.torneos.equipos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "EQUIPOS")
public class Equipo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "equipo_seq")
    @SequenceGenerator(name = "equipo_seq", sequenceName = "EQUIPO_SEQ", allocationSize = 1)
    private Long equipoId; //Ocupo Long en vez de Integer porque esto esta pensado como proyecto grande
    //(en un contexto de millones de personas por lo cual con integer quedaria corto)

    @Column(nullable = false, length = 20)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String region;

    @Column(nullable = false)
    private Integer ranking;

    @Column(name = "fecha_fundacion", nullable = false)
    private LocalDate fechaFundacion;

    @Column(name = "correo_contacto",length = 100)
    private String correoContacto;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @OneToMany(mappedBy = "equipo", cascade = CascadeType.ALL)
    private List<Integrantes> listaIntegrantes = new ArrayList<>();


}
