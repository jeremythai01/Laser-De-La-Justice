package physique;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

/**
 * Classe qui permet de créer les barres de progression des énergies utilisés par les balles. 
 * @author Jérémy Thai
 *
 */
public class BarreEnergie extends JPanel  {
	
	private Rectangle2D.Double rect1, rectVide;
	private int largeurForme=70;
	private double x=0;
	private double y=0;
	private int longueurForme=50;
	private double pourcentageEnergie;
	private double energie, energieMecanique;

	
/**
 * 	Méthode qui permet de dessiner la barre de progression
 * @param g contexte graphique  
 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		AffineTransform mat = g2d.getTransform();

		pourcentageEnergie= energie/energieMecanique;
		longueurForme=(int) (150*pourcentageEnergie);

		rectVide  = new Rectangle2D.Double(x, y, largeurForme, 150);
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.fill(rectVide);

		g2d.setColor(Color.GREEN);
		rect1  = new Rectangle2D.Double(x, y, largeurForme,longueurForme);
		g2d.fill(rect1);
		
		
		g2d.setColor(Color.BLACK);
		g2d.drawString((int)energie+"J", largeurForme/4  - largeurForme/5,longueurForme/2 + 15);
		g2d.drawString("("+ Math.round(pourcentageEnergie*100)+"%)", largeurForme/4- largeurForme/8,longueurForme/2 + 30);
		
		g2d.setTransform(mat);
	}
	
	public void setEnergies(double energie, double energieMecanique) {
		this.energie = energie;
		this.energieMecanique = energieMecanique;
	}


}