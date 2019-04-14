package miroir;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import geometrie.Vecteur;
import interfaces.Dessinable;

/**
 * Classe des miroirs convexes
 * @author Miora
 */
public class MiroirConvexe implements Dessinable {

	private Vecteur position;
	private Vecteur centreMiroir;
	private double x=0, y=0, rayon=0;  // x et y sont les coordonne du centre de l'arc
	private Arc2D.Double miroir ;
	private AffineTransform matLocale;
	private Vecteur normal; 
	private Line2D.Double m;
	private double hauteur =0;
	private double largeur = 0;
	private Ellipse2D.Double centre;
	private Vecteur vecLaser;
	private boolean laser=false;
	private double angle;

	/**
	 * Constructeur du miroir convexe
	 * @param position : le centre du miroir sous forme vectorielle
	 * @param rayon
	 */
	public MiroirConvexe(Vecteur position, double rayon, double angle) {
		this.position = position;
		this.rayon = rayon;
		this.angle = angle;
	}
	/**
	 * Dessiner le miroir convexe
	 * @param g2d : le composant graphique
	 * @param mat : la matrice de transformation
	 * @param hauteur : la hauteur de la scene
	 * @param largeur : largeur de la scene 
	 */
	public void dessiner(Graphics2D g2d, AffineTransform mat, double hauteur, double largeur) {
		this.hauteur = hauteur;
		this.largeur = largeur;
		AffineTransform matLocale = new AffineTransform(mat);
		
		//On tourne
		matLocale.rotate(Math.toRadians(-angle),position.getX(),position.getY());
		
		//On place l'arc au centre
		matLocale.translate(-rayon/2, -rayon/2);
		
		miroir = new Arc2D.Double(position.getX(), position.getY(), rayon, rayon, -180, 180, Arc2D.OPEN);
		g2d.draw(matLocale.createTransformedShape(miroir));
	}

	/**
	 * Cette methode permet d'obtenir l'aire du miroir convexe
	 * @return l'aire du miroir convexe
	 */
	public Area getAireMiroirConvexe() {
		AffineTransform matLocale = new AffineTransform();
		//On tourne
		matLocale.rotate(Math.toRadians(-angle),position.getX(),position.getY());
		//On place l'arc au centre
		matLocale.translate(-rayon/2, -rayon/2);
		
		miroir = new Arc2D.Double(position.getX(), position.getY(), rayon, rayon, -180, 180, Arc2D.OPEN);
		return new Area (matLocale.createTransformedShape(((miroir))));
		
	}

	/**
	 * Cette methode retourne le vecteur normal du laser apres intersection avec
	 * le miroir convexe
	 * @param posLaser : la position du laser
	 * @return le vecteur normal au miroir
	 */
	public Vecteur getNormalPosition(Vecteur posLaser) {
		return posLaser.soustrait(centreMiroir) ;
	}
	
	public Vecteur getNormalPosition(double x, double y) {
		Vecteur laser = new Vecteur (x,y);
		return laser.soustrait(centreMiroir) ;
	}
	/**
	 * Methode qui retourne la position du miroir
	 * @return le vecteur associe a la position du miroir
	 */
	public Vecteur getPosition() {
		return position;
	}
	/**
	 * Methode qui modifie la position du miroir
	 * @param position : le nouveau vecteur ou sera la position du miroir
	 */
	public void setPosition(Vecteur position) {
		this.position = position;
	}


}
