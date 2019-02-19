package pistolet;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import geometrie.Vecteur;
import interfaces.Dessinable;

public class Pistolet extends JPanel implements Dessinable  {

	/**
	 * Create the panel.
	 */
	Rectangle2D bloc;
	Rectangle2D sol;
	Line2D axe;
	
	private int LARGEUR_PISTOLET = 70;
	
	Vecteur positionIni;
	private Vecteur position = new Vecteur(0.0, 0.0);

	
	
	
	
	public Pistolet() {

	}





	@Override
	public void dessiner(Graphics2D g, AffineTransform mat, int hauteur) {
		
		bloc = new Rectangle2D.Double(getWidth()-LARGEUR_PISTOLET/2, getHeight()-LARGEUR_PISTOLET/2, LARGEUR_PISTOLET, LARGEUR_PISTOLET/2);
		
		
	}

	
	
}
