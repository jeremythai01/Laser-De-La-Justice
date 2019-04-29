package effets;

import java.awt.Image;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import aaplication.Scene;
import geometrie.Vecteur;
import interfaces.Dessinable;
import physique.MoteurPhysique;

/**
 * Classe abstraite contenant les méthodes permettant de créer le "squelette" d'un objet pouvoir.
 * @author Jérémy Thai 
 */
public abstract class Pouvoir implements Dessinable {

	private Vecteur position; 
	private Vecteur vitesse; 
	private Vecteur accel;
	private Image img = null;
	private Rectangle2D.Double rectFantome;
	private double largeurImg, longueurImg;
	private double compteurAvantDisparaitre = 0;

	
	Pouvoir(Vecteur position , Vecteur accel) {
		this.position = new Vecteur(position);
		this.vitesse = new Vecteur();
		this.accel = new Vecteur (accel);
	}
	
	/**
	 * Méthode abstraite qui retourne l'aire du pouvoir 
	 * @return aire du pouvoir 
	 */
	public abstract Area getAire();
	
	/**
	 * Méthode abstraite qui permet de lire l'image selon le type de pouvoir 
	 */
	abstract void lireImage();
	
	/**
	 * Méthode abstraite qui permet d'active l'effet selon le type de pouvoir 
	 * @param scene scene 
	 */
	public abstract void activeEffet( Scene scene);
	
	/**
	 * Constructeur ou la position et l'acceleration initiales sont spécifiés
	 * @param position Vecteur incluant les positions en x et y du coin superieur-gauche
	 * @param accel Vecteur incluant les accelerations en x et y  
	 */
	
	/**
	 * Effectue une iteration de l'algorithme de Verlet. Calcule la nouvelle vitesse et la nouvelle
	 * position du pouvoir
	 * @param deltaT intervalle de temps (pas)
	 */
	public void unPasVerlet(double deltaT) {
		MoteurPhysique.unPasVerlet(deltaT, position, vitesse, accel);
	}
	
	/**
	 * modifie ou affecte une vitesse a celle courante du pouvoir 
	 * @param vitesse vecteur des vitesse x et y du pouvoir 
	 */
	public void setVitesse(Vecteur vitesse) {
		Vecteur newVec = new Vecteur(vitesse.getX(), vitesse.getY());
		this.vitesse = newVec;
	}

	/**
	 * Modifie la position du pouvoir 
	 * @param pos vecteur des positions x et y du pouvoir 
	 */
	public void setPosition(Vecteur pos) {
		Vecteur newVec = new Vecteur(pos.getX(), pos.getY());
		this.position = newVec;
	}

	/**
	 * Associe une acceleration, ou modifie l'acceleration courante du pouvoir
	 * @param accel vecteur des accélérations en x et y du pouvoir 
	 */
	public void setAccel(Vecteur accel) {
		Vecteur newVec = new Vecteur(accel.getX(), accel.getY());
		this.accel = newVec;
	}
	
	/**
	 * Modifie l'image du pouvoir par celle passée en paramètre
	 * @param img nouvelle image en paramètre
	 */
	public void setImg(Image img) { this.img = img; }

	/**
	 * Modifie la longueur de l'image par celle passée en paramètre
	 * @param longueurImg  nouvelle longueur de l'image 
	 */
	public void setLongueurImg(double longueurImg) { this.longueurImg = longueurImg; }
	
	/**
	 * Modifie la largeur de l'image par celle passée en paramètre
	 * @param largeurImg  nouvelle largeur de l'image 
	 */
	public void setLargeurImg(double largeurImg) {
		this.largeurImg = largeurImg;
	}

	/**
	 * Modifie le rectangle invisible du pouvoir par celui en paramètre
	 * @param rectFantome nouveau rectangle invisible
	 */
	public void setRectFantome(Rectangle2D.Double rectFantome) { this.rectFantome = rectFantome; }
	
	/**
	 * Modifie le compteur qui compte le nombre de temps avant que le pouvoir disparait par celui passé en paramètre
	 * @param compteurAvantDisparaitre nouveau compteur de temps 
	 */
	public void setCompteurAvantDisparaitre(double compteurAvantDisparaitre) { this.compteurAvantDisparaitre = compteurAvantDisparaitre; }
	
	
	/**
	 * Retourne la vitesse courante du pouvoir 
	 * @return la vitesse courante du pouvoir 
	 */
	public Vecteur getVitesse() { return (vitesse); }

	
	/**
	 * Retourne la position courante du pouvoir 
	 * @return la position courante du pouvoir 
	 */
	public Vecteur getPosition() { return (position); }

	
	/**
	 * Retourne l'image du pouvoir 
	 * @return img image du pouvoir 
	 */
	public Image getImg() { return img; }
	
	/**
	 * Retourne le rectangle invisible du pouvoir 
	 * @return rectFantome rectangle invisible
	 */
	public Rectangle2D.Double getRectFantome() { return rectFantome; }

	/**
	 * Retourne la largeur de l'image   
	 * @return largeurImg largeur de l'image 
	 */
	public double getLargeurImg() { return largeurImg; }
	
	
	/**
	 * Retourne la longueur de l'image   
	 * @return longueurImg longueur de l'image 
	 */
	public double getLongueurImg() { return longueurImg; }
	
	
	/**
	 * Retourne le compteur qui compte le nombre de temps avant que le pouvoir disparait
	 * @return compteurAvantDisparaitre compteur de temps 
	 */
	public double getCompteurAvantDisparaitre() { return compteurAvantDisparaitre; }
	
	 
	
}
