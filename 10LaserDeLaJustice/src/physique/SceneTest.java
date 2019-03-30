package physique;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JPanel;

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
	private double deltaT = 0.01;
	private  double LARGEUR_DU_MONDE = 30; //en metres
	private  double HAUTEUR_DU_MONDE;
	private boolean enCoursAnimation= false;
	private double tempsTotalEcoule = 0;
	private double diametre = 2;  //em mètres
	private ArrayList<Balle> listeBalles = new ArrayList<Balle>();

	private boolean premiereFois = true;
	private ModeleAffichage modele;
	private AffineTransform mat;
	private Vecteur vitesse;
	private Personnage personnage1;
	private Personnage personnage2;
	private Personnage personnage3;

	private double angle;
	private ArrayList<Laser> listeLasers = new ArrayList<Laser>();

	private int toucheGauche = 37;
	private int toucheDroite = 39;
	private int toucheTir = 32;

	private ArrayList<Mur> listeMurs = new ArrayList<Mur>();

	private ArrayList<Personnage> listePerso = new ArrayList<Personnage>();

	private Mur mur;


	private double xSouris ;
	
	/**
	 * Create the panel.
	 */
	public SceneTest() {

		personnage1 = new Personnage(LARGEUR_DU_MONDE/2 -5, toucheGauche, toucheDroite,toucheTir,  "JOUEUR1");
		listePerso.add(personnage1);
		personnage2 = new Personnage(LARGEUR_DU_MONDE/2 + 5, toucheGauche, toucheDroite,toucheTir, "JOUEUR2");
		listePerso.add(personnage2);


		personnage3 = new Personnage(LARGEUR_DU_MONDE/2, toucheGauche, toucheDroite,toucheTir,  "JOUEUR1");
		personnage3.setModeSouris(true);
		angle = 90;
		vitesse = new Vecteur(0.5 ,0);

		
		mur = new Mur ( new Vecteur(0,5), LARGEUR_DU_MONDE, 0.5 , 0, "HORIZONTAL");
		listeMurs.add(mur);
		mur = new Mur ( new Vecteur(0,10), LARGEUR_DU_MONDE, 0.5 , 0, "HORIZONTAL");
		listeMurs.add(mur);

		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				//debut

				//fin
			}
		});




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
					Balle balle = new Balle(new Vecteur(eXR-diametre/2, eYR-diametre/2),vitesse, "LARGE" );
					listeBalles.add(balle);
				}

				shootEtAddLaser(e, personnage3);

				repaint();
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {

				personnage3.relacheTouche();

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

		for(Laser laser : listeLasers) { 

			if(laser.getLigneFinY() <= 0 ) 
				listeLasers.remove(laser);
		}

		for(Laser laser : listeLasers) {
			laser.dessiner(g2d, mat, 0, 0);
		}


		for(Mur mur : listeMurs) {
			mur.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		}

		collisionBalleLaser( listeBalles,  listeLasers);


		for(Balle balle: listeBalles) {

			balle.dessiner(g2d,mat,HAUTEUR_DU_MONDE,LARGEUR_DU_MONDE);
		}

		
		for(Personnage perso : listePerso) {
			perso.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE );
		}

		 
		personnage3.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE );
	}//fin paintComponent



	private void calculerUneIterationPhysique() {

		for (int i = 0; i < listeBalles.size(); i++) {
			Balle balle1 = listeBalles.get(i);
			double n = 0;
			if (balle1.getType().toString().equals("SMALL"))
				System.out.println("balle rouge   = "+ balle1.isPremiereCollision() );

			for (int j = i+1; j < listeBalles.size(); j++) {
				
				Balle balle2 = listeBalles.get(j);

				if(intersection(balle1.getAireBalle(), balle2.getAireBalle())) 
					n++;
			}
			if(n ==0) 
				balle1.setPremiereCollision(false);
		}

		for (int i = 0; i < listeBalles.size(); i++) {

			Balle balle1 = listeBalles.get(i);

			if(balle1.isPremiereCollision() == false) {

				for (int j = i+1; j < listeBalles.size(); j++) {

					Balle balle2 = listeBalles.get(j);
					if (balle2.isPremiereCollision() == false) 
						MoteurPhysique.detectionCollisionBalles(balle1, balle2);
				}
			}
		}



		collisionBalleMur(listeBalles, listeMurs);

		for(Balle balle: listeBalles) {
			balle.unPasVerlet(deltaT);
		}

		for(Laser laser : listeLasers) {
			laser.move();
		}
		
		for(Personnage perso : listePerso) {
			perso.bouge();
		}
		 
		personnage3.bouge();
		tempsTotalEcoule += deltaT;
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
		// TODO Auto-generated method stub
		while (enCoursAnimation) {	
			calculerUneIterationPhysique();
			repaint();
			try {
				Thread.sleep(tempsDuSleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}//fin while
		System.out.println("Le thread est mort...");
	}




	private void collisionBalleLaser(ArrayList<Balle> listeBalles, ArrayList<Laser> listeLasers) {

		for(Laser laser : listeLasers) {
			if(listeBalles.size()>0) {
				for(Balle balle : listeBalles ) {
					if(intersection(balle.getAireBalle(), laser.getLaserAire())) {
						listeLasers.remove(laser);   
						balle.shrink(listeBalles);
					}
				}
			}
		}
		/*for(Laser laser : listeLasers) {
			for(Balle balle : listeBalles ) {
				if(intersection(balle.getAireBalle(), laser.getLaserAire())) {
					listeLasers.remove(laser);   
					balle.shrink(listeBalles);
				}	


			}
		}*/
	}


	private void collisionBalleMur(ArrayList<Balle> listeBalles, ArrayList<Mur> listeMurs) {
		for(Mur mur : listeMurs) {
			for(Balle balle : listeBalles ) {
				if(intersection(balle.getAireBalle(), mur.getAireMur())) {
					MoteurPhysique.collisionMurBalle(balle,mur);
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


	private void shootEtAddLaser(KeyEvent e, Personnage perso) {
		if(enCoursAnimation == true) {
			int code = e.getKeyCode();
			if(code == perso.getToucheTir()) {
				perso.neBougePas();
				listeLasers.add(
						new Laser(new Vecteur(
								perso.getPositionX()+perso.getLARGEUR_PERSO()/2,HAUTEUR_DU_MONDE-perso.getLONGUEUR_PERSO()) , angle, new Vecteur(0, 1 )));
			}
		}

	}

	private void shootEtAddLaser(MouseEvent e, Personnage perso) {
		if(perso.isModeSouris()) {
			if(enCoursAnimation == true) {
				perso.neBougePas();
				listeLasers.add(
						new Laser(new Vecteur(
								perso.getPositionX()+perso.getLARGEUR_PERSO()/2,HAUTEUR_DU_MONDE-perso.getLONGUEUR_PERSO()) , angle, new Vecteur(0, 1 )));
			}
		}
	}
}



