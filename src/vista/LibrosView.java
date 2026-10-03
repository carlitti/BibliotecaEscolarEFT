package vista;

import controlador.LibroController;

import modelo.Categoria;
import modelo.Libro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LibrosView extends JFrame {

    private final LibroController libroController;

    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JTextField txtIsbn;
    private JTextField txtEditorial;
    private JTextField txtStock;

    private JComboBox<Categoria> comboCategoria;

    private JTable tablaLibros;
    private DefaultTableModel modeloTabla;

    private List<Libro> librosMostrados;
    private List<Categoria> categorias;

    public LibrosView() {

        libroController =
                new LibroController();

        librosMostrados =
                new ArrayList<>();

        categorias =
                new ArrayList<>();

        setTitle(
                "Biblioteca Escolar - Gestión de Libros"
        );

        setSize(
                950,
                600
        );

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        inicializarComponentes();

        cargarCategorias();

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
                        "GESTIÓN DE LIBROS",
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

        JPanel panelFormulario =
                new JPanel(
                        new GridLayout(
                                6,
                                2,
                                10,
                                10
                        )
                );

        panelFormulario.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        20,
                        15,
                        20
                )
        );

        panelFormulario.add(
                new JLabel("Título:")
        );

        txtTitulo =
                new JTextField();

        panelFormulario.add(
                txtTitulo
        );

        panelFormulario.add(
                new JLabel("Autor:")
        );

        txtAutor =
                new JTextField();

        panelFormulario.add(
                txtAutor
        );

        panelFormulario.add(
                new JLabel("ISBN:")
        );

        txtIsbn =
                new JTextField();

        panelFormulario.add(
                txtIsbn
        );

        panelFormulario.add(
                new JLabel("Editorial:")
        );

        txtEditorial =
                new JTextField();

        panelFormulario.add(
                txtEditorial
        );

        panelFormulario.add(
                new JLabel("Stock:")
        );

        txtStock =
                new JTextField();

        panelFormulario.add(
                txtStock
        );

        panelFormulario.add(
                new JLabel("Categoría:")
        );

        comboCategoria =
                new JComboBox<>();

        panelFormulario.add(
                comboCategoria
        );

        String[] columnas = {

                "ID",
                "Título",
                "Autor",
                "ISBN",
                "Editorial",
                "Stock",
                "Categoría"
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

        tablaLibros =
                new JTable(
                        modeloTabla
                );

        tablaLibros.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaLibros.setRowHeight(
                25
        );

        JScrollPane scroll =
                new JScrollPane(
                        tablaLibros
                );

        JPanel panelCentro =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        panelCentro.add(
                panelFormulario,
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
                e -> registrarLibro()
        );

        btnEditar.addActionListener(
                e -> editarLibro()
        );

        btnEliminar.addActionListener(
                e -> eliminarLibro()
        );

        btnActualizar.addActionListener(
                e -> {

                    cargarCategorias();

                    actualizarTabla();
                }
        );

        btnLimpiar.addActionListener(
                e -> limpiarFormulario()
        );

        tablaLibros
                .getSelectionModel()
                .addListSelectionListener(
                        e -> {

                            if (!e.getValueIsAdjusting()) {

                                cargarLibroSeleccionado();
                            }
                        }
                );
    }

    private void cargarCategorias() {

        categorias =
                libroController.listarCategorias();

        comboCategoria.removeAllItems();

        for (
                Categoria categoria
                : categorias
        ) {

            comboCategoria.addItem(
                    categoria
            );
        }
    }

    private void registrarLibro() {

        Libro libro =
                obtenerLibroFormulario();

        if (libro == null) {

            return;
        }

        boolean guardado =
                libroController
                        .registrarLibro(
                                libro
                        );

        if (guardado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Libro registrado correctamente.",
                    "Biblioteca",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarFormulario();

            actualizarTabla();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo registrar el libro.\n"
                            + "Verifique que el ISBN no esté repetido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void editarLibro() {

        int fila =
                tablaLibros
                        .getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un libro.",
                    "Editar",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Libro original =
                librosMostrados.get(
                        fila
                );

        Libro nuevosDatos =
                obtenerLibroFormulario();

        if (nuevosDatos == null) {

            return;
        }

        nuevosDatos.setId(
                original.getId()
        );

        boolean actualizado =
                libroController
                        .actualizarLibro(
                                nuevosDatos
                        );

        if (actualizado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Libro actualizado correctamente.",
                    "Biblioteca",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarFormulario();

            actualizarTabla();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo actualizar el libro.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void eliminarLibro() {

        int fila =
                tablaLibros
                        .getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un libro.",
                    "Eliminar",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Libro libro =
                librosMostrados.get(
                        fila
                );

        int opcion =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea eliminar el libro?\n"
                                + libro.getTitulo(),
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
                libroController
                        .eliminarLibro(
                                libro.getId()
                        );

        if (eliminado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Libro eliminado correctamente.",
                    "Biblioteca",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarFormulario();

            actualizarTabla();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo eliminar el libro.\n"
                            + "Puede estar asociado a un préstamo.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private Libro obtenerLibroFormulario() {

        String titulo =
                txtTitulo
                        .getText()
                        .trim();

        String autor =
                txtAutor
                        .getText()
                        .trim();

        String isbn =
                txtIsbn
                        .getText()
                        .trim();

        String editorial =
                txtEditorial
                        .getText()
                        .trim();

        String stockTexto =
                txtStock
                        .getText()
                        .trim();

        Categoria categoria =
                (Categoria)
                        comboCategoria
                                .getSelectedItem();

        if (
                titulo.isEmpty()
                        ||
                        autor.isEmpty()
                        ||
                        isbn.isEmpty()
                        ||
                        editorial.isEmpty()
                        ||
                        stockTexto.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Todos los campos son obligatorios.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );

            return null;
        }

        if (categoria == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar una categoría.",
                    "Dato faltante",
                    JOptionPane.WARNING_MESSAGE
            );

            return null;
        }

        int stock;

        try {

            stock =
                    Integer.parseInt(
                            stockTexto
                    );

        } catch (
                NumberFormatException e
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "El stock debe ser un número entero.",
                    "Stock inválido",
                    JOptionPane.WARNING_MESSAGE
            );

            return null;
        }

        if (stock < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "El stock no puede ser negativo.",
                    "Stock inválido",
                    JOptionPane.WARNING_MESSAGE
            );

            return null;
        }

        return new Libro(
                0,
                titulo,
                autor,
                isbn,
                editorial,
                stock,
                categoria.getId()
        );
    }

    private void actualizarTabla() {

        librosMostrados =
                libroController.listarLibros();

        modeloTabla.setRowCount(
                0
        );

        for (
                Libro libro
                : librosMostrados
        ) {

            String categoria =
                    obtenerNombreCategoria(
                            libro.getIdCategoria()
                    );

            modeloTabla.addRow(
                    new Object[]{

                            libro.getId(),

                            libro.getTitulo(),

                            libro.getAutor(),

                            libro.getIsbn(),

                            libro.getEditorial(),

                            libro.getStock(),

                            categoria
                    }
            );
        }
    }

    private void cargarLibroSeleccionado() {

        int fila =
                tablaLibros
                        .getSelectedRow();

        if (fila == -1) {

            return;
        }

        Libro libro =
                librosMostrados.get(
                        fila
                );

        txtTitulo.setText(
                libro.getTitulo()
        );

        txtAutor.setText(
                libro.getAutor()
        );

        txtIsbn.setText(
                libro.getIsbn()
        );

        txtEditorial.setText(
                libro.getEditorial()
        );

        txtStock.setText(
                String.valueOf(
                        libro.getStock()
                )
        );

        seleccionarCategoria(
                libro.getIdCategoria()
        );
    }

    private String obtenerNombreCategoria(
            int idCategoria
    ) {

        for (
                Categoria categoria
                : categorias
        ) {

            if (
                    categoria.getId()
                            == idCategoria
            ) {

                return categoria.getNombre();
            }
        }

        return "Sin categoría";
    }

    private void seleccionarCategoria(
            int idCategoria
    ) {

        for (
                int i = 0;
                i < comboCategoria.getItemCount();
                i++
        ) {

            Categoria categoria =
                    comboCategoria
                            .getItemAt(
                                    i
                            );

            if (
                    categoria.getId()
                            == idCategoria
            ) {

                comboCategoria
                        .setSelectedIndex(
                                i
                        );

                return;
            }
        }
    }

    private void limpiarFormulario() {

        tablaLibros.clearSelection();

        txtTitulo.setText("");
        txtAutor.setText("");
        txtIsbn.setText("");
        txtEditorial.setText("");
        txtStock.setText("");

        if (
                comboCategoria.getItemCount()
                        > 0
        ) {

            comboCategoria
                    .setSelectedIndex(
                            0
                    );
        }
    }
}