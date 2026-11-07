package domain;

public class Habitacion {

	private int numero;
	private int planta;
	private TipoHabitacion tipo;
	private double precioNoche;
	private EstadoHabitacion estado;

	public Habitacion(int numero, int planta, TipoHabitacion tipo, double precioNoche, EstadoHabitacion estado) {
		this.numero = numero;
		this.planta = planta;
		this.tipo = tipo;
		this.precioNoche = precioNoche;
		this.estado = estado;
	}

	public boolean estaDisponible() {
		return estado == EstadoHabitacion.DISPONIBLE;
	}

	public void cambiarEstado(EstadoHabitacion nuevoEstado) {
		this.estado = nuevoEstado;
	}

	public void actualizarPrecio(double nuevoPrecio) {
		if (nuevoPrecio <= 0) {
			throw new IllegalArgumentException("El precio tiene que ser mayor que 0");
		}
		this.precioNoche = nuevoPrecio;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public int getPlanta() {
		return planta;
	}

	public void setPlanta(int planta) {
		this.planta = planta;
	}

	public TipoHabitacion getTipo() {
		return tipo;
	}

	public void setTipo(TipoHabitacion tipo) {
		this.tipo = tipo;
	}

	public double getPrecioNoche() {
		return precioNoche;
	}

	public void setPrecioNoche(double precioNoche) {
		this.precioNoche = precioNoche;
	}

	public EstadoHabitacion getEstado() {
		return estado;
	}

	public void setEstado(EstadoHabitacion estado) {
		this.estado = estado;
	}

	@Override
	public String toString() {
		return "Habitación " + numero;
	}
}