package controlador;

import dao.CategoriaDAO;
import dao.LibroDAO;

import modelo.Categoria;
import modelo.Libro;

import java.util.List;

public class LibroController {

    private final LibroDAO libroDAO;
    private final CategoriaDAO categoriaDAO;


    public LibroController() {

        libroDAO =
                new LibroDAO();

        categoriaDAO =
                new CategoriaDAO();
    }


    public List<Libro> listarLibros() {

        return libroDAO.listarTodos();
    }


    public List<Categoria> listarCategorias() {

        return categoriaDAO.listarTodos();
    }


    public boolean registrarLibro(
            Libro libro
    ) {

        return libroDAO.guardar(
                libro
        );
    }


    public boolean actualizarLibro(
            Libro libro
    ) {

        return libroDAO.actualizar(
                libro
        );
    }


    public boolean eliminarLibro(
            int id
    ) {

        return libroDAO.eliminar(
                id
        );
    }


    public Libro buscarLibroPorId(
            int id
    ) {

        return libroDAO.buscarPorId(
                id
        );
    }
}