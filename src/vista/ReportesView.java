package vista;

import controlador.ReporteController;

import modelo.Estudiante;
import modelo.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReportesView extends JFrame {

    private final Usuario usuario;

    private final ReporteController reporteController;

    private JTable tabla;

    private DefaultTableModel modeloTabla;

    private JComboBox<Estudiante> comboEstudiante;

    public ReportesView(
            Usuario usuario
    ) {

        this.usuario =
                usuario;

        reporteController =
                new ReporteController();

        setTitle(
                "Biblioteca Escolar - Reportes"
        );

        setSize(
                950,
                580
        );

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        inicializarComponentes();

        cargarEstudiantes();
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
                        "REPORTES",
                        SwingConstants.CENTER
                );

        titulo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        22
                )
        );

        JButton btnMasPrestados =
                new JButton(
                        "Libros más prestados"
                );

        JButton btnActivos =
                new JButton(
                        "Libros actualmente en préstamo"
                );

        JPanel filaReportes =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                15,
                                10
                        )
                );

        filaReportes.add(
                btnMasPrestados
        );

        filaReportes.add(
                btnActivos
        );

        comboEstudiante =
                new JComboBox<>();

        comboEstudiante.setPreferredSize(
                new Dimension(
                        250,
                        30
                )
        );

        JButton btnHistorial =
                new JButton(
                        "Ver historial"
                );

        JPanel filaHistorial =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                10,
                                10
                        )
                );

        filaHistorial.add(
                new JLabel(
                        "Estudiante:"
                )
        );

        filaHistorial.add(
                comboEstudiante
        );

        filaHistorial.add(
                btnHistorial
        );

        JPanel panelOpciones =
                new JPanel();

        panelOpciones.setLayout(
                new BoxLayout(
                        panelOpciones,
                        BoxLayout.Y_AXIS
                )
        );

        panelOpciones.add(
                filaReportes
        );

        panelOpciones.add(
                filaHistorial
        );

        JPanel superior =
                new JPanel(
                        new BorderLayout()
                );

        superior.add(
                titulo,
                BorderLayout.NORTH
        );

        superior.add(
                panelOpciones,
                BorderLayout.CENTER
        );

        add(
                superior,
                BorderLayout.NORTH
        );

        modeloTabla =
                new DefaultTableModel() {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {

                        return false;
                    }
                };

        tabla =
                new JTable(
                        modeloTabla
                );

        tabla.setRowHeight(
                25
        );

        add(
                new JScrollPane(
                        tabla
                ),
                BorderLayout.CENTER
        );

        btnMasPrestados
                .addActionListener(
                        e ->
                                mostrarLibrosMasPrestados()
                );

        btnActivos
                .addActionListener(
                        e ->
                                mostrarPrestamosActivos()
                );

        btnHistorial
                .addActionListener(
                        e ->
                                mostrarHistorial()
                );
    }

    private void cargarEstudiantes() {

        List<Estudiante> estudiantes =
                reporteController
                        .listarEstudiantes();

        comboEstudiante.removeAllItems();

        if (
                usuario.esEstudiante()
        ) {

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

                    comboEstudiante.addItem(
                            estudiante
                    );

                    break;
                }
            }

            comboEstudiante.setEnabled(
                    false
            );

        } else {

            for (
                    Estudiante estudiante
                    : estudiantes
            ) {

                comboEstudiante.addItem(
                        estudiante
                );
            }
        }
    }

    private void mostrarLibrosMasPrestados() {

        modeloTabla.setDataVector(
                new Object[][]{},
                new String[]{

                        "Libro",
                        "Cantidad de préstamos"
                }
        );

        List<String[]> datos =
                reporteController
                        .obtenerLibrosMasPrestados();

        for (
                String[] fila
                : datos
        ) {

            modeloTabla.addRow(
                    fila
            );
        }
    }

    private void mostrarPrestamosActivos() {

        modeloTabla.setDataVector(
                new Object[][]{},
                new String[]{

                        "ID",
                        "Estudiante",
                        "Libro",
                        "Fecha préstamo",
                        "Vencimiento"
                }
        );

        List<String[]> datos =
                reporteController
                        .obtenerPrestamosActivos();

        for (
                String[] fila
                : datos
        ) {

            modeloTabla.addRow(
                    fila
            );
        }
    }

    private void mostrarHistorial() {

        Estudiante estudiante =
                (Estudiante)
                        comboEstudiante
                                .getSelectedItem();

        if (
                estudiante == null
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un estudiante.",
                    "Reporte",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        modeloTabla.setDataVector(
                new Object[][]{},
                new String[]{

                        "ID",
                        "Libro",
                        "Préstamo",
                        "Vencimiento",
                        "Devolución",
                        "Estado"
                }
        );

        List<String[]> datos =
                reporteController
                        .obtenerHistorialEstudiante(
                                estudiante.getId()
                        );

        for (
                String[] fila
                : datos
        ) {

            modeloTabla.addRow(
                    fila
            );
        }
    }
}