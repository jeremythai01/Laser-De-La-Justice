package aaplication;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import interfaces.SceneListener;
import options.Options;
import son.Bruit;

import javax.swing.JList;
import javax.swing.ListSelectionModel;

/**
 * 
 * Cette classe est la fenetre d'application du jeu
 * 
 * @author Arezki et Miora
 */
public class FenetreJeu extends JFrame {

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
	private Scene sceneFinale;
	private Options optionJeu;

	private boolean isNouveauOption = true;
	boolean triche = false;

	private static String nomFichier;


	private ActionListener listener;
	private Timer tempsJeu;
	double secondes = 60;
	private static boolean  isNouvelle = true, isOptiPerso = true;
	private JProgressBar barreTempsDuJeu;


	private Bruit son = new Bruit();
	private JButton buttonMiroirCourbe;


	// Par Arezki 
	/**
	 * Lancement de l'application
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FenetreJeu frame;
					frame = new FenetreJeu(isNouvelle, nomFichier);
					frame.setVisible(true);
					frame.sceneFinale.requestFocusInWindow();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Creation de la fenêtre
	 * @param isNouvelle : retourne vrai si il s'agit d'une nouvelle partie
	 * @author Miora nomFichier : le nom du fichier de sauvegarde ou niveau
	 */
	// Par Arezki
	public FenetreJeu(boolean isNouvelle, String nomFichier) {
		setTitle("Laser de la Justice.exe");
		setBackground(Color.GRAY);
		this.isNouvelle = isNouvelle;
		this.nomFichier = nomFichier;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1184, 969);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.menu);
		contentPane.setForeground(new Color(255, 175, 175));
		contentPane.setBounds(new Rectangle(2, 0, 0, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);


		JButton btnPlay = new JButton("play");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sceneFinale.demarrer();
				tempsJeu.start();
				donneFocusALasceneFinale();
			}
		});
		btnPlay.setBounds(10, 61, 96, 38);
		contentPane.add(btnPlay);
		//associerBoutonAvecImage(btnPlay, "play.png");

		JButton btnPause = new JButton("pause");
		btnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sceneFinale.arreter();
				tempsJeu.stop();
			}
		});
		btnPause.setBounds(116, 61, 89, 38);
		contentPane.add(btnPause);
		//associerBoutonAvecImage(btnPause, "pause.jpg");

		btnImage = new JButton("image");
		btnImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sceneFinale.calculerUneIterationPhysique();
				repaint();
			}
		});
		btnImage.setBounds(215, 61, 89, 38);
		contentPane.add(btnImage);
		//associerBoutonAvecImage(btnImage, "rejouer.png");

		JButton btnTutoriel = new JButton("Tutoriel");
		btnTutoriel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FenetreTuto fenetreTuto = new FenetreTuto();

				fenetreTuto.setVisible(true);
			}
		});
		btnTutoriel.setBounds(420, 61, 96, 38);
		contentPane.add(btnTutoriel);

		JButton btnOption = new JButton("Option");
		btnOption.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				choixOption();
			}
		});
		btnOption.setBounds(964, 61, 89, 38);
		contentPane.add(btnOption);
		//associerBoutonAvecImage(btnOption, "reglage.png");

		JButton btnEnregistrer = new JButton("Enregistrer partie\r\n");
		btnEnregistrer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sceneFinale.arreter();
				tempsJeu.stop();
				String nom = JOptionPane.showInputDialog("Entrez le nom de votre sauvagarde :");
				sceneFinale.ecritureFichierSauvegarde(nom, false);
			}
		});
		btnEnregistrer.setBounds(689, 61, 159, 38);
		contentPane.add(btnEnregistrer);
		//associerBoutonAvecImage(btnEnregistrer, "enregistrer.png");

		btnMiroirPlan = new JButton("miroir Plan");
		btnMiroirPlan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sceneFinale.ajoutMiroirPlan();
				donneFocusALasceneFinale();
			}
		});
		btnMiroirPlan.setEnabled(false);

		btnMiroirPlan.setBounds(934, 868, 105, 23);
		contentPane.add(btnMiroirPlan);

		btnMiroirConvexe = new JButton("miroir Convexe");
		btnMiroirConvexe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sceneFinale.ajoutMiroireConvexe();
				donneFocusALasceneFinale();
			}
		});
		btnMiroirConvexe.setEnabled(false);
		btnMiroirConvexe.setBounds(1329, 852, 105, 23);


		contentPane.add(btnMiroirConvexe);


		btnTrouNoir = new JButton("Trou Noir");
		btnTrouNoir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sceneFinale.ajoutTrouNoir();
				donneFocusALasceneFinale();
			}
		});
		btnTrouNoir.setEnabled(false);
		btnTrouNoir.setBounds(835, 834, 89, 23);
		contentPane.add(btnTrouNoir);

		btnBlocDeau = new JButton("bloc D'eau ");
		btnBlocDeau.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sceneFinale.ajoutBlocEau();
				donneFocusALasceneFinale();
			}
		});
		btnBlocDeau.setEnabled(false);
		btnBlocDeau.setBounds(835, 800, 89, 23);
		contentPane.add(btnBlocDeau);

		btnGrosseBalle = new JButton("Grosse Balle");
		btnGrosseBalle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sceneFinale.ajoutBalleGrosse();
			}
		});
		btnGrosseBalle.setEnabled(false);
		btnGrosseBalle.setBounds(729, 800, 96, 23);
		contentPane.add(btnGrosseBalle);

		btnMediumBalle = new JButton("Medium Balle");
		btnMediumBalle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sceneFinale.ajoutBalleMedium();
				donneFocusALasceneFinale();
			}
		});
		btnMediumBalle.setEnabled(false);
		btnMediumBalle.setBounds(729, 834, 96, 23);
		contentPane.add(btnMediumBalle);

		btnPetiteBalle = new JButton("petite balle ");
		btnPetiteBalle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sceneFinale.ajoutBallePetite();
				donneFocusALasceneFinale();
			}
		});
		btnPetiteBalle.setEnabled(false);
		btnPetiteBalle.setBounds(729, 868, 96, 26);
		contentPane.add(btnPetiteBalle);

		btnPrisme = new JButton("prisme");
		btnPrisme.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sceneFinale.ajoutPrisme();
			}
		});
		btnPrisme.setEnabled(false);
		btnPrisme.setBounds(835, 868, 89, 23);
		contentPane.add(btnPrisme);

		JSeparator separator_5 = new JSeparator();
		separator_5.setForeground(SystemColor.activeCaption);
		separator_5.setBounds(44, 730, 1108, 2);
		contentPane.add(separator_5);

		JSeparator separator_6 = new JSeparator();
		separator_6.setOrientation(SwingConstants.VERTICAL);
		separator_6.setBounds(44, 730, 9, 198);
		contentPane.add(separator_6);

		JSeparator separator_7 = new JSeparator();
		separator_7.setForeground(SystemColor.activeCaption);
		separator_7.setBounds(30, 1048, 1114, 2);
		contentPane.add(separator_7);

		JSeparator separator_8 = new JSeparator();
		separator_8.setOrientation(SwingConstants.VERTICAL);
		separator_8.setBounds(1149, 730, 9, 198);
		contentPane.add(separator_8);

		JLabel lblditeur = new JLabel("Mini \u00E9diteur");
		lblditeur.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 17));
		lblditeur.setHorizontalTextPosition(SwingConstants.CENTER);
		lblditeur.setHorizontalAlignment(SwingConstants.CENTER);
		lblditeur.setBounds(719, 730, 105, 59);
		contentPane.add(lblditeur);

		JLabel lblLesSorties = new JLabel("Les sorties: ");
		lblLesSorties.setBounds(54, 743, 69, 14);
		contentPane.add(lblLesSorties);

		sceneFinale = new Scene(isNouvelle, nomFichier);
		sceneFinale.addSceneListener(new SceneListener() {
			public void changementTempsListener(int temps) {
				modifierLeTemps(temps);
			}
		});
		sceneFinale.setBounds(10, 110, 1146, 585);
		contentPane.add(sceneFinale);



		toucheScene();
		//associerBoutonAvecImage(btnVidersceneFinale, "corbeille.png");

		barreTempsDuJeu = new JProgressBar();
		barreTempsDuJeu.setMaximum(60);
		barreTempsDuJeu.setString("60.0 secondes restantes");
		barreTempsDuJeu.setValue(60);
		barreTempsDuJeu.setForeground(new Color(0, 0, 255));
		barreTempsDuJeu.setOpaque(true);

		barreTempsDuJeu.setStringPainted(true);
		barreTempsDuJeu.setBounds(10, 696, 1148, 23);
		contentPane.add(barreTempsDuJeu);

		JToggleButton btnEditeur = new JToggleButton("Editeur");
		btnEditeur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(btnEditeur.isSelected()) {
					sceneFinale.requestFocusInWindow();
					sceneFinale.arreter();
					activerEditeur();
					sceneFinale.ActiverEditeur();
					tempsJeu.stop();
				}else {
					desactiverEditeur();
					sceneFinale.DesactiverEditeur();
					donneFocusALasceneFinale();
					tempsJeu.stop();
				}
			}
		});
		btnEditeur.setBounds(858, 61, 96, 38);
		contentPane.add(btnEditeur);

		JButton btnDemarrage = new JButton("demarrage\r\n");
		btnDemarrage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				App10LaserDeLaJustice demarrage = new App10LaserDeLaJustice();
				demarrage.setVisible(true);
				setVisible(false);

			}
		});
		btnDemarrage.setBounds(314, 62, 96, 36);
		contentPane.add(btnDemarrage);

		listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// TODO Auto-generated method stub
				if(!triche)
					if (barreTempsDuJeu.getValue() > 00 && secondes >= 0) {

						barreTempsDuJeu.setValue(barreTempsDuJeu.getValue() - 1);

						//barreTempsDuJeu.setString(secondes-- + " secondes restantes");
						sceneFinale.setTempsDuJeu(barreTempsDuJeu.getValue());
					} else {
						son.joue("gameover");
						tempsJeu.stop();
						sceneFinale.arreter();

						FenetreGameOver gameOver = new FenetreGameOver();
						setVisible(false);
						gameOver.setVisible(true);
					}
			}
		};

		tempsJeu = new Timer(1000, listener);

		/*
		associerBoutonAvecImage(btnBlocDeau, "Bloc.JPG");
		associerBoutonAvecImage(btnGrosseBalle, "GrBalle.JPG");
		associerBoutonAvecImage(btnMediumBalle, "MedBalle.JPG");
		associerBoutonAvecImage(btnMiroirConcave, "convex.JPG");
		associerBoutonAvecImage(btnMiroirConvexe, "Concave.JPG");
		associerBoutonAvecImage(btnPetiteBalle, "PetBalle.JPG");
		associerBoutonAvecImage(btnTrouNoir, "Trou.JPG");
		associerBoutonAvecImage(btnMiroirPlan, "plan.JPG");
		 */
		JButton btnSaveNiveau = new JButton("Enregistrer niveau\r\n");
		btnSaveNiveau.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String nomSauv = JOptionPane.showInputDialog("Entrer le nom de votre niveau :");
				sceneFinale.ecritureNiveau(nomSauv);
			}
		});
		btnSaveNiveau.setBounds(526, 61, 153, 38);
		contentPane.add(btnSaveNiveau);

		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(719, 730, 9, 198);
		contentPane.add(separator);

		JToggleButton tglbtnTriche = new JToggleButton("Triche\r\n");
		tglbtnTriche.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if(tglbtnTriche.isSelected()) {
					triche = true;
				}
			}
		});
		tglbtnTriche.setBounds(1063, 61, 89, 38);
		contentPane.add(tglbtnTriche);

		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(SystemColor.activeCaption);
		separator_1.setBounds(44, 926, 1108, 2);
		contentPane.add(separator_1);
		
		buttonMiroirCourbe = new JButton("Miroir Courbe\r\n");
		buttonMiroirCourbe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sceneFinale.ajoutMiroirCourbe();
			}
		});
		buttonMiroirCourbe.setEnabled(false);
		buttonMiroirCourbe.setBounds(934, 834, 105, 23);
		contentPane.add(buttonMiroirCourbe);
	}

	//Par Miora
	/**
	 * Cette methode permet de montrer a l'utilisateur les touches pour jouer
	 */
	private void toucheScene() {
		JOptionPane.showMessageDialog(null, " " + "Vos touches ont été initialisé a " + KeyEvent.getKeyText(sceneFinale.getToucheGauche()) + " et " + KeyEvent.getKeyText(sceneFinale.getToucheDroite()));

	}

	// Par Miora
	/**
	 * Methode qui donne le focus a la sceneFinale
	 */
	public void donneFocusALasceneFinale() {
		sceneFinale.requestFocusInWindow();
	}

	//Par Arezki
	/**
	 * permet d'étendre la fenêtre et d'activer les boutons de l'éditeur pour mettre
	 * les objets dans la sceneFinale
	 * 
	 */
	public void activerEditeur() {
		btnBlocDeau.setEnabled(true);
		btnGrosseBalle.setEnabled(true);
		btnMediumBalle.setEnabled(true);
		btnMiroirPlan.setEnabled(true);
		btnPrisme.setEnabled(true);
		btnTrouNoir.setEnabled(true);
		btnPetiteBalle.setEnabled(true);
		buttonMiroirCourbe.setEnabled(true);
	}

	// Par Arezki
	/**
	 * permet de réduire la fenêtre à sa forme initiale et de désactiver les boutons
	 * de l'éditeur, mais les dessins restent
	 * 
	 * @author Arezki
	 */
	public void desactiverEditeur() {
		btnBlocDeau.setEnabled(false);
		btnGrosseBalle.setEnabled(false);
		btnMediumBalle.setEnabled(false);
		btnMiroirPlan.setEnabled(false);
		btnPrisme.setEnabled(false);
		btnTrouNoir.setEnabled(false);
		btnPetiteBalle.setEnabled(false);
		buttonMiroirCourbe.setEnabled(false);
	}


	// Miora
	/**
	 * Cette methode defini si les options doivent etre change ou non 
	 * @param isNouveauOption : s'il y a un changement ou non S
	 */
	public void setNouveauOption(boolean isNouveauOption) {
		this.isNouveauOption = isNouveauOption;
	}


	//Miora
	/**
	 * Cette methode permet de choisir une sceneFinale sauvegarde ou une nouvelle
	 * sceneFinale
	 * @param reponse : oui ou non s'il s'agit d'une nouvelle scene
	 */
//
//	public void isNouvelle(boolean reponse) {
//		isNouvelle = reponse;
//	}

	//Par Miora
	/**
	 * Cette methode ouvre le menu option lorsque la touche option est cliquee
	 */
	private void choixOption() {
		optionJeu = new Options(true);
		optionJeu.setDansScene(true);
		optionJeu.setVisible(true);
		sceneFinale.ecritureFichierSauvegarde("temporaire", true); // je sauvegarde
		sceneFinale.arreter();
		tempsJeu.stop();
		setVisible(false);
	}
	

	//Par Miora
	/**
	 * Cette methode modifie le temps pour la barre de temps
	 * @param temps : le nouveau temps
	 */
	private void modifierLeTemps(int temps) {
		barreTempsDuJeu.setValue(temps);
		barreTempsDuJeu.setString(temps + " secondes restantes");
		
	}

}
