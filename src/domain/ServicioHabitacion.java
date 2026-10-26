
package domain;

public class ServicioHabitacion {

    // Atributos del servicio
    private int idServicio;
    private String nombre;
    private double precio;

    // Constructor
    public ServicioHabitacion(int idServicio, String nombre, double precio) {
        this.idServicio = idServicio;
        this.nombre = nombre;
        this.precio = precio;
    }

    // Getters y setters
    public int getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    // Mostramos la informacion del servicio
    @Override
    public String toString() {
        return nombre + " - " + precio + " euros";
    }
}
