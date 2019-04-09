package objets;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import geometrie.Vecteur;
import interfaces.Dessinable;

public class Spirale implements Dessinable {
	
	
	int large;
	int longueur;
	Vecteur position;
	
	public Spirale(Vecteur position, int large, int longueur) {
		this.position=position;
		this.large=large;
		this.longueur=longueur;
	}

	@Override
	public void dessiner(Graphics2D g, AffineTransform mat, double hauteur, double largeur) {
		 int x = 60;
	        int y = 60;
	        int width = 1;
	        int height = 1;
	        int startAngle = 0;
	        int arcAngle = 180;
	        int depth = 3;
	        for (int i = 0; i < 10; i++) {
	            if (i % 2 == 0) {
	                //   g.drawArc(x + 10, y + 10, width, height, startAngle + 10, -arcAngle);
	                //  x = x - 5;
	                y = y - depth;
	                width = width + 2 * depth;
	                height = height + 2 * depth;
	                g.drawArc(x, y, width, height, startAngle, -arcAngle);
	            } else {
	                //  g.drawArc(x + 10, y + 10, width, height, startAngle + 10, arcAngle);
	                x = x - 2 * depth;
	                y = y - depth;
	                width = width + 2 * depth;
	                height = height + 2 * depth;
	                g.drawArc(x, y, width, height, startAngle, arcAngle);
	            }
	        }
		
	}

}
