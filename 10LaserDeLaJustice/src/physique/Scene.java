package physique;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.JPanel;

import geometrie.Vecteur;
import utilite.ModeleAffichage;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;




public class Scene extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	private int tempsDuSleep = 25;
	private double deltaT = 0.05;
	private  double LARGEUR_DU_MONDE = 50; //en metres
	private  double HAUTEUR_DU_MONDE;
	private boolean enCoursAnimation= false;
	private double tempsTotalEcoule = 0;
	private double masse = 15; //en kg
	private double diametre = 1;  //em mètres
	private ArrayList<Balle> listeBalles = new ArrayList<Balle>();
	private Balle balle1;
	private Balle balle;
	private boolean premiereFois = true;
	private ModeleAffichage modele;
	private AffineTransform mat;
	
	private Vecteur position;
	
	private Vecteur vitesse;

	private Vecteur gravity ;
	




	/**
	 * Create the panel.
	 */
	public Scene() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				balle = new Balle(new Vecteur(e.getX()-diametre/2, e.getY()-diametre/2), new Vecteur(3,0),gravity,diametre, masse );
				
				listeBalles.add(balle);
				repaint();
			}
		});
		
		
		
		 position = new Vecteur(0.3, 10);
		
		 vitesse = new Vecteur(2.0 ,0);

		 gravity = new Vecteur(0,1);
		
		 balle1 = new Balle(position, vitesse,gravity,diametre, masse );
		
		

		
	
		
		
		
		
		
		
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

		System.out.println("hauteur est de "+getHeight());
		balle1.dessiner(g2d,mat, (int)HAUTEUR_DU_MONDE, (int)LARGEUR_DU_MONDE);
		
		


		

	}//fin paintComponent




	private void calculerUneIterationPhysique() {
	
		
		
		balle1.unPasRK4( deltaT, tempsTotalEcoule);
		tempsTotalEcoule += deltaT;

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

	
	
	
	
	
}

