package gui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import domain.Habitacion;

public class HabitacionTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private String[] columnas = { "Número", "Planta", "Tipo", "Precio", "Estado" };
	private List<Habitacion> habitaciones;

	public HabitacionTableModel(List<Habitacion> habitaciones) {
		this.habitaciones = habitaciones;
	}

	public void setHabitaciones(List<Habitacion> habitaciones) {
		this.habitaciones = habitaciones;
		fireTableDataChanged(); //Avisa a la tabla de que los datos han cambiado para que se vuelva a pintar
	}

	public Habitacion getHabitacion(int fila) {
		return habitaciones.get(fila);
	}

	@Override
	public int getRowCount() {
		return habitaciones.size();
	}

	@Override
	public int getColumnCount() {
		return columnas.length;
	}

	@Override
	public String getColumnName(int columna) {
		return columnas[columna];
	}

	@Override
	public boolean isCellEditable(int fila, int columna) {
		return false;
	}

	@Override
	public Object getValueAt(int fila, int columna) {
		Habitacion h = habitaciones.get(fila);
		switch (columna) {
		case 0:
			return h.getNumero();
		case 1:
			return h.getPlanta();
		case 2:
			return h.getTipo();
		case 3:
			return h.getPrecio() + " €";
		default:
			return h.getEstado();
		}
	}
}