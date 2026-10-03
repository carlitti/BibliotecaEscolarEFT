package dao;

import modelo.Prestamo;
import util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {


    // =========================================
    // REGISTRAR PRÉSTAMO
    // =========================================

    public synchronized boolean registrarPrestamo(
            Prestamo prestamo
    ) {

        Connection conexion = null;

        try {

            conexion =
                    DatabaseConnection
                            .getInstance()
                            .getConnection();


            conexion.setAutoCommit(
                    false
            );


            // =====================================
            // 1. COMPROBAR STOCK Y BLOQUEAR FILA
            // =====================================

            String sqlStock =
                    """
                    SELECT stock
                    FROM libros
                    WHERE id = ?
                    FOR UPDATE
                    """;


            int stockActual;


            try (
                    PreparedStatement statement =
                            conexion.prepareStatement(
                                    sqlStock
                            )
            ) {

                statement.setInt(
                        1,
                        prestamo.getIdLibro()
                );


                try (
                        ResultSet resultado =
                                statement.executeQuery()
                ) {

                    if (!resultado.next()) {

                        conexion.rollback();

                        return false;
                    }


                    stockActual =
                            resultado.getInt(
                                    "stock"
                            );
                }
            }


            if (stockActual <= 0) {

                conexion.rollback();

                return false;
            }


            // =====================================
            // 2. INSERTAR PRÉSTAMO
            // =====================================

            String sqlPrestamo =
                    """
                    INSERT INTO prestamos
                    (
                        id_estudiante,
                        id_libro,
                        fecha_prestamo,
                        fecha_vencimiento,
                        fecha_devolucion,
                        devuelto
                    )
                    VALUES (?, ?, ?, ?, NULL, FALSE)
                    """;


            try (
                    PreparedStatement statement =
                            conexion.prepareStatement(
                                    sqlPrestamo,
                                    Statement.RETURN_GENERATED_KEYS
                            )
            ) {

                statement.setInt(
                        1,
                        prestamo.getIdEstudiante()
                );

                statement.setInt(
                        2,
                        prestamo.getIdLibro()
                );

                statement.setDate(
                        3,
                        Date.valueOf(
                                prestamo.getFechaPrestamo()
                        )
                );

                statement.setDate(
                        4,
                        Date.valueOf(
                                prestamo.getFechaVencimiento()
                        )
                );


                statement.executeUpdate();


                try (
                        ResultSet claves =
                                statement.getGeneratedKeys()
                ) {

                    if (claves.next()) {

                        prestamo.setId(
                                claves.getInt(1)
                        );
                    }
                }
            }


            // =====================================
            // 3. DESCONTAR STOCK
            // =====================================

            String sqlActualizarStock =
                    """
                    UPDATE libros
                    SET stock = stock - 1
                    WHERE id = ?
                    """;


            try (
                    PreparedStatement statement =
                            conexion.prepareStatement(
                                    sqlActualizarStock
                            )
            ) {

                statement.setInt(
                        1,
                        prestamo.getIdLibro()
                );

                statement.executeUpdate();
            }


            // =====================================
            // 4. CONFIRMAR TRANSACCIÓN
            // =====================================

            conexion.commit();

            return true;


        } catch (SQLException e) {

            System.out.println(
                    "Error al registrar préstamo: "
                            + e.getMessage()
            );


            if (conexion != null) {

                try {

                    conexion.rollback();

                } catch (SQLException ex) {

                    System.out.println(
                            "Error en rollback: "
                                    + ex.getMessage()
                    );
                }
            }


            return false;


        } finally {

            if (conexion != null) {

                try {

                    conexion.setAutoCommit(
                            true
                    );

                    conexion.close();

                } catch (SQLException e) {

                    System.out.println(
                            "Error al cerrar conexión: "
                                    + e.getMessage()
                    );
                }
            }
        }
    }


    // =========================================
    // LISTAR PRÉSTAMOS
    // =========================================

    public List<Prestamo> listarTodos() {

        List<Prestamo> prestamos =
                new ArrayList<>();


        String sql =
                """
                SELECT
                    id,
                    id_estudiante,
                    id_libro,
                    fecha_prestamo,
                    fecha_vencimiento,
                    fecha_devolucion,
                    devuelto
                FROM prestamos
                ORDER BY id DESC
                """;


        try (
                Connection conexion =
                        DatabaseConnection
                                .getInstance()
                                .getConnection();

                PreparedStatement statement =
                        conexion.prepareStatement(
                                sql
                        );

                ResultSet resultado =
                        statement.executeQuery()
        ) {

            while (resultado.next()) {

                Date fechaDevolucionSQL =
                        resultado.getDate(
                                "fecha_devolucion"
                        );


                LocalDate fechaDevolucion =
                        fechaDevolucionSQL == null
                                ? null
                                : fechaDevolucionSQL.toLocalDate();


                Prestamo prestamo =
                        new Prestamo(
                                resultado.getInt("id"),

                                resultado.getInt(
                                        "id_estudiante"
                                ),

                                resultado.getInt(
                                        "id_libro"
                                ),

                                resultado
                                        .getDate(
                                                "fecha_prestamo"
                                        )
                                        .toLocalDate(),

                                resultado
                                        .getDate(
                                                "fecha_vencimiento"
                                        )
                                        .toLocalDate(),

                                fechaDevolucion,

                                resultado.getBoolean(
                                        "devuelto"
                                )
                        );


                prestamos.add(
                        prestamo
                );
            }


        } catch (SQLException e) {

            System.out.println(
                    "Error al listar préstamos: "
                            + e.getMessage()
            );
        }


        return prestamos;
    }


    // =========================================
    // DEVOLVER LIBRO
    // =========================================

    public synchronized boolean devolverPrestamo(
            Prestamo prestamo
    ) {

        Connection conexion = null;


        try {

            conexion =
                    DatabaseConnection
                            .getInstance()
                            .getConnection();


            conexion.setAutoCommit(
                    false
            );


            // =====================================
            // 1. MARCAR COMO DEVUELTO
            // =====================================

            String sqlPrestamo =
                    """
                    UPDATE prestamos
                    SET
                        devuelto = TRUE,
                        fecha_devolucion = ?
                    WHERE id = ?
                    AND devuelto = FALSE
                    """;


            int filas;


            try (
                    PreparedStatement statement =
                            conexion.prepareStatement(
                                    sqlPrestamo
                            )
            ) {

                statement.setDate(
                        1,
                        Date.valueOf(
                                LocalDate.now()
                        )
                );

                statement.setInt(
                        2,
                        prestamo.getId()
                );


                filas =
                        statement.executeUpdate();
            }


            if (filas == 0) {

                conexion.rollback();

                return false;
            }


            // =====================================
            // 2. DEVOLVER STOCK
            // =====================================

            String sqlStock =
                    """
                    UPDATE libros
                    SET stock = stock + 1
                    WHERE id = ?
                    """;


            try (
                    PreparedStatement statement =
                            conexion.prepareStatement(
                                    sqlStock
                            )
            ) {

                statement.setInt(
                        1,
                        prestamo.getIdLibro()
                );

                statement.executeUpdate();
            }


            conexion.commit();

            return true;


        } catch (SQLException e) {

            System.out.println(
                    "Error al devolver préstamo: "
                            + e.getMessage()
            );


            if (conexion != null) {

                try {

                    conexion.rollback();

                } catch (SQLException ex) {

                    System.out.println(
                            "Error en rollback: "
                                    + ex.getMessage()
                    );
                }
            }


            return false;


        } finally {

            if (conexion != null) {

                try {

                    conexion.setAutoCommit(
                            true
                    );

                    conexion.close();

                } catch (SQLException e) {

                    System.out.println(
                            "Error al cerrar conexión: "
                                    + e.getMessage()
                    );
                }
            }
        }
    }


    // =========================================
    // HISTORIAL POR ESTUDIANTE
    // =========================================

    public List<Prestamo> listarPorEstudiante(
            int idEstudiante
    ) {

        List<Prestamo> resultadoFinal =
                new ArrayList<>();


        for (
                Prestamo prestamo
                : listarTodos()
        ) {

            if (
                    prestamo.getIdEstudiante()
                            == idEstudiante
            ) {

                resultadoFinal.add(
                        prestamo
                );
            }
        }


        return resultadoFinal;
    }
    // =========================================
// REPORTE: LIBROS MÁS PRESTADOS
// =========================================

    public List<String[]> librosMasPrestados() {

        List<String[]> reporte =
                new ArrayList<>();

        String sql =
                """
                SELECT
                    l.titulo,
                    COUNT(p.id) AS cantidad
                FROM prestamos p
                INNER JOIN libros l
                    ON p.id_libro = l.id
                GROUP BY
                    l.id,
                    l.titulo
                ORDER BY cantidad DESC
                """;

        try (
                Connection conexion =
                        DatabaseConnection
                                .getInstance()
                                .getConnection();

                PreparedStatement statement =
                        conexion.prepareStatement(sql);

                ResultSet resultado =
                        statement.executeQuery()
        ) {

            while (resultado.next()) {

                reporte.add(
                        new String[]{

                                resultado.getString(
                                        "titulo"
                                ),

                                String.valueOf(
                                        resultado.getInt(
                                                "cantidad"
                                        )
                                )
                        }
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error en reporte de libros más prestados: "
                            + e.getMessage()
            );
        }

        return reporte;
    }


// =========================================
// REPORTE: PRÉSTAMOS ACTIVOS
// =========================================

    public List<String[]> prestamosActivos() {

        List<String[]> reporte =
                new ArrayList<>();

        String sql =
                """
                SELECT
                    p.id,
                    e.nombre AS estudiante,
                    l.titulo AS libro,
                    p.fecha_prestamo,
                    p.fecha_vencimiento
                FROM prestamos p
                INNER JOIN estudiantes e
                    ON p.id_estudiante = e.id
                INNER JOIN libros l
                    ON p.id_libro = l.id
                WHERE p.devuelto = FALSE
                ORDER BY p.fecha_vencimiento
                """;

        try (
                Connection conexion =
                        DatabaseConnection
                                .getInstance()
                                .getConnection();

                PreparedStatement statement =
                        conexion.prepareStatement(sql);

                ResultSet resultado =
                        statement.executeQuery()
        ) {

            while (resultado.next()) {

                reporte.add(
                        new String[]{

                                String.valueOf(
                                        resultado.getInt(
                                                "id"
                                        )
                                ),

                                resultado.getString(
                                        "estudiante"
                                ),

                                resultado.getString(
                                        "libro"
                                ),

                                resultado.getDate(
                                        "fecha_prestamo"
                                ).toString(),

                                resultado.getDate(
                                        "fecha_vencimiento"
                                ).toString()
                        }
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error en reporte de préstamos activos: "
                            + e.getMessage()
            );
        }

        return reporte;
    }


// =========================================
// REPORTE: HISTORIAL POR ESTUDIANTE
// =========================================

    public List<String[]> historialEstudiante(
            int idEstudiante
    ) {

        List<String[]> reporte =
                new ArrayList<>();

        String sql =
                """
                SELECT
                    p.id,
                    l.titulo,
                    p.fecha_prestamo,
                    p.fecha_vencimiento,
                    p.fecha_devolucion,
                    p.devuelto
                FROM prestamos p
                INNER JOIN libros l
                    ON p.id_libro = l.id
                WHERE p.id_estudiante = ?
                ORDER BY p.fecha_prestamo DESC
                """;

        try (
                Connection conexion =
                        DatabaseConnection
                                .getInstance()
                                .getConnection();

                PreparedStatement statement =
                        conexion.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    idEstudiante
            );

            try (
                    ResultSet resultado =
                            statement.executeQuery()
            ) {

                while (resultado.next()) {

                    Date fechaDevolucion =
                            resultado.getDate(
                                    "fecha_devolucion"
                            );

                    String devolucion =
                            fechaDevolucion == null
                                    ? "-"
                                    : fechaDevolucion.toString();


                    String estado =
                            resultado.getBoolean(
                                    "devuelto"
                            )
                                    ? "DEVUELTO"
                                    : "EN PRÉSTAMO";


                    reporte.add(
                            new String[]{

                                    String.valueOf(
                                            resultado.getInt(
                                                    "id"
                                            )
                                    ),

                                    resultado.getString(
                                            "titulo"
                                    ),

                                    resultado.getDate(
                                            "fecha_prestamo"
                                    ).toString(),

                                    resultado.getDate(
                                            "fecha_vencimiento"
                                    ).toString(),

                                    devolucion,

                                    estado
                            }
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error en historial del estudiante: "
                            + e.getMessage()
            );
        }

        return reporte;
    }
}