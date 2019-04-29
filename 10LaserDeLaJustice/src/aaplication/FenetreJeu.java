package aaplication;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import documentation.FenetreConcept;
import geometrie.Vecteur;
import interfaces.SceneListener;
import options.Options;
import personnage.Personnage;
import physique.Balle;
import son.Bruit;

import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.JCheckBox;
import physique.BarreEnergie;
import physique.Laser;

/**
 * 
 * Cette classe est la fenetre d'application du jeu
 * 
 * @author Arezki, Miora, Jeremy 
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
	private JRadioButton rbtnOrdiNiveau1;
	private JRadioButton rbtnOrdiNiveau2;
	private JRadioButton rbtnOrdiNiveau3;
	private final ButtonGroup btnGroupOrdi = new ButtonGroup();

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
	private JCheckBox checkBoxModeScientifique;
	private Dimension ecranDimension;
	private BarreEnergie barreEnergieCinetique;
	private BarreEnergie barreEnergiePotentielle;
	private BarreEnergie barreEnergieMecanique;
	private JLabel lblVitesse;
	private JLabel lblGravit;
	private JLabel lblAccel;
	private JLabel lblAngle;
	private JLabel  lblCouleur;
	private JLabel lblPer;

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
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Creation de la fenêtre
	 * @param isNouvelle : retourne vrai si il s'agit d'une nouvelle partie
	 * @param nomFichier : le nom du fichier de sauvegarde ou niveau
	 */
	// Par Arezki
	public FenetreJeu(boolean isNouvelle, String nomFichier) {
		setResizable(false);
		setTitle("Laser de la Justice.exe");
		setBackground(Color.GRAY);
		this.isNouvelle = isNouvelle;
		this.nomFichier = nomFichier;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1174, 974);

		ecranDimension = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(ecranDimension.width/2-getSize().width/2, ecranDimension.height/2-getSize().height/2);
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
				son.joue("beep");
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
				son.joue("beep");
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
				son.joue("beep");
				FenetreTuto fenetreTuto = new FenetreTuto();

				fenetreTuto.setVisible(true);
			}
		});
		btnTutoriel.setBounds(420, 61, 96, 38);
		contentPane.add(btnTutoriel);

		JButton btnOption = new JButton("Option");
		btnOption.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				son.joue("beep");
				choixOption();
			}
		});
		btnOption.setBounds(964, 61, 89, 38);
		contentPane.add(btnOption);
		//associerBoutonAvecImage(btnOption, "reglage.png");

		JButton btnEnregistrer = new JButton("Enregistrer partie\r\n");
		btnEnregistrer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				son.joue("beep");
				enregistrer();
			}
		});
		btnEnregistrer.setBounds(689, 61, 159, 38);
		contentPane.add(btnEnregistrer);
		//associerBoutonAvecImage(btnEnregistrer, "enregistrer.png");

		btnMiroirPlan = new JButton("miroir Plan");
		btnMiroirPlan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				son.joue("beep");
				sceneFinale.ajoutMiroirPlan();
				donneFocusALasceneFinale();
			}
		});
		btnMiroirPlan.setEnabled(false);

		btnMiroirPlan.setBounds(934, 868, 105, 23);
		contentPane.add(btnMiroirPlan);

		btnMiroirConvexe = new JButton("Miroir courbe");
		btnMiroirConvexe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				son.joue("beep");
				sceneFinale.ajoutMiroirCourbe();
				donneFocusALasceneFinale();
			}
		});
		btnMiroirConvexe.setEnabled(false);
		btnMiroirConvexe.setBounds(934, 800, 105, 23);


		contentPane.add(btnMiroirConvexe);


		btnTrouNoir = new JButton("Trou Noir");
		btnTrouNoir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				son.joue("beep");
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
				son.joue("beep");
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
				son.joue("beep");
				sceneFinale.ajoutBalleGrosse();
				donneFocusALasceneFinale();
			}
		});
		btnGrosseBalle.setEnabled(false);
		btnGrosseBalle.setBounds(729, 800, 96, 23);
		contentPane.add(btnGrosseBalle);

		btnMediumBalle = new JButton("Medium Balle");
		btnMediumBalle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				son.joue("beep");
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
				son.joue("beep");
				sceneFinale.ajoutBallePetite();
				donneFocusALasceneFinale();
			}
		});
		btnPetiteBalle.setEnabled(false);
		btnPetiteBalle.setBounds(729, 868, 96, 26);
		contentPane.add(btnPetiteBalle);
		
		
		rbtnOrdiNiveau1 = new JRadioButton("Aide niveau 1");
		rbtnOrdiNiveau1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//sceneFinale.
				donneFocusALasceneFinale();
			}
		});
		rbtnOrdiNiveau1.setBounds(1040, 800, 96, 23);
		rbtnOrdiNiveau1.setEnabled(false);
		contentPane.add(rbtnOrdiNiveau1);
		btnGroupOrdi.add(rbtnOrdiNiveau1);
		
		rbtnOrdiNiveau2 = new JRadioButton("Aide niveau 2");
		rbtnOrdiNiveau2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				donneFocusALasceneFinale();
			}
		});
		rbtnOrdiNiveau2.setBounds(1040,834, 96, 23);
		rbtnOrdiNiveau2.setEnabled(false);
		contentPane.add(rbtnOrdiNiveau2);
		btnGroupOrdi.add(rbtnOrdiNiveau2);
		
		rbtnOrdiNiveau3 = new JRadioButton("Aide niveau 3");
		rbtnOrdiNiveau3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				donneFocusALasceneFinale();
			}
		});
		rbtnOrdiNiveau3.setBounds(1040, 868, 96, 23);
		rbtnOrdiNiveau3.setEnabled(false);
		contentPane.add(rbtnOrdiNiveau3);
		btnGroupOrdi.add(rbtnOrdiNiveau3);
		

		btnPrisme = new JButton("prisme");
		btnPrisme.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				son.joue("beep");
				sceneFinale.ajoutPrisme();
				donneFocusALasceneFinale();
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


			//Jeremy Thai 
			/**
			 * Permet de mettre à jour les sorties scientfiques des objets de la scene dans d'autres panels 
			 * @param listeBalles liste de balles 
			 */
			@Override
			public void modeScientifiqueListener(ArrayList<Balle> listeBalles, double hauteurMonde) {

				double sommeEnergieCinetique = 0;
				double sommeEnergiePotentielle = 0;

				for(Balle balle : listeBalles) {
					sommeEnergieCinetique = sommeEnergieCinetique + balle.getEnergieCinetique();
					sommeEnergiePotentielle = sommeEnergiePotentielle + balle.getEnergiePotentielle(hauteurMonde);
				}
				barreEnergieCinetique.setEnergies(sommeEnergieCinetique, sommeEnergieCinetique+sommeEnergiePotentielle);
				barreEnergiePotentielle.setEnergies(sommeEnergiePotentielle, sommeEnergieCinetique+sommeEnergiePotentielle);
				barreEnergieMecanique.setEnergies(sommeEnergieCinetique+sommeEnergiePotentielle, sommeEnergieCinetique+sommeEnergiePotentielle );
				barreEnergieCinetique.repaint();
				barreEnergiePotentielle.repaint();
				barreEnergieMecanique.repaint();
			}
			
			/**
			 * Permet d'avoir en sortie la vitesse, l'accélération et la force gravitationnelle des balles. 
			 */
			//Arezki 
			public void evenementBalles(ArrayList<Balle> listeBalles) {
				double vitesseMoyX = 0;
				double vitesseMoyY = 0;
				DecimalFormat df = new DecimalFormat("#.##");
				if(listeBalles.size()!=0) {
				lblAccel.setText("[x: "+(listeBalles.get(0).getAccel().getX()+ " , y: "+ listeBalles.get(0).getAccel().getY()+" ] m²/s"));
				for(int i = 0; i < listeBalles.size();i++ ) {
					vitesseMoyX += listeBalles.get(i).getVitesse().getX();
					
					vitesseMoyY += listeBalles.get(i).getVitesse().getY();
					System.out.println();
					lblVitesse.setText("[x: "+df.format(vitesseMoyX)+" ,y : "+df.format(vitesseMoyY)+" ] m/s");
					lblGravit.setText("[x: "+listeBalles.get(i).getForceGravi().getX()+ " , y: "+ listeBalles.get(i).getForceGravi().getY()+" ] N");
					
				    }
				}
			}

			/**
			 * 
			 */
			@Override
			public void evenementLaser(ArrayList<Laser> lasers, double angle) {
				DecimalFormat df = new DecimalFormat("#.##");
				lblAngle.setText(""+df.format(angle));
				
				lblCouleur.setForeground(lasers.get(0).getCouleurLaser());
				
			}


			@Override
			public void evenementPersonnage(Personnage personnage) {
				DecimalFormat df = new DecimalFormat("#.##");

				lblPer.setText("[x: "+df.format(personnage.getPosition())+"]");
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
				son.joue("beep");
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
					if (barreTempsDuJeu.getValue() > 00 && secondes >= 0 && (sceneFinale.getCoeurs().getCombien()!=0)) {
						barreTempsDuJeu.setValue(barreTempsDuJeu.getValue() - 1);
						sceneFinale.setTempsDuJeu(barreTempsDuJeu.getValue());
					} else {
						son.joueSonLorsqueFini();
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
				son.joue("beep");
				sauvegarderNiveau();
				donneFocusALasceneFinale();
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
					donneFocusALasceneFinale();
				}
			
			donneFocusALasceneFinale();
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
				son.joue("beep");
				sceneFinale.ajoutMiroirCourbe();
				donneFocusALasceneFinale();
			}
		});
		buttonMiroirCourbe.setEnabled(false);
		buttonMiroirCourbe.setBounds(934, 834, 105, 23);
		contentPane.add(buttonMiroirCourbe);
		checkBoxModeScientifique = new JCheckBox("Mode scientifique");
		checkBoxModeScientifique.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				son.joue("beep");
				donneFocusALasceneFinale();
				if(checkBoxModeScientifique.isSelected()) {
					setSize(1300, getHeight());
					sceneFinale.setModeScientifique(true);
					donneFocusALasceneFinale();
				}else {
					setSize(1174, getHeight());
					sceneFinale.setModeScientifique(false);
					donneFocusALasceneFinale();
				}
			}
		});
		checkBoxModeScientifique.setBounds(568, 743, 125, 36);
		contentPane.add(checkBoxModeScientifique);

		barreEnergieCinetique = new BarreEnergie();
		barreEnergieCinetique.setBounds(1178, 142, 50, 151);
		contentPane.add(barreEnergieCinetique);

		barreEnergiePotentielle = new BarreEnergie();
		barreEnergiePotentielle.setBounds(1178, 337, 50, 151);
		contentPane.add(barreEnergiePotentielle);

		barreEnergieMecanique = new BarreEnergie();
		barreEnergieMecanique.setBounds(1178, 524, 50, 151);
		contentPane.add(barreEnergieMecanique);

		JLabel lblNewLabel = new JLabel("\u00C9nergie cin\u00E9tique");
		lblNewLabel.setBounds(1166, 122, 101, 16);
		contentPane.add(lblNewLabel);

		JLabel lblnergiePotentielle = new JLabel("\u00C9nergie potentielle");
		lblnergiePotentielle.setBounds(1166, 304, 118, 26);
		contentPane.add(lblnergiePotentielle);

		JLabel lblnergieMcanique = new JLabel("\u00C9nergie m\u00E9canique");
		lblnergieMcanique.setBounds(1166, 499, 118, 26);
		contentPane.add(lblnergieMcanique);

		JButton btnLireLesConcepts = new JButton("Lire les concepts scientifiques");
		btnLireLesConcepts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				affichierConcept();
			}
		});
		btnLireLesConcepts.setBounds(858, 11, 195, 39);
		contentPane.add(btnLireLesConcepts);
		
		JLabel lblVitesseMoyenneDes = new JLabel("Vitesse totale des balles :");
		lblVitesseMoyenneDes.setBounds(60, 784, 145, 14);
		contentPane.add(lblVitesseMoyenneDes);
		
		lblVitesse = new JLabel("vitesse");
		lblVitesse.setBounds(215, 783, 145, 14);
		contentPane.add(lblVitesse);
		
		JLabel lblForceRavitationnelle = new JLabel("Force gravitationnelle :");
		lblForceRavitationnelle.setBounds(63, 825, 142, 14);
		contentPane.add(lblForceRavitationnelle);
		
		lblGravit = new JLabel("Gravit\u00E9");
		lblGravit.setBounds(215, 825, 130, 14);
		contentPane.add(lblGravit);
		
		JLabel lblAcclerationGravitationnelle = new JLabel("Acc\u00E9leration Gravitationnelle: ");
		lblAcclerationGravitationnelle.setBounds(60, 868, 145, 14);
		contentPane.add(lblAcclerationGravitationnelle);
		
		lblAccel = new JLabel("Accel");
		lblAccel.setBounds(215, 868, 130, 14);
		contentPane.add(lblAccel);
		
	
		JLabel lblAngleLaser = new JLabel("L'angle du laser: ");
		lblAngleLaser.setBounds(360, 784, 145, 14);
		contentPane.add(lblAngleLaser);
		
		lblAngle = new JLabel("Angle");
		lblAngle.setBounds(470, 784, 130, 14);
		contentPane.add(lblAngle);
		
		
		JLabel lblCouleurLaser = new JLabel("Couleur du laser :");
		lblCouleurLaser.setBounds(360, 825, 142, 14);
		contentPane.add(lblCouleurLaser);
		
		lblCouleur= new JLabel ("Couleur");
		lblCouleur.setBackground(Color.GRAY);
		lblCouleur.setBounds(470, 825, 130, 14);
		contentPane.add(lblCouleur);
		
		

		JLabel lblPersonnage = new JLabel("Position du personnage :");
		lblPersonnage.setBounds(360, 868, 142, 14);
		contentPane.add(lblPersonnage);
		
		lblPer= new JLabel ("position");
		lblPer.setBounds(500, 868, 130, 14);
		contentPane.add(lblPer);
		
		
	}



		//Par Miora
		/**
		 * Cette methode permet d'ouvrir une fenetre pour consulter les concepts scientifiques
		 * derriere l'application
		 */
		private void affichierConcept() {
			FenetreConcept fenetreConcept = new FenetreConcept();
			fenetreConcept.setVisible(true);
			setVisible(false);

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
			rbtnOrdiNiveau1.setEnabled(true);
			rbtnOrdiNiveau2.setEnabled(true);
			rbtnOrdiNiveau3.setEnabled(true);
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
			rbtnOrdiNiveau1.setEnabled(false);
			rbtnOrdiNiveau2.setEnabled(false);
			rbtnOrdiNiveau3.setEnabled(false);
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

		//Par Miora
		/**
		 * Cette methode permet de sauvegarder une partie
		 */
		private void enregistrer() {
			sceneFinale.arreter();
			tempsJeu.stop();
			String nom = JOptionPane.showInputDialog("Entrez le nom de votre sauvagarde :");
			sceneFinale.ecritureFichierSauvegarde(nom, false);

		}

		//Par Miora
		/**
		 * Cette methode permet de sauvegarder le niveau
		 */
		private void sauvegarderNiveau() {
			String nomSauv = JOptionPane.showInputDialog("Entrer le nom de votre niveau :");
			sceneFinale.ecritureNiveau(nomSauv);

		}
	}
