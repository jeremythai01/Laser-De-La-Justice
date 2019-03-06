package objets;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import geometrie.Vecteur;
import interfaces.Dessinable;
import physique.Balle;
import physique.Laser;

/**
 * Classe qui cree un trou noir et qui memorise sa position
 * Un trou noir, lorsqu'un laser rentre en contact avec, fait disparaitre le laser
 * @author Arnaud Lefebvre
 *
 */
public class TrouNoir extends Objet implements Dessinable {
	
	private Vecteur position;
	private final int LARGEUR=2;
	private final int distance=2;
	
	private Ellipse2D.Double trou;
	
	/**
	 * Constructeur qui memorise la position
	 * @param position, la position donnee au trou noir
	 */
	public TrouNoir(Vecteur position) {
		this.position=position;
	}
	

	@Override
	/**
	 * Permet de dessiner le trou selon le contexte graphique en parametre.
	 * @param g2d contexte graphique
	 * @param mat matrice de transformation monde-vers-composant
	 * @param hauteur hauteur du monde reelle
	 * @param largeur largeur du monde reelle
	 * 
	 */
	public void dessiner(Graphics2D g, AffineTransform mat, double hauteur, double largeur) {
		AffineTransform matLocal = new AffineTransform(mat);
		g.setColor(Color.LIGHT_GRAY);
		trou=new Ellipse2D.Double(position.getX()-distance, position.getY()-distance, LARGEUR+distance*2, LARGEUR+distance*2);
		g.fill(matLocal.createTransformedShape(trou));
		
		trou= new Ellipse2D.Double(position.getX(), position.getY(), LARGEUR, LARGEUR);
		g.setColor(Color.black);
		
		
		
		
		g.fill(matLocal.createTransformedShape(trou));
		
	}
	
	/**
	 * Methode qui permet de calculer l'aire du trou noir
	 * @return, l'aire du trou noir sous forme d'area
	 */
	public Area getAireTrou() {
		 Ellipse2D aire= new Ellipse2D.Double(position.getX(), position.getY(), LARGEUR, LARGEUR);
		 return new Area(aire);
	}
}