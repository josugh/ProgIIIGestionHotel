
package domain;

import java.time.LocalDate;

public class Temporada {

    // Atributos de la temporada
    private int idTemporada;
    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double factorPrecio;

    // Constructor
    public Temporada(int idTemporada, String nombre,
            LocalDate fechaInicio, LocalDate fechaFin,
            double factorPrecio) {

        this.idTemporada = idTemporada;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.factorPrecio = factorPrecio;
    }

    // Getters y setters
    public int getIdTemporada() {
        return idTemporada;
    }

    public void setIdTemporada(int idTemporada) {
        this.idTemporada = idTemporada;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public double getFactorPrecio() {
        return factorPrecio;
    }

    public void setFactorPrecio(double factorPrecio) {
        this.factorPrecio = factorPrecio;
    }

    // Comprueba si una fecha esta dentro de la temporada
    public boolean incluyeFecha(LocalDate fecha) {
        return !fecha.isBefore(fechaInicio)
                && !fecha.isAfter(fechaFin);
    }

    // Calcula el precio segun la temporada
    public double calcularPrecio(double precioBase) {
        return precioBase * factorPrecio;
    }

    // Mostramos los datos de la temporada
    @Override
    public String toString() {
        return nombre + " - Factor: " + factorPrecio;
    }
}
