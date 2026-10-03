package vista;

import modelo.Usuario;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipalView extends JFrame {

    private final Usuario usuario;

    public MenuPrincipalView(
            Usuario usuario
    ) {

        this.usuario = usuario;

        setTitle(
                "Biblioteca Escolar - Menú Principal"
        );

        setSize(
                500,
                450
        );

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        inicializarComponentes();
    }

    private void inicializarComponentes() {

        JPanel panelPrincipal =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
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

        JLabel usuarioLabel =
                new JLabel(
                        "Usuario: "
                                + usuario.getNombre()
                                + " | Rol: "
                                + usuario.getRol(),
                        SwingConstants.CENTER
                );

        JPanel encabezado =
                new JPanel(
                        new GridLayout(
                                2,
                                1
                        )
                );

        encabezado.add(
                titulo
        );

        encabezado.add(
                usuarioLabel
        );

        panelPrincipal.add(
                encabezado,
                BorderLayout.NORTH
        );

        JPanel panelBotones =
                new JPanel(
                        new GridLayout(
                                0,
                                1,
                                10,
                                10
                        )
                );

        JButton btnLibros =
                new JButton(
                        "Gestión de Libros"
                );

        JButton btnEstudiantes =
                new JButton(
                        "Gestión de Estudiantes"
                );

        JButton btnPrestamos =
                new JButton(
                        "Préstamos y Devoluciones"
                );

        JButton btnReportes =
                new JButton(
                        "Reportes"
                );

        JButton btnCerrarSesion =
                new JButton(
                        "Cerrar Sesión"
                );

        panelBotones.add(
                btnLibros
        );

        panelBotones.add(
                btnEstudiantes
        );

        panelBotones.add(
                btnPrestamos
        );

        panelBotones.add(
                btnReportes
        );

        panelBotones.add(
                btnCerrarSesion
        );

        panelPrincipal.add(
                panelBotones,
                BorderLayout.CENTER
        );

        // =========================
        // PERMISOS
        // =========================

        if (
                usuario.esEstudiante()
        ) {

            btnLibros.setVisible(
                    false
            );

            btnEstudiantes.setVisible(
                    false
            );
        }

        // =========================
        // LIBROS
        // =========================

        btnLibros.addActionListener(
                e -> {

                    LibrosView ventana =
                            new LibrosView();

                    ventana.setVisible(
                            true
                    );
                }
        );

        // =========================
        // ESTUDIANTES
        // =========================

        btnEstudiantes.addActionListener(
                e -> {

                    EstudiantesView ventana =
                            new EstudiantesView();

                    ventana.setVisible(
                            true
                    );
                }
        );

        // =========================
        // PRÉSTAMOS
        // =========================

        btnPrestamos.addActionListener(
                e -> {

                    PrestamosView ventana =
                            new PrestamosView(
                                    usuario
                            );

                    ventana.setVisible(
                            true
                    );
                }
        );

        // =========================
        // REPORTES
        // =========================

        btnReportes.addActionListener(
                e -> {

                    ReportesView ventana =
                            new ReportesView(
                                    usuario
                            );

                    ventana.setVisible(
                            true
                    );
                }
        );

        // =========================
        // CERRAR SESIÓN
        // =========================

        btnCerrarSesion.addActionListener(
                e -> {

                    dispose();

                    LoginView login =
                            new LoginView();

                    login.setVisible(
                            true
                    );
                }
        );

        add(
                panelPrincipal
        );
    }
}