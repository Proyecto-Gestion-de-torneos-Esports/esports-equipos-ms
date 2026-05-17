package com.torneos.equipos.repository;

import com.torneos.equipos.model.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Long> {

    List<Equipo> findAllByOrderByEquipoIdAsc();
    List<Equipo> findByActivoTrue();
    Optional<Equipo> findByEquipoIdAndActivoTrue(Long equipoId);
}
