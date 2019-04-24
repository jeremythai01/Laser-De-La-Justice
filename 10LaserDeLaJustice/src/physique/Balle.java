package physique;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import geometrie.Vecteur;
import geometrie.VecteurGraphique;
import interfaces.Dessinable;

/**
 * Classe qui créée une balle et mémorise sa masse, son diamètre, sa position, sa vitesse, son image, son accélération, la somme des forces qui s'applique sur elle et son type.
 * @author Jeremy Thai 
 */

public class Balle implements Dessinable, Serializable { 

	private static final long serialVersionUID = 1L ;
	private double diametre = 3;
	private double masse = 15;
	private Ellipse2D.Double cercle;
	private Vecteur position, vitesse, accel = new Vecteur(0,9.8);
	private Vecteur forceGravi;
	private MoteurPhysique mt = new MoteurPhysique();
	private Type type;
	private double qtRot;
	private Image img;
	private final double VITESSE_ROTATION = 0.04;
	
	
	
	

	/**
	 * Classe enumeration des types de balle
	 * @author Jeremy Thai
	 *
	 */
	enum Type {
		SMALL, MEDIUM, LARGE;
	}

	
	
	
	/**
	 * Constructeur ou la position, la vitesse , l'acceleration et le type de balle initiales sont spécifiés
	 * @param position Vecteur incluant les positions en x et y du coin superieur-gauche
	 * @param vitesse Vecteur incluant les vitesses en x et y
	 * @param type  type de la balle(petit, moyen, grand)
	 * @param accel Vecteur incluant les accelerations en x et y  

	 */
	public Balle(Vecteur position, Vecteur vitesse, String type, Vecteur accel) {	
		setPosition( position );
		setVitesse( vitesse );
		setAccel( accel);
		switch(type) {
		case "SMALL":
			this.type = Type.SMALL;
			setMasse(5);
			setDiametre(1);
			//lireImage("Pokeball.png");
			break;
		case "MEDIUM":
			this.type = Type.MEDIUM;
			setMasse(10);
			setDiametre(2);
			//	lireImage("UltraBall.png");
			break;
		case "LARGE":
			this.type = Type.LARGE;
			//	lireImage("MasterBall.jpg");
			break;
		}
		forceGravi = mt.forceGravi(masse, accel);
	}

	

	/**
	 * Constructeur qui permet de copier les données dune balle passée en parametre dans la balle courante
	 * @param balle balle dont les données seront copiés 
	 */
	public Balle ( Balle balle ) {
		this.diametre = balle.getDiametre();
		setAccel( balle.getAccel());
		this.masse = balle.getMasse();
		setPosition( balle.getPosition() );
		setVitesse(balle.getVitesse());
		this.forceGravi = balle.getForceGravi();
		this.type = balle.getType();
		setImg(balle.getImg());

	}


	/**
	 * Permet de lire et enregistrer limage de la balle 
	 * @param str chaine de caractere representant le nom du fichier image 
	 */
	private void lireImage(String str) {

		URL urlBalle= getClass().getClassLoader().getResource(str);
		if (urlBalle == null) {
			JOptionPane.showMessageDialog(null , "Fichier coeur.png introuvable");
			System.exit(0);}
		try {
			img = ImageIO.read(urlBalle);
		}
		catch (IOException e) {
			System.out.println("Erreur pendant la lecture du fichier d'image");
		}
	}


	/**
	 * Permet de dessiner la balle selon le contexte graphique en parametre.
	 * @param g2d contexte graphique
	 * @param mat matrice de transformation monde-vers-composant
	 * @param hauteur hauteur du monde reelle
	 * @param largeur largeur du monde reelle
	 * 
	 */
	@Override
	public void dessiner(Graphics2D g2d, AffineTransform mat, double hauteur, double largeur) {
		AffineTransform matLocal = new AffineTransform(mat);

		/*
		double factX = (diametre)/ img.getWidth(null) ;
		double factY = (diametre)/ img.getHeight(null) ;
		matLocal.rotate(qtRot, position.getX()+diametre/2, position.getY()+diametre/2);
		matLocal.scale( factX, factY);

		matLocal.translate( (position.getX() )   / factX , (position.getY()) / factY);

		g2d.drawImage(img, matLocal, null);
		 */

		switch(type){
		case LARGE:
			g2d.setColor(Color.blue);
			break;
		case MEDIUM:
			g2d.setColor(Color.green);
			break;
		case SMALL:
			g2d.setColor(Color.red);
			break;
		}		

		cercle = new Ellipse2D.Double(position.getX(), position.getY(), diametre, diametre);
		g2d.fill( matLocal.createTransformedShape(cercle) );	

/*
		VecteurGraphique vG = new VecteurGraphique(vitesse.getX() /2, vitesse.getY() /2);
		vG.setOrigineXY(position.getX()+diametre/2 , position.getY()+diametre/2); // origine du vecteur au centre de la balle 
		vG.setLongueurTete(1);
		vG.dessiner(g2d, mat, hauteur, largeur);
		*/
	}




	/**
	 * Méthode qui permet de faire tourner l'image de la balle 
	 */
	public void updateRotation() {

		if(vitesse.getX() > 0)
			qtRot += VITESSE_ROTATION ;
		if(vitesse.getX() < 0)
			qtRot -= VITESSE_ROTATION ;

	}

	
	
	/**
	 * Effectue une iteration de l'algorithme d'Euler implicite. Calcule la nouvelle vitesse et la nouvelle
	 * position de la balle.
	 * @param deltaT intervalle de temps (pas)
	 */
	public void unPasEuler(double deltaT) {
		MoteurPhysique.unPasEuler(deltaT, position, vitesse, accel);
		//System.out.println("Nouvelle vitesse: " + vitesse.toString() + "  Nouvelle position: " + position.toString());
	}
	/**
	 * Effectue une iteration de l'algorithme Verlet. Calcule la nouvelle vitesse et la nouvelle
	 * position de la balle.
	 * @param deltaT intervalle de temps (pas)
	 */
	public void unPasVerlet(double deltaT) {
		MoteurPhysique.miseAJourAcceleration(forceGravi, masse, accel);
		MoteurPhysique.unPasVerlet(deltaT, position, vitesse, accel);
	}
	/**
	 * Effectue une iteration de l'algorithme de Runge-Kutta ordre 4. Calcule la nouvelle vitesse et la nouvelle
	 * position de la balle.
	 * @param deltaT intervalle de temps (pas)
	 * @param tempsEcoule temps simule  (s)
	 */
	public void unPasRK4(double deltaT, double tempsEcoule) {
		MoteurPhysique.miseAJourAcceleration(forceGravi, masse, accel);
		MoteurPhysique.unPasRK4(deltaT, tempsEcoule, position, vitesse, accel);
	}



	/**
	 * Modifie le type de balle en creant 2 balles d'un plus petit type et en les ajoutant dans la liste de balles 
	 * @param liste liste des balles 
	 */

	public void shrink(ArrayList<Balle> liste, Vecteur accel) {

		Balle nouvBalle1;
		Balle nouvBalle2;

		switch(type)	{

		case LARGE:

			nouvBalle1 = new Balle(position, vitesse, "MEDIUM", accel);
			nouvBalle2 = new Balle(position, vitesse, "MEDIUM", accel);
			if(vitesse.getX() < 0) {

				//gauche
				nouvBalle1.setVitesse(new Vecteur(vitesse.getX(),vitesse.getY())); 
				nouvBalle1.getPosition().setX(position.getX()-1.000005);

				//droite
				nouvBalle2.setVitesse(new Vecteur(-vitesse.getX(),vitesse.getY())); 
				nouvBalle2.getPosition().setX(position.getX()+1.000005);
			}

			if(vitesse.getX() > 0) {
				//gauche
				nouvBalle1.setVitesse(new Vecteur(-vitesse.getX(),vitesse.getY())); 
				nouvBalle1.getPosition().setX(position.getX()-1.000005);

				//droite
				nouvBalle2.setVitesse(new Vecteur(vitesse.getX(),vitesse.getY())); 
				nouvBalle2.getPosition().setX(position.getX()+1.000005);
			}
			liste.remove(this);
			liste.add(nouvBalle1);
			liste.add(nouvBalle2);
			break;

		case MEDIUM:

			nouvBalle1 = new Balle(position, vitesse, "SMALL", accel);
			nouvBalle2 = new Balle(position, vitesse, "SMALL", accel);

			if(vitesse.getX() < 0) {
				//gauche
				nouvBalle1.setVitesse(new Vecteur(vitesse.getX(),vitesse.getY())); 
				nouvBalle1.getPosition().setX(position.getX()-0.7);

				//droite
				nouvBalle2.setVitesse(new Vecteur(-vitesse.getX(),vitesse.getY())); 
				nouvBalle2.getPosition().setX(position.getX()+0.7);

			}

			if(vitesse.getX() > 0) {

				//gauche
				nouvBalle1.setVitesse(new Vecteur(-vitesse.getX(),vitesse.getY())); 
				nouvBalle1.getPosition().setX(position.getX()-0.7);

				//droite
				nouvBalle2.setVitesse(new Vecteur(vitesse.getX(),vitesse.getY())); 
				nouvBalle2.getPosition().setX(position.getX()+0.7);
			}
			liste.remove(this);
			liste.add(nouvBalle1);
			liste.add(nouvBalle2);
			break;
			
		case SMALL:
			liste.remove(this);
			break;
		}		
	}

	
	
	
	/**
	 * Modifie la position de la balle
	 * @param pos vecteur des positions x et y
	 */
	public void setPosition(Vecteur pos) {
		Vecteur newVec = new Vecteur(pos.getX(), pos.getY());
		this.position = newVec;
	}
	
	
	/**
	 * modifie ou affecte une vitesse a celle courante de la balle
	 * @param vitesse vecteur des vitesse x et y
	 */
	public void setVitesse(Vecteur vitesse) {

		Vecteur newVec = new Vecteur(vitesse.getX(), vitesse.getY());
		this.vitesse = newVec;
	}
	
	/**
	 * Associe une acceleration, ou modifie l'acceleration courante de la balle
	 * @param accel vecteur des accélérations en x et y
	 */
	public void setAccel(Vecteur accel) {
		Vecteur newVec = new Vecteur(accel.getX(), accel.getY());
		this.accel = newVec;
	}

	
	/**
	 * Modifie le diametre de la balle
	 * @param diametre Le nouveau diamètre
	 */
	public void setDiametre(double diametre) { this.diametre = diametre; }

	
	/**
	 * Modifie la masse 
	 * @param masseEnKg masse en kg
	 */
	public void setMasse(double masse) { this.masse = masse; }

	
	/**
	 * Modifie l'image de la balle par celle passée en paramètre
	 * @param img nouvelle image en paramètre
	 */
	public void setImg(Image img) {
		this.img = img;
	}
	
	
	
	/**
	 * Retourne la position courante
	 * @return la position courante
	 */
	public Vecteur getPosition() { return (position); }

	
	/**
	 * Retourne la vitesse courante
	 * @return la vitesse courante
	 */
	public Vecteur getVitesse() { return (vitesse); }

	/**
	 * Retourne l'acceleration courante
	 * @return acceleration courante
	 */
	public Vecteur getAccel() { return (accel); }


	/**
	 * Retourne le diametre de la balle
	 * @return Le diamètre
	 */
	public double getDiametre() { return diametre; }


	/**
	 * Retourne la masse en Kg
	 * @return La masse en kg
	 */
	public double getMasse() {	return masse; }


	/**
	 * Retourne la force gravitationnelle
	 * @return force gravitationnelle
	 */
	public Vecteur getForceGravi() { return forceGravi; }


	/**
	 * retourne l'aire d'une balle en forme de cercle
	 * @return aire de la balle 
	 */
	public Area getAire() {
		cercle = new Ellipse2D.Double(position.getX(), position.getY(), diametre, diametre);
		return new Area(cercle);

	}


	/**
	 * Methode qui retourne le type de la balle 
	 * @return type type de la balle
	 */
	public Type getType()  {return type; }


	/**
	 * Retourne l'image de la balle 
	 * @return img image de la balle 
	 */
	public Image getImg() {return img; }
}





