package objets;




import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.util.ArrayList;

import javax.swing.JPanel;

import geometrie.Vecteur;
import miroir.MiroirConcave;
import physique.Balle;
import utilite.ModeleAffichage;

import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;




public class SceneTestObjets extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	private int tempsDuSleep = 25;
	private double deltaT = 0.07;
	private  double LARGEUR_DU_MONDE = 50; //en metres
	private  double HAUTEUR_DU_MONDE;
	private boolean enCoursAnimation= false;
	private double tempsTotalEcoule = 0;
	private double masse = 15; //en kg
	private double diametre = 5;  //em mètres

	private boolean premiereFois = true;
	private ModeleAffichage modele;
	private AffineTransform mat;

	private Vecteur position;

	private Vecteur vitesse;

	private Vecteur gravity ;
	
	private BlocDEau bloc;
	private ArrayList<BlocDEau> listeBloc = new ArrayList<BlocDEau>();
	private TrouNoir trou;
	private ArrayList<TrouNoir> listeTrou = new ArrayList<TrouNoir>();


	/**
	 * Create the panel.
	 */
	public SceneTestObjets() {


		position = new Vecteur(0.3, 10);

		vitesse = new Vecteur(3 ,0);

		gravity = new Vecteur(0,9.8);



		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

				double eXR = e.getX()/modele.getPixelsParUniteX();
				double eYR = e.getY()/modele.getPixelsParUniteY();
				bloc= new BlocDEau(new Vecteur(eXR,eYR));
				listeBloc.add(bloc);
				
				trou= new TrouNoir(new Vecteur(eXR,eYR));
				listeTrou.add(trou);
				
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

		
		
		for(BlocDEau bloc: listeBloc) {
			

			bloc.dessiner(g2d,mat,HAUTEUR_DU_MONDE,LARGEUR_DU_MONDE);
		}
		
		/*for(TrouNoir trou: listeTrou) {
			

			trou.dessiner(g2d,mat,HAUTEUR_DU_MONDE,LARGEUR_DU_MONDE);
		}*/

	}//fin paintComponent




	private void calculerUneIterationPhysique() {


		/*balle1.unPasRK4( deltaT, tempsTotalEcoule);
		laser.unPasRK4(deltaT, tempsTotalEcoule);

		for(Balle balle: listeBalles) {
			balle.unPasRK4( deltaT, tempsTotalEcoule);
		}*/

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






}