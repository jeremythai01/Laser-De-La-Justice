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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class Scene extends JPanel implements Runnable {

	private int tempsDuSleep = 25;
	private double deltaT = 0.3;
	private final double LARGEUR_DU_MONDE = 3; //en metres
	private boolean enCoursAnimation= false;
	private double tempsTotalEcoule = 0;
	

	private double masse = 1; //en kg
	private double diametre = 50;  //em mètres
	private ArrayList<Balle> listeBalles = new ArrayList<Balle>();
	private Vecteur gravity;
	Balle balle;






	/**
	 * Create the panel.
	 */
	public Scene() {
		gravity = new Vecteur(0,9.8);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
			
				balle = new Balle(new Vecteur(e.getX()-diametre/2, e.getY()-diametre/2), new Vecteur(3,0),gravity,diametre, masse );
	
				listeBalles.add(balle);
				repaint();
				
			}
		});
	}






	public void paintComponent(Graphics g) {



		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;	
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

		


		Line2D.Double ligne = new Line2D.Double(30,200,getWidth(),200);

		g2d.setColor(Color.RED);
		g2d.draw(ligne);

		for(Balle balle: listeBalles) {

			balle.setHeight(getHeight());
			balle.setWidth(getWidth());	
			balle.checkCollisions();
			g2d.setColor(Color.blue);
			balle.dessiner(g2d);

		}


		//ModeleAffichage modele = new ModeleAffichage(getWidth(), getHeight(), LARGEUR_DU_MONDE);
		//	AffineTransform matMC = modele.getMatMC();	
		//blocRessort.setLargeurDuMonde(getWidth());
		//blocRessort.dessiner(g2d, matMC);


		g2d.setStroke( new BasicStroke());

	}//fin paintComponent




	private void calculerUneIterationPhysique() {
		//leverEvenResultatsApresUneImage();
		for(Balle balle : listeBalles) {
			balle.unPasRK4( deltaT, tempsTotalEcoule );
		}
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

