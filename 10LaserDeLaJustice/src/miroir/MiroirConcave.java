package miroir;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Path2D;
import java.awt.geom.QuadCurve2D;

import javax.swing.JPanel;

import geometrie.Vecteur;
import interfaces.Dessinable;

public class MiroirConcave extends JPanel implements Dessinable {

	private Vecteur positionIni;
	private Vecteur P;
	private Vecteur Q;

	
	private double angleDebut;
	private double grosseurMiroire;

	private Arc2D miroirConcave;

	public MiroirConcave(double x , double y) {
		positionIni = new Vecteur(x, y);
		angleDebut = 150;
		grosseurMiroire = 150;

	}

	@Override
	public void dessiner(Graphics2D g, AffineTransform mat, double hauteur, double largeur) {
		AffineTransform matLocal = new AffineTransform(mat);

		miroirConcave = new Arc2D.Double(positionIni.getX(), positionIni.getY(), 5, 5, getAngleDebut(), getGrosseurMiroire(), Arc2D.CHORD);
		// matLocal.rotate(90);
		// En rouge
		g.setColor(Color.red);
		// Demi cercle plein
		g.draw(matLocal.createTransformedShape(miroirConcave));

		// couleur noire

	}

	
	
	public double getAngleDebut() {
		return angleDebut;
	}

	public void setAngleDebut(double angleDebut) {
		this.angleDebut = angleDebut;
	}

	public double getGrosseurMiroire() {
		return grosseurMiroire;
	}

	public void setGrosseurMiroire(double grosseurMiroire) {
		this.grosseurMiroire = grosseurMiroire;
	}

}
