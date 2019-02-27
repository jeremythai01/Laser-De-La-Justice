package aaplication;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import options.Options;

public class FenetreDemarrage extends JFrame {

	private JPanel contentPane;
	private Options fenetreOption;
	private App10LaserDeLaJustice jeu;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FenetreDemarrage frame = new FenetreDemarrage();
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
	public FenetreDemarrage() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 661, 509);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		
		JButton btnEditeurDeNiveau = new JButton("Editeur de niveau");
		btnEditeurDeNiveau.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnEditeurDeNiveau.setBounds(47, 101, 125, 45);
		contentPane.add(btnEditeurDeNiveau);
		
		JButton btnQuitter = new JButton("quitter");
		btnQuitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnQuitter.setBounds(473, 289, 125, 45);
		contentPane.add(btnQuitter);
		
		JButton btnOption = new JButton("option du jeu ");
		btnOption.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fenetreOption = new Options();
				fenetreOption.setVisible(true);
			}
		});
		btnOption.setBounds(473, 101, 125, 45);
		contentPane.add(btnOption);
		
		JButton button_2 = new JButton("Tutoriel et information");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		button_2.setBounds(47, 289, 125, 45);
		contentPane.add(button_2);
		
		JButton btnJouer = new JButton("jouer");
		btnJouer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jeu = new App10LaserDeLaJustice();
				jeu.setVisible(true);
				jeu.donneFocusALaScene();
				setVisible(false);
			}
		});
		btnJouer.setBounds(277, 173, 86, 85);
		contentPane.add(btnJouer);
		associerBoutonAvecImage(btnJouer, "play.png");
	
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
