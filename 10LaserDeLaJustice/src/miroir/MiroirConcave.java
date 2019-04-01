package miroir;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Path2D;
import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import geometrie.Vecteur;
import interfaces.Dessinable;
import physique.Laser;
/**
 * 
 * @author Arezki Issaadi
 *
 */
public class MiroirConcave extends JPanel implements Dessinable {

	private Vecteur position;

	private double angleDebut;
	private double grosseurMiroire;

	private Arc2D miroirConcave;

	/**
	 * Constructeur du miroir concave
	 * @param position c'est la position du miroir en vecteur (x,y). (mesures r�elle)
	 * @param grosseurMiroire c'est le param�tre qui permet d'ajuster la largeur du miroir.(mesures r�elle)
	 */
	public MiroirConcave(Vecteur position, double grosseurMiroire) {
		this.position = position;
		angleDebut = 90;
		this.grosseurMiroire = grosseurMiroire;

	}

	@Override
	/**
	  * Elle permet d'alleger le paintComponent dans la scene principale.
	 * @param g: c'est le param�tre Graphics2D qui permettra de dessiner les objets
	 * @param mat: c'est la matrice qui permettra de transformer le monde pixel en monde monde r�el
	 * @param hauteur: c'est la hauteur en r�el de la scene
	 * @param largeur: C'est la largeure en r�el de la scene
	 */
	public void dessiner(Graphics2D g, AffineTransform mat, double hauteur, double largeur) {
		AffineTransform matLocal = new AffineTransform(mat);

		miroirConcave = new Arc2D.Double(position.getX(), position.getY(), grosseurMiroire, grosseurMiroire, 180,
				-180, Arc2D.OPEN);
		//matLocal.rotate(angleDebut, position.getX(), position.getY());
		// En rouge
		g.setColor(Color.red);
		// Demi cercle plein
		g.draw(matLocal.createTransformedShape(miroirConcave));

		// couleur noire

	}

	/**
	 * 
	 * @param laser mettre le laser qui va entrer en collision avec le miroir 
	 * @param miroireAire c'est l'air du miroir concave;
	 * @param miroire mettre un miroir concave pour faire les calculs n�cessaires
	 * @return un nouveau rayon (laser) qui est r�flchi par le miroir
	 * @throws Exception 
	 */
	public Laser calculRayonReflechi(Laser laser, Area miroireAire, MiroirConcave miroire) throws Exception {

		Vecteur rayonIncident = laser.getPosition();
		Vecteur normal = collisionAvecMiroireLaser(laser, miroireAire, miroire);
		//Vecteur rayonIncidentInverse = calculRayonIncidentInverse(laser);
		Vecteur rayonReflechi;
		Laser nouveauLaser;

		// Pas oublier de normaliser ...
		try {
			normal = normal.normalise();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Ancienne version.
		//rayonReflechi = rayonIncident.additionne(normal.multiplie(rayonIncidentInverse.multiplie(2).prodScalaire(normal.multiplie(2))));
		
		// Nouvelle version.
		rayonReflechi = rayonIncident.additionne( normal.multiplie(2.0).multiplie(rayonIncident.multiplie(-1.0).prodScalaire(normal)));
				
		nouveauLaser = new Laser(rayonReflechi, -laser.getAngleTir(), laser.getVitesse());

		return nouveauLaser;
	}
	
	/**
	 * Cette m�thode calculun vecteur de position de la collision entre le laser et le miroir (en r�el)
	   @param laser: mettre le laser qui va entrer en collision avec le miroir
	 * @param miroireAire c'est l'air du miroir concave;
	 * @param miroire mettre un miroir concave pour faire les calculs n�cessaires
	 * @return un vecteur de position 
	 */
	public Vecteur collisionAvecMiroireLaser(Laser laser, Area miroireAire, MiroirConcave miroire) {
		
		
		if (!intersection(aire(), laser.getLaserAire())) {
			//System.out.println("je touche");
			return calculNormal(laser, miroire);
		} else {
		//	System.out.println("XD");
			return new Vecteur();
		}

	
	}
/**
 * Cette m�thode retourne une aire du miroir concave
 * @return une aire (Area)
 */
	public Area aire() {
		return new Area(miroirConcave);
	}
	/**
	
	 * @param laser mettre le laser qui va entrer en collision avec le miroir
	 * @return un vecteur qui a la direction oppos� du laser 
	 */
	public Vecteur calculRayonIncidentInverse(Laser laser) {
		return new Vecteur(-laser.getPosition().getX(), -laser.getPosition().getY());

	}
/**
 * Cette m�tjode calcule un vecteur de la normal entre le laser et le miroir concave (en r�el)
 * @param laser: mettre le laser qui va entrer en collision avec le miroir
 * @param miroire mettre un miroir concave qui va entrer en collision avec le laser et pour faire les calculs n�cessaires
 * @return un vecteur de la normal 
 */
	public Vecteur calculNormal(Laser laser, MiroirConcave miroire) {

		return new Vecteur(laser.getPosition().getX() - miroire.getPosition().getX(),
				laser.getPosition().getY() - miroire.getPosition().getY());

	}
	
	
	/**
	 * @author Miora
	 * @param aire1 : aire de la premiere geometrie
	 * @param aire2 : aire de la deuxieme geometrie
	 * @return si il y'a une intersection entre 2 aires 
	 */
	private boolean intersection(Area aire1, Area aire2) {
		Area aireInter = new Area(aire1);
		aireInter.intersect(aire2);
		if(!aireInter.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * @author Miora
	 * @param laser mettre le laser qui va entrer en collision avec le miroir
	 * @return un vecteur de la normal entre le laser et le miroir
	 */
	public Vecteur getNormalPosition(Vecteur position) {
		return new Vecteur (position.getX()-position.getX(), position.getY()-position.getY());
		
	}
	/**
	 * Cette m�thode retourne le parametre angle de debut du miroir concave en degr�s
	 * @return l'angle de d�but du miroir concave en degr�s
	 */
	public double getAngleDebut() {
		return angleDebut;
	}
	/**
	 * Cette m�thode retourne la position initiale du miroir concave en vecteur dans les mesures du r�el
	 * @return la position initiale du miroir concave 
	 */
	public Vecteur getPosition() {
		return position;
	}
	/**
	 * methode qui permet de modifier la position du miroir dans les mesures du r�el
	 * @param position nouvelle position du miroire concave 
	 */
	public void setPosition(Vecteur position) {
		this.position = position;
	}
	/**
	 * Permet de modifier l'angle du miroir en degr�s. Elle sera utile lors des calculs pour trouver le rayon reflechi
	 * @param angleDebut nouveau angle du miroir 
	 */
	public void setAngleDebut(double angleDebut) {
		this.angleDebut = angleDebut;
	}

	/**
	 * Cette m�thode retourne la grosseur du miroire en double dans les mesures r�elle
	 * @return la grosseur du miroir 
	 */
	public double getGrosseurMiroire() {
		return grosseurMiroire;
	}
	/**
	 * Permet de modifier la grosseur du miroir avec les mesures du r�el 
	 * @param grosseurMiroire nouvelle grosseur du miroir 
	 */
	public void setGrosseurMiroire(double grosseurMiroire) {
		this.grosseurMiroire = grosseurMiroire;
	}

	public Area getAireMiroirConcave() {
		return new Area (miroirConcave);
	}
}
