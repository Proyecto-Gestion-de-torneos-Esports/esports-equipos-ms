package com.torneos.equipos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.torneos.equipos.dto.EquipoRequestDTO;
import com.torneos.equipos.dto.EquipoResponseDTO;
import com.torneos.equipos.model.Integrantes;
import com.torneos.equipos.service.EquipoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EquipoController.class)
public class EquipoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EquipoService equipoService;

    private EquipoResponseDTO equipoResponse;
    private EquipoRequestDTO equipoRequest;

    @BeforeEach
    void setUp() {
        equipoResponse = new EquipoResponseDTO(1L, "Team Alpha", "LATAM", LocalDate.now(), "contacto@alpha.com", 5, true);
        equipoRequest = new EquipoRequestDTO("Team Alpha", "LATAM", LocalDate.now(), "contacto@alpha.com", true);
    }

    @Test
    @WithMockUser(roles = "ADMIN") // Autenticación simulada
    void obtenerTodos_DebeRetornarLista200() throws Exception {
        when(equipoService.listarTodos()).thenReturn(Arrays.asList(equipoResponse));
        mockMvc.perform(get("/api/equipos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Team Alpha"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void obtenerPorId_Existe_DebeRetornar200() throws Exception {
        when(equipoService.buscarPorId(1L)).thenReturn(Optional.of(equipoResponse));

        mockMvc.perform(get("/api/equipos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Team Alpha"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void obtenerPorId_NoExiste_DebeRetornar404() throws Exception {
        when(equipoService.buscarPorId(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/equipos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void crear_DatosValidos_DebeRetornar201() throws Exception {
        when(equipoService.guardar(any(EquipoRequestDTO.class))).thenReturn(equipoResponse);
        mockMvc.perform(post("/api/equipos")
                        .with(csrf()) // Token simulado para petición POST
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(equipoRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Team Alpha"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void eliminar_DebeRetornar204() throws Exception {
        doNothing().when(equipoService).eliminar(1L, 2L);

        mockMvc.perform(delete("/api/equipos/1")
                        .with(csrf()) // Token simulado para petición DELETE
                        .header("idUsuario", 2L))
                .andExpect(status().isNoContent());
        verify(equipoService, times(1)).eliminar(1L, 2L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void listarIntegrantes_DebeRetornar200() throws Exception {
        Integrantes integrante = new Integrantes();
        integrante.setNombre("Faker");
        when(equipoService.obtenerIntegrantesPorEquipo(1L)).thenReturn(List.of(integrante));

        mockMvc.perform(get("/api/equipos/1/integrantes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Faker"));
    }
}