package com.torneos.equipos.dto;

import lombok.Data;

@Data
public class UsuarioDTO {

    private Long usuarioId;
    private String nombreUsuario;
    private String correo;
    private String rol;
}
