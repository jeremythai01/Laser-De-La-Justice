package physique;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import geometrie.Vecteur;
import geometrie.VecteurGraphique;
import interfaces.Dessinable;
import utilite.ModeleAffichage;

/**
 * Classe qui créée une balle et mémorise sa masse, son diamètre, sa position, sa vitesse, son image, son accélération, la somme des forces qui s'applique sur elle et son type.
 * @author Jeremy Thai 
 * @author Miora R. Rakoto
 */

public class Balle implements Dessinable, Serializable { 

	private static final long serialVersionUID = 1L ;
	private double diametre = 3;
	private double masse = 15;
	private Ellipse2D.Double cercle;
	private Vecteur position, vitesse, accel = new Vecteur(0,9.8);
	private Vecteur forceGravi,forceElectrique = new Vecteur(0,0);
	private MoteurPhysique mt = new MoteurPhysique();
	private Type type;
	private double qtRot;
	private final double VITESSE_ROTATION = 0.04;
	private VecteurGraphique vG;
	private boolean modeScientifique = false;
	private Vecteur sommeForces;
	private static ModeleAffichage modele;
	private double charge=0.000006;
	
	private transient BufferedImage img;
	private transient ArrayList<BufferedImage> images = new ArrayList ();


	/**
	 * Classe enumeration des types de balle
	 * @author Jeremy Thai
	 *
	 */
	enum Type {
		SMALL, MEDIUM, LARGE;
	}

	//Jeremy Thai
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
			lireImage("Pokeball.png");
			break;
		case "MEDIUM":
			this.type = Type.MEDIUM;
			setMasse(10);
			setDiametre(2);
			lireImage("UltraBall.png");
			break;
		case "LARGE":
			this.type = Type.LARGE;
			lireImage("MasterBall.jpg");
			break;
		}
		forceGravi = MoteurPhysique.forceGravi(masse, accel);
	}


	//Jeremy Thai
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

	//Jeremy Thai
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
			images.add(img); // on rentre les images dans une listes
		}
		catch (IOException e) {
			System.out.println("Erreur pendant la lecture du fichier d'image");
		}
	}
	
	//Par Miora
	/**
	 * Cette methode permet d'écrire les images de la classe balle
	 * @param out : l'objet qui s'occupe des flux de sorties
	 * @throws IOException
	 */
	private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(images.size());
        for (BufferedImage chaqueImage : images) {
            ImageIO.write(chaqueImage, "png", out);
            ImageIO.write(chaqueImage, "jpg", out);
        }
    }

	//Par Miora
	/**
	 * Cette methode permet lire les images de la classe balle
	 * @param in : l'objet qui s'occupe des flux d'entrees
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        final int imageCount = in.readInt();
        images = new ArrayList<BufferedImage>(imageCount);
        for (int i=0; i<imageCount; i++) {
            img = ImageIO.read(in);
        }
    }

    //Jeremy Thai
	/**
	 * Permet de dessiner la balle selon le contexte graphique en parametre.
	 * @param g2d contexte graphique
	 * @param mat matrice de transformation monde-vers-composant
	 * @param hauteur hauteur du monde reelle
	 * @param largeur largeur du monde reelle
	 * 
	 */
	public void dessiner(Graphics2D g2d, AffineTransform mat, double hauteur, double largeur) {

		AffineTransform matLocal = new AffineTransform(mat);

	
		double factX = (diametre)/ img.getWidth(null) ;
		double factY = (diametre)/ img.getHeight(null) ;
		matLocal.rotate(qtRot, position.getX()+diametre/2, position.getY()+diametre/2);
		matLocal.scale( factX, factY);

		matLocal.translate( (position.getX() )   / factX , (position.getY()) / factY);

		g2d.drawImage(img, matLocal, null);
		 

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


		if(modeScientifique) {
			
			Stroke stroke = g2d.getStroke();
			g2d.setStroke(new BasicStroke(3.0f));
			
			//Vecteur vitesse
			
			g2d.setColor(Color.black);
			vG = new VecteurGraphique(vitesse.getX() /2, vitesse.getY() /2);
			vG.setOrigineXY(position.getX()+diametre/2 , position.getY()+diametre/2); // origine du vecteur au centre de la balle 
			vG.setLongueurTete(1);
			vG.dessiner(g2d, mat, hauteur, largeur);

	
			double eXR1 = (position.getX()+diametre/2 +  vitesse.getX() /4 + (0.30*(vitesse.getX()/Math.abs(vitesse.getX()))) ) * modele.getPixelsParUniteX();
			double eYR1 =  (position.getY()+diametre/2 + vitesse.getY() /4 ) * modele.getPixelsParUniteY();

			g2d.drawString("V", (int)( eXR1), (int)(eYR1));
			

			//Vecteur forceGravi
			g2d.setColor(Color.red);
			vG = new VecteurGraphique(accel.getX() /2, accel.getY() /2);
			vG.setOrigineXY(position.getX()+diametre/2 , position.getY()+diametre/2); // origine du vecteur au centre de la balle 
			vG.setLongueurTete(1);
			vG.dessiner(g2d, mat, hauteur, largeur);
			
			double eXR2 =(position.getX()+0.25+diametre/2) * modele.getPixelsParUniteX();
			double eYR2 = ( position.getY()+diametre/2+diametre) * modele.getPixelsParUniteY();
			
			g2d.drawString("FG", (int)( eXR2), (int)(eYR2));

		
			g2d.setStroke(stroke);
		}
	}



	//Jeremy Thai
	/**
	 * Méthode qui permet de faire tourner l'image de la balle 
	 */
	public void updateRotation() {

		if(vitesse.getX() > 0)
			qtRot += VITESSE_ROTATION ;
		if(vitesse.getX() < 0)
			qtRot -= VITESSE_ROTATION ;

	}


	//Jeremy Thai
	/**
	 * Effectue une iteration de l'algorithme d'Euler implicite. Calcule la nouvelle vitesse et la nouvelle
	 * position de la balle.
	 * @param deltaT intervalle de temps (pas)
	 */
	public void unPasEuler(double deltaT) {
		MoteurPhysique.unPasEuler(deltaT, position, vitesse, accel);
		//System.out.println("Nouvelle vitesse: " + vitesse.toString() + "  Nouvelle position: " + position.toString());
	}
	
	//Jeremy Thai
	/**
	 * Effectue une iteration de l'algorithme Verlet. Calcule la nouvelle vitesse et la nouvelle
	 * position de la balle.
	 * @param deltaT intervalle de temps (pas)
	 */
	public void unPasVerlet(double deltaT) {
		calculerForcesEtAccel();
		MoteurPhysique.unPasVerlet(deltaT, position, vitesse, accel);
	}
	
	//Jeremy Thai
	/**
	 * Effectue une iteration de l'algorithme de Runge-Kutta ordre 4. Calcule la nouvelle vitesse et la nouvelle
	 * position de la balle.
	 * @param deltaT intervalle de temps (pas)
	 * @param tempsEcoule temps simule  (s)
	 */
	public void unPasRK4(double deltaT, double tempsEcoule) {
		//	MoteurPhysique.miseAJourAcceleration(forceGravi, masse, accel);
		MoteurPhysique.unPasRK4(deltaT, tempsEcoule, position, vitesse, accel);
	}



	//Jeremy Thai
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




	//Jeremy Thai
	/**
	 * Modifie la position de la balle
	 * @param pos vecteur des positions x et y
	 */
	public void setPosition(Vecteur pos) {
		Vecteur newVec = new Vecteur(pos.getX(), pos.getY());
		this.position = newVec;
	}


	//Jeremy Thai
	/**
	 * modifie ou affecte une vitesse a celle courante de la balle
	 * @param vitesse vecteur des vitesse x et y
	 */
	public void setVitesse(Vecteur vitesse) {

		Vecteur newVec = new Vecteur(vitesse.getX(), vitesse.getY());
		this.vitesse = newVec;
	}

	//Jeremy Thai
	/**
	 * Associe une acceleration, ou modifie l'acceleration courante de la balle
	 * @param accel vecteur des accélérations en x et y
	 */
	public void setAccel(Vecteur accel) {
		Vecteur newVec = new Vecteur(accel.getX(), accel.getY());
		this.accel = newVec.additionne(this.accel);
	}


	//Jeremy Thai
	/**
	 * Modifie le diametre de la balle
	 * @param diametre Le nouveau diamètre
	 */
	public void setDiametre(double diametre) { this.diametre = diametre; }


	//Jeremy Thai
	/**
	 * Modifie la masse 
	 * @param masseEnKg masse en kg
	 */
	public void setMasse(double masse) { this.masse = masse; }


	//Jeremy Thai
	/**
	 * Modifie l'image de la balle par celle passée en paramètre
	 * @param img nouvelle image en paramètre
	 */
	public void setImg(BufferedImage img) {
		this.img = img;
	}



	//Jeremy Thai
	/**
	 * Retourne la position courante
	 * @return la position courante
	 */
	public Vecteur getPosition() { return (position); }


	//Jeremy Thai
	/**
	 * Retourne la vitesse courante
	 * @return la vitesse courante
	 */
	public Vecteur getVitesse() { return (vitesse); }

	//Jeremy Thai
	/**
	 * Retourne l'acceleration courante
	 * @return acceleration courante
	 */
	public Vecteur getAccel() { return (accel); }


	//Jeremy Thai
	/**
	 * Retourne le diametre de la balle
	 * @return Le diamètre
	 */
	public double getDiametre() { return diametre; }


	//Jeremy Thai
	/**
	 * Retourne la masse en Kg
	 * @return La masse en kg
	 */
	public double getMasse() {	return masse; }



	//Jeremy Thai
	/**
	 * retourne l'aire d'une balle en forme de cercle
	 * @return aire de la balle 
	 */
	public Area getAire() {
		cercle = new Ellipse2D.Double(position.getX(), position.getY(), diametre, diametre);
		return new Area(cercle);

	}


	//Jeremy Thai
	/**
	 * Methode qui retourne le type de la balle 
	 * @return type type de la balle
	 */
	public Type getType()  {return type; }


	//Jeremy Thai
	/**
	 * Retourne l'image de la balle 
	 * @return img image de la balle 
	 */
	public BufferedImage getImg() {return img; }


	//Jeremy Thai
	public double getEnergieCinetique() {
		return MoteurPhysique.energieCinetique(masse, vitesse);
	}

	//Jeremy Thai
	public double getEnergiePotentielle(double hauteur) {

		return MoteurPhysique.energiePotentielle( masse, accel, hauteur-position.getY());
	}


	//Jeremy Thai
	/**
	 * Retourne la force gravitationnelle
	 * @return force gravitationnelle
	 */
	public Vecteur getForceGravi() {
		return forceGravi;
	}



	//Jeremy Thai
	/**
	 * Modifie la valeur (vrai ou faux) du mode scientifique par celle passee en parametre
	 * @param modeScientifique nouvelle valeur passee en parametre
	 */
	public void setModeScientifique(boolean modeScientifique) {
		this.modeScientifique = modeScientifique;
	}

	//Jeremy Thai
	
	public void calculerForcesEtAccel() {
		forceGravi = MoteurPhysique.forceGravi(masse, accel);
		sommeForces = MoteurPhysique.sommeForces(forceGravi, forceElectrique);
		MoteurPhysique.miseAJourAcceleration(sommeForces, masse, accel);
		
	}

	//Jeremy Thai
	public void setForcesElectrique(Vecteur forceElectrique) {
		this.forceElectrique = new Vecteur(forceElectrique);
	}


	//Jeremy Thai
	public static void setModele(double longueur, double hauteur, double longueurMonde) {
	   modele =  new ModeleAffichage(longueur, hauteur, longueurMonde);
	}
}

