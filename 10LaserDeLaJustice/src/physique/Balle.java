package physique;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;
import java.util.ArrayList;

import geometrie.Vecteur;
import interfaces.Dessinable;

/**
 * Classe qui créée une balle et mémorise sa masse, son diamètre, sa position, sa vitesse, son accélération, la somme des forces qui s'applique sur elle et son type.
 * @author Jeremy Thai 
 */

public class Balle implements Dessinable, Serializable { 
	private double diametre = 3;
	private double masse = 15;
	private Ellipse2D.Double cercle;
	private Vecteur position, vitesse, accel;
	private double vInitY;
	private boolean toucheSolPremiereFois = true;
	private Vecteur forceGravi;
	private MoteurPhysique mt = new MoteurPhysique();
	private Type type;



	/**
	 * Classe enumeration des types de balle
	 * @author Jeremy Thai
	 *
	 */
	enum Type {
		SMALL, MEDIUM, LARGE;
	}


	/**
	 * Constructeur ou la position, la vitesse et l'acceleration  initiales sont spécifiés
	 * @param position Vecteur incluant les positions en x et y du coin superieur-gauche
	 * @param vitesse Vecteur incluant les vitesses en x et y
	 * @param accel Vecteur incluant les accelerations en x et y  
	 * @param diametre diametre (unites du monde reel)
	 * @param masse masse (kg)
	 */
	public Balle(Vecteur position, Vecteur vitesse, String size) {	
		setPosition( position );
		setVitesse( vitesse );
		setAccel( new Vecteur(0,9.8) );
		forceGravi = mt.forceGravi(masse, accel);
		System.out.println(accel);

		switch(size) {
		case "SMALL":
			type = Type.SMALL;
			setMasse(5);
			setDiametre(1);
			break;
		case "MEDIUM":
			type = Type.MEDIUM;
			setMasse(10);
			setDiametre(2);
			break;
		case "LARGE":
			type = Type.LARGE;
			break;
		}
	}
	

	public Balle ( Balle balle ) {
		this.diametre = balle.getDiametre();
		setAccel( balle.getAccel());
		this.masse = balle.getMasse();
		this.vInitY = balle.getvInitY();
		setPosition( balle.getPosition() );
		setVitesse(balle.getVitesse());
		this.forceGravi = balle.getForceGravi();
		this.type = balle.getType();
		this.toucheSolPremiereFois = balle.isToucheSolPremiereFois();
	}



	public boolean isToucheSolPremiereFois() {
		return toucheSolPremiereFois;
	}


	/**
	 * Permet de dessiner la balle selon le contexte graphique en parametre.
	 * @param g2d contexte graphique
	 * @param mat matrice de transformation monde-vers-composant
	 * @param hauteur hauteur du monde reelle
	 * @param largeur largeur du monde reelle
	 * 
	 */
	@Override
	public void dessiner(Graphics2D g2d, AffineTransform mat, double hauteur, double largeur) {
		AffineTransform matLocal = new AffineTransform(mat);
		cercle = new Ellipse2D.Double(position.getX(), position.getY(), diametre, diametre);
		checkCollisions(largeur , hauteur); 

		switch(type){
		case LARGE:
			g2d.setColor(Color.blue);
			break;
		case MEDIUM:
			g2d.setColor(Color.green);
			break;
		case SMALL:
			g2d.setColor(Color.red);
			break;
		}		
		g2d.fill( matLocal.createTransformedShape(cercle) );		
	}

	/**
	 * Effectue une iteration de l'algorithme d'Euler implicite. Calcule la nouvelle vitesse et la nouvelle
	 * position de la balle.
	 * @param deltaT intervalle de temps (pas)
	 */
	public void unPasEuler(double deltaT) {
		MoteurPhysique.unPasEuler(deltaT, position, vitesse, accel);
		//System.out.println("Nouvelle vitesse: " + vitesse.toString() + "  Nouvelle position: " + position.toString());
	}
	/**
	 * Effectue une iteration de l'algorithme Verlet. Calcule la nouvelle vitesse et la nouvelle
	 * position de la balle.
	 * @param deltaT intervalle de temps (pas)
	 */
	public void unPasVerlet(double deltaT) {
		MoteurPhysique.miseAJourAcceleration(forceGravi, masse, accel);
		MoteurPhysique.unPasVerlet(deltaT, position, vitesse, accel);
		//System.out.println("Nouvelle vitesse: " + vitesse.toString() + "  Nouvelle position: " + position.toString());
	}
	/**
	 * Effectue une iteration de l'algorithme de Runge-Kutta ordre 4. Calcule la nouvelle vitesse et la nouvelle
	 * position de la balle.
	 * @param deltaT intervalle de temps (pas)
	 * @param tempsEcoule temps simule  (s)
	 */
	public void unPasRK4(double deltaT, double tempsEcoule) {
		MoteurPhysique.miseAJourAcceleration(forceGravi, masse, accel);
		MoteurPhysique.unPasRK4(deltaT, tempsEcoule, position, vitesse, accel);
		//System.out.println("Nouvelle vitesse: " + vitesse.toString() + "  Nouvelle position: " + position.toString());
	}


	/**
	 * Modifie la position de la balle
	 * @param pos vecteur des positions x et y
	 */
	public void setPosition(Vecteur pos) {
		Vecteur newVec = new Vecteur(pos.getX(), pos.getY());
		this.position = newVec;
	}

	/**
	 * Retourne la position courante
	 * @return la position courante
	 */
	public Vecteur getPosition() { return (position); }

	/**
	 * modifie ou affecte une vitesse a celle courante de la balle
	 * @param vitesse vecteur des vitesse x et y
	 */
	public void setVitesse(Vecteur vitesse) {

		Vecteur newVec = new Vecteur(vitesse.getX(), vitesse.getY());
		this.vitesse = newVec;
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
	 * @param accel vecteur des accélérations en x et y
	 */

	public void setAccel(Vecteur accel) {
		Vecteur newVec = new Vecteur(accel.getX(), accel.getY());
		this.accel = newVec;
	}


	/**
	 * Retourne l'acceleration courante
	 * @return acceleration courante
	 */
	public Vecteur getAccel() { return (accel); }


	/**
	 * Retourne le diametre de la balle
	 * @return Le diamètre
	 */
	public double getDiametre() { return diametre; }


	/**
	 * MOdifie le diametre de la balle
	 * @param diametre Le nouveau diamètre
	 */
	public void setDiametre(double diametre) { this.diametre = diametre; }


	/**
	 * Retourne la masse en Kg
	 * @return La masse en kg
	 */
	public double getMasse() {	return masse; }


	/**
	 * Modifie la masse 
	 * @param masseEnKg masse en kg
	 */
	public void setMasse(double masse) { this.masse = masse; }



	/**
	 * Modifie la vitesse courante par la vitesse initiale
	 * @param vInitY vitesse initiale 
	 */

	public void setvInitY(double vInitY) { this.vInitY = vInitY; }


	/**
	 * Retourne la force gravitationnelle
	 * @return force gravitationnelle
	 */
	public Vecteur getForceGravi() {
		return forceGravi;
	}


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
			if(vitesse.getX() >0 )
				vitesse.setX(-vitesse.getX());

		if(position.getX() <= 0) // touche le mur gauche 
			if(vitesse.getX() < 0 )
				vitesse.setX(-vitesse.getX());


	}

	/**
	 * Modifie le type de balle en creant 2 balles d'un plus petit type et en les ajoutant dans la liste de balles 
	 * @param liste liste des balles 
	 */

	public void shrink(ArrayList<Balle> liste) {

		Balle nouvBalle1;
		Balle nouvBalle2;
		ArrayList<Balle> listeNouvBalles = new ArrayList<Balle> ();

		switch(type)	{

		case LARGE:


			nouvBalle1 = new Balle(position, vitesse, "MEDIUM");
			nouvBalle2 = new Balle(position, vitesse, "MEDIUM");
			if(vitesse.getX() < 0) {

				//gauche
				nouvBalle1.setVitesse(new Vecteur(vitesse.getX(),vitesse.getY())); 
				nouvBalle1.getPosition().setX(position.getX()-1.000005);

				//droite
				nouvBalle2.setVitesse(new Vecteur(-vitesse.getX(),vitesse.getY())); 
				nouvBalle2.getPosition().setX(position.getX()+1.000005);
			}

			if(vitesse.getX() > 0) {
				//gauche
				nouvBalle1.setVitesse(new Vecteur(-vitesse.getX(),vitesse.getY())); 
				nouvBalle1.getPosition().setX(position.getX()-1.000005);

				//droite
				nouvBalle2.setVitesse(new Vecteur(vitesse.getX(),vitesse.getY())); 
				nouvBalle2.getPosition().setX(position.getX()+1.000005);
			}

			listeNouvBalles.add(nouvBalle1);
			listeNouvBalles.add(nouvBalle2);
			liste.remove(this);
			liste.add(nouvBalle1);
			liste.add(nouvBalle2);
			

			break;

		case MEDIUM:

			nouvBalle1 = new Balle(position, vitesse, "SMALL");
			nouvBalle2 = new Balle(position, vitesse, "SMALL");

			if(vitesse.getX() < 0) {
				//gauche
				nouvBalle1.setVitesse(new Vecteur(vitesse.getX(),vitesse.getY())); 
				nouvBalle1.getPosition().setX(position.getX()-0.7);


				//droite
				nouvBalle2.setVitesse(new Vecteur(-vitesse.getX(),vitesse.getY())); 
				nouvBalle2.getPosition().setX(position.getX()+0.7);

			}



			if(vitesse.getX() > 0) {

				//gauche
				nouvBalle1.setVitesse(new Vecteur(-vitesse.getX(),vitesse.getY())); 
				nouvBalle1.getPosition().setX(position.getX()-0.7);

				//droite
				nouvBalle2.setVitesse(new Vecteur(vitesse.getX(),vitesse.getY())); 
				nouvBalle2.getPosition().setX(position.getX()+0.7);
			}

			listeNouvBalles.add(nouvBalle1);
			listeNouvBalles.add(nouvBalle2);
			liste.remove(this);
			liste.add(nouvBalle1);
			liste.add(nouvBalle2);
			

			break;

		case SMALL:
			liste.remove(this);
			break;
		}		




	}

	/**
	 * retourne l'aire d'une balle en forme de cercle
	 * @return aire de la balle 
	 */
	public Area getAireBalle() {
		cercle = new Ellipse2D.Double(position.getX(), position.getY(), diametre, diametre);
		return new Area(cercle);

	}

	
	/**
	 * Methode qui retourne le type de la balle 
	 * @return type type de la balle
	 */
	public Type getType() {
		return type;
	}



	public double getvInitY() {
		return vInitY;
	}
	
	
}//fin classe





