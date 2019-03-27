package physique;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
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
	private Path2D.Double trace;
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
		updaterAngleVitesse(angleTir);
		


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
		trace.moveTo(ligneDebutX+(LONGUEUR*Math.cos(Math.toRadians(angleTir))), ligneFinY-(LONGUEUR*Math.sin(Math.toRadians(angleTir))));
		trace.lineTo(ligneDebutX, ligneFinY);
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
	 * retourne le point final y du trace
	 * @return ligneFinY le point final y du trace
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
	//	System.out.println("position de laser quand il bouge" + ligneDebutX +" "+ ligneFinY) ;
	}
	
	/**
	 * retourne le point initial en x du trace
	 * @return ligneDebutX retourne le point initial en x du trace
	 */
	//auteur Jeremy Thai
	public double getLigneDebutX() {
		return ligneDebutX;
	}

	/**
	 * Modifie le point initial en x du trace par celle en parametre
	 * @param ligneDebutX le point initial en x du trace par celle en parametre
	 */
	//auteur Jeremy Thai
	public void setLigneDebutX(double ligneDebutX) {
		this.ligneDebutX = ligneDebutX;
	}


	/**
	 * Retourne l'aire en forme de rectangle du laser
	 * @return l'aire en forme de rectangle du laser
	 */
	//auteur Jeremy Thai
	public Area getLaserAire(){ // pour detecter lintersection
		AffineTransform matLocal = new AffineTransform(); // pourquoi mat ne fonctionne pas ?
		Rectangle2D.Double rect = new Rectangle2D.Double(ligneDebutX, ligneFinY,LONGUEUR,0.01);
		 matLocal.rotate(-Math.toRadians(angleTir), ligneDebutX ,ligneFinY);
		return new Area(matLocal.createTransformedShape(((rect))));
	 //
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
		//return new Vecteur (position.getX(),position.getY());
		return new Vecteur (ligneDebutX+(LONGUEUR*Math.cos(Math.toRadians(angleTir))), ligneFinY-(LONGUEUR*Math.sin(Math.toRadians(angleTir))));
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
	//auteur Arnaud Lefebvre
	public void setAngleTir(double angleTir) {
		this.angleTir = angleTir;
		updaterAngleVitesse(angleTir);
	}
	/**
	 * retourne lacceleration du laser
	 * @return accel acceleration du laser
	 */
	//auteur Jeremy Thai
	public Vecteur getAccel() {
		return accel;
	}

	/**
	 * modifie l'acceleration du laser par celle en parametre
	 * @param accel acceleration du laser
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
		setVitesse(vec);
	//	System.out.println("modification vitesse"+ vec );
	}


}
