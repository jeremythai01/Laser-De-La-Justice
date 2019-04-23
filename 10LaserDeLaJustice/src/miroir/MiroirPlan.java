package miroir;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import geometrie.Vecteur;
import interfaces.Dessinable;

/**
 * Classe des miroirs plan
 * @author Miora
 * @author Arezki
 *
 */
public class MiroirPlan implements Dessinable, Serializable {

	private static final long serialVersionUID = 1L;

	private double angle=0,x=0,y=0;
	private Rectangle2D.Double miroir;

	private double longueur = 10; 
	private Shape miroirTransfo;
	private Vecteur normal;
	private Vecteur position;
	private Vecteur inter;
	private double largeur = 0.1;
	private Area aireMiroir;
	private boolean modeSci;

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
		matLocale.rotate(Math.toRadians(-angle),position.getX(),position.getY());
		miroir = new Rectangle2D.Double(position.getX(),position.getY(), longueur, this.largeur);
		miroirTransfo = matLocale.createTransformedShape(miroir); // transforme en pixel
		g2d.fill(miroirTransfo); //dessine en pixel
		if(modeSci == true) {
			matLocale = new AffineTransform(mat);
			g2d.fill(matLocale.createTransformedShape(new Ellipse2D.Double(inter.getX()-0.5/2, inter.getY()-0.5/2, 0.5, 0.5)));
		}
	}

	//Par Miora
	/**
	 * Methode qui retourne aire du miroir
	 * @return aire du miroir
	 */
	public Area getAireMiroirPixel() {
		AffineTransform matLocal = new AffineTransform();
		matLocal.rotate(Math.toRadians(-angle), position.getX(), position.getY());
		Rectangle2D.Double rect = new Rectangle2D.Double(position.getX(), position.getY(),longueur, this.largeur);
		return new Area(matLocal.createTransformedShape(((rect))));
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
	
	/**
	 * Retourne la position du miroir
	 * @return
	 */
	public Vecteur getPosition() {
		return position;
	}
	/**
	 * Change la position du miroir
	 * @param position : la nouvelle position
	 */
	public void setPosition(Vecteur position) {
		this.position = position;
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


	public void afficherVecteur(Vecteur normal, Vecteur inter) {
		modeSci = true;
		this.inter = inter;
	}


}

