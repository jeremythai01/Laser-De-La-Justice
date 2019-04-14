package prisme;

import java.awt.BasicStroke;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import javax.swing.JPanel;
import javax.swing.plaf.synth.SynthSeparatorUI;

import geometrie.Vecteur;
import miroir.MiroirConcave;
import objets.BlocDEau;
import objets.Echelle;
import objets.Ordinateur;
import objets.OrdinateurNiveau2;
import objets.OrdinateurNiveau3;
import objets.Personnage;
import objets.TrouNoir;
import physique.Balle;
import physique.Coeurs;
import physique.Laser;
import physique.MoteurPhysique;
import utilite.ModeleAffichage;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * 
 * @author Arnaud
 *
 */
public class SceneTestPrisme extends JPanel implements Runnable {

	private static double EPSILON = 8.85418*Math.pow(10, -12) ;
	
	private static final long serialVersionUID = 1L;
	private int tempsDuSleep = 25;
	// private double deltaT = 0.07;
	private double deltaT = 0.01;
	private double LARGEUR_DU_MONDE = 50; // en metres
	private double HAUTEUR_DU_MONDE;
	private boolean enCoursAnimation = false;
	private double tempsTotalEcoule = 0;
	private double masse = 15; // en kg
	private double diametre = 2; // em mètres
	private ArrayList<Balle> listeBalles = new ArrayList<Balle>();
	private Balle balle1;
	private Balle balle;
	private boolean premiereFois = true;
	private ModeleAffichage modele;
	private AffineTransform mat;

	private boolean premiereFoisPrisme = true;
	
	private double n2 = 2.0;

	private Laser laser = new Laser(new Vecteur(), 2, new Vecteur()) ;
	
	private Vecteur position;

	private Vecteur vitesse;

	private Vecteur gravity;

	private Personnage character;

	private double angle;
	private ArrayList<Laser> listeLasers = new ArrayList<Laser>();

	private BlocDEau bloc;
	private ArrayList<BlocDEau> listeBloc = new ArrayList<BlocDEau>();
	private TrouNoir trou;
	private ArrayList<TrouNoir> listeTrou = new ArrayList<TrouNoir>();
	private Coeurs coeur;
	private int nombreVies = 3;

	private Prisme prisme;
	private ArrayList<Prisme> listePrisme = new ArrayList<Prisme>();
	private Echelle echelle;

	private Ordinateur ordi;
	private OrdinateurNiveau2 ordi2;
	private OrdinateurNiveau3 ordi3;
	private boolean enMouvement = false;
	
	/**
	 * Create the panel.
	 */
	public SceneTestPrisme() {

		// ordi= new Ordinateur(1, new Vecteur(20,44));
		// ordi2= new OrdinateurNiveau2(new Vecteur(30,44));
		ordi3 = new OrdinateurNiveau3(new Vecteur(40, 44));
		ordi3.ajouterListesObstacles(listeBalles);

		angle = 100;
		character = new Personnage();

		position = new Vecteur(0.3, 10);

		vitesse = new Vecteur(3, 0);

		gravity = new Vecteur(0, 9.8);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

				double eXR = e.getX() / modele.getPixelsParUniteX();
				double eYR = e.getY() / modele.getPixelsParUniteY();

				prisme = new Prisme(new Vecteur(eXR, eYR));
				listePrisme.add(prisme);
				// balle = new Balle(new Vecteur(eXR-diametre/2, eYR-diametre/2),vitesse,
				// "LARGE" );
				// listeBalles.add(balle);
				// bloc= new BlocDEau(new Vecteur(eXR,eYR));
				// listeBloc.add(bloc);

				// trou= new TrouNoir(new Vecteur(eXR,eYR));
				// listeTrou.add(trou);

				if (character.airePersonnage().contains(eXR, eYR)) {
					// System.out.println("je suis dans le personnage");
					addMouseMotionListener(new MouseMotionAdapter() {
						@Override
						public void mouseDragged(MouseEvent f) {
							double fX = f.getX() / modele.getPixelsParUniteX();
							// double fY= f.getY()/modele.getPixelsParUniteY();
							if (fX > eXR && angle > 5) {
								System.out.println("je vais vers la droite");
								double distance = fX - eXR;
								angle = angle - (int) distance;
								System.out.println("angle33 " + angle);
							}
							if (fX < eXR && angle < 175) {
								double distance = eXR - fX;
								angle = angle + (int) distance;
								System.out.println("angle " + angle);
							}
							enMouvement = true;
							repaint();
						}
					});
				}

				repaint();
			}
		});

		addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				character.deplacerLePersoSelonTouche(e);
				shoot(e);
				// tirer();
				repaint();
			}
		});

		balle1 = new Balle(position, vitesse, "LARGE", new Vecteur(0, 9.8));
		/*
		 * laser = new Laser( new Vecteur(
		 * character.getPositionX()+character.getLARGEUR_PERSO(),LARGEUR_DU_MONDE),
		 * angle, new Vecteur(2,2));
		 */
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (premiereFois) {
			modele = new ModeleAffichage(getWidth(), getHeight(), LARGEUR_DU_MONDE);
			mat = modele.getMatMC();
			HAUTEUR_DU_MONDE = modele.getHautUnitesReelles();
			premiereFois = false;
		}

		try {

			for (Laser laser : listeLasers) {

				if (laser.getPositionHaut().getY() <= 0) {
					listeLasers.remove(laser);
				}
				g2d.setStroke(new BasicStroke(3));
				laser.dessiner(g2d, mat, 0, 0);
			}

		} catch (ConcurrentModificationException e) {
			System.out.println("le laser nexiste pas");
		}
		checkCollisionBalleLaserPersonnage(listeBalles, listeLasers, character);
		checkCollisionTrouLaserPersonnage(listeLasers);
		checkCollisionBlocLaserPersonnage(listeLasers);
		for (BlocDEau bloc : listeBloc) {
			g2d.setColor(Color.blue);
			bloc.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		}

		for (int i = 0; i < listeBalles.size(); i++) {
			for (int j = i + 1; j < listeBalles.size(); j++) {
				Balle balle1 = listeBalles.get(i);
				Balle balle2 = listeBalles.get(j);
				MoteurPhysique.detectionCollisionBalles(balle1, balle2);
			}
		}

		for (Prisme pris : listePrisme) {
			pris.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		}

		for (Balle balle : listeBalles) {

			// balle.dessiner(g2d,mat,HAUTEUR_DU_MONDE,LARGEUR_DU_MONDE);
		}

		/*
		 * for(TrouNoir trou: listeTrou) {
		 * trou.dessiner(g2d,mat,HAUTEUR_DU_MONDE,LARGEUR_DU_MONDE); }
		 */
		coeur = new Coeurs(nombreVies);
		character.dessiner(g2d, mat, LARGEUR_DU_MONDE, HAUTEUR_DU_MONDE);

		coeur.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		coeur.setCombien(nombreVies - 1);
		coeur.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		echelle = new Echelle(50, 3, 4);
		echelle.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);

		g2d.setColor(Color.yellow);
		// ordi.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		// ordi2.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		ordi3.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);

		tracerVecteurGraphique(g2d);
		/*
		 * if(enMouvement) { //dessinerTracerAngle(g2d); g2d.setColor(Color.RED);
		 * Path2D.Double trace= new Path2D.Double();
		 * trace.moveTo(character.getPositionX()+character.getLARGEUR_PERSO()/2,
		 * HAUTEUR_DU_MONDE-character.getLONGUEUR_PERSO());
		 * trace.lineTo(character.getPositionX()+character.getLARGEUR_PERSO()/2+2*Math.
		 * cos(Math.toRadians(angle)),
		 * HAUTEUR_DU_MONDE-character.getLONGUEUR_PERSO()-2*Math.sin(Math.toRadians(
		 * angle))); g2d.draw(mat.createTransformedShape(trace)); }
		 */

	}// fin paintComponent

	/*
	 * private void dessinerTracerAngle(Graphics2D g) { g.draw(arg0); }
	 */

	private void tracerVecteurGraphique(Graphics2D g) {
		if (enMouvement) {
			g.setColor(Color.red);
			Path2D.Double trace = new Path2D.Double();
			trace.moveTo(character.getPositionX() + character.getLARGEUR_PERSO() / 2,
					HAUTEUR_DU_MONDE - character.getLONGUEUR_PERSO());
			trace.lineTo(
					character.getPositionX() + character.getLARGEUR_PERSO() / 2 + 2 * Math.cos(Math.toRadians(angle)),
					HAUTEUR_DU_MONDE - character.getLONGUEUR_PERSO() - 2 * Math.sin(Math.toRadians(angle)));
			g.draw(mat.createTransformedShape(trace));
		}
	}

	private void calculerUneIterationPhysique() {

		for (Balle balle : listeBalles) {
			balle.unPasEuler(deltaT);
		}

		for (Laser laser : listeLasers) {
			laser.move();
		}

		tempsTotalEcoule += deltaT;
		// ordi.bouge();
		// ordi2.bouge();
		// ordi3.bouge();
	}

	public void arreter() {
		if (enCoursAnimation)
			enCoursAnimation = false;
	}

	public void demarrer() {
		if (!enCoursAnimation) {
			Thread proc = new Thread(this);
			proc.start();
			enCoursAnimation = true;
		}

	}

	private int compteur = 0;

	@Override
	public void run() {

		while (enCoursAnimation) {
			compteur++;
			calculerUneIterationPhysique();

			// ordi3.verifierCollisionBalleEtLaserSimulation();
			if (compteur == 60) {
				// tirer();
				compteur = 0;
			}
			collisionLaserPrisme();
			repaint();
			try {
				Thread.sleep(tempsDuSleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} // fin while
		System.out.println("Le thread est mort...");
	}

	private void shoot(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_SPACE) {
			// if(listeLasers.size() <1) { // Pour que 1 laser soit tirer a la fois
			listeLasers.add(new Laser(new Vecteur(character.getPositionX() + character.getLARGEUR_PERSO() / 2,
					HAUTEUR_DU_MONDE - character.getLONGUEUR_PERSO()), angle, new Vecteur(0, 0.5)));
			// }
		}
	}

	private void checkCollisionBalleLaserPersonnage(ArrayList<Balle> listeBalles, ArrayList<Laser> listeLasers,
			Personnage character) {

		ArrayList<Balle> listeBalleTouche = new ArrayList<Balle>();
		try {
			for (Laser laser : listeLasers) {

				for (Balle balle : listeBalles) {

					if (intersection(balle.getAire(), laser.getAire())) {

						// if(balle.getAireBalle().intersects(laser.getLine())) {

						listeLasers.remove(laser);
						listeBalleTouche.add(balle);
						balle.shrink(listeBalles, new Vecteur(0, 9.8));
						coeur.setCombien(nombreVies - 1);
						nombreVies -= 1;
					}

				}

			}
		} catch (ConcurrentModificationException e) {
		}
	}
	/*
	 * for(Balle balle : listeBalleTouche) { balle.shrink(listeBalles); }
	 * 
	 * 
	 */

	// }
	private boolean intersection(Area aire1, Area aire2) {
		Area aireInter = new Area(aire1);
		aireInter.intersect(aire2);
		if (!aireInter.isEmpty()) {
			return true;
		}
		return false;
	}

	private void checkCollisionTrouLaserPersonnage(ArrayList<Laser> listeLasers) {

		for (Laser laser : listeLasers) {
			for (TrouNoir trou : listeTrou) {
				// if(trou.getAireTrou().intersects(laser.getLine())) {

				if (intersection(trou.getAireTrou(), laser.getAire())) {

					listeLasers.remove(laser);

				}
			}
		}
		/*
		 * for(Balle balle : listeBalleTouche) { balle.shrink(listeBalles); }
		 * 
		 * 
		 */

		// bloc.refraction(v, N, n1, n2);

	}

	private void checkCollisionBlocLaserPersonnage(ArrayList<Laser> listeLasers) {
		for (Laser laser : listeLasers) {
			for (BlocDEau bloc : listeBloc) {
				if (intersection(bloc.getAireBloc(), laser.getAire())) {
					// if(bloc.isPremiereCollision()) {

					// laser.setAngleTir(Math.atan(bloc.refraction(laser.getVitesse(),
					// bloc.calculNormal(laser,bloc), 1.33,
					// 1).getY()/bloc.refraction(laser.getVitesse(), bloc.calculNormal(laser,bloc),
					// 1.33, 1).getX()));

					try {
						Vecteur ref = bloc.refraction(laser.getVitesse().multiplie(-1).normalise(), bloc.getNormal());
						laser.setAngleTir(180 + Math.toDegrees(Math.atan(ref.getY() / ref.getX())));
						// laser.setAngleTir(30);
						System.out.println(laser.getAngleTir());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// System.out.println("nouvel angle:" + Math.toDegrees(laser.getAngleTir()));
					// laser.updaterAngleVitesse(Math.atan(bloc.refraction(laser.getVitesse(),
					// bloc.calculNormal(laser,bloc), 1,
					// 1.33).getY()/bloc.refraction(laser.getVitesse(),
					// bloc.calculNormal(laser,bloc), 1.33, 1).getX()));

					// laser.setAngleTir(90);
					// laser.updaterAngleVitesse((laser.getAngleTir()));
					// System.out.println("nouvel angle:" + Math.toDegrees(laser.getAngleTir()));
					// System.out.println("nouvelle vitesse laser: "+ laser.getVitesse());

					// laser.setAngleTir(30);
					// laser.updaterAngleVitesse((laser.getAngleTir()));
					// System.out.println("laser:" + laser.getVitesse().multiplie(-1));
					// System.out.println("nouvel normal:" + bloc.getNormal());
					// System.out.println();
					// System.out.println("valeur bloc: "+ ref);

					// repaint();
					// bloc.setPremiereCollision(false);
					// }
				}
			}
		}
	}

	private void tirer() {

		ordi3.ajouterListesObstacles(listeBalles);
		// listeLasers.add(ordi.tirer());
		// listeLasers.add(ordi2.tirer());
		// listeLasers.add(ordi3.tirer());
		// listeLasers.add(new Laser(new
		// Vecteur(ordi.getPositionX()+ordi.getLargeurOrdi()/2,HAUTEUR_DU_MONDE-ordi.getLongueurOrdi()),
		// angle, new Vecteur(0,0.5)));

	}

	private void collisionLaserPrisme() {
		
		
		for (int i = 0; i < listeLasers.size(); i++) {
			
			for (int j = 0; j < listePrisme.size(); j++) {
				
				if (enIntersection(listePrisme.get(j).getAirPrisme(), listeLasers.get(i).getAire()) ) {

					System.out.println(j);
					prisme = listePrisme.get(j);
					laser = listeLasers.get(i);
					calculRefractionPrisme(laser,prisme);
					j = listePrisme.size();
					i = listeLasers.size();
					//premiereFoisPrisme = false;
				} 
				
			}
		}

		
	}

	

	private void calculRefractionPrisme(Laser laser, Prisme prismes) {
	
		
		Vecteur T = new Vecteur();
		Vecteur V = laser.getPositionHaut();
		Vecteur N = normalPrisme(laser, prismes);
		Vecteur E = V.multiplie(-1);
		double n = 1.0/5.0;
		System.out.println("la position laser avant refraction : "+ laser.getPositionHaut() );
		
			System.out.println("normal"+ N );
			System.out.println("position: "+ prismes.getP1());
			T = V.multiplie(n).additionne(N.multiplie((n*(E.prodScalaire(N)) - Math.sqrt(1-Math.pow(n, 2) * (1-(Math.pow(E.prodScalaire(N), 2)))))));
			//T = T.multiplie(-1);
		
	
		//laser.setPositionHaut(new Vecteur (anciennePosLaser.getX(),  T.getY()));
			double angleAncien= laser.getAngleTir(); 
		System.out.println("l'angle du laser avant refraction: "+ angleAncien);
		laser.setAngleTir(Math.toDegrees(Math.atan(T.getY()/T.getX()))+angle);
		System.out.println("l'angle du laser apres refraction: "+ laser.getAngleTir());
		
		System.out.println("la position laser apres refraction : "+ laser.getPositionHaut() );		
		
		double angleLaser = laser.getAngleTir();
		listeLasers.add(new Laser(laser.getPositionHaut().additionne(new Vecteur(0.5 ,0)), angleLaser+10, laser.getVitesse()));
		
			repaint();
		
	}

	private boolean enIntersection(Area aire1, Area aire2) {
		Area aireInter = new Area(aire1);
		aireInter.intersect(aire2);
		if (!aireInter.isEmpty()) {
			return true;
		}
		return false;
	}

	private Vecteur normalPrisme(Laser laser, Prisme prisme) {
		
		//System.out.println("position p1"+ prisme.getP1());
		//System.out.println("position p2"+ prisme.getP2());
		//System.out.println("position p3"+ prisme.getP3());
		
		
		
		double resultat13 = prisme.getLigne13().ptSegDist(laser.getPositionHaut().getX(), laser.getPositionHaut().getY());
		double resultat12 = prisme.getLigne12().ptSegDist(laser.getPositionHaut().getX(), laser.getPositionHaut().getY());
		double resultat23 = prisme.getLigne23().ptSegDist(laser.getPositionHaut().getX(), laser.getPositionHaut().getY());
		//System.out.println("resultat pour la ligne 13:"+resultat13); 
		//System.out.println("resultat pour la ligne 12:"+resultat12); 
		//System.out.println("resultat pour la ligne 23:"+resultat13); 
		
		
			
		if(resultat13 > 0 && resultat13 < 0.80) {
			System.out.println("jai touché ligne13");
			return ((prisme.getP3().soustrait(prisme.getP1()).cross(laser.getPositionHaut()))).normalise();
			
		}else if(resultat12 > 0 && resultat12 < 0.80){
			System.out.println("jai toucher la ligne12");
			return ((prisme.getP2().soustrait(prisme.getP1())).cross(laser.getPositionHaut())).normalise();
			
		}else if(resultat23 > 0 && resultat23 < 0.80){
			System.out.println("jai toucher la ligne23");
			return ((prisme.getP3().soustrait(prisme.getP2())).cross(laser.getPositionHaut())).normalise();
			
		}
		
		return new Vecteur ();
	
	}
}

// return collision;
// return collision;F

//}
