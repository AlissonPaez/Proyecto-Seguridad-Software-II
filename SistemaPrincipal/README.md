# Proyecto-Seguridad-Software-II - Sistema Principal

Sistema de gestión de usuarios con autenticación y comunicación segura entre aplicaciones.

## 📋 Requisitos Previos

- Java 21 o superior
- Maven 3.6+
- Git

## 🚀 Pasos para Ejecutar el Proyecto

### Opción 1: Usar Maven Wrapper (Recomendado)

```bash
# Navegar al directorio del proyecto
cd SistemaPrincipal

# Ejecutar la aplicación
./mvnw spring-boot:run
```

### Opción 2: Usar Maven Instalado

```bash
cd SistemaPrincipal
mvn spring-boot:run
```

### Opción 3: Compilar y ejecutar el JAR

```bash
cd SistemaPrincipal

# Compilar el proyecto
./mvnw clean package

# Ejecutar el JAR
java -jar target/Proyecto-Seguridad-Software-II-0.0.1-SNAPSHOT.jar
```

La aplicación estará disponible en: **http://localhost:8080**

## 🔌 Endpoints Disponibles

### 1. Registrar Usuario
**POST** `/auth/registrar`

Crea un nuevo usuario en el sistema.

**Request Body (JSON):**
```json
{
  "nombreUsuario": "juan123",
  "contraseña": "miContraseña123",
  "mfaHabilitado": false
}
```

**Respuestas:**
- `200 OK`: "Usuario registrado exitosamente."
- `400 Bad Request`: "Error: El nombre de usuario ya existe."

**Ejemplo con cURL:**
```bash
curl -X POST http://localhost:8080/auth/registrar \
  -H "Content-Type: application/json" \
  -d '{"nombreUsuario":"juan123","contraseña":"miContraseña123","mfaHabilitado":false}'
```

---

### 2. Iniciar Sesión (Login)
**GET** `/auth/login`

Valida las credenciales del usuario.

**Parámetros de Query:**
- `nombre` (string): Nombre de usuario
- `clave` (string): Contraseña

**Respuestas:**
- `200 OK`: "¡Bienvenido, [nombre]! Ha ingresado al sistema."
- `401 Unauthorized`: "Error: Usuario o contraseña incorrectos."

**Ejemplo con cURL:**
```bash
curl "http://localhost:8080/auth/login?nombre=juan123&clave=miContraseña123"
```

---

### 3. Listar Todos los Usuarios
**GET** `/auth/usuarios`

Obtiene la lista completa de usuarios registrados en el sistema.

**Respuesta (JSON):**
```json
[
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

## 📧 Soporte

Para preguntas o problemas, consulta con tu profesor o revisa la documentación oficial de Spring Boot.