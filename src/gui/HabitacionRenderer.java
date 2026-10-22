package gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import domain.EstadoHabitacion;

public class HabitacionRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column) {
		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		label.setHorizontalAlignment(JLabel.CENTER);

		if (!isSelected) {
			HabitacionTableModel modelo = (HabitacionTableModel) table.getModel();
			EstadoHabitacion estado = modelo.getHabitacion(row).getEstado();

			if (estado == EstadoHabitacion.DISPONIBLE) {
				label.setBackground(new Color(200, 240, 200));
			} else if (estado == EstadoHabitacion.OCUPADA) {
				label.setBackground(new Color(250, 200, 200));
			} else if (estado == EstadoHabitacion.RESERVADA) {
				label.setBackground(new Color(255, 235, 180));
			} else {
				label.setBackground(Color.LIGHT_GRAY);
			}
		}
		return label;
	}
}