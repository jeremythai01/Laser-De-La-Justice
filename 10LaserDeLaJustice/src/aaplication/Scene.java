package aaplication;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import personnage.Personnage;

public class Scene extends JPanel {

	private Personnage principal;
	
	
	public Scene() {
		principal = new Personnage ();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.yellow);
		principal.dessiner(g2d);
	}

}
