package aaplication;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import options.Options;

/**
 * Cette fenetre est la première chose que l'utilisateur va voir.
 * Elle donne accès à l'éditeur de niveau, aux options, aux règles et à la documentation du jeu.
 * @author Arezki Issaadi
 * @author Miora R. Rakoto
 *
 */
public class App10LaserDeLaJustice extends JFrame {

	private JPanel contentPane;
	private Options fenetreOption;
	private FenetreJeu jeu;
	private FenetrePartie jouerPartie;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App10LaserDeLaJustice frame = new App10LaserDeLaJustice();
					frame.setVisible(true);
					//	creationFichier();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			//Par Miora
			/**
			 * Cette methode va creer un fichier pour le sauvegarde des niveaux personnalise et
			 * des parties sauvegardees
			 */

			private void creationFichier() {
				String direction = System.getProperty("user.dir");// + File.separator + "Desktop";
				direction += File.separator + "Laser de la justice";
				File customDir = new File(direction);
				if (customDir.exists()) {
					System.out.println(customDir + " already exists");
				} else if (customDir.mkdirs()) {
					System.out.println(customDir + " was created");
				} else {
					System.out.println(customDir + " was not created");
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public App10LaserDeLaJustice() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 661, 509);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);



		JButton btnEditeurDeNiveau = new JButton("Editeur de niveau");
		btnEditeurDeNiveau.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FenetreJeu fenetreJeu = new FenetreJeu(true, null);
				fenetreJeu.setVisible(true);
				setVisible(false);
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
				ouvrirOptions();
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
				nouvellePartie();
			}
		});
		btnJouer.setBounds(277, 173, 86, 85);
		contentPane.add(btnJouer);
		associerBoutonAvecImage(btnJouer, "play.png");

		//essaie delete sinon
		//Icon imgIcon = new ImageIcon(this.getClass().getResource("ajax-loader.gif"));
		/*JPanel panel = new JPanel();
		//frame.getContentPane().add(panel);

		panel.setBounds(0, 0, 645, 470);
		contentPane.add(panel);*/
		Icon imgIcon = new ImageIcon(this.getClass().getClassLoader().getResource("astronaute.gif"));
		JLabel label = new JLabel(imgIcon);
		label.setBounds(0, 0, 635, 459);
		//label.setBounds(getBounds());
		contentPane.add(label);

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

	//Par Miora
	/**
	 * Cette methode permet de savoir si il s'agit d'une nouvelle partie ou d'une partie charge
	 */
	private void nouvellePartie() {
		jouerPartie = new FenetrePartie();
		jouerPartie.setVisible(true);
		setVisible(false);
	}


	//Par Miora
	/**
	 * Cette methode permet d'ouvir la fenetre des options
	 */
	private void ouvrirOptions() {
		fenetreOption = new Options(true);
		fenetreOption.setVisible(true);
	}
}
