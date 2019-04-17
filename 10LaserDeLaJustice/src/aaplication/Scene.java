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
import miroir.MiroirConcave;
import miroir.MiroirConvexe;
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
 * Cette classe contient la scene d'animation du jeu.
 * 
 * @author Miora, Arezki, Jeremy
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
	private int toucheGauche = 37;
	private Vecteur accBalle;

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

	private ModeleAffichage modele;
	private AffineTransform mat;
	private Vecteur vitesse = new Vecteur(1, 1);

	private ArrayList<Laser> listeLasers = new ArrayList<Laser>();
	private ArrayList<TrouNoir> listeTrou = new ArrayList<TrouNoir>();
	private ArrayList<Balle> listeBalles = new ArrayList<Balle>();
	private ArrayList<MiroirConcave> listeMiroireConcave = new ArrayList<MiroirConcave>();
	private ArrayList<MiroirConvexe> listeMiroireConvexe = new ArrayList<MiroirConvexe>();
	private ArrayList<MiroirPlan> listeMiroirePlan = new ArrayList<MiroirPlan>();
	private ArrayList<BlocDEau> listeBlocEau = new ArrayList<BlocDEau>();
	private ArrayList<Prisme> listePrisme = new ArrayList<Prisme>();

	private Balle balle;
	private TrouNoir trou;
	private MiroirConcave miroirConcave;
	private MiroirConvexe miroireConvexe;
	private MiroirPlan miroirePlan;
	private BlocDEau bloc;
	private OrdinateurNiveau3 ordi;
	private int compteur=0;
	private Coeurs coeurs = new Coeurs(nombreVies);
	private Prisme prisme = new Prisme(new Vecteur(1, 1));

	private Echelle echelle;
	private Color couleurLaser = null;

	private Vecteur gravite = new Vecteur (0,9.8); // pour miora 


	private Balle grosseBalle = new Balle(new Vecteur(), vitesse, "LARGE", gravite);
	private Balle moyenneBalle = new Balle(new Vecteur(1, 0), vitesse, "MEDIUM", gravite);
	private Balle petiteBalle = new Balle(new Vecteur(2, 2), vitesse, "SMALL", gravite);


	private ArrayList<SceneListener> listeEcouteur = new ArrayList<SceneListener>();

	private int toucheTir = 32;

	private boolean enMouvement = false;

	private double tempsEcoule = 0;
	private double deltaTInit = 0.06;
	private double deltaT = deltaTInit;
	private ArrayList<Pouvoir> listePouvoirs = new ArrayList<Pouvoir>();
	private Vecteur vitesseLaserInit = new Vecteur(0,0.5);
	private Vecteur vitesseLaser = vitesseLaserInit;
	private double compteurVitesse = 0 ;
	private double compteurRalenti= 0 ;
	private double compteurBouclier= 0 ;



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

		listePrisme.add(prisme);

		lireFond();

		angle = valeurAngleRoulette;

		nouvellePartie(isPartieNouveau, nomFichier);
	//	lectureNiveau(nomFichier);
		lectureFichierOption();
		personnage.setModeSouris(true);


		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				double xSouris= e.getX()/modele.getPixelsParUniteX();
				personnage.setPosSouris(xSouris);
			}
		});

		addMouseListener(new MouseAdapter() {
			@Override

			public void mousePressed(MouseEvent e) {

				double eXR = e.getX() / modele.getPixelsParUniteX();
				double eYR = e.getY() / modele.getPixelsParUniteY();

				// pour drag toutes les balles

				for (int i = 0; i < listeBalles.size(); i++) {
					if ((listeBalles.get(i).getAire().contains(eXR, eYR))) {

						balle = listeBalles.get(i);
						System.out.println(balle.getAccel());
						bonneBalle = true;

						i = listeBalles.size();
					}
				}

				for (int i = 0; i < listeMiroireConvexe.size(); i++) {
					if (listeMiroireConvexe.get(i).getAireMiroirConvexe().contains(eXR, eYR)) {

						bonMiroirConvexe = true;
						miroireConvexe = listeMiroireConvexe.get(i);

						i = listeMiroireConvexe.size();
					}
				}

				for (int i = 0; i < listeMiroireConcave.size(); i++) {
					if (listeMiroireConcave.get(i).getAireMiroirConcave().contains(eXR, eYR)) {

						bonMiroirConcave = true;
						miroirConcave = listeMiroireConcave.get(i);
						
						i = listeMiroireConcave.size();
					}
				}

				for (int i = 0; i < listeMiroirePlan.size(); i++) {
					if (listeMiroirePlan.get(i).getAireMiroirPixel().contains(eXR, eYR)) {

						bonMiroirPlan = true;
						miroirePlan = listeMiroirePlan.get(i);
						System.out.println("hello");
						i = listeMiroirePlan.size();
					}
				}

				for (int i = 0; i < listeTrou.size(); i++) {
					if (listeTrou.get(i).getAireTrou().contains(eXR, eYR)) {

						bonTrouNoir = true;
						trou = listeTrou.get(i);

						i = listeTrou.size();
					}
				}

				for (int i = 0; i < listeBlocEau.size(); i++) {
					if (listeBlocEau.get(i).getAireBloc().contains(eXR, eYR)) {

						bonBlocEau = true;
						bloc = listeBlocEau.get(i);

						i = listeBlocEau.size();
					}
				}


				tirLaser(e, personnage);
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {

				personnage.relacheTouche();
				bonneBalle = false;
				bonMiroirConvexe = false;
				bonMiroirConcave = false;
				bonMiroirPlan = false;
				bonTrouNoir = false;
				bonBlocEau = false;

			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {

				if (bonneBalle) {
					dragBalle();
				} else if (bonMiroirConvexe) {
					dragMiroirConvexe();
				} else if (bonMiroirConcave) {
					dragMiroirConcave();
				} else if (bonMiroirPlan) {
					dragMiroirPlan();
				} else if (bonTrouNoir) {
					dragTrouNoir();
				} else if (bonBlocEau) {
					dragBlocEau();
				}

			}
		});

		addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				personnage.deplacerLePersoSelonTouche(e);
				tirLaser(e);
			}

			@Override
			// Jeremy Thai
			public void keyReleased(KeyEvent e) {
				personnage.relacheTouche(e);

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


		detectionCollisionPouvoirsPersonnages();
		detectionCollisionBalleLaser(listeBalles, listeLasers);
		detectionCollisionTrouLaser(listeLasers);
		detectionCollisionBallePersonnage(listeBalles, personnage);
		detectionCollisionMurBalle();
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

		for (Prisme pri : listePrisme) {
			//pri.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		}

		personnage.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		coeurs.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);

		echelle = new Echelle(30.0, LARGEUR_DU_MONDE - 3.5, HAUTEUR_DU_MONDE - 0.75);
		echelle.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);

		ordi= new OrdinateurNiveau3(new Vecteur(28,LARGEUR_DU_MONDE-10));
		ordi.ajouterListesObstacles(listeBalles);
		ordi.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		ordi.savoirTempsSleep(tempsDuSleep);

		tracerVecteurGraphique(g2d);


		for(Pouvoir pouvoir : listePouvoirs) {
			pouvoir.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
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


		updateMouvementPouvoirs();

		if(personnage.isEnVitesse()){
			personnage.bouge();
			personnage.bouge();
			personnage.bouge();
			personnage.bouge();
		}else {
			personnage.bouge();
		}
		ordi.bouge();

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
			try {
				colisionLaserMiroirPlan();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			updateDureeCompteurs();

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
	 * @author Arnaud
	 */
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
	
	//Par Miora
	/**
	 * Cette methode permet de mettre le niveau pesonnalise
	 */
	private void lectureNiveau(String nomFichier) {
		final String NOM_FICHIER_OPTION = nomFichier;
		ObjectInputStream fluxEntree = null;
		File fichierDeTravail = new File(NOM_FICHIER_OPTION);

		try {
			fluxEntree = new ObjectInputStream(new FileInputStream(fichierDeTravail));
			try {
				listeBalles = (ArrayList<Balle>) fluxEntree.readObject();
				listeBlocEau = (ArrayList<BlocDEau>) fluxEntree.readObject();
				listeMiroirePlan = (ArrayList<MiroirPlan>) fluxEntree.readObject();
				listePrisme = (ArrayList<Prisme>) fluxEntree.readObject();
				listeTrou= (ArrayList<TrouNoir>) fluxEntree.readObject();
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
					pouvoirAuHasard(balle );
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
					coeurs.setCombien(nombreVies - 1);
					nombreVies--;
					personnage.setTempsMort(tempsEcoule+2);
					personnage.setMort(true);
				}
			}
		}
	}


	private void detectionCollisionTrouLaser(ArrayList<Laser> listeLasers) {

		for (Laser laser : listeLasers) {
			for (TrouNoir trou : listeTrou) {
				if (enIntersection(trou.getAireTrou(), laser.getAire())) {
					listeLasers.remove(laser);
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
			while(n< listeMiroirePlan.size() && collision == false) {
				//System.out.println(laser.getPointHaut().toString());
				//System.out.println("laser haut :" + laser.getPositionHaut());
				System.out.println("calcul distance" + listeMiroirePlan.get(n).getLine().ptSegDist(laser.getPointHaut()));
				if(listeMiroirePlan.get(n).getLine().ptSegDist(laser.getPointHaut()) < 2) {
					Vecteur posInter = laser.getPositionHaut();

					collision = true;

					Vecteur normal =listeMiroirePlan.get(n).getNormal().normalise();
					System.out.println("\n"+"La normal est du miroir est :" +normal);

					double angleR = Math.toRadians(laser.getAngleTir() ) ;
					Vecteur incident = new Vecteur(Math.cos(angleR), Math.sin(angleR)).normalise();

					System.out.println("Orientation du laser :" + incident);

					Vecteur reflexion = incident.additionne(normal.multiplie(2.0*(incident.multiplie(-1).prodScalaire(normal))));
					System.out.println("Orientation apres reflexion" + reflexion);	

					//change orientation 
					double angleReflexion = Math.toDegrees(Math.atan(reflexion.getY()/reflexion.getX()));
					System.out.println("Angle reflexion en degree " + angleReflexion);
					System.out.println("angle rad reflexion" + Math.atan(reflexion.getY()/reflexion.getX()));
					if(Math.abs(listeMiroirePlan.get(n).getAngle())>90) {
						System.out.println("ici");
						laser.setAngleTir(180+angleReflexion);
					}else{
						laser.setAngleTir(angleReflexion);
					}
					laser.setPositionHaut(posInter);
					System.out.println("pos haut fleche apres trans angle : " + laser.getPositionHaut() + " bas : " + laser.getPositionBas());

					//Il faut faire une translation du du haut du laser
					double xt = (laser.getPositionHaut().getX())-laser.getPositionBas().getX(); // translation x
					double yt = laser.getPositionHaut().getY() - laser.getPositionBas().getY(); // translation y
					double a[][]={{1,0,xt},{0,1,yt},{0,0,1}};
					double b[]={laser.getPositionHaut().getX(),laser.getPositionHaut().getY(),1};  // le point a translater  

					//creer une matrice qui va acceuillir la transformation
					double c[]=new double[3];  //matrice de 1 colonne et 1 ligne  

					//multiplication matriciel 
					for(int i=0;i<3;i++){    
						c[i]=0;      
						for(int k=0;k<3;k++)      
						{      
							c[i]+=a[i][k]*b[k];      
						}
						System.out.print(c[i]+" ");
						System.out.println();
					} 
					laser.setPositionHaut(new Vecteur (c[0], c[1]));
					System.out.println("laser bas : " + laser.getPositionBas() + "laser haut " + laser.getPositionHaut() );
					System.out.println("-----------------------------------------------------------------------------");
				}
				n++;
			}

		}

	} // fin methode

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
		listeMiroireConvexe.add(new MiroirConvexe(new Vecteur(3, 0), 2, 0));
		repaint();
	}

	/**
	 * Arezki Issaadi permet d'ajouter et de dessiner un miroir plan en appuyant sur
	 * le boutton miroire plan
	 */
	public void ajoutMiroirPlan() {
		listeMiroirePlan.add(new MiroirPlan(new Vecteur(1, 2), 0));
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
				if (bonneBalle) {

					double xDrag = e.getX() / modele.getPixelsParUniteX();
					double yDrag = e.getY() / modele.getPixelsParUniteY();
					balle.setPosition(new Vecteur(xDrag - diametre / 2, yDrag - diametre / 2));

					repaint();
				}

			}
		});

	}

	private void dragMiroirConvexe() {
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (bonMiroirConvexe) {

					double xDrag = e.getX() / modele.getPixelsParUniteX();
					double yDrag = e.getY() / modele.getPixelsParUniteY();
					miroireConvexe.setPosition(new Vecteur(xDrag, yDrag));

					repaint();
				}

			}
		});
	}

	private void dragMiroirConcave() {
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (bonMiroirConcave) {

					double xDrag = e.getX() / modele.getPixelsParUniteX();
					double yDrag = e.getY() / modele.getPixelsParUniteY();
					miroirConcave.setPosition(new Vecteur(xDrag, yDrag));

					repaint();
				}

			}
		});
	}

	private void dragMiroirPlan() {
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (bonMiroirPlan) {

					double xDrag = e.getX() / modele.getPixelsParUniteX();
					double yDrag = e.getY() / modele.getPixelsParUniteY();
					
					miroirePlan.setPosition(new Vecteur (xDrag, yDrag));
					repaint();
				}

			}
		});
	}

	private void dragTrouNoir() {
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (bonTrouNoir) {

					double xDrag = e.getX() / modele.getPixelsParUniteX();
					double yDrag = e.getY() / modele.getPixelsParUniteY();
					trou.setPosition(new Vecteur(xDrag, yDrag));

					repaint();
				}

			}
		});
	}

	private void dragBlocEau() {
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (bonBlocEau) {

					double xDrag = e.getX() / modele.getPixelsParUniteX();
					double yDrag = e.getY() / modele.getPixelsParUniteY();
					bloc.setPosition(new Vecteur(xDrag, yDrag));

					repaint();
				}

			}
		});
	}

	private void setAngleRoulette() {
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				// System.out.println("wheel rotation:"+ arg0.getWheelRotation());
				if (arg0.getWheelRotation() == -1 && (valeurAngleRoulette >= 0)) {
					valeurAngleRoulette -= 0.05;
					setAngle(valeurAngleRoulette);
					System.out.println(valeurAngleRoulette);

				} else if (arg0.getWheelRotation() == 1 && (valeurAngleRoulette < 180)) {
					valeurAngleRoulette += 0.05;
					setAngle(valeurAngleRoulette);
					System.out.println();
					System.out.println(valeurAngleRoulette);
				}

				enMouvement = true;
			}
		});

		repaint();
	}

	private void tirer() {

		ordi.ajouterListesObstacles(listeBalles);
		//	listeLasers.add(ordi.tirer());
		//	listeLasers.add(ordi2.tirer());
		listeLasers.add(ordi.tirer());
		//listeLasers.add(new Laser(new Vecteur(ordi.getPositionX()+ordi.getLargeurOrdi()/2,HAUTEUR_DU_MONDE-ordi.getLongueurOrdi()), angle, new Vecteur(0,0.5)));

	}

	/**
	 * 
	 * @param g
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

	private void CollisionLaserPrisme(ArrayList<Laser> listeLasers, ArrayList<Prisme> listePrismes) {
		boolean collisionLaserPrisme = false;
		Vecteur collision = new Vecteur();
		while (!collisionLaserPrisme) {
			for (Prisme pris : listePrisme) {
				for (Laser laser : listeLasers) {
					if (pris.getAirPrisme().contains(laser.getPositionHaut().getX(), laser.getPositionHaut().getY())) {
						collisionLaserPrisme = true;
						collision = laser.getPositionHaut();

					}

					while (!collisionLaserPrisme) {
						for (Laser lasers : listeLasers)
							for (Prisme pris1 : listePrismes) {

								if (enIntersection(pris1.getAirPrisme(), lasers.getAire())) {

									collisionLaserPrisme = true;
									collision = laser.getPositionHaut();
									System.out.println("jai collision avec le prisme");
									System.err.println("le vecteur de la collision: " + collision);

								} else {
									System.out.println("exit");
								}

							}
					}
				}
			}
		}

		// return collision;
		// return collision;
	}

	// Miora
	/**
	 * Cette methode permet de sauvegarder le nombre de vie, le nombre des balles,
	 * la position du joueur, la couleur du rayon et les touches utilisées
	 */
	public void ecritureFichierSauvegarde() {
		String nomSauv = JOptionPane.showInputDialog("Entrez votre nom de sauvegarde");
		String NOM_FICHIER_OPTION = nomSauv + ".save";
		File fichierDeTravail = new File(NOM_FICHIER_OPTION);

		ObjectOutputStream fluxSortie = null;
		try {
			fluxSortie = new ObjectOutputStream(new FileOutputStream(fichierDeTravail));
			fluxSortie.writeInt(nombreVies); // nombre de vie
			fluxSortie.writeObject(listeBalles); // la liste des balles
			fluxSortie.writeDouble(personnage.getPositionX()); // les caracteristiques du personnage
			if (couleurLaser == null) {
				fluxSortie.writeObject(null);
			} else {
				fluxSortie.writeObject(couleurLaser);
			} // la couleur du rayon
			fluxSortie.writeInt(toucheGauche); // la touche gauche
			fluxSortie.writeInt(toucheDroite); // la touche droite
			fluxSortie.writeDouble(tempsEcoule);
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
		String nomFichierSauv = nomFichier;
		ObjectInputStream fluxEntree = null;
		File fichierDeTravail = new File(nomFichierSauv);

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
			tempsEcoule = fluxEntree.readInt();
			System.out.println("le temps lu dans scene " + tempsEcoule);
			leverEvenChangementTemps();
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
	 * @param isNouvelle  : retourne vrai s'il s'agit d'une nouvelle scene
	 * @param nomfichier : le nom du fichier de sauvegarde ou de niveau
	 */

	private void nouvellePartie(boolean isNouvelle, String nomFichier) {
		if (!isNouvelle) {
			if(nomFichier.endsWith(".niveau")) {
				lectureNiveau(nomFichier);
			}else {
			System.out.println("scene partie charge " + isNouvelle);
			lectureFichierSauvegarde(nomFichier);
			coeurs.setCombien(nombreVies);
			personnage = new Personnage(positionPerso, toucheGauche, toucheDroite, toucheTir);
			}
		} else {
			System.out.println("nouvelle partie come on");
			System.out.println("scene isNouvelle" + isNouvelle + " " + toucheGauche);
			personnage = new Personnage(LARGEUR_DU_MONDE / 2, toucheGauche, toucheDroite, toucheTir);
		}
	}

	public void addSceneListener(SceneListener ecouteur) {
		listeEcouteur.add(ecouteur);
	}

	public void leverEvenCouleurLaser() {
		for (SceneListener ecout : listeEcouteur) {
			ecout.couleurLaserListener();
		}
	}

	public void leverEvenChangementTemps() {
		System.out.println("je suis dans la levee evenement " + tempsEcoule );
		for (SceneListener ecout : listeEcouteur) {
			ecout.changementTempsListener(tempsEcoule);
		}
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

	private void pouvoirAuHasard(Balle balle ) {

		Vecteur position = new Vecteur(balle.getPosition().getX(), balle.getPosition().getY());
		Vecteur accel =  new Vecteur(gravite);

		int nb = 0 + (int)(Math.random() * ((10 - 0) + 1));

		Pouvoir pouvoir;
		switch(nb) {

		case 1: 
			pouvoir = new BoostVitesse( position, accel);
			pouvoir.setCompteurAvantDisparaitre(tempsEcoule + 5);
			listePouvoirs.add(pouvoir);
			break;

		case 2: 
			pouvoir = new Bouclier( position, accel);
			pouvoir.setCompteurAvantDisparaitre(tempsEcoule + 5);
			listePouvoirs.add(pouvoir);
			break;

		case 3: 
			pouvoir = new Ralenti( position, accel);
			pouvoir.setCompteurAvantDisparaitre(tempsEcoule + 5);
			listePouvoirs.add(pouvoir);
			break;

		case 4: 
			pouvoir = new AjoutVie( position, accel);
			pouvoir.setCompteurAvantDisparaitre(tempsEcoule + 5);
			listePouvoirs.add(pouvoir);
			break;
		}
	}


	private void ajoutCompteurs() {

		if(vitesseLaser.getY()>vitesseLaserInit.getY()) 
			compteurVitesse = tempsEcoule + 5;

		if( deltaT < deltaTInit) 
			compteurRalenti = tempsEcoule + 5;

		if(personnage.isBouclierActive())
			compteurBouclier = tempsEcoule + 9;
	}


	private void updateDureeCompteurs() {

		for(Balle balle : listeBalles) {
			balle.updateRotation();
		}
		try {
			for (Pouvoir pouvoir : listePouvoirs) {
				if( pouvoir.getCompteurAvantDisparaitre() <= tempsEcoule)
					listePouvoirs.remove(pouvoir);
			} 
		}catch(ConcurrentModificationException e) {
		} 

		if( compteurVitesse <= tempsEcoule ) {
			vitesseLaser = vitesseLaserInit;
			compteurVitesse = 0;
			personnage.setEnVitesse(false);
		}

		if( compteurRalenti <= tempsEcoule ) {
			deltaT = deltaTInit;
			compteurRalenti = 0;
		}

		if(compteurBouclier <= tempsEcoule)
			personnage.setBouclierActive(false);

		if(personnage.getTempsMort() <= tempsEcoule)
			personnage.setMort(false);

	}
	private void detectionCollisionPouvoirsPersonnages() {

		for(Pouvoir pouvoir : listePouvoirs) {
			if(enIntersection(pouvoir.getAire(), personnage.getAire() )) {
				pouvoir.activeEffet(this);
				ajoutCompteurs();
				listePouvoirs.remove(pouvoir);
			}
		}
	}	

	private void updateMouvementPouvoirs() {
		for(Pouvoir pouvoir: listePouvoirs) {
			if(pouvoir.getPosition().getY()+pouvoir.getLongueurImg() >= HAUTEUR_DU_MONDE) {
				pouvoir.getPosition().setY(HAUTEUR_DU_MONDE-pouvoir.getLongueurImg());
			}else {
				pouvoir.unPasVerlet(deltaT);
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
	
	//Par Miora R. Rakoto
	/**
	 * Cette methode permet de sauvegarder un niveau
	 * @param nomSauv : le nom du niveau
	 */
	public void ecritureNiveau(String nomSauv) {
		String NOM_FICHIER_OPTION = nomSauv + ".niveau";
		File fichierDeTravail = new File(NOM_FICHIER_OPTION);
		ObjectOutputStream fluxSortie = null;
		try {
			fluxSortie = new ObjectOutputStream(new FileOutputStream(fichierDeTravail));
			fluxSortie.writeObject(listeBalles); // la liste des balles
			fluxSortie.writeObject(listeBlocEau);
			fluxSortie.writeObject(listeMiroirePlan);
			fluxSortie.writeObject(listePrisme);
			fluxSortie.writeObject(listeTrou);
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

}
