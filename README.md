# Proyecto de Seguridad en Software II

Proyecto integrado que demuestra arquitectura de microservicios con autenticación segura y comunicación entre sistemas.

## 📋 Descripción General

Este proyecto contiene **dos sistemas independientes** que se comunican entre sí:

1. **Sistema Principal** - Gestión de usuarios, autenticación y orquestación
2. **Sistema Secundario** - Receptor de mensajes y procesamiento

## 🏗️ Arquitectura

```
┌─────────────────────────────────────────────────────────────┐
│                                                             │
│  ┌──────────────────────┐        ┌──────────────────────┐  │
│  │ SISTEMA PRINCIPAL    │        │ SISTEMA SECUNDARIO   │  │
│  │  (Puerto 8080)       │        │  (Puerto 8081)       │  │
│  │                      │        │                      │  │
│  │ ✓ Gestión usuarios   │        │ ✓ Recepción datos    │  │
│  │ ✓ Autenticación      ├───────→│ ✓ Procesamiento      │  │
│  │ ✓ BD Usuarios        │ HTTP   │ ✓ Validación        │  │
│  │ ✓ 2FA                │        │                      │  │
│  └──────────────────────┘        └──────────────────────┘  │
│                                                             │
│  Base de datos: H2 Database (En memoria)                   │
│  Framework: Spring Boot 4.0.5                              │
│  Java: 21 LTS                                              │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

## 📁 Estructura del Proyecto

```
Proyecto-Seguridad-Software-II/
├── SistemaPrincipal/           # Sistema de gestión de usuarios
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/edu/uptc/software/
│   │   │   │   ├── controller/
│   │   │   │   ├── model/
│   │   │   │   ├── repositorio/
│   │   │   │   ├── servicio/
│   │   │   │   └── DemoApplication.java
│   │   │   └── resources/
│   │   └── test/
│   ├── pom.xml
│   ├── mvnw
│   ├── mvnw.cmd
│   └── README.md               # Documentación específica
│
├── SoftwareSecundario/         # Sistema receptor
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/edu/uptc/co/SoftwareSecundario/
│   │   │   │   ├── controller/
│   │   │   │   ├── model/
│   │   │   │   └── SoftwareSecundarioApplication.java
│   │   │   └── resources/
│   │   └── test/
│   ├── pom.xml
│   ├── mvnw
│   ├── mvnw.cmd
│   └── README.md               # Documentación específica
│
└── README.md                   # Este archivo
```

## 🚀 Inicio Rápido

### Requisitos
- **Java 21** o superior
- **Maven 3.6+**
- **Git**

### Paso 1: Clonar/Descargar el Proyecto

```bash
# Si está versión controlada
git clone <repositorio>
cd Proyecto-Seguridad-Software-II
```

### Paso 2: Ejecutar Sistema Principal

```bash
cd SistemaPrincipal
./mvnw spring-boot:run
```

Disponible en: **http://localhost:8080**

### Paso 3: Ejecutar Sistema Secundario (en otra terminal)

```bash
cd SoftwareSecundario
./mvnw spring-boot:run
```

Disponible en: **http://localhost:8081**

## 🔌 Endpoints Clave

### Sistema Principal

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/auth/registrar` | Registrar nuevo usuario |
| GET | `/auth/login` | Validar credenciales |
| GET | `/auth/usuarios` | Listar todos los usuarios |
| GET | `/auth/test-envio` | Enviar mensaje al Sistema Secundario |

### Sistema Secundario

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/receptos/recibir` | Recibir datos del Sistema Principal |

## 📚 Documentación Detallada

- **[Sistema Principal](SistemaPrincipal/README.md)** - Endpoints, guía de uso y características
- **[Sistema Secundario](SoftwareSecundario/README.md)** - Endpoints, guía de uso y características

## 💾 Acceso a Bases de Datos

### Consola H2 - Sistema Principal

```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
Usuario: sa
Contraseña: (vacío)
```

**Query para ver usuarios:**
```sql
SELECT * FROM USUARIOS;
```

## 🔒 Características de Seguridad (En Desarrollo)

- ✅ Autenticación de usuario/contraseña
- 🔄 Autenticación de dos factores (2FA) - En desarrollo
- ✅ Validación de credenciales
- 🔄 Encriptación de contraseñas (Pendiente)
- 🔄 HTTPS/SSL (Pendiente)
- 🔄 Rate limiting (Pendiente)
- ✅ Modelo seguro para comunicación entre sistemas

## 🛠️ Stack Tecnológico

| Tecnología | Versión | Propósito |
|-----------|---------|----------|
| Java | 21 LTS | Lenguaje principal |
| Spring Boot | 4.0.5 | Framework web |
| Spring Data JPA | - | ORM |
| Spring Security | - | Seguridad |
| H2 Database | - | BD en memoria |
| Maven | 3.6+ | Gestor de dependencias |
| JUnit | - | Testing |

## 📋 Dependencias Principales

```xml
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- spring-boot-starter-security
- h2 (database)
- spring-boot-h2console
```

## 🧪 Pruebas

### Sistema Principal

```bash
cd SistemaPrincipal
./mvnw test
```

### Sistema Secundario

```bash
cd SoftwareSecundario
./mvnw test
```

## 📊 Diagrama de Flujo de Autenticación

```
1. Usuario → POST /auth/registrar
             ↓
2. Sistema Principal: Valida usuario único
             ↓
3. Registra en BD
             ↓
4. Responde: "Usuario registrado exitosamente"

---

1. Usuario → GET /auth/login?nombre=x&clave=y
             ↓
2. Sistema Principal: Busca usuario en BD
             ↓
3. Valida contraseña
             ↓
4. Retorna mensaje de bienvenida o error
```

## 🔧 Configuración Personalizada

### Cambiar Puerto del Sistema Principal

Editar `SistemaPrincipal/src/main/resources/application.properties`:
```properties
server.port=9090
```

### Cambiar Puerto del Sistema Secundario

Editar `SoftwareSecundario/src/main/resources/application.properties`:
```properties
server.port=9091
```

### Log Level

Agregar a `application.properties`:
```properties
logging.level.root=INFO
logging.level.edu.uptc.software=DEBUG
```

## 🚨 Resolución de Problemas

### Los proyectos no se compilan
```bash
# Limpiar y recompilar
./mvnw clean install
```

### Puerto ya en uso
Cambiar el puerto en `application.properties` o cerrar la aplicación que lo está usando:
```bash
# En Windows (PowerShell)
Get-Process -Id (Get-NetTCPConnection -LocalPort 8080).OwningProcess
```

### No se puede conectar entre sistemas
1. Verifica que ambos servicios estén ejecutándose
2. Comprueba las URLs de comunicación
3. Verifica que no haya firewall bloqueando

### H2 Console no carga
Asegúrate que en `application.properties` esté:
```properties
spring.h2.console.enabled=true
```

## 📝 Notas de Seguridad ⚠️

Este es un **proyecto educativo**. Para producción:

- ❌ NO guardes contraseñas en texto plano - Usa BCrypt
- ❌ NO expongas endpoints sin autenticación
- ❌ NO uses HTTP - Implementa HTTPS/SSL
- ❌ NO publiques secretos en Git - Usa variables de entorno
- ✅ Valida TODAS las entradas del usuario
- ✅ Implementa logging y monitoreo
- ✅ Usa CORS apropiadamente
- ✅ Implementa rate limiting

## 📚 Recursos Adicionales

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [H2 Database](https://www.h2database.com/)
- [Apache Maven](https://maven.apache.org/)

## 👨‍💼 Información del Proyecto

- **Asignatura:** Ingeniería de Software II
- **Semestre:** 9
- **Tema:** Seguridad en Software
- **Institución:** UPTC

## 📞 Contacto y Soporte

Para preguntas o problemas, consulta:
1. La documentación en los README específicos de cada sistema
2. Los comentarios en el código fuente
3. Tu profesor o asistente del curso

---

**Última actualización:** Abril 2026
**Estado:** En desarrollo 🚧
