# API Gateway BFF — RedSalud

Componente central de la arquitectura. Actúa como Backend For Frontend (BFF): recibe todas las peticiones del frontend y las enruta al microservicio correspondiente.

## Tecnologías
- Spring Boot 3.3.5
- Spring Cloud Gateway (WebFlux)
- Resilience4j (Circuit Breaker)
- Spring Cloud Netflix Eureka Client
- SpringDoc OpenAPI (Swagger UI unificado)

## Puerto
`8090`

## Rutas configuradas

| Ruta                   | Microservicio destino     | Puerto |
|------------------------|--------------------------|--------|
| `/api/patients/**`     | patient-service          | 8081   |
| `/api/requests/**`     | request-service          | 8082   |
| `/api/waiting-list/**` | waiting-list-service     | 8083   |

## Circuit Breaker
El servicio `request-service` está protegido con Circuit Breaker. Si falla más del 50% de las llamadas en una ventana de 10 peticiones, el circuito se abre y redirige al endpoint `/fallback/requests` que retorna una respuesta segura vacía.

## Swagger UI unificado
Acceder a `http://localhost:8090/swagger-ui.html` para ver la documentación de los 3 microservicios desde un solo lugar.

## Requisitos previos
- Java 17+
- Maven 3.8+
- Eureka Server corriendo en `http://localhost:8761`

## Instalación y ejecución
```bash
cd api-gateway-bff
./mvnw spring-boot:run        # Linux/Mac
mvnw.cmd spring-boot:run      # Windows
```

## Verificar funcionamiento
- Health: `http://localhost:8090/actuator/health`
- Métricas: `http://localhost:8090/actuator/metrics`
- Swagger: `http://localhost:8090/swagger-ui.html`
