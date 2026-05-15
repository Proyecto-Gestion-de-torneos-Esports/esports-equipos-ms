package com.torneos.equipos.client;

import com.torneos.equipos.dto.AuditoriaRequestDTO;
import com.torneos.equipos.dto.AuditoriaResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "microservicio-auditoria", url = "http://localhost:8011/api/auditoria")
public interface AuditoriaClient {

    @PostMapping
    AuditoriaResponseDTO generarAuditoria(@RequestBody AuditoriaRequestDTO auditoria);
}
