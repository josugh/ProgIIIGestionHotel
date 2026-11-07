package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import domain.Cliente;
import domain.EstadoHabitacion;
import domain.Habitacion;
import domain.Hotel;
import domain.Reserva;

public class VentanaReservas extends JFrame {

    private static final long serialVersionUID = 1L;

    // ATRIBUTOS
    private List<Cliente> clientes;
    private List<Habitacion> habitaciones;
    private List<Reserva> reservas;

    private JTable tablaReservas;
    private DefaultTableModel modeloTabla;
    private TableRowSorter<DefaultTableModel> ordenador;

    private JTextField txtBuscar;

    private JButton btnCrear;
    private JButton btnModificar;
    private JButton btnCancelar;
    private JButton btnBuscar;
    private JButton btnMostrarTodos;

    private int siguienteId = 1;

    // CONSTRUCTOR PRINCIPAL
    public VentanaReservas(List<Cliente> clientes,
                           List<Habitacion> habitaciones,
                           List<Reserva> reservas) {

        this.clientes = clientes;
        this.habitaciones = habitaciones;
        this.reservas = reservas;

        for (Reserva r : reservas) {
            siguienteId = Math.max(
                siguienteId, r.getIdReserva() + 1);
        }

        setTitle("Gestión de Reservas");
        setSize(1050, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarComponentes();
        mostrarReservas();
    }

    // CONSTRUCTOR PARA PROBAR LA VENTANA
    public VentanaReservas() {
        this(new Hotel());
    }

    private VentanaReservas(Hotel hotel) {
        this(hotel.getClientes(), hotel.getHabitaciones(), hotel.getReservas());
    }

    // INICIALIZAR COMPONENTES
    private void inicializarComponentes() {

        setLayout(new BorderLayout());

        // TABLA
        String[] columnas = {
            "ID", "Cliente", "Habitación",
            "Entrada", "Salida", "Huéspedes",
            "Noches", "Estado"
        };

        modeloTabla = new DefaultTableModel(columnas, 0) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaReservas = new JTable(modeloTabla);
        tablaReservas.setRowHeight(25);
        tablaReservas.setSelectionMode(
            javax.swing.ListSelectionModel.SINGLE_SELECTION
        );

        ordenador = new TableRowSorter<>(modeloTabla);
        tablaReservas.setRowSorter(ordenador);

        add(new JScrollPane(tablaReservas),
            BorderLayout.CENTER);

        // PANEL DE BÚSQUEDA
        JPanel panelBusqueda = new JPanel(new FlowLayout());

        panelBusqueda.add(new JLabel("Buscar reserva:"));

        txtBuscar = new JTextField(20);
        btnBuscar = new JButton("Buscar");
        btnMostrarTodos = new JButton("Mostrar todos");

        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(btnMostrarTodos);

        add(panelBusqueda, BorderLayout.NORTH);

        // PANEL DE BOTONES
        JPanel panelBotones = new JPanel(new FlowLayout());

        btnCrear = new JButton("Crear reserva");
        btnModificar = new JButton("Modificar");
        btnCancelar = new JButton("Cancelar reserva");

        panelBotones.add(btnCrear);
        panelBotones.add(btnModificar);
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);

        // EVENTOS
        btnCrear.addActionListener(e -> crearReserva());
        btnModificar.addActionListener(e -> modificarReserva());
        btnCancelar.addActionListener(e -> cancelarReserva());
        btnBuscar.addActionListener(e -> buscarReserva());

        btnMostrarTodos.addActionListener(e -> {
            txtBuscar.setText("");
            ordenador.setRowFilter(null);
        });

        txtBuscar.addActionListener(e -> buscarReserva());

        // DOBLE CLIC PARA MODIFICAR
        tablaReservas.addMouseListener(
            new java.awt.event.MouseAdapter() {

                @Override
                public void mouseClicked(
                        java.awt.event.MouseEvent e) {

                    if (e.getClickCount() == 2) {
                        modificarReserva();
                    }
                }
            }
        );
    }

    // MOSTRAR RESERVAS
    public void mostrarReservas() {

        modeloTabla.setRowCount(0);

        for (Reserva reserva : reservas) {

            Object[] fila = {
                reserva.getIdReserva(),
                reserva.getCliente().mostrarInformacion(),
                nombreHabitacion(reserva.getHabitacion()),
                reserva.getFechaEntrada(),
                reserva.getFechaSalida(),
                reserva.getNumeroHuespedes(),
                reserva.calcularNoches(),
                reserva.getEstadoReserva()
            };

            modeloTabla.addRow(fila);
        }
    }

    // NOMBRE DE HABITACIÓN
    private String nombreHabitacion(Habitacion habitacion) {

        if (habitacion != null && habitaciones.contains(habitacion)) {
            return "Habitación " + habitacion.getNumero();
        }

        return "Habitación no encontrada";
    }

    // FORMULARIO DE RESERVA
    private Reserva formularioReserva(Reserva existente) {

        if (clientes.isEmpty() || habitaciones.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Debe haber clientes y habitaciones registrados.",
                "Aviso",
                JOptionPane.WARNING_MESSAGE
            );

            return null;
        }

        JComboBox<String> comboClientes =
            new JComboBox<>();

        for (Cliente c : clientes) {
            comboClientes.addItem(
                c.getIdCliente() + " - " + c.mostrarInformacion()
            );
        }

        JComboBox<String> comboHabitaciones =
            new JComboBox<>();

        for (int i = 0; i < habitaciones.size(); i++) {
            comboHabitaciones.addItem(
                    "Habitación " + habitaciones.get(i).getNumero()
                    + " (" + habitaciones.get(i).getTipo() + ")"
             );
        }

        JTextField txtEntrada = new JTextField(10);
        JTextField txtSalida = new JTextField(10);
        JTextField txtHuespedes = new JTextField(5);

        // RELLENAR DATOS SI SE MODIFICA
        if (existente != null) {

            comboClientes.setSelectedIndex(
                clientes.indexOf(existente.getCliente())
            );

            comboHabitaciones.setSelectedIndex(
                habitaciones.indexOf(existente.getHabitacion())
            );

            txtEntrada.setText(
                existente.getFechaEntrada().toString()
            );

            txtSalida.setText(
                existente.getFechaSalida().toString()
            );

            txtHuespedes.setText(
                String.valueOf(existente.getNumeroHuespedes())
            );
        } else {

            txtEntrada.setText(LocalDate.now().toString());

            txtSalida.setText(
                LocalDate.now().plusDays(1).toString()
            );

            txtHuespedes.setText("1");
        }

        JPanel panel = new JPanel(
            new GridLayout(5, 2, 5, 5)
        );

        panel.add(new JLabel("Cliente:"));
        panel.add(comboClientes);

        panel.add(new JLabel("Habitación:"));
        panel.add(comboHabitaciones);

        panel.add(new JLabel("Entrada (AAAA-MM-DD):"));
        panel.add(txtEntrada);

        panel.add(new JLabel("Salida (AAAA-MM-DD):"));
        panel.add(txtSalida);

        panel.add(new JLabel("Número de huéspedes:"));
        panel.add(txtHuespedes);

        while (true) {

            int opcion = JOptionPane.showConfirmDialog(
                this,
                panel,
                existente == null
                    ? "Nueva reserva" : "Modificar reserva",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
            );

            if (opcion != JOptionPane.OK_OPTION) {
                return null;
            }

            try {

                Cliente cliente = clientes.get(
                    comboClientes.getSelectedIndex()
                );

                Habitacion habitacion = habitaciones.get(
                    comboHabitaciones.getSelectedIndex()
                );

                LocalDate entrada = LocalDate.parse(
                    txtEntrada.getText().trim()
                );

                LocalDate salida = LocalDate.parse(
                    txtSalida.getText().trim()
                );

                int huespedes = Integer.parseInt(
                    txtHuespedes.getText().trim()
                );

                if (!salida.isAfter(entrada)) {
                    throw new IllegalArgumentException(
                        "La salida debe ser posterior a la entrada."
                    );
                }

                if (huespedes <= 0) {
                    throw new IllegalArgumentException(
                        "El número de huéspedes debe ser positivo."
                    );
                }

                int id = existente == null
                    ? siguienteId
                    : existente.getIdReserva();

                Reserva nueva = new Reserva(
                    id, cliente, habitacion,
                    entrada, salida, huespedes
                );
                
                if (habitacion.getEstado() == EstadoHabitacion.MANTENIMIENTO) {

                    JOptionPane.showMessageDialog(
                        this,
                        "La habitación " + habitacion.getNumero()
                        + " está en mantenimiento y no se puede reservar.",
                        "No disponible",
                        JOptionPane.WARNING_MESSAGE
                    );

                    continue;
                }

                if (!comprobarDisponibilidad(nueva, existente)) {

                    JOptionPane.showMessageDialog(
                        this,
                        "La habitación ya está reservada "
                        + "para esas fechas.",
                        "No disponible",
                        JOptionPane.WARNING_MESSAGE
                    );

                    continue;
                }

                return nueva;

            } catch (DateTimeParseException ex) {

                JOptionPane.showMessageDialog(
                    this,
                    "Introduce las fechas con formato AAAA-MM-DD.",
                    "Error de fecha",
                    JOptionPane.ERROR_MESSAGE
                );

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                    this,
                    "El número de huéspedes debe ser un entero.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );

            } catch (IllegalArgumentException
                     | IndexOutOfBoundsException ex) {

                JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    // CREAR RESERVA
    public void crearReserva() {

        Reserva nueva = formularioReserva(null);

        if (nueva == null) {
            return;
        }

        reservas.add(nueva);
        siguienteId++;

        if (nueva.getHabitacion().estaDisponible()) {
            nueva.getHabitacion().cambiarEstado(EstadoHabitacion.RESERVADA);
        }

        mostrarReservas();

        JOptionPane.showMessageDialog(
            this,
            "Reserva creada correctamente."
        );
    }

    // MODIFICAR RESERVA
    public void modificarReserva() {

        Reserva original = obtenerReservaSeleccionada();

        if (original == null) {
            return;
        }

        if (original.estaCancelada()) {

            JOptionPane.showMessageDialog(
                this,
                "No se puede modificar una reserva cancelada."
            );

            return;
        }

        Reserva nueva = formularioReserva(original);

        if (nueva == null) {
            return;
        }

        original.modificarCliente(nueva.getCliente());
        original.modificarHabitacion(nueva.getHabitacion());

        original.modificarFechas(
            nueva.getFechaEntrada(),
            nueva.getFechaSalida()
        );

        original.setNumeroHuespedes(
            nueva.getNumeroHuespedes()
        );

        mostrarReservas();

        JOptionPane.showMessageDialog(
            this,
            "Reserva modificada correctamente."
        );
    }

    // CANCELAR RESERVA
    public void cancelarReserva() {

        Reserva reserva = obtenerReservaSeleccionada();

        if (reserva == null) {
            return;
        }

        if (reserva.estaCancelada()) {

            JOptionPane.showMessageDialog(
                this,
                "Esta reserva ya está cancelada."
            );

            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
            this,
            "¿Quieres cancelar la reserva "
            + reserva.getIdReserva() + "?",
            "Confirmar cancelación",
            JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {

            reserva.cancelarReserva();

            if (reserva.getHabitacion().getEstado() == EstadoHabitacion.RESERVADA) {
                reserva.getHabitacion().cambiarEstado(EstadoHabitacion.DISPONIBLE);
            }
            
            mostrarReservas();

            JOptionPane.showMessageDialog(
                this,
                "Reserva cancelada correctamente."
            );
        }
    }

    // COMPROBAR DISPONIBILIDAD
    private boolean comprobarDisponibilidad(
            Reserva nueva, Reserva original) {

        for (Reserva existente : reservas) {

            if (existente == original) {
                continue;
            }

            if (nueva.seSolapaCon(existente)) {
                return false;
            }
        }

        return true;
    }

    // BUSCAR RESERVA
    public void buscarReserva() {

        String texto = txtBuscar.getText().trim();

        if (texto.isEmpty()) {
            ordenador.setRowFilter(null);
            return;
        }

        ordenador.setRowFilter(
            RowFilter.regexFilter(
                "(?i)" + java.util.regex.Pattern.quote(texto),
                0, 1, 2, 3, 4, 7
            )
        );
    }

    // OBTENER RESERVA SELECCIONADA
    private Reserva obtenerReservaSeleccionada() {

        int filaVista = tablaReservas.getSelectedRow();

        if (filaVista == -1) {

            JOptionPane.showMessageDialog(
                this,
                "Selecciona primero una reserva.",
                "Aviso",
                JOptionPane.WARNING_MESSAGE
            );

            return null;
        }

        int filaModelo =
            tablaReservas.convertRowIndexToModel(filaVista);

        int id = (int) modeloTabla.getValueAt(
            filaModelo, 0
        );

        for (Reserva reserva : reservas) {

            if (reserva.getIdReserva() == id) {
                return reserva;
            }
        }

        return null;
    }

    // MAIN PARA PROBAR LA VENTANA
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new VentanaReservas().setVisible(true);
        });
    }
}
