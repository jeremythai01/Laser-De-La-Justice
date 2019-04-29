package prisme;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.io.Serializable;

import geometrie.Vecteur;
import interfaces.Dessinable;

public class Prisme implements Dessinable, Serializable {
	/**
	 * Cette classe permet de dessiner le prisme et son air
	 * 
	 * @author Arezki
	 */

	private static final long serialVersionUID = 1L;

	private Vecteur p1;
	private Vecteur p2;
	private Vecteur p3;

	public double indiceRefraction = 2.0;

	private boolean science = false;

	private Polygon triangles;
	Line2D ligne13;
	Line2D ligne12;
	Line2D ligne23;

	/**
	 * Constructeur du prisme qui initialise les 3 positions(sommets) du prisme
	 * @param position, le sommet principal
	 */
	public Prisme(Vecteur position, double indiceRefraction) {
		p1 = position;
		this.indiceRefraction = indiceRefraction;
		p2 = new Vecteur(position.getX() + 6, position.getY());
		p3 = new Vecteur((p2.getX() + p1.getX()) / 2, position.getY() + 5);

	}

	@Override
	/**
	 * Cette m�thode permet de dessiner un triangle avec l'indice de r�faction si
	 * l'utilisateur active le mode scientifique
	 */
	public void dessiner(Graphics2D g, AffineTransform mat, double hauteur, double largeur) {
		AffineTransform matLocal = new AffineTransform(mat);
		// triangles = new Polygon(p, ypoints, npoints);
		g.setColor(Color.RED);

		int[] pointsX = { (int) p1.getX(), (int) p2.getX(), (int) p3.getX() };
		int[] pointsY = { (int) p1.getY(), (int) p2.getY(), (int) p3.getY() };
		triangles = new Polygon(pointsX, pointsY, 3);

		Point p1 = new Point((int) getP1().getX(), (int) getP1().getY());
		Point p2 = new Point((int) getP2().getX(), (int) getP2().getY());
		Point p3 = new Point((int) getP3().getX(), (int) getP3().getY());

		ligne13 = new Line2D.Double(p3, p1);
		ligne23 = new Line2D.Double(p2, p3);
		ligne12 = new Line2D.Double(p1, p2);
		if (science)
			g.drawString("n: " + getIndiceRefraction(), (float) (19 * p1.getX()), (float) (20 * p1.getY()));

		matLocal.rotate(Math.toRadians(0), p1.getX(), p1.getY());

		g.setColor(Color.yellow);
		g.draw(matLocal.createTransformedShape(ligne13));
		g.draw(matLocal.createTransformedShape(ligne23));
		g.draw(matLocal.createTransformedShape(ligne12));

	}

	/**
	 * Cette m�thode permet d'avoir le point principal du triangle, car c'est
	 * celui-ci qui contient la position que l'utilisateur va mettre
	 * 
	 * @return p1: Point situ� au coin sup�rieur gauche du triangle. Il sera
	 *         retourn� en Vecteur avec les unit�s r�elles
	 */
	public Vecteur getP1() {
		return p1;
	}

	/**
	 * Cette m�thode retourne en vecteur le 2e point du triangle qui permet de faire
	 * le dessin
	 * 
	 * @return p2: Point situ� sur la m�me orizontale que le point 1 (p1). Il sera
	 *         retourn� en Vecteur avec les unit�s r�elles
	 */
	public Vecteur getP2() {
		return p2;
	}

	/**
	 * Cette m�thode retourne en vecteur le 3e point du triangle qui permet de faire
	 * le dessin
	 * 
	 * @return p3: le point de la point du triangle en vecteur en m�tre(m,m). Il
	 *         sera retourn� en Vecteur avec les unit�s r�elles
	 */

	public Vecteur getP3() {
		return p3;
	}

	/**
	 * Cette m�thode permet d'acquerir l'air du prisme pour faire les collisions
	 * entre objets
	 * 
	 * @return Area: une nouvelle Area de type polygyon qui a la m�me forme que le
	 *         triangle (forme fant�me)
	 */

	public Area getAirPrisme() {

		// System.out.println("lair du prisme:" + prismeLocal.isEmpty());
		return new Area(triangles);
	}

	/**
	 * Cette m�thode permet d'avoir la ligne entre les points p1 et p3
	 * 
	 * @return ligne13: Shape Line2D entre les point p1 et p3
	 */
	public Line2D getLigne13() {
		return ligne13;
	}

	/**
	 * Cette m�thode permet d'avoir la ligne entre les points p1 et p2
	 * 
	 * @return ligne13: Shape Line2D entre les point p1 et p2
	 */

	public Line2D getLigne12() {
		return ligne12;
	}

	/**
	 * Cette m�thode permet d'avoir la ligne entre les points p2 et p3
	 * 
	 * @return ligne13: Shape Line2D entre les point p2 et p3
	 */

	public Line2D getLigne23() {
		return ligne23;
	}

	/**
	 * Permet de modifier la position compl�te du triangle lorsque l'utilisateur va
	 * le drag avec la souris en refaisant tous les calculs pour replacer les points
	 * 
	 * @param pos: Vecteur de la position que l'utilisateur a choisit
	 */
	public void setPosition(Vecteur pos) {
		p1 = pos;
		p2 = new Vecteur(pos.getX() + 6, pos.getY());
		p3 = new Vecteur((p2.getX() + p1.getX()) / 2, pos.getY() + 5);
	}

	/**
	 * Cette m�thode retourne l'indice de r�fraction du prisme
	 * 
	 * @return indiceRefraction: Double qui est l'indice de r�fraction
	 */
	public double getIndiceRefraction() {
		return indiceRefraction;
	}

	/**
	 * Cette m�thode permet de modifier l'indice de r�fraction du prisme
	 * 
	 * @param indiceRefraction: Nouveau indice de r�fraction
	 */
	public void setIndiceRefraction(double indiceRefraction) {
		this.indiceRefraction = indiceRefraction;
	}

	
	/**
	 * Cette m�thode permet de savoir si le mode scientifique est activ�. Si c'est
	 * le cas, il ecrira l'indice de r�fraction du prisme
	 * 
	 * @return science : Boolean pour savoir si le mode est activ�
	 */
	public boolean isScience() {
		return science;
	}

	/**
	 * Cette m�thode permet de modifier la valeur de la variable de science. Permet
	 * de d�sactiver et d'activer le mode scientifique
	 * 
	 * @param science: param�tre qui activera ou d�sactivera le mode scientifique
	 *        (true or false)
	 */
	public void setScience(boolean science) {
		this.science = science;
	}

}
