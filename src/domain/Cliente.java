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
	
	public void modificarDatos() {
		
	}
	
	public String mostrarInformacion() {
		return nombre + "" + apellidos;
	}
	
	public boolean esClienteFrecuente() {
		return clienteFrecuente;
	}
	
}
