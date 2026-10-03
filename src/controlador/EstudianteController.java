package controlador;

import dao.EstudianteDAO;
import modelo.Estudiante;

import java.util.List;

public class EstudianteController {

    private final EstudianteDAO estudianteDAO;


    public EstudianteController() {

        estudianteDAO =
                new EstudianteDAO();
    }


    public List<Estudiante> listarEstudiantes() {

        return estudianteDAO.listarTodos();
    }


    public boolean registrarEstudiante(
            Estudiante estudiante
    ) {

        return estudianteDAO.guardar(
                estudiante
        );
    }


    public boolean actualizarEstudiante(
            Estudiante estudiante
    ) {

        return estudianteDAO.actualizar(
                estudiante
        );
    }


    public boolean eliminarEstudiante(
            int id
    ) {

        return estudianteDAO.eliminar(
                id
        );
    }
}