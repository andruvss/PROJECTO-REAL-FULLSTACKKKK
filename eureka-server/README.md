# Eureka Server — RedSalud

Servidor de Service Discovery. Actúa como registro central donde todos los microservicios se registran al iniciar, permitiendo que se descubran entre sí sin necesidad de conocer IPs o puertos fijos.

## Tecnologías
- Spring Boot 3.3.5
- Spring Cloud Netflix Eureka Server

## Puerto
`8761`

## Función en la arquitectura
Implementa el patrón **Self-Registration**: cada microservicio se registra automáticamente en Eureka al arrancar, informando su nombre, IP y puerto. El API Gateway y los otros servicios consultan Eureka para saber dónde está cada servicio.

## Orden de arranque
**Eureka Server debe ser el primero en levantarse.** El resto de los servicios dependen de él para registrarse.

## Requisitos previos
- Java 17+
- Maven 3.8+

## Instalación y ejecución
```bash
cd eureka-server
./mvnw spring-boot:run        # Linux/Mac
mvnw.cmd spring-boot:run      # Windows
```

## Verificar funcionamiento
- Dashboard Eureka: `http://localhost:8761`
- En el dashboard se pueden ver todos los microservicios registrados con su estado (`UP` / `DOWN`)

## Configuración importante
```properties
eureka.client.register-with-eureka=false   # El server no se registra a sí mismo
eureka.client.fetch-registry=false          # No necesita consultar el registro
eureka.server.enable-self-preservation=false # Desactivado para desarrollo local
```
