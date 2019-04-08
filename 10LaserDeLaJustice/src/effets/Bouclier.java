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

public class Bouclier extends Pouvoir  {

	private Personnage perso;
	private Boolean effetActive = false;
	private double posX;
	private double posY;
	public Bouclier ( Vecteur position , Vecteur accel) {
		super(position, accel);
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
	public void dessiner(Graphics2D g2d, AffineTransform mat, double hauteur, double largeur) {
		AffineTransform matLocale = new AffineTransform(mat);
		
		 posX = getPosition().getX() * matLocale.getScaleX();
		 posY = getPosition().getY() * matLocale.getScaleY();

		g2d.drawImage(getImg(),(int)posX,(int) posY, null);
		Rectangle2D.Double y = new Rectangle2D.Double(posX, posY, getImg().getWidth(null), getImg().getHeight(null));
		g2d.draw(y);
		
		if(effetActive) {
	
			Ellipse2D.Double ellipse = new Ellipse2D.Double(perso.getPositionX(),hauteur-perso.getLONGUEUR_PERSO() , perso.getLARGEUR_PERSO(), perso.getLONGUEUR_PERSO());
			g2d.setColor(new Color(1.0f, 1.0f, 0.5f, 0.6f ));	
			g2d.fill(matLocale.createTransformedShape(ellipse));
			g2d.setColor(new Color(0.0f, 1.0f, 0.2f, 0.9f ));	
			g2d.draw(matLocale.createTransformedShape(ellipse)); // Contour vert 
		}
	}

	@Override
	public Area getAire() {
		setRectFantome(new Rectangle2D.Double(posX, posY, getImg().getWidth(null), getImg().getHeight(null))); // probleme de detection
		return new Area(getRectFantome());
	}

	public void activeEffet(Scene scene, Coeurs coeurs, ArrayList<Balle> listeBalles,Personnage perso, double tempsEcoule) {
		this.perso = perso;
		this.effetActive = true;
		perso.setTempsInvincible(tempsEcoule + 6);
	}

	@Override
	public void retireEffet() {
		// TODO Auto-generated method stub
		this.effetActive = false;
	}



}
