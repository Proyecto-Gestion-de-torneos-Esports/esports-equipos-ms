package com.torneos.equipos.service;


import com.torneos.equipos.client.AuditoriaClient;
import com.torneos.equipos.client.UsuarioClient;
import com.torneos.equipos.dto.AuditoriaRequestDTO;
import com.torneos.equipos.dto.EquipoRequestDTO;
import com.torneos.equipos.dto.EquipoResponseDTO;
import com.torneos.equipos.dto.UsuarioDTO;
import com.torneos.equipos.model.Equipo;
import com.torneos.equipos.model.Integrantes;
import com.torneos.equipos.model.Rol;
import com.torneos.equipos.repository.EquipoRepository;
import com.torneos.equipos.repository.IntegrantesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EquipoService {

    private final EquipoRepository equipoRepository;
    private final IntegrantesRepository integrantesRepository;
    private final UsuarioClient usuarioClient;
    private final AuditoriaClient auditoriaClient;


    private EquipoResponseDTO mapToDTO(Equipo equipo){
        int cantidad = (equipo.getListaIntegrantes()!= null) ? equipo.getListaIntegrantes().size() : 0;
        return new EquipoResponseDTO(
                equipo.getEquipoId(),
                equipo.getNombre(),
                equipo.getRegion(),
                equipo.getFechaFundacion(),
                equipo.getCorreoContacto(),
                cantidad,
                equipo.getActivo()
        );
    }

    //Para obtener todos los equipos activos e inactivos
    public List<EquipoResponseDTO> listarTodos(){
        log.info("Listando todos los equipos");
        List<Equipo> equipos = equipoRepository.findAllByOrderByEquipoIdAsc();
        log.info("Hay {} equipos en total", equipos.size());
        return equipos.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    //Para obtener unicamente los equipos activos
    @Transactional(readOnly = true)
    public List<EquipoResponseDTO> obtenerActivos(){
        log.info("Listando solo los equipos activos");
        List<Equipo> activos = equipoRepository.findByActivoTrue();
        log.info("Hay {} equipos activos", activos.size());
        return activos.stream().map(this::mapToDTO).collect(Collectors.toList());
    }
    //Para obtener equipo mediante el Id
    @Transactional(readOnly = true)
    public Optional<EquipoResponseDTO> buscarPorId(Long equipoId){
        Optional<EquipoResponseDTO> resultado = equipoRepository.findByEquipoIdAndActivoTrue(equipoId).map(this::mapToDTO);

        resultado.ifPresentOrElse(
                dto->log.info("Equipo '{}' encontrado correctamente", dto.getNombre()),
                ()->log.warn("No se encontro ningun equipo activo con el ID: {}", equipoId)
        );
        return resultado;
    }
    //Para guardar un equipo
    @Transactional
    public EquipoResponseDTO guardar(EquipoRequestDTO dto){
        Equipo equipo = new Equipo(
                null,
                dto.getNombre(),
                dto.getRegion(),
                dto.getFechaFundacion(),
                dto.getCorreoContacto(),
                true,
                new ArrayList<>()
        );
        EquipoResponseDTO respuesta = mapToDTO(equipoRepository.save(equipo));
        log.info("Equipo '{}' creado y guardado correctamente en la base de datos",dto.getNombre());
        String detalleAuditoria = "Se creo un nuevo equipo con el ID:" + respuesta.getEquipoId();
        generarAuditoria(detalleAuditoria);
        return respuesta;
    }

    @Transactional
    public Optional<EquipoResponseDTO> actualizar(Long id, EquipoRequestDTO dto){
        return equipoRepository.findByEquipoIdAndActivoTrue(id).map(existente->{
            log.info("Equipo con ID: {} encontrado. Actualizando sus datos",id);
            existente.setNombre(dto.getNombre());
            existente.setRegion(dto.getRegion());
            existente.setFechaFundacion(dto.getFechaFundacion());
            existente.setCorreoContacto(dto.getCorreoContacto());
            existente.setActivo(dto.getActivo());

            EquipoResponseDTO respuesta = mapToDTO(equipoRepository.save(existente));
            log.info("El Equipo '{}' (ID: {}) fue actualizado correctamente", respuesta.getNombre(), id);

            String detalleAuditoria = "Se actualizo el equipo con ID: " + id;
            generarAuditoria(detalleAuditoria);
            return respuesta;
        });
    }

    @Transactional
    public void eliminar(Long equipoId, Long ejecutorId) {
        log.info("Procesando solicitud para eliminar (inactivo) el equipo con ID: {} por el ejecutor ID: {}", equipoId, ejecutorId);
        UsuarioDTO ejecutor = usuarioClient.obtenerUsuarioPorId(ejecutorId);
        String rol = ejecutor.getRol();
        if (!"ADMIN".equalsIgnoreCase(rol) && !"ARBITRO".equalsIgnoreCase(rol)) {
            log.warn("Intento de eliminación de equipo no autorizado por el usuario ID: {}", ejecutorId);
            throw new IllegalArgumentException("Acceso denegado: solo los Árbitros y Administradores están autorizados para eliminar equipos.");
        }
        Equipo existente = equipoRepository.findByEquipoIdAndActivoTrue(equipoId)
                .orElseThrow(() -> {
                    log.warn("Intento de eliminación fallido: No se encontró ningún equipo activo con el ID: {}", equipoId);
                    return new java.util.NoSuchElementException("No se encontró ningún equipo activo con el ID: " + equipoId);
                });
        existente.setActivo(false);
        equipoRepository.save(existente);

        log.info("El equipo '{}' (ID: {}) fue desactivado correctamente por el ejecutor ID: {}", existente.getNombre(), equipoId, ejecutorId);
        String detalleAuditoria = "Se desactivó el equipo con ID:" + equipoId;
        generarAuditoria(detalleAuditoria);
    }

    @Transactional
    public String inscribirIntegrante(Long equipoId, Long usuarioId, Rol rol, Long ejecutorId) {
        UsuarioDTO ejecutor = usuarioClient.obtenerUsuarioPorId(ejecutorId);
        String rolEjecutor = ejecutor.getRol();

        if (!"ADMIN".equalsIgnoreCase(rolEjecutor) && !"ARBITRO".equalsIgnoreCase(rolEjecutor)) {
            log.warn("Intento de inscripción no autorizado por el usuario ID: {}", ejecutorId);
            throw new IllegalArgumentException("Acceso denegado: solo los Árbitros y Administradores pueden inscribir jugadores.");
        }
        Equipo equipo = equipoRepository.findById(equipoId)
                .orElseThrow(() -> new java.util.NoSuchElementException("Equipo no encontrado con ID: " + equipoId));

        if (equipo.getListaIntegrantes().size() >= 6) {
            log.warn("Intento de inscripción fallido: El equipo '{}' ya está lleno.", equipo.getNombre());
            throw new IllegalArgumentException("Error: El equipo ya alcanzó el máximo de 6 integrantes (incluyendo coach).");
        }
        String nombreReal = usuarioClient.obtenerUsuarioPorId(usuarioId).getNombreUsuario();

        Integrantes nuevo = new Integrantes();
        nuevo.setUsuarioId(usuarioId);
        nuevo.setNombre(nombreReal);
        nuevo.setRol(rol);
        nuevo.setEquipo(equipo);

        integrantesRepository.save(nuevo);

        log.info("Usuario '{}' (ID: {}) inscrito como {} en el equipo '{}' por el ejecutor ID: {}",
                nombreReal, usuarioId, rol, equipo.getNombre(), ejecutorId);
        return "Usuario '" + nombreReal + "' inscrito correctamente en el equipo " + equipo.getNombre();
    }

    public List<Integrantes> obtenerIntegrantesPorEquipo(Long equipoId) {
        Equipo equipo = equipoRepository.findById(equipoId)
                .orElseThrow(() -> new RuntimeException("Error: Equipo no encontrado con ID " + equipoId));
        return equipo.getListaIntegrantes();
    }

    public void generarAuditoria(String detalle){
        AuditoriaRequestDTO dto = new AuditoriaRequestDTO();
        LocalDate ahora = LocalDate.now();
        dto.setDetalle(detalle);
        dto.setFecha(ahora);
        auditoriaClient.generarAuditoria(dto);
        log.info("Auditoria generada con exito!");
    }

}
