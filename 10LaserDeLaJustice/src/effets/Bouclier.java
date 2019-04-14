package effets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import aaplication.Scene;
import geometrie.Vecteur;
import personnage.Personnage;
import physique.Balle;
import physique.Coeurs;
import physique.Laser;
import physique.SceneTest;

public class Bouclier extends Pouvoir  {

	public Bouclier ( Vecteur position , Vecteur accel) {
		super(position, accel);
		lireImage();
		setLargeurImg(1.0);
		setLongueurImg(1.6);
	}

	@Override
	public void lireImage() {

		URL fich = getClass().getClassLoader().getResource("narutoDebout.png");
		if (fich == null) {
			JOptionPane.showMessageDialog(null, "Fichier narutoDebout.jpg introuvable!");
		} else {
			try {
				setImg(ImageIO.read(fich));
			} catch (IOException e) {
				System.out.println("Erreur de lecture du fichier d'image du BoostVitesse");
			}
		}

	}
	
	@Override
	
	public void dessiner(Graphics2D g2d, AffineTransform mat, double hauteur, double largeur) {
		
		AffineTransform matLocale = new AffineTransform(mat);
		
		double factX = getLargeurImg()/ getImg().getWidth(null) ;
		double factY = getLongueurImg()/ getImg().getHeight(null) ;
		matLocale.scale( factX, factY);
	
		
		matLocale.translate( getPosition().getX() / factX ,  getPosition().getY() / factY);
		
		g2d.drawImage(getImg(), matLocale, null);
	
	}

	@Override
	public Area getAire() {
		setRectFantome(new Rectangle2D.Double(getPosition().getX(), getPosition().getY(), getLargeurImg(), getLongueurImg())); // probleme de detection
		return new Area(getRectFantome());
	}

	@Override
	public void activeEffet(Scene scene) {
		scene.getPersonnage().setBouclierActive(true);
		
	}
	

	



}
