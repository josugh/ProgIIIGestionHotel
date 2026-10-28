package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class VentanaLogin extends JFrame{

	private static final long serialVersionUID = 1L;
	
	public  VentanaLogin() {
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
		add(panelTitulo , BorderLayout.NORTH);

		
		
		
		
		
		
		
		setVisible(true);
	}
	
	
	

}
