package physique;

import geometrie.Vecteur;

public class TestNumerique {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		MoteurPhysique mt = new MoteurPhysique();
		double masse = 0.7;
		double k = 500;
		double u = 0.64;
		Vecteur position = new Vecteur(0.8,0);
		Vecteur vitesse = new Vecteur(-0.0000001, 0);
		double deltaT = 0.008;
		Vecteur accel = new Vecteur(0,0);
		Vecteur forceRappel;
		Vecteur forceFriction;
		Vecteur sommeForces;
		Vecteur n;


		for (int i = 1; i< 5 ; i++) {

			n = mt.forceNormale(masse);
			forceRappel = mt.forceRappel(masse, k, position.getX());
			forceFriction = mt.forceFriction(masse, u, n, vitesse);
			sommeForces = mt.sommeForces(forceRappel, forceFriction);
			MoteurPhysique.miseAJourAcceleration(sommeForces, masse, accel);
			MoteurPhysique.unPasEuler(deltaT, position, vitesse, accel);
			System.out.println("Iteration "+i+" tempsSimule = "+deltaT+" forceRappel = "+forceRappel.getX()+" forceFriction ="+forceFriction.getX()+" a = "+accel.getX()+" v = "+ vitesse.getX() + " x= "+ position.getX() );

		}
	}
}






