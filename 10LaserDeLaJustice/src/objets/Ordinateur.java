package objets;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import geometrie.Vecteur;
import interfaces.Dessinable;
import physique.Laser;

/**
 * classe qui permet la création d'un ordinateur de plusieurs niveaux de talent
 *@author Arnaud Lefebvre
 */
public class Ordinateur implements Dessinable {
	
	private double largeurOrdi=1;
	private double longueurOrdi=1;
	private double vitesse=0.2;
	private Rectangle2D.Double forme;
	private Vecteur position;
	private ArrayList<Laser> listeLasers = new ArrayList<Laser>();
	private double hauteurDuMonde;
	
	/**
	 * Constructeur de l'ordinateur qui prend en parametre la position 
	 * @param position, la position de l'ordinateur
	 */
	public Ordinateur(int talent, Vecteur position) {
		//talent(talent);
		this.position=position;
	}
	//if talent==2
	/*private void talent(int talent) {
		switch(talent) {
		
		
		case 1: 	;
		break;
		case 2: ;
		break;
		case 3: ;
		break;
		}
		
	}*/

	@Override
	/**
	 * Permet de dessiner l'ordinateur selon le contexte graphique en parametre.
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
		
	}

	/**
	 * Methode qui permet de faire deplacer l'ordinateur selon sa vitesse
	 */
	public void bouge() {
		
		if(position.getX()+vitesse>45) {
			vitesse=-vitesse;
		}
		if(position.getX()+vitesse<5) {
			vitesse=-vitesse;
		}
		
		
		
		position.setX(position.getX()+vitesse);
		
	}
	
	/**
	 * Methode qui tire un laser selon la position et l'angle de l'ordi
	 * @return, un laser selon la position et l'angle de tir
	 */
	public Laser tirer() {
		return (new Laser(new Vecteur(getPositionX()+getLargeurOrdi()/2,hauteurDuMonde-getLongueurOrdi()), angleAleatoire(), new Vecteur(0,0.5)));
		
	}
	/**
	 * Methode qui permet de calculer un angle de tir aleatoire
	 * @return, un angle aleatoire
	 */
	private int angleAleatoire() {
		return (int) (Math.floor(Math.random() * (180 - 1 +1)) + 1);
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
