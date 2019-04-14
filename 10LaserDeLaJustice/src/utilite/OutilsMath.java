package utilite;

import geometrie.Vecteur;

/**
 * Cette classe offre des outils pour resoudre des problemes a bases mathematique
 * @author Miora
 *
 */
public class OutilsMath  {
	
	/**
	 * Cette methode permet de trouver l'intersection entre deux droite en utilisant la methode de Cramer sous 
	 * forme matricielle
	 * @param a : le 1er coefficient de la ligne 1
	 * @param b : le 2e coefficient de la ligne 1
	 * @param c : le 1er coefficient de la ligne 2
	 * @param d : le 2e coefficient de la ligne 2
	 * @param e : le resultat de la ligne 1 
	 * @param f : le resultat de la ligne 2
	 * @return : un tableau ou les points en intersection
	 */
	public static double [] intersectionCramer(double a, double b, double c, double d, double e, double f) {
		double det;
		double [] tab = new double [2];
		det = a*d-b*c;
		tab[0] = (e*d-b*f)/det; //t
		tab[1] = (a*f-e*c)/det; //k
		return tab;
	}
	
	

}
