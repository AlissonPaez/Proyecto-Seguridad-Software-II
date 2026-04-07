# Sistema Principal - Gestión de Autenticación e Integridad

Sistema de gestión de usuarios con autenticación de dos factores (2FA) y comunicación segura entre aplicaciones.

## Descripción

El Sistema Principal actúa como emisor de datos críticos. Gestiona la autenticación de usuarios, genera códigos TOTP para 2FA y envía mensajes con integridad garantizada al Sistema Secundario.

## Arquitectura

```
Sistema Principal (Puerto 8080)
├── ControladorUsuario (@RestController)
├── ServicioUsuario (@Service)
├── ServicioIntegridad (@Service)
├── ServicioComunicacion (@Service)
├── RepositorioUsuario (JPA)
└── Base de Datos H2 (En memoria)
```

## Características Principales

- Gestión completa de usuarios con persistencia en H2
- Autenticación de dos factores con Google Authenticator
- Generación de hashes SHA-256 para integridad de mensajes
- Comunicación segura con Sistema Secundario vía RestTemplate
- Protección de datos sensibles en respuestas JSON

## Inicio Rápido

### Requisitos
- Java 21 o superior
- Maven 3.6+

### Ejecutar

```bash
cd SistemaPrincipal
./mvnw spring-boot:run
```

Disponible en: http://localhost:8080

## Endpoints

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/auth/registrar` | Registrar nuevo usuario |
| POST | `/auth/login` | Validar credenciales con 2FA |
| GET | `/auth/activar-mfa` | Generar secreto para 2FA |
| GET | `/auth/test-envio` | Enviar mensaje de prueba al Sistema Secundario |

## Detalles Técnicos

### Gestión de Autenticación

#### Registro de Usuario
```java
@PostMapping("/auth/registrar")
public String registrar(@RequestBody Usuario usuario)
```

- Valida unicidad del nombre de usuario
- Persiste en base de datos H2 usando JPA
- Retorna mensaje de confirmación

#### Login con 2FA
```java
@PostMapping("/auth/login")
public String login(@RequestBody LoginRequest request)
```

Flujo de autenticación:
1. Valida credenciales (usuario/contraseña)
2. Verifica código TOTP de 6 dígitos
3. Si válido, envía mensaje seguro al Sistema Secundario

Request Body:
```json
{
  "nombre": "juan123",
  "clave": "miContraseña123",
  "codigo": 123456
}
```

### Autenticación de Dos Factores (2FA)

#### Generación de Secreto
```java
public String habilitar2FA(String nombre)
```

- Usa librería `google-authenticator` para generar secreto
- Persiste secreto en campo `secreto2fa` de la entidad Usuario
- Establece `mfaHabilitado = true`

#### Verificación de Código TOTP
```java
public boolean verificarCodigo2FA(String nombre, int codigo)
```

- Recupera secreto de base de datos
- Valida código usando `GoogleAuthenticator.authorize()`
- Soporta ventana de tiempo de 30 segundos

### Integridad de Datos

#### Servicio de Integridad
```java
@Service
public class ServicioIntegridad {
    public String generarHash(String mensaje) {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(mensaje.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hashBytes);
    }
}
```

- Genera hash SHA-256 del contenido del mensaje
- Codifica en Base64 para transmisión segura
- Usado antes de enviar datos al Sistema Secundario

#### Comunicación Segura
```java
@Service
public class ServicioComunicacion {
    public String enviarDatos(String texto) {
        MensajeSeguro mensajeSeguro = new MensajeSeguro();
        mensajeSeguro.setContenido(texto);
        String hash = servicioIntegridad.generarHash(texto);
        mensajeSeguro.setHash(hash);
        return restTemplate.postForObject(url, mensajeSeguro, String.class);
    }
}
```

- Crea objeto `MensajeSeguro` con contenido y hash
- Envía POST a `http://localhost:8081/receptor/recibir`
- Usa `RestTemplate` inyectado desde `DemoApplication`

### Modelo de Datos

#### Usuario
```java
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nombreUsuario;

    @Column(nullable = false)
    private String contraseña;

    private String secreto2fa;
    private boolean mfaHabilitado = false;
}
```

- Persistencia con JPA/Hibernate
- Campo `secreto2fa` para almacenar secreto de Google Authenticator
- Campo `mfaHabilitado` para controlar requerimiento de 2FA

#### MensajeSeguro
```java
public class MensajeSeguro {
    private String contenido;
    private String hash;
    // getters y setters
}
```

- Contenedor para mensaje y su hash de integridad
- Transmitido como JSON en comunicación inter-sistemas

### Protección de Datos Sensibles

Los campos sensibles están protegidos en respuestas JSON:

```java
public class Usuario {
    // ...

    @JsonIgnore
    private String contraseña;

    @JsonIgnore
    private String secreto2fa;
}
```

Esto previene la exposición accidental de contraseñas y secretos 2FA.

## Base de Datos H2

### Configuración
```properties
# application.properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
```

### Acceso a Consola
- URL: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:testdb
- Usuario: sa
- Contraseña: (vacío)

### Queries Útiles
```sql
-- Ver todos los usuarios
SELECT * FROM usuarios;

-- Ver estructura de tabla
DESCRIBE usuarios;
```

## Dependencias

```xml
<!-- pom.xml -->
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>com.warrenstrange</groupId>
        <artifactId>google-authenticator</artifactId>
        <version>1.05</version>
    </dependency>
</dependencies>
```

## Flujo de Uso

1. **Registro**: Usuario se registra con nombre y contraseña
2. **Activación 2FA**: Usuario solicita activación de MFA y recibe secreto
3. **Configuración App**: Usuario configura Google Authenticator con el secreto
4. **Login**: Usuario envía POST con nombre, contraseña y código TOTP
5. **Validación**: Sistema valida credenciales y 2FA
6. **Envío Seguro**: Si válido, genera hash y envía mensaje al Sistema Secundario
7. **Confirmación**: Recibe respuesta de integridad verificada

## Configuración Personalizada

### Cambiar Puerto
```properties
server.port=9090
```

### Configuración 2FA
```java
// En ServicioUsuario constructor
GoogleAuthenticatorConfig config = new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder()
    .setTimeStepSizeInMillis(30000) // 30 segundos
    .setWindowSize(3) // Tolerancia de 3 periodos
    .build();
```

## Pruebas

```bash
cd SistemaPrincipal
./mvnw test
```

## Notas de Seguridad

- Contraseñas almacenadas en texto plano (para fines educativos)
- Comunicación HTTP sin encriptación (para fines educativos)
- Para producción: implementar BCrypt para contraseñas y HTTPS

## Información del Proyecto

- Parte de: Proyecto de Seguridad en Software II
- Rol: Emisor de datos con autenticación
- Puerto: 8080
- Framework: Spring Boot 4.0.5
  {
    "id": 1,
    "nombreUsuario": "juan123",
    "contraseña": "miContraseña123",
    "secreto2fa": null,
    "mfaHabilitado": false
  },
  {
    "id": 2,
    "nombreUsuario": "maria456",
    "contraseña": "otraContraseña",
    "secreto2fa": "JBSWY3DPEBLW64TMMQ======",
    "mfaHabilitado": true
  }
]
```

**Ejemplo con cURL:**
```bash
curl http://localhost:8080/auth/usuarios
```

---

### 4. Prueba de Envío
**GET** `/auth/test-envio`

Envía un mensaje de prueba desde el Sistema Principal al Sistema Secundario.

**Respuesta:**
```
Mensaje enviado exitosamente: Mensaje de prueba desde Sistema Principal
```

**Ejemplo con cURL:**
```bash
curl http://localhost:8080/auth/test-envio
```

---

## 🔐 Características

- ✅ Registro de nuevos usuarios
- ✅ Validación de credenciales
- ✅ Autenticación de dos factores (2FA) - En desarrollo
- ✅ Comunicación con Sistema Secundario
- ✅ Consola H2 para acceso a la base de datos

## 💾 Acceso a la Base de Datos

### Consola H2

Accede a: **http://localhost:8080/h2-console**

**Credenciales:**
- **JDBC URL:** `jdbc:h2:mem:testdb`
- **Usuario:** `sa`
- **Contraseña:** (dejar en blanco)

**Query para ver todos los usuarios:**
```sql
SELECT * FROM USUARIOS;
```

---

## 📁 Estructura del Proyecto

```
SistemaPrincipal/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── edu/uptc/software/
│   │   │       ├── controller/     # Controladores REST
│   │   │       ├── model/          # Entidades de datos
│   │   │       ├── repositorio/    # Acceso a datos (JPA)
│   │   │       ├── servicio/       # Lógica de negocios
│   │   │       └── DemoApplication.java  # Clase principal
│   │   └── resources/
│   │       └── application.properties    # Configuración
│   └── test/
│       └── java/                   # Pruebas unitarias
├── pom.xml                         # Dependencias Maven
├── mvnw                            # Maven Wrapper
└── README.md                       # Este archivo
```

---

## 🛠️ Dependencias Principales

- **Spring Boot 4.0.5**: Framework web
- **Spring Data JPA**: ORM para base de datos
- **Spring Security**: Seguridad y autenticación
- **H2 Database**: Base de datos en memoria
- **Jakarta Persistence**: API de persistencia

---

## 📝 Notas de Seguridad

⚠️ **IMPORTANTE**: Este proyecto está en fase de desarrollo educativo. Para producción:
- Encriptar contraseñas usando BCryptPasswordEncoder
- Implementar HTTPS/SSL
- Validar y sanitizar todas las entradas
- Implementar rate limiting en endpoints
- Usar variables de entorno para configuración sensible

---

## 🔧 Resolución de Problemas

### Error: "Puerto 8080 en uso"
```bash
# Cambiar el puerto en application.properties
server.port=8081
```

### Error: "No se puede conectar a H2"
Asegúrate de que `spring.h2.console.enabled=true` esté en `application.properties`

### Maven Build falla
```bash
./mvnw clean install
```

---
