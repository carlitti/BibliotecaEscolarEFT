package controlador;

import dao.EstudianteDAO;
import dao.LibroDAO;
import dao.PrestamoDAO;

import modelo.Estudiante;
import modelo.Libro;
import modelo.Prestamo;

import java.util.List;

public class PrestamoController {

    private final PrestamoDAO prestamoDAO;
    private final EstudianteDAO estudianteDAO;
    private final LibroDAO libroDAO;


    public PrestamoController() {

        prestamoDAO =
                new PrestamoDAO();

        estudianteDAO =
                new EstudianteDAO();

        libroDAO =
                new LibroDAO();
    }


    public List<Estudiante> listarEstudiantes() {

        return estudianteDAO.listarTodos();
    }


    public List<Libro> listarLibros() {

        return libroDAO.listarTodos();
    }


    public List<Prestamo> listarPrestamos() {

        return prestamoDAO.listarTodos();
    }


    public List<Prestamo> listarPrestamosPorEstudiante(
            int idEstudiante
    ) {

        return prestamoDAO.listarPorEstudiante(
                idEstudiante
        );
    }


    public boolean registrarPrestamo(
            Prestamo prestamo
    ) {

        return prestamoDAO.registrarPrestamo(
                prestamo
        );
    }


    public boolean devolverPrestamo(
            Prestamo prestamo
    ) {

        return prestamoDAO.devolverPrestamo(
                prestamo
        );
    }
}