package objets;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import geometrie.Vecteur;
import interfaces.Dessinable;
import physique.Laser;

/**
 * classe qui permet la création d'un ordinateur de talent 2
 *@author Arnaud Lefebvre
 */
public class OrdinateurNiveau2 implements Dessinable {
	
	private double largeurOrdi=2;
	private double longueurOrdi=2;
	private double vitesse=0.6;
	private Rectangle2D.Double forme;
	private Vecteur position;
	private double hauteurDuMonde;
	private Image img=null;
	private double largeurMonde=0;
	
	/**
	 * Constructeur de l'ordinateurNiveau2 qui prend en parametre la position 
	 * @param position, la position de l'ordinateurNiveau2
	 */
	public OrdinateurNiveau2(Vecteur position) {
		lireImage();
		this.position=position;
	}
	
	/**
	 * Methode qui permet de lire une image
	 */
	public void lireImage() {
		URL urlCoeur = getClass().getClassLoader().getResource("niveau2.png");
		if (urlCoeur == null) {
			JOptionPane.showMessageDialog(null , "Fichier coeur.png introuvable");
			System.exit(0);}
		try {
			img = ImageIO.read(urlCoeur);
		}
		catch (IOException e) {
			System.out.println("Erreur pendant la lecture du fichier d'image");
		}
	}
	
	@Override
	/**
	 * Permet de dessiner l'ordinateurNiveau2 selon le contexte graphique en parametre.
	 * @param g contexte graphique
	 * @param mat matrice de transformation monde-vers-composant
	 * @param hauteur hauteur du monde reelle
	 * @param largeur largeur du monde reelle
	 */
	public void dessiner(Graphics2D g, AffineTransform mat, double hauteur, double largeur) {
		AffineTransform matLocal = new AffineTransform(mat);
		forme = new Rectangle2D.Double(position.getX(), position.getY(), largeurOrdi, longueurOrdi);
		g.fill(matLocal.createTransformedShape(forme));
		hauteurDuMonde=hauteur;
		
		double factX = largeurOrdi/ img.getWidth(null) ;
		double factY = largeurOrdi/ img.getHeight(null) ;
		matLocal.scale( factX, factY);
		matLocal.translate( getPosition().getX() / factX ,  getPosition().getY() / factY);
		g.drawImage(img, matLocal, null);
			
		largeurMonde=largeur;
	}

	/**
	 * Methode qui permet de faire deplacer l'ordinateurNiveau2 selon sa vitesse
	 */
	public void bouge() {
		
		if(position.getX()+vitesse>largeurMonde-largeurOrdi) {
			vitesse=-vitesse;
		}
		if(position.getX()+vitesse<5) {
			vitesse=-vitesse;
		}
		
		
		
		position.setX(position.getX()+vitesse);
		
	}
	
	
	
	/**
	 * Methode qui tire un laser selon la position et l'angle de l'ordiNiveau2
	 * @return, un laser selon la position et l'angle de tir
	 */
	public Laser tirer() {
		return (new Laser(new Vecteur(getPositionX()+getLargeurOrdi()/2,hauteurDuMonde-getLongueurOrdi()), angleAleatoire(), new Vecteur(0,0.5)));

	}
	
	/**
	 * Methode qui permet de calculer un angle de tir aleatoire
	 * @return, un angle aleatoire
	 */
	public int angleAleatoire() {
		return (int) (Math.floor(Math.random() * (125 - 45 +1)) + 45);
	}
	
	/**
	 * Retourne la vitesse X
	 * @return, la vitesse en X
	 */
	public double getVitesse() {
		return vitesse;
	}

	/**
	 * Modifie la vitesse de l'ordi
	 * @param vitesse, la position de l'ordi qu'on veut modifier
	 */
	public void setVitesse(double vitesse) {
		this.vitesse = vitesse;
	}


	/**
	 * Retourne la position
	 * @return, la position en  vecteur
	 */
	public Vecteur getPosition() {
		return position;
	}
	
	/**
	 * Retourne la position en X
	 * @return, la position en X en double
	 */
	public double getPositionX() {
		return position.getX();
	}

	
	/**
	 * Modifie la position de l'ordi
	 * @param position, la position qu'on veut modifier
	 */
	public void setPosition(Vecteur position) {
		this.position = position;
	}

	/**
	 * Retourne la largeur de l'ordinateur
	 * @return, la largeur en  double
	 */
	public double getLargeurOrdi() {
		return largeurOrdi;
	}

	/**
	 * Retourne la longueur
	 * @return, la longueur en  double
	 */
	public double getLongueurOrdi() {
		return longueurOrdi;
	}
}
