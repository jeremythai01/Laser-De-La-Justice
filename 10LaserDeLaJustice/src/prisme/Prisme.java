package prisme;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import geometrie.Vecteur;
import interfaces.Dessinable;
import physique.Laser;

public class Prisme extends JPanel implements Dessinable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Creer le panel.
	 * 
	 * @author Arezki
	 */

	private int angle;
	private int DEFAULT_ANGLE = 0;

	private Laser laser;
	private Path2D triangle;

	private Vecteur p1;
	private Vecteur p2;
	private Vecteur p3;

	private Vecteur centre;

	private double indiceRefraction = 2.0;
	
	private boolean science = false;



	private Polygon triangles;
	Line2D ligne13;
	Line2D ligne12;
	Line2D ligne23;

	public Prisme(Vecteur position) {
		p1 = position;
		p2 = new Vecteur(position.getX() + 6, position.getY());
		p3 = new Vecteur((p2.getX() + p1.getX()) / 2, position.getY() + 5);

	}

	@Override
	public void dessiner(Graphics2D g, AffineTransform mat, double hauteur, double largeur) {
		AffineTransform matLocal = new AffineTransform(mat);
		// triangles = new Polygon(p, ypoints, npoints);
		g.setColor(Color.RED);

		int[] pointsX = { (int) p1.getX(), (int) p2.getX(), (int) p3.getX() };
		int[] pointsY = { (int) p1.getY(), (int) p2.getY(), (int) p3.getY() };
		// Demi cercle plein
		triangles = new Polygon(pointsX, pointsY, 3);
		float xCentre = (float)((p2.getX() + p1.getY()) / 2.0);
		float yCentre = (float)(p3.getY() / 2.0);

		Point p1 = new Point((int) getP1().getX(), (int) getP1().getY());
		Point p2 = new Point((int) getP2().getX(), (int) getP2().getY());
		Point p3 = new Point((int) getP3().getX(), (int) getP3().getY());

		ligne13 = new Line2D.Double(p3, p1);
		ligne23 = new Line2D.Double(p2, p3);
		ligne12 = new Line2D.Double(p1, p2);
		if(science)
		g.drawString("n: "+indiceRefraction,(float)(10*p1.getX()), (float)(10*p1.getY()));
		
		matLocal.rotate(Math.toRadians(0), p1.getX(), p1.getY());
		// g.draw(matLocal.createTransformedShape(triangles));
		g.setColor(Color.yellow);
		g.draw(matLocal.createTransformedShape(ligne13));
		g.draw(matLocal.createTransformedShape(ligne23));
		g.draw(matLocal.createTransformedShape(ligne12));
		// g.fill(new Ellipse2D.Double(xCentre, yCentre, 0.5, 0.5));
		// g.draw(matLocal.createTransformedShape(new Ellipse2D.Double(xCentre, yCentre,
		// 0.5, 0.5)));

	}

	public Vecteur getP2() {
		return p2;
	}

	public void setP2(Vecteur p2) {
		this.p2 = p2;
	}

	public Vecteur getP3() {
		return p3;
	}

	public void setP3(Vecteur p3) {
		this.p3 = p3;
	}

	public Area getAirPrisme() {
		Rectangle2D recFantome = new Rectangle2D.Double(p1.getX(), p3.getY() - p2.getY(), p3.getX(), p2.getY());
		Area prismeLocal = new Area(recFantome);

		// System.out.println("lair du prisme:" + prismeLocal.isEmpty());
		return new Area(triangles);
	}

	
	public Vecteur getNormalPrisme() {
		
		return (p2.soustrait(p3)).cross((p2.soustrait(p1)));
		
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

	public Line2D getLigne13() {
		return ligne13;
	}

	public void setLigne13(Line2D ligne13) {
		this.ligne13 = ligne13;
	}

	public Line2D getLigne12() {
		return ligne12;
	}

	public void setLigne12(Line2D ligne12) {
		this.ligne12 = ligne12;
	}

	public Line2D getLigne23() {
		return ligne23;
	}

	public void setLigne23(Line2D ligne23) {
		this.ligne23 = ligne23;
	}

	public void setPosition(Vecteur pos) {
		p1 = pos;
		p2 = new Vecteur(pos.getX() + 6, pos.getY());
		p3 = new Vecteur((p2.getX() + p1.getX()) / 2, pos.getY() + 5);
	}

	
	public double getIndiceRefraction() {
		return indiceRefraction;
	}

	public void setIndiceRefraction(double indiceRefraction) {
		this.indiceRefraction = indiceRefraction;
	}
	
	public boolean isScience() {
		return science;
	}

	public void setScience(boolean science) {
		this.science = science;
	}
	
	
}
