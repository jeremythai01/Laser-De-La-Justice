package physique;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

import geometrie.Vecteur;
import interfaces.Dessinable;

public class Mur implements Dessinable {


	private Area aireMur ;

	private Vecteur position;
	private double largeur, hauteur;
	private double largeurMonde, hauteurMonde;
	private Type type;

	/**
	 * Classe enumeration des types de mur
	 * @author Jeremy Thai
	 *
	 */
	private enum Type {
		HORIZONTAL,VERTICAL;
	}



	public Mur( Vecteur position, double largeur, double hauteur) {
		setPosition( position );
		this.hauteur = hauteur ; 
		this.largeur = largeur;
	}



	public Mur( Vecteur position, String choix) {
		setPosition( position );

	}
	/**
	 * Modifie la position de la balle
	 * @param pos vecteur des positions x et y
	 */
	public void setPosition(Vecteur pos) {
		Vecteur newVec = new Vecteur(pos.getX(), pos.getY());
		this.position = newVec;
	}


	public Area getAireMur() {
		Rectangle2D.Double rect = new Rectangle2D.Double(position.getX(), position.getY(), largeur, hauteur );
		aireMur = new Area(rect);
		return aireMur;
	}

	@Override
	public void dessiner(Graphics2D g2d, AffineTransform mat, double hauteurMonde, double largeurMonde) {
		AffineTransform matLocal = new AffineTransform(mat);
		g2d.setColor(Color.blue);
		Rectangle2D.Double rect = new Rectangle2D.Double(position.getX(), position.getY(), largeur, hauteur );
		g2d.draw(matLocal.createTransformedShape(rect));

	}

	public void collisionsMurBalles(Balle balle) {


		if(balle.getPosition().getX() >= position.getX()+ largeur) {// balle a droite du mur 
			balle.getVitesse().setX(-balle.getVitesse().getX());
			System.out.println("a droite ");
			return;
		}

		if(balle.getPosition().getX() + balle.getDiametre() >= position.getX()) { // balle a gauche du mur 
			balle.getVitesse().setX(-balle.getVitesse().getX());
			return;
		}

		if(balle.getPosition().getY() + balle.getDiametre() <= position.getY()){ // balle en haut du mur 
			balle.getVitesse().setY(-balle.getVitesse().getY());
			return;
		}
		
		if(balle.getPosition().getY() <= position.getY() + hauteur) { // balle en bas du mur
			balle.getVitesse().setY(-balle.getVitesse().getY());
			return;
		}
		
	}

}





