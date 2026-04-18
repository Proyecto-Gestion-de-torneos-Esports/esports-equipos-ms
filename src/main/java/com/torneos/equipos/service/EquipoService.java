package com.torneos.equipos.service;

import com.torneos.equipos.model.Equipo;
import com.torneos.equipos.repository.EquipoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j //Esta anotacion nos permite ocupar log ya que registra que se hizo en el sistema y por quien.
public class EquipoService {
    @Autowired
    private EquipoRepository repository;

    @Transactional(readOnly = true) //transactional readOnly = true sirve para hacer consultas de get haciendo consultas mas rapida para el sql.
    public List<Equipo> listarTodos(){
        log.info("Extrayendo lista de equipos participantes");
        return repository.findAll();
    }
    @Transactional //el transactional solo es por si algo falla al guardar evita que la base de datos guarde datos corruptos.
    public Equipo registrar(Equipo equipo){
        log.info("Registrando nuevo equipo: {}",equipo.getNombre());
        return repository.save(equipo);
    }
    @Transactional
    public Optional<Equipo> actualizar(Long id, Equipo equipoUpdate){
        return repository.findById(id).map(equipoExistente->{ /*el map sirve si coincide en este caso el id para luego actualizar,
                                                                     genera la funcion pero sino el map no hace nada*/
            log.info("Actualizando datos del equipo: {}", equipoExistente.getNombre());
            equipoExistente.setNombre(equipoUpdate.getNombre());
            equipoExistente.setRegion(equipoUpdate.getRegion());
            equipoExistente.setCorreoContacto(equipoUpdate.getCorreoContacto());
            equipoExistente.setFechaFundacion(equipoUpdate.getFechaFundacion());
            equipoExistente.setRanking(equipoUpdate.getRanking());
            return repository.save(equipoExistente);

        });

    }
    @Transactional(readOnly = true)
    public Optional<Equipo> buscarPorId(Long id){
        log.info("Auditoria: Buscando equipo con ID: {}", id);
        return repository.findById(id);
    }

}
