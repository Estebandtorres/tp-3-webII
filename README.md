# TP1: Catálogo de Productos y Favoritos

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

## TP-2

## Paso 4: Puertos y Adaptadores - Análisis de la Migración a JPA

### 1. Comparación antes y después de la migración
Al realizar la migración del almacenamiento en memoria (TP1) a una base de datos relacional PostgreSQL con JPA (TP2), las siguientes capas se mantuvieron **intactas**:
*   `FavoritoService`
*   `FavoritoController`
*   DTOs (`FavoritoRequestDto`, `FavoritoResponseDto`) y el `FavoritoMapper`

**¿Qué cambió en estas clases?** Absolutamente nada en cuanto a su lógica o estructura por la migración a base de datos. *(Nota: El único cambio mínimo fue ajustar el tipo de dato del `id` de `Long` a `Integer` para que coincidiera con el tipo `SERIAL` de la base de datos, pero la lógica de las clases permaneció idéntica).*

### 2. Clases modificadas vs. Clases que quedaron iguales

**Clases que quedaron exactamente iguales (El Core):**
*   **Dominio:** `Favorito` (Modelo).
*   **Puerto:** `FavoritoRepository` (La interfaz).
*   **Casos de uso/Web:** `FavoritoService`, `FavoritoController`, DTOs.

**Nuevas clases creadas para la migración (La Infraestructura):**
*   `FavoritoEntity`: Representación de la tabla de la base de datos.
*   `FavoritoJpaRepository`: Interfaz de Spring Data JPA.
*   `FavoritoRepositoryAdapter`: El nuevo adaptador que conecta nuestro dominio con JPA.
*   *Archivos SQL de Flyway para crear el esquema.*

**Clases eliminadas:**
*   `InMemoryFavoritoRepository` (o la implementación anterior basada en `ConcurrentHashMap`).

### 3. ¿Por qué fue posible hacer este cambio sin tocar el servicio o el controlador?
Esto fue posible gracias al patrón de **Puertos y Adaptadores**. 

En nuestro diseño, la interfaz `FavoritoRepository` actúa como un **Puerto** (un contrato). Nuestro `FavoritoService` solo conoce este contrato; no sabe, ni le importa, cómo o dónde se guardan los datos. 

En el TP1, conectamos a este puerto un adaptador que guardaba los datos en la memoria (`InMemoryFavoritoRepository`). En el TP2, simplemente desconectamos ese adaptador y enchufamos uno nuevo (`FavoritoRepositoryAdapter` que usa JPA). 

Como ambos adaptadores respetan el mismo contrato (implementan la misma interfaz), el resto de la aplicación ni siquiera se entera de que la base de datos cambió. Esto demuestra un alto nivel de desacoplamiento y facilita el mantenimiento y la evolución del software.

### ¿Por qué se usa una nueva migración (V4) en lugar de editar V1, V2 o V3?
Las migraciones en Flyway son inmutables porque el motor guarda un *checksum* de cada script ejecutado. Si modificamos un script anterior, la validación falla y rompemos la aplicación en cualquier entorno (o para cualquier otro desarrollador) que ya haya aplicado la versión original. Las bases de datos siempre deben evolucionar "hacia adelante".

## Configuración y Ejecución del Proyecto (PostgreSQL + Flyway)

Este proyecto utiliza PostgreSQL como motor de base de datos relacional y Flyway para el control de versiones del esquema de datos (migraciones).

### 1. Levantar la base de datos PostgreSQL

Puedes levantar la base de datos de dos maneras:

**Opción A: Usando Docker (Recomendado)**
Si tienes Docker instalado, ejecuta el siguiente comando en tu terminal para levantar un contenedor con PostgreSQL:
\`\`\`bash
docker run --name tp2-postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=root -e POSTGRES_DB=tp2_db -p 5432:5432 -d postgres:15
\`\`\`
*(Asegúrate de que las credenciales coincidan con las de tu archivo `application.properties`).*

**Opción B: Instalación Local**
1. Instala PostgreSQL en tu máquina.
2. Abre pgAdmin o tu cliente SQL preferido.
3. Crea una base de datos llamada `tp2_db`.
4. Verifica que el usuario y contraseña en `src/main/resources/application.properties` coincidan con tu instalación local.

### 2. Confirmar que las migraciones de Flyway corrieron

Al ejecutar la aplicación Spring Boot (desde el IDE o con `./mvnw spring-boot:run`), Flyway se ejecutará automáticamente antes de que la aplicación esté disponible.

Para confirmar que las migraciones se aplicaron correctamente:
1. **Revisar la consola de Spring Boot:** Al iniciar, busca líneas en los logs que digan algo como `Flyway Community Edition` y `Successfully applied 4 migrations to schema "public"`.
2. **Revisar la base de datos:** Conéctate a tu base de datos `tp2_db` usando un cliente SQL (como pgAdmin o DBeaver) y busca una tabla especial llamada `flyway_schema_history`. Si le haces un `SELECT *`, verás un registro histórico detallado de los scripts `V1`, `V2`, `V3` y `V4` con el estado "Success".

## Arquitectura de Seguridad y Autorización

La lógica de autorización sobre los recursos (verificar que una lista le pertenezca al usuario autenticado o que este tenga el rol `ADMIN`) se encuentra centralizada en la capa **Service** (dentro de `ListaService`, mediante el método `obtenerYValidarAcceso`).

Se eligió implementarla en el Service y no en el Controller por las siguientes razones:

* **Separación de responsabilidades:** El Controller tiene como única función manejar la capa web (interceptar la petición HTTP, extraer el token/identidad y formatear la respuesta). Las reglas sobre quién puede ver o modificar un dato son lógica de negocio estricta y pertenecen al Service.
* **Reusabilidad y consistencia:** Al encapsular la validación en el servicio, cualquier otro método o endpoint futuro que necesite operar sobre una lista pasará obligatoriamente por el mismo control de acceso, evitando duplicar código (`if/else`) en múltiples métodos del controlador.
* **Manejo centralizado de excepciones:** El servicio lanza una excepción nativa de Spring Security (`AccessDeniedException`), permitiendo que el controlador simplemente la capture de forma limpia para devolver un `403 Forbidden` sin ensuciarse con validaciones manuales.