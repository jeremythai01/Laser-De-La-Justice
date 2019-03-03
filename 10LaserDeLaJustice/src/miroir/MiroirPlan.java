package miroir;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

import geometrie.Vecteur;
import interfaces.Dessinable;
import physique.Laser;

/**
 * Cette classe permet 
 * @author Miora R. Rakoto
 *
 */
public class MiroirPlan implements Dessinable {
	private double hauteur = 0;
	private Rectangle2D.Double miroirPlan;
	
	private double x=0, y=0;
	private double epaisseur = 0.05;
	private Area aireRect = null; 
	AffineTransform matLocal;
	/*
	 * Constructeur de la classe
	 * @param hauteur : hauteur du miroire
	 */
	public MiroirPlan(double hauteur, double x, double y) {
		this.hauteur = hauteur;
		this.x = x;
		this.y = y;
	}
	public void dessiner(Graphics2D g, AffineTransform mat, double hauteur, double largeur) {
		matLocal = new AffineTransform(mat);
		miroirPlan = new Rectangle2D.Double(x,y, epaisseur, y+ this.hauteur);
		g.setColor(Color.red);
		aireRect = new Area (miroirPlan);
		g.fill(matLocal.createTransformedShape(miroirPlan));
	}
	public Area aireMiroir() {
		return aireRect;
	}

	/*public Vecteur calculNormal(Laser laser, MiroirPlan miroire) {
		return new Vecteur(laser.getPosition().getX() - miroire.getPositionIni().getX(),
				laser.getPosition().getY() - miroire.getPositionIni().getY());

	}
*/
	
	public Rectangle2D.Double getMiroirPlan() {
		return miroirPlan;
	}

}
