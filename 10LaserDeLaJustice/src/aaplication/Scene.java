package aaplication;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import personnage.Personnage;
import pistolet.Pistolet;

public class Scene extends JPanel {

	private Personnage principal;
	private Pistolet pistoletPrincipal;
	private AffineTransform mat;
	private int HAUTEUR=0;
	
	public Scene() {
		principal = new Personnage ();
		pistoletPrincipal= new Pistolet();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.yellow);
		principal.dessiner(g2d, mat, HAUTEUR);
		pistoletPrincipal.dessiner(g2d, mat, HAUTEUR);
	}

}
