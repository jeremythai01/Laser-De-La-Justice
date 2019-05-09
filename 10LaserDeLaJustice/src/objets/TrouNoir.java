package objets;

import java.awt.Color;
import java.awt.Graphics2D;
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
import interfaces.Dessinable;

/**
 * Classe qui cree un trou noir et qui memorise sa position
 * Un trou noir, lorsqu'un laser rentre en contact avec, fait disparaitre le laser
 * @author Arnaud Lefebvre
 * @author Miora R. Rakoto
 *
 */
public class TrouNoir extends Obstacles implements Dessinable, Serializable {
	
    private static final long serialVersionUID = 1L;
	
	private Vecteur position;
	private final double LARGEUR=2;
	private final double distance=2;
	private double qtRot=0;
	private Ellipse2D.Double trou;
	
	private transient BufferedImage img;
	private transient ArrayList<BufferedImage> listeImages = new ArrayList ();
	
	
	//Par Arnaud Lefebvre
	/**
	 * Constructeur qui memorise la position
	 * @param position, la position donnee au trou noir
	 */
	public TrouNoir(Vecteur position) {
		this.position=position;
		
		URL urlCoeur = getClass().getClassLoader().getResource("trounoir2.png");
		if (urlCoeur == null) {
			JOptionPane.showMessageDialog(null , "Fichier coeur.png introuvable");
			System.exit(0);}
		try {
			img = ImageIO.read(urlCoeur);
			listeImages.add(img);
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
        System.out.println(listeImages.size() + " write");
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
	
	//Par Arnaud Lefebvre
	/**
	 * Methode qui permet de savoir la largeur du trou noir
	 * @return LARGEUR, la largeur du trou noir
	 */
	public double getLARGEUR() {
		return LARGEUR;
	}

	//Par Arnaud Lefebvre
	/**
	 * Methode qui permet de savoir la distance entre les deux cercles du trou noir
	 * @return distnace, la distance entre les deux cercles du trou noir
	 */
	public double getDistance() {
		return distance;
	}




	//Par Arnaud Lefebvre
	/**
	 * Permet de dessiner le trou selon le contexte graphique en parametre.
	 * @param g2d contexte graphique
	 * @param mat matrice de transformation monde-vers-composant
	 * @param hauteur hauteur du monde reelle
	 * @param largeur largeur du monde reelle
	 * 
	 */
	public void dessiner(Graphics2D g, AffineTransform mat, double hauteur, double largeur) {
		AffineTransform matLocal = new AffineTransform(mat);
		AffineTransform matLocale = new AffineTransform(mat);
		img = listeImages.get(0);
		
	
		
		double factX = (LARGEUR+distance*2)/ img.getWidth(null) ;
		double factY = (LARGEUR+distance*2)/ img.getHeight(null) ;
		matLocale.rotate(qtRot, position.getX()+LARGEUR/2, position.getY()+LARGEUR/2);
		matLocale.scale( factX, factY);
	
		matLocale.translate( (position.getX()-distance)   / factX , (position.getY() -distance) / factY);
		
		g.setColor(Color.LIGHT_GRAY);
		trou=new Ellipse2D.Double(position.getX()-distance, position.getY()-distance, LARGEUR+distance*2, LARGEUR+distance*2);
		g.fill(matLocal.createTransformedShape(trou));
		
		trou= new Ellipse2D.Double(position.getX(), position.getY(), LARGEUR, LARGEUR);
		g.setColor(Color.black);
		g.draw(matLocale.createTransformedShape(trou));
		
		
		
		g.drawImage(img, matLocale, null);
		
		
		
	}
	
	//Par Arnaud Lefebvre
	/**
	 * Methode qui permet au trou noir de savoir a quelle rotation il est rendu
	 * @param rotation, la rotation a faire
	 */
	public void savoirQuantiteRotation(double rotation) {
		this.qtRot=rotation;
	}
	
	//Par Arnaud Lefebvre
	/**
	 * Methode qui permet de calculer l'aire du trou noir
	 * @return, l'aire du trou noir sous forme d'area
	 */
	public Area getAireTrou() {
		 Ellipse2D aire= new Ellipse2D.Double(position.getX(), position.getY(), LARGEUR, LARGEUR);
		 return new Area(aire);
	}
	
	//Par Arnaud Lefebvre
	/**
	 * Methode qui permet de calculer l'aire du trou d'attraction
	 * @return, l'aire du trou d'attraction sous forme d'area
	 */
	public Area getAireGrandTrou() {
		Ellipse2D aire = new Ellipse2D.Double(position.getX()-distance, position.getY()-distance, LARGEUR+distance*2, LARGEUR+distance*2);
		return new Area(aire);
	}



	//Par Arnaud Lefebvre
	/**
	 * Cette méthode permet d'avoir accès à la valeur de la position
	 * @return le vecteur de la position du trou noir
	 */
	public Vecteur getPosition() {
		return position;
	}



	//Par Arnaud Lefebvre
	/**
	 * Cette méthode permet de modifier la valeur de la position du trou noir
	 * @param position est la nouvelle position en vecteur du trou noir dans les mesures du réel
	 */
	public void setPosition(Vecteur position) {
		this.position = position;
	}






}