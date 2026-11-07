
package domain;

public class Cliente {

    private int idCliente;
    private String nombre;
    private String apellidos;
    private String dni;
    private String telefono;
    private String email;
    private String direccion;
    private boolean clienteFrecuente;

    
    public Cliente(int idCliente, String nombre,
                   String apellidos, String dni,
                   String telefono, String email,
                   String direccion) {

        this.idCliente = idCliente;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.clienteFrecuente = false;
    }

    
    public String mostrarInformacion() {
        return nombre + " " + apellidos;
    }

    
    public boolean esClienteFrecuente() {
        return clienteFrecuente;
    }

   
    public void establecerClienteFrecuente(boolean frecuente) {
        this.clienteFrecuente = frecuente;
    }

    
    public void modificarTelefono(String nuevoTelefono) {
        this.telefono = nuevoTelefono;
    }

   
    public void modificarEmail(String nuevoEmail) {
        this.email = nuevoEmail;
    }



    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public boolean isClienteFrecuente() {
        return clienteFrecuente;
    }

    public void setClienteFrecuente(boolean clienteFrecuente) {
        this.clienteFrecuente = clienteFrecuente;
    }

    @Override
    public String toString() {
        return idCliente + " - " + nombre + " "
                + apellidos + " (" + dni + ")";
    }
}
