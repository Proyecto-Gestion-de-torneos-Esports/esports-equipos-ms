package com.torneos.equipos.config;

import com.torneos.equipos.model.Equipo;
import com.torneos.equipos.repository.EquipoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final EquipoRepository equipoRepository;

    @Override
    public void run(String... args) {
        if (equipoRepository.count() > 0) {
            log.info("La base de datos ya tiene {} registros. Omitiendo carga inicial.", equipoRepository.count());
            return;
        }

        log.info("Base de datos vacía. Cargando 20 equipos de respaldo...");

        // 2. Insertamos los datos de Mockaroo (id en null para que Oracle use la secuencia)
        equipoRepository.saveAll(List.of(
                new Equipo(null, "Lajo", "EU", 14, LocalDate.of(2025, 7, 21), "calbinson7@whitehouse.gov", false,new ArrayList<>()),
                new Equipo(null, "Gabspot", "LATAM", 90, LocalDate.of(2025, 11, 23), "lderoberto8@icq.com", false,new ArrayList<>()),
                new Equipo(null, "Shuffletag", "EU", 73, LocalDate.of(2026, 3, 20), "bschulter9@columbia.edu", true,new ArrayList<>()),
                new Equipo(null, "Avavee", "NA", 37, LocalDate.of(2025, 10, 9), "jminneya@parallels.com", false,new ArrayList<>()),
                new Equipo(null, "Browsetype", "NA", 100, LocalDate.of(2025, 9, 11), "emurleyb@miitbeian.gov.cn", true,new ArrayList<>()),
                new Equipo(null, "Quatz", "NA", 85, LocalDate.of(2025, 11, 12), "hminchellac@tinypic.com", false,new ArrayList<>()),
                new Equipo(null, "Blogtags", "LATAM", 88, LocalDate.of(2025, 10, 24), "ralbond@shop-pro.jp", false,new ArrayList<>()),
                new Equipo(null, "Eabox", "BR", 46, LocalDate.of(2025, 10, 16), "cskelhornee@loc.gov", false,new ArrayList<>()),
                new Equipo(null, "Tazz", "NA", 29, LocalDate.of(2025, 5, 11), "avalentinef@indiegogo.com", true,new ArrayList<>()),
                new Equipo(null, "Tanoodle", "BR", 47, LocalDate.of(2025, 9, 9), "spoundfordg@phoca.cz", true,new ArrayList<>()),
                new Equipo(null, "Teklist", "BR", 7, LocalDate.of(2026, 1, 27), "apiolah@chronoengine.com", true,new ArrayList<>()),
                new Equipo(null, "Devify", "EU", 44, LocalDate.of(2026, 4, 4), "kwillbournei@samsung.com", true,new ArrayList<>()),
                new Equipo(null, "Avamm", "NA", 67, LocalDate.of(2025, 8, 15), "rmannaghj@upenn.edu", false,new ArrayList<>()),
                new Equipo(null, "Midel", "NA", 51, LocalDate.of(2025, 6, 19), "ddesimoni0@toplist.cz", false,new ArrayList<>()),
                new Equipo(null, "Snaptags", "BR", 34, LocalDate.of(2025, 8, 18), "atotaro1@archive.org", true,new ArrayList<>()),
                new Equipo(null, "Yambee", "NA", 97, LocalDate.of(2025, 10, 9), "strinbey2@spiegel.de", true,new ArrayList<>()),
                new Equipo(null, "Bubbletube", "BR", 79, LocalDate.of(2025, 7, 19), "amartygin3@furl.net", true,new ArrayList<>()),
                new Equipo(null, "Dynabox", "NA", 20, LocalDate.of(2026, 1, 27), "tbaynton4@nbcnews.com", true, new ArrayList<>()),
                new Equipo(null, "Twinder", "BR", 42, LocalDate.of(2025, 7, 21), "dansett5@godaddy.com", true,new ArrayList<>()),
                new Equipo(null, "Livetube", "BR", 36, LocalDate.of(2025, 8, 29), "jgault6@wordpress.org", false,new ArrayList<>())
        ));

        log.info("Carga de respaldo completada. {} equipos insertados.", equipoRepository.count());
    }
}
