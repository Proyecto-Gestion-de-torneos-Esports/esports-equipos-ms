package com.torneos.equipos.service;


import com.torneos.equipos.dto.EquipoRequestDTO;
import com.torneos.equipos.dto.EquipoResponseDTO;
import com.torneos.equipos.model.Equipo;
import com.torneos.equipos.repository.EquipoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EquipoService {

    private final EquipoRepository equipoRepository;


    private EquipoResponseDTO mapToDTO(Equipo equipo){
        return new EquipoResponseDTO(
                equipo.getId(),
                equipo.getNombre(),
                equipo.getRegion(),
                equipo.getRanking(),
                equipo.getFechaFundacion(),
                equipo.getCorreoContacto(),
                equipo.getActivo()
        );
    }

    //Para obtener todos los equipos activos e inactivos
    public List<EquipoResponseDTO> listarTodos(){
        log.info("Listando todos los equipos");
        List<Equipo> equipos = equipoRepository.findAllByOrderByIdAsc();
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
    public Optional<EquipoResponseDTO> buscarPorId(Long id){
        Optional<EquipoResponseDTO> resultado = equipoRepository.findByIdAndActivoTrue(id).map(this::mapToDTO);

        resultado.ifPresentOrElse(
                dto->log.info("Equipo '{}' encontrado correctamente", dto.getNombre()),
                ()->log.warn("No se encontro ningun equipo activo con el ID: {}", id)

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
                dto.getRanking(),
                dto.getFechaFundacion(),
                dto.getCorreoContacto(),
                true

        );
        EquipoResponseDTO respuesta = mapToDTO(equipoRepository.save(equipo));
        log.info("Equipo '{}' creado y guardado correctamente en la base de datos",dto.getNombre());
        return respuesta;
    }

    @Transactional
    public Optional<EquipoResponseDTO> actualizar(Long id, EquipoRequestDTO dto){
        return equipoRepository.findByIdAndActivoTrue(id).map(existente->{
            log.info("Equipo con ID: {} encontrado. Actualizando sus datos",id);
            existente.setNombre(dto.getNombre());
            existente.setRegion(dto.getRegion());
            existente.setRanking(dto.getRanking());
            existente.setFechaFundacion(dto.getFechaFundacion());
            existente.setCorreoContacto(dto.getCorreoContacto());
            existente.setActivo(dto.getActivo());

            EquipoResponseDTO respuesta = mapToDTO(equipoRepository.save(existente));
            log.info("El Equipo '{}' (ID: {}) fue actualizado correctamente", respuesta.getNombre(), id);
            return respuesta;
        });
    }

    @Transactional
    public void eliminar(Long id){
        log.info("Procesando solicitud para eliminar (inactivo) el equipo con ID: {}",id);
        equipoRepository.findByIdAndActivoTrue(id).ifPresentOrElse(existente->{
            existente.setActivo(false);
            equipoRepository.save(existente);
            log.info("El equipo '{}' (ID: {}) fue desactivado correctamente", existente.getNombre(), id);
        },()->{
            log.warn("Intento de eliminación fallido: No se encontro ningun equipo activo con el ID: {}", id);
                }
        );
    }

    @Transactional(readOnly = true)
    public List<EquipoResponseDTO> obtenerTop(Integer top){
        List<Equipo> equiposTop = equipoRepository.obtenerTopEquipos(top);
        log.info("Consulta exitosa. Hay {} equipos para el Top {}", equiposTop.size(), top);
        return equipoRepository.obtenerTopEquipos(top).stream().map(this::mapToDTO).collect(Collectors.toList());
    }



}
