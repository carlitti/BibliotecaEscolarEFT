# Biblioteca Escolar EFT

## Desarrollo Orientado a Objetos II

Proyecto correspondiente a la Evaluación Final Transversal de Desarrollo Orientado a Objetos II.

El sistema implementa una aplicación de escritorio para la gestión de una biblioteca escolar utilizando Java, Java Swing, MySQL y JDBC.

---

## Funcionalidades

El sistema permite gestionar:

- Autenticación de usuarios.
- Roles de bibliotecario y estudiante.
- Gestión de libros.
- Gestión de estudiantes.
- Préstamos de libros.
- Devoluciones.
- Control automático de stock.
- Fechas de vencimiento.
- Detección de atrasos.
- Reportes de biblioteca.

---

## Tecnologías utilizadas

- Java 17
- IntelliJ IDEA
- Maven
- Java Swing
- MySQL
- JDBC
- MySQL Connector/J
- Git
- GitHub

---

# Arquitectura

El proyecto utiliza los patrones:

- MVC
- DAO
- Singleton

La arquitectura general es:

```text
Vista
  |
  v
Controlador
  |
  v
DAO
  |
  v
DatabaseConnection
  |
  v
MySQL
```

---

# Estructura del proyecto

```text
BibliotecaEscolarEFT
│
├── sql
│   ├── Script_crea_tablas_biblioteca.sql
│   └── Script_poblado_tablas_biblioteca.sql
│
├── src
│   ├── controlador
│   │   ├── LoginController.java
│   │   ├── LibroController.java
│   │   ├── EstudianteController.java
│   │   ├── PrestamoController.java
│   │   └── ReporteController.java
│   │
│   ├── dao
│   │   ├── UsuarioDAO.java
│   │   ├── EstudianteDAO.java
│   │   ├── LibroDAO.java
│   │   ├── CategoriaDAO.java
│   │   └── PrestamoDAO.java
│   │
│   ├── interfaces
│   │   └── Prestable.java
│   │
│   ├── modelo
│   │   ├── Persona.java
│   │   ├── Usuario.java
│   │   ├── Estudiante.java
│   │   ├── Libro.java
│   │   ├── Categoria.java
│   │   └── Prestamo.java
│   │
│   ├── util
│   │   └── DatabaseConnection.java
│   │
│   ├── vista
│   │   ├── LoginView.java
│   │   ├── MenuPrincipalView.java
│   │   ├── LibrosView.java
│   │   ├── EstudiantesView.java
│   │   ├── PrestamosView.java
│   │   └── ReportesView.java
│   │
│   └── main
│       └── Main.java
│
└── pom.xml
```

---

# Programación Orientada a Objetos

El sistema aplica los principales conceptos de POO.

## Clase abstracta

Se utiliza:

```java
Persona
```

como clase abstracta común para distintos tipos de personas del sistema.

## Herencia

```text
Persona
   |
   +--- Usuario
   |
   +--- Estudiante
```

`Usuario` y `Estudiante` heredan los atributos y comportamientos comunes definidos en `Persona`.

## Polimorfismo

La clase `Persona` contiene el método abstracto:

```java
obtenerTipoPersona()
```

que es implementado de manera distinta por sus subclases.

## Interfaces

Se implementa la interfaz:

```java
Prestable
```

que define operaciones relacionadas con materiales que pueden ser prestados.

`Libro` implementa esta interfaz.

---

# Base de datos

La aplicación utiliza:

```text
biblioteca_escolar
```

La base contiene:

```text
usuarios
estudiantes
categorias
libros
prestamos
```

---

## Usuarios

Almacena:

- ID.
- Nombre.
- RUT.
- Correo.
- Contraseña.
- Rol.

Roles disponibles:

```text
BIBLIOTECARIO
ESTUDIANTE
```

---

## Estudiantes

Almacena:

- ID.
- Nombre.
- RUT.
- Curso.
- Correo.

---

## Categorías

Almacena las categorías utilizadas para clasificar los libros.

---

## Libros

Almacena:

- ID.
- Título.
- Autor.
- ISBN.
- Editorial.
- Stock.
- Categoría.

---

## Préstamos

Almacena:

- Estudiante.
- Libro.
- Fecha de préstamo.
- Fecha de vencimiento.
- Fecha de devolución.
- Estado de devolución.

---

# JDBC

La conexión con MySQL se realiza mediante JDBC utilizando:

```java
DriverManager
Connection
PreparedStatement
ResultSet
SQLException
```

El driver utilizado es MySQL Connector/J.

---

# Patrón Singleton

La conexión se administra mediante:

```java
DatabaseConnection
```

Esta clase implementa Singleton y entrega su instancia mediante:

```java
DatabaseConnection.getInstance()
```

De esta forma existe un punto centralizado para gestionar las conexiones a MySQL.

---

# Patrón DAO

Las operaciones de persistencia se encuentran separadas en clases DAO:

```text
UsuarioDAO
EstudianteDAO
LibroDAO
CategoriaDAO
PrestamoDAO
```

Los DAO se encargan exclusivamente de las operaciones relacionadas con la base de datos.

---

# Patrón MVC

La aplicación mantiene separadas las responsabilidades.

## Modelo

Representa los objetos de negocio:

```text
Usuario
Estudiante
Libro
Categoria
Prestamo
```

## Vista

Contiene las interfaces creadas mediante Java Swing.

## Controlador

Actúa como intermediario entre la interfaz y los DAO.

Ejemplo:

```text
LibrosView
    |
    v
LibroController
    |
    v
LibroDAO
    |
    v
MySQL
```

---

# Autenticación y roles

El sistema valida a los usuarios contra la base de datos.

## Bibliotecario

Tiene acceso a:

- Gestión de libros.
- Gestión de estudiantes.
- Préstamos y devoluciones.
- Reportes.

## Estudiante

Tiene acceso a:

- Préstamos y devoluciones.
- Consulta de sus préstamos.
- Reportes asociados a su cuenta.

Las opciones administrativas se ocultan para los estudiantes.

---

# CRUD

El sistema implementa CRUD completo para múltiples entidades.

## Libros

- Crear.
- Listar.
- Editar.
- Eliminar.

## Estudiantes

- Crear.
- Listar.
- Editar.
- Eliminar.

## Categorías

Las operaciones CRUD se encuentran implementadas mediante `CategoriaDAO`.

También existen operaciones de persistencia para usuarios y préstamos.

---

# Préstamos

Al registrar un préstamo el sistema:

1. Selecciona el estudiante.
2. Selecciona el libro.
3. Verifica que exista stock.
4. Registra la fecha de préstamo.
5. Calcula automáticamente la fecha de vencimiento a siete días.
6. Registra el préstamo.
7. Disminuye el stock del libro.

---

# Devoluciones

Al registrar una devolución:

1. Se selecciona un préstamo activo.
2. Se registra la fecha de devolución.
3. El préstamo queda marcado como devuelto.
4. El stock del libro aumenta automáticamente.

---

# Concurrencia

Los procesos de préstamo y devolución se ejecutan en hilos independientes.

Ejemplo:

```java
Thread hiloPrestamo = new Thread(...);
hiloPrestamo.start();
```

Esto permite realizar operaciones de base de datos sin bloquear la interfaz gráfica.

Las actualizaciones visuales posteriores se realizan mediante:

```java
SwingUtilities.invokeLater(...)
```

---

# Sincronización y consistencia

Las operaciones críticas utilizan sincronización:

```java
synchronized
```

Además, el registro de préstamos utiliza transacciones SQL.

El sistema ejecuta:

```sql
SELECT stock
FROM libros
WHERE id = ?
FOR UPDATE;
```

para bloquear temporalmente el registro del libro mientras se verifica y modifica su stock.

Si la operación falla se utiliza:

```text
ROLLBACK
```

y si termina correctamente:

```text
COMMIT
```

Esto ayuda a prevenir condiciones de carrera y stock inconsistente ante solicitudes simultáneas.

---

# Reportes

El sistema incluye los siguientes informes:

## Libros más prestados

Muestra cada libro y la cantidad de préstamos registrados.

## Historial por estudiante

Permite seleccionar un estudiante y visualizar:

- Libro.
- Fecha de préstamo.
- Fecha de vencimiento.
- Fecha de devolución.
- Estado.

## Libros actualmente en préstamo

Muestra los préstamos que todavía no han sido devueltos.

---

# Validaciones

El sistema valida:

- Campos obligatorios.
- Correos.
- Stock numérico.
- Stock no negativo.
- ISBN duplicados.
- RUT duplicados.
- Selección de estudiantes.
- Selección de libros.
- Disponibilidad antes de prestar.
- Préstamos ya devueltos.

Los resultados y errores se informan mediante:

```java
JOptionPane
```

---

# Instalación de la base de datos

Ejecutar primero:

```text
sql/Script_crea_tablas_biblioteca.sql
```

Después ejecutar:

```text
sql/Script_poblado_tablas_biblioteca.sql
```

---

# Usuarios de prueba

## Bibliotecario

```text
Correo: admin@biblioteca.cl
Contraseña: admin123
```

## Estudiante

```text
Correo: carlos@estudiante.cl
Contraseña: 1234
```

---

# Ejecución

Ejecutar:

```text
src/main/Main.java
```

La primera pantalla corresponde al sistema de autenticación.

---

# Autor

**Carlos Felipe González Cereceda**

## Desarrollo Orientado a Objetos II

Evaluación Final Transversal - Sistema de Gestión de Biblioteca Escolar.