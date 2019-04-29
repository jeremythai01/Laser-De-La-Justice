package pouvoir;

import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import aaplication.Scene;
import geometrie.Vecteur;


/**
 * Classe qui créée un pouvoir de bouclier et mémorise sa position, son accélération,son image et ses dimensions.
 * @author Arnaud Lefebvre 
 */

public class BoostVitesse extends Pouvoir {


	/**
	 * Constructeur ou la position et l'acceleration initiales sont spécifiés qui appelle le constructeur de la classe ascendante et qui modifie la largeur et la longueur de l'image du pouvoir.
	 * @param position Vecteur incluant les positions en x et y du coin superieur-gauche
	 * @param accel Vecteur incluant les accelerations en x et y  
	 */
	public BoostVitesse ( Vecteur position , Vecteur accel) {
		super(position,accel);
		lireImage();
		setLargeurImg(2);
		setLongueurImg(3);
	}


	/**
	 * Methode qui permet d'associer une image a l'objet
	 */
	@Override
	public void lireImage() {
		
		setImg(Toolkit.getDefaultToolkit().createImage(getClass().getClassLoader().getResource("boost.gif")));
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
	public void dessiner(Graphics2D g2d, AffineTransform mat, double hauteur, double largeur) {

		AffineTransform matLocale = new AffineTransform(mat);
		double factX = getLargeurImg()/ getImg().getWidth(null) ;
		double factY = getLongueurImg()/ getImg().getHeight(null) ;
		matLocale.scale( factX, factY);
		matLocale.translate( getPosition().getX() / factX ,  getPosition().getY() / factY);
		g2d.drawImage(getImg(), matLocale, null);
	}

	/**
	 * Methode qui permet de retourner l'aire de l'objet
	 * @return l'aire de l'objet sous forme de area
	 */
	@Override
	public Area getAire() {
		setRectFantome(new Rectangle2D.Double(getPosition().getX(), getPosition().getY(), getLargeurImg(), getLongueurImg())); // probleme de detection
		return new Area(getRectFantome());
	}

	/**
	 * Methode qui active l'effet de l'objet lorsque appelé
	 */
	@Override
	public void activeEffet(Scene scene) {
		scene.setVitesseLaser(scene.getVitesseLaser().additionne(new Vecteur(0,3)));
		scene.getPersonnage().setEnVitesse(true);
	}

}
