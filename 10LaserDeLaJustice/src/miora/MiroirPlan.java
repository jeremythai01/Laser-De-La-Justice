package miora;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import geometrie.Vecteur;
import interfaces.Dessinable;

public class MiroirPlan implements Dessinable {
	
	private double x=0, y=0, angle=0;
	private Rectangle2D.Double miroir;
	private AffineTransform matLocale;
	private double longueur = 2; 
	private Shape miroirTransfo;
	private Vecteur normal;
	
	public MiroirPlan(double x, double y, double angle) {
		super();
		this.x = x;
		this.y = y;
		this.angle = angle;
	}

	public void dessiner(Graphics2D g2d, AffineTransform mat, double hauteur, double largeur) {
		matLocale = new AffineTransform(mat);
		matLocale.rotate(-angle,x,y);
		miroir = new Rectangle2D.Double(x,y, longueur, 0.05);
		miroirTransfo = matLocale.createTransformedShape(miroir);
		//g2d.fill(miroirTransfo);
	}
	public Area getAireMiroir() {
		return new Area (miroirTransfo);
	}
	
	public Vecteur calculNormal() {
		Vecteur vecMiroir = new Vecteur (Math.cos(angle), Math.sin(angle));
		normal = new Vecteur(-vecMiroir.getY(), vecMiroir.getX());
		return normal;
	}
	

}
