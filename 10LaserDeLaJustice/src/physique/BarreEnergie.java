package physique;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

/**
 * @author Guillaume Bavota
 */

public class BarreEnergie extends JPanel  {
	private Rectangle2D.Double rect1, rectVide;
	private int largeurForme=50;
	private double x=0;
	private double y=0;
	private int longueurForme1=50;
	private double pourcentageEnergie;
	private double energie, energieMecanique;


	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;


		pourcentageEnergie= energie/energieMecanique;
		longueurForme1=(int) (150*pourcentageEnergie);

			rectVide  = new Rectangle2D.Double(x, y, largeurForme, 150);
			    g2d.setColor(Color.gray);
		    g2d.fill(rectVide);

		g2d.setColor(Color.BLUE);
		rect1  = new Rectangle2D.Double(x, y, largeurForme,longueurForme1);
		g2d.fill(rect1);

		g2d.setColor(Color.BLACK);
		g2d.drawString(Math.round(pourcentageEnergie*100)+" %", largeurForme/4,longueurForme1/2 + 15);
		g2d.drawString(" Barre (energie)", largeurForme, longueurForme1/2 + 15);

	}


	public double getEnergie() {
		return energie;
	}


	public void setEnergie(double energie) {
		this.energie = energie;
	}


	public double getEnergieMecanique() {
		return energieMecanique;
	}


	public void setEnergieMecanique(double energieTotal) {
		this.energieMecanique = energieTotal;
	}



}