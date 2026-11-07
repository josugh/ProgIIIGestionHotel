package main;

import javax.swing.SwingUtilities;

import domain.Hotel;
import gui.VentanaLogin;

public class MainProject {

	public static void main(String[] args) {
		Hotel hotel = new Hotel();
		SwingUtilities.invokeLater(() -> new VentanaLogin(hotel));
	}

}