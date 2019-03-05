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
import miora.MiroirConvexe;
import miora.MiroirPlan;
import miroir.MiroirConcave;
import objets.BlocDEau;
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
 * @author Miora
 *
 */
public class Scene extends JPanel implements Runnable {

	private Image fond = null;

	private Personnage principal;

	private Rectangle2D.Double fantomePerso;
	private Shape fantomeTransfo;

	private static final long serialVersionUID = 1L;

	private double angle;
	private double deltaT = 0.08;
	private double LARGEUR_DU_MONDE = 30; // en metres
	private double HAUTEUR_DU_MONDE;

	private double tempsTotalEcoule = 0;
	private double diametre = 2; // em m�tres

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

	private int toucheGauche = 37;
	private int toucheDroite = 39;
	private int tempsDuSleep = 30;

	// Jeremy Thai

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
			// Jeremy Thai
			public void mousePressed(MouseEvent e) {

				double eXR = e.getX() / modele.getPixelsParUniteX();
				double eYR = e.getY() / modele.getPixelsParUniteY();

				balle = new Balle(new Vecteur(eXR - diametre / 2, eYR - diametre / 2), vitesse, "LARGE");
				trou = new TrouNoir(new Vecteur(eXR, eYR));
				miroireConvexe = new MiroirConvexe(eXR, eYR, 1);
				miroirConcave = new MiroirConcave(new Vecteur(eXR, eYR));
				bloc = new BlocDEau(new Vecteur(eXR,eYR));

				listeBalles.add(balle);
				listeTrou.add(trou);
				listeMiroireConcave.add(miroirConcave);
				listeMiroireConvexe.add(miroireConvexe);
				listeBlocEau.add(bloc);

				repaint();
			}
		});

		addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				principal.deplacerLePersoSelonTouche(e);
				System.out.println(principal.getToucheDroite() + " " + principal.getToucheGauche());
				shootEtAddLaser(e);
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

		checkCollisionBalleLaserPersonnage(listeBalles, listeLasers, principal);
		checkCollisionTrouLaserPersonnage(listeLasers);

		if (editeurActiver) {

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
	
		}
		
		// creerLePersonnagePrincipal(g2d, mat);

		principal.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);

	}

	/**
	 * Cette methode permet de dessiner le personnage principal, ainsi qu'un carre
	 * autour de lui
	 * 
	 * @param g2d : le composant graphique
	 * @param mat : la matrice de transformation
	 */
	// Miora
	private void creerLePersonnagePrincipal(Graphics2D g2d, AffineTransform mat) {
		principal.dessiner(g2d, mat, modele.getLargUnitesReelles(), modele.getHautUnitesReelles());

		fantomePerso = new Rectangle2D.Double(principal.getPositionX(),
				modele.getHautUnitesReelles() - principal.getLONGUEUR_PERSO(), principal.getLARGEUR_PERSO(),
				principal.getLONGUEUR_PERSO());
		g2d.draw(mat.createTransformedShape(fantomePerso));

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

	// Jeremy Thai
	private void calculerUneIterationPhysique() {

		for (Balle balle : listeBalles) {
			balle.unPasRK4(deltaT, tempsTotalEcoule);
		}

		for (Laser laser : listeLasers) {
			laser.move();
		}
		tempsTotalEcoule += deltaT;
		;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (enCoursAnimation) {
			principal.move();
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

	/**
	 * Methode permettant de mettre un fond a la scene
	 */ // Miora
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

	public void setAngle(double angle) {
		laser.setAngleTir(angle);
		principal.getPositionX();

	}

	/**
	 * Cette methode permet de lire le fichier option de la classe option modifie
	 * avant le debut de la partie
	 */
	// Miora

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
			// on ex�cutera toujours ceci, erreur ou pas
			try {
				fluxEntree.close();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Erreur rencontr�e lors de la fermeture!");
			}
		} // fin finally
	}

	// Jeremy Thai
	private void checkCollisionBalleLaserPersonnage(ArrayList<Balle> listeBalles, ArrayList<Laser> listeLasers,
			Personnage character) {

		ArrayList<Balle> listeBalleTouche = new ArrayList<Balle>();
		for (Laser laser : listeLasers) {
			for (Balle balle : listeBalles) {
				if (intersection(balle.getAireBalle(), laser.getLaserAire())) {
					listeLasers.remove(laser);
					listeBalleTouche.add(balle);
					balle.shrink(listeBalles);
				}

			}
		}
	}

	private void checkCollisionTrouLaserPersonnage(ArrayList<Laser> listeLasers) {

		for (Laser laser : listeLasers) {
			for (TrouNoir trou : listeTrou) {
				if (intersection(trou.getAireTrou(), laser.getLaserAire())) {
					listeLasers.remove(laser);

				}
			}
		}

	}

	// Jeremy Thai
	private void shootEtAddLaser(KeyEvent e) {
		if (enCoursAnimation) {
			int code = e.getKeyCode();
			if (code == KeyEvent.VK_SPACE) {
				principal.shoot(code);
				if (listeLasers.size() < 1) { // Pour que 1 laser soit tirer a la fois
					listeLasers.add(new Laser(
							new Vecteur(principal.getPositionX() + principal.getLARGEUR_PERSO() / 2, LARGEUR_DU_MONDE),
							angle, new Vecteur(0, 0.5)));
				}
			}
		}
	}

	// Jeremy Thai
	private boolean intersection(Area aire1, Area aire2) {
		Area aireInter = new Area(aire1);
		aireInter.intersect(aire2);
		if (!aireInter.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * Arezki Issaadi
	 * permet d'ajouter une grosse balle via lediteur  
	 */
	public void ajoutBalleGrosse() {
		listeBalles.add(new Balle(new Vecteur(), vitesse, "LARGE"));
		repaint();

	}

	/**
	 * Arezki Issaadi
	 * permet d'ajouter une balle medium via lediteur 
	 */
	public void ajoutBalleMedium() {
		listeBalles.add(new Balle(new Vecteur(1, 0), vitesse, "MEDIUM"));
		repaint();

	}

	/**
	 * Arezki Issaadi
	 * permet d'ajouter une petite balle via lediteur 
	 */
	public void ajoutBallePetite() {
		listeBalles.add(new Balle(new Vecteur(2, 2), vitesse, "SMALL"));
		repaint();

	}

	/**
	 * Arezki Issaadi
	 * permet d'ajouter un miroir concave via lediteur 
	 */
	public void ajoutMiroireConcave() {
		listeMiroireConcave.add(new MiroirConcave(new Vecteur(3, 0)));
		repaint();
	}

	/**
	 * Arezki Issaadi
	 *  permet d'ajouter un miroir convexe via lediteur 
	 */
	public void ajoutMiroireConvexe() {
		listeMiroireConvexe.add(new MiroirConvexe(4, 0, 1));
		repaint();
	}

	/**
	 * Arezki Issaadi
	 *  permet d'ajouter un miroir plan via lediteur 
	 */
	public void ajoutMiroirPlan() {
		listeMiroirePlan.add(new MiroirPlan(5, 0, 0));
		repaint();

	}

	/**
	 * Arezki Issaadi
	 *  permet d'ajouter un trou noir via lediteur 
	 */
	public void ajoutTrouNoir() {
		listeTrou.add(new TrouNoir(new Vecteur(7, 0)));
		repaint();

	}
	
	/**
	 * Arezki Issaadi
	 *  permet d'ajouter un bloc d'eau via lediteur 
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
	 * Arezki
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
