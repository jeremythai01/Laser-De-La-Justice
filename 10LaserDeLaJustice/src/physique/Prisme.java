package physique;

import java.awt.Color;
import java . awt.Polygon ; 
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
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
	
	
	
	
	private int angle; 
	private int DEFAULT_ANGLE = 0;
	
	private Laser laser;
	private Path2D triangle;


	private Vecteur p1;
	private Vecteur p2;
	private Vecteur p3;
	
	
	private Polygon triangles;
	
	
	public Prisme(Vecteur position) {
		p1 = position;
		p2 = new Vecteur(position.getX()+2, position.getY());
		p3 = new Vecteur ((p2.getX()+p1.getX())/2,position.getY()+1);

	}
	

	@Override
	public void dessiner(Graphics2D g, AffineTransform mat, double hauteur, double largeur) {
		AffineTransform matLocal = new AffineTransform(mat);
		//triangles = new Polygon(p, ypoints, npoints);
		g.setColor(Color.RED); 
		
		 int [] pointsX =  {(int)p1.getX(),(int)p2.getX(),(int)p3.getX()};
		 int [] pointsY =  {(int)p1.getY(),(int)p2.getY(),(int)p3.getY()};
		// Demi cercle plein
		 triangles = new Polygon(pointsX, pointsY, 3);
		
		 matLocal.rotate(Math.toRadians(DEFAULT_ANGLE), p1.getX(), p1.getY());
		g.draw(matLocal.createTransformedShape(triangles));
		

	}
	
	
	public Area getAirPrisme() {
		 Rectangle2D recFantome = new Rectangle2D.Double(p1.getX(), p3.getY()-p2.getY(), p3.getX(), p2.getY());
		Area prismeLocal = new Area(recFantome);
		
		//System.out.println("lair du prisme:" + prismeLocal.isEmpty());
		return prismeLocal;
	}
	
	
	
	public Color getCouleurLaser(Laser laser) {
		return Color.black; //methode pour avoir la couleur du laser nest pas encore implementer
	}




	public int getAngle() {
		return angle;
	}





	public void setAngle(int angle) {
		this.angle = angle;
	}

	
	
	
	public Vecteur getP1() {
		return p1;
	}


	public void setP1(Vecteur p1) {
		this.p1 = p1;
	}
	
	
	
	
	
	
	
	
	
}
