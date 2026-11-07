
package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class VentanaCheckInOut extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTabbedPane pestanas; // con esto conseguimos qu ese hagan pestañas en vez de hacer diferentes ventanas 

    private JComboBox<String> cbReservaEntrada;
    private JTextField txtClienteEntrada;
    private JTextField txtHabitacionEntrada;
    private JTextField txtFechaEntrada;
    private JSpinner spHuespedes;

    private JComboBox<String> cbReservaSalida;
    private JTextField txtClienteSalida;
    private JTextField txtHabitacionSalida;
    private JTextField txtFechaSalida;

    // Constructor de la ventana
    public VentanaCheckInOut() {
        setTitle("Gestion Hotel - Check-in / Check-out");
        setSize(480, 330);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        crearVentana();
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
        btnFecha.addActionListener(e -> seleccionarFecha(txtFechaSalida));
        btnConfirmar.addActionListener(e -> confirmarCheckOut());

        return panel;
    }

    // Busca la reserva seleccionada
    private void buscarReserva(boolean entrada) {
        JOptionPane.showMessageDialog(this,
                "La busqueda de reservas se conectara mas adelante.");
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

        JOptionPane.showMessageDialog(this,
                "Datos del check-in preparados.");
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

        JOptionPane.showMessageDialog(this,
                "Datos del check-out preparados.");
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
