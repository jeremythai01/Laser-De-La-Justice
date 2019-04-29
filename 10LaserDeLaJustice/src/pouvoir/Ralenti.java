package pouvoir;

import java.awt.Graphics2D;
import java.awt.Toolkit;
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
import physique.Laser;
import physique.SceneTest;

/**
 * Classe qui créée un pouvoir de bouclier et mémorise sa position, son accélération,son image et ses dimensions.
 * @author Jeremy Thai 
 */

public class Ralenti extends Pouvoir{

	/**
	 * Constructeur ou la position et l'acceleration initiales sont spécifiés qui appelle le constructeur de la classe ascendante et qui modifie la largeur et la longueur de l'image du pouvoir.
	 * @param position Vecteur incluant les positions en x et y du coin superieur-gauche
	 * @param accel Vecteur incluant les accelerations en x et y  
	 */
	public Ralenti ( Vecteur position, Vecteur accel) {
		super(position, accel);
		lireImage();
		setLargeurImg(2);
		setLongueurImg(3.6);
	}

	
	@Override
	public void lireImage() {
	
		setImg(Toolkit.getDefaultToolkit().createImage(getClass().getClassLoader().getResource("sablier.gif")));

	}
	
	/**
	 * Permet de dessiner le pouvoir selon le contexte graphique en parametre.
	 * @param g2d contexte graphique
	 * @param mat matrice de transformation monde-vers-composant
	 * @param hauteur hauteur du monde reelle
	 * @param largeur largeur du monde reelle
	 * 
	 */
	@Override
	public void dessiner(Graphics2D g, AffineTransform mat, double hauteur, double largeur) {

		AffineTransform matLocale = new AffineTransform(mat);

		double factX = getLargeurImg()/ getImg().getWidth(null) ;
		double factY = getLongueurImg()/ getImg().getHeight(null) ;
		matLocale.scale( factX, factY);

		matLocale.translate( getPosition().getX() / factX ,  getPosition().getY() / factY);

		g.drawImage(getImg(), matLocale, null);
	}

	@Override
	public Area getAire() {
		setRectFantome(new Rectangle2D.Double(getPosition().getX(), getPosition().getY(), getLargeurImg(), getLongueurImg())); // probleme de detection
		return new Area(getRectFantome());
	}

	@Override
	public void activeEffet( Scene scene) {
		scene.setDeltaT(0.0006);
	}






}
