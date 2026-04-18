package com.torneos.equipos.repository;

import com.torneos.equipos.model.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipoRepository extends JpaRepository<Equipo, Long> {

    //Aqui agregare los metodos personalizados
}
