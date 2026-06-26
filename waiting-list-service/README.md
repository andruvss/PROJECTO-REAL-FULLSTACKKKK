# Waiting List Service - Microservicio de Cola de Espera

## Descripción
Microservicio para gestionar la cola de espera de pacientes.

## Ejecución
```bash
cd waiting-list-service
./mvnw spring-boot:run
```

## Endpoints (Swagger: http://localhost:8090/swagger-ui.html)

| Método | Ruta |
|--------|------|
| GET | `/api/waiting-lists` |
| GET | `/api/waiting-lists/{id}` |
| POST | `/api/waiting-lists` |
| PUT | `/api/waiting-lists/{id}` |
| DELETE | `/api/waiting-lists/{id}` |

## Pruebas y Cobertura
```bash
./mvnw clean test jacoco:report
# Ver: target/site/jacoco/index.html
```

**Cobertura**: ~70% (10 tests)
