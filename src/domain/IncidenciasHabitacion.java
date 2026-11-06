package domain;

import java.time.LocalDate;

public class IncidenciasHabitacion {

	private int idIncidencia;
	private Habitacion habitacion;
	private String descripcion;
	private LocalDate fechaApertura;
	private boolean resuelta;

	public IncidenciasHabitacion(Habitacion habitacion, String descripcion) {
		this(0, habitacion, descripcion, LocalDate.now(), false);
	}

	public IncidenciasHabitacion(int idIncidencia, Habitacion habitacion, String descripcion, LocalDate fechaApertura, boolean resuelta) {
		this.idIncidencia = idIncidencia;
		this.habitacion = habitacion;
		this.descripcion = descripcion;
		this.fechaApertura = fechaApertura;
		this.resuelta = resuelta;
	}

	public void abrir() {
		resuelta = false;
		habitacion.cambiarEstado(EstadoHabitacion.MANTENIMIENTO);
	}

	public void resolver() {
		resuelta = true;
		habitacion.cambiarEstado(EstadoHabitacion.DISPONIBLE);
	}

	public int getIdIncidencia() {
		return idIncidencia;
	}

	public void setIdIncidencia(int idIncidencia) {
		this.idIncidencia = idIncidencia;
	}

	public Habitacion getHabitacion() {
		return habitacion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public LocalDate getFechaApertura() {
		return fechaApertura;
	}

	public boolean isResuelta() {
		return resuelta;
	}

	@Override
	public String toString() {
		return fechaApertura + " - " + descripcion + (resuelta ? " (resuelta)" : " (pendiente)");
	}
}