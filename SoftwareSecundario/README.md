# Sistema Secundario - Proyecto Seguridad Software II

Sistema receptor de mensajes que se comunica con el Sistema Principal para recibir datos de forma segura.

## 📋 Requisitos Previos

- Java 21 o superior
- Maven 3.6+
- Sistema Principal ejecutándose en http://localhost:8080

## 🚀 Pasos para Ejecutar el Proyecto

### Opción 1: Usar Maven Wrapper (Recomendado)

```bash
# Navegar al directorio del proyecto
cd SoftwareSecundario

# Ejecutar la aplicación
./mvnw spring-boot:run
```

### Opción 2: Usar Maven Instalado

```bash
cd SoftwareSecundario
mvn spring-boot:run
```

### Opción 3: Compilar y ejecutar el JAR

```bash
cd SoftwareSecundario

# Compilar el proyecto
./mvnw clean package

# Ejecutar el JAR
java -jar target/SoftwareSecundario-0.0.1-SNAPSHOT.jar
```

La aplicación estará disponible en: **http://localhost:8081**

## 🔌 Endpoints Disponibles

### 1. Recibir Datos
**POST** `/receptos/recibir`

Recibe datos enviados desde el Sistema Principal.

**Request Body (JSON):**
```json
{
  "contenido": "Mensaje de prueba desde Sistema Principal"
}
```

**Respuestas:**
- `200 OK`: "Datos recibidos correctamente: [contenido]"
- `400 Bad Request`: Error si falta el contenido

**Ejemplo con cURL:**
```bash
curl -X POST http://localhost:8081/receptos/recibir \
  -H "Content-Type: application/json" \
  -d '{"contenido":"Mensaje de prueba"}'
```

---

## 🔐 Características

- ✅ Recepción de mensajes desde Sistema Principal
- ✅ Modelo seguro para comunicación (MensajeSeguro)
- ✅ API REST para recibir datos
- ✅ Pruebas unitarias incluidas

## 📊 Modelo de Datos

### MensajeSeguro

Estructura de datos para la comunicación segura entre sistemas:

```java
public class MensajeSeguro {
    private String contenido;      // Contenido del mensaje
    private LocalDateTime timestamp; // Marca de tiempo
    private String procedencia;     // Sistema que envía el mensaje
    // ... getters y setters
}
```

---

## 📁 Estructura del Proyecto

```
SoftwareSecundario/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── edu/uptc/co/SoftwareSecundario/
│   │   │       ├── controller/     # Controladores REST
│   │   │       ├── model/          # Entidades de datos
│   │   │       └── SoftwareSecundarioApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/             # Archivos estáticos
│   │       └── templates/          # Plantillas
│   └── test/
│       └── java/                   # Pruebas unitarias
├── pom.xml                         # Dependencias Maven
├── mvnw                            # Maven Wrapper
└── README.md                       # Este archivo
```

---

## 🔗 Comunicación con Sistema Principal

### Flujo de Comunicación

1. **Sistema Principal**: Envía datos a través de `POST /auth/test-envio`
2. **Sistema Secundario**: Recibe datos en `POST /receptos/recibir`
3. **Sistema Secundario**: Procesa y valida el mensaje
4. **Sistema Secundario**: Retorna confirmación

### URL de Comunicación

Desde Sistema Principal:
```
http://localhost:8081/receptos/recibir
```

---

## 🛠️ Dependencias Principales

- **Spring Boot 4.0.5**: Framework web
- **Spring Web**: Api REST
- **Jakarta Persistence**: API de persistencia

---

## 📝 Notas

- El puerto por defecto es **8081** (diferente del Sistema Principal para ejecutar ambos simultáneamente)
- Los mensajes se procesan inmediatamente al recibirlos
- Se incluyen validaciones básicas de entrada

## 🔧 Cambiar Puerto

Si necesitas cambiar el puerto, edita `application.properties`:

```properties
server.port=8082
```

---

## 📝 Resolución de Problemas

### Error: "Puerto 8081 en uso"
```bash
# Cambiar el puerto en application.properties
server.port=8082
```

### Error: "No se puede conectar a Sistema Principal"
Asegúrate de que:
1. Sistema Principal está ejecutándose en `http://localhost:8080`
2. La URL de comunicación es correcta en la configuración
3. No hay firewall bloqueando la conexión

### Maven Build falla
```bash
./mvnw clean install
```

---

## 📧 Documentación Relacionada

- Ver [README.md del Sistema Principal](../SistemaPrincipal/README.md) para más detalles del proyecto completo
- Documentación oficial: [Spring Boot](https://spring.io/projects/spring-boot)
