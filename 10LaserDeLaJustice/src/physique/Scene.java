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
		private double diametrePourCetteScene = 0.18;  //em mètres
		private double posInitX = 0.2, posInitY = 0.1;//position intiales pour la balle
		private Vecteur posInitBalle;

		
	
	/**
	 * Create the panel.
	 */
	public Scene() {

		posInitBalle = new Vecteur(posInitX, posInitY);
		balle = new Balle(posInitBalle, new Vecteur(0, 0), diametrePourCetteScene);
		balle.setMasseEnKg(massePourCetteScene);
		
	}

	
	
	
	
	
public void paintComponent(Graphics g) {

		
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;	
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		
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
	
	
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
