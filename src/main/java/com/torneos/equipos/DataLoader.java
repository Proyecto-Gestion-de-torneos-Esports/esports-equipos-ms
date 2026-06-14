package com.torneos.equipos;

import com.torneos.equipos.model.Equipo;
import com.torneos.equipos.model.Integrantes;
import com.torneos.equipos.model.Rol;
import com.torneos.equipos.repository.EquipoRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    private EquipoRepository equipoRepository;

    @Override
    public void run(String... args) throws Exception {
        if (equipoRepository.count() > 0) {
            System.out.println("La base de datos ya tiene equipos. Omitiendo generación de datos...");
            return;
        }
        Faker faker = new Faker();
        Random random = new Random();
        String[] regiones = {"LATAM", "EU", "NA", "BR"};
        System.out.println("Iniciando DataLoader: Generando datos falsos de Equipos e Integrantes...");
        for (int i = 0; i < 15; i++) {
            Equipo equipo = new Equipo();
            // Generar un nombre de equipo estilo e-sports
            equipo.setNombre(faker.esports().team());
            equipo.setRegion(regiones[random.nextInt(regiones.length)]);
            java.util.Random rand = new java.util.Random();
            equipo.setFechaFundacion(java.time.LocalDate.now().minusDays(rand.nextInt(3650)));
            equipo.setCorreoContacto("contacto@" + equipo.getNombre().toLowerCase().replace(" ", "") + ".com");
            equipo.setActivo(random.nextInt(10) < 8);

            List<Integrantes> listaIntegrantes = new ArrayList<>();
            int cantidadIntegrantes = random.nextInt(4) + 3; // Entre 3 y 6 integrantes

            for (int j = 0; j < cantidadIntegrantes; j++) {
                Integrantes integrante = new Integrantes();
                integrante.setNombre(faker.esports().player());
                integrante.setUsuarioId((long) faker.number().numberBetween(1, 30));
                integrante.setRol(j == 0 ? Rol.COACH : Rol.JUGADOR);
                integrante.setEquipo(equipo);

                listaIntegrantes.add(integrante);
            }
            equipo.setListaIntegrantes(listaIntegrantes);
            equipoRepository.save(equipo);
        }
        System.out.println("¡DataLoader finalizado! 15 equipos generados con éxito.");
    }
}
