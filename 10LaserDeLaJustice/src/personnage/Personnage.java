package personnage;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import geometrie.Vecteur;
import interfaces.Dessinable;

/**
 * La classe personnage sert a creer le heros du jeu.
 * @author Miora
 *
 */
public class Personnage implements Dessinable {
	private double LONGUEUR_PERSO = 1.6;
	private double LARGEUR_PERSO = 1;
	private double LARGEUR_COMPO;
	private  double POSITION_INITIALE;
	private Image imgPerso = null;
	private double HAUTEUR_COMPO;
	private double positionX ;
	private boolean premiereFois = true;
	private int toucheGauche = 37, toucheDroite = 39;
	
	public int getToucheGauche() {
		return toucheGauche;
	}
	public void setToucheGauche(int toucheGauche) {
		this.toucheGauche = toucheGauche;
	}
	public int getToucheDroite() {
		return toucheDroite;
	}
	public void setToucheDroite(int toucheDroite) {
		this.toucheDroite = toucheDroite;
	}
	private double vitesseX =0;
	private boolean gauche;
	private boolean droite;
	private boolean bougePas;

	private static final double VITESSE = 0.1;




	/**
	 Constructeur de la classe.
	 @param gauche : le code (KeyCode) de la touche gauche lorsque clique, le personnage veut aller a gauche
	 @param droite : le code (KeyCode) de la touche droite lorsque clique, le personnage veut aller a gauche
	 */
	public Personnage(int gauche, int droite) {
		URL fich = getClass().getClassLoader().getResource("narutoDebout.png");
		if (fich == null) {
			JOptionPane.showMessageDialog(null, "Fichier Fnaruto_debout.jpg introuvable!");
		} else {
			try {
				imgPerso = ImageIO.read(fich);
			} catch (IOException e) {
				System.out.println("Erreur de lecture du fichier d'image");
			}
		}
		this.toucheGauche = gauche;
		this.toucheDroite = droite;
	}
	/**
	 * Methode permettant de savoir la position initial du personnage a partir du cote le plus a
	 * gauche.
	 * @return la position initial du personnage
	 */
	public double getPOSITION_INITIALE() {
		return POSITION_INITIALE;
	}
	/**
	 * Methode permettant de dessiner le personnage
	 */
	public void dessiner(Graphics2D g2d, AffineTransform mat, double hauteurScene, double largeurScene) {

		AffineTransform matLocale = new AffineTransform(mat);
		LARGEUR_COMPO = largeurScene;
		HAUTEUR_COMPO = hauteurScene;
		POSITION_INITIALE=LARGEUR_COMPO/2;
		if(premiereFois) {
			positionX = POSITION_INITIALE-LARGEUR_PERSO/2;
			premiereFois = false;
		}
		//Facteur de réduction de l'image du bloc 
		double factPersoY = LONGUEUR_PERSO / imgPerso.getHeight(null);
		double factPersoX = LARGEUR_PERSO / imgPerso.getWidth(null);

		// redimenssionne le personnage
		matLocale.scale(factPersoX, factPersoY);

		//Deplacement du personnage a sa position initiale
		matLocale.translate( (positionX) / factPersoX , (HAUTEUR_COMPO-LONGUEUR_PERSO) / factPersoY);
		g2d.drawImage(imgPerso, matLocale, null); 


	}
	/**
	 * Cette methode permet de deplacer le personnage  selon les touches du clavier dans option de jeu.
	 * Si ces touches n'ont pas ete modife, gauche et droite seront les touches qui feront bouger le personnage
	 * @param e : la touche enfoncee
	 */
	//Miora
	public void deplacerLePersoSelonTouche(KeyEvent e) {
		int code = e.getKeyCode();

		if(code == toucheGauche) {
			gauche = true;
		}
		if(code == toucheDroite) {
			droite = true;
		}
		update();
	}
	
	//Jeremy Thai
	public void relacheTouche(KeyEvent e) {
		int code = e.getKeyCode();
		if(code == toucheGauche) {
			gauche = false;
		}
		if(code == toucheDroite) {
			droite = false;
		}
		if(code == KeyEvent.VK_SPACE) {
			bougePas = false;
		}
		
		
		update();

	}


	public void move() { 
		positionX += vitesseX;
	}

	
	public void update() {
		vitesseX = 0;
		if(gauche) 
			vitesseX = -VITESSE;
		if(droite) 
			vitesseX = VITESSE;
		if(bougePas) 
			vitesseX = 0;
		
	}

	public void shoot(int code) {
			bougePas = true; 
			update();
		}
	
	
	


	public void setVitesseX(double vitesseX) {
		this.vitesseX = vitesseX;
	}
	/**
	 * Methode permettant de savoir la position du personnage
	 * @return la position initiale du personnage
	 */
	public double getPositionX() {
		return positionX;
	}
	/**
	 * Methode permettant de modifier la position du personnage.
	 * @param positionX : position voulue du personnage
	 */
	public void setPositionX(double positionX) {
		this.positionX = positionX;
	}
	/**
	 * Methode permettant qui retourne la longueur du personnage.
	 * @return la longueur du personnage
	 */
	public double getLONGUEUR_PERSO() {
		return LONGUEUR_PERSO;
	}
	/**
	 * Methode permettant de savoir la largeur du personnae
	 * @return la largeur du personnage
	 */
	public double getLARGEUR_PERSO() {
		return LARGEUR_PERSO;
	}
}
