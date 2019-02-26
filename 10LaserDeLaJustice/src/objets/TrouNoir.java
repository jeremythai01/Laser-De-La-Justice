package objets;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import geometrie.Vecteur;
import interfaces.Dessinable;
import physique.Balle;
import physique.Laser;

public class TrouNoir extends Objet implements Dessinable {
	
	private Vecteur position;
	private final int LARGEUR=2;
	private Rectangle2D.Double trou;
	
	public TrouNoir(Vecteur position) {
		this.position=position;
	}
	

	@Override
	public void dessiner(Graphics2D g, AffineTransform mat, double hauteur, double largeur) {
		AffineTransform matLocal = new AffineTransform(mat);
		trou= new Rectangle2D.Double(position.getX(), position.getY(), LARGEUR, LARGEUR);
		g.setColor(Color.black);
		g.fill(matLocal.createTransformedShape(trou));
		
	}
	
	public void collisionLaserTrouNoir(Laser laser, ArrayList<Balle> liste, Area aireTrou) {
		Area aireIntermediaire= new Area(laser.getAireLaser());
		aireIntermediaire.intersect(aireTrou);
	
		if(!aireIntermediaire.isEmpty()) { //si un laser rentre dans le trou noir
			
			
		}
	

	}
}