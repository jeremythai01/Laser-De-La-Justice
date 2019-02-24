package physique;

import geometrie.Vecteur;

/**
 * Cette classe regroupera les calculs physiques n�cessaires au mouvement des objets des divers objets dans la sc�ne. 
 *  
 * @author Jeremy Thai
 *  @author Caroline Houle 
 *
 */
public class MoteurPhysique {

	// Caroline Houle 
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
	// Caroline Houle 
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

	// Jeremy Thai 
	/**
	 * Calcule une it�ration de l'algorithme d'int�gration num�rique de Verlet et modifie les vecteurs position et vitesse.. 
	 * @param deltaT Incr�ment de temps
	 * @param position Vecteur de position  
	 * @param vitesse Vecteur de vitesse
	 * @param accel Vecteur d'acc�l�ration
	 */

	public static void unPasVerlet( double deltaT, Vecteur position, Vecteur vitesse, Vecteur accel) {


		Vecteur  resultP =  position.additionne(  (vitesse.additionne(accel.multiplie(deltaT).multiplie(1/2))) );
		Vecteur resultV = vitesse.additionne(accel.multiplie(deltaT));

		position.setX(resultP.getX());  //on change le vecteur position
		position.setY(resultP.getY());
		//vitesse.setX(resultV.getX());	//on ne change pas le vx car constant?
		vitesse.setY(resultV.getY());


	}

	// Jeremy Thai 
	/**
	 * Calcule une it�ration de l'algorithme d'int�gration num�rique de Runge-Kutta d'ordre 4 et modifie les vecteurs position et vitesse.
	 * 
	 * @param deltaT Incr�ment de temps
	 * @param temps temps simule
	 * @param pos Vecteur de position  
	 * @param vitesse Vecteur de vitesse e	/
	 * @param a Vecteur d'acc�l�ration
	 */

	public static void unPasRK4(double deltaT,double temps, Vecteur pos, Vecteur vitesse, Vecteur a) {

		//Calculs a la 1ere position finale

		Vecteur posF1 = pos.additionne(Vecteur.multiplie(vitesse, deltaT)); //Evaluer la position finale #1

		Vecteur vF1 = vitesse.additionne(Vecteur.multiplie(a, deltaT)); //Evaluer la vitesse finale #1


		double tempsF1 = temps+deltaT; // �valuer le temps final #1

		//on evalue pas a car a est constant

		//Calculs a la position a demie-temps

		Vecteur posMilieu = vitesse.multiplie(deltaT/2).additionne( a.multiplie(Math.pow(deltaT/2,2)).multiplie(1/2)).additionne(pos); // �valuer la position milieu

		Vecteur vMilieu =  vitesse.additionne(Vecteur.multiplie(a, deltaT/2)); //Evaluer la vitesse milieu ( axi = axf1 car a est constant)

		double tempsMilieu = temps+ deltaT/2;

		//on evalue pas a car a est constant

		//Calcul a la 2e position finale

		Vecteur posF2 = vitesse.multiplie(deltaT).additionne( a.multiplie(Math.pow(deltaT,2)).multiplie(1/2)).additionne(pos); // �valuer la position finale #2

		Vecteur vF2 =  vitesse.additionne(Vecteur.multiplie(a, deltaT)); // �valuer la vitesse finale #2 

		double tempsFinal2 = temps+ deltaT; // �valuer le temps final #2 :

		//on evalue pas a car a est constant

		//Calcul de la position finale avec pond�ration de l�acc�l�ration

		Vecteur posF = vitesse.multiplie(deltaT).additionne( a.multiplie(Math.pow(deltaT,2)).multiplie(1/2)).additionne(pos); // �valuer la position finale

		Vecteur vF =  vitesse.additionne(Vecteur.multiplie(a, deltaT)); // �valuer la vitesse finale 

		double tempsF = temps + deltaT; // �valuer le temps final 


		pos.setX(posF.getX());  //on change le vecteur position
		pos.setY(posF.getY());
		//vitesse.setX(resultV.getX());	//on ne change pas le vx car constant?
		vitesse.setY(vF.getY());
	}

	//Jeremy Thai
	/**
	 *  Calcule et retourne l'energie cinetique de l'objet 	
	 * @param masse masse de l'objet en kg 
	 * @param v vitesse de l'objet en metre par seconde
	 * @return energie cinetique en Joules 
	 */
	public double eC(double masse, Vecteur v) {
		return (1/2) * masse* v.getY()*v.getY(); 
	}

	//Jeremy Thai
	/**
	 * 
	 * @param masse masse de l'objet 
	 * @param a acceleration de l'objet en metre par secondes carrees 
	 * @param hauteur hauteur de l'objet par rapport au sol ( en m)
	 * @return l'energie potentielle
	 */

	public double eP(double masse, Vecteur a, double hauteur) {
		return a.getY()*masse*hauteur;
	}
	//Jeremy Thai
	/**
	 * calcule et retoune l'energie mecanique de l'objet 
	 * @param eC l'energie cinetique de l'objet 	(J)
	 * @param eP l'energie potentielle  de l'objet 	(J)
	 * @return energie mecanique
	 */

	public double eM(double eC, double eP) {
		return eC+eP;
	}

	//Jeremy Thai
	/** 
	 * retourne un vecteur du calcul de la force gravitationnelle
	 /* @param masse masse (kg)
	 * @param accel acceleration en m par s
	 */

	public Vecteur forceGravi(double masse, Vecteur accel) {
		return new Vecteur(0, masse*accel.getY());
	}

}
