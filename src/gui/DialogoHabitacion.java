package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import domain.EstadoHabitacion;
import domain.Habitacion;
import domain.TipoHabitacion;

public class DialogoHabitacion extends JDialog {

	private static final long serialVersionUID = 1L;
	
	//Componentes
	private JTextField txtNumero = new JTextField(10);
	private JTextField txtPlanta = new JTextField(10);
	private JComboBox<TipoHabitacion> cbTipo = new JComboBox<>(TipoHabitacion.values());
	private JTextField txtPrecio = new JTextField(10);
	private JComboBox<EstadoHabitacion> cbEstado = new JComboBox<>(EstadoHabitacion.values());
	private JButton btnGuardar = new JButton("Guardar");
	private JButton btnCancelar = new JButton("Cancelar");

	private Habitacion habitacion;
	private List<Habitacion> habitaciones;
	private boolean guardado = false;
	
	//Constructor
	public DialogoHabitacion(Habitacion habitacion, List<Habitacion> habitaciones) {
		this.habitacion = habitacion;
		this.habitaciones = habitaciones;

		setTitle(habitacion == null ? "Nueva habitación" : "Editar habitación");
		setModal(true);

		JPanel panelCampos = new JPanel(new GridLayout(5, 2, 5, 5));
		panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panelCampos.add(new JLabel("Número:"));
		panelCampos.add(txtNumero);
		panelCampos.add(new JLabel("Planta:"));
		panelCampos.add(txtPlanta);
		panelCampos.add(new JLabel("Tipo:"));
		panelCampos.add(cbTipo);
		panelCampos.add(new JLabel("Precio por noche:"));
		panelCampos.add(txtPrecio);
		panelCampos.add(new JLabel("Estado:"));
		panelCampos.add(cbEstado);

		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panelBotones.add(btnCancelar);
		panelBotones.add(btnGuardar);

		add(panelCampos, BorderLayout.CENTER);
		add(panelBotones, BorderLayout.SOUTH);

		if (habitacion != null) {
			txtNumero.setText(String.valueOf(habitacion.getNumero()));
			txtPlanta.setText(String.valueOf(habitacion.getPlanta()));
			cbTipo.setSelectedItem(habitacion.getTipo());
			txtPrecio.setText(String.valueOf(habitacion.getPrecioNoche()));
			cbEstado.setSelectedItem(habitacion.getEstado());
		}

		btnGuardar.addActionListener(e -> guardar());
		btnCancelar.addActionListener(e -> dispose());

		pack();
		setLocationRelativeTo(null);
	}
	
	//Guardar Habitacion
	private void guardar() {
		int numero;
		int planta;
		double precio;
		try {
			numero = Integer.parseInt(txtNumero.getText().trim());
			planta = Integer.parseInt(txtPlanta.getText().trim());
			precio = Double.parseDouble(txtPrecio.getText().trim().replace(",", "."));
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "El número, la planta y el precio deben ser números.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (numero <= 0 || planta < 0 || precio <= 0) {
			JOptionPane.showMessageDialog(this, "El número y el precio deben ser mayores que 0 y la planta no puede ser negativa.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		for (Habitacion h : habitaciones) {
			if (h.getNumero() == numero && h != habitacion) {
				JOptionPane.showMessageDialog(this, "Ya existe una habitación con ese número.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

		TipoHabitacion tipo = (TipoHabitacion) cbTipo.getSelectedItem();
		EstadoHabitacion estado = (EstadoHabitacion) cbEstado.getSelectedItem();

		if (habitacion == null) {
			habitacion = new Habitacion(numero, planta, tipo, precio, estado);
		} else {
			habitacion.setNumero(numero);
			habitacion.setPlanta(planta);
			habitacion.setTipo(tipo);
			habitacion.setPrecioNoche(precio);
			habitacion.setEstado(estado);
		}
		guardado = true;
		dispose();
	}

	public boolean isGuardado() {
		return guardado;
	}

	public Habitacion getHabitacion() {
		return habitacion;
	}
}