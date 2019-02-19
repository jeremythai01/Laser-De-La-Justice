package personnage;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import interfaces.Dessinable;

public class Personnage implements Dessinable {
	private Image imgPerso = null;
	private double positionX = 100;
	private double positonY = 50;
	

	
	public Personnage() {
		URL fich = getClass().getClassLoader().getResource("narutoDebout.png");
		if (fich == null) {
			JOptionPane.showMessageDialog(null, "Fichier Fnaruto_debout.jpg introuvable!");
		} else {
			try {
				imgPerso = ImageIO.read(fich);
			} catch (IOException e) {
				System.out.println("Erreur de lecture du fichier d'image");
			}
		}

		
	}

	public void dessiner(Graphics2D g2d, AffineTransform mat, int hauteur) {
		g2d.drawImage(imgPerso, (int)positionX,(int) positonY, null);
		
	}
}
