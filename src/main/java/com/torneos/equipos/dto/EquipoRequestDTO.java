package com.torneos.equipos.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipoRequestDTO {

    @NotBlank(message = "El nombre del equipo no puede estar vacio")
    @Size(min = 3, max = 20, message = "El nombre del equipo debe tener entre 3 y 20 caracteres")
    private String nombre;

    @NotBlank(message = "La región es obligatoria(EU,NA,LATAM,BR,etc)")
    private String region;

    @NotNull(message = "El ranking es obligatorio")
    @Min(value = 1, message = "El ranking minimo es 1")
    private Integer ranking;

    @NotNull(message = "La fecha de fundación es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate fechaFundacion;

    @Email(message = "Debe ser un correo valido")
    private String correoContacto;

    @NotNull(message = "El estado es obligatorio")
    private Boolean activo;


}
