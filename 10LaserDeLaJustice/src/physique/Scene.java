package physique;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import geometrie.Vecteur;
import utilite.ModeleAffichage;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class Scene extends JPanel implements Runnable {

	private int tempsDuSleep = 20;
	private double deltaT = 0.05;
	private final double LARGEUR_DU_MONDE = 5; //en metres
	private boolean enCoursAnimation= false;
	private double tempsTotalEcoule = 0;
	

	private double masse = 15; //en kg
	private double diametre = 0.5;  //em mètres
	private ArrayList<Balle> listeBalles = new ArrayList<Balle>();
	Balle balle;
	private boolean premiereFois = true;
	private ModeleAffichage modele;
	private AffineTransform mat;
	
	private Vecteur position = new Vecteur(0.3, 2.5);
	
	private Vecteur vitesse = new Vecteur(1.0 ,0);

	private Vecteur gravity = new Vecteur(0,9.8);




	/**
	 * Create the panel.
	 */
	public Scene() {
		
		/*
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
			
				balle = new Balle(new Vecteur(e.getX()-diametre/2, e.getY()-diametre/2), new Vecteur(3,0),gravity,diametre, masse );
	
				listeBalles.add(balle);
				repaint();
				
			}
		});
		
	
		
		*/
		
		
		
		
	}


	public void paintComponent(Graphics g) {



		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;	
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

		//if(premiereFois) {
			modele = new ModeleAffichage(getWidth(),getHeight(),LARGEUR_DU_MONDE);
		mat = modele.getMatMC();
		premiereFois = false;
		//}


		balle = new Balle(position, vitesse,gravity,diametre, masse );

		balle.dessiner(g2d,mat, getHeight(),getWidth());
		
		/*
		for(Balle balle: listeBalles) {

			g2d.setColor(Color.blue);
			balle.dessiner(g2d,mat, getHeight(),getWidth());

		}
		
		*/
	
		


		

	}//fin paintComponent




	private void calculerUneIterationPhysique() {
		/*
		for(Balle balle : listeBalles) {
			balle.unPasRK4( deltaT, tempsTotalEcoule );
		}
		*/
		balle.unPasRK4( deltaT, tempsTotalEcoule );
		tempsTotalEcoule += deltaT;


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

