package miroir;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Line2D;

import geometrie.Vecteur;
import interfaces.Dessinable;

public class MiroirConvexe implements Dessinable {
	private double x=0, y=0, rayon=0;  // x et y sont les coordonne du centre de l'arc
	private Arc2D.Double miroir ;
	private AffineTransform matLocale;
	private Vecteur normal; 
	private Line2D.Double m;
	private double hauteur =0;
	private double largeur = 0;
	
	/**
	 * Constructeur du miroir convexe
	 * @param x : la position en x la plus a gauche du rectangle qu contient l'arc
	 * @param y : la position en x la plus a gauche du rectangle qu contient l'arc
	 * @param rayon
	 * note : x et y vont devenir les coordonnes du centre du miroir
	 */
	public MiroirConvexe(double x, double y, double rayon) {
		this.x = x - rayon/2;
		this.y = y - rayon/2;
		this.rayon = rayon;
	}

/**
 * Dessiner le miroir convexe
 */
	public void dessiner(Graphics2D g2d, AffineTransform mat, double hauteur, double largeur) {
		matLocale = new AffineTransform(mat);
		this.hauteur = hauteur;
		this.largeur = largeur;
		miroir = new Arc2D.Double(x, y, rayon, rayon, -180, 180, Arc2D.OPEN);
		g2d.fill(matLocale.createTransformedShape(miroir));
	}
	
	/**
	 * Cette methode permet d'obtenir l'aire du miroir convexe
	 * @return l'aire du miroir convexe
	 */
	public Area getAireMiroirConvexe() {
		return new Area (miroir);
	}

	/**
	 * Cette methode retourne le vecteur normal du laser apres intersection avec
	 * le miroir convexe
	 * @param position : la position de l'intersection
	 * @return le vecteur normal au miroir
	 */
	public Vecteur getNormalPosition(Vecteur position) {
		Vecteur centrePoint = position.soustrait(new Vecteur(x,y));
		return centrePoint;
	}


}
