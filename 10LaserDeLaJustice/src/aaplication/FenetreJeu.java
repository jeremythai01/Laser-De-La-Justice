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
	 * Creation de la fen�tre
	 * @param isNouvelle : retourne vrai si il s'agit d'une nouvelle partie
	 */
	// Par Arezki
	public FenetreJeu(boolean isNouvelle, String nomFichier) {
		setTitle("Laser de la Justice.exe");
		setBackground(Color.GRAY);
		this.isNouvelle = isNouvelle;
		this.nomFichier = nomFichier;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1507, 1056);
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
		btnPlay.setBounds(44, 61, 96, 38);
		contentPane.add(btnPlay);
		//associerBoutonAvecImage(btnPlay, "play.png");

		JButton btnPause = new JButton("pause");
		btnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sceneFinale.arreter();
				tempsJeu.stop();
			}
		});
		btnPause.setBounds(150, 61, 110, 38);
		contentPane.add(btnPause);
		//associerBoutonAvecImage(btnPause, "pause.jpg");

		btnImage = new JButton("image");
		btnImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sceneFinale.calculerUneIterationPhysique();
				repaint();
			}
		});
		btnImage.setBounds(270, 61, 122, 38);
		contentPane.add(btnImage);
		//associerBoutonAvecImage(btnImage, "rejouer.png");

		JButton btnTutoriel = new JButton("Tutoriel");
		btnTutoriel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FenetreTuto fenetreTuto = new FenetreTuto();

				fenetreTuto.setVisible(true);
			}
		});
		btnTutoriel.setBounds(508, 61, 96, 38);
		contentPane.add(btnTutoriel);

		JButton btnOption = new JButton("Option");
		btnOption.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				choixOption();
			}
		});
		btnOption.setBounds(1101, 61, 110, 38);
		contentPane.add(btnOption);
		//associerBoutonAvecImage(btnOption, "reglage.png");

		JButton btnEnregistrer = new JButton("Enregistrer partie\r\n");
		btnEnregistrer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			//	sceneFinale.ecritureFichierSauvegarde();
				donneFocusALasceneFinale();
				sceneFinale.arreter();
				tempsJeu.stop();
				JOptionPane.showMessageDialog(null, "Votre partie a �t� sauvegard�e");
			}
		});
		btnEnregistrer.setBounds(776, 61, 167, 38);
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
		btnMiroirPlan.setBounds(1004, 977, 105, 23);
		contentPane.add(btnMiroirPlan);

		btnMiroirConvexe = new JButton("miroir Convexe");
		btnMiroirConvexe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sceneFinale.ajoutMiroireConvexe();
				donneFocusALasceneFinale();
			}
		});
		btnMiroirConvexe.setEnabled(false);
		btnMiroirConvexe.setBounds(1004, 943, 105, 23);
		contentPane.add(btnMiroirConvexe);

		btnMiroirConcave = new JButton("miroir Concave");
		btnMiroirConcave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sceneFinale.ajoutMiroireConcave();
				donneFocusALasceneFinale();
			}
		});
		btnMiroirConcave.setEnabled(false);
		btnMiroirConcave.setBounds(1004, 909, 105, 23);
		contentPane.add(btnMiroirConcave);

		btnTrouNoir = new JButton("Trou Noir");
		btnTrouNoir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sceneFinale.ajoutTrouNoir();
				donneFocusALasceneFinale();
			}
		});
		btnTrouNoir.setEnabled(false);
		btnTrouNoir.setBounds(905, 943, 89, 23);
		contentPane.add(btnTrouNoir);

		btnBlocDeau = new JButton("bloc D'eau ");
		btnBlocDeau.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sceneFinale.ajoutBlocEau();
				donneFocusALasceneFinale();
			}
		});
		btnBlocDeau.setEnabled(false);
		btnBlocDeau.setBounds(905, 909, 89, 23);
		contentPane.add(btnBlocDeau);

		btnGrosseBalle = new JButton("Grosse Balle");
		btnGrosseBalle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sceneFinale.ajoutBalleGrosse();
			}
		});
		btnGrosseBalle.setEnabled(false);
		btnGrosseBalle.setBounds(786, 909, 96, 23);
		contentPane.add(btnGrosseBalle);

		btnMediumBalle = new JButton("Medium Balle");
		btnMediumBalle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sceneFinale.ajoutBalleMedium();
				donneFocusALasceneFinale();
			}
		});
		btnMediumBalle.setEnabled(false);
		btnMediumBalle.setBounds(786, 943, 96, 23);
		contentPane.add(btnMediumBalle);

		btnPetiteBalle = new JButton("petite balle ");
		btnPetiteBalle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sceneFinale.ajoutBallePetite();
				donneFocusALasceneFinale();
			}
		});
		btnPetiteBalle.setEnabled(false);
		btnPetiteBalle.setBounds(786, 975, 96, 26);
		contentPane.add(btnPetiteBalle);

		btnPrisme = new JButton("prisme");
		btnPrisme.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sceneFinale.ajoutPrisme();
			}
		});
		btnPrisme.setEnabled(false);
		btnPrisme.setBounds(905, 977, 89, 23);
		contentPane.add(btnPrisme);

		JSeparator separator_5 = new JSeparator();
		separator_5.setForeground(SystemColor.activeCaption);
		separator_5.setBounds(30, 852, 1114, 2);
		contentPane.add(separator_5);

		JSeparator separator_6 = new JSeparator();
		separator_6.setOrientation(SwingConstants.VERTICAL);
		separator_6.setBounds(30, 852, 9, 198);
		contentPane.add(separator_6);

		JSeparator separator_7 = new JSeparator();
		separator_7.setForeground(SystemColor.activeCaption);
		separator_7.setBounds(30, 1048, 1114, 2);
		contentPane.add(separator_7);

		JSeparator separator_8 = new JSeparator();
		separator_8.setOrientation(SwingConstants.VERTICAL);
		separator_8.setBounds(1143, 852, 9, 198);
		contentPane.add(separator_8);

		JLabel lblditeur = new JLabel("Mini \u00E9diteur");
		lblditeur.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 17));
		lblditeur.setHorizontalTextPosition(SwingConstants.CENTER);
		lblditeur.setHorizontalAlignment(SwingConstants.CENTER);
		lblditeur.setBounds(776, 848, 105, 59);
		contentPane.add(lblditeur);

		JLabel lblLesSorties = new JLabel("Les sorties: ");
		lblLesSorties.setBounds(40, 856, 69, 14);
		contentPane.add(lblLesSorties);

		sceneFinale = new Scene(isNouvelle, nomFichier);
		sceneFinale.setBounds(44, 110, 1405, 685);
		contentPane.add(sceneFinale);
		
		sceneFinale.addSceneListener(new SceneListener() {


			@Override
			public void couleurLaserListener() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void changementTempsListener(double temps) {
				System.out.println("dans le listener dans l'application ");
				barreTempsDuJeu.setValue((int)temps);
			}
		});
	

		toucheScene();
		//associerBoutonAvecImage(btnVidersceneFinale, "corbeille.png");

		barreTempsDuJeu = new JProgressBar();
		barreTempsDuJeu.setMaximum(60);
		barreTempsDuJeu.setString("60.0 secondes restantes");
		barreTempsDuJeu.setValue(60);
		barreTempsDuJeu.setForeground(new Color(0, 0, 255));
		barreTempsDuJeu.setOpaque(true);

		barreTempsDuJeu.setStringPainted(true);
		barreTempsDuJeu.setBounds(44, 796, 1405, 38);
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
		btnEditeur.setBounds(953, 61, 138, 38);
		contentPane.add(btnEditeur);

		JButton btnDemarrage = new JButton("demarrage\r\n");
		btnDemarrage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				App10LaserDeLaJustice demarrage = new App10LaserDeLaJustice();
				demarrage.setVisible(true);

			}
		});
		btnDemarrage.setBounds(402, 62, 96, 36);
		contentPane.add(btnDemarrage);

		listener = new ActionListener() {

			

			@Override
			public void actionPerformed(ActionEvent e) {
				
				// TODO Auto-generated method stub
				if(!triche)
				if (barreTempsDuJeu.getValue() > 00 && secondes >= 0) {

					barreTempsDuJeu.setValue(barreTempsDuJeu.getValue() - 1);

					barreTempsDuJeu.setString(secondes-- + " secondes restantes");
					//sceneFinale.setTempsTotalEcoule(barreTempsDuJeu.getValue());

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
		btnSaveNiveau.setBounds(614, 61, 153, 38);
		contentPane.add(btnSaveNiveau);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(757, 852, 9, 198);
		contentPane.add(separator);
		
		JToggleButton tglbtnTriche = new JToggleButton("Triche\r\n");
		tglbtnTriche.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if(tglbtnTriche.isSelected()) {
					triche = true;
				}
			}
		});
		tglbtnTriche.setBounds(1221, 61, 121, 38);
		contentPane.add(tglbtnTriche);
	}

	//Par Miora
	/**
	 * Cette methode permet de montrer a l'utilisateur les touches pour jouer
	 */
	private void toucheScene() {
		JOptionPane.showMessageDialog(null, " " + "Vos touches ont �t� initialis� a " + KeyEvent.getKeyText(sceneFinale.getToucheGauche()) + " et " + KeyEvent.getKeyText(sceneFinale.getToucheDroite()));

	}

	// Par Miora
	/**
	 * Methode qui donne le focus a la sceneFinale
	 */
	public void donneFocusALasceneFinale() {
		sceneFinale.requestFocusInWindow();
	}

	// // Par Arezki
	/**
	 * permet d'�tendre la fen�tre et d'activer les boutons de l'�diteur pour mettre
	 * les objets dans la sceneFinale
	 * 
	 * @author Arezki
	 */
	public void activerEditeur() {
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

	// Par Arezki
	/**
	 * permet de r�duire la fen�tre � sa forme initiale et de d�sactiver les boutons
	 * de l'�diteur, mais les dessins restent
	 * 
	 * @author Arezki
	 */
	public void desactiverEditeur() {
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
	// par Caroline Houle

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
	public void isNouvelle(boolean reponse) {
		isNouvelle = reponse;
	}

	//Par Miora
	/**
	 * Cette methode ouvre le menu option lorsque la touche option est cliquee
	 */
	private void choixOption() {
		optionJeu = new Options();
		optionJeu.setDansScene(true);
		optionJeu.setVisible(true);
	//	sceneFinale.ecritureFichierSauvegarde(); // je sauvegarde
		sceneFinale.arreter();
		tempsJeu.stop();
		setVisible(false);
	}
}
