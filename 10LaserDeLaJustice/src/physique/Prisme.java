package physique;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import geometrie.Vecteur;
import interfaces.Dessinable;

public class Prisme extends JPanel implements Dessinable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Creer le panel.
		@author Arezki
	 */
	
	
	
	private Vecteur position;
	private int angle; 
	private Laser laser;
	private Path2D triangle;
	
	
	
	
	public Prisme(Vecteur position) {
		this.position = position;
		this.angle = 90;
	}
	

	@Override
	public void dessiner(Graphics2D g, AffineTransform mat, double hauteur, double largeur) {
		AffineTransform matLocal = new AffineTransform(mat);
		/*
		triangle.moveTo(position.getX(), position.getY());
		triangle.lineTo(position.getX()+2, position.getY());
		triangle.moveTo((position.getX()+2)/2, position.getY()+2);
		triangle.closePath();
		// Demi cercle plein
		g.draw(matLocal.createTransformedShape(triangle));
*/

	}

	
	

	public Vecteur getPosition() {
		return position;
	}





	public void setPosition(Vecteur position) {
		this.position = position;
	}





	public int getAngle() {
		return angle;
	}





	public void setAngle(int angle) {
		this.angle = angle;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
}
