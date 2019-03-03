package aaplication;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.Image;
import java.awt.Shape;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import java.util.ArrayList;


import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import javax.swing.JPanel;
import geometrie.Vecteur;
import objets.TrouNoir;
import personnage.Personnage;
import physique.Balle;

import pistolet.Pistolet;

import physique.Coeurs;
import physique.Laser;
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


	private Vecteur positionInit;
	private Vecteur vitesseInit;
	private Rectangle2D.Double fantomePerso;
	private Shape fantomeTransfo;
	private Pistolet pistoletPrincipal;
	private Laser laser;
	private static final long serialVersionUID = 1L;
	private int tempsDuSleep = 30;
	private double deltaT = 0.08;
	private  double LARGEUR_DU_MONDE = 30; //en metres
	private  double HAUTEUR_DU_MONDE;
	private boolean enCoursAnimation= false;
	private double tempsTotalEcoule = 0;
	private double diametre = 2;  //em mètres
	private ArrayList<Balle> listeBalles = new ArrayList<Balle>();
	private Balle balle;
	private boolean premiereFois = true;
	private ModeleAffichage modele;
	private AffineTransform mat;
	private Vecteur vitesse;
	private Personnage character;
	private double angle;
	private ArrayList<Laser> listeLasers = new ArrayList<Laser>();
	private ArrayList<TrouNoir> listeTrou = new ArrayList<TrouNoir>();
	private TrouNoir trou;
	private int toucheGauche =37;
	private int toucheDroite=39;


	
	
	//Jeremy Thai
	

	
	
	public Scene() {
		lireFond();
		lectureFichierOption();
		angle = -90;
		principal = new Personnage (toucheGauche, toucheDroite);
		pistoletPrincipal= new Pistolet();
		positionInit = new Vecteur(12, 10);
		vitesseInit = new Vecteur(2.0 ,0);
		
		
		addMouseListener(new MouseAdapter() {
			@Override
			//Jeremy Thai
			public void mousePressed(MouseEvent e) {

				double eXR = e.getX()/modele.getPixelsParUniteX();
				double eYR = e.getY()/modele.getPixelsParUniteY();
				balle = new Balle(new Vecteur(eXR-diametre/2, eYR-diametre/2),vitesse, "LARGE" );
				listeBalles.add(balle);

				trou= new TrouNoir(new Vecteur(eXR,eYR));
				listeTrou.add(trou);

				repaint();
			}
		});
		
		
		
		addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				character.deplacerLePersoSelonTouche( e );
				shootEtAddLaser(e);
				repaint();
			}
			@Override
			//Jeremy Thai
			public void keyReleased(KeyEvent e) {
				character.relacheTouche(e);
				repaint();
			}
		});


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
		
	
		g2d.drawImage(fond, 0, 0, (int) modele.getLargPixels(),(int) modele.getHautPixels(), null);


		for(Laser laser : listeLasers) { 
			if(laser.getLigneFinY() <= 0 )
				listeLasers.remove(laser);
			g2d.setColor((new Color(255,255,200)));
			laser.dessiner(g2d, mat, 0, 0);
			laser.move();
		}

		checkCollisionBalleLaserPersonnage( listeBalles,  listeLasers,character);
		checkCollisionTrouLaserPersonnage( listeLasers );

		for(Balle balle: listeBalles) {

			balle.dessiner(g2d,mat,HAUTEUR_DU_MONDE,LARGEUR_DU_MONDE);
		}


		for(TrouNoir trou: listeTrou) {
			trou.dessiner(g2d,mat,HAUTEUR_DU_MONDE,LARGEUR_DU_MONDE);
		}

		character.dessiner(g2d, mat, LARGEUR_DU_MONDE, HAUTEUR_DU_MONDE);


		
		
		creerLePersonnagePrincipal(g2d, mat);
		
		

	}

	/**
	 * Cette methode permet de dessiner le personnage principal, ainsi qu'un carre autour de lui
	 * @param g2d : le composant graphique
	 * @param mat : la matrice de transformation
	 */
	//Miora
	private void creerLePersonnagePrincipal(Graphics2D g2d, AffineTransform mat) {
		principal.dessiner(g2d, mat,modele.getLargUnitesReelles(),modele.getHautUnitesReelles());

		fantomePerso = new Rectangle2D.Double (principal.getPositionX(), modele.getHautUnitesReelles() - principal.getLONGUEUR_PERSO(), 
				principal.getLARGEUR_PERSO(), principal.getLONGUEUR_PERSO());
		g2d.draw(mat.createTransformedShape(fantomePerso));

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
	
	//Jeremy Thai
	private void calculerUneIterationPhysique() {

		for(Balle balle: listeBalles) {
			balle.unPasRK4( deltaT, tempsTotalEcoule);
		}

		tempsTotalEcoule += deltaT;;
	}

	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (enCoursAnimation) {
			character.move();
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

	public void setAngle(double angle) {
		laser.setAngleTir(angle);
		principal.getPositionX();
		
	}
	/**
	 * Cette methode permet de lire le fichier option de la classe option modifie avant le debut de la partie
	 */
	//Miora
	private void lectureFichierOption() {
		final String NOM_FICHIER_OPTION = "DonneeOption.d3t";
		DataInputStream fluxEntree = null;
		double acceleration= 9.8;
		int niveau=0;
		File fichierDeTravail = new File( NOM_FICHIER_OPTION );
		
		try {
			fluxEntree =  new DataInputStream(new BufferedInputStream(new FileInputStream(fichierDeTravail)));
			niveau = fluxEntree.readInt();
			acceleration = fluxEntree.readDouble();
			toucheGauche= fluxEntree.readInt();
			toucheDroite= fluxEntree.readInt();
		} // fin try

		catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Fichier  " + fichierDeTravail.getAbsolutePath() + "  introuvable!");
			System.exit(0);
		}
		
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Erreur rencontree lors de la lecture");
			e.printStackTrace();
			System.exit(0);
		}
		
		finally {
			//on exécutera toujours ceci, erreur ou pas
		  	try { 
		  		fluxEntree.close();  
		  	}
		    catch (IOException e) { 
		    	JOptionPane.showMessageDialog(null,"Erreur rencontrée lors de la fermeture!"); 
		    }
		}//fin finally
	}
	
	//Jeremy Thai
	private void checkCollisionBalleLaserPersonnage(ArrayList<Balle> listeBalles, ArrayList<Laser> listeLasers, Personnage character ) {

		ArrayList<Balle> listeBalleTouche = new ArrayList<Balle>();
		for(Laser laser : listeLasers) {
			for(Balle balle : listeBalles ) {
				if(intersection(balle.getAireBalle(), laser.getLaserAire())) {
					listeLasers.remove(laser);   
					listeBalleTouche.add(balle);
					balle.shrink(listeBalles);
				}	

			}
		}
	}
	
	private void checkCollisionTrouLaserPersonnage( ArrayList<Laser> listeLasers ) {


		for(Laser laser : listeLasers) {
			for(TrouNoir trou : listeTrou ) {
				if(intersection(trou.getAireTrou(), laser.getLaserAire())) {
					listeLasers.remove(laser);   

				}	
			}
		}


	}
	
	//Jeremy Thai
	private void shootEtAddLaser(KeyEvent e) {
		int code = e.getKeyCode();
		if(code == KeyEvent.VK_SPACE) {
			character.shoot(code);
			if(listeLasers.size() <1) { // Pour que 1 laser soit tirer  a la fois 
				listeLasers.add(
						new Laser(new Vecteur(
								character.getPositionX()+character.getLARGEUR_PERSO()/2,LARGEUR_DU_MONDE), angle, new Vecteur(0,0.5)));
			}
		}
	}
	
	//Jeremy Thai
	private boolean intersection(Area aire1, Area aire2) {
		Area aireInter = new Area(aire1);
		aireInter.intersect(aire2);
		if(!aireInter.isEmpty()) {
			return true;
		}
		return false;
	}
	
	
	

}
