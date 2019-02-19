package aaplication;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import pistolet.Pistolet;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class App10LaserDeLaJustice extends JFrame {

	private JPanel contentPane;
	private JButton btnImage;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App10LaserDeLaJustice frame = new App10LaserDeLaJustice();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public App10LaserDeLaJustice() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1018, 728);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		Scene scene = new Scene();
		scene.setBackground(Color.WHITE);
		scene.setBounds(10, 110, 569, 552);
		contentPane.add(scene);
		
		Pistolet pistolet = new Pistolet();
		pistolet.setBounds(675, 213, 201, 265);
		contentPane.add(pistolet);
		
		JButton btnPlay = new JButton("play");
		btnPlay.addActionListener(new ActionListener() {
	
			public void actionPerformed(ActionEvent arg0) {
				scene.demarrer();
			}
		});
		btnPlay.setBounds(10, 45, 63, 54);
		contentPane.add(btnPlay);
		associerBoutonAvecImage(btnPlay, "play.png");
		
		JButton btnPause = new JButton("pause");
		btnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnPause.setBounds(83, 45, 63, 54);
		contentPane.add(btnPause);
		associerBoutonAvecImage(btnPause, "pause.jpg");
		
		btnImage = new JButton("image");
		btnImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnImage.setBounds(152, 45, 70, 54);
		contentPane.add(btnImage);
		associerBoutonAvecImage(btnImage, "rejouer.png");
		
		JButton btnTutoriel = new JButton("Tutoriel");
		btnTutoriel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FenetreTuto fenetreTuto= new FenetreTuto();
				
				fenetreTuto.setVisible(true);
			}
		});
		btnTutoriel.setBounds(437, 0, 89, 23);
		contentPane.add(btnTutoriel);
		
		JButton btnOption = new JButton("Option");
		btnOption.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnOption.setBounds(510, 45, 57, 54);
		contentPane.add(btnOption);
		associerBoutonAvecImage(btnOption, "reglage.jpg");
		
		JButton btnEditeur = new JButton("editeur");
		btnEditeur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnEditeur.setBounds(443, 45, 57, 54);
		contentPane.add(btnEditeur);
		associerBoutonAvecImage(btnEditeur, "editeur.png");
		
		JButton btnEnregistrer = new JButton("enregistrer");
		btnEnregistrer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnEnregistrer.setBounds(370, 45, 63, 54);
		contentPane.add(btnEnregistrer);
		associerBoutonAvecImage(btnEnregistrer, "enregistrer.png");
	}

	/**
	 * @param leBouton
	 * @param fichierImage
	 */
	//par Caroline Houle

	private void associerBoutonAvecImage(JButton leBouton, String fichierImage) {
		Image imgLue = null;
		java.net.URL urlImage = getClass().getClassLoader().getResource(fichierImage);
		if (urlImage == null) {
			JOptionPane.showMessageDialog(null, "Fichier " + fichierImage + " introuvable");
		}
		try {
			imgLue = ImageIO.read(urlImage);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Erreur pendant la lecture du fichier d'image");
		}

		// redimensionner l'image de la meme grandeur que le bouton
		Image imgRedim = imgLue.getScaledInstance(leBouton.getWidth(), leBouton.getHeight(), Image.SCALE_SMOOTH);

		// au cas ou le fond de limage serait transparent
		leBouton.setOpaque(false);
		leBouton.setContentAreaFilled(false);
		leBouton.setBorderPainted(false);

		// associer l'image au bouton
		leBouton.setText("");
		leBouton.setIcon(new ImageIcon(imgRedim));

		// on se debarrasse des images intermediaires
		imgLue.flush();
		imgRedim.flush();
	}
}
