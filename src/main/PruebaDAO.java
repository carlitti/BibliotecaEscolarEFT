package main;

import dao.CategoriaDAO;
import dao.EstudianteDAO;
import dao.LibroDAO;

import modelo.Categoria;
import modelo.Estudiante;
import modelo.Libro;

public class PruebaDAO {

    public static void main(String[] args) {

        CategoriaDAO categoriaDAO =
                new CategoriaDAO();

        EstudianteDAO estudianteDAO =
                new EstudianteDAO();

        LibroDAO libroDAO =
                new LibroDAO();


        System.out.println(
                "===== CATEGORIAS ====="
        );

        for (
                Categoria categoria
                : categoriaDAO.listarTodos()
        ) {

            System.out.println(
                    categoria
            );
        }


        System.out.println(
                "\n===== ESTUDIANTES ====="
        );

        for (
                Estudiante estudiante
                : estudianteDAO.listarTodos()
        ) {

            System.out.println(
                    estudiante.getId()
                            + " | "
                            + estudiante.getNombre()
                            + " | "
                            + estudiante.getCurso()
            );
        }


        System.out.println(
                "\n===== LIBROS ====="
        );

        for (
                Libro libro
                : libroDAO.listarTodos()
        ) {

            System.out.println(
                    libro.getId()
                            + " | "
                            + libro.getTitulo()
                            + " | Stock: "
                            + libro.getStock()
            );
        }
    }
}