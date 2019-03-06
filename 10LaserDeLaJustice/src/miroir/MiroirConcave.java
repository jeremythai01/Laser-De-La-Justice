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
import miora.MiroirPlan;
import physique.Laser;
/**
 * 
 * @author Arezki Issaadi
 *
 */
public class MiroirConcave extends JPanel implements Dessinable {

	private Vecteur positionIni;

	private double angleDebut;
	private double grosseurMiroire;

	private Arc2D miroirConcave;

	/**
	 * 
	 * @param position c'est la position du miroir en vecteur (x,y)
	 * @param grosseurMiroire c'est le paramètre qui permet d'ajuster la largeur du miroir.
	 */
	public MiroirConcave(Vecteur position, double grosseurMiroire) {
		positionIni = position;
		angleDebut = 100;
		this.grosseurMiroire = grosseurMiroire;

	}

	@Override
	/**
	 * @param hauteur c'est la hauteur du monde réel
	 * @param largeur c'est la largeur du monde réel 
	 * dessiner le miroir concave
	 */
	public void dessiner(Graphics2D g, AffineTransform mat, double hauteur, double largeur) {
		AffineTransform matLocal = new AffineTransform(mat);

		miroirConcave = new Arc2D.Double(positionIni.getX(), positionIni.getY(), grosseurMiroire, grosseurMiroire, -180,
				180, Arc2D.CHORD);
		// matLocal.rotate(90);
		// En rouge
		g.setColor(Color.red);
		// Demi cercle plein
		g.draw(matLocal.createTransformedShape(miroirConcave));

		// couleur noire

	}

	/**
	 * 
	 * @param laser 
	 * @param miroireAire c'est l'air du miroir concave;
	 * @param miroire mettre un miroir concave pour faire les calculs nécessaires
	 * @return un nouveau rayon (laser) qui est réflchi par le miroir
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
	
	
	public Vecteur collisionAvecMiroireLaser(Laser laser, Area miroireAire, MiroirConcave miroire) {
		
		
		if (!intersection(aire(), laser.getLaserAire())) {
			System.out.println("je touche");
			return calculNormal(laser, miroire);
		} else {
			System.out.println("XD");
			return new Vecteur();
		}

	
	}

	public Area aire() {
		return new Area(miroirConcave);
	}

	public Vecteur calculRayonIncidentInverse(Laser laser) {
		return new Vecteur(-laser.getPosition().getX(), -laser.getPosition().getY());

	}

	public Vecteur calculNormal(Laser laser, MiroirConcave miroire) {

		return new Vecteur(laser.getPosition().getX() - miroire.getPositionIni().getX(),
				laser.getPosition().getY() - miroire.getPositionIni().getY());

	}
	
	
	
	private boolean intersection(Area aire1, Area aire2) {
		Area aireInter = new Area(aire1);
		aireInter.intersect(aire2);
		if(!aireInter.isEmpty()) {
			return true;
		}
		return false;
	}

	
	public Vecteur getNormalPosition(Vecteur position) {
		return new Vecteur (positionIni.getX()-position.getX(), positionIni.getY()-position.getY());
		
	}

	public double getAngleDebut() {
		return angleDebut;
	}

	public Vecteur getPositionIni() {
		return positionIni;
	}

	public void setPositionIni(Vecteur positionIni) {
		this.positionIni = positionIni;
	}

	public void setAngleDebut(double angleDebut) {
		this.angleDebut = angleDebut;
	}

	public double getGrosseurMiroire() {
		return grosseurMiroire;
	}

	public void setGrosseurMiroire(double grosseurMiroire) {
		this.grosseurMiroire = grosseurMiroire;
	}

}
