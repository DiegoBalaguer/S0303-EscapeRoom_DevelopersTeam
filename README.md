# Virtual Escape Room - Nivel 3  
**Autores**: Diego Balaguer y Pablo Gómez San Joaquín

Bienvenido al proyecto **Virtual Escape Room - Nivel 3**, una aplicación desarrollada para facilitar la **administración de negocios de Escape Rooms**. Este sistema permite gestionar de manera eficiente todos los aspectos relacionados con la operación de un Escape Room, desde la creación de salas temáticas e inventarios hasta la venta de tickets y la gestión de usuarios. Además, la solución incorpora bases de datos como **H2**, **MongoDB** y **SQL** para la persistencia de datos, así como patrones de diseño avanzados que aseguran una arquitectura sólida y escalable.

##  Resumen del Proyecto
Este proyecto simula la gestión de un Escape Room, permitiendo a los usuarios administrar todo lo relacionado a la lógica de negocio correspondiente al rubro, como gestión de usuarios, salas, inventarios, ventas, finanzas, etc. El desarrollo incluye persistencia de datos a través de H2, con soporte también para bases de datos SQL y MongoDB.

---

## ️ Tecnologías Usadas / Technologies Used
- **Java 21** (compilado con Maven)
- **H2 Database** (como base de datos en memoria)
- Implementación opcional: **MongoDB** y **MySQL**
- **Lombok** (para reducir código repetitivo)
- **JUnit** y **Mockito** (para pruebas unitarias)
- **SLF4J** y **Logback** (para manejo de logs)

---

##  Patrones de Diseño / Design Patterns
✅ **Singleton**  
✅ **Abstract Factory**  
✅ **Observer**  
✅ **DAO (Data Access Object)**  

Los patrones de diseño están aplicados en diferentes áreas:
1. **Singleton**: Este patrón se utiliza en varias clases de nuestro programa y desempeñó un papel fundamental para garantizar el correcto funcionamiento del sistema. Su uso permitió mantener instancias únicas en algunos componentes clave, evitando problemas de redundancia y uso excesivo de recursos. Durante el proceso de desarrollo, cuando probamos eliminarlo, el sistema dejó de funcionar correctamente debido a problemas de memoria, demostrando así la importancia de su implementación.

2. **Abstract Factory**: Se utiliza para la creación de las conexiones a las diferentes bases de datos del sistema. 
También para crear objetos relacionados como pistas y decoraciones, según el tema de las salas (e.g., Egipto, Espacio, Gánsteres).

3. **Observer**: Facilita las notificaciones (e.g., creación de salas, entrega de certificados y recompensas).

4. **DAO**: Para la abstracción de operaciones específicas de la base de datos.

---

##  Entidades Clave / Core Entities
- **Room**: Salas temáticas con niveles de dificultad, pistas y decoraciones.
- **Clue (Pista)**: Representa objetos con temas específicos que ayudan a resolver problemas.
- **Decoration (Decoración)**: Decoraciones con tipos de materiales para ambientación.
- **Player (Jugador)**: Usuarios registrados, con soporte a suscripciones y compra de tickets.
- **Ticket**: Representa el acceso de jugadores al Escape Room.
- **Certificate**: Certificados otorgados por completar con éxito las salas.

---

##  Funcionalidades / Features
✅ Crear una nueva sala de Escape Room.  
✅ Agregar pistas y decoraciones personalizadas.  
✅ Implementar niveles de dificultad por sala.  
✅ Mostrar el inventario completo con su valor total.  
✅ Generar certificados de logros para jugadores.  
✅ Notificar la creación de salas (rooms).  
✅ Notificar el recibimiento de certificados y recompensas.  

---

##  Pruebas Unitarias / Unit Testing
Se realizaron pruebas unitarias cubriendo las siguientes clases críticas del proyecto:
1. **`AbstractEscapeRoomTest`**: Validación de la creación de salas, pistas y decoraciones utilizando el patrón Factory, así como el manejo de errores al enviar parámetros inválidos.
2. **`PlayerDAOH2ImplTest`**: Operaciones relacionadas con la persistencia de jugadores en H2, incluyendo métodos `create`, `findById`, `findAll`, `update` y `deleteById`, con uso de *Mockito* para simular conexiones a la base de datos.
3. **`RoomDAOH2ImplTest`**: Gestión de las operaciones de persistencia relacionadas a las salas del escape room implementadas en H2.

---

##  Integración con Bases de Datos / Database Integration
### H2 (Por defecto):  
- Uso para pruebas como database en archivo local.
- Nos permitió trabajar a todo el equipo con los mismos datos al poder subirla en el repositorio.

### MongoDB:  
- Uso opcional para guardar `id`, `nombre` y `dirección` del EscapeRoom.

### MySQL:
- Integración para un sistema SQL relacional más robusto.

---

## ▶️ Primeros Pasos / Getting Started
1. Asegúrate de tener instalado **Java 21** y un IDE como IntelliJ IDEA.  
2. Configura la base de datos MongoDB o H2 mediante el archivo `LoadConfigDB`.  
3. Clona el proyecto:  
   ```bash
   git clone https://github.com/DiegoBalaguer/S0303-EscapeRoom_DevelopersTeam.git  
   cd S0303-EscapeRoom_DevelopersTeam  
   ```
4. Compila y ejecuta el proyecto desde la clase `Main`.

---

## Configuración de Propiedades
La aplicación utiliza dos ficheros de configuración de propiedades para su funcionamiento:
1. **Archivo de propiedades generales**: Incluye configuraciones como el título y los mensajes que se utilizan en el programa.
2. **Archivo de propiedades para conexiones a bases de datos**: Define la información necesaria para conectarse a las bases de datos, como URL, usuario y contraseña.

Ambos archivos se cargan de manera genérica desde la carpeta **resources** del proyecto. No obstante, existe la posibilidad de indicar una ubicación diferente para el archivo de propiedades generales pasándolo como parámetro durante la ejecución del programa. Esto permite una mayor flexibilidad y personalización para distintos entornos.

---

## Validaciones y Diseño de Views
El diseño de las *views* mantiene las entradas de datos específicas dentro de cada vista individual. Esto asegura que las comprobaciones necesarias para los valores ingresados se realicen dentro del *controller*. Este comportamiento permitiría, en caso de futuras expansiones, generar una clase de **servicio dedicada para centralizar las validaciones** y reducir la lógica en los controladores.

---

##  Notificaciones / Notifications
- **Al crear una nueva sala (room)**.  
- **Por el recibimiento de certificados y recompensas**.

---

##  Mejoras Futuras / Future Improvements
- Implementar GUI con JavaFX o una API REST con Spring Boot.  
- Autenticación de usuarios para una experiencia personalizada.  
- Colaboración multijugador en tiempo real.  

---

##  Autores / Authors
- **Diego Balaguer**  
- **Pablo Gómez San Joaquín**  
