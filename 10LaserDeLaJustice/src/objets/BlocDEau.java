package objets;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import geometrie.Vecteur;
import interfaces.Dessinable;
//import sim.math.SVector3d;

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
	
	public Vecteur refraction(Vecteur v, Vecteur N, double n1, double n2) {
		Vecteur vecteur= new Vecteur();
		double n= n1/n2;
		Vecteur E=new Vecteur();
		E=v.multiplie(-1);
		vecteur = v.multiplie(n).additionne(N.multiplie(((E.prodScalaire(N)*n)-Math.sqrt(1-((n*n)*(1-(E.prodScalaire(N)*(E.prodScalaire(N)))))))));
		return vecteur;
	}
	
	
	
	

}
