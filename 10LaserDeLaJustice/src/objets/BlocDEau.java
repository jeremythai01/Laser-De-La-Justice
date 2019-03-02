package objets;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import geometrie.Vecteur;
import interfaces.Dessinable;
//import sim.math.SVector3d;
import miroir.MiroirConcave;
import physique.Laser;

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
		return new Vecteur(vecteur.getX(),vecteur.getY());
	}


	public Vecteur calculNormal(Laser laser, BlocDEau bloc) {

		return new Vecteur(laser.getPosition().getX() - bloc.getPosition().getX(),
				laser.getPosition().getY() - bloc.getPosition().getY());

	}


	public Vecteur getPosition() {
		return position;
	}


	public void setPosition(Vecteur position) {
		this.position = position;
	}


	public Rectangle2D.Double getBloc() {
		return bloc;
	}


	public void setBloc(Rectangle2D.Double bloc) {
		this.bloc = bloc;
	}


	public int getLARGEUR() {
		return LARGEUR;
	}


	public Area getAireBloc() {
		 Ellipse2D aire= new Ellipse2D.Double(position.getX(), position.getY(), LARGEUR, LARGEUR);
		 return new Area(aire);
	}
	
	
	

}
