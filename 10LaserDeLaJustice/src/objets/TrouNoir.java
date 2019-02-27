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

public class TrouNoir extends Objet implements Dessinable {
	
	private Vecteur position;
	private final int LARGEUR=2;
	private final int distance=2;
	
	private Ellipse2D.Double trou;
	
	public TrouNoir(Vecteur position) {
		this.position=position;
	}
	

	@Override
	public void dessiner(Graphics2D g, AffineTransform mat, double hauteur, double largeur) {
		AffineTransform matLocal = new AffineTransform(mat);
		g.setColor(Color.LIGHT_GRAY);
		trou=new Ellipse2D.Double(position.getX()-distance, position.getY()-distance, LARGEUR+distance*2, LARGEUR+distance*2);
		g.fill(matLocal.createTransformedShape(trou));
		
		trou= new Ellipse2D.Double(position.getX(), position.getY(), LARGEUR, LARGEUR);
		g.setColor(Color.black);
		
		
		
		
		g.fill(matLocal.createTransformedShape(trou));
		
	}
	
	public void collisionLaserTrouNoir(Laser laser, ArrayList<Balle> liste, Area aireTrou) {
		Area aireIntermediaire= new Area(laser.getAireLaser());
		aireIntermediaire.intersect(aireTrou);
	
		if(!aireIntermediaire.isEmpty()) { //si un laser rentre dans le trou noir
			
			
		}
	

	}
	
	public Area getAireTrou() {
		 Ellipse2D aire= new Ellipse2D.Double(position.getX(), position.getY(), LARGEUR, LARGEUR);
		 return new Area(aire);
	}
}