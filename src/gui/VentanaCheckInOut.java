package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import domain.EstadoHabitacion;
import domain.EstadoReserva;
import domain.Habitacion;
import domain.Hotel;
import domain.Reserva;

public class VentanaCheckInOut extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTabbedPane pestanas; // con esto conseguimos qu ese hagan pestañas en vez de hacer diferentes ventanas 

    private List<Reserva> reservas;

    private JComboBox<Reserva> cbReservaEntrada;
    private JTextField txtClienteEntrada;
    private JTextField txtHabitacionEntrada;
    private JTextField txtFechaEntrada;
    private JSpinner spHuespedes;

    private JComboBox<Reserva> cbReservaSalida;
    private JTextField txtClienteSalida;
    private JTextField txtHabitacionSalida;
    private JTextField txtFechaSalida;

    // Constructor de la ventana
    public VentanaCheckInOut() {
        this(new Hotel().getReservas());
    }

    public VentanaCheckInOut(List<Reserva> reservas) {
        this.reservas = reservas;
        setTitle("Gestion Hotel - Check-in / Check-out");
        setSize(640, 330);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        crearVentana();
        cargarReservas();
    }

    private void cargarReservas() {
        cbReservaEntrada.removeAllItems();
        cbReservaSalida.removeAllItems();
        for (Reserva r : reservas) {
            if (r.getEstadoReserva() == EstadoReserva.CONFIRMADA) {
                cbReservaEntrada.addItem(r);
            } else if (r.getEstadoReserva() == EstadoReserva.EN_CURSO) {
                cbReservaSalida.addItem(r);
            }
        }
        limpiarCampos();
        mostrarDatos(true);
        mostrarDatos(false);
    }

    private void limpiarCampos() {
        txtClienteEntrada.setText("");
        txtHabitacionEntrada.setText("");
        txtFechaEntrada.setText("");
        spHuespedes.setValue(1);
        txtClienteSalida.setText("");
        txtHabitacionSalida.setText("");
        txtFechaSalida.setText("");
    }

    // Creamos las dos pestanas
    private void crearVentana() {
        pestanas = new JTabbedPane();

        pestanas.addTab("Check-in", crearPanelCheckIn());
        pestanas.addTab("Check-out", crearPanelCheckOut());

        add(pestanas);
    }

    // Panel para registrar la entrada del cliente
    private JPanel crearPanelCheckIn() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBorder(new TitledBorder("Realizar check-in"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cbReservaEntrada = new JComboBox<>();
        txtClienteEntrada = new JTextField(18);
        txtHabitacionEntrada = new JTextField(18);
        txtFechaEntrada = new JTextField(12);

        txtClienteEntrada.setEditable(false);
        txtHabitacionEntrada.setEditable(false);

        spHuespedes = new JSpinner(
                new SpinnerNumberModel(1, 1, 10, 1));

        JButton btnBuscar = new JButton("Buscar");
        JButton btnFecha = new JButton("...");
        JButton btnConfirmar = new JButton("Confirmar check-in");

        // Fila de reserva
        gbc.gridx = 0;
        gbc.gridy = 0;
        formulario.add(new JLabel("Reserva:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        formulario.add(cbReservaEntrada, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        formulario.add(btnBuscar, gbc);

        // Fila del cliente
        gbc.gridx = 0;
        gbc.gridy = 1;
        formulario.add(new JLabel("Cliente:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        formulario.add(txtClienteEntrada, gbc);
        gbc.gridwidth = 1;

        // Fila de habitacion
        gbc.gridx = 0;
        gbc.gridy = 2;
        formulario.add(new JLabel("Habitacion:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        formulario.add(txtHabitacionEntrada, gbc);
        gbc.gridwidth = 1;

        // Fila de fecha
        gbc.gridx = 0;
        gbc.gridy = 3;
        formulario.add(new JLabel("Fecha entrada:"), gbc);

        gbc.gridx = 1;
        formulario.add(txtFechaEntrada, gbc);

        gbc.gridx = 2;
        formulario.add(btnFecha, gbc);

        // Fila de huespedes
        gbc.gridx = 0;
        gbc.gridy = 4;
        formulario.add(new JLabel("Huespedes:"), gbc);

        gbc.gridx = 1;
        formulario.add(spHuespedes, gbc);

        panel.add(formulario, BorderLayout.CENTER);

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.add(btnConfirmar);
        panel.add(panelBoton, BorderLayout.SOUTH);

        // Eventos de los botones
        btnBuscar.addActionListener(e -> buscarReserva(true));
        cbReservaEntrada.addActionListener(e -> mostrarDatos(true));
        btnFecha.addActionListener(e -> seleccionarFecha(txtFechaEntrada));
        btnConfirmar.addActionListener(e -> confirmarCheckIn());

        return panel;
    }

    // Panel para registrar la salida del cliente
    private JPanel crearPanelCheckOut() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBorder(new TitledBorder("Realizar check-out"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cbReservaSalida = new JComboBox<>();
        txtClienteSalida = new JTextField(18);
        txtHabitacionSalida = new JTextField(18);
        txtFechaSalida = new JTextField(12);

        txtClienteSalida.setEditable(false);
        txtHabitacionSalida.setEditable(false);

        JButton btnBuscar = new JButton("Buscar");
        JButton btnFecha = new JButton("...");
        JButton btnConfirmar = new JButton("Confirmar check-out");

        gbc.gridx = 0;
        gbc.gridy = 0;
        formulario.add(new JLabel("Reserva:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        formulario.add(cbReservaSalida, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        formulario.add(btnBuscar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formulario.add(new JLabel("Cliente:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        formulario.add(txtClienteSalida, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 2;
        formulario.add(new JLabel("Habitacion:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        formulario.add(txtHabitacionSalida, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 3;
        formulario.add(new JLabel("Fecha salida:"), gbc);

        gbc.gridx = 1;
        formulario.add(txtFechaSalida, gbc);

        gbc.gridx = 2;
        formulario.add(btnFecha, gbc);

        panel.add(formulario, BorderLayout.CENTER);

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.add(btnConfirmar);
        panel.add(panelBoton, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> buscarReserva(false));
        cbReservaSalida.addActionListener(e -> mostrarDatos(false));
        btnFecha.addActionListener(e -> seleccionarFecha(txtFechaSalida));
        btnConfirmar.addActionListener(e -> confirmarCheckOut());

        return panel;
    }

    // Busca la reserva seleccionada
    private void buscarReserva(boolean entrada) {
        Reserva r = (Reserva) (entrada ? cbReservaEntrada : cbReservaSalida).getSelectedItem();
        if (r == null) {
            JOptionPane.showMessageDialog(this,
                    "No hay reservas pendientes de " + (entrada ? "check-in." : "check-out."));
            return;
        }
        mostrarDatos(entrada);
    }

    private void mostrarDatos(boolean entrada) {
        Reserva r = (Reserva) (entrada ? cbReservaEntrada : cbReservaSalida).getSelectedItem();
        if (r == null) {
            return;
        }
        String cliente = r.getCliente().mostrarInformacion();
        String habitacion = r.getHabitacion().toString();
        if (entrada) {
            txtClienteEntrada.setText(cliente);
            txtHabitacionEntrada.setText(habitacion);
            txtFechaEntrada.setText(r.getFechaEntrada().toString());
            spHuespedes.setValue(r.getNumeroHuespedes());
        } else {
            txtClienteSalida.setText(cliente);
            txtHabitacionSalida.setText(habitacion);
            txtFechaSalida.setText(r.getFechaSalida().toString());
        }
    }

    // Coloca la fecha actual en el campo
    private void seleccionarFecha(JTextField campo) {
        campo.setText(LocalDate.now().toString());
    }

    // Comprueba los datos del check-in
    private void confirmarCheckIn() {
        if (cbReservaEntrada.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona una reserva.");
            return;
        }

        if (!fechaValida(txtFechaEntrada.getText())) {
            return;
        }

        Reserva r = (Reserva) cbReservaEntrada.getSelectedItem();
        Habitacion h = r.getHabitacion();
        if (h.getEstado() != EstadoHabitacion.DISPONIBLE && h.getEstado() != EstadoHabitacion.RESERVADA) {
            JOptionPane.showMessageDialog(this,
                    "La habitacion " + h.getNumero() + " esta " + h.getEstado() + ".",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        r.setNumeroHuespedes((Integer) spHuespedes.getValue());
        r.setEstadoReserva(EstadoReserva.EN_CURSO);
        h.cambiarEstado(EstadoHabitacion.OCUPADA);
        JOptionPane.showMessageDialog(this,
                "Check-in realizado: " + r.getCliente().mostrarInformacion()
                + " en la habitacion " + h.getNumero() + ".");
        cargarReservas();
    }

    // Comprueba los datos del check-out
    private void confirmarCheckOut() {
        if (cbReservaSalida.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona una reserva.");
            return;
        }

        if (!fechaValida(txtFechaSalida.getText())) {
            return;
        }

        Reserva r = (Reserva) cbReservaSalida.getSelectedItem();
        Habitacion h = r.getHabitacion();
        r.setEstadoReserva(EstadoReserva.FINALIZADA);
        h.cambiarEstado(EstadoHabitacion.LIMPIEZA);
        PanelHabitaciones.iniciarLimpieza(h);
        JOptionPane.showMessageDialog(this,
                "Check-out realizado: " + r.getCliente().mostrarInformacion()
                + "\nNoches: " + r.calcularNoches()
                + "\nLa habitacion " + h.getNumero() + " estara disponible cuando termine la limpieza.");
        cargarReservas();
    }

    // Comprueba que la fecha sea correcta
    private boolean fechaValida(String fecha) {
        try {
            LocalDate.parse(fecha);
            return true;
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                    "Introduce una fecha valida (AAAA-MM-DD).");
            return false;
        }
    }
}