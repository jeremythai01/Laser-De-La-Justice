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
	private double vitesseNiveau1=0.2;
	private double vitesseNiveau2=0.3;
	//private int talent;
	private Rectangle2D.Double forme;
	private Vecteur position;
	private boolean enCoursAnimation=false;
	private ArrayList<Laser> listeLasers = new ArrayList<Laser>();
	private double hauteurDuMonde;
	
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
	public void dessiner(Graphics2D g, AffineTransform mat, double hauteur, double largeur) {
		AffineTransform matLocal = new AffineTransform(mat);
		forme = new Rectangle2D.Double(position.getX(), position.getY(), largeurOrdi, longueurOrdi);
		g.fill(matLocal.createTransformedShape(forme));
		hauteurDuMonde=hauteur;
		
	}

	
	public void bouge() {
		
		if(position.getX()+vitesseNiveau1>45) {
			vitesseNiveau1=-vitesseNiveau1;
		}
		if(position.getX()+vitesseNiveau1<5) {
			vitesseNiveau1=-vitesseNiveau1;
		}
		
		
		
		position.setX(position.getX()+vitesseNiveau1);
		
	}
	
	public Laser tirer() {
		return (new Laser(new Vecteur(getPositionX()+getLargeurOrdi()/2,hauteurDuMonde-getLongueurOrdi()), angleAleatoire(), new Vecteur(0,0.5)));
		
	}
	private int angleAleatoire() {
		return (int) (Math.floor(Math.random() * (180 - 1 +1)) + 1);
	}

	public double getVitesseNiveau1() {
		return vitesseNiveau1;
	}

	public void setVitesseNiveau1(double vitesseNiveau1) {
		this.vitesseNiveau1 = vitesseNiveau1;
	}

	public double getVitesseNiveau2() {
		return vitesseNiveau2;
	}

	public void setVitesseNiveau2(double vitesseNiveau2) {
		this.vitesseNiveau2 = vitesseNiveau2;
	}

	public Vecteur getPosition() {
		return position;
	}
	public double getPositionX() {
		return position.getX();
	}

	public void setPosition(Vecteur position) {
		this.position = position;
	}

	public boolean isEnCoursAnimation() {
		return enCoursAnimation;
	}

	public void setEnCoursAnimation(boolean enCoursAnimation) {
		this.enCoursAnimation = enCoursAnimation;
	}

	public double getLargeurOrdi() {
		return largeurOrdi;
	}

	public double getLongueurOrdi() {
		return longueurOrdi;
	}
}
