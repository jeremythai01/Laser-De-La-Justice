package physique;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import geometrie.Vecteur;
import interfaces.Dessinable;

/**
 * Classe Balle: représentation sommaire d'une balle à l'aide d'un simple cercle.
 * Une balle mémorise sa masse, son diamètre, sa position, sa vitesse, son accélération, la somme des forces qui s'applique sur elle.
 *
 */

public class Balle implements Dessinable { 
	private double diametre = 1;
	private double masseEnKg = 1;
	private Ellipse2D.Double cercle=null;
	private Vecteur position, vitesse, accel;
	private double width,height;
	private double vInitX,vInitY;
	private boolean toucheSol = true;
	private Vecteur forceGravi;



	public Balle(Vecteur position, Vecteur vitesse, Vecteur accel, double diametre, double masse) {
		this.diametre = diametre;	
		setPosition( position ); //ces setters crent des copies des vecteurs
		setVitesse( vitesse );
		setAccel( accel );
		this.masseEnKg = masse;
		forceGravi = new Vecteur(0, masse*9.8);
	}




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


	/**
	 * Constructeur ou la position initiale est specifiee par un vecteur
	 * La vitesse et acceleration sont mises à zero. 
	 * @param position Vecteur incluant les positions en x et y du coin superieur-gauche
	 * @param diametre diametre (unites du monde reel)
	 */
	public Balle(Vecteur position, double diametre) {
		this( position, new Vecteur(0,0),  new Vecteur(0, 0), diametre );
	}

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

	/**
	 * Constructeur ou la position initiale est specifiee par un vecteur et ou la vitesse initiale est aussi specifiee
	 * @param position Vecteur incluant les positions en x et y du coin superieur-gauche
	 * @param vitesse Vecteur incluant les vitesses en x et y 
	 * @param diametre diametre (unites du monde reel)
	 */
	public Balle(Vecteur position, Vecteur vitesse, double diametre) {
		this( position, vitesse,  new Vecteur(0, 0), diametre );
	}

	/**
	 * Permet de dessiner la balle, sur le contexte graphique passe en parametre.
	 * @param g2d contexte graphique
	 * @param matMC matrice de transformation monde-vers-composant
	 */
	public void dessiner(Graphics2D g2d, AffineTransform mat ) {
		AffineTransform matLocal = new AffineTransform(mat);
		cercle = new Ellipse2D.Double(position.getX(), position.getY(), diametre, diametre);
		g2d.draw( matLocal.createTransformedShape(cercle) );		
	}//fin methode


	/**
	 * Effectue une iteration de l'algorithme d'Euler implicite. Calcule la nouvelle vitesse et la nouvelle
	 * position de la balle.
	 * @param deltaT intervalle de temps (pas)
	 */
	public void unPasEuler(double deltaT) {
		MoteurPhysique.unPasEuler(deltaT, position, vitesse, accel);
		System.out.println("Nouvelle vitesse: " + vitesse.toString() + "  Nouvelle position: " + position.toString());
	}


	public void unPasVerlet(double deltaT) {
		MoteurPhysique.miseAJourAcceleration(forceGravi, masseEnKg, accel);
		MoteurPhysique.unPasVerlet(deltaT, position, vitesse, accel);
		System.out.println("Nouvelle vitesse: " + vitesse.toString() + "  Nouvelle position: " + position.toString());
	}


	/**
	 * Modifie la somme des forces excercées sur la balle
	 * Ceci a pour consequence de modifier l'acceleration
	 * @param forces Les force exercees sur la balle
	 */


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


	/**
	 * Retourne la position courante
	 * @return la position courante
	 */
	public Vecteur getPosition() {
		return (position);
	}

	/**
	 * Associe une vitesse, ou modifie la vitesse courante de la balle
	 * @param vitesse Vecteur incluant les vitesses en x et y 
	 */
	public void setVitesse(Vecteur vitesse) {
		//on fait une copie du vecteur passé en paramètre (l'orignal peut donc être modifé au besoin)
		Vecteur nouveauVec = new Vecteur(vitesse.getX(), vitesse.getY());
		this.vitesse = nouveauVec;
	}

	/**
	 * Retourne la vitesse courante
	 * @return la vitesse courante
	 */
	public Vecteur getVitesse() {
		return (vitesse);
	}

	/**
	 * Associe une acceleration, ou modifie l'acceleration courante de la balle
	 * @param accel Vecteur incluant les accelerations en x et y 
	 */

	public void setAccel(Vecteur accel) {
		//on fait une copie du vecteur  passé en paramètre (l'orignal peut donc être modifé au besoin)
		Vecteur nouveauVec = new Vecteur(accel.getX(), accel.getY());
		this.accel = nouveauVec;
	}

	/**
	 * Retourne l'acceleration courante
	 * @return acceleration courante
	 */
	public Vecteur getAccel() {
		return (accel);
	}


	/**
	 * Retourne le diametre de la balle
	 * @return Le diamètre
	 */
	public double getDiametre() {
		return diametre;
	}

	/**
	 * MOdifie le diametre de la balle
	 * @param diametre Le nouveau diamètre
	 */
	public void setDiametre(double diametre) {
		this.diametre = diametre;
	}


	/**
	 * Retourne la masse en Kg
	 * @return La masse en kg
	 */
	public double getMasseEnKg() {
		return masseEnKg;
	}

	/**
	 * Modifie la masse 
	 * @param masseEnKg La msse exprimee en kg
	 */
	public void setMasseEnKg(double masseEnKg) {
		this.masseEnKg = masseEnKg;
	}


	@Override
	public void dessiner(Graphics2D g2d) {
		// TODO Auto-generated method stub
		cercle = new Ellipse2D.Double(position.getX(), position.getY(), diametre, diametre);
		g2d.draw(cercle);
	}

	public double getWidth() {
		return width;
	}


	public void setWidth(double width) {
		this.width = width;
	}


	public double getHeight() {
		return height;
	}


	public  void setHeight(double height) {
		this.height = height;
	}

	public void checkCollisions( ) {
		if(position.getY()+diametre >= height){ // touche le sol 
			if(toucheSol) {
				vInitY = vitesse.getY();
				vitesse.setY(-vInitY);
				toucheSol = false;
			}
			vitesse.setY(-vInitY);
		}
		if(position.getX()+diametre >= width) // touche le mur droite 
			vitesse.setX(-vitesse.getX());

		if(position.getX() <= 0) // touche le mur gauche 
			vitesse.setX(-vitesse.getX());
	}



	public double getvInitY() {
		return vInitY;
	}


	public void setvInitY(double vInitY) {
		this.vInitY = vInitY;
	}



}//fin classe





