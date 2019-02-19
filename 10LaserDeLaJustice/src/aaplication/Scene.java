package aaplication;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import geometrie.Vecteur;
import personnage.Personnage;
import physique.Balle;
import physique.Coeurs;
import physique.Laser;
import pistolet.Pistolet;
import utilite.ModeleAffichage;

public class Scene extends JPanel implements Runnable {

	private Personnage principal;
	private Pistolet pistoletPrincipal;
	private AffineTransform mat;
	private int HAUTEUR=0;
	
	private int tempsDuSleep = 100;
	private double deltaT = 0.05;
	private final double LARGEUR_DU_MONDE = 10; //en metres
	private boolean enCoursAnimation= false;
	private double tempsTotalEcoule = 0;
	

	private double masse = 15; //en kg
	private double diametre = 3;  //em mètres
	private ArrayList<Balle> listeBalles = new ArrayList<Balle>();
	private Vecteur gravity;
	private Balle balle;
	private boolean premiereFois = true;
	private ModeleAffichage modele;

	
	
	public Scene() {
		principal = new Personnage ();
		pistoletPrincipal= new Pistolet();
		gravity = new Vecteur(0,9.8);
		
		
	
		
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		if(premiereFois) {
			modele = new ModeleAffichage(getWidth(),getHeight(),LARGEUR_DU_MONDE);
		mat = modele.getMatMC();
		premiereFois = false;
		}
		
			
		g2d.setColor(Color.yellow);
		principal.dessiner(g2d, mat, HAUTEUR);
		pistoletPrincipal.dessiner(g2d, mat, HAUTEUR, 0);
	
	}
	

	private void calculerUneIterationPhysique() {
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
