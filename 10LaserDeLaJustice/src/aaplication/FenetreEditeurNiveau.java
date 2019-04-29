package aaplication;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import interfaces.MiroirListener;
import miroir.FenetreMiroir;
import son.Bruit;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;


public class FenetreEditeurNiveau extends JFrame {
	/**
	 * Cette classe sert à éditer et créer un niveau
	 * @author Arezki
	 * @author Miora
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
	private JButton btnMiroirCourbe;
	private JButton btnMiroirPlan;
	private Scene sceneFinale;
	private Bruit son = new Bruit();
	private FenetreMiroir fenetreMiroir;
	//private Options optionJeu;
	private boolean isNouveauOption = true;
	boolean triche = false;

	
	private static String nomFichier;

	private ActionListener listener;
	private Timer tempsJeu;
	double secondes = 60;
	private static boolean  isNouvelle = true;
	// Par Arezki 
	/**
	 * Lancement de l'application
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
		
		
		setResizable(false);
		setTitle("Mode \u00E9diteur");
		setBackground(Color.GRAY);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1413, 854);
		Dimension ecranDimension = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(0, ecranDimension.height/2-getSize().height/2); //le plus a gauche
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
				son.joue("beep");
				FenetreTuto fenetreTuto = new FenetreTuto();
				fenetreTuto.setVisible(true);
			}
		});
		btnTutoriel.setBounds(318, 38, 148, 38);
		contentPane.add(btnTutoriel);

		//associerBoutonAvecImage(btnEnregistrer, "enregistrer.png");

		btnMiroirPlan = new JButton("Miroir plan");
		btnMiroirPlan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				son.joue("beep");
				sceneFinale.ajoutMiroirPlan();
				donneFocusALasceneFinale();
				sceneFinale.activerDrag(true);
			}
		});
		btnMiroirPlan.setEnabled(true);
		btnMiroirPlan.setBounds(1198, 547, 148, 23);
		contentPane.add(btnMiroirPlan);

		btnMiroirCourbe = new JButton("Miroir courbe");
		btnMiroirCourbe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				son.joue("beep");
				sceneFinale.ajoutMiroirCourbe();
				sceneFinale.activerDrag(true);
				donneFocusALasceneFinale();
			}
		});
		btnMiroirCourbe.setEnabled(true);
		btnMiroirCourbe.setBounds(1198, 513, 148, 23);
		contentPane.add(btnMiroirCourbe);


		btnTrouNoir = new JButton("Trou Noir");
		btnTrouNoir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				son.joue("beep");
				sceneFinale.ajoutTrouNoir();
				donneFocusALasceneFinale();
				sceneFinale.activerDrag(true);
			}
		});
		btnTrouNoir.setEnabled(true);
		btnTrouNoir.setBounds(1198, 372, 148, 23);
		contentPane.add(btnTrouNoir);

		btnBlocDeau = new JButton("Bloc ");
		btnBlocDeau.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				son.joue("beep");
				sceneFinale.ajoutBlocEau();
				donneFocusALasceneFinale();
				sceneFinale.activerDrag(true);
				
			}
		});
		btnBlocDeau.setEnabled(true);
		btnBlocDeau.setBounds(1198, 337, 148, 23);
		contentPane.add(btnBlocDeau);

		btnGrosseBalle = new JButton("Grosse Balle");
		btnGrosseBalle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				son.joue("beep");
				sceneFinale.ajoutBalleGrosse();
				sceneFinale.activerDrag(true);
			}
		});
		btnGrosseBalle.setEnabled(true);
		btnGrosseBalle.setBounds(1198, 157, 148, 23);
		contentPane.add(btnGrosseBalle);

		btnMediumBalle = new JButton("Balle moyenne");
		btnMediumBalle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				son.joue("beep");
				sceneFinale.ajoutBalleMedium();
				donneFocusALasceneFinale();
				sceneFinale.activerDrag(true);
			}
		});
		btnMediumBalle.setEnabled(true);
		btnMediumBalle.setBounds(1198, 192, 148, 23);
		contentPane.add(btnMediumBalle);

		btnPetiteBalle = new JButton("Petite balle");
		btnPetiteBalle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				son.joue("beep");
				sceneFinale.ajoutBallePetite();
				donneFocusALasceneFinale();
				sceneFinale.activerDrag(true);
			}
		});
		btnPetiteBalle.setEnabled(true);
		btnPetiteBalle.setBounds(1198, 226, 148, 23);
		contentPane.add(btnPetiteBalle);

		btnPrisme = new JButton("Prisme");
		btnPrisme.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				son.joue("beep");
				sceneFinale.ajoutPrisme();
				sceneFinale.activerDrag(true);
			}
		});
		btnPrisme.setEnabled(true);
		btnPrisme.setBounds(1198, 407, 148, 23);
		contentPane.add(btnPrisme);

		JSeparator separator_5 = new JSeparator();
		separator_5.setForeground(SystemColor.activeCaption);
		separator_5.setBounds(1179, 304, 182, 2);
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
		lblditeur.setBounds(1215, 49, 105, 59);
		contentPane.add(lblditeur);

		sceneFinale = new Scene(isNouvelle, nomFichier);
		sceneFinale.setBounds(10, 110, 1146, 585);
		contentPane.add(sceneFinale);

		JButton btnDemarrage = new JButton("Retour au d\u00E9marrage");
		btnDemarrage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				son.joue("beep");
				App10LaserDeLaJustice demarrage = new App10LaserDeLaJustice();
				demarrage.setVisible(true);
				setVisible(false);

			}
		});
		btnDemarrage.setBounds(10, 39, 166, 36);
		contentPane.add(btnDemarrage);

		tempsJeu = new Timer(1000, listener);


		JButton btnSaveNiveau = new JButton("Enregistrer niveau\r\n");
		btnSaveNiveau.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				son.joue("beep");
				String nomSauv = JOptionPane.showInputDialog("Entrer le nom de votre niveau :");
				sceneFinale.ecritureNiveau(nomSauv);
			}
		});
		btnSaveNiveau.setBounds(626, 38, 153, 38);
		contentPane.add(btnSaveNiveau);

		JLabel lblTypeDeBalle = new JLabel("Type de balle\r\n");
		lblTypeDeBalle.setHorizontalTextPosition(SwingConstants.CENTER);
		lblTypeDeBalle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTypeDeBalle.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 10));
		lblTypeDeBalle.setBounds(1215, 119, 105, 38);
		contentPane.add(lblTypeDeBalle);

		JLabel lblTypeDeRfraction = new JLabel("Translation\r\n");
		lblTypeDeRfraction.setHorizontalTextPosition(SwingConstants.CENTER);
		lblTypeDeRfraction.setHorizontalAlignment(SwingConstants.CENTER);
		lblTypeDeRfraction.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 10));
		lblTypeDeRfraction.setBounds(1215, 304, 105, 38);
		contentPane.add(lblTypeDeRfraction);

		JSeparator separator = new JSeparator();
		separator.setForeground(SystemColor.activeCaption);
		separator.setBounds(1179, 472, 182, 4);
		contentPane.add(separator);

		JLabel lblTypeDeRflexion = new JLabel("Miroir");
		lblTypeDeRflexion.setHorizontalTextPosition(SwingConstants.CENTER);
		lblTypeDeRflexion.setHorizontalAlignment(SwingConstants.CENTER);
		lblTypeDeRflexion.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 10));
		lblTypeDeRflexion.setBounds(1215, 479, 105, 38);
		contentPane.add(lblTypeDeRflexion);

		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(SystemColor.activeCaption);
		separator_1.setBounds(1179, 622, 182, 2);
		contentPane.add(separator_1);

		JSeparator separator_2 = new JSeparator();
		separator_2.setForeground(SystemColor.activeCaption);
		separator_2.setBounds(1179, 110, 182, 2);
		contentPane.add(separator_2);

		JLabel lblChangementsDivers = new JLabel("Changements Divers");
		lblChangementsDivers.setHorizontalTextPosition(SwingConstants.CENTER);
		lblChangementsDivers.setHorizontalAlignment(SwingConstants.CENTER);
		lblChangementsDivers.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 10));
		lblChangementsDivers.setBounds(1215, 627, 105, 38);
		contentPane.add(lblChangementsDivers);

		JLabel lblRfractionP = new JLabel("Indice de r\u00E9fracrion prisme :");
		lblRfractionP.setBounds(1193, 656, 178, 38);
		contentPane.add(lblRfractionP);

		JSpinner spinner = new JSpinner();
		spinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sceneFinale.setIndiceRefractionPrisme((double) spinner.getValue());
				repaint();
			}
		});

		spinner.setBounds(1306, 692, 40, 23);

		contentPane.add(spinner);

		JLabel lblRefBloc = new JLabel("Indice de r\u00E9fraction du bloc :");
		lblRefBloc.setBounds(1189, 727, 182, 14);
		contentPane.add(lblRefBloc);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setOrientation(SwingConstants.VERTICAL);
		separator_3.setForeground(SystemColor.activeCaption);
		separator_3.setBounds(186, 692, 2, 123);
		contentPane.add(separator_3);

		JButton btnEffacerToutesLes = new JButton("Effacer toutes les balles");
		btnEffacerToutesLes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				son.joue("beep");
				sceneFinale.effacerBalles();
			}
		});
		btnEffacerToutesLes.setBounds(10, 741, 166, 23);
		contentPane.add(btnEffacerToutesLes);

		JButton btnEffacerTousLes = new JButton("Effacer tous les miroirs");
		btnEffacerTousLes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				son.joue("beep");
				sceneFinale.effacerMiroir();
			}
		});
		btnEffacerTousLes.setBounds(198, 741, 176, 23);
		contentPane.add(btnEffacerTousLes);

		JButton btnEffacerTousLes_1 = new JButton("Effacer Tous les prismes");
		btnEffacerTousLes_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				son.joue("beep");
				sceneFinale.effacerPrisme();
				sceneFinale.activerDrag(true);
			}
		});
		btnEffacerTousLes_1.setBounds(397, 741, 176, 23);
		contentPane.add(btnEffacerTousLes_1);

		JButton btnEffacerTousLes_2 = new JButton("Effacer tous les blocs");
		btnEffacerTousLes_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				son.joue("beep");
				sceneFinale.effacerBloc();
				sceneFinale.activerDrag(true);
			}
		});
		btnEffacerTousLes_2.setBounds(595, 741, 176, 23);
		contentPane.add(btnEffacerTousLes_2);

		JButton btnEffacerTousLes_3 = new JButton("Effacer tous les trous noirs");
		btnEffacerTousLes_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				son.joue("beep");
				sceneFinale.effacerTrouNoir();
				sceneFinale.activerDrag(true);
			}
		});
		btnEffacerTousLes_3.setBounds(793, 741, 200, 23);
		contentPane.add(btnEffacerTousLes_3);

		JButton btnToutEffacer = new JButton("Tout effacer");
		btnToutEffacer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				sceneFinale.activerDrag(true);
			}
		});
		btnToutEffacer.setBounds(939, 38, 148, 38);
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
					sceneFinale.activerDrag(true);
				}else {
					sceneFinale.effacementPrecis(false);
					sceneFinale.activerDrag(true);
				}
			}
		});
		tglEffacementPrecis.setBounds(1011, 741, 145, 23);
		contentPane.add(tglEffacementPrecis);


		JSeparator separator_11 = new JSeparator();
		separator_11.setOrientation(SwingConstants.VERTICAL);
		separator_11.setBounds(1362, 110, 9, 768);
		contentPane.add(separator_11);

		JButton btnAvance = new JButton("Modifications avanc\u00E9es");
		btnAvance.setHorizontalAlignment(SwingConstants.LEFT);
		btnAvance.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnAvance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				optionAvanceMiroir();
				sceneFinale.activerDrag(true);
			}	
		});
		btnAvance.setBounds(1198, 581, 148, 23);
		contentPane.add(btnAvance);


		
		JLabel lblAngleBloc = new JLabel("Angle bloc");
		lblAngleBloc.setBounds(1187, 771, 89, 14);
		contentPane.add(lblAngleBloc);
		
		JSpinner spnAngleBloc = new JSpinner();
		spnAngleBloc.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sceneFinale.setAngleBloc((double)spnAngleBloc.getValue());
				sceneFinale.activerDrag(true);
			}
		});
		spnAngleBloc.setBounds(1297, 795, 49, 20);
		contentPane.add(spnAngleBloc);
		
		JComboBox cmbBloc = new JComboBox();
		cmbBloc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sceneFinale.modifierIndiceBloc(cmbBloc.getSelectedIndex());
			}
		});
		cmbBloc.setFont(new Font("SansSerif", Font.PLAIN, 11));
		cmbBloc.setModel(new DefaultComboBoxModel(new String[] {"Eau(n=1.33)", "Verre(n=1.5)", "Diamant(n=2.42)", "Disulfure de carbone(n=1.63)"}));
		cmbBloc.setBounds(1179, 741, 182, 24);
		contentPane.add(cmbBloc);
		
		JButton btnTeleporteur = new JButton("T\u00E9l\u00E9porteur");
		btnTeleporteur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				son.joue("beep");
				sceneFinale.ajoutTeleporteur();
				sceneFinale.activerDrag(true);
				donneFocusALasceneFinale();
			}
		});
		btnTeleporteur.setBounds(1198, 442, 148, 23);
		contentPane.add(btnTeleporteur);
	}

	// Par Miora
	/**
	 * Methode qui donne le focus a la sceneFinale
	 */
	public void donneFocusALasceneFinale() {
		sceneFinale.requestFocusInWindow();
	}

	
	//Par Miora
	/**
	 * Cette methode permet d'acceder au mode de visualisation et de creation avancee des miroirs
	 */
	private void optionAvanceMiroir() {
		fenetreMiroir = new FenetreMiroir ();
		fenetreMiroir.setVisible(true);
		fenetreMiroir.addMiroirListener(new MiroirListener() {
			public void changementAngleMiroirListener(int angle) {
				sceneFinale.setAngleMiroir(angle);
			}

			public void changementLongueurMiroirListener(int longueur) {
				sceneFinale.setLongueurMiroir(longueur);
			}

			public void dessinerLeMiroirListener(String typeMiroir) {
				if(typeMiroir.equals("MiroirPlan")) {
					sceneFinale.ajoutMiroirPlan();
				}else {
					sceneFinale.ajoutMiroirCourbe();
				}
			}
	});
	}
	
	
	
	// // Par Arezki
	/**
	 * permet d'étendre la fenêtre et d'activer les boutons de l'éditeur pour mettre
	 * les objets dans la sceneFinale
	 * 
	 */
	public void activerEditeur() {
		btnBlocDeau.setEnabled(true);
		btnGrosseBalle.setEnabled(true);
		btnMediumBalle.setEnabled(true);
		
		btnMiroirCourbe.setEnabled(true);
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
	 */
	public void desactiverEditeur() {
		btnBlocDeau.setEnabled(false);
		btnGrosseBalle.setEnabled(false);
		btnMediumBalle.setEnabled(false);

		btnMiroirCourbe.setEnabled(false);
		btnMiroirPlan.setEnabled(false);
		btnPrisme.setEnabled(false);
		btnTrouNoir.setEnabled(false);
		btnPetiteBalle.setEnabled(false);
	}
}
