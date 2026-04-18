package com.torneos.equipos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "equipos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Equipo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //Ocupo Long en vez de Integer porque esto esta pensado como proyecto grande
    //(en un contexto de millones de personas por lo cual con integer quedaria corto)

    @NotBlank(message = "El nombre del equipo no puede estar vacio")
    @Size(min = 3, max = 20, message = "El nombre debe tener entre 3 y 20 caracteres")
    private String nombre;

    @NotBlank(message = "La región es obligatoria(EU, NA, LATAM, BR")
    @Min(value = 1, message = "El ranking minimo es 1")
    private String region;

    @NotNull(message = "La fecha de fundacion es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate fechaFundacion;

    @Email(message = "Debe ser un correo de valido")
    private String correoContacto;


}
