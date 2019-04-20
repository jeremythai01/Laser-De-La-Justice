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

/**
 * Cette classe permet de visualiser le test
 * @author Miora, Arezki, Jeremy et Arnaud
 **/
public class SceneMiroir extends JPanel implements Runnable {

	private int tempsDuSleep = 30;
	private double deltaT = 0.06;
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

	private Vecteur vitesse;

	private Vecteur gravite ;

	private Personnage character;
	private VecteurGraphique q;
	private Vecteur origin;
	private boolean afficherVec =false;
	Laser laser2;


	private int nbCollison = 0;


	private double ANGLE_DE_MIROIR;


	private double angleLaser;
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
					plan = new MiroirPlan (new Vecteur (posX,posY), ANGLE_DE_MIROIR);
					listeMiroirPlan.add(plan);
					repaint();
				}
				if(miroirPlan == false && miroirConcave == false && miroirConvexe == true) {
					double posX = e.getX()/modele.getPixelsParUniteX();
					double posY = e.getY()/modele.getPixelsParUniteY();
					convexe = new MiroirConvexe (new Vecteur(posX, posY), 4, (int) ANGLE_DE_MIROIR);
					listeMiroirConvexe.add(convexe);
					repaint();
				}
				if(miroirPlan == false && miroirConcave == true && miroirConvexe == false) {
					double posX = e.getX()/modele.getPixelsParUniteX();
					double posY = e.getY()/modele.getPixelsParUniteY();
					concave = new MiroirConcave (new Vecteur(posX, posY), 4, ANGLE_DE_MIROIR);
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
			if(laser.getPositionHaut().getY() <= 0 ) {
				listeLasers.remove(laser);
			}
			//g2d.setStroke( new BasicStroke(3));
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


		//Rectangle2D.Double ligne = new Rectangle2D.Double(getWidth()/2, 0, 0 , getHeight() );
		//System.out.println("position de la souris  = " + xSouris);
		g2d.setColor(Color.black);
		//g2d.draw(ligne);

		if(afficherVec) {
			double diam = 0.25;
			Ellipse2D.Double pts = new Ellipse2D.Double(posInter.getX()-diam/2,posInter.getY()-diam/2,diam,diam);
			//laser2.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
			g2d.setStroke(new BasicStroke(3));
			g2d.setColor(Color.green);
			g2d.fill(mat.createTransformedShape(pts));
			/*VecteurGraphique vec = new VecteurGraphique(normal.getX(), normal.getY(), posInter.getX(), posInter.getY());
			vec.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
			 */
		}

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
					HAUTEUR_DU_MONDE - character.getLONGUEUR_PERSO()), angleLaser, new Vecteur(0,.5)));
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
		for(Laser laser : listeLasers) {
			int n=0;
			boolean collision = false;
			while(n< listeMiroirPlan.size() && collision == false) {
				if(intersection(listeMiroirPlan.get(n).getAireMiroirPixel(), laser.getAire())) {
					collision = true;
					System.out.println("position haut laser anim " + laser.getPositionHaut());

					Vecteur ptsLaser = laser.getPositionHaut(); // un point du laser
					Vecteur vecDirLaser = (new Vecteur (Math.cos(Math.toRadians(-laser.getAngleTir()) ) , Math.sin(Math.toRadians(-laser.getAngleTir()) ))).normalise();;
					//Vecteur vecDirLaser = (new Vecteur (0,1));
					System.out.println("pts laser : " + ptsLaser + "\n" +  "vec dir : " +vecDirLaser );


					Vecteur ptsMiroir = listeMiroirPlan.get(n).getPosition();
					Vecteur vecDirMiroir = (new Vecteur (Math.cos(Math.toRadians(-listeMiroirPlan.get(n).getAngle()) ) , Math.sin(Math.toRadians(-listeMiroirPlan.get(n).getAngle()))));
					System.out.println("pts miroir : " + ptsMiroir + "\n" +  "vec miroir : " +vecDirMiroir );


					Vecteur sous = (ptsLaser.soustrait(ptsMiroir)).multiplie(-1); // de l'autre cote equation
					Vecteur kMiroir = (new Vecteur (0,0)).soustrait(vecDirMiroir); // devient moins


					double [] inter = intersectionCramer(vecDirLaser.getX(), kMiroir.getX(), vecDirLaser.getY(), kMiroir.getY(), sous.getX(), sous.getY());

					double x = ptsLaser.getX() + inter[0]*vecDirLaser.getX();
					double y= ptsLaser.getY() + inter[0]*vecDirLaser.getY();

					double x1 = ptsMiroir.getX()+inter[1]*vecDirMiroir.getX();
					double y1 = ptsMiroir.getY()+inter[1]*vecDirMiroir.getY();

					//afficherVec = true;
					posInter = new Vecteur (x,y);

					System.out.println(" ");
					if(listeMiroirPlan.get(n).getAngle()<180) {
						normal =listeMiroirPlan.get(n).getNormal().normalise().multiplie(-1);
					}else {
						normal =listeMiroirPlan.get(n).getNormal().normalise();
					}
					System.out.println("La normal est du miroir est :" +normal);

					double angleR = Math.toRadians(laser.getAngleTir()) ;	
					System.out.println("     " + laser.getAngleTir());
					Vecteur incident = new Vecteur(Math.cos(angleR), Math.sin(angleR)).normalise();
					System.out.println("Orientation incident :" + incident);

					Vecteur reflexion = incident.additionne(normal.multiplie(2.0*(incident.multiplie(-1).prodScalaire(normal))));
					System.out.println("Orientation apres reflexion" + reflexion);	

					//change orientation 
					double angleReflexion = Math.toDegrees(Math.atan(reflexion.getY()/reflexion.getX()));
					System.out.println("Angle reflexion en degree " + angleReflexion);
					if(reflexion.getX()<0) {
						laser.setAngleTir(angleReflexion+180);
					}else {
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
						System.out.print(c[i]);
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



	//Miora
	/**
	 * Cette methode reoriente l'angle de depart du laser s'il y a une intersection
	 * avec un miroir convexe
	 * @throws Exception
	 */

	private void colisionLaserMiroirConvexe() throws Exception{
		for(Laser laser : listeLasers) {
			int n=0;
			boolean collision = false;
			while(n< listeMiroirConvexe.size() && collision == false) {
				for(Ligne ligne :listeMiroirConvexe.get(n).getListeLigne()) {
					if(intersection(ligne.getAireLigne(), laser.getAire())){
						collision = true;
						//System.out.println("position haut laser anim " + laser.getPositionHaut());

						Vecteur ptsLaser = laser.getPositionHaut(); // un point du laser
						Vecteur vecDirLaser = (new Vecteur (Math.cos(Math.toRadians(-laser.getAngleTir()) ) , Math.sin(Math.toRadians(-laser.getAngleTir()) ))).normalise();;
						//Vecteur vecDirLaser = (new Vecteur (0,1));
						//System.out.println("pts laser : " + ptsLaser + "\n" +  "vec dir : " +vecDirLaser );


						Vecteur ptsMiroir = new Vecteur (ligne.getX1(), ligne.getY2());
						Vecteur vecDirMiroir = ligne.getVecDir();
						//System.out.println("pts miroir : " + ptsMiroir + "\n" +  "vec miroir : " +vecDirMiroir );


						Vecteur sous = (ptsLaser.soustrait(ptsMiroir)).multiplie(-1); // de l'autre cote equation
						Vecteur kMiroir = (new Vecteur (0,0)).soustrait(vecDirMiroir); // devient moins


						double [] inter = intersectionCramer(vecDirLaser.getX(), kMiroir.getX(), vecDirLaser.getY(), kMiroir.getY(), sous.getX(), sous.getY());

						double x = ptsLaser.getX() + inter[0]*vecDirLaser.getX();
						double y= ptsLaser.getY() + inter[0]*vecDirLaser.getY();

						posInter = new Vecteur (x,y);


						normal = listeMiroirConvexe.get(n).getNormal(posInter);

						//System.out.println("La normal est du miroir est :" +normal);

						double angleR = Math.toRadians(laser.getAngleTir()) ;	
						//	System.out.println("     " + laser.getAngleTir());
						Vecteur incident = new Vecteur(Math.cos(angleR), Math.sin(angleR)).normalise();
						//System.out.println("Orientation incident :" + incident);

						Vecteur reflexion = incident.additionne(normal.multiplie(2.0*(incident.multiplie(-1).prodScalaire(normal))));
						System.out.println("Orientation apres reflexion" + reflexion);	

						//change orientation 
						double angleReflexion = Math.toDegrees(Math.atan(reflexion.getY()/reflexion.getX()));
						System.out.println("Angle reflexion en degree " + angleReflexion);
						if(reflexion.getY()<-1 && reflexion.getX() <0) {
							System.out.println("premier");
							laser.setAngleTir(-angleReflexion);
						}else if(reflexion.getX() >0 && reflexion.getY() <0 ) {
							System.out.println("deuxieme");
							laser.setAngleTir(angleReflexion-90);
						}else if(reflexion.getX()>0 && reflexion.getY()>1) {
							System.out.println("troisieme");
							laser.setAngleTir(-1*angleReflexion);
						}else if (reflexion.getX()<0 &&reflexion.getY()>-1){
							System.out.println("quatrieme");
							laser.setAngleTir(180+angleReflexion);
						}
						System.out.println("angle final" + laser.getAngleTir());
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
							System.out.print(c[i]);
							System.out.println();
						} 
						laser.setPositionHaut(new Vecteur (c[0], c[1]));
						System.out.println("laser bas : " + laser.getPositionBas() + "laser haut " + laser.getPositionHaut() );
						System.out.println("-----------------------------------------------------------------------------");
					}
				}


				n++;

			}
		}
	}
	// fin methode

	//Miora 
	/**
	 * Cette methode methode reoriente l'angle de depart du laser s'il y a une intersection
	 * avec un miroir concave
	 * @throws Exception
	 */

	private void colisionLaserMiroirConcave() throws Exception{
		for(Laser laser : listeLasers) {
			int n=0;
			boolean collision = false;
			while(n< listeMiroirConcave.size() && collision == false) {
				for(Ligne ligne :listeMiroirConcave.get(n).getListeLigne()) {
					if(intersection(ligne.getAireLigne(), laser.getAire())){
						collision = true;
						//System.out.println("position haut laser anim " + laser.getPositionHaut());

						Vecteur ptsLaser = laser.getPositionHaut(); // un point du laser
						Vecteur vecDirLaser = (new Vecteur (Math.cos(Math.toRadians(-laser.getAngleTir()) ) , Math.sin(Math.toRadians(-laser.getAngleTir()) ))).normalise();;
						//Vecteur vecDirLaser = (new Vecteur (0,1));
						//System.out.println("pts laser : " + ptsLaser + "\n" +  "vec dir : " +vecDirLaser );


						Vecteur ptsMiroir = new Vecteur (ligne.getX1(), ligne.getY2());
						Vecteur vecDirMiroir = ligne.getVecDir();
						//System.out.println("pts miroir : " + ptsMiroir + "\n" +  "vec miroir : " +vecDirMiroir );


						Vecteur sous = (ptsLaser.soustrait(ptsMiroir)).multiplie(-1); // de l'autre cote equation
						Vecteur kMiroir = (new Vecteur (0,0)).soustrait(vecDirMiroir); // devient moins


						double [] inter = intersectionCramer(vecDirLaser.getX(), kMiroir.getX(), vecDirLaser.getY(), kMiroir.getY(), sous.getX(), sous.getY());

						double x = ptsLaser.getX() + inter[0]*vecDirLaser.getX();
						double y= ptsLaser.getY() + inter[0]*vecDirLaser.getY();

						posInter = new Vecteur (x,y);


						normal = listeMiroirConcave.get(n).getNormal(posInter).normalise();

						System.out.println("La normal est du miroir est :" +normal);

						double angleR = Math.toRadians(laser.getAngleTir()) ;	
						//	System.out.println("     " + laser.getAngleTir());
						Vecteur incident = new Vecteur(Math.cos(angleR), Math.sin(angleR)).normalise();
						//System.out.println("Orientation incident :" + incident);

						Vecteur reflexion = incident.additionne(normal.multiplie(2.0*(incident.multiplie(-1).prodScalaire(normal))));


						//change orientation 
						double angleReflexion = Math.toDegrees(Math.atan(reflexion.getY()/reflexion.getX()));
						System.out.println("angle ini aavant changement :" + angleReflexion );

						if(normal.getX()<0 && normal.getY()>0) {
							System.out.println("droite");
							if(reflexion.getX()>0) {
								reflexion.setX(-reflexion.getX());
							}
							if(reflexion.getY()<0) {
								reflexion.setY(-reflexion.getY());
							}
							angleReflexion = Math.toDegrees(Math.atan(reflexion.getY()/reflexion.getX()));
							System.out.println("droite" + angleReflexion);
							laser.setAngleTir(180-angleReflexion);

						}
						if(normal.getX()>0 && normal.getY()>0){
							System.out.println("je suis a gauche");
							if(reflexion.getX()<0) {
								reflexion.setX(-reflexion.getX());
							}
							if(reflexion.getY() >0) {
								reflexion.setY(-reflexion.getY());
							}
							angleReflexion = Math.toDegrees(Math.atan(reflexion.getY()/reflexion.getX()));
							laser.setAngleTir(angleReflexion);
						}


						System.out.println("Angle reflexion en degree " + angleReflexion);

						System.out.println("angle final" + laser.getAngleTir());
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
							System.out.print(c[i]);
							System.out.println();
						} 
						laser.setPositionHaut(new Vecteur (c[0], c[1]));
						System.out.println("laser bas : " + laser.getPositionBas() + "laser haut " + laser.getPositionHaut() );
						System.out.println("-----------------------------------------------------------------------------");
					}
				}


				n++;

			}
		}
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
		ANGLE_DE_MIROIR = value;

	}

	public double getAngleLaser() {
		return angleLaser;
	}

	public void setAngleLaser(double angleLaser) {
		this.angleLaser = angleLaser;
	}

	public void nettoyer() {
		listeMiroirPlan.removeAll(listeMiroirPlan);
		listeMiroirConvexe.removeAll(listeMiroirConvexe);
		listeMiroirConcave.removeAll(listeMiroirConcave);
		repaint();
	}


}
