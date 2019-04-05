package effets;

import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import geometrie.Vecteur;
import interfaces.Dessinable;
import physique.Laser;
import physique.MoteurPhysique;

 abstract class Pouvoir implements Dessinable {

	private Vecteur position; 
	private Vecteur vitesse; 
	private Vecteur accel;
	private Image img = null;
	private Rectangle2D.Double rectFantome;


	Pouvoir(Vecteur position , Vecteur vitesse, Vecteur accel) {
		this.position = new Vecteur(position);
		this.vitesse = new Vecteur (vitesse);
		this.accel = new Vecteur (accel);
	}
	abstract Area getAire();
	abstract void lireImage();

	/**
	 * modifie ou affecte une vitesse a celle courante de la balle
	 * @param vitesse vecteur des vitesse x et y
	 */
	public void setVitesse(Vecteur vitesse) {
		Vecteur newVec = new Vecteur(vitesse.getX(), vitesse.getY());
		this.vitesse = newVec;
	}

	/**
	 * Retourne la vitesse courante
	 * @return la vitesse courante
	 */
	public Vecteur getVitesse() {
		return (vitesse);
	}

	/**
	 * Modifie la position de la balle
	 * @param pos vecteur des positions x et y
	 */
	public void setPosition(Vecteur pos) {
		Vecteur newVec = new Vecteur(pos.getX(), pos.getY());
		this.position = newVec;
	}
	
	/**
	 * Retourne la position courante
	 * @return la position courante
	 */
	public Vecteur getPosition() { return (position); }
	
	public void setAccel(Vecteur accel) {
		Vecteur newVec = new Vecteur(accel.getX(), accel.getY());
		this.accel = newVec;
	}
	
	
	/**
	 * Effectue une iteration de l'algorithme d'Euler implicite. Calcule la nouvelle vitesse et la nouvelle
	 * position de la balle.
	 * @param deltaT intervalle de temps (pas)
	 */
	public void unPasEuler(double deltaT) {
		MoteurPhysique.unPasEuler(deltaT, position, vitesse, accel);
	}


	public Image getImg() {
		return img;
	}

	public void setImg(BufferedImage img) {
		this.img = img;
	}


	public Rectangle2D.Double getRectFantome() {
		return rectFantome;
	}

	public void setRectFantome(Rectangle2D.Double rectFantome) {
		this.rectFantome = rectFantome;
	}
}
