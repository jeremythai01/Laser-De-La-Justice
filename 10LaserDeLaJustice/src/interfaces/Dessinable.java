package interfaces;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
/**
 * 
 * @author Arezki
 *
 */
public interface Dessinable {
	
	/**
	 * Cette méthode sera implementer dans les classes des objets qui devront être dessiner.
	 * Elle permet d'alleger le paintComponent dans la scene principale.
	 * @param g: c'est le paramètre Graphics2D qui permettra de dessiner les objets
	 * @param mat: c'est la matrice qui permettra de transformer le monde pixel en monde monde réel
	 * @param hauteur: c'est la hauteur en réel de la scene
	 * @param largeur: C'est la largeure en réel de la scene
	 */
	public void dessiner (Graphics2D g, AffineTransform mat, double hauteur, double largeur);
}
