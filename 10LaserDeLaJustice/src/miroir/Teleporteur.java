package miroir;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import geometrie.Vecteur;
import interfaces.Dessinable;

/**
 * Classe des teleporteurs
 * @author Miora
 *
 */
public class Teleporteur implements Dessinable, Serializable {

	private static final long serialVersionUID = 1L;
	private Vecteur positionPremier,positionDeuxieme;
	private double longueur = 6, epaisseur = 0.1;
	private Rectangle2D.Double premier, deuxieme;

	/**
	 * Constructeur de la classe 
	 * @param positionPremier : la position du premier teleporteur
	 */
	public Teleporteur (Vecteur positionPremier) {
		this.positionPremier = positionPremier;
		positionDeuxieme = new Vecteur (positionPremier.getX() + 2*longueur, positionPremier.getY());
	}

	/**
	 * Dessiner le teleporteur
	 * @param g2d le contexte graphique
	 * @param mat la matrice de rotation
	 * @param hauteur la hauteur du monde en unite reel
	 * @param largeur la largeur du monde en unite reel
	 */
	public void dessiner(Graphics2D g2d, AffineTransform mat, double hauteur, double largeur) {
		AffineTransform matLocal = new AffineTransform (mat);
		g2d.setColor(Color.yellow);
		premier = new Rectangle2D.Double(positionPremier.getX()-longueur/2, positionPremier.getY(), longueur, epaisseur);
		g2d.fill(matLocal.createTransformedShape(premier));
		deuxieme = new Rectangle2D.Double(positionDeuxieme.getX()-longueur/2, positionDeuxieme.getY(), longueur, epaisseur);
		g2d.fill(matLocal.createTransformedShape(deuxieme));

	}

	/**
	 * Cette methode permet d'avoir l'aire du premier teleporteur
	 * @return l'aide du premier teleporteur
	 */
	public Area getAireTeleporteur1() {
		return new Area (premier);
	}

	/**Cette methode permet d'avoir l'aire du deuxieme teleporteur
	 * @return l'aire du deuxieme teleporteur
	 */
	public Area getAireTeleporteur2() {
		return new Area (deuxieme);
	}

	/**
	 * Cette methode permet d'avoir la position du premier teleporteur
	 * @return la position du premier teleporteur 
	 */
	public Vecteur getPositionPremier() {
		return positionPremier;
	}

	/**
	 * Cette methode permet de changer la position du premier teleporteur
	 * @param positionPremier la nouvelle position du premier teleporteur
	 */
	public void setPositionPremier(Vecteur positionPremier) {
		this.positionPremier = positionPremier;
	}

	/**
	 * Cette methode permet d'avoir la position du deuxieme teleporteur
	 * @return la position du deuxieme teleporteur 
	 */
	public Vecteur getPositionDeuxieme() {
		return positionDeuxieme;
	}

	/**
	 * Cette methode permet de changer la position du deuxieme teleporteur
	 * @param positionPremier la nouvelle position du deuxieme teleporteur
	 */
	public void setPositionDeuxieme(Vecteur positionDeuxieme) {
		this.positionDeuxieme = positionDeuxieme;
	}

	/**
	 * Cette methode permet de changer la longueur du teleporteur
	 * @return la longueur du teleporteur
	 */
	public double getLongueur() {
		return longueur;
	}

	/**
	 * Cette methode permet de changer la longueur du teleporteur
	 * @param longueur la longueur du teleporteur
	 */
	public void setLongueur(double longueur) {
		this.longueur = longueur;
	}


}
