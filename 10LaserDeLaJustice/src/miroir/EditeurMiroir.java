package miroir;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;

import geometrie.Vecteur;
import utilite.ModeleAffichage;

public class EditeurMiroir extends JPanel {
	MiroirPlan miroirPlan = new MiroirPlan(new Vecteur (0.5,0.5), 45);

	/**
	 * Create the panel.
	 */
	public EditeurMiroir() {
		setBackground(Color.yellow);
		
		
		

	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;	
		ModeleAffichage modele = new ModeleAffichage(getWidth(),getHeight(),5);
		AffineTransform mat = modele.getMatMC();
		
		g2d.setColor(Color.red);
		g2d.draw(mat.createTransformedShape(new Ellipse2D.Double(0.5,0.5,1,1)));
		g2d.translate(20, 100);
		miroirPlan.dessiner(g2d, mat, 0, 0);
		
		
	}

}
