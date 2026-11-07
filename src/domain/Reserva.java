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
    private EstadoReserva estadoReserva;


    public Reserva(int idReserva, Cliente cliente,
                   Habitacion habitacion,
                   LocalDate fechaEntrada,
                   LocalDate fechaSalida,
                   int numeroHuespedes) {

        if (cliente == null) {
            throw new IllegalArgumentException(
                "La reserva debe tener un cliente.");
        }

        if (habitacion == null) {
            throw new IllegalArgumentException(
                "La reserva debe tener una habitación.");
        }

        validarFechas(fechaEntrada, fechaSalida);

        if (numeroHuespedes <= 0) {
            throw new IllegalArgumentException(
                "El número de huéspedes debe ser mayor que cero.");
        }

        this.idReserva = idReserva;
        this.cliente = cliente;
        this.habitacion = habitacion;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.numeroHuespedes = numeroHuespedes;
        this.estadoReserva = EstadoReserva.CONFIRMADA;
    }


    // Validar fechas
    private void validarFechas(LocalDate entrada,
                              LocalDate salida) {

        if (entrada == null || salida == null) {
            throw new IllegalArgumentException(
                "Las fechas no pueden estar vacías.");
        }

        if (!salida.isAfter(entrada)) {
            throw new IllegalArgumentException(
                "La fecha de salida debe ser posterior a la entrada.");
        }
    }

    // Calcular número de noches
    public long calcularNoches() {

        return ChronoUnit.DAYS.between(
            fechaEntrada, fechaSalida);
    }

    // Cancelar reserva
    public void cancelarReserva() {

        estadoReserva = EstadoReserva.CANCELADA;
    }

    // Comprobar si está cancelada
    public boolean estaCancelada() {

        return estadoReserva == EstadoReserva.CANCELADA;
    }

    // Modificar fechas
    public void modificarFechas(LocalDate nuevaEntrada,
                                LocalDate nuevaSalida) {

        validarFechas(nuevaEntrada, nuevaSalida);

        this.fechaEntrada = nuevaEntrada;
        this.fechaSalida = nuevaSalida;
    }

    // Modificar cliente
    public void modificarCliente(Cliente nuevoCliente) {

        if (nuevoCliente == null) {
            throw new IllegalArgumentException(
                "El cliente no puede ser nulo.");
        }

        this.cliente = nuevoCliente;
    }

    // Modificar habitación
    public void modificarHabitacion(Habitacion nuevaHabitacion) {

        if (nuevaHabitacion == null) {
            throw new IllegalArgumentException(
                "La habitación no puede ser nula.");
        }

        this.habitacion = nuevaHabitacion;
    }

    // Comprobar solapamiento con otra reserva
    public boolean seSolapaCon(Reserva otraReserva) {

        if (otraReserva == null) {
            return false;
        }

        if (this.estaCancelada() || otraReserva.estaCancelada()) {
            return false;
        }

        if (this.habitacion == null
                || otraReserva.habitacion == null) {
            return false;
        }

        // Comparamos la misma habitación
        if (this.habitacion != otraReserva.habitacion) {
            return false;
        }

        return fechaEntrada.isBefore(otraReserva.fechaSalida)
            && fechaSalida.isAfter(otraReserva.fechaEntrada);
    }


    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        modificarCliente(cliente);
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion) {
        modificarHabitacion(habitacion);
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

        if (numeroHuespedes <= 0) {
            throw new IllegalArgumentException(
                "El número de huéspedes debe ser mayor que cero.");
        }

        this.numeroHuespedes = numeroHuespedes;
    }

    public EstadoReserva getEstadoReserva() {
        return estadoReserva;
    }

    public void setEstadoReserva(EstadoReserva estadoReserva) {
        if (estadoReserva == null) {
            throw new IllegalArgumentException(
                "El estado de la reserva no puede estar vacío.");
        }
        this.estadoReserva = estadoReserva;
    }

    @Override
    public String toString() {

        return "Reserva " + idReserva
                + " - " + cliente.mostrarInformacion()
                + " - Entrada: " + fechaEntrada
                + " - Salida: " + fechaSalida
                + " - Estado: " + estadoReserva;
    }
}