package physique;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import aaplication.Scene;
import geometrie.Vecteur;
import interfaces.SceneListener;

import objets.TrouNoir;
import personnage.Personnage;
import pouvoir.AjoutVie;
import pouvoir.BoostVitesse;
import pouvoir.Bouclier;
import pouvoir.Pouvoir;
import pouvoir.Ralenti;
import son.Bruit;
import utilite.ModeleAffichage;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;



/**
 * 
 * @author Jeremy et Arnaud( A verifier )
 *
 */
public class SceneTest extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	private int tempsDuSleep = 30;

	private  double LARGEUR_DU_MONDE = 30; //en metres
	private  double HAUTEUR_DU_MONDE;
	private boolean enCoursAnimation= false;
	private double tempsEcoule = 0;
	private double diametre = 2;  //em mètres
	private ArrayList<Balle> listeBalles = new ArrayList<Balle>();

	private boolean premiereFois = true;
	private ModeleAffichage modele;
	private AffineTransform mat;
	private Vecteur vitesse;
	private Personnage personnage;


	private Vecteur gravite = new Vecteur(0, 9.8);

	private double angle;
	private ArrayList<Laser> listeLasers = new ArrayList<Laser>();

	private int toucheGauche = 37;
	private int toucheDroite = 39;
	private int toucheTir = 32;


	private ArrayList<Personnage> listePerso = new ArrayList<Personnage>();

	private Coeurs coeurs;

	private double xSouris ;




	private double deltaTInit = 0.05;
	private double deltaT = deltaTInit;
	private ArrayList<Pouvoir> listePouvoirs = new ArrayList<Pouvoir>();
	private Vecteur vitesseLaserInit = new Vecteur(0,1);
	private Vecteur vitesseLaser = vitesseLaserInit;
	private double compteurVitesse = 0 ;
	private double compteurRalenti= 0 ;
	private double compteurBouclier= 0 ;
	private boolean vitessePerso = false;
	private double qtRotation;

	private Bruit son = new Bruit();
	/**
	 * Create the panel.
	 */
	public SceneTest() {

		vitesseLaser= new Vecteur(0, 1 );
		//	personnage1 = new Personnage(LARGEUR_DU_MONDE/2 -5, toucheGauche, toucheDroite,toucheTir);
		//	listePerso.add(personnage1);
		//	personnage2 = new Personnage(LARGEUR_DU_MONDE/2 + 5, toucheGauche, toucheDroite,toucheTir);
		//	listePerso.add(personnage2);


		personnage = new Personnage(LARGEUR_DU_MONDE/2, toucheGauche, toucheDroite,toucheTir);
		personnage.setModeSouris(true);
		listePerso.add(personnage);
		angle = 90;
		vitesse = new Vecteur(0.5 ,0);

		addMouseMotionListener(new MouseMotionAdapter() {


			@Override
			public void mouseMoved(MouseEvent e) {
				//debut

				xSouris= e.getX()/modele.getPixelsParUniteX();
				personnage.setPosSouris(xSouris);
				repaint();
				//fin

			}
		});






		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

				double eXR = e.getX()/modele.getPixelsParUniteX();
				double eYR = e.getY()/modele.getPixelsParUniteY();

				if(!enCoursAnimation) {
					Balle balle = new Balle(new Vecteur(eXR-diametre/2, eYR-diametre/2),vitesse, "LARGE" , gravite);
					listeBalles.add(balle);
				}

				for(Personnage perso : listePerso) {
					shootEtAddLaser(e, perso);
				}


				repaint();
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
				for(Personnage perso : listePerso) {
					perso.relacheToucheEnModeSouris();
				}
				repaint();
			}
		});

		addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				for(Personnage perso : listePerso) {
					perso.deplacerLePersoSelonTouche( e );
					shootEtAddLaser(e, perso);
				}
				repaint();
			}
			@Override
			public void keyReleased(KeyEvent e) {
				for(Personnage perso : listePerso) {
					perso.relacheTouche(e);

				}
				repaint();
			}
		});
	}
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;	
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

		if(premiereFois) {
			modele = new ModeleAffichage(getWidth(),getHeight(),LARGEUR_DU_MONDE);
			mat = modele.getMatMC();
			HAUTEUR_DU_MONDE = modele.getHautUnitesReelles() ;
			premiereFois = false;
			Balle.setModele(getWidth(),getHeight(),LARGEUR_DU_MONDE);
			
		}

		collisionPouvoirsPersonnages();
		
		for(Laser laser : listeLasers) { 

			if(laser.getPositionHaut().getY() <= 0 ) 
				listeLasers.remove(laser);
		}

		for(Laser laser : listeLasers) {
			laser.dessiner(g2d, mat, 0, 0);
		}


		collisionBalleLaser();
	
		
		for(Balle balle: listeBalles) {
			balle.dessiner(g2d,mat,HAUTEUR_DU_MONDE,LARGEUR_DU_MONDE);
		}


		for(Personnage perso : listePerso) {
			perso.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE );
		}

		for(Pouvoir pouvoir : listePouvoirs) {
			pouvoir.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		}

	}//fin paintComponent



	private void calculerUneIterationPhysique() {

		
		//collisionMurBalle(  listeBalles, listeMurs );


		for (int i = 0; i < listeBalles.size(); i++) {
			Balle balle1 = listeBalles.get(i);
			for (int j = i+1; j < listeBalles.size(); j++) {
				Balle balle2 = listeBalles.get(j);
				MoteurPhysique.detectionCollisionBalles(balle1, balle2);
			}
		}

		detectionCollisionBallePersonnage( listeBalles, listePerso );


		updateMouvementPouvoirs();

		for(Balle balle: listeBalles) {
			balle.unPasVerlet(deltaT);
		}

		detectionCollisionMurBalle();
		for(Laser laser : listeLasers) {
			laser.move();
		}

		for(Personnage perso : listePerso) {
			if(vitessePerso == true){
				perso.bouge();
				perso.bouge();
				perso.bouge();
				perso.bouge();
			}else {
				perso.bouge();
			}

		}
		tempsEcoule += deltaT;
	}



	public void arreter( ) {
		if(enCoursAnimation)
			enCoursAnimation = false;
	}

	public void demarrer() {
		if (!enCoursAnimation) { 
			Thread proc = new Thread(this);
			proc.start();
			enCoursAnimation = true;
		}

	}

	@Override
	public void run() {
		while (enCoursAnimation) {	
			son.joueMusique("themesong");
			calculerUneIterationPhysique();
			updateDureeCompteurs();
			repaint();
			try {
				Thread.sleep(tempsDuSleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}//fin while
		System.out.println("Le thread est mort...");
	}




	private void collisionBalleLaser() {

		for(Laser laser : listeLasers) {
			if(listeBalles.size()>0) {
				for(Balle balle : listeBalles ) {
					if(intersection(balle.getAire(), laser.getAire())) {
						listeLasers.remove(laser);  
						pouvoirAuHasard(balle);
						balle.shrink(listeBalles, gravite);
					}
				}
			}
		}
	}

	private void pouvoirAuHasard(Balle balle ) {

		Vecteur position = new Vecteur(balle.getPosition().getX(), balle.getPosition().getY());
		Vecteur accel =  new Vecteur(gravite);

		int nb = 0 + (int)(Math.random() * ((10 - 0) + 1));

		Pouvoir pouvoir;
		switch(nb) {

		case 1: 
			pouvoir = new BoostVitesse( position, accel);
			pouvoir.setCompteurAvantDisparaitre(tempsEcoule + 7);
			listePouvoirs.add(pouvoir);
			break;

		case 2: 
			pouvoir = new Bouclier( position, accel);
			pouvoir.setCompteurAvantDisparaitre(tempsEcoule + 7);
			listePouvoirs.add(pouvoir);
			break;

		case 3: 
			pouvoir = new Ralenti( position, accel);
			pouvoir.setCompteurAvantDisparaitre(tempsEcoule + 7);
			listePouvoirs.add(pouvoir);
			break;

		case 4: 
			pouvoir = new AjoutVie( position, accel);
			pouvoir.setCompteurAvantDisparaitre(tempsEcoule + 7);
			listePouvoirs.add(pouvoir);
			break;
		}
	}


	private void ajoutCompteurs() {

		if(vitesseLaser.getY()>vitesseLaserInit.getY()) 
			compteurVitesse = tempsEcoule + 5;

		if( deltaT < deltaTInit) 
			compteurRalenti = tempsEcoule + 5;

		if(personnage.isBouclierActive())
			compteurBouclier = tempsEcoule + 9;
	}


	private void updateDureeCompteurs() {

		for(Balle balle : listeBalles) {
			balle.updateRotation();
		}
		for (Pouvoir pouvoir : listePouvoirs) {
			if( pouvoir.getCompteurAvantDisparaitre() <= tempsEcoule)
				listePouvoirs.remove(pouvoir);
		}

		if( compteurVitesse <= tempsEcoule ) {
			vitesseLaser = vitesseLaserInit;
			compteurVitesse = 0;
			vitessePerso = false;
		}

		if( compteurRalenti <= tempsEcoule ) {
			deltaT = deltaTInit;
			compteurRalenti = 0;
		}

		if(compteurBouclier <= tempsEcoule)
			personnage.setBouclierActive(false);
	}


	private void collisionPouvoirsPersonnages() {

		for(Pouvoir pouvoir : listePouvoirs) {
			for(Personnage perso : listePerso ) {
				if(intersection(pouvoir.getAire(), perso.getAire() )) {
					//	pouvoir.activeEffet(this);
					ajoutCompteurs();
					listePouvoirs.remove(pouvoir);
				}
			}
		}	
	}

	private void updateMouvementPouvoirs() {
		for(Pouvoir pouvoir: listePouvoirs) {
			if(pouvoir.getPosition().getY()+pouvoir.getLongueurImg() >= HAUTEUR_DU_MONDE) {
				pouvoir.getPosition().setY(HAUTEUR_DU_MONDE-pouvoir.getLongueurImg());
			}else {
				pouvoir.unPasVerlet(deltaT);
			}

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





	private void shootEtAddLaser(KeyEvent e, Personnage perso) {
		if(!perso.isModeSouris()) {
			if(enCoursAnimation == true) {
				int code = e.getKeyCode();
				if(code == perso.getToucheTir()) {
					perso.neBougePas();
					listeLasers.add(
							new Laser(new Vecteur(
									perso.getPosition()+perso.getLARGEUR_PERSO()/2,HAUTEUR_DU_MONDE-perso.getLONGUEUR_PERSO()) , angle, vitesseLaser));
				}
			}
		}
	}

	private void detectionCollisionBallePersonnage(ArrayList<Balle> listeBalles, ArrayList<Personnage> listePerso ) {

		for (Balle balle : listeBalles){
			for(Personnage perso: listePerso)
				if (intersection(balle.getAire(), perso.getAire())) {
					if (perso.isBouclierActive()) {
						perso.setBouclierActive(false);
					}
					son.joue("decision");
				}
		}
		
	}

	private void shootEtAddLaser(MouseEvent e, Personnage perso) {
		if(perso.isModeSouris()) {
			if(enCoursAnimation == true) {
				perso.neBougePas();
				listeLasers.add(
						new Laser(new Vecteur(
								perso.getPosition()+perso.getLARGEUR_PERSO()/2,HAUTEUR_DU_MONDE-perso.getLONGUEUR_PERSO()) , angle, vitesseLaser));
				son.joue("tir");
			}
		}
	}


	public void setVitesseLaser(Vecteur vitesseLaser) {
		this.vitesseLaser = vitesseLaser;
	}


	public Vecteur getVitesseLaser() {
		return vitesseLaser;
	}

	public void setVitessePerso(boolean vitessePerso) {
		this.vitessePerso = vitessePerso;
	}

	public Personnage getPersonnage() {
		return personnage;
	}

	public Coeurs getCoeurs() {
		return coeurs;
	}

	public void setDeltaT(double deltaT) {
		this.deltaT = deltaT;
	}

	/**
	 *Evalue une collision avec le sol ou un mur et modifie la vitesse courante selon la collision
	 * @param width largeur du monde 
	 * @param height hauteur  du monde 
	 */

	private void detectionCollisionMurBalle() {

		for(Balle balle : listeBalles) {

			Vecteur position = new Vecteur(balle.getPosition());
			double diametre = balle.getDiametre();

			if(position.getY()+diametre >= HAUTEUR_DU_MONDE) { // touche le sol 
				balle.getVitesse().setY(-balle.getVitesse().getY());
				
			}

			if(position.getX()+diametre >= LARGEUR_DU_MONDE || position.getX() <= 0) {
				balle.getVitesse().setX(-balle.getVitesse().getX());

			}
		}

	}

}



