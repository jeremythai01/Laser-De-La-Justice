package miroir;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import geometrie.Vecteur;
import interfaces.Dessinable;

public class Teleporteur implements Dessinable, Serializable {



	private Vecteur positionPremier,positionDeuxieme;
	private double longueur = 4, epaisseur = 0.1;
	private Rectangle2D.Double premier, deuxieme;
	private Area airePremier, aireDeuxieme;

	
	
	public Teleporteur (Vecteur positionPremier) {
		this.positionPremier = positionPremier;
		positionDeuxieme = new Vecteur (positionPremier.getX() + 2*longueur, positionPremier.getY());
	}

	public void dessiner(Graphics2D g2d, AffineTransform mat, double hauteur, double largeur) {
		AffineTransform matLocal = new AffineTransform (mat);
		premier = new Rectangle2D.Double(positionPremier.getX()-longueur/2, positionPremier.getY(), longueur, epaisseur);
		g2d.setColor(Color.black);
		g2d.fill(matLocal.createTransformedShape(premier));
		deuxieme = new Rectangle2D.Double(positionDeuxieme.getX()-longueur/2, positionDeuxieme.getY(), longueur, epaisseur);
		g2d.fill(matLocal.createTransformedShape(deuxieme));

	}
	
	public Area getAireTeleporteur1() {
		return new Area (premier);
	}
	
	
	public Area getAireTeleporteur2() {
		return new Area (deuxieme);
	}
	
	public Vecteur getPositionPremier() {
		return positionPremier;
	}


	public void setPositionPremier(Vecteur positionPremier) {
		this.positionPremier = positionPremier;
	}


	public Vecteur getPositionDeuxieme() {
		return positionDeuxieme;
	}


	public void setPositionDeuxieme(Vecteur positionDeuxieme) {
		this.positionDeuxieme = positionDeuxieme;
	}


	public double getLongueur() {
		return longueur;
	}


	public void setLongueur(double longueur) {
		this.longueur = longueur;
	}


}
