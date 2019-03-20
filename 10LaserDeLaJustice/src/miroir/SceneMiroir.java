package miroir;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.util.ArrayList;

import javax.swing.JPanel;

import geometrie.Vecteur;
import geometrie.VecteurGraphique;
import physique.Laser;
import utilite.ModeleAffichage;

/**
 * Cette classe permet de visualiser le test
 * @author Miora, Arezki, Jeremy et Arnaud
 **/
public class SceneMiroir extends JPanel implements Runnable {

	private int tempsDuSleep = 100;
	private double deltaT = 1000;
	private  double LARGEUR_DU_MONDE = 10; //en metres
	private  double HAUTEUR_DU_MONDE;
	private double tempsTotalEcoule = 0;
	private boolean enCoursAnimation= false;
	private boolean premiereFois = true;
	private ModeleAffichage modele;
	private AffineTransform mat;
	private Vecteur position;
	private boolean miroirPlan = true, miroirConvexe = false, miroirConcave = false;

	private Vecteur vitesse;

	private Vecteur gravite ;

	private Personnage character;
	private VecteurGraphique q;
	private Vecteur origin;
	private boolean afficherVec =false;
	
	
	
	
	private int cmptMiroir = 0;
	private boolean collision = false;
	
	
	
	private double angle;
	private ArrayList<Laser> listeLasers = new ArrayList<Laser>();
	private MiroirPlan plan;
	private MiroirConvexe convexe;
	private MiroirConcave concave;
	private ArrayList<MiroirPlan> listeMiroirPlan = new ArrayList<MiroirPlan>();
	private ArrayList<MiroirConvexe> listeMiroirConvexe = new ArrayList<MiroirConvexe>();
	private ArrayList<MiroirConcave> listeMiroirConcave = new ArrayList<MiroirConcave>();

	//Miora
	/**
	 * Constructeur de la classe
	 */
	public SceneMiroir() {
		angle = 90;
		character = new Personnage();

		position = new Vecteur(0.5, 10);

		vitesse = new Vecteur(3 ,0);

		gravite = new Vecteur(0,9.8);

		addMouseListener(new MouseAdapter() {

			//Miora
			/**
			 * Ecouteur de souris qui permet de dessiner le type de miroir choisi
			 */
			public void mousePressed(MouseEvent e) {
				if(miroirPlan == true && miroirConcave == false && miroirConvexe == false) {
					double posX = e.getX()/modele.getPixelsParUniteX();
					double posY = e.getY()/modele.getPixelsParUniteY();
					plan = new MiroirPlan (posX,posY, 0);
					listeMiroirPlan.add(plan);
					repaint();
				}
				if(miroirPlan == false && miroirConcave == false && miroirConvexe == true) {
					double posX = e.getX()/modele.getPixelsParUniteX();
					double posY = e.getY()/modele.getPixelsParUniteY();
					convexe = new MiroirConvexe (new Vecteur(posX, posY), 3);
					listeMiroirConvexe.add(convexe);
					repaint();
				}
				if(miroirPlan == false && miroirConcave == true && miroirConvexe == false) {
					double posX = e.getX()/modele.getPixelsParUniteX();
					double posY = e.getY()/modele.getPixelsParUniteY();
					concave = new MiroirConcave (new Vecteur(posX, posY), 3);
					listeMiroirConcave.add(concave);
					repaint();
				}
			}
		});
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				character.deplacerLePersoSelonTouche(e);
				shoot(e);
				repaint();
			}
		});
	}

	//Miora
	/**
	 * Cette methode permet de dessiner sur le composant g2d
	 * @param g le composant graphique
	 */

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
			//laser.setAngleTir(angle);
			laser.dessiner(g2d, mat, 0, 0);
		}

		for(MiroirPlan miroir:listeMiroirPlan) {
			g2d.setColor(Color.gray);
			miroir.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		}

		for(MiroirConvexe miroir:listeMiroirConvexe) {
			g2d.setColor(Color.gray);
			miroir.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		}

		for(MiroirConcave miroir:listeMiroirConcave) {
			//	g2d.setColor(Color.gray);
			miroir.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
			//	g2d.setColor(Color.yellow);
		}

		character.dessiner(g2d, mat, LARGEUR_DU_MONDE, HAUTEUR_DU_MONDE);

	}//fin paintComponent

	//Jeremy
	/**
	 * Cette methode permet de calculer une iteration physique
	 */
	private void calculerUneIterationPhysique() {
		for(Laser laser : listeLasers) {
			laser.move();
		}
		tempsTotalEcoule += deltaT;;
	}

	//Jeremy
	/**
	 * Cette methode permet d'arreter l'animation
	 */

	public void arreter( ) {
		if(enCoursAnimation)
			enCoursAnimation = false;
	}

	//Jeremy
	/**
	 * Cette methode permet de demarrer l'animation
	 */

	public void demarrer() {
		if (!enCoursAnimation) { 
			Thread proc = new Thread(this);
			proc.start();
			enCoursAnimation = true;
		}

	}

	//Jeremy
	/**
	 * Cette methode demarre l'animation
	 */
	public void run() {
		// TODO Auto-generated method stub
		while (enCoursAnimation) {	
			calculerUneIterationPhysique();
			repaint();

			try {
				colisionLaserMiroirPlan();
				colisionLaserMiroirConvexe();
				colisionLaserMiroirConcave();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(tempsDuSleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}//fin while
		System.out.println("Le thread est mort...");
	}

	//Arnaud
	/**
	 * Cette methode permet d'ecouter quel touche a ete actionne et s'il s'agit de la touche espace, le laser va s'actionner
	 * @param e : la touche
	 */
	private void shoot(KeyEvent e) {
		int code = e.getKeyCode();
		if(code == KeyEvent.VK_SPACE) {
			//	if(listeLasers.size() <1) { // Pour que 1 laser soit tirer  a la fois 
			listeLasers.add(
					new Laser(new Vecteur(
							character.getPositionX()+character.getLARGEUR_PERSO()/2,LARGEUR_DU_MONDE), angle, new Vecteur(0,0.5)));
			//}
		}
	}

	//Miora
	/**
	 * Cette methode verifie s'il y a une intersection entre deux aires
	 * @param aire1 : aire de la premiere geometrie
	 * @param aire2 : aire de la deuxieme geometrie
	 * @return vrai s'il y a intersection
	 */

	private boolean intersection(Area aire1, Area aire2) {
		Area aireInter = new Area(aire1);
		aireInter.intersect(aire2);
		if(!aireInter.isEmpty()) {
			return true;
		}
		return false;
	}

	//Miora
	/**
	 * Cette methode methode reoriente l'angle de depart du laser s'il y a une intersection
	 * avec un miroir plan
	 * @throws Exception
	 */

	private void colisionLaserMiroirPlan() throws Exception{
		for(MiroirPlan miroir : listeMiroirPlan ) {
			for(Laser laser : listeLasers) {
				if(intersection(miroir.getAireMiroir(), laser.getLaserAire())) {

					// v orientation rayon incident
					double angleLaser = Math.toRadians(laser.getAngleTir());
					Vecteur v = new Vecteur (Math.cos(angleLaser), Math.sin(angleLaser)).normalise();

					//n vecteur normal au miroir
					Vecteur n = miroir.getNormal().normalise();	

					//e = -v
					Vecteur e = v.multiplie(-1);
					double angle2 =  Math.toDegrees(Math.atan( (v.additionne(n.multiplie(2*(e.prodScalaire(n)))).getY() / ((v.additionne(n.multiplie(2*(e.prodScalaire(n))))).getX()))));
					laser.setAngleTir(angle2);
					System.out.println("le nouvel angle apres intersection miroir plan est : " + angle2);
				}	
			}
		}
	}

	//Miora
	/**
	 * Cette methode reoriente l'angle de depart du laser s'il y a une intersection
	 * avec un miroir convexe
	 * @throws Exception
	 */

	private void colisionLaserMiroirConvexe() throws Exception{
		System.out.println("entree dans colisionLaserMiroirConvexe");
		/*for(MiroirConvexe miroir : listeMiroirConvexe ) {
			for(Laser laser : listeLasers) {
				System.out.println("j'ai lanc� un laser");
				if(intersection(miroir.getAireMiroirConvexe(), laser.getLaserAire()) ) {
					comptInter++;
					if(comptInter == 1) {
						double angleLaser = Math.toRadians(laser.getAngleTir());
						Vecteur v = new Vecteur (Math.cos(angleLaser), Math.sin(angleLaser)).normalise();

						//n vecteur normal au miroir
						Vecteur n = miroir.getNormalPosition(laser.getPosition()).normalise();	
						//arreter();
						System.out.println("la normal " + n);

						//e = -v
						Vecteur e = v.multiplie(-1);

						double angle2=  Math.toDegrees(Math.atan( (v.additionne(n.multiplie(2*(e.prodScalaire(n)))).getY() / ((v.additionne(n.multiplie(2*(e.prodScalaire(n))))).getX()))));
						laser.setAngleTir(angle2);
						System.out.println("Intersection totale" + comptInter);
					}

				}
			}
		}
		 */

		for (Laser laser : listeLasers) {
			//System.out.println(listeMiroirConvexe.size() + "               " + collision);
		//	System.out.println("cmptMiroir" + cmptMiroir );
			while (cmptMiroir< listeMiroirConvexe.size() && collision == false) {
				System.out.println("je suis dans le while");
				if(intersection(listeMiroirConvexe.get(cmptMiroir).getAireMiroirConvexe(), laser.getLaserAire())) {
					System.out.println("Jai eu une intersection yey");
					collision = true;
					double angleLaser = Math.toRadians(laser.getAngleTir());
					Vecteur v = new Vecteur (Math.cos(angleLaser), Math.sin(angleLaser)).normalise();

					//n vecteur normal au miroir
					Vecteur n = listeMiroirConvexe.get(cmptMiroir).getNormalPosition(laser.getPosition()).normalise();	
					//arreter();
					System.out.println("la normal " + n);

					//e = -v
					Vecteur e = v.multiplie(-1);

					double angle2=  Math.toDegrees(Math.atan( (v.additionne(n.multiplie(2*(e.prodScalaire(n)))).getY() / ((v.additionne(n.multiplie(2*(e.prodScalaire(n))))).getX()))));
					laser.setAngleTir(angle2);
					cmptMiroir++;
				}
				
			}
		}
	}

	//Miora et Arezki
	/**
	 * Cette methode methode reoriente l'angle de depart du laser s'il y a une intersection
	 * avec un miroir concave
	 * @throws Exception
	 */

	private void colisionLaserMiroirConcave() throws Exception{
		for(MiroirConcave miroirC : listeMiroirConcave ) {
			for(Laser laser : listeLasers) {
				if(intersection(miroirC.aire(), laser.getLaserAire())) {
					System.out.println("j'ai une intersection");
					//laser.setPosition(new Vecteur (0,0));
					double angleLaser = Math.toRadians(laser.getAngleTir());

					Vecteur v = new Vecteur (Math.cos(angleLaser), Math.sin(angleLaser)).normalise();
					//n vecteur normal au miroir
					Vecteur n = miroirC.getNormalPosition(laser.getPosition()).normalise();	
					repaint();

					//e = -v
					Vecteur e = v.multiplie(-1);

					Vecteur resultat = v.additionne(n.multiplie(2.0).multiplie(e.prodScalaire(n)));
					laser.setAngleTir( Math.toDegrees(Math.atan( resultat.getY()/resultat.getX())));

				}
			}
		}
	}

	//Miora
	/**
	 * Cette methode permet de savoir si l'utilisateur veut oui ou non dessiner un miroir plan
	 * @param reponse : vrai s'il veut dessiner un miroir plan
	 *@author Miora
	 */
	public void setMiroirPlan(boolean reponse) {
		miroirPlan = reponse;
	}

	//Miora
	/**
	 * Cette methode permet de savoir si l'utilisateur veut oui ou non dessiner un miroir convexe
	 * @param reponse : vrai s'il veut dessiner un miroir plan
	 */
	public void setMiroirConvexe(boolean reponse) {
		miroirConvexe = reponse;
	}

	//Miora
	/**
	 * Cette methode permet de savoir si l'utilisateur veut oui ou non dessiner un miroir concave
	 * @param reponse : vrai s'il veut dessiner un miroir plan
	 */
	public void setMiroiConcave(boolean reponse) {
		miroirConcave = reponse;
	}

}