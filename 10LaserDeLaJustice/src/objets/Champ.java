package objets;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

import geometrie.Vecteur;
import interfaces.Dessinable;

public class Champ implements Dessinable {
	
	private Vecteur position;
	private double charge;
	private double k=9*(10^9);
	private Ellipse2D.Double champ;
	private double LARGEUR=10;
	
	public Champ(Vecteur position, double charge) {
		this.position=position;
		this.charge=charge;
		
	}

	@Override
	public void dessiner(Graphics2D g2d, AffineTransform mat, double hauteur, double largeur) {
		AffineTransform matLocal = new AffineTransform(mat);
		g2d.setColor(Color.LIGHT_GRAY);
		champ = new Ellipse2D.Double(position.getX(), position.getY(), LARGEUR, LARGEUR);
		g2d.draw(matLocal.createTransformedShape(champ));;
		
	}

	public Vecteur forceElectrique(Vecteur positionSubit, Vecteur positionApplique, double chargeSubit) {
		return (positionSubit.soustrait(positionApplique).multiplie(1/(Math.pow((positionSubit.soustrait(positionApplique)).module(),3)))).multiplie(k*chargeSubit*charge);
	}
	
	public Area getAireChamp() {
		Ellipse2D aire= new Ellipse2D.Double(position.getX(), position.getY(), LARGEUR, LARGEUR);
		 return new Area(aire);
	}

	public Vecteur getPosition() {
		return position;
	}

	public void setPosition(Vecteur position) {
		this.position = position;
	}

	public double getCharge() {
		return charge;
	}

	public void setCharge(double charge) {
		this.charge = charge;
	}

	public double getK() {
		return k;
	}

	

	public Ellipse2D.Double getChamp() {
		return champ;
	}

	public void setChamp(Ellipse2D.Double champ) {
		this.champ = champ;
	}

	public double getLARGEUR() {
		return LARGEUR;
	}
	
}
