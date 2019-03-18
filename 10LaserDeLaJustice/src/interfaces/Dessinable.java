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
	 * Cette m�thode sera implementer dans les classes des objets qui devront �tre dessiner
	 * @param g: c'est le param�tre Graphics2D qui permettra de dessiner les objets
	 * @param mat: c'est la matrice qui permettra de transformer le monde pixel en monde monde r�el
	 * @param hauteur: c'est la hauteur en pixel de la scene
	 * @param largeur: C'est la largeure en pixel de la scene
	 */
	public void dessiner (Graphics2D g, AffineTransform mat, double hauteur, double largeur);
}
