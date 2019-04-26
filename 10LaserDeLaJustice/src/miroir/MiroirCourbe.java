package miroir;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;

import geometrie.Vecteur;
import interfaces.Dessinable;
import prisme.Prisme;

/**
 * Classe des miroirs convexes
 * @author Miora R. Rakoto
 */
public class MiroirCourbe implements Dessinable, Serializable {

	private static final long serialVersionUID = 1L;
	private Vecteur position;
	private double rayon=0;  // x et y sont les coordonne du centre de l'arc
	private Arc2D.Double arc; 
	private double angle;
	double approximation = 1;
	private boolean modeSci;
	private ArrayList <Ligne> listeLigne = new ArrayList <Ligne> () ;
	Vecteur inter ;
	boolean dessiner =false;
	private Point2D.Double pts;

	/**
	 * Constructeur de la classe miroirConvexe
	 * @param position : la poisition du centre
	 * @param rayon : le rayon
	 * @param angle : l'angle de rotation
	 */
	public MiroirCourbe(Vecteur position, double rayon, double angle) {
		this.position = position;
		this.rayon = rayon;
		this.angle = angle;

		initialiserMiroir();
	}
	/**
	 * Dessiner le miroir convexe
	 * @param g2d : le composant graphique
	 * @param mat : la matrice de transformation
	 * @param hauteur : la hauteur de la scene
	 * @param largeur : largeur de la scene 
	 */
	public void dessiner(Graphics2D g2d, AffineTransform mat, double hauteur, double largeur) {
		AffineTransform aff = new AffineTransform(mat);
		g2d.fill(aff.createTransformedShape(new Ellipse2D.Double(position.getX()-0.5/2, position.getY()-0.5/2, 0.5, 0.5)));

		//on dessine la courbe avec des petites ligne
		g2d.setColor(Color.red);
		for(Ligne ligne : listeLigne) {
			g2d.draw(aff.createTransformedShape(ligne));
		}

		if(dessiner) {
			aff = new AffineTransform(mat);
			g2d.draw(aff.createTransformedShape(new Line2D.Double(inter.getX(),inter.getY(), position.getX(), position.getY())));
			g2d.draw(aff.createTransformedShape(new Ellipse2D.Double(inter.getX()-0.5/2, inter.getY()-0.5/2, 0.5, 0.5)));
		}
	}

	/**
	 * Cette methode permet d'obtenir l'aire du miroir convexe
	 * @return l'aire du miroir convexe
	 */
	public Area getAireMiroirCourbe() {
		AffineTransform matLocale = new AffineTransform();
		matLocale.rotate(Math.toRadians(-angle),position.getX(),position.getY());
		matLocale.translate(-rayon, -rayon);
		//return new Area(new Arc2D.Double(position.getX(), position.getY(), 2*rayon, 2*rayon, -180, 180, Arc2D.OPEN));
		return new Area(new Rectangle2D.Double(position.getX(), position.getY(), rayon*10, rayon*10));
	}

	/**
	 * Cette methode retourne la normal du miroir
	 * @param x : la position du point ou on cherche la normal
	 * @return : le vecteur normal
	 */
	public Vecteur getNormal(Vecteur posInter) {
		dessiner = true;
		this.inter = posInter;
		return (posInter.soustrait(position)) ;
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
		if(listeLigne.size()==0) {
			initialiserMiroir();
		}else {
			listeLigne.removeAll(listeLigne);
			initialiserMiroir();
		}

	}

	/**
	 * Cette methode retourne la liste de Line2D utilise pour former le miroir
	 */
	public ArrayList<Ligne> getListeLigne() {
		return listeLigne;
	}

	/**
	 * Cette methode permet d'initialiser un miroir courbe venant d'une liste de point
	 * @return 
	 */
	private void initialiserMiroir() {
		ArrayList <Point2D.Double> listePoints = new ArrayList <Point2D.Double> () ;
		for(int i=0; i<=180; i+=approximation) {
			pts = new Point2D.Double (position.getX()+Math.cos(Math.toRadians(-i))*rayon  ,  position.getY()- Math.sin(Math.toRadians(-i))*rayon );
			double a = Math.toRadians(-angle);
			double matR [][]={{Math.cos(a), -Math.sin(a)},{Math.sin(a),Math.cos(a)}}; // matrice de rotation
			double ptsD[] = {pts.getX()-position.getX() , pts.getY()-position.getY()}; 
			double d[]=new double[2]; //nouvelle coordonees

			//multiplication matriciel avec matrice de rotation sur les points, pas utilisation aff.rotate
			for(int j=0;j<2;j++){    
				d[j]=0;      
				for(int k=0;k<2;k++)      
				{      
					d[j]+=matR[j][k]*ptsD[k];      
				}
			}
			d[0] = d[0]+position.getX();
			d[1] = d[1]+position.getY();
			pts = new Point2D.Double(d[0], d[1]);
			listePoints.add(pts);
		}
		//Petites lignes qui vont former le demi-cercle
		for(int j=0;j<= listePoints.size()-2; j++) {
			Ligne ligne = new Ligne (listePoints.get(j), listePoints.get(j+1));
			listeLigne.add(ligne);
		}
	}



}
