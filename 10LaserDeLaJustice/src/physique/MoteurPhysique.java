package physique;

import java.io.Serializable;

import geometrie.Vecteur;

/**
 * Cette classe regroupera les calculs physiques n�cessaires au mouvement des objets des divers objets dans la sc�ne. 
 *  
 * @author Jeremy Thai
 * 
 *
 */

public class MoteurPhysique implements Serializable {

	private static final long serialVersionUID = 2057630780843954427L;

	// Jeremy Thai 
	/**
	 * Calcule une it�ration de l'algorithme d'int�gration num�rique de Verlet et modifie les vecteurs position et vitesse.. 
	 * @param deltaT Incr�ment de temps
	 * @param position Vecteur de position  
	 * @param vitesse Vecteur de vitesse
	 * @param accel Vecteur d'acc�l�ration
	 */

	public static void unPasVerlet( double deltaT, Vecteur position, Vecteur vitesse, Vecteur accel) {

		Vecteur  resultP = position.additionne(vitesse.multiplie(deltaT)).additionne(accel.multiplie(deltaT).multiplie(deltaT).multiplie(0.5));

		//Vecteur  resultP =  position.additionne(  (vitesse.additionne(accel.multiplie(deltaT).multiplie(1/2))) );
		Vecteur resultV = vitesse.additionne(accel.multiplie(deltaT));

		position.setX(resultP.getX());
		position.setY(resultP.getY());

		vitesse.setX(resultV.getX());			// ajout du prof de physique !!!
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

		Vecteur axf1 = vF1.soustrait(vitesse).multiplie(1/tempsF1);

		//Calculs a la position a demie-temps

		Vecteur posMilieu = vitesse.multiplie(deltaT/2).additionne( a.multiplie(Math.pow(deltaT/2,2)).multiplie(1/2)).additionne(pos); // �valuer la position milieu

		Vecteur vMilieu =  vitesse.additionne(   Vecteur.multiplie(a, 3/4).additionne(Vecteur.multiplie(axf1, 1/4)).multiplie(deltaT/2)); //Evaluer la vitesse milieu ( axi = axf1 car a est constant)

		double tempsMilieu = temps+ deltaT/2;

		Vecteur axMid = vMilieu.soustrait(vF1).multiplie(1/tempsMilieu); 

		//Calcul a la 2e position finale

		Vecteur posF2 = vitesse.multiplie(deltaT).additionne( a.multiplie(Math.pow(deltaT,2)).multiplie(1/2)).additionne(pos); // �valuer la position finale #2

		Vecteur vF2 =  vitesse.additionne(   Vecteur.multiplie(a, 1/2).additionne(Vecteur.multiplie(axf1, 1/2)).multiplie(deltaT)); //Evaluer la vitesse milieu ( axi = axf1 car a est constant)

		double tempsFinal2 = temps+ deltaT; // �valuer le temps final #2 :

		Vecteur axF2 = vF2.soustrait(vMilieu).multiplie(1/tempsFinal2);

		//Calcul de la position finale avec pond�ration de l�acc�l�ration

		Vecteur posF = vitesse.multiplie(deltaT).additionne( Vecteur.multiplie(a, 1/3).additionne(Vecteur.multiplie(axMid, 2/3).multiplie(deltaT*deltaT).multiplie(1/2)).additionne(pos)); // �valuer la position finale

		Vecteur vF =  vitesse.additionne( Vecteur.multiplie(a, 1/6).additionne(Vecteur.multiplie(axMid, 4/6)).additionne(Vecteur.multiplie(axF2, 1/6)).multiplie(deltaT)); // �valuer la vitesse finale 

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
	protected static double energieCinetique(double masse, Vecteur v) {

		double vF = Math.sqrt(v.getX()*v.getX() + v.getY()*v.getY());
		return (0.5 * masse* vF*vF); 

	}

	//Jeremy Thai
	/**
	 * 
	 * @param masse masse de l'objet 
	 * @param a acceleration de l'objet en metre par secondes carrees 
	 * @param hauteur hauteur de l'objet par rapport au sol ( en m)
	 * @return l'energie potentielle
	 */
	protected static double energiePotentielle(double masse, Vecteur a, double hauteur) {
		return a.getY()*masse*hauteur;
	}
	//Jeremy Thai
	/**
	 * calcule et retoune l'energie mecanique de l'objet 
	 * @param eC l'energie cinetique de l'objet 	(J)
	 * @param eP l'energie potentielle  de l'objet 	(J)
	 * @return energie mecanique
	 */
	protected static double energieMecanique(double eC, double eP) {
		return eC+eP;
	}

	//Jeremy Thai
	/** 
	 * retourne un vecteur du calcul de la force gravitationnelle
	 /* @param masse masse (kg)
	 * @param accel acceleration en m par s
	 */
	protected static Vecteur forceGravi(double masse, Vecteur accel) {
		return new Vecteur(0, masse*accel.getY());
	}

	/**
	 *  Methode qui calcule le temps ou les temps  exactes lors  dune collision a l'aide de la formule quadratique
	 * @param A 1er element   de l'equation
	 * @param B 2e element   de l'equation
	 * @param C 3e element   de l'equation
	 * @return tab tableau contenant les valeurs de temps en ordre croissants 
	 */
	//Jeremy Thai
	public static double[] quadricRealRoot(double A, double B, double C)
	{
		double dis = B*B-(4*A*C);
		if(dis <0) {
			return new double[0];
		} else if(dis ==0) {
			double tab[] = {-B/(2*A) };
			return tab;
		} else {
			double tab[] = { Math.min((-B-Math.sqrt(dis))/(2*A),(-B+Math.sqrt(dis))/(2*A)), Math.max((-B-Math.sqrt(dis))/(2*A),(-B+Math.sqrt(dis))/(2*A))};
			return tab;
		}
	}


	/**
	 * Methode qui permet de faire la detection de balles et s il y en a une, realiser la collision entre celles-ci
	 * @param balle1 une balle
	 * @param balle2 une autre balle 
	 */
	//Jeremy Thai
	public static void detectionCollisionBalles(Balle balle1, Balle balle2) {

		double rayonA = balle1.getDiametre()/2 ;

		Vecteur rA0 = new Vecteur(balle1.getPosition().getX()+ rayonA ,balle1.getPosition().getY() +  rayonA );

		double rayonB = balle2.getDiametre()/2; 

		Vecteur rB0 =  new Vecteur(balle2.getPosition().getX()+ rayonB ,balle2.getPosition().getY() +  rayonB );
		Vecteur r0 = rB0.soustrait(rA0);

		double D = rayonA + rayonB;


		if(D >= r0.module()) 
			collisionBalles(balle1, balle2);
	}

	/**
	 * Methode qui permet de realiser la collision entre 2 balles 
	 * @param balle1 une balle
	 * @param balle2 une autre balle 
	 */
	//Jeremy Thai

	private static void collisionBalles(Balle balle1, Balle balle2) {

		double rayonA = balle1.getDiametre()/2 ;
		Vecteur rA0 = new Vecteur(balle1.getPosition().getX()+ rayonA ,balle1.getPosition().getY() +  rayonA );
		Vecteur vA =  balle1.getVitesse();
		double rayonB = balle2.getDiametre()/2; 
		Vecteur rB0 =  new Vecteur(balle2.getPosition().getX()+ rayonB ,balle2.getPosition().getY() +  rayonB );
		Vecteur vB =  balle2.getVitesse();


		Vecteur nAB = rB0.soustrait(rA0).normalise();

		double masseA = balle1.getMasse();
		double masseB = balle2.getMasse();

		double e = 1;
		double impulsion = nAB.prodScalaire(vA.soustrait(vB))*(-1-e)/(1/masseA + 1/masseB);

		balle1.setVitesse(vA.additionne(nAB.multiplie(impulsion/masseA)));
		balle2.setVitesse(vB.soustrait(nAB.multiplie(impulsion/masseB)));
	}
}


