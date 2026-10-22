package domain;

public class Habitacion {

	private int numero;
	private int planta;
	private TipoHabitacion tipo;
	private double precio;
	private EstadoHabitacion estado;

	public Habitacion(int numero, int planta, TipoHabitacion tipo, double precio, EstadoHabitacion estado) {
		this.numero = numero;
		this.planta = planta;
		this.tipo = tipo;
		this.precio = precio;
		this.estado = estado;
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

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
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