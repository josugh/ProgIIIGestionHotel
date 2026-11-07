package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import domain.Empleado;
import domain.EstadoHabitacion;
import domain.Hotel;

public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;

	private Hotel hotel;
	private JLabel lblDisponibles = new JLabel("", SwingConstants.CENTER);
	private JLabel lblOcupadas = new JLabel("", SwingConstants.CENTER);
	private JLabel lblReservadas = new JLabel("", SwingConstants.CENTER);
	private JLabel lblOtras = new JLabel("", SwingConstants.CENTER);
	private Thread hiloResumen;

	public VentanaPrincipal(Hotel hotel, Empleado empleado) {
		this.hotel = hotel;

		setTitle("Gestión Hotel - Menú principal");
		setSize(620, 420);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		JLabel lblBienvenida = new JLabel("Bienvenido/a, " + empleado.getNombre() + " (" + empleado.getTipo() + ")", SwingConstants.CENTER);
		lblBienvenida.setFont(new Font("Arial", Font.BOLD, 18));
		lblBienvenida.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

		JPanel panelResumen = new JPanel(new GridLayout(1, 4, 10, 10));
		panelResumen.setBorder(BorderFactory.createTitledBorder("Resumen de habitaciones"));
		panelResumen.add(lblDisponibles);
		panelResumen.add(lblOcupadas);
		panelResumen.add(lblReservadas);
		panelResumen.add(lblOtras);

		JPanel panelArriba = new JPanel(new BorderLayout());
		panelArriba.add(lblBienvenida, BorderLayout.NORTH);
		panelArriba.add(panelResumen, BorderLayout.CENTER);

		JPanel panelBotones = new JPanel(new GridLayout(2, 3, 10, 10));
		panelBotones.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		JButton btnHabitaciones = new JButton("Habitaciones");
		JButton btnClientes = new JButton("Clientes");
		JButton btnReservas = new JButton("Reservas");
		JButton btnCheckInOut = new JButton("Check-in / Check-out");
		JButton btnFacturacion = new JButton("Facturación");
		JButton btnCerrarSesion = new JButton("Cerrar sesión");
		panelBotones.add(btnHabitaciones);
		panelBotones.add(btnClientes);
		panelBotones.add(btnReservas);
		panelBotones.add(btnCheckInOut);
		panelBotones.add(btnFacturacion);
		panelBotones.add(btnCerrarSesion);

		add(panelArriba, BorderLayout.NORTH);
		add(panelBotones, BorderLayout.CENTER);

		btnHabitaciones.addActionListener(e -> new PanelHabitaciones(hotel.getHabitaciones(), hotel.getIncidencias()).setVisible(true));
		btnClientes.addActionListener(e -> new VentanaClientes(hotel.getClientes()).setVisible(true));
		btnReservas.addActionListener(e -> new VentanaReservas(hotel.getClientes(), hotel.getHabitaciones(), hotel.getReservas()).setVisible(true));
		btnCheckInOut.addActionListener(e -> new VentanaCheckInOut(hotel.getReservas()).setVisible(true));
		btnFacturacion.addActionListener(e -> new VentanaFacturacion().setVisible(true));
		btnCerrarSesion.addActionListener(e -> cerrarSesion());

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				salir();
			}
		});

		actualizarResumen();
		iniciarHiloResumen();
		setVisible(true);
	}

	private void actualizarResumen() {
		int disponibles = hotel.contarHabitaciones(EstadoHabitacion.DISPONIBLE);
		int ocupadas = hotel.contarHabitaciones(EstadoHabitacion.OCUPADA);
		int reservadas = hotel.contarHabitaciones(EstadoHabitacion.RESERVADA);
		int otras = hotel.getHabitaciones().size() - disponibles - ocupadas - reservadas;
		lblDisponibles.setText("<html><center>Disponibles<br><b>" + disponibles + "</b></center></html>");
		lblOcupadas.setText("<html><center>Ocupadas<br><b>" + ocupadas + "</b></center></html>");
		lblReservadas.setText("<html><center>Reservadas<br><b>" + reservadas + "</b></center></html>");
		lblOtras.setText("<html><center>Limpieza / mant.<br><b>" + otras + "</b></center></html>");
	}

	private void iniciarHiloResumen() {
		hiloResumen = new Thread(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					return;
				}
				SwingUtilities.invokeLater(this::actualizarResumen);
			}
		});
		hiloResumen.setDaemon(true);
		hiloResumen.start();
	}

	private void cerrarSesion() {
		int opcion = JOptionPane.showConfirmDialog(this, "¿Quieres cerrar sesión?", "Cerrar sesión", JOptionPane.YES_NO_OPTION);
		if (opcion == JOptionPane.YES_OPTION) {
			hiloResumen.interrupt();
			for (Window w : Window.getWindows()) {
				if (w != this) {
					w.dispose();
				}
			}
			dispose();
			new VentanaLogin(hotel);
		}
	}

	private void salir() {
		int opcion = JOptionPane.showConfirmDialog(this, "¿Quieres salir de la aplicación?", "Salir", JOptionPane.YES_NO_OPTION);
		if (opcion == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}
}