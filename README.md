# Screen Match

Screen Match es una aplicación web para gestionar y visualizar información sobre películas y series, utilizando la **OMDb API** para obtener datos externos. Está desarrollada en **Java** con **Spring Boot** y utiliza **PostgreSQL** como base de datos. Se usa **Hibernate** para la persistencia y **Insomnia** para probar las diferentes rutas del API.

## Características

- Búsqueda de películas y series.
- Consulta de detalles (sinopsis, actores, género, año de lanzamiento, etc.).
- Almacenamiento de películas y series favoritas.
- Gestión de listas personalizadas.
- API REST para operaciones CRUD (Crear, Leer, Actualizar y Eliminar).

## Tecnologías Utilizadas

- **Java 17**: Lenguaje de programación.
- **Spring Boot 3**: Framework para la creación de aplicaciones basadas en Java.
- **PostgreSQL**: Base de datos relacional.
- **Hibernate**: ORM para la interacción con la base de datos.
- **OMDb API**: API externa para obtener datos de películas y series.
- **Insomnia**: Cliente API para pruebas y desarrollo de la API REST.
- **Maven**: Herramienta de gestión de dependencias y construcción.

## Instalación y Configuración

### Prerrequisitos

Asegúrate de tener instalados los siguientes programas:

- [Java 17](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
- [PostgreSQL](https://www.postgresql.org/download/)
- [Maven](https://maven.apache.org/download.cgi)
- [Insomnia](https://insomnia.rest/download)

### Configuración del Proyecto

1. Clona este repositorio en tu máquina local:
   ```bash
   git clone https://github.com/LiaS1818/screenmatch-backend.git
   cd screen-match
