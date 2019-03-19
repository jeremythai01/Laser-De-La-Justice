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
	private Vecteur posLaser;
	private boolean laser=false;

	/**
	 * Constructeur du miroir convexe
	 * @param position : la position du miroir sous forme vectorielle
	 * @param rayon
	 * note : la position du miroir est calcule a partir de son centre
	 */
	public MiroirConvexe(Vecteur position, double rayon) {
		System.out.println(position);
		this.position = new Vecteur (position.getX()-rayon/2 , position.getY()-rayon/2);
		centreMiroir = new Vecteur (position.getX(),position.getY());
		this.rayon = rayon;
	}

	/**
	 * Dessiner le miroir convexe
	 */
	public void dessiner(Graphics2D g2d, AffineTransform mat, double hauteur, double largeur) {
		matLocale = new AffineTransform(mat);
		this.hauteur = hauteur;
		this.largeur = largeur;
		centre = new Ellipse2D.Double(centreMiroir.getX(),centreMiroir.getY() , 0.05, 0.05);
		miroir = new Arc2D.Double(position.getX(), position.getY(), rayon, rayon, -180, 180, Arc2D.OPEN);
		g2d.draw(matLocale.createTransformedShape(miroir));
		g2d.draw(matLocale.createTransformedShape(centre));
		
		//dessine la normal ( verification : utilisation meme variablle que la methode getNormalPosition() ) 
		if(laser) {
			g2d.draw(mat.createTransformedShape(new Line2D.Double(centreMiroir.getX(), centreMiroir.getY(),posLaser.getX() , posLaser.getY())));
		}
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
	 * @param posLaser : la position du laser
	 * @return le vecteur normal au miroir
	 */
	public Vecteur getNormalPosition(Vecteur posLaser) {
		this.posLaser = posLaser;
		laser= true;
		return centreMiroir.soustrait(posLaser) ;
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
