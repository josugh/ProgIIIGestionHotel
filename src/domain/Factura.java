package domain;

import java.time.LocalDate;

public class Factura {

    // Atributos de la factura
    private int idFactura;
    private Reserva reserva;
    private LocalDate fecha;
    private double importeHabitacion;
    private double importeExtras;
    private double descuento;
    private double total;
    private boolean pagada;

    // Constructor
    public Factura(int idFactura, Reserva reserva, LocalDate fecha,
            double importeHabitacion, double importeExtras,
            double descuento) {

        this.idFactura = idFactura;
        this.reserva = reserva;
        this.fecha = fecha;
        this.importeHabitacion = importeHabitacion;
        this.importeExtras = importeExtras;
        this.descuento = descuento;
        this.pagada = false;

        calcularTotal();
    }

    // Getters y setters
    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public double getImporteHabitacion() {
        return importeHabitacion;
    }

    public void setImporteHabitacion(double importeHabitacion) {
        this.importeHabitacion = importeHabitacion;
        calcularTotal();
    }

    public double getImporteExtras() {
        return importeExtras;
    }

    public void setImporteExtras(double importeExtras) {
        this.importeExtras = importeExtras;
        calcularTotal();
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        aplicarDescuento(descuento);
    }

    public double getTotal() {
        return total;
    }

    public boolean isPagada() {
        return pagada;
    }

    public void setPagada(boolean pagada) {
        this.pagada = pagada;
    }

    // Calcula el total de la factura
    public double calcularTotal() {
        double subtotal = importeHabitacion + importeExtras;
        total = subtotal - (subtotal * descuento / 100);
        return total;
    }

    // Aplica un descuento en porcentaje
    public void aplicarDescuento(double porcentaje) {
        if (porcentaje >= 0 && porcentaje <= 100) {
            this.descuento = porcentaje;
            calcularTotal();
        }
    }

    // Genera un texto con los datos de la factura
    public String generarTextoFactura() {
        String texto = "FACTURA HOTEL\n";
        texto += "Numero de factura: " + idFactura + "\n";
        texto += "Fecha: " + fecha + "\n";
        texto += "Importe habitacion: " + importeHabitacion + " euros\n";
        texto += "Importe extras: " + importeExtras + " euros\n";
        texto += "Descuento: " + descuento + "%\n";
        texto += "Total: " + total + " euros\n";
        texto += "Pagada: " + (pagada ? "Si" : "No") + "\n";

        return texto;
    }

    @Override
    public String toString() {
        return "Factura " + idFactura + " - " + total + " euros";
    }
}
