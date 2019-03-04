package miora;

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
import physique.Balle;
import physique.Laser;
import utilite.ModeleAffichage;

public class SceneMiroirConvexe extends JPanel implements Runnable {
	private int tempsDuSleep = 200;
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

	private double angle;
	private ArrayList<Laser> listeLasers = new ArrayList<Laser>();
	private MiroirConvexe miroir;
	private ArrayList<MiroirConvexe> listeMiroirPlan = new ArrayList<MiroirConvexe>();

	public SceneMiroirConvexe() {
		angle = -90;
		character = new Personnage();

		position = new Vecteur(0.5, 10);

		vitesse = new Vecteur(3 ,0);

		gravity = new Vecteur(0,9.8);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				double posX = e.getX()/modele.getPixelsParUniteX();
				double posY = e.getY()/modele.getPixelsParUniteY();
				miroir = new MiroirConvexe (posX,posY,1.0);
				listeMiroirPlan.add(miroir);
				repaint();
			}
		});
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				character.deplacerLePersoSelonTouche( e );
				shoot(e);
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
		/*try {
			colisionLaserMiroir(listeLasers, listeMiroirPlan);
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		for(MiroirConvexe miroir:listeMiroirPlan) {
			g2d.setColor(Color.gray);
			miroir.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
			g2d.setColor(Color.yellow);
			//g2d.draw(miroir.getAireMiroir());
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

	private boolean intersection(Area aire1, Area aire2) {
		Area aireInter = new Area(aire1);
		aireInter.intersect(aire2);
		if(!aireInter.isEmpty()) {
			return true;
		}
		return false;
	}

	private void colisionLaserMiroir( ArrayList<Laser> listeLasers, ArrayList<MiroirPlan> listeMiroirPlan  ) throws Exception {
		for(Laser laser : listeLasers) {
			for(MiroirPlan miroir : listeMiroirPlan ) {
				if(intersection(miroir.getAireMiroir(), laser.getLaserAire())) {
					Vecteur v = new Vecteur (Math.cos(angleMiroir), Math.sin(angleMiroir)).normalise();
					Vecteur n = miroir.calculNormal().normalise();
					Vecteur e = v.multiplie(-1);
					laser.setAngleTir( Math.toDegrees(Math.atan( (v.additionne(n.multiplie(2*(e.prodScalaire(n)))).getY() / ((v.additionne(n.multiplie(2*(e.prodScalaire(n))))).getX())))));
				}	
			}
		}
	}

}
