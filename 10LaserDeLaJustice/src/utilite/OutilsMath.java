package utilite;

import geometrie.Vecteur;
import physique.Laser;

/**
 * Cette classe offre des outils pour resoudre des problemes a bases mathematiques
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


	/**
	 * Cette methode permet d'ajuster la fonction arcTan
	 * @param angle le vecteur de l'angle
	 * @return la bonne orientation de l'angle en degree
	 */
	public static double ajustementArcTan(Vecteur angle) {
		double angleDegre = Math.abs(Math.toDegrees(Math.atan(angle.getY()/angle.getX())));
		if(angle.getX() >=0  && angle.getY() >=0) {
			//premier quadrant
			return angleDegre;
		}else if (angle.getX() <= 0 && angle.getY()>=0 ){
			//deuxieme quadrant
			return (180-angleDegre);
		}else if (angle.getX() <= 0 && angle.getY()<=0 ){
			//troisieme quadrant;
			return (180+ angleDegre);
		}else if (angle.getX() >= 0 && angle.getY()<=0 ){
			//quatrieme quadrant
			return (-angleDegre);
		}
		return 0; // caprice de Java
	}
	
	/**
	 * Cette methode permet d'applquer une translation a un point
	 * @param xt translation en x
	 * @param yt translation en y
	 * @param point le point a translater
	 * @return le point translater
	 */
		public static Vecteur translation(double xt, double yt, Vecteur point) {
			double decalage = 1.5; // Pour annuler le "en intersection"
			
			double a[][]={{1,0,xt},{0,1,yt},{0,0,1}}; //matrice de translation
			double b[]={point.getX(), point.getY(),1};  

			//creer une matrice qui va acceuillir la transformation
			double c[]=new double[3];  //matrice de 1 colonne et 1 ligne  

			//multiplication matriciel 
			for(int i=0;i<3;i++){    
				c[i]=0;      
				for(int k=0;k<3;k++)      
				{      
					c[i]+=a[i][k]*b[k];      
				}
			} 
			return new Vecteur (c[0],c[1]);
		}



}
