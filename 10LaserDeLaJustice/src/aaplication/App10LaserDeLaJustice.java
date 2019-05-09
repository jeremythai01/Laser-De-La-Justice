package aaplication; 

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import options.Options;
import son.Bruit;

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
	private Bruit son = new Bruit();
	private Image imgFond;
	private JLabel lblNewLabel;

	/**
	 * Lancer l'appplication
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
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
	 * Creation de la fenetre
	 */
	public App10LaserDeLaJustice() {
		setResizable(false);
		setTitle("Laser de la justice");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 662, 501);
		Dimension ecranDimension = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(ecranDimension.width/2-getSize().width/2, ecranDimension.height/2-getSize().height/2);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);



		JButton btnEditeurDeNiveau = new JButton("Editeur de niveau");
		btnEditeurDeNiveau.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				son.joue("beep");
				FenetreEditeurNiveau fenetreEditeur = new FenetreEditeurNiveau();
				fenetreEditeur.setVisible(true);
				setVisible(false);
			}
		});
		btnEditeurDeNiveau.setBounds(47, 101, 175, 45);
		contentPane.add(btnEditeurDeNiveau);

		JButton btnQuitter = new JButton("Quitter");
		btnQuitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				son.joue("leave");
				setVisible(false);
			}
		});
		btnQuitter.setBounds(413, 289, 175, 45);
		contentPane.add(btnQuitter);

		JButton btnOption = new JButton("Option du jeu ");
		btnOption.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				son.joue("beep");
				try {
					ouvrirOptions();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnOption.setBounds(413, 101, 175, 45);
		contentPane.add(btnOption);

		JButton button_2 = new JButton("Tutoriel et information");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				son.joue("beep");
				FenetreTuto fenetreTuto = new FenetreTuto();
				fenetreTuto.setVisible(true);
				setVisible(false);
			}
		});
		button_2.setBounds(47, 289, 175, 45);
		contentPane.add(button_2);

		JButton btnJouer = new JButton("jouer");
		btnJouer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				son.joue("beep");
				nouvellePartie();
			}
		});
		btnJouer.setBounds(277, 173, 86, 85);
		contentPane.add(btnJouer);
		associerBoutonAvecImage(btnJouer, "play.png");

		Icon imgIcon = new ImageIcon(this.getClass().getClassLoader().getResource("astronaute.gif"));
		lblNewLabel = new JLabel(imgIcon);
		lblNewLabel.setBounds(0, 0, 646, 462);
		contentPane.add(lblNewLabel);

		//essaie delete sinon
		//Icon imgIcon = new ImageIcon(this.getClass().getResource("ajax-loader.gif"));
		/*JPanel panel = new JPanel();
		//frame.getContentPane().add(panel);

		panel.setBounds(0, 0, 645, 470);
		contentPane.add(panel);*/

		/*
		 JPanel panelAvecImage = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);

				imgFond  = Toolkit.getDefaultToolkit().createImage(getClass().getClassLoader().getResource("astronaute.gif"));
				Graphics2D g2d = (Graphics2D) g; 
				g2d.drawImage(imgFond,0,0 ,null);
			}
		};
		setContentPane(panelAvecImage);
		 */	

		/*
	Icon imgIcon = new ImageIcon(this.getClass().getClassLoader().getResource("astronaute.gif"));
	JLabel label = new JLabel(imgIcon);
	label.setBounds(112, 46, 635, 459);
	label.setBounds(getBounds());
	contentPane.add(label);
		 */

	}


	/**Cette methode permet d'associer un bouton a une image
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

		creationNiveau();
	}

	//Par Miora
	/**
	 * Cette methode permet de creer un dossier qui va sauvegarder les niveaux dès l'initialisation
	 */
	private void creationNiveau() {
		String direction = System.getProperty("user.home") + "/Desktop"+ "/10LaserDeLaJustice";
		direction += "/Niveau" ;
		File customDir = new File(direction);
		customDir.mkdirs();
		
		InputStream in;  

		for(int i = 1; i<=5; i++) {
			in = getClass().getResourceAsStream("/niveau" + i + ".niv"); 
			Path sort = Paths.get(customDir.getAbsolutePath() + "/niveau" + i + ".niv" );
			try {
				Files.copy(in, sort, REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	//Par Miora
	/**
	 * Cette methode permet de savoir si il s'agit d'une nouvelle partie ou d'une partie charge
	 */
	private void nouvellePartie() {
		jouerPartie = new FenetrePartie();
		String direction =  System.getProperty("user.home") + "/Desktop"+ "/10LaserDeLaJustice";
		File customDir = new File(direction);
		if (customDir.exists()) {
		} else if (customDir.mkdirs()) {
		} else {
		}

		jouerPartie.setVisible(true);
		setVisible(false);
	}


	//Par Miora
	/**
	 * Cette methode permet d'ouvir la fenetre des options
	 * @throws IOException lecture possible du fichier
	 * @throws FileNotFoundException fichier non trouve
	 */
	private void ouvrirOptions() throws FileNotFoundException, IOException {
		fenetreOption = new Options(false);
		fenetreOption.setVisible(true);
	}

}
