# Esports Equipos Microservicio
## Tecnologias Utilizadas

* Oracle Cloud Database
* Java 21
* Maven
* Spring Data JPA
* Lombok
* Spring Web
* Oracle Driver
* Validation

## Tips para conectarse correctamente a la base de datos y correr el programa:
En propierties poner lo siguiente:
```
server.port = 8002

spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

spring.datasource.url=jdbc:oracle:thin:@esports_high?TNS_ADMIN=src/main/resources/wallet (esta en la ruta correcta ya que agregue la wallet al proyecto)
spring.datasource.username=MS_EQUIPOS
spring.datasource.password=(viene incluida en el archivo o pedirla)

spring.jpa.hibernate.ddl-auto=update


spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.OracleDialect
logging.level.org.hibernate=DEBUG
logging.level.com.zaxxer.hikari=DEBUG
```


## Url directas

| **GET** | `/api/equipos` | Listar todos los equipos (activos e inactivos) |
| **GET** | `/api/equipos/activos` | Listar solo los equipos con estado activo |
| **GET** | `/api/equipos/{id}` | Buscar un equipo específico por su ID |
| **POST** | `/api/equipos` | Crear un nuevo equipo |
| **PUT** | `/api/equipos/{id}` | Actualizar datos de un equipo existente |
| **DELETE** | `/api/equipos/{id}` | Baja lógica (eliminación) de un equipo |
| **GET** | `/api/equipos/top?top=5` | Ranking de equipos (por defecto 3 sin el ?top=5 solo el top) |
