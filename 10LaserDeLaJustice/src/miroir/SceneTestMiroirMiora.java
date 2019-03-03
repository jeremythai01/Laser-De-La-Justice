package miroir;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import geometrie.Vecteur;
import physique.Laser;
import utilite.ModeleAffichage;

public class SceneTestMiroirMiora extends JPanel {
	private AffineTransform mat;
	private ModeleAffichage modele;
	private  MiroirPlan plan;
	private Laser laser;
	private double hauteur=0, largeur=0;

	/**
	 * Create the panel.
	 */
	public SceneTestMiroirMiora() {
		setBackground(Color.gray);
		plan = new MiroirPlan (1, 4,1); //epaisseur, x, y
		laser = new Laser (new Vecteur(3.8,2) , -45.0, (new Vecteur (2,2)) );
		
		

	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		modele = new ModeleAffichage(getWidth(),getHeight(),10);
		mat = modele.getMatMC();
		hauteur = modele.getHautUnitesReelles();
		largeur = modele.getLargUnitesReelles();
		plan.dessiner(g2d, mat,hauteur, largeur) ;
		laser.dessiner(g2d, mat, hauteur, largeur);
		Rectangle2D test = new Rectangle2D.Double(4,1, 2, 2);
		g2d.setColor(Color.CYAN);
		g2d.draw(mat.createTransformedShape(test));
		System.out.println(intersection( (new Area(test)), laser.getAireLaser()) );
		
		/*g2d.setColor(Color.yellow);
		g2d.draw(mat.createTransformedShape(plan.aireMiroir()));
		g2d.draw(mat.createTransformedShape(laser.getTrace()));
		System.out.println(intersection(mat.createTransformedShape(laser.getTrace()), mat.createTransformedShape(plan.getMiroirPlan()),g2d));
	//	collisionAvecMiroireLaser(laser, plan);
	  */
	}
	
	private boolean intersection(Area aire1, Area aire2) {
		Area aireInter = new Area(aire1);
		aireInter.intersect(aire2);
		if(!aireInter.isEmpty()) {
			return true;
		}
		return false;
	}
}
