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

    //Aqui agregare los metodos personalizados

    List<Equipo> findAllByOrderByEquipoIdAsc();

    //Metodo para traer solo a los equipos activos
    List<Equipo> findByActivoTrue();

    //Metodo para buscar por Id si el equipo esta activo
    Optional<Equipo> findByEquipoIdAndActivoTrue(Long equipoId);

    //Metodo para buscar por nombre
    List<Equipo> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);

    //Esta query trae los equipos de una region especifica y ordenara segun su ranking
    @Query("SELECT e FROM Equipo e WHERE e.region = :region AND e.activo = true ORDER BY e.ranking ASC")
    List<Equipo> buscarPorRegionOrdenados(@Param("region") String region);

    //Query para traer el top que queramos del torneo por ejemplo el top 3 top 5 top 10, etc
    @Query(value = "SELECT * FROM equipos WHERE activo = 1 ORDER BY ranking ASC FETCH FIRST :top ROWS ONLY",
    nativeQuery = true)
    List<Equipo> obtenerTopEquipos(@Param("top") Integer top);
}
