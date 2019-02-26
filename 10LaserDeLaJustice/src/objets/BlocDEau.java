package objets;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import geometrie.Vecteur;
import interfaces.Dessinable;

public class BlocDEau extends Objet implements Dessinable {
	
	private Vecteur position;
	private final int LARGEUR=2;
	private Rectangle2D.Double bloc;
	
	public BlocDEau(Vecteur position) {
		this.position=position;
	}
	

	@Override
	public void dessiner(Graphics2D g, AffineTransform mat, double hauteur, double largeur) {
		AffineTransform matLocal = new AffineTransform(mat);
		bloc= new Rectangle2D.Double(position.getX(), position.getY(), LARGEUR, LARGEUR);
		g.setColor(Color.blue);
		g.fill(matLocal.createTransformedShape(bloc));
		
	}

}
