# Examen Final - Microservicios (ms-auth, ms-productos, ms-ordenes)

## 📋 Descripción

Proyecto de microservicios desarrollado en Java 17 y Spring Boot 3.3.5 que simula una plataforma de autenticación, gestión de productos y órdenes, con seguridad basada en JWT, validación entre servicios, y arquitectura desacoplada.

Cada microservicio corre independientemente, se comunica vía HTTP usando OpenFeign, y comparte una única base de datos PostgreSQL.

---

## 🏗 Arquitectura General

# Examen Final - Microservicios (ms-auth, ms-productos, ms-ordenes)

## 📋 Descripción

Proyecto de microservicios desarrollado en Java 17 y Spring Boot 3.3.5 que simula una plataforma de autenticación, gestión de productos y órdenes, con seguridad basada en JWT, validación entre servicios, y arquitectura desacoplada.

Cada microservicio corre independientemente, se comunica vía HTTP usando OpenFeign, y comparte una única base de datos PostgreSQL.

---

## 🏗 Arquitectura General

- **ms-auth**: Gestión de usuarios y autenticación JWT.
- **ms-productos**: CRUD de productos, protegido por roles.
- **ms-ordenes**: Crear y listar órdenes, consumiendo ms-auth y ms-productos para validar.

**Infraestructura adicional:**
- Base de datos PostgreSQL compartida
- JWT para autenticación segura
- OpenFeign para comunicación entre microservicios
- Spring Security por roles
- SonarCloud y JaCoCo para calidad de código

---

## 🚀 Tecnologías Usadas

- Java 17
- Spring Boot 3.3.5
- Spring Security
- Spring Data JPA
- OpenFeign
- PostgreSQL
- Lombok
- JUnit 5 + Mockito
- Maven
- Jacoco
- SonarCloud

---

## 🛠 Configuración Inicial

### 1. Base de Datos

Crear base de datos:

```sql
CREATE DATABASE exam_final;
CREATE USER admin WITH PASSWORD '12345';
GRANT ALL PRIVILEGES ON DATABASE exam_final TO admin;

## 🛢 Datos de Base de Datos (inserción SQL)

### Roles

```sql
INSERT INTO roles (id, nombre) VALUES (1, 'SUPERADMIN');
INSERT INTO roles (id, nombre) VALUES (2, 'ADMIN');
INSERT INTO roles (id, nombre) VALUES (3, 'USUARIO');

Usuarios (se recomienda cambiar contraseña con el endpoint de actualizar)
INSERT INTO auth_usuarios (id, nombre, email, password, rol_id)
VALUES (1, 'Super Admin', 'superadmin@example.com', '$2a$10$encryptedpassword', 1);

INSERT INTO auth_usuarios (id, nombre, email, password, rol_id)
VALUES (2, 'Administrador', 'admin@example.com', '$2a$10$encryptedpassword', 2);

INSERT INTO auth_usuarios (id, nombre, email, password, rol_id)
VALUES (3, 'Usuario Normal', 'usuario@example.com', '$2a$10$encryptedpassword', 3);

Productos
INSERT INTO productos (id, nombre, precio, categoria)
VALUES (1, 'Espada de Fuego', 1200.0, 'Armas Legendarias');

INSERT INTO productos (id, nombre, precio, categoria)
VALUES (2, 'Escudo de Hielo', 800.0, 'Armaduras');

Órdenes 
INSERT INTO ordenes (id, usuario_id, productos_ids, fecha)
VALUES (1, 3, ARRAY[1,2], NOW());


##CONFIGURACION VAULT
### configurar las siguiente propiedades para ms-auth, ms-productos, ms-ordenes (Con los datos de conexion de DB local)
spring.datasource.url=jdbc:postgresql://localhost:5433/exam_final
spring.datasource.username=admin
spring.datasource.password=12345
spring.datasource.password=12345

para ms-auth
jwt.secret=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9