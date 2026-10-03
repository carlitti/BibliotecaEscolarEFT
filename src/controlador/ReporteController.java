package controlador;

import dao.EstudianteDAO;
import dao.PrestamoDAO;

import modelo.Estudiante;

import java.util.List;

public class ReporteController {

    private final PrestamoDAO prestamoDAO;
    private final EstudianteDAO estudianteDAO;


    public ReporteController() {

        prestamoDAO =
                new PrestamoDAO();

        estudianteDAO =
                new EstudianteDAO();
    }


    public List<Estudiante> listarEstudiantes() {

        return estudianteDAO.listarTodos();
    }


    public List<String[]> obtenerLibrosMasPrestados() {

        return prestamoDAO.librosMasPrestados();
    }


    public List<String[]> obtenerPrestamosActivos() {

        return prestamoDAO.prestamosActivos();
    }


    public List<String[]> obtenerHistorialEstudiante(
            int idEstudiante
    ) {

        return prestamoDAO.historialEstudiante(
                idEstudiante
        );
    }
}