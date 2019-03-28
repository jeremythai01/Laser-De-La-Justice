package miroir;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import geometrie.Vecteur;
import interfaces.Dessinable;
import physique.Laser;

/**
 * Classe des miroirs plan
 * @author Miora
 * @author Arezki
 *
 */
public class MiroirPlan implements Dessinable {
	
	private double x=0, y=0, angle=0;
	private Rectangle2D.Double miroir;
	private AffineTransform matLocale;
	private double longueur = 2; 
	private Shape miroirTransfo;
	private Vecteur normal;
	
	/**
	 * Constructeur d'un miroir plan
	 * @param x : la position la plus a gauche du miroir en x
	 * @param y : la position la plus a droite du miroir en y
	 * @param angle : angle de miroir avec une rotation en degre a partir de x,y
	 // Miora
	 */
	public MiroirPlan(double x, double y, double angle) {
		super();
		this.x = x;
		this.y = y;
		this.angle = angle;
	}
	/**
	 * Dessiner le miroir
	 * @param g2d : le composant graphique
	 * @param mat : la matrice de transformation
	 * @param hauteur : la hauteur du composant acceuillant le miroir
	 * @param largeur : la largeur du composant acceuillant le miroir
	 //Miora
	 */
	public void dessiner(Graphics2D g2d, AffineTransform mat, double hauteur, double largeur) {
		matLocale = new AffineTransform(mat);
		matLocale.rotate(-angle,x,y);
		miroir = new Rectangle2D.Double(x,y, longueur, 0.05);
		//miroirTransfo = matLocale.createTransformedShape(miroir);
		//g2d.fill(miroirTransfo);
		g2d.draw(matLocale.createTransformedShape(miroir));
	}
	/**
	 * Methode qui retourne aire du miroir
	 * @return aire du miroir
	 //Miora
	 */
	public Area getAireMiroir() {
		return new Area (miroir);
	}
	
	/**
	 * Methode pour obtenir le vecteur normal au miroir
	 * @return le vecteur perpendiculaire au miroir
	 //Miora
	 */
	public Vecteur getNormal() {
		angle = Math.toRadians(angle);
		Vecteur vecMiroir = new Vecteur (Math.cos(angle), Math.sin(angle));
		normal = new Vecteur(-vecMiroir.getY(), vecMiroir.getX());
		return normal;
	}
	
	/**
	 * Cette m�thode permet de faire la mis � jour de la position du miroir plan 
	 * @param x c'est la nouvelle position x du miroir dans les unit�s du r�el
	 * @param y c'est la nouvelle position y du miroir dans les unit�s du r�el
	 //Arezki 
	 */
	
	public void setPosition(double x , double y) {
		this.x = x;
		this.y = y;
	}

}
