import helpers.TCPClient;
import helpers.TCPServer;

import java.awt.EventQueue;

import objects.Me;

import Interfaces.Profil;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Profil profil = new Profil();
					Profil.initializeProfil();
					profil.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
