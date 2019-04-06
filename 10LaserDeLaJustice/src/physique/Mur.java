package physique;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

import geometrie.Vecteur;
import interfaces.Dessinable;

/**
 * Classe qui s'occupe de créer  un mur en mémorisant sa position et ses dimensions. 
 * @author Jeremy Thai
 */


public class Mur implements Dessinable {


	private Area aireMur ;

	private Vecteur position;
	private double largeur, hauteur;
	private double largeurMonde, hauteurMonde;
	private Vecteur normal;



	public Mur( Vecteur position, double largeur, double hauteur ) {
		setPosition( position );
		this.hauteur = hauteur ; 
		this.largeur = largeur;
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

	@Override
	public void dessiner(Graphics2D g2d, AffineTransform mat, double hauteurMonde, double largeurMonde) {
		AffineTransform matLocal = new AffineTransform(mat);
		g2d.setColor(Color.blue);
		Rectangle2D.Double rect = new Rectangle2D.Double(position.getX(), position.getY(), largeur, hauteur );
		g2d.fill(matLocal.createTransformedShape(rect));
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
	

}



