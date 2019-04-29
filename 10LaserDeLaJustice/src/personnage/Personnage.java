package personnage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import aaplication.Scene;
import geometrie.Vecteur;
import geometrie.VecteurGraphique;
import interfaces.Dessinable;
import physique.Balle;
import physique.Coeurs;

/**
 * La classe personnage sert a creer le heros du jeu.
 * @author Miora
 *  @author Jeremy Thai
 *
 */
public class Personnage implements Dessinable, Serializable {
	private final double LONGUEUR_PERSO = 1.6;
	private final double LARGEUR_PERSO = 1;
	private double largeurCompo;
	private double position;
	private Image imgPerso = null;
	private double HAUTEUR_COMPO;
	private boolean premiereFois = true;
	private int toucheGauche = 37, toucheDroite = 39, toucheTir = 32;
	private double vitesseX =0;
	private boolean gauche;
	private boolean droite;
	private boolean bougePas;
	private static final double VITESSE = 0.1;
	private boolean modeSouris = false;
	private double posSouris;
	private boolean bouclierActive = false;
	private Ellipse2D.Double ellipse;
	private double tempsMort = 0;
	private boolean mort= false;
	private String imageNom;
	

	private boolean enVitesse = false;

	/**
	 Constructeur 1 de la classe.
	 @param position: la position du personnage
	 @param gauche : le code (KeyCode) de la touche gauche lorsque clique, le personnage va aller a gauche
	 @param droite : le code (KeyCode) de la touche droite lorsque clique, le personnage va aller a droite
	@param tir : le code (keyCode) de la touche de tir
	 */
	//Miora
	public Personnage(double position, int gauche, int droite, int tir) {
		URL fich = getClass().getClassLoader().getResource("AstronautMilieu.png");
		if (fich == null) {
			JOptionPane.showMessageDialog(null, "Fichier astronaut introuvable!");
		} else {
			try {
				imgPerso = ImageIO.read(fich);
				imageNom = "Milieu";
			} catch (IOException e) {
				System.out.println("Erreur de lecture du fichier d'image");
			}
		}
		this.position = position;
		this.toucheGauche = gauche;
		this.toucheDroite = droite;
		this.toucheTir = tir;
		System.out.println(KeyEvent.getKeyText(toucheTir));
	}

	// Jeremy Thai
	private void lireImage(String str) {
		URL fich = getClass().getClassLoader().getResource("Astronaut"+str+".png");
		if (fich == null) {
			JOptionPane.showMessageDialog(null, "Fichier astronaut introuvable!");
		} else {
			try {
				imgPerso = ImageIO.read(fich);
				imageNom = str;
			} catch (IOException e) {
				System.out.println("Erreur de lecture du fichier d'image");
			}
		}
	}

	/**
	 * Methode permettant de dessiner le personnage
	 * @param g2d : le contexte graphique
	 * @param mat : la matrice de transformation
	 * @param hauteurScene : la hauteur de la scene
	 * @param largeurScene : la largeur de la scene
	 */
	//Miora
	public void dessiner(Graphics2D g2d, AffineTransform mat, double hauteurScene, double largeurScene) {
		AffineTransform matLocale = new AffineTransform(mat);
		largeurCompo = largeurScene;
		HAUTEUR_COMPO = hauteurScene;
		
		//position = positionIni-LARGEUR_PERSO/2;
		premiereFois = false;
		
		//Facteur de réduction de l'image du bloc 
		double factPersoY = LONGUEUR_PERSO / imgPerso.getHeight(null);
		double factPersoX = LARGEUR_PERSO / imgPerso.getWidth(null);

		// redimenssionne le personnage
		matLocale.scale(factPersoX, factPersoY);

		//Deplacement du personnage a sa position initiale
		matLocale.translate( (position) / factPersoX , (HAUTEUR_COMPO-LONGUEUR_PERSO) / factPersoY);
		g2d.drawImage(imgPerso, matLocale, null); 

		if(bouclierActive) {
			matLocale.setTransform(mat);
			ellipse = new Ellipse2D.Double(position,HAUTEUR_COMPO-LONGUEUR_PERSO , LARGEUR_PERSO, LONGUEUR_PERSO);
			g2d.setColor(new Color(1.0f, 1.0f, 0.5f, 0.6f ));	
			g2d.fill(matLocale.createTransformedShape(ellipse));
			g2d.setColor(new Color(0.0f, 1.0f, 0.2f, 0.9f ));	
			g2d.draw(matLocale.createTransformedShape(ellipse)); // Contour vert 
		}


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
		
		if (code == KeyEvent.VK_SPACE) 
			bougePas = true;
		update();
	}

	//Jeremy Thai
	/**
	 * Cette méthode permet de rendre le changement de direction du personnage plus rapide en mode clavier 
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
		if(code == toucheTir) {
			bougePas = false;
		}
		update();
	}

	//Jeremy Thai
		/**
		 * Cette méthode permet de rendre le changement de direction du personnage plus rapide en mode souris
		 */
	public void relacheToucheEnModeSouris() {
		bougePas = false;
		update();
	}


	//Jeremy Thai
	/**
	 * Méthode qui permet de modifier la position du personnage selon la vitesse en x 
	 */
	public void bouge() { 
		position += vitesseX;
		update();
	}

	//Jeremy Thai
	/**
	 * Méthode qui permet de modifier la vitesse du personnage selon la touche enfoncee
	 */
	public void update() {
		animation();

		if(modeSouris) {
			if(bougePas) {
				vitesseX = 0;
				return;
			}

			if(Math.abs(posSouris-position) < 0.1 ) {
				vitesseX = 0;
				return;
			}
			if(posSouris < position ){
				vitesseX = -VITESSE;
				return;
			}
			if(posSouris > position) {
				vitesseX = VITESSE;
				return;
			}

		} else {

			vitesseX = 0;
			if(gauche) 
				vitesseX = -VITESSE;
			if(droite) 
				vitesseX = VITESSE;
			if(bougePas) 
				vitesseX = 0;
		}
	}

	//Jeremy Thai
	/**
	 * méthode qui permet au personnage de ne pas bouger si un laser est lancé
	 */
	public void neBougePas() {
		bougePas = true; 
		update();
	}
	
	public void neBougePasLorsqueTir(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_SPACE) 
			neBougePas();
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
	 */
	//Miora
	public double getPosition() {
		return position;
	}
	/**
	 * Methode permettant de modifier la position du personnage.
	 * @param positionX : position voulue du personnage
	 */
	//Miora
	public void setPosition(double positionX) {
		this.position = positionX;
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


	/**
	 * Cree et retourne une aire en forme de rectangle du personnage 
	 * @return aire du personnage 
	 */
	//Jeremy Thai
	public Area getAire() {
		return new Area( new Rectangle2D.Double(position,
				HAUTEUR_COMPO - LONGUEUR_PERSO, LARGEUR_PERSO, LONGUEUR_PERSO));
	}


	/**
	 * Methode permettant de savoir la touche utilise pour tirer un laser 
	 * @return toucheTir touche pour tirer un laser
	 */
	//Jeremy Thai
	public int getToucheTir() {return toucheTir; }

	/**
	 * Methode pour changer la touche utilise pour tirer un laser
	 * @param touchetir touche pour tirer un laser
	 */
	//Jeremy Thai
	public void setToucheTir(int toucheTir) {
		this.toucheTir = toucheTir;
	}


	//Jeremy Thai
	/**
	 * Methode qui retourne la valeur booleenne du mode de souris 
	 * @return modeSouris variable qui montre si le mode souris est activé ou non.
	 */
	public boolean isModeSouris() {return modeSouris;}

	//Jeremy Thai
	/**
	 * Methode qui modifie  la valeur booleenne du mode de souris 
	 * @param modeSouris nouvelle valeur du mode de la souris passée en parametres
	 */
	public void setModeSouris(boolean modeSouris) {
		this.modeSouris = modeSouris;
	}

	/**
	 * Methode qui retourne la position x  courante de la souris 
	 * @return posSouris position courante de la souris en x
	 */
	//Jeremy Thai
	public double getPosSouris() {return posSouris;}

	/**
	 * Methode qui modifie  la position x  courante de la souris 
	 * @param posSouris nouvelle position  de la souris en x passée en parametres
	 */
	//Jeremy Thai
	public void setPosSouris(double posSouris) {
		this.posSouris = posSouris;
	}

	//Jeremy Thai
	/**
	 * Retourne la valeur booleenne du bouclier afin de savoir si celui-ci est active ou non 
	 * @return bouclierActive valeur booleene  du bouclier 
	 */
	public boolean isBouclierActive() {
		return bouclierActive;
	}
	//Jeremy Thai
		/**
		 * Modifie la valeur booleenne du bouclier par celle passee en parametre
		 * @param bouclierActive nouvelle valeur booleene du bouclier 
		 */
	public void setBouclierActive(boolean bouclierActive) {
		this.bouclierActive = bouclierActive;
	}

	/**
	 * Methode qui retourne le temps mort du personnage  
	 * @return tempsMort temps mort du personnage 
	 */
	//Jeremy Thai
	public double getTempsMort() {
		return tempsMort;
	}

	/**
	 * Methode qui modifie le temps mort du personnage  
	 * @param tempsMort temps mort du personnage 
	 */
	//Jeremy Thai
	public void setTempsMort(double tempsMort) {
		this.tempsMort = tempsMort;
	}

	//Jeremy Thai
	/**
	 * retourne la valeur booleene mort du personnage 
	 * @return mort booleen qui represente si le personnage est mort ou non 
	 */
	public boolean isMort() {
		return mort;
	}
	
	//Jeremy Thai
	/**
	 * Modifie la valeur booleene mort du personnage par celle passée en paramètre
	 * @param mort nouveau booleen
	 */
	public void setMort(boolean mort) {
		this.mort = mort;
	}
	
	//Jeremy Thai
		/**
		 * retourne la valeur booleene de la vitesse du personnage 
		 * @return enVitesse booleen qui represente si le personnage est en vitesse ou non  
		 */
	public boolean isEnVitesse() {
		return enVitesse;
	}
	
	//Jeremy Thai
			/**
			 * Modifie la valeur booleene de la vitesse du personnage 
			 * @param enVitesse nouveau booleen qui represente si le personnage est en vitesse ou non  
			 */
	public void setEnVitesse(boolean enVitesse) {
		this.enVitesse = enVitesse;
	}
	
	//Jeremy Thai 
	/**
	 * Animation des images du personnages selon son déplacement 
	 */
	private void animation() {

		if( vitesseX == 0)
			lireImage("Milieu");
		if( vitesseX > 0) 
			lireImage("DroitCours");

		if( vitesseX < 0)
			lireImage("GaucheCours");
	}
}




