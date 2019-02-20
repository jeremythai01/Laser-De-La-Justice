package aaplication;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.Image;
import java.awt.Shape;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.net.URL;

import java.util.ArrayList;


import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import javax.swing.JPanel;
import geometrie.Vecteur;
import personnage.Personnage;
import physique.Balle;

import pistolet.Pistolet;

import physique.Coeurs;
//import physique.Laser;
//import pistolet.Pistolet;
import utilite.ModeleAffichage;

/**
 * Cette classe contient la scene d'animation du jeu.
 * @author Miora
 *
 */
public class Scene extends JPanel implements Runnable {


	private Image fond = null;

	private Personnage principal;
//	private Pistolet pistoletPrincipal;

	private AffineTransform mat;
	private int HAUTEUR=0;

	private int tempsDuSleep = 100;
	private double deltaT = 0.05;
	private final double LARGEUR_DU_MONDE = 10;
	private boolean enCoursAnimation= false;

	private double masse = 15; //en kg
	private double diametre = 5;  //em mètres
	private ArrayList<Balle> listeBalles = new ArrayList<Balle>();
	private boolean premiereFois = true;
	private ModeleAffichage modele;
	private double HAUTEUR_SCENE;
	private  double HAUTEUR_DU_MONDE;
	private Balle balle1;
	private Vecteur positionInit;
	private Vecteur vitesseInit;
	private Vecteur gravity ;
	private Rectangle2D.Double fantomePerso;
	private Shape fantomeTransfo;



	public Scene() {
		lireFond();
		principal = new Personnage ();
		//pistoletPrincipal= new Pistolet();
		

		positionInit = new Vecteur(12, 10);

		vitesseInit = new Vecteur(2.0 ,0);
		gravity = new Vecteur(0,9.8);
		principal = new Personnage (); 
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				deplacerLePersoSelonTouche( e );
				repaint();
			}
		});

		balle1 = new Balle(positionInit, vitesseInit,gravity,diametre, masse );
	}
	/*addMouseListener(new MouseAdapter() { // pour tester les balles 
			@Override
			public void mousePressed(MouseEvent e) {
				balle = new Balle(new Vecteur(e.getX(), e.getY()), new Vecteur(3,0),gravity,diametre, masse );
				listeBalles.add(balle);
				repaint();
			}
		});
	 */

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		if(premiereFois) {
			modele = new ModeleAffichage(getWidth(),getHeight(),LARGEUR_DU_MONDE);
			mat = modele.getMatMC();
			HAUTEUR_DU_MONDE = modele.getHautUnitesReelles() ;
			premiereFois = false;
		}
			mat = modele.getMatMC();
			premiereFois = false;
		
		g2d.drawImage(fond, 0, 0, (int) modele.getLargPixels(),(int) modele.getHautPixels(), null);

		creerLePersonnagePrincipal(g2d, mat);

	}

	/**
	 * Cette methode permet de dessiner le personnage principal, ainsi qu'un carre autour de lui
	 * @param g2d : le composant graphique
	 * @param mat : la matrice de transformation
	 */
	//Miora
	private void creerLePersonnagePrincipal(Graphics2D g2d, AffineTransform mat) {
		principal.setDimensionScene(modele.getLargUnitesReelles(), modele.getHautUnitesReelles());
		principal.dessiner(g2d, mat);

		fantomePerso = new Rectangle2D.Double (principal.getPositionX(), modele.getHautUnitesReelles() - principal.getLONGUEUR_PERSO(), 
				principal.getLARGEUR_PERSO(), principal.getLONGUEUR_PERSO());
		g2d.draw(mat.createTransformedShape(fantomePerso));

	}
	/*
		for(Balle balle: listeBalles) {
=======
>>>>>>> branch 'master' of https://gitlab.com/MacVac/10laserdelajustice

		g2d.setColor(Color.red);
		//principal.dessiner(g2d, mat, HAUTEUR);
		//pistoletPrincipal.dessiner(g2d, mat, HAUTEUR, 0);

<<<<<<< HEAD
		}
=======
		balle1.dessiner(g2d, mat, (int) HAUTEUR_DU_MONDE,(int) LARGEUR_DU_MONDE);
>>>>>>> branch 'master' of https://gitlab.com/MacVac/10laserdelajustice

	}


	private void calculerUneIterationPhysique() {

		balle1.unPasRK4( deltaT, tempsTotalEcoule );
		tempsTotalEcoule += deltaT;


	}
*/
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
		//	calculerUneIterationPhysique();
			repaint();
			try {
				Thread.sleep(tempsDuSleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}//fin while
		System.out.println("Le thread est mort...");
	}
	
	
	/**
	 * Methode permettant de mettre un fond a la scene
	 */ // Miora
	private void lireFond() {
		URL fich = getClass().getClassLoader().getResource("space.jpg");
		if (fich == null) {
			JOptionPane.showMessageDialog(null, "Fichier space.jpg introuvable!");
		} else {
			try {
				fond = ImageIO.read(fich);
			} catch (IOException e) {
				System.out.println("Erreur de lecture du fichier d'image");
			}
		}
	}

	private void deplacerLePersoSelonTouche(KeyEvent e) {
		int code = e.getKeyCode();
		switch (code) {
		case KeyEvent.VK_LEFT:
			principal.setPositionX(principal.getPositionX()-10);
			//	x = Math.max(x, 0);
			break;

		case KeyEvent.VK_RIGHT:
			principal.setPositionX(principal.getPositionX()+10);
			//x = Math.min(x, getWidth() - DIAMETRE);
			break;
		}// fin switch
	}//fin methode
}
