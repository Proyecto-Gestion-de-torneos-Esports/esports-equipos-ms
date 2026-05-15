package com.torneos.equipos.client;

import com.torneos.equipos.dto.UsuarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "usuarios", url = "http://localhost:8014/api/usuarios")
public interface UsuarioClient {
    @GetMapping("/{usuarioId}")
    UsuarioDTO obtenerUsuarioPorId(@PathVariable("usuarioId") Long usuarioId);
}
