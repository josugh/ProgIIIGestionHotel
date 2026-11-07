package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import domain.Empleado;
import domain.Hotel;

public class VentanaLogin extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final Font FUENTE_TEXTO = new Font("Arial", Font.PLAIN, 14);
	
	private JTextField textoUsuario;
	private JPasswordField textoPassword;
	private JButton botonIniciarSesion;
	private JLabel labelEstado = new JLabel(" ", SwingConstants.CENTER);
	private Hotel hotel;

	public VentanaLogin(Hotel hotel) {
		this.hotel = hotel;
		setTitle("Gestión Hotel - Login");
		setSize(420, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);

		
		
		//PANEL TITULO
		JPanel panelTitulo = new JPanel();
		panelTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
		//Label titulo
		JLabel labelTitulo = new JLabel("HOTEL");
		labelTitulo.setFont(new Font("Arial", Font.BOLD, 24));
		panelTitulo.add(labelTitulo);
		add(panelTitulo, BorderLayout.NORTH);


		//PANEL LOGIN (para usuario y contraseña
		JPanel panelLogIn = new JPanel();
		panelLogIn.setLayout(new BoxLayout(panelLogIn, BoxLayout.Y_AXIS));


		//PANEL LOGIN -> PANEL USUARIO
		JPanel panelUsuario = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel labelUsuario = new JLabel("Usuario:");
		labelUsuario.setFont(FUENTE_TEXTO);
		textoUsuario = new JTextField(15);

		panelUsuario.add(labelUsuario);
		panelUsuario.add(textoUsuario);
		panelLogIn.add(panelUsuario);


		//PANEL LOGIN -> PANEL CONTRASEÑA
		JPanel panelPassword = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel labelPassword = new JLabel("Contraseña:");
		labelPassword.setFont(FUENTE_TEXTO);
		textoPassword = new JPasswordField(15);

		panelPassword.add(labelPassword);
		panelPassword.add(textoPassword);
		panelLogIn.add(panelPassword);


		//PANEL LOGIN -> BOTON PARA INICIAR SESION
		JPanel panelBoton = new JPanel();
		botonIniciarSesion = new JButton("Iniciar sesión");
		botonIniciarSesion.setFont(FUENTE_TEXTO);
		panelBoton.add(botonIniciarSesion);
		panelLogIn.add(panelBoton);

		//PANEL LOGIN -> MENSAJE DE ESTADO ("Comprobando...")
		labelEstado.setFont(FUENTE_TEXTO);
		labelEstado.setAlignmentX(CENTER_ALIGNMENT);
		panelLogIn.add(labelEstado);

		add(panelLogIn);

		botonIniciarSesion.addActionListener(e -> iniciarSesion());
		textoPassword.addActionListener(e -> iniciarSesion());


		setVisible(true);
	}

	private void iniciarSesion() {
		String usuario = textoUsuario.getText().trim();
		String password = new String(textoPassword.getPassword());
		if (usuario.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Introduce el usuario y la contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		activarFormulario(false);
		labelEstado.setText("Comprobando credenciales...");

		//la comprobación  fuera del hilo de Swing para que la ventana no se congele
		Thread hiloLogin = new Thread(() -> {
			try {
				Thread.sleep(1000); // simula el tiempo de consultar la BD
			} catch (InterruptedException e) {
				return;
			}
			Empleado empleado = hotel.iniciarSesion(usuario, password);

			SwingUtilities.invokeLater(() -> {
				if (empleado == null) {
					labelEstado.setText(" ");
					activarFormulario(true);
					JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
					textoPassword.setText("");
					textoPassword.requestFocus();
				} else {
					new VentanaPrincipal(hotel, empleado);
					dispose();
				}
			});
		});
		hiloLogin.setDaemon(true);   //IA
		hiloLogin.start();
	}

	private void activarFormulario(boolean activo) {
		textoUsuario.setEnabled(activo);
		textoPassword.setEnabled(activo);
		botonIniciarSesion.setEnabled(activo);
	}

}