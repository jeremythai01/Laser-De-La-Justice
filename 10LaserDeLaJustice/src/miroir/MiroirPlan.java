package miroir;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import geometrie.Vecteur;
import interfaces.Dessinable;
import physique.Laser;

/**
 * Classe des miroirs plan
 * @author Miora
 * @author Arezki
 *
 */
public class MiroirPlan implements Dessinable, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private double x=0, y=0, angle=0;
	private Rectangle2D.Double miroir;

	private double longueur = 2; 
	private Shape miroirTransfo;
	private Vecteur normal;
	private Vecteur position;
	private Vecteur positionBas;
	private double largeur = 0.05;
	
	
	public Vecteur getPositionBas() {
		return positionBas;
	}
	public void setPositionBas(Vecteur positionBas) {
		this.positionBas = positionBas;
	}
	public Vecteur getPosition() {
		return position;
	}
	public void setPosition(Vecteur position) {
		this.position = position;
	}
	
	

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
	public void setAngle(double angle) {
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
		AffineTransform matLocale = new AffineTransform(mat);
		matLocale.rotate(Math.toRadians(-angle),position.getX(),position.getY());
		miroir = new Rectangle2D.Double(position.getX(),position.getY(), longueur, this.largeur);
		//Ellipse2D.Double haut = new Ellipse2D.Double(position.getX()-0.05, position.getY()-0.05, 0.1, 0.1);
		//g2d.draw(matLocale.createTransformedShape(haut));
		miroirTransfo = matLocale.createTransformedShape(miroir); // transforme en pixel
		g2d.draw(miroirTransfo); //dessine en pixel
	}
	/**
	 * Methode qui retourne aire du miroir
	 * @return aire du miroir
	 //Miora
	 */
	public Area getAireMiroirPixel() {
		/*AffineTransform matLocale = new AffineTransform();
		matLocale.rotate(Math.toRadians(-angle),position.getX(),position.getY());
		miroir = new Rectangle2D.Double(position.getX(),position.getY(), longueur, 0.01);
		return new Area(miroir);
		*/
		AffineTransform matLocal = new AffineTransform();
		matLocal.rotate(Math.toRadians(-angle), position.getX(), position.getY());
		Rectangle2D.Double rect = new Rectangle2D.Double(position.getX(), position.getY(),longueur, this.largeur);
		return new Area(matLocal.createTransformedShape(((rect))));
	}
	
	public Area getAire() {
		return new Area(miroir);
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
		normal = new Vecteur(vecMiroir.getY(), -vecMiroir.getX()).normalise();
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
