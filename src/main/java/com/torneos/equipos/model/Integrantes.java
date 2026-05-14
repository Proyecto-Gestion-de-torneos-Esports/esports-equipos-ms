package com.torneos.equipos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "INTEGRANTES")
public class Integrantes {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "integrante_seq")
    @SequenceGenerator(name = "integrante_seq", sequenceName = "INTEGRANTE_SEQ", allocationSize = 1)
    private Long integranteId;

    @Column(name = "usuario_id",nullable = false)
    private Long usuarioId;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Rol rol;

    @ManyToOne
    @JoinColumn(name = "equipo_id", nullable = false)
    private Equipo equipo;
}
