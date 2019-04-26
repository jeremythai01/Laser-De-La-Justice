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
import utilite.ModeleAffichage;
import utilite.OutilsMath;

/**
 * Cette classe contient la scene du tutoriel.
 * 
 * @author Arnaud
 *
 */
//Cette classe est plus avancee, mais jai completement oublie de push avant de venir au cours du mercredi 24 avril, cette classe parait donc comme la scenePrincipale en terme de javadoc
public class SceneTutoriel extends JPanel implements Runnable {

	private Image fond = null;

	private Personnage personnage;

	private static final long serialVersionUID = 1L;

	private double angle;

	private double LARGEUR_DU_MONDE = 35; // en metres
	private double HAUTEUR_DU_MONDE;
	private double diametre = 2; // em mètres
	private int tempsDuSleep = 30;
	private int nombreVies = 5;
	private int toucheGauche = 37;
	private double n2 = 2.00;
	private int compteur=0;
	private double qtRotation=0;
	
	private int toucheDroite = 39;
	private double positionPerso = 0;
	private float valeurAngleRoulette = 90;

	private final int TOUCHE_GAUCHE_INI = 37;
	private final int TOUCHE_DROITE_INI = 39;

	private boolean enCoursAnimation = false;
	private boolean premiereFois = true;
	private boolean isGrCercleCliquer = false;
	private boolean isMedCercleCliquer = false;
	private boolean isPetCercleCliquer = false;
	private boolean bonneBalle = false;
	private boolean bonMiroirConvexe = false;
	private boolean bonMiroirConcave = false;
	private boolean bonMiroirPlan = false;
	private boolean bonTrouNoir = false;
	private boolean bonBlocEau = false;
	private boolean editeurActiver = false;
	private boolean couleurPersoLaser = false;
	private boolean bonPrisme = false;
	private boolean ligne13 = false;
	private boolean ligne23 = false;
	private boolean ligne12 = false;
	private boolean triche=false;

	private ModeleAffichage modele;
	private AffineTransform mat;
	private Vecteur vitesse = new Vecteur(1, 1);
	private Vecteur accBalle;

	private ArrayList<Laser> listeLasers = new ArrayList<Laser>();
	private ArrayList<TrouNoir> listeTrou = new ArrayList<TrouNoir>();
	private ArrayList<Balle> listeBalles = new ArrayList<Balle>();
	private ArrayList<MiroirCourbe> listeMiroireConvexe = new ArrayList<MiroirCourbe>();
	private ArrayList<MiroirPlan> listeMiroirePlan = new ArrayList<MiroirPlan>();
	private ArrayList<BlocDEau> listeBlocEau = new ArrayList<BlocDEau>();
	private ArrayList<Prisme> listePrisme = new ArrayList<Prisme>();
	private ArrayList<Pouvoir> listePouvoirs = new ArrayList<Pouvoir>();

	private Balle balle;
	private TrouNoir trou;
	private MiroirCourbe miroireConvexe;
	private MiroirPlan miroirePlan;
	private BlocDEau bloc;
	private OrdinateurNiveau3 ordi;
	private Laser laser;
	private Coeurs coeurs = new Coeurs(nombreVies);
	private Prisme prisme ;

	private Echelle echelle;
	private Color couleurLaser = null;

	private Vecteur gravite = new Vecteur (0,9.8); // pour miora 


	private Balle grosseBalle = new Balle(new Vecteur(), vitesse, "LARGE", gravite);
	private Balle moyenneBalle = new Balle(new Vecteur(1, 0), vitesse, "MEDIUM", gravite);
	private Balle petiteBalle = new Balle(new Vecteur(2, 2), vitesse, "SMALL", gravite);


	private ArrayList<SceneListener> listeEcouteur = new ArrayList<SceneListener>();

	private int toucheTir = 32;

	private boolean enMouvement = false;
	private boolean etape1=false;
	private boolean etape2=false;
	private boolean etape3=false;
	private boolean etape4=false;

	private double tempsEcoule = 0;
	private double deltaTInit = 0.06;
	private double deltaT = deltaTInit;
	
	private Vecteur vitesseLaserInit = new Vecteur(0,0.5);
	private Vecteur vitesseLaser = vitesseLaserInit;
	private double compteurVitesse = 0 ;
	private double compteurRalenti= 0 ;
	private double compteurBouclier= 0 ;



	// Par Arnaud
	/**
	 * Constructeur de la scene du tutoriel
	 * 
	 *
	 */

	public SceneTutoriel() {

		/*etape 1: bouger
		 *etape 2: tirer
		 *etape 3 changer langle
		 *etape 4 tirer la boule
		 *BRAVO vous etes pret a jouer
		 */


		lireFond();
	//	lectureFichierOption();

	/*	addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if(etape1) {
				personnage.deplacerLePersoSelonTouche(e);
				etape1=false;
				etape2=true;
				}
			
				if(etape2) {
					tirLaser(e);
					etape2=false;
					etape3=true;
				}
				if(etape3) {
				changerAngle(e);
				etape3=false;
				etape2=true;
				}
				//modeTriche();
			}
		});*/
		



		

		

		

		

	}
	public void etape1() {
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				personnage.deplacerLePersoSelonTouche(e);
				if(personnage.isEnVitesse())etape1=true;
			}
		});
		
		
	}
	public boolean getEtape1() {
		return etape1;
	}

	/**
	 * Méthode qui permet de dessiner toutes les formes sur la scene incluant le
	 * personnage et de savoir s'il y a des collisions entre le laser et les balles
	 * 
	 */
	//Jeremy
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		if (premiereFois) {
			modele = new ModeleAffichage(getWidth(), getHeight(), LARGEUR_DU_MONDE);
			mat = modele.getMatMC();
			HAUTEUR_DU_MONDE = modele.getHautUnitesReelles();
			premiereFois = false;
		}

		g2d.drawImage(fond, 0, 0, (int) modele.getLargPixels(), (int) modele.getHautPixels(), null);


		try {
			for (Laser laser : listeLasers) {
				if (laser.getPositionHaut().getY() <= 0)
					listeLasers.remove(laser);
			}

		}catch (ConcurrentModificationException e) {
		}
		try {
			for (Laser laser : listeLasers) {
				laser.dessiner(g2d, mat, 0, 0);
			}
		}catch (ConcurrentModificationException e) {
		}


		detectionCollisionBalleLaser(listeBalles, listeLasers);
		//detectionCollisionTrouLaser(listeLasers);
		detectionCollisionBallePersonnage(listeBalles, personnage);
		detectionCollisionMurBalle();
		for (Balle balle : listeBalles) {

			balle.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		}

		for (MiroirCourbe miroirV : listeMiroireConvexe) {

			miroirV.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		}

		for (MiroirPlan miroirP : listeMiroirePlan) {

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

		
		
		personnage.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		coeurs.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);

		//echelle = new Echelle(LARGEUR_DU_MONDE, LARGEUR_DU_MONDE - 3.5, HAUTEUR_DU_MONDE - 0.75);
		//echelle.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);

		//ordi= new OrdinateurNiveau3(new Vecteur(28,LARGEUR_DU_MONDE-10));
		//ordi.ajouterListesObstacles(listeBalles);
		//ordi.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		//ordi.savoirTempsSleep(tempsDuSleep);

		tracerVecteurGraphique(g2d);
		dessinerTexte(g2d);

		

	}
	
	public void dessinerTexte(Graphics2D g2d) {
		if(etape1) {
			g2d.drawString("BOuger le personnage a laide des fleches!!", 20,20);
		}
		if(etape2) {
			g2d.drawString("Tirer un laser!!", 20,20);
		}
		if(etape3) {
			g2d.drawString("Changer langle du laser a laide des flehces", 20,20);
		}
	}

	/**
	 * Cette méthode permet d'arreter l'animation
	 * 
	 * @author Jeremy
	 */
	public void arreter() {
		if (enCoursAnimation)
			enCoursAnimation = false;
	}

	/**
	 * Cette méthode permet de démarrer l'animation
	 * 
	 * @author Jeremy
	 */
	public void demarrer() {
		if (!enCoursAnimation) {
			Thread proc = new Thread(this);
			proc.start();
			enCoursAnimation = true;
		}

	}

	/**
	 * permet de calculer les collision la vitessse des balles et tout autres
	 * animation ayant de la physiques
	 * 
	 * @author Jeremy
	 */
	public void calculerUneIterationPhysique() {


		for (int i = 0; i < listeBalles.size(); i++) {
			Balle balle1 = listeBalles.get(i);
			for (int j = i+1; j < listeBalles.size(); j++) {
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


		

		if(personnage.isEnVitesse()){
			personnage.bouge();
			personnage.bouge();
			personnage.bouge();
			personnage.bouge();
		}else {
			personnage.bouge();
		}
		//ordi.bouge();

		tempsEcoule += deltaTInit;

	}

	@Override
	/**
	 * Animation du bloc-ressort
	 * 
	 * @author Jeremy
	 */
	public void run() {
		// TODO Auto-generated method stub
		while (enCoursAnimation) {
			compteur++;
			calculerUneIterationPhysique();
			qtRotation=qtRotation+0.2;
			for(TrouNoir trou : listeTrou) {
				//trou.savoirQuantiteRotation(qtRotation);
			}
			try {
				
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			

			if(compteur==60) {
				tirer();
				compteur=0;
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

		/*URL fich = getClass().getClassLoader().getResource("mars.png");
		if (fich == null) {
			JOptionPane.showMessageDialog(null, "Fichier introuvable!");
		} else {
			try {
				fond = ImageIO.read(fich);
			} catch (IOException e) {
				System.out.println("Erreur de lecture du fichier d'image");
			}
		}*/

		fond=Toolkit.getDefaultToolkit().createImage(getClass().getClassLoader().getResource("space.gif"));
	}

	/**
	 * Cette methode permet de modifier l'angle du laser
	 * 
	 * @param angle: C'est le nouveau angle du laser
	 * 
	 */
	//auteur Arnaud Lefebvre
	public void setAngle(double angle) {
		// System.out.println("Angle: " + angle);
		/*
		 * try { laser.setAngleTir(angle); System.out.println("Angle: " + angle); }
		 * catch (NullPointerException e) {
		 * System.out.println("Laser existe pas, enlevez vos Sysout"); }
		 */
		this.angle = angle;
	}

	// Par Miora
	/**
	 * Cette methode permet de lire le fichier option de la classe option modifie
	 * avant le debut de la partie
	 */
	private void lectureFichierOption() {
		System.out.println("je suis lecture option");
		final String NOM_FICHIER_OPTION = "DonneeOption.d3t";
		ObjectInputStream fluxEntree = null;
		int niveau = 0;
		File fichierDeTravail = new File(NOM_FICHIER_OPTION);

		try {
			fluxEntree = new ObjectInputStream(new BufferedInputStream(new FileInputStream(fichierDeTravail)));
			niveau = fluxEntree.readInt();
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
				personnage = new Personnage(LARGEUR_DU_MONDE / 2, toucheGauche, toucheDroite, toucheTir);

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

				if (personnage.isBouclierActive()) {
					personnage.setBouclierActive(false);
				}else if (personnage.isMort() == false) {
					if(triche==false) {
					coeurs.setCombien(nombreVies - 1);
					nombreVies--;
					personnage.setTempsMort(tempsEcoule+2);
					personnage.setMort(true);
					}
				}
			}
		}
	}


	
	

	// Jeremy Thai
	/**
	 * Ajoute un laser dans la liste de lasers si l'utilisateur appuie sur la bonne
	 * touche de clavier
	 * 
	 * @param e touche du clavier
	 */
	private void tirLaser(KeyEvent e) {
		if (enCoursAnimation) {
			int code = e.getKeyCode();
			if (code == KeyEvent.VK_SPACE) {
				personnage.neBougePas(); // Pour que 1 laser soit tirer a la fois

				if (couleurPersoLaser == false) {
					System.out.println("tir laser false");
					listeLasers.add(new Laser(new Vecteur(personnage.getPositionX() + personnage.getLARGEUR_PERSO() / 2,
							HAUTEUR_DU_MONDE - personnage.getLONGUEUR_PERSO()), angle, vitesseLaser));
				} else {
					System.out.println("tir laser false");
					listeLasers.add(new Laser(
							new Vecteur(personnage.getPositionX() + personnage.getLARGEUR_PERSO() / 2,
									HAUTEUR_DU_MONDE - personnage.getLONGUEUR_PERSO()),
							angle, vitesseLaser, couleurLaser));
				}

			}
		}
	}

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

	/**
	 * Arezki Issaadi permet d'ajouter et de dessiner une grosse balle en appuyant
	 * sur le boutton grosse balle
	 */
	public void ajoutBalleGrosse() {

		grosseBalle = new Balle(new Vecteur(), vitesse, "LARGE", gravite);
		listeBalles.add(grosseBalle);
		repaint();

	}

	/**
	 * Arezki Issaadi permet d'ajouter et de dessiner une balle medium en appuyant
	 * sur le boutton medium balle
	 */
	public void ajoutBalleMedium() {

		moyenneBalle = new Balle(new Vecteur(1, 0), vitesse, "MEDIUM", gravite);
		listeBalles.add(moyenneBalle);

		repaint();

	}

	/**
	 * Arezki Issaadi permet d'ajouter et de dessiner une petite balle en appuyant
	 * sur le boutton petite balle
	 */
	public void ajoutBallePetite() {

		// System.out.println("avant :"+petiteBalle.toString());
		petiteBalle = new Balle(new Vecteur(2, 2), vitesse, "SMALL", gravite);
		listeBalles.add(petiteBalle);

		repaint();

	}



	/**
	 * Arezki Issaadi permet d'ajouter et de dessiner un trou noir en appuyant sur
	 * le boutton Trou noir
	 */
	public void ajoutTrouNoir() {
		listeTrou.add(new TrouNoir(new Vecteur(7, 0)));
		repaint();

	}


	public void ajoutPrisme() {
		listePrisme.add(new Prisme(new Vecteur(2,2)));
		repaint();

	}
	
	/**
	 * Arezki Issaadi permet d'ajouter et de dessiner un bloc d'eau en appuyant sur
	 * le boutton Bloc d'eau
	 */
	public void ajoutBlocEau() {
		//listeBlocEau.add(new BlocDEau(new Vecteur(9, 0)));
		repaint();

	}

	/**
	 * Arezki Issaadi permet aux dessins de safficher a chaque clic
	 */
	public void ActiverEditeur() {
		editeurActiver = true;
		repaint();
	}

	/**
	 * Arezki Issaadi permet d'empecher que les dessins saffichent a chaque clic
	 */

	public void DesactiverEditeur() {
		editeurActiver = false;

	}

	/**
	 * @author Arezki efface tous les dessins sur la scene en effaçant tous les
	 *         objets dans les listes. Agisse comme une corbeille
	 */
	public void reinitialiserDessin() {
		listeBalles.removeAll(listeBalles);
		listeBlocEau.removeAll(listeBlocEau);
		listeMiroireConvexe.removeAll(listeMiroireConvexe);
		listeMiroirePlan.removeAll(listeMiroirePlan);
		listeTrou.removeAll(listeTrou);
		repaint();
	}

	
	
	/**
	 * Methode qui permet de changer l'angle de tir si le joueur enfonce les fleches
	 * @param e, la touche que le joueur a enfoncée
	 */
	//auteur Arnaud Lefebvre
	private void changerAngle(KeyEvent e) {
		if(e.getKeyCode()==38 && angle<180) {
			angle=angle+45;
			if(angle>180)angle=180;
		}
		if(e.getKeyCode()==40 && angle>0) {
			angle=angle-45;
			if(angle<0)angle=0;
		}
		enMouvement=true;
	}
	
	/**
	 * Methode qui indique aux ordis de tirer
	 */
	//auteur Arnaud Lefebvre
	private void tirer() {

		//ordi.ajouterListesObstacles(listeBalles);
		//	listeLasers.add(ordi.tirer());
		//	listeLasers.add(ordi2.tirer());
		//listeLasers.add(ordi.tirer());
		//listeLasers.add(new Laser(new Vecteur(ordi.getPositionX()+ordi.getLargeurOrdi()/2,HAUTEUR_DU_MONDE-ordi.getLongueurOrdi()), angle, new Vecteur(0,0.5)));

	}

	
	
	/**
	 * Methode qui permet de tracer un vecteur qui indique l'angle de tir du fusil
	 * @param g, le composant graphique
	 */
	// Arnaud Lefebvre
	private void tracerVecteurGraphique(Graphics2D g) {
		if (enMouvement) {
			g.setColor(Color.red);
			Path2D.Double trace = new Path2D.Double();
			trace.moveTo(personnage.getPositionX() + personnage.getLARGEUR_PERSO() / 2,
					HAUTEUR_DU_MONDE - personnage.getLONGUEUR_PERSO());
			trace.lineTo(
					personnage.getPositionX() + personnage.getLARGEUR_PERSO() / 2 + 2 * Math.cos(Math.toRadians(angle)),
					HAUTEUR_DU_MONDE - personnage.getLONGUEUR_PERSO() - 2 * Math.sin(Math.toRadians(angle)));
			g.draw(mat.createTransformedShape(trace));
		}
	}

	

	


	
	public void addSceneListener(SceneListener ecouteur) {
		listeEcouteur.add(ecouteur);
	}

	public int getToucheGauche() {
		return toucheGauche;
	}

	public void setToucheGauche(int toucheGauche) {
		this.toucheGauche = toucheGauche;
	}

	public int getToucheDroite() {
		return toucheDroite;
	}

	public void setToucheDroite(int toucheDroite) {
		this.toucheDroite = toucheDroite;
	}

	public void setTempsTotalEcoule(int value) {
		this.tempsEcoule = value;
	}

	private void tirLaser(MouseEvent e, Personnage perso) {
		if(perso.isModeSouris()) {
			if(enCoursAnimation == true) {
				perso.neBougePas();
				listeLasers.add(
						new Laser(new Vecteur(
								perso.getPositionX()+perso.getLARGEUR_PERSO()/2,HAUTEUR_DU_MONDE-perso.getLONGUEUR_PERSO() ) , angle, vitesseLaser));
			}
		}
	}

	




	public void setVitesseLaser(Vecteur vitesseLaser) {
		this.vitesseLaser = vitesseLaser;
	}


	public Vecteur getVitesseLaser() {
		return vitesseLaser;
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

			if(position.getX()+diametre >= LARGEUR_DU_MONDE ) {
				if(balle.getVitesse().getX() >0)
					balle.getVitesse().setX(-balle.getVitesse().getX());

			}
			if(position.getX() <= 0 ) {
				if(balle.getVitesse().getX() < 0)
					balle.getVitesse().setX(-balle.getVitesse().getX());
			}
		}

	}
	
	
	
}
