package objets;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import interfaces.Dessinable;

/**
 * Classe qui permet la creation d'un prisme qui va ensuite separer un laser en plusieurs
 * @author Arnaud
 *
 */
public class Prisme extends Objet implements Dessinable{

	@Override
	/**
	 * Permet de dessiner le prisme selon le contexte graphique en parametre.
	 * @param g2d contexte graphique
	 * @param mat matrice de transformation monde-vers-composant
	 * @param hauteur hauteur du monde reelle
	 * @param largeur largeur du monde reelle
	 * 
	 */
	public void dessiner(Graphics2D g, AffineTransform mat, double hauteur, double largeur) {
		// TODO Auto-generated method stub
		
	}

}
