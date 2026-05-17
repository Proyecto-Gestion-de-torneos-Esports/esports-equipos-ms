package com.torneos.equipos.controller;

import com.torneos.equipos.dto.EquipoRequestDTO;
import com.torneos.equipos.dto.EquipoResponseDTO;
import com.torneos.equipos.model.Integrantes;
import com.torneos.equipos.model.Rol;
import com.torneos.equipos.service.EquipoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/equipos")
@RequiredArgsConstructor
public class EquipoController {
    private final EquipoService equipoService;

    @GetMapping
    public ResponseEntity<List<EquipoResponseDTO>> obtenerTodos(){
        return ResponseEntity.ok(equipoService.listarTodos());
    }
    @GetMapping("/{equipoId}/integrantes")
    public ResponseEntity<List<Integrantes>> listarIntegrantes(@PathVariable Long equipoId) {

        List<Integrantes> integrantes = equipoService.obtenerIntegrantesPorEquipo(equipoId);
        return ResponseEntity.ok(integrantes);
    }

    @PostMapping("/{equipoId}/integrantes")
    public ResponseEntity<?> inscribirIntegrante(@PathVariable Long equipoId, @RequestParam Long usuarioId, @RequestParam Rol rol,
                                                 @RequestHeader("usuarioId") Long ejecutorId) {
        String respuesta = equipoService.inscribirIntegrante(equipoId, usuarioId, rol, ejecutorId);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<EquipoResponseDTO>> obtenerActivos(){
        return ResponseEntity.ok(equipoService.obtenerActivos());
    }
    //Buscar por Id
    @GetMapping("/{equipoId}")
    public ResponseEntity<EquipoResponseDTO> obtenerPorId(@PathVariable Long equipoId){
        return equipoService.buscarPorId(equipoId).map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    //Crear equipo
    @PostMapping
    public ResponseEntity<EquipoResponseDTO> crear(@Valid @RequestBody EquipoRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(equipoService.guardar(dto));
    }

    //Actualizar
    @PutMapping("/{equipoId}")
    public ResponseEntity<EquipoResponseDTO> actualizar(@PathVariable Long equipoId, @Valid @RequestBody EquipoRequestDTO dto){
        return equipoService.actualizar(equipoId, dto).map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{equipoId}")
    public ResponseEntity<Void> eliminar(@PathVariable Long equipoId, @RequestHeader("usuarioId") Long ejecutorId) {
        equipoService.eliminar(equipoId, ejecutorId);
        return ResponseEntity.noContent().build();
    }

}
