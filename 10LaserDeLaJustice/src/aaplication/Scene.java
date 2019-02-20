package aaplication;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import javax.swing.JPanel;
import geometrie.Vecteur;
import personnage.Personnage;
import physique.Balle;
import physique.Coeurs;
//import physique.Laser;
//import pistolet.Pistolet;
import utilite.ModeleAffichage;

public class Scene extends JPanel implements Runnable {

	//private Personnage principal;
	//private Pistolet pistoletPrincipal;
	private AffineTransform mat;
	private int HAUTEUR=0;
	private int tempsDuSleep = 25;
	private double deltaT = 0.10;
	private final double LARGEUR_DU_MONDE = 50; //en metres
	private boolean enCoursAnimation = false;
	private double tempsTotalEcoule = 0;
	private double masse = 15; //en kg
	private double diametre = 5;  //em mètres
	private ArrayList<Balle> listeBalles = new ArrayList<Balle>();
	private boolean premiereFois = true;
	private ModeleAffichage modele;
	private  double HAUTEUR_DU_MONDE;
	private Balle balle1;
	private Vecteur positionInit;
	private Vecteur vitesseInit;
	private Vecteur gravity ;



	public Scene() {
		
		
		
		//principal = new Personnage ();
		//pistoletPrincipal= new Pistolet();
		

		positionInit = new Vecteur(12, 10);

		vitesseInit = new Vecteur(2.0 ,0);
		gravity = new Vecteur(0,9.8);

		balle1 = new Balle(positionInit, vitesseInit,gravity,diametre, masse );



	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		if(premiereFois) {
			modele = new ModeleAffichage(getWidth(),getHeight(),LARGEUR_DU_MONDE);
			mat = modele.getMatMC();
			HAUTEUR_DU_MONDE = modele.getHautUnitesReelles() ;
			premiereFois = false;
		}


		g2d.setColor(Color.red);
		//principal.dessiner(g2d, mat, HAUTEUR);
		//pistoletPrincipal.dessiner(g2d, mat, HAUTEUR, 0);

		balle1.dessiner(g2d, mat, (int) HAUTEUR_DU_MONDE,(int) LARGEUR_DU_MONDE);

	}


	private void calculerUneIterationPhysique() {

		balle1.unPasRK4( deltaT, tempsTotalEcoule );
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
