package dao;

import modelo.Usuario;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public boolean guardar(
            Usuario usuario
    ) {

        String sql =
                """
                INSERT INTO usuarios
                (
                    nombre,
                    rut,
                    correo,
                    contrasena,
                    rol
                )
                VALUES (?, ?, ?, ?, ?)
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
                    usuario.getNombre()
            );

            statement.setString(
                    2,
                    usuario.getRut()
            );

            statement.setString(
                    3,
                    usuario.getCorreo()
            );

            statement.setString(
                    4,
                    usuario.getContrasena()
            );

            statement.setString(
                    5,
                    usuario.getRol()
            );


            int filas =
                    statement.executeUpdate();


            if (filas > 0) {

                try (
                        ResultSet claves =
                                statement.getGeneratedKeys()
                ) {

                    if (claves.next()) {

                        usuario.setId(
                                claves.getInt(1)
                        );
                    }
                }

                return true;
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al guardar usuario: "
                            + e.getMessage()
            );
        }

        return false;
    }


    public List<Usuario> listarTodos() {

        List<Usuario> usuarios =
                new ArrayList<>();


        String sql =
                """
                SELECT
                    id,
                    nombre,
                    rut,
                    correo,
                    contrasena,
                    rol
                FROM usuarios
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

                Usuario usuario =
                        new Usuario(
                                resultado.getInt("id"),
                                resultado.getString("nombre"),
                                resultado.getString("rut"),
                                resultado.getString("correo"),
                                resultado.getString("contrasena"),
                                resultado.getString("rol")
                        );

                usuarios.add(
                        usuario
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al listar usuarios: "
                            + e.getMessage()
            );
        }


        return usuarios;
    }


    public boolean actualizar(
            Usuario usuario
    ) {

        String sql =
                """
                UPDATE usuarios
                SET
                    nombre = ?,
                    rut = ?,
                    correo = ?,
                    contrasena = ?,
                    rol = ?
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
                    usuario.getNombre()
            );

            statement.setString(
                    2,
                    usuario.getRut()
            );

            statement.setString(
                    3,
                    usuario.getCorreo()
            );

            statement.setString(
                    4,
                    usuario.getContrasena()
            );

            statement.setString(
                    5,
                    usuario.getRol()
            );

            statement.setInt(
                    6,
                    usuario.getId()
            );


            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar usuario: "
                            + e.getMessage()
            );

            return false;
        }
    }


    public boolean eliminar(
            int id
    ) {

        String sql =
                "DELETE FROM usuarios WHERE id = ?";


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
                    "Error al eliminar usuario: "
                            + e.getMessage()
            );

            return false;
        }
    }


    // =========================================
    // AUTENTICACIÓN
    // =========================================

    public Usuario autenticar(
            String correo,
            String contrasena
    ) {

        String sql =
                """
                SELECT
                    id,
                    nombre,
                    rut,
                    correo,
                    contrasena,
                    rol
                FROM usuarios
                WHERE correo = ?
                AND contrasena = ?
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
                    correo
            );

            statement.setString(
                    2,
                    contrasena
            );


            try (
                    ResultSet resultado =
                            statement.executeQuery()
            ) {

                if (resultado.next()) {

                    return new Usuario(
                            resultado.getInt("id"),
                            resultado.getString("nombre"),
                            resultado.getString("rut"),
                            resultado.getString("correo"),
                            resultado.getString("contrasena"),
                            resultado.getString("rol")
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al autenticar usuario: "
                            + e.getMessage()
            );
        }


        return null;
    }
}