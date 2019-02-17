package physique;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.JPanel;


import geometrie.Vecteur;


public class Scene extends JPanel implements Runnable {

		private int tempsDuSleep = 25;
		private double deltaT = 0.008;
		private final double LARGEUR_DU_MONDE = 3; //en metres
		private Vecteur posInitBloc = new Vecteur(0.0, 0.0);//position intiales pour la bloc
		private boolean enCoursAnimation= false;
		private double tempsTotalEcoule = 0;
		private Balle balle;
		private double massePourCetteScene = 4.5; //en kg
		private double diametrePourCetteScene = 5;  //em mètres
		private double posInitX = 34, posInitY = 50;//position intiales pour la balle
		private Vecteur posInitBalle;

		
	
	/**
	 * Create the panel.
	 */
	public Scene() {

		posInitBalle = new Vecteur(posInitX, posInitY);
		balle = new Balle(posInitBalle, new Vecteur(5, 5), diametrePourCetteScene);
		balle.setMasseEnKg(massePourCetteScene);
		
	}

	
	
	
	
	
public void paintComponent(Graphics g) {

	
		
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;	
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		
		if(balle.getPosition().getY()+diametrePourCetteScene >= getHeight())
			balle.getVitesse().setY(balle.getVitesse().getY()*-1);
		balle.dessiner(g2d);
		
		
		
		
		//ModeleAffichage modele = new ModeleAffichage(getWidth(), getHeight(), LARGEUR_DU_MONDE);
	//	AffineTransform matMC = modele.getMatMC();	
		//blocRessort.setLargeurDuMonde(getWidth());
		//blocRessort.dessiner(g2d, matMC);
		
		
		g2d.setStroke( new BasicStroke());

	}//fin paintComponent

	
	
private void calculerUneIterationPhysique() {
	//leverEvenResultatsApresUneImage();
	tempsTotalEcoule += deltaT;
	balle.unPasVerlet( deltaT );
	
	

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

