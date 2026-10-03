package controlador;

import dao.UsuarioDAO;
import modelo.Usuario;

public class LoginController {

    private final UsuarioDAO usuarioDAO;

    public LoginController() {

        usuarioDAO =
                new UsuarioDAO();
    }


    public Usuario autenticar(
            String correo,
            String contrasena
    ) {

        return usuarioDAO.autenticar(
                correo,
                contrasena
        );
    }
}