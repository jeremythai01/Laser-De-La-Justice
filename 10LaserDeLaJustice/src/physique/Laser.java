package physique;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;

import geometrie.Vecteur;
import interfaces.Dessinable;

/**
 * Classe Laser: représentation sommaire d'un laser à l'aide d'un simple trace.
 * Un laser mémorise sa position, sa longueur et son angle
 * @author Arnaud Lefebvre
 *
 */
public class Laser implements Dessinable{
	private double LONGUEUR=2;
	private Vecteur position, vitesse;
	private double angleTir;
	private Line2D trace;
	private Vecteur accel;
	
	public Laser(Vecteur position, double angleTir, Vecteur vitesse) {
		this.position=position;
		this.angleTir=angleTir;
		this.vitesse=vitesse;
		accel = new Vecteur(0,0);
	}

	public void dessiner(Graphics2D g, AffineTransform mat) {
		AffineTransform matLocal = new AffineTransform(mat);
		trace=new Line2D.Double(position.getX(), position.getY(),position.getX()+(LONGUEUR*Math.cos(Math.toRadians(angleTir))), position.getY()+(LONGUEUR*Math.sin(Math.toRadians(angleTir))));
		g.draw(matLocal.createTransformedShape(((trace))));
		
	}
	public void unPasRK4(double deltaT, double tempsEcoule) {
		MoteurPhysique.unPasRK4(deltaT, tempsEcoule, position, vitesse, accel);
		System.out.println("Nouvelle vitesse: " + vitesse.toString() + "  Nouvelle position: " + position.toString());
	}
	

	/*@Override
	public void dessiner(Graphics2D g, AffineTransform mat) {
		// TODO Auto-generated method stub
		
	}*/

}
