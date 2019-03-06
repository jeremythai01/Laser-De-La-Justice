package physique;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import geometrie.Vecteur;
import interfaces.Dessinable;

/**
 * Classe Laser: représentation sommaire d'un laser à l'aide d'un simple trace.
 * Un laser mémorise sa position, sa longueur et son angle
 * @author Arnaud Lefebvre
 * @author Jeremy Thai
 *
 */
public class Laser implements Dessinable{
	private final double LONGUEUR= 1.5;
	private Vecteur position, vitesse;
	private double angleTir;
	private Vecteur accel;
	Path2D.Double trace;
	private double ligneFinY;
	public void setLigneFinY(double ligneFinY) {
		this.ligneFinY = ligneFinY;
	}
	private double ligneDebutX;

	Random rand = new Random();
	
	/**
	 * Constructeur du laser dont la position, la vitesse ainsi que l'angle de tir sont specifies
	 * @param position, la position de depart du laser
	 * @param angleTir, l'angle selon lequel le laser est tire
	 * @param vitesse, la vitesse du laser
	 */
	//auteur Arnaud Lefebvre
	public Laser(Vecteur position, double angleTir, Vecteur vitesse) {
		
		this.position=position;
		this.angleTir=angleTir;
		this.vitesse= vitesse;
		accel = new Vecteur(0,0);
		ligneFinY = position.getY();
		ligneDebutX=position.getX();
		//updaterAngleVitesse(angleTir);


	}
	/**
	 * Permet de dessiner le laser selon le contexte graphique en parametre.
	 * @param g2d contexte graphique
	 * @param mat matrice de transformation monde-vers-composant
	 * @param hauteur hauteur du monde reelle
	 * @param largeur largeur du monde reelle
	 * 
	 */
	//auteur Arnaud Lefebvre
	public void dessiner(Graphics2D g2d, AffineTransform mat, double hauteur, double largeur) {
		trace = new Path2D.Double();
		AffineTransform matLocal = new AffineTransform(mat);
		trace.moveTo(ligneDebutX, ligneFinY);
		trace.lineTo(ligneDebutX+(LONGUEUR*Math.cos(Math.toRadians(angleTir))), ligneFinY-(LONGUEUR*Math.sin(Math.toRadians(angleTir))));
		trace.closePath();
		randomColor(g2d);
		g2d.draw(matLocal.createTransformedShape(((trace))));

	}

	/**
	 * Methode qui permet de donner une couleur aleatoire au laser
	 * @param g2d, le contexte graphique
	 */
	//auteur Jeremy Thai
	private void randomColor(Graphics2D g2d) {
		float i = (float) 0.5;
		float r = (rand.nextFloat()/2f) + i;
		float g = (rand.nextFloat()/2f) + i;
		float b = (rand.nextFloat()/2f) + i;

		Color randomColor = new Color(r,g,b);
		g2d.setColor(randomColor);
	}


	/**
	 * 
	 * @return
	 */
	//auteur Jeremy Thai
	public double getLigneFinY() {
		return ligneFinY;
	}

	/**
	 * 
	 */
	//auteur Jeremy Thai
	public void move() { 
		ligneFinY -= vitesse.getY();
		ligneDebutX+=vitesse.getX();
		position = new Vecteur (ligneDebutX, ligneFinY);
		System.out.println(position.toString());
		//System.out.println("position de laser quand il bouge" + ligneDebutX +" "+ ligneFinY) ;
	}
	
	/**
	 * 
	 * @return
	 */
	//auteur Jeremy Thai
	public double getLigneDebutX() {
		return ligneDebutX;
	}

	/**
	 * 
	 * @param ligneDebutX
	 */
	//auteur Jeremy Thai
	public void setLigneDebutX(double ligneDebutX) {
		this.ligneDebutX = ligneDebutX;
	}


/*
	public Area getAireLaser() {
		Path2D.Double trace = new Path2D.Double();
		trace.moveTo(position.getX(), position.getY());
		trace.lineTo(position.getX()+(LONGUEUR*Math.cos(Math.toRadians(angleTir))), position.getY()+(LONGUEUR*Math.sin(Math.toRadians(angleTir))));
		trace.closePath();
		return new Area(trace);
	}
*/

	/*
	public Rectangle2D getLine(){ // pour detecter lintersection
        return new Rectangle2D.Double(position.getX(), ligneFinY,0.1,position.getY());
    }
	 */

	/**
	 * 
	 * @return
	 */
	//auteur Jeremy Thai
	public Area getLaserAire(){ // pour detecter lintersection
		return new Area(new Rectangle2D.Double(position.getX(), ligneFinY,LONGUEUR,position.getY()));
	}

	/**
	 * Retourne la longueur du laser
	 * @return, LONGUEUR, constante de longueur du laser
	 */
	//auteur Arnaud Lefebvre
	public double getLONGUEUR() {
		return LONGUEUR;
	}


	/**
	 * Retourne la position du laser
	 * @return, le vecteur de la position du laser
	 */
	//auteur Arnaud Lefebvre
	public Vecteur getPosition() {
		return new Vecteur (position.getX(),position.getY());
	}

	/**
	 * Permet de modifier la position du laser
	 * @param position, la nouvelle position desiree en vecteur
	 */
	//auteur Arnaud Lefebvre
	public void setPosition(Vecteur position) {
		this.position = position;
	}

	/**
	 * Retourne la vitesse du laser
	 * @return, vitesse, le vecteur vitesse du laser
	 */
	//auteur Arnaud Lefebvre
	public Vecteur getVitesse() {
		return vitesse;
	}

	/**
	 * Permet de modifier la vitesse du laser
	 * @param vitesse, la nouvelle vitesse desire
	 */
	//auteur Arnaud Lefebvre
	public void setVitesse(Vecteur vitesse) {
		this.vitesse = vitesse;
	}

	/**
	 * Retourne l'angle du laser
	 * @return, angleTir, l'angle du laser
	 */
	//auteur Arnaud Lefebvre
	public double getAngleTir() {
		return angleTir;
	}

	/**
	 * Permet de modifier l'angle de tir du laser
	 * @param angleTir, le nouvel angle de tir desire
	 */
	public void setAngleTir(double angleTir) {
		this.angleTir = angleTir;
		updaterAngleVitesse(angleTir);
	}
	/**
	 * 
	 * @return
	 */
	//auteur Jeremy Thai
	public Vecteur getAccel() {
		return accel;
	}

	/**
	 * 
	 * @param accel
	 */
	//auteur Jeremy Thai
	public void setAccel(Vecteur accel) {
		this.accel = accel;
	}
	/**
	 * Methode qui permet d'orienter le laser selon son angle
	 * Cette methode permet de s'assurer que le laser avance bien dans le sens de son angle
	 * @param angle, l'angle du laser
	 */
	//auteur Arnaud Lefebvre
	public void updaterAngleVitesse(double angle) {
		double vitesseEnX=0.5*Math.cos(Math.toRadians(angle));
		double vitesseEnY=0.5*Math.sin(Math.toRadians(angle));
		Vecteur vec = new Vecteur(vitesseEnX,vitesseEnY);
		setVitesse(new Vecteur(vitesseEnX,vitesseEnY));
	//	System.out.println("modification vitesse"+ vec );
	}


}
