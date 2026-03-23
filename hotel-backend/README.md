# Hotel Backend

> API REST para sistema de reservas de hotel con autenticación JWT, pagos con Stripe y notificaciones por email.

---

## Tecnologías

- Java 21
- Spring Boot 4
- Spring Security 7 + JWT + Refresh Tokens
- PostgreSQL (AWS RDS)
- Hibernate / JPA
- Stripe (pagos)
- Cloudinary (imágenes)
- Resend (emails)
- Quartz Scheduler (@Scheduled)
- Maven
- Lombok

---

## Características

- Registro e inicio de sesión con JWT
- Refresh Tokens con expiración de 7 días
- Roles: ROLE_USER y ROLE_ADMIN
- CRUD completo de habitaciones con fotos en Cloudinary
- Tipos de habitación: SIMPLE, DOBLE, SUITE, FAMILIAR
- Amenidades configurables por habitación
- Crear, cancelar y listar reservas
- Verificación de disponibilidad por fechas (sin traslapes)
- Pagos con Stripe (PaymentIntent + confirmación + reembolso)
- Email de confirmación al crear una reserva
- Tarea automática que cancela reservas PENDIENTE sin pago después de 15 minutos
- Panel admin para gestionar reservas y habitaciones
- Subir foto de perfil de usuario
- Manejo global de excepciones con formato JSON consistente
- CORS configurado para frontend React

---

## Instalación local

**Requisitos:**
- Java 21
- Maven
- PostgreSQL corriendo en localhost:5432

**Pasos:**

```bash
# 1. Clonar el repositorio
git clone https://github.com/Chiripi0rca/hotel-backend.git
cd hotel-backend

# 2. Crear la base de datos
psql -U postgres -c "CREATE DATABASE hotel_backend;"

# 3. Configurar application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/hotel_backend
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update

jwt.secret=${JWT_SECRET}
jwt.expiration=86400000
jwt.refresh-expiration=604800000

cloudinary.cloud-name=${CLOUDINARY_CLOUD_NAME}
cloudinary.api-key=${CLOUDINARY_API_KEY}
cloudinary.api-secret=${CLOUDINARY_API_SECRET}

stripe.secret-key=${STRIPE_SECRET_KEY}
stripe.public-key=${STRIPE_PUBLIC_KEY}

spring.mail.host=${MAIL_HOST}
spring.mail.port=${MAIL_PORT}
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.ssl.enable=true

# 4. Correr el proyecto
./mvnw spring-boot:run
```

El servidor arranca en `http://localhost:8080`

---

## Endpoints

### Auth — público

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | /api/auth/register | Registrar usuario |
| POST | /api/auth/login | Iniciar sesión |
| POST | /api/auth/refresh | Renovar access token |
| POST | /api/auth/logout | Cerrar sesión |

### Habitaciones

| Método | Endpoint | Auth | Descripción |
|--------|----------|------|-------------|
| GET | /api/room/all | No | Listar todas las habitaciones |
| GET | /api/room/{id} | No | Ver detalle de habitación |
| POST | /api/room/create | ADMIN | Crear habitación |
| PUT | /api/room/update/{id} | ADMIN | Actualizar habitación |
| POST | /api/room/{id}/fotos | ADMIN | Subir foto a habitación |

### Reservas

| Método | Endpoint | Auth | Descripción |
|--------|----------|------|-------------|
| GET | /api/reserva/mis-reservas | USER | Listar mis reservas |
| POST | /api/reserva/create | USER | Crear reserva |
| PUT | /api/reserva/cancelar/{id} | USER | Cancelar reserva propia |
| GET | /api/reserva/all | ADMIN | Listar todas las reservas |
| PUT | /api/reserva/update/{id} | ADMIN | Cambiar estado de reserva |

### Pagos

| Método | Endpoint | Auth | Descripción |
|--------|----------|------|-------------|
| POST | /api/pago/crear/{reservaId} | USER | Crear PaymentIntent en Stripe |
| POST | /api/pago/confirmar | USER | Confirmar pago |
| POST | /api/pago/reembolso/{reservaId} | USER | Reembolsar pago |

### Usuario

| Método | Endpoint | Auth | Descripción |
|--------|----------|------|-------------|
| GET | /api/user/perfil | USER | Ver perfil |
| PUT | /api/user/perfil | USER | Actualizar perfil |
| POST | /api/user/perfil/foto | USER | Subir foto de perfil |

---

## Estructura del proyecto

```
src/main/java/com/hotel/hotel_backend/
├── config/
│   └── SecurityConfig.java
├── controller/
│   ├── AuthController.java
│   ├── RoomController.java
│   ├── ReservaController.java
│   ├── PagoController.java
│   └── UserController.java
├── DTO/
├── entity/
├── Exception/
├── repository/
├── security/
│   ├── JwtService.java
│   ├── JwtAuthFilter.java
│   └── UserDetailServiceImpl.java
└── service/
    ├── AuthService.java
    ├── RoomService.java
    ├── ReservaService.java
    ├── PagoService.java
    ├── UserService.java
    ├── EmailService.java
    ├── CloudinaryService.java
    ├── RefreshTokenService.java
    └── ScheduledService.java
```

---

## Autor

Ricardo Ramos Puga — [GitHub](https://github.com/Chiripi0rca)