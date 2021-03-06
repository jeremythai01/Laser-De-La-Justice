package physique;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
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

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import geometrie.Vecteur;
import geometrie.VecteurGraphique;
import interfaces.Dessinable;
import utilite.ModeleAffichage;

/**
 * Classe qui cr��e une balle et m�morise sa masse, son diam�tre, sa position, sa vitesse, son image, son acc�l�ration, la somme des forces qui s'applique sur elle et son type.
 * @author Jeremy Thai 
 * @author Miora R. Rakoto
 */

public class Balle implements Dessinable, Serializable { 

	private static final long serialVersionUID = 1L ;
	private double diametre = 5;
	private double masse = 15;
	private Ellipse2D.Double cercle;
	private Vecteur position, vitesse, accel = new Vecteur(0,0);
	private Vecteur forceGravi;
	private Type type;
	private double qtRot;
	private final double VITESSE_ROTATION = 0.04;
	private VecteurGraphique vG;
	private boolean modeScientifique = false;
	private static ModeleAffichage modele;	
	private transient BufferedImage img;
	private transient ArrayList<BufferedImage> listeImages = new ArrayList ();

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
	 * Constructeur ou la position, la vitesse , l'acceleration et le type de balle initiales sont sp�cifi�s
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
			setDiametre(2);
			lireImage("alienBalleRouge.png");
			break;
		case "MEDIUM":
			this.type = Type.MEDIUM;
			setMasse(10);
			setDiametre(3);
			lireImage("alienBalleVerte.png");
			break;
		case "LARGE":
			this.type = Type.LARGE;
			lireImage("alienBalleBleue.png");
			break;
		}
		listeImages.add(img);
		//System.out.println(images.size());
		forceGravi = MoteurPhysique.forceGravi(masse, accel);
	}


	//Jeremy Thai
	/**
	 * Constructeur qui permet de copier les donn�es dune balle pass�e en parametre dans la balle courante
	 * @param balle balle dont les donn�es seront copi�s 
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
			JOptionPane.showMessageDialog(null , "Fichier "+str+" introuvable");
			System.exit(0);}
		try {
			img = ImageIO.read(urlBalle);
		 // on rentre les images dans une listes
		}
		catch (IOException e) {
			System.out.println("Erreur pendant la lecture du fichier d'image");
		}
	}
	
	//Par Miora
	/**
	 * Cette methode permet d'�crire les images de la classe balle
	 * @param out : l'objet qui s'occupe des flux de sorties
	 * @throws IOException
	 */
	private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        
        out.writeInt(listeImages.size());
        for (BufferedImage chaqueImage : listeImages) {
            ImageIO.write(chaqueImage, "png", out);
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
        int nbImgListe = in.readInt();
        listeImages = new ArrayList<BufferedImage>(nbImgListe);
        for (int i = 0; i < nbImgListe; i++) {
            listeImages.add(ImageIO.read(in));
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
		cercle = new Ellipse2D.Double(position.getX(), position.getY(), diametre, diametre);
		Stroke stroke = g2d.getStroke();
		g2d.setColor(Color.black);
		g2d.setStroke(new BasicStroke(2.0f));
		g2d.draw( matLocal.createTransformedShape(cercle) );
		g2d.setStroke(stroke);
		img = listeImages.get(0);
		
		double factX = (diametre)/ img.getWidth(null) ;
		double factY = (diametre)/ img.getHeight(null) ;
		matLocal.rotate(qtRot, position.getX()+diametre/2, position.getY()+diametre/2);
		matLocal.scale( factX, factY);


		matLocal.translate( (position.getX() )   / factX , (position.getY()) / factY);

		g2d.drawImage(img, matLocal, null);
		 


		if(modeScientifique) {
			
			Stroke stroke1 = g2d.getStroke();
			g2d.setStroke(new BasicStroke(3.0f));
			
			//Vecteur vitesse
			
			g2d.setColor(Color.black);
			vG = new VecteurGraphique(vitesse.getX() /2, vitesse.getY() /2);
			vG.setOrigineXY(position.getX()+diametre/2 , position.getY()+diametre/2); // origine du vecteur au centre de la balle 
			vG.setLongueurTete(1);
			vG.dessiner(g2d, mat, hauteur, largeur);

	
			double eXR1 = (position.getX()+diametre/2 +  vitesse.getX() /4 + (0.30*(vitesse.getX()/Math.abs(vitesse.getX()))) ) * modele.getPixelsParUniteX();
			double eYR1 =  (position.getY()+diametre/2 + vitesse.getY() /4 ) * modele.getPixelsParUniteY();

			g2d.setColor(Color.yellow);
			g2d.drawString("V", (int)( eXR1), (int)(eYR1));
			

			//Vecteur forceGravi
			g2d.setColor(Color.red);
			vG = new VecteurGraphique(accel.getX() /2, accel.getY() /2);
			vG.setOrigineXY(position.getX()+diametre/2 , position.getY()+diametre/2); // origine du vecteur au centre de la balle 
			vG.setLongueurTete(1);
			vG.dessiner(g2d, mat, hauteur, largeur);
			
			double eXR2 =(position.getX()+0.25+diametre/2) * modele.getPixelsParUniteX();
			double eYR2 = ( position.getY()+diametre/2+diametre) * modele.getPixelsParUniteY();
			
			g2d.setColor(Color.ORANGE);
			g2d.drawString("FG", (int)( eXR2), (int)(eYR2));

		
			g2d.setStroke(stroke1);
		}
	}



	//Jeremy Thai
	/**
	 * M�thode qui permet de faire tourner l'image de la balle 
	 */
	public void updateRotation() {

		if(vitesse.getX() > 0)
			qtRot += VITESSE_ROTATION ;
		if(vitesse.getX() < 0)
			qtRot -= VITESSE_ROTATION ;

	}


	
	//Jeremy Thai
	/**
	 * Effectue une iteration de l'algorithme Verlet. Calcule la nouvelle vitesse et la nouvelle
	 * position de la balle.
	 * @param deltaT intervalle de temps (pas)
	 */
	public void unPasVerlet(double deltaT) {
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
				nouvBalle1.getPosition().setX(position.getX()- nouvBalle1.getDiametre()/2 - 0.5  );

				//droite
				nouvBalle2.setVitesse(new Vecteur(-vitesse.getX(),vitesse.getY())); 
				nouvBalle2.getPosition().setX(position.getX()+nouvBalle1.getDiametre()/2  + 0.5 );
			}

			if(vitesse.getX() > 0) {
				//gauche
				nouvBalle1.setVitesse(new Vecteur(-vitesse.getX(),vitesse.getY())); 
				nouvBalle1.getPosition().setX(position.getX()-nouvBalle1.getDiametre()/2  - 0.5 );

				//droite
				nouvBalle2.setVitesse(new Vecteur(vitesse.getX(),vitesse.getY())); 
				nouvBalle2.getPosition().setX(position.getX()+nouvBalle1.getDiametre()/2  + 0.5 );
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
				nouvBalle1.getPosition().setX(position.getX()- nouvBalle1.getDiametre()/2 - 0.5);

				//droite
				nouvBalle2.setVitesse(new Vecteur(-vitesse.getX(),vitesse.getY())); 
				nouvBalle2.getPosition().setX(position.getX() + nouvBalle1.getDiametre()/2 + 0.5);

			}

			if(vitesse.getX() > 0) {

				//gauche
				nouvBalle1.setVitesse(new Vecteur(-vitesse.getX(),vitesse.getY())); 
				nouvBalle1.getPosition().setX(position.getX()- nouvBalle1.getDiametre()/2 - 0.5);

				//droite
				nouvBalle2.setVitesse(new Vecteur(vitesse.getX(),vitesse.getY())); 
				nouvBalle2.getPosition().setX(position.getX()+ nouvBalle1.getDiametre()/2 + 0.5);
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
	 * @param accel vecteur des acc�l�rations en x et y
	 */
	public void setAccel(Vecteur accel) {
		Vecteur newVec = new Vecteur(accel.getX(), accel.getY());
		this.accel = newVec;
	}


	//Jeremy Thai
	/**
	 * Modifie le diametre de la balle
	 * @param diametre Le nouveau diam�tre
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
	 * Modifie l'image de la balle par celle pass�e en param�tre
	 * @param img nouvelle image en param�tre
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
	 * @return Le diam�tre
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
		return MoteurPhysique.forceGravi(masse, accel);
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
	/**
	 * Modifie et creer un modele d'affichage avec les valeurs reelles venant de la scene 
	 * @param largeur largeur du monde en pixels  
	 * @param hauteur hauteur du monde en pixels 
	 * @param largeurMonde largeur du monde en unites reelles 
	 */
	public static void setModele(double largeur, double hauteur, double largeurMonde) {
	   modele =  new ModeleAffichage(largeur, hauteur, largeurMonde);
	}
}

