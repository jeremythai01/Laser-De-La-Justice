package personnage;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import geometrie.Vecteur;
import geometrie.VecteurGraphique;
import interfaces.Dessinable;

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
	private double positionIni;
	private Image imgPerso = null;
	private double HAUTEUR_COMPO;
	private double positionX ;
	private boolean premiereFois = true;
	private int toucheGauche = 37, toucheDroite = 39, toucheTir = 32;
	private double vitesseX =0;
	private boolean gauche;
	private boolean droite;
	private boolean bougePas;
	private static final double VITESSE = 0.1;
	private double tempsMort = 0;
	private Type type;
	private boolean modeSouris = false;
	private double posSouris;
	private VecteurGraphique flecheAngle ;

	/**
	 * Classe enumeration des types de joueurs
	 * @author Jeremy Thai
	 */
	private enum Type {
		JOUEUR1, JOUEUR2;
	}



	/**
	 Constructeur 1 de la classe.
	 @param gauche : le code (KeyCode) de la touche gauche lorsque clique, le personnage va aller a gauche
	 @param droite : le code (KeyCode) de la touche droite lorsque clique, le personnage va aller a droite
	 */
	//Miora
	public Personnage(double position, int gauche, int droite, int tir, String type) {


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

		switch(type) {

		case "JOUEUR1" : 
			this.type = Type.JOUEUR1;
			break;

		case "JOUEUR2" : 
			this.type = Type.JOUEUR2;
			break;
		}


		this.positionIni = position;
		this.toucheGauche = gauche;
		this.toucheDroite = droite;
		this.toucheTir = tir;
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
	 * @param g2d : le contexte graphique
	 * @param mat : la matrice de transformation
	 * @param hauteurScene : la hauteur de la scene
	 * @param largeurScene : la largeur de la scene
	 */
	//Miora
	public void dessiner(Graphics2D g2d, AffineTransform mat, double hauteurScene, double largeurScene) {
		//System.out.println("je suis dans dessiner " + getPositionX() );
		AffineTransform matLocale = new AffineTransform(mat);
		largeurCompo = largeurScene;
		HAUTEUR_COMPO = hauteurScene;
		if(premiereFois) {
			//System.out.println("je suis dans dessiner " + positionIni);
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
		if(code == toucheTir) {
			bougePas = false;
		}
		update();
	}

	public void relacheTouche() {

		bougePas = false;
		update();
	}


	//Jeremy Thai
	/**
	 * Méthode qui permet de modifier la position du personnage selon la vitesse en x 
	 */
	public void bouge() { 
		positionX += vitesseX;
		update();
	}

	//Jeremy Thai
	/**
	 * Méthode qui permet de modifier la vitesse du personnage selon la touche enfoncee
	 */
	public void update() {

		if(modeSouris) {
			if(bougePas) {
				vitesseX = 0;
				return;
			}
			
			if(Math.abs(posSouris-positionX) < 0.1 ) {
				vitesseX = 0;
				return;
			}
			if(posSouris < positionX ){
				vitesseX = -VITESSE;
				return;
			}
			if(posSouris > positionX) {
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
		System.out.println(this.positionX);
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
	public Area airePersonnage() {
		return new Area( new Rectangle2D.Double(positionX,
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

/**
 * Methode qui retourne le temps d'invicibilite du personnage pendant qu'il  est mort 
 * @return tempsMort temps d'invicibilite du personnage pendant qu'il est mort 
 */
	//Jeremy Thai
	public double getTempsMort() {
		return tempsMort;
	}
	/**
	 * Methode qui modifie le temps d'invicibilite du personnage pendant qu'il  est mort 
	 * @param tempsMort temps d'invicibilite du personnage pendant qu'il est mort 
	 */
		//Jeremy Thai
	public void setTempsMort(double tempsMort) {
		this.tempsMort = tempsMort;
	}








}
