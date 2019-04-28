package objets;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

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
public class BlocDEau extends Obstacles implements Dessinable, Serializable {
	
	private static final long serialVersionUID = 1L;
	private Vecteur position;
	private final int LARGEUR=2;
	private Rectangle2D.Double bloc;
	private boolean premiereCollision=true;
	private double indiceRefraction;
	private final double indiceAir=1.0;
	private Rectangle2D.Double hitbox;
	private final double largeurHitbox=0.0000001;
	private double hauteur=0.5;
	
	/**
	 * Constructeur du bloc deau qui prend en parametre la position du bloc
	 * @param position, la position du bloc
	 */
	public BlocDEau(Vecteur position, double indiceRefraction) {
		this.position=position;
		this.indiceRefraction=indiceRefraction;
	}
	

	@Override
	/**
	 * Permet de dessiner le bloc selon le contexte graphique en parametre.
	 * @param g2d contexte graphique
	 * @param mat matrice de transformation monde-vers-composant
	 * @param hauteur hauteur du monde reelle
	 * @param largeur largeur du monde reelle
	 */
	public void dessiner(Graphics2D g, AffineTransform mat, double hauteur, double largeur) {
		AffineTransform matLocal = new AffineTransform(mat);
		bloc= new Rectangle2D.Double(position.getX(), position.getY(), LARGEUR, this.hauteur);
		//hitbox= new Rectangle2D.Double(position.getX(), position.getY()+LARGEUR, LARGEUR, largeurHitbox);
		g.setColor(Color.blue);
		g.fill(matLocal.createTransformedShape(bloc));
		
	}
	
	/**
	 * Methode qui permet d'effectuer le calcul de la refraction
	 * @param v, le vecteur orientation du vecteur incident
	 * @param N, le vecteur de la normale du bloc
	 * @param n1,  Indice de réfraction du milieu incident.
	 * @param n2  Indice de réfraction du milieu refracte.
	 * @return, le nouveau vecteur T,  l`orientation du rayon réfracté
	 */
	public Vecteur refraction(Vecteur v, Vecteur N) {
		Vecteur vecteur= new Vecteur();
		double n= indiceAir/indiceRefraction;
		Vecteur E = new Vecteur();
		E=v.multiplie(-1);
		System.out.println("la normale est"+ N);
		
		// QUE FAIRE ICI !!!!! %???? /"%?"%?/"%?$ %
		if(((1-((n*n)*(1-(E.prodScalaire(N)*(E.prodScalaire(N))))))) < 0)
		throw new RuntimeException("OUPS !!!");
		double resultat=1-((n*n)*1-E.prodScalaire(N)*(E.prodScalaire(N)));
		//System.out.println("dsfsa"+resultat);
		vecteur = v.multiplie(n).additionne(N.multiplie(((E.prodScalaire(N)*n)-Math.sqrt(1-((n*n)*1-E.prodScalaire(N)*(E.prodScalaire(N)))))));
		return new Vecteur(-vecteur.getX(),-vecteur.getY());
		//return vecteur.multiplie(-1);
	}


	/**
	 * Methode qui permet d'effectuer le calcul de la normale
	 * @param laser, le laser qui entre en contact
	 * @param bloc, le bloc avec lequel le laser entre en contact
	 * @return, le vecteur normal du bloc
	 */
	public Vecteur calculNormal(Laser laser, BlocDEau bloc) {

		return new Vecteur(laser.getPositionHaut().getX() - bloc.getPosition().getX(),
				laser.getPositionHaut().getY() - bloc.getPosition().getY());

		
	}

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

	/**
	 * Retourne la position
	 * @return, la position en vecteur
	 */
	public Vecteur getPosition() {
		return position;
	}


	/**
	 * Modifie la position du bloc
	 * @param position, la position du bloc qu'on veut modifier
	 */
	public void setPosition(Vecteur position) {
		this.position = position;
	}


	/**
	 * Permet d'obtenir une copie du bloc
	 * @return, un bloc qui est une copie du bloc
	 */
	public Rectangle2D.Double getBloc() {
		return bloc;
	}

	/**
	 * Permet de modifier le bloc
	 * @param bloc, le nouveau bloc desire
	 */
	public void setBloc(Rectangle2D.Double bloc) {
		this.bloc = bloc;
	}


	/**
	 * Permet d'obtenir la largeur du bloc 
	 * @return la largeur du bloc
	 */
	public int getLARGEUR() {
		return LARGEUR;
	}

	/**
	 * Permet d'obtenir la hauteur du bloc 
	 * @return la hauteur du bloc
	 */
	public double getHauteur() {
		return hauteur;
	}

	/**
	 * Permet d'obtenir l'aire du bloc pour faire les intersections
	 * @return, l'aire du bloc sous forme d'area
	 */
	public Area getAireBloc() {
		 Rectangle2D.Double aire= new Rectangle2D.Double(position.getX(), position.getY(), LARGEUR, this.hauteur);
		 return new Area(aire);
	}


	/**
	 * Methode qui permet de savoir si c'est la premiere collision du bloc
	 * @return premiereCollision, si c'est la premiere
	 */
	public boolean isPremiereCollision() {
		return premiereCollision;
	}


	/**
	 * Methode qui indique au bloc si la premiere collision est arrivee
	 * @param premiereCollision, la valeur de la premiere collision
	 */
	public void setPremiereCollision(boolean premiereCollision) {
		this.premiereCollision = premiereCollision;
	}
	
	
	
	

}
