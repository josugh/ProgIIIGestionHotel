
package domain;

import java.time.LocalDate;

public class ConsumoHabitacion {

    // Atributos del consumo
    private int idConsumo;
    private Reserva reserva;
    private ServicioHabitacion servicio;
    private int cantidad;
    private LocalDate fecha;
    private String observaciones;

    // Constructor
    public ConsumoHabitacion(int idConsumo, Reserva reserva,
            ServicioHabitacion servicio, int cantidad,
            LocalDate fecha, String observaciones) {

        this.idConsumo = idConsumo;
        this.reserva = reserva;
        this.servicio = servicio;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.observaciones = observaciones;
    }

    // Getters y setters
    public int getIdConsumo() {
        return idConsumo;
    }

    public void setIdConsumo(int idConsumo) {
        this.idConsumo = idConsumo;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public ServicioHabitacion getServicio() {
        return servicio;
    }

    public void setServicio(ServicioHabitacion servicio) {
        this.servicio = servicio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    // Calcula el precio total del servicio consumido
    public double calcularSubtotal() {
        return servicio.getPrecio() * cantidad;
    }

    // Mostramos los datos del consumo
    @Override
    public String toString() {
        return servicio.getNombre() + " - " + cantidad
                + " unidades - " + calcularSubtotal() + " euros";
    }
}
