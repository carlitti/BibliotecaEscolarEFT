-- =========================================================
-- BIBLIOTECA ESCOLAR EFT
-- Script de poblamiento inicial
-- =========================================================

USE biblioteca_escolar;


-- =========================================================
-- USUARIOS
-- =========================================================

INSERT INTO usuarios
(nombre, rut, correo, contrasena, rol)
VALUES
    (
        'Administrador Biblioteca',
        '11111111-1',
        'admin@biblioteca.cl',
        'admin123',
        'BIBLIOTECARIO'
    ),
    (
        'Carlos Gonzalez',
        '22222222-2',
        'carlos@estudiante.cl',
        '1234',
        'ESTUDIANTE'
    );


-- =========================================================
-- ESTUDIANTES
-- =========================================================

INSERT INTO estudiantes
(nombre, rut, curso, correo)
VALUES
    (
        'Carlos Gonzalez',
        '22222222-2',
        '4 Medio A',
        'carlos@estudiante.cl'
    ),
    (
        'Maria Perez',
        '33333333-3',
        '3 Medio B',
        'maria@estudiante.cl'
    );


-- =========================================================
-- CATEGORIAS
-- =========================================================

INSERT INTO categorias (nombre)
VALUES
    ('Literatura'),
    ('Ciencia'),
    ('Historia'),
    ('Tecnologia');


-- =========================================================
-- LIBROS
-- =========================================================

INSERT INTO libros
(titulo, autor, isbn, editorial, stock, id_categoria)
VALUES
    (
        'Cien años de soledad',
        'Gabriel Garcia Marquez',
        '9780307474728',
        'Sudamericana',
        5,
        1
    ),
    (
        'Breve historia del tiempo',
        'Stephen Hawking',
        '9780553380163',
        'Bantam',
        3,
        2
    ),
    (
        'Historia de Chile',
        'Simon Collier',
        '9780521568272',
        'Cambridge',
        4,
        3
    ),
    (
        'Clean Code',
        'Robert C. Martin',
        '9780132350884',
        'Prentice Hall',
        2,
        4
    );


-- =========================================================
-- PRÉSTAMO HISTÓRICO DE EJEMPLO
-- =========================================================

INSERT INTO prestamos
(
    id_estudiante,
    id_libro,
    fecha_prestamo,
    fecha_vencimiento,
    fecha_devolucion,
    devuelto
)
VALUES
    (
        1,
        4,
        '2026-09-20',
        '2026-09-27',
        '2026-09-25',
        TRUE
    );