package com.torneos.equipos.controller;

import com.torneos.equipos.dto.EquipoRequestDTO;
import com.torneos.equipos.dto.EquipoResponseDTO;
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

    //Obtener todos los equipos que esten activos
    @GetMapping("/activos")
    public ResponseEntity<List<EquipoResponseDTO>> obtenerActivos(){
        return ResponseEntity.ok(equipoService.obtenerActivos());
    }
    //Buscar por Id
    @GetMapping("/{id}")
    public ResponseEntity<EquipoResponseDTO> obtenerPorId(@PathVariable Long id){
        return equipoService.buscarPorId(id).map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }
    //Crear equipo
    @PostMapping
    public ResponseEntity<EquipoResponseDTO> crear(@Valid @RequestBody EquipoRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(equipoService.guardar(dto));
    }

    //Actualizar
    @PutMapping("/{id}")
    public ResponseEntity<EquipoResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody EquipoRequestDTO dto){
        return equipoService.actualizar(id, dto).map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    //Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, @RequestParam String rol){
        if (!rol.equalsIgnoreCase("ARBITRO") && !rol.equalsIgnoreCase("ADMIN")){
            throw new RuntimeException("Acceso denegado: solo los Arbitros y Administradores estan autorizados para eliminar equipos");
        }
        if (equipoService.buscarPorId(id).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        equipoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    //Busqueda top equipos
    @GetMapping("/top")
    public ResponseEntity<List<EquipoResponseDTO>> obtenerTopEquipos(@RequestParam(defaultValue = "3") Integer top){
        return ResponseEntity.ok(equipoService.obtenerTop(top));
    }

}
