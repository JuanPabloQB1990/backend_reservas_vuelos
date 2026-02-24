# ✈️ Backend - Sistema de Reservas de Vuelos

API REST desarrollada con Java y Spring Boot para gestionar reservas de vuelos.
Este proyecto simula el backend de una plataforma de viajes donde los usuarios pueden consultar vuelos, registrar reservas y administrar información relacionada.

El objetivo del proyecto es practicar el desarrollo de APIs escalables, organización en capas y manejo de bases de datos en aplicaciones backend.

## 🚀 Tecnologías utilizadas:

✔️ Java
✔️ Spring Boot
✔️ Spring Data JPA
✔️ Maven
✔️ MySQL
✔️ REST API
✔️ Postman (testing)

🧠 Características principales:

✔️ Gestión de vuelos
✔️ Creación de reservas
✔️ Consulta de información de vuelos disponibles
✔️ API REST estructurada
✔️ Arquitectura en capas (Controller, Service, Repository)
✔️ Conexión con base de datos relacional

Las APIs REST permiten que diferentes sistemas se comuniquen mediante solicitudes HTTP estructuradas y son una práctica común en el desarrollo backend moderno.

⚙️ Instalación y ejecución

## 1. Clonar el repositorio

```js
git clone https://github.com/JuanPabloQB1990/backend_reservas_vuelos.git
```
```js
cd backend_reservas_vuelos
```

## 2. Configurar base de datos

Crear una base de datos en MySQL llamada:
```js
reservas
```

## 3. Configura las variables a tu gusto te doy 2 opciones: 

### 1️⃣ Desde la configuración de ejecución en IntelliJ (la más común)

Abre tu proyecto en IntelliJ IDEA

Ve a la parte superior derecha y haz clic en tu configuración de ejecución (donde aparece el nombre de la app).

Clic en Edit Configurations…

Selecciona tu aplicación de Spring Boot

Busca la sección Environment variables

Haz clic en el ícono ...

Agrega tus variables de entorno separadas por ; asi:

```js
DB_USER=xxxxxxx;DB_PASSWORD=xxxxxxxxxxxxxx;SERVER_PORT=8090;SECRET_KEY=**********************************
```

### 2️⃣ Configurar el archivo: application.properties con tus variables definidas anteriormente.

Ejemplo:

```js
server.port=8080
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
spring.datasource.url=jdbc:mysql://localhost:3306/reservas
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
springdoc.api-docs.path=/api-docs
SECRET_KEY=${SECRET_KEY}
```
## 5. Ejecutar la aplicación en la consola con este comando o en Intellij IDEA.

```js
mvn spring-boot:run
```

Servidor disponible en: http://localhost:8080/api

Servidor Swagger en: http://localhost:8080/swagger-ui/index.html#/
