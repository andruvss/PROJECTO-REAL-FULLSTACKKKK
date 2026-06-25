# Request Service - Microservicio de Gestión de Solicitudes

## Descripción
Microservicio para gestionar solicitudes de atención médica.

## Ejecución
```bash
cd request-service
./mvnw spring-boot:run
```

## Endpoints (Swagger: http://localhost:8090/swagger-ui.html)

| Método | Ruta |
|--------|------|
| GET | `/api/requests` |
| GET | `/api/requests/{id}` |
| POST | `/api/requests` |
| PUT | `/api/requests/{id}` |
| DELETE | `/api/requests/{id}` |

## Pruebas y Cobertura
```bash
./mvnw clean test jacoco:report
# Ver: target/site/jacoco/index.html
```

**Cobertura**: ~70% (10 tests)
