package miora;

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
import personnage.Personnage;
import physique.Balle;
import physique.Laser;
import utilite.ModeleAffichage;

public class SceneTestMiroirs extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	private int tempsDuSleep = 25;
	private double deltaT = 0.07;
	private  double LARGEUR_DU_MONDE = 10; //en metres
	private  double HAUTEUR_DU_MONDE;
	private boolean enCoursAnimation= false;
	private double tempsTotalEcoule = 0;
	private double masse = 15; //en kg
	private double diametre = 2;  //em mètres
	private Balle balle1;
	private Balle balle;
	private boolean premiereFois = true;
	private ModeleAffichage modele;
	private AffineTransform mat;
	private VecteurGraphique vecDess;
	private double angleMiroir = 0;
	private Vecteur position;

	private Vecteur vitesse;

	private Vecteur gravity ;

	private Personnage character;

	private boolean miroirPlan = true, miroirConvexe = false;

	private double angle;
	private ArrayList<Laser> listeLasers = new ArrayList<Laser>();
	private MiroirPlan plan;
	private MiroirConvexe convexe;
	private ArrayList<MiroirPlan> listeMiroirPlan = new ArrayList<MiroirPlan>();
	private ArrayList<MiroirConvexe> listeMiroirConvexe = new ArrayList<MiroirConvexe>();

	/**
	 * Create the panel.
	 */
	public SceneTestMiroirs() {
		angle = -90;
		character = new Personnage(37,39);

		position = new Vecteur(0.5, 10);

		vitesse = new Vecteur(3 ,0);

		gravity = new Vecteur(0,9.8);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(miroirPlan) {
					double posX = e.getX()/modele.getPixelsParUniteX();
					double posY = e.getY()/modele.getPixelsParUniteY();
					plan = new MiroirPlan (posX,posY, 0);
					listeMiroirPlan.add(plan);
					repaint();
				}else {
					double posX = e.getX()/modele.getPixelsParUniteX();
					double posY = e.getY()/modele.getPixelsParUniteY();
					convexe = new MiroirConvexe (posX,posY, 3);
					listeMiroirConvexe.add(convexe);
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
	} // fin constructeur

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
			/*if(laser.getLigneFinY() <= 0 ) {
				listeLasers.remove(laser);
			}
			*/
			//g2d.setStroke( new BasicStroke(3));
			//laser.setAngleTir(angle);
			laser.dessiner(g2d, mat, 0, 0);
		}
		try {
			colisionLaserMiroirPlan();
			colisionLaserMiroirPlan();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(MiroirPlan miroir:listeMiroirPlan) {
			g2d.setColor(Color.gray);
			miroir.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
			g2d.setColor(Color.yellow);
		}
		for(MiroirConvexe convexe :listeMiroirConvexe) {
			g2d.setColor(Color.gray);
			convexe.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
			g2d.setColor(Color.yellow);
		}
		character.dessiner(g2d, mat, LARGEUR_DU_MONDE, HAUTEUR_DU_MONDE);
	}//fin paintComponent

	private void calculerUneIterationPhysique() {
		for(Laser laser : listeLasers) {
			laser.move();
		}
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


	private void shoot(KeyEvent e) {
		int code = e.getKeyCode();
		if(code == KeyEvent.VK_SPACE) {
			if(listeLasers.size() <1) { // Pour que 1 laser soit tirer  a la fois 
				listeLasers.add(
						new Laser(new Vecteur(
								character.getPositionX()+character.getLARGEUR_PERSO()/2,LARGEUR_DU_MONDE), angle, new Vecteur(0,0.5)));
			}
		}
	}
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
					double angleLaser = -Math.toRadians(laser.getAngleTir());
					Vecteur v = new Vecteur (Math.cos(angleLaser), Math.sin(angleLaser)).normalise();
					//n vecteur normal au miroir
					Vecteur n = miroir.getNormal().normalise();	
					//e = -v
					Vecteur e = v.multiplie(-1);
					laser.setAngleTir( Math.toDegrees(Math.atan( (v.additionne(n.multiplie(2*(e.prodScalaire(n)))).getY() / ((v.additionne(n.multiplie(2*(e.prodScalaire(n))))).getX())))));
				}	
			}
		}
	}

	/**
	 * Cette methode methode reoriente l'angle de depart du laser s'il y a une intersection
	 * avec un miroir convexe
	 * @throws Exception
	 */
	private void colisionLaserMiroir() throws Exception{
		for(MiroirConvexe miroir : listeMiroirConvexe ) {
			for(Laser laser : listeLasers) {
				if(intersection(miroir.getAireMiroirConvexe(), laser.getLaserAire())) {
					System.out.println("j'ai une intersection");
					laser.setPosition(new Vecteur (0,0));
					//System.out.println(miroir.getNormalPosition(laser.getPosition()));
					double angleLaser = -Math.toRadians(laser.getAngleTir());
					Vecteur v = new Vecteur (Math.cos(angleLaser), Math.sin(angleLaser)).normalise();
					//n vecteur normal au miroir
					Vecteur n = miroir.getNormalPosition(laser.getPosition()).normalise();	
					//e = -v
					Vecteur e = v.multiplie(-1);
					laser.setAngleTir( Math.toDegrees(Math.atan( (v.additionne(n.multiplie(2*(e.prodScalaire(n)))).getY() / ((v.additionne(n.multiplie(2*(e.prodScalaire(n))))).getX())))));
				}
			}
		}
	}

	public void setMiroirPlan(boolean reponse) {
		miroirPlan = reponse;
		System.out.println(miroirPlan);
	}
}
