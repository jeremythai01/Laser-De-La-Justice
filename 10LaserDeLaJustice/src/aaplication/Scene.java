package aaplication;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.Image;
import java.awt.Shape;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

import pistolet.Pistolet;

import physique.Coeurs;
import physique.Laser;
import pistolet.Pistolet;
import utilite.ModeleAffichage;
import java.awt.event.MouseMotionAdapter;

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
	private double deltaT = 0.06;
	private double LARGEUR_DU_MONDE = 30; // en metres
	private double HAUTEUR_DU_MONDE;

	private double tempsTotalEcoule = 0;
	private double diametre = 2; // em mètres

	private boolean enCoursAnimation = false;
	private boolean premiereFois = true;
	private boolean editeurActiver = false;

	private ModeleAffichage modele;
	private AffineTransform mat;
	private Vecteur vitesse;

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
	private int nombreVies=5;
	private Coeurs coeurs = new Coeurs(nombreVies);

	private Echelle echelle;

	private int toucheGauche = 37;
	private int toucheDroite = 39;
	private int tempsDuSleep = 30;

	// Par Jeremy
	/**
	 * Constructeur de la scene et permet de mettre les objets avec le clique de la souris 
	 */

	public Scene() {
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				System.out.println("mouse is being dragged at location (" + e.getX() / modele.getPixelsParUniteX()
						+ ", " + e.getY() / modele.getPixelsParUniteY() + ")");

			}
		});
		lireFond();
		lectureFichierOption();
		angle = -90;
		principal = new Personnage(toucheGauche, toucheDroite);
		pistoletPrincipal = new Pistolet();

		vitesse = new Vecteur(3, 0);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

				double eXR = e.getX() / modele.getPixelsParUniteX();
				double eYR = e.getY() / modele.getPixelsParUniteY();

				/*balle = new Balle(new Vecteur(eXR - diametre / 2, eYR - diametre / 2), vitesse, "LARGE");
				trou = new TrouNoir(new Vecteur(eXR, eYR));
				miroireConvexe = new MiroirConvexe(eXR, eYR, 1);
				miroirConcave = new MiroirConcave(new Vecteur(eXR, eYR),2);
				bloc = new BlocDEau(new Vecteur(eXR,eYR));

				listeBalles.add(balle);
				listeTrou.add(trou);
				listeMiroireConcave.add(miroirConcave);
				listeMiroireConvexe.add(miroireConvexe);
				listeBlocEau.add(bloc);*/

				repaint();
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

	}
	// Par Jeremy
	/**
	 * Méthode qui permet de dessiner toutes les formes sur la scene incluant le personnage
	 * et de savoir s'il y a des collisions entre le laser et les balles  
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

		for(BlocDEau blocE : listeBlocEau) {
			blocE.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		}


		principal.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		coeurs.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);

		echelle = new Echelle(30.0,LARGEUR_DU_MONDE-7.5, HAUTEUR_DU_MONDE-1);
		echelle.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
	}

	/**
	 * Cette méthode permet d'arreter l'animation
	 * @author Jeremy
	 */
	public void arreter() {
		if (enCoursAnimation)
			enCoursAnimation = false;
	}

	/**
	 * Cette méthode permet de démarrer l'animation
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
	 *  permet de calculer les collision la vitessse des balles et tout autres animation ayant de la physiques
	 *  @author Jeremy
	 */
	private void calculerUneIterationPhysique() {

		detectionCollisionBalleLaser(listeBalles, listeLasers);
		detectionCollisionTrouLaser(listeLasers);
		detectionCollisionBallePersonnage( listeBalles, principal);	
	
		for (Balle balle : listeBalles) {
			balle.unPasEuler(deltaT);
		}

		for (Laser laser : listeLasers) {
			laser.move();
		}
		
		tempsTotalEcoule += deltaT;
		principal.bouge();
		
		
	}

	@Override
	/** 
	 * Animation du bloc-ressort
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

	//Par Miora
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
		DataInputStream fluxEntree = null;
		double acceleration = 9.8;
		int niveau = 0;
		File fichierDeTravail = new File(NOM_FICHIER_OPTION);

		try {
			fluxEntree = new DataInputStream(new BufferedInputStream(new FileInputStream(fichierDeTravail)));
			niveau = fluxEntree.readInt();
			acceleration = fluxEntree.readDouble();
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
	 * @param listeBalles liste de balles
	 * @param listeLasers liste de lasers
	 * @param character personnage 
	 */
	private void detectionCollisionBalleLaser(ArrayList<Balle> listeBalles, ArrayList<Laser> listeLasers) {

		for (Laser laser : listeLasers) {
			for (Balle balle : listeBalles) {
				if (enIntersection(balle.getAireBalle(), laser.getLaserAire())) {
					
					listeLasers.remove(laser);
					System.out.println("balle touche par laser");
					//balle.shrink(listeBalles);
				}
			}
		}
	}

	// Jeremy Thai
		/**
		 * Fait la detection d une collision entre toutes les balles et le personnage
		 * @param listeBalles liste de balles
		 * @param character personnage 
		 */
		private void detectionCollisionBallePersonnage(ArrayList<Balle> listeBalles, Personnage personnage) {

				for (Balle balle : listeBalles) {
					
					if(enIntersection(balle.getAireBalle(), principal.airePersonnage())) {
						if(personnage.getTempsMort() <= tempsTotalEcoule) {
						coeurs.setCombien(nombreVies-1);
						nombreVies--;
						personnage.setTempsMort(tempsTotalEcoule*2);
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
	 * Ajoute un laser dans la liste de lasers si l'utilisateur appuie sur la bonne touche de clavier
	 * @param e touche du clavier 
	 */
	private void tirLaser(KeyEvent e) {
		if (enCoursAnimation) {
			int code = e.getKeyCode();
			if (code == KeyEvent.VK_SPACE) {
				principal.neBougePas(); // Pour que 1 laser soit tirer a la fois
				listeLasers.add(new Laser(
						new Vecteur(principal.getPositionX() + principal.getLARGEUR_PERSO() / 2, LARGEUR_DU_MONDE - principal.getLONGUEUR_PERSO()),
						angle, new Vecteur(0, 0.5)));

			}
		}
	}

	// Jeremy Thai
	/**
	 * Retourne vrai si deux aires de formes sont en intersection, sinon faux.
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
	 * Arezki Issaadi
	 * permet d'ajouter et de dessiner une grosse balle en appuyant sur le boutton grosse balle 
	 */
	public void ajoutBalleGrosse() {
		listeBalles.add(new Balle(new Vecteur(), vitesse, "LARGE"));
		repaint();

	}

	/**
	 * Arezki Issaadi
	 * permet d'ajouter et de dessiner une balle medium en appuyant sur le boutton medium balle 
	 */
	public void ajoutBalleMedium() {
		listeBalles.add(new Balle(new Vecteur(1, 0), vitesse, "MEDIUM"));
		repaint();

	}

	/**
	 * Arezki Issaadi
	 * permet d'ajouter et de dessiner une petite balle en appuyant sur le boutton petite balle 
	 */
	public void ajoutBallePetite() {
		listeBalles.add(new Balle(new Vecteur(2, 2), vitesse, "SMALL"));
		repaint();

	}

	/**
	 * Arezki Issaadi
	 * permet d'ajouter et de dessiner un miroir concave en appuyant sur le boutton miroire concave  
	 */
	public void ajoutMiroireConcave() {
		listeMiroireConcave.add(new MiroirConcave(new Vecteur(3, 0),2));
		repaint();
	}

	/**
	 * Arezki Issaadi
	 *  permet d'ajouter et de dessiner un miroir convexe en appuyant sur le boutton miroire convexe  
	 */
	public void ajoutMiroireConvexe() {
		listeMiroireConvexe.add(new MiroirConvexe(new Vecteur(3, 0), 1));
		repaint();
	}

	/**
	 * Arezki Issaadi
	 *  permet d'ajouter et de dessiner un miroir plan en appuyant sur le boutton miroire plan
	 */
	public void ajoutMiroirPlan() {
		listeMiroirePlan.add(new MiroirPlan(5, 0, 0));
		repaint();

	}

	/**
	 * Arezki Issaadi
	 *  permet d'ajouter et de dessiner un trou noir en appuyant sur le boutton Trou noir
	 */
	public void ajoutTrouNoir() {
		listeTrou.add(new TrouNoir(new Vecteur(7, 0)));
		repaint();

	}

	/**
	 * Arezki Issaadi
	 *  permet d'ajouter et de dessiner un bloc d'eau en appuyant sur le boutton Bloc d'eau
	 */
	public void ajoutBlocEau() {
		listeBlocEau.add(new BlocDEau(new Vecteur (9,0)));
		repaint();

	}
	/**
	 * Arezki Issaadi
	 * permet aux dessins de safficher a chaque clic
	 */
	public void ActiverEditeur() {
		editeurActiver = true;
		repaint();
	}

	/**
	 * Arezki Issaadi
	 * permet d'empecher que les dessins saffichent a chaque clic
	 */

	public void DesactiverEditeur() {
		editeurActiver = false;

	}

	/**
	 *@author Arezki
	 *efface tous les dessins sur la scene en effaçant tous les objets dans les listes. Agisse comme une corbeille
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
}
