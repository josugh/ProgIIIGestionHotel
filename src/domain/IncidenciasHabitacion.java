package domain;

import java.time.LocalDate;

public class IncidenciasHabitacion {

	private Habitacion habitacion;
	private String descripcion;
	private LocalDate fecha;
	private boolean resuelta;

	public IncidenciasHabitacion(Habitacion habitacion, String descripcion) {
		this.habitacion = habitacion;
		this.descripcion = descripcion;
		this.fecha = LocalDate.now();
		this.resuelta = false;
	}

	public Habitacion getHabitacion() {
		return habitacion;
	}

	public void setHabitacion(Habitacion habitacion) {
		this.habitacion = habitacion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public boolean isResuelta() {
		return resuelta;
	}

	public void setResuelta(boolean resuelta) {
		this.resuelta = resuelta;
	}

	@Override
	public String toString() {
		return fecha + " - " + descripcion + (resuelta ? " (resuelta)" : " (pendiente)");
	}
}