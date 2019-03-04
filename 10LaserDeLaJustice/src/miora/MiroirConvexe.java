package miora;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;

import interfaces.Dessinable;

public class MiroirConvexe implements Dessinable {
	private double x=0, y=0, rayon=0;
	private Arc2D.Double miroir ;
	private AffineTransform matLocale;
	
	
	public MiroirConvexe(double x, double y, double rayon) {
		this.x = x;
		this.y = y;
		this.rayon = rayon;
	}


	public void dessiner(Graphics2D g2d, AffineTransform mat, double hauteur, double largeur) {
		matLocale = new AffineTransform(mat);
		miroir = new Arc2D.Double(x, y, rayon, rayon, 180, -180, Arc2D.OPEN);
		g2d.draw(matLocale.createTransformedShape(miroir));
	}


}
