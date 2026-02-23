✈️ Backend - Sistema de Reservas de Vuelos

API REST desarrollada con Java y Spring Boot para gestionar reservas de vuelos.
Este proyecto simula el backend de una plataforma de viajes donde los usuarios pueden consultar vuelos, registrar reservas y administrar información relacionada.

El objetivo del proyecto es practicar el desarrollo de APIs escalables, organización en capas y manejo de bases de datos en aplicaciones backend.

🚀 Tecnologías utilizadas

Java

Spring Boot

Spring Data JPA

Maven

MySQL

REST API

Postman (testing)

🧠 Características principales

Gestión de vuelos

Creación de reservas

Consulta de información de vuelos disponibles

API REST estructurada

Arquitectura en capas (Controller, Service, Repository)

Conexión con base de datos relacional

Las APIs REST permiten que diferentes sistemas se comuniquen mediante solicitudes HTTP estructuradas y son una práctica común en el desarrollo backend moderno.

⚙️ Instalación y ejecución
1. Clonar el repositorio
git clone https://github.com/JuanPabloQB1990/backend_reservas_vuelos.git
cd backend_reservas_vuelos
2. Configurar base de datos

Crear una base de datos en MySQL, por ejemplo:

reservas_vuelos

Configurar el archivo:

application.properties

Ejemplo:

spring.datasource.url=jdbc:mysql://localhost:3306/reservas_vuelos
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
3. Ejecutar la aplicación
mvn spring-boot:run

Servidor disponible en:

http://localhost:8080
