package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import domain.Cliente;

public class VentanaClientes extends JFrame {

    private static final long serialVersionUID = 1L;

    // ATRIBUTOS
    private List<Cliente> clientes;

    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;
    private TableRowSorter<DefaultTableModel> ordenador;

    private JButton btnAnadir;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JButton btnBuscar;
    private JButton btnMostrarTodos;

    private JTextField txtBuscar;

    private int siguienteId = 1;

    // CONSTRUCTOR
    public VentanaClientes() {
        this(new ArrayList<>());
    }

    public VentanaClientes(List<Cliente> clientes) {

        this.clientes = clientes;

        for (Cliente c : clientes) {
            siguienteId = Math.max(siguienteId, c.getIdCliente() + 1);
        }

        setTitle("Gestión de Clientes");
        setSize(950, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarComponentes();
        mostrarClientes();
    }

    // INICIALIZAR COMPONENTES
    private void inicializarComponentes() {

        setLayout(new BorderLayout());

        // TABLA DE CLIENTES
        String[] columnas = {
            "ID", "Nombre", "Apellidos", "DNI",
            "Teléfono", "Email", "Dirección", "Frecuente"
        };

        modeloTabla = new DefaultTableModel(columnas, 0) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columna) {
                if (columna == 0) {
                    return Integer.class;
                }

                if (columna == 7) {
                    return Boolean.class;
                }

                return String.class;
            }
        };

        tablaClientes = new JTable(modeloTabla);
        tablaClientes.setRowHeight(25);

        tablaClientes.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        ordenador = new TableRowSorter<>(modeloTabla);
        tablaClientes.setRowSorter(ordenador);

        add(new JScrollPane(tablaClientes),
            BorderLayout.CENTER);

        // PANEL DE BÚSQUEDA
        JPanel panelBusqueda = new JPanel(new FlowLayout());

        panelBusqueda.add(new JLabel("Buscar cliente:"));

        txtBuscar = new JTextField(20);
        btnBuscar = new JButton("Buscar");
        btnMostrarTodos = new JButton("Mostrar todos");

        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(btnMostrarTodos);

        add(panelBusqueda, BorderLayout.NORTH);

        // PANEL DE BOTONES
        JPanel panelBotones = new JPanel(new FlowLayout());

        btnAnadir = new JButton("Añadir");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");

        panelBotones.add(btnAnadir);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);

        add(panelBotones, BorderLayout.SOUTH);

        // EVENTOS
        btnAnadir.addActionListener(e -> anadirCliente());

        btnModificar.addActionListener(e -> modificarCliente());

        btnEliminar.addActionListener(e -> eliminarCliente());

        btnBuscar.addActionListener(e -> buscarCliente());

        btnMostrarTodos.addActionListener(e -> {
            txtBuscar.setText("");
            ordenador.setRowFilter(null);
        });

        // BUSCAR CON ENTER
        txtBuscar.addActionListener(e -> buscarCliente());

        // DOBLE CLIC PARA MODIFICAR
        tablaClientes.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    modificarCliente();
                }
            }
        });

        // SUPR PARA ELIMINAR
        tablaClientes.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    eliminarCliente();
                }
            }
        });
    }

    // MOSTRAR CLIENTES
    public void mostrarClientes() {

        modeloTabla.setRowCount(0);

        for (Cliente cliente : clientes) {

            Object[] fila = {
                cliente.getIdCliente(),
                cliente.getNombre(),
                cliente.getApellidos(),
                cliente.getDni(),
                cliente.getTelefono(),
                cliente.getEmail(),
                cliente.getDireccion(),
                cliente.esClienteFrecuente()
            };

            modeloTabla.addRow(fila);
        }
    }

    // CREAR FORMULARIO
    private JPanel crearFormulario(
            JTextField txtNombre,
            JTextField txtApellidos,
            JTextField txtDni,
            JTextField txtTelefono,
            JTextField txtEmail,
            JTextField txtDireccion,
            JCheckBox chkFrecuente) {

        JPanel panel = new JPanel(
            new GridLayout(7, 2, 5, 5)
        );

        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);

        panel.add(new JLabel("Apellidos:"));
        panel.add(txtApellidos);

        panel.add(new JLabel("DNI:"));
        panel.add(txtDni);

        panel.add(new JLabel("Teléfono:"));
        panel.add(txtTelefono);

        panel.add(new JLabel("Email:"));
        panel.add(txtEmail);

        panel.add(new JLabel("Dirección:"));
        panel.add(txtDireccion);

        panel.add(new JLabel("Cliente frecuente:"));
        panel.add(chkFrecuente);

        return panel;
    }

    // AÑADIR CLIENTE
    public void anadirCliente() {

        JTextField txtNombre = new JTextField();
        JTextField txtApellidos = new JTextField();
        JTextField txtDni = new JTextField();
        JTextField txtTelefono = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtDireccion = new JTextField();

        JCheckBox chkFrecuente =
            new JCheckBox("Sí");

        JPanel panel = crearFormulario(
            txtNombre, txtApellidos, txtDni,
            txtTelefono, txtEmail, txtDireccion,
            chkFrecuente
        );

        while (true) {

            int opcion = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Añadir cliente",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
            );

            if (opcion != JOptionPane.OK_OPTION) {
                return;
            }

            String nombre = txtNombre.getText().trim();
            String apellidos = txtApellidos.getText().trim();
            String dni = txtDni.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String email = txtEmail.getText().trim();
            String direccion = txtDireccion.getText().trim();

            if (nombre.isEmpty() || apellidos.isEmpty()
                    || dni.isEmpty()) {

                JOptionPane.showMessageDialog(
                    this,
                    "Nombre, apellidos y DNI son obligatorios.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );

                continue;
            }

            if (existeDni(dni, -1)) {

                JOptionPane.showMessageDialog(
                    this,
                    "Ya existe un cliente con ese DNI.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );

                continue;
            }

            Cliente nuevoCliente = new Cliente(
                siguienteId,
                nombre,
                apellidos,
                dni,
                telefono,
                email,
                direccion
            );

            nuevoCliente.establecerClienteFrecuente(
                chkFrecuente.isSelected()
            );

            clientes.add(nuevoCliente);
            siguienteId++;

            mostrarClientes();

            JOptionPane.showMessageDialog(
                this,
                "Cliente añadido correctamente."
            );

            return;
        }
    }

    // MODIFICAR CLIENTE
    public void modificarCliente() {

        Cliente cliente = obtenerClienteSeleccionado();

        if (cliente == null) {
            return;
        }

        JTextField txtNombre =
            new JTextField(cliente.getNombre());

        JTextField txtApellidos =
            new JTextField(cliente.getApellidos());

        JTextField txtDni =
            new JTextField(cliente.getDni());

        JTextField txtTelefono =
            new JTextField(cliente.getTelefono());

        JTextField txtEmail =
            new JTextField(cliente.getEmail());

        JTextField txtDireccion =
            new JTextField(cliente.getDireccion());

        JCheckBox chkFrecuente =
            new JCheckBox("Sí");

        chkFrecuente.setSelected(
            cliente.esClienteFrecuente()
        );

        JPanel panel = crearFormulario(
            txtNombre, txtApellidos, txtDni,
            txtTelefono, txtEmail, txtDireccion,
            chkFrecuente
        );

        while (true) {

            int opcion = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Modificar cliente",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
            );

            if (opcion != JOptionPane.OK_OPTION) {
                return;
            }

            String nombre = txtNombre.getText().trim();
            String apellidos = txtApellidos.getText().trim();
            String dni = txtDni.getText().trim();

            if (nombre.isEmpty() || apellidos.isEmpty()
                    || dni.isEmpty()) {

                JOptionPane.showMessageDialog(
                    this,
                    "Nombre, apellidos y DNI son obligatorios.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );

                continue;
            }

            if (existeDni(dni, cliente.getIdCliente())) {

                JOptionPane.showMessageDialog(
                    this,
                    "Ese DNI pertenece a otro cliente.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );

                continue;
            }

            cliente.setNombre(nombre);
            cliente.setApellidos(apellidos);
            cliente.setDni(dni);

            cliente.setTelefono(
                txtTelefono.getText().trim()
            );

            cliente.setEmail(
                txtEmail.getText().trim()
            );

            cliente.setDireccion(
                txtDireccion.getText().trim()
            );

            cliente.establecerClienteFrecuente(
                chkFrecuente.isSelected()
            );

            mostrarClientes();

            JOptionPane.showMessageDialog(
                this,
                "Cliente modificado correctamente."
            );

            return;
        }
    }

    // ELIMINAR CLIENTE
    public void eliminarCliente() {

        Cliente cliente = obtenerClienteSeleccionado();

        if (cliente == null) {
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
            this,
            "¿Quieres eliminar al cliente "
                + cliente.getNombre() + " "
                + cliente.getApellidos() + "?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {

            clientes.remove(cliente);
            mostrarClientes();

            JOptionPane.showMessageDialog(
                this,
                "Cliente eliminado correctamente."
            );
        }
    }

    // BUSCAR CLIENTES
    public void buscarCliente() {

        String texto = txtBuscar.getText().trim();

        if (texto.isEmpty()) {
            ordenador.setRowFilter(null);
            return;
        }

        ordenador.setRowFilter(
            RowFilter.regexFilter(
                "(?i)" + Pattern.quote(texto),
                1, 2, 3
            )
        );
    }

    // COMPROBAR DNI DUPLICADO
    private boolean existeDni(String dni, int idExcluir) {

        for (Cliente cliente : clientes) {

            if (cliente.getIdCliente() != idExcluir
                    && cliente.getDni().equalsIgnoreCase(dni)) {

                return true;
            }
        }

        return false;
    }

    // OBTENER CLIENTE SELECCIONADO
    private Cliente obtenerClienteSeleccionado() {

        int filaVista = tablaClientes.getSelectedRow();

        if (filaVista == -1) {

            JOptionPane.showMessageDialog(
                this,
                "Selecciona primero un cliente.",
                "Aviso",
                JOptionPane.WARNING_MESSAGE
            );

            return null;
        }

        int filaModelo =
            tablaClientes.convertRowIndexToModel(filaVista);

        int id = (int) modeloTabla.getValueAt(
            filaModelo, 0
        );

        return buscarClientePorId(id);
    }

    // BUSCAR POR ID
    private Cliente buscarClientePorId(int id) {

        for (Cliente cliente : clientes) {

            if (cliente.getIdCliente() == id) {
                return cliente;
            }
        }

        return null;
    }

    // MAIN PARA PROBAR LA VENTANA
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            VentanaClientes ventana = new VentanaClientes();
            ventana.setVisible(true);

        });
    }
}
