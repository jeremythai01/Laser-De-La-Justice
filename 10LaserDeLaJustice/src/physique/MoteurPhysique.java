package physique;

import geometrie.Vecteur;

/**
 * Cette classe regroupera les calculs physiques nécessaires au mouvement des objets des divers objets dans la scène. 
 *  
 * @author Jeremy Thai
 *
 */
public class MoteurPhysique {


	/**
	 * Calcule l'acceleration en fonction de la masse et des forces appliquees
	 * @param sommeDesForces La somme des forces appliquees
	 * @param masse La masse 
	 * @param accel En sortie, l'acceleraion calculee, avec a=m/F
	 */
	public static void miseAJourAcceleration(Vecteur sommeDesForces, double masse, Vecteur accel) {
		//sachant que f=ma, on calcule a = m / f
		accel.setX( sommeDesForces.getX() / masse );
		accel.setY( sommeDesForces.getY() / masse );

	}
	
	/**
	 * Calcule une itération de l'algorithem d'intégration numérique d'Euler semi-implicite. 
	 * Les vecteurs position et vitesse seront modifiés à la sortie!
	 * 
	 * @param deltaT Incrément de temps
	 * @param position Vecteur de position (sera modifié)
	 * @param vitesse Vecteur de vitesse (sera modifié)
	 * @param accel Vecteur d'accélération
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


	}//fin méthode

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
