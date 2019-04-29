package aaplication;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import utilite.ModeleAffichage;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
/**
 * Cette classe gère les instructions du jeu
 * 
 * @author Arezki
 */
public class SceneInstructionJeu extends JPanel {


	private int page = 1;

	private Image fond = null;

	private double LARGEUR_DU_MONDE = 100;

	private double scale = 1.00;

	private double x = 1;
	private double y = 1;

	ModeleAffichage modele;

	private static final long serialVersionUID = 1L;

	public SceneInstructionJeu() {
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				if(arg0.getWheelRotation()>0) {
					scale = scale-0.01;
					repaint();
				}else if(arg0.getWheelRotation()<0) {
					scale +=0.01;
					repaint();
				}

			}
		});
		setLayout(null);
		lireFond();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;

		modele = new ModeleAffichage(getWidth(), getHeight(), LARGEUR_DU_MONDE);
		lireFond();
		g2d.scale(scale, scale);
		g2d.translate(x, y);
		g2d.drawImage(fond, 0, 0, (int) modele.getLargPixels(), (int) modele.getHautPixels(), null);

	}

	public void lireFond() {

		if (page == 1) {

			URL fich = getClass().getClassLoader().getResource("page1.JPG");
			if (fich == null) {
				JOptionPane.showMessageDialog(null, "Fichier introuvable!");
			} else {
				try {
					fond = ImageIO.read(fich);
				} catch (IOException e) {
					System.out.println("Erreur de lecture du fichier d'image");
				}
			}

		}
		if (page == 2) {

			URL fich = getClass().getClassLoader().getResource("page2.JPG");
			if (fich == null) {
				JOptionPane.showMessageDialog(null, "Fichier introuvable!");
			} else {
				try {
					fond = ImageIO.read(fich);
				} catch (IOException e) {
					System.out.println("Erreur de lecture du fichier d'image");
				}
			}

		}
		if (page == 3) {
			URL fich = getClass().getClassLoader().getResource("page3.JPG");
			if (fich == null) {
				JOptionPane.showMessageDialog(null, "Fichier introuvable!");
			} else {
				try {
					fond = ImageIO.read(fich);
				} catch (IOException e) {
					System.out.println("Erreur de lecture du fichier d'image");
				}
			}
		}
		if (page == 4) {
			URL fich = getClass().getClassLoader().getResource("page4.JPG");
			if (fich == null) {
				JOptionPane.showMessageDialog(null, "Fichier introuvable!");
			} else {
				try {
					fond = ImageIO.read(fich);
				} catch (IOException e) {
					System.out.println("Erreur de lecture du fichier d'image");
				}
			}
		}
		if (page == 5) {

			URL fich = getClass().getClassLoader().getResource("page5.JPG");
			if (fich == null) {
				JOptionPane.showMessageDialog(null, "Fichier introuvable!");
			} else {
				try {
					fond = ImageIO.read(fich);
				} catch (IOException e) {
					System.out.println("Erreur de lecture du fichier d'image");
				}

			}
		}
	}




	public void augmenterPage() {
		page++;
		repaint();
	}

	public void baisserPage() {
		page--;

		repaint();
	}

	public void setX(double valeur) {
		x = valeur;
		repaint();
	}

	public void setY(double valeur) {
		y = valeur;
		repaint();
	}

	public void defaut() {

		x = 1;
		y = 1;
		scale = 1;

		repaint();
	}

}
