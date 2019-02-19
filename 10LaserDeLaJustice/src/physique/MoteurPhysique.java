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



	public static void unPasRK4(double deltaT,double tempsI, Vecteur posI, Vecteur vyI, Vecteur a) {

		//Calculs a la 1ere position finale

		Vecteur posF1 = posI.additionne(Vecteur.multiplie(vyI, deltaT)); //Evaluer la position finale #1

		Vecteur vyF1 = vyI.additionne(Vecteur.multiplie(a, deltaT)); //Evaluer la vitesse finale #1
		

		double tempsF1 = tempsI+deltaT; // Évaluer le temps final #1
		
		//on evalue pas a car a est constant

		//Calculs a la position a demie-temps

		Vecteur posMilieu = vyI.multiplie(deltaT/2).additionne( a.multiplie(Math.pow(deltaT/2,2)).multiplie(1/2)).additionne(posI); // Évaluer la position milieu

		Vecteur vMilieu =  vyI.additionne(Vecteur.multiplie(a, deltaT/2)); //Evaluer la vitesse milieu ( axi = axf1 car a est constant)

		double tempsMilieu = tempsI+ deltaT/2;

		//on evalue pas a car a est constant
		
		//Calcul a la 2e position finale

		Vecteur posF2 = vyI.multiplie(deltaT).additionne( a.multiplie(Math.pow(deltaT,2)).multiplie(1/2)).additionne(posI); // Évaluer la position finale #2

		Vecteur vyF2 =  vyI.additionne(Vecteur.multiplie(a, deltaT)); // Évaluer la vitesse finale #2 

		double tempsFinal2 = tempsI+ deltaT; // Évaluer le temps final #2 :
		
		//on evalue pas a car a est constant

		//Calcul de la position finale avec pondération de l’accélération
		
		Vecteur posF = vyI.multiplie(deltaT).additionne( a.multiplie(Math.pow(deltaT,2)).multiplie(1/2)).additionne(posI); // Évaluer la position finale
		
		Vecteur vyF =  vyI.additionne(Vecteur.multiplie(a, deltaT)); // Évaluer la vitesse finale 

		double tempsF = tempsI + deltaT; // Évaluer le temps final 

		
		posI.setX(posF.getX());  //on change le vecteur position
		posI.setY(posF.getY());
		//vitesse.setX(resultV.getX());	//on ne change pas le vx car constant?
		vyI.setY(vyF.getY());
		
		
	}





	public double eC(double masse, Vecteur v) {
		return (1/2) * masse* v.getY()*v.getY(); 
	}

	public double eP(double masse, Vecteur a, double hauteur) {
		return a.getY()*masse*hauteur;
	}

	public double eM(double eC, double eP) {
		return eC+eP;
	}


	public Vecteur forceGravi(double masse) {
		return new Vecteur(0, -masse*9.8066);
	}

	public Vecteur sommeForces(Vecteur forceRappel, Vecteur forceFriction) {
		return new Vecteur(forceRappel.additionne(forceFriction).getX(), forceRappel.additionne(forceFriction).getY() );
	}
}
