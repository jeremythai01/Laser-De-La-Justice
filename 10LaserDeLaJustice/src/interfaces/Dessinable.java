package interfaces;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public interface Dessinable {
	public void dessiner (Graphics2D g, AffineTransform mat, double hauteur, double largeur);
	
}