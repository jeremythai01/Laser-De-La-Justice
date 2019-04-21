package miroir;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import geometrie.Vecteur;
import interfaces.Dessinable;
/**
 * Classe des miroirs concave
 * @author Miora R. Rakoto
 *
 */
public class MiroirConcave extends JPanel implements Dessinable {

	private Vecteur position;
	private double rayon=0;  // x et y sont les coordonne du centre de l'arc
	private Arc2D.Double arc; 
	private double angle;
	double approximation = 1;
	private boolean modeSci;
	private ArrayList <Ligne> listeLigne = new ArrayList <Ligne> () ;

	/**
	 * Constructeur du miroir concave
	 * @param position : la poisition du centre
	 * @param rayon : le rayon
	 * @param angle : l'angle de rotation
	 */
	public MiroirConcave(Vecteur position, double rayon, double angle) {
		this.position = position;
		this.rayon = rayon;
		this.angle = -angle;
		this.modeSci = modeSci;

		//Le miroir est dessiner avec des Line2D
		ArrayList <Point2D.Double> listePoints = new ArrayList <Point2D.Double> () ;
		for(double i=0; i<=180; i+=approximation) {
			Point2D.Double pts = new Point2D.Double (position.getX()+Math.cos(Math.toRadians(i))*rayon,position.getY()- Math.sin(Math.toRadians(i))*rayon );
			listePoints.add(pts);
		}
		for(int j=0;j<= listePoints.size()-2; j++) {
			Ligne ligne = new Ligne (listePoints.get(j), listePoints.get(j+1),angle);
			listeLigne.add(ligne);

		}

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
		//On tourne
		aff.rotate(Math.toRadians(-angle),position.getX(),position.getY());

		//on dessine la courbe 
		g2d.setColor(Color.red);
		for(Ligne ligne : listeLigne) {
			g2d.draw(aff.createTransformedShape(ligne));
		}
	}

	/**
	 * Cette methode retourne la normal du miroir
	 * @param x : la position du point ou on cherche la normal
	 * @return : le vecteur normal
	 */
	public Vecteur getNormal(Vecteur x) {
		return position.soustrait(x) ;
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

	/**
	 * Cette methode retourne la liste de Line2D utilise pour former le miroir
	 */
	public ArrayList<Ligne> getListeLigne() {
		return listeLigne;
	}

	/**
	 * Cette methode retourne l'aire du miroir concave
	 * @return : aire du miroir concave
	 */
	public Area getAireMiroirConcave() {
		AffineTransform matLocale = new AffineTransform();

		//On tourne
		matLocale.rotate(Math.toRadians(-angle),position.getX(),position.getY());

		//On place l'arc au centre
		matLocale.translate(-(rayon*2)/2, -(rayon*2)/2);
		
		return (new Area( new Arc2D.Double(position.getX(), position.getY(), (rayon*2), (rayon*2), 180, -180, Arc2D.OPEN)));
	}



}
