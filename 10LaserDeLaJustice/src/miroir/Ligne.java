package miroir;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import geometrie.Vecteur;

/**
 * Cette classe permet d'avoir une ligne avec 2 points
 * @author Miora
 *
 */
public class Ligne extends Line2D.Double{


	private Point2D.Double debut, fin;
	private double epaisseur = 0.1;

	/**
	 * Constructeur de la classe
	 * @param debut : le point du debut
	 * @param fin : le point de la fin
	 */
	public Ligne(Point2D.Double debut, Point2D.Double fin) {
		super(debut, fin);
		this.debut = debut;
		this.fin = fin;
	}

	/**
	 * Cette methode permet de prendre l'aire de la ligne sous forme de mini-rectangle
	 * @return l'aire d'une ligne
	 */
	public Area getAireLigne() {
		AffineTransform aff = new AffineTransform();
		Rectangle2D.Double fantome = new Rectangle2D.Double(debut.x, debut.y,epaisseur, epaisseur);
		return new Area (aff.createTransformedShape(fantome));
	}



}
