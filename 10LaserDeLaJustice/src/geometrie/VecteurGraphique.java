package geometrie;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import interfaces.*;
/**
 * 
 * @author Cette classe était déjà construite
 *
 */
public class VecteurGraphique extends Vecteur implements Dessinable {

	//caractéristiques supplemetaires utiles pour le dessin
	private double origX=0, origY=0;			 //originep our dessiner le vecteur
	private Line2D.Double corps, traitDeTete;    //pour tracer la flèche
	private double angleTete = 0.5;              //angle entre les deux segments formant la tete de fleche
	private double longueurTete = 20;            //longueur des segments formant la tete (en pixels)


	public VecteurGraphique() {
		super();
	}

	public VecteurGraphique(double x, double y) {
		super(x, y);
	}

	public VecteurGraphique(Vecteur v) {
		super(v.x, v.y);
	}

	public VecteurGraphique (double x, double y, double origX, double origY) {
		super(x, y);
		this.origX = origX;
		this.origY = origY;
	}

	/**
	 * Cree les formes geometriques qui constituent le vecteur (à la position d'origine 0,0)
	 */
	private void creerRepresentationGeometrique() {
		//on cree la geometriea l'origine. Le tout sera dessiné avec une translation si son origine est ailleurs
		corps = new Line2D.Double(0, 0, x, y);
		double moduleVec  = module();
		double ratio = (moduleVec - longueurTete)/moduleVec;
		traitDeTete = new Line2D.Double( x*ratio, y*ratio, x, y);
	}

	/**
	 * Dessine le vecteur sous la forme d'une flèche orientée
	 * @param g2d Le contexte graphique
	 */
	public void dessiner(Graphics2D g2d, AffineTransform mat,double hauteur, double largeur) {	

		creerRepresentationGeometrique();

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.translate(origX, origY);

		g2d.draw( corps );  										//ligne formant le vecteur lui-meme
		g2d.rotate(angleTete/2, x,  y);
		g2d.draw(traitDeTete); 	//un des deux traits qui forment la tete du vecteur
		g2d.rotate(-angleTete/2, x,  y);
		g2d.draw(traitDeTete); //L'autre trait de tête

	}// fin

	/**
	 * Modifie l'origine du vecteur pour son dessin
	 * @param origX origine en x
	 * @param origY origine en y
	 */
	public void setOrigineXY(double origX, double origY) {
		this.origX = origX;
		this.origY = origY;
	}

	/**
	 * Retourne la longueur du segment utilisé pour tracer la flèche formant l'extrémité du vecteur
	 * @return Longueur du segment
	 */
	public double getLongueurTete() {
		return longueurTete;
	}

	/**
	 * Modifie la longueur du segment utilisé pour tracer la flèche formant l'extrémité du vecteur
	 * @param longueurTete longueur du segment
	 */
	public void setLongueurTete(double longueurTete) {
		this.longueurTete = longueurTete;
	}

	public void setAngleTete(double angleTete) {
		this.angleTete = angleTete;
	}

	


}
