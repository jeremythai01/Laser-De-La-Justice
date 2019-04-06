package miroir;

import java.awt.Color;
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

	private double longueur = 2; 
	private Shape miroirTransfo;
	private Vecteur normal;
	private Vecteur position;
	private boolean dessin = false;
	private Area aireMiroir;
	
	/**
	 * Constructeur d'un miroir plan
	 * @param position : la position la plus a gauche du miroir plan
	 * @param angle : angle de miroir avec une rotation en degre a partir de x,y
	 // Miora
	 */
	public MiroirPlan(Vecteur position, double angle) {
		super();
		this.position = position;
		this.angle = angle;
	}
	public double getAngle() {
		return angle;
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
		AffineTransform matLocale = new AffineTransform(mat);
		matLocale.rotate(Math.toRadians(-angle),position.getX(),position.getY());
		miroir = new Rectangle2D.Double(position.getX(),position.getY(), longueur, 0.05);
		miroirTransfo = matLocale.createTransformedShape(miroir); // transforme en pixel
		g2d.draw(miroirTransfo); //dessine en pixel
	}
	/**
	 * Methode qui retourne aire du miroir
	 * @return aire du miroir
	 //Miora
	 */
	public Area getAireMiroirPixel() {
		AffineTransform matLocale = new AffineTransform();
		matLocale.rotate(Math.toRadians(-angle),position.getX(),position.getY());
		miroir = new Rectangle2D.Double(position.getX(),position.getY(), longueur, 0.05);
		return new Area(matLocale.createTransformedShape(((miroir))));
	}
	
	/**
	 * Methode pour obtenir le vecteur normal au miroir
	 * @return le vecteur perpendiculaire au miroir
	 //Miora
	 */
	public Vecteur getNormal() {
		double angleMiroirNormal;
		angleMiroirNormal = Math.toRadians(angle);
		Vecteur vecMiroir = new Vecteur (Math.cos(angleMiroirNormal), Math.sin(angleMiroirNormal));
		System.out.println("orientation du miroir " + vecMiroir);
		normal = new Vecteur(vecMiroir.getY(), -vecMiroir.getX()).normalise();
		System.out.println("angle normal du miroir degree" + normal);
		return normal;
	}
	
	/**
	 * Cette méthode permet de faire la mis à jour de la position du miroir plan 
	 * @param x c'est la nouvelle position x du miroir dans les unités du réel
	 * @param y c'est la nouvelle position y du miroir dans les unités du réel
	 //Arezki 
	 */
	
	public void setPosition(double x , double y) {
		this.x = x;
		this.y = y;
	}

}
