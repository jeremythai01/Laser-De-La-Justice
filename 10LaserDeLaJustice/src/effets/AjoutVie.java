package effets;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
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

public class AjoutVie extends Pouvoir {

	public AjoutVie ( Vecteur position , Vecteur accel) {
		super(position,accel);
		lireImage();
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
	public void dessiner(Graphics2D g, AffineTransform mat, double hauteur, double largeur) {
		g.drawImage(getImg(),(int)getPosition().getX(),(int) getPosition().getY(), null);
	}

	@Override
	public Area getAire() {
		setRectFantome(new Rectangle2D.Double(getPosition().getX(), getPosition().getY(), getImg().getWidth(null), getImg().getHeight(null)));
		return new Area(getRectFantome());
	}



	@Override
	public void activeEffet(Scene scene, Coeurs coeurs, ArrayList<Balle> listeBalles, Personnage perso, double tempsEcoule) {
		coeurs.setCombien(coeurs.getCombien()+1);
		
	}

	@Override
	public void retireEffet() {
		// TODO Auto-generated method stub
		
	}
}
