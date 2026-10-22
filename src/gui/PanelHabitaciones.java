package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import domain.EstadoHabitacion;
import domain.Habitacion;
import domain.TipoHabitacion;

public class PanelHabitaciones extends JPanel {

	private static final long serialVersionUID = 1L;
	
	//Componentes
	private List<Habitacion> habitaciones = new ArrayList<>();
	private HabitacionTableModel modelo;
	private JTable tabla;
	private JTextField txtBuscar = new JTextField(10);
	
	//Constructor
	public PanelHabitaciones() {
		habitaciones.add(new Habitacion(101, 1, TipoHabitacion.INDIVIDUAL, 55, EstadoHabitacion.DISPONIBLE));
		habitaciones.add(new Habitacion(102, 1, TipoHabitacion.DOBLE, 80, EstadoHabitacion.OCUPADA));
		habitaciones.add(new Habitacion(103, 1, TipoHabitacion.DOBLE, 80, EstadoHabitacion.RESERVADA));
		habitaciones.add(new Habitacion(201, 2, TipoHabitacion.TRIPLE, 105, EstadoHabitacion.DISPONIBLE));
		habitaciones.add(new Habitacion(202, 2, TipoHabitacion.DOBLE, 80, EstadoHabitacion.MANTENIMIENTO));
		habitaciones.add(new Habitacion(301, 3, TipoHabitacion.SUITE, 180, EstadoHabitacion.OCUPADA));

		modelo = new HabitacionTableModel(habitaciones);
		tabla = new JTable(modelo);
		tabla.setDefaultRenderer(Object.class, new HabitacionRenderer());
		tabla.setRowHeight(25);

		JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel titulo = new JLabel("Habitaciones");
		titulo.setFont(titulo.getFont().deriveFont(20f)); //deriveFont: crea una fuente nueva a partir de la que ya tiene el componente, cambiando solo lo que le indicas
		panelNorte.add(titulo);
		panelNorte.add(new JLabel("     Buscar por número:"));
		panelNorte.add(txtBuscar);

		JButton btnAnadir = new JButton("Añadir");
		JButton btnEditar = new JButton("Editar");
		JButton btnEliminar = new JButton("Eliminar");
		JButton btnCheckIn = new JButton("Check-in");
		JButton btnCheckOut = new JButton("Check-out");

		JPanel panelBotones = new JPanel(new FlowLayout());
		panelBotones.add(btnAnadir);
		panelBotones.add(btnEditar);
		panelBotones.add(btnEliminar);
		panelBotones.add(btnCheckIn);
		panelBotones.add(btnCheckOut);

		setLayout(new BorderLayout(10, 10));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(panelNorte, BorderLayout.NORTH);
		add(new JScrollPane(tabla), BorderLayout.CENTER);
		add(panelBotones, BorderLayout.SOUTH);

		txtBuscar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				buscar();
			}
		});

		tabla.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					editar();
				}
			}
		});

		btnAnadir.addActionListener(e -> anadir());
		btnEditar.addActionListener(e -> editar());
		btnEliminar.addActionListener(e -> eliminar());
		btnCheckIn.addActionListener(e -> checkIn());
		btnCheckOut.addActionListener(e -> checkOut());
	}
	
	//Funcion Buscar
	private void buscar() {
		String texto = txtBuscar.getText().trim();
		List<Habitacion> resultado = new ArrayList<>();
		for (Habitacion h : habitaciones) {
			if (String.valueOf(h.getNumero()).startsWith(texto)) {
				resultado.add(h);
			}
		}
		modelo.setHabitaciones(resultado);
	}
	
	private Habitacion seleccionada() {
		int fila = tabla.getSelectedRow();
		if (fila == -1) {
			JOptionPane.showMessageDialog(this, "Selecciona una habitación.");
			return null;
		}
		return modelo.getHabitacion(fila);
	}
	
	//Funcion Anadir
	private void anadir() {
		DialogoHabitacion dialogo = new DialogoHabitacion(null, habitaciones);
		dialogo.setVisible(true);
		if (dialogo.isGuardado()) {
			habitaciones.add(dialogo.getHabitacion());
			buscar();
		}
	}
	
	//Funcion Editar
	private void editar() {
		Habitacion h = seleccionada();
		if (h == null) {
			return;
		}
		DialogoHabitacion dialogo = new DialogoHabitacion(h, habitaciones);
		dialogo.setVisible(true);
		if (dialogo.isGuardado()) {
			buscar();
		}
	}
	
	//Funcion Eliminar
	private void eliminar() {
		Habitacion h = seleccionada();
		if (h == null) {
			return;
		}
		int opcion = JOptionPane.showConfirmDialog(this, "¿Eliminar la habitación " + h.getNumero() + "?",
				"Eliminar", JOptionPane.YES_NO_OPTION);
		if (opcion == JOptionPane.YES_OPTION) {
			habitaciones.remove(h);
			buscar();
		}
	}
	
	//Funcion Check-In
	private void checkIn() {
		Habitacion h = seleccionada();
		if (h == null) {
			return;
		}
		if (h.getEstado() != EstadoHabitacion.DISPONIBLE && h.getEstado() != EstadoHabitacion.RESERVADA) {
			JOptionPane.showMessageDialog(this, "La habitación no está disponible.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		String cliente = JOptionPane.showInputDialog(this, "Nombre del cliente:");
		if (cliente != null && !cliente.trim().isEmpty()) {
			h.setEstado(EstadoHabitacion.OCUPADA);
			modelo.fireTableDataChanged();
			JOptionPane.showMessageDialog(this, "Check-in realizado para " + cliente + ".");
		}
	}
	
	//Funcion Check-Out
	private void checkOut() {
		Habitacion h = seleccionada();
		if (h == null) {
			return;
		}
		if (h.getEstado() != EstadoHabitacion.OCUPADA) {
			JOptionPane.showMessageDialog(this, "La habitación no está ocupada.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		String texto = JOptionPane.showInputDialog(this, "Número de noches:");
		if (texto == null) {
			return;
		}
		try {
			int noches = Integer.parseInt(texto.trim());
			double importe = noches * h.getPrecio();
			JOptionPane.showMessageDialog(this, "Importe a pagar: " + importe + " €");
			h.setEstado(EstadoHabitacion.DISPONIBLE);
			modelo.fireTableDataChanged();
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Escribe un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	//Main
	public static void main(String[] args) {
		JFrame ventana = new JFrame("Habitaciones");
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ventana.add(new PanelHabitaciones());
		ventana.setSize(700, 450);
		ventana.setLocationRelativeTo(null);
		ventana.setVisible(true);
	}
}