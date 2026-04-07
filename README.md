# Proyecto de Seguridad en Software II

Proyecto integrado que demuestra arquitectura de microservicios con autenticación segura y comunicación entre sistemas.

## Descripción General

Este proyecto contiene dos sistemas independientes que se comunican entre sí:

1. **Sistema Principal** - Gestión de usuarios, autenticación y orquestación
2. **Sistema Secundario** - Receptor de mensajes y procesamiento

## Arquitectura

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

## Estructura del Proyecto

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

## Inicio Rápido

### Requisitos
- Java 21 o superior
- Maven 3.6+
- Git

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

Disponible en: http://localhost:8080

### Paso 3: Ejecutar Sistema Secundario (en otra terminal)

```bash
cd SoftwareSecundario
./mvnw spring-boot:run
```

Disponible en: http://localhost:8081

## Endpoints Clave

### Sistema Principal

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/auth/registrar` | Registrar nuevo usuario |
| POST | `/auth/login` | Validar credenciales con 2FA |
| GET | `/auth/activar-mfa` | Generar secreto para 2FA |
| GET | `/auth/test-envio` | Enviar mensaje al Sistema Secundario |

### Sistema Secundario

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/receptor/recibir` | Recibir datos del Sistema Principal |

## Documentación Detallada

- [Sistema Principal](SistemaPrincipal/README.md) - Endpoints, guía de uso y características
- [Sistema Secundario](SoftwareSecundario/README.md) - Endpoints, guía de uso y características

## Acceso a Bases de Datos

### Consola H2 - Sistema Principal

```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
Usuario: sa
Contraseña: (vacío)
```

Query para ver usuarios:
```sql
SELECT * FROM USUARIOS;
```

## Características de Seguridad

- Autenticación de usuario/contraseña
- Autenticación de dos factores (2FA) con TOTP
- Validación de credenciales
- Modelo seguro para comunicación entre sistemas con integridad de datos

## Flujo de Seguridad

1. Usuario registra cuenta en Sistema Principal
2. Usuario activa 2FA y obtiene secreto para Google Authenticator
3. Usuario intenta login con POST /auth/login enviando JSON con nombre, clave y código TOTP
4. Sistema Principal valida credenciales y 2FA
5. Si válido, genera hash SHA-256 del mensaje de acceso
6. Envía mensaje seguro (contenido + hash) al Sistema Secundario
7. Sistema Secundario recalcula hash y verifica integridad
8. Responde confirmación o alerta de alteración

## Stack Tecnológico

| Tecnología | Versión | Propósito |
|-----------|---------|----------|
| Java | 21 LTS | Lenguaje principal |
| Spring Boot | 4.0.5 | Framework web |
| Spring Data JPA | - | ORM |
| Spring Security | - | Seguridad |
| H2 Database | - | BD en memoria |
| Maven | 3.6+ | Gestor de dependencias |
| JUnit | - | Testing |

## Dependencias Principales

```xml
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- spring-boot-starter-security
- h2
- spring-boot-h2console
- google-authenticator
```

## Pruebas

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

## Configuración Personalizada

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

## Resolución de Problemas

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

## Notas de Seguridad

Este es un proyecto educativo. Para producción:

- NO guardes contraseñas en texto plano - Usa BCrypt
- NO expongas endpoints sin autenticación
- NO uses HTTP - Implementa HTTPS/SSL
- NO publiques secretos en Git - Usa variables de entorno
- Valida TODAS las entradas del usuario
- Implementa logging y monitoreo
- Usa CORS apropiadamente
- Implementa rate limiting

## Recursos Adicionales

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [H2 Database](https://www.h2database.com/)
- [Apache Maven](https://maven.apache.org/)

## Información del Proyecto

- Asignatura: Ingeniería de Software II
- Semestre: 9
- Tema: Seguridad en Software
- Institución: UPTC


Última actualización: Abril 2026
Estado: En desarrollo
