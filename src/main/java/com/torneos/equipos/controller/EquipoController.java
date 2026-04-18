package com.torneos.equipos.controller;

import com.torneos.equipos.model.Equipo;
import com.torneos.equipos.service.EquipoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/equipos")
public class EquipoController {
    @Autowired
    private EquipoService service;

    @GetMapping
    public ResponseEntity<?> listar(){
        return ResponseEntity.ok(service.listarTodos());
    }
    @PostMapping
    public ResponseEntity<?> guardar(@Valid @RequestBody Equipo equipo, BindingResult result){
        if (result.hasErrors()){
            return validar(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(equipo));
    }
    @GetMapping("{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id){
        return service.buscarPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Equipo equipo){
        return service.actualizar(id, equipo).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    /* Este bloque funciona para revisar lo que se ingresa y si no cumple con las validaciones
     no permite que se registren en el caso de guardar y dice que es lo que fallo exactamente.*/

    private ResponseEntity<Map<String, String>> validar (BindingResult result){
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err-> /*aqui no use llaves{} porque como es solo una linea no lo necesita*/
            errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errores);
    }
}
