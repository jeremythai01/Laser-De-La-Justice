package physique;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
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

	private Image img=null;
	private int combien;
	

	public Coeurs(int combien) {
		this.combien=combien;
		URL urlCoeur = getClass().getClassLoader().getResource("coeur.png");
		if (urlCoeur == null) {
			JOptionPane.showMessageDialog(null , "Fichier coeur.jpg introuvable");
			System.exit(0);}
		 try {
			 img = ImageIO.read(urlCoeur);
			 }
		 catch (IOException e) {
			 System.out.println("Erreur pendant la lecture du fichier d'image");
			 }
	}
	
	
	/*public void image() {
	URL urlCoeur=  getClass().getClassLoader().getResource("coeur.jpg");
	if (urlCoeur == null) {
		JOptionPane.showMessageDialog(null , "Fichier coeur.jpg introuvable");
		System.exit(0);}
		coeur= new ImageIcon( urlCoeur );
		etiquette = new JLabel(coeur);
		bouton = new JButton(coeur);	
	}*/




	@Override
	public void dessiner(Graphics2D g, AffineTransform mat, int hauteur) {
		for(int i=0; i<combien; i++) {
			g.drawImage(img, 0+20*i, 0+20*i, 20, 20, Color.WHITE, null);
		}
		
		
	}
	
}
