package miroir;

import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import geometrie.Vecteur;

public class Ligne extends Line2D.Double{


	private Area aireLigne;
	private Point2D.Double debut, fin;
	private double angle;
	
	public Ligne(Point2D.Double debut, Point2D.Double fin, double angle) {
		super(debut, fin);
		this.debut = debut;
		this.fin = fin;
		this.angle = angle;
	}
	
	public Area getAireLigne() {
		AffineTransform aff = new AffineTransform();
		aff.rotate(Math.toRadians(-angle),x1,x2);
		Rectangle2D.Double fantome = new Rectangle2D.Double(debut.getX(), debut.getY(),0.08, 0.01);
		return new Area (fantome);
	}
	public Vecteur getVecDir () {
		return new Vecteur (x2-x1,y2-y1);
	}
	

}
