# Challenge-alura
Este es un aplicacion que funciona como biblioteca virtual utilizando la API de Gutendex, codeada con Spring Boot los procesa mediante Jackson y los manda a una base de datos

#Funcionalidades

Busqueda de libros por el titulo
Almacanen de datos ya sea el titulo del libro o el nombre del autor
Expositor de datos ya sea el idioma del libro o las descargas que tiene este

#Tecnologias Utilizadas
Spring Boot 3.x / 4.x (Spring Data JPA)
PostgreSQL (Base de Datos Relacional)
Jackson (Manipulación de JSON)

#Configuracion del proyecto

Base de Datos: Crea una base de datos llamada literatura en tu servidor PostgreSQL.
Properties: Configura tu archivo src/main/resources/application.properties:
Properties
spring.datasource.url=jdbc:postgresql://localhost:5432/literatura
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update
