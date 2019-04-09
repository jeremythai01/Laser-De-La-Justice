package objets;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import geometrie.Vecteur;
import interfaces.Dessinable;
import physique.Balle;
import physique.Laser;

/**
 * classe qui permet la cration d'un ordinateur de plusieurs niveaux de talent
 *@author Arnaud Lefebvre
 */
public class OrdinateurNiveau3 implements Dessinable, Runnable {

	private double largeurOrdi=1;
	private double longueurOrdi=1;
	private double vitesseNiveau2=0.6;
	//private int talent;
	private Rectangle2D.Double forme;
	private Vecteur position;
	private boolean enCoursAnimation=false;
	private double hauteurDuMonde;
	private ArrayList<Balle> listeBalle = new ArrayList<Balle>();
	private ArrayList<Vecteur> listeDistance = new ArrayList<Vecteur>();
	private ArrayList<Vecteur> listePosition = new ArrayList<Vecteur>();
	double angle;
	Laser test;
	Balle balleSimuler/*= new Balle()*/;
	private double vitesseLaser=1.5;
	private boolean enCollision=false;
	private double temps;
	
	
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
		//System.out.println((int)Math.random()*3+1);
		if((Math.floor(Math.random() * (2)) + 1)%2==0) {
			//vitesseNiveau2=-vitesseNiveau2;
		}
	}

	public Laser tirer() {
		angle=calculerAngleTir(savoirViser(listeBalle));
		calculerBalleAViser();
		//System.out.println(savoirViser(listeBalle)+" anglecxczc");
		return (new Laser(new Vecteur(getPositionX()+getLargeurOrdi()/2,hauteurDuMonde-getLongueurOrdi()), angle, new Vecteur(0,vitesseLaser)));
		//return (new Laser(new Vecteur(getPositionX()+getLargeurOrdi()/2,hauteurDuMonde-getLongueurOrdi()), angleAleatoire(), new Vecteur(0,0.5)));

	}

	public int angleAleatoire() {
		return (int) (Math.floor(Math.random() * (75 - 45 +1)) + 45);
	}

	public void ajouterListesObstacles(ArrayList<Balle> listeBalle) {
		this.listeBalle=new ArrayList<Balle>(listeBalle);

	}

	public Vecteur savoirViser(ArrayList<Balle> listeBalle) {
		if(listeBalle.size()>0) {
			//System.out.println("je suis dans le if");
			for(Balle balle: listeBalle) {
				listeDistance.add(new Vecteur((balle.getPosition().soustrait(getPosition()).getX()),(balle.getPosition().soustrait(getPosition()).getY())));
			}

			int avant=0;
			Vecteur reponse= listeDistance.get(avant);
			for(int i =1; i<listeDistance.size();i++) {
				if(listeDistance.get(i).module()<listeDistance.get(avant).module()) {
					reponse=listeDistance.get(i);
				}
				avant++;
			}
			listeDistance.clear();
			//	System.out.println(reponse+" allo");
			return reponse;
		}else
			return new Vecteur(0,1);


	}

	public void calculerBalleAViser() {
		Balle balleAViser;
		if(listeBalle.size()>0) {
			//System.out.println("je suis dans le if");
			for(Balle balle: listeBalle) {
				listeDistance.add(new Vecteur((balle.getPosition().soustrait(getPosition()).getX()),(balle.getPosition().soustrait(getPosition()).getY())));

			}

			int avant=0;
			balleAViser=new Balle(listeBalle.get(avant));
			Vecteur reponse= listeDistance.get(avant);
			for(int i =1; i<listeDistance.size();i++) {
				if(listeDistance.get(i).module()<listeDistance.get(avant).module()) {
					balleAViser= new Balle(listeBalle.get(i));
					reponse=listeDistance.get(i);

				}
				avant++;

			}listeDistance.clear();
			//System.out.println("position de la vrai balle "+balleAViser.getPosition());
			simulerMouvementBalle(balleAViser, reponse);



		}

		//return new Vecteur(0,0);
	}



	//doit on dessiner un objet pour quil ait une aire 


	public void savoirTempsSleep(double temps) {
		this.temps=temps;
	}
	
	public void simulerMouvementBalle(Balle viser, Vecteur distance) {
		double deltaT;
		balleSimuler= new Balle(viser);

		//balleSimuler.unPasEuler(0.5);
		//balleSimuler.unPasEuler(0.7);
		/// tu dois trouver un meilleur delta t( calculer la distance en y et trouver le temps que ca prendrait
		deltaT=-distance.getY()/(vitesseLaser/(temps/1000));
		System.out.println("delta t= "+ deltaT);
		//deltaT=-distance.getY()/25;
		//System.out.println(deltaT+"hwhwhwh");
		balleSimuler.unPasEuler(deltaT);
		
		//	balleSimuler.unPasEuler(deltaT);



		//System.out.println("position de la balle simulee "+balleSimuler.getPosition());
		//double angleAViser=calculerAngleTir(distance);
		double angleAViser=10;
		test= new Laser(new Vecteur(getPositionX()+getLargeurOrdi()/2,hauteurDuMonde-getLongueurOrdi()), angleAViser, new Vecteur(0,vitesseLaser));
		simulerMouvementLaser(test);
		for(int i=0;i<170; i++) {
			//System.out.println("je suis cici");
			//	balleSimuler.unPasEuler(deltaT);

			if(!verifierCollisionBalleEtLaserSimulation(balleSimuler, test)) {
				//if(enCollision==false) {
				angleAViser+=1;



			}
			else {
				System.out.println("les simules se sont touches");
				//System.exit(0);
				break;
			}
			//System.out.println("angle a viser dans el for="+ angleAViser);
			//System.out.println("position du laser simule" + test.getPositionHaut());
			test=new Laser(new Vecteur(getPositionX()+getLargeurOrdi()/2,hauteurDuMonde-getLongueurOrdi()), angleAViser, new Vecteur(0,vitesseLaser));
			simulerMouvementLaser(test);
		}
		//System.out.println("angle a viser ="+ angleAViser);
		//int pls = (int) (angleAViser/180);
		//System.out.println("pls = "+ pls);
		angle=angleAViser;
		System.out.println("angle a viser" + angleAViser);
		//angle=angleAViser+210;
	}

	public void simulerMouvementLaser(Laser laser) {

		while(laser.getPositionHaut().getY()>balleSimuler.getPosition().getY()+balleSimuler.getDiametre()/2)	{

			//laser.unPasEuler(0.01);
			laser.move();
			//System.out.println("position de la balle live"+ balleSimuler.getPosition());
			//System.out.println("position du laser "+laser.getPositionHaut());
		}
	}	

	public void essaiMouvementBalle(Balle balle) {
		double	positionBalleX=balle.getVitesse().getX()*0.8;//dans 0.8secondes
		double	positionBalleY=balle.getVitesse().getY()*0.8+0.5*-9.8*0.8*0.8;
	}

	public boolean verifierCollisionBalleEtLaserSimulation(Balle balle, Laser laser) {
		if(intersection(balle.getAire(), laser.getAire())) {
			return true;

		}else

			return false;
	}

	public void verifierCollisionBalleEtLaserSimulation() {
		if(intersection(balleSimuler.getAire(), test.getAire())) {//[pour ce soir, il reste a trouver un moyen de toujours verifier si le laser simule rentre en contact avec la balle
			enCollision=true;

		}else {

			enCollision=false;
		}


	}

	private boolean intersection(Area aire1, Area aire2) {
		Area aireInter = new Area(aire1);
		aireInter.intersect(aire2);
		if(!aireInter.isEmpty()) {
			return true;
		}
		return false;
	}

	public double calculerAngleTir(Vecteur distance) {
		//System.out.println("la distance a faire est de " + distance);
		//System.out.println("calcuer "+  Math.toDegrees(Math.atan(((-distance.getY()/distance.getX())))));
		double resultat= Math.atan(((-distance.getY()/distance.getX()))); // si jamais le calcul donne la valeur negative

		if(resultat<0) {
			System.out.println(90.0+90.0+Math.toDegrees(resultat));
			return 90.0+90.0+Math.toDegrees(resultat);
		}else
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

	@Override
	public void run() {


	}
}
