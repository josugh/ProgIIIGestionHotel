package domain;

public class Empleado {

	private int idEmpleado;
	private String nombre;
	private String usuario;
	private String password;
	private TipoEmpleado tipo;

	public Empleado(int idEmpleado, String nombre, String usuario, String password, TipoEmpleado tipo) {
		this.idEmpleado = idEmpleado;
		this.nombre = nombre;
		this.usuario = usuario;
		this.password = password;
		this.tipo = tipo;
	}

	public boolean validarCredenciales(String usuario, String password) {
		return this.usuario.equals(usuario) && this.password.equals(password);
	}

	public boolean tienePermiso(TipoEmpleado necesario) {
		return tipo.ordinal() >= necesario.ordinal();
	}

	public int getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(int idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public TipoEmpleado getTipo() {
		return tipo;
	}

	public void setTipo(TipoEmpleado tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return nombre + " (" + tipo + ")";
	}
}