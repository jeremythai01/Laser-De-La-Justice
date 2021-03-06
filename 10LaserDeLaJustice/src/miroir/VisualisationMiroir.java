package miroir;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;

import geometrie.Vecteur;
import utilite.ModeleAffichage;

/**
 * Cette classe permet de visualiser les miroirs avant de les dessiner
 * @author Miora
 *
 */
public class VisualisationMiroir extends JPanel {

	private int angle=0, longueur=2;
	private boolean dessinerMiroirPlan = false , dessinnerMiroirCourbe = false;

	/**
	 * Creation du JPanel
	 */
	public VisualisationMiroir() {
		setBackground(Color.yellow);
	}

	/**
	 * Cette methode permet de dessiner le composant de dessin
	 * @param g : le contexte graphique
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;	
		ModeleAffichage modele = new ModeleAffichage(getWidth(),getHeight(),25);
		AffineTransform mat = modele.getMatMC();
		g2d.setColor(Color.red);
		MiroirPlan miroirPlan = new MiroirPlan(new Vecteur (modele.getLargUnitesReelles()/2,modele.getHautUnitesReelles()/2), angle, longueur);
		MiroirCourbe miroirCourbe = new MiroirCourbe (new Vecteur (modele.getLargUnitesReelles()/2,modele.getLargUnitesReelles()/2), longueur/2, angle);
		if(dessinerMiroirPlan) {
			miroirPlan.dessiner(g2d, mat, 0, 0);
		}
		if(dessinnerMiroirCourbe) {
			miroirCourbe.dessiner(g2d,mat,0,0);
		}
	}

	/**
	 * Cette methode permet de modifier l'angle des miroirs 
	 * @param angle : le nouvel angle des miroirs
	 */
	public void setAngle(int angle) {
		this.angle = angle;
		repaint();
	}


	/**
	 * Cette methode permet de dessiner un miroir plan
	 */
	public void dessinerMiroirPlan() {
		this.dessinerMiroirPlan = true;
		this.dessinnerMiroirCourbe = false;
		repaint();
	}

	/**
	 * Cette methode permet de dessiner un miroir courbe
	 */
	public void dessinerMiroirCourbe() {
		this.dessinnerMiroirCourbe = true;
		this.dessinerMiroirPlan = false;
		repaint();
	}

	/**
	 * * Cette methode permet de modifier la longueur des miroirs 
	 * @param angle : la nouvelle longueur
	 */
	public void setLongueur(int longueur) {
		this.longueur = longueur;
		repaint();
	}



}
