package physique;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import geometrie.Vecteur;
import interfaces.Dessinable;


/**
 * Classe qui s'occupe de créer un mur en mémorisant sa position et ses dimensions. 
 * @author Jeremy Thai
 */


public class Mur implements Dessinable  {


	private Area aireMur ;
	private Rectangle2D.Double rect;
	private Vecteur position;
	private double largeur, hauteur;
	private double largeurMonde, hauteurMonde;
	private Type type;



	/**
	 * Classe enumeration des types de mur
	 * @author Jeremy Thai
	 *
	 */
	enum Type {
		VERTICAL, HORIZONTAL;
	}

	/**
	 * Constructeur ou la position et le type de mur sont spécifiés
	 * @param position Vecteur incluant les positions en x et y du coin superieur-gauche du mur 
	 * @param type type de mur( vertical, horizontal)
	 */
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
	 * Permet de dessiner le mur selon le contexte graphique en parametre.
	 * @param g2d contexte graphique
	 * @param mat matrice de transformation monde-vers-composant
	 * @param hauteur hauteur du monde reelle
	 * @param largeur largeur du monde reelle
	 * 
	 */
	@Override
	public void dessiner(Graphics2D g2d, AffineTransform mat, double hauteur, double largeur) {
		g2d.setColor(Color.black);
		AffineTransform matLocal = new AffineTransform(mat);
		rect = new Rectangle2D.Double(position.getX(),position.getY(), largeur, hauteur);
		g2d.fill(matLocal.createTransformedShape(rect));
	}
	
	
	
	/**
	 * Modifie la position du mur 
	 * @param pos vecteur des positions x et y du mur 
	 */
	public void setPosition(Vecteur pos) {
		Vecteur newVec = new Vecteur(pos.getX(), pos.getY());
		this.position = newVec;
	}

	/**
	 * Modifie la largeur du mur par celle passée en paramètre
	 * @param largeur nouvelle largeur du mur 
	 */
	public void setLargeur(double largeur) { this.largeur = largeur; }


	/**
	 * Modifie la hauteur du mur par celle passée en paramètre
	 * @param largeur nouvelle hauteur du mur 
	 */
	public void setHauteur(double hauteur) { this.hauteur = hauteur; }


	/**
	 * Modifie la largeur de la scene par celle passée en parametre 
	 * @param largeurMonde nouvelle largeur de la scene  
	 */
	public void setLargeurMonde(double largeurMonde) { this.largeurMonde = largeurMonde; }

	
	/**
	 * Modifie la hauteur de la scene par celle passée en parametre 
	 * @param hauteurMonde nouvelle hauteur de la scene  
	 */
	public void setHauteurMonde(double hauteurMonde) { this.hauteurMonde = hauteurMonde; }
	
	/**
	 * Retourne la position courante du mur 
	 * @return la position courante du mur 
	 */
	public Vecteur getPosition() { return (position); }


	/**
	 * retourne l'aire du mur  en forme de rectangle
	 * @return aireMur aire du mur 
	 */
	public Area getAire() {
		Rectangle2D.Double rect = new Rectangle2D.Double(position.getX(), position.getY(), largeur, hauteur );
		aireMur = new Area(rect);
		return aireMur;
	}

	/**
	 * Retourne la largeur du mur  
	 * @return largeur du mur  
	 */
	public double getLargeur()  {return largeur; }


	/**
	 * Retourne la hauteur du mur  
	 * @return hauteur du mur  
	 */
	public double getHauteur() { return hauteur; }


	/**
	 * Retourne le type de mur
	 * @return type type de mur 
	 */
	public Type getType() { return type; }

	/**
	 * Retourne la largeur de la scene 
	 * @return largeurMonde largeur de la scene 
	 */
	public double getLargeurMonde() { return largeurMonde; }

	
	/**
	 * Retourne la hauteur de la scene 
	 * @return hauteurMonde hauteur de la scene 
	 */
	public double getHauteurMonde() { return hauteurMonde; }

	
}



