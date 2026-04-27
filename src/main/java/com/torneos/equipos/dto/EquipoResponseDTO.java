package com.torneos.equipos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipoResponseDTO {

    private Long id;
    private String nombre;
    private String region;
    private Integer ranking;
    private LocalDate fechaFundacion;
    private String correoContacto;
    private Boolean activo;
}
