package physique;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

import geometrie.Vecteur;
import interfaces.Dessinable;

/**
 * Classe Laser: repr�sentation sommaire d'un laser � l'aide d'un simple trace.
 * Un laser m�morise sa positionHaut, sa longueur et son angle
 * 
 * @author Arnaud Lefebvre
 * @author Jeremy Thai
 * @author Miora R. Rakoto
 *
 */
public class Laser implements Dessinable {
	private final double LONGUEUR = 1.5;
	private Vecteur positionHaut, vitesse;
	private double angleTir;
	private Path2D.Double trace;
	private Color couleurLaser = null;
	private boolean isCouleurPerso = false;
	private Vecteur positionBas ;
	private double vitesseConstante;

	private Random rand = new Random();

	/**
	 * Constructeur du laser dont la positionHaut, la vitesse ainsi que l'angle de tir
	 * sont specifies
	 * 
	 * @param positionHaut, la positionHaut de depart du laser
	 * @param angleTir, l'angle selon lequel le laser est tire
	 * @param vitesse, la vitesse du laser
	 */
	// auteur Arnaud Lefebvre
	public Laser(Vecteur position, double angleTir, Vecteur vitesse) {

		this.positionHaut = position;
		this.angleTir = angleTir;
		this.vitesse = vitesse;
		updaterAngleVitesse(angleTir);
		isCouleurPerso = false;
	}

	// Par Miora
	/**
	 * Constructeur du laser dont la positionHaut, la couleur personnalis� du laser, la
	 * vitesse ainsi que l'angle de tir sont specifies
	 * 
	 * @param positionHaut: la positionHaut de depart du laser
	 * @param angleTir: l'angle selon lequel le laser est tire
	 * @param vitesse: la vitesse du laser
	 */
	public Laser(Vecteur position, double angleTir, Vecteur vitesse, Color couleurLaser) {
		this.positionHaut = position;
		this.angleTir = angleTir;
		this.vitesse = vitesse;
		this.couleurLaser = couleurLaser;
		updaterAngleVitesse(angleTir);
		isCouleurPerso = true;
	}
	
	

	/**
	 * Permet de dessiner le laser selon le contexte graphique en parametre.
	 * 
	 * @param g2d     contexte graphique
	 * @param mat     matrice de transformation monde-vers-composant
	 * @param hauteur hauteur du monde reelle
	 * @param largeur largeur du monde reelle
	 * 
	 */
	// auteur Arnaud Lefebvre
	public void dessiner(Graphics2D g2d, AffineTransform mat, double hauteur, double largeur) {

		trace = new Path2D.Double();
		AffineTransform matLocal = new AffineTransform(mat);


		trace.moveTo(positionHaut.getX(), positionHaut.getY());

		trace.lineTo(positionHaut.getX() - (LONGUEUR * Math.cos(Math.toRadians(angleTir))),
				positionHaut.getY() + (LONGUEUR * Math.sin(Math.toRadians(angleTir))));

		positionBas = new Vecteur ( positionHaut.getX() - (LONGUEUR * Math.cos(Math.toRadians(angleTir))),
				positionHaut.getY() + (LONGUEUR * Math.sin(Math.toRadians(angleTir))));
		trace.closePath();
		changerCouleurPerso(g2d);

		g2d.draw(matLocal.createTransformedShape(((trace))));
	}

	// Par Miora
	/**
	 * Cette methode change la couleur du laser selon si c'est une couleure
	 * personnalisee ou non. Si oui, la couleur personnalisee sera applique au
	 * laser. Si non, la couleur du laser sera au hasard
	 * 
	 * @param g2d : le contexte graphique
	 */
	private void changerCouleurPerso(Graphics2D g2d) {
		if (!isCouleurPerso) {
			randomColor(g2d);
		} else {
			g2d.setColor(couleurLaser);
		}
	}

	// Miora 
	/**
	 * Retourne la couleur du laser 
	 * @return couleur du laser
	 */
	public Color getCouleurLaser() {
		return couleurLaser;
	}
	// Miora 
		/**
		 * Modifie la couleur du laser par celle passee en parametre
		 * @param couleur du laser en parametre
		 */
	public void setCouleurLaser(Color couleurLaser) {
		this.couleurLaser = couleurLaser;
	}

	/**
	 * Methode qui permet de donner une couleur aleatoire au laser
	 * 
	 * @param g2d, le contexte graphique
	 */
	// auteur Jeremy Thai
	private void randomColor(Graphics2D g2d) {
		float i = (float) 0.5;
		float r = (rand.nextFloat() / 2f) + i;
		float g = (rand.nextFloat() / 2f) + i;
		float b = (rand.nextFloat() / 2f) + i;

		Color randomColor = new Color(r, g, b);
		g2d.setColor(randomColor);
	}

	/**
	 * 
	 */
	// auteur Jeremy Thai
	public void move() {

		positionHaut.setY(positionHaut.getY()-vitesse.getY());
		positionHaut.setX(positionHaut.getX() + vitesse.getX());
	}
	
	/**
	 * Retourne l'aire en forme de rectangle du laser
	 * 
	 * @return l'aire en forme de rectangle du laser
	 */
	// auteur Jeremy Thai
	public Area getAire() { // pour detecter lintersection
		AffineTransform matLocal = new AffineTransform();
		matLocal.rotate(Math.toRadians(180-angleTir), positionHaut.getX(), positionHaut.getY());
		Rectangle2D.Double rect = new Rectangle2D.Double(positionHaut.getX(), positionHaut.getY(), LONGUEUR, 0.01);
		return new Area(matLocal.createTransformedShape(((rect))));
		//
	}
	//Par Miora
	/**
	 * Cette methode retourne la g�ometrie Point du haut du laser
	 */
	public Point2D.Double getPointHaut(){
		Point2D.Double pts = new Point2D.Double(positionHaut.getX(), positionHaut.getY());
		return pts;
	}
	

	/**
	 * Retourne la longueur du laser @return, LONGUEUR, constante de longueur du
	 * laser
	 */
	// auteur Arnaud Lefebvre
	public double getLONGUEUR() {
		return LONGUEUR;
	}

	/**
	 * Retourne la positionHaut du laser @return, le vecteur de la positionHaut du laser
	 */
	// auteur Arnaud Lefebvre
	public Vecteur getPositionBas() {
		return new Vecteur(positionHaut.getX() - (LONGUEUR * Math.cos(Math.toRadians(angleTir))),
				positionHaut.getY() + (LONGUEUR * Math.sin(Math.toRadians(angleTir))));
	}

	/**
	 * Retourne la positionHaut du laser @return, le vecteur de la positionHaut du laser
	 */
	// auteur Arnaud Lefebvre
	public Vecteur getPositionHaut() {
		return positionHaut;
	}

	/**
	 * Methode qui permet de modifier la position du bas du laser
	 * @param pos, la nouvelle position du bas du laser
	 */
	public void setPositionBas(Vecteur pos) {
		Vecteur newVec = new Vecteur(pos.getX(), pos.getY());
		this.positionBas = newVec;
	}


	/**
	 * Permet de modifier la positionHaut du laser
	 * 
	 * @param positionHaut, la nouvelle positionHaut desiree en vecteur
	 */
	// auteur Arnaud Lefebvre
	public void setPositionHaut(Vecteur pos) {
		Vecteur newVec = new Vecteur(pos.getX(), pos.getY());
		this.positionHaut = newVec;
	}

	/**
	 * Retourne la vitesse du laser @return, vitesse, le vecteur vitesse du laser
	 */
	// auteur Arnaud Lefebvre
	public Vecteur getVitesse() {
		return vitesse;
	}

	/**
	 * Permet de modifier la vitesse du laser
	 * 
	 * @param vitesse, la nouvelle vitesse desire
	 */
	// auteur Arnaud Lefebvre
	public void setVitesse(Vecteur vitesse) {
		Vecteur newVec = new Vecteur(vitesse.getX(), vitesse.getY());
		this.vitesse = newVec;
	}

	/**
	 * Retourne l'angle du laser @return, angleTir, l'angle du laser
	 */
	// auteur Arnaud Lefebvre
	public double getAngleTir() {
		return angleTir;
	}

	/**
	 * Permet de modifier l'angle de tir du laser
	 * 
	 * @param angleTir, le nouvel angle de tir desire
	 */
	// auteur Arnaud Lefebvre
	public void setAngleTir(double angleTir) {
		this.angleTir = angleTir;
		updaterAngleVitesse(angleTir);
	}

	
	/**
	 * Methode qui permet d'orienter le laser selon son angle Cette methode permet
	 * de s'assurer que le laser avance bien dans le sens de son angle
	 * 
	 * @param angle, l'angle du laser
	 */
	// auteur Arnaud Lefebvre
	public void updaterAngleVitesse(double angle) {
		vitesseConstante=Math.sqrt(this.vitesse.getX()*this.vitesse.getX()+this.vitesse.getY()*this.vitesse.getY());
		double vitesseEnX = vitesseConstante * Math.cos(Math.toRadians(angle));
		double vitesseEnY = vitesseConstante * Math.sin(Math.toRadians(angle));
		Vecteur vec = new Vecteur(vitesseEnX, vitesseEnY);
		setVitesse(vec);
	}
	

}
