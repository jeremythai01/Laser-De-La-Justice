package aaplication;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import geometrie.Vecteur;
import interfaces.SceneListener;
import interfaces.SceneTutorielListener;
import personnage.Personnage;
import physique.Balle;
import physique.Laser;
import physique.MoteurPhysique;
import utilite.ModeleAffichage;

/**
 * Cette classe contient la scene du tutoriel.
 * 
 * @author Arnaud, Arezki, Jeremy
 *
 */
public class SceneTutoriel extends JPanel implements Runnable {

	
	private Image fond = null;

	private Personnage personnage;

	private static final long serialVersionUID = 1L;

	private double angle;

	private double LARGEUR_DU_MONDE = 40; // en metres
	private double HAUTEUR_DU_MONDE;
	private int tempsDuSleep = 30;
	


	private double valeurAngle= 90;

	private final int TOUCHE_GAUCHE = 37;
	private final int TOUCHE_DROITE = 39;
	private final int TOUCHE_ESPACE = 32;

	private boolean enCoursAnimation = false;
	private boolean premiereFois = true;
	private boolean couleurPersoLaser = false;


	private ModeleAffichage modele;
	private AffineTransform mat;
	private Vecteur vitesse = new Vecteur(1, 1);


	private ArrayList<Laser> listeLasers = new ArrayList<Laser>();
	private ArrayList<Balle> listeBalles = new ArrayList<Balle>();
	
	private ArrayList<SceneTutorielListener> listeEcouteurs= new  ArrayList<SceneTutorielListener>();

	
	private Color couleurLaser = null;

	private Vecteur gravite = new Vecteur (0,9.8); // pour miora 


	private Balle grosseBalle = new Balle(new Vecteur(), vitesse, "LARGE", gravite);


	private ArrayList<SceneListener> listeEcouteur = new ArrayList<SceneListener>();

	private boolean enMouvement = false;
	private boolean etape1=true;
	private boolean etape2=false;
	private boolean etape3=false;
	private boolean etape4=false;
	private boolean etape5=false;
	private boolean etape6=false;
	private boolean etape7=false;
	


	private double deltaTInit = 0.04;
	private double deltaT = deltaTInit;
	
	private Vecteur vitesseLaserInit = new Vecteur(0,0.5);
	private Vecteur vitesseLaser = vitesseLaserInit;


	private boolean premiereBalleTouche=false;
	private boolean deuxiemeBalleTouche=false;
	private boolean laserEteTire=false;
	private boolean changerAngleFait=false;
	private boolean toutesLesBallesMortes=false;


	// Par Arnaud
	/**
	 * Constructeur de la scene du tutoriel
	 * Permet de savoir a quel etape le joueur est rendu
	 *
	 */

	public SceneTutoriel() {

		personnage = new Personnage(LARGEUR_DU_MONDE / 2, TOUCHE_GAUCHE, TOUCHE_DROITE, TOUCHE_ESPACE);
		angle=valeurAngle;
		
		lireFond();
	

		addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if(etape1) {
					for(int i=0;i<10;i++) {
						personnage.deplacerLePersoSelonTouche(e);
					}
				
				personnage.neBougePas();
				if(e.getKeyCode()==37||e.getKeyCode()==39) { // pour s'assurer que le joueur bouge bien le personnage avant de passer a la prochaine etape
				etape1=false;
				etape2=true;}
				}
			
				if(etape2) {
					tirLaser(e);
					System.out.println("lalaos");
					if(laserEteTire) {
					etape2=false;
					etape3=true;}
				}
				if(etape3) {
				changerAngle(e);
				if(changerAngleFait) {
				etape3=false;
				etape4=true;}
				}
				if(etape4) {
					changerAngle(e);
					tirLaser(e);
					if(premiereBalleTouche) {
						etape4=false;
						etape5=true;
						repaint();
					}
				}
				if(etape5) {
					changerAngle(e);
					tirLaser(e);
					if(deuxiemeBalleTouche) {
						etape5=false;
						etape6=true;
						repaint();
					}
				}
				if(etape6) {
					changerAngle(e);
					tirLaser(e);
					if(toutesLesBallesMortes) {
						etape6=false;
						etape7=true;
					}
				}
				//modeTriche();
			}
		});
		



		if(premiereBalleTouche) {
			System.out.println("jeu fini");}


	}

	/**
	 * Méthode qui permet de dessiner toutes les formes de la scene et de verifier les collisions
	 */
	//Arnaud Lefebvre
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
		detectionCollisionMurBalle();
		for (Balle balle : listeBalles) {

			balle.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		}

		personnage.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);


		tracerVecteurGraphique(g2d);
		dessinerTexte(g2d);

		

	}
	
	/**
	 * Cette methode dessine le texte qui indique au joueur quoi faire selon le progres 
	 * @param g2d, le composant graphique
	 */
	//Arnaud Lefebvre
	public void dessinerTexte(Graphics2D g2d) {
		g2d.setColor(Color.WHITE);
		Font font = new Font ("Book Antiqua      ", Font.BOLD, 12);
		g2d.setFont(font);
		if(etape1) {
			g2d.drawString("Bouger le personnage en utilisant les fleches horizontales.", 20,20);
		}
		if(etape2) {
			g2d.drawString("Tirer un laser en touchant espace!!", 20,20);
		}
		if(etape3) {
			g2d.drawString("Changer l'angle du laser en utilisant les fleches verticales.", 20,20);
		}
		if(etape4) {
			g2d.drawString("WOWWW!!! Il ne vous reste plus qu'a eliminer la grosse boulle!!", 20,20);
		}
		if(etape5) {
			g2d.drawString("Mais... une autre balle??", 20,20);
		}
		if(etape6) {
			g2d.drawString("Quoi!!!! Encore??", 20,20);
		}
		if(etape7) {
			g2d.drawString("BRAVO!!! Vous etes prets a vous attaquer au vrai jeu. Cliquez sur Revenir!", 20,20);
		}
	}

	/**
	 * Cette méthode permet d'arreter l'animation
	 */
	//Arnaud Lefebvre
	public void arreter() {
		if (enCoursAnimation)
			enCoursAnimation = false;
	}

	/**
	 * Cette méthode permet de démarrer l'animation
	 * 
	 */
	//Arnaud Lefebvre
	public void demarrer() {
		if (!enCoursAnimation) {
			Thread proc = new Thread(this);
			proc.start();
			enCoursAnimation = true;
		}

	}



	@Override
	/**
	 * Animation de la scene
	 * 
	 */
	//Arnaud
	public void run() {
		// TODO Auto-generated method stub
		while (enCoursAnimation) {
			calculerUneIterationPhysique();

			try {

				Thread.sleep(tempsDuSleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} // fin while
		System.out.println("Le thread est mort...");
	}

	// Par Arnaud
	/**
	 * Methode permettant de mettre un fond a la scene
	 **/
	private void lireFond() {

		URL fich = getClass().getClassLoader().getResource("mars.png");
		if (fich == null) {
			JOptionPane.showMessageDialog(null, "Fichier introuvable!");
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
	 * 
	 */
	//auteur Arnaud Lefebvre
	public void setAngle(double angle) {
		
		this.angle = angle;
	}


	
	
	/**
	 * Methode qui permet de changer l'angle de tir si le joueur enfonce les fleches
	 * @param e, la touche que le joueur a enfoncée
	 */
	//auteur Arnaud Lefebvre
	private void changerAngle(KeyEvent e) {
		if(e.getKeyCode()==38 && angle<180) {
			angle=angle+5;
			if(angle>180)angle=180;
			changerAngleFait=true;
		}
		if(e.getKeyCode()==40 && angle>0) {
			angle=angle-5;
			if(angle<0)angle=0;
			changerAngleFait=true;
		}
		enMouvement=true;
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
			trace.moveTo(personnage.getPosition() + personnage.getLARGEUR_PERSO() / 2,
					HAUTEUR_DU_MONDE - personnage.getLONGUEUR_PERSO());
			trace.lineTo(
					personnage.getPosition() + personnage.getLARGEUR_PERSO() / 2 + 2 * Math.cos(Math.toRadians(angle)),
					HAUTEUR_DU_MONDE - personnage.getLONGUEUR_PERSO() - 2 * Math.sin(Math.toRadians(angle)));
			g.draw(mat.createTransformedShape(trace));
		}
	}

	

	


	/**
	 * Methode qui ajoute l'ecouteur a la liste d'ecouteurs
	 * @param ecouteur, le nouvel ecouteur personnalise
	 */
	//Arnaud Lefebvre
	public void addSceneListener(SceneListener ecouteur) {
		listeEcouteur.add(ecouteur);
	}


	/**
	 *Evalue une collision avec le sol ou un mur et modifie la vitesse courante selon la collision
	 * @param width largeur du monde 
	 * @param height hauteur  du monde 
	 */
	//Jeremy
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
	
	/**
	 * Methode qui permet au composant d'ajouter l'ecouteur personnalise
	 * @param objEcout, l'ecouteur personnalise
	 */
	//Arnaud Lefebvre
	public void addSceneTutorielListener(SceneTutorielListener objEcout) {
		listeEcouteurs.add(objEcout);
	}
	
	/**
	 * Methode qui, une fois appele par les ecouteurs personnalises lorsqu'une balle est touchee, s'assure que le tutoriel soit rendu a la bonne etape
	 */
	//Arnaud Lefebvre
	public void changerEtapeTir() {
		if(etape4) {
			etape4=false;
			etape5=true;
			return;
		}
		if(etape5) {
			etape5=false;
			etape6=true;
			return;
		}
		
		if(listeBalles.size()==0) {
			etape6=false;
			etape7=true;
		}
	}
		
	/**
	 * Permet d'ajouter et de dessiner une grosse balle en appuyant
	 * sur le boutton grosse balle
	 */
	//Arezki
	public void ajoutBalleGrosse() {

		grosseBalle = new Balle(new Vecteur(), vitesse, "LARGE", gravite);
		listeBalles.add(grosseBalle);
		repaint();

	}
	
	/**
	 * permet de calculer les collision la vitessse des balles et tout autres
	 * animation ayant de la physiques
	 * 
	 */
	//Jeremy
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
		try{
			for (Laser laser : listeLasers) {
				laser.move();
			}
		} catch(ConcurrentModificationException e)  {}
		if(personnage.isEnVitesse()){
			personnage.bouge();
			personnage.bouge();
			personnage.bouge();
			personnage.bouge();
		}else {
			personnage.bouge();
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

		try {
		for (Laser laser : listeLasers) {
			for (Balle balle : listeBalles) {
				if (enIntersection(balle.getAire(), laser.getAire())) {
					if(premiereBalleTouche) {
						deuxiemeBalleTouche=true;
					}
					premiereBalleTouche=true;
					listeLasers.remove(laser);
					balle.shrink(listeBalles, gravite);
					if(listeBalles.size()==0) {
						toutesLesBallesMortes=true;
					}
					for(SceneTutorielListener objEcout: listeEcouteurs) {
						objEcout.changerEtapeTutoriel();
					}
				}
			}
		}
		}catch(ConcurrentModificationException e) {
			System.out.println("le laser nexiste pas");
		}
		repaint();
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
					listeLasers.add(new Laser(new Vecteur(personnage.getPosition() + personnage.getLARGEUR_PERSO() / 2,
							HAUTEUR_DU_MONDE - personnage.getLONGUEUR_PERSO()), angle, vitesseLaser));

					laserEteTire=true;
				} else {
					System.out.println("tir laser false");
					listeLasers.add(new Laser(
							new Vecteur(personnage.getPosition() + personnage.getLARGEUR_PERSO() / 2,
									HAUTEUR_DU_MONDE - personnage.getLONGUEUR_PERSO()),
							angle, vitesseLaser, couleurLaser));

					laserEteTire=true;
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
	
}
