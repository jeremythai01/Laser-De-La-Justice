package objets;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

import interfaces.Dessinable;
import utilite.ModeleAffichage;

/**
 * Classe qui permet la creation d'une echelle de mesure pour identifier la difference entre le monde reel et pixel
 * @author Arnaud
 *
 */
public class Echelle extends Obstacles implements Dessinable{
	private double largeur; 
	private Path2D.Double trace;
	private double posX, posY;
	private ModeleAffichage modele;
	private int hauteurPixels=0;
	private int largeurPixels=0;
	private double LARGEUR_DU_MONDE=0;
	
	/**
	 * Constructeur de l'echelle qui prend en parametre ou nous voulons dessiner l'echelle et la largeur du monde reel
	 * @param largeur, la largeur du monde reel 
	 * @param posX, la position en x ou sera dessiner l'echelle
	 * @param posY, la position en y ou sera dessiner l'echelle
	 */
	public Echelle(double largeur, double posX, double posY) {
		this.largeur=largeur;
		this.posX=posX;
		this.posY=posY;
	}

	@Override
	/**
	 * Permet de dessiner l'echelle selon le contexte graphique en parametre.
	 * @param g contexte graphique
	 * @param mat matrice de transformation monde-vers-composant
	 * @param hauteur hauteur du monde reelle
	 * @param largeur largeur du monde reelle
	 */
	public void dessiner(Graphics2D g, AffineTransform mat, double hauteur, double largeur) {
		AffineTransform matLocal = new AffineTransform(mat);
		g.setColor(Color.GREEN);
		
		double pixelsParMetre=largeur/this.largeur;
		
		trace = new Path2D.Double();
		trace.moveTo(posX, posY-0.5);
		trace.lineTo(posX, posY+0.5);
		trace.moveTo(posX, posY);
		
		/*for(int i=1;i<=7;i++) {// pour avoir 7 barres dans l'echelle
			trace.lineTo(posX+pixelsParMetre*i, posY);
			trace.lineTo(posX+pixelsParMetre*i, posY-1);
			trace.lineTo(posX+pixelsParMetre*1, posY+1);
			trace.moveTo(posX+pixelsParMetre*1, posY);
		}*/
		
		trace.lineTo(posX+pixelsParMetre, posY);
		trace.lineTo(posX+pixelsParMetre, posY-0.5);
		trace.lineTo(posX+pixelsParMetre, posY+0.5);
		trace.moveTo(posX+pixelsParMetre, posY);
		
		trace.lineTo(posX+pixelsParMetre*2, posY);
		trace.lineTo(posX+pixelsParMetre*2, posY-0.5);
		trace.lineTo(posX+pixelsParMetre*2, posY+0.5);
		trace.moveTo(posX+pixelsParMetre*2, posY);
		
		trace.lineTo(posX+pixelsParMetre*3, posY);
		trace.lineTo(posX+pixelsParMetre*3, posY-0.5);
		trace.lineTo(posX+pixelsParMetre*3, posY+0.5);
		trace.moveTo(posX+pixelsParMetre*3, posY);
		
		
		/*trace.lineTo(posX+pixelsParMetre*4, posY);
		trace.lineTo(posX+pixelsParMetre*4, posY-0.5);
		trace.lineTo(posX+pixelsParMetre*4, posY+0.5);
		trace.moveTo(posX+pixelsParMetre*4, posY);*/
		
		/*trace.lineTo(posX+pixelsParMetre*5, posY);
		trace.lineTo(posX+pixelsParMetre*5, posY-0.5);
		trace.lineTo(posX+pixelsParMetre*5, posY+0.5);
		trace.moveTo(posX+pixelsParMetre*5, posY);*/
		
		/*trace.lineTo(posX+pixelsParMetre*6, posY);
		trace.lineTo(posX+pixelsParMetre*6, posY-0.5);
		trace.lineTo(posX+pixelsParMetre*6, posY+0.5);
		trace.moveTo(posX+pixelsParMetre*6, posY);*/
		
		/*trace.lineTo(posX+pixelsParMetre*7, posY);
		trace.lineTo(posX+pixelsParMetre*7, posY-0.5);
		trace.lineTo(posX+pixelsParMetre*7, posY+0.5);
		trace.moveTo(posX+pixelsParMetre*7, posY);*/
		
		
		
		trace.closePath();
		modele= new ModeleAffichage(largeurPixels, hauteurPixels, LARGEUR_DU_MONDE);
		g.drawString("1m", (int)(posX*modele.getPixelsParUniteX()-1), (int)(modele.getPixelsParUniteY()*posY-10));
		g.draw(matLocal.createTransformedShape(trace));
		
		
	}
	
	/**
	 * Methode qui permet a lechelle de connaitre les donnees necessaires a la creation d'un modeledonnees pour pouvoir ecrire les lettres
	 * en unites reelles
	 * @param lARGEUR_DU_MONDE 
	 * @param hauteurPixels 
	 * @param largeurPixels 
	 */
	public void savoirModele(int largeurPixels, int hauteurPixels, double LARGEUR_DU_MONDE) {
		 this.largeurPixels=largeurPixels;
		 this.hauteurPixels=hauteurPixels;
		 this.LARGEUR_DU_MONDE=LARGEUR_DU_MONDE;
	}

}
