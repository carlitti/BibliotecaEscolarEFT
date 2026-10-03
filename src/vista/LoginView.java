package vista;

import controlador.LoginController;
import modelo.Usuario;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {

    private JTextField txtCorreo;
    private JPasswordField txtContrasena;

    private final LoginController loginController;

    public LoginView() {

        loginController = new LoginController();

        setTitle("Biblioteca Escolar - Login");

        setSize(420, 300);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        inicializarComponentes();
    }

    private void inicializarComponentes() {

        JPanel panelPrincipal =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(
                        25,
                        40,
                        25,
                        40
                )
        );

        JLabel titulo =
                new JLabel(
                        "BIBLIOTECA ESCOLAR",
                        SwingConstants.CENTER
                );

        titulo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        22
                )
        );

        panelPrincipal.add(
                titulo,
                BorderLayout.NORTH
        );

        JPanel formulario =
                new JPanel(
                        new GridLayout(
                                4,
                                1,
                                5,
                                8
                        )
                );

        formulario.add(
                new JLabel("Correo:")
        );

        txtCorreo =
                new JTextField();

        formulario.add(
                txtCorreo
        );

        formulario.add(
                new JLabel("Contraseña:")
        );

        txtContrasena =
                new JPasswordField();

        formulario.add(
                txtContrasena
        );

        panelPrincipal.add(
                formulario,
                BorderLayout.CENTER
        );

        JButton btnIngresar =
                new JButton(
                        "Ingresar"
                );

        btnIngresar.addActionListener(
                e -> iniciarSesion()
        );

        JPanel panelBoton =
                new JPanel();

        panelBoton.add(
                btnIngresar
        );

        panelPrincipal.add(
                panelBoton,
                BorderLayout.SOUTH
        );

        add(
                panelPrincipal
        );
    }

    private void iniciarSesion() {

        String correo =
                txtCorreo
                        .getText()
                        .trim();

        String contrasena =
                new String(
                        txtContrasena
                                .getPassword()
                );

        if (
                correo.isEmpty()
                        ||
                        contrasena.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar correo y contraseña.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Usuario usuario =
                loginController.autenticar(
                        correo,
                        contrasena
                );

        if (usuario == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Correo o contraseña incorrectos.",
                    "Acceso denegado",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Bienvenido "
                        + usuario.getNombre()
                        + "\nRol: "
                        + usuario.getRol(),
                "Acceso correcto",
                JOptionPane.INFORMATION_MESSAGE
        );

        dispose();

        MenuPrincipalView menu =
                new MenuPrincipalView(
                        usuario
                );

        menu.setVisible(
                true
        );
    }
}