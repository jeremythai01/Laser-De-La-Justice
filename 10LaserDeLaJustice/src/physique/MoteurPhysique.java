package physique;

import geometrie.Vecteur;

/**
 * Cette classe regroupera les calculs physiques n�cessaires au mouvement des objets des divers objets dans la sc�ne. 
 *  
 * @author Jeremy Thai
 *
 */
public class MoteurPhysique {


	/**
	 * Calcule une it�ration de l'algorithem d'int�gration num�rique d'Euler semi-implicite. 
	 * Les vecteurs position et vitesse seront modifi�s � la sortie!
	 * 
	 * @param deltaT Incr�ment de temps
	 * @param position Vecteur de position (sera modifi�)
	 * @param vitesse Vecteur de vitesse (sera modifi�)
	 * @param accel Vecteur d'acc�l�ration
	 */
	public static void unPasEuler(double deltaT, Vecteur position, Vecteur vitesse, Vecteur accel) {

		Vecteur deltaVitesse = Vecteur.multiplie(accel, deltaT);
		Vecteur resultV = vitesse.additionne( deltaVitesse ); 
		vitesse.setX(resultV.getX());	//on change le vecteur vitesse
		vitesse.setY(resultV.getY());

		Vecteur deltaPosition = Vecteur.multiplie(vitesse, deltaT);
		Vecteur resultP = position.additionne(deltaPosition); //on calcule la position en considerant la nouvelle vitesse
		position.setX(resultP.getX());  //on change le vecteur position
		position.setY(resultP.getY());


	}//fin m�thode

	public static void unPasVerlet( double deltaT, Vecteur position, Vecteur vitesse, Vecteur accel) {

	
		Vecteur  resultP =  position.additionne(  (vitesse.additionne(accel.multiplie(deltaT).multiplie(1/2))) );
		Vecteur resultV = vitesse.additionne(accel.multiplie(deltaT));
		
		position.setX(resultP.getX());  //on change le vecteur position
		position.setY(resultP.getY());
		//vitesse.setX(resultV.getX());	//on ne change pas le vx car constant?
		vitesse.setY(resultV.getY());

		 
	}

	public Vecteur forceNormale(double masse) {
		return new Vecteur(0, masse*9.8066);
	}


	public Vecteur forceGravi(double masse) {
		return new Vecteur(0, -masse*9.8066);
	}

	public Vecteur sommeForces(Vecteur forceRappel, Vecteur forceFriction) {
		return new Vecteur(forceRappel.additionne(forceFriction).getX(), forceRappel.additionne(forceFriction).getY() );
	}
}
