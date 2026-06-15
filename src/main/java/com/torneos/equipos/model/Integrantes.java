package com.torneos.equipos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long integranteId;

    @Column(name = "id_usuario",nullable = false)
    private Long idUsuario;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Rol rol;

    @ManyToOne
    @JoinColumn(name = "equipo_id", nullable = false)
    @JsonIgnore
    private Equipo equipo;
}
