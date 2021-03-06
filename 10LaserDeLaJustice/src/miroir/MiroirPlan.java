package miroir;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import geometrie.Vecteur;
import geometrie.VecteurGraphique;
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

	private double longueur; 
	private Shape miroirTransfo;
	private Vecteur normal;
	private Vecteur position;
	private Vecteur inter;
	private double largeur = 0.2;
	private boolean modeSci = false;
	private VecteurGraphique vecGraph;

	//Par Miora
	/**
	 * Constructeur d'un miroir plan
	 * @param position la position la plus au centre du miroir
	 * @param angle angle de miroir avec une rotation en degre a partir de x,y
	 */
	public MiroirPlan(Vecteur position, double angle, int longueur) {
		super();
		this.position = position;
		this.angle = angle;
		if(angle>=180) {
			this.angle = angle-180;
		}
		this.longueur = longueur;
	}
	public Vecteur[] coordonneHautBas() {
		Vecteur tab[] = new Vecteur[2];
		//haut du miroir
		tab[0] = new Vecteur (position.getX() + Math.cos(Math.toRadians(-angle))*longueur/2, position.getY() - Math.sin(Math.toRadians(-angle))*longueur/2);
		
		//bas du miroir
		tab[1] = new Vecteur (position.getX() - Math.cos(Math.toRadians(-angle))*longueur/2, position.getY() + Math.sin(Math.toRadians(-angle))*longueur/2);
		return tab;
	}

	//Par Miora
	/**
	 * Dessiner le miroir
	 * @param g2d le composant graphique
	 * @param mat la matrice de transformation
	 * @param hauteur la hauteur du composant acceuillant le miroir
	 * @param largeur la largeur du composant acceuillant le miroir
	 */
	public void dessiner(Graphics2D g2d, AffineTransform mat, double hauteur, double largeur) {
		AffineTransform matLocale = new AffineTransform(mat);
		matLocale.rotate(Math.toRadians(-angle),position.getX(),position.getY());
		miroir = new Rectangle2D.Double(position.getX()-longueur/2,position.getY(), longueur, this.largeur);
		miroirTransfo = matLocale.createTransformedShape(miroir); // transforme en pixel
		g2d.setColor(Color.RED);
		g2d.fill(miroirTransfo); //dessine en pixel
		
		if(modeSci) {
			matLocale = new AffineTransform(mat);
			
			//Vecteur normal
			double lgVec = longueur;
			g2d.setColor(Color.red);
			normal = normal.multiplie(lgVec/normal.module()); //on agrandit la taille du vecteur
			vecGraph = new VecteurGraphique(normal);
			vecGraph.setOrigineXY(inter.getX(), inter.getY());  
			vecGraph.setLongueurTete(0.5);
			vecGraph.dessiner(g2d, matLocale, hauteur, largeur);
		}
	}

	//Par Miora
	/**
	 * Methode qui retourne l'aire du miroir
	 * @return aire du miroir
	 */
	public Area getAireMiroirPixel() {
		AffineTransform matLocal = new AffineTransform();
		matLocal.rotate(Math.toRadians(-angle), position.getX(), position.getY());
		Rectangle2D.Double rect = new Rectangle2D.Double(position.getX()-longueur/2, position.getY(),longueur, this.largeur);
		return new Area(matLocal.createTransformedShape(((rect))));
	}

	//Par Miora
	/**
	 * Methode pour obtenir le vecteur normal au miroir
	 * @return le vecteur perpendiculaire au miroir
	 */
	public Vecteur getNormal(Vecteur vecteurDirecteurLaser) {
		double angleMiroirNormal;
		angleMiroirNormal = Math.toRadians(angle);
		
		//Calcul selon le systeme g2d
		Vecteur vecMiroir = new Vecteur (Math.cos(angleMiroirNormal), -Math.sin(angleMiroirNormal));
		normal = new Vecteur(-vecMiroir.getY(), vecMiroir.getX()).normalise();;
		
		normal = (normal.multiplie(vecteurDirecteurLaser.prodScalaire(normal)/normal.prodScalaire(normal))).multiplie(-1).normalise();
		return normal;
	}

	//Arezki 
	/**
	 * Cette m�thode permet de faire la mise � jour de la position du miroir plan 
	 * @param x c'est la nouvelle position x du miroir dans les unit�s du r�el
	 * @param y c'est la nouvelle position y du miroir dans les unit�s du r�el
	 */

	public void setPosition(double x , double y) {
		this.x = x;
		this.y = y;
	}

	//Par Miora
	/**
	 * Retourne la position du miroir
	 * @return la position du centre du miroir en vecteur
	 */
	public Vecteur getPosition() {
		return position;
	}

	//Par Miora
	/**
	 * Change la position du miroir
	 * @param position la nouvelle position
	 */
	public void setPosition(Vecteur position) {
		this.position = position;
	}

	//Par Miora
	/**
	 * Cette methode permet d'obtenir l'angle du miroir
	 * @return l'angle du miroir
	 */
	public double getAngle() {
		return angle;
	}

	//Par Miora
	/**
	 * Cette methode permet de modifier l'angle du miroir
	 * @param angle le nouvel angle
	 */
	public void setAngle(double angle) {
		this.angle = angle;
	}


	//Par Miora
	/**
	 * Cette methode permet de dessiner le mode scientifique (vecteur normal)
	 * @param inter la position d'intersection avec le laser
	 * @param retourne vrai si le mode scientifique est active
	 */
	public void modeScientifique(Vecteur inter, boolean modeSci) {
		this.modeSci = modeSci;
		this.inter = inter;
	}


}

