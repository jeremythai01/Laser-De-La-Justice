package physique;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import geometrie.Vecteur;
import interfaces.Dessinable;

/**
 * Classe Balle: représentation sommaire d'une balle à l'aide d'un simple cercle.
 * Une balle mémorise sa masse, son diamètre, sa position, sa vitesse, son accélération, la somme des forces qui s'applique sur elle.
 * @author Jeremy Thai 
 * @author Caroline Houle 
 */

public class Balle implements Dessinable { 
	private double diametre = 1;
	private double masse = 1;
	private Ellipse2D.Double cercle;
	private Vecteur position, vitesse, accel;
	private double vInitY;
	private boolean toucheSolPremiereFois = true;
	private Vecteur forceGravi;
	private MoteurPhysique mt = new MoteurPhysique();
	private Area aireBalle;
	private Type type;

	private enum Type {
		SMALL, MEDIUM, LARGE;
	}



	// Jeremy Thai
	/**
	 * Constructeur ou la position, la vitesse et l'acceleration  initiales sont spécifiés
	 * @param position Vecteur incluant les positions en x et y du coin superieur-gauche
	 * @param vitesse Vecteur incluant les vitesses en x et y
	 * @param accel Vecteur incluant les accelerations en x et y  
	 * @param diametre diametre (unites du monde reel)
	 * @param masse masse (kg)
	 */
	public Balle(Vecteur position, Vecteur vitesse, Vecteur accel, double diametre, double masse, String size) {
		this.diametre = diametre;	
		setPosition( position ); //ces setters crent des copies des vecteurs
		setVitesse( vitesse );
		setAccel( accel );
		this.masse = masse;
		forceGravi = mt.forceGravi(masse, accel);
		switch(size) {
		case "SMALL":
			type = Type.SMALL;
			break;
		case "MEDIUM":
			type = Type.MEDIUM;
			break;
		case "LARGE":
			type = Type.LARGE;
			break;
		}

		cercle = new Ellipse2D.Double(position.getX(), position.getY(), diametre, diametre);
		aireBalle = new Area(cercle);
		
	}



	// Caroline Houle
	/**
	 * Constructeur ou la position, la vitesse et l'acceleration  initiales sont spécifiés
	 * @param position Vecteur incluant les positions en x et y du coin superieur-gauche
	 * @param vitesse Vecteur incluant les vitesses en x et y
	 * @param accel Vecteur incluant les accelerations en x et y  
	 * @param diametre diametre (unites du monde reel)
	 */

	public Balle(Vecteur position, Vecteur vitesse, Vecteur accel, double diametre) {
		this.diametre = diametre;	
		setPosition( position ); //ces setters crent des copies des vecteurs
		setVitesse( vitesse );
		setAccel( accel );
	}

	// Caroline Houle
	/**
	 * Constructeur ou la position initiale est specifiee par un vecteur
	 * La vitesse et acceleration sont mises à zero. 
	 * @param position Vecteur incluant les positions en x et y du coin superieur-gauche
	 * @param diametre diametre (unites du monde reel)
	 */
	public Balle(Vecteur position, double diametre) {
		this( position, new Vecteur(0,0),  new Vecteur(0, 0), diametre );
	}

	// Caroline Houle
	/**
	 * Constructeur ou la position initiale est specifiee par x et y
	 * La vitesse et acceleration sont mises à zero. 
	 * @param x Coin superieur-gauche en x
	 * @param y Coin superieur-gauche en y
	 * @param diametre diametre (unites du monde reel)
	 */
	public Balle(double x, double y, double diametre) {
		this( new Vecteur(x, y), new Vecteur(0,0),  new Vecteur(0, 0), diametre ); //appelle l'autre constructeur
	}

	// Caroline Houle
	/**
	 * Constructeur ou la position initiale est specifiee par un vecteur et ou la vitesse initiale est aussi specifiee
	 * @param position Vecteur incluant les positions en x et y du coin superieur-gauche
	 * @param vitesse Vecteur incluant les vitesses en x et y 
	 * @param diametre diametre (unites du monde reel)
	 */

	public Balle(Vecteur position, Vecteur vitesse, double diametre) {
		this( position, vitesse,  new Vecteur(0, 0), diametre );
	}
	// Jeremy Thai
	/**
	 * Permet de dessiner la balle, sur le contexte graphique passe en parametre.
	 * @param g2d contexte graphique
	 * @param matMC matrice de transformation monde-vers-composant
	 * @param matMC matrice de transformation monde-vers-composant
	 * 
	 */
	@Override
	public void dessiner(Graphics2D g2d, AffineTransform mat, double hauteur, double largeur) {
		AffineTransform matLocal = new AffineTransform(mat);
		cercle = new Ellipse2D.Double(position.getX(), position.getY(), diametre, diametre);
	//	aireBalle = new Area(cercle);
		checkCollisions(largeur , hauteur); 
		g2d.draw( matLocal.createTransformedShape(cercle) );		



	}//fin methode

	// Jeremy Thai
	/**
	 * Effectue une iteration de l'algorithme d'Euler implicite. Calcule la nouvelle vitesse et la nouvelle
	 * position de la balle.
	 * @param deltaT intervalle de temps (pas)
	 */
	public void unPasEuler(double deltaT) {
		MoteurPhysique.unPasEuler(deltaT, position, vitesse, accel);
		System.out.println("Nouvelle vitesse: " + vitesse.toString() + "  Nouvelle position: " + position.toString());
	}
	// Jeremy Thai
	/**
	 * Effectue une iteration de l'algorithme Verlet. Calcule la nouvelle vitesse et la nouvelle
	 * position de la balle.
	 * @param deltaT intervalle de temps (pas)
	 */
	public void unPasVerlet(double deltaT) {
		MoteurPhysique.miseAJourAcceleration(forceGravi, masse, accel);
		MoteurPhysique.unPasVerlet(deltaT, position, vitesse, accel);
		System.out.println("Nouvelle vitesse: " + vitesse.toString() + "  Nouvelle position: " + position.toString());
	}
	// Jeremy Thai
	/**
	 * Effectue une iteration de l'algorithme de Runge-Kutta ordre 4. Calcule la nouvelle vitesse et la nouvelle
	 * position de la balle.
	 * @param deltaT intervalle de temps (pas)
	 * @param tempsEcoule temps simule  (s)
	 */
	public void unPasRK4(double deltaT, double tempsEcoule) {
		MoteurPhysique.miseAJourAcceleration(forceGravi, masse, accel);
		MoteurPhysique.unPasRK4(deltaT, tempsEcoule, position, vitesse, accel);
		System.out.println("Nouvelle vitesse: " + vitesse.toString() + "  Nouvelle position: " + position.toString());
	}

	// Caroline Houle 
	/**
	 * Modifie la position de la balle
	 * Note: ici on decide de simplement refaire la forme sous-jacente!
	 * @param pos Vecteur incluant les positions en x et y 
	 */
	public void setPosition(Vecteur pos) {
		//on fait une copie du vecteur  passe en paramètre (l'orignal peut donc être modifé au besoin)
		Vecteur nouveauVec = new Vecteur(pos.getX(), pos.getY());
		this.position = nouveauVec;
	}

	// Jeremy Thai
	/**
	 * Retourne la position courante
	 * @return la position courante
	 */
	public Vecteur getPosition() { return (position); }

	// Caroline Houle 
	/**
	 * Associe une vitesse, ou modifie la vitesse courante de la balle
	 * @param vitesse Vecteur incluant les vitesses en x et y 
	 */
	public void setVitesse(Vecteur vitesse) {
		//on fait une copie du vecteur passé en paramètre (l'orignal peut donc être modifé au besoin)
		Vecteur nouveauVec = new Vecteur(vitesse.getX(), vitesse.getY());
		this.vitesse = nouveauVec;
	}
	// Jeremy Thai
	/**
	 * Retourne la vitesse courante
	 * @return la vitesse courante
	 */
	public Vecteur getVitesse() {
		return (vitesse);
	}

	// Caroline Houle 
	/**
	 * Associe une acceleration, ou modifie l'acceleration courante de la balle
	 * @param accel Vecteur incluant les accelerations en x et y 
	 */

	public void setAccel(Vecteur accel) {
		//on fait une copie du vecteur  passé en paramètre (l'orignal peut donc être modifé au besoin)
		Vecteur nouveauVec = new Vecteur(accel.getX(), accel.getY());
		this.accel = nouveauVec;
	}
	// Jeremy Thai
	/**
	 * Retourne l'acceleration courante
	 * @return acceleration courante
	 */
	public Vecteur getAccel() { return (accel); }

	// Jeremy Thai
	/**
	 * Retourne le diametre de la balle
	 * @return Le diamètre
	 */
	public double getDiametre() { return diametre; }

	// Jeremy Thai
	/**
	 * MOdifie le diametre de la balle
	 * @param diametre Le nouveau diamètre
	 */
	public void setDiametre(double diametre) { this.diametre = diametre; }

	// Jeremy Thai
	/**
	 * Retourne la masse en Kg
	 * @return La masse en kg
	 */
	public double getMasse() {	return masse; }

	// Jeremy Thai
	/**
	 * Modifie la masse 
	 * @param masseEnKg masse en kg
	 */
	public void setMasse(double masse) { this.masse = masse; }


	//Jeremy thai
	/**
	 * Modifie la vitesse courante par la vitesse initiale
	 * @param vInitY vitesse initiale 
	 */

	public void setvInitY(double vInitY) { this.vInitY = vInitY; }

	//Jeremy thai
	/**
	 * Retourne la force gravitationnelle
	 * @return force gravitationnelle
	 */
	public Vecteur getForceGravi() {
		return forceGravi;
	}

	//Jeremy thai
	/**
	 *Evalue une collision avec le sol ou un mur et modifie la vitesse courante selon la collision
	 * @param width largeur du monde 
	 * @param height hauteur  du monde 
	 */

	public void checkCollisions(double width, double height) {

		if(position.getY()+diametre >= height) { // touche le sol 
			if(toucheSolPremiereFois) {
				vInitY = vitesse.getY();
				vitesse.setY(-vInitY);
				toucheSolPremiereFois = false;
			} else {
				vitesse.setY(-vInitY);
			}
		}

		if(position.getX()+diametre >= width) // touche le mur droite 
			vitesse.setX(-vitesse.getX());

		if(position.getX() <= 0) // touche le mur gauche 
			vitesse.setX(-vitesse.getX());


	}


	public void collisionBalleLaser(Area aireBalle, Graphics2D g2d,  Area aireLaser, ArrayList<Balle> liste) {

		Area aireInter = new Area(aireBalle);
		aireInter.intersect(aireLaser);

		if(!aireInter.isEmpty()) { // Si la balle est touche  par le laser 

			Balle newBall;
			switch(type)	{

			case LARGE:

				g2d.setColor(Color.green);
				newBall = this; // rend a medium
				newBall.setVitesse(new Vecteur(vitesse.getX()+1,vitesse.getY()+1)); 
				liste.add(newBall);
				newBall.setMasse(masse-5);
				liste.add(newBall);
				newBall.setVitesse(new Vecteur(-(vitesse.getX()+1),vitesse.getY()+1)); 
				liste.add(newBall);
				type = Type.MEDIUM;
				liste.remove(this);
				break;

			case MEDIUM:
				g2d.setColor(Color.red);
				newBall = this; // rend a petit
				newBall.setVitesse(new Vecteur(vitesse.getX()+1,vitesse.getY()+1)); 
				newBall.setMasse(masse-5);				
				liste.add(newBall);
				newBall.setVitesse(new Vecteur(-(vitesse.getX()+1),vitesse.getY()+1)); 
				liste.add(newBall);
				type = Type.SMALL;
				liste.remove(this);
				break;

			case SMALL:
				liste.remove(this);
				break;
			}		
		}
	}
	
	public Ellipse2D getBallOval() { // pour detecter lintersection
        return new Ellipse2D.Double(position.getX(), position.getY(), diametre, diametre);
    }


}//fin classe





