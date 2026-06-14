package com.torneos.equipos.controller;

import com.torneos.equipos.dto.EquipoRequestDTO;
import com.torneos.equipos.dto.EquipoResponseDTO;
import com.torneos.equipos.model.Integrantes;
import com.torneos.equipos.service.EquipoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/equipos")
@RequiredArgsConstructor
@Tag(name = "Equipos", description = "Endpoints para la gestión de equipos de Esports y sus integrantes")
public class EquipoController {
    private final EquipoService equipoService;


    @Operation(summary = "Listar todos los equipos", description = "Retorna una lista completa de todos los equipos, incluyendo activos e inactivos.")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<EquipoResponseDTO>> obtenerTodos(){
        return ResponseEntity.ok(equipoService.listarTodos());
    }

    @Operation(summary = "Listar integrantes de un equipo", description = "Obtiene la lista de jugadores y coaches asociados a un equipo específico.")
    @ApiResponse(responseCode = "200", description = "Lista de integrantes obtenida")
    @ApiResponse(responseCode = "404", description = "Equipo no encontrado")
    @GetMapping("/{equipoId}/integrantes")
    public ResponseEntity<List<Integrantes>> listarIntegrantes(@PathVariable Long equipoId) {

        List<Integrantes> integrantes = equipoService.obtenerIntegrantesPorEquipo(equipoId);
        return ResponseEntity.ok(integrantes);
    }

    @Operation(summary = "Inscribir integrante en un equipo", description = "Asigna un usuario existente a un equipo. Requiere permisos administrativos en el Header.")
    @ApiResponse(responseCode = "200", description = "Integrante inscrito correctamente")
    @ApiResponse(responseCode = "403", description = "Acceso denegado")
    @PostMapping("/{equipoId}/integrantes")
    public ResponseEntity<?> inscribirIntegrante(@PathVariable Long equipoId, @RequestParam Long usuarioId,
                                                 @RequestHeader("usuarioId") Long ejecutorId) {
        String respuesta = equipoService.inscribirIntegrante(equipoId, usuarioId, ejecutorId);
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Listar equipos activos", description = "Retorna únicamente los equipos que tienen estado activo.")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping("/activos")
    public ResponseEntity<List<EquipoResponseDTO>> obtenerActivos(){
        return ResponseEntity.ok(equipoService.obtenerActivos());
    }

    //Buscar por Id

    @Operation(summary = "Buscar equipo por ID", description = "Obtiene los detalles de un equipo específico.")
    @ApiResponse(responseCode = "200", description = "Equipo encontrado")
    @ApiResponse(responseCode = "404", description = "Equipo no encontrado")
    @GetMapping("/{equipoId}")
    public ResponseEntity<EquipoResponseDTO> obtenerPorId(@PathVariable Long equipoId){
        return equipoService.buscarPorId(equipoId).map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    //Crear equipo
    @Operation(summary = "Crear nuevo equipo", description = "Registra un nuevo equipo en el sistema.")
    @ApiResponse(responseCode = "201", description = "Equipo creado con éxito")
    @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados")
    @PostMapping
    public ResponseEntity<EquipoResponseDTO> crear(@Valid @RequestBody EquipoRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(equipoService.guardar(dto));
    }

    //Actualizar
    @Operation(summary = "Actualizar equipo", description = "Modifica la información de un equipo. Requiere permisos administrativos.")
    @ApiResponse(responseCode = "200", description = "Equipo actualizado correctamente")
    @ApiResponse(responseCode = "404", description = "Equipo no encontrado")
    @PutMapping("/{equipoId}")
    public ResponseEntity<EquipoResponseDTO> actualizar(@PathVariable Long equipoId, @Valid @RequestBody EquipoRequestDTO dto, @RequestHeader("usuarioId") Long ejecutorId){
        return equipoService.actualizar(equipoId, dto, ejecutorId).map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar equipo", description = "Realiza un borrado lógico del equipo (lo desactiva). Requiere permisos administrativos.")
    @ApiResponse(responseCode = "204", description = "Equipo desactivado correctamente")
    @DeleteMapping("/{equipoId}")
    public ResponseEntity<Void> eliminar(@PathVariable Long equipoId, @RequestHeader("usuarioId") Long ejecutorId) {
        equipoService.eliminar(equipoId, ejecutorId);
        return ResponseEntity.noContent().build();
    }

}
