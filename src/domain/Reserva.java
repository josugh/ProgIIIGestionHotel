
package domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reserva {
	
    private int idReserva;
    private Cliente cliente;
    private Habitacion habitacion;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private int numeroHuespedes;
    private String estadoReserva;

    public Reserva(int idReserva, Cliente cliente,
                   Habitacion habitacion,
                   LocalDate fechaEntrada,
                   LocalDate fechaSalida,
                   int numeroHuespedes) {

        this.idReserva = idReserva;
        this.cliente = cliente;
        this.habitacion = habitacion;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.numeroHuespedes = numeroHuespedes;
        this.estadoReserva = "Confirmada";
    }

    public long calcularNoches() {
        return ChronoUnit.DAYS.between(fechaEntrada, fechaSalida);
    }

    public void cancelarReserva() {
        this.estadoReserva = "Cancelada";
    }

    public void modificarFechas(LocalDate nuevaEntrada,
                                LocalDate nuevaSalida) {
        this.fechaEntrada = nuevaEntrada;
        this.fechaSalida = nuevaSalida;
    }

    public boolean estaCancelada() {
        return estadoReserva.equals("Cancelada");
    }

    public int getIdReserva() {
        return idReserva;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public int getNumeroHuespedes() {
        return numeroHuespedes;
    }

    public void setNumeroHuespedes(int numeroHuespedes) {
        this.numeroHuespedes = numeroHuespedes;
    }

    public String getEstadoReserva() {
        return estadoReserva;
    }
}
