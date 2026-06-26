# Patient Service - Microservicio de Gestión de Pacientes

## 📋 Descripción
Microservicio responsable de la gestión de información de pacientes en el sistema RedSalud.

## 🛠️ Requisitos
- JDK 17+
- Maven 3.8+
- Eureka Server corriendo en puerto 8761

## 📥 Instalación

```bash
cd patient-service
./mvnw clean install
```

## ▶️ Ejecución

```bash
./mvnw spring-boot:run
```

El servicio se registrará automáticamente en Eureka Server (http://localhost:8761).

## 🔌 Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| **GET** | `/api/patients` | Obtener todos los pacientes |
| **GET** | `/api/patients/{id}` | Obtener paciente por ID |
| **POST** | `/api/patients` | Crear nuevo paciente |
| **PUT** | `/api/patients/{id}` | Actualizar paciente |
| **DELETE** | `/api/patients/{id}` | Eliminar paciente |

### Ejemplos de uso:

**GET - Listar pacientes**
```bash
curl http://localhost:8090/api/patients
```

**POST - Crear paciente**
```bash
curl -X POST http://localhost:8090/api/patients \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Juan",
    "lastName": "Pérez",
    "rut": "12345678-9"
  }'
```

**PUT - Actualizar paciente**
```bash
curl -X PUT http://localhost:8090/api/patients/1 \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Juan Actualizado"
  }'
```

**DELETE - Eliminar paciente**
```bash
curl -X DELETE http://localhost:8090/api/patients/1
```

## ✅ Pruebas

### Ejecutar tests
```bash
./mvnw clean test
```

### Generar reporte de cobertura JaCoCo
```bash
./mvnw test jacoco:report
```

### Ver reporte
```bash
# En navegador:
open target/site/jacoco/index.html

# O en Linux:
firefox target/site/jacoco/index.html
```

**Cobertura objetivo**: >= 60%

## 📁 Estructura

```
patient-service/
├── src/
│   ├── main/
│   │   ├── java/com/redsalud/patientservice/
│   │   │   ├── controller/PatientController.java
│   │   │   ├── service/PatientService.java
│   │   │   ├── repository/PatientRepository.java
│   │   │   ├── model/Patient.java
│   │   │   └── PatientServiceApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       ├── java/com/redsalud/patientservice/
│       │   └── PatientServiceTest.java (12 casos de prueba)
│       └── resources/
│           └── application-test.properties
└── pom.xml
```

## 🗄️ Base de Datos

- **Motor**: H2 en memoria
- **URL**: `jdbc:h2:mem:patientdb`
- **Aislamiento**: Base de datos independiente por microservicio

## 🔗 Integración

Este servicio se comunica con:
- **Eureka Server**: Para registro y descubrimiento de servicios
- **API Gateway**: Punto de entrada único (puerto 8090)
- **Request Service**: Puede consultar información de pacientes

## 📊 Cobertura de Pruebas

| Clase | Cobertura |
|-------|-----------|
| PatientController | 90%+ |
| PatientService | 95%+ |
| PatientRepository | 100% |

**Total**: >= 70%

## 🐛 Troubleshooting

### El servicio no aparece en Eureka
1. Verifica que Eureka Server está corriendo: `http://localhost:8761`
2. Revisa los logs para errores de conexión
3. Verifica `eureka.client.service-url.defaultZone` en application.properties

### Tests fallan
```bash
# Limpia y ejecuta nuevamente
./mvnw clean test -DskipTests=false
```

### Puerto 8081 en uso
```bash
# Cambia el puerto en application.properties
server.port=8082
```

## 👥 Autor
Equipo RedSalud - 2026
