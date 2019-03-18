package pistolet;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import geometrie.Vecteur;
import interfaces.Dessinable;
/**
 * 
 * @author Arezki Issaadi
 *
 */
public class Pistolet extends JPanel implements Dessinable {

	/**
	 * Create the panel.
	 */
	private Rectangle2D bloc;
	private Rectangle2D gachette;
	private Rectangle2D boutPistolet;
	
	private int LARGEUR_PISTOLET = 70;
	private double angle;

	public Pistolet() {
		angle = 90.00;
	}

	@Override
	/**
	 * Cette méthode permet de dessiner les objets
	 * @param g: c'est le paramètre Graphics2D qui permettra de dessiner les objets
	 * @param mat: c'est la matrice qui permettra de transformer le monde pixel en monde monde réel
	 * @param hauteur: c'est la hauteur en pixel de la scene
	 * @param largeur: C'est la largeure en pixel de la scene
	 */
	public void dessiner(Graphics2D g, AffineTransform mat, double hauteur, double largeur) {

		bloc = new Rectangle2D.Double(200 - LARGEUR_PISTOLET / 2, 200 - LARGEUR_PISTOLET / 2,
				LARGEUR_PISTOLET/2, LARGEUR_PISTOLET );
		
		gachette = new Rectangle2D.Double(200-LARGEUR_PISTOLET/2, 200+LARGEUR_PISTOLET/4, LARGEUR_PISTOLET, LARGEUR_PISTOLET/3.5); 
		
		boutPistolet = new Rectangle2D.Double(200-LARGEUR_PISTOLET/2.5, 200-LARGEUR_PISTOLET, LARGEUR_PISTOLET/3.5, LARGEUR_PISTOLET/2);
		g.scale(0.2, 0.2);
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(3));
		g.fill(gachette);
		g.fill(boutPistolet);
		
		g.draw(bloc);
		
	}
	/**
	 * 
	 * @return la valeur de l'angle du pistolet
	 */
	public double getAngle() {
		return angle;
	}
	/**
	 * 
	 * @param angle: C'est la nouvelle valeur de l'angle du pistolet
	 */
	public void setAngle(double angle) {
		this.angle = angle;
	}

	

	
}
