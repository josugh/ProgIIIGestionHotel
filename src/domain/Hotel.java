package domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Hotel {

	private List<Empleado> empleados = new ArrayList<>();
	private List<Habitacion> habitaciones = new ArrayList<>();
	private List<Cliente> clientes = new ArrayList<>();
	private List<Reserva> reservas = new ArrayList<>();
	private List<IncidenciasHabitacion> incidencias = new ArrayList<>();

	public Hotel() {
		cargarDatosPrueba();
	}

	private void cargarDatosPrueba() {
		empleados.add(new Empleado(1, "Administrador", "admin", "1234", TipoEmpleado.GERENCIA));
		empleados.add(new Empleado(2, "Recepción", "recepcion", "1234", TipoEmpleado.RECEPCION));

		habitaciones.add(new Habitacion(101, 1, TipoHabitacion.INDIVIDUAL, 50, EstadoHabitacion.DISPONIBLE));
		habitaciones.add(new Habitacion(102, 1, TipoHabitacion.DOBLE, 80, EstadoHabitacion.OCUPADA));
		habitaciones.add(new Habitacion(103, 1, TipoHabitacion.DOBLE, 80, EstadoHabitacion.RESERVADA));
		habitaciones.add(new Habitacion(201, 2, TipoHabitacion.INDIVIDUAL, 55, EstadoHabitacion.DISPONIBLE));
		habitaciones.add(new Habitacion(202, 2, TipoHabitacion.DOBLE, 85, EstadoHabitacion.MANTENIMIENTO));
		habitaciones.add(new Habitacion(301, 3, TipoHabitacion.SUITE, 150, EstadoHabitacion.OCUPADA));

		Cliente ana = new Cliente(1, "Ana", "García López", "12345678Z", "600111222", "ana@mail.com", "Bilbao");
		Cliente luis = new Cliente(2, "Luis", "Martínez Ruiz", "87654321X", "611333444", "luis@mail.com", "Donostia");
		Cliente carla = new Cliente(3, "Carla", "Rodríguez Sanz", "11223344B", "622555666", "carla@mail.com", "Vitoria");
		clientes.add(ana);
		clientes.add(luis);
		clientes.add(carla);

		LocalDate hoy = LocalDate.now();
		Reserva r1 = new Reserva(1, luis, buscarHabitacion(102), hoy.minusDays(2), hoy.plusDays(1), 2);
		r1.setEstadoReserva(EstadoReserva.EN_CURSO);
		Reserva r2 = new Reserva(2, ana, buscarHabitacion(103), hoy, hoy.plusDays(3), 2);
		Reserva r3 = new Reserva(3, carla, buscarHabitacion(301), hoy.minusDays(1), hoy.plusDays(2), 1);
		r3.setEstadoReserva(EstadoReserva.EN_CURSO);
		reservas.add(r1);
		reservas.add(r2);
		reservas.add(r3);

		Habitacion h202 = buscarHabitacion(202);
		incidencias.add(new IncidenciasHabitacion(1, h202, "No funciona el aire acondicionado", hoy.minusDays(3), false));
		incidencias.add(new IncidenciasHabitacion(2, h202, "Bombilla fundida en el baño", hoy.minusDays(40), true));
	}

	public Empleado iniciarSesion(String usuario, String password) {
		for (Empleado e : empleados) {
			if (e.validarCredenciales(usuario, password)) {
				return e;
			}
		}
		return null;
	}

	public Habitacion buscarHabitacion(int numero) {
		for (Habitacion h : habitaciones) {
			if (h.getNumero() == numero) {
				return h;
			}
		}
		return null;
	}

	public int contarHabitaciones(EstadoHabitacion estado) {
		int total = 0;
		for (Habitacion h : habitaciones) {
			if (h.getEstado() == estado) {
				total++;
			}
		}
		return total;
	}

	public List<Empleado> getEmpleados() {
		return empleados;
	}

	public List<Habitacion> getHabitaciones() {
		return habitaciones;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public List<Reserva> getReservas() {
		return reservas;
	}

	public List<IncidenciasHabitacion> getIncidencias() {
		return incidencias;
	}
}