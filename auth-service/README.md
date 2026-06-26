# Auth Service — RedSalud

Microservicio encargado de la autenticación de usuarios y generación de tokens JWT.

## Tecnologías
- Spring Boot 3.3.5
- Spring Security (BCrypt)
- JJWT (JSON Web Tokens)
- H2 Database (en memoria)
- Spring Cloud Netflix Eureka Client

## Puerto
`8084`

## Endpoints

| Método | Ruta                   | Descripción                         |
|--------|------------------------|-------------------------------------|
| POST   | `/api/auth/register`   | Registra un nuevo usuario           |
| POST   | `/api/auth/login`      | Autentica y retorna un token JWT    |

## Cómo funciona el JWT
1. El usuario envía sus credenciales a `/api/auth/login`
2. El servicio verifica la contraseña con BCrypt
3. Si es válida, genera un token JWT firmado con HS256
4. El token tiene validez de 24 horas (`jwt.expiration=86400000` ms)
5. El frontend incluye el token en el header `Authorization: Bearer <token>` en cada petición

## Requisitos previos
- Java 17+
- Maven 3.8+
- Eureka Server corriendo en `http://localhost:8761`

## Instalación y ejecución
```bash
cd auth-service
./mvnw spring-boot:run        # Linux/Mac
mvnw.cmd spring-boot:run      # Windows
```

## Verificar funcionamiento
- Health: `http://localhost:8084/actuator/health`
- Consola H2: `http://localhost:8084/h2-console`

## Ejemplo de login
```json
POST http://localhost:8084/api/auth/login
{
  "username": "admin",
  "password": "1234"
}
```
Respuesta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```
