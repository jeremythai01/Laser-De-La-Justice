package physique;

import java.awt.Graphics2D;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import interfaces.Dessinable;

/**
 * Classe coeur: classe permettant la realisation du nombre de vies du joueur
 * 
 * @author Arnaud Lefebvre
 *
 */
public class Coeurs implements Dessinable {

	@Override
	public void dessiner(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	
	
	
	public void image() {
	URL urlCoeur=  getClass().getClassLoader().getResource("coeur.jpg");
	if (urlCoeur == null) {
		JOptionPane.showMessageDialog(null , "Fichier coeur.jpg introuvable");
		System.exit(0);}
		ImageIcon coeur= new ImageIcon( urlCoeur );
		JLabel etiquette = new JLabel(coeur);
		JButton bouton = new JButton(coeur);	
	}
	
}
