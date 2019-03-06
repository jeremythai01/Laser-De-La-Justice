package aaplication;

import java.awt.Color;

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

import options.Editeur;
import options.Options;
import javax.swing.JSpinner;
import javax.swing.JLabel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.SpinnerNumberModel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.JSeparator;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.Font;
/**
 * 
 * @author Arezki
 *
 */
public class App10LaserDeLaJustice extends JFrame {

	private JPanel contentPane;
	private JButton btnImage;
	private JButton btnPrisme;
	private JButton btnPetiteBalle;
	private JButton btnMediumBalle;
	private JButton btnGrosseBalle;
	private JButton btnBlocDeau;
	private JButton btnTrouNoir;
	private JButton btnMiroirConcave;
	private JButton btnMiroirConvexe;
	private JButton btnMiroirPlan;
	private JLabel lblValeur;
	private Scene scene;
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					 App10LaserDeLaJustice frame;
					frame = new App10LaserDeLaJustice();
					frame.setVisible(true);
					frame.scene.requestFocusInWindow();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Creation de la fenêtre 
	 */
	public App10LaserDeLaJustice() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1389, 1034);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.menu);
		contentPane.setForeground(Color.DARK_GRAY);
		contentPane.setBounds(new Rectangle(2, 0, 0, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		JButton btnPlay = new JButton("play");
		btnPlay.addActionListener(new ActionListener() {
	
			public void actionPerformed(ActionEvent arg0) {
				scene.demarrer();
				donneFocusALaScene();
			}
		});
		btnPlay.setBounds(44, 61, 40, 38);
		contentPane.add(btnPlay);
		associerBoutonAvecImage(btnPlay, "play.png");
		
		JButton btnPause = new JButton("pause");
		btnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scene.arreter();
			}
		});
		btnPause.setBounds(94, 61, 40, 38);
		contentPane.add(btnPause);
		associerBoutonAvecImage(btnPause, "pause.jpg");
		
		btnImage = new JButton("image");
		btnImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnImage.setBounds(144, 61, 40, 38);
		contentPane.add(btnImage);
		associerBoutonAvecImage(btnImage, "rejouer.png");
		
		JButton btnTutoriel = new JButton("Tutoriel");
		btnTutoriel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FenetreTuto fenetreTuto= new FenetreTuto();
				
				fenetreTuto.setVisible(true);
			}
		});
		btnTutoriel.setBounds(609, 0, 89, 23);
		contentPane.add(btnTutoriel);
		
		JButton btnOption = new JButton("Option");
		btnOption.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Options fenetreOpt = new Options();
				fenetreOpt.setVisible(true);
			}
		});
		btnOption.setBounds(1281, 61, 40, 38);
		contentPane.add(btnOption);
		associerBoutonAvecImage(btnOption, "reglage.png");
		
		JButton btnEditeur = new JButton("editeur");
		btnEditeur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//editeur.setVisible(true);
				scene.requestFocusInWindow();
				scene.arreter();
				activerEditeur();
				scene.ActiverEditeur();
			}
		});
		btnEditeur.setBounds(1231, 61, 40, 38);
		contentPane.add(btnEditeur);
		associerBoutonAvecImage(btnEditeur, "editeur.png");
		
		JButton btnEnregistrer = new JButton("enregistrer");
		btnEnregistrer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			desactiverEditeur();
			scene.DesactiverEditeur();
			donneFocusALaScene();
			}
		});
		btnEnregistrer.setBounds(1181, 61, 40, 38);
		contentPane.add(btnEnregistrer);
		associerBoutonAvecImage(btnEnregistrer, "enregistrer.png");
		
		btnMiroirPlan = new JButton("miroir Plan");
		btnMiroirPlan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scene.ajoutMiroirPlan();
				donneFocusALaScene();
			}
		});
		btnMiroirPlan.setEnabled(false);
		btnMiroirPlan.setBounds(1443, 107, 105, 23);
		contentPane.add(btnMiroirPlan);
		
		btnMiroirConvexe = new JButton("miroir Convexe");
		btnMiroirConvexe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scene.ajoutMiroireConvexe();
				donneFocusALaScene();
			}
		});
		btnMiroirConvexe.setEnabled(false);
		btnMiroirConvexe.setBounds(1443, 175, 105, 23);
		contentPane.add(btnMiroirConvexe);
		
		btnMiroirConcave = new JButton("miroir Concave");
		btnMiroirConcave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scene.ajoutMiroireConcave();
				donneFocusALaScene();
			}
		});
		btnMiroirConcave.setEnabled(false);
		btnMiroirConcave.setBounds(1443, 255, 105, 23);
		contentPane.add(btnMiroirConcave);
		
		btnTrouNoir = new JButton("Trou Noir");
		btnTrouNoir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scene.ajoutTrouNoir();
				donneFocusALaScene();
			}
		});
		btnTrouNoir.setEnabled(false);
		btnTrouNoir.setBounds(1443, 335, 105, 23);
		contentPane.add(btnTrouNoir);
		
		btnBlocDeau = new JButton("bloc D'eau ");
		btnBlocDeau.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scene.ajoutBlocEau();
				donneFocusALaScene();
			}
		});
		btnBlocDeau.setEnabled(false);
		btnBlocDeau.setBounds(1443, 418, 105, 23);
		contentPane.add(btnBlocDeau);
		
		btnGrosseBalle = new JButton("Grosse Balle");
		btnGrosseBalle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scene.ajoutBalleGrosse();
			}
		});
		btnGrosseBalle.setEnabled(false);
		btnGrosseBalle.setBounds(1443, 509, 105, 23);
		contentPane.add(btnGrosseBalle);
		
		btnMediumBalle = new JButton("Medium Balle");
		btnMediumBalle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scene.ajoutBalleMedium();
				donneFocusALaScene();
			}
		});
		btnMediumBalle.setEnabled(false);
		btnMediumBalle.setBounds(1443, 600, 105, 23);
		contentPane.add(btnMediumBalle);
		
		btnPetiteBalle = new JButton("petite balle ");
		btnPetiteBalle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				scene.ajoutBallePetite();
				donneFocusALaScene();
			}
		});
		btnPetiteBalle.setEnabled(false);
		btnPetiteBalle.setBounds(1443, 702, 105, 23);
		contentPane.add(btnPetiteBalle);
		
		btnPrisme = new JButton("prisme");
		btnPrisme.setEnabled(false);
		btnPrisme.setBounds(1443, 811, 105, 23);
		contentPane.add(btnPrisme);
		
		JSlider sldAngle = new JSlider();
		sldAngle.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				//lblValeur.setText(""+(int)sldAngle.getValue());
			}
		});
		sldAngle.setFocusable(false);
		sldAngle.setMaximum(180);
		sldAngle.setBounds(1444, 909, 200, 26);
		contentPane.add(sldAngle);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.BLACK);
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(1380, 0, 9, 997);
		contentPane.add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(Color.DARK_GRAY);
		separator_1.setBounds(30, 107, 1303, 2);
		contentPane.add(separator_1);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setForeground(Color.DARK_GRAY);
		separator_2.setOrientation(SwingConstants.VERTICAL);
		separator_2.setBounds(30, 107, 9, 727);
		contentPane.add(separator_2);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setForeground(Color.DARK_GRAY);
		separator_3.setBounds(30, 832, 1303, 2);
		contentPane.add(separator_3);
		
		JSeparator separator_4 = new JSeparator();
		separator_4.setOrientation(SwingConstants.VERTICAL);
		separator_4.setForeground(Color.DARK_GRAY);
		separator_4.setBounds(1331, 107, 9, 727);
		contentPane.add(separator_4);
		
		JLabel lblMiroirPlan = new JLabel("Miroir Plan");
		lblMiroirPlan.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblMiroirPlan.setBounds(1581, 111, 117, 14);
		contentPane.add(lblMiroirPlan);
		
		JLabel lblMiroirConvexe = new JLabel("Miroir Convexe");
		lblMiroirConvexe.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblMiroirConvexe.setBounds(1581, 184, 117, 14);
		contentPane.add(lblMiroirConvexe);
		
		JLabel lblMiroirConcave = new JLabel("Miroir Concave");
		lblMiroirConcave.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblMiroirConcave.setBounds(1581, 259, 117, 14);
		contentPane.add(lblMiroirConcave);
		
		JLabel lblTrouNoir = new JLabel("Trou Noir");
		lblTrouNoir.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblTrouNoir.setBounds(1581, 339, 117, 14);
		contentPane.add(lblTrouNoir);
		
		JLabel lblBlocDeau = new JLabel("Bloc d'eau ");
		lblBlocDeau.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblBlocDeau.setBounds(1581, 422, 117, 14);
		contentPane.add(lblBlocDeau);
		
		JLabel lblGrosseBalle = new JLabel("Grosse Balle");
		lblGrosseBalle.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblGrosseBalle.setBounds(1581, 513, 117, 14);
		contentPane.add(lblGrosseBalle);
		
		JLabel lblMediumBalle = new JLabel("Medium Balle");
		lblMediumBalle.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblMediumBalle.setBounds(1581, 604, 117, 14);
		contentPane.add(lblMediumBalle);
		
		JLabel lblPetiteBalle = new JLabel("Petite balle");
		lblPetiteBalle.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblPetiteBalle.setBounds(1581, 706, 117, 14);
		contentPane.add(lblPetiteBalle);
		
		JLabel lblPrisme = new JLabel("Prisme");
		lblPrisme.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblPrisme.setBounds(1581, 815, 89, 14);
		contentPane.add(lblPrisme);
		
		JLabel lblAngle_1 = new JLabel("Angle: ");
		lblAngle_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblAngle_1.setBounds(1447, 884, 69, 23);
		contentPane.add(lblAngle_1);
		
		lblValeur = new JLabel("Valeur");
		lblValeur.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblValeur.setBounds(1502, 888, 60, 14);
		contentPane.add(lblValeur);
		
		JSeparator separator_5 = new JSeparator();
		separator_5.setForeground(SystemColor.activeCaption);
		separator_5.setBounds(30, 852, 1303, 2);
		contentPane.add(separator_5);
		
		JSeparator separator_6 = new JSeparator();
		separator_6.setOrientation(SwingConstants.VERTICAL);
		separator_6.setBounds(30, 852, 9, 125);
		contentPane.add(separator_6);
		
		JSeparator separator_7 = new JSeparator();
		separator_7.setForeground(SystemColor.activeCaption);
		separator_7.setBounds(30, 975, 1303, 2);
		contentPane.add(separator_7);
		
		JSeparator separator_8 = new JSeparator();
		separator_8.setOrientation(SwingConstants.VERTICAL);
		separator_8.setBounds(1331, 852, 9, 125);
		contentPane.add(separator_8);
		
		JLabel lblditeur = new JLabel("\u00C9DITEUR");
		lblditeur.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 30));
		lblditeur.setHorizontalTextPosition(SwingConstants.CENTER);
		lblditeur.setHorizontalAlignment(SwingConstants.CENTER);
		lblditeur.setBounds(1409, 25, 362, 59);
		contentPane.add(lblditeur);
		
		JLabel lblLesSorties = new JLabel("Les sorties: ");
		lblLesSorties.setBounds(40, 856, 69, 14);
		contentPane.add(lblLesSorties);
		
		scene = new Scene();
		scene.setBounds(30, 107, 1303, 727);
		contentPane.add(scene);
		
		JButton btnViderScene = new JButton("Vider Scene");
		btnViderScene.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scene.reinitialiserDessin();
				donneFocusALaScene();
			}
		});
		btnViderScene.setBounds(1131, 61, 40, 38);
		contentPane.add(btnViderScene);
		associerBoutonAvecImage(btnViderScene, "corbeille.png");
		
		
	}
	/**
	 * Methode qui donne le focus a la scene
	 *@author Miora
	 */
	public void donneFocusALaScene() {
		scene.requestFocusInWindow();
	}

	
	/**
	 * permet d'étendre la fenêtre et d'activer les boutons de l'éditeur
	 */
	public void activerEditeur() {
		setBounds(100, 100, 1800, 1006);
		btnBlocDeau.setEnabled(true);
		btnGrosseBalle.setEnabled(true);
		btnMediumBalle.setEnabled(true);
		btnMiroirConcave.setEnabled(true);
		btnMiroirConvexe.setEnabled(true);
		btnMiroirPlan.setEnabled(true);
		btnPrisme.setEnabled(true);
		btnTrouNoir.setEnabled(true);
		btnPetiteBalle.setEnabled(true);
	}
	/**
	 * permet de réduire la fenêtre à sa forme initiale et de désactiver les boutons de l'éditeur
	 */
	public void desactiverEditeur() {
		setBounds(100, 100, 1389, 1006);
		btnBlocDeau.setEnabled(false);
		btnGrosseBalle.setEnabled(false);
		btnMediumBalle.setEnabled(false);
		btnMiroirConcave.setEnabled(false);
		btnMiroirConvexe.setEnabled(false);
		btnMiroirPlan.setEnabled(false);
		btnPrisme.setEnabled(false);
		btnTrouNoir.setEnabled(false);
		btnPetiteBalle.setEnabled(false);
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
