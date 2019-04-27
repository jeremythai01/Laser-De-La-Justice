package objets;


import java.awt.BasicStroke;
import java.awt.Color;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import javax.swing.JPanel;

import geometrie.Vecteur;
import physique.Balle;
import physique.Coeurs;
import physique.Laser;
import physique.MoteurPhysique;
import utilite.ModeleAffichage;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;



/**
 * 
 * @author Arnaud
 *
 */
public class SceneTestPls extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	private int tempsDuSleep = 25;
	//private double deltaT = 0.06;
	private double deltaT = 0.01;
	private  double LARGEUR_DU_MONDE = 50; //en metres
	private  double HAUTEUR_DU_MONDE;
	private boolean enCoursAnimation= false;
	private double tempsTotalEcoule = 0;
	private double masse = 15; //en kg
	private double diametre = 2;  //em mtres
	private ArrayList<Balle> listeBalles = new ArrayList<Balle>();
	private Balle balle1;
	private Balle balle;
	private boolean premiereFois = true;
	private ModeleAffichage modele;
	private AffineTransform mat;

	private Vecteur position;

	private Vecteur vitesse;

	private Vecteur gravity ;

	private Personnage character;

	private double angle;
	private ArrayList<Laser> listeLasers = new ArrayList<Laser>();


	private BlocDEau bloc;
	private ArrayList<BlocDEau> listeBloc = new ArrayList<BlocDEau>();
	private TrouNoir trou;
	private ArrayList<TrouNoir> listeTrou = new ArrayList<TrouNoir>();
	private Champ champ;
	private ArrayList<Champ> listeChamps = new ArrayList<Champ>();
	private Coeurs coeur;
	private int nombreVies=3;

	private Echelle echelle;

	
	private Ordinateur ordi;
	private OrdinateurNiveau2 ordi2;
	private OrdinateurNiveau3 ordi3;
	private boolean enMouvement=false;
	private boolean triche=false;
	private Vecteur gravite = new Vecteur(0, 9.8);
	private double chargeBalle=-0.00006;
	private double chargeChamp=0.004;
	/**
	 * Create the panel.
	 */
	public SceneTestPls() {

		//ordi= new Ordinateur(1, new Vecteur(20,44));
		//ordi2= new OrdinateurNiveau2(new Vecteur(30,44));
		ordi3= new OrdinateurNiveau3(new Vecteur(40,44));
		ordi3.ajouterListesObstacles(listeBalles);
		ordi3.savoirTempsSleep(tempsDuSleep);

		angle = 30;
		character = new Personnage();

		position = new Vecteur(0.3, 10);

		vitesse = new Vecteur(3 ,0);

		gravity = new Vecteur(0,9.8);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

				double eXR = e.getX()/modele.getPixelsParUniteX();
				double eYR = e.getY()/modele.getPixelsParUniteY();
				balle = new Balle(new Vecteur(0, eYR-diametre/2),vitesse, "LARGE", gravite);
				listeBalles.add(balle);
				//bloc= new BlocDEau(new Vecteur(eXR,eYR),1.33);
				//listeBloc.add(bloc);
				champ=new Champ(new Vecteur(eXR-12.5, eYR-12.5), chargeChamp);
				listeChamps.add(champ);

				//trou= new TrouNoir(new Vecteur(eXR,eYR));
				//listeTrou.add(trou);


				if(character.airePersonnage().contains(eXR, eYR)) {
					//System.out.println("je suis dans le personnage");
					addMouseMotionListener(new MouseMotionAdapter() {
						@Override
						public void mouseDragged(MouseEvent f) {
							double fX=f.getX()/modele.getPixelsParUniteX();
							//double fY= f.getY()/modele.getPixelsParUniteY();
							if(fX>eXR&&angle>5) {
								System.out.println("je vais vers la droite");
								double distance=fX-eXR;
								angle=angle-(int)distance;
								System.out.println("angle33 "+ angle);
							}
							if(fX<eXR&&angle<175) {
								double distance=eXR-fX;
								angle=angle+(int)distance;
								System.out.println("angle "+ angle);
							}
							enMouvement=true;
							repaint();
						}
					});
				}





				repaint();
			}
		});


		addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				character.deplacerLePersoSelonTouche( e );
				changerAngle(e);
				modeTriche();
				shoot(e);
				//tirer();
				repaint();
			}
		});




		balle1 = new Balle(position, vitesse, "LARGE", new Vecteur(0,9.8));
		/*
		laser = new Laser(
				new Vecteur(
			character.getPositionX()+character.getLARGEUR_PERSO(),LARGEUR_DU_MONDE), angle, new Vecteur(2,2));
		 */
	}

	private void modeTriche() {
		triche=true;
	}
	
	private void changerAngle(KeyEvent e) {
		if(e.getKeyCode()==38 && angle<180) {
			angle=angle+5;
		}
		if(e.getKeyCode()==40 && angle>0) {
			angle=angle-5;
		}
		enMouvement=true;
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
		
		for(TrouNoir trou: listeTrou) {
			trou.dessiner(g2d,mat,HAUTEUR_DU_MONDE,LARGEUR_DU_MONDE);
		}
		for(Champ champ: listeChamps) {
			champ.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		}

		try {


			for(Laser laser : listeLasers) { 

				if(laser.getPositionHaut().getY() <= 0 ) {
					listeLasers.remove(laser);
				}
				g2d.setStroke( new BasicStroke(3));
				laser.dessiner(g2d, mat, 0, 0);
			}

		}catch(ConcurrentModificationException e) {
			System.out.println("le laser nexiste pas");
		}
		checkCollisionBalleLaserPersonnage( listeBalles,  listeLasers,character);
		checkCollisionTrouLaserPersonnage( listeLasers );
		checkCollisionBlocLaserPersonnage( listeLasers );
		checkCollisionChampBalle();
		detectionCollisionMurBalle();
		for(BlocDEau bloc:listeBloc) {
			g2d.setColor(Color.blue);
			bloc.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		}

		for (int i = 0; i < listeBalles.size(); i++) {
			for (int j = i+1; j < listeBalles.size(); j++) {
				Balle balle1 = listeBalles.get(i);
				Balle balle2 = listeBalles.get(j);
				MoteurPhysique.detectionCollisionBalles(balle1, balle2);
			}
		}


		for(Balle balle: listeBalles) {

			balle.dessiner(g2d,mat,HAUTEUR_DU_MONDE,LARGEUR_DU_MONDE);
		}

		
	/*for(TrouNoir trou: listeTrou) {
		trou.dessiner(g2d,mat,HAUTEUR_DU_MONDE,LARGEUR_DU_MONDE);
	}*/
		 
		coeur= new Coeurs(nombreVies);
		character.dessiner(g2d, mat, LARGEUR_DU_MONDE, HAUTEUR_DU_MONDE);

		coeur.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		coeur.setCombien(nombreVies-1);
		coeur.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		echelle = new Echelle(LARGEUR_DU_MONDE, 10,10);
		echelle.savoirModele(getWidth(), getHeight(), LARGEUR_DU_MONDE);
		echelle.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		
		g2d.setColor(Color.yellow);
		//ordi.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		//ordi2.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);
		ordi3.dessiner(g2d, mat, HAUTEUR_DU_MONDE, LARGEUR_DU_MONDE);

		tracerVecteurGraphique(g2d);
		/*if(enMouvement) {
			//dessinerTracerAngle(g2d);
			g2d.setColor(Color.RED);
			Path2D.Double trace= new Path2D.Double();
			trace.moveTo(character.getPositionX()+character.getLARGEUR_PERSO()/2,HAUTEUR_DU_MONDE-character.getLONGUEUR_PERSO());
			trace.lineTo(character.getPositionX()+character.getLARGEUR_PERSO()/2+2*Math.cos(Math.toRadians(angle)), HAUTEUR_DU_MONDE-character.getLONGUEUR_PERSO()-2*Math.sin(Math.toRadians(angle)));
			g2d.draw(mat.createTransformedShape(trace));
		}*/

	}//fin paintComponent


	/*private void dessinerTracerAngle(Graphics2D g) {
		g.draw(arg0);
	}*/

	private void tracerVecteurGraphique(Graphics2D g) {
		if(enMouvement) {
			g.setColor(Color.red);
			Path2D.Double trace= new Path2D.Double();
			trace.moveTo(character.getPositionX()+character.getLARGEUR_PERSO()/2,HAUTEUR_DU_MONDE-character.getLONGUEUR_PERSO());
			trace.lineTo(character.getPositionX()+character.getLARGEUR_PERSO()/2+2*Math.cos(Math.toRadians(angle)), HAUTEUR_DU_MONDE-character.getLONGUEUR_PERSO()-2*Math.sin(Math.toRadians(angle)));
			g.draw(mat.createTransformedShape(trace));
		}
	}

	
	private void calculerUneIterationPhysique() {


		for(Balle balle: listeBalles) {
			balle.unPasVerlet(deltaT);
		}

		for(Laser laser : listeLasers) {
			laser.move()/*laser.unPasEuler(deltaT)*/;
		}


		tempsTotalEcoule += deltaT;
		//ordi.bouge();
		//ordi2.bouge();
		ordi3.bouge();
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


	private int compteur=0;
	private double qtRotation=0;
	@Override
	public void run() {

		while (enCoursAnimation) {	
			compteur++;
			calculerUneIterationPhysique();
			qtRotation=qtRotation+0.2;
			for(TrouNoir trou : listeTrou) {
				trou.savoirQuantiteRotation(qtRotation);
			}
			//trou.savoirQuantiteRotation(qtRotation);
			//System.out.println("roa"+ qtRotation);
		//	ordi3.verifierCollisionBalleEtLaserSimulation();
			if(compteur==60) {
				tirer();
				compteur=0;
			}
			repaint();
			try {
				Thread.sleep(tempsDuSleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}//fin while
		System.out.println("Le thread est mort...");
	}


	private void shoot(KeyEvent e) {
		int code = e.getKeyCode();
		if(code == KeyEvent.VK_SPACE) {
			//if(listeLasers.size() <1) { // Pour que 1 laser soit tirer  a la fois 
			listeLasers.add(
					new Laser(new Vecteur(
							character.getPositionX()+character.getLARGEUR_PERSO()/2,HAUTEUR_DU_MONDE-character.getLONGUEUR_PERSO()), angle, new Vecteur(0,0.3)));
			//}
		}
	}


	private void checkCollisionBalleLaserPersonnage(ArrayList<Balle> listeBalles, ArrayList<Laser> listeLasers, Personnage character ) {

		ArrayList<Balle> listeBalleTouche = new ArrayList<Balle>();
		try {for(Laser laser : listeLasers) {
			
				for(Balle balle : listeBalles ) {

					if(intersection(balle.getAire(), laser.getAire())) {

						//if(balle.getAireBalle().intersects(laser.getLine())) {

						listeLasers.remove(laser);   
						listeBalleTouche.add(balle);
						balle.shrink(listeBalles, gravite);
						if(triche=false) {
						coeur.setCombien(nombreVies-1);
						nombreVies-=1;}
					}	

				}
			
		}} catch(ConcurrentModificationException e)  {}
	}
	/*
		for(Balle balle : listeBalleTouche) {
			balle.shrink(listeBalles);
		}


	 */



	//}
	private boolean intersection(Area aire1, Area aire2) {
		Area aireInter = new Area(aire1);
		aireInter.intersect(aire2);
		if(!aireInter.isEmpty()) {
			return true;
		}
		return false;
	}

	private void checkCollisionTrouLaserPersonnage( ArrayList<Laser> listeLasers ) {


		for(Laser laser : listeLasers) {
			for(TrouNoir trou : listeTrou ) {
				//if(trou.getAireTrou().intersects(laser.getLine())) {

				if(intersection(trou.getAireTrou(), laser.getAire())) {


					listeLasers.remove(laser);   

				}	
				if(intersection(trou.getAireGrandTrou(), laser.getAire())) {
					Vecteur distance=laser.getPositionHaut().soustrait(trou.getPosition());
					System.out.println("distance entre trou et laser" + distance);
					laser.setAngleTir(laser.getAngleTir()+distance.getX());
					}
			}
		}
		/*
		for(Balle balle : listeBalleTouche) {
			balle.shrink(listeBalles);
		}


		 */

		//bloc.refraction(v, N, n1, n2);

	}
	
	private void checkCollisionChampBalle() {
		
		for(Balle balle : listeBalles) {
			for(Champ champ: listeChamps ) {
				

				if(intersection(champ.getAireChamp(), balle.getAire())) {
						balle.setAccel((champ.forceElectrique(balle.getPosition(), champ.getPosition(),chargeBalle).multiplie(1.0/15.0)));
						System.out.println("accel "+(champ.forceElectrique(balle.getPosition(), champ.getPosition(),chargeBalle).multiplie(1.0/15.0)));
						//System.out.println("la vitess de la balle "+balle.getVitesse());
						System.out.println("Vecteur de force "+champ.forceElectrique(balle.getPosition(), champ.getPosition(),chargeBalle) );
						//System.out.println("force "+ champ.forceElectrique(balle.getPosition(), champ.getPosition(),3));

				}	
				
			}
		}
		
		
	}
	
	
	private void checkCollisionBlocLaserPersonnage(ArrayList<Laser> listeLasers) {
		//for(Laser laser : listeLasers) {
		for (int i = 0; i < listeLasers.size(); i++) {
			//for(BlocDEau bloc : listeBloc) {
			for (int j = 0; j < listeBloc.size(); j++) {
				//System.out.println("valeur de la position en y du laser"+ listeLasers.get(i).getPositionHaut().getY());
				//System.out.println("valeur de la position en y du bloc"+ listeBloc.get(j).getPosition().getY()+listeBloc.get(j).getHauteur());
				//if(intersection(listeBloc.get(j).getAireBloc(), listeLasers.get(i).getAire())) {
				if(listeLasers.get(i).getPositionHaut().getX()>=listeBloc.get(j).getPosition().getX()&&
						listeLasers.get(i).getPositionHaut().getX()<=listeBloc.get(j).getPosition().getX()+listeBloc.get(j).getLARGEUR()
					&&listeLasers.get(i).getPositionHaut().getY()<=listeBloc.get(j).getPosition().getY()+listeBloc.get(j).getHauteur()+0.1&&
					listeLasers.get(i).getPositionHaut().getY()>=listeBloc.get(j).getPosition().getY()+listeBloc.get(j).getHauteur()-0.1) {
					BlocDEau bloc = listeBloc.get(j);
					Laser laser = listeLasers.get(i);
					System.out.println("je suis ici");
					
					//if(bloc.isPremiereCollision()) {

					//laser.setAngleTir(Math.atan(bloc.refraction(laser.getVitesse(), bloc.calculNormal(laser,bloc), 1.33, 1).getY()/bloc.refraction(laser.getVitesse(), bloc.calculNormal(laser,bloc), 1.33, 1).getX()));

					try {
						System.out.println("le veiel angle est de "+laser.getAngleTir());
						Vecteur ref= bloc.refraction(laser.getVitesse().multiplie(-1).normalise(), bloc.getNormal());
						laser.setAngleTir(Math.toDegrees(Math.atan(ref.getY()/ref.getX())));
						j=listeBloc.size();
						//laser.setAngleTir(30);
						System.out.println("le nouvel angle est de "+laser.getAngleTir());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
					//System.out.println("nouvel angle:" + Math.toDegrees(laser.getAngleTir()));
					//	laser.updaterAngleVitesse(Math.atan(bloc.refraction(laser.getVitesse(), bloc.calculNormal(laser,bloc), 1, 1.33).getY()/bloc.refraction(laser.getVitesse(), bloc.calculNormal(laser,bloc), 1.33, 1).getX()));

					//laser.setAngleTir(90);
					//laser.updaterAngleVitesse((laser.getAngleTir()));
					//System.out.println("nouvel angle:" + Math.toDegrees(laser.getAngleTir()));
					//System.out.println("nouvelle vitesse laser: "+ laser.getVitesse());

					//laser.setAngleTir(30);
					//laser.updaterAngleVitesse((laser.getAngleTir()));
					//System.out.println("laser:" + laser.getVitesse().multiplie(-1));
					//System.out.println("nouvel normal:" + bloc.getNormal());
					//	System.out.println();
					//	System.out.println("valeur bloc: "+ ref);

					//repaint();
						//bloc.setPremiereCollision(false);
					//}
					
				//}
			}
		}
	}

	private void tirer() {

		ordi3.ajouterListesObstacles(listeBalles);
		//	listeLasers.add(ordi.tirer());
		//	listeLasers.add(ordi2.tirer());
		//listeLasers.add(ordi3.tirer());
		//listeLasers.add(new Laser(new Vecteur(ordi.getPositionX()+ordi.getLargeurOrdi()/2,HAUTEUR_DU_MONDE-ordi.getLongueurOrdi()), angle, new Vecteur(0,0.5)));

	}
	
	private void detectionCollisionMurBalle() {

		for (Balle balle : listeBalles) {

			Vecteur position = new Vecteur(balle.getPosition());
			double diametre = balle.getDiametre();

			if (position.getY() + diametre >= HAUTEUR_DU_MONDE) { // touche le sol
				balle.getVitesse().setY(-balle.getVitesse().getY());
			}

			if (position.getX() + diametre >= LARGEUR_DU_MONDE) {
				if (balle.getVitesse().getX() > 0)
					balle.getVitesse().setX(-balle.getVitesse().getX());

			}
			if (position.getX() <= 0) {
				if (balle.getVitesse().getX() < 0)
					balle.getVitesse().setX(-balle.getVitesse().getX());
			}
		}

	}
	
	
}




//}
