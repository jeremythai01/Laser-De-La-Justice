package physique;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
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
	private double LONGUEUR=0.5;
	private Vecteur position, angleTir, vitesse;
	private Path2D.Double trace=null;
	private Vecteur accel;
	
	public Laser(Vecteur position, Vecteur angleTir, Vecteur vitesse) {
		this.position=position;
		this.angleTir=angleTir;
		this.vitesse=vitesse;
		accel = new Vecteur(0,0);
	}

	public void dessiner(Graphics2D g2d) {

		trace.moveTo(position.getX(), position.getY());
		trace.lineTo(position.getX()+(LONGUEUR*Math.cos(angleTir.getX())), position.getY()+(LONGUEUR*Math.sin(angleTir.getY())));
		g2d.draw((trace));
		
	}
	public void unPasRK4(double deltaT, double tempsEcoule) {
		MoteurPhysique.unPasRK4(deltaT, tempsEcoule, position, vitesse, accel);
		System.out.println("Nouvelle vitesse: " + vitesse.toString() + "  Nouvelle position: " + position.toString());
	}
	

	@Override
	public void dessiner(Graphics2D g, AffineTransform mat) {
		// TODO Auto-generated method stub
		
	}

}
