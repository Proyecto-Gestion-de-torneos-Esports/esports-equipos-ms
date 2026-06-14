package com.torneos.equipos.assembler;

import com.torneos.equipos.controller.EquipoController;
import com.torneos.equipos.dto.EquipoResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class EquipoModelAssembler implements RepresentationModelAssembler<EquipoResponseDTO, EntityModel<EquipoResponseDTO>> {

    @Override
    public EntityModel<EquipoResponseDTO> toModel(EquipoResponseDTO equipo) {
        return EntityModel.of(equipo,
                linkTo(methodOn(EquipoController.class).obtenerPorId(equipo.getEquipoId())).withSelfRel(),
                linkTo(methodOn(EquipoController.class).obtenerTodos()).withRel("todos-los-equipos"),
                linkTo(methodOn(EquipoController.class).obtenerActivos()).withRel("equipos-activos")
        );
    }
}
