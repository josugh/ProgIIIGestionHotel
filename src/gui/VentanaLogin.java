package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class VentanaLogin extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final Font FUENTE_TEXTO = new Font("Arial", Font.PLAIN, 14);
	
	private JTextField textoUsuario;
	private JPasswordField textoPassword;
	private JButton botonIniciarSesion;

	public VentanaLogin() {
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

		add(panelLogIn);


		setVisible(true);
	}

}