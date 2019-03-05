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
	private Vecteur P;
	private Vecteur Q;

	private double angleDebut;
	private double grosseurMiroire;

	private Arc2D miroirConcave;

	public MiroirConcave(Vecteur position) {
		positionIni = position;
		angleDebut = 150;
		grosseurMiroire = 150;

	}

	@Override
	public void dessiner(Graphics2D g, AffineTransform mat, double hauteur, double largeur) {
		AffineTransform matLocal = new AffineTransform(mat);

		miroirConcave = new Arc2D.Double(positionIni.getX(), positionIni.getY(), 5, 5, getAngleDebut(),
				getGrosseurMiroire(), Arc2D.CHORD);
		// matLocal.rotate(90);
		// En rouge
		g.setColor(Color.red);
		// Demi cercle plein
		g.draw(matLocal.createTransformedShape(miroirConcave));

		// couleur noire

	}

	public Laser calculRayonReflechi(Laser laser, Area miroireAire, MiroirConcave miroire) {

		Vecteur rayonIncident = laser.getPosition();
		Vecteur normal = collisionAvecMiroireLaser(laser, miroireAire, miroire);
		Vecteur rayonIncidentInverse = calculRayonIncidentInverse(laser);
		Vecteur rayonReflechi;
		Laser nouveauLaser;

		rayonReflechi = rayonIncident.additionne(normal.multiplie(rayonIncidentInverse.multiplie(2).prodScalaire(normal.multiplie(2))));
		nouveauLaser = new Laser(rayonReflechi, -laser.getAngleTir(), laser.getVitesse());

		return nouveauLaser;
	}

	
	
	public Vecteur collisionAvecMiroireLaser(Laser laser, Area miroireAire, MiroirConcave miroire) {
		Area laserAire = new Area(laser.getLaserAire());
		
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
