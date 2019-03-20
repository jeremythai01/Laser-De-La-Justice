package personnage;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
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
 *  @author Jeremy Thai
 *
 */
public class Personnage implements Dessinable {
	private final double LONGUEUR_PERSO = 1.6;
	private final double LARGEUR_PERSO = 1;
	private double largeurCompo;
	private double positionIni;
	private Image imgPerso = null;
	private double HAUTEUR_COMPO;
	private double positionX ;
	private boolean premiereFois = true;
	private int toucheGauche = 37, toucheDroite = 39;


	private double vitesseX =0;
	private boolean gauche;
	private boolean droite;
	private boolean bougePas;

	private static final double VITESSE = 0.1;




	/**
	 Constructeur 1 de la classe.
	 @param gauche : le code (KeyCode) de la touche gauche lorsque clique, le personnage va aller a gauche
	 @param droite : le code (KeyCode) de la touche droite lorsque clique, le personnage va aller a droite
	 */
	//Miora
	public Personnage(int gauche, int droite) {
		URL fich = getClass().getClassLoader().getResource("narutoDebout.png");
		if (fich == null) {
			JOptionPane.showMessageDialog(null, "Fichier narutoDebout.jpg introuvable!");
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
	 Constructeur 2 de la classe.
	 */
	//Miora
	public Personnage() {
		URL fich = getClass().getClassLoader().getResource("narutoDebout.png");
		if (fich == null) {
			JOptionPane.showMessageDialog(null, "Fichier narutoDebout.jpg introuvable!");
		} else {
			try {
				imgPerso = ImageIO.read(fich);
			} catch (IOException e) {
				System.out.println("Erreur de lecture du fichier d'image");
			}
		}
	}
	/**
	 * Methode permettant de savoir la position initial du personnage a partir du cote le plus a
	 * gauche.
	 * @return la position initial du personnage
	 */
	//Miora
	public double getPOSITION_INITIALE() {
		return positionIni;
	}
	/**
	 * Methode permettant de dessiner le personnage
	 */
	//Miora
	public void dessiner(Graphics2D g2d, AffineTransform mat, double hauteurScene, double largeurScene) {

		AffineTransform matLocale = new AffineTransform(mat);
		largeurCompo = largeurScene;
		HAUTEUR_COMPO = hauteurScene;
		positionIni=largeurCompo/2;
		if(premiereFois) {
			positionX = positionIni-LARGEUR_PERSO/2;
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
	/**
	 * Cette méthode permet de rendre le changement de direction du personnage plus rapide
	 * @param e touche enfoncee
	 */
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

	//Jeremy Thai
	/**
	 * Méthode qui permet de modifier la position du personnage selon la vitesse en x 
	 */
	public void bouge() { 
		positionX += vitesseX;
	}

	//Jeremy Thai
	/**
	 * Méthode qui permet de modifier la vitesse du personnage selon la touche enfoncee
	 */
	public void update() {
		vitesseX = 0;
		if(gauche) 
			vitesseX = -VITESSE;
		if(droite) 
			vitesseX = VITESSE;
		if(bougePas) 
			vitesseX = 0;

	}

	//Jeremy Thai
	/**
	 * méthode qui permet au personnage de ne pas bouger si un laser est lancé
	 */
	public void neBougePas() {
		bougePas = true; 
		update();
	}

	/**
	 * Methode permettant de changer la vitesse du personnage
	 */
	//Jeremy Thai
	public void setVitesseX(double vitesseX) {
		this.vitesseX = vitesseX;
	}
	/**
	 * Methode permettant de savoir la position du personnage
	 * @return la position initiale du personnage
	 */
	//Miora
	public double getPositionX() {
		return positionX;
	}
	/**
	 * Methode permettant de modifier la position du personnage.
	 * @param positionX : position voulue du personnage
	 */
	//Miora
	public void setPositionX(double positionX) {
		this.positionX = positionX;
	}
	/**
	 * Methode permettant qui retourne la longueur du personnage.
	 * @return la longueur du personnage
	 */
	//Miora
	public double getLONGUEUR_PERSO() {
		return LONGUEUR_PERSO;
	}
	/**
	 * Methode permettant de savoir la largeur du personnae
	 * @return la largeur du personnage
	 */
	//Miora
	public double getLARGEUR_PERSO() {
		return LARGEUR_PERSO;
	}
	
	/**
	 * Methode permettant de savoir la touche utilise pour bouger le personnage a gauche
	 * @return la latouche pour bouger le personnage a gauche
	 */
	//Miora
	public int getToucheGauche() {
		return toucheGauche;
	}
	/**
	 * Methode de changer la touche utilise pour bouger le personnage a gauche
	 * @param la nouvelle touche gauche
	 */
	//Miora
	public void setToucheGauche(int toucheGauche) {
		this.toucheGauche = toucheGauche;
	}
	/**
	 * Methode permettant de savoir la touche utilise pour bouger le personnage a gauche
	 * @return la latouche pour bouger le personnage a gauche
	 */
	//Miora
	public int getToucheDroite() {
		return toucheDroite;
	}
	/**
	 * Methode de changer la touche utilise pour bouger le personnage a droite
	 * @param la nouvelle touche droite
	 */
	//Miora
	public void setToucheDroite(int toucheDroite) {
		this.toucheDroite = toucheDroite;
	}


	public Area airePersonnage() {
	return new Area( new Rectangle2D.Double(positionX,
			HAUTEUR_COMPO - LONGUEUR_PERSO, LARGEUR_PERSO, LONGUEUR_PERSO));
	}
	
}
