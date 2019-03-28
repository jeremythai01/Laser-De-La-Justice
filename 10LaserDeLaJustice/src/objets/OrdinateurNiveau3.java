package objets;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import geometrie.Vecteur;
import interfaces.Dessinable;
import physique.Balle;
import physique.Laser;

/**
 * classe qui permet la création d'un ordinateur de plusieurs niveaux de talent
 *@author Arnaud Lefebvre
 */
public class OrdinateurNiveau3 implements Dessinable {
	
	private double largeurOrdi=1;
	private double longueurOrdi=1;
	private double vitesseNiveau2=0.6;
	//private int talent;
	private Rectangle2D.Double forme;
	private Vecteur position;
	private boolean enCoursAnimation=false;
	private double hauteurDuMonde;
	private ArrayList<Balle> listeBalle = new ArrayList<Balle>();
	private ArrayList<Vecteur> listePosition = new ArrayList<Vecteur>();
	
	public OrdinateurNiveau3(Vecteur position) {
		this.position=position;
	}

	@Override
	public void dessiner(Graphics2D g, AffineTransform mat, double hauteur, double largeur) {
		AffineTransform matLocal = new AffineTransform(mat);
		forme = new Rectangle2D.Double(position.getX(), position.getY(), largeurOrdi, longueurOrdi);
		g.fill(matLocal.createTransformedShape(forme));
		hauteurDuMonde=hauteur;
	}

	
	public void bouge() {
		
		if(position.getX()+vitesseNiveau2>45) {
			vitesseNiveau2=-vitesseNiveau2;
		}
		if(position.getX()+vitesseNiveau2<5) {
			vitesseNiveau2=-vitesseNiveau2;
		}
		
		
		
		position.setX(position.getX()+vitesseNiveau2);
		changerVitesse();
		
	}
	
	public void changerVitesse() { 
		System.out.println((int)Math.random()*3+1);
		if((Math.floor(Math.random() * (2)) + 1)%2==0) {
			//vitesseNiveau2=-vitesseNiveau2;
		}
	}
	
	public Laser tirer() {
		double angle=calculerAngleTir(savoirViser(listeBalle));
		System.out.println(savoirViser(listeBalle)+" angle");
		return (new Laser(new Vecteur(getPositionX()+getLargeurOrdi()/2,hauteurDuMonde-getLongueurOrdi()), angle, new Vecteur(0,0.5)));
		//return (new Laser(new Vecteur(getPositionX()+getLargeurOrdi()/2,hauteurDuMonde-getLongueurOrdi()), angleAleatoire(), new Vecteur(0,0.5)));

	}
	
	public int angleAleatoire() {
		return (int) (Math.floor(Math.random() * (75 - 45 +1)) + 45);
	}
	
	public void ajouterListesObstacles(ArrayList<Balle> listeBalle) {
		this.listeBalle=listeBalle;
		
	}
	
	public Vecteur savoirViser(ArrayList<Balle> listeBalle) {
		if(listeBalle.size()>0) {
			//System.out.println("je suis dans le if");
			for(Balle balle: listeBalle) {
				listePosition.add(new Vecteur((balle.getPosition().soustrait(getPosition()).getX()),(balle.getPosition().soustrait(getPosition()).getY())));
			//	listePosition.add(new Vecteur(10,10));
			}

			int avant=0;
			Vecteur reponse= listePosition.get(avant);
			for(int i =1; i<listePosition.size();i++) {
				if(listePosition.get(i).module()<listePosition.get(avant).module()) {
					reponse=listePosition.get(i);
				}
				avant++;
			}
		//	System.out.println(reponse+" allo");
			return reponse;
		}else
			return new Vecteur(1,1);

		
	}
	public double calculerAngleTir(Vecteur distance) {
		//System.out.println("la distance a faire est de " + distance);
		//System.out.println("calcuer "+  Math.toDegrees(Math.atan(((-distance.getY()/distance.getX())))));
		double resultat= Math.atan(((-distance.getY()/distance.getX()))); // si jamais le calcul donne la valeur negative
		if(resultat<0) {
			resultat=-resultat;
		}
		return Math.toDegrees(resultat);
	}
	
	public double getVitesseNiveau1() {
		return vitesseNiveau2;
	}

	public void setVitesseNiveau1(double vitesseNiveau1) {
		this.vitesseNiveau2 = vitesseNiveau1;
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
