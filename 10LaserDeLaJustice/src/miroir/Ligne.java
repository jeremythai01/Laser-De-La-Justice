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

	public Ligne(double x1, double y1, double x2, double y2) {
		super(x1, y1, x2, y2);
	}

	public Ligne(Point2D.Double debut, Point2D.Double fin) {
		super(debut, fin);
		this.debut = debut;
		this.fin = fin;
	}
	
	public Area getAireLigne() {
		Rectangle2D.Double fantome = new Rectangle2D.Double(debut.getX(), debut.getY(),0.3, 0.01);
		return new Area (fantome);
	}
	public Vecteur getVecDir () {
		return new Vecteur (x2-x1,y2-y1);
	}
	

}
