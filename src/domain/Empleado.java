package domain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Empleado {

	private int idEmpleado;
	private String nombre;
	private String usuario;
	private String passwordHash;
	private TipoEmpleado tipo;

	public Empleado(int idEmpleado, String nombre, String usuario, String password, TipoEmpleado tipo) {
		this.idEmpleado = idEmpleado;
		this.nombre = nombre;
		this.usuario = usuario;
		this.passwordHash = hashear(password); // nunca se guarda la contraseña en claro IA(UNAI)
		this.tipo = tipo;
	}

	// Convierte la contraseña en un hash SHA-256 AYUDADO POR LA IA(UNAI)
	public static String hashear(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			for (byte b : bytes) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("SHA-256 no disponible", e);
		}
	}

	public boolean validarCredenciales(String usuario, String password) {
		return this.usuario.equals(usuario) && this.passwordHash.equals(hashear(password));
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

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPassword(String password) {
		this.passwordHash = hashear(password);
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