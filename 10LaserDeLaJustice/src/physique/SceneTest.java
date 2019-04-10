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
import effets.AjoutVie;
import effets.BoostVitesse;
import effets.Bouclier;
import effets.Pouvoir;
import geometrie.Vecteur;
import interfaces.SceneListener;

import objets.TrouNoir;
import personnage.Personnage;
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
	//private double deltaT = 0.006;
	private double deltaT = 0.06;
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
	private Personnage personnage1;
	private Personnage personnage2;
	private Personnage personnage3;

	
	private Vecteur gravite = new Vecteur(0, 9.8);
	
	private double angle;
	private ArrayList<Laser> listeLasers = new ArrayList<Laser>();

	private int toucheGauche = 37;
	private int toucheDroite = 39;
	private int toucheTir = 32;

	private ArrayList<Mur> listeMurs = new ArrayList<Mur>();

	private ArrayList<Personnage> listePerso = new ArrayList<Personnage>();

	private Mur mur;

	private Coeurs coeurs;

	private double xSouris ;

	private ArrayList<Pouvoir> listePouvoirs = new ArrayList<Pouvoir>();
	private Vecteur vitesseLaserInit = new Vecteur(0,1);
	private Vecteur vitesseLaser = vitesseLaserInit;
	
	
	private int compteurVitesse = 0 ;
	private boolean vitessePerso = false;
	/**
	 * Create the panel.
	 */
	public SceneTest() {

		vitesseLaser= new Vecteur(0, 1 );
		personnage1 = new Personnage(LARGEUR_DU_MONDE/2 -5, toucheGauche, toucheDroite,toucheTir);
		listePerso.add(personnage1);
		//	personnage2 = new Personnage(LARGEUR_DU_MONDE/2 + 5, toucheGauche, toucheDroite,toucheTir);
		//	listePerso.add(personnage2);


		personnage3 = new Personnage(LARGEUR_DU_MONDE/2, toucheGauche, toucheDroite,toucheTir);
		personnage3.setModeSouris(true);
		listePerso.add(personnage3);
		angle = 90;
		vitesse = new Vecteur(0.5 ,0);


		mur = new Mur ( new Vecteur(0,0), 0, HAUTEUR_DU_MONDE  );
		listeMurs.add(mur);
		mur = new Mur ( new Vecteur(0,HAUTEUR_DU_MONDE ), LARGEUR_DU_MONDE, 0 );
		listeMurs.add(mur);
		mur = new Mur ( new Vecteur(LARGEUR_DU_MONDE,0 ), 0 , HAUTEUR_DU_MONDE);
		listeMurs.add(mur);


		addMouseMotionListener(new MouseMotionAdapter() {


			@Override
			public void mouseMoved(MouseEvent e) {
				//debut

				xSouris= e.getX()/modele.getPixelsParUniteX();
				personnage3.setPosSouris(xSouris);
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
					perso.relacheTouche();
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
		}

		
		collisionPouvoirsPersonnages();
		
		g2d.setStroke(new BasicStroke(3));

		for(Laser laser : listeLasers) { 

			if(laser.getPositionHaut().getY() <= 0 ) 
				listeLasers.remove(laser);
		}

		for(Laser laser : listeLasers) {
			laser.dessiner(g2d, mat, 0, 0);
		}


		for(Mur mur : listeMurs) {
			mur.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
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
				
		collisionMurBalle(  listeBalles, listeMurs );
		
		
		for (int i = 0; i < listeBalles.size(); i++) {
			Balle balle1 = listeBalles.get(i);
			for (int j = i+1; j < listeBalles.size(); j++) {
				Balle balle2 = listeBalles.get(j);
				MoteurPhysique.detectionCollisionBalles(balle1, balle2);
			}
		}

		detectionCollisionBallePersonnage( listeBalles, listePerso );

	
		for(Pouvoir pouvoir : listePouvoirs) {
			pouvoir.unPasVerlet(deltaT);
		}


		for(Balle balle: listeBalles) {
			balle.unPasVerlet(deltaT);
		}

		for(Laser laser : listeLasers) {
			laser.move();
		}

		for(Personnage perso : listePerso) {
			if(vitessePerso == true){
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
			
			calculerUneIterationPhysique();
			
			
			if(vitesseLaser.getY()>vitesseLaserInit.getY()) 
				compteurVitesse++;
			if( compteurVitesse == 168 ) {
				vitesseLaser = vitesseLaserInit;
				compteurVitesse = 0;
				setVitessePerso(false);
			}
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
						Vecteur position = new Vecteur(balle.getPosition().getX(), balle.getPosition().getY());
						Vecteur accel =   new Vecteur ( 0, 9.8);
						listePouvoirs.add(new AjoutVie( position, accel));
						balle.shrink(listeBalles, accel);
					}
				}
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

	private void collisionPouvoirsPersonnages() {
		
		for(Pouvoir pouvoir : listePouvoirs) {
			for(Personnage perso : listePerso ) {
				if(intersection(pouvoir.getAire(), perso.getAire() )) {
						pouvoir.activeEffet(  listeLasers, this, coeurs, listeBalles ,perso, tempsEcoule);
						listePouvoirs.remove(pouvoir);
					}
				}
			}	
	}

	private void shootEtAddLaser(KeyEvent e, Personnage perso) {
		if(enCoursAnimation == true) {
			int code = e.getKeyCode();
			if(code == perso.getToucheTir()) {
				perso.neBougePas();
				listeLasers.add(
						new Laser(new Vecteur(
								perso.getPositionX()+perso.getLARGEUR_PERSO()/2,HAUTEUR_DU_MONDE-perso.getLONGUEUR_PERSO()) , angle, vitesseLaser));
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
			}
		}

	}

	private void shootEtAddLaser(MouseEvent e, Personnage perso) {
		if(perso.isModeSouris()) {
			if(enCoursAnimation == true) {
				perso.neBougePas();
				listeLasers.add(
						new Laser(new Vecteur(
								perso.getPositionX()+perso.getLARGEUR_PERSO()/2,HAUTEUR_DU_MONDE-perso.getLONGUEUR_PERSO()) , angle, vitesseLaser));
			}
		}
	}

	private void collisionMurBalle( ArrayList<Balle> listeBalles, ArrayList<Mur> listeMurs ) {

		for(Mur mur : listeMurs) {
			for(Balle balle : listeBalles ) {
				if(intersection(balle.getAire(), mur.getAire() )) {
				}
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

}



