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
	private  double LARGEUR_DU_MONDE = 65; //en metres
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


	private double ANGLE_DE_MIROIR = 0;


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
					convexe = new MiroirConvexe (new Vecteur(posX, posY), 4, ANGLE_DE_MIROIR);
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



		character.dessiner(g2d, mat, LARGEUR_DU_MONDE, HAUTEUR_DU_MONDE);

	}//fin paintComponent

	//Jeremy
	/**
	 * Cette methode permet de calculer une iteration physique
	 */
	private void calculerUneIterationPhysique() {
		System.out.println("dans calcul it phys...");
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
				//System.out.println(laser.getPointHaut().toString());
				//System.out.println("laser haut :" + laser.getPositionHaut());
				System.out.println("calcul distance" + listeMiroirPlan.get(n).getLine().ptLineDist(laser.getPointHaut()));
				if(listeMiroirPlan.get(n).getLine().ptLineDist(laser.getPointHaut()) < 0.04) {
					Vecteur posInt = laser.getPositionHaut();

					collision = true;

					normal =listeMiroirPlan.get(n).getNormal().normalise();
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
					if(Math.abs(listeMiroirPlan.get(n).getAngle())>90) {
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
	 * Cette methode reoriente l'angle de depart du laser s'il y a une intersection
	 * avec un miroir convexe
	 * @throws Exception
	 */

	private void colisionLaserMiroirConvexe() throws Exception{
		for(Laser laser : listeLasers) {
			int n=0;
			boolean collision = false;
			while(n< listeMiroirConvexe.size() && collision == false) {
				System.out.println("pts laser :  "+laser.getPointHaut());
				System.out.println("position miroir :  "+ listeMiroirConvexe.get(n).getPosition());
				for(Point2D.Double points : listeMiroirConvexe.get(n).getListePoint() ) {
					if(points.distance(laser.getPointHaut())<0.4) {
						System.out.println(points.distance(laser.getPointHaut()));
					}
					if (points.distance(laser.getPointHaut()) <0.06){
						collision = true;
						arreter();
						/*
						Vecteur vecLaser = laser.getPositionHaut(); // un point du laser
						System.out.println("laser" + vecLaser );
						Vecteur vecDirLaser = (new Vecteur (Math.cos(Math.toRadians(laser.getAngleTir()) ) , Math.sin(Math.toRadians(laser.getAngleTir()) ))).normalise();;
						System.out.println("vecteur dir laser " +vecDirLaser );

						Vecteur vecMiroir = listeMiroirConvexe.get(n).getPosition(); // un point du miroir
						Vecteur vecDirMiroir = (new Vecteur (Math.cos(Math.toRadians(listeMiroirPlan.get(n).getAngle()) ) , Math.sin(Math.toRadians(listeMiroirPlan.get(n).getAngle()) ))).normalise();
						System.out.println("miroir" + vecMiroir );
						System.out.println("vecteur dir Miroir " +vecDirMiroir );
						Vecteur sous = (vecLaser.soustrait(vecMiroir));
						System.out.println("haut av intersection : " + laser.getPositionHaut() + "bas laser av inter : " + laser.getPositionBas());
						double [] inter = intersectionCramer(vecDirMiroir.getX()*-1,vecDirLaser.getX(),vecDirMiroir.getY()*-1,vecDirLaser.getY(),sous.getX()*-1, sous.getY()*-1);

						double x = vecMiroir.getX()+inter[0]*(vecDirMiroir.getX());
						double y= vecMiroir.getY()+inter[0]*(vecDirMiroir.getY());


						//afficherVec = true;
						posInter = new Vecteur (x,y);
						System.out.println("interection laser et miroir " + posInter);
						afficherVec = true;


						Vecteur normal =listeMiroirPlan.get(n).getNormal().normalise();
						System.out.println("\n"+"La normal est du miroir est :" +normal);

						double angleR = Math.toRadians(laser.getAngleTir() ) ;
						Vecteur incident = new Vecteur(Math.cos(angleR), Math.sin(angleR)).normalise();

						System.out.println("Orientation du laser :" + incident);

						Vecteur reflexion = incident.additionne(normal.multiplie(2.0*(incident.multiplie(-1).prodScalaire(normal))));
						//Vecteur reflexion = incident.additionne(normal.multiplie(2.0*incident.multiplie(-1.0).prodScalaire(normal)));
						System.out.println("Orientation apres reflexion" + reflexion);


						//change orientation 
						double angleReflexion = Math.toDegrees(Math.atan(reflexion.getY()/reflexion.getX()));
						System.out.println("Angle reflexion en degree " + angleReflexion);
						System.out.println("angle rad reflexion" + Math.atan(reflexion.getY()/reflexion.getX()));
						if(reflexion.getX()<0) {
							System.out.println("ici");
							laser.setAngleTir(angleReflexion+180);
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
							}//end of k loop  
							System.out.print(c[i]+" ");
							System.out.println();
						} 
						laser.setPositionHaut(new Vecteur (c[0], c[1]));
						System.out.println("laser bas : " + laser.getPositionBas() + "laser haut " + laser.getPositionHaut() );
						*/
						System.out.println("-----------------------------------------------------------------------------");
						
					} //fin collisions
				} // fin liste points
				n++;
			}

		}
	} // fin methode

	//Miora et Arezki
	/**
	 * Cette methode methode reoriente l'angle de depart du laser s'il y a une intersection
	 * avec un miroir concave
	 * @throws Exception
	 */

	private void colisionLaserMiroirConcave() throws Exception{
		for(MiroirConcave miroirC : listeMiroirConcave ) {
			for(Laser laser : listeLasers) {
				if(intersection(miroirC.aire(), laser.getAire())) {
					System.out.println("j'ai une intersection");
					//laser.setPosition(new Vecteur (0,0));
					double angleLaser = Math.toRadians(laser.getAngleTir());

					Vecteur v = new Vecteur (Math.cos(angleLaser), Math.sin(angleLaser)).normalise();
					//n vecteur normal au miroir
					Vecteur n = miroirC.getNormalPosition(laser.getPositionHaut()).normalise();	
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

	public void setAngleMiroir(int value) {
		ANGLE_DE_MIROIR = value;

	}

	public double getAngleLaser() {
		return angleLaser;
	}

	public void setAngleLaser(double angleLaser) {
		this.angleLaser = angleLaser;
	}


}
