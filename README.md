# TP1: Catálogo de Productos y Favoritos

**Autor:** Esteban David Torres  
**Institución:** UNVIME  

Este proyecto es una API RESTful desarrollada con Spring Boot que implementa una arquitectura en capas (Controller → Service → Repository). Consta de dos módulos principales:

1. **Catálogo de Productos:** Consume la API pública externa DummyJSON mediante `RestClient` y expone los datos bajo un contrato propio (DTO), filtrando únicamente la información relevante.
2. **Favoritos:** Un CRUD completo en memoria para gestionar los productos favoritos del usuario. No utiliza base de datos real (sin JPA), gestionando la persistencia temporalmente mediante estructuras de datos en memoria.

## Tecnologías Utilizadas
* Java 25+
* Spring Boot 4.1.x
* Maven
* Spring Web (Spring MVC + RestClient)
* Spring Boot Validation (Jakarta Bean Validation)
* Springdoc OpenAPI (Swagger UI) para documentación

## Instrucciones de Ejecución

1. Abrir una terminal en el directorio raíz del proyecto.
2. Ejecutar el siguiente comando para levantar la aplicación usando el Wrapper de Maven incluido:
   ```bash
   .\mvnw.cmd spring-boot:run
3. La aplicación se iniciará y estará disponible en el puerto 8080.

Una vez que la aplicación esté corriendo, tienes dos formas principales de interactuar con la API:

1. Interfaz de Swagger UI
Navega a la siguiente URL en tu explorador web para acceder a la documentación interactiva, donde podrás ver todos los endpoints documentados con @Operation y probarlos directamente:
👉 http://localhost:8080/swagger-ui.html

2. Archivos de Prueba (.http)
El repositorio incluye archivos en la carpeta de utilidades con extensiones .http (compatibles con la extensión REST Client de VS Code o IDEs similares) para probar automáticamente casos de éxito y de error.

Catálogo de Productos (/api/productos)

GET /api/productos - Devuelve la lista completa mapeada a nuestro DTO.

GET /api/productos/{id} - Devuelve un producto específico desde DummyJSON.

Favoritos (/api/favoritos)

POST /api/favoritos - Crea un nuevo favorito. Retorna 201 Created.

GET /api/favoritos - Lista todos los favoritos guardados.

GET /api/favoritos/{id} - Obtiene un favorito por su ID.

PUT /api/favoritos/{id} - Actualiza la nota o el producto de un favorito.

DELETE /api/favoritos/{id} - Elimina un favorito. Retorna 204 No Content.

Manejo Uniforme de Errores
El proyecto implementa un manejador centralizado de excepciones (@RestControllerAdvice) que captura errores globales y los devuelve bajo el estándar RFC 7807 (ProblemDetail).

Casos contemplados:

404 Not Found: Al buscar o intentar eliminar un ID de producto o favorito inexistente.

400 Bad Request: Al enviar datos inválidos (ej. un POST de favorito con campos vacíos o nulos).

502 Bad Gateway: Ante fallas de comunicación o timeout con la API externa DummyJSON.