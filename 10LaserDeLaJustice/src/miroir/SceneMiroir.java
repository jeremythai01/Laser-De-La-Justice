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
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import geometrie.Vecteur;
import geometrie.VecteurGraphique;
import physique.Laser;
import utilite.ModeleAffichage;
import utilite.OutilsMath;

/**
 * Cette classe permet de visualiser le test
 * @author Miora, Arezki, Jeremy et Arnaud
 **/
public class SceneMiroir extends JPanel implements Runnable {

	private int tempsDuSleep = 30;
	private double deltaT = 0.6;
	private  double LARGEUR_DU_MONDE = 30; //en metres
	private  double HAUTEUR_DU_MONDE;
	private double tempsTotalEcoule = 0;
	private boolean enCoursAnimation= false;
	private boolean premiereFois = true;
	private ModeleAffichage modele;
	private AffineTransform mat;
	private Vecteur position;
	private boolean miroirPlan = true, miroirConvexe = false, miroirConcave = false;
	Vecteur posInter;
	Vecteur normal;
	private boolean modeScientifique = false;

	private Vecteur vitesse;

	private Vecteur gravite ;

	private Personnage character;
	private VecteurGraphique q;

	private double angle = 0;


	private double angleLaser;
	private ArrayList<Laser> listeLasers = new ArrayList<Laser>();
	private MiroirPlan plan;
	private MiroirCourbe miroirCourbe;
	private ArrayList<MiroirPlan> listeMiroirPlan = new ArrayList<MiroirPlan>();
	private ArrayList<MiroirCourbe> listeMiroirCourbe = new ArrayList<MiroirCourbe>();

	//Miora
	/**
	 * Constructeur de la classe
	 */
	public SceneMiroir() {
		setBackground(Color.cyan);
		angleLaser = 90;
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
					plan = new MiroirPlan (new Vecteur (posX,posY), angle, 8);
					listeMiroirPlan.add(plan);
					repaint();
				}
				if(miroirPlan == false && miroirConcave == false && miroirConvexe == true) {
					double posX = e.getX()/modele.getPixelsParUniteX();
					double posY = e.getY()/modele.getPixelsParUniteY();
					miroirCourbe = new MiroirCourbe (new Vecteur(posX, posY), 8, (int) angle);
					listeMiroirCourbe.add(miroirCourbe);
					repaint();
				}
				if(miroirPlan == false && miroirConcave == true && miroirConvexe == false) {
					double posX = e.getX()/modele.getPixelsParUniteX();
					double posY = e.getY()/modele.getPixelsParUniteY();
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
			laser.dessiner(g2d, mat, 0, 0);
		}

		for(MiroirPlan miroir:listeMiroirPlan) {
			g2d.setColor(Color.gray);
			miroir.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		}


		for(MiroirCourbe miroir:listeMiroirCourbe) {
			g2d.setColor(Color.gray);
			miroir.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		}


		character.dessiner(g2d, mat, LARGEUR_DU_MONDE, HAUTEUR_DU_MONDE);

		g2d.setColor(Color.black);

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


			try {
				colisionLaserMiroirPlan();
				colisionLaserMiroirCourbe();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(tempsDuSleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			calculerUneIterationPhysique();
			repaint();
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
			listeLasers.add(new Laser(new Vecteur(character.getPositionX() + character.getLARGEUR_PERSO() / 2,
					HAUTEUR_DU_MONDE - character.getLONGUEUR_PERSO()), angleLaser, new Vecteur(0,.5), Color.RED) );
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

	private boolean enIntersection(Area aire1, Area aire2) {
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
		for(Laser laser : listeLasers) {
			int n=0;
			boolean collision = false;
			while(n< listeMiroirPlan.size() && collision == false) {
				if(enIntersection(listeMiroirPlan.get(n).getAireMiroirPixel(), laser.getAire())) {
					collision = true;
					
					Vecteur ptsLaser = laser.getPositionHaut(); // un point du laser
					System.out.println("pts Laser : " + ptsLaser );
					System.out.println("angle miroir " + listeMiroirPlan.get(n).getAngle());

					double angleR = Math.toRadians(laser.getAngleTir()) ;	
					Vecteur vecDirLaser = (new Vecteur (Math.cos(angleR), -(Math.sin(angleR)))).normalise();
					System.out.println("dir laser : " + vecDirLaser);

					Vecteur ptsMiroir = listeMiroirPlan.get(n).getPosition();
					Vecteur vecDirMiroir = (new Vecteur (Math.cos(Math.toRadians(listeMiroirPlan.get(n).getAngle()) ) , -Math.sin(Math.toRadians(listeMiroirPlan.get(n).getAngle()))));
					System.out.println("pts miroir : " + ptsMiroir + "\n" +  "vec miroir : " +vecDirMiroir );


					Vecteur sous = (ptsLaser.soustrait(ptsMiroir)).multiplie(-1); // de l'autre cote equation
					Vecteur kMiroir = (new Vecteur (0,0)).soustrait(vecDirMiroir); // devient moins


					double [] inter = intersectionCramer(vecDirLaser.getX(), kMiroir.getX(), vecDirLaser.getY(), kMiroir.getY(), sous.getX(), sous.getY());

					double x = ptsLaser.getX() + inter[0]*vecDirLaser.getX();
					double y= ptsLaser.getY() + inter[0]*vecDirLaser.getY();
					
					
					posInter = new Vecteur (x,y);
					System.out.println("position intersection :" + posInter);
					System.out.println(" ");

					
					//Calcul selon g2d
					Vecteur normal = listeMiroirPlan.get(n).getNormal(vecDirLaser).normalise();

					System.out.println("normal " + normal);
					if(modeScientifique) {
						listeMiroirPlan.get(n).afficherVecteur(posInter);
					}

					Vecteur incident = (new Vecteur (Math.cos(angleR), -(Math.sin(angleR)))).normalise();
					System.out.println("incident" + incident);

					Vecteur reflexion = (incident.additionne(normal.multiplie(2.0*(incident.multiplie(-1).prodScalaire(normal))))).normalise();
					System.out.println("Orientation apres reflexion" + reflexion);	

					//ajustement en systeme d'axe normal
					reflexion = new Vecteur (reflexion.getX(),-1*reflexion.getY());

					laser.setAngleTir(ajustementArcTan(reflexion));
					System.out.println("l'angle de tir du laser" + laser.getAngleTir());

					System.out.println("pos haut fleche apres trans angle : " + laser.getPositionHaut() + " bas : " + laser.getPositionBas());
					laser.setPositionHaut(new Vecteur (translation(laser)[0],translation(laser)[1]));

					System.out.println("laser bas : " + laser.getPositionBas() + "laser haut " + laser.getPositionHaut() );
					System.out.println("-----------------------------------------------------------------------------");
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
						
						Vecteur ptsLaser = laser.getPositionHaut(); // un point du laser
						System.out.println("centre miroir" + listeMiroirCourbe.get(n).getPosition());

						double angleR = Math.toRadians(laser.getAngleTir()) ;	
						Vecteur vecDirLaser = (new Vecteur (Math.cos(angleR), -(Math.sin(angleR)))).normalise();
						System.out.println("dir laser : " + vecDirLaser);

						Vecteur ptsMiroir = (new Vecteur (ligne.getX1(), ligne.getY1()));
						System.out.println("pts miroir " + ptsMiroir);
						
						Vecteur vecDirMiroir = new Vecteur (ligne.x2-ligne.x1, ligne.y2-ligne.y1).normalise();
						System.out.println("vecDirMiroir = " + vecDirMiroir);

						Vecteur sous = (ptsLaser.soustrait(ptsMiroir)).multiplie(-1); // de l'autre cote equation
						Vecteur kMiroir = (new Vecteur (0,0)).soustrait(vecDirMiroir); // devient moins
						double [] inter = OutilsMath.intersectionCramer(vecDirLaser.getX(), kMiroir.getX(), vecDirLaser.getY(), kMiroir.getY(), sous.getX(), sous.getY());

						double x = ptsLaser.getX() + inter[0]*vecDirLaser.getX();
						double y= ptsLaser.getY() + inter[0]*vecDirLaser.getY();
						Vecteur posInter = new Vecteur (x,y);
						System.out.println("position inter" + posInter);

						//Les calculs sont fait selon l'orientation de g2d 
						Vecteur normal = listeMiroirCourbe.get(n).getNormal(posInter).normalise();
						System.out.println("La normal du miroir est :" +normal);



						Vecteur incident = (new Vecteur (Math.cos(angleR), -(Math.sin(angleR)))).normalise();
						System.out.println("incident" + incident);

						Vecteur reflexion = (incident.additionne(normal.multiplie(2.0*(incident.multiplie(-1).prodScalaire(normal))))).normalise();
						System.out.println("Orientation apres reflexion" + reflexion);	

						//ajustement en systeme d'axe normal
						reflexion = new Vecteur (reflexion.getX(),-1*reflexion.getY());

						laser.setAngleTir(ajustementArcTan(reflexion));
						System.out.println(laser.getAngleTir());

						System.out.println("pos haut fleche apres trans angle : " + laser.getPositionHaut() + " bas : " + laser.getPositionBas());
						laser.setPositionHaut(new Vecteur (translation(laser)[0],translation(laser)[1]));
						
						System.out.println("laser bas : " + laser.getPositionBas() + "laser haut " + laser.getPositionHaut() );
						System.out.println("-----------------------------------------------------------------------------");
					}
				}


				n++;

			}
		}
	}
	// fin methode

	private double ajustementArcTan(Vecteur angle) {
		double angleDegre = Math.abs(Math.toDegrees(Math.atan(angle.getY()/angle.getX())));
		if(angle.getX() >=0  && angle.getY() >=0) {
			//premier quadrant
			System.out.println("premier");
			return angleDegre;
		}else if (angle.getX() <= 0 && angle.getY()>=0 ){
			//deuxieme quadrant
			System.out.println("2e");
			return (180-angleDegre);
		}else if (angle.getX() <= 0 && angle.getY()<=0 ){
			//troisieme quadrant
			System.out.println("3e");
			return (180+ angleDegre);
		}else if (angle.getX() >= 0 && angle.getY()<=0 ){
			//quatrieme quadrant
			System.out.println("4e");
			return (-angleDegre);
		}
		return 0; // caprice de Java
	}


	//Par Miora
	/**
	 * Cette methode permet d'appliquer une translation a la tete du laser pour ne plus etre en intersection et rester dans la boucle
	 * @param laser le laser 
	 * @return la nouvelle position de la tete du laser
	 */
	private double[] translation(Laser laser) {
		double decalage = 1.5; // Pour annuler le "en intersection"
		double xt = decalage*(laser.getPositionHaut().getX()-laser.getPositionBas().getX()); // translation x
		double yt = decalage*(laser.getPositionHaut().getY() - laser.getPositionBas().getY()); // translation y
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
		} 
		return c;
	}

	private double [] intersectionCramer(double a, double b, double c, double d, double e, double f) {
		double det, k,t;
		double [] tab = new double [2];
		det = a*d-b*c;
		tab[0] = (e*d-b*f)/det; //t
		tab[1] = (a*f-e*c)/det; //k
		return tab;
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

	public void setAngleMiroir(int value) {
		angle = value;

	}

	public double getAngleLaser() {
		return angleLaser;
	}

	public void setAngleLaser(double angleLaser) {
		this.angleLaser = angleLaser;
	}

	public void nettoyer() {
		listeMiroirPlan.removeAll(listeMiroirPlan);
		listeMiroirCourbe.removeAll(listeMiroirCourbe);
		repaint();
	}

	public void setScientifique() {
		modeScientifique = true;
	}


}
