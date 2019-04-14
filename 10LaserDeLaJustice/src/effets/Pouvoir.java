package effets;

import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import aaplication.Scene;
import geometrie.Vecteur;
import interfaces.Dessinable;
import personnage.Personnage;
import physique.Balle;
import physique.Coeurs;
import physique.Laser;
import physique.MoteurPhysique;
import physique.SceneTest;

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
	public abstract Area getAire();
	abstract void lireImage();
	public abstract void activeEffet( Scene scene);
	
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
	 * Effectue une iteration de l'algorithme de Verlet. Calcule la nouvelle vitesse et la nouvelle
	 * position de la balle.
	 * @param deltaT intervalle de temps (pas)
	 */
	public void unPasVerlet(double deltaT) {
		MoteurPhysique.unPasEuler(deltaT, position, vitesse, accel);
	}

	public Image getImg() {
		return img;
	}

	public void setImg(BufferedImage img) {
		this.img = img;
	}

	public void setImg(Image img) {
		this.img = img;
	}


	public Rectangle2D.Double getRectFantome() {
		return rectFantome;
	}

	public void setRectFantome(Rectangle2D.Double rectFantome) {
		this.rectFantome = rectFantome;
	}

	
	public double getLargeurImg() {
		return largeurImg;
	}
	public void setLargeurImg(double largeurImg) {
		this.largeurImg = largeurImg;
	}
	
	public double getLongueurImg() {
		return longueurImg;
	}
	public void setLongueurImg(double longueurImg) {
		this.longueurImg = longueurImg;
	}
	
	public double getCompteurAvantDisparaitre() {
		return compteurAvantDisparaitre;
	}
	public void setCompteurAvantDisparaitre(double compteurAvantDisparaitre) {
		this.compteurAvantDisparaitre = compteurAvantDisparaitre;
	}
	 
	
}
