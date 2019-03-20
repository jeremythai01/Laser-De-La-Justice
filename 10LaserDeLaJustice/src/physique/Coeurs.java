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
	private Image imgGris=null;
	private int combien;
	private int combienGris;


	public Coeurs(int combien) {
		this.combien=combien;
		combienGris=combien;
		URL urlCoeur = getClass().getClassLoader().getResource("coeur.png");
		if (urlCoeur == null) {
			JOptionPane.showMessageDialog(null , "Fichier coeur.png introuvable");
			System.exit(0);}
		try {
			img = ImageIO.read(urlCoeur);
		}
		catch (IOException e) {
			System.out.println("Erreur pendant la lecture du fichier d'image");
		}

		URL urlCoeurGris = getClass().getClassLoader().getResource("coeurGris.png");
		if (urlCoeurGris == null) {
			JOptionPane.showMessageDialog(null , "Fichier coeurGris.png introuvable");
			System.exit(0);}
		try {
			imgGris = ImageIO.read(urlCoeurGris);
		}
		catch (IOException e) {
			System.out.println("Erreur pendant la lecture du fichier d'image");
		}



	}

	@Override
	/**
	 * Cette méthode donnera la possibilité de dessiner les objets
	 * @param g: c'est le paramètre Graphics2D qui permettra de dessiner les objets
	 * @param mat: c'est la matrice qui permettra de transformer le monde pixel en monde monde réel
	 * @param hauteur: c'est la hauteur en pixel de la scene
	 * @param largeur: C'est la largeure en pixel de la scene
	 */
	public void dessiner(Graphics2D g, AffineTransform mat, double hauteur ,double largeur) {
		AffineTransform matLocale = new AffineTransform(mat);
		double factPersoY = 0.5 / img.getHeight(null);
		double factPersoX = 0.5 / imgGris.getWidth(null);

		// redimenssionne le personnage
		matLocale.scale(factPersoX, factPersoY);

		//Deplacement du personnage a sa position initiale
		matLocale.translate( (0.5) / factPersoX , (2-0.5) / factPersoY);
		
		
		for(int i=0; i<combienGris; i++) {
			g.drawImage(imgGris, 0+20*i, 0, 20, 20, null, null);
			
		}

		for(int i=0; i<combien; i++) {
			g.drawImage(img, 0+20*i, 0, 20, 20, null, null);
		}


	}
/**
 * 
 * @param combien
 */
	public void setCombien(int combien) {
		this.combien=combien;
	}

}
