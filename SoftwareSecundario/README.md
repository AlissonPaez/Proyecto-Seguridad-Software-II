# Sistema Secundario - Verificación de Integridad

Sistema receptor de mensajes que valida la integridad de los datos recibidos del Sistema Principal.

## Descripción

El Sistema Secundario actúa como receptor de datos críticos. No confía ciegamente en la información que recibe; en su lugar, verifica activamente la integridad de cada mensaje mediante el recálculo de hashes SHA-256.

## Arquitectura

```
Sistema Secundario (Puerto 8081)
├── ControladorReceptor (@RestController)
├── ServicioIntegridad (@Service)
├── Modelo MensajeSeguro
└── Logs de verificación de integridad
```

## Características Principales

- Recepción de mensajes con verificación de integridad
- Recálculo independiente de hashes SHA-256
- Detección de alteraciones en mensajes transmitidos
- Logging detallado de verificaciones de integridad
- Respuestas claras sobre el estado de los mensajes

## Inicio Rápido

### Requisitos
- Java 21 o superior
- Maven 3.6+
- Sistema Principal ejecutándose en http://localhost:8080

### Ejecutar

```bash
cd SoftwareSecundario
./mvnw spring-boot:run
```

Disponible en: http://localhost:8081

## Endpoints

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/receptor/recibir` | Recibir y verificar mensaje del Sistema Principal |

## Verificación de Integridad

### Lógica de Recepción

```java
@PostMapping("/recibir")
public String recibirMensaje(@RequestBody MensajeSeguro mensaje) {
    // Recalculamos el hash del contenido recibido
    String hashCalculado = servicioIntegridad.generarHash(mensaje.getContenido());

    // Comparamos el hash que llegó con el que acabamos de calcular
    if (hashCalculado.equals(mensaje.getHash())) {
        System.out.println("INTEGRIDAD OK: El mensaje no ha sido alterado");
        return "Mensaje verificado";
    } else {
        System.out.println("ALERTA: Mensaje alterado detectado");
        return "ERROR: Integridad comprometida";
    }
}
```

### Servicio de Integridad

```java
@Service
public class ServicioIntegridad {
    public String generarHash(String mensaje) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(mensaje.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al calcular SHA-256: " + e.getMessage());
        }
    }
}
```

- Utiliza el mismo algoritmo SHA-256 que el Sistema Principal
- Codificación Base64 consistente
- Manejo de excepciones para algoritmos no disponibles

### Modelo de Datos

#### MensajeSeguro

```java
public class MensajeSeguro {
    private String contenido;
    private String hash;

    // Getters y setters
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
    public String getHash() { return hash; }
    public void setHash(String hash) { this.hash = hash; }
}
```

- `contenido`: El mensaje transmitido
- `hash`: Firma digital SHA-256 en Base64 del contenido

### Proceso de Verificación

1. **Recepción**: Recibe POST con objeto `MensajeSeguro`
2. **Recálculo**: Genera hash SHA-256 del campo `contenido`
3. **Comparación**: Compara hash calculado con `mensaje.getHash()`
4. **Respuesta**: Retorna resultado de verificación
5. **Logging**: Imprime mensaje en consola según resultado

### Ejemplos de Comunicación

#### Mensaje Válido
Request:
```json
{
  "contenido": "Acceso exitoso para: juan123",
  "hash": "aGVsbG93b3JsZA=="
}
```

Response: `"Mensaje verificado"`

Console: `INTEGRIDAD OK: El mensaje no ha sido alterado`

#### Mensaje Alterado
Request:
```json
{
  "contenido": "Acceso exitoso para: juan123",
  "hash": "aGVsbG93b3JsZA=="
}
```

Si el contenido fue alterado durante transmisión, Response: `"ERROR: Integridad comprometida"`

Console: `ALERTA: Mensaje alterado detectado`

## Configuración

### Puerto
```properties
# application.properties
server.port=8081
spring.application.name=SoftwareSecundario
```

### Dependencias

```xml
<!-- pom.xml -->
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

## Pruebas

```bash
cd SoftwareSecundario
./mvnw test
```

## Logs de Verificación

El sistema imprime mensajes detallados en la consola:

- **Verificación exitosa**: `INTEGRIDAD OK: El mensaje no ha sido alterado`
- **Verificación fallida**: `ALERTA: Mensaje alterado detectado`

Estos logs permiten monitorear la integridad de la comunicación en tiempo real.

## Flujo de Comunicación

1. **Sistema Principal** valida credenciales y 2FA
2. **Sistema Principal** genera hash SHA-256 del mensaje de éxito
3. **Sistema Principal** envía POST con `MensajeSeguro` a `/receptor/recibir`
4. **Sistema Secundario** recibe el mensaje
5. **Sistema Secundario** recalcula hash del contenido
6. **Sistema Secundario** compara hashes
7. **Sistema Secundario** responde con resultado de verificación
8. **Sistema Secundario** registra resultado en logs

## Seguridad

- Verificación independiente de integridad
- No confía en el emisor; valida cada mensaje
- Detección de manipulaciones durante transmisión
- Logging de todas las verificaciones para auditoría

## Información del Proyecto

- Parte de: Proyecto de Seguridad en Software II
- Rol: Receptor de datos con verificación de integridad
- Puerto: 8081
- Framework: Spring Boot 4.0.5
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
