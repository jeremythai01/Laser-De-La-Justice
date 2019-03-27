package objets;


import java.awt.BasicStroke;
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
import physique.Balle;
import physique.Coeurs;
import physique.Laser;
import physique.MoteurPhysique;
import utilite.ModeleAffichage;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;



/**
 * 
 * @author Arnaud
 *
 */
public class SceneTestPls extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	private int tempsDuSleep = 25;
	private double deltaT = 0.07;
	private  double LARGEUR_DU_MONDE = 50; //en metres
	private  double HAUTEUR_DU_MONDE;
	private boolean enCoursAnimation= false;
	private double tempsTotalEcoule = 0;
	private double masse = 15; //en kg
	private double diametre = 2;  //em mètres
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
	private Coeurs coeur;
	private int nombreVies=3;

	private Echelle echelle;

	private Ordinateur ordi;
	private OrdinateurNiveau2 ordi2;
	/**
	 * Create the panel.
	 */
	public SceneTestPls() {

		ordi= new Ordinateur(1, new Vecteur(20,44));
		ordi2= new OrdinateurNiveau2(new Vecteur(30,44));
		
		angle = 30   ;
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

				//	trou= new TrouNoir(new Vecteur(eXR,eYR));
				//listeTrou.add(trou);
				repaint();
			}
		});


		addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				character.deplacerLePersoSelonTouche( e );
				shoot(e);
				//tirer();
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



		for(Laser laser : listeLasers) { 

			if(laser.getLigneFinY() <= 0 ) {
				listeLasers.remove(laser);
			}
			g2d.setStroke( new BasicStroke(3));
			laser.dessiner(g2d, mat, 0, 0);
		}
		checkCollisionBalleLaserPersonnage( listeBalles,  listeLasers,character);
		checkCollisionTrouLaserPersonnage( listeLasers );
		checkCollisionBlocLaserPersonnage( listeLasers );
		for(BlocDEau bloc:listeBloc) {
			g2d.setColor(Color.blue);
			bloc.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		}

		for (int i = 0; i < listeBalles.size(); i++) {
			for (int j = i+1; j < listeBalles.size(); j++) {
				Balle balle1 = listeBalles.get(i);
				Balle balle2 = listeBalles.get(j);
				MoteurPhysique.detectionCollisionBalles(balle1, balle2);
			}
		}


		/*for(Balle balle: listeBalles) {

			balle.dessiner(g2d,mat,HAUTEUR_DU_MONDE,LARGEUR_DU_MONDE);
		}*/

		/*
	for(TrouNoir trou: listeTrou) {
		trou.dessiner(g2d,mat,HAUTEUR_DU_MONDE,LARGEUR_DU_MONDE);
	}
		 */
		coeur= new Coeurs(nombreVies);
		character.dessiner(g2d, mat, LARGEUR_DU_MONDE, HAUTEUR_DU_MONDE);
		
		coeur.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		coeur.setCombien(nombreVies-1);
		coeur.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		echelle = new Echelle(50, 3,4);
		echelle.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		
		g2d.setColor(Color.yellow);
		ordi.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		ordi2.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);

	}//fin paintComponent




	private void calculerUneIterationPhysique() {

		
		for(Balle balle: listeBalles) {
			balle.unPasRK4(deltaT, tempsTotalEcoule);
		}

		for(Laser laser : listeLasers) {
			laser.move();
		}

		
		tempsTotalEcoule += deltaT;
		ordi.bouge();
		ordi2.bouge();
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

	
	private int compteur=0;
	
	@Override
	public void run() {
		
		while (enCoursAnimation) {	
			compteur++;
			calculerUneIterationPhysique();
			if(compteur==60) {
				tirer();
				compteur=0;
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


	private void shoot(KeyEvent e) {
		int code = e.getKeyCode();
		if(code == KeyEvent.VK_SPACE) {
			//if(listeLasers.size() <1) { // Pour que 1 laser soit tirer  a la fois 
			listeLasers.add(
					new Laser(new Vecteur(
							character.getPositionX()+character.getLARGEUR_PERSO()/2,HAUTEUR_DU_MONDE-character.getLONGUEUR_PERSO()), angle, new Vecteur(0,0.5)));
			//}
		}
	}


	private void checkCollisionBalleLaserPersonnage(ArrayList<Balle> listeBalles, ArrayList<Laser> listeLasers, Personnage character ) {

		ArrayList<Balle> listeBalleTouche = new ArrayList<Balle>();
		for(Laser laser : listeLasers) {
			for(Balle balle : listeBalles ) {

				if(intersection(balle.getAireBalle(), laser.getLaserAire())) {

					//if(balle.getAireBalle().intersects(laser.getLine())) {

					listeLasers.remove(laser);   
					listeBalleTouche.add(balle);
					balle.shrink(listeBalles);
					coeur.setCombien(nombreVies-1);
					nombreVies-=1;
				}	

			}
		}
	}
	/*
		for(Balle balle : listeBalleTouche) {
			balle.shrink(listeBalles);
		}


	 */



	//}
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
				//if(trou.getAireTrou().intersects(laser.getLine())) {

				if(intersection(trou.getAireTrou(), laser.getLaserAire())) {


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

	private void checkCollisionBlocLaserPersonnage(ArrayList<Laser> listeLasers) {
		for(Laser laser : listeLasers) {
			for(BlocDEau bloc : listeBloc) {
				if(intersection(bloc.getAireBloc(), laser.getLaserAire())) {
					//if(bloc.isPremiereCollision()) {

					//laser.setAngleTir(Math.atan(bloc.refraction(laser.getVitesse(), bloc.calculNormal(laser,bloc), 1.33, 1).getY()/bloc.refraction(laser.getVitesse(), bloc.calculNormal(laser,bloc), 1.33, 1).getX()));

					try {
						Vecteur ref= bloc.refraction(laser.getVitesse().multiplie(-1).normalise(), bloc.getNormal(), 1, 1.33);
						laser.setAngleTir(90-Math.atan(ref.getY()/ref.getX()));
						//laser.setAngleTir(30);
						System.out.println(laser.getAngleTir());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					//System.out.println("nouvel angle:" + Math.toDegrees(laser.getAngleTir()));
					//	laser.updaterAngleVitesse(Math.atan(bloc.refraction(laser.getVitesse(), bloc.calculNormal(laser,bloc), 1, 1.33).getY()/bloc.refraction(laser.getVitesse(), bloc.calculNormal(laser,bloc), 1.33, 1).getX()));

					//laser.setAngleTir(90);
					//laser.updaterAngleVitesse((laser.getAngleTir()));
					//System.out.println("nouvel angle:" + Math.toDegrees(laser.getAngleTir()));
					//System.out.println("nouvelle vitesse laser: "+ laser.getVitesse());

					//laser.setAngleTir(30);
					//laser.updaterAngleVitesse((laser.getAngleTir()));
					//System.out.println("laser:" + laser.getVitesse().multiplie(-1));
					//System.out.println("nouvel normal:" + bloc.getNormal());
					//	System.out.println();
					//	System.out.println("valeur bloc: "+ ref);

					//repaint();
				//	bloc.setPremiereCollision(false);
					//}
				}
			}
		}
	}
	
	private void tirer() {
		
			
			listeLasers.add(ordi.tirer());
			listeLasers.add(ordi2.tirer());
		//listeLasers.add(new Laser(new Vecteur(ordi.getPositionX()+ordi.getLargeurOrdi()/2,HAUTEUR_DU_MONDE-ordi.getLongueurOrdi()), angle, new Vecteur(0,0.5)));
			
	}
}




//}
