package com.torneos.equipos.repository;

import com.torneos.equipos.model.Integrantes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntegrantesRepository extends JpaRepository<Integrantes, Long> {
}
