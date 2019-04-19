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

	private double angle,x=0,y=0;
	private Rectangle2D.Double miroir;

	private double longueur = 20; 
	private Vecteur normal;
	private Vecteur position;
	private Vecteur positionBas;
	private double largeur = 0.1;
	private Shape transfo;


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

	//Par Miora
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
		positionBas = new Vecteur ( position.getX() + (longueur * Math.cos(Math.toRadians(angle))),
				position.getY() - (longueur * Math.sin(Math.toRadians(angle))));
	}

	//Par Miora
	/**
	 * Cette methode permet d'obtenir l'angle du miroir
	 * @return : l'angle du miroir
	 */
	public double getAngle() {
		return angle;
	}

	//Par Miora
	/**
	 * Cette methode permet de modifier l'angle du miroir
	 * @param angle : le nouvel angle
	 */
	public void setAngle(double angle) {
		this.angle = angle;
	}

	//Par Miora
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
		Rectangle2D.Double miroir = new Rectangle2D.Double(position.getX(), position.getY(), longueur,this.largeur);
		matLocale.rotate(Math.toRadians(-angle),position.getX(), position.getY());
		transfo = matLocale.createTransformedShape(miroir);
		g2d.fill(transfo);
	}

	//Par Miora
	/**
	 * Methode qui retourne aire du miroir
	 * @return aire du miroir
	 */
	public Area getAireMiroirPixel() {
		AffineTransform matLocale = new AffineTransform();
		matLocale.rotate(Math.toRadians(-angle),position.getX(), position.getY());
		Rectangle2D.Double aireMir = new Rectangle2D.Double(position.getX(), position.getY(), longueur,this.largeur);
		return new Area (matLocale.createTransformedShape(aireMir));
	}

	//Par Miora
	/**
	 * Methode pour obtenir le vecteur normal au miroir
	 * @return le vecteur perpendiculaire au miroir
	 */
	public Vecteur getNormal() {
		double angleMiroirNormal;
		angleMiroirNormal = Math.toRadians(angle);
		Vecteur vecMiroir = new Vecteur (Math.cos(angleMiroirNormal), Math.sin(angleMiroirNormal));
		System.out.println("miroir orientation dir " + vecMiroir);
		normal = new Vecteur(vecMiroir.getY(), -vecMiroir.getX()).normalise();
		return normal;
	}

	//Arezki 
	/**
	 * Cette méthode permet de faire la mis à jour de la position du miroir plan 
	 * @param x c'est la nouvelle position x du miroir dans les unités du réel
	 * @param y c'est la nouvelle position y du miroir dans les unités du réel
	 */

	public void setPosition(double x , double y) {
		this.x = x;
		this.y = y;
	}

	//Par Arezki
	/**
	 * Cette methode permet de trouver l'aire pour deplacement
	 * @return une aire
	 */
	public Area getAire() {
		return new Area(miroir);
	}

}

