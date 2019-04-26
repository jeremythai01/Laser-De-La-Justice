package physique;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

/**
 * @author Jeremy Thai
 */

public class BarreEnergie extends JPanel  {
	private Rectangle2D.Double rect1, rectVide;
	private int largeurForme=50;
	private double x=0;
	private double y=0;
	private int longueurForme=50;
	private double pourcentageEnergie;
	private double energie, energieMecanique;


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
		//g2d.rotate(Math.toRadians(180), x,y);
		//g2d.translate(x+largeurForme, y+ 150);
		rect1  = new Rectangle2D.Double(x, y, largeurForme,longueurForme);
		g2d.fill(rect1);
		g2d.setTransform(mat);
		
		g2d.setColor(Color.BLACK);
		g2d.drawString((int)energie+" J", largeurForme/4- largeurForme/8,longueurForme/2 + 15);
	
		
		
	}


	public double getEnergie() {
		return energie;
	}

	public double getEnergieMecanique() {
		return energieMecanique;
	}

	public void setEnergies(double energie, double energieMecanique) {
		this.energie = energie;
		this.energieMecanique = energieMecanique;

	}


}