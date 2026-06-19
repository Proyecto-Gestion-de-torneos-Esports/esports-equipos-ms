package com.torneos.equipos.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenApi(){

        final String securitySchemeName = "bearerAuth";
        return new OpenAPI().info(
                new Info()
                        .title("API de Gestión de Equipos - Gestión de Torneos Esports")
                        .version("2.0")
                        .description("Documentación de los endpoints para el microservicio de gestión de Equipos " +
                                "Permite la gestion completa de equipos, editar, dar de baja, inscribrir integrantes, etc.")
        )
        .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .description("TOKEN JWT")
                        )
                );
    }
}
