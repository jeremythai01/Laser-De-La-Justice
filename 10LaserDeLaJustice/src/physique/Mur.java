package physique;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

import geometrie.Vecteur;
import interfaces.Dessinable;
import physique.Balle.Type;

/**
 * Classe qui s'occupe de créer  un mur en mémorisant sa position et ses dimensions. 
 * @author Jeremy Thai
 */


public class Mur  {


	private Area aireMur ;

	private Vecteur position;
	private double largeur, hauteur;
	private double largeurMonde, hauteurMonde;



	private Type type;



	/**
	 * Classe enumeration des types de balle
	 * @author Jeremy Thai
	 *
	 */
	enum Type {
		VERTICAL, HORIZONTAL;
	}



	public Mur( Vecteur position, String type ) {
		setPosition( position );

		switch(type) {
		case "VERTICAL" : 
			this.type = Type.VERTICAL;
			break;
			
		case "HORIZONTAL" : 
			this.type = Type.HORIZONTAL;
			break;
		}

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


	public Area getAire() {
		Rectangle2D.Double rect = new Rectangle2D.Double(position.getX(), position.getY(), largeur, hauteur );
		aireMur = new Area(rect);
		return aireMur;
	}

	public double getLargeur() {
		return largeur;
	}

	public void setLargeur(double largeur) {
		this.largeur = largeur;
	}



	public double getHauteur() {
		return hauteur;
	}

	public void setHauteur(double hauteur) {
		this.hauteur = hauteur;
	}


	public Type getType() { 
		return type;
	}
}



