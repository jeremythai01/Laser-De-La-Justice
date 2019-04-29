package objets;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import geometrie.Vecteur;
import interfaces.Dessinable;
import physique.Balle;
import physique.Laser;

/**
 * classe qui permet la cration d'un ordinateur de talent 3
 *@author Arnaud Lefebvre
 */
public class OrdinateurNiveau3 implements Dessinable {

	private double largeurOrdi=1;
	private double longueurOrdi=1;
	private double vitesse=0.6;
	//private int talent;
	private Rectangle2D.Double forme;
	private Vecteur position;
	private boolean enCoursAnimation=false;
	private double hauteurDuMonde;
	private ArrayList<Balle> listeBalle = new ArrayList<Balle>();
	private ArrayList<Vecteur> listeDistance = new ArrayList<Vecteur>();
	private ArrayList<Vecteur> listePosition = new ArrayList<Vecteur>();
	private double angle;
	private Laser test;
	private Balle balleSimuler/*= new Balle()*/;
	private double vitesseLaser=1;
	private boolean enCollision=false;
	private double temps;
	private Image img=null;
	private double largeurMonde=0;
	
	/**
	 * Constructeur de l'ordinateurNiveau3 qui prend en parametre la position 
	 * @param position, la position de l'ordinateurNiveau3
	 */
	public OrdinateurNiveau3(Vecteur position) {
		this.position=position;
		lireImage();
	}
	
	public void lireImage() {
		URL urlCoeur = getClass().getClassLoader().getResource("ordi.jpg");
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
	 * Methode qui permet de faire deplacer l'ordinateurNiveau3 selon sa vitesse
	 */
	public void bouge() {

		if(position.getX()+vitesse>largeurMonde-1) {
			vitesse=-vitesse;
		}
		if(position.getX()+vitesse<1) {
			vitesse=-vitesse;
		}



		position.setX(position.getX()+vitesse);
		changerVitesse();

	}

	/**
	 * Methode qui change le sens de la vitesse du laser 
	 */
	public void changerVitesse() { 
		//System.out.println((int)Math.random()*3+1);
		if((Math.floor(Math.random() * (2)) + 1)%2==0) {
			//vitesseNiveau2=-vitesseNiveau2;
		}
	}

	/**
	 * Methode qui tire un laser selon la position et l'angle de l'ordiNiveau3
	 * @return, un laser selon la position et l'angle de tir
	 */
	public Laser tirer() {
		angle=calculerAngleTir(savoirViser(listeBalle));
		calculerBalleAViser();
		//System.out.println(savoirViser(listeBalle)+" anglecxczc");
		return (new Laser(new Vecteur(getPositionX()+getLargeurOrdi()/2,hauteurDuMonde-getLongueurOrdi()), angle, new Vecteur(0,vitesseLaser)));
		//return (new Laser(new Vecteur(getPositionX()+getLargeurOrdi()/2,hauteurDuMonde-getLongueurOrdi()), angleAleatoire(), new Vecteur(0,0.5)));

	}

	/**
	 * Methode qui permet de calculer un angle de tir aleatoire
	 * @return, un angle aleatoire
	 */
	public int angleAleatoire() {
		return (int) (Math.floor(Math.random() * (75 - 45 +1)) + 45);
	}

	/**
	 * Methode qui permet a lordi de connaitre les balles(position, vitesse, etc de chaque balle)
	 * @param listeBalle, la liste des balles
	 */
	public void ajouterListesObstacles(ArrayList<Balle> listeBalle) {
		this.listeBalle=new ArrayList<Balle>(listeBalle);

	}

	/**
	 * Methode qui retourne la plus petite distance entre l'ordi et une balle parmi toutes les balles
	 * @param listeBalle, la liste de toutes les balles
	 * @return un vecteur qui indique la distance entre l'ordi et la plus proche balle
	 */
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
	
	/**
	 * Methode qui permet a l'ordi de savoir  quel balle 
	 */
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


	/**
	 * Methode qui permet a l'ordi de savoir le temps de sleep de la scene
	 * @param temps, le temps de sleep de la scene
	 */
	public void savoirTempsSleep(double temps) {
		this.temps=temps;
	}
	
	/**
	 * Methode qui simule le mouvement de la balle et qui lance un laser a chaque degre pour savoir a quel orientation lancer le laser pour entrer en collision avec la balle en mouvement
	 * @param viser, la balle a viser
	 * @param distance, la distance entre la balle a viser et l'ordi
	 */
	public void simulerMouvementBalle(Balle viser, Vecteur distance) {
		double deltaT;
		balleSimuler= new Balle(viser);

		//balleSimuler.unPasVerlet(0.55);
		//balleSimuler.unPasVerlet(0.7);
		/// tu dois trouver un meilleur delta t( calculer la distance en y et trouver le temps que ca prendrait
		deltaT=-distance.getY()/(vitesseLaser/(temps/1000));
		//System.out.println("delta t= "+ deltaT);
		//deltaT=-distance.getY()/25;
		//System.out.println(deltaT+"hwhwhwh");
		//balleSimuler.unPasEuler(deltaT);
		
			balleSimuler.unPasVerlet(deltaT);



		//System.out.println("position de la balle simulee "+balleSimuler.getPosition());
		//double angleAViser=calculerAngleTir(distance);
		double angleAViser=1;
		test= new Laser(new Vecteur(getPositionX()+getLargeurOrdi()/2,hauteurDuMonde-getLongueurOrdi()), angleAViser, new Vecteur(0,vitesseLaser));
		simulerMouvementLaser(test);
		for(int i=0;i<179; i++) {
			//System.out.println("je suis cici");
			//	balleSimuler.unPasEuler(deltaT);

			if(!verifierCollisionBalleEtLaserSimulation(balleSimuler, test)) {
				//if(enCollision==false) {
				angleAViser+=1;



			}
			else {
				System.out.println("les simules se sont touches");
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
		angle=angleAViser+1;
		System.out.println("angle a viser" + angleAViser);
		//angle=angleAViser+210;
	}

	/**
	 * Methode qui permet de simuler un laser 
	 * @param laser, le laser a simuler
	 */
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

	/**
	 * Methode qui permet de savori si la balle simulee et le laser simule se sont touches
	 * @param balle, la balle simulee
	 * @param laser, le laser simule
	 * @return vrai  ou faux, si la balle et le laser se sont touches
	 */
	public boolean verifierCollisionBalleEtLaserSimulation(Balle balle, Laser laser) {
		if(intersection(balle.getAire(), laser.getAire())) {
			return true;

		}else

			return false;
	}

	/**
	 * Methode qui permet de savori si la balle simulee et le laser simule se sont touches
	 * @return vrai  ou faux, si la balle et le laser se sont touches
	 */
	public void verifierCollisionBalleEtLaserSimulation() {
		if(intersection(balleSimuler.getAire(), test.getAire())) {//[pour ce soir, il reste a trouver un moyen de toujours verifier si le laser simule rentre en contact avec la balle
			enCollision=true;

		}else {

			enCollision=false;
		}


	}

	/**
	 * Methode qui permet de detecter une intersection entre deux aires
	 * @param aire1, la premiere aire
	 * @param aire2, la deuxieme aire
	 * @return vrai ou faux si elles se sont touchees
	 */
	private boolean intersection(Area aire1, Area aire2) {
		Area aireInter = new Area(aire1);
		aireInter.intersect(aire2);
		if(!aireInter.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * Methode qui permet de calculer l'angle de tir a laide de la distance entre l'ordi et la balle
	 * @param distance, la distance entre l'ordi et la balle
	 * @return l'angle a viser
	 */
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

	


	/**
	 * Methode qui permet de savoir la position de l'ordi
	 * @return position, la position de lordi en vecteur
	 */
	public Vecteur getPosition() {
		return position;
	}
	/**
	 * Methode qui permet de savoir la position de l'ordi en x
	 * @return position, la position de lordi en x en vecteur
	 */
	public double getPositionX() {
		return position.getX();
	}

	/**
	 * Methode qui permet de modifier la position de l'ordi 
	 * @param position, la nouvelle position
	 */
	public void setPosition(Vecteur position) {
		this.position = position;
	}

	/**
	 * Methode qui permet de savoir la largeur de l'ordi
	 * @return largeurOrdi, la largeur de l'ordi
	 */
	public double getLargeurOrdi() {
		return largeurOrdi;
	}

	/**
	 * Methode qui permet de savoir la longueur de l'ordi
	 * @return longueurOrdi, la longueur de l'ordi
	 */
	public double getLongueurOrdi() {
		return longueurOrdi;
	}


}
