package miora;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;

import geometrie.Vecteur;
import interfaces.Dessinable;
import objets.BlocDEau;
import physique.Laser;

/**
 * Cette classe permet 
 * @author Miora R. Rakoto
 *
 */
public class MiroirPlan implements Dessinable {
	private double hauteur = 0;
	private Path2D.Double miroirPlan;
	
	private double x1=0, y1=0, x2=0, y2=0;
	private double epaisseur = 5;
	private Area aireMiroir = null; 
	AffineTransform matLocal;
	/*
	 * Constructeur de la classe
	 * @param x1 : position sur l'axe des x de point le plus a gauche du miroir
	 * @param y1 : position sur l'axe des y de point le plus a gauche du miroir
	 * @param x2 : position sur l'axe des x de point le plus a droite du miroir
	 * @param y2 : position sur l'axe des y de point le plus a droite du miroir
	 */
	public MiroirPlan(double x1, double y1, double x2, double y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	public void dessiner(Graphics2D g, AffineTransform mat, double hauteur, double largeur) {
		matLocal = new AffineTransform(mat);
		miroirPlan = new Path2D.Double();
		miroirPlan.moveTo(x1,y1);
		miroirPlan.lineTo(x2, y2);
		miroirPlan.closePath();
		g.setStroke(new BasicStroke((float) epaisseur));
		g.setColor(Color.red);
		g.draw(matLocal.createTransformedShape(miroirPlan));
	}
	public Area getAireMiroir() {
		miroirPlan.moveTo(x1,y1);
		miroirPlan.lineTo(x2, y2);
		miroirPlan.closePath();
		return new Area(miroirPlan);
	}
	
	public Vecteur calculNormal() {
		Vecteur positionMiroir = new Vecteur (x2-x1, y2-y1);
		return new Vecteur(-positionMiroir.getY(), positionMiroir.getX());
	}

}
