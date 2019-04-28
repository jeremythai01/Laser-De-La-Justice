package aaplication;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketOptions;
import java.net.URL;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import effets.AjoutVie;
import effets.BoostVitesse;
import effets.Bouclier;
import effets.Pouvoir;
import effets.Ralenti;
import geometrie.Vecteur;
import interfaces.SceneListener;
import miroir.Ligne;
import miroir.MiroirCourbe;
import miroir.MiroirPlan;
import objets.BlocDEau;
import objets.Echelle;
import objets.OrdinateurNiveau3;
import objets.TrouNoir;
import personnage.Personnage;
import physique.Balle;
import physique.Coeurs;
import physique.Laser;
import physique.MoteurPhysique;
import prisme.Prisme;
import son.Bruit;
import utilite.ModeleAffichage;
import utilite.OutilsMath;

/**
 * Cette classe contient la scene d'animation du jeu.
 * 
 * @author Miora, Arezki, Jeremy, Arnaud
 *
 */
public class Scene extends JPanel implements Runnable {

	private Image fond = null;

	private Personnage personnage;

	private static final long serialVersionUID = 1L;

	private double angle;

	private double LARGEUR_DU_MONDE = 65; // en metres
	private double HAUTEUR_DU_MONDE;
	private double diametre = 2; // em mètres
	private int tempsDuSleep = 30;
	private int nombreVies = 5;
	private int tempsDuJeu = 60; //initialement
	private int toucheGauche = 37;
	private double n2 = 2.00;
	private int compteur = 0;
	private double qtRotation = 0;

	private int toucheDroite = 39;
	private double positionPerso;
	private float valeurAngleRoulette = 90;
	private boolean enCoursAnimation = false;
	private boolean premiereFois = true;
	private boolean isGrCercleCliquer = false;
	private boolean isMedCercleCliquer = false;
	private boolean isPetCercleCliquer = false;
	private boolean bonneBalle = false;
	private boolean bonMiroirCourbe = false;
	private boolean bonMiroirPlan = false;
	private boolean bonTrouNoir = false;
	private boolean bonBlocEau = false;
	private boolean editeurActiver = false;
	private boolean couleurPersoLaser = false;
	private boolean bonPrisme = false;
	private boolean ligne13 = false;
	private boolean ligne23 = false;
	private boolean ligne12 = false;
	private boolean triche = false;
	private boolean effacement = false;

	private ModeleAffichage modele;
	private AffineTransform mat;
	private Vecteur vitesse = new Vecteur(1, 1);
	private Vecteur accBalle;

	private ArrayList<Laser> listeLasers = new ArrayList<Laser>();
	private ArrayList<TrouNoir> listeTrou = new ArrayList<TrouNoir>();
	private ArrayList<Balle> listeBalles = new ArrayList<Balle>();
	private ArrayList<MiroirCourbe> listeMiroirCourbe = new ArrayList<MiroirCourbe>();
	private ArrayList<MiroirPlan> listeMiroirPlan = new ArrayList<MiroirPlan>();
	private ArrayList<BlocDEau> listeBlocEau = new ArrayList<BlocDEau>();
	private ArrayList<Prisme> listePrisme = new ArrayList<Prisme>();
	private ArrayList<Pouvoir> listePouvoirs = new ArrayList<Pouvoir>();

	private Balle balle;
	private TrouNoir trou;
	private MiroirCourbe miroirCourbe;
	private MiroirPlan miroirePlan;
	private BlocDEau bloc;
	private OrdinateurNiveau3 ordi;
	private Laser laser;
	private Coeurs coeurs = new Coeurs(nombreVies);
	private Prisme prisme;

	private Echelle echelle;
	private Color couleurLaser = null;

	private Vecteur gravite = new Vecteur(0, 0); // pour miora

	private Balle grosseBalle = new Balle(new Vecteur(), vitesse, "LARGE", gravite);
	private Balle moyenneBalle = new Balle(new Vecteur(1, 0), vitesse, "MEDIUM", gravite);
	private Balle petiteBalle = new Balle(new Vecteur(2, 0), vitesse, "SMALL", gravite);

	private ArrayList<SceneListener> listeEcouteur = new ArrayList<SceneListener>();

	private int toucheTir = 32;

	private boolean enMouvement = false;

	private double tempsEcoule = 0;
	private double deltaTInit = 0.03;
	private double deltaT = deltaTInit;

	private Vecteur vitesseLaserInit = new Vecteur(0, 0.5);
	private Vecteur vitesseLaser = vitesseLaserInit;
	private double compteurVitesse = 0;
	private double compteurRalenti = 0;
	private double compteurBouclier = 0;
	private Bruit son = new Bruit();

	private int angleMiroir = 0;

	private boolean modeScientifique = false;

	// Par Jeremy
	/**
	 * Constructeur de la scene et permet de mettre les objets avec le clique de la
	 * souris
	 * 
	 * @param isPartieNouveau : retourne vrai s'il s'agit d'une nouvelle partie ou
	 *                        d'une partie sauvegardée
	 */

	public Scene(boolean isPartieNouveau, String nomFichier) {
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				setAngleRoulette();
			}
		});

		lireFond();

		angle = valeurAngleRoulette;
		nouvellePartie(isPartieNouveau, nomFichier);
		lectureFichierOption();
		// personnage.setModeSouris(true);

		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				double xSouris = e.getX() / modele.getPixelsParUniteX();
				personnage.setPosSouris(xSouris);
			}
		});

		addMouseListener(new MouseAdapter() {
			@Override

			public void mousePressed(MouseEvent e) {

				double eXR = e.getX() / modele.getPixelsParUniteX();
				double eYR = e.getY() / modele.getPixelsParUniteY();

				selectionneurObjets( eXR,  eYR);

				tirLaser(e, personnage); // Tir du laser lorsque le personnage est en mode souris
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {

				personnage.relacheToucheEnModeSouris();
				bonneBalle = false;
				bonMiroirCourbe = false;
				bonMiroirPlan = false;
				bonTrouNoir = false;
				bonBlocEau = false;
				bonPrisme = false;
				bonMiroirCourbe = false;

			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {

				dragObjet(e);

			}
		});

		addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				personnage.deplacerLePersoSelonTouche(e);
				changerAngle(e);
				// modeTriche();
			}

			@Override
			// Jeremy Thai
			public void keyReleased(KeyEvent e) {
				tirLaser(e);
				personnage.relacheTouche(e);

			}
		});

	}

	// Par Arnaud Lefebvre
	/**
	 * Méthode qui permet de dessiner toutes les formes sur la scene incluant le
	 * personnage et de savoir s'il y a des collisions entre le laser et les balles
	 * 
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		if (premiereFois) {
			modele = new ModeleAffichage(getWidth(), getHeight(), LARGEUR_DU_MONDE);
			mat = modele.getMatMC();
			HAUTEUR_DU_MONDE = modele.getHautUnitesReelles();
			premiereFois = false;
			Balle.setModele(getWidth(),getHeight(),LARGEUR_DU_MONDE);
		}

		g2d.drawImage(fond, 0, 0, (int) modele.getLargPixels(), (int) modele.getHautPixels(), null);

		try {
			for (Laser laser : listeLasers) {
				if (laser.getPositionHaut().getY() <= 0)
					listeLasers.remove(laser);
			}

		} catch (ConcurrentModificationException e) {
		}
		try {
			for (Laser laser : listeLasers) {
				laser.dessiner(g2d, mat, 0, 0);
			}
		} catch (ConcurrentModificationException e) {
		}

		detectionCollisionPersonnageMur();
		detectionCollisionPouvoirsPersonnages();
		detectionCollisionBalleLaser(listeBalles, listeLasers);
		detectionCollisionTrouLaser(listeLasers);
		detectionCollisionBlocLaser(listeLasers);
		detectionCollisionBallePersonnage(listeBalles, personnage);
		detectionCollisionMurBalle();
		for (Balle balle : listeBalles) {
			balle.setModeScientifique(modeScientifique);
			balle.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		}

		for (MiroirCourbe miroirV : listeMiroirCourbe) {

			miroirV.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		}

		for (MiroirPlan miroirP : listeMiroirPlan) {

			miroirP.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		}

		for (TrouNoir trou : listeTrou) {
			trou.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		}

		for (BlocDEau blocE : listeBlocEau) {
			blocE.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		}

		for (Prisme pri : listePrisme) {
			pri.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		}

		for(MiroirCourbe courbe : listeMiroirCourbe) {
			courbe.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		}

		personnage.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		coeurs.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);

		echelle = new Echelle(LARGEUR_DU_MONDE, LARGEUR_DU_MONDE - 3.5, HAUTEUR_DU_MONDE - 0.75);
		echelle.savoirModele(getWidth(), getHeight(), LARGEUR_DU_MONDE);
		echelle.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);

		ordi = new OrdinateurNiveau3(new Vecteur(28, LARGEUR_DU_MONDE - 10));
		ordi.ajouterListesObstacles(listeBalles);
		ordi.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		ordi.savoirTempsSleep(tempsDuSleep);

		tracerVecteurGraphique(g2d);

		for (Pouvoir pouvoir : listePouvoirs) {
			pouvoir.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		}

		//Le listenern marche pas dans le constructeur...
		leverEvenChangementTemps(tempsDuJeu);

	}

	
	// Arnaud Lefebvre
	/**
	 * Cette méthode permet d'arreter l'animation
	 */
	public void arreter() {
		if (enCoursAnimation) {
			enCoursAnimation = false;
			son.arreter();
		}
	}

	//Jeremy Thai
	/**
	 * Cette méthode permet de démarrer l'animation
	 */
	public void demarrer() {
		if (!enCoursAnimation) {
			Thread proc = new Thread(this);
			proc.start();
			enCoursAnimation = true;
		}

	}

	//Jeremy Thai
	/**
	 * permet de calculer les collision la vitessse des balles et tout autres
	 * animation ayant de la physiques
	 */
	public void calculerUneIterationPhysique() {

		for (int i = 0; i < listeBalles.size(); i++) {
			Balle balle1 = listeBalles.get(i);
			for (int j = i + 1; j < listeBalles.size(); j++) {
				Balle balle2 = listeBalles.get(j);
				MoteurPhysique.detectionCollisionBalles(balle1, balle2);
			}
		}

		for (Balle balle : listeBalles) {
			balle.unPasVerlet(deltaT);
		}
		for (Laser laser : listeLasers) {
			laser.move();
		}

		updateMouvementPouvoirs();

		if (personnage.isEnVitesse()) {
			personnage.bouge();
			personnage.bouge();
			personnage.bouge();
			personnage.bouge();
		} else {
			personnage.bouge();
		}
		ordi.bouge();

		tempsEcoule += deltaTInit;

	}

	/**
	 * Animation du jeu 
	 */ 
	//Jeremy Thai
	public void run() {
		// TODO Auto-generated method stub
		while (enCoursAnimation) {
			compteur++;
			son.joueMusique("alienMusique");
			calculerUneIterationPhysique();
			leverEventBalle(listeBalles);
			leverEventPersonnage(personnage);
			leverEvenModeScientifique();
			qtRotation = qtRotation + 0.2;
			for (TrouNoir trou : listeTrou) {
				trou.savoirQuantiteRotation(qtRotation);
			}
			try {
				colisionLaserMiroirPlan();
				colisionLaserMiroirCourbe() ;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			updateDureeCompteurs();
			collisionLaserPrisme();

			if (compteur == 60) {
				tirer();
				compteur = 0;
			}
			repaint();

			try {
				// CollisionLaserPrisme(listeLasers, listePrisme);
			} catch (ConcurrentModificationException e) {
				e.printStackTrace();
			}

			try {

				Thread.sleep(tempsDuSleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} // fin while
		System.out.println("Le thread est mort...");
	}

	// Par Miora
	/**
	 * Methode permettant de mettre un fond a la scene
	 **/
	private void lireFond() {

		fond = Toolkit.getDefaultToolkit().createImage(getClass().getClassLoader().getResource("space.gif"));
	}

	// --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	
	// Jeremy Thai
	/**
	 * Fait la detection d une collision entre toutes les balles et tous les lasers
	 * 
	 * @param listeBalles liste de balles
	 * @param listeLasers liste de lasers
	 * @param character   personnage
	 */
	private void detectionCollisionBalleLaser(ArrayList<Balle> listeBalles, ArrayList<Laser> listeLasers) {

		for (Laser laser : listeLasers) {
			for (Balle balle : listeBalles) {
				if (enIntersection(balle.getAire(), laser.getAire())) {
					listeLasers.remove(laser);
					pouvoirAuHasard(balle);
					balle.shrink(listeBalles, gravite);
				}
			}
		}
	}

	// Jeremy Thai
	/**
	 * Fait la detection d une collision entre toutes les balles et le personnage
	 * 
	 * @param listeBalles liste de balles
	 * @param character   personnage
	 */
	private void detectionCollisionBallePersonnage(ArrayList<Balle> listeBalles, Personnage personnage) {

		for (Balle balle : listeBalles) {
			if (enIntersection(balle.getAire(), personnage.getAire())) {

				if (triche == true) 
					return;

				if (personnage.isBouclierActive()) {
					personnage.setBouclierActive(false);
					return;
				} 

				if (personnage.isMort() == true)
					return;

				son.joueSonLorsqueTouche();
				coeurs.setCombien(nombreVies - 1);
				nombreVies--;
				personnage.setTempsMort(tempsEcoule + 3);
				personnage.setMort(true);

				if(coeurs.getCombien() == 0) {
					son.joueSonLorsqueFini();
					arreter();
				}
			}
		}
	}

	/**
	 * Methode qui indique quand un laser entre en collision avec un trou et ensuite
	 * comment changer l'orientation du laser
	 * 
	 * @param listeLasers, la liste des lasers tirés
	 */
	// auteur Arnaud Lefebvre
	private void detectionCollisionTrouLaser(ArrayList<Laser> listeLasers) {
		for (Laser laser : listeLasers) {
			for (TrouNoir trou : listeTrou) {
				// if(trou.getAireTrou().intersects(laser.getLine())) {

				if (enIntersection(trou.getAireTrou(), laser.getAire())) {

					listeLasers.remove(laser);

				}
				if (enIntersection(trou.getAireGrandTrou(), laser.getAire())) {
					Vecteur distance = laser.getPositionHaut().soustrait(trou.getPosition());
					System.out.println("distance entre trou et laser" + distance);
					laser.setAngleTir(laser.getAngleTir() + distance.getX());
				}
			}
		}
	}
	
	/**
	 * Methode qui indique quand un laser entre en collision avec un bloc pour ensuite lui savoir comment changer l'orientation du laser selon l'indice du bloc
	 * @param listeLasers, la liste des lasers tires
	 */
	//Arnaud Lefebvre
	private void detectionCollisionBlocLaser(ArrayList<Laser> listeLasers) {
		for (int i = 0; i < listeLasers.size(); i++) {
			for (int j = 0; j < listeBlocEau.size(); j++) {
				if(listeLasers.get(i).getPositionHaut().getX()>=listeBlocEau.get(j).getPosition().getX()&&
						listeLasers.get(i).getPositionHaut().getX()<=listeBlocEau.get(j).getPosition().getX()+listeBlocEau.get(j).getLARGEUR()
					&&listeLasers.get(i).getPositionHaut().getY()<=listeBlocEau.get(j).getPosition().getY()+listeBlocEau.get(j).getHauteur()+0.2&&
					listeLasers.get(i).getPositionHaut().getY()>=listeBlocEau.get(j).getPosition().getY()+listeBlocEau.get(j).getHauteur()-0.2) {
					BlocDEau bloc = listeBlocEau.get(j);
					Laser laser = listeLasers.get(i);
					System.out.println("je suis ici");
					
					
					try {
						System.out.println("le veiel angle est de "+laser.getAngleTir());
						Vecteur ref= bloc.refraction(laser.getVitesse().multiplie(-1).normalise(), bloc.getNormal());
						double angle = Math.toDegrees(Math.atan(ref.getY()/ref.getX()));
						if(angle<0) {
							angle=angle+180;
						}
						laser.setAngleTir(angle);
						j=listeBlocEau.size();
						System.out.println("le nouvel angle est de "+laser.getAngleTir());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
					
			}
		}
	}

	//Miora
	/**
	 * Cette methode methode reoriente l'angle de depart du laser s'il y a une intersection
	 * avec un miroir plan
	 * @throws Exception
	 */
	private void colisionLaserMiroirPlan() throws Exception{
		for(Laser laser : listeLasers) {
			int n=0;
			boolean collision = false;
			while(n< listeMiroirPlan.size() && collision == false) {
				if(enIntersection(listeMiroirPlan.get(n).getAireMiroirPixel(), laser.getAire())) {
					collision = true;
					
					//Les calculs se font a partir du referentiel g2d. Calcul position d'intersection
					//entre deux droites
					double angleR = Math.toRadians(laser.getAngleTir()) ;
					Vecteur ptsLaser = laser.getPositionHaut(); // un point du laser
					Vecteur vecDirLaser = (new Vecteur (Math.cos(angleR), -(Math.sin(angleR)))).normalise();

					Vecteur ptsMiroir = listeMiroirPlan.get(n).getPosition();
					Vecteur vecDirMiroir = (new Vecteur (Math.cos(Math.toRadians(listeMiroirPlan.get(n).getAngle()) ) , -Math.sin(Math.toRadians(listeMiroirPlan.get(n).getAngle()))));

					Vecteur sous = (ptsLaser.soustrait(ptsMiroir)).multiplie(-1);
					Vecteur kMiroir = (new Vecteur (0,0)).soustrait(vecDirMiroir); 

					double [] inter = OutilsMath.intersectionCramer(vecDirLaser.getX(), kMiroir.getX(), vecDirLaser.getY(), kMiroir.getY(), sous.getX(), sous.getY());

					double x = ptsLaser.getX() + inter[0]*vecDirLaser.getX();
					double y= ptsLaser.getY() + inter[0]*vecDirLaser.getY();
					
					
					Vecteur posInter = new Vecteur (x,y);
			
					
					//Calcul du nouvel angle
					Vecteur normal = listeMiroirPlan.get(n).getNormal(vecDirLaser).normalise();
					if(modeScientifique) {
						listeMiroirPlan.get(n).afficherVecteur(posInter);
					}
					Vecteur incident = (new Vecteur (Math.cos(angleR), -(Math.sin(angleR)))).normalise();
					Vecteur reflexion = (incident.additionne(normal.multiplie(2.0*(incident.multiplie(-1).prodScalaire(normal))))).normalise();
	
					//ajustement en systeme d'axe normal
					reflexion = new Vecteur (reflexion.getX(),-1*reflexion.getY());

					laser.setAngleTir(OutilsMath.ajustementArcTan(reflexion));

					//Translation pour mettre le laser au bon endroit. Le bas du laser va aller a la position intersection
					//le haut va aller a sa nouvelle position
					double decalage = 1.5; // Pour annuler le "en intersection"
					double xt = decalage*(laser.getPositionHaut().getX()-laser.getPositionBas().getX()); // translation x
					double yt = decalage*(laser.getPositionHaut().getY() - laser.getPositionBas().getY()); // translation y

					Vecteur nouveauHaut = OutilsMath.translation(xt, yt, laser.getPositionHaut());
					
					laser.setPositionHaut(nouveauHaut);
				}
				n++;

			}
		}
	} // fin methode



	//Miora
	/**
	 * Cette methode reoriente l'angle de depart du laser s'il y a une intersection
	 * avec un miroir courbe
	 * @throws Exception
	 */
	private void colisionLaserMiroirCourbe() throws Exception{
		for(Laser laser : listeLasers) {
			int n=0;
			boolean collision = false;
			while(n < listeMiroirCourbe.size() && collision == false) {
				for(Ligne ligne :listeMiroirCourbe.get(n).getListeLigne()) {
					if(enIntersection(ligne.getAireLigne(), laser.getAire())){
						collision = true;
						
						//Calcul selon g2d
						
						//Calcul position intersection (intersection entre deux droites)
						Vecteur ptsLaser = laser.getPositionHaut(); // un point du laser
						System.out.println("centre miroir" + listeMiroirCourbe.get(n).getPosition());

						double angleR = Math.toRadians(laser.getAngleTir()) ;	
						Vecteur vecDirLaser = (new Vecteur (Math.cos(angleR), -(Math.sin(angleR)))).normalise();
						System.out.println("dir laser : " + vecDirLaser);

						Vecteur ptsMiroir = (new Vecteur (ligne.getX1(), ligne.getY1()));
						Vecteur vecDirMiroir = new Vecteur (ligne.x2-ligne.x1, ligne.y2-ligne.y1).normalise();
		
						Vecteur sous = (ptsLaser.soustrait(ptsMiroir)).multiplie(-1); // de l'autre cote equation
						Vecteur kMiroir = (new Vecteur (0,0)).soustrait(vecDirMiroir); // devient moins
						double [] inter = OutilsMath.intersectionCramer(vecDirLaser.getX(), kMiroir.getX(), vecDirLaser.getY(), kMiroir.getY(), sous.getX(), sous.getY());

						double x = ptsLaser.getX() + inter[0]*vecDirLaser.getX();
						double y= ptsLaser.getY() + inter[0]*vecDirLaser.getY();
						Vecteur posInter = new Vecteur (x,y);

						//Calcul nouvel angle
						Vecteur normal = listeMiroirCourbe.get(n).getNormal(posInter).normalise();
						Vecteur incident = (new Vecteur (Math.cos(angleR), -(Math.sin(angleR)))).normalise();
						Vecteur reflexion = (incident.additionne(normal.multiplie(2.0*(incident.multiplie(-1).prodScalaire(normal))))).normalise();
	
						//ajustement en systeme d'axe normal
						reflexion = new Vecteur (reflexion.getX(),-1*reflexion.getY());

						//Nouvel angle
						laser.setAngleTir(OutilsMath.ajustementArcTan(reflexion));
						
						//Translation pour mettre le laser au bon endroit. Le bas du laser va aller a la position intersection
						//le haut va aller a sa nouvelle position
						double decalage = 1.5; // Pour annuler le "en intersection"
						double xt = decalage*(laser.getPositionHaut().getX()-laser.getPositionBas().getX()); // translation x
						double yt = decalage*(laser.getPositionHaut().getY() - laser.getPositionBas().getY()); // translation y

						Vecteur nouveauHaut = OutilsMath.translation(xt, yt, laser.getPositionHaut());
						
						laser.setPositionHaut(nouveauHaut);
					}
				}
				n++;

			}
		}
	}
	// fin methode
	

	// Jeremy Thai
	/**
	 * Ajoute un laser dans la liste de lasers si l'utilisateur appuie sur la bonnetouche de clavier
	 * @param e touche du clavier
	 */
	private void tirLaser(KeyEvent e) {
		if (enCoursAnimation) {
			int code = e.getKeyCode();
			if (code == KeyEvent.VK_SPACE) {
				double x = personnage.getPosition() + personnage.getLARGEUR_PERSO() / 2
						+ 2 * Math.cos(Math.toRadians(angle));
				double y = HAUTEUR_DU_MONDE - personnage.getLONGUEUR_PERSO() - 2 * Math.sin(Math.toRadians(angle));
				if (couleurPersoLaser == false) {
					listeLasers.add(new Laser(new Vecteur(x, y), angle, vitesseLaser));
					leverEventLaser(listeLasers, angle);
				} else {
					listeLasers.add(new Laser(new Vecteur(x, y), angle, vitesseLaser, couleurLaser));
					leverEventLaser(listeLasers, angle);
				}
				son.joue("tir");
			}
		}
	}

	// --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	// Jeremy Thai
	/**
	 * Retourne vrai si deux aires de formes sont en intersection, sinon faux.
	 * 
	 * @param aire1 aire de la premiere forme
	 * @param aire2 aire de la deuxieme forme
	 * @return boolean true or false
	 */
	private boolean enIntersection(Area aire1, Area aire2) {
		Area aireInter = new Area(aire1);
		aireInter.intersect(aire2);
		if (!aireInter.isEmpty()) {
			return true;
		}
		return false;
	}

	// -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Permet d'ajouter et de dessiner une grosse balle en appuyant
	 * sur le boutton grosse balle
	 */
	// Auteur: Arezki Issaadi

	public void ajoutBalleGrosse() {
		grosseBalle = new Balle(new Vecteur(), vitesse, "LARGE", gravite);
		listeBalles.add(grosseBalle);
		repaint();

	}

	/**
	 * Permet d'ajouter et de dessiner une balle medium en appuyant
	 * sur le boutton medium balle
	 */
	// Auteur: Arezki Issaadi

	public void ajoutBalleMedium() {
		moyenneBalle = new Balle(new Vecteur(1, 0), vitesse, "MEDIUM", gravite);
		listeBalles.add(moyenneBalle);

		repaint();

	}

	/**
	 * Permet d'ajouter et de dessiner une petite balle en appuyant
	 * sur le boutton petite balle
	 */
	// Auteur: Arezki Issaadi

	public void ajoutBallePetite() {

		// System.out.println("avant :"+petiteBalle.toString());
		petiteBalle = new Balle(new Vecteur(2, 2), vitesse, "SMALL", gravite);
		listeBalles.add(petiteBalle);

		repaint();

	}


	/**
	 * Permet d'ajouter et de dessiner un miroir plan en appuyant sur
	 * le boutton miroire plan
	 */
	// Auteur: Arezki Issaadi

	public void ajoutMiroirPlan() {
		listeMiroirPlan.add(new MiroirPlan(new Vecteur(4, 4), angleMiroir));
		repaint();

	}

	/**
	 * Permet d'ajouter et de dessiner un trou noir en appuyant sur
	 * le boutton Trou noir
	 */
	// Auteur: Arezki Issaadi

	public void ajoutTrouNoir() {
		listeTrou.add(new TrouNoir(new Vecteur(7, 0)));
		repaint();

	}

	/**
	 * Permet d'ajouter et de dessiner un prisme en appuyant sur
	 * le boutton prisme
	 */
	// Auteur: Arezki Issaadi
	public void ajoutPrisme() {
		listePrisme.add(new Prisme(new Vecteur(2, 2)));
		repaint();

	}

	/**
	 * Permet d'ajouter et de dessiner un bloc d'eau en appuyant sur
	 * le boutton Bloc d'eau
	 */
	// Auteur: Arezki Issaadi
	public void ajoutBlocEau() {
		listeBlocEau.add(new BlocDEau(new Vecteur(9, 0), 2));
		repaint();

	}


	/**
	 * Permet d'ajouter et de dessiner un miroir courbe en appuyant sur
	 * le boutton miroir courbe
	 */
	// Auteur: Arezki Issaadi
	public void ajoutMiroirCourbe() {
		listeMiroirCourbe.add(new MiroirCourbe(new Vecteur(2,2), 2, angleMiroir));
		repaint();

	}
	// -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 *Permet aux dessins de safficher a chaque clic
	 */
	// Auteur: Arezki Issaadi
	public void ActiverEditeur() {
		editeurActiver = true;
		repaint();
	}

	/**
	 * Permet d'empecher que les dessins saffichent a chaque clic
	 */
	// Auteur: Arezki Issaadi
	public void DesactiverEditeur() {
		editeurActiver = false;

	}

	/**
	 * Efface tous les dessins sur la scene en effaçant tous les
	 * objets dans les listes. Agisse comme une corbeille
	 */
	// Auteur: Arezki Issaadi
	public void reinitialiserDessin() {
		listeBalles.removeAll(listeBalles);
		listeBlocEau.removeAll(listeBlocEau);
		listeMiroirCourbe.removeAll(listeMiroirCourbe);
		listeMiroirPlan.removeAll(listeMiroirPlan);
		listeTrou.removeAll(listeTrou);
		repaint();
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------





	/**
	 * Methode qui fait drag tous les objets 
	 */
	// Auteur: Arezki Issaadi
	private void dragObjet(MouseEvent e) {
		if (bonPrisme) {

			double xDrag = e.getX() / modele.getPixelsParUniteX();
			double yDrag = e.getY() / modele.getPixelsParUniteY();
			prisme.setPosition((new Vecteur(xDrag, yDrag)));

			repaint();
		}
		if (bonBlocEau) {

			double xDrag = e.getX() / modele.getPixelsParUniteX();
			double yDrag = e.getY() / modele.getPixelsParUniteY();
			bloc.setPosition(new Vecteur(xDrag, yDrag));

			repaint();
		}
		if (bonTrouNoir) {

			double xDrag = e.getX() / modele.getPixelsParUniteX();
			double yDrag = e.getY() / modele.getPixelsParUniteY();
			trou.setPosition(new Vecteur(xDrag, yDrag));

			repaint();
		}
		if (bonTrouNoir) {

			double xDrag = e.getX() / modele.getPixelsParUniteX();
			double yDrag = e.getY() / modele.getPixelsParUniteY();
			trou.setPosition(new Vecteur(xDrag, yDrag));

			repaint();
		}
		if (bonMiroirPlan) {

			double xDrag = e.getX() / modele.getPixelsParUniteX();
			double yDrag = e.getY() / modele.getPixelsParUniteY();

			miroirePlan.setPosition(new Vecteur(xDrag, yDrag));
			repaint();
		}

		if (bonMiroirCourbe) {

			double xDrag = e.getX() / modele.getPixelsParUniteX();
			double yDrag = e.getY() / modele.getPixelsParUniteY();
			miroirCourbe.setPosition(new Vecteur(xDrag, yDrag));

			repaint();
		}
		if (bonneBalle) {

			double xDrag = e.getX() / modele.getPixelsParUniteX();
			double yDrag = e.getY() / modele.getPixelsParUniteY();
			balle.setPosition(new Vecteur(xDrag - diametre / 2, yDrag - diametre / 2));

			repaint();
		}
	}

	// -------------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Methode qui permet de changer l'angle de tir si le joueur enfonce les fleches
	 * 
	 * @param e, la touche que le joueur a enfoncée
	 */
	// auteur Arnaud Lefebvre
	private void changerAngle(KeyEvent e) {
		if (e.getKeyCode() == 38 && angle < 180) {
			angle = angle + 5;
			if (angle > 180)
				angle = 180;
		}
		if (e.getKeyCode() == 40 && angle > 0) {
			angle = angle - 5;
			if (angle < 0)
				angle = 0;
		}
		enMouvement = true;
	}

	/**
	 * Methode qui indique aux ordis de tirer
	 */
	// auteur Arnaud Lefebvre
	private void tirer() {

		ordi.ajouterListesObstacles(listeBalles);
		// listeLasers.add(ordi.tirer());
		// listeLasers.add(ordi2.tirer());
		listeLasers.add(ordi.tirer());
		// listeLasers.add(new Laser(new
		// Vecteur(ordi.getPositionX()+ordi.getLargeurOrdi()/2,HAUTEUR_DU_MONDE-ordi.getLongueurOrdi()),
		// angle, new Vecteur(0,0.5)));

	}

	/**
	 * Methode qui active le mode de triche
	 */
	// auteur Arnaud Lefebvre
	private void modeTriche() {
		triche = true;
	}

	/**
	 * Methode qui permet de tracer un vecteur qui indique l'angle de tir du fusil
	 * 
	 * @param g, le composant graphique
	 */
	// Arnaud Lefebvre
	private void tracerVecteurGraphique(Graphics2D g) {
		if (enMouvement) {
			g.setColor(Color.red);
			Path2D.Double trace = new Path2D.Double();
			trace.moveTo(personnage.getPosition() + personnage.getLARGEUR_PERSO() / 2,
					HAUTEUR_DU_MONDE - personnage.getLONGUEUR_PERSO());
			trace.lineTo(
					personnage.getPosition() + personnage.getLARGEUR_PERSO() / 2 + 2 * Math.cos(Math.toRadians(angle)),
					HAUTEUR_DU_MONDE - personnage.getLONGUEUR_PERSO() - 2 * Math.sin(Math.toRadians(angle)));
			g.draw(mat.createTransformedShape(trace));
		}
	}

	// ----------------------------------------------------------------------------------------------------------------------------------------------

	// Par Miora
	/**
	 * Cette methode permet de lire le fichier option de la classe option modifie
	 * avant le debut de la partie
	 */
	private void lectureFichierOption() {
		File fichierDeTravail;
		ObjectInputStream fluxEntree = null;

		// Path du dossier contenant les modifications, les options sont crees par
		// ordinateur et non par partie
		String direction = System.getProperty("user.home") + File.separator + "Desktop" + File.separator
				+ "Laser de la justice";
		direction += File.separator + "Option" + File.separator + "modifie.d3t";
		File f = new File(direction);
		// Fin path

		if (f.exists()) { // si le fichier modiefie existe, changement
			fichierDeTravail = new File(direction);
		}else { //sinon version initiale
			String autreDir = System.getProperty("user.dir");
			fichierDeTravail = new File(autreDir, "DonneeInitiale.d3t");
			System.out.println("else");
		}
		try {
			fluxEntree = new ObjectInputStream(new BufferedInputStream(new FileInputStream(fichierDeTravail)));
			gravite = new Vecteur(0, fluxEntree.readDouble());
			System.out.println("accballe option" + accBalle);

			toucheGauche = fluxEntree.readInt();
			toucheDroite = fluxEntree.readInt();
			try {
				Color couleurOption;
				couleurOption = (Color) fluxEntree.readObject();
				if (couleurOption == null) {
					couleurPersoLaser = false;
					couleurLaser = null;
				} else {
					couleurPersoLaser = true;
					couleurLaser = couleurOption;
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} // fin try

		catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Fichier  " + fichierDeTravail.getAbsolutePath() + "  introuvable!");
			System.exit(0);
		}

		catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Erreur rencontree lors de la lecture");
			e.printStackTrace();
			System.exit(0);
		}

		finally {
			// on exécutera toujours ceci, erreur ou pas
			try {
				fluxEntree.close();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Erreur rencontrée lors de la fermeture!");
			}
		} // fin finally
	}

	// Par Miora
	/**
	 * Cette methode creer de mettre le niveau pesonnalise
	 * @param nomFichier : le nom du fichier a lire
	 */
	private void lectureNiveau(String nomFichier) {
		String nomFichierNiveau = nomFichier;
		ObjectInputStream fluxEntree = null;

		String direction = System.getProperty("user.dir") + File.separator + "Laser de la justice";
		direction += File.separator + "Niveau";

		File fichierDeTravail = new File(direction, nomFichierNiveau);

		try {
			fluxEntree = new ObjectInputStream(new FileInputStream(fichierDeTravail));
			try {
				listeBalles = (ArrayList<Balle>) fluxEntree.readObject();
				listeBlocEau = (ArrayList<BlocDEau>) fluxEntree.readObject();
				listeMiroirPlan = (ArrayList<MiroirPlan>) fluxEntree.readObject();
				listePrisme = (ArrayList<Prisme>) fluxEntree.readObject();
				listeTrou = (ArrayList<TrouNoir>) fluxEntree.readObject();
				listeMiroirCourbe = (ArrayList<MiroirCourbe>) fluxEntree.readObject();

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} // fin try

		catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Fichier  " + fichierDeTravail.getAbsolutePath() + "  introuvable!");
			System.exit(0);
		}

		catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Erreur rencontree lors de la lecture niveau ");
			e.printStackTrace();
			System.exit(0);
		}

	}

	// Miora
	/**
	 * Cette methode permet de sauvegarder le nombre de vie, le nombre des balles,
	 * la position du joueur, la couleur du rayon et les touches utilisées
	 * 
	 * @param nomSauv    : le nom de la sauvegarde
	 * @param dansOption : retourne vrai si la methode est appele dans le frame
	 *                   option
	 */
	public void ecritureFichierSauvegarde(String nomSauv, boolean dansOption) {
		String nomSave = "";
		if (dansOption) {
			nomSave = "temporaire";
		} else {
			nomSave = nomSauv + ".save";
		}

		// Creation dossier
		String direction = System.getProperty("user.dir") + File.separator + "Laser de la justice";
		direction += File.separator + "Sauvegarde";
		File customDir = new File(direction);
		if (customDir.exists()) {
			System.out.println(customDir + "le fichier existe deja");
		} else if (customDir.mkdirs()) {
			System.out.println(customDir + "il a ete cree");
		} else {
			System.out.println(customDir + "le fichier n'a pas ete cree");
		}
		// Fin creation dossier
		
		String nomFichierSauvegarde = nomSave;
		File fichierDeTravail = new File(customDir, nomFichierSauvegarde);
		ObjectOutputStream fluxSortie = null;
		try {
			fluxSortie = new ObjectOutputStream(new FileOutputStream(fichierDeTravail));
			fluxSortie.writeInt(nombreVies); // nombre de vie
			fluxSortie.writeObject(listeBalles); // la liste des balles
			fluxSortie.writeObject(listeMiroirPlan);
			fluxSortie.writeObject(listeMiroirCourbe);
			fluxSortie.writeObject(listePrisme);
			fluxSortie.writeObject(listeTrou);
			fluxSortie.writeObject(listeBlocEau);
			fluxSortie.writeDouble(personnage.getPosition());
			System.out.println("ecrit " + personnage.getPosition() );
			if (couleurLaser == null) {
				fluxSortie.writeObject(null);
			} else {
				fluxSortie.writeObject(couleurLaser);
			} // la couleur du rayon
			fluxSortie.writeInt(toucheGauche); // la touche gauche
			fluxSortie.writeInt(toucheDroite); // la touche droite
			fluxSortie.writeInt(tempsDuJeu);

		} catch (IOException e) {
			System.out.println("Erreur lors de l'écriture!");
			e.printStackTrace();
		} finally {
			// on exécutera toujours ceci, erreur ou pas
			try {
				fluxSortie.close();
			} catch (IOException e) {
				System.out.println("Erreur rencontrée lors de la fermeture!");
			}
		} // fin finally
	}

	// Par Miora
	/**
	 * Cette methode permet de lire le fichier qui sauvegarde le nombre de vie, le
	 * nombre des balles, la position du joueur, la couleur du rayon et les touches
	 * utilisées
	 * 
	 * @param nomFichier : le nom du fichier de sauvegarde
	 */
	private void lectureFichierSauvegarde(String nomFichier) {
		String direction = System.getProperty("user.dir") + File.separator + "Laser de la justice";
		direction += File.separator + "Sauvegarde";
		File fichierDeTravail;
		if (nomFichier.equals("temporaire")) {
			fichierDeTravail = new File(direction, "temporaire");

		} else {
			fichierDeTravail = new File(direction, nomFichier);
			System.out.println(nomFichier);
		}
		ObjectInputStream fluxEntree = null;

		try {
			fluxEntree = new ObjectInputStream(new FileInputStream(fichierDeTravail));
			nombreVies = fluxEntree.readInt();
			try {
				listeBalles = (ArrayList<Balle>) fluxEntree.readObject();
				listeMiroirPlan = (ArrayList<MiroirPlan>) fluxEntree.readObject();
				listeMiroirCourbe = (ArrayList<MiroirCourbe>) fluxEntree.readObject();
				listePrisme = (ArrayList<Prisme>) fluxEntree.readObject();
				listeTrou = (ArrayList<TrouNoir>) fluxEntree.readObject();
				listeBlocEau = (ArrayList<BlocDEau>) fluxEntree.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			positionPerso = fluxEntree.readDouble();
			System.out.println("lu" + positionPerso );
			try {
				couleurLaser = (Color) fluxEntree.readObject();
				if (couleurLaser == null) {
					couleurPersoLaser = false;
				} else {
					couleurPersoLaser = true;
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			toucheGauche = fluxEntree.readInt();
			toucheDroite = fluxEntree.readInt();
			tempsDuJeu = fluxEntree.readInt();
		} // fin try

		catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Fichier  " + fichierDeTravail.getAbsolutePath() + "  introuvable!");
			System.exit(0);
		}

		catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Erreur rencontree lors de la lecture");
			e.printStackTrace();
			System.exit(0);
		}
	}

	// Par Miora
	/**
	 * Cette methode definie si la scene est une nouvelle scene ou une scene charge
	 * 
	 * @param isNouvelle : retourne vrai s'il s'agit d'une nouvelle scene
	 * @param nomFichier : le nom du fichier a jouer
	 */

	private void nouvellePartie(boolean isNouvelle, String nomFichier) {
		if (isNouvelle == false) {
			// partie chage
			if (nomFichier.endsWith(".niv")) {
				lectureNiveau(nomFichier);
				personnage = new Personnage(LARGEUR_DU_MONDE / 2, toucheGauche, toucheDroite, toucheTir);
			} else if (nomFichier.endsWith(".save") || nomFichier.equals("temporaire")) {
				lectureFichierSauvegarde(nomFichier);
				coeurs.setCombien(nombreVies);
				personnage = new Personnage(positionPerso, toucheGauche, toucheDroite, toucheTir);
			}
		}else {
			// partie nouvelle
			System.out.println("nouvelle partie come on");
			System.out.println("scene isNouvelle" + isNouvelle + " " + toucheGauche);
			personnage = new Personnage(LARGEUR_DU_MONDE / 2, toucheGauche, toucheDroite, toucheTir);
		}
	}


	// Par Miora R. Rakoto
	/**
	 * Cette methode permet de sauvegarder un niveau
	 * 
	 * @param nomSauv : le nom du niveau
	 */
	public void ecritureNiveau(String nomSauv) {
		// Creation dossier
		String direction = System.getProperty("user.dir") + File.separator + "Laser de la justice";
		direction += File.separator + "Niveau";
		File customDir = new File(direction);

		if (customDir.exists()) {
			System.out.println(customDir + "Le fichier niveau n'existe pas");
		} else if (customDir.mkdirs()) {
			System.out.println(customDir + "Le fichier niveau a ete cree");
		} else {
			System.out.println(customDir + "Le fichier niveau n'a pas ete cree");
		}
		// Fin creation dossier

		String nomFichierNiveau = nomSauv + ".niv";
		File fichierDeTravail = new File(direction, nomFichierNiveau);
		ObjectOutputStream fluxSortie = null;
		try {
			fluxSortie = new ObjectOutputStream(new FileOutputStream(fichierDeTravail));
			fluxSortie.writeObject(listeBalles); // la liste des balles
			fluxSortie.writeObject(listeBlocEau);
			fluxSortie.writeObject(listeMiroirPlan);
			fluxSortie.writeObject(listePrisme);
			fluxSortie.writeObject(listeTrou);
			fluxSortie.writeObject(listeMiroirCourbe);
		} catch (IOException e) {
			System.out.println("Erreur lors de l'écriture!");
			e.printStackTrace();
		} finally {
			// on exécutera toujours ceci, erreur ou pas
			try {
				fluxSortie.close();
			} catch (IOException e) {
				System.out.println("Erreur rencontrée dans la sauvagarde de niveau!");
			}
		} // fin finally
	}

	// --------------------------------------------------------------------------------------------------------------------------------------

	//Miora
	/**
	 * Cette methode permet d'ajouter la liste d'écouteur a la scene
	 * @param ecouteur : l'ecouteur
	 */
	public void addSceneListener(SceneListener ecouteur) {
		listeEcouteur.add(ecouteur);
	}
	// Par Miora
	/**
	 * Cette methode permet de remettre le temps de la partie sauvegarde
	 */
	public void leverEvenChangementTemps(int temps) {
		for (SceneListener ecout : listeEcouteur) {
			ecout.changementTempsListener(temps);
		}
	}
	
	/**
	 * Permet d'avoir en sortie la vitesse, l'accélération et la force gravitationnelle des balles. 
	 * @param listeBalles
	 */
	// Arezki
	public void leverEventBalle(ArrayList<Balle> listeBalles) {
		for(SceneListener ecout : listeEcouteur) {
			ecout.evenementBalles(listeBalles);
		}
	}
	/**
	 * Cette méthode gère les évènements qui sont reliés au laser
	 * @param lasers
	 * @param angle
	 */
	//Arezki
	public void leverEventLaser(ArrayList<Laser> lasers, double angle) {
		for(SceneListener ecout : listeEcouteur) {
			ecout.evenementLaser(lasers, angle);
		}
	}

	public void leverEventPersonnage(Personnage personnage) {
		for(SceneListener ecout : listeEcouteur) {
			ecout.evenementPersonnage(personnage);
		}
	}
	
	// Par Jeremy 
	/**
	 * Permet de mettre a jour les sorties du mode scientifique
	 */
	private void leverEvenModeScientifique() {
		if(modeScientifique) {
			for (SceneListener ecout : listeEcouteur) {
				ecout.modeScientifiqueListener(listeBalles, HAUTEUR_DU_MONDE);
			}
		}
	}

	//Jeremy Thai
	/**
	 * Ajoute un laser dans la liste de lasers si l'utilisateur appuie sur la bonne touche de souris et qu'il est en mode souris
	 * @param e touche de souris
	 * @param perso personnage
	 */
	private void tirLaser(MouseEvent e, Personnage perso) {
		if (perso.isModeSouris()) {
			if (enCoursAnimation == true) {
				perso.neBougePas();
				listeLasers.add(new Laser(new Vecteur(perso.getPosition() + perso.getLARGEUR_PERSO() / 2,
						HAUTEUR_DU_MONDE - perso.getLONGUEUR_PERSO()), angle, vitesseLaser));
			}
		}
	}

	// -------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	//Jeremy Thai
	/**
	 * 
	 * @param balle
	 */
	private void pouvoirAuHasard(Balle balle) {

		Vecteur position = new Vecteur(balle.getPosition().getX(), balle.getPosition().getY());
		Vecteur accel = new Vecteur(gravite);

		int range = (10 - 1) + 1;     

		int nb =  (int)(Math.random() * range) + 1;

		Pouvoir pouvoir;
		switch (nb) {

		case 1:
			pouvoir = new BoostVitesse(position, accel);
			pouvoir.setCompteurAvantDisparaitre(tempsEcoule + 12);
			listePouvoirs.add(pouvoir);
			son.joue("pouvoirApparait");
			break;

		case 2:
			pouvoir = new Bouclier(position, accel);
			pouvoir.setCompteurAvantDisparaitre(tempsEcoule + 12);
			listePouvoirs.add(pouvoir);
			son.joue("pouvoirApparait");
			break;

		case 3:
			pouvoir = new Ralenti(position, accel);
			pouvoir.setCompteurAvantDisparaitre(tempsEcoule + 12);
			listePouvoirs.add(pouvoir);
			son.joue("pouvoirApparait");
			break;

		case 4:
			pouvoir = new AjoutVie(position, accel);
			pouvoir.setCompteurAvantDisparaitre(tempsEcoule + 12);
			listePouvoirs.add(pouvoir);
			son.joue("pouvoirApparait");
			break;
		}
	}

	//Jeremy Thai
	/**
	 * Ajoute les compteurs de pouvoirs et les fait partir
	 */
	private void ajoutCompteurs() {

		if (vitesseLaser.getY() > vitesseLaserInit.getY())
			compteurVitesse = tempsEcoule + 10;

		if (deltaT < deltaTInit)
			compteurRalenti = tempsEcoule + 10;

		if (personnage.isBouclierActive())
			compteurBouclier = tempsEcoule + 10;
	}

	//Jeremy Thai
	/**
	 * Met a jour la durée de compteurs de pouvoirs et de la rotation des images des balles 
	 */
	private void updateDureeCompteurs() {

		for (Balle balle : listeBalles) {
			balle.updateRotation();
		}
		try {
			for (Pouvoir pouvoir : listePouvoirs) {
				if (pouvoir.getCompteurAvantDisparaitre() <= tempsEcoule)
					listePouvoirs.remove(pouvoir);
			}
		} catch (ConcurrentModificationException e) {
		}

		if (compteurVitesse <= tempsEcoule) {
			vitesseLaser = vitesseLaserInit;
			compteurVitesse = 0;
			personnage.setEnVitesse(false);
		}

		if (compteurRalenti <= tempsEcoule) {
			deltaT = deltaTInit;
			compteurRalenti = 0;
		}

		if (compteurBouclier <= tempsEcoule)
			personnage.setBouclierActive(false);

		if (personnage.getTempsMort() <= tempsEcoule)
			personnage.setMort(false);

	}

	//Jérémy Thai
	/**
	 * Détecte et réalise la collision entre un pouvoir et le personnage
	 */
	private void detectionCollisionPouvoirsPersonnages() {

		for (Pouvoir pouvoir : listePouvoirs) {
			if (enIntersection(pouvoir.getAire(), personnage.getAire())) {
				son.joue("pouvoirActive");
				pouvoir.activeEffet(this);
				ajoutCompteurs();
				listePouvoirs.remove(pouvoir);
			}
		}
	}

	//Jeremy Thai
	/**
	 * Met a jour la position de chaque pouvoir de la liste de pouvoirs
	 */
	private void updateMouvementPouvoirs() {
		for (Pouvoir pouvoir : listePouvoirs) {
			if (pouvoir.getPosition().getY() + pouvoir.getLongueurImg() >= HAUTEUR_DU_MONDE) {
				pouvoir.getPosition().setY(HAUTEUR_DU_MONDE - pouvoir.getLongueurImg());
			} else {
				pouvoir.unPasVerlet(deltaT);
			}

		}
	}

	// -------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	//Jeremy Thai
	/**
	 * Evalue une collision avec le sol ou un mur et modifie la vitesse courante selon la collision
	 */
	private void detectionCollisionMurBalle() {

		for (Balle balle : listeBalles) {

			Vecteur position = new Vecteur(balle.getPosition());
			double diametre = balle.getDiametre();

			if (position.getY() + diametre >= HAUTEUR_DU_MONDE) { // touche le sol
				balle.getVitesse().setY(-balle.getVitesse().getY());
			}

			if (position.getX() + diametre >= LARGEUR_DU_MONDE) {
				if (balle.getVitesse().getX() > 0)
					balle.getVitesse().setX(-balle.getVitesse().getX());

			}
			if (position.getX() <= 0) {
				if (balle.getVitesse().getX() < 0)
					balle.getVitesse().setX(-balle.getVitesse().getX());
			}
		}

	}


	//Jeremy Thai
	/**
	 * Détecte et s'il y a une collision entre le personnage et un mur et s'occupe de la collision
	 */
	private void detectionCollisionPersonnageMur() {

		if (personnage.getPosition() <= 0)
			personnage.setPosition(0);

		if (personnage.getPosition() + personnage.getLARGEUR_PERSO() >= LARGEUR_DU_MONDE)
			personnage.setPosition(LARGEUR_DU_MONDE - personnage.getLARGEUR_PERSO());
	}



	// ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Méthode pour savoir quel prisme entre en collision avec quel laser
	 */
	// Auteur: Arezki Issaadi
	private void collisionLaserPrisme() {

		for (int i = 0; i < listeLasers.size(); i++) {

			for (int j = 0; j < listePrisme.size(); j++) {

				if (enIntersection(listePrisme.get(j).getAirPrisme(), listeLasers.get(i).getAire())) {

					System.out.println(j);
					prisme = listePrisme.get(j);
					laser = listeLasers.get(i);

					j = listePrisme.size();
					i = listeLasers.size();

					calculRefractionPrisme(laser, prisme);
					// premiereFoisPrisme = false;
				}

			}
		}

	}

	/**
	 * Cette méthode fait les calculs pour a réfraction du laser lorsqu'il touche un prisme et crée plusieur lasers comme dans la vraie vie 
	 * @param laser: laser qui sera en collision avec le prisme
	 * @param prismes: prisme qui sera en collision avec le laser
	 */
	// Auteur: Arezki Issaadi
	private void calculRefractionPrisme(Laser laser, Prisme prismes) {

		System.out.println("------------------------------------------------------------------------------");
		Vecteur T = new Vecteur();
		Vecteur V = laser.getPositionHaut();
		Vecteur N = normalPrisme(laser, prismes);
		Vecteur E = V.multiplie(-1);
		double n = 1.0 / prisme.getIndiceRefraction();
		System.out.println("la position laser avant refraction : " + laser.getPositionHaut());

		System.out.println("normal" + N);
		System.out.println("n: " + n);
		System.out.println("position: " + prismes.getP1());
		T = V.multiplie(n).additionne(N.multiplie(
				(n * (E.prodScalaire(N)) - Math.sqrt(1 - Math.pow(n, 2) * (1 - (Math.pow(E.prodScalaire(N), 2)))))));
		T = T.multiplie(-1);
		System.out.println("T: " + T);

		// laser.setPositionHaut(new Vecteur (anciennePosLaser.getX(), T.getY()));
		double angleAncien = laser.getAngleTir();
		System.out.println("l'angle du laser avant refraction: " + angleAncien);

		if (ligne23) {
			laser.setAngleTir(Math.toDegrees(Math.atan(T.getY() / T.getX())) + angle);
		} else {
			laser.setAngleTir(Math.toDegrees(Math.atan(T.getY() / T.getX())));
		}

		System.out.println("l'angle du laser apres refraction: " + laser.getAngleTir());

		System.out.println("la position laser apres refraction : " + laser.getPositionHaut());

		double angleLaser = laser.getAngleTir();
		listeLasers.add(
				new Laser(laser.getPositionHaut().additionne(new Vecteur(0.005, 0)), angleLaser, laser.getVitesse()));

		repaint();

	}

	/**
	 * Cette méthode calcule la normal pour chaque côté du prisme et la retourne en vecteur dans les unités du rélles
	 * @param laser: laser qui sera en collision avec le prisme
	 * @param prisme: le prisme qui sera en collision avec le laser
	 * @return Vecteur: Vecteur en x et en y de la normal du segment du prisme
	 */
	// Auteur: Arezki Issaadi
	private Vecteur normalPrisme(Laser laser, Prisme prisme) {

		// System.out.println("position p1"+ prisme.getP1());
		// System.out.println("position p2"+ prisme.getP2());
		// System.out.println("position p3"+ prisme.getP3());

		double resultat13 = prisme.getLigne13().ptSegDist(laser.getPositionHaut().getX(),
				laser.getPositionHaut().getY());
		double resultat12 = prisme.getLigne12().ptSegDist(laser.getPositionHaut().getX(),
				laser.getPositionHaut().getY());
		double resultat23 = prisme.getLigne23().ptSegDist(laser.getPositionHaut().getX(),
				laser.getPositionHaut().getY());
		// System.out.println("resultat pour la ligne 13:"+resultat13);
		// System.out.println("resultat pour la ligne 12:"+resultat12);
		// System.out.println("resultat pour la ligne 23:"+resultat13);

		if (resultat13 > 0 && resultat13 < 0.80) {
			System.out.println("jai touché ligne13 " + resultat13);
			ligne13 = true;
			ligne12 = false;
			ligne23 = false;

			if (angle > 90) {

				double xNormal = prisme.getP3().getX() - prisme.getP1().getX();
				double yNormal = prisme.getP3().getY() - prisme.getP1().getY();

				return new Vecteur(yNormal, xNormal);

			} else {

				double xNormal = prisme.getP3().getX() - prisme.getP1().getX();
				double yNormal = prisme.getP3().getY() - prisme.getP1().getY();

				return new Vecteur(-yNormal, xNormal);
			}

		} else if (resultat12 > 0 && resultat12 < 0.80) {
			// a refaire le calcul est pas bon

			System.out.println("jai toucher la ligne12 " + resultat12);
			ligne13 = false;
			ligne12 = true;
			ligne23 = false;

			double xNormal = prisme.getP2().getX() - prisme.getP1().getX();
			double yNormal = prisme.getP2().getY() - prisme.getP1().getY();

			return new Vecteur(xNormal, yNormal);

		} else if (resultat23 > 0 && resultat23 < 0.80) {
			System.out.println("jai toucher la ligne23 " + resultat23);
			ligne13 = false;
			ligne12 = false;
			ligne23 = true;

			if (angle > 90) {

				double xNormal = prisme.getP2().getX() - prisme.getP3().getX();
				double yNormal = prisme.getP2().getY() - prisme.getP3().getY();

				return new Vecteur(-yNormal, xNormal);

			} else {

				double xNormal = prisme.getP2().getX() - prisme.getP3().getX();
				double yNormal = prisme.getP3().getY() - prisme.getP2().getY();

				return new Vecteur(-yNormal, xNormal);
			}
		}

		return new Vecteur();

	}

	// -------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	//Jeremy Thai 
	/**
	 * Retourne la vitesse dun laser 
	 * @return vitesseLaser vitesse du laser
	 */
	public Vecteur getVitesseLaser() { return vitesseLaser; }

	//Jeremy Thai
	/**
	 * Retourne le personnage
	 * @return personnage personnage du jeu
	 */
	public Personnage getPersonnage() { return personnage; }

	//Jeremy Thai
	/**
	 * Retourne les coeurs
	 * @return coeurs coeurs 
	 */
	public Coeurs getCoeurs() { return coeurs; }

	//Par Miora
	/**
	 * Cette methode retourne la touche gauche pour deplacer le personnage
	 * @return le KeyCode de la touche gauche
	 */
	public int getToucheGauche() {
		return toucheGauche;
	}

	//Par Miora
	/**
	 * Cette methode retourne la touche droite pour deplacer le personnage
	 * @return le KeyCode de la touche droite
	 */
	public int getToucheDroite() {
		return toucheDroite;
	}

	// -------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	//Jeremy Thai
	/**
	 * Modifie la vitesse dun laser par celle passée en parametre
	 * @param vitesseLaser vitesse du laser
	 */
	public void setVitesseLaser(Vecteur vitesseLaser) { this.vitesseLaser = vitesseLaser; }

	//Jeremy Thai
	/**
	 * Modifie le pas d'incrémentation par celui passé en paramètre
	 * @param deltaT nouveau pas
	 */

	public void setDeltaT(double deltaT) { this.deltaT = deltaT; }
	public void setToucheDroite(int toucheDroite) {
		this.toucheDroite = toucheDroite;
	}

	public void setTempsTotalEcoule(int value) {
		this.tempsEcoule = value;
	}

	public void setToucheGauche(int toucheGauche) {
		this.toucheGauche = toucheGauche;
	}

	public void setSciencePrisme(boolean valeur) {
		prisme.setScience(valeur);
		repaint();
	}

	public void setIndiceRefractionPrisme(double valeur) {
		prisme.setIndiceRefraction(valeur);
		repaint();
	}

	public void setRefractionBloc(double valeur) {

	}

	/**
	 * Cette methode permet de modifier l'angle du laser
	 * 
	 * @param angle: C'est le nouveau angle du laser
	 * 
	 */
	// auteur Arnaud Lefebvre
	public void setAngle(double angle) {
		// System.out.println("Angle: " + angle);
		/*
		 * try { laser.setAngleTir(angle); System.out.println("Angle: " + angle); }
		 * catch (NullPointerException e) {
		 * System.out.println("Laser existe pas, enlevez vos Sysout"); }
		 */
		this.angle = angle;
	}

	/**
	 * Cette méthode permet de modifier l'angle du laser avec la roulette de la souris
	 */
	// Auteur: Arezki Issaadi
	private void setAngleRoulette() {
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				if (arg0.getWheelRotation() == -1 && (valeurAngleRoulette >= 0)) {
					valeurAngleRoulette -= 0.05;
					setAngle(valeurAngleRoulette);

				} else if (arg0.getWheelRotation() == 1 && (valeurAngleRoulette < 180)) {
					valeurAngleRoulette += 0.05;
					setAngle(valeurAngleRoulette);
				}

				enMouvement = true;
			}
		});

		repaint();
	}

	// -------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Methode qui permet d'effacer toutes type de balle
	 */
	// Auteur: Arezki
	public void effacerBalles() {
		listeBalles.removeAll(listeBalles);
		repaint();
	}

	/**
	 * Methode qui permet d'effacer tous les prismes
	 */
	// Auteur: Arezki
	public void effacerPrisme() {
		listePrisme.removeAll(listePrisme);
		repaint();
	}

	/**
	 * Méthode qui permet d'effacer tous les miroirs
	 */
	// Auteur: Arezki
	public void effacerMiroir() {
		listeMiroirCourbe.removeAll(listeMiroirCourbe);
		listeMiroirPlan.removeAll(listeMiroirPlan);
		repaint();
	}

	/**
	 * Methode qui permet d'effacer tous les trou-noira
	 */
	// Auteur: Arezki
	public void effacerTrouNoir() {
		listeTrou.removeAll(listeTrou);
		repaint();
	}

	/**
	 * Permet d'effacer tous les blocs
	 */
	// Auteur: Arezki
	public void effacerBloc() {
		listeBlocEau.removeAll(listeBlocEau);
		repaint();
	}

	/**
	 * Permet d'activer ou de désactiver la fonctionnalité d'effacement precis pour
	 * les objets: Cliquer sur l'obet et ce dernier s'éffacera
	 * 
	 * @param valeur: Boolean pour activer ou désactiver l'effacement des objet avec
	 *        le clique de la souris (true or false)
	 */
	//Auteur: Arezki
	public void effacementPrecis(boolean valeur) {

		effacement = valeur;

	}

	// Par Miora
	/**
	 * Cette methode permet de changer l'angle des miroirs
	 */
	public void setAngleMiroir(int angle) {
		this.angleMiroir = angle;
	}

	/**
	 * Cette methode permet de modifier le temps de jeu
	 * @param tempsDuJeu : le temps du jeu
	 */
	public void setTempsDuJeu(int tempsDuJeu) {
		this.tempsDuJeu = tempsDuJeu;
	}

	//jeremy 
	/**
	 * Modifie la valeur (vrai ou faux) du mode scientifique par celle passee en parametre
	 * @param modeScientifique nouvelle valeur passee en parametre
	 */
	public void setModeScientifique(boolean modeScientifique) {
		this.modeScientifique = modeScientifique;
	}

	//Arezki
	/**
	 * Selectionne les objets en contact avec le curseur de la souris 
	 * @param eXR position x du curseur de la souris 
	 * @param eYR position y du curseur de la souris 
	 */
	private  void selectionneurObjets(double eXR, double eYR) {
		for (int i = 0; i < listeBalles.size(); i++) {
			if ((listeBalles.get(i).getAire().contains(eXR, eYR)) && (!effacement)) {

				balle = listeBalles.get(i);
				System.out.println(balle.getAccel());
				bonneBalle = true;

				i = listeBalles.size();
			} else if ((listeBalles.get(i).getAire().contains(eXR, eYR)) && (effacement)) {
				listeBalles.remove(i);
				repaint();
			}
		}


		for (int i = 0; i < listeMiroirCourbe.size(); i++) {
			if (listeMiroirCourbe.get(i).getAireMiroirCourbe().contains(eXR, eYR)&&(!effacement)) {
				System.out.println("miroir courbe");
				bonMiroirCourbe = true;
				miroirCourbe = listeMiroirCourbe.get(i);
				i = listeMiroirCourbe.size();

			}else if((listeMiroirCourbe.get(i).getAireMiroirCourbe().contains(eXR, eYR))&&(effacement)) {


			}else if((listeMiroirCourbe.get(i).getAireMiroirCourbe().contains(eXR, eYR))&&(effacement)) {
				listeMiroirCourbe.remove(i);
				repaint();
			}
		}

		for (int i = 0; i < listeMiroirPlan.size(); i++) {
			if (listeMiroirPlan.get(i).getAireMiroirPixel().contains(eXR, eYR) && (!effacement)) {

				bonMiroirPlan = true;
				miroirePlan = listeMiroirPlan.get(i);

				i = listeMiroirPlan.size();
			} else if ((listeMiroirPlan.get(i).getAireMiroirPixel().contains(eXR, eYR)) && (effacement)) {
				listeMiroirPlan.remove(i);
				repaint();
			}
		}

		for (int i = 0; i < listeTrou.size(); i++) {
			if (listeTrou.get(i).getAireTrou().contains(eXR, eYR) && (!effacement)) {

				bonTrouNoir = true;
				trou = listeTrou.get(i);

				i = listeTrou.size();
			} else if ((listeTrou.get(i).getAireTrou().contains(eXR, eYR)) && (effacement)) {
				listeTrou.remove(i);
				repaint();
			}
		}

		for (int i = 0; i < listeBlocEau.size(); i++) {
			if (listeBlocEau.get(i).getAireBloc().contains(eXR, eYR) && (!effacement)) {

				bonBlocEau = true;
				bloc = listeBlocEau.get(i);

				i = listeBlocEau.size();
			} else if ((listeBlocEau.get(i).getAireBloc().contains(eXR, eYR)) && (effacement)) {
				listeBlocEau.remove(i);
				repaint();
			}
		}

		for (int i = 0; i < listePrisme.size(); i++) {
			if (listePrisme.get(i).getAirPrisme().contains(eXR, eYR) && (!effacement)) {

				bonPrisme = true;
				prisme = listePrisme.get(i);

				i = listePrisme.size();
			} else if ((listePrisme.get(i).getAirPrisme().contains(eXR, eYR)) && (effacement)) {
				listePrisme.remove(i);
				repaint();
			}
		}
	}
	
}


