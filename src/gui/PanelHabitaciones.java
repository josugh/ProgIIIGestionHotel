package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import domain.EstadoHabitacion;
import domain.Hotel;
import domain.Habitacion;
import domain.IncidenciasHabitacion;
import domain.TipoHabitacion;

public class PanelHabitaciones extends JFrame {

	private static final long serialVersionUID = 1L;

	private List<Habitacion> habitaciones;
	private List<Habitacion> visibles = new ArrayList<>();
	private List<IncidenciasHabitacion> incidencias;
	private int siguienteIdIncidencia = 1;

	private DefaultTableModel modelo;
	private JTable tabla;
	private JTextField txtBuscar;
	private JComboBox<Object> cbEstado;
	private JLabel lblResumen;

	public PanelHabitaciones() {
		this(new Hotel());
	}

	private PanelHabitaciones(Hotel hotel) {
		this(hotel.getHabitaciones(), hotel.getIncidencias());
	}

	public PanelHabitaciones(List<Habitacion> habitaciones, List<IncidenciasHabitacion> incidencias) {
		this.habitaciones = habitaciones;
		this.incidencias = incidencias;
		for (IncidenciasHabitacion inc : incidencias) {
			siguienteIdIncidencia = Math.max(siguienteIdIncidencia, inc.getIdIncidencia() + 1);
		}
		setTitle("Habitaciones");
		setSize(900, 520);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel panelArriba = new JPanel(new FlowLayout(FlowLayout.LEFT));
		txtBuscar = new JTextField(8);
		cbEstado = new JComboBox<>();
		cbEstado.addItem("Todos");
		for (EstadoHabitacion e : EstadoHabitacion.values()) {
			cbEstado.addItem(e);
		}
		panelArriba.add(new JLabel("Buscar por número:"));
		panelArriba.add(txtBuscar);
		panelArriba.add(new JLabel("Estado:"));
		panelArriba.add(cbEstado);

		modelo = new DefaultTableModel(new String[] { "Número", "Planta", "Tipo", "Precio/noche", "Estado" }, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		tabla = new JTable(modelo);
		tabla.setRowHeight(24);
		tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabla.getTableHeader().setReorderingAllowed(false);
		tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				setHorizontalAlignment(CENTER);
				if (!isSelected) {
					c.setBackground(colorEstado(visibles.get(row).getEstado()));
				}
				return c;
			}
		});

		JPanel panelBotones = new JPanel(new GridLayout(2, 3, 5, 5));
		panelBotones.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		JButton btnAnadir = new JButton("Añadir");
		JButton btnEditar = new JButton("Editar");
		JButton btnEliminar = new JButton("Eliminar");
		JButton btnTarifas = new JButton("Tarifas");
		JButton btnIncidencia = new JButton("Nueva incidencia");
		JButton btnVerIncidencias = new JButton("Ver incidencias");
		panelBotones.add(btnAnadir);
		panelBotones.add(btnEditar);
		panelBotones.add(btnEliminar);
		panelBotones.add(btnIncidencia);
		panelBotones.add(btnTarifas);
		panelBotones.add(btnVerIncidencias);

		lblResumen = new JLabel();
		lblResumen.setBorder(BorderFactory.createEmptyBorder(0, 8, 5, 8));

		JPanel panelAbajo = new JPanel(new BorderLayout());
		panelAbajo.add(panelBotones, BorderLayout.CENTER);
		panelAbajo.add(lblResumen, BorderLayout.SOUTH);

		add(panelArriba, BorderLayout.NORTH);
		add(new JScrollPane(tabla), BorderLayout.CENTER);
		add(panelAbajo, BorderLayout.SOUTH);

		txtBuscar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				cargarTabla();
			}
		});
		
		cbEstado.addActionListener(e -> cargarTabla());

		tabla.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					editar();
				}
			}
		});
		
		tabla.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					eliminar();
				}
			}
		});

		btnAnadir.addActionListener(e -> anadir());
		btnEditar.addActionListener(e -> editar());
		btnEliminar.addActionListener(e -> eliminar());
		btnTarifas.addActionListener(e -> cambiarTarifas());
		btnIncidencia.addActionListener(e -> nuevaIncidencia());
		btnVerIncidencias.addActionListener(e -> verIncidencias());

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				cargarTabla();
			}
		});

		cargarTabla();
	}

	private Color colorEstado(EstadoHabitacion estado) {
		switch (estado) {
		case DISPONIBLE:
			return new Color(200, 240, 200);
		case OCUPADA:
			return new Color(250, 200, 200);
		case RESERVADA:
			return new Color(255, 235, 180);
		case LIMPIEZA:
			return new Color(200, 220, 250);
		default:
			return Color.LIGHT_GRAY;
		}
	}

	private void cargarTabla() {
		String texto = txtBuscar.getText().trim();
		Object estado = cbEstado.getSelectedItem();
		visibles.clear();
		for (Habitacion h : habitaciones) {
			boolean coincideNumero = String.valueOf(h.getNumero()).contains(texto);
			boolean coincideEstado = estado.equals("Todos") || h.getEstado() == estado;
			if (coincideNumero && coincideEstado) {
				visibles.add(h);
			}
		}
		modelo.setRowCount(0);
		for (Habitacion h : visibles) {
			modelo.addRow(new Object[] { h.getNumero(), h.getPlanta(), h.getTipo(), h.getPrecioNoche() + " €", h.getEstado() });
		}
		actualizarResumen();
	}

	private void actualizarResumen() {
		int[] contador = new int[EstadoHabitacion.values().length];
		for (Habitacion h : habitaciones) {
			contador[h.getEstado().ordinal()]++;
		}
		String texto = "Total: " + habitaciones.size();
		for (EstadoHabitacion e : EstadoHabitacion.values()) {
			texto += "   |   " + e + ": " + contador[e.ordinal()];
		}
		lblResumen.setText(texto);
	}

	private Habitacion seleccionada() {
		int fila = tabla.getSelectedRow();
		if (fila == -1) {
			JOptionPane.showMessageDialog(this, "Selecciona una habitación de la tabla", "Aviso", JOptionPane.WARNING_MESSAGE);
			return null;
		}
		return visibles.get(fila);
	}

	private Habitacion buscarPorNumero(int numero) {
		for (Habitacion h : habitaciones) {
			if (h.getNumero() == numero) {
				return h;
			}
		}
		return null;
	}

	private Habitacion formulario(Habitacion h) {
		JTextField txtNumero = new JTextField(h == null ? "" : String.valueOf(h.getNumero()));
		JTextField txtPlanta = new JTextField(h == null ? "" : String.valueOf(h.getPlanta()));
		JComboBox<TipoHabitacion> cbTipo = new JComboBox<>(TipoHabitacion.values());
		JTextField txtPrecio = new JTextField(h == null ? "" : String.valueOf(h.getPrecioNoche()));
		JComboBox<EstadoHabitacion> cbEst = new JComboBox<>(EstadoHabitacion.values());
		if (h != null) {
			txtNumero.setEditable(false);
			cbTipo.setSelectedItem(h.getTipo());
			cbEst.setSelectedItem(h.getEstado());
		}

		Object[] campos = { "Número:", txtNumero, "Planta:", txtPlanta, "Tipo:", cbTipo, "Precio por noche:", txtPrecio, "Estado:", cbEst };
		int opcion = JOptionPane.showConfirmDialog(this, campos, "Datos de la habitación", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (opcion != JOptionPane.OK_OPTION) {
			return null;
		}

		int numero;
		int planta;
		double precio;
		try {
			numero = Integer.parseInt(txtNumero.getText().trim());
			planta = Integer.parseInt(txtPlanta.getText().trim());
			precio = Double.parseDouble(txtPrecio.getText().trim().replace(",", "."));
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Número, planta y precio tienen que ser números", "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if (numero <= 0 || planta < 0 || precio <= 0) {
			JOptionPane.showMessageDialog(this, "Los valores tienen que ser positivos", "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if (h == null && buscarPorNumero(numero) != null) {
			JOptionPane.showMessageDialog(this, "Ya existe la habitación " + numero, "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		return new Habitacion(numero, planta, (TipoHabitacion) cbTipo.getSelectedItem(), precio, (EstadoHabitacion) cbEst.getSelectedItem());
	}

	private void anadir() {
		Habitacion nueva = formulario(null);
		if (nueva != null) {
			habitaciones.add(nueva);
			cargarTabla();
		}
	}

	private void editar() {
		Habitacion h = seleccionada();
		if (h == null) {
			return;
		}
		Habitacion datos = formulario(h);
		if (datos == null) {
			return;
		}
		h.setPlanta(datos.getPlanta());
		h.setTipo(datos.getTipo());
		h.actualizarPrecio(datos.getPrecioNoche());
		h.cambiarEstado(datos.getEstado());
		cargarTabla();
	}

	private void eliminar() {
		Habitacion h = seleccionada();
		if (h == null) {
			return;
		}
		if (h.getEstado() == EstadoHabitacion.OCUPADA) {
			JOptionPane.showMessageDialog(this, "No se puede eliminar una habitación ocupada", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		int opcion = JOptionPane.showConfirmDialog(this, "¿Eliminar la habitación " + h.getNumero() + " y sus incidencias?", "Confirmar", JOptionPane.YES_NO_OPTION);
		if (opcion == JOptionPane.YES_OPTION) {
			incidencias.removeIf(inc -> inc.getHabitacion() == h);
			habitaciones.remove(h);
			cargarTabla();
		}
	}

	private void cambiarTarifas() {
		JComboBox<TipoHabitacion> cbTipo = new JComboBox<>(TipoHabitacion.values());
		JTextField txtPrecio = new JTextField();
		Object[] campos = { "Tipo de habitación:", cbTipo, "Nuevo precio por noche:", txtPrecio };
		int opcion = JOptionPane.showConfirmDialog(this, campos, "Cambiar tarifas", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (opcion != JOptionPane.OK_OPTION) {
			return;
		}
		double precio;
		try {
			precio = Double.parseDouble(txtPrecio.getText().trim().replace(",", "."));
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "El precio tiene que ser un número", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (precio <= 0) {
			JOptionPane.showMessageDialog(this, "El precio tiene que ser mayor que 0", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		TipoHabitacion tipo = (TipoHabitacion) cbTipo.getSelectedItem();
		int cambiadas = 0;
		for (Habitacion h : habitaciones) {
			if (h.getTipo() == tipo) {
				h.actualizarPrecio(precio);
				cambiadas++;
			}
		}
		cargarTabla();
		JOptionPane.showMessageDialog(this, "Se ha cambiado el precio de " + cambiadas + " habitaciones " + tipo);
	}

	public static void iniciarLimpieza(Habitacion h) {
		Thread hilo = new Thread(() -> {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				return;
			}
			SwingUtilities.invokeLater(() -> {
				if (h.getEstado() == EstadoHabitacion.LIMPIEZA) {
					h.cambiarEstado(EstadoHabitacion.DISPONIBLE);
				}
				for (Window w : Window.getWindows()) {
					if (w instanceof PanelHabitaciones && w.isShowing()) {
						((PanelHabitaciones) w).cargarTabla();
					}
				}
			});
		});
		hilo.setDaemon(true);
		hilo.start();
	}

	private void nuevaIncidencia() {
		Habitacion h = seleccionada();
		if (h == null) {
			return;
		}
		if (h.getEstado() == EstadoHabitacion.OCUPADA) {
			JOptionPane.showMessageDialog(this, "No se puede poner en mantenimiento una habitación ocupada", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		String texto = JOptionPane.showInputDialog(this, "Describe la incidencia de la habitación " + h.getNumero() + ":", "Nueva incidencia", JOptionPane.QUESTION_MESSAGE);
		if (texto == null || texto.trim().isEmpty()) {
			return;
		}
		IncidenciasHabitacion inc = new IncidenciasHabitacion(siguienteIdIncidencia++, h, texto.trim(), LocalDate.now(), false);
		inc.abrir();
		incidencias.add(inc);
		cargarTabla();
	}

	private void verIncidencias() {
		Habitacion h = seleccionada();
		if (h == null) {
			return;
		}
		List<IncidenciasHabitacion> lista = new ArrayList<>();
		for (IncidenciasHabitacion inc : incidencias) {
			if (inc.getHabitacion() == h) {
				lista.add(inc);
			}
		}
		if (lista.isEmpty()) {
			JOptionPane.showMessageDialog(this, "La habitación " + h.getNumero() + " no tiene incidencias");
			return;
		}
		JList<IncidenciasHabitacion> jlist = new JList<>(lista.toArray(new IncidenciasHabitacion[0]));
		jlist.setVisibleRowCount(6);
		String[] botones = { "Resolver seleccionada", "Cerrar" };
		int opcion = JOptionPane.showOptionDialog(this, new JScrollPane(jlist), "Incidencias de la habitación " + h.getNumero(), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, botones, botones[1]);
		if (opcion != 0) {
			return;
		}
		IncidenciasHabitacion inc = jlist.getSelectedValue();
		if (inc == null || inc.isResuelta()) {
			JOptionPane.showMessageDialog(this, "Selecciona una incidencia pendiente", "Aviso", JOptionPane.WARNING_MESSAGE);
			return;
		}
		inc.resolver();
		for (IncidenciasHabitacion otra : lista) {
			if (!otra.isResuelta()) {
				h.cambiarEstado(EstadoHabitacion.MANTENIMIENTO);
			}
		}
		cargarTabla();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new PanelHabitaciones().setVisible(true));
	}
}