package miroir;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Path2D;
import java.awt.geom.QuadCurve2D;

import javax.swing.JPanel;

import geometrie.Vecteur;
import interfaces.Dessinable;

public class MiroirConcave extends JPanel implements Dessinable{

	
	private Vecteur positionIni;
	private Vecteur P;
	private Vecteur Q;
	
	private double rayonCorbure;
	
	private QuadCurve2D miroirConcave;
	
	public MiroirConcave() {
		positionIni = new Vecteur(0,0);
		rayonCorbure = 10;
		
	}

	@Override
	public void dessiner(Graphics2D g, AffineTransform mat, double hauteur, double largeur) {
		AffineTransform matLocal = new AffineTransform(mat);
		miroirConcave = new QuadCurve2D.Double(positionIni.getX(), positionIni.getY(), 5, 5, positionIni.getX()+12, positionIni.getY());
		//matLocal.rotate(90);
		g.draw(matLocal.createTransformedShape(miroirConcave));
	}

	
	
	
}
