package objets;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.util.ArrayList;

import javax.swing.JPanel;

import geometrie.Vecteur;
import miroir.MiroirConcave;
import physique.Personnage;
import physique.Balle;
import physique.Coeurs;
import physique.Laser;
import utilite.ModeleAffichage;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;




public class SceneTestPls extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	private int tempsDuSleep = 25;
	private double deltaT = 0.07;
	private  double LARGEUR_DU_MONDE = 50; //en metres
	private  double HAUTEUR_DU_MONDE;
	private boolean enCoursAnimation= false;
	private double tempsTotalEcoule = 0;
	private double masse = 15; //en kg
	private double diametre = 2;  //em m�tres
	private ArrayList<Balle> listeBalles = new ArrayList<Balle>();
	private Balle balle1;
	private Balle balle;
	private boolean premiereFois = true;
	private ModeleAffichage modele;
	private AffineTransform mat;

	private Vecteur position;

	private Vecteur vitesse;

	private Vecteur gravity ;

	private Personnage character;

	private double angle;
	private ArrayList<Laser> listeLasers = new ArrayList<Laser>();
	
	
	private BlocDEau bloc;
	private ArrayList<BlocDEau> listeBloc = new ArrayList<BlocDEau>();
	private TrouNoir trou;
	private ArrayList<TrouNoir> listeTrou = new ArrayList<TrouNoir>();



	/**
	 * Create the panel.
	 */
	public SceneTestPls() {

		angle = -90;
		character = new Personnage();

		position = new Vecteur(0.3, 10);

		vitesse = new Vecteur(3 ,0);

		gravity = new Vecteur(0,9.8);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

				double eXR = e.getX()/modele.getPixelsParUniteX();
				double eYR = e.getY()/modele.getPixelsParUniteY();
				balle = new Balle(new Vecteur(eXR-diametre/2, eYR-diametre/2),vitesse, "LARGE" );
				listeBalles.add(balle);
				bloc= new BlocDEau(new Vecteur(eXR,eYR));
				listeBloc.add(bloc);
				
				trou= new TrouNoir(new Vecteur(eXR,eYR));
				listeTrou.add(trou);
				repaint();
			}
		});


		addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				character.deplacerLePersoSelonTouche( e );
				shoot(e);
				repaint();
			}
		});




		balle1 = new Balle(position, vitesse, "LARGE" );
		/*
		laser = new Laser(
				new Vecteur(
			character.getPositionX()+character.getLARGEUR_PERSO(),LARGEUR_DU_MONDE), angle, new Vecteur(2,2));
		 */
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
		for(TrouNoir trou: listeTrou) {
			

			trou.dessiner(g2d,mat,HAUTEUR_DU_MONDE,LARGEUR_DU_MONDE);
		}

		g2d.setColor(Color.pink);
		balle1.dessiner(g2d,mat,HAUTEUR_DU_MONDE,LARGEUR_DU_MONDE);

		Coeurs coeur = new Coeurs(4);
		coeur.dessiner(g2d, mat,0,0);
		coeur.setCombien(3);
		coeur.dessiner(g2d, mat,0,0);


		checkCollisionBalleLaserPersonnage( listeBalles,  listeLasers,character);
		checkCollisionTrouLaserPersonnage( listeLasers );
		/*	
		for(Balle balle: listeBalles) {
			g2d.setColor(Color.black);
			balle.collisionBalleLaser(balle.getAireBalle(),g2d, laser.getAireLaser(), listeBalles);
		}
		 */
		for(Balle balle: listeBalles) {

			g2d.setColor(Color.black);
			//balle.dessiner(g2d,mat,HAUTEUR_DU_MONDE,LARGEUR_DU_MONDE);
		}

		character.dessiner(g2d, mat, LARGEUR_DU_MONDE, HAUTEUR_DU_MONDE);

		for(Laser laser : listeLasers) { 

			if(laser.getLigneFinY() <= 0 )
				listeLasers.remove(laser);

			laser.dessiner(g2d, mat, 0, 0);


		}

	}//fin paintComponent




	private void calculerUneIterationPhysique() {


		balle1.unPasRK4( deltaT, tempsTotalEcoule);

		for(Laser laser : listeLasers) { 

			laser.move();

		}
		for(Balle balle: listeBalles) {
			balle.unPasRK4( deltaT, tempsTotalEcoule);
		}

		tempsTotalEcoule += deltaT;;
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


	private void shoot(KeyEvent e) {
		int code = e.getKeyCode();
		if(code == KeyEvent.VK_SPACE) {
			if(listeLasers.size() <1) { // Pour que 1 laser soit tirer  a la fois 
				listeLasers.add(
						new Laser(new Vecteur(
								character.getPositionX()+character.getLARGEUR_PERSO()/2,LARGEUR_DU_MONDE), angle, new Vecteur(0.5,0.5)));
			}
		}
	}


	private void checkCollisionBalleLaserPersonnage(ArrayList<Balle> listeBalles, ArrayList<Laser> listeLasers, Personnage character ) {

		ArrayList<Balle> listeBalleTouche = new ArrayList<Balle>();
		for(Laser laser : listeLasers) {
			for(Balle balle : listeBalles ) {
				if(balle.getAireBalle().intersects(laser.getLine())) {
					listeLasers.remove(laser);   
					listeBalleTouche.add(balle);
					balle.shrink(listeBalles);
				}	
			}
		}
/*
		for(Balle balle : listeBalleTouche) {
			balle.shrink(listeBalles);
		}
		

*/



	}
	private boolean intersection(Area aire1, Area aire2) {
		Area aireInter = new Area(aire1);
		aireInter.intersect(aire2);
		if(!aireInter.isEmpty()) {
			return true;
		}
		return false;
	}
	
private void checkCollisionTrouLaserPersonnage( ArrayList<Laser> listeLasers ) {

		
		for(Laser laser : listeLasers) {
			for(TrouNoir trou : listeTrou ) {
				if(trou.getAireTrou().intersects(laser.getLine())) {
					listeLasers.remove(laser);   
					
				}	
			}
		}
/*
		for(Balle balle : listeBalleTouche) {
			balle.shrink(listeBalles);
		}
		

*/

		//bloc.refraction(v, N, n1, n2);

	}












}
