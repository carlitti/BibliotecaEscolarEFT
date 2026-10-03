package vista;

import controlador.PrestamoController;

import modelo.Estudiante;
import modelo.Libro;
import modelo.Prestamo;
import modelo.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrestamosView extends JFrame {

    private final Usuario usuario;

    private final PrestamoController prestamoController;

    private JComboBox<Estudiante> comboEstudiante;
    private JComboBox<Libro> comboLibro;

    private JTextField txtFechaPrestamo;
    private JTextField txtFechaVencimiento;

    private JTable tablaPrestamos;
    private DefaultTableModel modeloTabla;

    private JButton btnRegistrarPrestamo;
    private JButton btnDevolver;

    private List<Estudiante> estudiantes;
    private List<Libro> libros;
    private List<Prestamo> prestamosMostrados;

    public PrestamosView(
            Usuario usuario
    ) {

        this.usuario =
                usuario;

        prestamoController =
                new PrestamoController();

        estudiantes =
                new ArrayList<>();

        libros =
                new ArrayList<>();

        prestamosMostrados =
                new ArrayList<>();

        setTitle(
                "Biblioteca Escolar - Préstamos y Devoluciones"
        );

        setSize(
                1000,
                620
        );

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        inicializarComponentes();

        cargarDatos();

        actualizarTabla();
    }

    private void inicializarComponentes() {

        setLayout(
                new BorderLayout(
                        10,
                        10
                )
        );

        JLabel titulo =
                new JLabel(
                        "PRÉSTAMOS Y DEVOLUCIONES",
                        SwingConstants.CENTER
                );

        titulo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        22
                )
        );

        add(
                titulo,
                BorderLayout.NORTH
        );

        JPanel formulario =
                new JPanel(
                        new GridLayout(
                                4,
                                2,
                                10,
                                10
                        )
                );

        formulario.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        25,
                        15,
                        25
                )
        );

        formulario.add(
                new JLabel(
                        "Estudiante:"
                )
        );

        comboEstudiante =
                new JComboBox<>();

        formulario.add(
                comboEstudiante
        );

        formulario.add(
                new JLabel(
                        "Libro:"
                )
        );

        comboLibro =
                new JComboBox<>();

        comboLibro.setRenderer(
                new DefaultListCellRenderer() {

                    @Override
                    public Component getListCellRendererComponent(
                            JList<?> list,
                            Object value,
                            int index,
                            boolean isSelected,
                            boolean cellHasFocus
                    ) {

                        JLabel label =
                                (JLabel)
                                        super.getListCellRendererComponent(
                                                list,
                                                value,
                                                index,
                                                isSelected,
                                                cellHasFocus
                                        );

                        if (
                                value
                                        instanceof Libro libro
                        ) {

                            label.setText(
                                    libro.getId()
                                            + " - "
                                            + libro.getTitulo()
                                            + " | Stock: "
                                            + libro.getStock()
                            );
                        }

                        return label;
                    }
                }
        );

        formulario.add(
                comboLibro
        );

        formulario.add(
                new JLabel(
                        "Fecha préstamo:"
                )
        );

        txtFechaPrestamo =
                new JTextField();

        txtFechaPrestamo.setEditable(
                false
        );

        formulario.add(
                txtFechaPrestamo
        );

        formulario.add(
                new JLabel(
                        "Fecha vencimiento:"
                )
        );

        txtFechaVencimiento =
                new JTextField();

        txtFechaVencimiento.setEditable(
                false
        );

        formulario.add(
                txtFechaVencimiento
        );

        String[] columnas = {

                "ID",
                "Estudiante",
                "Libro",
                "Préstamo",
                "Vencimiento",
                "Devolución",
                "Estado"
        };

        modeloTabla =
                new DefaultTableModel(
                        columnas,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {

                        return false;
                    }
                };

        tablaPrestamos =
                new JTable(
                        modeloTabla
                );

        tablaPrestamos.setRowHeight(
                25
        );

        tablaPrestamos.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scroll =
                new JScrollPane(
                        tablaPrestamos
                );

        JPanel panelCentro =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        panelCentro.add(
                formulario,
                BorderLayout.NORTH
        );

        panelCentro.add(
                scroll,
                BorderLayout.CENTER
        );

        add(
                panelCentro,
                BorderLayout.CENTER
        );

        JPanel panelBotones =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                20,
                                10
                        )
                );

        btnRegistrarPrestamo =
                new JButton(
                        "Registrar Préstamo"
                );

        btnDevolver =
                new JButton(
                        "Registrar Devolución"
                );

        JButton btnActualizar =
                new JButton(
                        "Actualizar"
                );

        panelBotones.add(
                btnRegistrarPrestamo
        );

        panelBotones.add(
                btnDevolver
        );

        panelBotones.add(
                btnActualizar
        );

        add(
                panelBotones,
                BorderLayout.SOUTH
        );

        btnRegistrarPrestamo
                .addActionListener(
                        e ->
                                registrarPrestamoEnSegundoPlano()
                );

        btnDevolver
                .addActionListener(
                        e ->
                                devolverPrestamoEnSegundoPlano()
                );

        btnActualizar
                .addActionListener(
                        e -> {

                            cargarDatos();

                            actualizarTabla();
                        }
                );
    }

    private void cargarDatos() {

        estudiantes =
                prestamoController
                        .listarEstudiantes();

        libros =
                prestamoController
                        .listarLibros();

        comboEstudiante.removeAllItems();

        comboLibro.removeAllItems();

        if (
                usuario.esEstudiante()
        ) {

            Estudiante estudianteActual =
                    buscarEstudianteUsuario();

            if (
                    estudianteActual
                            != null
            ) {

                comboEstudiante.addItem(
                        estudianteActual
                );

                comboEstudiante.setEnabled(
                        false
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No existe un estudiante asociado al usuario actual.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } else {

            comboEstudiante.setEnabled(
                    true
            );

            for (
                    Estudiante estudiante
                    : estudiantes
            ) {

                comboEstudiante.addItem(
                        estudiante
                );
            }
        }

        for (
                Libro libro
                : libros
        ) {

            comboLibro.addItem(
                    libro
            );
        }

        LocalDate hoy =
                LocalDate.now();

        LocalDate vencimiento =
                hoy.plusDays(
                        7
                );

        txtFechaPrestamo.setText(
                hoy.toString()
        );

        txtFechaVencimiento.setText(
                vencimiento.toString()
        );
    }

    private Estudiante buscarEstudianteUsuario() {

        for (
                Estudiante estudiante
                : estudiantes
        ) {

            if (
                    estudiante
                            .getRut()
                            .equalsIgnoreCase(
                                    usuario.getRut()
                            )
            ) {

                return estudiante;
            }
        }

        return null;
    }

    private void registrarPrestamoEnSegundoPlano() {

        Estudiante estudiante =
                (Estudiante)
                        comboEstudiante
                                .getSelectedItem();

        Libro libro =
                (Libro)
                        comboLibro
                                .getSelectedItem();

        if (
                estudiante == null
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un estudiante.",
                    "Dato faltante",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (
                libro == null
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un libro.",
                    "Dato faltante",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (
                libro.getStock()
                        <= 0
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "El libro seleccionado no tiene stock disponible.",
                    "Sin stock",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Prestamo prestamo =
                new Prestamo(
                        estudiante.getId(),
                        libro.getId(),
                        LocalDate.now(),
                        LocalDate.now()
                                .plusDays(
                                        7
                                )
                );

        btnRegistrarPrestamo.setEnabled(
                false
        );

        Thread hiloPrestamo =
                new Thread(
                        () -> {

                            boolean registrado =
                                    prestamoController
                                            .registrarPrestamo(
                                                    prestamo
                                            );

                            SwingUtilities.invokeLater(
                                    () -> {

                                        btnRegistrarPrestamo
                                                .setEnabled(
                                                        true
                                                );

                                        if (registrado) {

                                            JOptionPane.showMessageDialog(
                                                    this,
                                                    "Préstamo registrado correctamente.\n"
                                                            + "Fecha de vencimiento: "
                                                            + prestamo
                                                            .getFechaVencimiento(),
                                                    "Biblioteca",
                                                    JOptionPane.INFORMATION_MESSAGE
                                            );

                                            cargarDatos();

                                            actualizarTabla();

                                        } else {

                                            JOptionPane.showMessageDialog(
                                                    this,
                                                    "No se pudo registrar el préstamo.\n"
                                                            + "Verifique que exista stock disponible.",
                                                    "Error",
                                                    JOptionPane.ERROR_MESSAGE
                                            );

                                            cargarDatos();

                                            actualizarTabla();
                                        }
                                    }
                            );
                        }
                );

        hiloPrestamo.start();
    }

    private void devolverPrestamoEnSegundoPlano() {

        int fila =
                tablaPrestamos
                        .getSelectedRow();

        if (
                fila == -1
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un préstamo.",
                    "Devolución",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Prestamo prestamo =
                prestamosMostrados.get(
                        fila
                );

        if (
                prestamo.isDevuelto()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Este préstamo ya fue devuelto.",
                    "Devolución",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int opcion =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Registrar devolución del préstamo #"
                                + prestamo.getId()
                                + "?",
                        "Confirmar devolución",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (
                opcion
                        != JOptionPane.YES_OPTION
        ) {

            return;
        }

        btnDevolver.setEnabled(
                false
        );

        Thread hiloDevolucion =
                new Thread(
                        () -> {

                            boolean devuelto =
                                    prestamoController
                                            .devolverPrestamo(
                                                    prestamo
                                            );

                            SwingUtilities.invokeLater(
                                    () -> {

                                        btnDevolver
                                                .setEnabled(
                                                        true
                                                );

                                        if (devuelto) {

                                            JOptionPane.showMessageDialog(
                                                    this,
                                                    "Devolución registrada correctamente.",
                                                    "Biblioteca",
                                                    JOptionPane.INFORMATION_MESSAGE
                                            );

                                            cargarDatos();

                                            actualizarTabla();

                                        } else {

                                            JOptionPane.showMessageDialog(
                                                    this,
                                                    "No se pudo registrar la devolución.",
                                                    "Error",
                                                    JOptionPane.ERROR_MESSAGE
                                            );
                                        }
                                    }
                            );
                        }
                );

        hiloDevolucion.start();
    }

    private void actualizarTabla() {

        if (
                usuario.esEstudiante()
        ) {

            Estudiante estudiante =
                    buscarEstudianteUsuario();

            if (
                    estudiante == null
            ) {

                prestamosMostrados =
                        new ArrayList<>();

            } else {

                prestamosMostrados =
                        prestamoController
                                .listarPrestamosPorEstudiante(
                                        estudiante.getId()
                                );
            }

        } else {

            prestamosMostrados =
                    prestamoController
                            .listarPrestamos();
        }

        modeloTabla.setRowCount(
                0
        );

        for (
                Prestamo prestamo
                : prestamosMostrados
        ) {

            String estudiante =
                    obtenerNombreEstudiante(
                            prestamo.getIdEstudiante()
                    );

            String libro =
                    obtenerNombreLibro(
                            prestamo.getIdLibro()
                    );

            String devolucion =
                    prestamo
                            .getFechaDevolucion()
                            == null
                            ?
                            "-"
                            :
                            prestamo
                            .getFechaDevolucion()
                            .toString();

            String estado;

            if (
                    prestamo.isDevuelto()
            ) {

                if (
                        prestamo.estaAtrasado()
                ) {

                    estado =
                            "DEVUELTO CON ATRASO";

                } else {

                    estado =
                            "DEVUELTO";
                }

            } else {

                if (
                        prestamo.estaAtrasado()
                ) {

                    estado =
                            "ATRASADO";

                } else {

                    estado =
                            "EN PRÉSTAMO";
                }
            }

            modeloTabla.addRow(
                    new Object[]{

                            prestamo.getId(),

                            estudiante,

                            libro,

                            prestamo.getFechaPrestamo(),

                            prestamo.getFechaVencimiento(),

                            devolucion,

                            estado
                    }
            );
        }
    }

    private String obtenerNombreEstudiante(
            int idEstudiante
    ) {

        for (
                Estudiante estudiante
                : estudiantes
        ) {

            if (
                    estudiante.getId()
                            == idEstudiante
            ) {

                return estudiante.getNombre();
            }
        }

        return "Estudiante #"
                + idEstudiante;
    }

    private String obtenerNombreLibro(
            int idLibro
    ) {

        for (
                Libro libro
                : libros
        ) {

            if (
                    libro.getId()
                            == idLibro
            ) {

                return libro.getTitulo();
            }
        }

        return "Libro #"
                + idLibro;
    }
}