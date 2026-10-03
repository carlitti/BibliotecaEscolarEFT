package main;

import util.DatabaseConnection;

import java.sql.Connection;

public class PruebaConexion {

    public static void main(String[] args) {

        try {

            Connection conexion =
                    DatabaseConnection
                            .getInstance()
                            .getConnection();

            System.out.println(
                    "Conexión exitosa a biblioteca_escolar"
            );

            conexion.close();

        } catch (Exception e) {

            System.out.println(
                    "Error de conexión: "
                            + e.getMessage()
            );
        }
    }
}