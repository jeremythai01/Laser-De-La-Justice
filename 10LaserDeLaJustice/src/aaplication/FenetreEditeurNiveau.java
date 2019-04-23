package aaplication;


import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javax.swing.JSpinner;

public class FenetreEditeurNiveau extends JFrame {
	/**
	 * Cette classe sert à éditer et créer un niveau
	 * @author Arezki
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
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
	//private Options optionJeu;

	private boolean isNouveauOption = true;
	boolean triche = false;
	
	private static String nomFichier;


	private ActionListener listener;
	private Timer tempsJeu;
	double secondes = 60;
	private static boolean  isNouvelle = true;
	
	
	private JSpinner spnAngleMiroir;
	
	
	// Par Arezki 
	/**
	 * Lancement de l'application
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FenetreEditeurNiveau frame;
					frame = new FenetreEditeurNiveau();
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
	 */
	// Par Arezki
	public FenetreEditeurNiveau() {
		setTitle("Laser de la Justice.exe");
		setBackground(Color.GRAY);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1413, 854);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.menu);
		contentPane.setForeground(new Color(255, 175, 175));
		contentPane.setBounds(new Rectangle(2, 0, 0, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		//associerBoutonAvecImage(btnImage, "rejouer.png");

		JButton btnTutoriel = new JButton("Tutoriel");
		btnTutoriel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FenetreTuto fenetreTuto = new FenetreTuto();

				fenetreTuto.setVisible(true);
			}
		});
		btnTutoriel.setBounds(186, 61, 96, 38);
		contentPane.add(btnTutoriel);

		//associerBoutonAvecImage(btnEnregistrer, "enregistrer.png");

		btnMiroirPlan = new JButton("miroir Plan");
		btnMiroirPlan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sceneFinale.ajoutMiroirPlan();
				donneFocusALasceneFinale();
			}
		});
		btnMiroirPlan.setEnabled(false);
		btnMiroirPlan.setBounds(1198, 604, 105, 23);
		contentPane.add(btnMiroirPlan);

		btnMiroirConvexe = new JButton("miroir Convexe");
		btnMiroirConvexe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sceneFinale.ajoutMiroireConvexe();
				donneFocusALasceneFinale();
			}
		});
		btnMiroirConvexe.setEnabled(false);
		btnMiroirConvexe.setBounds(1198, 570, 105, 23);
		contentPane.add(btnMiroirConvexe);

		btnMiroirConcave.setEnabled(false);
		btnMiroirConcave.setBounds(1198, 536, 105, 23);
		contentPane.add(btnMiroirConcave);

		btnTrouNoir = new JButton("Trou Noir");
		btnTrouNoir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sceneFinale.ajoutTrouNoir();
				donneFocusALasceneFinale();
			}
		});
		btnTrouNoir.setEnabled(false);
		btnTrouNoir.setBounds(1198, 387, 89, 23);
		contentPane.add(btnTrouNoir);

		btnBlocDeau = new JButton("Bloc ");
		btnBlocDeau.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sceneFinale.ajoutBlocEau();
				donneFocusALasceneFinale();
			}
		});
		btnBlocDeau.setEnabled(false);
		btnBlocDeau.setBounds(1198, 353, 89, 23);
		contentPane.add(btnBlocDeau);

		btnGrosseBalle = new JButton("Grosse Balle");
		btnGrosseBalle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sceneFinale.ajoutBalleGrosse();
			}
		});
		btnGrosseBalle.setEnabled(false);
		btnGrosseBalle.setBounds(1198, 166, 96, 23);
		contentPane.add(btnGrosseBalle);

		btnMediumBalle = new JButton("Medium Balle");
		btnMediumBalle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sceneFinale.ajoutBalleMedium();
				donneFocusALasceneFinale();
			}
		});
		btnMediumBalle.setEnabled(false);
		btnMediumBalle.setBounds(1198, 200, 96, 23);
		contentPane.add(btnMediumBalle);

		btnPetiteBalle = new JButton("petite balle ");
		btnPetiteBalle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sceneFinale.ajoutBallePetite();
				donneFocusALasceneFinale();
			}
		});
		btnPetiteBalle.setEnabled(false);
		btnPetiteBalle.setBounds(1198, 234, 96, 26);
		contentPane.add(btnPetiteBalle);

		btnPrisme = new JButton("prisme");
		btnPrisme.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sceneFinale.ajoutPrisme();
			}
		});
		btnPrisme.setEnabled(false);
		btnPrisme.setBounds(1198, 421, 89, 23);
		contentPane.add(btnPrisme);

		JSeparator separator_5 = new JSeparator();
		separator_5.setForeground(SystemColor.activeCaption);
		separator_5.setBounds(1179, 304, 218, 2);
		contentPane.add(separator_5);

		JSeparator separator_6 = new JSeparator();
		separator_6.setOrientation(SwingConstants.VERTICAL);
		separator_6.setBounds(1179, 110, 9, 809);
		contentPane.add(separator_6);

		JSeparator separator_7 = new JSeparator();
		separator_7.setForeground(SystemColor.activeCaption);
		separator_7.setBounds(30, 1048, 1114, 2);
		contentPane.add(separator_7);

		JLabel lblditeur = new JLabel("\u00C9diteur\r\n");
		lblditeur.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 17));
		lblditeur.setHorizontalTextPosition(SwingConstants.CENTER);
		lblditeur.setHorizontalAlignment(SwingConstants.CENTER);
		lblditeur.setBounds(1198, 49, 105, 59);
		contentPane.add(lblditeur);

		sceneFinale = new Scene(isNouvelle, nomFichier);
		sceneFinale.setBounds(10, 110, 1146, 585);
		contentPane.add(sceneFinale);
		
		
	

		toucheScene();

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
		btnEditeur.setBounds(886, 61, 96, 38);
		contentPane.add(btnEditeur);

		JButton btnDemarrage = new JButton("demarrage\r\n");
		btnDemarrage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				App10LaserDeLaJustice demarrage = new App10LaserDeLaJustice();
				demarrage.setVisible(true);
				setVisible(false);

			}
		});
		btnDemarrage.setBounds(10, 62, 96, 36);
		contentPane.add(btnDemarrage);


			

			
			
		tempsJeu = new Timer(1000, listener);

		
		JButton btnSaveNiveau = new JButton("Enregistrer niveau\r\n");
		btnSaveNiveau.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String nomSauv = JOptionPane.showInputDialog("Entrer le nom de votre niveau :");
				sceneFinale.ecritureNiveau(nomSauv);
			}
		});
		btnSaveNiveau.setBounds(364, 61, 153, 38);
		contentPane.add(btnSaveNiveau);
		
		JLabel lblTypeDeBalle = new JLabel("Type de balle\r\n");
		lblTypeDeBalle.setHorizontalTextPosition(SwingConstants.CENTER);
		lblTypeDeBalle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTypeDeBalle.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 10));
		lblTypeDeBalle.setBounds(1189, 127, 105, 38);
		contentPane.add(lblTypeDeBalle);
		
		JLabel lblTypeDeRfraction = new JLabel("Type de R\u00E9fraction\r\n");
		lblTypeDeRfraction.setHorizontalTextPosition(SwingConstants.CENTER);
		lblTypeDeRfraction.setHorizontalAlignment(SwingConstants.CENTER);
		lblTypeDeRfraction.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 10));
		lblTypeDeRfraction.setBounds(1198, 314, 105, 38);
		contentPane.add(lblTypeDeRfraction);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(SystemColor.activeCaption);
		separator.setBounds(1179, 472, 218, 4);
		contentPane.add(separator);
		
		JLabel lblTypeDeRflexion = new JLabel("Type de R\u00E9flexion\r\n");
		lblTypeDeRflexion.setHorizontalTextPosition(SwingConstants.CENTER);
		lblTypeDeRflexion.setHorizontalAlignment(SwingConstants.CENTER);
		lblTypeDeRflexion.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 10));
		lblTypeDeRflexion.setBounds(1198, 487, 105, 38);
		contentPane.add(lblTypeDeRflexion);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(SystemColor.activeCaption);
		separator_1.setBounds(1179, 652, 218, 2);
		contentPane.add(separator_1);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setForeground(SystemColor.activeCaption);
		separator_2.setBounds(1179, 110, 208, 2);
		contentPane.add(separator_2);
		
		JLabel lblChangementsDivers = new JLabel("Changements Divers");
		lblChangementsDivers.setHorizontalTextPosition(SwingConstants.CENTER);
		lblChangementsDivers.setHorizontalAlignment(SwingConstants.CENTER);
		lblChangementsDivers.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 10));
		lblChangementsDivers.setBounds(1198, 657, 105, 38);
		contentPane.add(lblChangementsDivers);
		
		JLabel lblAngleMiroir = new JLabel("Angle Miroir");
		lblAngleMiroir.setBounds(1198, 701, 118, 14);
		contentPane.add(lblAngleMiroir);
		
		spnAngleMiroir = new JSpinner();
		spnAngleMiroir.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				changerAngleMiroirs();
				
			}
		});
		spnAngleMiroir.setBounds(1317, 698, 29, 20);
		contentPane.add(spnAngleMiroir);
		
		JLabel lblIndiceRefMiroir = new JLabel("R\u00E9fraction Miroir\r\n");
		lblIndiceRefMiroir.setBounds(1198, 729, 118, 14);
		contentPane.add(lblIndiceRefMiroir);
		
		JSpinner spnRefMiroir = new JSpinner();
		spnRefMiroir.setBounds(1317, 726, 29, 20);
		contentPane.add(spnRefMiroir);
		
		JLabel lblRfractionP = new JLabel("R\u00E9fraction Prisme");
		lblRfractionP.setBounds(1198, 760, 118, 14);
		contentPane.add(lblRfractionP);
		
		JSpinner spinner = new JSpinner();
		spinner.setBounds(1317, 757, 29, 20);
		contentPane.add(spinner);
		
		JLabel lblRefBloc = new JLabel("R\u00E9fraction Bloc");
		lblRefBloc.setBounds(1198, 791, 118, 14);
		contentPane.add(lblRefBloc);
		
		JSpinner spinner_1 = new JSpinner();
		spinner_1.setBounds(1317, 788, 29, 20);
		contentPane.add(spinner_1);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setOrientation(SwingConstants.VERTICAL);
		separator_3.setForeground(SystemColor.activeCaption);
		separator_3.setBounds(186, 692, 2, 123);
		contentPane.add(separator_3);
		
		JButton btnEffacerToutesLes = new JButton("Effacer toutes balles");
		btnEffacerToutesLes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sceneFinale.effacerBalles();
			}
		});
		btnEffacerToutesLes.setBounds(10, 741, 166, 23);
		contentPane.add(btnEffacerToutesLes);
		
		JButton btnEffacerTousLes = new JButton("Effacer tous les miroirs");
		btnEffacerTousLes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sceneFinale.effacerMiroir();
			}
		});
		btnEffacerTousLes.setBounds(198, 741, 176, 23);
		contentPane.add(btnEffacerTousLes);
		
		JButton btnEffacerTousLes_1 = new JButton("Effacer Tous les prismes");
		btnEffacerTousLes_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sceneFinale.effacerPrisme();
			}
		});
		btnEffacerTousLes_1.setBounds(397, 741, 176, 23);
		contentPane.add(btnEffacerTousLes_1);
		
		JButton btnEffacerTousLes_2 = new JButton("Effacer tous les blocs");
		btnEffacerTousLes_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sceneFinale.effacerBloc();
			}
		});
		btnEffacerTousLes_2.setBounds(595, 741, 176, 23);
		contentPane.add(btnEffacerTousLes_2);
		
		JButton btnEffacerTousLes_3 = new JButton("Effacer tous les trous noirs");
		btnEffacerTousLes_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sceneFinale.effacerTrouNoir();
			}
		});
		btnEffacerTousLes_3.setBounds(793, 741, 200, 23);
		contentPane.add(btnEffacerTousLes_3);
		
		JButton btnToutEffacer = new JButton("Tout effacer");
		btnToutEffacer.setBounds(612, 61, 159, 38);
		contentPane.add(btnToutEffacer);
		
		JSeparator separator_4 = new JSeparator();
		separator_4.setOrientation(SwingConstants.VERTICAL);
		separator_4.setForeground(SystemColor.activeCaption);
		separator_4.setBounds(385, 692, 2, 123);
		contentPane.add(separator_4);
		
		JSeparator separator_8 = new JSeparator();
		separator_8.setOrientation(SwingConstants.VERTICAL);
		separator_8.setForeground(SystemColor.activeCaption);
		separator_8.setBounds(583, 692, 2, 123);
		contentPane.add(separator_8);
		
		JSeparator separator_9 = new JSeparator();
		separator_9.setOrientation(SwingConstants.VERTICAL);
		separator_9.setForeground(SystemColor.activeCaption);
		separator_9.setBounds(781, 692, 2, 123);
		contentPane.add(separator_9);
		
		JSeparator separator_10 = new JSeparator();
		separator_10.setOrientation(SwingConstants.VERTICAL);
		separator_10.setForeground(SystemColor.activeCaption);
		separator_10.setBounds(999, 692, 2, 123);
		contentPane.add(separator_10);
		
		JToggleButton tglEffacementPrecis = new JToggleButton("Effacement pr\u00E9cis");
		tglEffacementPrecis.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if(tglEffacementPrecis.isSelected()) {
					sceneFinale.effacementPrecis(true);
				}else {
					sceneFinale.effacementPrecis(false);
				}
			}
		});
		tglEffacementPrecis.setBounds(1011, 741, 145, 23);
		contentPane.add(tglEffacementPrecis);
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

	// // Par Arezki
	/**
	 * permet d'étendre la fenêtre et d'activer les boutons de l'éditeur pour mettre
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
	 * permet de réduire la fenêtre à sa forme initiale et de désactiver les boutons
	 * de l'éditeur, mais les dessins restent
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
	 * Cette methode permet de changer l'angle des miroirs
	 */
	private void changerAngleMiroirs() {
		sceneFinale.setAngleMiroir(Integer.parseInt(spnAngleMiroir.getValue().toString()));
		
	}
	

}
