package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import domain.Empleado;
import domain.TipoEmpleado;

public class VentanaEmpleados extends JFrame {

	private static final long serialVersionUID = 1L;

	private List<Empleado> empleados;
	private Empleado empleadoActual; 

	private DefaultTableModel modeloTabla;
	private JTable tablaEmpleados;

	private JTextField textoNombre = new JTextField(15);
	private JTextField textoUsuario = new JTextField(15);
	private JPasswordField textoPassword = new JPasswordField(15);
	private JComboBox<TipoEmpleado> comboTipo = new JComboBox<>(TipoEmpleado.values());

	public VentanaEmpleados(List<Empleado> empleados, Empleado empleadoActual) {
		this.empleados = empleados;
		this.empleadoActual = empleadoActual;

		setTitle("Gestión Hotel - Empleados");
		setSize(650, 450);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		//TABLA DE EMPLEADOS
		String[] columnas = { "ID", "Nombre", "Usuario", "Tipo" };
		modeloTabla = new DefaultTableModel(columnas, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int fila, int columna) {
				return false;
			}
		};
		tablaEmpleados = new JTable(modeloTabla);
		tablaEmpleados.setRowHeight(25);
		tablaEmpleados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		add(new JScrollPane(tablaEmpleados), BorderLayout.CENTER);

		//PANEL ABAJO -> FORMULARIO
		JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 5, 5));
		panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del empleado"));
		panelFormulario.add(new JLabel("Nombre:"));
		panelFormulario.add(textoNombre);
		panelFormulario.add(new JLabel("Usuario:"));
		panelFormulario.add(textoUsuario);
		panelFormulario.add(new JLabel("Contraseña:"));
		panelFormulario.add(textoPassword);
		panelFormulario.add(new JLabel("Tipo:"));
		panelFormulario.add(comboTipo);

		//PANEL ABAJO -> BOTONES
		JPanel panelBotones = new JPanel(new FlowLayout());
		JButton btnAlta = new JButton("Dar de alta");
		JButton btnModificar = new JButton("Modificar");
		JButton btnLimpiar = new JButton("Limpiar");
		panelBotones.add(btnAlta);
		panelBotones.add(btnModificar);
		panelBotones.add(btnLimpiar);

		JPanel panelAbajo = new JPanel(new BorderLayout());
		panelAbajo.add(panelFormulario, BorderLayout.CENTER);
		panelAbajo.add(panelBotones, BorderLayout.SOUTH);
		add(panelAbajo, BorderLayout.SOUTH);

		//EVENTOS
		btnAlta.addActionListener(e -> darDeAlta());
		btnModificar.addActionListener(e -> modificar());
		
		btnLimpiar.addActionListener(e -> limpiarFormulario());
		// Al seleccionar una fila se cargan sus datos en el formulario 
		tablaEmpleados.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				cargarSeleccionado();
			}
		});

		mostrarEmpleados();
		setVisible(true);
	}

	private void mostrarEmpleados() {
		modeloTabla.setRowCount(0);
		for (Empleado e : empleados) {
			modeloTabla.addRow(new Object[] { e.getIdEmpleado(), e.getNombre(), e.getUsuario(), e.getTipo() });
		}
	}

	private Empleado getSeleccionado() {
		int fila = tablaEmpleados.getSelectedRow();
		if (fila == -1) {
			return null;
		}
		return empleados.get(fila);
	}

	private void cargarSeleccionado() {
		Empleado e = getSeleccionado();
		if (e == null) {
			return;
		}
		textoNombre.setText(e.getNombre());
		textoUsuario.setText(e.getUsuario());
		textoPassword.setText(""); // la contraseña no se enseña nunca
		comboTipo.setSelectedItem(e.getTipo());
	}

	private void darDeAlta() {
		String nombre = textoNombre.getText().trim();
		String usuario = textoUsuario.getText().trim();
		String password = new String(textoPassword.getPassword());
		if (nombre.isEmpty() || usuario.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Rellena nombre, usuario y contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
			
			return;
		}
		if (existeUsuario(usuario, null)) {
			JOptionPane.showMessageDialog(this, "Ya existe un empleado con ese usuario.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		TipoEmpleado tipo = (TipoEmpleado) comboTipo.getSelectedItem();
		
		empleados.add(new Empleado(siguienteId(), nombre, usuario, password, tipo));
		mostrarEmpleados();
		limpiarFormulario();
	}

	private void modificar() {
		Empleado e = getSeleccionado();
		if (e == null) {
			JOptionPane.showMessageDialog(this, "Selecciona un empleado de la tabla.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		String nombre = textoNombre.getText().trim();
		String usuario = textoUsuario.getText().trim();
		String password = new String(textoPassword.getPassword());
		TipoEmpleado tipo = (TipoEmpleado) comboTipo.getSelectedItem();
		if (nombre.isEmpty() || usuario.isEmpty()) {
			JOptionPane.showMessageDialog(this, "El nombre y el usuario no pueden estar vacíos.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (existeUsuario(usuario, e)) {
			JOptionPane.showMessageDialog(this, "Ya existe otro empleado con ese usuario.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (e == empleadoActual && tipo != e.getTipo()) {
			JOptionPane.showMessageDialog(this, "No puedes cambiar tu propio tipo de empleado.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		e.setNombre(nombre);
		e.setUsuario(usuario);
		e.setTipo(tipo);
		if (!password.isEmpty()) { // si se deja vacía, se mantiene la que tenía
			e.setPassword(password);
		}
		mostrarEmpleados();
		limpiarFormulario();
	}

	private boolean existeUsuario(String usuario, Empleado excepto) {
		for (Empleado e : empleados) {
			if (e != excepto && e.getUsuario().equalsIgnoreCase(usuario)) {
				return true;
				
			}
		}
		return false;
	}

	private int siguienteId() {
		int max = 0;
		for (Empleado e : empleados) {
			max = Math.max(max, e.getIdEmpleado());
		}
		
		return max + 1;
	}

	private void limpiarFormulario() {
		tablaEmpleados.clearSelection();
		textoNombre.setText("");
		textoUsuario.setText("");
		textoPassword.setText("");
		comboTipo.setSelectedIndex(0);
	}
}