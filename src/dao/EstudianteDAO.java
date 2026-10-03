package dao;

import modelo.Estudiante;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO {

    public boolean guardar(
            Estudiante estudiante
    ) {

        String sql =
                """
                INSERT INTO estudiantes
                (nombre, rut, curso, correo)
                VALUES (?, ?, ?, ?)
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
                    estudiante.getNombre()
            );

            statement.setString(
                    2,
                    estudiante.getRut()
            );

            statement.setString(
                    3,
                    estudiante.getCurso()
            );

            statement.setString(
                    4,
                    estudiante.getCorreo()
            );


            int filas =
                    statement.executeUpdate();


            if (filas > 0) {

                try (
                        ResultSet claves =
                                statement.getGeneratedKeys()
                ) {

                    if (claves.next()) {

                        estudiante.setId(
                                claves.getInt(1)
                        );
                    }
                }

                return true;
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al guardar estudiante: "
                            + e.getMessage()
            );
        }

        return false;
    }


    public List<Estudiante> listarTodos() {

        List<Estudiante> estudiantes =
                new ArrayList<>();


        String sql =
                """
                SELECT
                    id,
                    nombre,
                    rut,
                    curso,
                    correo
                FROM estudiantes
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

                Estudiante estudiante =
                        new Estudiante(
                                resultado.getInt("id"),
                                resultado.getString("nombre"),
                                resultado.getString("rut"),
                                resultado.getString("curso"),
                                resultado.getString("correo")
                        );

                estudiantes.add(
                        estudiante
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al listar estudiantes: "
                            + e.getMessage()
            );
        }


        return estudiantes;
    }


    public boolean actualizar(
            Estudiante estudiante
    ) {

        String sql =
                """
                UPDATE estudiantes
                SET
                    nombre = ?,
                    rut = ?,
                    curso = ?,
                    correo = ?
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
                    estudiante.getNombre()
            );

            statement.setString(
                    2,
                    estudiante.getRut()
            );

            statement.setString(
                    3,
                    estudiante.getCurso()
            );

            statement.setString(
                    4,
                    estudiante.getCorreo()
            );

            statement.setInt(
                    5,
                    estudiante.getId()
            );


            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar estudiante: "
                            + e.getMessage()
            );

            return false;
        }
    }


    public boolean eliminar(
            int id
    ) {

        String sql =
                "DELETE FROM estudiantes WHERE id = ?";


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
                    "Error al eliminar estudiante: "
                            + e.getMessage()
            );

            return false;
        }
    }
}