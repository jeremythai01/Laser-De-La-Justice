package aaplication;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import geometrie.Vecteur;
import miroir.MiroirConcave;
import miroir.MiroirConvexe;
import miroir.MiroirPlan;
import objets.BlocDEau;
import objets.Echelle;
import objets.TrouNoir;
import personnage.Personnage;
import physique.Balle;
import physique.Coeurs;
import physique.Laser;
import pistolet.Pistolet;
import utilite.ModeleAffichage;

/**
 * Cette classe contient la scene d'animation du jeu.
 * 
 * @author Miora, Arezki, Jeremy
 *
 */
public class Scene extends JPanel implements Runnable {

	private Image fond = null;

	private Personnage principal;

	private static final long serialVersionUID = 1L;

	private double angle;
	private double deltaT = 0.006;
	private double LARGEUR_DU_MONDE = 30; // en metres
	private double HAUTEUR_DU_MONDE;
	private double tempsTotalEcoule = 0;
	private double diametre = 2; // em mètres
	private int tempsDuSleep = 30;
	private int compteurBalle = 0;
	private int nombreVies = 5;
	private int toucheGauche = 37;
	private int toucheDroite = 39;
	private double positionPerso = 0;
	
	private boolean enCoursAnimation = false;
	private boolean premiereFois = true;
	private boolean editeurActiver = false;
	private boolean isGrCercleCliquer = false;
	private boolean isMedCercleCliquer = false;
	private boolean isPetCercleCliquer = false;
	private boolean isCercleCliquer;
	private boolean bonneBalle = false;
	
	private ModeleAffichage modele;
	private AffineTransform mat;
	private Vecteur vitesse = new Vecteur(1,1);

	private ArrayList<Laser> listeLasers = new ArrayList<Laser>();
	private ArrayList<TrouNoir> listeTrou = new ArrayList<TrouNoir>();
	private ArrayList<Balle> listeBalles = new ArrayList<Balle>();
	private ArrayList<MiroirConcave> listeMiroireConcave = new ArrayList<MiroirConcave>();
	private ArrayList<MiroirConvexe> listeMiroireConvexe = new ArrayList<MiroirConvexe>();
	private ArrayList<MiroirPlan> listeMiroirePlan = new ArrayList<MiroirPlan>();
	private ArrayList<BlocDEau> listeBlocEau = new ArrayList<BlocDEau>();

	private Balle balle;
	private TrouNoir trou;
	private Pistolet pistoletPrincipal;
	private Laser laser;
	private MiroirConcave miroirConcave;
	private MiroirConvexe miroireConvexe;
	private MiroirPlan miroirePlan;
	private BlocDEau bloc;
	private Coeurs coeurs = new Coeurs(nombreVies);

	private Echelle echelle;

	private Color couleurLaser = null;
	private boolean couleurPersoLaser = false;

	private Balle grosseBalle = new Balle(new Vecteur(), vitesse, "LARGE") ;
	private Balle moyenneBalle  = new Balle(new Vecteur(1, 0), vitesse, "MEDIUM"); 
	private Balle petiteBalle = new Balle(new Vecteur(2, 2), vitesse, "SMALL") ;

	
	
	


	// Par Jeremy
	/**
	 * Constructeur de la scene et permet de mettre les objets avec le clique de la
	 * souris
	 */

	public Scene(boolean isNouvelleScene) {

		lireFond();
		
		angle = 30;

		pistoletPrincipal = new Pistolet();

		nouvellePartie(isNouvelleScene);
		
		lectureFichierOption();
		vitesse = new Vecteur(3, 0);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

				double eXR = e.getX() / modele.getPixelsParUniteX();
				double eYR = e.getY() / modele.getPixelsParUniteY();
				System.out.println("taille de la liste des balles: "+compteurBalle);
				System.out.println();
				while (!bonneBalle) {
					System.out.println("je suis dans mouse pressed");
					if(listeBalles.get(compteurBalle-1).getAireBalle().contains(eXR, eYR)) {
						
						System.out.println("jai trouver la balle");
						bonneBalle = true;
					}else {
						System.out.println("jai pas trouver la balle :(");
						compteurBalle--;
					}
					dragBalle();
				}
					
				
				
				
				if(moyenneBalle.getAireBalle().contains(eXR, eYR)) {
					isMedCercleCliquer = true;
					isGrCercleCliquer = false;
					isPetCercleCliquer = false;
					dragBalleMedium();
					
				}
						
				else if(grosseBalle.getAireBalle().contains(eXR, eYR)) {
					isGrCercleCliquer = true;
					isMedCercleCliquer = false;
					isPetCercleCliquer = false;
					dragGrosseCercle();
					
				}
				
				else if (petiteBalle.getAireBalle().contains(eXR, eYR)) {
					isPetCercleCliquer = true;
					isGrCercleCliquer = false;
					isMedCercleCliquer = false;
					dragPetiteBalle();
					
				}
						
				/*
				 * balle = new Balle(new Vecteur(eXR - diametre / 2, eYR - diametre / 2),
				 * vitesse, "LARGE"); trou = new TrouNoir(new Vecteur(eXR, eYR)); miroireConvexe
				 * = new MiroirConvexe(eXR, eYR, 1); miroirConcave = new MiroirConcave(new
				 * Vecteur(eXR, eYR),2); bloc = new BlocDEau(new Vecteur(eXR,eYR));
				 * 
				 * listeBalles.add(balle); listeTrou.add(trou);
				 * listeMiroireConcave.add(miroirConcave);
				 * listeMiroireConvexe.add(miroireConvexe); listeBlocEau.add(bloc);
				 */

				repaint();
			}
			
			
			


			@Override
			public void mouseReleased(MouseEvent arg0) {
				if(isMedCercleCliquer&& moyenneBalle.getAireBalle().contains(arg0.getX()/modele.getPixelsParUniteX(), arg0.getY()/modele.getPixelsParUniteY())){
					isMedCercleCliquer = false;
					}
				else if(isGrCercleCliquer&& grosseBalle.getAireBalle().contains(arg0.getX()/modele.getPixelsParUniteX(), arg0.getY()/modele.getPixelsParUniteY())){
					isGrCercleCliquer = false;
				
				}
			
				else if(isPetCercleCliquer&& petiteBalle.getAireBalle().contains(arg0.getX()/modele.getPixelsParUniteX(), arg0.getY()/modele.getPixelsParUniteY())){
					isPetCercleCliquer = false;
				
				}
			
			}
		});

		addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				principal.deplacerLePersoSelonTouche(e);
				tirLaser(e);
				repaint();
			}

			@Override
			// Jeremy Thai
			public void keyReleased(KeyEvent e) {
				principal.relacheTouche(e);
				repaint();
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				
				dragGrosseCercle();
				dragBalleMedium();
				dragPetiteBalle();

				

			}
		});

		
		
	}

	// Par Jeremy
	/**
	 * Méthode qui permet de dessiner toutes les formes sur la scene incluant le
	 * personnage et de savoir s'il y a des collisions entre le laser et les balles
	 * 
	 * @author Arezki
	 */
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


		for (Laser laser : listeLasers) {
			if (laser.getLigneFinY() <= 0)
				listeLasers.remove(laser);
			g2d.setStroke(new BasicStroke(3));
			laser.dessiner(g2d, mat, 0, 0);
		}

		detectionCollisionBalleLaser(listeBalles, listeLasers);
		detectionCollisionTrouLaser(listeLasers);
		detectionCollisionBallePersonnage(listeBalles, principal);
		detectionCollisionBallePersonnage( listeBalles, principal);	


		for (Balle balle : listeBalles) {

			balle.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		}

		for (MiroirConcave miroirC : listeMiroireConcave) {

			miroirC.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		}

		for (MiroirConvexe miroirV : listeMiroireConvexe) {

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

		principal.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		coeurs.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);

		echelle = new Echelle(30.0, LARGEUR_DU_MONDE - 7.5, HAUTEUR_DU_MONDE - 1);
		echelle.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
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

		for (Balle balle : listeBalles) {
			balle.unPasVerlet(deltaT);
		}
		for (Laser laser : listeLasers) {
			laser.move();
			System.out.println("YEET" + laser.getPosition());
		}

		tempsTotalEcoule += deltaT;
		principal.bouge();


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
			calculerUneIterationPhysique();
			repaint();
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
		URL fich = getClass().getClassLoader().getResource("space.jpg");
		if (fich == null) {
			JOptionPane.showMessageDialog(null, "Fichier space.jpg introuvable!");
		} else {
			try {
				fond = ImageIO.read(fich);
			} catch (IOException e) {
				System.out.println("Erreur de lecture du fichier d'image");
			}
		}
	}

	/**
	 * Cette methode permet de modifier l'angle du laser
	 * 
	 * @param angle: C'est le nouveau angle du laser
	 * @author Arnaud
	 */
	public void setAngle(double angle) {
		laser.setAngleTir(angle);
		principal.getPositionX();

	}

	// Par Miora
	/**
	 * Cette methode permet de lire le fichier option de la classe option modifie
	 * avant le debut de la partie
	 */
	private void lectureFichierOption() {
		final String NOM_FICHIER_OPTION = "DonneeOption.d3t";
		ObjectInputStream fluxEntree = null;
		double acceleration = 9.8;
		int niveau = 0;
		File fichierDeTravail = new File(NOM_FICHIER_OPTION);

		try {
			fluxEntree = new ObjectInputStream(new BufferedInputStream(new FileInputStream(fichierDeTravail)));
			niveau = fluxEntree.readInt();
			acceleration = fluxEntree.readDouble();
			toucheGauche = fluxEntree.readInt();
			toucheDroite = fluxEntree.readInt();
			try {
				Color couleurOption;
				couleurOption = (Color) fluxEntree.readObject();
				if(couleurOption == null) {
					couleurPersoLaser = false;
					couleurLaser = null;
					System.out.println("On ne m'a pas donne de couleur");
				}else {
					couleurPersoLaser = true;
					couleurLaser = couleurOption;
					System.out.println("On m'a donné une couleur");
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
				if (enIntersection(balle.getAireBalle(), laser.getLaserAire())) {

					listeLasers.remove(laser);
					System.out.println("balle touche par laser");
					balle.shrink(listeBalles);
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
			if (enIntersection(balle.getAireBalle(), principal.airePersonnage())) {
				if (personnage.getTempsMort() <= tempsTotalEcoule) {
					coeurs.setCombien(nombreVies - 1);
					nombreVies--;
					personnage.setTempsMort(tempsTotalEcoule + 1);

				}
			}

		}

	}

	private void detectionCollisionTrouLaser(ArrayList<Laser> listeLasers) {

		for (Laser laser : listeLasers) {
			for (TrouNoir trou : listeTrou) {
				if (enIntersection(trou.getAireTrou(), laser.getLaserAire())) {
					listeLasers.remove(laser);
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
				principal.neBougePas(); // Pour que 1 laser soit tirer a la fois

				if(couleurPersoLaser == false) {
					System.out.println("tir laser false");
					listeLasers.add(new Laser(new Vecteur(principal.getPositionX() + principal.getLARGEUR_PERSO() / 2,
							HAUTEUR_DU_MONDE - principal.getLONGUEUR_PERSO()), angle, new Vecteur(0, 0.5)));
				}else {
					System.out.println("tir laser false");
					listeLasers.add(new Laser(new Vecteur(principal.getPositionX() + principal.getLARGEUR_PERSO() / 2,
							HAUTEUR_DU_MONDE - principal.getLONGUEUR_PERSO()), angle, new Vecteur(0, 0.5),couleurLaser ));
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

		grosseBalle = new Balle(new Vecteur(), vitesse, "LARGE");
		listeBalles.add(grosseBalle);
		compteurBalle++;
		repaint();

	}

	/**
	 * Arezki Issaadi permet d'ajouter et de dessiner une balle medium en appuyant
	 * sur le boutton medium balle
	 */
	public void ajoutBalleMedium() {

		moyenneBalle = new Balle(new Vecteur(1, 0), vitesse, "MEDIUM");
		listeBalles.add(moyenneBalle);
		compteurBalle++;
		repaint();

	}

	/**
	 * Arezki Issaadi permet d'ajouter et de dessiner une petite balle en appuyant
	 * sur le boutton petite balle
	 */
	public void ajoutBallePetite() {

		//System.out.println("avant :"+petiteBalle.toString());
		petiteBalle = new Balle(new Vecteur(2, 2), vitesse, "SMALL");
		listeBalles.add(petiteBalle);
		compteurBalle++;
		repaint();

	}

	/**
	 * Arezki Issaadi permet d'ajouter et de dessiner un miroir concave en appuyant
	 * sur le boutton miroire concave
	 */
	public void ajoutMiroireConcave() {
		listeMiroireConcave.add(new MiroirConcave(new Vecteur(3, 0), 2));
		repaint();
	}

	/**
	 * Arezki Issaadi permet d'ajouter et de dessiner un miroir convexe en appuyant
	 * sur le boutton miroire convexe
	 */
	public void ajoutMiroireConvexe() {
		listeMiroireConvexe.add(new MiroirConvexe(new Vecteur(3, 0), 1));
		repaint();
	}

	/**
	 * Arezki Issaadi permet d'ajouter et de dessiner un miroir plan en appuyant sur
	 * le boutton miroire plan
	 */
	public void ajoutMiroirPlan() {
		listeMiroirePlan.add(new MiroirPlan(5, 0, 0));
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

	/**
	 * Arezki Issaadi permet d'ajouter et de dessiner un bloc d'eau en appuyant sur
	 * le boutton Bloc d'eau
	 */
	public void ajoutBlocEau() {
		listeBlocEau.add(new BlocDEau(new Vecteur(9, 0)));
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
		listeMiroireConcave.removeAll(listeMiroireConcave);
		listeMiroireConvexe.removeAll(listeMiroireConvexe);
		listeMiroirePlan.removeAll(listeMiroirePlan);
		listeTrou.removeAll(listeTrou);
		repaint();
	}

	
	private void dragBalle() {
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (isCercleCliquer && !enCoursAnimation) {
					System.out.println("je suis dans dragMed");
					double xDrag = e.getX() / modele.getPixelsParUniteX();
					double yDrag = e.getY() / modele.getPixelsParUniteY();
					listeBalles.get(compteurBalle).setPosition(new Vecteur(xDrag - diametre / 2, yDrag - diametre / 2));
					
					repaint();
				}
				

			}
		});
		
	}
	
	
	private void dragGrosseCercle() {
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (isGrCercleCliquer && !enCoursAnimation) {
					System.out.println("je suis dans dragGrosse");
					double xDrag = e.getX() / modele.getPixelsParUniteX();
					double yDrag = e.getY() / modele.getPixelsParUniteY();
					grosseBalle.setPosition(new Vecteur(xDrag - diametre / 2, yDrag - diametre / 2));
					repaint();
				}
				

			}
		});
	}

	private void dragBalleMedium() {
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (isMedCercleCliquer && !enCoursAnimation) {
					System.out.println("je suis dans dragMed");
					double xDrag = e.getX() / modele.getPixelsParUniteX();
					double yDrag = e.getY() / modele.getPixelsParUniteY();
					moyenneBalle.setPosition(new Vecteur(xDrag - diametre / 2, yDrag - diametre / 2));
					
					repaint();
				}
				

			}
		});
	}

	private void dragPetiteBalle() {
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (isPetCercleCliquer && !enCoursAnimation) {

					double xDrag = e.getX() / modele.getPixelsParUniteX();
					double yDrag = e.getY() / modele.getPixelsParUniteY();
					petiteBalle.setPosition(new Vecteur(xDrag - diametre / 2, yDrag - diametre / 2));

				}
				repaint();

			}
		});
	}

	//Miora
	/**
	 * Cette methode permet de sauvegarder le nombre de vie, le nombre des balles, la position du joueur, 
	 * la couleur du rayon et les touches utilisées
	 */
	public void ecritureFichierSauvegarde(){
		final String NOM_FICHIER_OPTION = "sauvegarde.d3t";
		File fichierDeTravail = new File(NOM_FICHIER_OPTION);

		ObjectOutputStream fluxSortie = null;
		try {
			fluxSortie = new ObjectOutputStream(new FileOutputStream(fichierDeTravail));
			fluxSortie.writeInt(nombreVies); // nombre de vie
			fluxSortie.writeObject(listeBalles);		// la liste des balles
			fluxSortie.writeDouble(principal.getPositionX());			// les caracteristiques du personnage
			if(couleurLaser == null) {
				fluxSortie.writeObject(null);
			}else {
				fluxSortie.writeObject(couleurLaser);
			}// la couleur du rayon
			fluxSortie.writeInt(toucheGauche);			// la touche gauche
			fluxSortie.writeInt(toucheDroite);			// la touche droite
			JOptionPane.showMessageDialog(null,"Votre partie a ete sauvegarde");
		} 
		catch (IOException e) {
			System.out.println("Erreur lors de l'écriture!");
			e.printStackTrace();
		}
		finally {
			//on exécutera toujours ceci, erreur ou pas
			try { 
				fluxSortie.close();  
			}
			catch (IOException e) { 
				System.out.println("Erreur rencontrée lors de la fermeture!"); 
			}
		}//fin finally
	}

	// Par Miora
	/**
	 * Cette methode permet de lire le fichier qui sauvegarde le  nombre de vie, le nombre des balles, la position du joueur, 
	 * la couleur du rayon et les touches utilisées
	 */
	private void lectureFichierSauvegarde(String nomFichier) {
		final String NOM_FICHIER_OPTION = nomFichier;
		ObjectInputStream fluxEntree = null;
		File fichierDeTravail = new File(NOM_FICHIER_OPTION);

		try {
			fluxEntree = new ObjectInputStream(new FileInputStream(fichierDeTravail));
			nombreVies = fluxEntree.readInt();
			try {
				listeBalles = (ArrayList<Balle>) fluxEntree.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			positionPerso = fluxEntree.readDouble();
			try {
				couleurLaser = (Color) fluxEntree.readObject();
				if(couleurLaser == null) {
					couleurPersoLaser = false;
				}else {
					couleurPersoLaser = true;
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			toucheGauche = fluxEntree.readInt();
			toucheDroite = fluxEntree.readInt();
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

	//Par Miora 
	/**
	 *Cette methode definie si la scene est une nouvelle scene ou une scene charge 
	 * @param isNouvelle : retourne vrai s'il s'agit d'une nouvelle scene
	 */
	private void nouvellePartie(boolean isNouvelle) {
		if(!isNouvelle) {
			System.out.println("je suis une partie charge");
			lectureFichierSauvegarde("sauvegarde.d3t");
			coeurs.setCombien(nombreVies);
			principal = new Personnage (positionPerso,toucheGauche, toucheDroite );
		}else {
			principal = new Personnage(LARGEUR_DU_MONDE/2, toucheGauche, toucheDroite);
			coeurs.setCombien(nombreVies);
		}
	}
}
