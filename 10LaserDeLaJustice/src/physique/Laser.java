package physique;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import geometrie.Vecteur;
import interfaces.Dessinable;

/**
 * Classe Laser: représentation sommaire d'un laser à l'aide d'un simple trace.
 * Un laser mémorise sa position, sa longueur et son angle
 * @author Arnaud Lefebvre
 * @author Jeremy Thai
 *
 */
public class Laser implements Dessinable{
	private double LONGUEUR=2;
	private Vecteur position, vitesse;
	private double angleTir;
	private Path2D trace;
	private Vecteur accel;
	private Area aireLaser;
	
	private double ligneFinY;
	private double ligneDebutY;
	
	public Laser(Vecteur position, double angleTir, Vecteur vitesse) {
		trace=new Path2D.Double();
		this.position=position;
		this.angleTir=angleTir;
		this.vitesse= vitesse;
		accel = new Vecteur(0,0);
		ligneFinY = position.getY();
		ligneDebutY = position.getY();
	}


	public void dessiner(Graphics2D g, AffineTransform mat, double hauteur, double largeur) {
		AffineTransform matLocal = new AffineTransform(mat);
		trace.moveTo(position.getX(), ligneDebutY);
		trace.lineTo(position.getX()+(LONGUEUR*Math.cos(Math.toRadians(angleTir))), ligneFinY+(LONGUEUR*Math.sin(Math.toRadians(angleTir))));
		trace.closePath();
		g.draw(matLocal.createTransformedShape(((trace))));
		
		aireLaser = new Area(trace);
		
	}
	
	public double getLigneFinY() {
		return ligneFinY;
	}


	public void move() { 
		ligneFinY -= vitesse.getY();
	}
	
	
	
	public Area getAireLaser() {
		trace.moveTo(position.getX(), ligneDebutY);
		trace.lineTo(position.getX()+(LONGUEUR*Math.cos(Math.toRadians(angleTir))), ligneFinY+(LONGUEUR*Math.sin(Math.toRadians(angleTir))));
		trace.closePath();
		return new Area(trace);
	}

	public void unPasRK4(double deltaT, double tempsEcoule) {
		MoteurPhysique.unPasRK4(deltaT, tempsEcoule, position, vitesse, accel);
		System.out.println("Nouvelle vitesse: " + vitesse.toString() + "  Nouvelle position: " + position.toString());
	}
	

	public Rectangle2D getLine(){ // pour detecter lintersection
        return new Rectangle2D.Double(position.getX(), ligneFinY,0.1,position.getY());
    }
	
	

	public double getLONGUEUR() {
		return LONGUEUR;
	}

	public void setLONGUEUR(double lONGUEUR) {
		LONGUEUR = lONGUEUR;
	}

	public Vecteur getPosition() {
		return position;
	}

	public void setPosition(Vecteur position) {
		this.position = position;
	}

	public Vecteur getVitesse() {
		return vitesse;
	}

	public void setVitesse(Vecteur vitesse) {
		this.vitesse = vitesse;
	}

	public double getAngleTir() {
		return angleTir;
	}

	public void setAngleTir(double angleTir) {
		this.angleTir = angleTir;
	}

	public Path2D getTrace() {
		return trace;
	}

	public void setTrace(Path2D trace) {
		this.trace = trace;
	}

	public Vecteur getAccel() {
		return accel;
	}

	public void setAccel(Vecteur accel) {
		this.accel = accel;
	}
	
	
}
