package objets;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
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
import interfaces.Dessinable;
//import sim.math.SVector3d;
import physique.Laser;
/**
 * Classe qui cree un bloc d'eau et memorise sa position
 * Un bloc d'eau va devier le laser selon la loi de la refraction lorsque le laser entre en contact avec
 * @author Arnaud Lefebvre
 *
 */
public class BlocRefraction extends Obstacles implements Dessinable, Serializable {
	
	private static final long serialVersionUID = 1L;
	private Vecteur position;
	private final double LARGEUR=2.0;
	private Rectangle2D.Double bloc;
	private boolean premiereCollision=true;
	private double indiceRefraction;
	private final double indiceVide=1.0;
	private double hauteurBloc=0.5;
	private double angle;
	private boolean eau=true;
	private boolean verre=false;
	private boolean diamant=false;
	private boolean disulfureCarbone=false;
	private transient BufferedImage img;
	private transient ArrayList<BufferedImage> listeImages = new ArrayList ();
	private URL urlCoeur;
	
	

	
	
	//Par Arnaud
	/**
	 * Constructeur du bloc deau qui prend en parametre la position du bloc et son indice de refraction
	 * @param position, la position du bloc
	 * @param indiceRefraction, l'indice de refraction du bloc
	 */
	public BlocRefraction(Vecteur position, double indiceRefraction) {
		this.position=position;
		this.indiceRefraction=indiceRefraction;
		lireImage();
	}
	
	//Par Arnaud
	/**
	 * Methode qui permet de lire une image
	 */
	public void lireImage() {
		
		if(indiceRefraction==1.33) {
			urlCoeur = getClass().getClassLoader().getResource("eau.jpg");
		}
		if(indiceRefraction==1.50) {
			urlCoeur = getClass().getClassLoader().getResource("verre.jpg");
		}
		if(indiceRefraction==2.42) {
			urlCoeur = getClass().getClassLoader().getResource("diamant.jpg");	
		}
		if(indiceRefraction==1.63) {
			urlCoeur = getClass().getClassLoader().getResource("disulfure.jpg");
		}
		
		if (urlCoeur == null) {
			JOptionPane.showMessageDialog(null , "Fichier niveau2.png introuvable");
			System.exit(0);}
		try {
			img = ImageIO.read(urlCoeur);
		    listeImages.add(img);
		}
		catch (IOException e) {
			System.out.println("Erreur pendant la lecture du fichier d'image");
		}
	}
	
	//Par Arnaud
	/**
	 * Permet de dessiner le bloc selon le contexte graphique en parametre.
	 * @param g2d contexte graphique
	 * @param mat matrice de transformation monde-vers-composant
	 * @param hauteur hauteur du monde reelle
	 * @param largeur largeur du monde reelle
	 */
	public void dessiner(Graphics2D g, AffineTransform mat, double hauteur, double largeur) {
		AffineTransform matLocal = new AffineTransform(mat);
		bloc= new Rectangle2D.Double(position.getX(), position.getY(), LARGEUR, this.hauteurBloc);
		g.setColor(Color.blue);
		//g.fill(matLocal.createTransformedShape(bloc));
		img = listeImages.get(0);
		
		double factX = LARGEUR/ img.getWidth(null) ;
		double factY = hauteurBloc/ img.getHeight(null) ;
		matLocal.scale( factX, factY);
		matLocal.translate( getPosition().getX() / factX ,  getPosition().getY() / factY);
		g.drawImage(img, matLocal, null);
		
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
	
	//Par Arnaud
	/**
	 * Methode qui permet d'effectuer le calcul de la refraction
	 * @param v, le vecteur orientation du vecteur incident
	 * @param N, le vecteur de la normale du bloc
	 * @param n1,  Indice de r�fraction du milieu incident.
	 * @param n2  Indice de r�fraction du milieu refracte.
	 * @return, le nouveau vecteur T,  l`orientation du rayon r�fract�
	 */
	public Vecteur refraction(Vecteur v, Vecteur N) {
		Vecteur vecteur= new Vecteur();
		double n= indiceVide/indiceRefraction;
		Vecteur E = new Vecteur();
		E=v.multiplie(-1);
		System.out.println("la normale est"+ N);
		
		if(((1-((n*n)*(1-(E.prodScalaire(N)*(E.prodScalaire(N))))))) < 0)
		throw new RuntimeException("OUPS !!!");
		vecteur = v.multiplie(n).additionne(N.multiplie(((E.prodScalaire(N)*n)-Math.sqrt(1-((n*n)*1-E.prodScalaire(N)*(E.prodScalaire(N)))))));
		return new Vecteur(-vecteur.getX(),-vecteur.getY());
	
	}

	//Par Arnaud
	/**
	 * Methode qui permet d'effectuer le calcul de la normale
	 * @param laser, le laser qui entre en contact
	 * @param bloc, le bloc avec lequel le laser entre en contact
	 * @return, le vecteur normal du bloc
	 */
	public Vecteur calculNormal(Laser laser, BlocRefraction bloc) {

		return new Vecteur(laser.getPositionHaut().getX() - bloc.getPosition().getX(),
				laser.getPositionHaut().getY() - bloc.getPosition().getY());

		
	}

	//Par Arnaud
	/**
	 * Retourne la normale
	 * @return, la normale en  vecteur
	 */
	public Vecteur getNormal() {
		double angle = Math.toRadians(0);
		Vecteur vecMiroir = new Vecteur (Math.cos(angle), Math.sin(angle));
		Vecteur normal = new Vecteur(-vecMiroir.getY(), vecMiroir.getX());
		return normal;
	}

	//Par Arnaud
	/**
	 * Retourne la position
	 * @return, la position en vecteur
	 */
	public Vecteur getPosition() {
		return position;
	}


	//Par Arnaud
	/**
	 * Modifie la position du bloc
	 * @param position, la position du bloc qu'on veut modifier
	 */
	public void setPosition(Vecteur position) {
		this.position = position;
	}


	//Par Arnaud
	/**
	 * Permet d'obtenir une copie du bloc
	 * @return, un bloc qui est une copie du bloc
	 */
	public Rectangle2D.Double getBloc() {
		return bloc;
	}

	//Par Arnaud
	/**
	 * Permet de modifier le bloc
	 * @param bloc, le nouveau bloc desire
	 */
	public void setBloc(Rectangle2D.Double bloc) {
		this.bloc = bloc;
	}


	//Par Arnaud
	/**
	 * Permet d'obtenir la largeur du bloc 
	 * @return la largeur du bloc
	 */
	public double getLARGEUR() {
		return LARGEUR;
	}

	//Par Arnaud
	/**
	 * Permet d'obtenir la hauteur du bloc 
	 * @return la hauteur du bloc
	 */
	public double getHauteur() {
		return hauteurBloc;
	}

	//Par Arnaud
	/**
	 * Permet d'obtenir l'aire du bloc pour faire les intersections
	 * @return, l'aire du bloc sous forme d'area
	 */
	public Area getAireBloc() {
		 Rectangle2D.Double aire= new Rectangle2D.Double(position.getX(), position.getY(), LARGEUR, this.hauteurBloc);
		 return new Area(aire);
	}


	//Par Arnaud
	/**
	 * Methode qui permet de savoir si c'est la premiere collision du bloc
	 * @return premiereCollision, si c'est la premiere
	 */
	public boolean isPremiereCollision() {
		return premiereCollision;
	}


	//Par Arnaud
	/**
	 * Methode qui indique au bloc si la premiere collision est arrivee
	 * @param premiereCollision, la valeur de la premiere collision
	 */
	public void setPremiereCollision(boolean premiereCollision) {
		this.premiereCollision = premiereCollision;
	}


	//Par Arnaud
	/**
	 * Methode qui modifie l'indice du bloc
	 * @param value, le nouvel indice
	 */
	public void setIndiceRefraction(double value) {
		indiceRefraction = value;
	}


	//Par Arnaud
	/**
	 * Methode qui modifie l'angle de dessin du bloc
	 * @param value,le nouvel angle
	 */
	public void setAngle(double value) {

		angle = value;
	}
	
	
	
	

}
