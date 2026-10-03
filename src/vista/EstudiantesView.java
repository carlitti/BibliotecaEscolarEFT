package vista;

import controlador.EstudianteController;
import modelo.Estudiante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EstudiantesView extends JFrame {

    private final EstudianteController estudianteController;

    private JTextField txtNombre;
    private JTextField txtRut;
    private JTextField txtCurso;
    private JTextField txtCorreo;

    private JTable tablaEstudiantes;
    private DefaultTableModel modeloTabla;

    private List<Estudiante> estudiantesMostrados;

    public EstudiantesView() {

        estudianteController =
                new EstudianteController();

        estudiantesMostrados =
                new ArrayList<>();

        setTitle(
                "Biblioteca Escolar - Gestión de Estudiantes"
        );

        setSize(
                800,
                550
        );

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        inicializarComponentes();

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
                        "GESTIÓN DE ESTUDIANTES",
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
                new JLabel("Nombre:")
        );

        txtNombre =
                new JTextField();

        formulario.add(
                txtNombre
        );

        formulario.add(
                new JLabel("RUT:")
        );

        txtRut =
                new JTextField();

        formulario.add(
                txtRut
        );

        formulario.add(
                new JLabel("Curso:")
        );

        txtCurso =
                new JTextField();

        formulario.add(
                txtCurso
        );

        formulario.add(
                new JLabel("Correo:")
        );

        txtCorreo =
                new JTextField();

        formulario.add(
                txtCorreo
        );

        String[] columnas = {

                "ID",
                "Nombre",
                "RUT",
                "Curso",
                "Correo"
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

        tablaEstudiantes =
                new JTable(
                        modeloTabla
                );

        tablaEstudiantes.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaEstudiantes.setRowHeight(
                25
        );

        JScrollPane scroll =
                new JScrollPane(
                        tablaEstudiantes
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
                                15,
                                10
                        )
                );

        JButton btnRegistrar =
                new JButton(
                        "Registrar"
                );

        JButton btnEditar =
                new JButton(
                        "Editar"
                );

        JButton btnEliminar =
                new JButton(
                        "Eliminar"
                );

        JButton btnActualizar =
                new JButton(
                        "Actualizar"
                );

        JButton btnLimpiar =
                new JButton(
                        "Limpiar"
                );

        panelBotones.add(
                btnRegistrar
        );

        panelBotones.add(
                btnEditar
        );

        panelBotones.add(
                btnEliminar
        );

        panelBotones.add(
                btnActualizar
        );

        panelBotones.add(
                btnLimpiar
        );

        add(
                panelBotones,
                BorderLayout.SOUTH
        );

        btnRegistrar.addActionListener(
                e -> registrarEstudiante()
        );

        btnEditar.addActionListener(
                e -> editarEstudiante()
        );

        btnEliminar.addActionListener(
                e -> eliminarEstudiante()
        );

        btnActualizar.addActionListener(
                e -> actualizarTabla()
        );

        btnLimpiar.addActionListener(
                e -> limpiarFormulario()
        );

        tablaEstudiantes
                .getSelectionModel()
                .addListSelectionListener(
                        e -> {

                            if (!e.getValueIsAdjusting()) {

                                cargarEstudianteSeleccionado();
                            }
                        }
                );
    }

    private void registrarEstudiante() {

        Estudiante estudiante =
                obtenerEstudianteFormulario();

        if (estudiante == null) {

            return;
        }

        boolean guardado =
                estudianteController
                        .registrarEstudiante(
                                estudiante
                        );

        if (guardado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Estudiante registrado correctamente.",
                    "Biblioteca",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarFormulario();

            actualizarTabla();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo registrar el estudiante.\n"
                            + "Revise que el RUT o correo no estén repetidos.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void editarEstudiante() {

        int fila =
                tablaEstudiantes
                        .getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un estudiante.",
                    "Editar",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Estudiante original =
                estudiantesMostrados.get(
                        fila
                );

        Estudiante nuevosDatos =
                obtenerEstudianteFormulario();

        if (nuevosDatos == null) {

            return;
        }

        nuevosDatos.setId(
                original.getId()
        );

        boolean actualizado =
                estudianteController
                        .actualizarEstudiante(
                                nuevosDatos
                        );

        if (actualizado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Estudiante actualizado correctamente.",
                    "Biblioteca",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarFormulario();

            actualizarTabla();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo actualizar el estudiante.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void eliminarEstudiante() {

        int fila =
                tablaEstudiantes
                        .getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un estudiante.",
                    "Eliminar",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Estudiante estudiante =
                estudiantesMostrados.get(
                        fila
                );

        int opcion =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea eliminar al estudiante?\n"
                                + estudiante.getNombre(),
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (
                opcion
                        != JOptionPane.YES_OPTION
        ) {

            return;
        }

        boolean eliminado =
                estudianteController
                        .eliminarEstudiante(
                                estudiante.getId()
                        );

        if (eliminado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Estudiante eliminado correctamente.",
                    "Biblioteca",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarFormulario();

            actualizarTabla();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo eliminar el estudiante.\n"
                            + "Puede tener préstamos asociados.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private Estudiante obtenerEstudianteFormulario() {

        String nombre =
                txtNombre
                        .getText()
                        .trim();

        String rut =
                txtRut
                        .getText()
                        .trim();

        String curso =
                txtCurso
                        .getText()
                        .trim();

        String correo =
                txtCorreo
                        .getText()
                        .trim();

        if (
                nombre.isEmpty()
                        ||
                        rut.isEmpty()
                        ||
                        curso.isEmpty()
                        ||
                        correo.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Todos los campos son obligatorios.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );

            return null;
        }

        if (!correo.contains("@")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese un correo válido.",
                    "Correo inválido",
                    JOptionPane.WARNING_MESSAGE
            );

            return null;
        }

        return new Estudiante(
                0,
                nombre,
                rut,
                curso,
                correo
        );
    }

    private void actualizarTabla() {

        estudiantesMostrados =
                estudianteController
                        .listarEstudiantes();

        modeloTabla.setRowCount(
                0
        );

        for (
                Estudiante estudiante
                : estudiantesMostrados
        ) {

            modeloTabla.addRow(
                    new Object[]{

                            estudiante.getId(),

                            estudiante.getNombre(),

                            estudiante.getRut(),

                            estudiante.getCurso(),

                            estudiante.getCorreo()
                    }
            );
        }
    }

    private void cargarEstudianteSeleccionado() {

        int fila =
                tablaEstudiantes
                        .getSelectedRow();

        if (fila == -1) {

            return;
        }

        Estudiante estudiante =
                estudiantesMostrados.get(
                        fila
                );

        txtNombre.setText(
                estudiante.getNombre()
        );

        txtRut.setText(
                estudiante.getRut()
        );

        txtCurso.setText(
                estudiante.getCurso()
        );

        txtCorreo.setText(
                estudiante.getCorreo()
        );
    }

    private void limpiarFormulario() {

        tablaEstudiantes.clearSelection();

        txtNombre.setText("");
        txtRut.setText("");
        txtCurso.setText("");
        txtCorreo.setText("");
    }
}