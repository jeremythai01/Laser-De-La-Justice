package physique;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

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
	private double LONGUEUR= 1.5;
	private Vecteur position, vitesse;
	private double angleTir;
	private Vecteur accel;
	Path2D.Double trace;
	private double ligneFinY;
	public void setLigneFinY(double ligneFinY) {
		this.ligneFinY = ligneFinY;
	}
	private double ligneDebutX;

	Random rand = new Random();
	
	public Laser(Vecteur position, double angleTir, Vecteur vitesse) {
		
		this.position=position;
		this.angleTir=angleTir;
		this.vitesse= vitesse;
		accel = new Vecteur(0,0);
		ligneFinY = position.getY();
		ligneDebutX=position.getX();
		//updaterAngleVitesse(angleTir);
	}


	public void dessiner(Graphics2D g2d, AffineTransform mat, double hauteur, double largeur) {
		trace = new Path2D.Double();
		AffineTransform matLocal = new AffineTransform(mat);
		trace.moveTo(ligneDebutX, ligneFinY);
		trace.lineTo(ligneDebutX+(LONGUEUR*Math.cos(Math.toRadians(angleTir))), ligneFinY-(LONGUEUR*Math.sin(Math.toRadians(angleTir))));
		trace.closePath();
		randomColor(g2d);
		g2d.draw(matLocal.createTransformedShape(((trace))));

	}

	private void randomColor(Graphics2D g2d) {
		float i = (float) 0.5;
		float r = (rand.nextFloat()/2f) + i;
		float g = (rand.nextFloat()/2f) + i;
		float b = (rand.nextFloat()/2f) + i;

		Color randomColor = new Color(r,g,b);
		g2d.setColor(randomColor);
	}


	public double getLigneFinY() {
		return ligneFinY;
	}


	public void move() { 
		ligneFinY -= vitesse.getY();
		ligneDebutX+=vitesse.getX();
		position = new Vecteur (ligneDebutX, ligneFinY);
		System.out.println(position.toString());
		//System.out.println("position de laser quand il bouge" + ligneDebutX +" "+ ligneFinY) ;
	}
	
	public double getLigneDebutX() {
		return ligneDebutX;
	}


	public void setLigneDebutX(double ligneDebutX) {
		this.ligneDebutX = ligneDebutX;
	}


/*
	public Area getAireLaser() {
		Path2D.Double trace = new Path2D.Double();
		trace.moveTo(position.getX(), position.getY());
		trace.lineTo(position.getX()+(LONGUEUR*Math.cos(Math.toRadians(angleTir))), position.getY()+(LONGUEUR*Math.sin(Math.toRadians(angleTir))));
		trace.closePath();
		return new Area(trace);
	}
*/
	public void unPasRK4(double deltaT, double tempsEcoule) {
		MoteurPhysique.unPasRK4(deltaT, tempsEcoule, position, vitesse, accel);
		System.out.println("Nouvelle vitesse: " + vitesse.toString() + "  Nouvelle position: " + position.toString());
	}

	/*
	public Rectangle2D getLine(){ // pour detecter lintersection
        return new Rectangle2D.Double(position.getX(), ligneFinY,0.1,position.getY());
    }
	 */


	public Area getLaserAire(){ // pour detecter lintersection
		return new Area(new Rectangle2D.Double(position.getX(), ligneFinY,0.5,position.getY()));
	}

	public double getLONGUEUR() {
		return LONGUEUR;
	}

	public void setLONGUEUR(double lONGUEUR) {
		LONGUEUR = lONGUEUR;
	}

	public Vecteur getPosition() {
		return new Vecteur ();
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
		updaterAngleVitesse(angleTir);
	}
/*
	public Path2D getTrace() {
		return trace;
	}

	public void setTrace(Path2D trace) {
		this.trace = trace;
	}

*/
	public Vecteur getAccel() {
		return accel;
	}

	public void setAccel(Vecteur accel) {
		this.accel = accel;
	}
	public void updaterAngleVitesse(double angle) {
		double vitesseEnX=0.5*Math.cos(Math.toRadians(angle));
		double vitesseEnY=0.5*Math.sin(Math.toRadians(angle));
		Vecteur vec = new Vecteur(vitesseEnX,vitesseEnY);
		setVitesse(new Vecteur(vitesseEnX,vitesseEnY));
	//	System.out.println("modification vitesse"+ vec );
	}


}
