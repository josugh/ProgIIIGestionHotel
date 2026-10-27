
package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class VentanaCheckInOut extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTextField txtReserva;
    private JTextField txtFecha;
    private JSpinner spHuespedes;
    private JButton btnCheckIn;
    private JButton btnCheckOut;
    private JButton btnConfirmar;
    private boolean esCheckIn;

    // Constructor de la ventana
    public VentanaCheckInOut() {
        setTitle("Gestion Hotel - Check-in / Check-out");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        esCheckIn = true;

        crearVentana();
    }

    // Creamos los componentes de la ventana
    private void crearVentana() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(panel);

        JLabel lblTitulo = new JLabel(
                "Gestion de entradas y salidas", SwingConstants.CENTER);
        panel.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new BorderLayout(10, 10));
        panel.add(panelCentral, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 10, 0));

        btnCheckIn = new JButton("Check-in");
        btnCheckOut = new JButton("Check-out");

        panelBotones.add(btnCheckIn);
        panelBotones.add(btnCheckOut);
        panelCentral.add(panelBotones, BorderLayout.NORTH);

        JPanel panelDatos = new JPanel(new GridLayout(3, 2, 10, 15));

        txtReserva = new JTextField();
        txtFecha = new JTextField("AAAA-MM-DD");
        spHuespedes = new JSpinner(
                new SpinnerNumberModel(1, 1, 10, 1));

        panelDatos.add(new JLabel("Numero de reserva:"));
        panelDatos.add(txtReserva);

        panelDatos.add(new JLabel("Fecha:"));
        panelDatos.add(txtFecha);

        panelDatos.add(new JLabel("Numero de huespedes:"));
        panelDatos.add(spHuespedes);

        panelCentral.add(panelDatos, BorderLayout.CENTER);

        btnConfirmar = new JButton("Confirmar check-in");
        panel.add(btnConfirmar, BorderLayout.SOUTH);

        // Eventos de los botones
        btnCheckIn.addActionListener(e -> cambiarModo(true));
        btnCheckOut.addActionListener(e -> cambiarModo(false));
        btnConfirmar.addActionListener(e -> confirmarOperacion());
    }

    // Cambia entre entrada y salida
    private void cambiarModo(boolean checkIn) {
        esCheckIn = checkIn;

        if (esCheckIn) {
            btnConfirmar.setText("Confirmar check-in");
            spHuespedes.setEnabled(true);
        } else {
            btnConfirmar.setText("Confirmar check-out");
            spHuespedes.setEnabled(false);
        }
    }

    // Comprueba los datos introducidos
    private void confirmarOperacion() {
        String reserva = txtReserva.getText().trim();
        String fecha = txtFecha.getText().trim();

        if (reserva.isEmpty() || fecha.isEmpty()
                || fecha.equals("AAAA-MM-DD")) {

            JOptionPane.showMessageDialog(this,
                    "Introduce la reserva y la fecha.");
            return;
        }

        try {
            java.time.LocalDate.parse(fecha);
        } catch (java.time.format.DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "La fecha debe tener el formato AAAA-MM-DD.");
            return;
        }

        if (esCheckIn) {
            JOptionPane.showMessageDialog(this,
                    "Datos del check-in preparados para la reserva "
                    + reserva);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Datos del check-out preparados para la reserva "
                    + reserva);
        }
    }
}
