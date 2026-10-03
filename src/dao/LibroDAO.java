package dao;

import modelo.Libro;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {

    public boolean guardar(
            Libro libro
    ) {

        String sql =
                """
                INSERT INTO libros
                (
                    titulo,
                    autor,
                    isbn,
                    editorial,
                    stock,
                    id_categoria
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;


        try (
                Connection conexion =
                        DatabaseConnection
                                .getInstance()
                                .getConnection();

                PreparedStatement statement =
                        conexion.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            statement.setString(
                    1,
                    libro.getTitulo()
            );

            statement.setString(
                    2,
                    libro.getAutor()
            );

            statement.setString(
                    3,
                    libro.getIsbn()
            );

            statement.setString(
                    4,
                    libro.getEditorial()
            );

            statement.setInt(
                    5,
                    libro.getStock()
            );

            statement.setInt(
                    6,
                    libro.getIdCategoria()
            );


            int filas =
                    statement.executeUpdate();


            if (filas > 0) {

                try (
                        ResultSet claves =
                                statement.getGeneratedKeys()
                ) {

                    if (claves.next()) {

                        libro.setId(
                                claves.getInt(1)
                        );
                    }
                }

                return true;
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al guardar libro: "
                            + e.getMessage()
            );
        }


        return false;
    }


    public List<Libro> listarTodos() {

        List<Libro> libros =
                new ArrayList<>();


        String sql =
                """
                SELECT
                    id,
                    titulo,
                    autor,
                    isbn,
                    editorial,
                    stock,
                    id_categoria
                FROM libros
                ORDER BY id
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

                Libro libro =
                        new Libro(
                                resultado.getInt("id"),
                                resultado.getString("titulo"),
                                resultado.getString("autor"),
                                resultado.getString("isbn"),
                                resultado.getString("editorial"),
                                resultado.getInt("stock"),
                                resultado.getInt("id_categoria")
                        );

                libros.add(
                        libro
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al listar libros: "
                            + e.getMessage()
            );
        }


        return libros;
    }


    public Libro buscarPorId(
            int id
    ) {

        String sql =
                """
                SELECT
                    id,
                    titulo,
                    autor,
                    isbn,
                    editorial,
                    stock,
                    id_categoria
                FROM libros
                WHERE id = ?
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
                    id
            );


            try (
                    ResultSet resultado =
                            statement.executeQuery()
            ) {

                if (resultado.next()) {

                    return new Libro(
                            resultado.getInt("id"),
                            resultado.getString("titulo"),
                            resultado.getString("autor"),
                            resultado.getString("isbn"),
                            resultado.getString("editorial"),
                            resultado.getInt("stock"),
                            resultado.getInt("id_categoria")
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al buscar libro: "
                            + e.getMessage()
            );
        }


        return null;
    }


    public boolean actualizar(
            Libro libro
    ) {

        String sql =
                """
                UPDATE libros
                SET
                    titulo = ?,
                    autor = ?,
                    isbn = ?,
                    editorial = ?,
                    stock = ?,
                    id_categoria = ?
                WHERE id = ?
                """;


        try (
                Connection conexion =
                        DatabaseConnection
                                .getInstance()
                                .getConnection();

                PreparedStatement statement =
                        conexion.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    libro.getTitulo()
            );

            statement.setString(
                    2,
                    libro.getAutor()
            );

            statement.setString(
                    3,
                    libro.getIsbn()
            );

            statement.setString(
                    4,
                    libro.getEditorial()
            );

            statement.setInt(
                    5,
                    libro.getStock()
            );

            statement.setInt(
                    6,
                    libro.getIdCategoria()
            );

            statement.setInt(
                    7,
                    libro.getId()
            );


            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar libro: "
                            + e.getMessage()
            );

            return false;
        }
    }


    public boolean eliminar(
            int id
    ) {

        String sql =
                "DELETE FROM libros WHERE id = ?";


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
                    id
            );


            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al eliminar libro: "
                            + e.getMessage()
            );

            return false;
        }
    }


    // =========================================
    // ACTUALIZAR STOCK
    // =========================================

    public synchronized boolean actualizarStock(
            int idLibro,
            int nuevoStock
    ) {

        if (nuevoStock < 0) {

            return false;
        }


        String sql =
                """
                UPDATE libros
                SET stock = ?
                WHERE id = ?
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
                    nuevoStock
            );

            statement.setInt(
                    2,
                    idLibro
            );


            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar stock: "
                            + e.getMessage()
            );

            return false;
        }
    }
}