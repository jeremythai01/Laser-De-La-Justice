package physique;


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
import interfaces.SceneListener;

import objets.TrouNoir;
import personnage.Personnage;
import utilite.ModeleAffichage;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;



/**
 * 
 * @author Jeremy et Arnaud( A verifier )
 *
 */
public class SceneTest extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	private int tempsDuSleep = 30;
	//private double deltaT = 0.006;
	private double deltaT = 0.06;
	private  double LARGEUR_DU_MONDE = 30; //en metres
	private  double HAUTEUR_DU_MONDE;
	private boolean enCoursAnimation= false;
	private double tempsTotalEcoule = 0;
	private double diametre = 2;  //em m�tres
	private ArrayList<Balle> listeBalles = new ArrayList<Balle>();
	private Balle balle;
	private boolean premiereFois = true;
	private ModeleAffichage modele;
	private AffineTransform mat;
	private Vecteur vitesse;
	private Personnage character;

	private double angle;
	private ArrayList<Laser> listeLasers = new ArrayList<Laser>();

	//private ArrayList<TrouNoir> listeTrou = new ArrayList<TrouNoir>();
	private TrouNoir trou;
	private int toucheGauche = 37;
	private int toucheDroite = 39;


	private ArrayList<SceneListener> listeEcouteurs = new ArrayList<SceneListener>();


	/**
	 * Create the panel.
	 */
	public SceneTest() {

		character = new Personnage(toucheGauche, toucheDroite);

		angle = -90;
		vitesse = new Vecteur(0.5 ,0);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

				double eXR = e.getX()/modele.getPixelsParUniteX();
				double eYR = e.getY()/modele.getPixelsParUniteY();
				balle = new Balle(new Vecteur(eXR-diametre/2, eYR-diametre/2),vitesse, "LARGE" );
				listeBalles.add(balle);

				//	trou= new TrouNoir(new Vecteur(eXR,eYR));
				//listeTrou.add(trou);

				repaint();
			}
		});


		addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				character.deplacerLePersoSelonTouche( e );
				shootEtAddLaser(e);
				repaint();
			}
			@Override
			public void keyReleased(KeyEvent e) {
				character.relacheTouche(e);
				repaint();
			}
		});

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
	//	checkCollisionTrouLaserPersonnage( listeLasers );



		for(Balle balle: listeBalles) {

			balle.dessiner(g2d,mat,HAUTEUR_DU_MONDE,LARGEUR_DU_MONDE);
		}

		/*
		for(TrouNoir trou: listeTrou) {
			trou.dessiner(g2d,mat,HAUTEUR_DU_MONDE,LARGEUR_DU_MONDE);
		}
		 */
		
		character.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE );




	}//fin paintComponent






	private void calculerUneIterationPhysique() {
		
		for (int i = 0; i < listeBalles.size(); i++) {
			for (int j = i+1; j < listeBalles.size(); j++) {
				Balle balle1 = listeBalles.get(i);
				Balle balle2 = listeBalles.get(j);
				MoteurPhysique.detectionCollisionBalles(balle1, balle2);
			}
		}

		checkCollisionBalleLaserPersonnage( listeBalles,  listeLasers,character);
		
		for(Balle balle: listeBalles) {
			balle.unPasVerlet(deltaT);
		}

		for(Laser laser : listeLasers) {
			laser.move();
		}

		character.bouge();

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




	private void checkCollisionBalleLaserPersonnage(ArrayList<Balle> listeBalles, ArrayList<Laser> listeLasers, Personnage character ) {

		for(Laser laser : listeLasers) {
			for(Balle balle : listeBalles ) {
				if(intersection(balle.getAireBalle(), laser.getLaserAire())) {
					listeLasers.remove(laser);   
					balle.shrink(listeBalles);
					System.out.println("1");
				}	

			}
		}


	}
	private boolean intersection(Area aire1, Area aire2) {
		Area aireInter = new Area(aire1);
		aireInter.intersect(aire2);
		if(!aireInter.isEmpty()) {
			return true;
		}
		return false;
	}

	/*
	private void checkCollisionTrouLaserPersonnage( ArrayList<Laser> listeLasers ) {


		for(Laser laser : listeLasers) {
			for(TrouNoir trou : listeTrou ) {
				if(intersection(trou.getAireTrou(), laser.getLaserAire())) {
					listeLasers.remove(laser);   

				}	
			}
		}


	}
	 */
	private void shootEtAddLaser(KeyEvent e) {
		if(enCoursAnimation == true) {
			int code = e.getKeyCode();
			if(code == KeyEvent.VK_SPACE) {
				character.neBougePas();
				if(listeLasers.size() <1) { // Pour que 1 laser soit tirer  a la fois 
					listeLasers.add(
							new Laser(new Vecteur(
									character.getPositionX()+character.getLARGEUR_PERSO()/2,HAUTEUR_DU_MONDE-character.getLONGUEUR_PERSO()) , angle, new Vecteur(0, 1 )));
					System.out.println("nb de laser :"+ listeLasers.size());
					repaint();
				}
			}
		}

	}
	/**
	 * prend un ecouteur en parametre pour �tre ajout� par le ArrayList d'�couteur
	 * @param ecouteur
	 */
	public void addSceneListener(SceneListener ecouteur) {
		listeEcouteurs.add(ecouteur);
	}







}

