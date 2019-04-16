package objets;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import geometrie.Vecteur;
import interfaces.Dessinable;
import physique.Balle;
import physique.Laser;

/**
 * Classe qui cree un trou noir et qui memorise sa position
 * Un trou noir, lorsqu'un laser rentre en contact avec, fait disparaitre le laser
 * @author Arnaud Lefebvre
 * @author Arezki Issaadi
 *
 */
public class TrouNoir extends Objet implements Dessinable, Serializable {
	
    private static final long serialVersionUID = 1L;
	
	private Vecteur position;
	private final double LARGEUR=2;
	private final double distance=2;
	private Image img=null;
	private double qtRot=0;
	
	private Ellipse2D.Double trou;
	
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
		}
		catch (IOException e) {
			System.out.println("Erreur pendant la lecture du fichier d'image");
		}
		
		
	}
	

	

	public double getLARGEUR() {
		return LARGEUR;
	}




	public double getDistance() {
		return distance;
	}




	@Override
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
		//double factPersoY = 0.5 / img.getHeight(null);
		//double factPersoX = 0.5 / img.getWidth(null);

		// redimenssionne le personnage
	//	matLocale.scale(factPersoX, factPersoY);

		//Deplacement du personnage a sa position initiale
		//matLocale.translate( (0.5) / factPersoX , (2-0.5) / factPersoY);
		
		double factX = (LARGEUR+distance*2)/ img.getWidth(null) ;
		double factY = (LARGEUR+distance*2)/ img.getHeight(null) ;matLocale.rotate(qtRot, position.getX()+LARGEUR/2, position.getY()+LARGEUR/2);
		matLocale.scale( factX, factY);
	
		matLocale.translate( (position.getX()-distance)   / factX , (position.getY() -distance) / factY);
		
		g.setColor(Color.LIGHT_GRAY);
		trou=new Ellipse2D.Double(position.getX()-distance, position.getY()-distance, LARGEUR+distance*2, LARGEUR+distance*2);
		g.fill(matLocal.createTransformedShape(trou));
		
		trou= new Ellipse2D.Double(position.getX(), position.getY(), LARGEUR, LARGEUR);
		g.setColor(Color.black);
		
		//g.drawImage(img, (int)position.getX(),(int) position.getY(), 60, 60, null, null);
		//System.out.println("chris"+ (int)position.getX());
		
		g.drawImage(img, matLocale, null);
		System.out.println("draw");
		
		
		
		
		g.fill(matLocal.createTransformedShape(trou));
		
		
	}
	
	public void savoirQuantiteRotation(double rotation) {
		this.qtRot=rotation;
	}
	
	/**
	 * Methode qui permet de calculer l'aire du trou noir
	 * @return, l'aire du trou noir sous forme d'area
	 */
	public Area getAireTrou() {
		 Ellipse2D aire= new Ellipse2D.Double(position.getX(), position.getY(), LARGEUR, LARGEUR);
		 return new Area(aire);
	}
	
	public Area getAireGrandTrou() {
		Ellipse2D aire = new Ellipse2D.Double(position.getX()-distance, position.getY()-distance, LARGEUR+distance*2, LARGEUR+distance*2);
		return new Area(aire);
	}


	//public Vecteur attirerLaser() {
		
	//}

	/**
	 * Cette méthode permet d'avoir accès à la valeur de la position
	 * @return le vecteur de la position du trou noir
	 // Arezki
	 */
	public Vecteur getPosition() {
		return position;
	}



	/**
	 * Cette méthode permet de modifier la valeur de la position du trou noir
	 * @param position est la nouvelle position en vecteur du trou noir dans les mesures du réel
	 //Arezki
	 */
	public void setPosition(Vecteur position) {
		this.position = position;
	}






}