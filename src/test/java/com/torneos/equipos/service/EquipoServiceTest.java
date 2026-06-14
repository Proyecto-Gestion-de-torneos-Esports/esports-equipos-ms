package com.torneos.equipos.service;

import com.torneos.equipos.client.AuditoriaClient;
import com.torneos.equipos.client.UsuarioClient;
import com.torneos.equipos.dto.EquipoRequestDTO;
import com.torneos.equipos.dto.EquipoResponseDTO;
import com.torneos.equipos.dto.UsuarioDTO;
import com.torneos.equipos.model.Equipo;
import com.torneos.equipos.model.Integrantes;
import com.torneos.equipos.model.Rol;
import com.torneos.equipos.repository.EquipoRepository;
import com.torneos.equipos.repository.IntegrantesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EquipoServiceTest {
    @Mock
    private EquipoRepository equipoRepository;

    @Mock
    private IntegrantesRepository integrantesRepository;

    @Mock
    private UsuarioClient usuarioClient;

    @Mock
    private AuditoriaClient auditoriaClient;

    @InjectMocks
    private EquipoService equipoService;

    private Equipo equipo;
    private UsuarioDTO adminUser;
    private UsuarioDTO jugadorUser;

    @BeforeEach
    void setUp() {
        equipo = new Equipo(1L, "Team Beta", "EU", LocalDate.now(), "beta@team.com", true, new ArrayList<>());

        adminUser = new UsuarioDTO();
        adminUser.setRol("ADMIN");
        adminUser.setNombreUsuario("AdminTest");

        jugadorUser = new UsuarioDTO();
        jugadorUser.setRol("JUGADOR");
        jugadorUser.setNombreUsuario("ProPlayer");
    }
    @Test
    void buscarPorId_Existe_DebeRetornarDto() {
        when(equipoRepository.findByEquipoIdAndActivoTrue(1L)).thenReturn(Optional.of(equipo));

        Optional<EquipoResponseDTO> resultado = equipoService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Team Beta", resultado.get().getNombre());
    }
    @Test
    void eliminar_RolAdmin_DebeDesactivarEquipo() {
        when(usuarioClient.obtenerUsuarioPorId(99L)).thenReturn(adminUser);
        when(equipoRepository.findByEquipoIdAndActivoTrue(1L)).thenReturn(Optional.of(equipo));

        equipoService.eliminar(1L, 99L);

        assertFalse(equipo.getActivo());
        verify(equipoRepository, times(1)).save(equipo);
        verify(auditoriaClient, times(1)).generarAuditoria(any());
    }
    @Test
    void eliminar_RolJugador_DebeLanzarExcepcion() {
        when(usuarioClient.obtenerUsuarioPorId(88L)).thenReturn(jugadorUser);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            equipoService.eliminar(1L, 88L);});
        assertEquals("Acceso denegado: solo los Árbitros y Administradores están autorizados para eliminar equipos.", ex.getMessage());
        verify(equipoRepository, never()).save(any());
    }
    @Test
    void inscribirIntegrante_Exito_DebeGuardarIntegrante() {
        when(usuarioClient.obtenerUsuarioPorId(99L)).thenReturn(adminUser); // El que ejecuta
        when(usuarioClient.obtenerUsuarioPorId(10L)).thenReturn(jugadorUser); // El que se inscribe
        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipo));

        String respuesta = equipoService.inscribirIntegrante(1L, 10L, 99L);

        assertEquals("Usuario 'ProPlayer' inscrito correctamente en el equipo Team Beta", respuesta);
        verify(integrantesRepository, times(1)).save(any(Integrantes.class));
    }
    @Test
    void inscribirIntegrante_EquipoLleno_DebeLanzarExcepcion() {
        // Llenamos el equipo con 6 integrantes
        for (int i = 0; i < 6; i++) {
            equipo.getListaIntegrantes().add(new Integrantes());
        }
        when(usuarioClient.obtenerUsuarioPorId(99L)).thenReturn(adminUser);
        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipo));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            equipoService.inscribirIntegrante(1L, 10L, 99L);
        });
        assertTrue(ex.getMessage().contains("máximo de 6 integrantes"));
    }

}
